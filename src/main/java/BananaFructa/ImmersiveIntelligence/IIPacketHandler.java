package BananaFructa.ImmersiveIntelligence;

import BananaFructa.ImmersiveIntelligence.network.MessageItemKeybind;
import BananaFructa.ImmersiveIntelligence.network.MessageManualClosedModifiedHandler;
import BananaFructa.TiagThings.Utils;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import pl.pabilo8.immersiveintelligence.common.network.MessageManualClose;

public class IIPacketHandler {

    public static SimpleNetworkWrapper wrapper;

    public static void registerPackets() {
        wrapper = NetworkRegistry.INSTANCE.newSimpleChannel("tiagthingsII");
        wrapper.registerMessage(MessageItemKeybind.Handler.class,MessageItemKeybind.class,0, Side.SERVER);
        pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler.INSTANCE.registerMessage(MessageManualClosedModifiedHandler.class, MessageManualClose.class, 18, Side.SERVER);
    }

}
