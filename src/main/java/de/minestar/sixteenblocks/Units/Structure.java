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
    private ArrayList<StructureBlock> BlockSet = null;

    public Structure(AreaManager areaManager, String structureName) {
        ArrayList<StructureBlock> loadedBlocks = areaManager.loadStructure(structureName);
        if (loadedBlocks != null) {
            this.BlockSet = loadedBlocks;
        } else {
            TextUtils.logWarning("Structure '" + structureName + "' could not be initialized!");
        }
    }

    public void createStructure(int zoneX, int zoneZ) {
        if (this.BlockSet == null)
            return;
        World world = Bukkit.getWorlds().get(0);
        BlockCreationThread thisThread = new BlockCreationThread(world, zoneX * Settings.getAreaSizeX() - (zoneZ % 2 != 0 ? (Settings.getAreaSizeX() >> 1) : 0), zoneZ * Settings.getAreaSizeZ(), this.BlockSet);
        thisThread.initTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), thisThread, 0, Settings.getTicksBetweenReplace()));
    }

    protected void addBlock(int x, int y, int z, int TypeID) {
        this.addBlock(new StructureBlock(x, y, z, TypeID));
    }

    protected void addBlock(int x, int y, int z, int TypeID, byte SubID) {
        this.addBlock(new StructureBlock(x, y, z, TypeID, SubID));
    }

    protected void addBlock(StructureBlock block) {
        this.BlockSet.add(block);
    }
}
