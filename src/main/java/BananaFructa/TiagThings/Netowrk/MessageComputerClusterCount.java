package BananaFructa.TiagThings.Netowrk;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageComputerClusterCount implements IMessage {

    public int count;

    public MessageComputerClusterCount() {

    }

    public MessageComputerClusterCount(int count) {
        this.count = count;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        count = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(count);
    }
}
