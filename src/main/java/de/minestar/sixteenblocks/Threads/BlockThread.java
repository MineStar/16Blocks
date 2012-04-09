package de.minestar.sixteenblocks.Threads;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.Bukkit;
import org.bukkit.World;

import de.minestar.minestarlibrary.utils.ConsoleUtils;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.Settings;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Units.StructureBlock;
import de.minestar.sixteenblocks.Units.ZoneXZ;

public class BlockThread implements Runnable {

    private final ConcurrentLinkedQueue<StructureBlock> queue = new ConcurrentLinkedQueue<StructureBlock>();
    private final World world;
    private int TaskID = -9999;

    public BlockThread(World world) {
        this.world = world;
    }

    public void addBlockList(final List<StructureBlock> extendList) {
        this.queue.addAll(extendList);
    }

    public void addRelativeBlockList(final List<StructureBlock> extendList, ZoneXZ thisZone) {
        for (StructureBlock block : extendList) {
            this.queue.add(block.clone(thisZone.getBaseX(), thisZone.getBaseZ()));
        }
    }

    public void initTask(int TaskID) {
        this.TaskID = TaskID;
    }

    public boolean isRunning() {
        return !this.queue.isEmpty();
    }

    @Override
    public void run() {
        if (TaskID == -9999)
            return;

        if (this.queue.isEmpty()) {
            // CHECK FOR RELOAD / STOP
            if (Core.shutdownServer) {
                if (!Core.isShutDown) {
                    // RELOAD
                    Core.shutdownServer = false;
                    Core.isShutDown = false;
                    Bukkit.broadcastMessage("Saving world and reloading server!");
                    Bukkit.reload();
                    Bukkit.broadcastMessage("Reload finished!");
                } else {
                    // SHUTDOWN
                    Bukkit.broadcastMessage("Saving world and stopping server!");
//                    Bukkit.savePlayers();
//                    Bukkit.getWorlds().get(0).save();
                    Bukkit.shutdown();
                }
            }
            return;
        }

        StructureBlock thisBlock = null;

        int maxPlace = Settings.getMaxBlocksReplaceAtOnce();
        // IF SERVER IS IN SHUTDOWN-MODE: REPLACE 50.000 BLOCKS AT ONCE
        if (Core.shutdownServer) {
            maxPlace = 50000;
            ConsoleUtils.printInfo(Core.NAME, "Queue size = " + queue.size());
        }

        // RUN THE QUEUE
        for (int i = 0; i < maxPlace && !queue.isEmpty(); ++i) {
            thisBlock = queue.remove();
            world.getBlockAt(thisBlock.getX(), thisBlock.getY(), thisBlock.getZ()).setTypeIdAndData(thisBlock.getTypeID(), thisBlock.getSubID(), false);
        }
    }
}
