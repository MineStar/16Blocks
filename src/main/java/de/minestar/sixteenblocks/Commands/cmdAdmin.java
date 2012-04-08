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
import de.minestar.sixteenblocks.Manager.ChannelManager;

public class cmdAdmin extends AbstractCommand {

    private ChannelManager cManager;

    public cmdAdmin(String syntax, String arguments, String node, ChannelManager cManager) {
        super(Core.NAME, syntax, arguments, node);
        this.cManager = cManager;

        this.description = "Show current connected supporter";
    }

    @Override
    public void execute(String[] args, Player player) {

        // CHECK: PLAYER IS OP OR SUPPORTER
        if (!Core.isSupporter(player)) {
            TextUtils.sendError(player, "You are not allowed to do this!");
            return;
        }

        StringBuilder supporter = new StringBuilder(256);
        StringBuilder vips = new StringBuilder(256);
        Player[] onlinePlayer = Bukkit.getOnlinePlayers();

        // CREATE LISTS
        for (Player online : onlinePlayer) {
            if (Core.isSupporter(online) && !Core.isVip(online)) {
                supporter.append(online.getName());
                appendChannel(online, supporter);
                supporter.append(", ");
            } else if (Core.isVip(online)) {
                vips.append(online.getName());
                appendChannel(online, vips);
                vips.append(", ");
            }

        }

        // DISPLAY LISTS
        if (supporter.length() > 2) {
            supporter.delete(supporter.length() - 2, supporter.length());
            TextUtils.sendSuccess(player, "Online Supporter:");
            TextUtils.sendInfo(player, supporter.toString());
        } else
            TextUtils.sendError(player, "No supporter online");

        if (vips.length() > 2) {
            vips.delete(supporter.length() - 2, vips.length());
            TextUtils.sendSuccess(player, "Online VIPs:");
            TextUtils.sendInfo(player, vips.toString());
        } else
            TextUtils.sendError(player, "No VIP online");
    }

    private void appendChannel(Player online, StringBuilder sBuilder) {
        sBuilder.append('(');
        sBuilder.append(cManager.getChannelOfPlayer(online).getChannelName());
        sBuilder.append(')');
    }
}
