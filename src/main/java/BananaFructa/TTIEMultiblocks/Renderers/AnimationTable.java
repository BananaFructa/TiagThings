package BananaFructa.TTIEMultiblocks.Renderers;

import BananaFructa.TTIEMultiblocks.IECopy.BlockTTBase;
import BananaFructa.TTIEMultiblocks.Utils.MultiblockAnimation.AnimationGroup;
import blusunrize.immersiveengineering.api.IEProperties;
import com.creativemd.creativecore.common.utils.math.vec.Vec;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;
import java.util.HashMap;
import java.util.Stack;

public class AnimationTable {

    public float animationLength;
    public int resolution;
    public TranslationRotation[][][] translationRotation;
    public int numElems;

    public AnimationTable(int numElems, float animationLength, int resolution, HashMap<String,InterpolationJob> interpolationJobs, AnimationGroup animationGroups) {
        translationRotation = new TranslationRotation[4][resolution][numElems*2];
        this.resolution = resolution;
        this.numElems = numElems;
        this.animationLength = animationLength;
        for (int mirrored = 0; mirrored <= 1;mirrored++) {
            for (int face = 0;face < 4;face++) {
                EnumFacing facing = EnumFacing.values()[face + 2];
                for (int i = 0; i < resolution; i++) {
                    float t = (float)i/resolution;
                    calculateTrFor((mirrored == 0 ? false : true),facing,t*animationLength,animationGroups,interpolationJobs,new Vector3f(),new Stack<>(),translationRotation[face][i]);
                }
            }
        }
    }

    private void calculateTrFor(boolean mirrored, EnumFacing facing, float t, AnimationGroup group, HashMap<String,InterpolationJob> interpolationJobs, Vector3f totalTranslation, Stack<Tuple<Vector3f,Vector3f>> rotationOperations, TranslationRotation[] required) {
        Vector3f pivot = group.pivot;
        Vector3f translation = new Vector3f();
        Vector3f rotation = new Vector3f();
        if (interpolationJobs.containsKey(group.name)) { // if it is not here it means that the component has no independent movement
            InterpolationJob.Keyframe key = interpolationJobs.get(group.name).getCurrentFrame(t);
            translation = key.translation;
            rotation = key.rotation;
        }

        Vector3f newTranslation = new Vector3f();
        newTranslation.add(totalTranslation,translation);
        rotationOperations.push(new Tuple<>(rotation,pivot));

        if (group.isFinal()) {
            int mirroredI = (mirrored ? 1 : 0);
            int id = 1 + (group.multiblockPosId-1)*2 + mirroredI;
            boolean northSouth = facing == EnumFacing.NORTH || facing ==EnumFacing.SOUTH;
            boolean eastWest = facing == EnumFacing.WEST || facing ==EnumFacing.EAST;
            boolean mirroredNorthSouth = mirrored && northSouth;
            boolean mirroredEastWest = mirrored && eastWest;
            float mirroredF = -mirroredI*2 + 1;
            float factorX = (mirroredNorthSouth ? -1 : 1);
            float factorZ = (mirroredEastWest ? -1 : 1);
            Vector3f finalTranslation = new Vector3f(newTranslation.x,newTranslation.y,newTranslation.z);
            finalTranslation = transformVectorToFace(finalTranslation,facing);
            finalTranslation.x *= factorX;
            finalTranslation.z *= factorZ;
            Vector3f finalRotation = new Vector3f();
            for (Tuple<Vector3f,Vector3f> operation : rotationOperations) {
                Vector3f rot = transformRotationCompensionToFace(transformVectorToFace(operation.getFirst(),facing),facing,mirrored);
                Vector3f p = transformVectorToFace(operation.getSecond(),facing);
                p.add(pivotOffsets[facing.ordinal()-2]);
                p.add(pivotMirrorOffsets[facing.ordinal()-2 + mirroredI*4]);
                Vector3f rotTranslation = new Vector3f();
                rotTranslation.add(translationForRotationZ(-rot.z*angle2rad, EnumFacing.Axis.Z,p));
                rotTranslation.add(translationForRotationY(rot.y*angle2rad,EnumFacing.Axis.Y,p));
                rotTranslation.add(translationForRotationX(-rot.x*angle2rad, EnumFacing.Axis.X,p));
                rotTranslation.x *= factorX;
                rotTranslation.z *= factorZ;
                finalTranslation.add(rotTranslation);
            }
            for (Tuple<Vector3f,Vector3f> operation : rotationOperations) {
                Vector3f rot = transformVectorToFace(operation.getFirst(),facing);
                finalRotation.add(new Vector3f(-rot.x * factorZ,-rot.y * mirroredF,-rot.z * factorX));
            }
            TranslationRotation tr = new TranslationRotation(finalTranslation,finalRotation);
            required[id-1] = tr;
        } else {
            for (AnimationGroup subGroups : group.subGroups) {
                calculateTrFor(mirrored,facing,t,subGroups,interpolationJobs,newTranslation,rotationOperations,required);
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
    private Vector3f transformVectorToFace(Vector3f vec, EnumFacing facing) {
        float x;
        vec = new Vector3f(vec);
        //vectorTransform[facing.ordinal()-2].transform(vec);
        //return vec;
        switch (facing){
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
        }
    }

    private Vector3f transformRotationCompensionToFace(Vector3f vec, EnumFacing facing, boolean mirrored) {
        vec = new Vector3f(vec);
        //vec.scale(rotationTransform[facing.ordinal()-2]);
        switch (facing){
            case WEST:
                vec.x = -vec.x;
                return vec;
            case EAST:
                vec.x = -vec.x;
                //vec.z = -vec.z;
                return vec;
            case NORTH:
                vec.x = -vec.x;
                return vec;
            case SOUTH:
                vec.x = -vec.x;
            default:
                return vec;
        }
        //return vec;
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

}
