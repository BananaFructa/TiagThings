package BananaFructa.TTIEMultiblocks.PowerRework;

import BananaFructa.TTIEMultiblocks.ControlBlocks.LoadSensorTileEntity;
import BananaFructa.TTIEMultiblocks.PowerNetworkInfo.GlobalNetworkInfoManager;
import BananaFructa.TTIEMultiblocks.PowerNetworkInfo.NetworkElement;
import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityCapacitorCreative;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityCapacitorLV;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConnectorLV;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.Consumer;

public class TransactionalTEConnectorLV extends TileEntityConnectorLV implements NetworkElement {

    public int delta = 0;
    public int currentDelta = 0;
    private boolean firstSimulate = true;
    public int netId = 0;

    public TransactionalTEConnectorLV(EnumFacing facing) {
        this();
        this.facing = facing;
        netId = GlobalNetworkInfoManager.getNewId();
    }

    public TransactionalTEConnectorLV() {
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

    public void onTick() {
        delta = Math.min(Math.max(currentDelta,-getMaxOutput()),getMaxInput());
        //System.out.println("SEND");
        GlobalNetworkInfoManager.notifyLoad(this,pos,world,isEnergyOutput(),world.getTileEntity(pos.offset(facing)));
        GlobalNetworkInfoManager.registerNetworkTransaction(this,pos,world,delta,isEnergyOutput(),world.getTileEntity(pos.offset(facing)));
        currentDelta = 0;
        markDirty();
        IEUtils.notifyClientUpdate(world, pos);
        firstSimulate = true;
    }

    public boolean isEnergyOutput() {
        BlockPos outPos = this.getPos().offset(this.facing);
        if (this.isRelay()) {
            return false;
        } else {
            TileEntity tile = Utils.getExistingTileEntity(this.world, outPos);
            return BananaFructa.TiagThings.Utils.isFluxReceiverFixed(tile, this.facing.getOpposite());
        }
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
        if (firstSimulate && energy > 0 && !(isTargetInSimulation() ^ simulate)) {
            firstSimulate = false;
            currentDelta += energy;
        }
        return super.receiveEnergy(from, energy, simulate);
    }

    @Override
    public int getId() {
        return netId;
    }

    boolean firstTick = true;

    @Override
    public void update() {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(this::onTick);
        if (!this.world.isRemote) {
            if (getFluxStorage().getEnergyStored() > 0) {
                int temp = this.transferEnergy(getFluxStorage().getEnergyStored(), true, 0);
                if (temp > 0) {
                    getFluxStorage().modifyEnergyStored(-this.transferEnergy(temp, false, 0));
                    this.markDirty();
                }

                this.addAvailableEnergy(-1.0F, (Consumer)null);
                this.notifyAvailableEnergy(getFluxStorage().getEnergyStored(), (Set)null);
            }

            this.currentTickToMachine = 0;
            this.currentTickToNet = 0;
        } else if (this.firstTick) {
            Set<ImmersiveNetHandler.Connection> conns = ImmersiveNetHandler.INSTANCE.getConnections(this.world, this.pos);
            if (conns != null) {
                for(ImmersiveNetHandler.Connection conn : conns) {
                    if (this.pos.compareTo(conn.end) < 0 && this.world.isBlockLoaded(conn.end)) {
                        this.markContainingBlockForUpdate((IBlockState)null);
                    }
                }
            }

            this.firstTick = false;
        }
    }

    private void notifyAvailableEnergy(int energyStored, @Nullable Set<ImmersiveNetHandler.AbstractConnection> outputs) {
        if (outputs == null) {
            outputs = ImmersiveNetHandler.INSTANCE.getIndirectEnergyConnections(this.pos, this.world, true);
        }

        for(ImmersiveNetHandler.AbstractConnection con : outputs) {
            IImmersiveConnectable end = ApiUtils.toIIC(con.end, this.world);
            if (con.cableType != null && end != null && end.allowEnergyToPass((ImmersiveNetHandler.Connection)null)) {
                Pair<Float, Consumer<Float>> e = this.getEnergyForConnection(con);
                end.addAvailableEnergy((Float)e.getKey(), (Consumer)e.getValue());
            }
        }

    }

    private Pair<Float, Consumer<Float>> getEnergyForConnection(@Nullable ImmersiveNetHandler.AbstractConnection c) {
        float loss = c != null ? c.getAverageLossRate() : 0.0F;
        float max = (1.0F - loss) * (float)getFluxStorage().getEnergyStored();
        Consumer<Float> extract = (energy) -> getFluxStorage().modifyEnergyStored((int)(-energy / (1.0F - loss)));
        return new ImmutablePair(max, extract);
    }
}
