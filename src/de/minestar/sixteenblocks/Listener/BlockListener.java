package de.minestar.sixteenblocks.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import de.minestar.sixteenblocks.Manager.AreaManager;
import de.minestar.sixteenblocks.core.TextUtils;

public class BlockListener implements Listener {

    private AreaManager areaManager;

    public BlockListener(AreaManager areaManager) {
        this.areaManager = areaManager;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!event.getPlayer().isOp() && !areaManager.isInArea(event.getPlayer().getName(), event.getBlock())) {
            TextUtils.sendError(event.getPlayer(), "You are not allowed to build here.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getPlayer().isOp() && !areaManager.isInArea(event.getPlayer().getName(), event.getBlock())) {
            TextUtils.sendError(event.getPlayer(), "You are not allowed to build here.");
            event.setCancelled(true);
        }
    }
}
