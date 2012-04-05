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

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.Settings;
import de.minestar.sixteenblocks.Threads.AFKThread;

public class LoginListener implements Listener {

    private AFKThread afkThread;

    private File file;

    public LoginListener(AFKThread afkThread) {
        this.afkThread = afkThread;
        this.file = new File("serverfull.txt");
    }

    @EventHandler
    public void onPlayerPreLogin(PlayerPreLoginEvent event) {
        if (Core.isShutDown) {
            event.disallow(Result.KICK_OTHER, "Server is shutting down...");
            return;
        }

        int onlinePlayer = Bukkit.getOnlinePlayers().length;
        // Only supporter can join a full server
        if (!Core.isSupporter(event.getName()) && onlinePlayer >= Core.getAllowedMaxPlayer()) {
            event.disallow(Result.KICK_FULL, "Server is full");
        }
    }

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event) {
        this.checkServerFull();
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        event.setMaxPlayers(Bukkit.getMaxPlayers() - Settings.getSupporterBuffer());
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        afkThread.removePlayer(event.getPlayer());

        this.checkServerFull();
    }

    private void checkServerFull() {
        if (Bukkit.getOnlinePlayers().length >= Bukkit.getMaxPlayers() - Settings.getSupporterBuffer()) {
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception e) {

                }
            }
        } else {
            if (file.exists()) {
                try {
                    file.delete();
                } catch (Exception e) {

                }
            }
        }
    }
}
