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

import de.minestar.minestarlibrary.commands.AbstractExtendedCommand;
import de.minestar.minestarlibrary.utils.ChatUtils;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Mail.MailHandler;

public class cmdTicket extends AbstractExtendedCommand {

    private MailHandler mHandler;

    public cmdTicket(String syntax, String arguments, String node, MailHandler mHandler) {
        super(Core.NAME, syntax, arguments, node);
        this.mHandler = mHandler;
    }

    @Override
    public void execute(String[] args, Player player) {
        String message = ChatUtils.getMessage(args);
        if (mHandler.sendTicket(player, message)) {
            TextUtils.sendSuccess(player, "Ticket successfully created.");
            TextUtils.sendInfo(player, "An admin will contact you as soon as possible.");
        } else
            TextUtils.sendError(player, "Can't create a ticket! Please try to contact an admin as soon as possible!");
    }

}
