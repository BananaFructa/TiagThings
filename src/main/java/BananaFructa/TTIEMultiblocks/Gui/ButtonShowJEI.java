package BananaFructa.TTIEMultiblocks.Gui;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class ButtonShowJEI extends GuiButton {

    public ButtonShowJEI(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            ClientUtils.bindTexture("tiagthings:textures/gui/lathe_gui.png");
            drawTexturedModalRect(x,y,0,192,9,14);
        }
    }
}
