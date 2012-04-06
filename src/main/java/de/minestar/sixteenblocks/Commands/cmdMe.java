package de.minestar.sixteenblocks.Commands;

import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;

public class cmdMe extends AbstractCommand {

    public cmdMe(String syntax, String arguments, String node) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Catch /me";
    }

    @Override
    public void execute(String[] arguments, Player player) {
        // CHECK: PLAYER IS OP OR SUPPORTER
        if (!Core.isSupporter(player) || Core.isVip(player)) {
            TextUtils.sendError(player, "You are not allowed to do this!");
            return;
        }
    }
}
