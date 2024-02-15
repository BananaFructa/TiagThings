package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.Renderers.AnimationFile;
import BananaFructa.TTIEMultiblocks.Renderers.InterpolationJob;
import BananaFructa.TTIEMultiblocks.Renderers.MultiblockAnimation;
import BananaFructa.TTIEMultiblocks.Utils.MultiblockAnimation.AnimationGroup;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockClass;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.TTMain;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nullable;
import javax.vecmath.Vector3f;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AnimatedOBJTileEntity<M extends SimplifiedTileEntityMultiblockMetal<M,R>,R extends SimplifiedMultiblockRecipe> extends SimplifiedTileEntityMultiblockMetal<M,R> {

    public IBlockState baseState;
    public AnimationGroup modelGroups;
    public int animIdMax;
    public float time;
    public float speed = 1;
    public float lastPartialTick = 0;

    public HashMap<String, MultiblockAnimation> animations = new HashMap<>();
    public MultiblockAnimation currentAnimation;
    String currentAnimationName = "NULL";
    @Override
    public void update() {
        super.update();
    }

    public AnimatedOBJTileEntity(SimplifiedMultiblockClass instance, int energyStorage, boolean redstoneControl, List<R> recipes, IBlockState baseState, AnimationGroup modelGroups, int animIdMax) {
        super(instance, energyStorage, redstoneControl, recipes);
        this.baseState = baseState;
        this.modelGroups = modelGroups;
        this.animIdMax = animIdMax;
    }

    public void loadAnimation(String animationName) throws IOException {
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
            animations.put(animName,new MultiblockAnimation(interpolationJobs));
        }
    }

    public void setAnimation(String name) {
        currentAnimationName = name;
    }

    public void setAnimationSpeed(float f) {
        speed = f;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        this.writeCustomNBT(nbtTagCompound, true);
        nbtTagCompound.setString("animationName",currentAnimationName);
        nbtTagCompound.setFloat("animationSpeed",speed);
        return new SPacketUpdateTileEntity(getPos(),1,nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net,pkt);
        currentAnimationName = pkt.getNbtCompound().getString("animationName");
        currentAnimation = animations.getOrDefault(currentAnimationName, null);
        speed = pkt.getNbtCompound().getFloat("animationSpeed");
    }
}
