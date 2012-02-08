package de.minestar.sixteenblocks.Units;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.World;

import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.Settings;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Manager.AreaManager;
import de.minestar.sixteenblocks.Threads.BlockCreationThread;

public class Structure {
    private ArrayList<StructureBlock> BlockList = null;

    public Structure(AreaManager areaManager, String structureName) {
        ArrayList<StructureBlock> loadedBlocks = areaManager.loadStructure(structureName);
        if (loadedBlocks != null) {
            this.BlockList = loadedBlocks;
        } else {
            TextUtils.logWarning("Structure '" + structureName + "' could not be initialized!");
        }
    }

    public void createStructure(int zoneX, int zoneZ) {
        if (this.BlockList == null)
            return;
        World world = Bukkit.getWorlds().get(0);
        BlockCreationThread thisThread = new BlockCreationThread(world, zoneX * Settings.getAreaSizeX() - (zoneZ % 2 != 0 ? (Settings.getAreaSizeX() >> 1) : 0), zoneZ * Settings.getAreaSizeZ(), this.BlockList);
        thisThread.initTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), thisThread, 0, Settings.getTicksBetweenReplace()));
    }

    public void createStructure(ArrayList<StructureBlock> BlockList, int zoneX, int zoneZ) {
        if (BlockList == null)
            return;
        World world = Bukkit.getWorlds().get(0);
        BlockCreationThread thisThread = new BlockCreationThread(world, zoneX * Settings.getAreaSizeX() - (zoneZ % 2 != 0 ? (Settings.getAreaSizeX() >> 1) : 0), zoneZ * Settings.getAreaSizeZ(), BlockList);
        thisThread.initTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), thisThread, 0, Settings.getTicksBetweenReplace()));
    }

    public ArrayList<StructureBlock> flipX(ArrayList<StructureBlock> BlockList) {
        if (BlockList == null)
            return null;
        ArrayList<StructureBlock> newList = this.cloneList(BlockList);
        for (StructureBlock block : newList) {
            block.setX(Settings.getAreaSizeX() - 1 - block.getX());
        }
        return newList;
    }

    public ArrayList<StructureBlock> flipX() {
        return this.flipX(this.BlockList);
    }

    public ArrayList<StructureBlock> flipZ(ArrayList<StructureBlock> BlockList) {
        if (BlockList == null)
            return null;
        ArrayList<StructureBlock> newList = this.cloneList(BlockList);
        for (StructureBlock block : newList) {
            block.setZ(Settings.getAreaSizeZ() - 1 - block.getZ());
        }
        return newList;
    }

    public ArrayList<StructureBlock> flipZ() {
        return this.flipZ(this.BlockList);
    }

    public ArrayList<StructureBlock> rotate180(ArrayList<StructureBlock> BlockList) {
        if (BlockList == null)
            return null;
        ArrayList<StructureBlock> newList = this.flipZ(BlockList);
        newList = this.flipX(newList);
        return newList;
    }

    public ArrayList<StructureBlock> rotate180() {
        return this.rotate180(this.BlockList);
    }

    public ArrayList<StructureBlock> rotate270() {
        if (this.BlockList == null)
            return null;
        ArrayList<StructureBlock> newList = this.cloneList(this.BlockList);
        int oldX, oldZ;
        for (StructureBlock block : newList) {
            oldX = block.getX();
            oldZ = block.getZ();
            block.setX(oldZ);
            block.setZ(Settings.getAreaSizeZ() - 1 - oldX);
        }
        return newList;
    }

    public ArrayList<StructureBlock> rotate90() {
        if (this.BlockList == null)
            return null;
        ArrayList<StructureBlock> newList = this.rotate270();
        newList = this.rotate180(newList);
        return newList;
    }

    protected void addBlock(int x, int y, int z, int TypeID) {
        this.addBlock(new StructureBlock(x, y, z, TypeID));
    }

    protected void addBlock(int x, int y, int z, int TypeID, byte SubID) {
        this.addBlock(new StructureBlock(x, y, z, TypeID, SubID));
    }

    protected void addBlock(StructureBlock block) {
        this.BlockList.add(block);
    }

    public ArrayList<StructureBlock> getBlockList() {
        return BlockList;
    }

    private ArrayList<StructureBlock> cloneList(ArrayList<StructureBlock> list) {
        ArrayList<StructureBlock> clone = new ArrayList<StructureBlock>(list.size());
        for (StructureBlock item : list)
            clone.add(item.clone());
        return clone;
    }

}
