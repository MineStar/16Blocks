package de.minestar.sixteenblocks.core;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Console {
    private static String pluginName = "";
    private static Logger logger = Logger.getLogger("Minecraft");

    public static void setPluginName(String pluginName) {
        Console.pluginName = pluginName;
    }

    public static void log(String message, Level level) {
        logger.log(level, "[ " + pluginName + " ] " + message);
    }

    public static void logInfo(String message) {
        log(message, Level.INFO);
    }

    public static void logWarning(String message) {
        log(message, Level.WARNING);
    }
}
