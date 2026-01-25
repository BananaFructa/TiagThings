package BananaFructa.TTIEMultiblocks.Gui;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class ButtonActionLathe extends GuiButton {

    LatheAction action;

    public ButtonActionLathe(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, LatheAction action) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.action = action;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            ClientUtils.bindTexture("tiagthings:textures/gui/lathe_gui.png");
            drawScaledCustomSizeModalRect(x,y,(129+30*action.ordinal()),224,30,30,16,16,256,256);
        }
    }
}
