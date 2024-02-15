package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;
import java.util.List;

public class TileEntityCCM extends SimplifiedTileEntityMultiblockMetal<TileEntityCCM, SimplifiedMultiblockRecipe> {

    private static final String[] items = new String[] {
            "<tfc:metal/ingot/bismuth>",
            "<tfc:metal/ingot/bismuth_bronze>",
            "<tfc:metal/ingot/black_bronze>",
            "<tfc:metal/ingot/brass>",
            "<tfc:metal/ingot/bronze>",
            "<tfc:metal/ingot/copper>",
            "<tfc:metal/ingot/gold>",
            "<tfc:metal/ingot/lead>",
            "<tfc:metal/ingot/nickel>",
            "<tfc:metal/ingot/rose_gold>",
            "<tfc:metal/ingot/silver>",
            "<tfc:metal/ingot/tin>",
            "<tfc:metal/ingot/zinc>",
            "<tfc:metal/ingot/sterling_silver>",
            "<tfc:metal/ingot/wrought_iron>",
            "<tfc:metal/ingot/pig_iron>",
            "<tfc:metal/ingot/steel>",
            "<tfc:metal/ingot/platinum>",
            "<tfc:metal/ingot/weak_steel>",
            "<tfc:metal/ingot/high_carbon_steel>",
            "<tfc:metal/ingot/unknown>",
            "<tfc:metal/ingot/aluminium>",
            "<tfc:metal/ingot/constantan>",
            "<tfc:metal/ingot/electrum>",
            "<tfc:metal/ingot/boron>",
            "<tfc:metal/ingot/titanium>",
            "<tfc:metal/ingot/tungsten>",
            "<tfc:metal/ingot/duraluminium>",
            "<tfc:metal/ingot/magnesium>",
            "<tfc:metal/ingot/manganese>",
            "<tfc:metal/ingot/zirconium>",
            "<tfc:metal/ingot/advanced_electronic_alloy>",
            "<tfc:metal/ingot/mild_steel>",
            "<tfc:metal/ingot/beryllium>",
            "<tfc:metal/ingot/lithium>",
            "<tfc:metal/ingot/tough>",
            "<tfc:metal/ingot/hsla_steel>",
            "<tfc:metal/ingot/zircaloy>",
            "<tfc:metal/ingot/ferroboron>",
            "<tfc:metal/ingot/magnesium_diboride>"
    };

    private static String[] fluids = new String[] {
            "bismuth",
            "bismuth_bronze",
            "black_bronze",
            "brass",
            "bronze",
            "copper",
            "gold",
            "lead",
            "nickel",
            "rose_gold",
            "silver",
            "tin",
            "zinc",
            "sterling_silver",
            "wrought_iron",
            "pig_iron",
            "steel",
            "platinum",
            "weak_steel",
            "high_carbon_steel",
            "unknown",
            "aluminium",
            "constantan",
            "electrum",
            "boron",
            "titanium",
            "tungsten",
            "duraluminium",
            "magnesium",
            "manganese_dioxide",
            "zirconium",
            "advanced_electronic_alloy",
            "mild_steel",
            "beryllium",
            "lithium",
            "tough",
            "hsla_steel",
            "zircaloy",
            "ferroboron",
            "magnesium_diboride"
    };

    public static List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        for (int i = 0;i < items.length;i++) {
            add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId(fluids[i]),100)},new ItemStack[]{Utils.itemStackFromCTId(items[i])},new FluidStack[0],20*5,100));
        }
    }};
    public TileEntityCCM() {
        super(TTIEContent.ccm, 16000, false,recipes);
    }

    @Override
    public int getProcessQueueMaxLength() {
        return 1;
    }

    @Override
    public void initPorts() {
        int fluidIn = registerFluidTank(100);
        registerFluidPort(16,fluidIn,PortType.INPUT,EnumFacing.UP);
        addEnergyPort(21);
        registerItemPort(15,-1,PortType.OUTPUT,EnumFacing.NORTH);
    }
}
