package de.minestar.sixteenblocks.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.Settings;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Manager.AreaManager;
import de.minestar.sixteenblocks.Threads.AreaDeletionThread;
import de.minestar.sixteenblocks.Units.ZoneXZ;

public class cmdReset extends AbstractCommand {

    private AreaManager areaManager;

    public cmdReset(String syntax, String arguments, String node, AreaManager areaManager) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Reset a skin";
        this.areaManager = areaManager;
    }

    @Override
    public void execute(String[] arguments, Player player) {
        // CHECK : SHUTDOWN / RELOAD
        if (Core.shutdownServer) {
            TextUtils.sendError(player, "/reset is not possible at this moment. Server will reload or restart in a few moments.");
            TextUtils.sendLine(player, ChatColor.GRAY, "Please try again in a few moments.");
            return;
        }

        // CHECK: PLAYER IS OP OR SUPPORTER
        if (!Core.isSupporter(player)) {
            TextUtils.sendError(player, "You are not allowed to do this!");
            return;
        }

        // CHECK : AREA IS USED
        ZoneXZ thisZone = ZoneXZ.fromPoint(player.getLocation().getBlockX(), player.getLocation().getBlockZ());
        if (!this.areaManager.containsPlayerArea(thisZone)) {
            TextUtils.sendError(player, "This area is not taken by anyone.");
            return;
        }

        // DELETE OLD BLOCKS
        TextUtils.sendSuccess(player, "Resetting area of '" + this.areaManager.getPlayerArea(thisZone).getAreaOwner() + "'...");
        AreaDeletionThread aThread = new AreaDeletionThread(Bukkit.getWorlds().get(0), thisZone, player.getName());
        aThread.initTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), aThread, 0, Settings.getTicksBetweenReplace()));
    }
}
