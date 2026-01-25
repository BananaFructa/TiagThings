package BananaFructa.AdvancedTFCTech;

import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import BananaFructa.TiagThings.Utils;
import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import com.pyraliron.advancedtfctech.crafting.PowerLoomRecipe;
import com.pyraliron.advancedtfctech.init.ModItems;
import com.pyraliron.advancedtfctech.te.TileEntityPowerLoom;
import com.pyraliron.advancedtfctech.util.inventory.ATTInventoryHandler;
import net.dries007.tfc.objects.items.ItemsTFC;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.ItemHandlerHelper;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryStorage;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ModifiedTileEntityPowerLoomParent extends TileEntityPowerLoom.TileEntityPowerLoomParent {

    static {
        for (PowerLoomRecipe recipe : PowerLoomRecipe.recipeList) {
            Utils.writeDeclaredField(MultiblockRecipe.class,recipe,"totalProcessEnergy",0,false);
        }
    }

    private RotaryStorage rotaryStorage;

    public ModifiedTileEntityPowerLoomParent() {
        super();
        //Utils.writeDeclaredField(TileEntityMultiblockMetal.class,this,"energyStorage",new FluxStorageAdvanced(0),true);
        rotaryStorage = new RotaryStorage();
        this.inputHandler = new ATTInventoryHandler(8, this, 0, true, false, 1) {
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                if (stack.isEmpty()) {
                    return stack;
                } else {
                    if (slot == 16) {
                        stack = new ItemStack(stack.getItem(), Math.min(stack.getCount(), 16), stack.getMetadata());
                    } else {
                        stack = stack.copy();
                    }

                    List<Integer> possibleSlots = new ArrayList(8);
                    int i;
                    ItemStack herex;
                    int j;
                    if (slot < 8) {
                        for(i = 0; i < 8; ++i) {
                            herex = (ItemStack)inventory.get(i);

                            for(j = 0; j < 8; ++j) {
                                if (!((ItemStack)inventory.get(j)).isEmpty() && !((ItemStack)inventory.get(j)).getItem().equals(stack.getItem())) {
                                    return stack;
                                }
                            }

                            for(j = 13; j < 17; ++j) {
                                if (!inputMatchesSecondaryFixed(stack, (ItemStack)inventory.get(j), true)) {
                                    return stack;
                                }
                            }

                            if (herex.isEmpty()) {
                                if (!simulate) {
                                    inventory.set(i, stack);
                                }

                                return ItemStack.EMPTY;
                            }

                            if (ItemHandlerHelper.canItemStacksStack(stack, herex) && herex.getCount() < 1) {
                                possibleSlots.add(i);
                            }
                        }
                    } else if (slot >= 13 && slot < 16) {
                        for(i = 13; i < 16; ++i) {
                            herex = (ItemStack)inventory.get(i);

                            for(j = 0; j < 8; ++j) {
                                if (!inputMatchesSecondaryFixed((ItemStack)inventory.get(j), stack, true)) {
                                    return stack;
                                }
                            }

                            for(j = 13; j < 17; ++j) {
                                if (!((ItemStack)inventory.get(j)).isEmpty() && (!((ItemStack)inventory.get(j)).getItem().equals(stack.getItem()) || ((ItemStack)inventory.get(j)).getMetadata() != stack.getMetadata())) {
                                    return stack;
                                }
                            }

                            if (herex.isEmpty()) {
                                if (!simulate) {
                                    inventory.set(i, stack);
                                }

                                return ItemStack.EMPTY;
                            }

                            if (ItemHandlerHelper.canItemStacksStack(stack, herex) && herex.getCount() < 64) {
                                possibleSlots.add(i);
                            }
                        }
                    } else if (slot >= 16 && slot < 17) {
                        for(i = 16; i < 17; ++i) {
                            herex = (ItemStack)inventory.get(i);

                            for(j = 0; j < 8; ++j) {
                                if (!inputMatchesSecondaryFixed((ItemStack)inventory.get(j), stack, true)) {
                                    return stack;
                                }
                            }

                            for(j = 13; j < 17; ++j) {
                                if (!((ItemStack)inventory.get(j)).isEmpty() && (!((ItemStack)inventory.get(j)).getItem().equals(stack.getItem()) || ((ItemStack)inventory.get(j)).getMetadata() != stack.getMetadata())) {
                                    return stack;
                                }
                            }

                            if (herex.isEmpty()) {
                                if (!simulate) {
                                    inventory.set(i, stack);
                                }

                                return ItemStack.EMPTY;
                            }

                            if (ItemHandlerHelper.canItemStacksStack(stack, herex) && herex.getCount() < 16) {
                                possibleSlots.add(i);
                            }
                        }
                    }

                    Collections.sort(possibleSlots, (a, b) -> {
                        return Integer.compare(((ItemStack)inventory.get(a)).getCount(), ((ItemStack)inventory.get(b)).getCount());
                    });
                    Iterator var9 = possibleSlots.iterator();

                    do {
                        if (!var9.hasNext()) {
                            return stack;
                        }

                        int ix = (Integer)var9.next();
                        ItemStack here = (ItemStack)inventory.get(ix);
                        int fillCount = Math.min(here.getMaxStackSize() - here.getCount(), stack.getCount());
                        if (!simulate) {
                            here.grow(fillCount);
                        }

                        stack.shrink(fillCount);
                    } while(!stack.isEmpty());

                    return ItemStack.EMPTY;
                }
            }
        };
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.writeCustomNBT(nbt, descPacket);
        nbt.setTag("rotary",rotaryStorage.toNBT());
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.readCustomNBT(nbt, descPacket);
        rotaryStorage.fromNBT(nbt.getCompoundTag("rotary"));
    }

    @Override
    public void update() {
        super.func_73660_a();
        if (isDummy()) return;
        this.energyStorage.setEnergy(16000);
        IEUtils.inputRotaryPower(this,getPosInput(),rotaryStorage);
    }

    @Override
    public boolean additionalCanProcessCheck(MultiblockProcess<IMultiblockRecipe> process) {
        return rotaryStorage.getOutputTorque() >= getMinTorque() && rotaryStorage.getOutputRotationSpeed() >= getMinSpeed() && rotaryStorage.getOutputRotationSpeed() <= getMaxSpeed();
    }

    public static float getMinTorque() {
        return 10;
    }

    public static float getMinSpeed() {
        return 150;
    }

    public static float getMaxSpeed() {
        return 200;
    }

    public static int getPosInput() {return 28;}

    @Override
    public int getEnergyStored(@Nullable EnumFacing fd) {
        return -1;
    }

    public static boolean inputMatchesSecondaryFixed(ItemStack input, ItemStack secondary, boolean allowEmpty) {
        if (input.isEmpty() || secondary.isEmpty()) {return allowEmpty;}
        for (PowerLoomRecipe r : PowerLoomRecipe.recipeList) {
            if (Utils.ItemStacksEqualNoCount(r.input.stack,input) && Utils.ItemStacksEqualNoCount(r.secondaryInput.stack,secondary)) return true;
        }
        System.out.println("AAAAAAAAAAAAAAAA");
        return false;
    }
}
