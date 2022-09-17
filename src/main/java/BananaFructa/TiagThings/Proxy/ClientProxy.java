package BananaFructa.TiagThings.Proxy;

import BananaFructa.THAHModifications.TTChooseClimateGUi;
import BananaFructa.TiagThings.MainMenu.TTMainMenuGui;
import BananaFructa.thah.gui.ChooseClimateGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ClientProxy extends CommonProxy {

    public ClientProxy() {
        super();
    }

    @SubscribeEvent
    public void Tick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu && !(Minecraft.getMinecraft().currentScreen instanceof TTMainMenuGui)) {
            Minecraft.getMinecraft().displayGuiScreen(new TTMainMenuGui());
        }
    }

    @SubscribeEvent
    public void onGui(GuiOpenEvent event) {
        if (event.getGui() instanceof ChooseClimateGui) {
            event.setCanceled(true);
            Minecraft.getMinecraft().displayGuiScreen(new TTChooseClimateGUi());
        }
    }
}
