package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.TTMain;
import blusunrize.immersiveengineering.common.IEContent;
import net.dries007.tfc.objects.fluids.FluidsTFC;
import net.minecraft.block.BlockLiquid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;

import java.util.ArrayList;
import java.util.List;

public class TileEntityClarifier extends SimplifiedTileEntityMultiblockMetal<TileEntityClarifier, SimplifiedMultiblockRecipe> {



    public static List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(FluidsTFC.FRESH_WATER.get(),1)},new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.clarifiedWater,1)},50,2));
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(FluidsTFC.SALT_WATER.get(),1)},new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.clarifiedSeaWater,1)},50,2));
    }};

    public TileEntityClarifier() {
        super(TTIEContent.clarifierMultiblock, 16000, true,recipes);
    }

    @Override
    public boolean canDoRecipe(SimplifiedMultiblockRecipe recipe) {
        return tanks.get(0).getFluidAmount() > 149990;
    }

    @Override
    public void initPorts() {
        int inputTank = registerFluidTank(150000);
        int outputTank = registerFluidTank(10000);
        registerFluidPort(120,inputTank, PortType.INPUT, EnumFacing.NORTH);
        registerFluidPort(110,outputTank,PortType.OUTPUT,EnumFacing.NORTH);
        addEnergyPort(131);
        addRedstonePorts(130);
    }
}
