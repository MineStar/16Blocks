package de.minestar.sixteenblocks.Core;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

public class Settings {
    private static int AREA_SIZE_X = 32, AREA_SIZE_Z = 32;
    private static int MINIMUM_BUILD_Y = 6, MAXIMUM_BUILD_Y = 50;
    private static int BASE_Y = 4;

    private static int CHAT_PAUSE_IN_SECONDS = 5;

    private static int SKINS_LEFT = 3;
    private static int SKINS_RIGHT = 3;

    private static int MAX_BLOCKS_REPLACE_AT_ONCE = 100;
    private static int TICKS_BETWEEN_REPLACE = 5;
    private static int CREATE_ROWS_AT_ONCE = 2;

    private static Vector SPAWN_VECTOR = new Vector(0, 4, 0), INFOWALL_VECTOR = new Vector(0, 4, 0);

    public static void init(File dataFolder) {
        try {
            File file = new File(dataFolder, "config.yml");
            if (!file.exists()) {
                saveSettings(dataFolder);
                return;
            }

            YamlConfiguration config = new YamlConfiguration();
            config.load(file);

            BASE_Y = config.getInt("general.baseLevel", BASE_Y);
            CHAT_PAUSE_IN_SECONDS = config.getInt("general.chatPauseInSeconds", CHAT_PAUSE_IN_SECONDS);

            AREA_SIZE_X = config.getInt("Zone.sizeX", AREA_SIZE_X);
            AREA_SIZE_Z = config.getInt("Zone.sizeZ", AREA_SIZE_Z);
            MINIMUM_BUILD_Y = config.getInt("Zone.minimumBuildLevel", MINIMUM_BUILD_Y);

            SKINS_LEFT = config.getInt("Skins.left", SKINS_LEFT);
            SKINS_RIGHT = config.getInt("Skins.right", SKINS_RIGHT);

            MAX_BLOCKS_REPLACE_AT_ONCE = config.getInt("Threads.Structures.MaxReplaceAtOnce", MAX_BLOCKS_REPLACE_AT_ONCE);
            TICKS_BETWEEN_REPLACE = config.getInt("Threads.Structures.ticksBetweenReplace", TICKS_BETWEEN_REPLACE);
            CREATE_ROWS_AT_ONCE = config.getInt("Threads.Structures.createRowsAtOnce", CREATE_ROWS_AT_ONCE);

            SPAWN_VECTOR = config.getVector("Locations.spawn", SPAWN_VECTOR);
            INFOWALL_VECTOR = config.getVector("Locations.infoWall", INFOWALL_VECTOR);
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

            config.set("general.baseLevel", BASE_Y);
            config.set("general.chatPauseInSeconds", CHAT_PAUSE_IN_SECONDS);

            config.set("Zone.sizeX", AREA_SIZE_X);
            config.set("Zone.sizeZ", AREA_SIZE_Z);
            config.set("Zone.minimumBuildLevel", MINIMUM_BUILD_Y);

            config.set("Skins.left", SKINS_LEFT);
            config.set("Skins.right", SKINS_RIGHT);

            config.set("Threads.Structures.MaxReplaceAtOnce", MAX_BLOCKS_REPLACE_AT_ONCE);
            config.set("Threads.Structures.ticksBetweenReplace", TICKS_BETWEEN_REPLACE);
            config.set("Threads.Structures.createRowsAtOnce", CREATE_ROWS_AT_ONCE);

            config.set("Locations.spawn", SPAWN_VECTOR);
            config.set("Locations.infoWall", INFOWALL_VECTOR);

            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getAreaSizeX() {
        return AREA_SIZE_X;
    }

    public static int getAreaSizeZ() {
        return AREA_SIZE_Z;
    }

    public static int getMinimumBuildY() {
        return MINIMUM_BUILD_Y;
    }

    public static int getMaximumBuildY() {
        return MAXIMUM_BUILD_Y;
    }

    public static int getBaseY() {
        return BASE_Y;
    }

    public static int getMaxBlockxReplaceAtOnce() {
        return MAX_BLOCKS_REPLACE_AT_ONCE;
    }

    public static int getTicksBetweenReplace() {
        return TICKS_BETWEEN_REPLACE;
    }

    public static Vector getSpawnVector() {
        return SPAWN_VECTOR;
    }

    public static Vector getInfoWallVector() {
        return INFOWALL_VECTOR;
    }

    public static int getSkinsLeft() {
        return SKINS_LEFT;
    }

    public static int getSkinsRight() {
        return SKINS_RIGHT;
    }

    public static int getChatPauseTimeInSeconds() {
        return CHAT_PAUSE_IN_SECONDS;
    }

    public static int getCreateRowsAtOnce() {
        return CREATE_ROWS_AT_ONCE;
    }
}
