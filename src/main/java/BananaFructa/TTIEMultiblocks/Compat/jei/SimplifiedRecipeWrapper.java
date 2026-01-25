package BananaFructa.TTIEMultiblocks.Compat.jei;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityCCM;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityLathe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TiagThings.Utils;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.rmi.CORBA.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SimplifiedRecipeWrapper implements IRecipeWrapper {

    public List<ItemStack> inputs;
    public List<ItemStack> outputs;
    public List<FluidStack> fluidInputs;
    public List<FluidStack> fluidOutputs;
    public boolean hasOtherInfo = false;
    public NBTTagCompound otherInfo = new NBTTagCompound();

    public SimplifiedRecipeWrapper(SimplifiedMultiblockRecipe recipe) {
        recipe.setupJEI();
        this.inputs = recipe.getJEITotalItemInputs();
        if (recipe instanceof TileEntityLathe.LatheRecipe) {
            TileEntityLathe.LatheRecipe lr = (TileEntityLathe.LatheRecipe) recipe;
            switch (lr.tier) {
                case STEEL:
                    this.inputs.add(Utils.itemStackFromCTId("<tiagthings:steel_lathe_tool>"));
                    break;
                case TUNGSTEN_STEEL:
                    this.inputs.add(Utils.itemStackFromCTId("<tiagthings:tungsten_steel_lathe_tool>"));
                    break;
            }
        }
        if (recipe instanceof TileEntityCCM.CCMRecipe) {
            hasOtherInfo = true;
            otherInfo.setInteger("mode",((TileEntityCCM.CCMRecipe)recipe).ccmMode.ordinal());
        }
        this.outputs = recipe.getJEITotalItemOutputs();
        this.fluidInputs = recipe.getJEITotalFluidInputs();
        this.fluidOutputs = recipe.getJEITotalFluidOutputs();
    }

    @Override
    public void getIngredients(IIngredients iIngredients) {
        iIngredients.setInputs(ItemStack.class,inputs);
        iIngredients.setOutputs(ItemStack.class,outputs);
        iIngredients.setInputs(FluidStack.class,fluidInputs);
        iIngredients.setOutputs(FluidStack.class,fluidOutputs);
    }


}
