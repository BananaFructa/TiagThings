package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.Items.ItemLoaderHandler;
import BananaFructa.TiagThings.Utils;
import blusunrize.immersiveengineering.common.IEContent;
import mctmods.immersivetechnology.common.ITContent;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;
import java.util.List;

public class TileEntityUMPLM extends SimplifiedTileEntityMultiblockMetal<TileEntityUMPLM, SimplifiedMultiblockRecipe> {

    public static List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{new ItemStack(ItemLoaderHandler.siliconWafer,1)},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:hydrogen>"),10000)}, new ItemStack[]{new ItemStack(ItemLoaderHandler.printedSiliconWafer1,1)},new FluidStack[0],2048,60 * 20));
    }};

    public TileEntityUMPLM() {
        super(TTIEContent.umPhotolithographyMachine, 16000, false,recipes);
    }

    @Override
    public void initPorts() {
        addEnergyPort(13);
        addRedstonePorts(11);
        int itemInput = registerItemHandler(1,new boolean[]{true},new boolean[]{false});
        int itemOutput = registerItemHandler(1,new boolean[]{false},new boolean[]{true});
        registerItemPort(8,itemInput,PortType.INPUT,EnumFacing.WEST);
        registerItemPort(6,itemOutput,PortType.OUTPUT,EnumFacing.EAST);
        int fluidInput = registerFluidTank(10000);
        registerFluidPort(12,fluidInput,PortType.INPUT,EnumFacing.SOUTH);
    }
}
