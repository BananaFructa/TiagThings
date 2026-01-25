package BananaFructa.TTIEMultiblocks.Gui.CokerUnit;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityCokerUnit;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class TileEntityCokerUnitGui extends GuiIEContainerBase {

    TileEntityCokerUnit tile;

    public TileEntityCokerUnitGui(InventoryPlayer inventoryPlayer, TileEntityCokerUnit tile) {
        super(new ContainerCokerUnit(inventoryPlayer,tile));
        this.tile = tile;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ArrayList<String> tooltip = new ArrayList();
        ClientUtils.handleGuiTank(this.tile.tanks.get(0), this.guiLeft + 16, this.guiTop + 20, 16, 47, 177, 31, 20, 51, mouseX, mouseY, "tiagthings:textures/gui/coker_unit.png", tooltip);
        ClientUtils.handleGuiTank(this.tile.tanks.get(1), this.guiLeft + 37, this.guiTop + 20, 16, 47, 177, 31, 20, 51, mouseX, mouseY, "tiagthings:textures/gui/coker_unit.png", tooltip);
        ClientUtils.handleGuiTank(this.tile.tanks.get(2), this.guiLeft + 145, this.guiTop + 20, 16, 47, 177, 31, 20, 51, mouseX, mouseY, "tiagthings:textures/gui/coker_unit.png", tooltip);
        RenderHelper.enableGUIStandardItemLighting();
        if(!tooltip.isEmpty()) {
            ClientUtils.drawHoveringText(tooltip, mouseX, mouseY, fontRenderer, guiLeft + xSize, - 1);
        }
        RenderHelper.enableGUIStandardItemLighting();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        ClientUtils.bindTexture("tiagthings:textures/gui/coker_unit.png");
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        ClientUtils.handleGuiTank(this.tile.tanks.get(0), this.guiLeft + 16, this.guiTop + 20, 16, 47, 177, 31, 20, 51, mouseX, mouseY, "tiagthings:textures/gui/coker_unit.png", null);
        ClientUtils.handleGuiTank(this.tile.tanks.get(1), this.guiLeft + 37, this.guiTop + 20, 16, 47, 177, 31, 20, 51, mouseX, mouseY, "tiagthings:textures/gui/coker_unit.png", null);
        ClientUtils.handleGuiTank(this.tile.tanks.get(2), this.guiLeft + 145, this.guiTop + 20, 16, 47, 177, 31, 20, 51, mouseX, mouseY, "tiagthings:textures/gui/coker_unit.png", null);
        float progres = this.tile.progress();
        drawTexturedModalRect(this.guiLeft+22,this.guiTop+12,1,167,(int)(133*progres),64);
    }
}
