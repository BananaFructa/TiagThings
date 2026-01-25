package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class TileEntityMagnetizer extends SimplifiedTileEntityMultiblockMetal<TileEntityMagnetizer, SimplifiedMultiblockRecipe> {

    public static List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:metal/sheet/nickel>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:magnetized_nickel_sheet>")},new FluidStack[0],300,10*20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:metal/sheet/steel>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:v_magnetized>")},new FluidStack[0],300,10*20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:metal/sheet/crucible_steel>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:v_magnetized>")},new FluidStack[0],300,10*20,true));
    }};
    public TileEntityMagnetizer() {
        super(TTIEContent.magnetizer, 16000, false,recipes);
    }

    @Override
    public void initPorts() {
        addEnergyPort(0);
        int itemIn = registerItemHandler(1,new boolean[]{true},new boolean[]{true});
        registerItemPort(0,itemIn, PortType.INPUT, EnumFacing.DOWN);
        int itemOut = registerItemHandler(1,new boolean[]{true},new boolean[]{true});
        registerItemPort(0,itemOut,PortType.OUTPUT,EnumFacing.DOWN);
    }
}
