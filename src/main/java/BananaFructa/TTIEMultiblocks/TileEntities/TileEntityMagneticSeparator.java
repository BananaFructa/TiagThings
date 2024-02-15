package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.STEMMInventoryHandler;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.Utils;
import net.dries007.tfc.objects.fluids.FluidsTFC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;
import java.util.List;

public class TileEntityMagneticSeparator extends SimplifiedTileEntityMultiblockMetal<TileEntityMagneticSeparator, SimplifiedMultiblockRecipe> {

    public static final List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(
                new ItemStack[]{Utils.itemStackFromCTId("<tfc:powder/hematite>",32)},
                new FluidStack[]{new FluidStack(FluidsTFC.FRESH_WATER.get(),10)},
                new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:rough_iron_powder>")},
                new FluidStack[0],
                20,20*60
                ));
        add(new SimplifiedMultiblockRecipe(
                new ItemStack[]{Utils.itemStackFromCTId("<tfc:powder/limonite>",32)},
                new FluidStack[]{new FluidStack(FluidsTFC.FRESH_WATER.get(),10)},
                new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:rough_iron_powder>")},
                new FluidStack[0],
                20,20*60
        ));
        add(new SimplifiedMultiblockRecipe(
                new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:magnetite_powder>",32)},
                new FluidStack[]{new FluidStack(FluidsTFC.FRESH_WATER.get(),10)},
                new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:rough_iron_powder>")},
                new FluidStack[0],
                20,20*60
        ));
    }};

    public TileEntityMagneticSeparator() {
        super(TTIEContent.magneticSeparator, 16000, false,recipes);
    }

    @Override
    public void initPorts() {
        addEnergyPort(2);
        int num = registerItemHandler(1,new boolean[]{true},new boolean[]{false});
        registerItemPort(1,num,PortType.INPUT,EnumFacing.SOUTH);
        int tank = registerFluidTank(100);
        registerFluidPort(6,tank,PortType.INPUT,EnumFacing.NORTH);
        registerItemPort(5,-1,PortType.OUTPUT,EnumFacing.NORTH);
    }

    @Override
    protected int registerItemHandler(int slots, boolean[] canInsert, boolean[] canExtract) {
        STEMMInventoryHandler handler = new STEMMInventoryHandler(slots,this,slotCounter,canInsert,canExtract) {
            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {

                ItemStack is = stack.copy();
                int mod = is.getCount() % 32;
                is.shrink(mod);
                ItemStack returned = super.insertItem(slot, is, simulate);
                return new ItemStack(stack.getItem(),returned.getCount() + mod);
            }
        };
        slotCounter += slots;
        inventoryHandlers.add(handler);
        return inventoryHandlers.size() - 1;
    }
}
