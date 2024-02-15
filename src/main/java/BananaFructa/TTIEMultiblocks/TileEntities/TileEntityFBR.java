package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.Utils;
import micdoodle8.mods.galacticraft.core.GCFluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;
import java.util.List;

public class TileEntityFBR extends SimplifiedTileEntityMultiblockMetal<TileEntityFBR, SimplifiedMultiblockRecipe> {

    public static List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:rutile_powder>",20)},new FluidStack[]{new FluidStack(FluidRegistry.getFluid("tt_chlorine"),1000)},new ItemStack[0],new FluidStack[]{new FluidStack(FluidRegistry.getFluid("ticl"),1000)},0,20*20));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:zirconia_powder>",20)},new FluidStack[]{new FluidStack(FluidRegistry.getFluid("tt_chlorine"),1000)},new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:zrcl>"),1000)},0,20*20));
    }};
    public TileEntityFBR() {
        super(TTIEContent.fbr, 0, false,recipes);
    }

    @Override
    public int getProcessQueueMaxLength() {
        return 1;
    }

    @Override
    public void initPorts() {
        int inputItem = registerItemHandler(1,new boolean[]{true},new boolean[]{true});
        registerItemPort(5,inputItem,PortType.INPUT,EnumFacing.WEST);
        int tankOut = registerFluidTank(2000);
        registerFluidPort(3,tankOut,PortType.OUTPUT,EnumFacing.EAST);
        int tank = registerFluidTank(2000);
        registerFluidPort(23,tank,PortType.INPUT,EnumFacing.WEST);
    }
}
