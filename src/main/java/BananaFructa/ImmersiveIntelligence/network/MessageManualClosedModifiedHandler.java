package BananaFructa.ImmersiveIntelligence.network;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import pl.pabilo8.immersiveintelligence.common.network.MessageManualClose;

public class MessageManualClosedModifiedHandler implements IMessageHandler<MessageManualClose, IMessage> {
    public MessageManualClosedModifiedHandler() {
    }

    public IMessage onMessage(MessageManualClose message, MessageContext ctx) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
            EntityPlayerMP player = ctx.getServerHandler().player;
            World world = ImmersiveEngineering.proxy.getClientWorld();
            if (world != null && player != null) {
                ItemStack mainItem = player.getHeldItemMainhand();
                ItemStack offItem = player.getHeldItemOffhand();
                boolean main = !mainItem.isEmpty() && mainItem.getItem() == IEContent.itemTool && mainItem.getItemDamage() == 3;
                boolean off = !offItem.isEmpty() && offItem.getItem() == IEContent.itemTool && offItem.getItemDamage() == 3;
                ItemStack target = main ? mainItem : offItem;
                if (main || off) {
                    if ((message.skin == null || message.skin.isEmpty()) && ItemNBTHelper.hasKey(target, "lastSkin")) {
                        ItemNBTHelper.remove(target, "lastSkin");
                    } else if (message.skin != null) {
                        ItemNBTHelper.setString(target, "lastSkin", message.skin);
                    }
                }
            }

        });
        return null;
    }
}
