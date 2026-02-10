package BananaFructa.TiagThings.Netowrk;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessagePowerNetworkUnsubscribe implements IMessage {

    public MessagePowerNetworkUnsubscribe() {

    }

    public MessagePowerNetworkUnsubscribe(int x, int y, int z) {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }
}
