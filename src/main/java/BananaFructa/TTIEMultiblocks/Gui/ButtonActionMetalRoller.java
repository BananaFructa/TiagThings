package BananaFructa.TTIEMultiblocks.Gui;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class ButtonActionMetalRoller extends GuiButton {

    MetalRollerAction action;

    public ButtonActionMetalRoller(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, MetalRollerAction action) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.action = action;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            ClientUtils.bindTexture("tiagthings:textures/gui/metal_roller_gui.png");
            drawScaledCustomSizeModalRect(x,y,(142+30*action.ordinal()),212,30,30,16,16,256,256);
        }
    }
}
