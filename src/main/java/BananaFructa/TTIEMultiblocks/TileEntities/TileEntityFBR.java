package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.RockTraces;
import BananaFructa.TiagThings.RockUtils;
import BananaFructa.TiagThings.Utils;
import micdoodle8.mods.galacticraft.core.GCFluids;
import net.dries007.tfc.api.types.Rock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;
import java.util.List;

public class TileEntityFBR extends SimplifiedTileEntityMultiblockMetal<TileEntityFBR, SimplifiedMultiblockRecipe> {

    public static List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:rutile_powder>",20)},new FluidStack[]{new FluidStack(FluidRegistry.getFluid("tt_chlorine"),1000)},new ItemStack[0],new FluidStack[]{new FluidStack(FluidRegistry.getFluid("ticl"),1000)},0,20*20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:zirconia_powder>",20)},new FluidStack[]{new FluidStack(FluidRegistry.getFluid("tt_chlorine"),1000)},new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:zrcl>"),1000)},0,20*20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:mg_silicon>")},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:hcl>"),1000)},new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:trichlorosilane>"),1000)},0,20*20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:bauxite_powder>",10)},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:sodium_hydroxide_solution>"),1000)},new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:alumina_oxide_sol>"),1000)},0,20*20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{RockTraces.ALUMINIUM.dustFor(40)},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:sodium_hydroxide_solution>"),1000)},new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:alumina_oxide_sol>"),1000)},0,20*20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:fluorite_powder>")},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:sulfuric_acid>"),1000)},new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:hydrofluoric_acid>"),1000)},0,20*20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:metal/dust/wrought_iron>")},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:tt_chlorine>"),500)},new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:etching_acid>"),1000)},0,20*20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:metal/dust/mild_steel>")},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:tt_chlorine>"),500)},new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:etching_acid>"),1000)},0,20*20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:pitchblende_powder>")},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:sulfuric_acid>"),1000)},new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:uranium_sulfate>"),1000)},0,20*20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{RockTraces.URANIUM.dustFor(40)},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:sulfuric_acid>"),1000)},new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:uranium_sulfate>"),1000)},0,20*20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:powder/flux>")},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:sulfuric_acid>"),666)},new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:calcium_sulfate_solution>"),666)},0,20*20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:beryl_powder>")},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:sulfuric_acid>"),250)},new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:beryllium_sulfide_solution>"),250)},0,10*20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{RockTraces.COPPER.dustFor(40)},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:sulfuric_acid>"),100)},new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:copper_sulfate_sol>"),100)},0,10*20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{RockTraces.GOLD.dustFor(40)},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:cy_sol>"),100)},new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:aurcy_sod_hy>"),100)},0,10*20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{RockTraces.PLATINUM.dustFor(40)},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:aqua_regia>"),100)},new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:hexplat_acid>"),100)},0,10*20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{RockTraces.TIN.dustFor(40)},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:hcl>"),100)},new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:tin_chl_sol>"),100)},0,10*20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{RockTraces.NICKEL.dustFor(40)},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:sulfuric_acid>"),100)},new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:nick_sulf_sol>"),100)},0,10*20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{RockTraces.SILVER.dustFor(40)},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:cy_sol>"),100)},new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:slv_cy_sol>"),100)},0,10*20,true));
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
