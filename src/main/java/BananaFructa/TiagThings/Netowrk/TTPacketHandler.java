package BananaFructa.TiagThings.Netowrk;

import BananaFructa.TiagThings.TTMain;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class TTPacketHandler {

    public static SimpleNetworkWrapper wrapper;

    public static void registerPackets() {
        wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(TTMain.name);
        //wrapper.registerMessage(MessageComputerClusterAskCountHandler.class,MessageComputerClusterAskCount.class,0, Side.SERVER);
        //wrapper.registerMessage(MessageComputerClusterCountHandler.class,MessageComputerClusterCount.class,1,Side.CLIENT);
        wrapper.registerMessage(MessageGuiEventHandler.class,MessageGuiEvent.class,2,Side.SERVER);
        wrapper.registerMessage(MessagePowerNetworkAskInfoHandler.class,MessagePowerNetworkAskInfo.class,3,Side.SERVER);
        wrapper.registerMessage(CMessageNetworkDataHandler.class, CMessageNetworkData.class,4,Side.CLIENT);
    }

}
