package BananaFructa.TiagThings.Netowrk;

import BananaFructa.TTIEMultiblocks.ControlBlocks.PIDControllerTileEntity;
import BananaFructa.TTIEMultiblocks.PowerNetworkInfo.GlobalNetworkInfoManager;
import BananaFructa.TTIEMultiblocks.PowerNetworkInfo.NetworkData;
import BananaFructa.TTIEMultiblocks.PowerNetworkInfo.NetworkElement;
import BananaFructa.TiagThings.TTMain;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class MessagePowerNetworkAskInfoHandler implements IMessageHandler<MessagePowerNetworkAskInfo, IMessage> {
    @Override
    public IMessage onMessage(MessagePowerNetworkAskInfo message, MessageContext ctx) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
            System.out.println("ASKED");
            BlockPos pos = new BlockPos(message.x,message.y,message.z);
            if (ctx.getServerHandler().player.world.getTileEntity(pos) instanceof NetworkElement) {
                UUID network = GlobalNetworkInfoManager.getNetworkFor((NetworkElement) ctx.getServerHandler().player.world.getTileEntity(pos));
                NetworkData data = GlobalNetworkInfoManager.getNetworkFromUUID(network);
                GlobalNetworkInfoManager.addNetworkSubscriber(ctx.getServerHandler().player.getPersistentID(), network);
                if (data != null) {
                    System.out.println("FOUND");
                    GlobalNetworkInfoManager.scheduleTask(()->{
                        TTPacketHandler.wrapper.sendTo(new CMessageNetworkData(data.toNBT()), ctx.getServerHandler().player);
                    });
                } else {
                    // TODO: maybe send a not found message
                }
            }
            if (ctx.getServerHandler().player.world.getTileEntity(pos) instanceof PIDControllerTileEntity) {
                PIDControllerTileEntity tile = (PIDControllerTileEntity) ctx.getServerHandler().player.world.getTileEntity(pos);
                NBTTagCompound graphs = new NBTTagCompound();
                graphs.setTag("in",tile.inputHistory.toNBT());
                graphs.setTag("out",tile.outputHistory.toNBT());
                System.out.println("FOUND_PID");
                GlobalNetworkInfoManager.scheduleTask(()->{
                    tile.subscribePlayer(ctx.getServerHandler().player.getPersistentID());
                    TTPacketHandler.wrapper.sendTo(new CMessageNetworkData(graphs), ctx.getServerHandler().player);
                });
            }
        });
        return null;
    }
}
