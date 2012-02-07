package de.minestar.sixteenblocks.Manager;

import org.bukkit.Location;

public class WorldManager {
    private static int tempSize = 500;
    private static int minX = -tempSize, maxX = tempSize;
    private static int minZ = -tempSize, maxZ = tempSize;

    // private StructureManager structureManager;

    public WorldManager(StructureManager structureManager) {
        // this.structureManager = structureManager;
    }

    public boolean canGoTo(int x, int y, int z) {
        if (x < minX || x > maxX || z < minZ || z > maxZ || y < 1 || y > 127)
            return false;
        return true;
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
