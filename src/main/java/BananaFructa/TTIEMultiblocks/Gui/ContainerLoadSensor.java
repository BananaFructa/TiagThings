package BananaFructa.TTIEMultiblocks.Gui;

import BananaFructa.TTIEMultiblocks.ControlBlocks.LoadSensorTileEntity;
import BananaFructa.TTIEMultiblocks.ElectricMotorTileEntity;
import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import BananaFructa.TiagThings.Netowrk.IGuiEventListener;
import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ContainerLoadSensor extends ContainerIEBase<LoadSensorTileEntity> implements IGuiEventListener {

    LoadSensorTileEntity tile;
    EntityPlayer player;
    public ContainerLoadSensor(InventoryPlayer inventoryPlayer, LoadSensorTileEntity tile) {
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

    @Override
    public void onEvent(int id, NBTTagCompound data) {
        float offset= Math.min(1,Math.max(0,data.getFloat("offset")));
        float mag = Math.min(1,Math.max(0,data.getFloat("magnitude")));
        tile.redstoneOffset = (int)(offset * 15);
        tile.magnitude = (int)(mag * LoadSensorTileEntity.maxRF) + 1;
        tile.markDirty();
        IEUtils.notifyClientUpdate(tile.getWorld(),tile.getPos());
    }
}
