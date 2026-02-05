package BananaFructa.TTIEMultiblocks.ControlBlocks;

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
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConnectorLV;
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
    public int magnitude = 16384;
    public int redstoneStrenght = 0;

    public static final int maxRF = 131072;

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
        if (capability == CapabilityEnergy.ENERGY && facing == IEUtils.shiftRelativeToNorth(this.facing,true,EnumFacing.WEST)) {
            System.out.println(facing);
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY && facing == IEUtils.shiftRelativeToNorth(this.facing,true,EnumFacing.WEST)) {
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
        if (nbtTagCompound.hasKey("virt_AF_in")) {
            //virtualAFInput = new VirtualConnector(true);
            //virtualAFInput.readCustomNBT(nbtTagCompound.getCompoundTag("virt_AF_in"),false);
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbtTagCompound, boolean b) {
        nbtTagCompound.setInteger("facing",facing.ordinal());
        nbtTagCompound.setInteger("delta",delta);
        nbtTagCompound.setInteger("redstone_offset",redstoneOffset);
        nbtTagCompound.setInteger("power_magnitude", magnitude);
        nbtTagCompound.setInteger("redstone_strength",redstoneStrenght);
        if (virtualAFInput != null) {
            NBTTagCompound virtualIn = new NBTTagCompound();
            virtualAFInput.writeCustomNBT(virtualIn, false);
            nbtTagCompound.setTag("virt_AF_in", virtualIn);
        }
    }

    int needed = 0;


    // IE wire connectors perform two simulation passes
    boolean selectFirstFlag = true;
    boolean firstSim = true;

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {

        EnumFacing face = IEUtils.shiftRelativeToNorth(facing,true,EnumFacing.EAST);
        BlockPos pos = getPos().offset(face);
        TileEntity te = this.world.getTileEntity(pos);
        if (te != null) {
            if (te.hasCapability(CapabilityEnergy.ENERGY,face.getOpposite())) {
                if (te instanceof IEnergyStorage) {
                    IEnergyStorage energyStorage = (IEnergyStorage) te;
                    if (simulate && selectFirstFlag) {
                        // Testing with 10 million RF/t
                        int maxOut = energyStorage.receiveEnergy(10000000,true);
                        if (firstSim) needed = maxOut;
                        markDirty();
                        IEUtils.notifyClientUpdate(world, pos);
                        firstSim = false;
                    }
                    selectFirstFlag = !selectFirstFlag;
                    return energyStorage.receiveEnergy(maxReceive, simulate);
                } else if (te instanceof IFluxReceiver) {
                    IFluxReceiver receiver = (IFluxReceiver) te;
                    if (receiver.canConnectEnergy(face.getOpposite())) {
                        if (simulate && selectFirstFlag) {
                            int maxOut = receiver.receiveEnergy(face.getOpposite(),10000000,true);
                            if (firstSim) needed = maxOut;
                            markDirty();
                            IEUtils.notifyClientUpdate(world, pos);
                            firstSim = false;
                        }
                        selectFirstFlag = !selectFirstFlag;
                        return receiver.receiveEnergy(face.getOpposite(),maxReceive,simulate);
                    }
                }
            }
        }

        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return 0;
    }

    @Override
    public int getMaxEnergyStored() {
        return 1024;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    public VirtualConnector virtualAFInput;

    @Override
    public void update() {
        if (world.isRemote) return;
        selectFirstFlag = true;
        firstSim = true;
        BlockPos in = getPos().offset(IEUtils.shiftRelativeToNorth(this.facing,true,EnumFacing.WEST));
        TileEntity te = getWorld().getTileEntity(in);
        if (world != null && te instanceof TileEntityRelayAF) {
            VirtualConnector conn = new VirtualConnector(true);
            conn.facing = ((TileEntityRelayAF) te).facing;
            world.setTileEntity(in, conn);
        }
        int avalabile = 0;
        if (world != null && te instanceof TileEntityConnectorLV) {
            TileEntityConnectorLV connector = (TileEntityConnectorLV) te;
            Set<ImmersiveNetHandler.AbstractConnection> cons = ImmersiveNetHandler.INSTANCE.getIndirectEnergyConnections(Utils.toCC(connector),this.world,true);
            for (ImmersiveNetHandler.AbstractConnection con : cons) {
                IImmersiveConnectable connectable = ApiUtils.toIIC(con.end,this.world);
                //if (!con.isEnergyOutput) {
                    if (connectable instanceof TransactionalTEConnectorLV) {
                        TransactionalTEConnectorLV teConnector = (TransactionalTEConnectorLV) connectable;
                        avalabile += teConnector.delta;
                    }
                    if (connectable instanceof TransactionalTEConnectorMV) {
                        TransactionalTEConnectorMV teConnector = (TransactionalTEConnectorMV) connectable;
                        avalabile += teConnector.delta;
                    }
                    if (connectable instanceof TransactionalTEConnectorHV) {
                        TransactionalTEConnectorHV teConnector = (TransactionalTEConnectorHV) connectable;
                        avalabile += teConnector.delta;
                    }
                //}
            }
        }
        delta = avalabile - needed;
        redstoneStrenght = (int)Math.max(Math.min(15,((float)delta / magnitude) * 15 + redstoneOffset),0);
        world.notifyNeighborsOfStateChange(getPos(),world.getBlockState(getPos()).getBlock(),true);

        if (virtualAFInput != null) {
            virtualAFInput.func_73660_a();
        }
        markDirty();
        IEUtils.notifyClientUpdate(world, pos);
    }
}
