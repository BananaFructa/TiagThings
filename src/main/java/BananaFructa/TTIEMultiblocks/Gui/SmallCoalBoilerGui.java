package BananaFructa.TTIEMultiblocks.Gui;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntitySmallCoalBoiler;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class SmallCoalBoilerGui extends GuiIEContainerBase {

    TileEntitySmallCoalBoiler tile;

    public SmallCoalBoilerGui(InventoryPlayer inventoryPlayer, TileEntitySmallCoalBoiler tile) {
        super(new ContainerSmallCoalBoiler(inventoryPlayer,tile));
        this.tile = tile;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ArrayList<String> tooltip = new ArrayList();
        ClientUtils.handleGuiTank(this.tile.tanks.get(1), this.guiLeft + 29, this.guiTop + 16, 16, 47, 177, 31, 20, 51, mouseX, mouseY, "tiagthings:textures/gui/small_coal_boiler_gui.png", tooltip);
        ClientUtils.handleGuiTank(this.tile.tanks.get(0), this.guiLeft + 131, this.guiTop + 16, 16, 47, 177, 31, 20, 51, mouseX, mouseY, "tiagthings:textures/gui/small_coal_boiler_gui.png", tooltip);
        RenderHelper.enableGUIStandardItemLighting();
        if(!tooltip.isEmpty()) {
            ClientUtils.drawHoveringText(tooltip, mouseX, mouseY, fontRenderer, guiLeft + xSize, - 1);
        }
        RenderHelper.enableGUIStandardItemLighting();
        String val = ""+(int)(this.tile.getTemperature());
        int x = (20 - mc.fontRenderer.getStringWidth(val))/2;
        mc.fontRenderer.drawString(val,this.guiLeft+152+x,this.guiTop+38,0xffffffff);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        ClientUtils.bindTexture("tiagthings:textures/gui/small_coal_boiler_gui.png");
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        ClientUtils.handleGuiTank(this.tile.tanks.get(1), this.guiLeft + 29, this.guiTop + 16, 16, 47, 177, 31, 20, 51, mouseX, mouseY, "tiagthings:textures/gui/small_coal_boiler_gui.png", null);
        ClientUtils.handleGuiTank(this.tile.tanks.get(0), this.guiLeft + 131, this.guiTop + 16, 16, 47, 177, 31, 20, 51, mouseX, mouseY, "tiagthings:textures/gui/small_coal_boiler_gui.png", null);
        float fuel = tile.getRemainingFuel();
        drawTexturedModalRect(this.guiLeft + 96,this.guiTop+(int)(70-12*fuel),178,(int)(26-12*fuel),9,(int)(12*fuel));
        int c = (int)(this.tile.getTemperature()/(100));
        drawTexturedModalRect(this.guiLeft+152,this.guiTop+28,176+7*c,0,7,7);
        float progres = this.tile.getBoilProcess();
        drawTexturedModalRect(this.guiLeft+49,this.guiTop+18,3,172,(int)(78*progres),28);
    }
}
