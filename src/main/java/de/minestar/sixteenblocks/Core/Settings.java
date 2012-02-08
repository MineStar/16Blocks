package de.minestar.sixteenblocks.Core;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

public class Settings {
    private static int areaSizeX = 32, areaSizeZ = 32;
    private static int minimumBuildY = 5;
    private static int baseY = 4;

    private static int skinsLeft = 3;
    private static int skinsRight = 3;

    private static int MAX_BLOCKS_REPLACE_AT_ONCE = 100;
    private static int TICKS_BETWEEN_REPLACE = 10;

    private static Vector spawnVector = new Vector(0, 4, 0), infoWallVector = new Vector(0, 4, 0);

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

            skinsLeft = config.getInt("Skins.left", skinsLeft);
            skinsRight = config.getInt("Skins.right", skinsRight);

            MAX_BLOCKS_REPLACE_AT_ONCE = config.getInt("Threads.Structures.MaxReplaceAtOnce", MAX_BLOCKS_REPLACE_AT_ONCE);
            TICKS_BETWEEN_REPLACE = config.getInt("Threads.Structures.ticksBetweenReplace", TICKS_BETWEEN_REPLACE);

            spawnVector = config.getVector("Locations.spawn", spawnVector);
            infoWallVector = config.getVector("Locations.infoWall", infoWallVector);
        } catch (Exception e) {
            e.printStackTrace();
            saveSettings(dataFolder);
        }
    }

    public static void saveSettings(File dataFolder) {
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

            config.set("Skins.left", skinsLeft);
            config.set("Skins.right", skinsRight);

            config.set("Threads.Structures.MaxReplaceAtOnce", MAX_BLOCKS_REPLACE_AT_ONCE);
            config.set("Threads.Structures.ticksBetweenReplace", TICKS_BETWEEN_REPLACE);

            config.set("Locations.spawn", spawnVector);
            config.set("Locations.infoWall", infoWallVector);

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

    public static int getTicksBetweenReplace() {
        return TICKS_BETWEEN_REPLACE;
    }

    public static Vector getSpawnVector() {
        return spawnVector;
    }

    public static Vector getInfoWallVector() {
        return infoWallVector;
    }

    public static int getSkinsLeft() {
        return skinsLeft;
    }

    public static int getSkinsRight() {
        return skinsRight;
    }

}
