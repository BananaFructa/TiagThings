package BananaFructa.TTIEMultiblocks.Gui;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityLathe;
import BananaFructa.TiagThings.Netowrk.IGuiEventListener;
import BananaFructa.TiagThings.TTMain;
import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

public class ContainerLathePlanner extends ContainerIEBase<TileEntityLathe> implements IGuiEventListener {

    TileEntityLathe tile;
    EntityPlayer player;

    public ContainerLathePlanner(InventoryPlayer inventoryPlayer, TileEntityLathe tile) {
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
    public void onEvent(int id, NBTTagCompound data) {
        ItemStack is = this.tile.inventoryHandlers.get(0).getStackInSlot(0);
        if (!is.isEmpty()) {
            if (is.hasTagCompound() && is.getTagCompound().hasKey("roller")) return;
            NBTTagCompound compound = is.getOrCreateSubCompound("lathe");
            compound.setInteger("recipe",id);
            this.tile.markDirty();
            compound.setIntArray("actions",new int[]{-1,-1,-1});
            compound.setInteger("progress",0);
        }
        player.openGui(TTMain.INSTANCE,1,tile.getWorld(),tile.getPos().getX(),tile.getPos().getY(),tile.getPos().getZ());
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
