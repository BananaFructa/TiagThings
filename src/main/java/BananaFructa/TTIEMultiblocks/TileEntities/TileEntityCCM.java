package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;
import java.util.List;

public class TileEntityCCM extends SimplifiedTileEntityMultiblockMetal<TileEntityCCM, TileEntityCCM.CCMRecipe> {

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
            "<tfc:metal/ingot/magnesium_diboride>",
            "<tfc:metal/ingot/crucible_steel>"
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
            "magnesium_diboride",
            "crucible_steel"
    };

    private static String[] plates = {
            "<tfc:metal/sheet/bismuth>",
            "<tfc:metal/sheet/bismuth_bronze>",
            "<tfc:metal/sheet/black_bronze>",
            "<tfc:metal/sheet/brass>",
            "<tfc:metal/sheet/bronze>",
            "<tfc:metal/sheet/copper>",
            "<tfc:metal/sheet/gold>",
            "<tfc:metal/sheet/lead>",
            "<tfc:metal/sheet/nickel>",
            "<tfc:metal/sheet/rose_gold>",
            "<tfc:metal/sheet/silver>",
            "<tfc:metal/sheet/tin>",
            "<tfc:metal/sheet/zinc>",
            "<tfc:metal/sheet/sterling_silver>",
            "<tfc:metal/sheet/wrought_iron>",
            "<tfc:metal/sheet/pig_iron>",
            "<tfc:metal/sheet/steel>",
            "<tfc:metal/sheet/platinum>",
            "<tfc:metal/sheet/aluminium>",
            "<tfc:metal/sheet/constantan>",
            "<tfc:metal/sheet/electrum>",
            "<tfc:metal/sheet/titanium>",
            "<tfc:metal/sheet/tungsten>",
            "<tfc:metal/sheet/boron>",
            "<tfc:metal/sheet/duraluminium>",
            "<tfc:metal/sheet/advanced_electronic_alloy>",
            "<tfc:metal/sheet/zirconium>",
            "<tfc:metal/sheet/magnesium>",
            "<tfc:metal/sheet/lithium>",
            "<tfc:metal/sheet/manganese>",
            "<tfc:metal/sheet/beryllium>",
            "<tfc:metal/sheet/mild_steel>",
            "<tfc:metal/sheet/crucible_steel>"
    };

    private static final String[] platesLiquids = new String[]{
            "bismuth",
            "bismuth_bronze",
            "black_bronze",
            "brass>",
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
            "aluminium",
            "constantan",
            "electrum",
            "titanium",
            "tungsten",
            "boron",
            "duraluminium",
            "advanced_electronic_alloy",
            "zirconium",
            "magnesium",
            "lithium",
            "manganese",
            "beryllium",
            "mild_steel",
            "crucible_steel"
    };

    CCMMode mode = CCMMode.INGOT;

    public static List<CCMRecipe> recipes = new ArrayList<CCMRecipe>() {{
        for (int i = 0;i < items.length;i++) {
            add(new CCMRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId(fluids[i]),100)},new ItemStack[]{Utils.itemStackFromCTId(items[i])},new FluidStack[0],20*5,100,CCMMode.INGOT));
        }
        add(new CCMRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("steel"),100)},new ItemStack[]{Utils.itemStackFromCTId("<railcraft:rail:3>")},new FluidStack[0],20*5,100,CCMMode.RAIL));
        add(new CCMRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("crucible_steel"),100)},new ItemStack[]{Utils.itemStackFromCTId("<railcraft:rail>")},new FluidStack[0],20*5,100,CCMMode.RAIL));
        add(new CCMRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("copper"),100)},new ItemStack[]{Utils.itemStackFromCTId("<railcraft:rail:5>")},new FluidStack[0],20*5,100,CCMMode.RAIL));
        for (int i = 0;i < plates.length;i++) {
            add(new CCMRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId(platesLiquids[i]),200)},new ItemStack[]{Utils.itemStackFromCTId(plates[i])},new FluidStack[0],20*5,100,CCMMode.PLATE));
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
        int fluidIn = registerFluidTank(200);
        registerFluidPort(16,fluidIn,PortType.INPUT,EnumFacing.UP);
        addEnergyPort(21);
        registerItemPort(15,-1,PortType.OUTPUT,EnumFacing.NORTH);
    }

    public CCMMode getMode() {
        return mode;
    }

    public enum CCMMode {
        INGOT("Ingot"),
        RAIL("Rail"),
        PLATE("Plate");

        String name;

        CCMMode(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.writeCustomNBT(nbt, descPacket);
        nbt.setInteger("mode",mode.ordinal());
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.readCustomNBT(nbt, descPacket);
        mode = CCMMode.values()[nbt.getInteger("mode")];
    }

    @Override
    public boolean canDoRecipe(CCMRecipe recipe) {
        return recipe.ccmMode == mode;
    }

    public void advanceMode() {
        if (mode.ordinal() + 1 >= CCMMode.values().length) {
            mode = CCMMode.values()[0];
        } else {
            mode = CCMMode.values()[mode.ordinal() + 1];
        }
        this.processQueue.clear();
        markDirty();
        IEUtils.notifyClientUpdate(world,getPos());
    }

    public static class CCMRecipe extends SimplifiedMultiblockRecipe {

        public CCMMode ccmMode;

        public CCMRecipe(ItemStack[] itemInputs, FluidStack[] fluidInputs, ItemStack[] itemOutputs, FluidStack[] fluidOutput, int energyPerTick, int duration, CCMMode ccmMode) {
            super(itemInputs, fluidInputs, itemOutputs, fluidOutput, energyPerTick, duration);
            this.ccmMode = ccmMode;
        }

        public CCMRecipe(ItemStack[] itemInputs, FluidStack[] fluidInputs, ItemStack[] itemOutputs, FluidStack[] fluidOutput, int energyPerTick, int duration, CCMMode ccmMode, boolean consumeFirst) {
            super(itemInputs, fluidInputs, itemOutputs, fluidOutput, energyPerTick, duration, consumeFirst);
            this.ccmMode = ccmMode;
        }
    }
}
