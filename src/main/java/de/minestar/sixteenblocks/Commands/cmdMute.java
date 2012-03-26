package de.minestar.sixteenblocks.Commands;

import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;

public class cmdMute extends AbstractCommand {

    public cmdMute(String syntax, String arguments, String node) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Jump to a specific player";
    }

    @Override
    public void execute(String[] arguments, Player player) {
        // CHECK: PLAYER IS OP OR SUPPORTER
        if (!Core.isSupporter(player)) {
            TextUtils.sendError(player, "You are not allowed to do this!");
            return;
        }

        Player target = PlayerUtils.getOnlinePlayer(arguments[0]);
        if (target == null) {
            TextUtils.sendError(player, "Player '" + arguments[0] + "' was not found (maybe he is offline?)");
            return;
        } else {
            boolean muted = Core.getInstance().getChatListener().toggleMute(target);
            if (muted)
                TextUtils.sendInfo(player, "Player '" + target.getName() + "' is now muted...");
            else
                TextUtils.sendInfo(player, "Player '" + target.getName() + "' is no longer muted...");
        }
    }
}
