package de.minestar.sixteenblocks.Commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractExtendedCommand;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Manager.AreaManager;
import de.minestar.sixteenblocks.Manager.SkinArea;
import de.minestar.sixteenblocks.Units.ZoneXZ;

public class cmdDeleteArea extends AbstractExtendedCommand {

    private AreaManager areaManager;
    public cmdDeleteArea(String syntax, String arguments, String node, AreaManager areaManager) {
        super(Core.NAME, syntax, arguments, node);
        this.areaManager = areaManager;
        this.description = "Delete an area";
    }

    @Override
    public void execute(String[] arguments, Player player) {
        // CHECK : SHUTDOWN / RELOAD
        if (Core.shutdownServer) {
            TextUtils.sendError(player, "/delete is not possible at this moment. Server will reload or restart in a few moments.");
            TextUtils.sendLine(player, ChatColor.GRAY, "Please try again in a few moments.");
            return;
        }

        // CHECK: PLAYER IS OP OR SUPPORTER
        if (!Core.isSupporter(player) || Core.isVip(player)) {
            TextUtils.sendError(player, "You are not allowed to do this!");
            return;
        }

        if (arguments.length > 0) {
            // GET AREA
            SkinArea thisArea = this.areaManager.getExactPlayerArea(arguments[0]);

            // CHECK: NO AREA FOUND
            if (thisArea == null) {
                TextUtils.sendError(player, "No area for player '" + arguments[0] + "' found!");
                return;
            }

            // CHECK : AREA IS NOT BLOCKED
            if (this.areaManager.isAreaBlocked(thisArea.getZoneXZ())) {
                TextUtils.sendError(player, "This area is currently blocked by another process.");
                return;
            }

            // DELETE THIS AREA
            this.areaManager.deletePlayerArea(thisArea, player);
            TextUtils.sendSuccess(player, "Starting deletion of '" + thisArea.getAreaOwner() + "'.");
        } else {
            // GET AREA
            ZoneXZ thisZone = ZoneXZ.fromPoint(player.getLocation().getBlockX(), player.getLocation().getBlockZ());
            SkinArea thisArea = this.areaManager.getPlayerArea(thisZone);

            // CHECK: NO AREA FOUND
            if (thisArea == null) {
                TextUtils.sendError(player, "No area found at this position!");
                return;
            }

            // CHECK : AREA IS NOT BLOCKED
            if (this.areaManager.isAreaBlocked(thisArea.getZoneXZ())) {
                TextUtils.sendError(player, "This area is currently blocked by another process.");
                return;
            }

            // DELETE THIS AREA
            this.areaManager.deletePlayerArea(thisArea, player);
            TextUtils.sendSuccess(player, "Starting deletion of '" + thisArea.getAreaOwner() + "'.");
        }

    }
}
