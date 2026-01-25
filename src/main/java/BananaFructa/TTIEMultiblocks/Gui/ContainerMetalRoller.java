package BananaFructa.TTIEMultiblocks.Gui;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityMetalRoller;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntitySmallCoalBoiler;
import BananaFructa.TiagThings.Netowrk.IGuiEventListener;
import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class ContainerMetalRoller extends ContainerIEBase<TileEntityMetalRoller> implements IGuiEventListener {

    TileEntityMetalRoller tile;
    EntityPlayer player;
    public ContainerMetalRoller(InventoryPlayer inventoryPlayer, TileEntityMetalRoller tile) {
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

        this.addSlotToContainer(new Slot(tile.inventoryHandlers.get(0),0,44,35){
            @Override
            public int getSlotStackLimit() {
                return 1;
            }

            @Override
            public boolean isItemValid(ItemStack stack) {
                return inventory.isItemValidForSlot(slotNumber,stack);
            }
        });
        this.addSlotToContainer(new Slot(tile.inventoryHandlers.get(1),0,116,35));
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

    private boolean isRotaryPowered() {
        return tile.inTorque >= 20 && tile.inSpeed >= 80 && tile.inSpeed < 100;
    }

    @Override
    public void onEvent(int id, NBTTagCompound data) {
        ItemStack is = tile.inventoryHandlers.get(0).getStackInSlot(0);
        if (is.isEmpty()) return;
        NBTTagCompound compound = is.getTagCompound();
        if (!isRotaryPowered()) {
            player.sendMessage(new TextComponentString("Inadequate speed/torque. Speed must be between 80RPM and 100 RPM and the minimum torque is 20Nm."));
            return;
        }
        if (id == 0) {
            if (compound == null || !compound.hasKey("roller")) return;
            NBTTagCompound roller = compound.getCompoundTag("roller");
            if (!roller.hasKey("failed")) return;
            if (roller.getBoolean("failed")) {
                player.sendMessage(new TextComponentString("The input is too deformed."));
                return;
            }
            if (!roller.hasKey("progress") || !roller.hasKey("tight")) return;
            float p = roller.getFloat("progress");
            float t = roller.getFloat("tight");
            if (p < 1) {
                p += t / 2.0f;
                if (p > 1) p = 1;
                t -= t / 2.0f;
                roller.setFloat("progress", p);
                roller.setFloat("tight", t);
            }
        }
        if (id == 1) {
            if (compound == null || (!compound.hasKey("roller") && !compound.hasKey("lathe"))) {
                NBTTagCompound roller = is.getOrCreateSubCompound("roller");
                roller.setFloat("tight",0.0f);
                roller.setFloat("progress",0.0f);
                roller.setBoolean("failed",false);
            }
            if (compound == null || !compound.hasKey("roller")) return;
            NBTTagCompound roller = compound.getCompoundTag("roller");
            if (!roller.hasKey("failed")) return;
            if (roller.getBoolean("failed")) {
                player.sendMessage(new TextComponentString("The input is too deformed."));
                return;
            }
            if (!roller.hasKey("tight")) return;
            float t = roller.getFloat("tight");
            Random rand = tile.getWorld().rand;
            t += 0.05f + rand.nextFloat()*0.05f; // TODO: add skill requirement
            if ((t > 0.5f && (1-t)*2 < rand.nextFloat()) || t > 1.0f) {
                roller.setBoolean("failed",true);
                player.sendMessage(new TextComponentString("The input has been deformed and cannot be further worked on."));
                return;
            }
            roller.setFloat("tight",t);
        }
    }
}
