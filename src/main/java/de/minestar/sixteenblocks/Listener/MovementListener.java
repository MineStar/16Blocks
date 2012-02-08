package de.minestar.sixteenblocks.Listener;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import de.minestar.sixteenblocks.Core.Settings;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Manager.WorldManager;

public class MovementListener implements Listener {

    private WorldManager worldManager;

    public MovementListener(WorldManager worldManager) {
        this.worldManager = worldManager;
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
        if (event.getPlayer().isOp())
            return;

        // ONLY CHECK, IF THE LOCATION IS DIFFERENT
        if (this.locationEquals(event.getFrom(), event.getTo()))
            return;

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
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        event.setRespawnLocation(new Location(event.getRespawnLocation().getWorld(), Settings.getSpawnVector().getX(), Settings.getSpawnVector().getY(), Settings.getSpawnVector().getZ()));
    }
}
