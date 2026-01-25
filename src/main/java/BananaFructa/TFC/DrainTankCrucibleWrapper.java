package BananaFructa.TFC;

import BananaFructa.TiagThings.Utils;
import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.objects.fluids.FluidsTFC;
import net.dries007.tfc.objects.fluids.properties.MetalProperty;
import net.dries007.tfc.objects.te.TECrucible;
import net.dries007.tfc.util.Alloy;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

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
            NBTTagCompound nbt = teCrucible.func_189515_b(new NBTTagCompound());
            Alloy alloy = new Alloy();
            alloy.deserializeNBT(nbt.getCompoundTag("alloy"));
            Metal alloyMetal = alloy.getResult();
            return nbt.getFloat("temp") > alloyMetal.getMeltTemp();
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
            addMetal(resource);
            filled = resource.amount;
        }
        else {
            addMetal(new FluidStack(resource.getFluid(),filled));
        }
        return filled;
    }

    protected void addMetal(FluidStack resource) {
        float temperature = Utils.readDeclaredField(TECrucible.class, teCrucible, "temperature");
        float targetTemperature = Utils.readDeclaredField(TECrucible.class, teCrucible, "targetTemperature");
        teCrucible.addMetal(FluidsTFC.getMetalFromFluid(resource.getFluid()), resource.amount);
        Utils.writeDeclaredField(TECrucible.class, teCrucible, "temperature", temperature, false);
        Utils.writeDeclaredField(TECrucible.class, teCrucible, "targetTemperature", targetTemperature, false);
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
