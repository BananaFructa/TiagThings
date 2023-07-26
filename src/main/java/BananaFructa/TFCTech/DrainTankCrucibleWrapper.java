package BananaFructa.TFCTech;

import net.dries007.tfc.objects.fluids.FluidsTFC;
import net.dries007.tfc.objects.fluids.properties.MetalProperty;
import net.dries007.tfc.objects.te.TECrucible;
import net.dries007.tfc.util.Alloy;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import org.lwjgl.Sys;

import javax.annotation.Nullable;

public class DrainTankCrucibleWrapper implements IFluidHandler {

    Alloy alloy;
    TECrucible teCrucible;

    IFluidTankProperties properties = new IFluidTankProperties() {
        @Nullable
        @Override
        public FluidStack getContents() {
            FluidStack stack = getFluid();
            return stack == null ? null : stack.copy();
        }

        @Override
        public int getCapacity() {
            return alloy.getMaxAmount();
        }

        @Override
        public boolean canFill() {
            return true;
        }

        @Override
        public boolean canDrain() {
            return true;
        }

        @Override
        public boolean canFillFluidType(FluidStack fluidStack) {
            if (isLiquidMetal(fluidStack.getFluid())) return canFill();
            return false;
        }

        @Override
        public boolean canDrainFluidType(FluidStack fluidStack) {
            return canDrain();
        }
    };

    private boolean isLiquidMetal(Fluid fluid) {
        return FluidsTFC.getWrapper(fluid).get(MetalProperty.METAL) != null;
    }

    public DrainTankCrucibleWrapper(TECrucible teCrucible) {
        this.teCrucible = teCrucible;
        alloy = teCrucible.getAlloy();
    }

    @Nullable
    public FluidStack getFluid() {
        return new FluidStack(FluidsTFC.getFluidFromMetal(alloy.getResult()),alloy.getAmount());
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return new IFluidTankProperties[]{properties};
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        FluidStack fluid = getFluid();

        if (resource == null || resource.amount <= 0 || !isLiquidMetal(resource.getFluid())) {
            return 0;
        }

        if (!doFill) {
            if (fluid == null)
            {
                return Math.min(alloy.getMaxAmount(), resource.amount);
            }

            return Math.min(alloy.getMaxAmount() - fluid.amount, resource.amount);
        }

        int filled = alloy.getMaxAmount() - fluid.amount;

        if (resource.amount < filled) {
            teCrucible.addMetal(FluidsTFC.getMetalFromFluid(resource.getFluid()),resource.amount);
            filled = resource.amount;
        }
        else {
            teCrucible.addMetal(FluidsTFC.getMetalFromFluid(resource.getFluid()),filled);
        }
        return filled;
    }

    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        return resource == null ? null : drain(resource.amount,doDrain);
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain)
    {
        FluidStack fluid = getFluid();

        if (fluid == null || maxDrain <= 0) {
            return null;
        }

        int drained = maxDrain;
        if (fluid.amount < drained) {
            drained = fluid.amount;
        }

        FluidStack stack = new FluidStack(fluid, drained);
        if (doDrain) {
            alloy.removeAlloy(drained,false);
            teCrucible.markForSync();
        }

        return stack;
    }
}
