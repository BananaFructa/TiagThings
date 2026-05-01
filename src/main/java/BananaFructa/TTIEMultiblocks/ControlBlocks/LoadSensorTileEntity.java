package BananaFructa.TTIEMultiblocks.ControlBlocks;

import BananaFructa.TTIEMultiblocks.PowerNetworkInfo.ModularList;
import BananaFructa.TTIEMultiblocks.PowerNetworkInfo.NetworkElement;
import BananaFructa.TTIEMultiblocks.PowerRework.TransactionalTEConnectorHV;
import BananaFructa.TTIEMultiblocks.PowerRework.TransactionalTEConnectorLV;
import BananaFructa.TTIEMultiblocks.PowerRework.TransactionalTEConnectorMV;
import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import antibluequirk.alternatingflux.block.TileEntityRelayAF;
import antibluequirk.alternatingflux.wire.AFWireType;
import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.energy.immersiveflux.IFluxReceiver;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConnectorHV;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConnectorLV;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConnectorMV;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import pl.pabilo8.immersiveintelligence.api.rotary.CapabilityRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryStorage;

import javax.annotation.Nullable;
import java.util.Set;

public class LoadSensorTileEntity extends TileEntityIEBase implements IEnergyStorage, ITickable {

    public EnumFacing facing;
    public int delta = 0;

    public int redstoneOffset = 7;
    public int magnitude = 1000;
    public int redstoneStrenght = 0;
    public int scale = -1;

    public static final int maxRF = 131072;
    public boolean autoSale = true;
    public boolean wrongPower = false;

    public int energy = 0;
    public int maxEnergy = 1;

    ModularList deltaHistory = new ModularList(200);

    public LoadSensorTileEntity() {
        super();
    }

    public LoadSensorTileEntity(EnumFacing facing) {
        super();
        this.facing = facing;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        IEUtils.notifyClientUpdate(world,pos);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY && (facing == IEUtils.shiftRelativeToNorth(this.facing,true,EnumFacing.EAST) || facing == null)) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY && (facing == IEUtils.shiftRelativeToNorth(this.facing,true,EnumFacing.EAST) || facing == null)) {
            return (T)this;
        }
        return super.getCapability(capability, facing);
    }


    @Override
    public void readCustomNBT(NBTTagCompound nbtTagCompound, boolean b) {
        facing = EnumFacing.values()[nbtTagCompound.getInteger("facing")];
        delta = nbtTagCompound.getInteger("delta");
        redstoneOffset = nbtTagCompound.getInteger("redstone_offset");
        magnitude = nbtTagCompound.getInteger("power_magnitude");
        redstoneStrenght = nbtTagCompound.getInteger("redstone_strength");
        scale = nbtTagCompound.getInteger("scale");
        autoSale = nbtTagCompound.getBoolean("auto_scale");
        energy = nbtTagCompound.getInteger("energy");
        maxEnergy = nbtTagCompound.getInteger("max_energy");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbtTagCompound, boolean b) {
        nbtTagCompound.setInteger("facing",facing.ordinal());
        nbtTagCompound.setInteger("delta",delta);
        nbtTagCompound.setInteger("redstone_offset",redstoneOffset);
        nbtTagCompound.setInteger("power_magnitude", magnitude);
        nbtTagCompound.setInteger("redstone_strength",redstoneStrenght);
        nbtTagCompound.setInteger("scale",scale);
        nbtTagCompound.setBoolean("auto_scale",autoSale);
        nbtTagCompound.setInteger("energy",energy);
        nbtTagCompound.setInteger("max_energy",maxEnergy);
    }


    // IE wire connectors perform two simulation passes
    boolean selectFirstFlag = true;
    boolean firstSim = true;

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
        return 0;
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

    @Override
    public void update() {
        if (world.isRemote) return;
        selectFirstFlag = true;
        firstSim = true;

        BlockPos out = getPos().offset(IEUtils.shiftRelativeToNorth(this.facing,true,EnumFacing.EAST));
        TileEntity teOut = getWorld().getTileEntity(out);
        wrongPower = false;
        int avalabile = 0;
        int wantedPower = 0;
        if (world != null && teOut instanceof TileEntityConnectorLV) {
            TileEntityConnectorLV connector = (TileEntityConnectorLV) teOut;
            if (connector instanceof TileEntityConnectorMV & connector instanceof TileEntityConnectorHV) wrongPower = true;
            Set<ImmersiveNetHandler.AbstractConnection> cons = ImmersiveNetHandler.INSTANCE.getIndirectEnergyConnections(Utils.toCC(connector), this.world, true);
            for (ImmersiveNetHandler.AbstractConnection con : cons) {
                IImmersiveConnectable connectable = ApiUtils.toIIC(con.end, this.world);
                if (connectable instanceof NetworkElement) {
                    if (connectable.isEnergyOutput()) {
                        wantedPower += ((NetworkElement) connectable).getWantedLoad();
                    } else {
                        avalabile += ((NetworkElement) connectable).getDelta();
                    }
                }
            }
        }

        delta = avalabile - wantedPower;
        redstoneStrenght = (int)Math.max(Math.min(15,((float)delta / magnitude) * 15 + redstoneOffset),0);
        world.notifyNeighborsOfStateChange(getPos(),world.getBlockState(getPos()).getBlock(),true);

        markDirty();
        IEUtils.notifyClientUpdate(world, pos);
    }
}
