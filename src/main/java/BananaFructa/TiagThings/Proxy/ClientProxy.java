package BananaFructa.TiagThings.Proxy;

import BananaFructa.TiagThings.MainMenu.TTMainMenuGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
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
}
