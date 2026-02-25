package BananaFructa.TTIEMultiblocks.Gui;

import BananaFructa.TTIEMultiblocks.ControlBlocks.LoadSensorTileEntity;
import BananaFructa.TTIEMultiblocks.ControlBlocks.PIDControllerTileEntity;
import BananaFructa.TTIEMultiblocks.PowerNetworkInfo.ModularList;
import BananaFructa.TTIEMultiblocks.PowerNetworkInfo.NetworkDeviceHistory;
import BananaFructa.TiagThings.Netowrk.MessageGuiEvent;
import BananaFructa.TiagThings.Netowrk.TTPacketHandler;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.*;

public class PIDControllerGui extends GuiIEContainerBase {

    PIDControllerTileEntity tile;
    GuiElementSlider referenceSlider;
    GuiElementSlider pSlider;
    GuiElementSlider iSlider;
    GuiElementSlider dSlider;
    ScalingButton pScale;
    ScalingButton iScale;
    ScalingButton dScale;
    ModularList input;
    ModularList output;
    int scalingP;
    int scalingI;
    int scalingD;

    public PIDControllerGui(InventoryPlayer inventoryPlayer, PIDControllerTileEntity tile) {
        super(new ContainerPIDController(inventoryPlayer,tile));
        this.tile = tile;
    }

    public void setInputOutput(ModularList input, ModularList output) {
        this.input = input;
        this.output = output;
    }

    public void updateGraph(NBTTagCompound tagCompound) {
        input.add(tagCompound.getInteger("in"));
        output.add(tagCompound.getInteger("out"));
    }

    @Override
    public void initGui() {
        ySize = 256;
        super.initGui();
        this.referenceSlider = new GuiElementSlider(0,guiLeft+25,guiTop+23,126,4,"Reference Value");
        this.pSlider = new GuiElementSlider(1,guiLeft+25,guiTop+44,126,4,"Proportional Gain");
        this.iSlider = new GuiElementSlider(2,guiLeft+25,guiTop+65,126,4,"Integral Gain");
        this.dSlider = new GuiElementSlider(3,guiLeft+25,guiTop+86,126,4,"Derivative Gain");
        this.pScale = new ScalingButton(4,guiLeft+6,guiTop+32,16,16);
        this.iScale = new ScalingButton(5,guiLeft+6,guiTop+53,16,16);
        this.dScale = new ScalingButton(6,guiLeft+6,guiTop+74,16,16);

        this.referenceSlider.val = (float)tile.reference/15;
        this.scalingP = tile.scalingP;
        this.scalingD = tile.scalingD;
        this.scalingI = tile.scalingI;
        float pScaleVal = (float)Math.pow(10,scalingP);
        float iScaleVal = (float)Math.pow(10,scalingI);
        float dScaleVal = (float)Math.pow(10,scalingD);
        this.pSlider.val = ((float)tile.p+20*pScaleVal)/(40*pScaleVal);
        this.iSlider.val = ((float)tile.i+20*iScaleVal)/(40*iScaleVal);
        this.dSlider.val = ((float)tile.d+20*dScaleVal)/(40*dScaleVal);

        addButton(referenceSlider);
        addButton(pSlider);
        addButton(iSlider);
        addButton(dSlider);
        addButton(pScale);
        addButton(iScale);
        addButton(dScale);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ArrayList<String> tooltip = new ArrayList();
        RenderHelper.enableGUIStandardItemLighting();
        for (GuiButton button : this.buttonList) {
            boolean hovered = mouseX >= button.x && mouseY >= button.y && mouseX < button.x + button.width && mouseY < button.y + button.height;
            if (hovered) {
                tooltip.add(button.displayString);
            }
        }
        if (input != null) {
            renderGraph(input, 0xc80000, guiLeft + 17, guiTop + 164, 15);
        }
        if (output != null) {
            renderGraph(output, 0x2c27dc, guiLeft + 17, guiTop + 164, 15 * 1000);
        }
        mc.fontRenderer.drawString("Reference Value: " + (int)(referenceSlider.val * 15),guiLeft+25,guiTop+23-12,0xff000000);
        mc.fontRenderer.drawString("Proportional Gain: " + String.format("%.2f",(pSlider.val * 40 - 20) * Math.pow(10,scalingP)),guiLeft+25,guiTop+44-12,0xff000000);
        mc.fontRenderer.drawString("Integral Gain: " + String.format("%.2f",(iSlider.val * 40 - 20)* Math.pow(10,scalingI)),guiLeft+25,guiTop+65-12,0xff000000);
        mc.fontRenderer.drawString("Derivative Gain: " + String.format("%.2f",(dSlider.val * 40 - 20)* Math.pow(10,scalingD)),guiLeft+25,guiTop+86-12,0xff000000);
        if(!tooltip.isEmpty()) {
            ClientUtils.drawHoveringText(tooltip, mouseX, mouseY, fontRenderer, guiLeft + xSize, - 1);
        }
        RenderHelper.enableGUIStandardItemLighting();
    }

    private boolean hovered (int mx, int my, int x, int y, int w, int h) {
        return mx >= x && my >= y && mx < x + w && my < y + h;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        ClientUtils.bindTexture("tiagthings:textures/gui/pid_controller_gui.png");
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button instanceof GuiElementSlider) {
            ((GuiElementSlider)(button)).buttonHeld = true;
        }
        int add = isShiftKeyDown() ? -1 : 1;
        if (button instanceof ScalingButton) {
            switch (button.id) {
                case 4:
                    scalingP += add;
                    break;
                case 5:
                    scalingI += add;
                    break;
                case 6:
                    scalingD += add;
                    break;
            }
            scalingP = Math.max(Math.min(scalingP,2),-2);
            scalingI = Math.max(Math.min(scalingI,2),-2);
            scalingD = Math.max(Math.min(scalingD,2),-2);
            sendUpdate();
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        for (GuiButton button : this.buttonList) {
            if (button instanceof GuiElementSlider) {
                GuiElementSlider buttonElectricMotorSlider = ((GuiElementSlider)(button));
                if (buttonElectricMotorSlider.buttonHeld) {
                    sendUpdate();
                    buttonElectricMotorSlider.buttonHeld = false;
                }
            }
        }
    }

    private void sendUpdate() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setFloat("reference", referenceSlider.val);
        tagCompound.setFloat("p",pSlider.val);
        tagCompound.setFloat("i",iSlider.val);
        tagCompound.setFloat("d",dSlider.val);
        tagCompound.setInteger("scaling_p",scalingP);
        tagCompound.setInteger("scaling_d",scalingD);
        tagCompound.setInteger("scaling_i",scalingI);
        TTPacketHandler.wrapper.sendToServer(new MessageGuiEvent(0,tagCompound));
    }

    public void renderGraph(ModularList history, int color,int x,int y, float top) {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        GL11.glColor4f(1, 1, 1, 1);
        float r = (color >> 16)/255.0f;
        float g = ((color & 0x00ff00) >> 8) / 255.0f;
        float b = (color & 0xff)/255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();

        GL11.glLineWidth(2.0F); // thickness

        for (int i = 0; i < history.length() - 1; i++) {
            int first = history.get(i);
            int second = history.get(i+1);
            GL11.glBegin(GL11.GL_LINES);
            GL11.glColor4f(r, g, b, 1F); // RGBA
            GL11.glVertex2f(x+141-i*0.71f, y-56*(first) / top);
            GL11.glVertex2f(x+141-(i+1)*0.71f, y-56*(second)/top);
            GL11.glEnd();
        }

        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }
}
