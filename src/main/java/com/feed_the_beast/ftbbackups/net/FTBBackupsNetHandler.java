package com.feed_the_beast.ftbbackups.net;

import com.feed_the_beast.ftbbackups.FTBBackups;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class FTBBackupsNetHandler
{
	public static final SimpleNetworkWrapper NET = new SimpleNetworkWrapper(FTBBackups.MOD_ID);

	public static void init()
	{
		NET.registerMessage(new MessageBackupProgress.Handler(), MessageBackupProgress.class, 1, Side.CLIENT);
	}
}
