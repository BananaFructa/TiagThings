package BananaFructa.AppliedEnergistics;

import BananaFructa.TiagThings.TTMain;
import BananaFructa.TiagThings.Utils;
import appeng.api.networking.IGridNode;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.tile.AEBaseTile;
import appeng.tile.misc.TileInscriber;
import mods.railcraft.common.blocks.BlockMeta;
import nc.init.NCFluids;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.registries.ForgeRegistry;

import javax.annotation.Nullable;

public class TileInscriberModified extends TileInscriber {

    public boolean needToSetOrientation = false;
    public FluidTank tank = new FluidTank(1000);
    private Fluid liquidHelium = FluidRegistry.getFluid("liquid_helium");
    private boolean canContinue = false;

    EnumFacing forward;
    EnumFacing up;

    public TileInscriberModified() {
        super();
    }
    public TileInscriberModified(EnumFacing forward,EnumFacing up) {
        this.forward = forward;
        this.up = up;
        needToSetOrientation = true;
    }

    @Override
    public void validate() {
        super.validate();
        if (needToSetOrientation) {
            this.setOrientation(
                    forward,
                    up
            );
            needToSetOrientation = false;
        }
    }

    @Override
    public boolean isSmash() {
        return super.isSmash() && canContinue;
    }

    @Override
    public TickRateModulation tickingRequest(IGridNode node, int ticksSinceLastCall) {
        canContinue = false;
        if (super.isSmash() && this.getTask() != null && (int)Utils.readDeclaredField(TileInscriber.class,this,"finalStep") == 7) {
            if (tank.getFluid() != null && (tank.getFluid().getFluid() == liquidHelium || tank.getFluid().getFluid() == TTMain.liquidHelium3) && tank.getFluid().amount >= 1000) {
                tank.drain(1000, true);
                canContinue = true;
            }
        } else  {
            canContinue = true;
        }
        return super.tickingRequest(node, ticksSinceLastCall);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing != null) return true;
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing != null) return (T)tank;
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        tank.readFromNBT(compound.getCompoundTag("tank"));
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("tank",tank.writeToNBT(new NBTTagCompound()));
        return super.writeToNBT(compound);
    }
}
