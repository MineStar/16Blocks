package de.minestar.sixteenblocks.Listener;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

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
        // ONLY CHECK, IF THE LOCATION IS DIFFERENT
        if (this.locationEquals(event.getFrom(), event.getTo()))
            return;

        // CHECK IF THE PLAYER CAN GO THERE
        if (!worldManager.canGoTo(event.getTo().getBlockX(), event.getTo().getBlockZ())) {
            event.setTo(event.getFrom().clone());
            event.getPlayer().sendMessage(ChatColor.RED + "You are not allowed to go here.");
            return;
        }
    }
}
