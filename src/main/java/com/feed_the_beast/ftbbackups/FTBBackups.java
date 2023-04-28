package com.feed_the_beast.ftbbackups;

import com.feed_the_beast.ftbbackups.net.FTBBackupsNetHandler;
import com.feed_the_beast.ftbbackups.command.CmdBackup;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IChatComponent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;

import com.feed_the_beast.ftblib.lib.util.SidedUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;

@Mod(
		modid = FTBBackups.MOD_ID,
		name = FTBBackups.MOD_NAME,
		version = FTBBackups.VERSION,
		acceptableRemoteVersions = "*"
)
public class FTBBackups
{
	public static final String MOD_ID = "ftbbackups";
	public static final String MOD_NAME = "FTB Backups";
	public static final String VERSION = "GRADLETOKEN_VERSION";
	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

	public static IChatComponent lang(@Nullable ICommandSender sender, String key, Object... args)
	{
		return SidedUtils.lang(sender, MOD_ID, key, args);
	}

	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{
		FTBBackupsNetHandler.init();
		FTBBackupsConfig.init(event);

		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(FTBBackupsConfig.INST);
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			FMLCommonHandler.instance().bus().register(FTBBackupsClientEventHandler.INST);
			MinecraftForge.EVENT_BUS.register(FTBBackupsClientEventHandler.INST);
		}
	}

	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CmdBackup());
	}

	@Mod.EventHandler
	public void onServerStarted(FMLServerStartedEvent event)
	{
		Backups.INSTANCE.init();
	}

	@Mod.EventHandler
	public void onServerStopping(FMLServerStoppingEvent event)
	{
		if (FTBBackupsConfig.general.force_on_shutdown)
		{
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

			if (server != null)
			{
				Backups.INSTANCE.run(server, server, "");
			}
		}
	}

	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event)
	{
		if (event.phase != TickEvent.Phase.START)
		{
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

			if (server != null)
			{
				Backups.INSTANCE.tick(server, System.currentTimeMillis());
			}
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
	{
		Backups.INSTANCE.hadPlayersOnline = true;
	}
}
