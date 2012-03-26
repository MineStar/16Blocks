package de.minestar.sixteenblocks.Units;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import de.minestar.sixteenblocks.Core.Settings;

public class ZoneXZ {
    private final int x, z;

    private ZoneXZ(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public Location getSpawnLocation() {
        Location location = new Location(Bukkit.getWorlds().get(0), x * Settings.getAreaSizeX() + (z % 2 == 0 ? (Settings.getAreaSizeX() >> 1) : 0), Settings.getBaseY(), z * Settings.getAreaSizeZ() + 6);
        location.setPitch(-60.0f);
        return location;
    }

    public static ZoneXZ fromPoint(int x, int z) {
        int thisZ = z >> 5;
        int thisX = (x + (thisZ % 2 != 0 ? (Settings.getAreaSizeX() >> 1) : 0)) >> 5;
        return new ZoneXZ(thisX, thisZ);
    }

    public static ZoneXZ fromCoordinates(int x, int z) {
        return new ZoneXZ(x, z);
    }

    @Override
    public String toString() {
        return this.z + ":" + this.x;
    }
}
