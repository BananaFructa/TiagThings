package BananaFructa.TTIEMultiblocks.Utils;

import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.nbt.NBTTagCompound;

public class OffsetExposedIEInventoryHandler extends IEInventoryHandler {

    int offset = 0;

    boolean[] canInsert;
    boolean[] canExtract;

    public OffsetExposedIEInventoryHandler(int slots, IIEInventory inventory, int slotOffset, boolean[] canInsert, boolean[] canExtract) {
        super(slots, inventory, slotOffset, canInsert, canExtract);
        this.offset = slotOffset;
        this.canInsert = canInsert;
        this.canExtract = canExtract;
    }

    public int getOffset() {
        return offset;
    }

    public boolean[] getCanInsert() {
        return canInsert;
    }

    public boolean[] getCanExtract() {
        return canExtract;
    }

}
