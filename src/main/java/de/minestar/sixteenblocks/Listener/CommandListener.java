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

package de.minestar.sixteenblocks.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class CommandListener implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (!event.getPlayer().isOp())
            return;

        // ON RELOAD > TUNNEL THE COMMAND
        if (event.getMessage().toLowerCase().startsWith("/reload")) {
            event.setMessage("/rel");
        } else if (event.getMessage().toLowerCase().startsWith("/stop")) {
            event.setMessage("/shutdown");
        }
    }

    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
        // ON RELOAD > TUNNEL THE COMMAND
        if (event.getCommand().toLowerCase().startsWith("reload")) {
            event.setCommand("rel");
        } else if (event.getCommand().toLowerCase().startsWith("stop")) {
            event.setCommand("shutdown");
        }
    }

}
