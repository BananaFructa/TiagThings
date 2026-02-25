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
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Consumer;

public class TransactionalTEConnectorLV extends TileEntityConnectorLV implements NetworkElement {

    public int delta = 0;
    public int currentDelta = 0;

    public int loss = 0;
    public int currentLoss = 0;

    private boolean firstSimulate = true;
    public int netId = 0;

    public TransactionalTEConnectorLV(EnumFacing facing) {
        this();
        this.facing = facing;
        netId = GlobalNetworkInfoManager.getNewId();
    }

    public TransactionalTEConnectorLV() {
        super();
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.writeCustomNBT(nbt, descPacket);
        nbt.setInteger("delta",delta);
        nbt.setInteger("loss",loss);
        nbt.setInteger("current_delta",currentDelta);
        nbt.setBoolean("first_simulate",firstSimulate);
        nbt.setInteger("net_id",netId);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.readCustomNBT(nbt, descPacket);
        delta = nbt.getInteger("delta");
        loss = nbt.getInteger("loss");
        currentDelta = nbt.getInteger("current_delta");
        firstSimulate = nbt.getBoolean("first_simulate");
        netId = nbt.getInteger("net_id");
    }

    public void onTick() {
        try {
            loss = currentLoss;//
            delta = Math.min(Math.max(currentDelta, -getMaxOutput()), getMaxInput());//
            //System.out.println("SEND");
            GlobalNetworkInfoManager.notifyLoad(this, pos, world, isEnergyOutput(), world.getTileEntity(pos.offset(facing)));
            GlobalNetworkInfoManager.registerNetworkTransaction(this, pos, world, isEnergyOutput(), world.getTileEntity(pos.offset(facing)));
            currentDelta = 0; //
            currentLoss = 0;//
            markDirty();//
            IEUtils.notifyClientUpdate(world, pos);//
            firstSimulate = true;//
        } catch (Exception err) {
            err.printStackTrace(); // TODO: check this
        }
    }

    public boolean isEnergyOutput() {
        BlockPos outPos = this.getPos().offset(this.facing);
        if (this.isRelay()) {
            return false;
        } else {
            TileEntity tile = Utils.getExistingTileEntity(this.world, outPos);
            if (tile == null) return false;
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
                    currentDelta += -ret;
                    this.currentTickToMachine += ret;
                }

                return ret;
            }
        }
    }

    public int transferEnergy(int energy, boolean simulate, int energyType) {
        int received = 0;
        if (!this.world.isRemote) {
            Set<ImmersiveNetHandler.AbstractConnection> outputs = ImmersiveNetHandler.INSTANCE.getIndirectEnergyConnections(Utils.toCC(this), this.world, true);
            int powerLeft = Math.min(Math.min(this.getMaxOutput(), this.getMaxInput()), energy);
            int powerForSort = powerLeft;
            if (outputs.isEmpty()) {
                return 0;
            }

            int sum = 0;
            Map<ImmersiveNetHandler.AbstractConnection, Integer> powerSorting = new TreeMap();

            for(ImmersiveNetHandler.AbstractConnection con : outputs) {
                if (con.isEnergyOutput) {
                    IImmersiveConnectable end = ApiUtils.toIIC(con.end, this.world);
                    if (con.cableType != null && end != null) {
                        int atmOut = Math.min(powerForSort, con.cableType.getTransferRate());
                        int tempR = end.outputEnergy(atmOut, true, energyType);
                        if (tempR > 0) {
                            powerSorting.put(con, tempR);
                            sum += tempR;
                        }
                    }
                }
            }

            if (sum > 0) {
                for(ImmersiveNetHandler.AbstractConnection con : powerSorting.keySet()) {
                    IImmersiveConnectable end = ApiUtils.toIIC(con.end, this.world);
                    if (con.cableType != null && end != null) {
                        float prio = (float)(Integer)powerSorting.get(con) / (float)sum;
                        int output = Math.min(MathHelper.ceil((float)powerForSort * prio), powerLeft);
                        int tempR = end.outputEnergy(Math.min(output, con.cableType.getTransferRate()), true, energyType);
                        int r = tempR;
                        int maxInput = this.getMaxInput();
                        int lAmount = (int)Math.max((double)0.0F, Math.floor((double)((float)tempR * con.getPreciseLossRate(tempR, maxInput))));;
                        if (!simulate) currentLoss = lAmount;
                        tempR -= lAmount;
                        end.outputEnergy(tempR, simulate, energyType);
                        HashSet<IImmersiveConnectable> passedConnectors = new HashSet();
                        float intermediaryLoss = 0.0F;

                        for(ImmersiveNetHandler.Connection sub : con.subConnections) {
                            float length = (float)sub.length / (float)sub.cableType.getMaxLength();
                            float baseLoss = (float)sub.cableType.getLossRatio();
                            float mod = (float)(maxInput - tempR) / (float)maxInput / 0.25F * 0.1F;
                            intermediaryLoss = MathHelper.clamp(intermediaryLoss + length * (baseLoss + baseLoss * mod), 0.0F, 1.0F);
                            int transferredPerCon = (Integer)ImmersiveNetHandler.INSTANCE.getTransferedRates(this.world.provider.getDimension()).getOrDefault(sub, 0);
                            transferredPerCon += r;
                            if (!simulate) {
                                ImmersiveNetHandler.INSTANCE.getTransferedRates(this.world.provider.getDimension()).put(sub, transferredPerCon);
                                IImmersiveConnectable subStart = ApiUtils.toIIC(sub.start, this.world);
                                IImmersiveConnectable subEnd = ApiUtils.toIIC(sub.end, this.world);
                                if (subStart != null && passedConnectors.add(subStart)) {
                                    subStart.onEnergyPassthrough((double)((float)r - (float)r * intermediaryLoss));
                                }

                                if (subEnd != null && passedConnectors.add(subEnd)) {
                                    subEnd.onEnergyPassthrough((double)((float)r - (float)r * intermediaryLoss));
                                }
                            }
                        }

                        received += r;
                        powerLeft -= r;
                        if (powerLeft <= 0) {
                            break;
                        }
                    }
                }
            }
        }

        return received;
    }

    @Override
    public int receiveEnergy(EnumFacing from, int energy, boolean simulate) {
        if (firstSimulate && energy > 0 && !(isTargetInSimulation() ^ simulate)) {
            firstSimulate = false;
            TileEntity interactor = getInteractor();
            int accepted = ActualPowerReader.getActualPower(interactor,getMaxInput(),true);
            if (accepted != -1) {
                if (accepted > energy) {
                    currentDelta += energy;
                    ActualPowerReader.substractAvalabiltiy(interactor,energy);
                } else {
                    currentDelta += ActualPowerReader.getActualPower(interactor,getMaxInput(),false);;
                }
            }
            else currentDelta += energy;
        }
        return super.receiveEnergy(from, energy, simulate);
    }

    @Override
    public int getId() {
        return netId;
    }

    @Override
    public int getDelta() {
        return delta;
    }

    @Override
    public int getWantedLoad() {
        if (!isEnergyOutput()) return 0;
        return Math.min(BananaFructa.TiagThings.Utils.getProcessLoad(getInteractor()),getMaxOutput());
    }

    @Override
    public int getLoss() {
        return loss;
    }

    @Override
    public TileEntity getInteractor() {
        BlockPos outPos = this.getPos().offset(this.facing);
        TileEntity te =  Utils.getExistingTileEntity(this.world, outPos);
        if (te instanceof TileEntityMultiblockMetal<?,?>) return ((TileEntityMultiblockMetal<?, ?>) te).master();
        return te;
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

            if (isEnergyOutput()) {

            }
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
        Consumer<Float> extract = (energy) -> {
            getFluxStorage().modifyEnergyStored((int)(-energy / (1.0F - loss)));
        };
        return new ImmutablePair(max, extract);
    }
}
