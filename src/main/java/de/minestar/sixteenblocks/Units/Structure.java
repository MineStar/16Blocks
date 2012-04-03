package de.minestar.sixteenblocks.Units;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

import de.minestar.sixteenblocks.Core.Settings;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Enums.EnumDirection;
import de.minestar.sixteenblocks.Manager.StructureManager;
import de.minestar.sixteenblocks.Threads.BlockThread;

public class Structure {
    private HashMap<EnumDirection, ArrayList<StructureBlock>> blockList = new HashMap<EnumDirection, ArrayList<StructureBlock>>();

    public Structure(StructureManager structureManager, String structureName) {
        ArrayList<StructureBlock> loadedBlocks = structureManager.loadStructure(structureName);
        if (loadedBlocks != null) {
            this.blockList.put(EnumDirection.NORMAL, loadedBlocks);
            this.blockList.put(EnumDirection.FLIP_X, this.flipX());
            this.blockList.put(EnumDirection.FLIP_Z, this.flipZ());
            this.blockList.put(EnumDirection.ROTATE_90, this.rotate90());
            this.blockList.put(EnumDirection.ROTATE_180, this.rotate180());
            this.blockList.put(EnumDirection.ROTATE_270, this.rotate270());
        } else {
            TextUtils.logWarning("Structure '" + structureName + "' could not be initialized!");
        }
    }

    public void createStructure(int zoneX, int zoneZ, BlockThread thread) {
        if (this.blockList.get(EnumDirection.NORMAL) == null)
            return;

        ZoneXZ thisZone = ZoneXZ.fromCoordinates(zoneX, zoneZ);
        thread.addRelativeBlockList(this.blockList.get(EnumDirection.NORMAL), thisZone);
    }

    public void createStructureWithSign(int zoneX, int zoneZ, Player player, BlockThread thread) {
        if (this.blockList.get(EnumDirection.NORMAL) == null)
            return;
        ZoneXZ thisZone = ZoneXZ.fromCoordinates(zoneX, zoneZ);
        thread.addRelativeBlockList(this.blockList.get(EnumDirection.NORMAL), thisZone);
    }

    public void createStructure(EnumDirection direction, int zoneX, int zoneZ, BlockThread thread) {
        if (this.blockList.get(direction) == null)
            return;
        ZoneXZ thisZone = ZoneXZ.fromCoordinates(zoneX, zoneZ);
        thread.addRelativeBlockList(this.blockList.get(direction), thisZone);
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
        return this.flipX(this.blockList.get(EnumDirection.NORMAL));
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
        return this.flipZ(this.blockList.get(EnumDirection.NORMAL));
    }

    private ArrayList<StructureBlock> rotate180(ArrayList<StructureBlock> BlockList) {
        if (BlockList == null)
            return null;
        ArrayList<StructureBlock> newList = this.flipZ(BlockList);
        newList = this.flipX(newList);
        return newList;
    }

    private ArrayList<StructureBlock> rotate180() {
        return this.rotate180(this.blockList.get(EnumDirection.NORMAL));
    }

    private ArrayList<StructureBlock> rotate270() {
        if (this.blockList == null)
            return null;
        ArrayList<StructureBlock> newList = this.cloneList(this.blockList.get(EnumDirection.NORMAL));
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
        if (this.blockList == null)
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

    public ArrayList<StructureBlock> getBlocksForExtension(int baseX, int baseZ) {
        return this.getBlocksForExtension(EnumDirection.NORMAL, baseX, baseZ);
    }

    public ArrayList<StructureBlock> getBlocksForExtension(EnumDirection direction, int baseX, int baseZ) {
        ArrayList<StructureBlock> clone = new ArrayList<StructureBlock>(this.blockList.get(direction).size());
        for (StructureBlock item : this.blockList.get(direction)) {
            clone.add(item.clone(baseX, baseZ));
        }
        return clone;
    }

    public ArrayList<StructureBlock> getBlocksForExtension(EnumDirection direction, int baseX, int baseY, int baseZ) {
        ArrayList<StructureBlock> clone = new ArrayList<StructureBlock>(this.blockList.get(direction).size());
        for (StructureBlock item : this.blockList.get(direction)) {
            clone.add(item.clone(baseX, baseY, baseZ));
        }
        return clone;
    }
}
