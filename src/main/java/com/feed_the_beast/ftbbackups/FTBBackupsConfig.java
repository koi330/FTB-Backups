package com.feed_the_beast.ftbbackups;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * @author LatvianModder
 */
public class FTBBackupsConfig {

    public static final FTBBackupsConfig INST = new FTBBackupsConfig();

    public static Configuration config;

    public static void init(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        sync();
    }

    public static boolean sync() {

        config.load();

        general.enabled = config.get(Configuration.CATEGORY_GENERAL, "enabled", true, "Enables backups.").getBoolean();
        general.silent = config.get(
                Configuration.CATEGORY_GENERAL,
                "silent",
                false,
                "If set to true, no messages will be displayed in chat/status bar.").getBoolean();
        general.backups_to_keep = config.get(
                Configuration.CATEGORY_GENERAL,
                "backups_to_keep",
                12,
                "The number of backup files to keep. More backups = more space used 0 - Infinite",
                0,
                32000).getInt();
        general.backup_timer = config.get(
                Configuration.CATEGORY_GENERAL,
                "backup_timer",
                2D,
                "Timer in hours.\n1.0 - backups every hour\n6.0 - backups every 6 hours\n0.5 - backups every 30 minutes",
                0.05D,
                600D).getDouble();
        general.compression_level = config.get(
                Configuration.CATEGORY_GENERAL,
                "compression_level",
                1,
                "0 - Disabled (output = folders)\n1 - Best speed\n9 - Smallest file size",
                0,
                9).getInt();
        general.folder = config.get(Configuration.CATEGORY_GENERAL, "folder", "", "Absolute path to backups folder.")
                .getString();
        general.display_file_size = config.get(
                Configuration.CATEGORY_GENERAL,
                "display_file_size",
                true,
                "Prints (current size | total size) when backup is done.").getBoolean();
        general.extra_files = config.get(
                Configuration.CATEGORY_GENERAL,
                "extra_files",
                new String[] {},
                "Add extra files that will be placed in backup _extra_/ folder.").getStringList();
        general.max_total_size = config.get(
                Configuration.CATEGORY_GENERAL,
                "max_total_size",
                "75%",
                "Maximum total size that is allowed in backups folder. Older backups will be deleted to free space for newer ones.\nYou can use TB, GB, MB and KB for filesizes.\nYou can use % to set maximum total size based on your available disk space. It is still limited by max total backup count, so it's not gonna fill up large drives.\nValid inputs: 50 GB, 10 MB, 33%")
                .getString();
        general.disable_level_saving = config.get(
                Configuration.CATEGORY_GENERAL,
                "disable_level_saving",
                true,
                "Disables level saving while performing backup.").getBoolean();
        general.only_if_players_online = config.get(
                Configuration.CATEGORY_GENERAL,
                "only_if_players_online",
                true,
                "Only create backups when players have been online.").getBoolean();
        general.force_on_shutdown = config.get(
                Configuration.CATEGORY_GENERAL,
                "force_on_shutdown",
                false,
                "Create a backup when server is stopped.").getBoolean();
        general.buffer_size = config.get(
                Configuration.CATEGORY_GENERAL,
                "buffer_size",
                4096,
                "Buffer size for writing files Don't change unless you know what you are doing.",
                256,
                65536).getInt();

        config.save();
        general.cachedMaxTotalSize = -1L;
        general.cachedFolder = null;
        return true;
    }

    public static final General general = new General();

    public static class General {

        public boolean enabled;
        public boolean silent;
        public int backups_to_keep;
        public double backup_timer;
        public int compression_level;
        public String folder;
        public boolean display_file_size;
        public String[] extra_files;
        public String max_total_size;
        public boolean disable_level_saving;
        public boolean only_if_players_online;
        public boolean force_on_shutdown;
        public int buffer_size;
        private long cachedMaxTotalSize = -1L;
        private File cachedFolder;

        public long time() {
            return (long) (backup_timer * 3600000L);
        }

        public long getMaxTotalSize() {
            if (cachedMaxTotalSize == -1L) {
                cachedMaxTotalSize = getMaxTotalSize0();
            }

            return cachedMaxTotalSize;
        }

        private long getMaxTotalSize0() {
            String s = BackupUtils.removeAllWhitespace(max_total_size).toUpperCase();

            if (s.endsWith("%")) {
                return (long) (Double.parseDouble(s.substring(0, s.length() - 1).trim()) * 0.01D
                        * getFolder().getTotalSpace());
            } else if (s.endsWith("TB")) {
                return Long.parseLong(s.substring(0, s.length() - 2).trim()) * BackupUtils.TB;
            } else if (s.endsWith("GB")) {
                return Long.parseLong(s.substring(0, s.length() - 2).trim()) * BackupUtils.GB;
            } else if (s.endsWith("MB")) {
                return Long.parseLong(s.substring(0, s.length() - 2).trim()) * BackupUtils.MB;
            } else if (s.endsWith("KB")) {
                return Long.parseLong(s.substring(0, s.length() - 2).trim()) * BackupUtils.KB;
            }

            return Long.parseLong(s);
        }

        public File getFolder() {
            if (cachedFolder == null) {
                cachedFolder = FTBBackupsConfig.general.folder.trim().isEmpty()
                        ? FMLCommonHandler.instance().getMinecraftServerInstance().getFile("backups")
                        : new File(FTBBackupsConfig.general.folder.trim());
            }

            return cachedFolder;
        }

        public void clearCachedFolder() {
            cachedFolder = null;
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equals(FTBBackups.MOD_ID)) {
            sync();
        }
    }
}
