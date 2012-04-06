package de.minestar.sixteenblocks.Commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;

public class cmdCreateRow extends AbstractCommand {

    public cmdCreateRow(String syntax, String arguments, String node) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Recreate rowstructures";
    }

    @Override
    public void execute(String[] arguments, Player player) {
        // CHECK : SHUTDOWN / RELOAD
        if (Core.shutdownServer) {
            TextUtils.sendError(player, "/createrow is not possible at this moment. Server will reload or restart in a few moments.");
            TextUtils.sendLine(player, ChatColor.GRAY, "Please try again in a few moments.");
            return;
        }

        // CHECK: PLAYER IS OP OR SUPPORTER
        if (!Core.isSupporter(player) || Core.isVip(player)) {
            TextUtils.sendError(player, "You are not allowed to do this!");
            return;
        }
        try {
            int row = Integer.valueOf(arguments[0]);
            TextUtils.sendInfo(player, "Recreating rowstructures: " + row);
            Core.getInstance().getAreaManager().createRowStructures(row);
        } catch (Exception e) {
            TextUtils.sendError(player, "Wrong Syntax! Please use /createrow <Number>");
            TextUtils.sendInfo(player, "Example: /createrow 5");
        }
    }
}
