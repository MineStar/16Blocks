/*
 * Copyright (C) 2012 MineStar.de 
 * 
 * This file is part of SixteenBlocks.
 * 
 * SixteenBlocks is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * SixteenBlocks is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SixteenBlocks.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.sixteenblocks.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractExtendedCommand;
import de.minestar.minestarlibrary.utils.ConsoleUtils;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;

public class cmdStop extends AbstractExtendedCommand {

    public cmdStop(String syntax, String arguments, String node) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Stop the server";
    }

    @Override
    public void execute(String[] args, Player player) {
        // CHECK: PLAYER IS OP
        if (!player.isOp()) {
            TextUtils.sendError(player, "You are not allowed to do this!");
            return;
        }

        if (Core.isShutDown) {
            TextUtils.sendError(player, "Server is already marked as 'stop' or 'reloading'...");
            return;
        }

        // SHUTDOWN / RELOAD
        Core.shutdownServer = true;
        Core.isShutDown = true;
        TextUtils.sendInfo(player, "Server is marked as 'stop'...");

        // KICK ALL PLAYERS
        Player[] players = Bukkit.getOnlinePlayers();
        for (Player thisPlayer : players) {
            if (!Core.isSupporter(thisPlayer)) {
                thisPlayer.kickPlayer("YAM-Server is restarting...");
            }
        }
    }

    @Override
    public void execute(String[] args, ConsoleCommandSender console) {

        if (Core.isShutDown) {
            ConsoleUtils.printError("Server is already marked as 'stop' or 'reloading'...");
            return;
        }

        // SHUTDOWN / RELOAD
        Core.shutdownServer = true;
        Core.isShutDown = true;
        ConsoleUtils.printInfo("Server is marked as 'stop'...");
    }
}
