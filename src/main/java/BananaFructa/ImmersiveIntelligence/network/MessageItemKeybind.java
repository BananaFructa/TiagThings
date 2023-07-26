package BananaFructa.ImmersiveIntelligence.network;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.network.MessageNoSpamChatComponents;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.common.items.armor.ItemIILightEngineerHelmet;
import pl.pabilo8.immersiveintelligence.common.items.armor.ItemIILightEngineerLeggings;
import pl.pabilo8.immersiveintelligence.common.items.weapons.ItemIISubmachinegun;

/*
    FROM 0.3.0
 */

public class MessageItemKeybind implements IMessage {
    int id = 0;

    public MessageItemKeybind() {
    }

    public MessageItemKeybind(int id) {
        this.id = id;
    }

    public void fromBytes(ByteBuf buf) {
        this.id = buf.readInt();
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.id);
    }

    public static class Handler implements IMessageHandler<MessageItemKeybind, IMessage> {
        public Handler() {
        }

        public IMessage onMessage(MessageItemKeybind message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                ItemStack equipped;
                switch (message.id) {
                    case 0:
                        equipped = player.getHeldItem(EnumHand.MAIN_HAND);
                        if (equipped.getItem() instanceof ItemIISubmachinegun) {
                            ItemNBTHelper.setBoolean(equipped, "shouldReload", true);
                        }
                        break;
                    case 1:
                        equipped = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                        if (equipped.getItem() instanceof ItemIILightEngineerHelmet) {
                            boolean newState = !ItemNBTHelper.getBoolean(equipped, "headgearActive");
                            ItemNBTHelper.setBoolean(equipped, "headgearActive", newState);
                            if (!newState) {
                                ctx.getServerHandler().player.removePotionEffect(MobEffects.NIGHT_VISION);
                            }

                            ImmersiveEngineering.packetHandler.sendTo(new MessageNoSpamChatComponents(new ITextComponent[]{new TextComponentTranslation("desc.immersiveintelligence." + (newState ? "infrared_enabled" : "infrared_disabled"), new Object[0])}), player);
                        }
                        break;
                    case 2:
                        equipped = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
                        if (equipped.getItem() instanceof ItemIILightEngineerLeggings) {
                            int mode = ItemNBTHelper.getInt(equipped, "exoskeletonMode");
                            ItemNBTHelper.setInt(equipped, "exoskeletonMode", mode = Utils.cycleInt(!player.isSneaking(), mode, 0, 2));
                            ImmersiveEngineering.packetHandler.sendTo(new MessageNoSpamChatComponents(new ITextComponent[]{new TextComponentTranslation("info.immersiveintelligence.exoskeleton." + mode, new Object[0])}), player);
                        }
                }

            });
            return null;
        }
    }
}
