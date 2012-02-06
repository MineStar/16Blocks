package de.minestar.sixteenblocks.Manager;

import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.block.Block;

import de.minestar.sixteenblocks.core.Console;

public class AreaManager {
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
        Console.log("Areas loaded.", Level.FINE);
    }

    // ////////////////////////////////////////////////
    //
    // MODIFY & GET AREAS
    //
    // ////////////////////////////////////////////////

    public boolean containsArea(String playerName) {
        return this.areaList.containsKey(playerName);
    }

    public boolean addArea(String playerName, SkinArea skinArea) {
        if (this.containsArea(playerName))
            return false;
        return (this.areaList.put(playerName, skinArea) == null);
    }

    public boolean removeArea(String playerName) {
        if (!this.containsArea(playerName))
            return false;
        return (this.areaList.remove(playerName) != null);
    }

    public SkinArea getArea(String playerName) {
        return this.areaList.get(playerName);
    }

    // ////////////////////////////////////////////////
    //
    // IS IN AREA
    //
    // ////////////////////////////////////////////////

    public boolean isInArea(String playerName, Location location) {
        return this.isInArea(playerName, location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public boolean isInArea(String playerName, Block block) {
        return this.isInArea(playerName, block.getX(), block.getY(), block.getZ());
    }

    public boolean isInArea(String playerName, int x, int y, int z) {
        SkinArea thisArea = this.getArea(playerName);
        if (thisArea == null) {
            return false;
        }
        return thisArea.isInArea(x, y, z);
    }
}
