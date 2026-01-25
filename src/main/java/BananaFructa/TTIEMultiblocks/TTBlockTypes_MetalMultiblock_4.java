package BananaFructa.TTIEMultiblocks;

import BananaFructa.TTIEMultiblocks.IECopy.BlockTTBase;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

/**
 * CODE ADAPTED FROM THE IMMERSIVE ENGINEERING SOURCE CODE
 * REPOSITORY CAN BE FOUND HERE https://github.com/BluSunrize/ImmersiveEngineering/tree/1.13pre
 */

public enum TTBlockTypes_MetalMultiblock_4 implements IStringSerializable, BlockTTBase.IBlockEnum {

    COKE_OVEN_BATTERY(true,true,8),
    COKE_OVEN_BATTERY_CHILD(false,true,8),
    ELECTRIC_FOOD_OVEN(true,true,10),
    ELECTRIC_FOOD_OVEN_CHILD(false,true,10),
    MAGNETIZER(true,true,7),
    MAGNETIZER_CHILD(false,true,7),
    COKER_UNIT(true,true,9),
    COKER_UNIT_CHILD(false,true,9);

    private boolean needsCustomState;
    public boolean hasGui = false;
    public int guiId = -1;

    public boolean needsCustomState() {
        return needsCustomState;
    }

    TTBlockTypes_MetalMultiblock_4(boolean needsCustomState) {
        this.needsCustomState = needsCustomState;
    }
    TTBlockTypes_MetalMultiblock_4(boolean needsCustomState, boolean hasGui, int guiId) {
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