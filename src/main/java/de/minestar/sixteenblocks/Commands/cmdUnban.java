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

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;

public class cmdUnban extends AbstractCommand {

    private Set<String> supporter;

    public cmdUnban(String syntax, String arguments, String node, Set<String> supporter) {

        super(Core.NAME, syntax, arguments, node);
        this.supporter = supporter;
        this.description = "Unbann a player - Just tunneling";
    }
    @Override
    public void execute(String[] args, Player player) {

        // CHECK: PLAYER IS OP OR SUPPORTER
        if (!player.isOp() && !supporter.contains(player.getName().toLowerCase())) {
            TextUtils.sendError(player, "You are not allowed to do this!");
            return;
        }

        String playerName = PlayerUtils.getOfflinePlayerName(args[0]);
        if (playerName == null)
            TextUtils.sendInfo(player, "Player '" + args[0] + "' doesn't exist(was never on the server)!");
        else {
            // Unbann
            Bukkit.getOfflinePlayer(playerName).setBanned(false);
            TextUtils.sendSuccess(player, "Player '" + args[0] + "' unbanned!");
        }
    }
}
