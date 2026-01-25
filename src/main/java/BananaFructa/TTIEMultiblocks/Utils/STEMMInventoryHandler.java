package BananaFructa.TTIEMultiblocks.Utils;

import BananaFructa.TTIEMultiblocks.IECopy.BlockTTBase;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class STEMMInventoryHandler extends IEInventoryHandler implements IInventory {

    int offset = 0;
    boolean[] canInsert;
    boolean[] canExtract;
    SimplifiedTileEntityMultiblockMetal<?,?> parent;
    boolean fluidOnly = false;

    public List<Integer> recipeInputCorelation = new ArrayList<>();

    public STEMMInventoryHandler(int slots, SimplifiedTileEntityMultiblockMetal parent, int slotOffset, boolean[] canInsert, boolean[] canExtract) {
        super(slots, parent, slotOffset, canInsert, canExtract);
        this.offset = slotOffset;
        this.canInsert = canInsert;
        this.canExtract = canExtract;
        this.parent = parent;
    }

    public void addRecipeFlowCorelation(int id) {
        recipeInputCorelation.add(id);
    }

    // TODO: there is a bit of wierd logic here will need to clean up later
    public boolean allowItem(int slot, ItemStack is) {
        if (fluidOnly) {
            IFluidHandler fh = (IFluidHandler)getStackInSlot(0).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, (EnumFacing)null);
            return fh != null;
        }

        if (recipeInputCorelation.isEmpty()) {
            return slot >= canInsert.length || canInsert[slot];
        }
        if (slot < recipeInputCorelation.size()) {
            for (SimplifiedMultiblockRecipe recipe : parent.recipes) {
                Integer s = recipeInputCorelation.get(slot);
                if (recipe.getItemInputs().size() > s && recipe.getItemInputs().get(s).stack.getItem() == is.getItem() && recipe.getItemInputs().get(s).stack.getMetadata() == is.getMetadata())
                    return true;

            }
        } else return false;
        return false;
    }

    public boolean getFluidOnly() {
        return fluidOnly;
    }

    public void setFluidOnly(boolean fluidOnly) {
        this.fluidOnly =fluidOnly;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (!allowItem(slot, stack)) return stack;
        return super.insertItem(slot, stack, simulate);
    }

    public ItemStack outputItem(int slot, ItemStack stack, boolean simulate) {
        return super.insertItem(slot, stack, simulate);
    }

    public int getOffset() {
        return offset;
    }

    public boolean[] getCanInsert() {
        return canInsert;
    }

    public boolean[] getCanExtract() {
        return canExtract;
    }

    @Override
    public int getSizeInventory() {
        return getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return super.getStackInSlot(slot);
    }

    @Override
    public boolean isEmpty() {
        boolean empty = true;
        for (int i = 0;i < getSlots();i++) {
            empty &= getStackInSlot(i).isEmpty();
        }
        return empty;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack other = getStackInSlot(index).copy();
        getStackInSlot(index).shrink(count);
        other.shrink(getStackInSlot(index).getCount());
        markDirty();
        return other;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack is = getStackInSlot(index);
        setStackInSlot(index,ItemStack.EMPTY);
        markDirty();
        return is;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        setStackInSlot(index,stack);
        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return getSlotLimit(0);
    }

    @Override
    public void markDirty() {
        parent.markDirty();
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return allowItem(index-36,stack);
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        for (int i = 0;i < getSlots();i++) {
            setStackInSlot(i,ItemStack.EMPTY);
        }
        markDirty();
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        IBlockState blockState = parent.getWorld().getBlockState(parent.getPos());
        if (blockState.getBlock() instanceof BlockTTBase) {
            BlockTTBase<? extends IStringSerializable> b = (BlockTTBase<? extends IStringSerializable>) blockState;
            return new TextComponentString(blockState.getValue(b.property).getName());
        }
        return new TextComponentString("N/A");
    }
}
