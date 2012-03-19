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

package de.minestar.sixteenblocks.Threads;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Mail.Ticket;
import de.minestar.sixteenblocks.Manager.TicketDatabaseManager;

public class CheckTicketThread implements Runnable {

    private TicketDatabaseManager dbManager;

    public CheckTicketThread(TicketDatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public void run() {
        checkTicket();
    }

    private void checkTicket() {

        Player[] onlinePlayer = Bukkit.getOnlinePlayers();
        // No player online
        if (onlinePlayer.length == 0)
            return;

        Map<String, Ticket> tickets = dbManager.checkTickets(onlinePlayer);
        Ticket ticket = null;
        for (Player player : onlinePlayer) {
            ticket = tickets.get(player.getName());
            if (ticket != null && (ticket.isAnswered() || ticket.isClosed()))
                TextUtils.sendInfo(player, "Your ticket is processed. Thank you for your help. For questions please contact the YouAreMinecraft team.");
        }
    }
}
