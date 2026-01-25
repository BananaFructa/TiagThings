package BananaFructa.Uem;

import BananaFructa.UnecologicalMethods.Config;
import BananaFructa.UnecologicalMethods.DrainTank;
import BananaFructa.UnecologicalMethods.DrainTileEntity;
import blusunrize.immersiveengineering.api.IEEnums;
import blusunrize.immersiveengineering.api.IEEnums.SideConfig;
import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;

import java.util.*;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class DrainFluidPlacer extends DrainTileEntity {

    private static final List<String> restrictedFluids = new ArrayList<String>(){{
        add("tfc:fluid/fresh_water");
        add("tfc:fluid/salt_water");
        add("tfc:fluid/hot_water");
    }};

    private static final int maximumRange = 10;
    private FluidTank tankDFP;
    public TreeSet<Long> restrictedFluidSources = new TreeSet<>();
    public DrainFluidPlacer() {
        super();
        this.tankDFP = new FluidTank(2000);
    }

    public void func_73660_a() {

        if (world.isRemote || world.getBlockState(getPos()).getBlock() == Blocks.AIR) return;
        tickCount++;

        if (tickCount % (Config.tickFrequency * (tooManyIterations ? 5 : 1)) != 0) return;

        y = getPos().getY();

        List<Long> toRemove = new ArrayList<>();

        for (Long l : restrictedFluidSources) {
            BlockPos pos = getBlockPos(l);
            IBlockState block = this.getWorld().getBlockState(pos);
            if (block.getBlock() instanceof BlockLiquid || block.getBlock() instanceof IFluidBlock) { // TODO: fix gets removed when loads
                if (block.getValue(BlockLiquid.LEVEL) == 0) {
                    FluidStack extracted = tankDFP.drain(10, false);
                    if (extracted != null) {
                        if (extracted.amount != 10 || extracted.getFluid().getBlock() != block.getBlock()) {
                            toRemove.add(l);
                            killSource(l);
                        } else {
                            tankDFP.drain(10, true);
                        }
                    } else {
                        toRemove.add(l);
                        killSource(l);
                    }
                } else {
                    toRemove.add(l);
                }
            } else {
                toRemove.add(l);
            }
        }

        toRemove.forEach(restrictedFluidSources::remove);

        if (getWorld().isBlockIndirectlyGettingPowered(getPos()) != 0 || tankDFP.getFluidAmount() < 1000) return;
        if (!restrictedFluids.contains(tankDFP.getFluid().getFluid().getBlock().getRegistryName().toString())) return;

        for (int i = 0;i < 4;i++) {
            BlockPos pos = getPos().add(dir[i]);

            if (isValid(pos)) positions.add(convertPosition(pos.getX(),pos.getZ()));
        }

        int iteration = 0;

        do {

            BlockPos pos = checkForValid();

            if (pos != null) {
                polluteBlock(pos);
                tankDFP.drain(1000,true);
                break;
            }

            iteration++;

            if (iteration >= maximumRange) {
                break;
            }

        } while (advanceSearch());

        tooManyIterations = false;

        positions.clear();
    }

    private void killSource(Long l) {
        world.setBlockState(getBlockPos(l),Blocks.AIR.getDefaultState());
    }

    public void onDestroy() {
        for (Long l : restrictedFluidSources) {
            BlockPos pos = getBlockPos(l);
            IBlockState block = this.getWorld().getBlockState(pos);
            if (block.getBlock() instanceof BlockLiquid || block.getBlock() instanceof IFluidBlock) {
                if (block.getValue(BlockLiquid.LEVEL) == 0) {
                    killSource(l);
                }
            }
        }
    }

    protected boolean isWater(BlockPos pos) {
        IBlockState block = this.getWorld().getBlockState(pos);
        return block.getBlock() == Blocks.AIR || (tankDFP.getFluid() != null && block.getBlock() == tankDFP.getFluid().getFluid().getBlock() && block.getValue(BlockLiquid.LEVEL) != 0);
    }

    @Override
    protected boolean isWater(Block block) {
        return block == Blocks.AIR;
    }

    @Override
    protected boolean isValid(BlockPos pos) {
        if (!world.getWorldBorder().contains(pos)) return false;
        Block block = getWorld().getBlockState(pos).getBlock();
        return (isWater(pos) || (block == tankDFP.getFluid().getFluid().getBlock()));
    }

    protected void polluteBlock(BlockPos pos) {
        restrictedFluidSources.add(convertPosition(pos.getX(),pos.getZ()));
        markDirty();
        getWorld().setBlockState(pos, tankDFP.getFluid().getFluid().getBlock().getDefaultState());
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        nbt.setTag("tank",tankDFP.writeToNBT(new NBTTagCompound()));
        nbt.setLong("sourceSize",restrictedFluidSources.size());
        int i = 0;
        for (Long l : restrictedFluidSources) {
            nbt.setLong("source-"+i,l);
            i++;
        }
        super.writeCustomNBT(nbt, descPacket);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        tankDFP.readFromNBT((NBTTagCompound) nbt.getTag("tank"));
        long s = nbt.getLong("sourceSize");
        for (int i = 0;i < s;i++) {
            restrictedFluidSources.add(nbt.getLong("source-"+i));
        }
        super.readCustomNBT(nbt, descPacket);
    }

    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability != CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || facing != null && this.sideConfig[facing.ordinal()] != 0 ? super.hasCapability(capability, facing) : true;
    }

    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability != CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || facing != null && this.sideConfig[facing.ordinal()] != 0 ? super.getCapability(capability, facing) : (T)this.tankDFP;
    }
}
