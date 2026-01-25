package BananaFructa.TTIEMultiblocks.Gui;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityLathe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import li.cil.oc.common.block.Item;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class ButtonPlannerLathe extends GuiButton {

    IEInventoryHandler handler;

    public ButtonPlannerLathe(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, IEInventoryHandler input) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        handler = input;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            ClientUtils.bindTexture("tiagthings:textures/gui/lathe_gui.png");
            drawTexturedModalRect(x,y,218,0,18,18);
            ItemStack is = handler.getStackInSlot(0);
            boolean nothing = is.isEmpty();
            int itemIndex = 0;
            if (!nothing) {
                NBTTagCompound compound = is.getTagCompound();
                if (compound == null) {
                    nothing = true;
                } else {
                    if (compound.hasKey("lathe")) {
                        NBTTagCompound latheCompound = compound.getCompoundTag("lathe");
                        if (latheCompound.hasKey("recipe")) {
                            itemIndex = latheCompound.getInteger("recipe");
                        } else nothing = true;
                    } else nothing = true;
                }
            }
            if (nothing) drawTexturedModalRect(x,y,236,0,18,18); // plan icon
            else if (TileEntityLathe.recipes.size() > itemIndex){
                ItemStack stack = TileEntityLathe.recipes.get(itemIndex).getItemOutputs().get(0);
                drawItem(stack,x+1,y+1);
            }
        }
    }

    private void drawItem(ItemStack stack, int x, int y) {
        RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
        this.zLevel = 200.0F;
        itemRender.zLevel = 200.0F;
        itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.zLevel = 0.0F;
        itemRender.zLevel = 0.0F;
    }

}
