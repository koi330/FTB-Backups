package com.feed_the_beast.ftbbackups.command;

import com.feed_the_beast.ftbbackups.Backup;
import com.feed_the_beast.ftbbackups.BackupUtils;
import com.feed_the_beast.ftbbackups.Backups;
import com.feed_the_beast.ftbbackups.FTBBackups;
import com.feed_the_beast.ftbbackups.FTBBackupsConfig;
import com.feed_the_beast.ftblib.lib.command.CmdBase;

import net.minecraft.command.ICommandSender;

/**
 * @author LatvianModder
 */
public class CmdSize extends CmdBase
{
	public CmdSize() {
		super("size", Level.OP_OR_SP);
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		long totalSize = 0L;

		for (Backup backup : Backups.INSTANCE.backups)
		{
			totalSize += backup.size;
		}

		sender.addChatMessage(FTBBackups.lang(sender, "ftbbackups.lang.size.1", BackupUtils.getSizeString(sender.getEntityWorld().getSaveHandler().getWorldDirectory())));
		sender.addChatMessage(FTBBackups.lang(sender, "ftbbackups.lang.size.2", BackupUtils.getSizeString(totalSize)));
		sender.addChatMessage(FTBBackups.lang(sender, "ftbbackups.lang.size.3", BackupUtils.getSizeString(Math.min(FTBBackupsConfig.general.getMaxTotalSize(), FTBBackupsConfig.general.getFolder().getFreeSpace()))));
	}
}
