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

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.minestar.minestarlibrary.commands.AbstractExtendedCommand;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;

public class cmdGive extends AbstractExtendedCommand {

    public cmdGive(String syntax, String arguments, String node) {
        super(Core.NAME, syntax, arguments, node);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void execute(String[] args, Player player) {

        // CHECK: PLAYER IS OP OR SUPPORTER
        if (!Core.isSupporter(player)) {
            TextUtils.sendError(player, "You are not allowed to do this!");
            return;
        }

        // SEARCH PLAYER
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            TextUtils.sendError(player, "Player '" + args[0] + "' not found (maybe he is offline?)");
            return;
        }

        // SEARCH ID & DATA
        int ID = cmdGive.getID(args[1]);
        short SubID = cmdGive.getData(args[1]);
        if (ID == -1) {
            TextUtils.sendError(player, "Item '" + args[1] + "' not found!");
            return;
        }

        // CATCH THE AMOUNT
        int count = 64;
        if (args.length > 2) {
            try {
                count = Integer.valueOf(args[2]);
            } catch (Exception e) {
                count = 64;
            }
        }

        // CREATE THE ITEM
        ItemStack item = new ItemStack(ID);
        item.setAmount(count);
        item.setDurability(SubID);

        // FINALLY GIVE THE ITEM
        target.getInventory().addItem(item);
        TextUtils.sendSuccess(player, "Giving " + count + " x " + Material.getMaterial(item.getTypeId()) + ":" + SubID + " to '" + target.getName() + "'.");
        TextUtils.sendInfo(target, "'" + player.getName() + "' has given you " + count + " x " + Material.getMaterial(item.getTypeId()) + ":" + SubID + ".");
    }

    public static int getID(String txt) {
        String[] split = txt.split(":");
        if (split == null)
            return -1;

        String idPart = split[0];
        try {
            int id = Integer.valueOf(idPart);
            Material[] mats = Material.values();
            for (Material mat : mats) {
                if (mat.getId() == id)
                    return mat.getId();
            }
            return -1;
        } catch (Exception e) {
            Material[] mats = Material.values();
            for (Material mat : mats) {
                if (mat.name().equals(idPart))
                    return mat.getId();
            }
        }
        return -1;
    }

    public static short getData(String txt) {
        String[] split = txt.split(":");
        if (split == null)
            return 0;

        if (split.length != 2)
            return 0;

        String dataPart = split[1];
        try {
            short data = Short.valueOf(dataPart);
            return data;
        } catch (Exception e) {
            return 0;
        }
    }
}
