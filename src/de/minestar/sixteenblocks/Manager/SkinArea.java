package de.minestar.sixteenblocks.Manager;

import org.bukkit.entity.Player;

import de.minestar.sixteenblocks.units.ZoneXZ;

public class SkinArea {
    private final String areaOwner;
    private final ZoneXZ coordinates;

    public SkinArea(int x, int z, String areaOwner) {
        this.coordinates = ZoneXZ.fromCoordinates(x, z);
        this.areaOwner = areaOwner;
    }

    public String getAreaOwner() {
        return areaOwner;
    }

    public boolean isAreaOwner(String playerName) {
        return this.getAreaOwner().equalsIgnoreCase(playerName);
    }

    public boolean isAreaOwner(Player player) {
        return this.isAreaOwner(player.getName());
    }

    public ZoneXZ getZoneXZ() {
        return this.coordinates;
    }
}
