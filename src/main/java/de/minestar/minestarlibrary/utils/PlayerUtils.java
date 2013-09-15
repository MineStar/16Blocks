/*
 * Copyright (C) 2011 MineStar.de 
 * 
 * This file is part of ContaoPlugin.
 * 
 * ContaoPlugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * ContaoPlugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ContaoPlugin.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.minestarlibrary.utils;

import java.io.File;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlayerUtils {

    /**
     * Check if any player contains the name. At first it will search in online
     * player by testing the display and the account name and when no result is
     * found it will search in offline players data.
     * 
     * @param name
     *            Part of the target nickname. It needn't be lower case, will
     *            casted to lower case!
     * @return <code>True</code> if an player's nickname or account is existing
     *         that contains <code>name</code>, <code>false</code> if not
     */
    public static boolean isPlayerExisting(String name) {
        name = name.toLowerCase();

        // looking for display and account name at connected player
        if (getOnlinePlayer(name) != null)
            return true;

        // looking account name at all player
        OfflinePlayer[] allPlayers = Bukkit.getServer().getOfflinePlayers();
        for (OfflinePlayer player : allPlayers)
            if (player.getName().toLowerCase().contains(name))
                return true;

        return false;
    }

    /**
     * Check if the player name is existing. This method is very case sensitive
     * and only works if the player name is 1:1 the correct name!
     * 
     * @param name
     *            Correct name of the player
     * @return <code>True</code> when player is on the server or was on the
     *         server,otherwise <code>false</code>
     */
    public static boolean isPlayerExactExisting(String name) {

        if (Bukkit.getPlayerExact(name) != null)
            return true;
        else {
            File playersDir = new File(Bukkit.getWorlds().get(0).getWorldFolder(), "players/");
            if (playersDir.isDirectory())
                return new File(playersDir, name + ".dat").exists();
            else
                return false;
        }
    }

    /**
     * @return Sorted Set of all player's nicknames who has ever conntected to
     *         the server. The nicknames are all in lowercase!
     */
    public static String[] getAllPlayerNames() {

        TreeSet<String> playerNames = new TreeSet<String>();

        OfflinePlayer[] players = Bukkit.getServer().getOfflinePlayers();
        for (OfflinePlayer player : players)
            playerNames.add(player.getName().toLowerCase());

        return playerNames.toArray(new String[playerNames.size()]);
    }

    /**
     * Searches for the correct account name of the player by searching in nick-
     * and accountnames of all online player and accountnames of all offline
     * players.
     * 
     * @param name
     *            Part of the target nickname
     * @return The nickname of the player with the lowest difference to
     *         <code>nick</code>.
     */
    public static String getCorrectPlayerName(String name) {

        Player player = getOnlinePlayer(name);
        if (player != null)
            return player.getName();

        return getOfflinePlayerName(name);
    }

    /**
     * Searches for the offline player name.
     * 
     * @param name
     *            Part of the targeted account name
     * @return <code>Null</code> when no name was found, otherwise the account
     *         name
     */
    public static String getOfflinePlayerName(String name) {
        name = name.toLowerCase();
        OfflinePlayer[] offlinePlayer = Bukkit.getServer().getOfflinePlayers();
        String temp = "";
        int delta = Integer.MAX_VALUE;
        int curDelta = Integer.MAX_VALUE;
        String result = null;
        for (OfflinePlayer player : offlinePlayer) {
            temp = player.getName().toLowerCase();
            curDelta = temp.length() - name.length();
            if (curDelta < delta && temp.contains(name)) {
                delta = curDelta;
                result = player.getName();
            }
            if (delta == 0)
                return result;
        }
        return result;
    }

    /**
     * Searching for a player having the case insensitive name. If not found,
     * the first player that contains the name is returned.
     * 
     * @param name
     *            Part of the target nickname. Needn't be in lower case, will
     *            cast to it!
     * @return <code>Null</code> if no player was found, else the player object
     */
    public static Player getOnlinePlayer(String name) {

        Player[] onlinePlayer = Bukkit.getOnlinePlayers();
        // no player online
        if (onlinePlayer.length == 0)
            return null;

        Player result = null;
        int delta = Integer.MAX_VALUE;
        int curDelta = Integer.MAX_VALUE;
        String tempName = "";
        name = name.toLowerCase();

        for (Player player : onlinePlayer) {

            tempName = player.getName().toLowerCase();
            curDelta = tempName.length() - name.length();
            if (curDelta < delta && tempName.contains(name)) {
                delta = curDelta;
                result = player;
            } else {
                tempName = player.getDisplayName().toLowerCase();
                curDelta = tempName.length() - name.length();
                if (curDelta < delta && tempName.contains(name)) {
                    delta = curDelta;
                    result = player;
                }
            }
            if (delta == 0)
                return result;
        }

        return result;
    }

    /**
     * <code>IMPORTANT!</code> <br>
     * It will return ALWAYS an object doesn't matter the player is existing or
     * not! <br>
     * Searches for the name in offline player files.
     * 
     * @param name
     *            Part of the target nickname
     * @return An offline player object, ALWAYS!
     */
    public static OfflinePlayer getOfflinePlayer(String name) {
        return Bukkit.getOfflinePlayer(name);
    }

    // ***************************************************************************
    // Communication methods
    // ***************************************************************************

    // BLANK MESSAGE
    /**
     * Sending a blank uncolored message with the prefix
     * <code>[PLUGIN_NAME]</code> to the player using the method
     * <code>player.sendMessage(msg)</code>
     * 
     * @param player
     *            The recipient
     * @param pluginName
     *            Name of the plugin
     * @param message
     *            The message
     */
    public static void sendBlankMessage(Player player, String pluginName, String message) {
        player.sendMessage(message);
    }

    /**
     * Sending a blank uncolored message to the player using the method
     * <code>player.sendMessage(msg)</code>
     * 
     * @param player
     *            The recipient
     * @param message
     *            The message
     */
    public static void sendBlankMessage(Player player, String message) {
        player.sendMessage(message);
    }

    // COLORED MESSAGE

    /**
     * Sending a colored message with the prefix <code>[PLUGIN_NAME]</code> to
     * the player using the method <code>player.sendMessage(msg)</code>
     * 
     * @param player
     *            The recipient
     * @param color
     *            The color of the message {@link ChatColor}
     * @param pluginName
     *            Name of the plugin
     * @param message
     *            The message
     */
    public static void sendMessage(Player player, ChatColor color, String pluginName, String message) {
        sendBlankMessage(player, ChatColor.AQUA + "[" + pluginName + "] " + color + message);
    }

    /**
     * Sending a colored message to the player using the method
     * <code>player.sendMessage(msg)</code>
     * 
     * @param player
     *            The recipient
     * @param color
     *            The color of the message {@link ChatColor}
     * @param message
     *            The message
     */
    public static void sendMessage(Player player, ChatColor color, String message) {
        player.sendMessage(color + message);
    }

    // INFO
    /**
     * Sending a gray colored message with the prefix <code>[PLUGIN_NAME]</code>
     * to the player using the method <code>player.sendMessage(msg)</code>
     * 
     * @param player
     *            The recipient
     * @param pluginName
     *            Name of the plugin
     * @param message
     *            The message
     */
    public static void sendInfo(Player player, String pluginName, String message) {
        sendMessage(player, ChatColor.GRAY, pluginName, message);
    }

    /**
     * Sending a gray colored message to the player using the method
     * <code>player.sendMessage(msg)</code>
     * 
     * @param player
     *            The recipient
     * @param message
     *            The message
     */
    public static void sendInfo(Player player, String message) {
        sendMessage(player, ChatColor.GRAY, message);
    }

    // SUCCESS
    /**
     * Sending a green colored message with the prefix
     * <code>[PLUGIN_NAME]</code> to the player using the method
     * <code>player.sendMessage(msg)</code>
     * 
     * @param player
     *            The recipient
     * @param pluginName
     *            Name of the plugin
     * @param message
     *            The message
     */
    public static void sendSuccess(Player player, String pluginName, String message) {
        sendMessage(player, ChatColor.GREEN, pluginName, message);
    }

    /**
     * Sending a green colored message to the player using the method
     * <code>player.sendMessage(msg)</code>
     * 
     * @param player
     *            The recipient
     * @param message
     *            The message
     */
    public static void sendSuccess(Player player, String message) {
        sendMessage(player, ChatColor.GREEN, message);
    }

    // ERROR
    /**
     * Sending a red colored message with the prefix <code>[PLUGIN_NAME]</code>
     * to the player using the method <code>player.sendMessage(msg)</code>
     * 
     * @param player
     *            The recipient
     * @param pluginName
     *            Name of the plugin
     * @param message
     *            The message
     */
    public static void sendError(Player player, String pluginName, String message) {
        sendMessage(player, ChatColor.RED, pluginName, message);
    }

    /**
     * Sending a red colored message to the player using the method
     * <code>player.sendMessage(msg)</code>
     * 
     * @param player
     *            The recipient
     * @param message
     *            The message
     */
    public static void sendError(Player player, String message) {
        sendMessage(player, ChatColor.RED, message);
    }

    /**
     * Sends a list of dark red colored examples for commands with possible
     * pluginname to the player
     * 
     * @param player
     *            The recipient
     * @param pluginName
     *            Name of the plugin sending the message to the sender.
     * @param syntax
     *            Syntax of the commands, beginns normally with a
     *            <code>"/"</code>
     * @param examples
     *            List of examples for the Command<
     */
    public static void sendWrongSyntax(Player player, String pluginName, String syntax, String[] examples) {
        sendError(player, pluginName, "Wrong Syntax! Use: " + syntax);

        if (examples.length == 1)
            sendInfo(player, pluginName, "Example:");
        else if (examples.length > 1)
            sendMessage(player, ChatColor.DARK_RED, pluginName, "Examples:");

        for (int i = 0; i < examples.length; ++i)
            sendInfo(player, pluginName, examples[i]);
    }
}
