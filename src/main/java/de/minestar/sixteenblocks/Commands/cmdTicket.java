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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractExtendedCommand;
import de.minestar.minestarlibrary.utils.ChatUtils;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Mail.MailHandler;

public class cmdTicket extends AbstractExtendedCommand {

    private MailHandler mHandler;

    private Map<Player, Long> floodMap = new HashMap<Player, Long>();
    private static final long FLOOD_LIMIT = 1000L * 60L * 10L;

    private Set<String> supporter;

    public cmdTicket(String syntax, String arguments, String node, MailHandler mHandler, Set<String> supporter) {
        super(Core.NAME, syntax, arguments, node);
        this.mHandler = mHandler;
        this.supporter = supporter;
    }

    @Override
    public void execute(String[] args, Player player) {
        // CHECK: FLOOD PREVENTION
        long currentTime = System.currentTimeMillis();
        if (!player.isOp() && !supporter.contains(player.getName())) {
            Long old = floodMap.get(player);
            if (old != null && currentTime - old < FLOOD_LIMIT) {
                TextUtils.sendError(player, "You can create a new ticket in " + formatString(currentTime - old));
                return;
            }
        }

        String message = ChatUtils.getMessage(args);
        // SEND TICKET
        if (mHandler.sendTicket(player, message)) {
            TextUtils.sendSuccess(player, "Ticket successfully created.");
            TextUtils.sendInfo(player, "An admin will contact you as soon as possible.");
            floodMap.put(player, currentTime);
        }
        // SOMETHING WEIRED HAPPENED
        else
            TextUtils.sendError(player, "Can't create a ticket! Please try to contact an admin as soon as possible!");
    }

    private String formatString(long diff) {
        // milliseconds in seconds = 1/1000
        long sec = diff / 1000L;
        // seconds in minutes = 1/60
        long min = sec / 60L;
        // get real seconds
        sec = sec % 60;
        return min + " minutes and " + sec + " seconds";
    }
}
