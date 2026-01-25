package BananaFructa.TTIEMultiblocks.Renderers;

import BananaFructa.TTIEMultiblocks.IECopy.BlockTTBase;
import BananaFructa.TTIEMultiblocks.TTBlockMetalMultiblocks;
import BananaFructa.TTIEMultiblocks.TTBlockTypes_MetalMultiblock;
import BananaFructa.TTIEMultiblocks.TTIEContent;
import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.client.render.TileRenderMetalPress;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMetalPress;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import javax.vecmath.Vector3f;

public class NewMetalPressRenderer extends TileRenderMetalPress {

    @Override
    public void render(TileEntityMetalPress te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        if (te.isDummy()) return;
        renderSection(te,x,y,z);
    }


    private void renderSection(TileEntityMetalPress te, double x, double y, double z) {
        IBlockState state = TTIEContent.ttBlockMetalMultiblock.getStateFromMeta(TTBlockTypes_MetalMultiblock.COAL_BOILER_CHILD.getMeta());
        state = state.withProperty(((BlockTTBase)state.getBlock()).animProperty,1);
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.BLOCK);
        BlockPos blockpos = te.getPos();
        GlStateManager.translate(x,y,z);
        GlStateManager.translate(-blockpos.getX(),-blockpos.getY(),-blockpos.getZ());
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        blockrendererdispatcher.getBlockModelRenderer().renderModel(te.getWorld(), blockrendererdispatcher.getModelForState(state), state, blockpos, bufferbuilder, false, MathHelper.getPositionRandom(te.getOrigin()));
        tessellator.draw();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }
}
