package BananaFructa.TTIEMultiblocks.Gui;

import BananaFructa.TTIEMultiblocks.ElectricMotorTileEntity;
import BananaFructa.TiagThings.Netowrk.MessageGuiEvent;
import BananaFructa.TiagThings.Netowrk.TTPacketHandler;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;

public class ElectricalMotorGui extends GuiIEContainerBase {

    ElectricMotorTileEntity tile;
    GuiElementSlider speedSlider;
    GuiElementSlider torqueSlider;

    public ElectricalMotorGui(InventoryPlayer inventoryPlayer, ElectricMotorTileEntity tile) {
        super(new ContainerElectricMotor(inventoryPlayer,tile));
        this.tile = tile;
    }

    @Override
    public void initGui() {
        super.initGui();
        GuiElementSlider speed = new GuiElementSlider(0,guiLeft+25,guiTop+23,126,4,"Speed Control");
        GuiElementSlider torque = new GuiElementSlider(1,guiLeft+25,guiTop+44,126,4,"Torque Control");
        this.speedSlider = speed;
        this.torqueSlider = torque;
        ElectricMotorTileEntity.PowerConnectionType connectionType = tile.connectionType;
        if (connectionType == ElectricMotorTileEntity.PowerConnectionType.NONE) return;
        if (connectionType == ElectricMotorTileEntity.PowerConnectionType.LV) {
            speed.val = (float)tile.targetSpeed/(ElectricMotorTileEntity.maxRPMLV-ElectricMotorTileEntity.minRPMLV);
        } else if (connectionType == ElectricMotorTileEntity.PowerConnectionType.MV) {
            speed.val = (float)(tile.targetSpeed-ElectricMotorTileEntity.minRPMMV)/(ElectricMotorTileEntity.maxRPMMV-ElectricMotorTileEntity.minRPMMV);
        }
        torque.val = (float)tile.targetTorque/ElectricMotorTileEntity.torqueRange;
        addButton(speed);
        addButton(torque);
    }

    public float targetRPM(float val, ElectricMotorTileEntity.PowerConnectionType type) {
        if (type == ElectricMotorTileEntity.PowerConnectionType.NONE) return 0;
        if (type == ElectricMotorTileEntity.PowerConnectionType.LV) return (ElectricMotorTileEntity.maxRPMLV-ElectricMotorTileEntity.minRPMLV) * val + ElectricMotorTileEntity.minRPMLV;
        if (type == ElectricMotorTileEntity.PowerConnectionType.MV) return (ElectricMotorTileEntity.maxRPMMV-ElectricMotorTileEntity.minRPMMV) * val + ElectricMotorTileEntity.minRPMMV;
        return 0;
    }

    public float targetTorque(float val) {
        return ElectricMotorTileEntity.torqueRange * val;
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
        float targetTorque = targetTorque(torqueSlider.val);
        float targetSpeed = targetRPM(speedSlider.val,tile.connectionType);
        mc.fontRenderer.drawString("Connection type: " + (tile.connectionType.name),guiLeft+6,guiTop+6,0xff000000);
        mc.fontRenderer.drawString("Speed: " + String.format("%.2f",(targetRPM(speedSlider.val,tile.connectionType))) + " RPM",guiLeft+25,guiTop+30,0xff000000);
        mc.fontRenderer.drawString("Torque: " + String.format("%.2f",(targetTorque(torqueSlider.val))) + " Nm",guiLeft+25,guiTop+51,0xff000000);
        mc.fontRenderer.drawString("Power consumption:",guiLeft+6,guiTop+63,0xff000000);
        mc.fontRenderer.drawString((!tile.good ? "\u00a7c" : "") + String.format("%.2f",ElectricMotorTileEntity.getPowerConsumption(targetSpeed,targetTorque)) + " RF/t\u00a7r (Eff: " + String.format("%.2f",ElectricMotorTileEntity.getEfficiency(targetTorque)*100)+ "%)",guiLeft+6,guiTop+73,0xff000000);
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
        ClientUtils.bindTexture("tiagthings:textures/gui/electric_motor_gui.png");
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button instanceof GuiElementSlider) {
            ((GuiElementSlider)(button)).buttonHeld = true;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        for (GuiButton button : this.buttonList) {
            if (button instanceof GuiElementSlider) {
                GuiElementSlider buttonElectricMotorSlider = ((GuiElementSlider)(button));
                if (buttonElectricMotorSlider.buttonHeld) {
                    NBTTagCompound tagCompound = new NBTTagCompound();
                    tagCompound.setFloat("speed",speedSlider.val);
                    tagCompound.setFloat("torque",torqueSlider.val);
                    TTPacketHandler.wrapper.sendToServer(new MessageGuiEvent(0,tagCompound));
                    buttonElectricMotorSlider.buttonHeld = false;
                }
            }
        }
    }
}
