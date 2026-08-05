package BananaFructa.TTIEMultiblocks.Renderers;

import BananaFructa.TTIEMultiblocks.IECopy.BlockTTBase;
import BananaFructa.TTIEMultiblocks.TTBlockTypes_MetalMultiblock_4;
import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityClarifier;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityPLC;
import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidTank;

import javax.vecmath.Vector3f;

public class PLCRenderer extends TileEntitySpecialRenderer<TileEntityPLC> {

    public Vector3f[] axes = new Vector3f[]{
            new Vector3f(-1,0,0),
            new Vector3f(1,0,0),
            new Vector3f(0,0,1),
            new Vector3f(0,0,-1)
    };

    @Override
    public void render(TileEntityPLC te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        if (te.isDummy()) return;
        for (int i = 0;i < te.boards.size();i++) {
            Vector3f translation = new Vector3f(axes[te.facing.ordinal() - EnumFacing.NORTH.ordinal()]);
            translation.scale((i%7)/4.5f);
            translation.add(new Vector3f(0,(-(i/7))*0.78f,0));
            renderSection(te,x,y,z,translation);
        }

    }


    private void renderSection(TileEntityPLC te, double x, double y, double z, Vector3f translation) {
        IBlockState state = TTIEContent.ttBlockMetalMultiblock_4.getStateFromMeta(TTBlockTypes_MetalMultiblock_4.PLC.getMeta()).withProperty(IEProperties.FACING_HORIZONTAL,te.facing);
        state = state.withProperty(((BlockTTBase)state.getBlock()).animProperty,1);
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.BLOCK);
        BlockPos blockpos = te.getPos();
        GlStateManager.translate(x,y,z);
        GlStateManager.translate(translation.x,translation.y,translation.z);
        GlStateManager.translate(-blockpos.getX(),-blockpos.getY(),-blockpos.getZ());
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        blockrendererdispatcher.getBlockModelRenderer().renderModel(te.getWorld(), blockrendererdispatcher.getModelForState(state), state, blockpos, bufferbuilder, false, MathHelper.getPositionRandom(te.getOrigin()));
        tessellator.draw();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

}
