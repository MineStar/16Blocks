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

import java.util.Iterator;
import java.util.TimerTask;

import org.bukkit.entity.Player;

import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Listener.ChatListener;
import de.minestar.sixteenblocks.Manager.ChannelManager;

public class ChatThread extends TimerTask {

    private ChatListener chatListener;
    private ChannelManager channelManager;

    public ChatThread(ChatListener chatListener, ChannelManager channelManager) {
        this.chatListener = chatListener;
        this.channelManager = channelManager;
    }

    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();
        Iterator<Player> iterator = this.channelManager.getChannelByChannelName("Support").getPlayers().iterator();
        Player thisPlayer;
        while (iterator.hasNext()) {
            thisPlayer = iterator.next();
            if (!chatListener.hasWritten(thisPlayer, currentTime)) {
                this.channelManager.updatePlayer(thisPlayer, this.channelManager.getChannelByChannelName("Lobby"));
                TextUtils.sendSuccess(thisPlayer, "You are now in the lobby.");
            }
        }
    }
}
