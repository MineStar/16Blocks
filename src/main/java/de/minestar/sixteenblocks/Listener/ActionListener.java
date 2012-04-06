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

public class ActionListener implements Listener {

    private AreaManager areaManager;

    private AFKThread afkThread;

    public ActionListener(AreaManager areaManager, AFKThread afkThread) {
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

        if (Core.shutdownServer) {
            TextUtils.sendError(event.getPlayer(), "Server is shutting down...");
            event.setCancelled(false);
            return;
        }

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
            // CANCEL EVENTS WITH SPECIAL ITEMS
            switch (event.getMaterial()) {
//                case SNOW_BALL :
//                case POWERED_MINECART :
//                case STORAGE_MINECART :
//                case EGG :
//                case BOAT :
//                case MINECART :
//                case MONSTER_EGG :
//                case MONSTER_EGGS :
//                case POTION :
//                case EXP_BOTTLE :
                case FISHING_ROD :
                case PAINTING :
                case BOW :
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
