package BananaFructa.TiagThings.MainMenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

public class TTMainMenuButton extends GuiButton {

    private float AddedX = 0;
    private float BackgroundAddedX = 0;

    public TTMainMenuButton(int buttonId,int x,int y,int w,int h, String text) {
        super(buttonId,x,y, w,h,text);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible)
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            if (hovered && BackgroundAddedX < this.width) {
                BackgroundAddedX += 120f/Minecraft.getDebugFPS()*2;
            } else if (!hovered && BackgroundAddedX > 0) {
                BackgroundAddedX -= 120f/Minecraft.getDebugFPS()*2;
            }
            if (BackgroundAddedX < 0) {
                BackgroundAddedX = 0;
            } else if (BackgroundAddedX > this.width) {
                BackgroundAddedX = this.width;
            }
            drawRect(this.x,this.y,this.x + (int)BackgroundAddedX,this.y + this.height,0xaaffffff);
            if (hovered && AddedX < 6) {
                AddedX += 120f/Minecraft.getDebugFPS() * 0.5f;
            } else if (!hovered && AddedX > 0) {
                AddedX -= 120f/Minecraft.getDebugFPS() * 0.5f;
            }
            if (AddedX < 0) {
                AddedX = 0;
            } else if (AddedX > 6) {
                AddedX = 6;
            }
            this.mouseDragged(mc, mouseX, mouseY);
            //GlStateManager.pushMatrix();
            //GlStateManager.scale(1.2,1.2,1);
            this.drawString(mc.fontRenderer,this.displayString,(int)((this.x + AddedX)) + 2,(int)((this.y+(this.height-8)/2)),0xffffff);
            //GlStateManager.popMatrix();
        }
    }
}
