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

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import de.minestar.sixteenblocks.Manager.AreaManager;

public class BroadcastRunningThread implements Runnable {

    private Random rand = new Random();
    private AreaManager aManager;
    private String message;

    public BroadcastRunningThread(AreaManager aManager, String message) {
        this.message = message;
        this.aManager = aManager;
    }

    @Override
    public void run() {
        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "[YAM] :" + message);
        // Inform player about current skin count
        if (rand.nextBoolean())
            Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "[YAM] :" + aManager.getUsedAreaCount() + " Skins at the moment!");
    }
}
