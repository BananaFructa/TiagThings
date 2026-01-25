package BananaFructa.TTIEMultiblocks.Gui.ElectricfFoodOven;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityCokerUnit;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityElectricFoodOven;
import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerElectricFoodOven extends ContainerIEBase<TileEntityElectricFoodOven> {

    TileEntityElectricFoodOven tile;
    public ContainerElectricFoodOven(InventoryPlayer inventoryPlayer, TileEntityElectricFoodOven tile) {
        super(inventoryPlayer, tile);
        this.tile = tile;

        for(int i = 0 ; i < 3 ; i++) {
            for(int j = 0 ; j < 9 ; j++) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 85 + i * 18));
            }
        }
        for(int i = 0 ; i < 9 ; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 143));
        }

        this.addSlotToContainer(new Slot(tile.inventoryHandlers.get(0), 0, 44, 37) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return inventory.isItemValidForSlot(slotNumber,stack);
            }
        });
        this.addSlotToContainer(new Slot(tile.inventoryHandlers.get(1), 0, 115, 37) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return true;
            }
        });
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
