package de.minestar.sixteenblocks.Commands;

import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;

public class cmdTP extends AbstractCommand {

    public cmdTP(String syntax, String arguments, String node) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Jump to a specific row";
    }

    @Override
    public void execute(String[] arguments, Player player) {
        Player target = PlayerUtils.getOnlinePlayer(arguments[0]);
        if (target == null) {
            TextUtils.sendError(player, "Player '" + arguments[0] + "' was not found (maybe he is offline?)");
            return;
        } else {
            player.teleport(player.getLocation());
            TextUtils.sendInfo(player, "Teleporting you to '" + target.getName() + "'...");
        }
    }
}
