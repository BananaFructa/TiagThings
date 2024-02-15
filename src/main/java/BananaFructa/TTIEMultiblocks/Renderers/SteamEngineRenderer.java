package BananaFructa.TTIEMultiblocks.Renderers;

import BananaFructa.TTIEMultiblocks.IECopy.BlockTTBase;
import BananaFructa.TTIEMultiblocks.TTBlockTypes_MetalMultiblock_3;
import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntitySteamEngine;
import BananaFructa.TTIEMultiblocks.Utils.MultiblockAnimation.AnimationGroup;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3f;
import java.io.IOException;

public class SteamEngineRenderer extends AnimatedOBJTileRenderer<TileEntitySteamEngine> {
}
