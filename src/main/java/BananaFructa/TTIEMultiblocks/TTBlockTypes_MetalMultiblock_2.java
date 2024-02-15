package BananaFructa.TTIEMultiblocks;

import BananaFructa.TTIEMultiblocks.IECopy.BlockTTBase;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

/**
 * CODE ADAPTED FROM THE IMMERSIVE ENGINEERING SOURCE CODE
 * REPOSITORY CAN BE FOUND HERE https://github.com/BluSunrize/ImmersiveEngineering/tree/1.13pre
 */

public enum TTBlockTypes_MetalMultiblock_2 implements IStringSerializable, BlockTTBase.IBlockEnum {

    COMPUTER_CLUSTER_CONTROLLER(true),
    COMPUTER_CLUSTER_CONTROLLER_CHILD(false),
    MEMORY_FORMATTER(true),
    MEMORY_FORMATTER_CHILD(false),
    CLAY_OVEN(true),
    CLAY_OVEN_CHILD(false),
    MASONRY_HEATER(true),
    MASONRY_HEATER_CHILD(false),
    ROCKET_SCAFFOLD(true),
    ROCKET_SCAFFOLD_CHILD(false),
    ROCKET_SCAFFOLD_BODY(true),
    MAGNETIC_SEPARATOR(true),
    MAGNETIC_SEPARATOR_CHILD(false),
    OPEN_HEARTH_FURNACE(true),
    OPEN_HEARTH_FURNACE_CHILD(false);

    private boolean needsCustomState;
    public boolean hasGui = false;
    public int guiId = -1;


    public boolean needsCustomState() {
        return needsCustomState;
    }

    TTBlockTypes_MetalMultiblock_2(boolean needsCustomState) {
        this.needsCustomState = needsCustomState;
    }
    TTBlockTypes_MetalMultiblock_2(boolean needsCustomState,boolean hasGui,int guiId) {
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