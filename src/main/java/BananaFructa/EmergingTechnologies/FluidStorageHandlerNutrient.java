package BananaFructa.EmergingTechnologies;

import BananaFructa.TiagThings.TTMain;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class FluidStorageHandlerNutrient extends FluidTank {
    public FluidStorageHandlerNutrient(int capacity) {
        super(capacity);
    }

    public boolean canFillFluidType(FluidStack fluid) {
        return fluid.getFluid() == TTMain.nitrogenSolution || fluid.getFluid() == TTMain.potassiumSolution || fluid.getFluid() == TTMain.phosphorusSolution;
    }

}
