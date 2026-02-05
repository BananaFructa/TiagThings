package BananaFructa.TTIEMultiblocks.Gui;

import BananaFructa.TTIEMultiblocks.ControlBlocks.LoadSensor;
import BananaFructa.TTIEMultiblocks.ControlBlocks.LoadSensorTileEntity;
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

public class LoadSensorGui extends GuiIEContainerBase {

    LoadSensorTileEntity tile;
    GuiElementSlider offsetSlider;
    GuiElementSlider magnitudeSlider;

    public LoadSensorGui(InventoryPlayer inventoryPlayer, LoadSensorTileEntity tile) {
        super(new ContainerLoadSensor(inventoryPlayer,tile));
        this.tile = tile;
    }

    @Override
    public void initGui() {
        super.initGui();
        GuiElementSlider offset = new GuiElementSlider(0,guiLeft+25,guiTop+23,126,4,"Redstone Offset");
        GuiElementSlider scale = new GuiElementSlider(1,guiLeft+25,guiTop+44,126,4,"Power Scale");
        this.offsetSlider = offset;
        this.magnitudeSlider = scale;
        offset.val = (float)tile.redstoneOffset/15;
        scale.val = (float)tile.magnitude/ LoadSensorTileEntity.maxRF;
        addButton(offset);
        addButton(scale);
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
        mc.fontRenderer.drawString("Offset Strength: " + (int)(offsetSlider.val * 15),guiLeft+25,guiTop+30,0xff000000);
        mc.fontRenderer.drawString("Power Scale: " + (int)(magnitudeSlider.val * LoadSensorTileEntity.maxRF + 1) + " RF/t",guiLeft+25,guiTop+51,0xff000000);
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
        ClientUtils.bindTexture("tiagthings:textures/gui/load_sensor_gui.png");
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
                    tagCompound.setFloat("offset", offsetSlider.val);
                    tagCompound.setFloat("magnitude", magnitudeSlider.val);
                    TTPacketHandler.wrapper.sendToServer(new MessageGuiEvent(0,tagCompound));
                    buttonElectricMotorSlider.buttonHeld = false;
                }
            }
        }
    }
}
