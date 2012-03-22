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

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractExtendedCommand;
import de.minestar.minestarlibrary.utils.ChatUtils;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;

public class cmdReply extends AbstractExtendedCommand {

    private Map<Player, Player> recipients;

    public cmdReply(String syntax, String arguments, String node, Map<Player, Player> recipients) {
        super(Core.NAME, syntax, arguments, node);
        this.recipients = recipients;
        this.description = "Answere a players whisper";
    }

    @Override
    public void execute(String[] args, Player player) {
        Player target = recipients.get(player);
        if (target == null || !target.isOnline()) {
            TextUtils.sendError(player, "Player '" + args[0] + "' doesn't exist or is offline!");
            return;
        } else {
            recipients.put(target, player);
            String message = ChatColor.GRAY + ChatUtils.getMessage(args);
            player.sendMessage(ChatColor.GOLD + "[me -> " + target.getName() + "] : " + message);
            target.sendMessage(ChatColor.GOLD + "[" + player.getName() + " -> me] : " + message);
        }
    }

}
