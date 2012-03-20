package de.minestar.sixteenblocks.Manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

import net.minecraft.server.Packet130UpdateSign;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import de.minestar.sixteenblocks.Core.Settings;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Enums.EnumDirection;
import de.minestar.sixteenblocks.Enums.EnumStructures;
import de.minestar.sixteenblocks.Units.ZoneXZ;

public class AreaManager {
    private TreeMap<String, SkinArea> usedAreaList = new TreeMap<String, SkinArea>();
    private TreeMap<String, SkinArea> unusedAreaList = new TreeMap<String, SkinArea>();

    private HashSet<String> blockedAreas = new HashSet<String>();

    private StructureManager structureManager;
    private AreaDatabaseManager databaseManager;
    private WorldManager worldManager;

    private int lastRow = 0;

    // ////////////////////////////////////////////////
    //
    // CONSTRUCTOR
    //
    // ////////////////////////////////////////////////

    public AreaManager(AreaDatabaseManager databaseManager, WorldManager worldManager, StructureManager structureManager) {
        this.worldManager = worldManager;
        this.databaseManager = databaseManager;
        this.structureManager = structureManager;
        this.loadAreas();
        this.initMaximumZ();
        this.checkForZoneExtesion();
    }

    // ////////////////////////////////////////////////
    //
    // AREA CREATION
    //
    // ////////////////////////////////////////////////

    public void createRow(int z) {
        for (int x = -Settings.getSkinsRight() + (z % 2 == 0 ? 0 : 1); x <= Settings.getSkinsLeft(); x++) {
            this.createUnusedArea(new SkinArea(x, z, ""), true);
            if (z % 2 != 0) {
                this.structureManager.getStructure(EnumStructures.ZONE_STREETS_BACK).createStructure(x, z - 1);
                this.structureManager.getStructure(EnumStructures.ZONE_STREETS_BACK).createStructure(x - 1, z - 1);
            } else {
                if (x > -Settings.getSkinsRight()) {
                    this.structureManager.getStructure(EnumStructures.ZONE_STREETS_BACK).createStructure(x, z - 1);
                }
            }
        }
        if (z == 0) {
            this.structureManager.getStructure(EnumStructures.STREETS_CORNER).createStructure(-Settings.getSkinsRight(), z - 1);
            this.structureManager.getStructure(EnumStructures.STREETS_CORNER).createStructure(EnumDirection.FLIP_X, Settings.getSkinsLeft() + 1, z - 1);
        } else if (z % 2 != 0) {
            this.structureManager.getStructure(EnumStructures.STREETS_SIDE_1).createStructure(-Settings.getSkinsRight() - 1, z - 1);
            this.structureManager.getStructure(EnumStructures.STREETS_SIDE_1).createStructure(EnumDirection.FLIP_X, Settings.getSkinsLeft() + 1, z - 1);
        } else {
            this.structureManager.getStructure(EnumStructures.STREETS_SIDE_2).createStructure(-Settings.getSkinsRight(), z - 1);
            this.structureManager.getStructure(EnumStructures.STREETS_SIDE_2).createStructure(EnumDirection.FLIP_X, Settings.getSkinsLeft() + 1, z - 1);
        }
    }

    // ////////////////////////////////////////////////
    //
    // PERSISTENCE
    //
    // ////////////////////////////////////////////////

    private void loadAreas() {
        ArrayList<SkinArea> loadedAreas = databaseManager.loadZones();
        for (SkinArea thisArea : loadedAreas) {
            if (thisArea.getAreaOwner().equalsIgnoreCase("")) {
                this.unusedAreaList.put(thisArea.getZoneXZ().toString(), thisArea);
            } else {
                this.usedAreaList.put(thisArea.getZoneXZ().toString(), thisArea);
            }
        }
        TextUtils.logInfo(this.unusedAreaList.size() + " unused Areas loaded.");
        TextUtils.logInfo(this.usedAreaList.size() + " used Areas loaded.");
    }

    private void saveArea(SkinArea thisArea) {
        this.databaseManager.saveZone(thisArea);
    }

    private void updateAreaOwner(SkinArea thisArea) {
        this.databaseManager.updateAreaOwner(thisArea);
    }

    // ////////////////////////////////////////////////
    //
    // BLOCK AREAS
    //
    // ////////////////////////////////////////////////

    public void blockArea(ZoneXZ thisZone) {
        this.blockArea(thisZone.toString());
    }

    public void unblockArea(ZoneXZ thisZone) {
        this.unblockArea(thisZone.toString());
    }

    public boolean isAreaBlocked(ZoneXZ thisZone) {
        return this.isAreaBlocked(thisZone.toString());
    }

    public void blockArea(String zoneString) {
        this.blockedAreas.add(zoneString);
    }

    public void unblockArea(String zoneString) {
        this.blockedAreas.remove(zoneString);
    }

    public boolean isAreaBlocked(String zoneString) {
        return this.blockedAreas.contains(zoneString);
    }

    // ////////////////////////////////////////////////
    //
    // MODIFY & GET AREAS
    //
    // ////////////////////////////////////////////////

    public boolean createUnusedArea(SkinArea skinArea, boolean createStructures) {
        if (this.containsUnusedArea(skinArea.getZoneXZ()))
            return false;
        // UPDATE DATABASE
        if (!this.usedAreaList.containsKey(skinArea.getZoneXZ().toString())) {
            this.saveArea(skinArea);
        } else {
            this.updateAreaOwner(skinArea);
        }
        // UPDATE LISTS
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

    public boolean hasPlayerArea(Player player) {
        for (SkinArea thisArea : this.usedAreaList.values()) {
            if (thisArea.isAreaOwner(player)) {
                return true;
            }
        }
        return false;
    }

    public SkinArea getPlayerArea(Player player) {
        return this.getPlayerArea(player.getName());
    }

    public SkinArea getPlayerArea(String playerName) {
        for (SkinArea thisArea : this.usedAreaList.values()) {
            if (thisArea.isAreaOwner(playerName)) {
                return thisArea;
            }
        }

        // SEARCH FOR THE BEGINNING OF THE NAME
        for (SkinArea thisArea : this.usedAreaList.values()) {
            if (thisArea.getAreaOwner().toLowerCase().startsWith(playerName.toLowerCase())) {
                return thisArea;
            }
        }

        return null;
    }
    public SkinArea getPlayerArea(ZoneXZ thisZone) {
        return this.usedAreaList.get(thisZone.toString());
    }

    public boolean containsPlayerArea(ZoneXZ thisZone) {
        return this.containsPlayerArea(thisZone.toString());
    }

    public boolean containsPlayerArea(String coordinateString) {
        return this.usedAreaList.containsKey(coordinateString);
    }

    public boolean createPlayerArea(SkinArea skinArea, boolean createStructures, Player player) {
        if (this.containsPlayerArea(skinArea.getZoneXZ()))
            return false;

        // UPDATE DATABASE
        if (!this.unusedAreaList.containsKey(skinArea.getZoneXZ().toString())) {
            this.saveArea(skinArea);
        } else {
            this.updateAreaOwner(skinArea);
        }

        // UPDATE LISTS
        this.usedAreaList.put(skinArea.getZoneXZ().toString(), skinArea);
        this.unusedAreaList.remove(skinArea.getZoneXZ().toString());
        if (createStructures) {
            this.structureManager.getStructure(EnumStructures.ZONE_STEVE).createStructureWithSign(skinArea.getZoneXZ().getX(), skinArea.getZoneXZ().getZ(), player);
        }

        this.checkForZoneExtesion();
        return true;
    }

    public void createPlayerSign(Player player, ZoneXZ thisZone) {
        World world = Bukkit.getWorlds().get(0);

        // PLACE SIGN
        int x = thisZone.getX() * Settings.getAreaSizeX() - (thisZone.getZ() % 2 != 0 ? (Settings.getAreaSizeX() >> 1) : 0) + (Settings.getAreaSizeX() >> 1);
        int z = thisZone.getZ() * Settings.getAreaSizeZ() + 12;
        world.getBlockAt(x, Settings.getMinimumBuildY() - 1, z).setTypeIdAndData(Material.WALL_SIGN.getId(), (byte) 0x2, true);

        // UPDATE LINE
        Sign signBlock = (Sign) (world.getBlockAt(x, Settings.getMinimumBuildY() - 1, z).getState());
        String[] playerName = new String[3];

        signBlock.setLine(0, "Built by:");
        playerName[0] = player.getName();
        playerName[1] = "";
        playerName[2] = "";
        if (player.getName().length() > 15) {
            playerName[0] = player.getName().substring(0, 15);
            if (player.getName().length() > 30) {
                playerName[1] = player.getName().substring(15, 30);
                if (player.getName().length() > 45) {
                    playerName[2] = player.getName().substring(30, 45);
                } else {
                    playerName[2] = player.getName().substring(30);
                }
            } else {
                playerName[1] = player.getName().substring(15);
            }
        }

        signBlock.setLine(1, playerName[0]);
        signBlock.setLine(2, playerName[1]);
        signBlock.setLine(3, playerName[2]);
        signBlock.update(true);

        // SEND UPDATE => NEED HELP OF ORIGINAL MC-SERVERSOFTWARE
        CraftPlayer cPlayer = (CraftPlayer) player;
        Packet130UpdateSign signPacket = null;
        signPacket = new Packet130UpdateSign(signBlock.getX(), signBlock.getY(), signBlock.getZ(), signBlock.getLines());
        cPlayer.getHandle().netServerHandler.sendPacket(signPacket);
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
        if (y < Settings.getMinimumBuildY() || y > Settings.getMaximumBuildY())
            return false;

        ZoneXZ thisZone = ZoneXZ.fromPoint(x, z);
        SkinArea thisArea = this.getPlayerArea(thisZone);
        if (thisArea == null) {
            return false;
        }
        return thisArea.isAreaOwner(player);
    }

    public void checkForZoneExtesion() {
        if (this.unusedAreaList.size() <= (Settings.getSkinsLeft() + Settings.getSkinsRight()) * Settings.getCreateRowsAtOnce()) {
            while (true) {
                if (this.unusedAreaList.containsKey(lastRow + ":0") || this.usedAreaList.containsKey(lastRow + ":0")) {
                    ++lastRow;
                    this.worldManager.setMaxZ((lastRow + 1) * (Settings.getAreaSizeZ() + 1));
                    continue;
                } else {
                    for (int i = 0; i < Settings.getCreateRowsAtOnce(); i++) {
                        this.createRow(lastRow + i);
                    }
                    this.worldManager.setMaxZ((lastRow + Settings.getCreateRowsAtOnce()) * (Settings.getAreaSizeZ()));
                    return;
                }
            }
        }
    }

    private void initMaximumZ() {
        int i = 0;
        while (true) {
            if (this.unusedAreaList.containsKey(i + ":0") || this.usedAreaList.containsKey(i + ":0")) {
                ++i;
                this.worldManager.setMaxZ((i + 1) * (Settings.getAreaSizeZ() + 1));
                continue;
            } else {
                return;
            }
        }
    }

    public SkinArea getRandomUnusedArea() {
        return (SkinArea) this.unusedAreaList.values().toArray()[0];
    }
}
