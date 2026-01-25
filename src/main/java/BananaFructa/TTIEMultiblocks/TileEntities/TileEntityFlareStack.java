package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.Particles.FlameParticle;
import BananaFructa.TTIEMultiblocks.Particles.SmokeParticle;
import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.TTMain;
import BananaFructa.TiagThings.Utils;
import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;

public class TileEntityFlareStack extends SimplifiedTileEntityMultiblockMetal<TileEntityFlareStack, SimplifiedMultiblockRecipe> {

    public static List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
       add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.butane,1)},new ItemStack[0],new FluidStack[0],0,1));
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.hySulfide,1)},new ItemStack[0],new FluidStack[0],0,1));
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:hydrogen>"),1)},new ItemStack[0],new FluidStack[0],0,1));
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
            for (int i = 0;i < 2;i++) Minecraft.getMinecraft().effectRenderer.addEffect(new SmokeParticle(world,cx +0.5 + (world.rand.nextFloat() - 0.5)*0.3,cy+1+ (world.rand.nextFloat() - 0.5)*0.3,cz+0.5+ (world.rand.nextFloat() - 0.5)*0.3,world.rand.nextFloat() * 0.05,world.rand.nextFloat() * 0.1 +0.2,world.rand.nextFloat() * 0.05,(int)(10*20*(0.5+world.rand.nextInt(2))),2+world.rand.nextFloat()*2));
            for (int i = 0;i < 5;i++) Minecraft.getMinecraft().effectRenderer.addEffect(new FlameParticle(world,cx +0.5 + (world.rand.nextFloat() - 0.5)*0.3,cy+1+ (world.rand.nextFloat() - 0.5)*0.3,cz+0.5+ (world.rand.nextFloat() - 0.5)*0.3,world.rand.nextFloat() * 0.05,world.rand.nextFloat() * 0.1 +0.2,world.rand.nextFloat() * 0.05,(int)(20*(0.5+world.rand.nextInt(2))),3+world.rand.nextFloat()*2));
            //for (int i = 0;i < 5;i++) world.spawnParticle(EnumParticleTypes.SMOKE_LARGE,true,cx,cy,cz,0,world.rand.nextFloat() * 1,0);
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

        for (SimplifiedMultiblockRecipe recipe : recipes) {
            if (isCurrentlyDoingRecipe(recipe)) return true;
        }

        return false;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        this.writeCustomNBT(nbtTagCompound, true);
        nbtTagCompound.setBoolean("active",isBurning());
        BlockPos posUpper = getBlockPosForPos(166);
        Vector3f pos = new Vector3f(posUpper.getX(),posUpper.getY(),posUpper.getZ());
        switch (facing) {
            case EAST:
                pos.add(new Vector3f(-0.25f,0,0));
                break;
            case SOUTH:
                pos.add(new Vector3f(0,0,-0.25f));
                break;
            case NORTH:
                pos.add(new Vector3f(0,0,0.25f));
                break;
            case WEST:
                pos.add(new Vector3f(0.25f,0,0));
                break;
        }
        nbtTagCompound.setFloat("x",pos.x);
        nbtTagCompound.setFloat("y",pos.y);
        nbtTagCompound.setFloat("z",pos.z);
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
