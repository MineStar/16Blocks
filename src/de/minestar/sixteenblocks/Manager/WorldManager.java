package de.minestar.sixteenblocks.Manager;

public class WorldManager {
    private static int minX = -100, maxX = 100;
    private static int minZ = -100, maxZ = 100;

    public boolean canGoTo(int x, int z) {
        if (x < minX || x > maxX || z < minZ || z > maxZ)
            return false;
        return true;
    }
}
