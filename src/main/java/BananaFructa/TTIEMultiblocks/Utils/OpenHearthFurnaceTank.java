package BananaFructa.TTIEMultiblocks.Utils;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class OpenHearthFurnaceTank extends STEMMFluidTank{
    public OpenHearthFurnaceTank(int capacity, SimplifiedTileEntityMultiblockMetal parent) {
        super(capacity, parent);
    }

    @Override
    public boolean allowFluid(Fluid f) {
        if (this.getFluidAmount() == 0) return false;
        return super.allowFluid(f);
    }

    @Override
    public int fillFromRecipe(FluidStack resource, boolean doFill) {
        this.drain(this.getFluidAmount(),true);
        return fillInternal(resource, doFill);
    }
}
