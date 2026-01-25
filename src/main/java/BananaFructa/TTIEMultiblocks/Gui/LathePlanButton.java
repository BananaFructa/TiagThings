package BananaFructa.TTIEMultiblocks.Gui;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

public class LathePlanButton extends GuiButton {

    public ItemStack item;
    public int recipeIndex;
    public LathePlanButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, ItemStack item, int recipeIndex) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.item = item;
        this.recipeIndex = recipeIndex;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            ClientUtils.bindTexture("tfc:textures/gui/anvil_plan.png");
            drawTexturedModalRect(x,y,176,0,18,18);
            drawItem(item, x + 1, y + 1);
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
