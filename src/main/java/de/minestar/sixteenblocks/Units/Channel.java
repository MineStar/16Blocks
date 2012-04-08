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

package de.minestar.sixteenblocks.Units;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

public class Channel {

    private final String channelName;
    private final boolean sendMessage;
    private Set<Player> playerList = new HashSet<Player>(256);

    public Channel(String channelName, boolean sendMessage) {
        this.channelName = channelName;
        this.sendMessage = sendMessage;
    }

    public String getChannelName() {
        return this.channelName;
    }

    /**
     * Updates a player
     * 
     * @param player
     * @param add
     * @return true, if the player is in the channel. Otherwise false.
     */
    public boolean updatePlayer(Player player, boolean add) {
        if (add)
            this.playerList.add(player);
        else
            this.playerList.remove(player);

        return this.containsPlayer(player);
    }

    /**
     * Check if a player is in this channel
     * 
     * @param player
     * @return true, if the player is in the channel. Otherwise false.
     */
    public boolean containsPlayer(Player player) {
        return this.playerList.contains(player);
    }

    /**
     * Send a message to everyone in the channel
     * 
     * @param player
     * @param message
     */
    public void sendMessage(String message) {
        if (!this.sendMessage)
            return;

        for (Player thisPlayer : this.playerList) {
            thisPlayer.sendMessage(message);
        }
    }
}
