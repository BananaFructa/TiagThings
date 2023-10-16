package BananaFructa.TiagThings.Netowrk;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageComputerClusterAskCount implements IMessage {

    int x,y,z;

    public MessageComputerClusterAskCount() {

    }

    public MessageComputerClusterAskCount(int x,int y,int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }
}
