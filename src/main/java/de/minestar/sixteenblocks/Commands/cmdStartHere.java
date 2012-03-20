package de.minestar.sixteenblocks.Commands;

import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Manager.AreaManager;
import de.minestar.sixteenblocks.Manager.SkinArea;
import de.minestar.sixteenblocks.Units.ZoneXZ;

public class cmdStartHere extends AbstractCommand {

    private AreaManager areaManager;

    public cmdStartHere(String syntax, String arguments, String node, AreaManager areaManager) {
        super(Core.NAME, syntax, arguments, node);
        this.areaManager = areaManager;
        this.description = "Make the current zone to your zone";
    }

    @Override
    public void execute(String[] arguments, Player player) {
        ZoneXZ thisZone = ZoneXZ.fromPoint(player.getLocation().getBlockX(), player.getLocation().getBlockZ());
        // CHECK : PLAYER HAS NO AREA
//        if (this.areaManager.hasPlayerArea(player)) {
//            TextUtils.sendError(player, "You already own an area.");
//            TextUtils.sendLine(player, ChatColor.GRAY, "Go there with '/home'");
//            return;
//        }

        // CHECK : AREA IS VALID AND FREE
        if (!this.areaManager.containsUnusedArea(thisZone)) {
            if (this.areaManager.containsPlayerArea(thisZone)) {
                TextUtils.sendError(player, "This area is already taken by '" + this.areaManager.getPlayerArea(thisZone).getAreaOwner() + "'.");
            } else {
                TextUtils.sendError(player, "This is not a valid area.");
            }
            return;
        }

        // CHECK : AREA IS NOT BLOCKED
        if (this.areaManager.isAreaBlocked(thisZone)) {
            TextUtils.sendError(player, "This area is currently blocked by another process.");
            return;
        }

        // TAKE THIS AREA
        this.areaManager.createPlayerArea(new SkinArea(thisZone.getX(), thisZone.getZ(), player.getName()), true, player);
        TextUtils.sendSuccess(player, "You are now owner of this area: [ " + thisZone.getX() + " / " + thisZone.getZ() + " ]");

        // TELEPORT TO HOME-POINT
        player.teleport(thisZone.getSpawnLocation());
    }
}
