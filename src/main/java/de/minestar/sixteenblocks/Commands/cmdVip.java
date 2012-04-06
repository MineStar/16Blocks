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

import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.ChatUtils;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;

public class cmdVip extends AbstractCommand {

    public cmdVip(String syntax, String arguments, String node) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Toggle a supporter";
    }

    @Override
    public void execute(String[] args, Player player) {

        // CHECK: PLAYER IS OP OR SUPPORTER
        if (!player.isOp()) {
            TextUtils.sendError(player, "You are not allowed to do this!");
            return;
        }

        addVip(args, player);
    }

    @Override
    public void execute(String[] args, ConsoleCommandSender console) {

        addVip(args, console);
    }

    private void addVip(String[] args, CommandSender sender) {
        boolean result = Core.getInstance().toggleVip(args[0]);
        if (result)
            ChatUtils.writeSuccess(sender, "'" + args[0] + "' is now a vip!");
        else
            ChatUtils.writeSuccess(sender, "'" + args[0] + "' is no longer a vip!");

    }

}
