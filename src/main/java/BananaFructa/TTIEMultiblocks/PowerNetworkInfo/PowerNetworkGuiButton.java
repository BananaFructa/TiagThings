package BananaFructa.TTIEMultiblocks.PowerNetworkInfo;

import BananaFructa.TiagThings.TTMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class PowerNetworkGuiButton extends GuiButton {

    public GraphScale scale;

    public PowerNetworkGuiButton(int buttonId, int x, int y, GraphScale scale) {
        super(buttonId, x, y,51,15, scale.name);
        this.scale = scale;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible)
        {
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(new ResourceLocation(TTMain.modId, "textures/gui/electric_network.png"));
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

            drawTexturedModalRect512(this.x, this.y, 206, 298 + i * 16, this.width, this.height);

            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;

            if (packedFGColour != 0)
            {
                j = packedFGColour;
            }
            else
            if (!this.enabled)
            {
                j = 10526880;
            }
            else if (this.hovered)
            {
                j = 16777120;
            }

            this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
        }
    }

    private void drawTexturedModalRect512(int x,int y, int tx, int ty,int tw, int th) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(2,2,1);
        this.drawTexturedModalRect(x/2.0f,y/2.0f,tx/2,ty/2,tw/2,th/2);
        GlStateManager.popMatrix();
    }
}
