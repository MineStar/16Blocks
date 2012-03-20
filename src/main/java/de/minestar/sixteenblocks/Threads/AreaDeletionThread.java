package de.minestar.sixteenblocks.Threads;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.Settings;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Units.ZoneXZ;

public class AreaDeletionThread implements Runnable {

    private final World world;
    private final int baseX, baseZ;
    private int TaskID = -9999;
    private int counter = 0;

    public boolean createSign = false;
    private String playerName;

    private int maxBlocksToDelete;

    private ZoneXZ thisZone;

    public AreaDeletionThread(World world, int baseX, int baseZ, ZoneXZ thisZone, String playerName) {
        this.world = world;
        this.baseX = baseX;
        this.baseZ = baseZ;
        this.thisZone = thisZone;
        this.maxBlocksToDelete = Settings.getAreaSizeX() * Settings.getAreaSizeZ() * (Settings.getMaximumBuildY() - Settings.getMinimumBuildY());
        this.playerName = playerName;
    }

    public void initTask(int TaskID) {
        this.TaskID = TaskID;
    }

    @Override
    public void run() {
        if (TaskID == -9999)
            return;

        int currentX = baseX, currentY = Settings.getMinimumBuildY(), currentZ = baseZ;
        for (int i = 0; i < Settings.getMaxBlockxReplaceAtOnce(); i++) {
            // INCREMENT COUNTER
            currentX++;
            if (currentX >= Settings.getAreaSizeX()) {
                currentX = baseX;
                currentZ++;
            }
            if (currentZ >= Settings.getAreaSizeZ()) {
                currentZ = baseZ;
                currentY++;
            }

            world.getBlockAt(currentX, currentY, currentZ).setTypeId(Material.AIR.getId(), false);
            counter++;
            if (counter >= maxBlocksToDelete) {
                Bukkit.getScheduler().cancelTask(this.TaskID);
                Core.getInstance().getAreaManager().unblockArea(this.thisZone);
                Player player = Bukkit.getPlayer(this.playerName);
                if (player != null) {
                    TextUtils.sendSuccess(player, "Area [ " + this.thisZone.getX() + " / " + this.thisZone.getZ() + " ] deleted successfully!");
                }
                break;
            }
        }
    }
}
