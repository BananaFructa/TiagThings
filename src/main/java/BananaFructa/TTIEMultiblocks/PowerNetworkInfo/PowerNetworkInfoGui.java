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
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class PowerNetworkInfoGui extends GuiScreen {

    GraphScale scale = GraphScale.FIVE_SECONDS;

    int scrollLeft = 0;
    int scrollRight = 0;

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
            GL11.glVertex2f(x+199-i*2, y-100*(first) / (float)topRF);
            GL11.glVertex2f(x+199-(i+1)*2, y-100*(second)/(float)topRF);
            GL11.glEnd();
        }

        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    private void drawTexturedModalRect512(int x,int y, int tx, int ty,int tw, int th) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(2,2,1);
        this.drawTexturedModalRect(x/2.0f,y/2.0f,tx/2,ty/2,tw/2,th/2);
        GlStateManager.popMatrix();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (networkDataToDisplay != null) {

            List<NetworkDeviceHistory> cons = getSortedHistoryList(networkDataToDisplay,true);
            List<NetworkDeviceHistory> prod = getSortedHistoryList(networkDataToDisplay, false);

            scrollLeft = drawGraphList(30, scrollLeft, cons);
            scrollRight = drawGraphList(281, scrollRight, prod);
            GlStateManager.pushMatrix();
            GlStateManager.disableDepth();
            GL11.glColor4f(1, 1, 1, 1);
            mc.renderEngine.bindTexture(new ResourceLocation(TTMain.modId, "textures/gui/electric_network.png"));
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            int xLeft = sr.getScaledWidth() / 2 - 512 / 2;
            int yTop = sr.getScaledHeight() / 2 - 295 / 2;
            drawTexturedModalRect512(xLeft, yTop, 0, 0, 512, 296);
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
            drawGraph(30,scrollLeft,cons);
            drawGraph(281,scrollRight,prod);

            GlStateManager.pushMatrix();
            int totalC = 0;
            for (NetworkDeviceHistory h : cons) {
                if (!h.emptyFor(scale) && h.deviceCount > 0) {
                    totalC += h.getValue(0, scale);
                }
            }
            int totalP = 0;
            for (NetworkDeviceHistory h : prod) {
                if (!h.emptyFor(scale) && h.deviceCount > 0) {
                    totalP += h.getValue(0, scale);
                }
            }
            mc.fontRenderer.drawStringWithShadow("Consumption: " + formatPower(totalC),xLeft + 30, yTop + 15,  0xffdd6c00);
            mc.fontRenderer.drawStringWithShadow("Production: " + formatPower(totalP),xLeft + 281, yTop + 15,  0xff0066af);
            GlStateManager.popMatrix();
        }
    }

    private List<NetworkDeviceHistory> getSortedHistoryList(NetworkData data, boolean consumer) {
        HashMap<String,NetworkDeviceHistory> historyHashMap;
        if (consumer) historyHashMap = data.consumptionHistory;
        else historyHashMap = data.productionHistory;
        List<NetworkDeviceHistory> histories = new ArrayList<>();
        int maxRF = 0;
        for (String s : historyHashMap.keySet()) {
            NetworkDeviceHistory h = historyHashMap.get(s);
            if (!h.emptyFor(scale) && h.deviceCount > 0) {
                histories.add(historyHashMap.get(s));
            }
            for (int i = 0; i < h.getLength(scale) && i < 100; i++) {
                if (maxRF < h.getValue(i, scale)) maxRF = h.getValue(i, scale);
            }
        }
        histories = histories.stream().sorted((a, b) -> Float.compare(b.getAverage(scale), a.getAverage(scale))).collect(Collectors.toList());
        return histories;
    }

    private int drawGraphList(int posX, int scroll, List<NetworkDeviceHistory> histories) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        mc.renderEngine.bindTexture(new ResourceLocation(TTMain.modId, "textures/gui/electric_network.png"));
        int xLeft = sr.getScaledWidth() / 2 - 512 / 2;
        int yTop = sr.getScaledHeight() / 2 - 295 / 2;

        drawTexturedModalRect512(xLeft + posX, yTop + 136, 0, 298, 200, 130);

        float totalPower = 0;
        for (NetworkDeviceHistory h : histories) {
            if (!h.emptyFor(scale) && h.deviceCount > 0) {
                totalPower += h.getValue(0, scale);
            }
        }

        scroll = Math.max(Math.min(scroll, histories.size() * 24 - 130), 0);
        for (int i = 0; i < histories.size(); i++) {
            int y = yTop + 136 + 24 * i - scroll;
            if (y < yTop + 267 && y > yTop - 24 + 136) {
                NetworkDeviceHistory history = histories.get(i);
                ItemStack display = new ItemStack(history.deviceItem);
                display.setItemDamage(history.deviceMetadata);
                display.setCount(history.deviceCount);

                mc.renderEngine.bindTexture(new ResourceLocation(TTMain.modId, "textures/gui/electric_network.png"));
                drawTexturedModalRect512(xLeft + posX, y, 202, 402, 200, 24);

                GlStateManager.pushMatrix();
                GlStateManager.color(1, 1, 1);

                GlStateManager.popMatrix();
                setColor(colorPalette[i]);
                float val = history.getValue(0, scale) / totalPower;
                drawTexturedModalRect512(xLeft + posX + 27 - 1, y + 11 - 1, 202, 430, (int) (103 * val), 4);
                GlStateManager.pushMatrix();

                RenderHelper.enableGUIStandardItemLighting();
                GlStateManager.color(1, 1, 1);
                Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(display, xLeft + posX + 8 - 4, y + 8 - 4);
                RenderHelper.disableStandardItemLighting();
                GlStateManager.popMatrix();
            }
        }
        for (int i = 0; i < histories.size(); i++) {
            int y = yTop + 136 + 24 * i - scroll;
            if (y < yTop + 267 && y > yTop - 40) {
                NetworkDeviceHistory history = histories.get(i);
                ItemStack display = new ItemStack(history.deviceItem);
                display.setItemDamage(history.deviceMetadata);
                display.setCount(history.deviceCount);
                Minecraft.getMinecraft().getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, display, xLeft + posX + 8 - 4, y + 8 - 4, null);
            }
        }
        GlStateManager.pushMatrix();
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );
        GlStateManager.disableLighting();
        for (int i = 0; i < histories.size(); i++) {
            int y = yTop + 136 + 24 * i - scroll;
            if (y < yTop + 267 && y > yTop - 40) {
                NetworkDeviceHistory history = histories.get(i);
                String t = formatPower(history.getValue(0, scale));
                int x = xLeft + posX + 200 - 4 - mc.fontRenderer.getStringWidth(t);
                mc.fontRenderer.drawStringWithShadow(t, x, y + 7, 0xffffffff);

            }
        }
        GlStateManager.popMatrix();
        return scroll;
    }

    private void drawGraph(int posX, int scroll, List<NetworkDeviceHistory> histories) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int xLeft = sr.getScaledWidth() / 2 - 512 / 2;
        int yTop = sr.getScaledHeight() / 2 - 295 / 2;
        int maxRF = 0;
        for (NetworkDeviceHistory h : histories) {
            for (int i = 0; i < h.getLength(scale) && i < 100; i++) {
                if (maxRF < h.getValue(i, scale)) maxRF = h.getValue(i, scale);
            }
        }
        maxRF = (int) (maxRF * 1.1f + 5);
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        GL11.glColor4f(1, 1, 1, 1);
        for (int i = 0; i < histories.size(); i++) {
            NetworkDeviceHistory history = histories.get(i);
            renderGraph(history, colorPalette[i], xLeft + posX, yTop + 129, maxRF);
        }
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int scroll = Mouse.getDWheel();
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int mouseX = Mouse.getX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getY() * this.height / this.mc.displayHeight - 1;
        int xLeft = sr.getScaledWidth()/2 - 512/2;
        int yTop = sr.getScaledHeight()/2 - 295/2;
        if (scroll != 0) {
            if (mouseX >  30 + xLeft && mouseY > 136 + yTop && mouseX < 229 + xLeft && mouseY < 265 + yTop) {
                if (scroll < 0) {
                    scrollLeft += 15;
                } else {
                    scrollLeft -= 15;
                }
            } else if (mouseX >  281 + xLeft && mouseY > 136 + yTop && mouseX < 480 + xLeft && mouseY < 265 + yTop) {
                if (scroll < 0) {
                    scrollRight += 15;
                } else {
                    scrollRight -= 15;
                }
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
