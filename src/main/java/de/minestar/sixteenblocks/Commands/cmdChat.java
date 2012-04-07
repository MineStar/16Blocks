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
import de.minestar.sixteenblocks.Listener.ChatListener;

public class cmdChat extends AbstractCommand {

    private ChatListener chatListener;

    public cmdChat(String syntax, String arguments, String node, ChatListener chatListener) {
        super(Core.NAME, syntax, arguments, node);

        this.chatListener = chatListener;
    }

    @Override
    public void execute(String[] args, Player player) {

        chatListener.setHiddenChannel(player, false);
        chatListener.setSupportChannel(player, false);

        TextUtils.sendSuccess(player, "You are now in normal chat mode");
    }

}
