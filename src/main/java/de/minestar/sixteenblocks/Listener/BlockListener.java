package de.minestar.sixteenblocks.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import de.minestar.sixteenblocks.Manager.AreaManager;
import de.minestar.sixteenblocks.Manager.StructureManager;
import de.minestar.sixteenblocks.Structures.EnumStructures;
import de.minestar.sixteenblocks.Units.ZoneXZ;
import de.minestar.sixteenblocks.core.Settings;
import de.minestar.sixteenblocks.core.TextUtils;

public class BlockListener implements Listener {

    private AreaManager areaManager;
    private StructureManager structureManager;

    public BlockListener(AreaManager areaManager, StructureManager structureManager) {
        this.areaManager = areaManager;
        this.structureManager = structureManager;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ZoneXZ thisZone = ZoneXZ.fromPoint(event.getBlock().getX(), event.getBlock().getZ());
        TextUtils.sendInfo(event.getPlayer(), "AreaX / AreaZ : " + thisZone.getX() + " / " + thisZone.getZ());

        // TEST TO CREATE STRUCTURES
        if (!event.getPlayer().isOp()) {
            structureManager.getStructure(EnumStructures.ZONE_STREETS_AND_SOCKET).createStructure(event.getBlock().getWorld(), thisZone.getX() * Settings.getAreaSizeX() - (thisZone.getZ() % 2 != 0 ? (Settings.getAreaSizeX() >> 1) : 0), thisZone.getZ() * Settings.getAreaSizeZ());
        }
        // TEST - END

        if (!event.getPlayer().isOp() && !areaManager.isInArea(event.getPlayer(), event.getBlock())) {
            TextUtils.sendError(event.getPlayer(), "You are not allowed to build here.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        ZoneXZ thisZone = ZoneXZ.fromPoint(event.getBlock().getX(), event.getBlock().getZ());
        TextUtils.sendInfo(event.getPlayer(), "AreaX / AreaZ : " + thisZone.getX() + " / " + thisZone.getZ());

        // TEST TO EXPORT AREAS
        if (!event.getPlayer().isOp()) {
            if (areaManager.exportStructure(event.getBlock().getWorld(), EnumStructures.ZONE_STREETS_BACK.getName(), thisZone)) {
                TextUtils.sendSuccess(event.getPlayer(), "Saved Area as '" + EnumStructures.ZONE_STREETS_BACK.getName() + "'.");
            } else {
                TextUtils.sendError(event.getPlayer(), "Could not save area!");
            }
        }
        // TEST - END

        if (!event.getPlayer().isOp() && !areaManager.isInArea(event.getPlayer(), event.getBlock())) {
            TextUtils.sendError(event.getPlayer(), "You are not allowed to build here.");
            event.setCancelled(true);
        }
    }
}
