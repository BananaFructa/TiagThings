package BananaFructa.THAHModifications;

import BananaFructa.TiagThings.TTMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class AnimatedString {

    public double timeToComplete;
    public double delay = 0;
    public double elapsedTime = 0;
    public int upDistance = 0;
    public String s;
    public int x,y,color;

    public AnimatedString (String s,int x, int y, int color, double delay, double timeToComplete, int upDistance) {
        this.timeToComplete = timeToComplete;
        this.delay = delay;
        this.upDistance = upDistance;
        this.s = s;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public double getCompleted() {
        return (elapsedTime-delay) / timeToComplete;
    }

    public void draw(Minecraft mc, GuiScreen screen) {
        if (Minecraft.getDebugFPS() != 0) elapsedTime += 1.0/Minecraft.getDebugFPS();

        if (elapsedTime >= delay) {

            if (getCompleted() < 1) {

                int alpha = (int)(0xff * getCompleted());

                if (alpha < 20) alpha = 20;

                mc.fontRenderer.drawStringWithShadow(s,x,(int)(y - upDistance * (1-getCompleted())),color | (alpha << 24));
            } else {
                mc.fontRenderer.drawStringWithShadow(s,x,y,color);
            }

        }
    }

}
