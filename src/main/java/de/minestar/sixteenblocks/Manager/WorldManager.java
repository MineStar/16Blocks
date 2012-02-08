package de.minestar.sixteenblocks.Manager;

import org.bukkit.Location;

import de.minestar.sixteenblocks.Core.Settings;

public class WorldManager {
    // private StructureManager structureManager;

    private static int minZ = -50, maxZ = 200;

    public WorldManager(StructureManager structureManager) {
        // this.structureManager = structureManager;
    }

    public boolean canGoTo(int x, int y, int z) {
        if (x < -(Settings.getSkinsRight() * Settings.getAreaSizeX()) - (Settings.getAreaSizeX() >> 1) || x > Settings.getSkinsLeft() * Settings.getAreaSizeX() + Settings.getAreaSizeX() || z < minZ || z > maxZ || y < 1 || y > 127)
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
        if (location.getBlockX() >= Settings.getSkinsLeft() * Settings.getAreaSizeX() + Settings.getAreaSizeX())
            location.setX(Settings.getSkinsLeft() * Settings.getAreaSizeX() - 1 + Settings.getAreaSizeX());
        if (location.getBlockX() <= -(Settings.getSkinsRight() * Settings.getAreaSizeX()) - (Settings.getAreaSizeX() >> 1))
            location.setX(-(Settings.getSkinsRight() * Settings.getAreaSizeX()) + 1 - (Settings.getAreaSizeX() >> 1));
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
