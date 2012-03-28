package de.minestar.sixteenblocks.Listener;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.Settings;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Manager.WorldManager;
import de.minestar.sixteenblocks.Threads.AFKThread;

public class MovementListener implements Listener {

    private WorldManager worldManager;

    private AFKThread afkThread;

    public MovementListener(WorldManager worldManager, AFKThread afkThread) {
        this.worldManager = worldManager;
        this.afkThread = afkThread;
    }

    /**
     * Check if X & Z of both locations are equal
     * 
     * @param from
     *            Location 1
     * @param to
     *            Location 2
     * @return <b>true</b> if they are both equal, otherwise <b>false</b>
     */
    private boolean locationEquals(Location from, Location to) {
        return from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (Core.isSupporter(event.getPlayer()))
            return;

        // ONLY CHECK, IF THE LOCATION IS DIFFERENT
        if (this.locationEquals(event.getFrom(), event.getTo()))
            return;

        afkThread.takeAktion(event.getPlayer());

        // CHECK IF THE PLAYER CAN GO THERE
        if (!worldManager.canGoTo(event.getTo().getBlockX(), event.getTo().getBlockY(), event.getTo().getBlockZ())) {
            event.setTo(worldManager.getCorrectedResetLocation(event.getFrom()));
            TextUtils.sendError(event.getPlayer(), "You are not allowed to go here.");
            return;
        }
    }
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (!worldManager.canGoTo(event.getTo().getBlockX(), event.getTo().getBlockY(), event.getTo().getBlockZ())) {
            event.setTo(new Location(event.getTo().getWorld(), Settings.getSpawnVector().getX(), Settings.getSpawnVector().getY(), Settings.getSpawnVector().getZ()));
        }

        afkThread.takeAktion(event.getPlayer());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        event.setRespawnLocation(new Location(event.getRespawnLocation().getWorld(), Settings.getSpawnVector().getX(), Settings.getSpawnVector().getY(), Settings.getSpawnVector().getZ()));
    }
}
