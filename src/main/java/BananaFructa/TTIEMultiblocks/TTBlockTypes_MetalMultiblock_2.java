package BananaFructa.TTIEMultiblocks;

import BananaFructa.TTIEMultiblocks.IECopy.BlockTTBase;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

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
    ROCKET_SCAFFOLD_BODY(true);

    private boolean needsCustomState;

    public boolean needsCustomState() {
        return needsCustomState;
    }

    TTBlockTypes_MetalMultiblock_2(boolean needsCustomState) {
        this.needsCustomState = needsCustomState;
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