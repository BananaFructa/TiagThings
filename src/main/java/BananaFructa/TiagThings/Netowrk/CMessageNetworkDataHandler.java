package BananaFructa.TiagThings.Netowrk;

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
                    System.out.println("RECIEVED");
                    System.out.println(message.networkData);
                    ((PowerNetworkInfoGui) Minecraft.getMinecraft().currentScreen).setNetworkData(NetworkData.fromNBT(message.networkData));
                }
            }
        });
        return null;
    }
}
