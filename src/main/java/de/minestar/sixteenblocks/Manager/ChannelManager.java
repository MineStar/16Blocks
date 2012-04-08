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

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Units.Channel;

public class ChannelManager {

    private Set<Channel> channelList = new HashSet<Channel>(32);
    private ConcurrentHashMap<Player, Channel> playerList = new ConcurrentHashMap<Player, Channel>(512);

    public ChannelManager() {
        this.channelList.add(new Channel("Lobby", true));
        this.channelList.add(new Channel("Support", true));
        this.channelList.add(new Channel("Hidden", false));
        this.channelList.add(new Channel("MutedPlayers", false));

        this.startUp();
    }

    /**
     * PUT ALL USERS INTO THE LOBBY
     */
    private void startUp() {
        // ON STARTUP: PUT ALL PLAYERS INTO THE LOBBY
        Channel lobby = this.getChannelByChannelName("Lobby");
        Player[] pList = Bukkit.getOnlinePlayers();
        for (Player player : pList) {
            TextUtils.sendSuccess(player, "You are now in the lobby.");
            this.updatePlayer(player, lobby);
        }
    }

    /**
     * Get a channel by its name
     * 
     * @param channelName
     * @return the channel, of NULL if there is none
     */
    public Channel getChannelByChannelName(String channelName) {
        channelName = channelName.toLowerCase();
        for (Channel channel : this.channelList) {
            if (channelName.equalsIgnoreCase(channel.getChannelName()))
                return channel;
        }
        return null;
    }

    /**
     * Handle a chatevent
     * 
     * @param player
     * @param message
     * @return true, if the player is in a channel. Otherwise false.
     */
    public boolean handleChat(Player player, String message) {
        Channel channel = this.getChannelOfPlayer(player);
        if (channel != null) {
            channel.sendMessage(player, message);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Update the channel of a player
     * 
     * @param player
     * @param newChannel
     * @return the new channel of the player
     */
    public Channel updatePlayer(Player player, Channel newChannel) {
        Channel oldChannel = this.playerList.get(player);
        if (oldChannel != null) {
            oldChannel.updatePlayer(player, false);
        }
        if (newChannel != null) {
            this.playerList.put(player, newChannel);
            newChannel.updatePlayer(player, true);
        } else {
            this.playerList.remove(player);
        }
        return newChannel;
    }

    public void updatePlayer(Player player) {
        this.updatePlayer(player, this.getChannelOfPlayer(player));
    }

    /**
     * Remove a player from the channel
     * 
     * @param player
     */
    public void removePlayerFromChannel(Player player) {
        this.playerList.remove(player);
    }

    public boolean containsPlayer(Player player) {
        return this.playerList.containsKey(player);
    }

    public Channel getChannelOfPlayer(Player player) {
        return this.playerList.get(player);
    }
}
