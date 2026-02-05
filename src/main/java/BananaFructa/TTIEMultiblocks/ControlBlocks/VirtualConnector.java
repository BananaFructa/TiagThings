package BananaFructa.TTIEMultiblocks.ControlBlocks;

import antibluequirk.alternatingflux.Config;
import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorage;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConnectorLV;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.Consumer;

public class VirtualConnector extends TileEntityConnectorLV {

    boolean output = false;

    public VirtualConnector(boolean output) {
        super();
        this.output = output;
    }

    boolean firstTick = true;

    private Pair<Float, Consumer<Float>> getEnergyForConnection(@Nullable ImmersiveNetHandler.AbstractConnection c) {
        float loss = c != null ? c.getAverageLossRate() : 0.0F;
        float max = (1.0F - loss) * (float)this.getFluxStorage().getEnergyStored();
        Consumer<Float> extract = (energy) -> this.getFluxStorage().modifyEnergyStored((int)(-energy / (1.0F - loss)));
        return new ImmutablePair(max, extract);
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

    public void func_73660_a() {
        if (!this.world.isRemote) {
            if (this.getFluxStorage().getEnergyStored() > 0) {
                int temp = this.transferEnergy(this.getFluxStorage().getEnergyStored(), true, 0);
                if (temp > 0) {
                    this.getFluxStorage().modifyEnergyStored(-this.transferEnergy(temp, false, 0));
                    this.markDirty();
                }

                //this.addAvailableEnergy(-1.0F, (Consumer)null);
                this.notifyAvailableEnergy(this.getFluxStorage().getEnergyStored(), (Set)null);
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

    @Override
    public boolean isEnergyOutput() {
        return output;
    }

    /*public int outputEnergy(int amount, boolean simulate, int energyType) {
        if (this.isRelay()) {
            return 0;
        } else {
            int acceptanceLeft = this.getMaxOutput() - this.currentTickToMachine;
            if (acceptanceLeft <= 0) {
                System.out.println(acceptanceLeft);
                return 0;
            } else {
                int toAccept = Math.min(acceptanceLeft, amount);
                TileEntity capacitor = Utils.getExistingTileEntity(this.field_145850_b, this.func_174877_v().func_177972_a(this.facing));
                int ret = EnergyHelper.insertFlux(capacitor, this.facing.func_176734_d(), toAccept, simulate);
                if (!simulate) {
                    this.currentTickToMachine += ret;
                }

                return ret;
            }
        }
    }*/

    @Override
    public int getMaxInput() {
        return Config.AFConfig.wireTransferRate[0];
    }

    @Override
    public int getMaxOutput() {
        return Config.AFConfig.wireTransferRate[0];
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.writeCustomNBT(nbt, descPacket);
        nbt.setBoolean("virt_output",output);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.readCustomNBT(nbt, descPacket);
        output = nbt.getBoolean("virt_output");
    }

    @Override
    public boolean canConnectCable(WireType cableType, TargetingInfo target, Vec3i offset) {
        return "AF".equals(cableType.getCategory());
    }

    protected float getBaseDamage(ImmersiveNetHandler.Connection c) {
        return 240.0F / (float)c.cableType.getTransferRate();
    }

    public Vec3d getConnectionOffset(ImmersiveNetHandler.Connection con) {
        EnumFacing side = this.facing.getOpposite();
        double conRadius = con.cableType.getRenderDiameter() / (double)2.0F;
        return new Vec3d((double)0.5F + (double)side.getFrontOffsetX() * ((double)0.375F - conRadius), (double)0.5F + (double)side.getFrontOffsetY() * ((double)0.375F - conRadius), (double)0.5F + (double)side.getFrontOffsetZ() * ((double)0.375F - conRadius));
    }


}
