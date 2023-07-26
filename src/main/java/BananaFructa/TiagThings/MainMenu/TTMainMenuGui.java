package BananaFructa.TiagThings.MainMenu;

import BananaFructa.TiagThings.TTMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class TTMainMenuGui extends GuiMainMenu {


    Animation mainLogo = new Animation(1,"gui/frame-sequence",98);

    static List<String> iconSet = new ArrayList<String>() {{
        add("background.png");
        add("barrel-1.png");
        add("barrel-2.png");
        add("barrel-3.png");
        add("barrel-4.png");
        add("barrel-5.png");
        add("loom-1.png");
        add("loom-2.png");
        add("loom-3.png");
        add("loom-4.png");
        add("loom-5.png");
        add("loom-6.png");
        add("loom-7.png");
        add("rack-1.png");
        add("rack-2.png");
    }};

    float elapsedTime  = 0;
    boolean lagging = true;

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        this.buttonList.add(new TTMainMenuButton(1,5,60 + 10,100,17, I18n.format("menu.singleplayer")));
        this.buttonList.add(new TTMainMenuButton(2,5,77 + 10,100,17, I18n.format("menu.multiplayer")));
        this.buttonList.add(new TTMainMenuButton(0,5,94 + 10,100,17, I18n.format("menu.options")));
        GuiButton mod = new TTMainMenuButton(6,5,111 + 10,100,17, I18n.format("fml.menu.mods"));
        //Utils.WriteDeclaredField(GuiMainMenu.class,this,"modButton",mod);
        this.buttonList.add(mod);
        GuiButton realms = new TTMainMenuButton(14,5,128 + 10,100,17,I18n.format("menu.online").replace("Minecraft", "").trim());
        //Utils.WriteDeclaredField(GuiMainMenu.class,this,"realmsButton",realms);
        this.buttonList.add(realms);
        this.buttonList.add(new TTMainMenuButton(5,5,145 + 10,100,17,I18n.format("options.language")));
        this.buttonList.add(new TTMainMenuButton(4,5,162 + 10,100,17,I18n.format("menu.quit")));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(0, 0, this.width, this.height, 0xff004A7F);

        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        /*mc.renderEngine.bindTexture(new ResourceLocation(TTMain.modId,"gui/tiag-main-menu-atlas.png"));
        this.drawTexturedModalRect(5,5,0,0,80,43);*/
        if (Minecraft.getDebugFPS() > 20) {
            lagging = false;
        }
        if (!lagging) {
            int heightBody = (int)(0.6 * 5 + (60 + 10 - (162 + 10 + 17)));

            ScaledResolution sr = new ScaledResolution(mc);

            GlStateManager.pushMatrix();
            GlStateManager.scale(0.6, 0.6, 1);
            mainLogo.draw(mc, this, -40, 5, 256, 256 / 2);
            if (Minecraft.getDebugFPS() != 0 && elapsedTime <= iconSet.size() / 5.0f)
                elapsedTime += 1.0 / Minecraft.getDebugFPS();
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            int current = Math.min((int) (elapsedTime * 1000) / 200, iconSet.size());

            int xSpacing = 110;

            int ysize = (int) (sr.getScaledHeight() * 0.8);
            int xsize = (int) ((sr.getScaledWidth() - xSpacing) * 0.8);

            int w = 1500 / 8;
            int h = 1000 / 8;

            float factY = ysize / (float) h;
            float factX = xsize / (float) w;

            float factor = Math.min(factX, factY);

            //int x = (int)(150 + (sr.getScaledWidth()-150) * 0.1 - (factX - factor) * w) ;
            //int y = (int)(0.1 * sr.getScaledHeight() - (factY - factor) * h);

            float x = xSpacing + ((sr.getScaledWidth() - xSpacing) - factor * w) / 2;
            float y = (sr.getScaledHeight() - factor * h) / 2;

            GlStateManager.scale(factor, factor, 1);
            for (int i = 0; i < current; i++) {
                mc.renderEngine.bindTexture(new ResourceLocation(TTMain.modId, "gui/image-set-1/" + iconSet.get(i)));
                this.drawTexturedModalRect(x / factor, y / factor, 0, 0, (int) (1500 / (8)), (int) (1000 / (8)));
            }

            GlStateManager.popMatrix();
        }

        DrawMinecraftBrandingsAndCopyRight();

        for (int i = 0; i < this.buttonList.size(); ++i) {
            this.buttonList.get(i).drawButton(this.mc, mouseX, mouseY, partialTicks);
        }

        for (int j = 0; j < this.labelList.size(); ++j) {
            this.labelList.get(j).drawLabel(this.mc, mouseX, mouseY);
        }
    }

    private void DrawMinecraftBrandingsAndCopyRight() {
        java.util.List<String> brandings = com.google.common.collect.Lists.reverse(net.minecraftforge.fml.common.FMLCommonHandler.instance().getBrandings(true));
        for (int brdline = 0; brdline < brandings.size(); brdline++)
        {
            String brd = brandings.get(brdline);
            if (!com.google.common.base.Strings.isNullOrEmpty(brd))
            {
                this.drawString(this.fontRenderer, brd, 2, this.height - ( 10 + brdline * (this.fontRenderer.FONT_HEIGHT + 1)), 16777215);
            }
        }

        this.drawString(this.fontRenderer, "Copyright Mojang AB. Do not distribute!", this.width - this.fontRenderer.getStringWidth("Copyright Mojang AB. Do not distribute!") - 2, this.height - 10, -1);
    }
}
