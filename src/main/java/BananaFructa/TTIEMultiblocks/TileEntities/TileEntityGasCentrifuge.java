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

public class TileEntityGasCentrifuge extends SimplifiedTileEntityMultiblockMetal<TileEntityGasCentrifuge, SimplifiedMultiblockRecipe> {

    public static int time = 2;

    public static List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.uhexf1,100)},new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.uhexf2,25),new FluidStack(TTMain.uhexf,75)},100,time));
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.uhexf2,100)},new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.uhexf3,25),new FluidStack(TTMain.uhexf1,75)},100,time));
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.uhexf3,100)},new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.uhexf4,26),new FluidStack(TTMain.uhexf2,74)},100,time));
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.uhexf4,100)},new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.uhexf5,27),new FluidStack(TTMain.uhexf3,73)},100,time));
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.uhexf5,100)},new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.uhexf6,32),new FluidStack(TTMain.uhexf4,68)},100,time));
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.uhexf6,100)},new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.uhexf7,43),new FluidStack(TTMain.uhexf5,57)},100,time));
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.uhexf7,100)},new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.uhexf8,56),new FluidStack(TTMain.uhexf6,44)},100,time));
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.uhexf8,100)},new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.uhexf9,66),new FluidStack(TTMain.uhexf7,34)},100,time));
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.uhexf9,100)},new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.uhexf10,71),new FluidStack(TTMain.uhexf8,29)},100,time));
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.uhexf10,100)},new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.uhexf11,73),new FluidStack(TTMain.uhexf9,27)},100,time));
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.uhexf11,100)},new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.uhexf12,74),new FluidStack(TTMain.uhexf10,24)},100,time));
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.uhexf12,100)},new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.uhexf13,74),new FluidStack(TTMain.uhexf11,26)},100,time));
    }};

    public TileEntityGasCentrifuge() {
        super(TTIEContent.gasCentrifugeMultiblock, 16000, false,recipes);
    }

    @Override
    public void initPorts() {
        addEnergyPort(0);
        int inputTank = registerFluidTank(1000);
        int outputTank0 = registerFluidTank(1000);
        int outputTank1 = registerFluidTank(1000);
        registerFluidPort(0,inputTank, PortType.INPUT, EnumFacing.NORTH);
        registerFluidPort(0,outputTank0,PortType.OUTPUT,EnumFacing.SOUTH);
        registerFluidPort(0,outputTank1,PortType.OUTPUT,EnumFacing.WEST);
    }
}
