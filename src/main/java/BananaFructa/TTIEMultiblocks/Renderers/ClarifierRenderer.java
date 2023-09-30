package BananaFructa.TTIEMultiblocks.Renderers;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityClarifier;
import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidTank;

public class ClarifierRenderer extends TileEntitySpecialRenderer<TileEntityClarifier> {

    @Override
    public void render(TileEntityClarifier te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        if (te != null && !te.isDummy() && !te.tanks.isEmpty()) {
            FluidTank inputTank = te.tanks.get(0);
            if (inputTank != null && inputTank.getFluidAmount() > 0) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(x,y-2,z);

                float h = inputTank.getFluidAmount()/(float)inputTank.getCapacity();
                GlStateManager.translate(0,h * 1.99,0);
                GlStateManager.rotate(90, 1f, 0f, 0f);
                GlStateManager.enableAlpha();
                GlStateManager.enableBlend();
                GlStateManager.disableColorMaterial();
                GlStateManager.disableLighting();

                if (te.facing == EnumFacing.EAST) ClientUtils.drawRepeatedFluidSprite(inputTank.getFluid(),-9,-4,9.5f,9.5f);
                else if (te.facing == EnumFacing.WEST) ClientUtils.drawRepeatedFluidSprite(inputTank.getFluid(),0,-4,9.5f,9.5f);
                else if (te.facing == EnumFacing.NORTH) ClientUtils.drawRepeatedFluidSprite(inputTank.getFluid(),-4,0,9.5f,9.5f);
                else if (te.facing == EnumFacing.SOUTH) ClientUtils.drawRepeatedFluidSprite(inputTank.getFluid(),-4,-9,9.5f,9.5f);

                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
    }
}
