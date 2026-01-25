package BananaFructa.TTIEMultiblocks.Gui;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityMagnetizer;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityOpenHearthFurnace;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class OpenHearthFurnaceGui extends GuiIEContainerBase {

    TileEntityOpenHearthFurnace tile;

    public OpenHearthFurnaceGui(InventoryPlayer inventoryPlayer, TileEntityOpenHearthFurnace tile) {
        super(new ContainerOpenHearthFurnace(inventoryPlayer,tile));
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
        handleGuiTank(tile.tanks.get(1).getFluid(),tile.tanks.get(1).getCapacity(),guiLeft + 45,guiTop + 43,59,25,mouseX,mouseY, "tiagthings:textures/gui/open_hearth_furnace_gui.png",tooltip);
        ClientUtils.handleGuiTank(this.tile.tanks.get(0), this.guiLeft + 146, this.guiTop + 27, 16, 47, 177, 31, 20, 51, mouseX, mouseY, "tiagthings:textures/gui/open_hearth_furnace_gui.png", tooltip);
        RenderHelper.enableGUIStandardItemLighting();
        if (tile.idleTime > 0) {
            mc.fontRenderer.drawString("Decarburization in: " + (int)((TileEntityOpenHearthFurnace.idleTimeToMildSteel - tile.idleTime)/20) + " seconds",guiLeft + 9,guiTop + 7,0x000000);
        }
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
        ClientUtils.bindTexture("tiagthings:textures/gui/open_hearth_furnace_gui.png");
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        handleGuiTank(tile.tanks.get(1).getFluid(),tile.tanks.get(1).getCapacity(),guiLeft + 45,guiTop + 43,59,25,mouseX,mouseY, "tiagthings:textures/gui/open_hearth_furnace_gui.png",null);
        ClientUtils.handleGuiTank(this.tile.tanks.get(0), this.guiLeft + 146, this.guiTop + 27, 16, 47, 177, 32, 20, 51, mouseX, mouseY, "tiagthings:textures/gui/open_hearth_furnace_gui.png", null);
        drawTexturedModalRect(guiLeft+65,guiTop+38,187,12,(int)(47*tile.progress()),4);
        float fuel = tile.getRemainingFuel();
        drawTexturedModalRect(this.guiLeft + 124,this.guiTop+(int)((39+12)-12*fuel),179,(int)((13+12)-12*fuel),9,(int)(15*fuel));
        float progres = this.tile.getChargingProcess();
        drawTexturedModalRect(this.guiLeft+33,this.guiTop+24,3,173,(int)(45*progres),15);
    }

    public static void handleGuiTank(FluidStack fluid, int capacity, int x, int y, int w, int h, int mX, int mY, String originalTexture, ArrayList<String> tooltip) {
        if (tooltip == null) {
            if (fluid != null && fluid.getFluid() != null) {
                int fluidHeight = (int) ((float) h * ((float) fluid.amount / (float) capacity));
                ClientUtils.drawRepeatedFluidSprite(fluid, (float) x, (float) (y + h - fluidHeight), (float) w, (float) fluidHeight);
                ClientUtils.bindTexture(originalTexture);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            }
        } else {
            if (mX >= x && mX < x + w && mY >= y && mY < y + h) {
                ClientUtils.addFluidTooltip(fluid, tooltip, capacity);
            }
        }
    }
}
