package BananaFructa.TTIEMultiblocks.Commands;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityAE2CompatMultiblock;
import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class GetTEPos extends CommandBase {

    @Override
    public String getName() {
        return "gettepos";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        BlockPos pos = parseBlockPos(sender,args,0,false);
        TileEntity te = sender.getEntityWorld().getTileEntity(pos);
        if (te instanceof TileEntityMultiblockPart) {
            int poste = ((TileEntityMultiblockPart<?>) te).field_174879_c;
            sender.sendMessage(new TextComponentString("Multiblock Part has pos code: " + poste + "."));
        } else if (te instanceof TileEntityAE2CompatMultiblock) {
            int poste = ((TileEntityAE2CompatMultiblock<?>) te).pos;
            sender.sendMessage(new TextComponentString("Part in AE2 compatibility mode! (TileEntityAE2CompatMultiblock.class)"));
            sender.sendMessage(new TextComponentString("Multiblock Part has pos code: " + poste + "."));
        } else {

            sender.sendMessage(new TextComponentString("Not a multiblock part."));
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length > 0 && args.length <= 3) return getTabCompletionCoordinate(args, 0, targetPos);
        else return Collections.<String>emptyList();
    }

}
