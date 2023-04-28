package com.feed_the_beast.ftbbackups.command;

import com.feed_the_beast.ftblib.lib.command.CmdTreeBase;
import com.feed_the_beast.ftblib.lib.command.CmdTreeHelp;

import net.minecraft.command.ICommandSender;

/**
 * @author LatvianModder
 */
public class CmdBackup extends CmdTreeBase
{
	public CmdBackup() {
		super("backup");
		addSubcommand(new CmdStart());
		addSubcommand(new CmdSize());
		addSubcommand(new CmdTime());
		addSubcommand(new CmdTreeHelp(this));
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return true;
	}
}
