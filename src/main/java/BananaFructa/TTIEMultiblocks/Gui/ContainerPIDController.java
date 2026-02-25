package BananaFructa.TTIEMultiblocks.Gui;

import BananaFructa.TTIEMultiblocks.ControlBlocks.LoadSensorTileEntity;
import BananaFructa.TTIEMultiblocks.ControlBlocks.PIDControllerTileEntity;
import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import BananaFructa.TiagThings.Netowrk.IGuiEventListener;
import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ContainerPIDController extends ContainerIEBase<PIDControllerTileEntity> implements IGuiEventListener {

    PIDControllerTileEntity tile;
    EntityPlayer player;
    public ContainerPIDController(InventoryPlayer inventoryPlayer, PIDControllerTileEntity tile) {
        super(inventoryPlayer, tile);
        this.player = inventoryPlayer.player;
        this.tile = tile;

        for(int i = 0 ; i < 3 ; i++) {
            for(int j = 0 ; j < 9 ; j++) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 173 + i * 18+1));
            }
        }
        for(int i = 0 ; i < 9 ; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 231+1));
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
        float reference= Math.min(1,Math.max(0,data.getFloat("reference")));
        float p = Math.min(1,Math.max(0,data.getFloat("p")));
        float i = Math.min(1,Math.max(0,data.getFloat("i")));
        float d = Math.min(1,Math.max(0,data.getFloat("d")));
        int scalingP = data.getInteger("scaling_p");
        int scalingI = data.getInteger("scaling_i");
        int scalingD = data.getInteger("scaling_d");
        tile.reference = (int)(reference * 15);
        tile.p = (p * 40 - 20) * (float)Math.pow(10,scalingP);
        tile.i = (i * 40 - 20) * (float)Math.pow(10,scalingI);
        tile.d = (d * 40 - 20) * (float)Math.pow(10,scalingD);
        tile.scalingP = scalingP;
        tile.scalingI = scalingI;
        tile.scalingD = scalingD;
        tile.markDirty();
        IEUtils.notifyClientUpdate(tile.getWorld(),tile.getPos());
    }
}
