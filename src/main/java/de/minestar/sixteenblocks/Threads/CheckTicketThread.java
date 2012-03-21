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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.utils.ConsoleUtils;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Mail.Ticket;
import de.minestar.sixteenblocks.Manager.TicketDatabaseManager;

public class CheckTicketThread implements Runnable {

    private File checkedTicketsFile;
    private Set<Integer> checkedTickets = new HashSet<Integer>(500);

    private TicketDatabaseManager dbManager;

    public CheckTicketThread(TicketDatabaseManager dbManager, File dataFolder) {
        this.dbManager = dbManager;
        loadCheckTickets(dataFolder);
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

        Map<String, List<Ticket>> tickets = dbManager.checkTickets(onlinePlayer);
        List<Ticket> list;
        for (Player player : onlinePlayer) {
            list = tickets.get(tickets);
            if (list != null) {
                for (Ticket ticket : list) {
                    // only answered or closed tickets are shown and only once
                    if ((ticket.isAnswered() || ticket.isClosed()) && !checkedTickets.contains(ticket.getTickedId())) {
                        TextUtils.sendInfo(player, "Your ticket is processed. Thank you for your help. For questions please contact the YouAreMinecraft team.");
                        // prevent double shown tickets
                        checkedTickets.add(ticket.getTickedId());
                    }
                }
            }
        }
    }

    private void loadCheckTickets(File dataFolder) {
        checkedTicketsFile = new File(dataFolder, "usedTickets.txt");
        if (!checkedTicketsFile.exists())
            return;

        try {
            BufferedReader bReader = new BufferedReader(new FileReader(checkedTicketsFile));
            String line = "";
            while ((line = bReader.readLine()) != null)
                if (!line.isEmpty())
                    checkedTickets.add(Integer.valueOf(line));
            bReader.close();
        } catch (Exception e) {
            ConsoleUtils.printException(e, Core.NAME, "Can't load used ticket names!");
        }

    }

    public void saveCheckTickets() {
        if (checkedTickets.isEmpty())
            return;

        try {
            BufferedWriter bWriter = new BufferedWriter(new FileWriter(checkedTicketsFile));
            for (Integer i : checkedTickets) {
                bWriter.write(i.toString());
                bWriter.newLine();
            }
            bWriter.close();
        } catch (Exception e) {
            ConsoleUtils.printException(e, Core.NAME, "Can't save used ticket names!");
        }
    }
}
