package BananaFructa.TTIEMultiblocks.Gui;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityMagnetizer;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityOpenHearthFurnace;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityOpenHearthFurnaceOld;
import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerOpenHearthFurnace extends ContainerIEBase<TileEntityOpenHearthFurnace> {

    TileEntityOpenHearthFurnace tile;
    EntityPlayer player;
    public ContainerOpenHearthFurnace(InventoryPlayer inventoryPlayer, TileEntityOpenHearthFurnace tile) {
        super(inventoryPlayer, tile);
        this.player = inventoryPlayer.player;
        this.tile = tile;

        for(int i = 0 ; i < 3 ; i++) {
            for(int j = 0 ; j < 9 ; j++) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 85 + i * 18-1));
            }
        }
        for(int i = 0 ; i < 9 ; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 143-1));
        }

        this.addSlotToContainer(new Slot(tile.inventoryHandlers.get(0),0,121,57){
            @Override
            public boolean isItemValid(ItemStack stack) {
                return inventory.isItemValidForSlot(slotNumber,stack);
            }
        });
        this.addSlotToContainer(new Slot(tile.inventoryHandlers.get(1),0,10,19));
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
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
