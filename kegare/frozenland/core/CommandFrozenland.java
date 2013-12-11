package kegare.frozenland.core;

import java.util.List;

import kegare.frozenland.util.Version;
import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.Loader;

public class CommandFrozenland implements ICommand
{
	@Override
	public int compareTo(Object command)
	{
		return getCommandName().compareTo(((ICommand)command).getCommandName());
	}

	@Override
	public String getCommandName()
	{
		return "frozenland";
	}

	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		throw new CommandNotFoundException();
	}

	@Override
	public List getCommandAliases()
	{
		return null;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args)
	{
		StringBuilder message = new StringBuilder();

		if (sender instanceof EntityPlayerMP)
		{
			message.append(EnumChatFormatting.AQUA).append(" Frozenland ").append(EnumChatFormatting.RESET);
			message.append(Version.CURRENT.orNull()).append(" for ").append(Loader.instance().getMCVersionString());
			message.append(EnumChatFormatting.GRAY).append(" (Latest: ").append(Version.LATEST.orNull()).append(")");
		}
		else
		{
			message.append(" Frozenland ").append(Version.CURRENT.orNull()).append(" for ").append(Loader.instance().getMCVersionString());
			message.append(" (Latest: ").append(Version.LATEST.orNull()).append(")");
		}

		sender.sendChatToPlayer(ChatMessageComponent.createFromText(message.toString()));
		sender.sendChatToPlayer(ChatMessageComponent.createFromText("  " + Frozenland.metadata.description));
		sender.sendChatToPlayer(ChatMessageComponent.createFromText("  " + Frozenland.metadata.url).setColor(EnumChatFormatting.DARK_GRAY));
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender)
	{
		return sender instanceof MinecraftServer || sender instanceof EntityPlayerMP;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args)
	{
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index)
	{
		return false;
	}
}