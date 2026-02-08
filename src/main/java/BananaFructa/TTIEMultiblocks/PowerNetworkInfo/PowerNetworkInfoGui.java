package BananaFructa.TTIEMultiblocks.PowerNetworkInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

public class PowerNetworkInfoGui extends GuiScreen {

    int[] colorPalette = new int[] {
            0x1f77b4,
            0xff7f0e,
            0x2ca02c,
            0xd62728,
            0x9467bd,
            0x8c564b,
            0xe377c2,
            0x7f7f7f,
            0xbcbd22,
            0x17becf
    };

    NetworkData networkDataToDisplay;

    public void setNetworkData(NetworkData data) {
        System.out.println(data);
        this.networkDataToDisplay = data;
    }

    public void updateNetworkData(NBTTagCompound updateTag) {
        if (this.networkDataToDisplay != null) {
            this.networkDataToDisplay.updateFromDelta(updateTag);
        }
    }

    public void renderGraph(NetworkDeviceHistory history, int color,int x,int y) {
        float r = (color >> 16)/255.0f;
        float g = ((color & 0x00ff00) >> 8) / 255.0f;
        float b = (color & 0xff)/255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();

        GL11.glLineWidth(2.0F); // thickness

        for (int i = 0; i < history.getSize(GraphScale.FIVE_SECONDS) - 1; i++) {
            int first = history.getValue(i,GraphScale.FIVE_SECONDS);
            int second = history.getValue(i+1,GraphScale.FIVE_SECONDS);
            GL11.glBegin(GL11.GL_LINES);
            GL11.glColor4f(r, g, b, 1F); // RGBA
            GL11.glVertex2f(x+i * 10, y+first / 10.0f);
            GL11.glVertex2f(x+(i+1) * 10, y+second/10.0f);
            GL11.glEnd();
        }

        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        drawRect(0,0,sr.getScaledHeight(),sr.getScaledHeight(),0xaaffffff);
        if (networkDataToDisplay != null) {
            System.out.println(networkDataToDisplay.getActivityScore());
            HashMap<String,NetworkDeviceHistory> historyHashMap = networkDataToDisplay.consumptionHistory;
            int i = 0;
            for (String s : historyHashMap.keySet()) {
                renderGraph(historyHashMap.get(s),colorPalette[i],0,sr.getScaledHeight()/2);
                i++;
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
