package BananaFructa.TTIEMultiblocks;

import BananaFructa.TTIEMultiblocks.IECopy.BlockTTBase;
import BananaFructa.TiagThings.TTMain;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Locale;

/**
 * CODE ADAPTED FROM THE IMMERSIVE ENGINEERING SOURCE CODE
 * REPOSITORY CAN BE FOUND HERE https://github.com/BluSunrize/ImmersiveEngineering/tree/1.13pre
 */

public enum TTBlockTypes_MetalMultiblock_2 implements IStringSerializable, BlockTTBase.IBlockEnum {

    SILICON_CRUCIBLE(true,true,6),
    SILICON_CRUCIBLE_CHILD(false,true,6),
    MEMORY_FORMATTER(true),
    MEMORY_FORMATTER_CHILD(false),
    CLAY_OVEN(true),
    CLAY_OVEN_CHILD(false),
    MASONRY_HEATER(true),
    MASONRY_HEATER_CHILD(false),
    ROCKET_SCAFFOLD(true),
    ROCKET_SCAFFOLD_CHILD(false),
    ROCKET_SCAFFOLD_BODY(true),
    MAGNETIC_SEPARATOR(true,true,4),
    MAGNETIC_SEPARATOR_CHILD(false,true,4),
    OPEN_HEARTH_FURNACE(true,true,12),
    OPEN_HEARTH_FURNACE_CHILD(false,true, 12);

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