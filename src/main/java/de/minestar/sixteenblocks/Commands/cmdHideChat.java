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

import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Manager.ChannelManager;

public class cmdHideChat extends AbstractCommand {

    private ChannelManager channelManager;

    public cmdHideChat(String syntax, String arguments, String node, ChannelManager channelManager) {
        super(Core.NAME, syntax, arguments, node);

        this.channelManager = channelManager;
    }

    @Override
    public void execute(String[] args, Player player) {
        this.channelManager.updatePlayer(player, this.channelManager.getChannelByChannelName("Hidden"));
        TextUtils.sendSuccess(player, "Your chat is now hidden");
    }
}
