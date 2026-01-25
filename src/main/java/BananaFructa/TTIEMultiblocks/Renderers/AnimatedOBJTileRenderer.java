package BananaFructa.TTIEMultiblocks.Renderers;

import BananaFructa.TTIEMultiblocks.IECopy.BlockTTBase;
import BananaFructa.TTIEMultiblocks.TileEntities.AnimatedOBJTileEntity;
import BananaFructa.TTIEMultiblocks.Utils.CosSinTable;
import BananaFructa.TTIEMultiblocks.Utils.MultiblockAnimation.AnimationGroup;
import BananaFructa.TiagThings.TTMain;
import blusunrize.immersiveengineering.api.IEProperties;
import com.creativemd.creativecore.common.utils.math.vec.Vec;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.animation.FastTESR;
import org.apache.commons.io.IOUtils;

import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

// this is not the best :(
public class AnimatedOBJTileRenderer<T extends AnimatedOBJTileEntity<?,?>> extends TileEntitySpecialRenderer<T> {

    public AnimationGroup animationGroups;
    public int resolution;
    public HashMap<String,AnimationTable> animations = new HashMap<>();

    public AnimatedOBJTileRenderer(AnimationGroup animationGroups,int resolution) {
        this.animationGroups = animationGroups;
        this.resolution = resolution;
    }

    @Override
    public void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (te.isDummy()) return;
        float deltaTime = partialTicks - te.lastPartialTick;
        if (deltaTime < 0) deltaTime += 1;
        deltaTime /= 20;
        String name = te.currentAnimationName;
        if (animations.containsKey(name)) {
            AnimationTable table = animations.get(name);
            te.time += deltaTime * te.speed;
            if (te.time > table.animationLength) {
                te.time -= table.animationLength;
            }
            int face = te.facing.ordinal()-2;
            int res = (int)((te.time/table.animationLength)*table.resolution)%table.resolution;
            TranslationRotation[][][] arrTable = table.translationRotation;
            for (int e = 1;e <= table.numElems;e++) {
                int id = 1 + (e-1)*2 + (te.mirrored ? 1 : 0);
                TranslationRotation tr = arrTable[face][res][id-1];
                renderSection(te,x,y,z,id,tr.getFirst(),tr.getSecond());
            }
        } else {
            te.time = 0;
        }
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        te.lastPartialTick = partialTicks;
    }

    private void renderSection(T te, double x, double y, double z, int id, Vector3f translation, Vector3f rotation) {
        IBlockState state = te.baseState.withProperty(IEProperties.FACING_HORIZONTAL,te.facing);
        state = state.withProperty(((BlockTTBase)state.getBlock()).animProperty,id);
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.BLOCK);
        BlockPos blockpos = te.getPos();
        GlStateManager.translate(x,y,z);
        GlStateManager.translate(translation.x,translation.y,translation.z);
        GlStateManager.rotate(rotation.x,1,0,0);
        GlStateManager.rotate(rotation.y,0,1,0);
        GlStateManager.rotate(rotation.z,0,0,1);
        GlStateManager.translate(-blockpos.getX(),-blockpos.getY(),-blockpos.getZ());
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        blockrendererdispatcher.getBlockModelRenderer().renderModel(te.getWorld(), blockrendererdispatcher.getModelForState(state), state, blockpos, bufferbuilder, false, MathHelper.getPositionRandom(te.getOrigin()));
        tessellator.draw();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    public void loadAnimation(String animationName,float animationLength,int numElems) throws IOException {
        InputStream stream =  getClass().getClassLoader().getResourceAsStream("assets/"+ TTMain.modId+"/animations/"+animationName);
        String result = IOUtils.toString(stream, StandardCharsets.UTF_8);
        Gson g = new Gson();
        AnimationFile f = g.fromJson(result,new TypeToken<AnimationFile>(){}.getType());
        for (String animName : f.animations.keySet()) {
            Map<String,Map<String, Map<String, List<Double>>>> bones = f.animations.get(animName).bones;
            HashMap<String, InterpolationJob> interpolationJobs = new HashMap<>();
            for (String group : bones.keySet()) {
                Map<String,Map<String, List<Double>>> mapGroup = bones.get(group);
                boolean hasPosition = mapGroup.containsKey("position");
                boolean hasRotation = mapGroup.containsKey("rotation");
                List<InterpolationJob.Keyframe> keyframes = new ArrayList<>();
                if (hasPosition) {
                    for (String t : mapGroup.get("position").keySet()) {
                        // Blockbench to minecraft coordinate space
                        float px = (float)mapGroup.get("position").get(t).get(0).doubleValue()/16;
                        float py = (float)mapGroup.get("position").get(t).get(1).doubleValue()/16;
                        float pz = (float)mapGroup.get("position").get(t).get(2).doubleValue()/16;
                        keyframes.add(new InterpolationJob.Keyframe(Float.parseFloat(t),new Vector3f(px,py,pz),new Vector3f()));
                    }
                }
                if (hasRotation) {
                    int index = 0;
                    for (String t : mapGroup.get("rotation").keySet()) {
                        float rx = (float)mapGroup.get("rotation").get(t).get(0).doubleValue();
                        float ry = (float)mapGroup.get("rotation").get(t).get(1).doubleValue();
                        float rz = (float)mapGroup.get("rotation").get(t).get(2).doubleValue();
                        Vector3f rotation = new Vector3f(rx,ry,rz);
                        if (!hasPosition) keyframes.add(new InterpolationJob.Keyframe(Float.parseFloat(t),new Vector3f(),rotation));
                        else keyframes.get(index++).rotation = rotation;
                    }
                }
                interpolationJobs.put(group,new InterpolationJob(keyframes));
            }
            animations.put(animName,new AnimationTable(numElems,animationLength, resolution,interpolationJobs,animationGroups));
        }
    }
}
