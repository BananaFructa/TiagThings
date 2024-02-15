package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.TTMain;
import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;
import java.util.List;

public class TileEntityWaterFilter extends SimplifiedTileEntityMultiblockMetal<TileEntityWaterFilter, SimplifiedMultiblockRecipe> {

    public static List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.clarifiedWater,1)},new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.treatedWaterNN,1)},0,1));
    }};

    public TileEntityWaterFilter() {
        super(TTIEContent.waterFilter, 0, false,recipes);
    }

    @Override
    public void initPorts() {
        int inputTank = registerFluidTank(2500);
        int outputTank = registerFluidTank(2500);
        registerFluidPort(4,outputTank,PortType.OUTPUT,EnumFacing.NORTH);
        registerFluidPort(3,inputTank,PortType.INPUT,EnumFacing.SOUTH);
    }
}
