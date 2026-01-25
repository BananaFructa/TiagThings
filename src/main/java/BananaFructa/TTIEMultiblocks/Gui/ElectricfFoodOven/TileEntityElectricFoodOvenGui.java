package BananaFructa.TTIEMultiblocks.Gui.ElectricfFoodOven;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityCokerUnit;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityElectricFoodOven;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class TileEntityElectricFoodOvenGui extends GuiIEContainerBase {

    TileEntityElectricFoodOven tile;

    public TileEntityElectricFoodOvenGui(InventoryPlayer inventoryPlayer, TileEntityElectricFoodOven tile) {
        super(new ContainerElectricFoodOven(inventoryPlayer,tile));
        this.tile = tile;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ArrayList<String> tooltip = new ArrayList();
        RenderHelper.enableGUIStandardItemLighting();
        if(!tooltip.isEmpty()) {
            ClientUtils.drawHoveringText(tooltip, mouseX, mouseY, fontRenderer, guiLeft + xSize, - 1);
        }
        RenderHelper.enableGUIStandardItemLighting();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        ClientUtils.bindTexture("tiagthings:textures/gui/electric_food_oven.png");
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        float progres = this.tile.progress();
        drawTexturedModalRect(this.guiLeft+63,this.guiTop+33,178,2,(int)(47*progres),19);
    }
}
