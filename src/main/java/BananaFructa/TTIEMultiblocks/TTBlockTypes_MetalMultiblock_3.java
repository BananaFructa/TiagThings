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

public enum TTBlockTypes_MetalMultiblock_3 implements IStringSerializable, BlockTTBase.IBlockEnum {

    SHAFT_FURNACE(true),
    SHAFT_FURNACE_CHILD(false),
    FBR(true),
    FBR_CHILD(false),
    CCM(true),
    CCM_CHILD(false),
    SMALL_COAL_BOILER(true,true,0),
    SMALL_COAL_BOILER_CHILD(false,true,0),
    STEAM_ENGINE(true),
    STEAM_ENGINE_CHILD(false);

    private boolean needsCustomState;
    public boolean hasGui = false;
    public int guiId = -1;

    public boolean needsCustomState() {
        return needsCustomState;
    }

    TTBlockTypes_MetalMultiblock_3(boolean needsCustomState) {
        this.needsCustomState = needsCustomState;
    }
    TTBlockTypes_MetalMultiblock_3(boolean needsCustomState,boolean hasGui,int guiId) {
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