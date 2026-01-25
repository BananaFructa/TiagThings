package BananaFructa.TTIEMultiblocks.Gui.CokeOvenBattery;

import BananaFructa.TTIEMultiblocks.Gui.ContainerSmallCoalBoiler;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityCokeOvenBattery;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntitySmallCoalBoiler;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class TileEntityCokeOvenBatteryGui extends GuiIEContainerBase {

    TileEntityCokeOvenBattery tile;

    public TileEntityCokeOvenBatteryGui(InventoryPlayer inventoryPlayer, TileEntityCokeOvenBattery tile) {
        super(new ContainerCokeOvenBattery(inventoryPlayer,tile));
        this.tile = tile;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ArrayList<String> tooltip = new ArrayList();
        ClientUtils.handleGuiTank(this.tile.tanks.get(0), this.guiLeft + 16, this.guiTop + 20, 16, 47, 177, 31, 20, 51, mouseX, mouseY, "tiagthings:textures/gui/coke_oven_battery.png", tooltip);
        RenderHelper.enableGUIStandardItemLighting();
        if(!tooltip.isEmpty()) {
            ClientUtils.drawHoveringText(tooltip, mouseX, mouseY, fontRenderer, guiLeft + xSize, - 1);
        }
        RenderHelper.enableGUIStandardItemLighting();
        String val = ""+(int)(this.tile.getTemperature());
        int x = (37 - mc.fontRenderer.getStringWidth(val))/2;
        mc.fontRenderer.drawString(val,this.guiLeft+16+x,this.guiTop+6,0xff000000);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        ClientUtils.bindTexture("tiagthings:textures/gui/coke_oven_battery.png");
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        ClientUtils.handleGuiTank(this.tile.tanks.get(0), this.guiLeft + 16, this.guiTop + 20, 16, 47, 177, 31, 20, 51, mouseX, mouseY, "tiagthings:textures/gui/coke_oven_battery.png", null);
        float fuel = tile.getRemainingFuel();
        drawTexturedModalRect(this.guiLeft + 40,this.guiTop+(int)(46-12*fuel),178,(int)(26-12*fuel),9,(int)(12*fuel));
        float progres = this.tile.getCokeProcess();
        drawTexturedModalRect(this.guiLeft+104,this.guiTop+28,194,90,(int)(46*progres),41);
    }
}
