package BananaFructa.TTIEMultiblocks.Utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class STEMMFluidTank extends FluidTank {
    public int fluidIHInput = -1;
    public int fluidOHInput = -1;
    public List<Integer> recipeInputCorelation = new ArrayList<>();

    SimplifiedTileEntityMultiblockMetal<?,?> parent;
    public STEMMFluidTank(int capacity,SimplifiedTileEntityMultiblockMetal parent) {
        super(capacity);
        this.parent = parent;
    }

    public void setFluidIHInput(int ihi) {
        fluidIHInput = ihi;
    }

    public void setFluidOHInput(int iho) {
        fluidOHInput = iho;
    }

    public void addRecipeFlowCorelation(int id) {
        recipeInputCorelation.add(id);
    }

    public boolean allowFluid(Fluid f) {
        return true; // TODO: this is broken, check with the open hearth furnace, or maybe should be left like this idk ???
        /*
        if (recipeInputCorelation.isEmpty()) {
            return true;
        }
        for (SimplifiedMultiblockRecipe recipe : parent.recipes) {
            for (Integer s : recipeInputCorelation) {
                if (recipe != null && recipe.getFluidInputs() != null && recipe.getFluidInputs().size() > s && recipe.getFluidInputs().get(s) != null && recipe.getFluidInputs().get(s).getFluid() == f) {
                        return true;
                }
            }
        }
        return false;*/
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (!allowFluid(resource.getFluid())) return 0;
        return super.fill(resource, doFill);
    }

    public int fillFromRecipe(FluidStack resource, boolean doFill) {
        return super.fill(resource, doFill);
    }

    public STEMMFluidTank readFromNBT(NBTTagCompound nbt)
    {
        if (!nbt.hasKey("Empty"))
        {
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(nbt);
            setFluid(fluid);
            fluidIHInput = nbt.getInteger("ihi");
            fluidOHInput = nbt.getInteger("iho");
            int ids = nbt.getInteger("ids");
            for (int i = 0;i < ids;i++) {
                recipeInputCorelation.add(nbt.getInteger("id-"+i));
            }
        }
        else
        {
            setFluid(null);
        }
        return this;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        if (fluid != null)
        {
            fluid.writeToNBT(nbt);
        }
        else
        {
            nbt.setString("Empty", "");
        }
        nbt.setInteger("ihi",fluidIHInput);
        nbt.setInteger("iho",fluidOHInput);
        nbt.setInteger("ids", recipeInputCorelation.size());
        for (int i = 0;i < recipeInputCorelation.size();i++) {
            nbt.setInteger("id-"+i,recipeInputCorelation.get(i));
        }
        return nbt;
    }


}
