package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.TTMain;
import BananaFructa.TiagThings.Utils;
import micdoodle8.mods.galacticraft.core.GCFluids;
import nc.init.NCFluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TileEntityShaftFurnace extends SimplifiedTileEntityMultiblockMetal<TileEntityShaftFurnace, SimplifiedMultiblockRecipe> {

    public static List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:amon_hexcl>")},new FluidStack[]{new FluidStack(GCFluids.fluidHydrogenGas,1000)},new ItemStack[]{Utils.itemStackFromCTId("<tfc:metal/dust/platinum>")},new FluidStack[0],0,20*10));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:wolframite_powder>",20)},new FluidStack[]{new FluidStack(GCFluids.fluidHydrogenGas,1000)},new ItemStack[]{Utils.itemStackFromCTId("<tfc:metal/dust/tungsten>")},new FluidStack[0],0,20*10));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:rough_iron_powder>")},new FluidStack[]{new FluidStack(GCFluids.fluidHydrogenGas,1600)},new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:direct_reduced_iron>")},new FluidStack[0],0,20*16));
    }};
    public TileEntityShaftFurnace() {
        super(TTIEContent.shaftFurnace, 0, false,recipes);
    }

    @Override
    public int getProcessQueueMaxLength() {
        return 1;
    }

    @Override
    public void initPorts() {
        int input = registerItemHandler(1,new boolean[]{true},new boolean[]{false});
        registerItemPort(175,input,PortType.INPUT,EnumFacing.UP);
        registerItemPort(22,-1,PortType.OUTPUT,EnumFacing.DOWN);
        int fluidIn = registerFluidTank(2000);
        registerFluidPort(113,fluidIn,PortType.INPUT,EnumFacing.WEST);
    }
}
