package BananaFructa.TTIEMultiblocks.Utils;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

public class TTMultiblockFluidWrapper implements IFluidHandler {

    FluidTank tank;
    SimplifiedTileEntityMultiblockMetal<?,?> te;

    PortType type;

    public TTMultiblockFluidWrapper(FluidTank tank,SimplifiedTileEntityMultiblockMetal<?,?> te,PortType type) {
        this.tank = tank;
        this.te = te;
        this.type = type;
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return tank.getTankProperties();
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (type == PortType.OUTPUT) return 0;
        int fillAmount = tank.fill(resource,doFill);
        if (doFill && fillAmount != 0) te.updateMasterBlock(null,true);
        return fillAmount;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        if (type == PortType.INPUT) return null;
        FluidStack stack = tank.drain(resource,doDrain);
        if (stack != null && doDrain) te.updateMasterBlock(null,true);
        return stack;
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (type == PortType.INPUT) return null;
        FluidStack drainAmount = tank.drain(maxDrain,doDrain);
        if (drainAmount != null && doDrain) te.updateMasterBlock(null,true);
        return drainAmount;
    }
}
