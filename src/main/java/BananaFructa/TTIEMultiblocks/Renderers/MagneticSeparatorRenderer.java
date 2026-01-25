package BananaFructa.TTIEMultiblocks.Renderers;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityClarifier;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityMagneticSeparator;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntitySiliconCrucible;
import BananaFructa.TTIEMultiblocks.Utils.MultiblockAnimation.AnimationGroup;
import blusunrize.immersiveengineering.client.ClientUtils;
import mrtjp.projectred.integration.NOR;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidTank;

import javax.vecmath.Vector3f;
import java.io.IOException;

public class MagneticSeparatorRenderer extends AnimatedOBJTileRenderer<TileEntityMagneticSeparator> {
    public MagneticSeparatorRenderer() {
        super(new AnimationGroup("bones",new Vector3f(0,0,0),
                new AnimationGroup("bone",new Vector3f(-0.1767f,17.35f,16.0167f),1)
        ),1000);
        try {
            loadAnimation("magnetic_separator.animation.json",1.0f,1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render(TileEntityMagneticSeparator te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        if (te != null && !te.isDummy() && !te.tanks.isEmpty()) {
            FluidTank inputTank = te.tanks.get(0);
            if (inputTank != null && inputTank.getFluidAmount() > 0) {
                GlStateManager.pushMatrix();
                int addX = 0;
                int addZ = 0;
                if (!te.mirrored && (te.facing == EnumFacing.SOUTH)) addX = -1;
                if (!te.mirrored && (te.facing == EnumFacing.NORTH)) {
                    addX = -1;
                    addZ = 2;
                }
                if (te.mirrored && (te.facing == EnumFacing.NORTH)) {
                    addX = -2;
                    addZ = 2;
                }
                if (te.mirrored && te.facing == EnumFacing.EAST) {
                    addZ = -1;
                }
                if (te.mirrored && te.facing == EnumFacing.WEST) {
                    addZ = 1;
                }
                GlStateManager.translate(x+0.75 + addX,y+0.7,z-0.3 + addZ);

                float level = inputTank.getFluidAmount()/(float)inputTank.getCapacity();
                GlStateManager.translate(0,level * 0.2,0);
                GlStateManager.rotate(90, 1f, 0f, 0f);
                GlStateManager.enableAlpha();
                GlStateManager.enableBlend();
                GlStateManager.disableColorMaterial();
                GlStateManager.disableLighting();

                float xs = -1.5f;
                float ys = -0.5f;
                float w = 1.55f;
                float h = 3.6f;

                if (te.facing == EnumFacing.EAST) ClientUtils.drawRepeatedFluidSprite(inputTank.getFluid(),xs,ys,w,h);
                else if (te.facing == EnumFacing.WEST) ClientUtils.drawRepeatedFluidSprite(inputTank.getFluid(),ys,xs,w,h);
                else if (te.facing == EnumFacing.NORTH) ClientUtils.drawRepeatedFluidSprite(inputTank.getFluid(),ys,xs,h,w);
                else if (te.facing == EnumFacing.SOUTH) ClientUtils.drawRepeatedFluidSprite(inputTank.getFluid(),xs,ys,h,w);

                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
    }
}
