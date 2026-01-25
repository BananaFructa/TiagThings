package BananaFructa.TTIEMultiblocks.Gui;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class ButtonElectricMotorSlider extends GuiButton {

    public boolean buttonHeld = false;
    public float val = 0;

    public ButtonElectricMotorSlider(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            ClientUtils.bindTexture("tiagthings:textures/gui/electric_motor_gui.png");
            if (buttonHeld) {
                val = Math.max(0,Math.min(1,val));
                val = Math.max(0, Math.min(width, mouseX - x)) / (float) width;
            }
            float startX = x + 1;
            float endX = x + width - 1;
            int slider = (int) ((endX - startX) * val);
            drawTexturedModalRect(x + slider,y-3,183,5,4,9);
        }
    }
}
