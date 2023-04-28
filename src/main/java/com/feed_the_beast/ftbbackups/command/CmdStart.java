package com.feed_the_beast.ftbbackups.command;

import com.feed_the_beast.ftbbackups.Backups;
import com.feed_the_beast.ftbbackups.FTBBackups;
import com.feed_the_beast.ftblib.lib.command.CmdBase;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.List;

/**
 * @author LatvianModder
 */
public class CmdStart extends CmdBase
{

	public CmdStart() {
		super("start", Level.OP_OR_SP);
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		MinecraftServer server = MinecraftServer.getServer();
		if (Backups.INSTANCE.run(server, sender, args.length == 0 ? "" : args[0]))
		{
			for (EntityPlayerMP player : (List<EntityPlayerMP>) server.getConfigurationManager().playerEntityList)
			{
				player.addChatMessage(FTBBackups.lang(player, "ftbbackups.lang.manual_launch", sender.getCommandSenderName()));
			}
		}
		else
		{
			sender.addChatMessage(FTBBackups.lang(sender, "ftbbackups.lang.already_running"));
		}
	}
}
