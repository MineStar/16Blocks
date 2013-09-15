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

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import de.minestar.minestarlibrary.utils.ChatUtils;

/**
 * Class to support commands with sub commands
 * 
 * @author Meldanor, GeMoschen
 * 
 */
public abstract class AbstractSuperCommand extends AbstractCommand {

    // Registered subcommands
    private AbstractCommand[] subCommands;
    // Has this SuperCommand a function or does it only hold the sub commands
    private boolean hasFunction;

    /**
     * A super command holds many subcommands. <br>
     * <code>HINT!</code> The subcommands' syntax aren't allowed to start with a
     * '/' !
     * 
     * @param syntax
     *            The label of the command. Example : /who
     * @param arguments
     *            The arguments covered with Tags < >. Additional arguments can
     *            be covered by [ ]
     * @param node
     *            The permission needed to call this command. Leave it empty to
     *            allow this command to everybody
     * @param hasFunction
     *            Set this to true when SuperCommand has another function
     *            instead of only holding sub commands
     * @param subCommands
     *            A list of sub commands, can also be SuperCommands
     */
    public AbstractSuperCommand(String syntax, String arguments, String node, boolean hasFunction, AbstractCommand... subCommands) {
        super(syntax, arguments, node);
        this.hasFunction = hasFunction;
        for (AbstractCommand com : subCommands) {
            if (com.getSyntax().equalsIgnoreCase("help")) {
                throw new RuntimeException("There can't be subcommand labeled 'help'! It is a key  word!");
            }
        }
        this.subCommands = subCommands;
    }

    /**
     * A super command holds many subcommands. <br>
     * <code>HINT!</code> The subcommands' syntax aren't allowed to start with a
     * '/' !
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
     * @param hasFunction
     *            Set this to true when SuperCommand has another function
     *            instead of only holding sub commands
     * @param subCommands
     *            A list of sub commands, can also be SuperCommands
     */
    public AbstractSuperCommand(String pluginName, String syntax, String arguments, String node, boolean hasFunction, AbstractCommand... subCommands) {
        this(syntax, arguments, node, hasFunction, subCommands);
        this.pluginName = pluginName;
    }

    /**
     * A super command holds many subcommands. <br>
     * <code>HINT!</code> The subcommands' syntax aren't allowed to start with a
     * '/' ! <br>
     * This constructs a SuperCommand which only holds subCommands, but have no
     * function!
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
     * @param subCommands
     *            A list of sub commands, can also be SuperCommands
     */
    public AbstractSuperCommand(String pluginName, String syntax, String arguments, String node, AbstractCommand... subCommands) {
        this(pluginName, syntax, arguments, node, false, subCommands);
    }

    /**
     * A super command holds many subcommands. <br>
     * <code>HINT!</code> The subcommands' syntax aren't allowed to start with a
     * '/' ! <br>
     * This constructs a SuperCommand which only holds subCommands, but have no
     * function!
     * 
     * @param syntax
     *            The label of the command. Example : /who
     * @param arguments
     *            The arguments covered with Tags < >. Additional arguments can
     *            be covered by [ ]
     * @param node
     *            The permission needed to call this command. Leave it empty to
     *            allow this command to everybody
     * @param subCommands
     *            A list of sub commands, can also be SuperCommands
     */
    public AbstractSuperCommand(String syntax, String arguments, String node, AbstractCommand... subCommands) {
        this(syntax, arguments, node, false, subCommands);
    }

    public void run(String[] args, CommandSender sender) {

        if (!hasFunction && args.length == 0)
            printSubcommands(sender);

        // the key word "help" is served
        if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
            if (hasFunction)
                ChatUtils.writeInfo(sender, pluginName, getHelpMessage());
            printSubcommands(sender);
        }

        // When no sub command was found then call the run method
        if (!runSubCommand(args, sender))
            super.run(args, sender);
    }

    @Override
    protected boolean hasCorrectSyntax(String[] args) {
        return args.length >= getArgumentCount();
    }

    /**
     * Write all holded sub commands with help message
     * 
     * @param sender
     *            The reciever of the list
     */
    private void printSubcommands(CommandSender sender) {
        ChatUtils.writeColoredMessage(sender, pluginName, ChatColor.GOLD, "Possible subcommands:");
        for (AbstractCommand command : getSubCommands())
            ChatUtils.writeInfo(sender, pluginName, command.getHelpMessage());
    }

    /**
     * Searches for sub commands by comparing the first argument with the syntax
     * of the sub commands.
     * 
     * @param args
     *            The arguments that may contains the syntax
     * @param sender
     *            The command caller
     * @return True when a sub command is found, false if not
     */
    protected boolean runSubCommand(String[] args, CommandSender sender) {
        if (args != null && args.length == 0)
            return false;

        for (AbstractCommand com : subCommands) {
            if (com.getSyntax().equalsIgnoreCase(args[0])) {
                com.run(Arrays.copyOfRange(args, 1, args.length), sender);
                return true;
            }
        }
        return false;
    }

    /**
     * @return An array of all sub commands holded by this super command
     */
    protected AbstractCommand[] getSubCommands() {
        return subCommands;
    }
}
