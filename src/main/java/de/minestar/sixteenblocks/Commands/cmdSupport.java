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

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Manager.ChannelManager;
import de.minestar.sixteenblocks.Units.Channel;

public class cmdSupport extends AbstractCommand {

    private ChannelManager channelManager;

    public cmdSupport(String syntax, String arguments, String node, ChannelManager channelManager) {
        super(Core.NAME, syntax, arguments, node);
        this.channelManager = channelManager;
    }

    @Override
    public void execute(String[] args, Player player) {
        if (this.channelManager.getChannelOfPlayer(player).getChannelName().equalsIgnoreCase("Lobby")) {
            Channel ch = channelManager.getChannelByChannelName("Support");

            if (Core.isSupporter(player) && !Core.isVip(player)) {
                // DISPLAY CHANNEL MEMBER THAT A SUPPORTER HAS JOINED
                for (Player thisPlayer : ch.getPlayers())
                    TextUtils.sendMessage(thisPlayer, ChatColor.RED, "Supporter " + player.getName() + " has joined the channel");
            } else {
                // COUNT CURRENT CONNECTED SUPPORTER
                int count = 0;
                for (Player thisPlayer : ch.getPlayers()) {
                    if (Core.isSupporter(thisPlayer))
                        ++count;
                    if (count == 0)
                        TextUtils.sendMessage(player, ChatColor.RED, "There are not any supporter at the moment in this channel");
                    else
                        TextUtils.sendInfo(player, "There are " + count + " supporter in this channel");
                }
            }

            this.channelManager.updatePlayer(player, ch);
            TextUtils.sendSuccess(player, "You are now in the support channel.");
        } else {
            this.channelManager.updatePlayer(player, this.channelManager.getChannelByChannelName("Lobby"));
            TextUtils.sendSuccess(player, "You are now in the lobby.");
        }
    }
}
