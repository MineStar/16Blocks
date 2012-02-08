package de.minestar.sixteenblocks.Threads;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.World;

import de.minestar.sixteenblocks.Units.StructureBlock;
import de.minestar.sixteenblocks.core.Settings;

public class BlockCreationThread implements Runnable {

    private final ArrayList<StructureBlock> blockList;
    private final World world;
    private final int baseX, baseY, baseZ;
    private int TaskID = -9999;
    private int counter = 0;

    public BlockCreationThread(World world, int baseX, int baseY, int baseZ, ArrayList<StructureBlock> blockList) {
        this.world = world;
        this.baseX = baseX;
        this.baseY = baseY;
        this.baseZ = baseZ;
        this.blockList = blockList;
    }

    public void initTask(int TaskID) {
        this.TaskID = TaskID;
    }

    @Override
    public void run() {
        if (TaskID == -9999)
            return;

        StructureBlock thisBlock = null;
        for (int i = 0; i < Settings.getMaxBlockxReplaceAtOnce(); i++) {
            thisBlock = blockList.get(counter);
            world.getBlockAt(baseX + thisBlock.getX(), baseY + thisBlock.getY(), baseZ + thisBlock.getZ()).setTypeIdAndData(thisBlock.getTypeID(), thisBlock.getSubID(), false);
            counter++;
            if (counter >= blockList.size()) {
                Bukkit.getScheduler().cancelTask(this.TaskID);
                break;
            }
        }
        thisBlock = null;
    }
}
