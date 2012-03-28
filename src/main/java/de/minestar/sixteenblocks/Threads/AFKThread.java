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

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.minestar.sixteenblocks.Core.Core;

public class AFKThread implements Runnable {

    private Map<String, Long> afkTimes = new HashMap<String, Long>(Bukkit.getMaxPlayers());

    // 10 Minutes
    private static final long AFK_TIMER = 1000L * 60L * 5L;

    @Override
    public void run() {
        checkPlayer();
    }

    private void checkPlayer() {
        Long currentTime = System.currentTimeMillis();
        Long lastActivity = Long.MIN_VALUE;

        for (Player player : Bukkit.getOnlinePlayers()) {
            // Ignore supporter
            if (Core.isSupporter(player))
                continue;
            lastActivity = afkTimes.get(player.getName());
            // if player was checked before
            if (lastActivity != null) {
                if (currentTime - lastActivity >= AFK_TIMER)
                    player.kickPlayer("Y U NO MOVE?");

            } else {
                afkTimes.put(player.getName(), currentTime);
            }
        }
    }

    public void takeAktion(Player player) {
        // Ignore supporter
        if (!Core.isSupporter(player))
            afkTimes.put(player.getName(), System.currentTimeMillis());
    }

    public void removePlayer(Player player) {
        // Ignore supporter
        if (!Core.isSupporter(player))
            afkTimes.remove(player.getName());
    }
}
