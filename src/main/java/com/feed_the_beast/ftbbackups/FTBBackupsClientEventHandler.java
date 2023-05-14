package com.feed_the_beast.ftbbackups;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;

public class FTBBackupsClientEventHandler {

    public static final FTBBackupsClientEventHandler INST = new FTBBackupsClientEventHandler();
    public static int currentBackupFile = 0;
    public static int totalBackupFiles = 0;

    @SubscribeEvent
    public void onClientDisconnected(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        currentBackupFile = 0;
        totalBackupFiles = 0;
    }

    @SubscribeEvent
    public void onDebugInfoEvent(RenderGameOverlayEvent.Text event) {
        if (Minecraft.getMinecraft().gameSettings.showDebugInfo) {
            return;
        }

        if (totalBackupFiles > 0 && totalBackupFiles > currentBackupFile) {
            event.left.add(
                    EnumChatFormatting.LIGHT_PURPLE + I18n.format(
                            "ftbbackups.lang.timer_progress",
                            currentBackupFile * 100 / totalBackupFiles,
                            currentBackupFile,
                            totalBackupFiles));
        }
    }
}
