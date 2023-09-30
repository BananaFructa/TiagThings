package BananaFructa.TTIEMultiblocks.Compat.jei;

import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SimplifiedRecipeWrapper implements IRecipeWrapper {

    public List<ItemStack> inputs;
    public List<ItemStack> outputs;
    public List<FluidStack> fluidInputs;
    public List<FluidStack> fluidOutputs;

    public SimplifiedRecipeWrapper(SimplifiedMultiblockRecipe recipe) {
        recipe.setupJEI();
        this.inputs = recipe.getJEITotalItemInputs();
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
