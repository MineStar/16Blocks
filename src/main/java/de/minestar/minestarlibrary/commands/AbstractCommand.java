/*
 * Copyright (C) 2011 MineStar.de 
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

package de.minestar.minestarlibrary.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

//import com.bukkit.gemo.utils.UtilPermissions;

import de.minestar.minestarlibrary.utils.ChatUtils;
import de.minestar.minestarlibrary.utils.ConsoleUtils;
import de.minestar.minestarlibrary.utils.PlayerUtils;

/**
 * Represents a command with a fixed number of arguments and no subcommands. If
 * you want a command with at least number of arguments, use ExtendedCommand. If
 * you want a command which has a function/or not AND subcommands, use
 * SuperCommand.
 * 
 * You have to register the command by creating a command list
 * 
 * @author Meldanor, GeMoschen
 * 
 */
public abstract class AbstractCommand {

    public final static String NO_RIGHT = "You are not allowed to use this command!";

    // Add this in every command to add an description
    protected String description = "";
    // Example : /warp create
    private String syntax;
    // Example : <Name>
    private String arguments;
    // Example : contao.create
    protected String permissionNode;
    // Example: "[Contao]";
    protected String pluginName = "";
    // Number of opening < Tags
    private final int argumentCount;

    /**
     * A command with a fixed number of arguments
     * 
     * @param syntax
     *            The label of the command. Example : /who
     * @param arguments
     *            The arguments covered with Tags < >. Additional arguments can
     *            be covered by [ ]
     * @param node
     *            The permission needed to call this command. Leave it empty to
     *            allow this command to everybody
     */
    public AbstractCommand(String syntax, String arguments, String node) {
        this.syntax = syntax;
        this.arguments = arguments;
        this.permissionNode = node;
        this.argumentCount = countArguments();
    }

    /**
     * A command with a fixed number of arguments
     * 
     * @param pluginName
     *            Name of the plugin, without brackets [ ]
     * @param syntax
     *            The label of the command. Example : /who
     * @param arguments
     *            The arguments covered with Tags < >. Additional arguments can
     *            be covered by [ ]
     * @param node
     *            The permission needed to call this command. Leave it empty to
     *            allow this command to everybody
     */
    public AbstractCommand(String pluginName, String syntax, String arguments, String node) {
        this(syntax, arguments, node);
        this.pluginName = pluginName;
    }

    /**
     * Call this command to run it functions. It checks at first whether the
     * sender has enough rights to use this. Also it checks whether it uses the
     * correct snytax. If both is correct, the real function of the command is
     * called
     * 
     * @param args
     *            The arguments of this command
     * @param sender
     *            The command caller
     */
    public void run(String[] args, CommandSender sender) {
        if (!hasRights(sender)) {
            PlayerUtils.sendError((Player) sender, pluginName, NO_RIGHT);
            return;
        }

        if (!hasCorrectSyntax(args)) {
            ChatUtils.writeInfo(sender, pluginName, getHelpMessage());
            return;
        }

        if (sender instanceof ConsoleCommandSender)
            execute(args, (ConsoleCommandSender) sender);
        else if (sender instanceof Player)
            execute(args, (Player) sender);
        else
            ConsoleUtils.printError(pluginName, "Unknown command sender '" + sender.getClass().getName() + "'!");
    }

    /**
     * Implement the effect of the command
     * 
     * @param args
     *            The arguments of the command
     * @param player
     *            The command caller
     */
    public abstract void execute(String[] args, Player player);

    /**
     * Override this method to allow a command accessed by console. When this
     * method is not override and console tries to execute the command, the
     * command is canceled and an error message is sent
     * 
     * @param args
     *            The arguments of the command
     * @param console
     *            The minecraft console
     */
    public void execute(String[] args, ConsoleCommandSender console) {
        ConsoleUtils.printError(pluginName, "The command '" + getSyntax() + "' can't be executed by console!");
    }

    /**
     * @param sender
     *            The command caller
     * @return True when the sender has enough rights to use the command Or the
     *         permissionnode is empty, so everybody can use it
     */
    protected boolean hasRights(CommandSender sender) {
        // TODO: Implement your permission system
        if (sender instanceof Player)
            return permissionNode.length() == 0 || ((Player) sender).isOp();
//            return permissionNode.length() == 0 || UtilPermissions.playerCanUseCommand((Player) sender, getPermissionNode());
        else
            return true;
    }

    /**
     * Compares the count of arguments has and the count of arguments the
     * command should have.
     * 
     * @param args
     *            The arguments of the command given by the command caller
     * @return True when both number of arugments are equal
     */
    protected boolean hasCorrectSyntax(String[] args) {
        return args.length == argumentCount;
    }

    /**
     * @return The description of the Command, useful for help
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return Syntax + Arguments + Description
     */
    public String getHelpMessage() {
        return getSyntax() + " " + getArguments() + " : " + getDescription();
    }

    /**
     * @return The syntax (or label) of the Command
     */
    public String getSyntax() {
        return syntax;
    }

/**
     * @return The arguments in one string. Every argument is labeld in '<' and '>'
     */
    public String getArguments() {
        return arguments;
    }

    /**
     * @return The permission node, like contao.create
     */
    public String getPermissionNode() {
        return permissionNode;
    }

    /**
     * @return The number of possible arguments
     */
    public int getArgumentCount() {
        return argumentCount;
    }

/**
     * @return The number of '<' in the argument String
     */
    private int countArguments() {

        if (arguments.isEmpty())
            return 0;

        int counter = 0;
        for (int i = 0; i < arguments.length(); ++i)
            if (arguments.charAt(i) == '<')
                ++counter;
        return counter;
    }

    /**
     * Checks a permission node for the player and when the player doesn't have
     * the permission, the message <code>NO_RIGHT</code> is printed! <br>
     * Use this method for extended commands with multiple permission checks
     * 
     * @param sender
     *            The possible permission owner
     * @param node
     *            The permission node
     * @return <code>True</code> if player has permissions, <code>false</code>
     *         if not!
     */
    protected boolean checkSpecialPermission(CommandSender sender, String node) {
        if (sender instanceof ConsoleCommandSender)
            return true;
        else {
            Player player = (Player) sender;
            // TODO: Implement your permission
            if (!player.isOp()) {
//            if (!UtilPermissions.playerCanUseCommand(player, node)) {
                PlayerUtils.sendError(player, pluginName, NO_RIGHT);
                return false;
            } else
                return true;
        }
    }
}
