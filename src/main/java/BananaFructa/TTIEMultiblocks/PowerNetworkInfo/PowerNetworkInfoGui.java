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
import java.util.stream.Collectors;

public class PowerNetworkInfoGui extends GuiScreen {

    GraphScale scale = GraphScale.FIVE_SECONDS;

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

    private String formatPower(int amount) {

        // Could set up an enum but for three divisions thats just more work
        if (amount > 1e+3) {
            float v = amount/(float)1e+3;
            return String.format("%.1f",v) + " kRF/t";
        }
        if (amount > 1e+6) {
            float v = amount/(float)1e+6;
            return  String.format("%.1f",v) + " MRF/t";
        }
        if (amount > 1e+9) {
            float v = amount/(float)1e+9;
            return  String.format("%.1f",v) + " GRF/t";
        }
        return amount + " RF/t";
    }

    private void setColor (int color) {
        float r = (color >> 16)/255.0f;
        float g = ((color & 0x00ff00) >> 8) / 255.0f;
        float b = (color & 0xff)/255.0f;
        GlStateManager.color(r,g,b);
    }

    public void renderGraph(NetworkDeviceHistory history, int color,int x,int y, int topRF) {
        float r = (color >> 16)/255.0f;
        float g = ((color & 0x00ff00) >> 8) / 255.0f;
        float b = (color & 0xff)/255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();

        GL11.glLineWidth(2.0F); // thickness

        for (int i = 0; i < history.getSize(scale) - 1 && i < 99; i++) {
            int first = history.getValue(i,scale);
            int second = history.getValue(i+1,scale);
            GL11.glBegin(GL11.GL_LINES);
            GL11.glColor4f(r, g, b, 1F); // RGBA
            GL11.glVertex2f(x+200-i*2, y-100*(first) / (float)topRF);
            GL11.glVertex2f(x+200-(i+1)*2, y-100*(second)/(float)topRF);
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
        drawTexturedModalRect512(xLeft,yTop,0,0,512,296);
        //this.drawTexturedModalRect((sr.getScaledWidth()/2 - 512/2)/2, (sr.getScaledHeight()/2 - 295/2)/2, 0,0,256,256);

        if (networkDataToDisplay != null) {
            System.out.println(networkDataToDisplay.getActivityScore());
            HashMap<String,NetworkDeviceHistory> historyHashMap = networkDataToDisplay.consumptionHistory;

            List<NetworkDeviceHistory> histories = new ArrayList<>();
            float totalPowerConsumption = 0;
            int maxRF = 0;
            for (String s : historyHashMap.keySet()) {
                NetworkDeviceHistory h = historyHashMap.get(s);
                if(!h.emptyFor(scale) && h.deviceCount > 0) {
                    totalPowerConsumption += historyHashMap.get(s).getValue(0,scale);
                    histories.add(historyHashMap.get(s));
                }
                for (int i = 0;i < h.getLength(scale) && i < 100;i++) {
                    if (maxRF < h.getValue(i,scale)) maxRF = h.getValue(i,scale);
                }
            }
            maxRF = (int)(maxRF*1.1f + 5);
            histories = histories.stream().sorted((a,b) -> Float.compare(b.getAverage(scale), a.getAverage(scale))).collect(Collectors.toList());



            for (int i = 0;i < histories.size();i++) {
                NetworkDeviceHistory history = histories.get(i);
                ItemStack display = new ItemStack(history.deviceItem);
                display.setItemDamage(history.deviceMetadata);
                display.setCount(history.deviceCount);

                mc.renderEngine.bindTexture(new ResourceLocation(TTMain.modId, "textures/gui/electric_network.png"));
                drawTexturedModalRect512(xLeft + 30,yTop + 136 +24*i,202,402,200,24);

                GlStateManager.pushMatrix();
                GlStateManager.color(1,1,1);

                GlStateManager.popMatrix();
                setColor(colorPalette[i]);
                float val = history.getValue(0,scale) / totalPowerConsumption;
                drawTexturedModalRect512(xLeft + 30 + 27-2,yTop + 136 +24*i + 11-2,202,430,(int)(103*val),4);
                GlStateManager.pushMatrix();

                RenderHelper.enableGUIStandardItemLighting();
                GlStateManager.color(1,1,1);
                Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(display,xLeft + 38-5, yTop + 136 + 24*i + 8-5);
                RenderHelper.disableStandardItemLighting();
                GlStateManager.popMatrix();
            }
            for (int i = 0;i < histories.size();i++) {
                NetworkDeviceHistory history = histories.get(i);
                renderGraph(history, colorPalette[i], xLeft + 30, yTop + 129,maxRF);
            }
            for (int i = 0;i < histories.size();i++) {
                NetworkDeviceHistory history = histories.get(i);
                ItemStack display = new ItemStack(history.deviceItem);
                display.setItemDamage(history.deviceMetadata);
                display.setCount(history.deviceCount);
                Minecraft.getMinecraft().getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer,display,xLeft + 38-5, yTop + 136 + 24*i + 8-5,null);
            }
            GlStateManager.pushMatrix();
            GL11.glColor4f(1,1,1, 1);
            for (int i = 0;i < histories.size();i++) {
                NetworkDeviceHistory history = histories.get(i);
                String t = formatPower(history.getValue(0,scale));
                int x = xLeft + 30 + 200 - 3 - mc.fontRenderer.getStringWidth(t);
                int y = yTop + 136 +24*i + 7;
                mc.fontRenderer.drawStringWithShadow( t,x,y,0xffffffff);
            }
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
