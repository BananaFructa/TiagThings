package BananaFructa.TTIEMultiblocks.PowerNetworkInfo;

import BananaFructa.TiagThings.TTMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
            GL11.glVertex2f(x+i*2, y-first / 10.0f);
            GL11.glVertex2f(x+(i+1)*2, y-second/10.0f);
            GL11.glEnd();
        }

        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    private void drawTexturedModalRect512(int x,int y, int tx, int ty,int tw, int th) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(2,2,1);
        this.drawTexturedModalRect(x/2,y/2,tx/2,ty/2,tw/2,th/2);
        GlStateManager.popMatrix();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        mc.renderEngine.bindTexture(new ResourceLocation(TTMain.modId, "textures/gui/electric_network.png"));
        int xLeft = sr.getScaledWidth()/2 - 512/2;
        int yTop = sr.getScaledHeight()/2 - 295/2;
        drawTexturedModalRect512(xLeft,yTop,0,0,512,239);
        this.drawTexturedModalRect((sr.getScaledWidth()/2 - 512/2)/2, (sr.getScaledHeight()/2 - 295/2)/2, 0,0,256,256);

        if (networkDataToDisplay != null) {
            System.out.println(networkDataToDisplay.getActivityScore());
            HashMap<String,NetworkDeviceHistory> historyHashMap = networkDataToDisplay.consumptionHistory;
            int i = 0;

            List<NetworkDeviceHistory> histories = new ArrayList<>();
            for (String s : historyHashMap.keySet()) histories.add(historyHashMap.get(s));
            for (String s : historyHashMap.keySet()) {
                NetworkDeviceHistory history = historyHashMap.get(s);
                renderGraph(historyHashMap.get(s),colorPalette[i],xLeft+30,yTop + 129);
                ItemStack display = new ItemStack(history.deviceItem);
                display.setItemDamage(history.deviceMetadata);
                display.setCount(history.deviceCount);
                GlStateManager.pushMatrix();
                RenderHelper.enableGUIStandardItemLighting();
                Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(display,10, sr.getScaledHeight()/2 + 10 * i);
                RenderHelper.disableStandardItemLighting();
                GlStateManager.popMatrix();
                Minecraft.getMinecraft().getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer,display,10,sr.getScaledHeight()/2 + 10 * i,null);
                i++;
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
