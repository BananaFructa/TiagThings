package BananaFructa.TiagThings.Netowrk;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageComputerClusterCountHandler implements IMessageHandler<MessageComputerClusterCount, IMessage> {
    @Override
    public IMessage onMessage(MessageComputerClusterCount message, MessageContext ctx) {
        return null;
    }
}
