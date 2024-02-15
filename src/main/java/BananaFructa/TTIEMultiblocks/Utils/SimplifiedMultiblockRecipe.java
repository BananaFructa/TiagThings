package BananaFructa.TTIEMultiblocks.Utils;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class SimplifiedMultiblockRecipe extends MultiblockRecipe {

    public int totalProcessTime;
    public int totalProcessEnergy;
    boolean consumeFirst = false;

    public SimplifiedMultiblockRecipe(ItemStack[] itemInputs,FluidStack[] fluidInputs,ItemStack[] itemOutputs,FluidStack[] fluidOutput,int energyPerTick,int duration) {
        this.inputList = new ArrayList<>();
        for (ItemStack i : itemInputs) this.inputList.add(new IngredientStack(i));
        this.fluidInputList = new ArrayList<>(Arrays.asList(fluidInputs));

        this.outputList = NonNullList.create(); // tf ?
        this.outputList.addAll(Arrays.asList(itemOutputs));

        this.fluidOutputList = new ArrayList<>(Arrays.asList(fluidOutput));
        this.totalProcessTime = duration;
        this.totalProcessEnergy = energyPerTick * duration;
    }

    public SimplifiedMultiblockRecipe(ItemStack[] itemInputs,FluidStack[] fluidInputs,ItemStack[] itemOutputs,FluidStack[] fluidOutput,int energyPerTick,int duration,boolean consumeFirst) {
        this.inputList = new ArrayList<>();
        for (ItemStack i : itemInputs) this.inputList.add(new IngredientStack(i));
        this.fluidInputList = new ArrayList<>(Arrays.asList(fluidInputs));

        this.outputList = NonNullList.create(); // tf ?
        this.outputList.addAll(Arrays.asList(itemOutputs));

        this.fluidOutputList = new ArrayList<>(Arrays.asList(fluidOutput));
        this.totalProcessTime = duration;
        this.totalProcessEnergy = energyPerTick * duration;
        this.consumeFirst = consumeFirst;
    }

    @Override
    public int getTotalProcessEnergy() {
        return totalProcessEnergy;
    }

    @Override
    public int getTotalProcessTime() {
        return totalProcessTime;
    }

    @Override
    public int getMultipleProcessTicks() {
        return 0;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setInteger("inputItems",inputList.size());
        nbtTagCompound.setInteger("inputFluids",fluidInputList.size());
        for (int i = 0;i < inputList.size();i++) {
            if (this.inputList.get(i) != null) nbtTagCompound.setTag("inputItem-"+i,this.inputList.get(i).stack.writeToNBT(new NBTTagCompound()));
            else {
                NBTTagCompound compound = new NBTTagCompound();
                compound.setBoolean("isNull",true);
                nbtTagCompound.setTag("inputItem-"+i,compound);
            }
        }
        for (int i = 0;i < fluidInputList.size();i++) {
            if (this.fluidInputList.get(i) != null) nbtTagCompound.setTag("inputFluid-"+i,this.fluidInputList.get(i).writeToNBT(new NBTTagCompound()));
            else {
                NBTTagCompound compound = new NBTTagCompound();
                compound.setBoolean("isNull",true);
                nbtTagCompound.setTag("inputFluid-"+i,compound);
            }
        }
        return nbtTagCompound;
    }

    @Override
    public boolean shouldCheckItemAvailability() {
        return false;
    }
}
