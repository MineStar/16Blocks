package de.minestar.sixteenblocks.Threads;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.World;

import de.minestar.sixteenblocks.Core.Settings;
import de.minestar.sixteenblocks.Units.StructureBlock;

public class SuperBlockCreationThread implements Runnable {

    private final LinkedList<StructureBlock> blockList = new LinkedList<StructureBlock>();
    private final World world;
    private int TaskID = -9999;

    public SuperBlockCreationThread(World world) {
        this.world = world;
    }

    public void addBlockList(final List<StructureBlock> extendList) {
        synchronized (this.blockList) {
            this.blockList.addAll(extendList);
        }
    }

    public void initTask(int TaskID) {
        this.TaskID = TaskID;
    }

    @Override
    public void run() {
        if (TaskID == -9999)
            return;

        if (this.blockList.isEmpty())
            return;

        synchronized (this.blockList) {
            StructureBlock thisBlock = null;
            int counter = 0;
            while (!blockList.isEmpty()) {
                thisBlock = blockList.removeFirst();
                world.getBlockAt(thisBlock.getX(), thisBlock.getY(), thisBlock.getZ()).setTypeIdAndData(thisBlock.getTypeID(), thisBlock.getSubID(), false);

                counter++;
                if (counter >= Settings.getMaxBlockxReplaceAtOnce()) {
                    return;
                }
            }
        }
        System.out.println("---------------------");
        System.out.println("EXTENSION READY!");
        System.out.println("---------------------");
    }
}
