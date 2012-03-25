package de.minestar.sixteenblocks.Threads;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.Settings;
import de.minestar.sixteenblocks.Units.StructureBlock;
import de.minestar.sixteenblocks.Units.ZoneXZ;

public class BlockCreationThread implements Runnable {

    private final ArrayList<StructureBlock> blockList;
    private final World world;
    private final int baseX, baseZ;
    private int TaskID = -9999;
    private int counter = 0;

    public boolean createSign = false;
    private Player player = null;

    public BlockCreationThread(World world, int baseX, int baseZ, ArrayList<StructureBlock> blockList) {
        this.world = world;
        this.baseX = baseX;
        this.baseZ = baseZ;
        this.blockList = blockList;
    }

    public void createSign(Player player) {
        this.createSign = true;
        this.player = player;
    }

    public void initTask(int TaskID) {
        this.TaskID = TaskID;
        Core.getInstance().getAreaManager().incrementThreads();
    }

    @Override
    public void run() {
        if (TaskID == -9999)
            return;

        StructureBlock thisBlock = null;
        for (int i = 0; i < Settings.getMaxBlockxReplaceAtOnce(); i++) {
            thisBlock = blockList.get(counter);
            world.getBlockAt(baseX + thisBlock.getX(), thisBlock.getY(), baseZ + thisBlock.getZ()).setTypeIdAndData(thisBlock.getTypeID(), thisBlock.getSubID(), false);
            counter++;
            if (counter >= blockList.size()) {
                if (this.createSign && this.player != null) {
                    Core.getInstance().getAreaManager().createPlayerSign(this.player, ZoneXZ.fromPoint(baseX, baseZ));
                }
                Bukkit.getScheduler().cancelTask(this.TaskID);
                Core.getInstance().getAreaManager().decrementThreads();
                break;
            }
        }
        thisBlock = null;
    }
}
