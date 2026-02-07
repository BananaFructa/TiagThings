package BananaFructa.TiagThings.Netowrk;

import BananaFructa.TTIEMultiblocks.PowerNetworkInfo.GlobalNetworkInfoManager;
import BananaFructa.TTIEMultiblocks.PowerNetworkInfo.NetworkData;
import BananaFructa.TiagThings.TTMain;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessagePowerNetworkAskInfoHandler implements IMessageHandler<MessagePowerNetworkAskInfo, IMessage> {
    @Override
    public IMessage onMessage(MessagePowerNetworkAskInfo message, MessageContext ctx) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
            System.out.println("ASKED");
            BlockPos pos = new BlockPos(message.x,message.y,message.z);
            NetworkData data = GlobalNetworkInfoManager.getNetworkDataFor(ctx.getServerHandler().player.world.getTileEntity(pos));
            if (data != null) {
                System.out.println("FOUND");
                TTPacketHandler.wrapper.sendTo(new CMessageNetworkData(data.toNBT()),ctx.getServerHandler().player);
            } else {
                // TODO: maybe send a not found message
            }
        });
        return null;
    }
}
