package de.minestar.sixteenblocks.Core;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TextUtils {
    private static String pluginName = "";
    private static Logger logger = Logger.getLogger("Minecraft");

    public static void setPluginName(String pluginName) {
        TextUtils.pluginName = pluginName;
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

    public static void sendMessage(Player player, ChatColor color, String message) {
        player.sendMessage(ChatColor.AQUA + "[ " + pluginName + " ] " + color + message);
    }

    public static void sendError(Player player, String message) {
        sendMessage(player, ChatColor.RED, message);
    }

    public static void sendInfo(Player player, String message) {
        sendMessage(player, ChatColor.GRAY, message);
    }

    public static void sendSuccess(Player player, String message) {
        sendMessage(player, ChatColor.GREEN, message);
    }
}
