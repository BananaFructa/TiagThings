local event = require "event"
local term = require "term"
local os = require "os"
local component = require("component")
local keyboard = require("keyboard")
local computer = require("computer")
local gpu = component.gpu
local math = require("math")
local thread = require("thread")
local modem = component.modem

local width,height,xof,yof,relx,rely = term.getViewport()

local internalLog = {}
local maxLogLength = height - 5
local updatedLog = false
local knownServers = {}

function log(s)
    internalLog[#internalLog + 1] = s .. string.rep(" ",width-2-string.len(s))
    if #internalLog > maxLogLength then
        table.remove(internalLog,1)
    end
    event.push("logged")
end

-- Network functions

local commandPort = 20917
local recievePort = 20918

local researchSetCount = 5

modem.open(commandPort)
modem.open(recievePort)

function askToReport()
    modem.broadcast(commandPort,"report")
end

function handleRecievedMessages()
    while true do
        local _, _, from, port, _, message = event.pull(time,"modem_message")
        if port == recievePort then
            if message == "answer" then
                knownServers[#knownServers + 1] = from
                log("Contacted " .. from)
            elseif message == "last" then
                event.push("server_done",nil,nil,from,nil)
            elseif message ~= "no_token" and message ~= "not_registered" then
                event.push("server_performed",nil,nil,message,nil)
            end
        end
    end
end

function waitMessage(time,wantedPort)
    repeat
        local _, _, from, port, _, message = event.pull(time,"modem_message")
        if (port == wantedPort) then
            return message, from
        end
    until true
end

function sendMessage(to,port,message)
    modem.send(to,port,message)
end

function sendMessage(to,port,message)
    modem.send(to,port,message)
end

function acquireknownServers()
    log("Erasing current server list...")
    knownServers = {}
    local start = computer.uptime()
    local index = 1
    log("Sending pings...")
    modem.broadcast(commandPort,"ask")

end

function researchRequestAll(setAmount)
    for i =1,#knownServers,1 do
        sendMessage(knownServers[i],commandPort,"req" .. tostring(setAmount))
    end
end

function researchRequestServer(setAmount,address)
    sendMessage(address,commandPort,"req" .. tostring(setAmount))
end

function listenForTokens(timeout)

    while true do
        message, from = waitMessage(timeout,recievePort)
        if message == nil then
            print("Timeout")
            break
        end
        log("Got " .. message)
    end

end

--

local maxW, maxH = gpu.maxResolution()
local tier = 1
if maxW == 50 then
    tier = 1
elseif maxW == 80 then
    tier = 2
else
    tier = 3
end

local logoBasic = {

" _____ _                                  ",
"|_   _(_) __ _  __ _                      ",
"  | | | |/ _` |/ _` |                     ",
"  | | | | (_| | (_| |                     ",
" _|_| |_|\\__,_|\\__, |               _     ",
"|  _ \\ ___  ___|___/  __ _ _ __ ___| |__  ",
"| |_) / _ \\/ __|/ _ \\/ _` | '__/ __| '_ \\ ",
"|  _ <  __/\\__ \\  __/ (_| | | | (__| | | |",
"|_|_\\_\\___||___/\\___|\\__,_|_|  \\___|_| |_|",
"|_   _|__ _ __ _ __ ___ (_)_ __   __ _| | ",
"  | |/ _ \\ '__| '_ ` _ \\| | '_ \\ / _` | | ",
"  | |  __/ |  | | | | | | | | | | (_| | | ",
"  |_|\\___|_|  |_| |_| |_|_|_| |_|\\__,_|_| "

}

local logoLarge = {
" _____ _               ____                               _     ",
"|_   _(_) __ _  __ _  |  _ \\ ___  ___  ___  __ _ _ __ ___| |__  ",
"  | | | |/ _` |/ _` | | |_) / _ \\/ __|/ _ \\/ _` | '__/ __| '_ \\ ",
"  | | | | (_| | (_| | |  _ <  __/\\__ \\  __/ (_| | | | (__| | | |",
" _|_|_|_|\\__,_|\\__, | |_|_\\_\\___||___/\\___|\\__,_|_|  \\___|_| |_|",
"|_   _|__ _ __ |___/___ (_)_ __   __ _| |                       ",
"  | |/ _ \\ '__| '_ ` _ \\| | '_ \\ / _` | |                       ",
"  | |  __/ |  | | | | | | | | | | (_| | |                       ",
"  |_|\\___|_|  |_| |_| |_|_|_| |_|\\__,_|_|                       "
}

local menu = {
"> Advanced Electromachanics                                                 ",
"                                                                            ",
"                          +--> Dynamic Memory                               ",
"                         /                                                  ",
"                        /                                                   ",
"> VLSI digital logic --+-----> EUV Lithography ----------> 4nm Node Systems ",
"                        \\                                                   ",
"                         \\                                                  ",
"> Networking ----------+--+--> Digital RF Modulation ----> OFDM Trancievers ",
"                        \\                                                   ",
"                         \\                                                  ",
"                          +--> Distributed Computing                        ",
"                                                                            ",
"> Meteorology ---------------> Advanced Solar Panels                        ",
"                                                                            ",
"> Hydroponics                                                               "
}

local mapWidth = 3
local menuMapping = {

    {{x=3,y=1,l=25,id=0},       nil,                    nil},
    {nil,                       {x=32,y=3,l=14,id=5},   nil},
    {{x=3,y=6,l=18,id=1},       {x=32,y=6,l=15,id=6},   {x=60,y=6,l=16,id=10}},
    {{x=3,y=9,l=10,id=2},       {x=32,y=9,l=21,id=7},   {x=60,y=9,l=16,id=11}},
    {nil,                       {x=32,y=12,l=21,id=8},  nil},
    {{x=3,y=14,l=11,id=3},      {x=32,y=14,l=21,id=9},  nil},
    {{x=3,y=16,l=11,id=4},      nil,                    nil}

}
local menuX = 1
local menuY = 3

local logo;

if tier == 1 then
    logo = logoBasic
else
    logo = logoLarge
end

local logoW = string.len(logo[1])
local logoH = #logo
local tiagColor = 0x004a7f
local whiteColor = 0xFFFFFF

local borderLeftTop = "┌"
local borderRightTop = "┐"
local borderHorizontal = "─"
local borderVertical = "│"
local borderLeftBottom = "└"
local borderRightBottom = "┘"
local borderLRTop = "┬"
local borderLRBottom = "┴"

local currentResearch = -1
local researchState = "running"
local localTaskRunning = false

local screen = "research_explorer"

-- ==========================

function drawSplashScreen()
    -- Logo animation
    term.clear()

    for rowColor = 1,(logoH+1),1 do
        for i = 1,logoH,1 do
            if i < rowColor then
                --gpu.setForeground(tiagColor)
                term.setCursor(width/2 - logoW/2,height/2 - logoH/2 + i - 1)
                term.write(logo[i])
            else 
                --gpu.setForeground(whiteColor)
            end
        end
        os.sleep(0.01)
    end
    os.sleep(1)
end

function drawWindowFrame(x,y,w,h,title)

    local sLen = string.len(title)-2
    
    for j = y,(y+h),1 do
        term.setCursor(x,j)
        if j == y then
            term.write(borderLeftTop .. string.rep(borderHorizontal,w-2) .. borderRightTop)
            term.setCursor((x-1) + w/2-sLen/2,j)
            term.write(title)
        elseif j == y+h then
            term.write(borderLeftBottom .. string.rep(borderHorizontal,w-2) .. borderRightBottom)
        else
            term.write(borderVertical .. string.rep(" ",w-2) .. borderVertical)
        end
    end
    
end

function printInFrame(x,y,w,h,lines)

    for i = 1,#lines,1 do
        term.setCursor(x+1,y+1+i-1)
        term.write(lines[i] .. string.rep(" ",w-2-string.len(lines[i])))
    end
    

end

function printBottomFrame(x,y,w,h,lines)
    local total = 0
    for i = 1,#lines,1 do
        term.setCursor(x+3+total,y+h)
        term.write(lines[i])
        total = total + string.len(lines[i]) + 3
    end
end

local function wrapText(text, maxWidth)
    local lines = {}
    local currentLine = ""

    if maxWidth < 1 then
        error("maxWidth must be at least 1")
    end

    for word in text:gmatch("%S+") do
        -- Case 1: word fits onto the current line
        if currentLine == "" then
            if #word <= maxWidth then
                currentLine = word
            else
                -- word longer than maxWidth: hard-break it
                local w = word
                while #w > maxWidth do
                    table.insert(lines, w:sub(1, maxWidth))
                    w = w:sub(maxWidth + 1)
                end
                currentLine = w
            end
        else
            local candidate = currentLine .. " " .. word
            if #candidate <= maxWidth then
                currentLine = candidate
            else
                -- push current line, start new one with word
                table.insert(lines, currentLine)
                if #word <= maxWidth then
                    currentLine = word
                else
                    local w = word
                    while #w > maxWidth do
                        table.insert(lines, w:sub(1, maxWidth))
                        w = w:sub(maxWidth + 1)
                    end
                    currentLine = w
                end
            end
        end
    end

    if currentLine ~= "" then
        table.insert(lines, currentLine)
    end

    return lines
end

function combine(t1,t2)
    for i=1,#t2 do
        t1[#t1+1] = t2[i]
    end
    return t1
end

function researchInMenu(x,y)

    for i = 1,#menuMapping,1 do
        for j = 1,mapWidth,1 do
            local e = menuMapping[i][j]
            if e ~= nil then
                if e.y == y and x >= e.x and x < e.x + e.l then
                    return e.id
                end
            end
        end
    end    
    return -1

end

local anchorY = 1
local anchorX = 1
function printMenuInFrame(x,y,w,h)

    local pos = menuMapping[menuY][menuX]
    
    if pos.y - anchorY > h - 2 then
        anchorY = anchorY + ((pos.y - anchorY) - (h - 2))
    end
    
    if pos.y - anchorY < 1 then
        anchorY = anchorY + (pos.y - anchorY)
    end
    
    if pos.x - anchorX > math.floor(w/3) then
        anchorX = anchorX + (pos.x - anchorX - math.floor(w/3))
    end
    
    if (pos.x - anchorX < math.floor(w/4)) then
        anchorX = anchorX + ((pos.x - anchorX) - math.floor(w/4))
        anchorX = math.max(anchorX,1)
    end

    local curText = {}
    for i = anchorY,(anchorY + h-2),1 do
        if i <= #menu and i > 0 then
            curText[i-anchorY + 1] = string.sub(menu[i],anchorX,anchorX+w-2-1)
            curText[i-anchorY + 1] = curText[i-anchorY + 1] .. string.rep(" ",w-2 - string.len(curText[i-anchorY + 1]))
        else
            curText[i-anchorY + 1] = string.rep(" ",w-2)
        end
    end
    
    printInFrame(x,y,w,h,curText)
    
    
    gpu.setBackground(0xFFFFFF)
    gpu.setForeground(0x000000)
    for i = 1,#curText,1 do
        local tx = x + 1
        local l = ""
        for c = 1,string.len(curText[i]),1 do
            local id = researchInMenu(c + anchorX - 1,i + anchorY - 1)
            if (id ~= -1 and isIdResearched(id)) then
                term.setCursor(x+1+c-1,y+1+i-1)
                term.write(string.sub(curText[i],c,c))
            end
            --term.setCursor(tx,y+1+i-1)
            --term.write(string.sub(curText[i],c,c))
        end
    end
    gpu.setBackground(0x000000)
    gpu.setForeground(0xFFFFFF)
    
    term.setCursor(x + 1 + pos.x - anchorX - 1,y + 1 + pos.y - anchorY)
    term.write("[")
    term.setCursor(x + 1 + pos.x - anchorX + pos.l,y + 1 + pos.y - anchorY)
    term.write("]")

end

function moveToMain()
    term.clear()
    drawWindowFrame(1,1,width,3,"Research Status")
    if currentResearch ~= -1 then
        printBottomFrame(1,1,width,3,{"[Q] Cancel Research"})
    end
    drawWindowFrame(1,5,width,height-5,"Research Explorer")
    local researchName = "None"
    if currentResearch ~= -1 then
        researchName = research.nameOf(currentResearch)
    end
    local needed = getRequiredCost()
    local has = balance()
        if currentResearch ~= -1 then
        printInFrame(1,1,width,4,{"Current Research: " .. researchName, "Progress: " .. tostring(has) .. "/" .. tostring(needed) .. " RT"})
    else
        printInFrame(1,1,width,4,{"Current Research: " .. researchName, "Research Pool: " .. tostring(has) .. " RT"})
    end
    printMenuInFrame(1,5,width,height-5)
    screen = "research_explorer"
    printBottomFrame(1,5,width,height-5,{"[Enter] View Selected", "[C] Server Network"})
end

function redrawMainTop()
    drawWindowFrame(1,1,width,3,"Research Status")
    if currentResearch ~= -1 then
        printBottomFrame(1,1,width,3,{"[Q] Cancel Research"})
    end
    local researchName = "None"
    if currentResearch ~= -1 then
        researchName = research.nameOf(currentResearch)
    end
    local needed = getRequiredCost()
    local has = balance()
    if currentResearch ~= -1 then
        printInFrame(1,1,width,4,{"Current Research: " .. researchName, "Progress: " .. tostring(has) .. "/" .. tostring(needed) .. " RT"})
    else
        printInFrame(1,1,width,4,{"Current Research: " .. researchName, "Research Pool: " .. tostring(has) .. " RT"})
    end
end

function moveToResearchForce(id)
    term.clear()
    drawWindowFrame(1,1,width,height-1,research.nameOf(id))
    
    local completed = isIdResearched(id)
    local desc = wrapText(research.descriptionOf(id),width-2)
    local text = combine({"Cost: " .. tostring(research.costOf(id)) .. " Research Tokens", "Description:"}, desc)
    local prevReqMet = meetsRequirements(id)
    if not prevReqMet then
        text = combine({"Availability: Not all previous technologies","have been researched!",""},text)
    elseif completed then
        text = combine({"Availability: Researched!",""},text)
    else
        text = combine({"Availability: Can be researched!",""},text)
    end
    printInFrame(1,1,width,height-2,text)
    screen = "research_info"
    if not completed then
        printBottomFrame(1,1,width,height-1,{"[B] Back","[R] Research"})
    else
        printBottomFrame(1,1,width,height-1,{"[B] Back","[G] Get Research Report"})
    end
end

function moveToResearch()
    local pos = menuMapping[menuY][menuX]
    moveToResearchForce(pos.id)
end



function printLog()
    printInFrame(1,4,width,height-4,internalLog)
    updated = true
end

function moveToSevers()
    screen = "server_menu"
    term.clear()
    drawWindowFrame(1,1,width,2,"State")
    drawWindowFrame(1,4,width,height-4,"Logs")
    printInFrame(1,1,width,2,{"Reachable Servers: " .. tostring(#knownServers)})
    printBottomFrame(1,1,width,2,{"[I] Reindex Servers"})
    printBottomFrame(1,4,width,height-4,{"[B] Back"})
    printLog()
end

function moveToError(message)
    screen = "error_screen"
    x = math.floor(width/2) - math.floor(string.len(message)/2) - 1
    y = math.floor(height/2) - 1
    drawWindowFrame(x,y,string.len(message) + 2,2,"Error")
    printInFrame(x,y,string.len(message),2,{message})
    printBottomFrame(x,y,string.len(message),2,{"[Enter] Ok"})
end

function moveToConfirm(message)
    screen = "confirm_research"
    x = math.floor(width/2) - math.floor(string.len(message)/2) - 1
    y = math.floor(height/2) - 1
    drawWindowFrame(x,y,string.len(message) + 2,2,"Querry")
    printInFrame(x,y,string.len(message),2,{message})
    printBottomFrame(x,y,string.len(message),2,{"[B] Back","[Enter] Confirm"})
end

local researchDone = {}
local researchIdsDone = {}
local researchPath = "/home/research.data"
local tokens = {} -- its an array but will only have on element, this is to make a bit more future proof in case other features appear
local tokensPath = "/home/tokens.data"

function tryCreateFile(path)

    local f = io.open(path)
    if f ~= nil then
        io.close(f)
    else
        io.open(path,"w"):close()
    end

end

function readResearch()
    researchDone = {}
    researchIdsDone = {}
    local dataFile = io.open(researchPath,"r")
    local index = 1
    for line in dataFile:lines() do
        researchDone[index] = line
        researchIdsDone[#researchIdsDone + 1] = research.idOf(line)
        log(tostring(research.idOf(line)))
        index = index + 1
    end
    io.close(dataFile)
end

function readTokens()
    tokens = {}
    local dataFile = io.open(tokensPath,"r")
    local index = 1
    for line in dataFile:lines() do
        tokens[index] = line
        index = index + 1
    end
    io.close(dataFile)
end

function save(path,lines)
    --io.open(path,"w"):close()
    local file = io.open(path,"w")
    for i = 1,#lines,1 do
        file:write(lines[i])
        file:write("\n")
    end
    io.close(file)
end

function addToken(token)

    tokens[#tokens+1] = token
    save(tokensPath,tokens)

end

function balance()

    local total = 0
    for i = 1,#tokens,1 do
        total = total + research.check(tokens[i])
    end
    return total

end

function meetsRequirements(id)
    return research.meetsRequirements(id,#researchDone,table.unpack(researchDone))
end

function processToken(token)
    if #tokens == 0 then
        tokens[1] = token
    else
        research.merge(tokens[1],token)
    end
    --log(tostring(#tokens))
    save(tokensPath,tokens)
end

function processResult(result)
    researchDone[#researchDone + 1] = result
    researchIdsDone[#researchIdsDone + 1] = research.idOf(result)
    save(researchPath,researchDone)
end

function isIdResearched(id)
    for i = 1,#researchIdsDone,1 do
        if id == researchIdsDone[i] then
            return true
        end
    end
    return false
end

function scheduleTasks()
    scheduleLocal()
    scheduleServers()
end

function scheduleLocal()
    thread.create(function()
        for i = 1, researchSetCount, 1 do
            local token = "no_token"
            research.begin()
            while token == "no_token" do
                token = research.finish()
                os.sleep(0)
            end
            --log("Got " .. token)
            if token ~= "no_token" and token ~= "not_registered" then
                event.push("local_performed",nil,nil,token,nil)
            end
        end
        event.push("local_done")
    end)
end

function scheduleServers()
    researchRequestAll(researchSetCount)
end

function tryPurchase()
    if currentResearch == -1 or researchState == "paused" then
        return true
    end
    if #tokens == 0 then
        researchState = "running"
        return false
    else 
        local cost = research.costOf(currentResearch)
        local ok = balance() >= cost
        if ok then
            local res = research.acquire(currentResearch,tokens[1],#researchDone,table.unpack(researchDone))
            processResult(res)
            currentResearch = -1
            moveToResearchForce(research.idOf(res))
        end
        return ok
    end
end

function getRequiredCost()
    if currentResearch == -1 then
        return 0
    else
        return research.costOf(currentResearch)
    end
end

thread.create(function()
    handleRecievedMessages()
end)

tryCreateFile(researchPath)
tryCreateFile(tokensPath)
readResearch()
readTokens()

--drawSplashScreen()
moveToMain()

while true do
    local signal, address, char, code, player = event.pullMultiple("key_down","modem_message","logged","local_performed","local_done","server_performed","server_done")
    
    
    if signal == "key_down" then
        if (screen == "research_explorer") then
            -- there is a better way to do this but I am lazy and copy paste is easy
            if code == keyboard.keys.down then
                local drop = 1
                while menuY + drop <= #menuMapping and menuMapping[menuY + drop][menuX] == nil do
                    drop = drop + 1
                end
                if menuY + drop <= #menuMapping and menuMapping[menuY + drop][menuX] ~= nil then
                    menuY = menuY + drop
                    printMenuInFrame(1,5,width,height-5)
                end
            elseif code == keyboard.keys.up then
                 local drop = 1
                 while menuY - drop >= 1 and menuMapping[menuY - drop][menuX] == nil do
                    drop = drop + 1
                 end
                 if menuY - drop >= 1 and menuMapping[menuY - drop][menuX] ~= nil then
                     menuY = menuY - drop
                     printMenuInFrame(1,5,width,height-5)
                 end
            elseif code == keyboard.keys.right then
                local drop = 1
                while menuX + drop <= #menuMapping[menuY] and menuMapping[menuY][menuX+ drop] == nil do
                    drop = drop + 1
                end
                if menuX + drop <= #menuMapping and menuMapping[menuY][menuX+drop] ~= nil then
                    menuX = menuX + drop
                    printMenuInFrame(1,5,width,height-5)
                end
            elseif code == keyboard.keys.left then
                 local drop = 1
                 while menuX - drop >= 1 and menuMapping[menuY][menuX-drop] == nil do
                    drop = drop + 1
                 end
                 if menuX - drop >= 1 and menuMapping[menuY][menuX-drop] ~= nil then
                     menuX = menuX - drop
                     printMenuInFrame(1,5,width,height-5)
                 end
            elseif code == keyboard.keys.enter then
                 moveToResearch()
            elseif code == keyboard.keys.c then
                moveToSevers()
            end
            if code == keyboard.keys.q and currentResearch ~= -1 then
                currentResearch = -1
                redrawMainTop()
            end
        elseif screen == "research_info" then
            if code == keyboard.keys.b then
                moveToMain()
            end
            local pos = menuMapping[menuY][menuX]
            local completed = isIdResearched(pos.id)
            if not completed then
                if code == keyboard.keys.r then
                    local pos = menuMapping[menuY][menuX]
                    if not meetsRequirements(pos.id) then
                        moveToError("Not all previous researches are complete!")
                    else
                        moveToConfirm("Please confirm the research selection.")
                    end
                end
            else
                if code == keyboard.keys.g then
                    research.give(pos.id,#researchDone,table.unpack(researchDone))
                end
            end
        elseif screen == "server_menu" then
            if code == keyboard.keys.b then
                moveToMain()
            elseif code == keyboard.keys.i then
                acquireknownServers()
            end
        elseif screen == "error_screen" then
            if code == keyboard.keys.enter then
                moveToResearch()
            end
        elseif screen == "confirm_research" then
            if code == keyboard.keys.b then
                moveToResearch()
            end
            if code == keyboard.keys.enter then
                local pos = menuMapping[menuY][menuX]
                currentResearch = pos.id
                moveToMain()
                if not tryPurchase() then
                log("Started research")
                    scheduleTasks()
                end
            end
        end
     end
     if signal == "logged" and screen == "server_menu" then
        printLog()
        printInFrame(1,1,width,2,{"Reachable Servers: " .. tostring(#knownServers)})
     end
     if signal == "local_performed" or signal == "server_performed" then
        processToken(code)
        if screen == "research_explorer" then
            redrawMainTop()
        end
     end
     if signal == "local_done" then
        if not tryPurchase() then
            scheduleTasks()
        elseif screen == "research_explorer" then
            --redrawMainTop()
        end
     end
     if signal == "server_done" then
         if not tryPurchase() then
            researchRequestServer(researchSetCount,code)
        elseif screen == "research_explorer" then
            --redrawMainTop()
        end
     end
     

end
