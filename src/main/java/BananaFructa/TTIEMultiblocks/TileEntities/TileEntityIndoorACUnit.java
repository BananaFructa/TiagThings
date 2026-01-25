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

public class TileEntityIndoorACUnit extends SimplifiedTileEntityMultiblockMetal<TileEntityIndoorACUnit, SimplifiedMultiblockRecipe> {

    public static List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.coldF,500)},new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.hotF,500)},0,10));
    }};

    public TileEntityIndoorACUnit() {
        super(TTIEContent.indoorAcUnitMultiblock, 0, false,recipes);
    }

    @Override
    public void initPorts() {
        int inputTank = registerFluidTank(2000);
        int outputTank = registerFluidTank(2000);
        registerFluidPort(2,inputTank, PortType.INPUT, EnumFacing.SOUTH);
        registerFluidPort(0,outputTank,PortType.OUTPUT,EnumFacing.SOUTH);
    }
}
