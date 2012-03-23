package de.minestar.sixteenblocks.Listener;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Manager.AreaManager;
import de.minestar.sixteenblocks.Threads.BlockWorkaroundThread;

public class BlockListener implements Listener {

    private AreaManager areaManager;
    private BlockWorkaroundThread workaroundThread;

    public BlockListener(AreaManager areaManager, BlockWorkaroundThread workaroundThread) {
        this.areaManager = areaManager;
        this.workaroundThread = workaroundThread;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!Core.isSupporter(event.getPlayer()) && !areaManager.isInArea(event.getPlayer(), event.getBlock())) {
            TextUtils.sendError(event.getPlayer(), "You are not allowed to build here.");
            event.setCancelled(true);
        }
        // WORKAROUND FOR GRAVEL & SAND FALLING DOWN:
        // WE NEED TO REPLACE THE BLOCK BELOW (IF AIR), AND THEN REMOVE IT AFTER
        // 1 TICK TO PREVENT GRAVEL/SAND FROM FALLING
        else if (event.getBlock().getType().equals(Material.GRAVEL) || event.getBlock().getType().equals(Material.SAND)) {
            if (event.getBlockPlaced().getRelative(BlockFace.DOWN).getTypeId() == Material.AIR.getId()) {
                event.getBlockPlaced().getRelative(BlockFace.DOWN).setTypeId(Material.STONE.getId(), true);
                this.workaroundThread.addBlock(event.getBlock().getRelative(BlockFace.DOWN).getLocation());
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (Core.isSupporter(event.getPlayer()))
            return;

        if (event.hasBlock()) {
            if (!areaManager.isInArea(event.getPlayer(), event.getClickedBlock().getRelative(event.getBlockFace()))) {
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
            if (event.getMaterial().getId() == Material.MONSTER_EGG.getId() || event.getMaterial().getId() == Material.MONSTER_EGGS.getId() || event.getMaterial().getId() == Material.POTION.getId() || event.getMaterial().getId() == Material.EXP_BOTTLE.getId()) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!Core.isSupporter(event.getPlayer()) && !areaManager.isInArea(event.getPlayer(), event.getBlock())) {
            TextUtils.sendError(event.getPlayer(), "You are not allowed to build here.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if (!Core.isSupporter(event.getPlayer()) && !areaManager.isInArea(event.getPlayer(), event.getBlockClicked().getRelative(event.getBlockFace()))) {
            TextUtils.sendError(event.getPlayer(), "You are not allowed to build here.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketFill(PlayerBucketEmptyEvent event) {
        if (!Core.isSupporter(event.getPlayer()) && !areaManager.isInArea(event.getPlayer(), event.getBlockClicked().getRelative(event.getBlockFace()))) {
            TextUtils.sendError(event.getPlayer(), "You are not allowed to build here.");
            event.setCancelled(true);
        }
    }
}
