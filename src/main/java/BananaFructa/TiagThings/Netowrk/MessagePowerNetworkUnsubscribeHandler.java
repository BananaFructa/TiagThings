package BananaFructa.TiagThings.Netowrk;

import BananaFructa.TTIEMultiblocks.PowerNetworkInfo.GlobalNetworkInfoManager;
import BananaFructa.TTIEMultiblocks.PowerNetworkInfo.NetworkData;
import BananaFructa.TTIEMultiblocks.PowerNetworkInfo.NetworkElement;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class MessagePowerNetworkUnsubscribeHandler implements IMessageHandler<MessagePowerNetworkUnsubscribe, IMessage> {
    @Override
    public IMessage onMessage(MessagePowerNetworkUnsubscribe message, MessageContext ctx) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
            System.out.println("UNSUB");
            GlobalNetworkInfoManager.removeNetworkSubscriber(ctx.getServerHandler().player.getPersistentID());
        });
        return null;
    }
}
