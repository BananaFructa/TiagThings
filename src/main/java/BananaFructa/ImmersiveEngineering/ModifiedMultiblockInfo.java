package BananaFructa.ImmersiveEngineering;

import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import net.minecraft.nbt.NBTTagCompound;


public class ModifiedMultiblockInfo {

    public TEProvider<?> childProvider;
    public TEProvider<?> parentProvider;
    public int maxPos;

    public ModifiedMultiblockInfo(TEProvider<?> childProvider, TEProvider<?> parentProvider, int maxPos) {
        this.childProvider = childProvider;
        this.parentProvider = parentProvider;
        this.maxPos = maxPos;
    }

    public interface TEProvider<T extends TileEntityMultiblockPart> {
        T getTE(NBTTagCompound info);
    }

}
