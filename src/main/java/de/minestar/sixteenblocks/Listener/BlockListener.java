package de.minestar.sixteenblocks.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Enums.EnumStructures;
import de.minestar.sixteenblocks.Manager.AreaManager;
import de.minestar.sixteenblocks.Manager.StructureManager;
import de.minestar.sixteenblocks.Units.ZoneXZ;

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

        if (!event.getPlayer().isOp() && !areaManager.isInArea(event.getPlayer(), event.getBlock())) {
            TextUtils.sendError(event.getPlayer(), "You are not allowed to build here.");
            event.setCancelled(true);

            this.structureManager.getStructure(EnumStructures.ZONE_STEVE).createStructure(thisZone.getX(), thisZone.getZ());
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
