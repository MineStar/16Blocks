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

import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.Settings;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Enums.EnumDirection;
import de.minestar.sixteenblocks.Enums.EnumStructures;
import de.minestar.sixteenblocks.Threads.AreaDeletionThread;
import de.minestar.sixteenblocks.Threads.SuperBlockCreationThread;
import de.minestar.sixteenblocks.Units.StructureBlock;
import de.minestar.sixteenblocks.Units.ZoneXZ;

public class AreaManager {
    private TreeMap<String, SkinArea> usedAreaList = new TreeMap<String, SkinArea>();
    private TreeMap<String, SkinArea> unusedAreaList = new TreeMap<String, SkinArea>();

    private HashSet<String> blockedAreas = new HashSet<String>();

    private StructureManager structureManager;
    private AreaDatabaseManager databaseManager;
    private WorldManager worldManager;

    private int lastRow = 0;

    private int runningThreads = 0;

    // ////////////////////////////////////////////////
    //
    // CONSTRUCTOR
    //
    // ////////////////////////////////////////////////

    public AreaManager(AreaDatabaseManager databaseManager, WorldManager worldManager, StructureManager structureManager) {
        this.worldManager = worldManager;
        this.databaseManager = databaseManager;
        this.structureManager = structureManager;
    }

    public void init() {
        this.createNotExistingAreas();
        this.loadAreas();
        this.initMaximumZ();
        this.checkForZoneExtension();
    }

    public ArrayList<StructureBlock> getChangedBlocks(ZoneXZ thisZone) {
        ArrayList<StructureBlock> blockList = new ArrayList<StructureBlock>();
        int baseX = thisZone.getBaseX();
        int baseZ = thisZone.getBaseZ();
        World world = Bukkit.getWorlds().get(0);
        for (int y = Settings.getMaximumBuildY(); y >= Settings.getMinimumBuildY(); y--) {
            for (int x = 0; x < Settings.getAreaSizeX(); x++) {
                for (int z = 0; z < Settings.getAreaSizeZ(); z++) {
                    if (world.getBlockTypeIdAt(baseX + x, y, baseZ + z) != Material.AIR.getId()) {
                        blockList.add(new StructureBlock(x, y, z, 0));
                    }
                }
            }
        }
        return blockList;
    }

    private void createNotExistingAreas() {
        if (Settings.getSkinsLeft() == Settings.getSkinsLeftOld() && Settings.getSkinsRight() == Settings.getSkinsRightOld())
            return;

        ArrayList<SkinArea> newSkins = this.databaseManager.createNotExistingAreas();
        System.out.println("Create new areas: " + newSkins.size());
        ZoneXZ thisZone;
        int maxZ = Integer.MIN_VALUE;
        int currentRow = -1;
        World thisWorld = Bukkit.getWorlds().get(0);
        for (SkinArea thisArea : newSkins) {
            thisZone = thisArea.getZoneXZ();

            if (maxZ < thisZone.getZ())
                maxZ = thisZone.getZ();

            if (currentRow != thisZone.getZ()) {
                int baseXLeft = ZoneXZ.getBaseX(Settings.getSkinsLeftOld() + 1, thisZone.getZ());
                int baseXRight = ZoneXZ.getBaseX(-(Settings.getSkinsRightOld()), thisZone.getZ()) - 1;

                if (thisZone.getZ() % 2 != 0) {
                    baseXLeft -= (Settings.getAreaSizeX());
                }

                // REBASE ZONES
                int baseZ = thisZone.getBaseZ();
                Block thisBlock;
                for (int z = 0; z < Settings.getAreaSizeZ(); z++) {
                    for (int x = 0; x < Settings.getAreaSizeX() * 2; x++) {
                        thisWorld.getBlockAt(baseXLeft + x, 2, baseZ + z).setTypeId(Material.DIRT.getId(), false);
                        thisWorld.getBlockAt(baseXLeft + x, 3, baseZ + z).setTypeId(Material.GRASS.getId(), false);
                        thisBlock = thisWorld.getBlockAt(baseXLeft + x, 4, baseZ + z);
                        if (thisBlock.getTypeId() == 44) {
                            thisBlock.setTypeId(Material.AIR.getId(), false);
                        }

                        thisWorld.getBlockAt(baseXRight - x, 2, baseZ + z).setTypeId(Material.DIRT.getId(), false);
                        thisWorld.getBlockAt(baseXRight - x, 3, baseZ + z).setTypeId(Material.GRASS.getId(), false);
                        thisBlock = thisWorld.getBlockAt(baseXRight - x, 4, baseZ + z);
                        if (thisBlock.getTypeId() == 44) {
                            thisBlock.setTypeId(Material.AIR.getId(), false);
                        }
                    }
                }
                currentRow = thisZone.getZ();
                System.out.println("Current row: " + currentRow);
            }
            this.createSingleZone(thisZone);
        }
    }

    public void incrementThreads() {
        this.runningThreads++;
    }

    public void decrementThreads() {
        this.runningThreads--;
        if (this.runningThreads < 0)
            this.runningThreads = 0;
    }

    public int getRunningThreadCount() {
        return this.runningThreads;
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

            this.structureManager.getStructure(EnumStructures.INFO_WALL_1).createStructure(1, z - 1);
            this.structureManager.getStructure(EnumStructures.INFO_WALL_2).createStructure(0, z - 1);

        } else if (z % 2 != 0) {
            this.structureManager.getStructure(EnumStructures.STREETS_SIDE_1).createStructure(-Settings.getSkinsRight() - 1, z - 1);
            this.structureManager.getStructure(EnumStructures.STREETS_SIDE_1).createStructure(EnumDirection.FLIP_X, Settings.getSkinsLeft() + 1, z - 1);
        } else {
            this.structureManager.getStructure(EnumStructures.STREETS_SIDE_2).createStructure(-Settings.getSkinsRight(), z - 1);
            this.structureManager.getStructure(EnumStructures.STREETS_SIDE_2).createStructure(EnumDirection.FLIP_X, Settings.getSkinsLeft() + 1, z - 1);
        }
    }

    public void createSingleZone(ZoneXZ thisZone) {
        SuperBlockCreationThread extendThread = Core.getInstance().getExtendThread();

        int x = thisZone.getX();
        int row = thisZone.getZ();
        int baseX = thisZone.getBaseX();
        if (row % 2 != 0) {
            baseX -= Settings.getAreaSizeX();
        }
        int baseZ = thisZone.getBaseZ();

        // CREATE BACK-STREETS
        if (row % 2 != 0) {
            // UNEVEN ROWS
            extendThread.addBlockList(this.structureManager.getStructure(EnumStructures.ZONE_STREETS_AND_SOCKET).getBlocksForExtension(baseX + Settings.getAreaSizeX(), baseZ));

            extendThread.addBlockList(this.structureManager.getStructure(EnumStructures.ZONE_STREETS_BACK).getBlocksForExtension(ZoneXZ.getBaseX(x - 1, row - 1), ZoneXZ.getBaseZ(row - 1)));
            if (thisZone.getX() >= Settings.getSkinsLeft()) {
                extendThread.addBlockList(this.structureManager.getStructure(EnumStructures.ZONE_STREETS_BACK).getBlocksForExtension(ZoneXZ.getBaseX(x, row - 1), ZoneXZ.getBaseZ(row - 1)));
            }
        } else {
            // EVEN ROWS
            extendThread.addBlockList(this.structureManager.getStructure(EnumStructures.ZONE_STREETS_AND_SOCKET).getBlocksForExtension(baseX, baseZ));

            if (row != 0 || (thisZone.getX() >= -Settings.getSkinsRight())) {
                extendThread.addBlockList(this.structureManager.getStructure(EnumStructures.ZONE_STREETS_BACK).getBlocksForExtension(baseX, baseZ - Settings.getAreaSizeZ()));
            }
        }

        // CREATE SIDE-STREETS
        if (row == 0) {
            // ROW 0
            extendThread.addBlockList(this.structureManager.getStructure(EnumStructures.STREETS_CORNER).getBlocksForExtension(ZoneXZ.getBaseX(-Settings.getSkinsRight() - 1, row - 1), ZoneXZ.getBaseZ(row - 1)));
            extendThread.addBlockList(this.structureManager.getStructure(EnumStructures.STREETS_CORNER).getBlocksForExtension(EnumDirection.FLIP_X, ZoneXZ.getBaseX(Settings.getSkinsLeft(), row - 1), ZoneXZ.getBaseZ(row - 1)));
        }

        if (row % 2 != 0) {
            // UNEVEN ROWS
            extendThread.addBlockList(this.structureManager.getStructure(EnumStructures.STREETS_SIDE_2).getBlocksForExtension(ZoneXZ.getBaseX(-Settings.getSkinsRight() - 1, row), ZoneXZ.getBaseZ(row)));
            extendThread.addBlockList(this.structureManager.getStructure(EnumStructures.STREETS_SIDE_2).getBlocksForExtension(EnumDirection.FLIP_X, ZoneXZ.getBaseX(Settings.getSkinsLeft(), row), ZoneXZ.getBaseZ(row)));
            // FIX WRONG BLOCKS
            if (x == -Settings.getSkinsRightOld()) {
                int extendX = ZoneXZ.getBaseX(x, row - 1);
                int extendZ = ZoneXZ.getBaseZ(row);
                extendThread.addBlockList(this.structureManager.getStructure(EnumStructures.ZONE_STREETS_BACK).getBlocksForExtension(extendX, extendZ));
            }
            if (x - 1 == Settings.getSkinsLeftOld()) {
                extendThread.addBlockList(this.structureManager.getStructure(EnumStructures.ZONE_STREETS_BACK).getBlocksForExtension(ZoneXZ.getBaseX(x - 1, row - 1), ZoneXZ.getBaseZ(row)));
            }

        } else {
            // EVEN ROWS
            extendThread.addBlockList(this.structureManager.getStructure(EnumStructures.STREETS_SIDE_1).getBlocksForExtension(ZoneXZ.getBaseX(-Settings.getSkinsRight() - 1, row), ZoneXZ.getBaseZ(row)));
            extendThread.addBlockList(this.structureManager.getStructure(EnumStructures.STREETS_SIDE_1).getBlocksForExtension(EnumDirection.FLIP_X, ZoneXZ.getBaseX(Settings.getSkinsLeft() + 1, row), ZoneXZ.getBaseZ(row)));
        }
    }

    public void createRowStructures(int row) {
        for (int x = -Settings.getSkinsRight() + (row % 2 == 0 ? 0 : 1); x <= Settings.getSkinsLeft(); x++) {
            if (row % 2 != 0) {
                this.structureManager.getStructure(EnumStructures.ZONE_STREETS_BACK).createStructure(x, row - 1);
                this.structureManager.getStructure(EnumStructures.ZONE_STREETS_BACK).createStructure(x - 1, row - 1);
            } else {
                if (x > -Settings.getSkinsRight()) {
                    this.structureManager.getStructure(EnumStructures.ZONE_STREETS_BACK).createStructure(x, row - 1);
                }
            }
            this.structureManager.getStructure(EnumStructures.ZONE_STREETS_AND_SOCKET).createStructure(x, row);
        }

        if (row == 0) {
            this.structureManager.getStructure(EnumStructures.STREETS_CORNER).createStructure(-Settings.getSkinsRight(), row - 1);
            this.structureManager.getStructure(EnumStructures.STREETS_CORNER).createStructure(EnumDirection.FLIP_X, Settings.getSkinsLeft() + 1, row - 1);

            this.structureManager.getStructure(EnumStructures.INFO_WALL_1).createStructure(1, row - 1);
            this.structureManager.getStructure(EnumStructures.INFO_WALL_2).createStructure(0, row - 1);

        } else if (row % 2 != 0) {
            this.structureManager.getStructure(EnumStructures.STREETS_SIDE_1).createStructure(-Settings.getSkinsRight() - 1, row - 1);
            this.structureManager.getStructure(EnumStructures.STREETS_SIDE_1).createStructure(EnumDirection.FLIP_X, Settings.getSkinsLeft() + 1, row - 1);
        } else {
            this.structureManager.getStructure(EnumStructures.STREETS_SIDE_2).createStructure(-Settings.getSkinsRight(), row - 1);
            this.structureManager.getStructure(EnumStructures.STREETS_SIDE_2).createStructure(EnumDirection.FLIP_X, Settings.getSkinsLeft() + 1, row - 1);
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

    public int getUsedAreaCount() {
        return usedAreaList.size();
    }

    // ///////////////////////////
    // DELETE AREA
    // ///////////////////////////

    public void deletePlayerArea(SkinArea thisArea, Player player) {
        // BLOCK AREA
        this.blockArea(thisArea.getZoneXZ());
        // CREATE THREAD AND START IT
        World world = Bukkit.getWorlds().get(0);
        ZoneXZ thisZone = thisArea.getZoneXZ();
        AreaDeletionThread thisThread = new AreaDeletionThread(world, thisZone.getX() * Settings.getAreaSizeX() - (thisZone.getZ() % 2 != 0 ? (Settings.getAreaSizeX() >> 1) : 0), thisZone.getZ() * Settings.getAreaSizeZ(), thisZone, player.getName());
        thisThread.initTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), thisThread, 0, Settings.getTicksBetweenReplace()));
    }

    // ///////////////////////////
    // USED AREAS
    // ///////////////////////////

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

    public SkinArea getExactPlayerArea(String playerName) {
        for (SkinArea thisArea : this.usedAreaList.values()) {
            if (thisArea.isAreaOwner(playerName)) {
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

        this.checkForZoneExtension();
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
        this.databaseManager.deleteAreaOwner(thisZone);
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

    public void checkForZoneExtension() {
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
        ZoneXZ thisZone;
        int minX = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
        for (SkinArea thisArea : this.unusedAreaList.values()) {
            thisZone = thisArea.getZoneXZ();
            if (!this.isAreaBlocked(thisZone)) {
                if (thisZone.getZ() <= minZ) {
                    if (thisZone.getZ() < minZ)
                        minX = Integer.MAX_VALUE;
                    minZ = thisZone.getZ();
                    if (thisZone.getX() < minX)
                        minX = thisZone.getX();
                }
            }
        }

        if (this.unusedAreaList.containsKey(minZ + ":" + minX))
            return this.unusedAreaList.get(minZ + ":" + minX);
        else
            return (SkinArea) this.unusedAreaList.values().toArray()[0];
    }

    /**
     * @return the lastRow
     */
    public int getLastRow() {
        int last = Integer.MIN_VALUE;

        // UNUSED AREAS
        for (SkinArea thisArea : this.unusedAreaList.values()) {
            if (thisArea.getZoneXZ().getZ() > last)
                last = thisArea.getZoneXZ().getZ();
        }

        // USED AREAS
        for (SkinArea thisArea : this.usedAreaList.values()) {
            if (thisArea.getZoneXZ().getZ() > last)
                last = thisArea.getZoneXZ().getZ();
        }

        return last;
    }
}
