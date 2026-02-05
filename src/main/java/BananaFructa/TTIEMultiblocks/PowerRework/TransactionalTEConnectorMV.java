package BananaFructa.TTIEMultiblocks.PowerRework;

import BananaFructa.TTIEMultiblocks.ControlBlocks.LoadSensorTileEntity;
import BananaFructa.TTIEMultiblocks.PowerNetworkInfo.GlobalNetworkInfoManager;
import BananaFructa.TTIEMultiblocks.PowerNetworkInfo.NetworkElement;
import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityCapacitorCreative;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityCapacitorLV;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConnectorMV;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TransactionalTEConnectorMV extends TileEntityConnectorMV implements NetworkElement {

    public int delta = 0;
    public int currentDelta = 0;
    private boolean firstSimulate = true;
    public int netId = 0;

    protected boolean canTakeMV() {
        return true;
    }

    protected boolean canTakeLV() {
        return false;
    }

    public TransactionalTEConnectorMV(EnumFacing facing) {
        super();
        netId = GlobalNetworkInfoManager.getNewId();
        this.facing = facing;
        MinecraftForge.EVENT_BUS.register(this);
    }

    public TransactionalTEConnectorMV() {
        super();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.writeCustomNBT(nbt, descPacket);
        nbt.setInteger("delta",delta);
        nbt.setInteger("current_delta",currentDelta);
        nbt.setBoolean("first_simulate",firstSimulate);
        nbt.setInteger("net_id",netId);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.readCustomNBT(nbt, descPacket);
        delta = nbt.getInteger("delta");
        currentDelta = nbt.getInteger("current_delta");
        firstSimulate = nbt.getBoolean("first_simulate");
        netId = nbt.getInteger("net_id");
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onFirstTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        delta = Math.min(Math.max(currentDelta,-getMaxOutput()),getMaxInput());;
        currentDelta = 0;
        markDirty();
        IEUtils.notifyClientUpdate(world, pos);
        firstSimulate = true;
    }

    public boolean isTargetInSimulation() {
        TileEntity te = world.getTileEntity(pos.offset(facing));
        if (te instanceof TileEntityCapacitorLV || te instanceof TileEntityCapacitorCreative || te instanceof LoadSensorTileEntity) {
            return false;
        }
        return true;
    }

    public int outputEnergy(int amount, boolean simulate, int energyType) {
        if (this.isRelay()) {
            return 0;
        } else {
            int acceptanceLeft = this.getMaxOutput() - this.currentTickToMachine;
            if (acceptanceLeft <= 0) {
                return 0;
            } else {
                int toAccept = Math.min(acceptanceLeft, amount);
                TileEntity capacitor = Utils.getExistingTileEntity(this.world, this.getPos().offset(this.facing));
                int ret = EnergyHelper.insertFlux(capacitor, this.facing.getOpposite(), toAccept, simulate);
                if (!simulate) {
                    currentDelta = -ret;
                    this.currentTickToMachine += ret;
                }

                return ret;
            }
        }
    }

    @Override
    public int receiveEnergy(EnumFacing from, int energy, boolean simulate) {
        if (firstSimulate && energy > 0 && isTargetInSimulation() == simulate) {
            firstSimulate = false;
            currentDelta += energy;
        }
        return super.receiveEnergy(from, energy, simulate);
    }

    @Override
    public int getId() {
        return netId;
    }

    @Override
    public void update() {

    }
}
