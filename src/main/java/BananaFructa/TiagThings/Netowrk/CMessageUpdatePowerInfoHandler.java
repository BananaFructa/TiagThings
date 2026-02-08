package BananaFructa.TiagThings.Netowrk;

import BananaFructa.TTIEMultiblocks.PowerNetworkInfo.NetworkData;
import BananaFructa.TTIEMultiblocks.PowerNetworkInfo.PowerNetworkInfoGui;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CMessageUpdatePowerInfoHandler implements IMessageHandler<CMessageUpdatePowerInfo, IMessage> {
    @Override
    public IMessage onMessage(CMessageUpdatePowerInfo message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(new Runnable() {
            @Override
            public void run() {
                if (Minecraft.getMinecraft().currentScreen instanceof PowerNetworkInfoGui) {
                    System.out.println("RECV_DELTA");
                    System.out.println(message.deltaTag);
                    ((PowerNetworkInfoGui) Minecraft.getMinecraft().currentScreen).updateNetworkData(message.deltaTag);
                }
            }
        });
        return null;
    }
}
