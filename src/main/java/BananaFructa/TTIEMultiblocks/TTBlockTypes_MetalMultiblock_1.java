package BananaFructa.TTIEMultiblocks;

import BananaFructa.TTIEMultiblocks.IECopy.BlockTTBase;
import com.eerussianguy.firmalife.registry.ItemsFL;
import net.minecraft.util.IStringSerializable;
import tfcflorae.TFCFlorae;

import java.util.Locale;

public enum TTBlockTypes_MetalMultiblock_1 implements IStringSerializable, BlockTTBase.IBlockEnum {

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

    private boolean needsCustomState;

    public boolean needsCustomState() {
        return needsCustomState;
    }

    TTBlockTypes_MetalMultiblock_1(boolean needsCustomState) {
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