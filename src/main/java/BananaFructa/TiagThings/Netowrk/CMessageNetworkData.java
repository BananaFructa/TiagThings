package BananaFructa.TiagThings.Netowrk;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CMessageNetworkData implements IMessage {

    NBTTagCompound networkData;

    public CMessageNetworkData(NBTTagCompound networkData) {
        this.networkData = networkData;
    }

    public CMessageNetworkData() {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        networkData = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf,networkData);
    }
}
