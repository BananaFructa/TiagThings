package BananaFructa.TTIEMultiblocks.Gui;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityLathe;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntitySmallCoalBoiler;
import BananaFructa.TiagThings.Netowrk.IGuiEventListener;
import BananaFructa.TiagThings.TTMain;
import BananaFructa.TiagThings.Utils;
import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.item.Item;

import javax.annotation.Nullable;

public class ContainerLathe extends ContainerIEBase<TileEntityLathe> implements IGuiEventListener {
    TileEntityLathe tile;
    EntityPlayer player;
    public ContainerLathe(InventoryPlayer inventoryPlayer, TileEntityLathe tile) {
        super(inventoryPlayer, tile);
        this.player = inventoryPlayer.player;
        this.tile = tile;

        for(int i = 0 ; i < 3 ; i++) {
            for(int j = 0 ; j < 9 ; j++) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 25+85 + i * 18));
            }
        }
        for(int i = 0 ; i < 9 ; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 25+143));
        }

        this.addSlotToContainer(new Slot(tile.inventoryHandlers.get(0),0,22,68) {
            @Override
            public int getSlotStackLimit() {
                return 1;
            }
            @Override
            public boolean isItemValid(ItemStack stack) {
                return inventory.isItemValidForSlot(slotNumber,stack);
            }
        });
        this.addSlotToContainer(new Slot(tile.inventoryHandlers.get(1),0,138,68){
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() == steelTip.getItem() || stack.getItem() == tungstenSteelTip.getItem();
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
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true; // ?
    }

    private static final int[] ps = {-11,-20,11,20};

    private static ItemStack steelTip = Utils.itemStackFromCTId("<tiagthings:steel_lathe_tool>");
    private static ItemStack tungstenSteelTip = Utils.itemStackFromCTId("<tiagthings:tungsten_steel_lathe_tool>");

    public boolean canAct(Item secondary, TileEntityLathe.LatheTier tier) {
        switch (tier){
            case STEEL:
                return secondary == steelTip.getItem() || secondary == tungstenSteelTip.getItem();
            case TUNGSTEN_STEEL:
                return secondary == tungstenSteelTip.getItem(); // well shit just realised this is not needed
            default:
                return false;
        }
    }

    @Override
    public void onEvent(int id,NBTTagCompound data) {
        ItemStack is = tile.inventoryHandlers.get(0).getStackInSlot(0);
        if (id == 4) {
            if (!is.isEmpty()) {
                if (is.hasTagCompound() && is.getTagCompound().hasKey("roller")) return;
                player.openGui(TTMain.INSTANCE, 2, tile.getWorld(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ());
            }
        } else if (id >= 0 && id < 4) {
            if (!is.isEmpty()) {
                if (tile.inSpeed >= 200 && tile.inSpeed < 300 && tile.inTorque > 10) {
                    if (is.hasTagCompound()) {
                        NBTTagCompound nbt = is.getTagCompound();
                        if (nbt.hasKey("lathe")) {
                            NBTTagCompound latheC = nbt.getCompoundTag("lathe");
                            if (latheC.hasKey("recipe")) {
                                int r = latheC.getInteger("recipe");
                                if (r < TileEntityLathe.recipes.size()) {
                                    TileEntityLathe.LatheRecipe recipe = TileEntityLathe.recipes.get(r);
                                    ItemStack second = tile.inventoryHandlers.get(0).getStackInSlot(1);
                                    if (second.isEmpty()) {
                                        player.sendMessage(new TextComponentString("No lathe tip."));
                                        return;
                                    }
                                    Item sitem = second.getItem();
                                    if (!canAct(sitem, recipe.tier)) {
                                        player.sendMessage(new TextComponentString("Lathe tip too soft to work."));
                                        return;
                                    }
                                    second.damageItem(1,player);
                                    if (latheC.hasKey("actions")) {
                                        int[] actions = latheC.getIntArray("actions");
                                        if (actions != null && actions.length == 3) {
                                            actions[2] = actions[1];
                                            actions[1] = actions[0];
                                            actions[0] = id;
                                        }
                                    }
                                    if (latheC.hasKey("progress")) {
                                        int p = latheC.getInteger("progress");
                                        p += ps[id];
                                        if (p < 0) p = 0;
                                        if (p > 150) p = 150;
                                        latheC.setInteger("progress", p);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    player.sendMessage(new TextComponentString("Inadequate speed/torque. Speed must be between 200 and 300 RPM and the minimum torque is 10Nm."));
                }
            }
        }
    }
}
