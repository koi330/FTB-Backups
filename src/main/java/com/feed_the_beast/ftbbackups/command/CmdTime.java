package com.feed_the_beast.ftbbackups.command;

import net.minecraft.command.ICommandSender;

import com.feed_the_beast.ftbbackups.BackupUtils;
import com.feed_the_beast.ftbbackups.Backups;
import com.feed_the_beast.ftbbackups.FTBBackups;
import com.feed_the_beast.ftblib.lib.command.CmdBase;

/**
 * @author LatvianModder
 */
public class CmdTime extends CmdBase {

    public CmdTime() {
        super("time", Level.ALL);
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        sender.addChatMessage(
                FTBBackups.lang(
                        sender,
                        "ftbbackups.lang.timer",
                        BackupUtils.getTimeString(Backups.INSTANCE.nextBackup - System.currentTimeMillis())));
    }
}
