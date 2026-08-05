package BananaFructa.TTIEMultiblocks.Gui;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityPLC;
import BananaFructa.TTIEMultiblocks.TileEntities.plc.*;
import BananaFructa.TiagThings.Netowrk.MessageGuiEvent;
import BananaFructa.TiagThings.Netowrk.TTPacketHandler;
import BananaFructa.TiagThings.JEIContainerTogglable;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.*;

public class PLCGui extends GuiIEContainerBase implements JEIContainerTogglable {

    public static HashMap<BlockPos, NBTTagCompound> clientStateCache = new HashMap<>();

    TileEntityPLC tile;
    PLCModule selected = null;
    int selectedOutput = -1;

    PLCModule highlighted = null;
    boolean wasDragger = false;

    PLCModule selectedLineOf = null;
    int inputLineSelected = -1;


    public List<PLCModule> moduleList = new ArrayList<>();
    public Map<Integer,PLCModule> idToMod = new HashMap<>();
    public double vX = 0;
    public double vY = 0;

    public double selX = 0;
    public double selY = 0;

    public boolean draggingScreen = false;
    public int screenShiftX = 0;
    public int screenShiftY = 0;

    private int idCounter = 0;

    public int portWidth = 9;
    public int portHeight = 5;

    public int scrollButtons = 0;

    private List<GuiButton> elementButtons = new ArrayList<>();
    private List<GuiTextField> textBoxes = new ArrayList<>();
    private List<GuiButton> channelSelectionButtons = new ArrayList<>();

    private int possibleFields = 3;

    private String complexityBudget = I18n.translateToLocalFormatted("tiag.complexity_budget");

    public PLCGui(InventoryPlayer inventoryPlayer, TileEntityPLC tile) {
        super(new ContainerPLC(inventoryPlayer,tile));
        this.tile = tile;
        synchronized (tile.moduleList) {
            moduleList = new ArrayList<>(tile.moduleList);
            idToMod = new HashMap<>(tile.idToMod);
            idCounter = tile.counter;
        }
    }

    public void saveClientState() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("screen_x",screenShiftX);
        tag.setInteger("screen_y",screenShiftY);
        tag.setInteger("scroll_button",scrollButtons);
        clientStateCache.put(tile.getPos(),tag);
    }

    public void readClientState() {
        if (clientStateCache.containsKey(tile.getPos())) {
            NBTTagCompound tagCompound = clientStateCache.get(tile.getPos());
            screenShiftX = tagCompound.getInteger("screen_x");
            screenShiftY = tagCompound.getInteger("screen_y");saveClientState();
            scrollButtons = tagCompound.getInteger("scroll_button");
        }
    }

    public int buttonWidth = 0;

    public int[] colors = {
            0x000000,
            0x100000,
            0x200000,
            0x300000,
            0x400000,
            0x500000,
            0x600000,
            0x700000,
            0x800000,
            0x900000,
            0xa00000,
            0xb00000,
            0xc00000,
            0xd00000,
            0xe00000,
            0xff0000,
    };

    @Override
    public void initGui() {
        super.initGui();
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        buttonWidth = Math.min((int)(0.2 * sr.getScaledWidth()),130);
        for (int i = 0;i < Modules.values().length;i++) {
            GuiButton b = new GuiButton(i,sr.getScaledWidth()-buttonWidth-20,getElementButtonYPos(sr,i),buttonWidth,15,Modules.values()[i].name);
            elementButtons.add(b);
            this.addButton(b);
        }
        for (int i = 0;i < possibleFields;i++) {
            GuiTextField field = new GuiTextField(i,mc.fontRenderer,sr.getScaledWidth()-buttonWidth-20+2,(i+1)*40+19,buttonWidth-4,20);
            textBoxes.add(field);
            field.setVisible(false);
        }
        for (int i = 0;i < 16;i++) {
            GuiButton b = new GuiButton(-1-i,sr.getScaledWidth()-buttonWidth-20,(i+1)*15+19,buttonWidth,15, ChannelColors.values()[i].format.toString() + ChannelColors.values()[i].name);
            b.visible = false;
            channelSelectionButtons.add(b);
            this.addButton(b);
        }
        readClientState();
    }

    public int getElementButtonYPos(ScaledResolution sr,int i) {
        return i*15+19;
    }

    public void displayChannels() {
        for (GuiButton button : elementButtons) button.visible = false;
        for (GuiButton button : channelSelectionButtons) {
            button.visible = true;
            button.enabled = true;
            int id = -button.id-1;
            if (highlighted instanceof InputPLCModule && id == ((InputPLCModule) highlighted).channel) button.enabled = false;
            else if (highlighted instanceof OutputPLCModule && id == ((OutputPLCModule) highlighted).channel) button.enabled = false;
        }
    }

    public void hideChannels() {
        for (GuiButton button : elementButtons) button.visible = true;
        for (GuiButton button : channelSelectionButtons) button.visible = false;
    }

    public void displayFields() {
        if (highlighted.fieldCount() == 0) return;
        for (GuiButton button : elementButtons) button.visible = false;
        for (int i = 0;i < highlighted.fieldCount();i++) {
            textBoxes.get(i).setVisible(true);
            textBoxes.get(i).setText(highlighted.getFieldValue(i));
        }
    }

    public void hideFields() {
        for (GuiButton button : elementButtons) button.visible = true;
        for (int i = 0;i < textBoxes.size();i++) {
            textBoxes.get(i).setVisible(false);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (selectedLineOf != null && keyCode == Keyboard.KEY_DELETE) {
            selectedLineOf.inputsFrom[inputLineSelected] = -1;
            inputLineSelected = -1;
            selectedLineOf = null;
            updateSizes();
            updateTile();
        }
        if (highlighted != null && keyCode == Keyboard.KEY_DELETE) {
            moduleList.remove(highlighted);
            idToMod.remove(highlighted.id);
            updateSizes();
            updateTile();
        }
        if (highlighted != null) {

            for (int i = 0;i < textBoxes.size();i++) {
                if (textBoxes.get(i).getVisible()) {
                    textBoxes.get(i).textboxKeyTyped(typedChar,keyCode);
                }
            }

            for (int i = 0;i < highlighted.fieldCount();i++) {
                highlighted.setFieldValue(i,textBoxes.get(i).getText());
            }

            updateSizes();
            updateTile();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateModuleStates();
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        if (scrollButtons > elementButtons.size() * 15 - (sr.getScaledHeight() - 20) + 3) {
            scrollButtons = elementButtons.size() * 15 - (sr.getScaledHeight() - 20) + 3;
            saveClientState();
        }
        if (scrollButtons < 0) {
            scrollButtons = 0;
            saveClientState();
        }

        for (int i = 0; i < elementButtons.size(); i++) {
            elementButtons.get(i).y = getElementButtonYPos(sr, i) - scrollButtons;
        }

        drawRect(0,0,sr.getScaledWidth(),sr.getScaledHeight(),0xffffffff);

        GlStateManager.pushMatrix();

        if (draggingScreen) {
            screenShiftX += (int) (mouseX - selX);
            screenShiftY += (int) (mouseY - selY);
            selX = mouseX;
            selY = mouseY;
            saveClientState();
        }
        GlStateManager.translate(screenShiftX, screenShiftY, 0);

        if (selected != null && selectedOutput == -1) {
            if (mouseX - selX != 0 || mouseY - selY != 0) wasDragger = true;
            selected.translate(mouseX - selX, mouseY - selY);
            selX = mouseX;
            selY = mouseY;
        }

        for (PLCModule module : moduleList) {
            drawModule(module);
        }
        for (int i = 0; i < tile.hasConnector.length; i++) {
            buttonList.get(i).enabled = tile.hasConnector[i];
            buttonList.get(i + 4).enabled = tile.hasConnector[i];
        }

        for (PLCModule module : moduleList) {
            for (int i = 0; i < module.outputs.length; i++) {
                Tuple<Integer, Integer> pos = outputRootPos(module, i);
                GlStateManager.pushMatrix();
                GlStateManager.scale(0.4, 0.4, 0.4);
                String v = Integer.toString(module.outputs[i]);
                mc.fontRenderer.drawString(v, (int) ((pos.getFirst() - portWidth / 2.0) / 0.4) - mc.fontRenderer.getStringWidth(v) / 2, (int) ((pos.getSecond() - 5) / 0.4), colors[module.outputs[i]]);
                GlStateManager.popMatrix();
            }
            for (int i = 0; i < module.inputsFrom.length; i++) {
                int value = 0;
                if (module.inputsFrom[i] != -1) {
                    if (idToMod.containsKey(module.inputsFrom[i])) {
                        PLCModule module2 = idToMod.get(module.inputsFrom[i]);
                        int output = module.inputsFromPosition[i];
                        if (output >= 0 && output < module2.outputs.length && module2.outputs[output] >= 0 && module2.outputs[output] < 16) {
                            value = module2.outputs[output];
                        }
                    }

                }
                Tuple<Integer, Integer> pos = inputRootPos(module, i);
                GlStateManager.pushMatrix();
                GlStateManager.scale(0.4, 0.4, 0.4);
                String v = Integer.toString(value);
                mc.fontRenderer.drawString(v, (int) ((pos.getFirst() + portWidth / 2.0) / 0.4) - mc.fontRenderer.getStringWidth(v) / 2, (int) ((pos.getSecond() - 5) / 0.4), colors[value]);
                GlStateManager.popMatrix();

            }
        }


        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();

        if (selected != null && selectedOutput != -1) {
            drawModuleToPos(selected, selectedOutput, mouseX-screenShiftX, mouseY-screenShiftY);
        }

        for (PLCModule module : moduleList) {
            for (int i = 0; i < module.inputsFrom.length; i++) {
                if (module.inputsFrom[i] != -1) {
                    int fromId = module.inputsFrom[i];
                    int output = module.inputsFromPosition[i];
                    if (idToMod.containsKey(fromId)) {
                        PLCModule from = idToMod.get(fromId);
                        drawModuleToModule(from, output, module, i, i == inputLineSelected && selectedLineOf != null && module.id == selectedLineOf.id);
                    }
                }
            }
        }

        GlStateManager.enableDepth();
        GlStateManager.popMatrix();

        GlStateManager.popMatrix();

        GlStateManager.popMatrix();

        drawRect(sr.getScaledWidth()-buttonWidth-30,0,sr.getScaledWidth(),sr.getScaledHeight(),0xffffffff);

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (highlighted != null) {
            for (int i = 0; i < highlighted.fieldCount(); i++) {
                String name = highlighted.getFieldName(i);
                mc.fontRenderer.drawString(name, sr.getScaledWidth() - buttonWidth - 20+2, (i + 1) * 40 + 20 - 14, buttonWidth);
            }
            for (int i = 0; i < textBoxes.size(); i++) {
                if (textBoxes.get(i).getVisible()) textBoxes.get(i).drawTextBox();
            }
            if (highlighted instanceof InputPLCModule || highlighted instanceof OutputPLCModule) {
                mc.fontRenderer.drawString("Channel", sr.getScaledWidth() - buttonWidth - 20+2,  20 + 5, buttonWidth);
            }
        }

        GlStateManager.pushMatrix();

        int wDesc = 200;

        GL11.glColor4f(1, 1, 1, 1);
        GlStateManager.color(1,1,1);
        for (GuiButton b : elementButtons) {
            if (b.visible && b.isMouseOver()){

                List<String> desc = Modules.values()[b.id].getDescription(mc,wDesc-40);
                int height = desc.size() * 11 + 20;

                drawRect(sr.getScaledWidth()-buttonWidth-20-20-wDesc,19,sr.getScaledWidth()-buttonWidth-20-20,20+height,0xdd000000);
                GL11.glColor4f(1, 1, 1, 1);
                GlStateManager.color(1,1,1);

                for (int i = 0;i < desc.size();i++) {
                    GL11.glColor4f(1, 1, 1, 1);
                    GlStateManager.color(1,1,1);
                    mc.fontRenderer.drawString(desc.get(i),sr.getScaledWidth()-buttonWidth-20-20-wDesc+10,30 + i * 11,0xffffff);
                }

                break;
            }
        }

        GL11.glColor4f(1, 1, 1, 1);
        GlStateManager.color(1,1,1);
        drawFrame(sr.getScaledWidth() - buttonWidth - 40, 0, buttonWidth + 40, sr.getScaledHeight());
        GL11.glColor4f(1, 1, 1, 1);
        GlStateManager.color(1,1,1);
        drawFrame(0, 0, sr.getScaledWidth() - buttonWidth - 40, sr.getScaledHeight());
        GlStateManager.popMatrix();
        GL11.glColor4f(1, 1, 1, 1);
        GlStateManager.color(1,1,1);
        int complexity = tile.designComplexity;
        int avComplexity = tile.availableComplexity;
        mc.fontRenderer.drawStringWithShadow((complexity <= avComplexity ? "\u00a7a" : "\u00a7c") + complexityBudget + ": " + complexity + "/" + avComplexity,22,19-13,0xffffff);
    }

    public void updateModuleStates() {
        synchronized (tile.moduleList) {
            for (PLCModule module : tile.moduleList) {
                int id = module.id;
                if (idToMod.containsKey(id)) {
                    idToMod.get(id).readState(module.getState());
                    //idToMod.get(id).outputs = module.outputs;
                }
            }
        }
    }

    @Override
    public void drawDefaultBackground() {

    }

    public void drawFrame(int x, int y, int w, int h) {
        GlStateManager.pushMatrix();
        GlStateManager.color(1,1,1);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        ClientUtils.bindTexture("tiagthings:textures/gui/plc.png");
        int vertical = 256;
        int currentH = 0;
        for (int i = 0;i < h/vertical;i++) {
            drawTexturedModalRect(x,y+i*vertical,0,0,20,vertical);
            drawTexturedModalRect(x+w-20,y+i*vertical,21,0,20,vertical);
            currentH += vertical;
        }
        drawTexturedModalRect(x, y + currentH, 0, 0, 20, h-currentH);
        drawTexturedModalRect(x+w-20,y+currentH,21,0,20,h-currentH);
        int horizontal = 214;
        int currentW = 0;
        for (int i = 0;i < w/horizontal;i++) {
            drawTexturedModalRect(x+i*horizontal,y,42,0,horizontal,20);
            drawTexturedModalRect(x+i*horizontal,y+h-20,42,20,horizontal,20);
            currentW += horizontal;
        }
        drawTexturedModalRect(x+currentW,y,42,0,w-currentW,20);
        drawTexturedModalRect(x+currentW,y+h-20,42,20,w-currentW,20);
        drawTexturedModalRect(x,y,42,41,20,20);
        drawTexturedModalRect(x,y+h-20,42,63,20,20);
        drawTexturedModalRect(x+w-20,y,42,85,20,20);
        drawTexturedModalRect(x+w-20,y+h-20,42,107,20,20);
        GlStateManager.popMatrix();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        for (GuiTextField field : textBoxes) {
            if (field.getVisible()) field.updateCursorCounter();
        }
    }

    public void drawModule(PLCModule module) {
        String[] lines = module.getDisplay();
        int height = module.height;
        int width = module.width;
        drawRect((int)module.x,(int)module.y,(int)(module.x + width),(int)(module.y + height),0xffcfd3fc);

        int color = 0xff000000;
        if (module == highlighted) {
            color = 0xff00f7ff;
        }

        drawRect((int)module.x,(int)module.y,(int)(module.x + width),(int)(module.y + 1),color);
        drawRect((int)module.x,(int)module.y,(int)(module.x + 1),(int)(module.y + height),color);
        drawRect((int)module.x+width-1,(int)module.y,(int)(module.x + width),(int)(module.y+height),color);
        drawRect((int)module.x,(int)module.y+height-1,(int)(module.x + width),(int)(module.y+height),color);

        for (int i = 0;i < module.inputCount();i++) {
            int y = (int)(module.y + (double)height/(module.inputCount() + 1) * (i+1) - 3);
            drawRect((int)module.x-9,y,(int)(module.x),y+5,module.colorForInput(i));
        }
        for (int i = 0;i < module.outputCount();i++) {
            int y = (int)(module.y + (double)height/(module.outputCount() + 1) * (i+1) - 3);
            drawRect((int)module.x+width,y,(int)(module.x +width+ 9),y+5,module.colorForOutput(i));
        }
        for (int i = 0;i < lines.length;i++) {
            int y = (height - 10)/(lines.length + 1) + 3;
            int x = (int)(module.x + 4);
            if (i == 0) x = (int)(module.x + width/2.0 - mc.fontRenderer.getStringWidth(lines[0])/2.0);
            if (i != 0) {
                GlStateManager.pushMatrix();

                GlStateManager.scale(0.8,0.8,0.8);

                mc.fontRenderer.drawString(lines[i], (int)(x/0.8), (int) ((module.y + 5 + y * (i + 1) - 4)/0.8), 0x000000);

                GlStateManager.popMatrix();
            } else {
                mc.fontRenderer.drawString(lines[i], x, (int) (module.y + 5 + y * (i + 1) - 4), 0x000000);
            }
        }
    }

    public void updateSizes() {
        for (PLCModule module : moduleList) {
            String[] lines = module.getDisplay();
            int heightLines = lines.length * 11 + 5 * 2;
            int inputHeight = module.inputCount() * (5 + 6) + 3;
            int outputHeight = module.outputCount() * (5 + 6) + 3;
            int height = Math.max(Math.max(heightLines, inputHeight), outputHeight);
            float maxL = 0;
            for (int i = 0;i < lines.length;i++) {
                float l = mc.fontRenderer.getStringWidth(lines[i]);
                if (i != 0) l *= 0.8f * 1.2f;
                if (l > maxL) maxL = l;
            }
            int width = (int)maxL + 14;
            module.setSize(width, height);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int scroll = Mouse.getDWheel();
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        if (scroll != 0) {
            if (mouseX > sr.getScaledWidth() - buttonWidth-20) {
                if (scroll < 0) {
                    scrollButtons += 15;
                } else {
                    scrollButtons -= 15;
                }
                saveClientState();
            }
        }
    }

    public void addElem(Modules mod) {
        PLCModule module = mod.instance();
        module.setPosition(vX+100-screenShiftX,vY+100-screenShiftY);
        module.setId(idCounter++);
        idToMod.put(module.id,module);
        moduleList.add(module);
        updateSizes();
        updateTile();
    }

    private boolean hovered (int mx, int my, int x, int y, int w, int h) {
        return mx >= x && my >= y && mx < x + w && my < y + h;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button != null && button.enabled) {
            if (button.id >= 0) {
                addElem(Modules.values()[button.id]);
            } else {
                if (highlighted instanceof InputPLCModule) {
                    ((InputPLCModule) highlighted).channel = -button.id-1;
                    displayChannels();
                    updateSizes();
                    updateTile();
                }
                if (highlighted instanceof OutputPLCModule) {
                    ((OutputPLCModule) highlighted).channel = -button.id-1;
                    displayChannels();
                    updateSizes();
                    updateTile();
                }
            }
        }
    }



    private int inputHovered(int mouseX, int mouseY, PLCModule module) {
        int height = module.height;
        int width = module.width;
        for (int j = 0;j < module.inputCount();j++) {
            int y = (int)(module.y + (double)height/(module.inputCount() + 1) * (j+1) - 3);
            int x = (int)module.x-9;
            int h = 5;
            int w = 9;
            if (hovered(mouseX,mouseY,x,y,w,h)) return j;
        }
        return -1;
    }

    private int outputHovered(int mouseX, int mouseY, PLCModule module) {
        int height = module.height;
        int width = module.width;
        for (int j = 0;j < module.outputCount();j++) {
            int y = (int)(module.y + (double)height/(module.outputCount() + 1) * (j+1) - 3);
            int x = (int)module.x+width;
            int h = 5;
            int w = 9;
            if (hovered(mouseX,mouseY,x,y,w,h)) return j;
        }
        return -1;
    }

    private Tuple<Integer,Integer> outputRootPos(PLCModule module, int out) {
        int height = module.height;
        int width = module.width;
        int y = (int)(module.y + (double)height/(module.outputCount() + 1) * (out+1) - 3);
        int x = (int)module.x+width;
        int h = 5;
        int w = 9;
        return new Tuple<>(x + w,y + h/2);
    }

    private Tuple<Integer,Integer> inputRootPos(PLCModule module, int out) {
        int height = module.height;
        int width = module.width;
        int y = (int)(module.y + (double)height/(module.inputCount() + 1) * (out+1) - 3);
        int x = (int)module.x;
        int h = 5;
        int w = 9;
        return new Tuple<>(x - w,y + h/2);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        for (int i = 0;i < textBoxes.size();i++) {
            if (textBoxes.get(i).getVisible()) {
                textBoxes.get(i).mouseClicked(mouseX,mouseY,mouseButton);
            }
        }

        if (mouseX < sr.getScaledWidth() - buttonWidth - 20) {
            highlighted = null;
            inputLineSelected = -1;
            selectedLineOf = null;
            selectedOutput = -1;
            hideFields();
            hideChannels();
            for (int i = moduleList.size() - 1; i >= 0; i--) {
                PLCModule module = moduleList.get(i);
                if (hovered(mouseX-screenShiftX, mouseY-screenShiftY, (int) module.x, (int) module.y, module.width, module.height)) {
                    selected = module;
                    selX = mouseX;
                    selY = mouseY;
                    selectedOutput = -1;
                    highlighted = module;
                    wasDragger = false;
                    if (module instanceof InputPLCModule || module instanceof OutputPLCModule) displayChannels();
                    else displayFields();
                    break;
                }
                int inH = outputHovered(mouseX-screenShiftX, mouseY-screenShiftY, module);
                if (inH != -1) {
                    selected = module;
                    selX = mouseX;
                    selY = mouseY;
                    selectedOutput = inH;
                    break;
                }

            }
        }

        if (selected == null) {
            for (PLCModule module : moduleList) {
                for (int j = 0; j < module.inputsFrom.length; j++) {
                    if (module.inputsFrom[j] != -1) {
                        int fromId = module.inputsFrom[j];
                        int output = module.inputsFromPosition[j];
                        if (idToMod.containsKey(fromId)) {
                            PLCModule from = idToMod.get(fromId);
                            if (onLine(from, output, module, j, mouseX-screenShiftX, mouseY-screenShiftY)) {
                                inputLineSelected = j;
                                selectedLineOf = module;
                                break;
                            }
                        }
                    }
                }
            }
            if (selectedLineOf == null && mouseX < sr.getScaledWidth() - buttonWidth - 20 && !moduleList.isEmpty()) {
                draggingScreen = true;
                selX = mouseX;
                selY = mouseY;
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);

        if (highlighted != null) {
            if (!wasDragger && selected instanceof SwitchPLCModule) {
                ((SwitchPLCModule) selected).toggle();
                updateTile();
            }
        }

        if (selected != null) {
            if (selectedOutput != -1) {
                for (PLCModule module : moduleList) {
                    int inH = inputHovered(mouseX-screenShiftX, mouseY-screenShiftY, module);
                    if (inH != -1) {
                        module.inputsFrom[inH] = selected.id;
                        module.inputsFromPosition[inH] = selectedOutput;
                    }
                }
            }
            updateTile();
            selected = null;
        }
        draggingScreen = false;
    }

    public boolean onLine(PLCModule module, int out, PLCModule module2, int in,int x,int y) {
        Tuple<Integer, Integer> rootFrom = outputRootPos(module, out);
        Tuple<Integer, Integer> rootTo = inputRootPos(module2, in);
        int xi = rootFrom.getFirst();
        int yi = rootFrom.getSecond();
        int xf = rootTo.getFirst();
        int yf = rootTo.getSecond();
        return onLine(xi,yi,xi+10,yi,x,y) || onLine(xf,yf,xf-10,yf,x,y) || onLine(xi+10,yi,xf-10,yf,x,y);
    }

    public boolean onLine(int xi,int yi,int xf,int yf,int xp,int yp) {
        int left = Math.min(xi,xf);
        int top = Math.min(yi,yf);
        int w = Math.max(xi,xf) - left;
        int h = Math.max(yi,yf) - top;
        if (!hovered(xp,yp,left,top,w,h)) return false;
        double u = Math.abs((yf-yi)*xp-(xf-xi)*yp+xf*yi-yf*xi)/Math.sqrt(Math.pow((yf-yi),2)+Math.pow(xf-xi,2));
        return u < 3.0;
    }

    public void updateTile() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setInteger("count",moduleList.size());
        for (int i = 0;i < moduleList.size();i++) {
            tagCompound.setTag("comp_"+i,moduleList.get(i).toNBT(false));
        }
        tagCompound.setInteger("counter",idCounter);
        TTPacketHandler.wrapper.sendToServer(new MessageGuiEvent(0,tagCompound));
    }

    public int getColor(PLCModule module, int out) {
        if (out < 0 || out > module.outputs.length) return 0;
        int val = module.outputs[out];
        if (val < 0 || val > colors.length) return 0;
        return colors[val];
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    }

    public void drawModuleToModule(PLCModule module, int out, PLCModule module2, int in,boolean selected) {
        Tuple<Integer,Integer> rootFrom = outputRootPos(module,out);
        Tuple<Integer,Integer> rootTo = inputRootPos(module2,in);
        drawFromTo(rootFrom.getFirst(),rootFrom.getSecond(),rootTo.getFirst(),rootTo.getSecond(),selected ? 0x00f7ff : getColor(module,out));
    }

    public void drawModuleToPos(PLCModule module, int out, int xf, int yf) {
        Tuple<Integer,Integer> rootFrom = outputRootPos(module,out);
        drawFromTo(rootFrom.getFirst(),rootFrom.getSecond(),xf,yf,getColor(module,out));
    }

    public void drawFromTo(int xi,int yi,int xf, int yf,int color) {
        int l = 10;
        drawLine(color,xi,yi,xi+l,yi);
        drawLine(color,xi+l,yi,xf-10,yf);
        drawLine(color,xf-10,yf,xf,yf);
    }

    public void drawLine(int color, int xi, int yi, int xf, int yf) {
        float r = (color >> 16)/255.0f;
        float g = ((color & 0x00ff00) >> 8) / 255.0f;
        float b = (color & 0xff)/255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();

        GL11.glLineWidth(4.0F); // thickness

        GL11.glBegin(GL11.GL_LINES);
        GL11.glColor4f(r, g, b, 1F); // RGBA
        GL11.glVertex2f(xi, yi);
        GL11.glVertex2f(xf, yf);
        GL11.glEnd();


        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    @Override
    public boolean shouldDisplay() {
        return false;
    }
}
