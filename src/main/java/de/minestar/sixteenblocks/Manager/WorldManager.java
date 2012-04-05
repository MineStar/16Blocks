package de.minestar.sixteenblocks.Manager;

import org.bukkit.Location;

import de.minestar.sixteenblocks.Core.Settings;

public class WorldManager {
    // private StructureManager structureManager;

    private static int minZ = -100, maxZ = 200;
    private static int minX, maxX;

    public WorldManager() {
        minX = -((Settings.getSkinsRight() + 1) * Settings.getAreaSizeX());
        maxX = (Settings.getSkinsLeft() + 2) * Settings.getAreaSizeX();
    }

    public boolean canGoTo(int x, int y, int z) {
        if (x < minX || x > maxX || z < minZ || z > maxZ || y < 1 || y > 127)
            return false;
        return true;
    }

    public void setMaxZ(int z) {
        maxZ = z;
    }

    public void setMinZ(int z) {
        minZ = z;
    }

    public Location getCorrectedResetLocation(Location location) {
        // CORRECT X
        if (location.getBlockX() >= maxX)
            location.setX(maxX - 1);
        if (location.getBlockX() <= minX)
            location.setX(minX + 1);
        // CORRECT Y
        if (location.getBlockY() >= 127)
            location.setY(126);
        if (location.getBlockY() <= 1)
            location.setY(1);
        // CORRECT Z
        if (location.getBlockZ() >= maxZ)
            location.setZ(maxZ - 1);
        if (location.getBlockZ() <= minZ)
            location.setZ(minZ + 1);
        return location.clone();
    }
}
