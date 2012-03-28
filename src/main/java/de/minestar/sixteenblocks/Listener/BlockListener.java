package de.minestar.sixteenblocks.Listener;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Manager.AreaManager;
import de.minestar.sixteenblocks.Threads.AFKThread;

public class BlockListener implements Listener {

    private AreaManager areaManager;

    private AFKThread afkThread;

    public BlockListener(AreaManager areaManager, AFKThread afkThread) {
        this.areaManager = areaManager;
        this.afkThread = afkThread;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!Core.isSupporter(event.getPlayer()) && !areaManager.isInArea(event.getPlayer(), event.getBlock())) {
            TextUtils.sendError(event.getPlayer(), "You are not allowed to build here.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (Core.isSupporter(event.getPlayer()))
            return;

        afkThread.takeAktion(event.getPlayer());

        if (event.hasBlock()) {
            if (!areaManager.isInArea(event.getPlayer(), event.getClickedBlock().getRelative(event.getBlockFace()))) {
                TextUtils.sendError(event.getPlayer(), "You are not allowed to build here.");
                event.setCancelled(true);
                return;
            }
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getTypeId() == Material.JUKEBOX.getId()) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.hasItem()) {
            if (event.getMaterial().getId() == Material.SNOW_BALL.getId() || event.getMaterial().getId() == Material.POWERED_MINECART.getId() || event.getMaterial().getId() == Material.STORAGE_MINECART.getId() || event.getMaterial().getId() == Material.EGG.getId() || event.getMaterial().getId() == Material.BOAT.getId() || event.getMaterial().getId() == Material.MINECART.getId() || event.getMaterial().getId() == Material.MONSTER_EGG.getId() || event.getMaterial().getId() == Material.MONSTER_EGGS.getId() || event.getMaterial().getId() == Material.POTION.getId() || event.getMaterial().getId() == Material.EXP_BOTTLE.getId()) {
                event.setCancelled(true);
            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getMaterial().equals(Material.PAINTING)) {
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
        if (event.getBlock().getRelative(BlockFace.UP).getTypeId() == Material.FIRE.getId()) {
            event.getBlock().getRelative(BlockFace.UP).setTypeId(0, false);
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
