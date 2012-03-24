package de.minestar.sixteenblocks.Commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Manager.AreaManager;

public class cmdBan extends AbstractCommand {

    private AreaManager areaManager;

    public cmdBan(String syntax, String arguments, String node, AreaManager areaManager) {
        super(Core.NAME, syntax, arguments, node);
        this.areaManager = areaManager;
        this.description = "Ban a player and delete the area";
    }

    @Override
    public void execute(String[] arguments, Player player) {

        // CHECK: PLAYER IS OP OR SUPPORTER
        if (!Core.isSupporter(player)) {
            TextUtils.sendError(player, "You are not allowed to do this!");
            return;
        }

        // GET AREA
        Player target = PlayerUtils.getOnlinePlayer(arguments[0]);
        if (target == null) {
            String playerName = PlayerUtils.getOfflinePlayerName(arguments[0]);
            if (playerName == null) {
                TextUtils.sendInfo(player, "Player '" + arguments[0] + "' doesn't exist(was never on the server), but was banned!");
                playerName = arguments[0];
            } else
                TextUtils.sendInfo(player, "Player '" + arguments[0] + "' is offline!");

            Bukkit.getOfflinePlayer(playerName).setBanned(true);
            TextUtils.sendSuccess(player, "Player '" + arguments[0] + "' banned.");
            return;
        }

        // BAN PLAYER
        target.setBanned(true);
        TextUtils.sendSuccess(player, "Player '" + target.getName() + "' banned.");

        /*
         * SkinArea thisArea =
         * this.areaManager.getExactPlayerArea(target.getName());
         * 
         * // CHECK: NO AREA FOUND if (thisArea == null) {
         * TextUtils.sendSuccess(player, "Player '" + target.getName() +
         * "' banned. This player did not have a skin.");
         * target.kickPlayer("You were BANNED!"); return; }
         * 
         * // CHECK : AREA IS NOT BLOCKED if
         * (this.areaManager.isAreaBlocked(thisArea.getZoneXZ())) {
         * TextUtils.sendError(player, "Player '" + target.getName() +
         * "' banned, but Skin was not deleted (blocked by another process!)");
         * target.kickPlayer("You were BANNED!"); return; }
         * 
         * // DELETE THIS AREA this.areaManager.deletePlayerArea(thisArea,
         * player); TextUtils.sendSuccess(player, "Player '" + target.getName()
         * + "' banned and started deletion of '" + thisArea.getAreaOwner() +
         * "'.");
         */

        // KICK PLAYER
        target.kickPlayer("You were BANNED!");
    }
}
