package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.Utils;
import com.eerussianguy.firmalife.init.FoodFL;
import com.eerussianguy.firmalife.init.FruitTreeFL;
import com.eerussianguy.firmalife.recipe.DryingRecipe;
import com.eerussianguy.firmalife.registry.ItemsFL;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import tfcflorae.objects.items.ItemsTFCF;
import tfcflorae.util.agriculture.FruitsTFCF;

import java.util.ArrayList;
import java.util.List;

public class TileEntityElectricOven extends SimplifiedTileEntityMultiblockMetal<TileEntityElectricOven, SimplifiedMultiblockRecipe> {

    public static List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:raw_electrode>")},new FluidStack[0], new ItemStack[]{Utils.itemStackFromCTId("<immersiveengineering:graphite_electrode>")},new FluidStack[0],500,120*2*20));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:petcoke_powder>")},new FluidStack[0], new ItemStack[]{Utils.itemStackFromCTId("<tfc:powder/graphite>")},new FluidStack[0],500,120*2*20));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:rock/chalk>")},new FluidStack[0], new ItemStack[]{Utils.itemStackFromCTId("<tfctech:powder/lime>",2)},new FluidStack[0],500,20*10));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:rock/limestone>")},new FluidStack[0], new ItemStack[]{Utils.itemStackFromCTId("<tfctech:powder/lime>",2)},new FluidStack[0],500,20*10));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:rock/marble>")},new FluidStack[0], new ItemStack[]{Utils.itemStackFromCTId("<tfctech:powder/lime>",2)},new FluidStack[0],500,20*10));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:rock/dolomite>")},new FluidStack[0], new ItemStack[]{Utils.itemStackFromCTId("<tfctech:powder/lime>",2)},new FluidStack[0],500,20*10));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:rock/marble>")},new FluidStack[0], new ItemStack[]{Utils.itemStackFromCTId("<tfctech:powder/lime>",2)},new FluidStack[0],500,20*10));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:wet_bioplastic_resin_sheet>")},new FluidStack[0], new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:bioplastic_sheet>",1)},new FluidStack[0],500,20));
                }};

    public TileEntityElectricOven() {
        super(TTIEContent.electricOvenMultiblock, 16000, false,recipes);
    }

    @Override
    public void initPorts() {
        addEnergyPort(64);
        int outputHandler = registerItemHandler(1,new boolean[]{false},new boolean[]{true});
        int inputHandler = registerItemHandler(1,new boolean[]{true},new boolean[]{false});
        registerItemPort(29,outputHandler, PortType.OUTPUT, EnumFacing.WEST);
        registerItemPort(24,inputHandler,PortType.INPUT,EnumFacing.EAST);
    }
}
