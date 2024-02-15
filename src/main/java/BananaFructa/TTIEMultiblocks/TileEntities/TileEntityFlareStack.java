package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.TTMain;
import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileEntityFlareStack extends SimplifiedTileEntityMultiblockMetal<TileEntityFlareStack, SimplifiedMultiblockRecipe> {

    public static List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
       add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.butane,1)},new ItemStack[0],new FluidStack[0],0,1));
    }};

    boolean activeClient = false;
    float cx,cy,cz;

    boolean wasBurning = false;

    public TileEntityFlareStack() {
        super(TTIEContent.flareStackMultiblock, 0, false,recipes);
    }

    @Override
    public void update() {
        super.update();
        if (world.isRemote && activeClient) {
            for (int i = 0;i < 5;i++) world.spawnParticle(EnumParticleTypes.SMOKE_LARGE,true,cx,cy,cz,0,world.rand.nextFloat() * 1,0);
        }
        if (world.isRemote || isDummy()) return;
        boolean burning = isBurning();
        if (wasBurning ^ burning) world.notifyBlockUpdate(getPos(),world.getBlockState(getPos()),world.getBlockState(getPos()),2);
        wasBurning = burning;
    }

    @Override
    public void initPorts() {
        int inputTank = registerFluidTank(1000);
        registerFluidPort(7,inputTank, PortType.INPUT,EnumFacing.NORTH);
    }

    private boolean isBurning() {
        return isCurrentlyDoingRecipe(recipes.get(0));
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        this.writeCustomNBT(nbtTagCompound, true);
        nbtTagCompound.setBoolean("active",isCurrentlyDoingRecipe(recipes.get(0)));
        BlockPos posUpper = getBlockPosForPos(166);
        BlockPos posBack = getBlockPosForPos(1);
        BlockPos posRight = getBlockPosForPos(5);
        float x = (posUpper.getX() + posBack.getX())/2.0f + (posBack.getX() - posUpper.getX()) * 0.2f + (-posUpper.getX() + posRight.getX()) * 0.2f;
        float z = (posUpper.getZ() + posBack.getZ())/2.0f + (posBack.getZ() - posUpper.getZ()) * 0.2f + (-posUpper.getZ() + posRight.getZ()) * 0.5f;
        float y = posUpper.getY();
        nbtTagCompound.setFloat("x",x);
        nbtTagCompound.setFloat("y",y);
        nbtTagCompound.setFloat("z",z);
        return new SPacketUpdateTileEntity(getPos(),1,nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        activeClient = pkt.getNbtCompound().getBoolean("active");
        cx = pkt.getNbtCompound().getFloat("x");
        cy = pkt.getNbtCompound().getFloat("y");
        cz = pkt.getNbtCompound().getFloat("z");
    }
}
