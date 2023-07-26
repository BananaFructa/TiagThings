package BananaFructa.TTIEMultiblocks;

import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum TTBlockTypes_MetalMultiblock implements IStringSerializable, BlockIEBase.IBlockEnum {

    ELECTRIC_HEATER(false);

    private boolean needsCustomState;

    TTBlockTypes_MetalMultiblock(boolean needsCustomState) {
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
