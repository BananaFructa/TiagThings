package BananaFructa.TTIEMultiblocks.Renderers;

import BananaFructa.TTIEMultiblocks.IECopy.BlockTTBase;
import BananaFructa.TTIEMultiblocks.TileEntities.AnimatedOBJTileEntity;
import BananaFructa.TTIEMultiblocks.Utils.CosSinTable;
import BananaFructa.TTIEMultiblocks.Utils.MultiblockAnimation.AnimationGroup;
import blusunrize.immersiveengineering.api.IEProperties;
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

import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;
import java.util.*;

// this is not the best :(
public class AnimatedOBJTileRenderer<T extends AnimatedOBJTileEntity<?,?>> extends TileEntitySpecialRenderer<T> {

    @Override
    public void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (te.isDummy()) return;
        float deltaTime = partialTicks - te.lastPartialTick;
        if (deltaTime < 0) deltaTime += 1;
        deltaTime /= 20;
        if (te.currentAnimation != null) {
            te.time += deltaTime * te.speed;
            if (te.time > te.currentAnimation.totalTime) {
                te.time -= te.currentAnimation.totalTime;
            }
            for (AnimationGroup subGroup : te.modelGroups.subGroups) {
                renderMovingParts(subGroup, te, x, y, z, te.time, new Vector3f(), new Stack<>());
            }
        } else {
            te.time = 0;
        }
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        te.lastPartialTick = partialTicks;
    }



    private void renderMovingParts(AnimationGroup group, T te, double x, double y, double z, float t,Vector3f totalTranslation,Stack<Tuple<Vector3f,Vector3f>> rotationOperations) {
        Vector3f pivot = group.pivot;
        Vector3f translation = new Vector3f();
        Vector3f rotation = new Vector3f();
        if (te.currentAnimation.interpolationJobs.containsKey(group.name)) { // if it is not here it means that the component has no independent movement
            InterpolationJob.Keyframe key = te.currentAnimation.interpolationJobs.get(group.name).getCurrentFrame(t);
            translation = key.translation;
            rotation = key.rotation;
        }

        Vector3f newTranslation = new Vector3f();
        newTranslation.add(totalTranslation,translation);
        rotationOperations.push(new Tuple<>(rotation,pivot));

        if (group.isFinal()) {
            renderSection(te,x,y,z,group.multiblockPosId,newTranslation, rotationOperations);
        } else {
            for (AnimationGroup subGroups : group.subGroups) {
                renderMovingParts(subGroups, te,x,y,z,t,newTranslation, rotationOperations);
            }
        }
        rotationOperations.pop();
    }

    private Vector3f translationForRotationX(float angle, EnumFacing.Axis axis, Vector3f pivot) {
        float x0,y0;
        x0 = pivot.z;
        y0 = pivot.y;
        float cos = (float)Math.cos(angle);
        float sin = (float)Math.sin(angle);
        float x1 = x0*cos - y0*sin;
        float y1 = y0*cos + x0*sin;
        float deltaX = x0-x1;
        float deltaY = y0-y1;
        return new Vector3f(0,deltaY,deltaX);

    }
    private Vector3f translationForRotationY(float angle, EnumFacing.Axis axis, Vector3f pivot) {
        float x0,y0;
        x0 = pivot.x;
        y0 = pivot.z;
        float cos = (float)Math.cos(angle);
        float sin = (float)Math.sin(angle);
        float x1 = x0*cos - y0*sin;
        float y1 = y0*cos + x0*sin;
        float deltaX = x0-x1;
        float deltaY = y0-y1;
        return new Vector3f(deltaX,0,deltaY);
    }
    private Vector3f translationForRotationZ(float angle, EnumFacing.Axis axis, Vector3f pivot) {
        float x0,y0;
        x0 = pivot.x;
        y0 = pivot.y;
        float cos = (float)Math.cos(angle);
        float sin = (float)Math.sin(angle);
        float x1 = x0*cos - y0*sin;
        float y1 = y0*cos + x0*sin;
        float deltaX = x0-x1;
        float deltaY = y0-y1;
        return new Vector3f(deltaX,deltaY,0);
    }

    static Matrix3f[] vectorTransform = new Matrix3f[] {
            new Matrix3f(
                    1,0,0,
                    0,1,0,
                    0,0,1
            ),
            new Matrix3f(
                    -1,0,0,
                    0,1,0,
                    0,0,-1
            ),
            new Matrix3f(
                    0,0,1,
                    0,1,0,
                    -1,0,0
            ),
            new Matrix3f(
                    0,0,-1,
                    0,1,0,
                    1,0,0
            )
    };

    private Vector3f transformVectorToFace(Vector3f vec, EnumFacing facing) {
        //float x;
        vec = new Vector3f(vec);
        vectorTransform[facing.ordinal()-2].transform(vec);
        return vec;
        /*switch (facing){
            case WEST:
                x = vec.x;
                vec.x = vec.z;
                vec.z = -x;
                return vec;
            case EAST:
                x = vec.x;
                vec.x = -vec.z;
                vec.z = x;
                return vec;
            case SOUTH:
                vec.x = -vec.x;
                vec.z = -vec.z;
                return vec;
            case NORTH:
            default:
                return vec;
        }*/
    }

    static float[] rotationTransform = new float[] {
            1,
            1,
            -1,
            -1
    };

    private Vector3f transformRotationCompensionToFace(Vector3f vec, EnumFacing facing) {
        vec = new Vector3f(vec);
        vec.scale(rotationTransform[facing.ordinal()-2]);
        /*switch (facing){
            case WEST:
            case EAST:
                vec.x = -vec.x;
                vec.z = -vec.z;
                return vec;
            case NORTH:
            case SOUTH:
            default:
                return vec;
        }*/
        return vec;
    }

    static Vector3f[] pivotOffsets = new Vector3f[]{
            new Vector3f(0,0,0),
            new Vector3f(1,0,1),
            new Vector3f(0,0,1),
            new Vector3f(1,0,0)
    };

    static Vector3f[] pivotMirrorOffsets = new Vector3f[] {
            new Vector3f(),
            new Vector3f(),
            new Vector3f(),
            new Vector3f(),
            new Vector3f(-1,0,0),
            new Vector3f(-1,0,0),
            new Vector3f(0,0,-1),
            new Vector3f(0,0,-1)
    };

    static float angle2rad = (float)Math.PI/180.0f;

    private void renderSection(T te, double x, double y, double z, int id, Vector3f translation, Stack<Tuple<Vector3f,Vector3f>> rotationOperations) {
        boolean mirrored = te.mirrored;
        int mirroredI = (mirrored ? 1 : 0);
        id = 1 + (id-1)*2 + mirroredI;
        IBlockState state = te.baseState.withProperty(IEProperties.FACING_HORIZONTAL,te.facing);
        state = state.withProperty(((BlockTTBase)state.getBlock()).animProperty,id);
        EnumFacing facing = te.facing;
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        boolean northSouth = facing == EnumFacing.NORTH || facing ==EnumFacing.SOUTH;
        boolean eastWest = facing == EnumFacing.WEST || facing ==EnumFacing.EAST;
        boolean mirroredNorthSouth = mirrored && northSouth;
        boolean mirroredEastWest = mirrored && eastWest;
        float mirroredF = -mirroredI*2 + 1;
        float factorX = (mirroredNorthSouth ? -1 : 1);
        float factorZ = (mirroredEastWest ? -1 : 1);
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.BLOCK);
        BlockPos blockpos = te.getPos();
        GlStateManager.translate(x,y,z);
        translation = transformVectorToFace(translation,facing);
        GlStateManager.translate(translation.x* factorX,translation.y,translation.z*factorZ);
        for (Tuple<Vector3f,Vector3f> operation : rotationOperations) {
            Vector3f rot = transformRotationCompensionToFace(transformVectorToFace(operation.getFirst(),facing),facing);
            Vector3f pivot = transformVectorToFace(operation.getSecond(),facing);
            pivot.add(pivotOffsets[facing.ordinal()-2]);
            pivot.add(pivotMirrorOffsets[facing.ordinal()-2 + mirroredI*4]);
            Vector3f t = translationForRotationZ(-rot.z*angle2rad, EnumFacing.Axis.Z,pivot);
            t.add(translationForRotationY(rot.y*angle2rad,EnumFacing.Axis.Y,pivot));
            t.add(translationForRotationX(-rot.x*angle2rad, EnumFacing.Axis.X,pivot));
            GlStateManager.translate(t.x * factorX,t.y,t.z*factorZ);
        }
        for (Tuple<Vector3f,Vector3f> operation : rotationOperations) {
            Vector3f rot = transformVectorToFace(operation.getFirst(),facing);
            GlStateManager.rotate(-rot.z * factorX,0,0,1);
            GlStateManager.rotate(-rot.y * mirroredF,0,1,0);
            GlStateManager.rotate(-rot.x * factorZ,1,0,0);
        }
        GlStateManager.translate(-blockpos.getX(),-blockpos.getY(),-blockpos.getZ());
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        blockrendererdispatcher.getBlockModelRenderer().renderModel(te.getWorld(), blockrendererdispatcher.getModelForState(state), state, blockpos, bufferbuilder, false, MathHelper.getPositionRandom(te.getOrigin()));
        tessellator.draw();

        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }
}
