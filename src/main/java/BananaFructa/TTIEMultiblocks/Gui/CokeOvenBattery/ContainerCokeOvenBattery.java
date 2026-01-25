package BananaFructa.TTIEMultiblocks.Gui.CokeOvenBattery;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityCokeOvenBattery;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntitySmallCoalBoiler;
import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.lwjgl.Sys;

public class ContainerCokeOvenBattery extends ContainerIEBase<TileEntityCokeOvenBattery> {

    TileEntityCokeOvenBattery tile;
    public ContainerCokeOvenBattery(InventoryPlayer inventoryPlayer, TileEntityCokeOvenBattery tile) {
        super(inventoryPlayer, tile);
        tile = tile.master();
        this.tile = tile;

        for(int i = 0 ; i < 3 ; i++) {
            for(int j = 0 ; j < 9 ; j++) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 85 + i * 18));
            }
        }
        for(int i = 0 ; i < 9 ; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 143));
        }


        for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
            System.out.println(ste + "\n");
        }
        this.addSlotToContainer(new Slot(tile.inventoryHandlers.get(0), 0, 36, 50) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return inventory.isItemValidForSlot(slotNumber,stack);
            }
        });

        this.addSlotToContainer(new Slot(tile.inventoryHandlers.get(1),0,83,22));
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index >= 4 * 9)
            {
                if (!this.mergeItemStack(itemstack1, 0, 4 * 9, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 4 * 9, this.inventorySlots.size(), true))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return inv != null && !this.tile.isInvalid() && this.tile.getDistanceSq(player.posX, player.posY, player.posZ) < 20*20;
    }
}
