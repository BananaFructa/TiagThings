package BananaFructa.TTIEMultiblocks;

import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import BananaFructa.TiagThings.Utils;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import pl.pabilo8.immersiveintelligence.api.rotary.CapabilityRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryStorage;

import javax.annotation.Nullable;

public class SignalSourceTileEntity extends TileEntityIEBase implements IEnergyStorage, ITickable {

    public int energy = 0;
    public boolean good = false;
    public int maxEnergy = 2;
    public EnumFacing facing;
    public int signalLevel = 15;

    public SignalSourceTileEntity() {
        super();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        IEUtils.notifyClientUpdate(world,pos);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY && facing == EnumFacing.UP) return true;
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY && facing == EnumFacing.UP) return (T)this;
        return super.getCapability(capability, facing);
    }


    @Override
    public void readCustomNBT(NBTTagCompound nbtTagCompound, boolean b) {
        energy = nbtTagCompound.getInteger("energy");
        good = nbtTagCompound.getBoolean("good");
        signalLevel = nbtTagCompound.getInteger("signalLevel");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbtTagCompound, boolean b) {
        nbtTagCompound.setInteger("energy",energy);
        nbtTagCompound.setBoolean("good",good);
        nbtTagCompound.setInteger("signalLevel",signalLevel);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;

        int energyReceived = Math.min(maxEnergy - energy, maxReceive);
        if (!simulate) {
            IEUtils.notifyClientUpdate(world,pos);
            energy += energyReceived;
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;

        int energyExtracted = Math.min(energy, maxExtract);
        if (!simulate) {
            IEUtils.notifyClientUpdate(world,pos);
            energy -= energyExtracted;
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return maxEnergy;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        this.writeCustomNBT(nbtTagCompound, true);
        return new SPacketUpdateTileEntity(getPos(),1,nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net,pkt);
    }

    @Override
    public void update() {
        if (energy >= 1) {
            energy -= 1;
            markDirty();

            boolean update = !good;

            good = true;

            if (update) {
                IEUtils.notifyClientUpdate(world, pos);
                world.notifyNeighborsOfStateChange(getPos(),world.getBlockState(getPos()).getBlock(),true);
            }
        } else {
            boolean update = good;
            good = false;
            if (update) {
                IEUtils.notifyClientUpdate(world, pos);
                world.notifyNeighborsOfStateChange(getPos(),world.getBlockState(getPos()).getBlock(),true);
            }
        }
    }
}
