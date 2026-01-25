package BananaFructa.TTIEMultiblocks.Commands;

import BananaFructa.TTIEMultiblocks.TileEntities.AnimatedOBJTileEntity;
//import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityAE2CompatMultiblock;
//import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityComputerClusterUnit_AE2;
import BananaFructa.TTIEMultiblocks.Utils.MultiblockAnimation.AnimationGroup;
//import appeng.api.util.AEPartLocation;
//import appeng.tile.networking.TileCableBus;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.IClientCommand;

import javax.annotation.Nullable;
import javax.vecmath.Vector3f;
import java.util.Collections;
import java.util.List;

public class AdjustOBJAnimatedPivot extends CommandBase implements IClientCommand {

    @Override
    public String getName() {
        return "adjustOBJAnimationPivot";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        BlockPos pos = parseBlockPos(sender,args,0,false);
        TileEntity te = sender.getEntityWorld().getTileEntity(pos);

        if (te instanceof AnimatedOBJTileEntity) {
            if (args.length >= 7) {
                try {
                    float x = Float.parseFloat(args[3]);
                    float y = Float.parseFloat(args[4]);
                    float z = Float.parseFloat(args[5]);
                    String name = args[6];
                    //Vector3f newPivot = applyOffset(((AnimatedOBJTileEntity)te).modelGroups,name,new Vector3f(x,y,z));
                    //if (newPivot == null) {
                    //    sender.sendMessage(new TextComponentString("No group with that name!"));
                    //} else {
                    //    sender.sendMessage(new TextComponentString("New pivot for group \"" + name +  "\" is " + newPivot.x + " " + newPivot.y + " " + newPivot.z));
                    //}
                } catch (NumberFormatException err) {
                    sender.sendMessage(new TextComponentString("Not a float offset!"));
                }
            } else {
                sender.sendMessage(new TextComponentString("Incomplete offset!"));
            }
        } else {
            sender.sendMessage(new TextComponentString("Not an OBJ animated tile entity!"));
        }
    }

    public Vector3f applyOffset(AnimationGroup base,String name,Vector3f offset){
        if (base.name.equals(name)) {
            base.pivot.add(offset);
            return base.pivot;
        } else {
            if (base.isFinal()) return null;
            for (AnimationGroup subGroup : base.subGroups) {
                Vector3f returned = applyOffset(subGroup,name,offset);
                if (returned != null) return returned;
            }
        }
        return null;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length > 0 && args.length <= 3) return getTabCompletionCoordinate(args, 0, targetPos);
        else return Collections.<String>emptyList();
    }

    @Override
    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
        return false;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
