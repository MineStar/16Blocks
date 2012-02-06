package de.minestar.sixteenblocks.Manager;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import de.minestar.sixteenblocks.core.TextUtils;
import de.minestar.sixteenblocks.units.ZoneXZ;

public class AreaManager {
    private static int areaSizeX = 32, areaSizeZ = 32;
    private static int minimumY = 5;

    private HashMap<String, SkinArea> areaList = new HashMap<String, SkinArea>();

    // ////////////////////////////////////////////////
    //
    // CONSTRUCTOR
    //
    // ////////////////////////////////////////////////

    public AreaManager() {
        this.loadAreas();
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

    public SkinArea getArea(ZoneXZ thisZone) {
        return this.areaList.get(thisZone.toString());
    }

    public boolean containsArea(ZoneXZ thisZone) {
        return this.containsArea(thisZone.toString());
    }

    public boolean containsArea(String coordinateString) {
        return this.areaList.containsKey(coordinateString);
    }

    public boolean addArea(SkinArea skinArea) {
        if (this.containsArea(skinArea.getZoneXZ()))
            return false;
        this.areaList.put(skinArea.getZoneXZ().toString(), skinArea);
        return true;
    }

    public boolean removeArea(ZoneXZ thisZone) {
        return (this.areaList.remove(thisZone.toString()) != null);
    }

    public boolean removeArea(SkinArea thisArea) {
        return this.removeArea(thisArea.getZoneXZ());
    }

    public boolean removeArea(Player player) {
        SkinArea toDelete = null;
        for (SkinArea thisArea : this.areaList.values()) {
            if (thisArea.isAreaOwner(player)) {
                toDelete = thisArea;
                break;
            }
        }
        // DELETE IF FOUND
        if (toDelete != null)
            return this.removeArea(toDelete);
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
        if (y < AreaManager.minimumY)
            return false;

        ZoneXZ thisZone = ZoneXZ.fromPoint(x, z);
        SkinArea thisArea = this.getArea(thisZone);
        if (thisArea == null) {
            return false;
        }
        return thisArea.isAreaOwner(player);
    }

    // ////////////////////////////////////////////////
    //
    // GETTER-METHODS
    //
    // ////////////////////////////////////////////////

    public static int getAreaSizeX() {
        return areaSizeX;
    }

    public static int getAreaSizeZ() {
        return areaSizeZ;
    }

    public static int getMinimumY() {
        return minimumY;
    }
}
