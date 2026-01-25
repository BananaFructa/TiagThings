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
    public float time;
    public float speed = 1;
    public float lastPartialTick = 0;
    public String currentAnimationName = "NULL";
    @Override
    public void update() {
        super.update();
    }

    public AnimatedOBJTileEntity(SimplifiedMultiblockClass instance, int energyStorage, boolean redstoneControl, List<R> recipes, IBlockState baseState) {
        super(instance, energyStorage, redstoneControl, recipes);
        this.baseState = baseState;
    }

    public void setAnimation(String name) {
        currentAnimationName = name;
    }

    public void setAnimationSpeed(float f) {
        if (Float.isInfinite(f)) return;
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
        speed = pkt.getNbtCompound().getFloat("animationSpeed");
    }
}
