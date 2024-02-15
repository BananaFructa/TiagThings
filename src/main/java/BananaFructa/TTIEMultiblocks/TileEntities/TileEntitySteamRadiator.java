package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.Utils;
import blusunrize.immersiveengineering.common.IEContent;
import mods.railcraft.common.fluids.RailcraftFluids;
import nc.init.NCFluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TileEntitySteamRadiator extends SimplifiedTileEntityMultiblockMetal<TileEntitySteamRadiator, SimplifiedMultiblockRecipe> {

    public static List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(RailcraftFluids.STEAM.getBlock().getFluid(), 100)},new ItemStack[0],new FluidStack[]{new FluidStack(NCFluids.fluidPairList.stream().filter(p->p.getLeft().getName().equals("exhaust_steam")).collect(Collectors.toList()).get(0).getLeft(),100)},0,20));
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:low_quality_steam>"), 100)},new ItemStack[0],new FluidStack[]{null},0,20));
    }};

    public TileEntitySteamRadiator() {
        super(TTIEContent.steamRadiatorMultiblock, 0, false,recipes);
    }

    @Override
    public void initPorts() {
        int inputTank = registerFluidTank(2000);
        int outputTank = registerFluidTank(2000);
        registerFluidPort(2,inputTank, PortType.INPUT, EnumFacing.WEST);
        registerFluidPort(0,outputTank,PortType.OUTPUT,EnumFacing.EAST);
    }
}
