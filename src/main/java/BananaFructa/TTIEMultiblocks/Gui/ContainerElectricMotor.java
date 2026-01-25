package BananaFructa.TTIEMultiblocks.Gui;

import BananaFructa.TTIEMultiblocks.ElectricMotorTileEntity;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityMetalRoller;
import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import BananaFructa.TiagThings.Netowrk.IGuiEventListener;
import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;

import java.util.Random;

public class ContainerElectricMotor extends ContainerIEBase<ElectricMotorTileEntity> implements IGuiEventListener {

    ElectricMotorTileEntity tile;
    EntityPlayer player;
    public ContainerElectricMotor(InventoryPlayer inventoryPlayer, ElectricMotorTileEntity tile) {
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

    public float targetRPM(float val, ElectricMotorTileEntity.PowerConnectionType type) {
        if (type == ElectricMotorTileEntity.PowerConnectionType.NONE) return 0;
        if (type == ElectricMotorTileEntity.PowerConnectionType.LV) return (ElectricMotorTileEntity.maxRPMLV-ElectricMotorTileEntity.minRPMLV) * val + ElectricMotorTileEntity.minRPMLV;
        if (type == ElectricMotorTileEntity.PowerConnectionType.MV) return (ElectricMotorTileEntity.maxRPMMV-ElectricMotorTileEntity.minRPMMV) * val + ElectricMotorTileEntity.minRPMMV;
        return 0;
    }

    public float targetTorque(float val) {
        return ElectricMotorTileEntity.torqueRange * val;
    }

    @Override
    public void onEvent(int id, NBTTagCompound data) {
        float speed = Math.min(1,Math.max(0,data.getFloat("speed")));
        float torque = Math.min(1,Math.max(0,data.getFloat("torque")));
        float targetSpeed = targetRPM(speed,tile.connectionType);
        float targetTorque = targetTorque(torque);
        tile.targetSpeed = targetSpeed;
        tile.targetTorque = targetTorque;
        tile.markDirty();
        IEUtils.notifyClientUpdate(tile.getWorld(),tile.getPos());
    }
}
