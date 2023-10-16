package BananaFructa.TiagThings.Netowrk;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityComputerClusterUnit_AE2;
import appeng.api.util.AEPartLocation;
import appeng.block.networking.BlockCableBus;
import appeng.tile.networking.TileCableBus;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageComputerClusterAskCountHandler implements IMessageHandler<MessageComputerClusterAskCount, IMessage> {
    @Override
    public IMessage onMessage(MessageComputerClusterAskCount message, MessageContext ctx) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
            if (ctx.getServerHandler().player.getDistance(message.x, message.y, message.z) < 6) {
                BlockPos pos = new BlockPos(message.x, message.y, message.z);
                TileEntity te = ctx.getServerHandler().player.world.getTileEntity(pos);
                if (te instanceof TileCableBus) {
                    TileCableBus tcb = (TileCableBus) te;
                    tcb.getCableBus().getPart(AEPartLocation.INTERNAL).getGridNode().getGrid().getMachines(TileEntityComputerClusterUnit_AE2.class);
                }
            }
        });
        return null;
    }
}
