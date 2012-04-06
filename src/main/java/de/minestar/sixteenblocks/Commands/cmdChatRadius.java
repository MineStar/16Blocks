package de.minestar.sixteenblocks.Commands;

import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.Settings;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Listener.ChatListener;

public class cmdChatRadius extends AbstractCommand {

    public cmdChatRadius(String syntax, String arguments, String node) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Recreate rowstructures";
    }

    @Override
    public void execute(String[] arguments, Player player) {
        // CHECK: PLAYER IS OP OR SUPPORTER
        if (!Core.isSupporter(player) || Core.isVip(player)) {
            TextUtils.sendError(player, "You are not allowed to do this!");
            return;
        }
        try {
            int radius = Integer.valueOf(arguments[0]);
            Settings.setChatRadius(radius);
            if (radius < 1) {
                ChatListener.radiusOff = true;
            } else {
                ChatListener.radiusOff = false;
            }
            TextUtils.sendInfo(player, "Chatradius set to: " + radius);
        } catch (Exception e) {
            TextUtils.sendError(player, "Wrong Syntax! Please use /chatradius <Number>");
            TextUtils.sendInfo(player, "Example: /chatradius 150");
        }
    }
}
