package BananaFructa.TiagThings.Netowrk;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageGuiEventHandler implements IMessageHandler<MessageGuiEvent, IMessage> {
    @Override
    public IMessage onMessage(MessageGuiEvent message, MessageContext ctx) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
            if (ctx.getServerHandler().player.openContainer instanceof IGuiEventListener) {
                ((IGuiEventListener)ctx.getServerHandler().player.openContainer).onEvent(message.id,message.tagCompound);
            }
        });
        return null;
    }

}
