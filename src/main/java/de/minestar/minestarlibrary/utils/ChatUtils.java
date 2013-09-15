/*
 * Copyright (C) 2012 MineStar.de 
 * 
 * This file is part of MineStarLibrary.
 * 
 * MineStarLibrary is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * MineStarLibrary is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MineStarLibrary.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.minestarlibrary.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * This is a Wrapper for ConsoleUtils and PlayerUtils chat methods. When using a
 * command which can be executed by player and console you should use this
 * method to get the right output
 * 
 * @author Meldanor, GeMoschen
 * 
 */
public class ChatUtils {

    /**
     * Concat all strings in the array to one string seperated by single space
     * character
     * 
     * @param args
     *            The string array containing the
     * @return Single string
     */
    public static String getMessage(String[] args) {
        return getMessage(args, " ");
    }

    /**
     * Concat all strings in the array to one string seperated by delimter
     * 
     * @param args
     *            The string array containing the
     * @param delimiter
     *            Seperator between the single strings
     * @return Single string
     */
    public static String getMessage(String[] args, String delimiter) {
        return getMessage(args, delimiter, 0);
    }

    /**
     * Concat all strings in the array to one string seperated by delimter
     * 
     * @param args
     *            The string array containing the
     * @param start
     *            The index of the array where the message fragmet
     * @return Single string
     */
    public static String getMessage(String[] args, int start) {
        return getMessage(args, " ", start);
    }

    /**
     * Concat all strings in the array to one string seperated by delimter
     * 
     * @param args
     *            The string array containing the
     * @param delimiter
     *            Seperator between the single strings
     * @param start
     *            The index of the array where the message fragmet
     * 
     * @return Single string
     */
    public static String getMessage(String[] args, String delimiter, int start) {
        if (start < 0 || start >= args.length)
            throw new RuntimeException("start is out of range!");
        StringBuilder sBuilder = new StringBuilder(256);
        int i = start;
        for (; i < args.length - 1; ++i) {
            sBuilder.append(args[i]);
            sBuilder.append(delimiter);
        }
        sBuilder.append(args[i]);
        return sBuilder.toString();
    }

    // ***************************************************************************
    // Communication methods
    // ***************************************************************************

    /**
     * Sending an information to the reciever. <br>
     * When <code>reciever</code> is a {@link Player}, it will use
     * {@link PlayerUtils#sendInfo(Player, String, String) PlayerUtils.sendInfo} <br>
     * When <code>reciever</code> is a {@link ConsoleCommandSender}, it will use
     * {@link ConsoleUtils#printInfo(String, String) ConsoleUtils.printInfo} <br>
     * 
     * @param reciever
     *            The reciever of the message. Must be an instance of
     *            {@link ConsoleCommandSender} or {@link Player}
     * @param pluginName
     *            The name of the plugin
     * @param message
     *            The message to be send
     */
    public static void writeInfo(CommandSender reciever, String pluginName, String message) {
        if (reciever instanceof Player)
            PlayerUtils.sendInfo((Player) reciever, pluginName, message);
        else if (reciever instanceof ConsoleCommandSender)
            ConsoleUtils.printInfo(pluginName, message);
        else
            throwException(reciever);
    }

    /**
     * Sending an information to the reciever. <br>
     * When <code>reciever</code> is a {@link Player}, it will use
     * {@link PlayerUtils#sendInfo(Player, String) PlayerUtils.sendInfo} <br>
     * When <code>reciever</code> is a {@link ConsoleCommandSender}, it will use
     * {@link ConsoleUtils#printInfo(String) ConsoleUtils.printInfo} <br>
     * 
     * @param reciever
     *            The reciever of the message. Must be an instance of
     *            {@link ConsoleCommandSender} or {@link Player}
     * @param message
     *            The message to be send
     */
    public static void writeInfo(CommandSender reciever, String message) {
        if (reciever instanceof Player)
            PlayerUtils.sendInfo((Player) reciever, message);
        else if (reciever instanceof ConsoleCommandSender)
            ConsoleUtils.printInfo(message);
        else
            throwException(reciever);
    }

    /**
     * Sending an success hint to the reciever. <br>
     * When <code>reciever</code> is a {@link Player}, it will use
     * {@link PlayerUtils#sendSuccess(Player, String, String)
     * PlayerUtils.sendSuccess} <br>
     * When <code>reciever</code> is a {@link ConsoleCommandSender}, it will use
     * {@link ConsoleUtils#printInfo(String, String) ConsoleUtils.printInfo} <br>
     * 
     * @param reciever
     *            The reciever of the message. Must be an instance of
     *            {@link ConsoleCommandSender} or {@link Player}
     * @param pluginName
     *            The name of the plugin
     * @param message
     *            The message to be send
     */
    public static void writeSuccess(CommandSender reciever, String pluginName, String message) {
        if (reciever instanceof Player)
            PlayerUtils.sendSuccess((Player) reciever, pluginName, message);
        else if (reciever instanceof ConsoleCommandSender)
            ConsoleUtils.printInfo(pluginName, "Success! " + message);
        else
            throwException(reciever);
    }

    /**
     * Sending an success hint to the reciever. <br>
     * When <code>reciever</code> is a {@link Player}, it will use
     * {@link PlayerUtils#sendSuccess(Player, String) PlayerUtils.sendSuccess} <br>
     * When <code>reciever</code> is a {@link ConsoleCommandSender}, it will use
     * {@link ConsoleUtils#printInfo(String) ConsoleUtils.printInfo} <br>
     * 
     * @param reciever
     *            The reciever of the message. Must be an instance of
     *            {@link ConsoleCommandSender} or {@link Player}
     * @param message
     *            The message to be send
     */
    public static void writeSuccess(CommandSender reciever, String message) {
        if (reciever instanceof Player)
            PlayerUtils.sendSuccess((Player) reciever, message);
        else if (reciever instanceof ConsoleCommandSender)
            ConsoleUtils.printInfo("Success! " + message);
        else
            throwException(reciever);
    }

    /**
     * Sending an error to the reciever. <br>
     * When <code>reciever</code> is a {@link Player}, it will use
     * {@link PlayerUtils#sendError(Player, String, String)
     * PlayerUtils.sendError} <br>
     * When <code>reciever</code> is a {@link ConsoleCommandSender}, it will use
     * {@link ConsoleUtils#printError(String, String) ConsoleUtils.printError} <br>
     * 
     * @param reciever
     *            The reciever of the message. Must be an instance of
     *            {@link ConsoleCommandSender} or {@link Player}
     * @param pluginName
     *            The name of the plugin
     * @param message
     *            The message to be send
     */
    public static void writeError(CommandSender reciever, String pluginName, String message) {
        if (reciever instanceof Player)
            PlayerUtils.sendError((Player) reciever, pluginName, message);
        else if (reciever instanceof ConsoleCommandSender)
            ConsoleUtils.printError(pluginName, message);
        else
            throwException(reciever);
    }

    /**
     * Sending an error to the reciever. <br>
     * When <code>reciever</code> is a {@link Player}, it will use
     * {@link PlayerUtils#sendError(Player, String) PlayerUtils.sendError} <br>
     * When <code>reciever</code> is a {@link ConsoleCommandSender}, it will use
     * {@link ConsoleUtils#printError(String) ConsoleUtils.printError} <br>
     * 
     * @param reciever
     *            The reciever of the message. Must be an instance of
     *            {@link ConsoleCommandSender} or {@link Player}
     * @param message
     *            The message to be send
     */
    public static void writeError(CommandSender reciever, String message) {
        if (reciever instanceof Player)
            PlayerUtils.sendError((Player) reciever, message);
        else if (reciever instanceof ConsoleCommandSender)
            ConsoleUtils.printError(message);
        else
            throwException(reciever);
    }

    /**
     * Sending an blank message to the reciever. <br>
     * When <code>reciever</code> is a {@link Player}, it will use
     * {@link PlayerUtils#sendBlankMessage(Player, String, String)
     * PlayerUtils.sendBlankMessage} <br>
     * When <code>reciever</code> is a {@link ConsoleCommandSender}, it will use
     * {@link ConsoleUtils#printInfo(String, String) ConsoleUtils.printInfo} <br>
     * 
     * @param reciever
     *            The reciever of the message. Must be an instance of
     *            {@link ConsoleCommandSender} or {@link Player}
     * @param pluginName
     *            The name of the plugin
     * @param message
     *            The message to be send
     */
    public static void writeMessage(CommandSender reciever, String pluginName, String message) {
        if (reciever instanceof Player)
            PlayerUtils.sendBlankMessage((Player) reciever, pluginName, message);
        else if (reciever instanceof ConsoleCommandSender)
            ConsoleUtils.printInfo(pluginName, message);
        else
            throwException(reciever);
    }

    /**
     * Sending an blank message to the reciever. <br>
     * When <code>reciever</code> is a {@link Player}, it will use
     * {@link PlayerUtils#sendBlankMessage(Player, String)
     * PlayerUtils.sendBlankMessage} <br>
     * When <code>reciever</code> is a {@link ConsoleCommandSender}, it will use
     * {@link ConsoleUtils#printInfo(String) ConsoleUtils.printInfo} <br>
     * 
     * @param reciever
     *            The reciever of the message. Must be an instance of
     *            {@link ConsoleCommandSender} or {@link Player}
     * @param message
     *            The message to be send
     */
    public static void writeMessage(CommandSender reciever, String message) {
        if (reciever instanceof Player)
            PlayerUtils.sendBlankMessage((Player) reciever, message);
        else if (reciever instanceof ConsoleCommandSender)
            ConsoleUtils.printInfo(message);
        else
            throwException(reciever);
    }

    /**
     * Sending an colored message to the reciever.<br>
     * When <code>reciever</code> is a {@link Player}, it will use
     * {@link PlayerUtils#sendMessage(Player, ChatColor, String, String)
     * PlayerUtils.sendMessage} <br>
     * When <code>reciever</code> is a {@link ConsoleCommandSender}, it will use
     * {@link ConsoleUtils#printInfo(String, String) ConsoleUtils.printInfo}.
     * 
     * @param reciever
     *            The reciever of the message. Must be an instance of
     *            {@link ConsoleCommandSender} or {@link Player}
     * @param pluginName
     *            The name of the plugin
     * @param message
     *            The message to be send
     */
    public static void writeColoredMessage(CommandSender reciever, String pluginName, ChatColor color, String message) {
        if (reciever instanceof Player)
            PlayerUtils.sendMessage((Player) reciever, color, pluginName, message);
        else if (reciever instanceof ConsoleCommandSender)
            ConsoleUtils.printInfo(pluginName, message);
        else
            throwException(reciever);
    }

    /**
     * Sending an colored message to the reciever.<br>
     * When <code>reciever</code> is a {@link Player}, it will use
     * {@link PlayerUtils#sendMessage(Player, ChatColor, String)
     * PlayerUtils.sendMessage} <br>
     * When <code>reciever</code> is a {@link ConsoleCommandSender}, it will use
     * {@link ConsoleUtils#printInfo(String) ConsoleUtils.printInfo}.
     * 
     * @param reciever
     *            The reciever of the message. Must be an instance of
     *            {@link ConsoleCommandSender} or {@link Player}
     * @param message
     *            The message to be send
     */
    public static void writeColoredMessage(CommandSender reciever, ChatColor color, String message) {
        if (reciever instanceof Player)
            PlayerUtils.sendMessage((Player) reciever, color, message);
        else if (reciever instanceof ConsoleCommandSender)
            ConsoleUtils.printInfo(message);
        else
            throwException(reciever);
    }

    /**
     * Throws an exception when the sender is no instance of
     * ConsoleCommandSender nor Player
     * 
     * @param sender
     *            The false class
     * @throws RuntimeException
     *             The runtime Exception
     */
    private static void throwException(CommandSender sender) throws RuntimeException {
        throw new RuntimeException("Senders's Class '" + sender.getClass().getName() + "' isn't supported! Use Player or ConsoleCommandSender");
    }
}
