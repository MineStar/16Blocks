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
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;

public class cmdAdmin extends AbstractCommand {

    public cmdAdmin(String syntax, String arguments, String node) {
        super(Core.NAME, syntax, arguments, node);

        this.description = "Show current connected supporter";
    }

    @Override
    public void execute(String[] args, Player player) {

        // CHECK: PLAYER IS OP OR SUPPORTER
        if (!Core.isSupporter(player)) {
            TextUtils.sendError(player, "You are not allowed to do this!");
            return;
        }

        TextUtils.sendSuccess(player, "Current connected supporter:");
        StringBuilder sBuilder = new StringBuilder(256);
        Player[] onlinePlayer = Bukkit.getOnlinePlayers();
        for (Player online : onlinePlayer) {
            if (Core.isSupporter(online)) {
                sBuilder.append(online.getName());
                sBuilder.append(", ");
            }

        }
        if (sBuilder.length() > 2)
            sBuilder.delete(sBuilder.length() - 2, sBuilder.length());
        TextUtils.sendInfo(player, sBuilder.toString());
    }
}
