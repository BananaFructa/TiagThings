package BananaFructa.TiagThings.Commands;

import BananaFructa.StgDel.StageKey;
import BananaFructa.TiagThings.RockUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;

public class SetRock extends CommandBase {
    public SetRock() {
    }

    public String getName() {
        return "setrock";
    }

    public String getUsage(ICommandSender sender) {
        return "";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (!(sender.getCommandSenderEntity() instanceof EntityPlayerMP)) return;
        EntityPlayerMP player = (EntityPlayerMP)sender.getCommandSenderEntity();
        if (!player.isCreative()) return;
        ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
        if (RockUtils.isRock(heldItem) && args.length > 0 && !args[0].chars().anyMatch((o) -> !Character.isDigit((char)o))) {
            NBTTagCompound nbt = new NBTTagCompound();
            int i = Integer.parseInt(args[0]);
            nbt.setInteger("trace", i);
            heldItem.setTagCompound(nbt);
        }

    }
}
