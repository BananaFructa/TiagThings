package BananaFructa.ImmersiveEngineering;

import BananaFructa.TiagThings.TTMain;
import BananaFructa.TiagThings.Utils;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;

public class GasFluidTank extends FluidTank {

    static Fluid hydrogen = Utils.fluidFromCTId("<liquid:hydrogen>");
    int universalCapacity = 0;

    public GasFluidTank(int capacity) {
        super(capacity);
    }

    @Nullable
    @Override
    public FluidStack getFluid() {
        return null;
    }

    @Override
    public boolean canDrain() {
        return false;
    }

    @Override
    public int fillInternal(FluidStack resource, boolean doFill) {
        if (resource.getFluid() == TTMain.butane || resource.getFluid() == TTMain.hySulfide || resource.getFluid() == hydrogen) return 0;
        if (!resource.getFluid().isGaseous() && resource.getFluid() != TTMain.ttChlorine) return 0;
        //if (resource.getFluid().getDensity() > 0) return 0;
        if (universalCapacity + resource.amount <= getCapacity()) {
            if (doFill) universalCapacity += resource.amount;
            return resource.amount;
        } else {
            int dif =  getCapacity() - universalCapacity;
            if (doFill) universalCapacity = getCapacity();
            return dif;

        }
    }

    void dump(int maxDrain) {
        if (maxDrain <= universalCapacity) {
            universalCapacity -= maxDrain;
        } else {
            int dif = universalCapacity;
            universalCapacity = 0;
        }
    }

    @Override
    public int getFluidAmount() {
        return universalCapacity;
    }

    public void setFluidAmount(int amount) {
        universalCapacity = amount;
    }

}
