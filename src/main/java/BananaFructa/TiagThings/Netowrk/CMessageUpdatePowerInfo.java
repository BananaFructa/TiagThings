package BananaFructa.TiagThings.Netowrk;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CMessageUpdatePowerInfo implements IMessage {

    public NBTTagCompound deltaTag;

    public CMessageUpdatePowerInfo() {

    }

    public CMessageUpdatePowerInfo(NBTTagCompound deltaTag) {
        this.deltaTag = deltaTag;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        deltaTag = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf,deltaTag);
    }
}
