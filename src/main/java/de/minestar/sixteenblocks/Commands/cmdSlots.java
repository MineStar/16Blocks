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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.ConsoleUtils;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;

public class cmdSlots extends AbstractCommand {

    public cmdSlots(String syntax, String arguments, String node) {
        super(Core.NAME, syntax, arguments, node);
    }

    @Override
    public void execute(String[] args, Player player) {

        // CHECK: PLAYER IS OP OR SUPPORTER
        if (!Core.isSupporter(player)) {
            TextUtils.sendError(player, "You are not allowed to do this!");
            return;
        }

        try {
            Integer maxPlayer = Integer.valueOf(args[0]);
            CraftServer server = (CraftServer) Bukkit.getServer();
            server.getHandle().maxPlayers = maxPlayer;

            TextUtils.sendSuccess(player, "MaxPlayers set to " + maxPlayer + " !");

            // Store new maxPlayer in server.properties
            Properties p = new Properties();
            BufferedReader bReader = new BufferedReader(new FileReader("server.properties"));
            p.load(bReader);
            bReader.close();
            p.setProperty("max-players", maxPlayer.toString());
            BufferedWriter bWriter = new BufferedWriter(new FileWriter("server.properties"));
            p.store(bWriter, "");
            bWriter.close();
        } catch (IOException IOE) {
            ConsoleUtils.printException(IOE, "Can't update server.properties!");
        } catch (NumberFormatException e) {
            TextUtils.sendError(player, args[0] + " is not a number!");
        }
    }
}
