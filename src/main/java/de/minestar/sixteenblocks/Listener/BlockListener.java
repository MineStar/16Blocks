package de.minestar.sixteenblocks.Listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Manager.AreaManager;

public class BlockListener implements Listener {

    private AreaManager areaManager;
    public BlockListener(AreaManager areaManager) {
        this.areaManager = areaManager;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!event.getPlayer().isOp() && !areaManager.isInArea(event.getPlayer(), event.getBlock())) {
            TextUtils.sendError(event.getPlayer(), "You are not allowed to build here.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer().isOp())
            return;

        if (event.hasBlock()) {
            if (!areaManager.isInArea(event.getPlayer(), event.getClickedBlock())) {
                TextUtils.sendError(event.getPlayer(), "You are not allowed to build here.");
                event.setCancelled(true);
                return;
            }
            if (event.getClickedBlock().getTypeId() == Material.JUKEBOX.getId()) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.hasItem()) {
            if (event.getMaterial().getId() == Material.MONSTER_EGG.getId() || event.getMaterial().getId() == Material.MONSTER_EGGS.getId()) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getPlayer().isOp() && !areaManager.isInArea(event.getPlayer(), event.getBlock())) {
            TextUtils.sendError(event.getPlayer(), "You are not allowed to build here.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if (!event.getPlayer().isOp() && !areaManager.isInArea(event.getPlayer(), event.getBlockClicked().getRelative(event.getBlockFace()))) {
            TextUtils.sendError(event.getPlayer(), "You are not allowed to build here.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketFill(PlayerBucketEmptyEvent event) {
        if (!event.getPlayer().isOp() && !areaManager.isInArea(event.getPlayer(), event.getBlockClicked().getRelative(event.getBlockFace()))) {
            TextUtils.sendError(event.getPlayer(), "You are not allowed to build here.");
            event.setCancelled(true);
        }
    }
}
