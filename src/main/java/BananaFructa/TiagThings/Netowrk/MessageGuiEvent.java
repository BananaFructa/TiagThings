package BananaFructa.TiagThings.Netowrk;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.io.IOException;

public class MessageGuiEvent implements IMessage {

    int id;
    NBTTagCompound tagCompound;

    public MessageGuiEvent() {

    }

    public MessageGuiEvent(int id,NBTTagCompound tagCompound) {
        this.id = id;
        this.tagCompound = tagCompound;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        id = buf.readInt();
        PacketBuffer wrapper = new PacketBuffer(buf);
        try {
            tagCompound = wrapper.readCompoundTag();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(id);
        PacketBuffer wrapper = new PacketBuffer(buf);
        wrapper.writeCompoundTag(tagCompound);
    }
}
