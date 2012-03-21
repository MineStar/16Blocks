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

package de.minestar.sixteenblocks.Manager;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.config.MinestarConfig;
import de.minestar.minestarlibrary.database.AbstractDatabaseHandler;
import de.minestar.minestarlibrary.database.DatabaseConnection;
import de.minestar.minestarlibrary.database.DatabaseType;
import de.minestar.minestarlibrary.database.DatabaseUtils;
import de.minestar.minestarlibrary.utils.ConsoleUtils;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Mail.Ticket;

public class TicketDatabaseManager extends AbstractDatabaseHandler {

    public TicketDatabaseManager(String pluginName, File dataFolder) {
        super(pluginName, dataFolder);
    }

    @Override
    protected DatabaseConnection createConnection(String pluginName, File dataFolder) throws Exception {
        File configFile = new File(dataFolder, "ticket_sqlconfig.yml");
        // load configuration from config file
        if (configFile.exists())
            return new DatabaseConnection(pluginName, DatabaseType.MySQL, new MinestarConfig(configFile));
        else {
            // create default database config
            DatabaseUtils.createDatabaseConfig(DatabaseType.MySQL, configFile, pluginName);
            return null;
        }
    }

    @Override
    protected void createStructure(String pluginName, Connection con) throws Exception {
        // Nothing to do here
    }

    @Override
    protected void createStatements(String pluginName, Connection con) throws Exception {
        // Nothing to do here
    }

    public Map<String, List<Ticket>> checkTickets(Player[] onlinePlayer) {
        try {
            ResultSet rs = dbConnection.getConnection().createStatement().executeQuery(createTicketQuery(onlinePlayer));

            Map<String, List<Ticket>> tickets = new HashMap<String, List<Ticket>>(onlinePlayer.length);
            List<Ticket> list = null;
            while (rs.next()) {
                String userName = rs.getString(1);
                list = tickets.get(userName);
                if (list == null)
                    list = new LinkedList<Ticket>();
                list.add(new Ticket(rs.getBoolean(2), rs.getBoolean(3), rs.getInt(4)));
                tickets.put(userName, list);
            }

            return tickets;
        } catch (Exception e) {
            ConsoleUtils.printException(e, Core.NAME, "Can't check tickets");
            return null;
        }
    }
    private String createTicketQuery(Player[] onlinePlayer) {
        StringBuilder sBuilder = new StringBuilder("SELECT SUBSTRING_INDEX(`ost_ticket`.`subject`,'SERVERREPORT - ',-1), `ost_ticket`.`isanswered`, `ost_ticket`.`status` = 'closed' , `ost_ticket`.`ticket_id` FROM `ost_ticket` WHERE SUBSTRING_INDEX(`ost_ticket`.`subject`,'SERVERREPORT - ',-1) IN (");
        int i = 0;
        for (; i < onlinePlayer.length - 1; ++i) {
            sBuilder.append('\'');
            sBuilder.append(onlinePlayer[i].getName());
            sBuilder.append('\'');
            sBuilder.append(',');
        }
        sBuilder.append('\'');
        sBuilder.append(onlinePlayer[i].getName());
        sBuilder.append('\'');
        sBuilder.append(");");
        return sBuilder.toString();
    }
}
