package BananaFructa.TTIEMultiblocks.Renderers;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityClarifier;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityRocketScaffold;
import BananaFructa.TiagThings.TTMain;
import blusunrize.immersiveengineering.client.ClientUtils;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import static org.lwjgl.opengl.GL11.*;

public class ScaffoldRenderer extends TileEntitySpecialRenderer<TileEntityRocketScaffold> {

    IBakedModel bakedModel;
    private static final ResourceLocation resourceLocation = new ResourceLocation(TTMain.modId,"models/block/rocket_scaffold.obj");

    public ScaffoldRenderer() {
        bakedModel = getModelBaked(resourceLocation);
    }

    @Override
    public void render(TileEntityRocketScaffold te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        if (!te.isDummy()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            renderModel(te, 0,0,0, Tessellator.getInstance().getBuffer());
            Tessellator.getInstance().draw();
            GlStateManager.translate(-x, -y, -z);
            GlStateManager.popMatrix();
        }
    }

    public void renderModel(TileEntityRocketScaffold te, double xOffset, double yOffset, double zOffset, BufferBuilder buffer) {
        GlStateManager.translate(xOffset, yOffset, zOffset);

        World world = te.getWorld();
        BlockPos pos = te.getPos();

        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
                world,
                bakedModel,
                world.getBlockState(pos),
                pos,
                buffer,
                true

        );

        GlStateManager.translate(-xOffset, -yOffset, -zOffset);

    }

    private IBakedModel getModelBaked(ResourceLocation modelLoc) {
        IModel model;
        try {
            model = OBJLoader.INSTANCE.loadModel(modelLoc);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        IBakedModel bakedModel = model.bake(
                model.getDefaultState(),
                DefaultVertexFormats.BLOCK,
                (resourceLocation) -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(resourceLocation.toString())
        );

        return bakedModel;
    }

    @Override
    public boolean isGlobalRenderer(TileEntityRocketScaffold te) {
        return true;
    }
}
