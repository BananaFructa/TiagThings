local component = require("component")
local event = require("event")
local computer = require("computer")
local fs = component.filesystem
local modem = component.modem
local os = require("os")
local shell = require("shell")

local recievePort = 20917
local commandPort = 20918

modem.open(commandPort)
modem.open(recievePort)

local servers = {}

function askToReport()
    modem.broadcast(commandPort,"report")
end

function waitMessage(wantedPort)
    repeat
        local _, _, from, port, _, message = event.pull("modem_message")
        if (port == wantedPort) then
            return message, from
        end
    until true
end

function sendMessage(to, port, message)
    modem.send(to, port, message)
end

if fs.makeDirectory("not_readonly") then
    fs.remove("not_readonly")
    while true do
        message, from = waitMessage(recievePort)
        if message == "ask" then
            sendMessage(from,commandPort,"answer")
        elseif string.sub(message,1,3) == "req" then
            amountStr = string.sub(message,4,string.len(message))
            amount = tonumber(amountStr)
            for i = 1, amount, 1 do

                local token = "no_token"
                research.begin()
                while token == "no_token" do
                    token = research.finish()
                    os.sleep(0)
                end

                sendMessage(from, commandPort, token)
            end
            sendMessage(from,commandPort,"last")
        end

    end
end