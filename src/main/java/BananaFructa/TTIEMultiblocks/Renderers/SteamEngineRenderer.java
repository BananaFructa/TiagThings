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
    public SteamEngineRenderer() {
        super(new AnimationGroup("bones",new Vector3f(0,0,0),
                new AnimationGroup("flywheele",new Vector3f(72,24.0f,21.0f),2),
                new AnimationGroup("transmission_arm",new Vector3f(0,0,0),
                        new AnimationGroup("lever",new Vector3f(46.75f,24,-8),4),
                        new AnimationGroup("horizontal_lever",new Vector3f(0,0,0),3)
                ),
                new AnimationGroup("transmission_arm2",new Vector3f(0,0,0),
                        new AnimationGroup("lever2",new Vector3f(64.5f,23.75f,5),5),
                        new AnimationGroup("bone",new Vector3f(0,0,0),1)
                ),
                new AnimationGroup("no_idea_what_these_are",new Vector3f(16,38,25),6)
        ),1000);
        try {
            loadAnimation("steamengine.mirrored.animation.json",6.0f,6);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
