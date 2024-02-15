package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.TTMain;
import blusunrize.immersiveengineering.common.IEContent;
import nc.init.NCFluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TileEntityOutdoorACUnit extends SimplifiedTileEntityMultiblockMetal<TileEntityOutdoorACUnit, SimplifiedMultiblockRecipe> {

    public static List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(NCFluids.fluidPairList.stream().filter(p->p.getLeft().getName().equals("fluoromethane")).collect(Collectors.toList()).get(0).getLeft(),500)},new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.coldF,500)},2000,10));
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.hotF,500)},new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.coldF,500)},2000,10));
    }};
    public TileEntityOutdoorACUnit() {
        super(TTIEContent.outdoorAcUnitMultiblock, 16000, false,recipes);
    }

    @Override
    public void initPorts() {
        addEnergyPort(3);
        int inputTank = registerFluidTank(2000);
        int outputTank = registerFluidTank(2000);
        registerFluidPort(0,inputTank, PortType.INPUT, EnumFacing.SOUTH);
        registerFluidPort(2,outputTank,PortType.OUTPUT,EnumFacing.SOUTH);
    }
}
