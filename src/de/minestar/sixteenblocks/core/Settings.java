package de.minestar.sixteenblocks.core;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

public class Settings {
    private static int areaSizeX = 32, areaSizeZ = 32;
    private static int minimumBuildY = 5;
    private static int baseY = 4;

    private static int MAX_BLOCKS_REPLACE_AT_ONCE = 50;
    private static int TICKS_BETWEEN_REPLACE = 20;

    public static void init(File dataFolder) {
        try {
            File file = new File(dataFolder, "config.yml");
            if (!file.exists()) {
                saveSettings(dataFolder);
                return;
            }

            YamlConfiguration config = new YamlConfiguration();
            config.load(file);

            baseY = config.getInt("general.baseLevel", baseY);

            areaSizeX = config.getInt("Zone.sizeX", areaSizeX);
            areaSizeZ = config.getInt("Zone.sizeZ", areaSizeZ);
            minimumBuildY = config.getInt("Zone.minimumBuildLevel", minimumBuildY);

            MAX_BLOCKS_REPLACE_AT_ONCE = config.getInt("Threads.Structures.MaxReplaceAtOnce", MAX_BLOCKS_REPLACE_AT_ONCE);
            TICKS_BETWEEN_REPLACE = config.getInt("Threads.Structures.ticksBetweenReplace", TICKS_BETWEEN_REPLACE);

        } catch (Exception e) {
            e.printStackTrace();
            saveSettings(dataFolder);
        }
    }
    private static void saveSettings(File dataFolder) {
        try {
            File file = new File(dataFolder, "config.yml");
            boolean fileExists = file.exists();

            YamlConfiguration config = new YamlConfiguration();
            if (fileExists)
                config.load(file);

            config.set("general.baseLevel", baseY);

            config.set("Zone.sizeX", areaSizeX);
            config.set("Zone.sizeZ", areaSizeZ);
            config.set("Zone.minimumBuildLevel", minimumBuildY);

            config.set("Threads.Structures.MaxReplaceAtOnce", MAX_BLOCKS_REPLACE_AT_ONCE);
            config.set("Threads.Structures.ticksBetweenReplace", TICKS_BETWEEN_REPLACE);

            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static int getAreaSizeX() {
        return areaSizeX;
    }
    public static int getAreaSizeZ() {
        return areaSizeZ;
    }
    public static int getMinimumBuildY() {
        return minimumBuildY;
    }
    public static int getBaseY() {
        return baseY;
    }
    public static int getMaxBlockxReplaceAtOnce() {
        return MAX_BLOCKS_REPLACE_AT_ONCE;
    }
    public static int getTicksBetweenReplac() {
        return TICKS_BETWEEN_REPLACE;
    }

}
