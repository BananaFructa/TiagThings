package BananaFructa.TiagThings.Commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.ForgeHooks;

public class Wikis extends CommandBase {
    @Override
    public String getName() {
        return "wikis";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.wikis.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP player = (EntityPlayerMP) sender.getCommandSenderEntity();
        player.sendMessage(new TextComponentString("\n\u00A7cWikis:\u00A7r\n"));
        player.sendMessage(ForgeHooks.newChatWithLinks("\u00A7eTerraFirmaCraft\u00A7r - https://tng.terrafirmacraft.com/Main_Page"));
        player.sendMessage(ForgeHooks.newChatWithLinks("\u00A7eFirmaLife\u00A7r - https://github.com/eerussianguy/firmalife/wiki"));
        player.sendMessage(ForgeHooks.newChatWithLinks("\u00A7eRailcraft\u00A7r - https://ftb.fandom.com/wiki/Railcraft"));
        player.sendMessage(ForgeHooks.newChatWithLinks("\u00A7eNuclearCraft\u00A7r - https://ftb.fandom.com/wiki/NuclearCraft or https://www.youtube.com/@NuclearCraftMod"));
        player.sendMessage(ForgeHooks.newChatWithLinks("\u00A7eTFC Farming\u00A7r - https://github.com/BananaFructa/TFC-Farming/blob/main/README.md"));
    }
}
