package BananaFructa.TiagThings.Netowrk;

import BananaFructa.TTIEMultiblocks.Gui.PIDControllerGui;
import BananaFructa.TTIEMultiblocks.PowerNetworkInfo.ModularList;
import BananaFructa.TTIEMultiblocks.PowerNetworkInfo.NetworkData;
import BananaFructa.TTIEMultiblocks.PowerNetworkInfo.PowerNetworkInfoGui;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CMessageNetworkDataHandler implements IMessageHandler<CMessageNetworkData, IMessage> {
    @Override
    public IMessage onMessage(CMessageNetworkData message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(new Runnable() {
            @Override
            public void run() {
                if (Minecraft.getMinecraft().currentScreen instanceof PowerNetworkInfoGui) {
                    PowerNetworkInfoGui networkInfoGui = new PowerNetworkInfoGui();
                    networkInfoGui.setNetworkData(NetworkData.fromNBT(message.networkData));
                    Minecraft.getMinecraft().displayGuiScreen(networkInfoGui);
                } else if (Minecraft.getMinecraft().currentScreen instanceof PIDControllerGui) {
                    NBTTagCompound tag = message.networkData;
                    ModularList in = ModularList.fromNBT(tag.getCompoundTag("in"));
                    ModularList out = ModularList.fromNBT(tag.getCompoundTag("out"));
                    ((PIDControllerGui) Minecraft.getMinecraft().currentScreen).setInputOutput(in,out);
                } else {
                    TTPacketHandler.wrapper.sendToServer(new MessagePowerNetworkUnsubscribe());
                }
            }
        });
        return null;
    }
}
