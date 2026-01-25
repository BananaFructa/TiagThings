package BananaFructa.THAHModifications;

import BananaFructa.TiagThings.MainMenu.Animation;
import BananaFructa.thah.Climates;
import BananaFructa.thah.ThahConfig;
import BananaFructa.thah.gui.ClimateScreenButton;
import BananaFructa.thah.network.CPacketClimateType;
import BananaFructa.thah.network.PacketHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TTChooseClimateGUi extends GuiScreen {

    private final int buttonWidth = 90;
    private final int buttonHeight = 16;
    private final int spacing = 3;

    private final int screenWidth = 240;
    private final int screenHeight = 120;

    private int yStartButtons;

    @Override
    public void initGui() {
        super.initGui();

        int climateCount = 0;

        for (Climates climate : Climates.values()) {
            if (climate.enabled) climateCount++;
        }

        ScaledResolution sr = new ScaledResolution(mc);

        int y = sr.getScaledHeight()/2 - climateCount * buttonHeight / 2 - (climateCount - 1) * spacing / 2;
        int x;
        if (ThahConfig.enableDescription) {
            x = sr.getScaledWidth() / 2 - buttonWidth / 2 - spacing / 2 - screenWidth / 2;
        } else {
            x = sr.getScaledWidth() / 2 - buttonWidth / 2;
        }

        yStartButtons = y;

        for (Climates climate : Climates.values()) {
            if (climate.enabled) {
                buttonList.add(new ClimateScreenButton(0, x, y, buttonWidth, buttonHeight, climate));
                y += buttonHeight + spacing;
            }
        }

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        drawRect(0,0,sr.getScaledWidth(), sr.getScaledHeight(), 0xff004A7F);
        super.drawScreen(mouseX, mouseY, partialTicks);
        String title = "Choose the climate in which you want to spawn";
        mc.fontRenderer.drawStringWithShadow(title, sr.getScaledWidth()/2-mc.fontRenderer.getStringWidth(title)/2,yStartButtons/2,0xffffff);
        if (ThahConfig.enableDescription) {
            int x = sr.getScaledWidth() / 2 + buttonWidth / 2 + spacing / 2 - screenWidth / 2;
            int y = sr.getScaledHeight() / 2 - screenHeight / 2;
            drawHorizontalLine(x + 1, x + screenWidth - 2, y, 0xffffffff);
            drawHorizontalLine(x + 1, x + screenWidth - 2, y + screenHeight, 0xffffffff);
            drawVerticalLine(x, y, y + screenHeight, 0xffffffff);
            drawVerticalLine(x + screenWidth - 1, y, y + screenHeight, 0xffffffff);

            Climates hoveredClimate = null;

            for (GuiButton button : buttonList) {
                if (button instanceof ClimateScreenButton) {
                    ClimateScreenButton b = (ClimateScreenButton)button;
                    if (b.isHovered()) {
                        hoveredClimate = b.climate;
                        break;
                    }
                }
            }

            if (hoveredClimate != null) {
                displayDescription(x,y,hoveredClimate.desc,hoveredClimate.dif,false);
            } else {
                displayDescription(x,y,"Hover over a climate for information!",-1,true);
            }

        }
    }

    List<AnimatedString> currentAnimations = new ArrayList<>();

    String lastText = "";

    public void displayDescription(int xScreen,int yScreen, String text,int dif,boolean info) {

        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);

        if (!lastText.equals(text)) {

            currentAnimations.clear();

            if (info) {
                currentAnimations.add(
                        new AnimatedString(
                                text,
                                xScreen + screenWidth / 2 - mc.fontRenderer.getStringWidth(text) / 2,
                                yScreen + screenHeight / 2 - mc.fontRenderer.FONT_HEIGHT,
                                0xffffff,
                                0,
                                0.14,
                                5
                        )
                );
            } else {
                List<String> strings = wrapStringToWidth(text,screenWidth-20);
                currentAnimations.add(new AnimatedString(
                        "Difficulty:",xScreen+5,yScreen+5,0xffffff,
                        0,0.14,5
                ));
                for (int i = 0;i < strings.size();i++) {
                    currentAnimations.add(new AnimatedString(
                            strings.get(i),xScreen+5,yScreen+25+i*mc.fontRenderer.FONT_HEIGHT,0xffffff,
                            (i+1) * 0.073,0.3,5
                    ));
                }
            }

            lastText = text;
        }

        for (AnimatedString animatedString : currentAnimations) animatedString.draw(mc,this);

        if (!info) {

            int color = 0;
            int startX = mc.fontRenderer.getStringWidth("Difficulty:") + xScreen + 5 + 2;
            int segmentSpacing = 2;
            if (dif >= 5) color = 0xe80909;
            if (dif >= 3 && dif <= 4) color = 0xd9d916;
            if (dif <= 2) color = 0x16b50b;
            for (int i = 0;i < dif;i++) {
                int added = i * (3 + segmentSpacing);
                drawRect(startX + added,yScreen+5,startX + 3 + added,yScreen+5+9,color + 0xff000000);
            }
        }
        GL11.glDisable(GL11.GL_BLEND);
        GlStateManager.popMatrix();
    }

    private List<String> wrapStringToWidth(String s, int widthToWrap) {
        List<String> lines = new ArrayList<String>();
        if (s.equals("")) {
            lines.add("");
            return lines;
        }
        String[] words = s.split(" ");
        String currentSentence = "";
        for (String word : words) {
            if (mc.fontRenderer.getStringWidth(currentSentence + word) < widthToWrap) {
                if (currentSentence.equals("")) {
                    currentSentence = word;
                } else {
                    currentSentence += " " + word;
                }
            } else {
                lines.add(currentSentence);
                currentSentence = word;
            }
        }
        if (!currentSentence.equals("")) {
            lines.add(currentSentence);
        }
        return lines;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button instanceof ClimateScreenButton) {
            ClimateScreenButton b = (ClimateScreenButton)button;
            PacketHandler.INSTANCE.sendToServer(new CPacketClimateType(b.climate.ordinal(),mc.player.getUniqueID()));
            mc.displayGuiScreen(null);
        }
    }
}
