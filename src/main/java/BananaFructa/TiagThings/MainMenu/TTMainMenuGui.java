package BananaFructa.TiagThings.MainMenu;

import BananaFructa.TiagThings.TTMain;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class TTMainMenuGui extends GuiMainMenu {

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        this.buttonList.add(new TTMainMenuButton(1,5,60,100,17, I18n.format("menu.singleplayer")));
        this.buttonList.add(new TTMainMenuButton(2,5,77,100,17, I18n.format("menu.multiplayer")));
        this.buttonList.add(new TTMainMenuButton(0,5,94,100,17, I18n.format("menu.options")));
        GuiButton mod = new TTMainMenuButton(6,5,111,100,17, I18n.format("fml.menu.mods"));
        //Utils.WriteDeclaredField(GuiMainMenu.class,this,"modButton",mod);
        this.buttonList.add(mod);
        GuiButton realms = new TTMainMenuButton(14,5,128,100,17,I18n.format("menu.online").replace("Minecraft", "").trim());
        //Utils.WriteDeclaredField(GuiMainMenu.class,this,"realmsButton",realms);
        this.buttonList.add(realms);
        this.buttonList.add(new TTMainMenuButton(5,5,145,100,17,I18n.format("options.language")));
        this.buttonList.add(new TTMainMenuButton(4,5,162,100,17,I18n.format("menu.quit")));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(0,0,this.width,this.height, 0xff004A7F);

        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        mc.renderEngine.bindTexture(new ResourceLocation(TTMain.modId,"gui/tiag-main-menu-atlas.png"));
        this.drawTexturedModalRect(5,5,0,0,80,43);

        //mc.renderEngine.bindTexture(new ResourceLocation(TTMain.modId,"gui/tiag_mm_bkg.png"));
        //this.drawTexturedModalRect(100,100,0,0,256,192);

        DrawMinecraftBrandingsAndCopyRight();

        for (int i = 0; i < this.buttonList.size(); ++i)
        {
            this.buttonList.get(i).drawButton(this.mc, mouseX, mouseY, partialTicks);
        }

        for (int j = 0; j < this.labelList.size(); ++j)
        {
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
