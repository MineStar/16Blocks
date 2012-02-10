package de.minestar.sixteenblocks.Manager;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import de.minestar.sixteenblocks.Core.Settings;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Enums.EnumStructures;
import de.minestar.sixteenblocks.Units.Structure;
import de.minestar.sixteenblocks.Units.ZoneXZ;

public class AreaManager {
    private HashMap<String, SkinArea> usedAreaList = new HashMap<String, SkinArea>();
    private HashMap<String, SkinArea> unusedAreaList = new HashMap<String, SkinArea>();

    private StructureManager structureManager;

    // ////////////////////////////////////////////////
    //
    // CONSTRUCTOR
    //
    // ////////////////////////////////////////////////

    public AreaManager() {
        this.loadAreas();
    }

    public void init(StructureManager structureManager) {
        this.structureManager = structureManager;
    }

    // ////////////////////////////////////////////////
    //
    // AREA CREATION
    //
    // ////////////////////////////////////////////////

    public void createRow(int z) {
        int i = 0;
        for (int x = -Settings.getSkinsRight() + (z % 2 == 0 ? 0 : 1); x <= Settings.getSkinsLeft(); x++) {
            this.createUnusedArea(new SkinArea(x, z, ""), true);
            this.structureManager.getStructure(EnumStructures.ZONE_STREETS_BACK).createStructure(x, z - 1);
            if (z % 2 != 0 && i == 0) {
                this.structureManager.getStructure(EnumStructures.ZONE_STREETS_BACK).createStructure(x - 1, z - 1);
            }
        }
    }

    // ////////////////////////////////////////////////
    //
    // PERSISTENCE
    //
    // ////////////////////////////////////////////////

    // TODO: PLACE PERSISTENCE-METHODS HERE
    // I THINK WE SHOULD USE MySQL
    private void loadAreas() {
        TextUtils.logInfo("Areas loaded.");
    }

    // ////////////////////////////////////////////////
    //
    // MODIFY & GET AREAS
    //
    // ////////////////////////////////////////////////

    public boolean createUnusedArea(SkinArea skinArea, boolean createStructures) {
        if (this.containsUnusedArea(skinArea.getZoneXZ()))
            return false;
        this.unusedAreaList.put(skinArea.getZoneXZ().toString(), skinArea);
        this.usedAreaList.remove(skinArea.getZoneXZ().toString());
        if (createStructures) {
            this.structureManager.getStructure(EnumStructures.ZONE_STREETS_AND_SOCKET).createStructure(skinArea.getZoneXZ().getX(), skinArea.getZoneXZ().getZ());
        }
        return true;
    }

    public SkinArea getUnusedArea(ZoneXZ thisZone) {
        return this.unusedAreaList.get(thisZone.toString());
    }

    public boolean containsUnusedArea(ZoneXZ thisZone) {
        return this.containsUnusedArea(thisZone.toString());
    }

    public boolean containsUnusedArea(String coordinateString) {
        return this.unusedAreaList.containsKey(coordinateString);
    }

    // ///////////////////////////
    // USED AREAS
    // //////////////////////////
    public SkinArea getPlayerArea(ZoneXZ thisZone) {
        return this.usedAreaList.get(thisZone.toString());
    }

    public boolean containsPlayerArea(ZoneXZ thisZone) {
        return this.containsPlayerArea(thisZone.toString());
    }

    public boolean containsPlayerArea(String coordinateString) {
        return this.usedAreaList.containsKey(coordinateString);
    }

    public boolean createPlayerArea(SkinArea skinArea, boolean createStructures) {
        if (this.containsPlayerArea(skinArea.getZoneXZ()))
            return false;
        this.usedAreaList.put(skinArea.getZoneXZ().toString(), skinArea);
        this.unusedAreaList.remove(skinArea.getZoneXZ().toString());
        if (createStructures) {
            this.structureManager.getStructure(EnumStructures.ZONE_STEVE).createStructure(skinArea.getZoneXZ().getX(), skinArea.getZoneXZ().getZ());
        }
        return true;
    }

    public boolean removePlayerArea(ZoneXZ thisZone) {
        if (!this.containsPlayerArea(thisZone.toString()))
            return false;

        this.unusedAreaList.put(thisZone.toString(), new SkinArea(thisZone.getX(), thisZone.getZ(), ""));
        this.usedAreaList.remove(thisZone.toString());
        return true;
    }

    public boolean removePlayerArea(SkinArea thisArea) {
        return this.removePlayerArea(thisArea.getZoneXZ());
    }

    public boolean removePlayerArea(Player player) {
        SkinArea toDelete = null;
        for (SkinArea thisArea : this.usedAreaList.values()) {
            if (thisArea.isAreaOwner(player)) {
                toDelete = thisArea;
                break;
            }
        }
        // DELETE IF FOUND
        if (toDelete != null)
            return this.removePlayerArea(toDelete);
        return false;
    }

    // ////////////////////////////////////////////////
    //
    // IS IN AREA
    //
    // ////////////////////////////////////////////////

    public boolean isInArea(Player player, Location location) {
        return this.isInArea(player, location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public boolean isInArea(Player player, Block block) {
        return this.isInArea(player, block.getX(), block.getY(), block.getZ());
    }

    public boolean isInArea(Player player, int x, int y, int z) {
        if (y < Settings.getMinimumBuildY())
            return false;

        ZoneXZ thisZone = ZoneXZ.fromPoint(x, z);
        SkinArea thisArea = this.getPlayerArea(thisZone);
        if (thisArea == null) {
            return false;
        }
        return thisArea.isAreaOwner(player);
    }

    public void testMethod() {
        Structure thisStructure = this.structureManager.getStructure(EnumStructures.STREETS_CORNER);
        thisStructure.createStructure(0, -2);
    }
}
