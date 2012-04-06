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

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractExtendedCommand;
import de.minestar.minestarlibrary.utils.ChatUtils;
import de.minestar.minestarlibrary.utils.PlayerUtils;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;

public class cmdKick extends AbstractExtendedCommand {

    public cmdKick(String syntax, String arguments, String node) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Kick a player from the server - Just tunneling";
    }

    @Override
    public void execute(String[] args, Player player) {

        // CHECK: PLAYER IS OP OR SUPPORTER
        if (!Core.isSupporter(player) || Core.isVip(player)) {
            TextUtils.sendError(player, "You are not allowed to do this!");
            return;
        }

        kick(args, player);
    }

    @Override
    public void execute(String[] args, ConsoleCommandSender console) {

        kick(args, console);
    }

    private void kick(String[] args, CommandSender sender) {

        Player target = PlayerUtils.getOnlinePlayer(args[0]);
        if (target == null)
            ChatUtils.writeError(sender, "Player '" + target + "' does not exist or is offline!");
        else {
            target.kickPlayer(getKickMessage(args));
            TextUtils.sendSuccess(target, "Player '" + target + "' was kicked from the server!");
        }
    }

    private String getKickMessage(String[] args) {
        if (args.length == 1)
            return "You was kicked from the server!";
        else
            return ChatUtils.getMessage(args, " ", 1);
    }
}
