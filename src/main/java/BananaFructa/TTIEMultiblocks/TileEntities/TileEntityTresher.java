package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.Utils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class TileEntityTresher extends SimplifiedTileEntityMultiblockMetal<TileEntityTresher, SimplifiedMultiblockRecipe> {

    static private final int time = 5 * 20;
    static private final int energy = 100;

    public static String[] items = new String[]{
            "<tfcflorae:food/amaranth>",
            "<tfcflorae:food/amaranth_grain>",
            "<tfcflorae:food/buckwheat>",
            "<tfcflorae:food/buckwheat_grain>",
            "<tfcflorae:food/fonio>",
            "<tfcflorae:food/fonio_grain>",
            "<tfcflorae:food/millet>",
            "<tfcflorae:food/millet_grain>",
            "<tfcflorae:food/quinoa>",
            "<tfcflorae:food/quinoa_grain>",
            "<tfcflorae:food/spelt>",
            "<tfcflorae:food/spelt_grain>",
            "<tfc:food/barley>",
            "<tfc:food/barley_grain>",
            "<tfc:food/maize>",
            "<tfc:food/maize_grain>",
            "<tfc:food/oat>",
            "<tfc:food/oat_grain>",
            "<tfc:food/rice>",
            "<tfc:food/rice_grain>",
            "<tfc:food/rye>",
            "<tfc:food/rye_grain>",
            "<tfc:food/wheat>",
            "<tfc:food/wheat_grain>",
            "<tfcflorae:crop/product/rape>",
            "<tfcflorae:food/rape_seed>"
    };

    public static List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        for (int i = 0;i < items.length;i+=2) {
            add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId(items[i])},new FluidStack[0], new ItemStack[]{Utils.itemStackFromCTId(items[i+1])},new FluidStack[0],energy,time));
        }
    }};


    public TileEntityTresher() {
        super(TTIEContent.tresherMultiblock, 16000, false,recipes);
    }

    @Override
    public void initPorts() {
        addEnergyPort(13);
        int outputHandler = registerItemHandler(1,new boolean[]{false},new boolean[]{true});
        int inputHandler = registerItemHandler(1,new boolean[]{true},new boolean[]{false});
        registerItemPort(1,outputHandler, PortType.OUTPUT, EnumFacing.SOUTH);
        registerItemPort(16,inputHandler,PortType.INPUT,EnumFacing.NORTH);
    }
}
