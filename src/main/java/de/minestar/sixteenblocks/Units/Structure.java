package de.minestar.sixteenblocks.Units;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;

import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.Settings;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Enums.EnumDirection;
import de.minestar.sixteenblocks.Manager.StructureManager;
import de.minestar.sixteenblocks.Threads.BlockCreationThread;

public class Structure {
    private HashMap<EnumDirection, ArrayList<StructureBlock>> BlockList = new HashMap<EnumDirection, ArrayList<StructureBlock>>();

    public Structure(StructureManager structureManager, String structureName) {
        ArrayList<StructureBlock> loadedBlocks = structureManager.loadStructure(structureName);
        if (loadedBlocks != null) {
            this.BlockList.put(EnumDirection.NORMAL, loadedBlocks);
            this.BlockList.put(EnumDirection.FLIP_X, this.flipX());
            this.BlockList.put(EnumDirection.FLIP_Z, this.flipZ());
            this.BlockList.put(EnumDirection.ROTATE_90, this.rotate90());
            this.BlockList.put(EnumDirection.ROTATE_180, this.rotate180());
            this.BlockList.put(EnumDirection.ROTATE_270, this.rotate270());
        } else {
            TextUtils.logWarning("Structure '" + structureName + "' could not be initialized!");
        }
    }

    public void createStructure(int zoneX, int zoneZ) {
        if (this.BlockList == null)
            return;
        World world = Bukkit.getWorlds().get(0);
        BlockCreationThread thisThread = new BlockCreationThread(world, zoneX * Settings.getAreaSizeX() - (zoneZ % 2 != 0 ? (Settings.getAreaSizeX() >> 1) : 0), zoneZ * Settings.getAreaSizeZ(), this.BlockList.get(EnumDirection.NORMAL));
        thisThread.initTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), thisThread, 0, Settings.getTicksBetweenReplace()));
    }

    public void createStructure(EnumDirection direction, int zoneX, int zoneZ) {
        if (this.BlockList == null)
            return;
        World world = Bukkit.getWorlds().get(0);
        BlockCreationThread thisThread = new BlockCreationThread(world, zoneX * Settings.getAreaSizeX() - (zoneZ % 2 != 0 ? (Settings.getAreaSizeX() >> 1) : 0), zoneZ * Settings.getAreaSizeZ(), this.BlockList.get(direction));
        thisThread.initTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), thisThread, 0, Settings.getTicksBetweenReplace()));
    }

    private ArrayList<StructureBlock> flipX(ArrayList<StructureBlock> BlockList) {
        if (BlockList == null)
            return null;
        ArrayList<StructureBlock> newList = this.cloneList(BlockList);
        for (StructureBlock block : newList) {
            block.setX(Settings.getAreaSizeX() - 1 - block.getX());
        }
        return newList;
    }

    private ArrayList<StructureBlock> flipX() {
        return this.flipX(this.BlockList.get(EnumDirection.NORMAL));
    }

    private ArrayList<StructureBlock> flipZ(ArrayList<StructureBlock> BlockList) {
        if (BlockList == null)
            return null;
        ArrayList<StructureBlock> newList = this.cloneList(BlockList);
        for (StructureBlock block : newList) {
            block.setZ(Settings.getAreaSizeZ() - 1 - block.getZ());
        }
        return newList;
    }

    private ArrayList<StructureBlock> flipZ() {
        return this.flipZ(this.BlockList.get(EnumDirection.NORMAL));
    }

    private ArrayList<StructureBlock> rotate180(ArrayList<StructureBlock> BlockList) {
        if (BlockList == null)
            return null;
        ArrayList<StructureBlock> newList = this.flipZ(BlockList);
        newList = this.flipX(newList);
        return newList;
    }

    private ArrayList<StructureBlock> rotate180() {
        return this.rotate180(this.BlockList.get(EnumDirection.NORMAL));
    }

    private ArrayList<StructureBlock> rotate270() {
        if (this.BlockList == null)
            return null;
        ArrayList<StructureBlock> newList = this.cloneList(this.BlockList.get(EnumDirection.NORMAL));
        int oldX, oldZ;
        for (StructureBlock block : newList) {
            oldX = block.getX();
            oldZ = block.getZ();
            block.setX(oldZ);
            block.setZ(Settings.getAreaSizeZ() - 1 - oldX);
        }
        return newList;
    }

    private ArrayList<StructureBlock> rotate90() {
        if (this.BlockList == null)
            return null;
        ArrayList<StructureBlock> newList = this.rotate270();
        newList = this.rotate180(newList);
        return newList;
    }

    private ArrayList<StructureBlock> cloneList(ArrayList<StructureBlock> list) {
        ArrayList<StructureBlock> clone = new ArrayList<StructureBlock>(list.size());
        for (StructureBlock item : list)
            clone.add(item.clone());
        return clone;
    }
}
