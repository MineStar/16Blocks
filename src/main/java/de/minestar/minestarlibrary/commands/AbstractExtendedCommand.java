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

/**
 * Represents a command with a varible number of Arguments. The only difference
 * to Command is, that the argumentcount given by the constructor is the
 * argumentcount the command must have atleast!
 * 
 * @author Meldanor, GeMoschen
 * 
 */
public abstract class AbstractExtendedCommand extends AbstractCommand {

    /**
     * Creating an ExtendedCommand which can have at least arguments than given
     * in the paramater
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
    public AbstractExtendedCommand(String syntax, String arguments, String node) {
        super(syntax, arguments, node);
    }

    /**
     * Creating an ExtendedCommand which can have at least arguments than given
     * in the paramater
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
    public AbstractExtendedCommand(String pluginName, String syntax, String arguments, String node) {
        super(pluginName, syntax, arguments, node);
    }

    /**
     * Allows at least the count of arguments given in constructor
     * 
     * @param args
     *            The arguments
     */
    @Override
    protected boolean hasCorrectSyntax(String[] args) {
        return args.length >= super.getArgumentCount();
    }
}
