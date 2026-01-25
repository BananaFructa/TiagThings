package BananaFructa.TTIEMultiblocks.Gui;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityFBR;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntitySiliconCrucible;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class FBRGui extends GuiIEContainerBase {

    TileEntityFBR tile;

    public FBRGui(InventoryPlayer inventoryPlayer, TileEntityFBR tile) {
        super(new ContainerFBR(inventoryPlayer,tile));
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
        ClientUtils.handleGuiTank(this.tile.tanks.get(1), this.guiLeft + 26, this.guiTop + 19, 16, 47, 177, 31, 20, 51, mouseX, mouseY, "tiagthings:textures/gui/fbr_gui.png", tooltip);
        ClientUtils.handleGuiTank(this.tile.tanks.get(0), this.guiLeft + 132, this.guiTop + 19, 16, 47, 177, 31, 20, 51, mouseX, mouseY, "tiagthings:textures/gui/fbr_gui.png", tooltip);
        RenderHelper.enableGUIStandardItemLighting();
        for (GuiButton button : this.buttonList) {
            boolean hovered = mouseX >= button.x && mouseY >= button.y && mouseX < button.x + button.width && mouseY < button.y + button.height;
            if (hovered) {
                tooltip.add(button.displayString);
            }
        }
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
        ClientUtils.bindTexture("tiagthings:textures/gui/fbr_gui.png");
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        ClientUtils.handleGuiTank(this.tile.tanks.get(1), this.guiLeft + 26, this.guiTop + 19, 16, 47, 177, 31, 20, 51, mouseX, mouseY, "tiagthings:textures/gui/fbr_gui.png", null);
        ClientUtils.handleGuiTank(this.tile.tanks.get(0), this.guiLeft + 132, this.guiTop + 19, 16, 47, 177, 31, 20, 51, mouseX, mouseY, "tiagthings:textures/gui/fbr_gui.png", null);
        drawTexturedModalRect(this.guiLeft+47,this.guiTop+14,4,169,(int)(80*tile.progress()),50);
    }
}
