package BananaFructa.TTIEMultiblocks.Gui;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityMagneticSeparator;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityMetalRoller;
import BananaFructa.TiagThings.Netowrk.MessageGuiEvent;
import BananaFructa.TiagThings.Netowrk.TTPacketHandler;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;

public class MagneticSeparatorGui extends GuiIEContainerBase {

    TileEntityMagneticSeparator tile;

    public MagneticSeparatorGui(InventoryPlayer inventoryPlayer, TileEntityMagneticSeparator tile) {
        super(new ContainerMagneticSeparatorGui(inventoryPlayer,tile));
        this.tile = tile;
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ArrayList<String> tooltip = new ArrayList();
        ClientUtils.handleGuiTank(this.tile.tanks.get(0), this.guiLeft + 21, this.guiTop + 18, 16, 47, 177, 31, 20, 51, mouseX, mouseY, "tiagthings:textures/gui/magnetic_separator_gui.png", tooltip);
        RenderHelper.enableGUIStandardItemLighting();
        for (GuiButton button : this.buttonList) {
            boolean hovered = mouseX >= button.x && mouseY >= button.y && mouseX < button.x + button.width && mouseY < button.y + button.height;
            if (hovered) {
                tooltip.add(button.displayString);
            }
        }
        if (hovered(mouseX,mouseY,guiLeft+70,guiTop+15,22,9)) {
            tooltip.add("Input torque: " + String.format("%.1f", tile.inTorque) + "Nm.");
            tooltip.add(TextFormatting.AQUA + "Minimum torque is 5Nm.");
        }
        if (hovered(mouseX,mouseY,guiLeft+106,guiTop+15,22,9)) {
            tooltip.add("Input speed: " + String.format("%.1f", tile.inSpeed) + "RPM.");
            tooltip.add(TextFormatting.AQUA + "Minimum speed is 20RPM and the maximum speed is 40RPM.");
        }
        mc.fontRenderer.drawString(""+ String.format("%.1f", tile.inTorque),guiLeft+83,guiTop+15,tile.inTorque >= 5 ? 0xff339900 : 0xffff6600);
        mc.fontRenderer.drawString(""+ String.format("%.1f", tile.inSpeed),guiLeft+115,guiTop+15,tile.inSpeed >= 20 && tile.inSpeed < 40 ? 0xff339900 : 0xffff6600);
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
        ClientUtils.bindTexture("tiagthings:textures/gui/magnetic_separator_gui.png");
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        ClientUtils.handleGuiTank(this.tile.tanks.get(0), this.guiLeft + 21, this.guiTop + 18, 16, 47, 177, 31, 20, 51, mouseX, mouseY, "tiagthings:textures/gui/magnetic_separator_gui.png", null);
        drawTexturedModalRect(guiLeft+42,guiTop+25,1,167,(int)(97*tile.progress()),38);
    }
}
