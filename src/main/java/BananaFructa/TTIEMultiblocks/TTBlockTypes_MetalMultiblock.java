package BananaFructa.TTIEMultiblocks;

import BananaFructa.TTIEMultiblocks.IECopy.BlockTTBase;
import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

/**
 * CODE ADAPTED FROM THE IMMERSIVE ENGINEERING SOURCE CODE
 * REPOSITORY CAN BE FOUND HERE https://github.com/BluSunrize/ImmersiveEngineering/tree/1.13pre
 */

public enum TTBlockTypes_MetalMultiblock implements IStringSerializable, BlockTTBase.IBlockEnum {

    COAL_BOILER(true),
    COAL_BOILER_CHILD(false),
    ELECTRIC_HEATER(true),
    ELECTRIC_HEATER_CHILD(false),
    FLARE_STACK(true),
    FLARE_STACK_CHILD(false),
    CLARIFIER(true),
    CLARIFIER_CHILD(false),
    WATER_FILTER(true),
    WATER_FILTER_CHILD(false),
    OIL_BOILER(true),
    OIL_BOILER_CHILD(false),
    UM_PHOTOLITHOGRAPHY_MACHINE(true),
    UM_PHOTOLITHOGRAPHY_MACHINE_CHILD(false),
    COMPUTER_CLUSTER_UNIT(true), // UNUSED
    COMPUTER_CLUSTER_UNIT_CHILD(false); // UNUSED
    /*
    NMPLM(true),
    NMPLM_CHILD(false),
    EUVPLM(true),
    EUVPLM_CHILD(false),
    GAS_CENTRIFUGE(true),
    GAS_CENTRIFUGE_CHILD(false),
    STEAM_RADIATOR(true),
    STEAM_RADIATOR_CHILD(false),
    INDOOR_AC_UNIT(true),
    INDOOR_AC_UNIT_CHILD(false),
    OUTDOOR_AC_UNIT(true),
    OUTDOOR_AC_UNIT_CHILD(false),
    TRESHER(true),
    TRESHER_CHILD(false),
    ELECTRIC_OVEN(true),
    ELECTRIC_OVEN_CHILD(false);
     */

    private boolean needsCustomState;
    public boolean hasGui = false;
    public int guiId = -1;

    public boolean needsCustomState() {
        return needsCustomState;
    }

    TTBlockTypes_MetalMultiblock(boolean needsCustomState) {
        this.needsCustomState = needsCustomState;
    }
    TTBlockTypes_MetalMultiblock(boolean needsCustomState,boolean hasGui,int guiId) {
        this.needsCustomState = needsCustomState;
        this.hasGui = hasGui;
        this.guiId = guiId;
    }

    @Override
    public int getMeta() {
        return this.ordinal();
    }

    @Override
    public boolean listForCreative() {
        return false;
    }

    @Override
    public String getName() {
        return this.toString().toLowerCase(Locale.ENGLISH);
    }

    public String getCustomState() {
        return this.getName().toLowerCase();
    }

}
