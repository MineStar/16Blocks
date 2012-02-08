package de.minestar.sixteenblocks.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Manager.AreaManager;
import de.minestar.sixteenblocks.Units.ZoneXZ;

public class BlockListener implements Listener {

    private AreaManager areaManager;

    public BlockListener(AreaManager areaManager) {
        this.areaManager = areaManager;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ZoneXZ thisZone = ZoneXZ.fromPoint(event.getBlock().getX(), event.getBlock().getZ());
        TextUtils.sendInfo(event.getPlayer(), "AreaX / AreaZ : " + thisZone.getX() + " / " + thisZone.getZ());

        if (!event.getPlayer().isOp() && !areaManager.isInArea(event.getPlayer(), event.getBlock())) {
            TextUtils.sendError(event.getPlayer(), "You are not allowed to build here.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        ZoneXZ thisZone = ZoneXZ.fromPoint(event.getBlock().getX(), event.getBlock().getZ());
        TextUtils.sendInfo(event.getPlayer(), "AreaX / AreaZ : " + thisZone.getX() + " / " + thisZone.getZ());
        if (!event.getPlayer().isOp() && !areaManager.isInArea(event.getPlayer(), event.getBlock())) {
            TextUtils.sendError(event.getPlayer(), "You are not allowed to build here.");
            event.setCancelled(true);
        }
    }
}
