package BananaFructa.TTIEMultiblocks.Gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class ScalingButton extends GuiButton {
    public ScalingButton(int buttonId, int x, int y) {
        super(buttonId, x, y, "");
    }

    public ScalingButton(int buttonId, int x, int y, int widthIn, int heightIn) {
        super(buttonId, x, y, widthIn, heightIn, "");
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (GuiScreen.isShiftKeyDown()) this.displayString = "/";
        else this.displayString = "x";
        super.drawButton(mc, mouseX, mouseY, partialTicks);
    }
}
