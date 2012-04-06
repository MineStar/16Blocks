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

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractExtendedCommand;
import de.minestar.minestarlibrary.utils.ConsoleUtils;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;

public class cmdReload extends AbstractExtendedCommand {

    public cmdReload(String syntax, String arguments, String node) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Reload the server";
    }

    @Override
    public void execute(String[] args, Player player) {
        // CHECK: PLAYER IS OP
        if (!player.isOp() || Core.isVip(player)) {
            TextUtils.sendError(player, "You are not allowed to do this!");
            return;
        }

        if (Core.isShutDown) {
            TextUtils.sendError(player, "Server is already marked as 'stop' or 'reloading'...");
            return;
        }

        // SHUTDOWN / RELOAD
        Core.shutdownServer = true;
        Core.isShutDown = false;
        TextUtils.sendInfo(player, "Server is marked as 'reloading'...");
    }

    @Override
    public void execute(String[] args, ConsoleCommandSender console) {

        if (Core.isShutDown) {
            ConsoleUtils.printError("Server is already marked as 'stop' or 'reloading'...");
            return;
        }

        // SHUTDOWN / RELOAD
        Core.shutdownServer = true;
        Core.isShutDown = false;
        ConsoleUtils.printInfo("Server is marked as 'reloading'...");
    }
}
