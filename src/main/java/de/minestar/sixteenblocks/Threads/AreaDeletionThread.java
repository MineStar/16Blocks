package de.minestar.sixteenblocks.Threads;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.Settings;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Enums.EnumStructures;
import de.minestar.sixteenblocks.Units.StructureBlock;
import de.minestar.sixteenblocks.Units.ZoneXZ;

public class AreaDeletionThread implements Runnable {

    private ArrayList<StructureBlock> blockList;
    private final World world;
    private final int baseX, baseZ;
    private int TaskID = -9999;
    private int counter = 0;

    public boolean createSign = false;
    private String playerName;

    private ZoneXZ thisZone;
    private int percent = 0, oldPercent = 0;

    public boolean totalRemove = true;

    public AreaDeletionThread(World world, int baseX, int baseZ, ZoneXZ thisZone, String playerName) {
        this.world = world;
        this.baseX = baseX;
        this.baseZ = baseZ;
        this.thisZone = thisZone;
        this.playerName = playerName;
    }

    public AreaDeletionThread(World world, ZoneXZ thisZone, String playerName) {
        this.world = world;
        this.baseX = thisZone.getBaseX();
        this.baseZ = thisZone.getBaseZ();
        this.thisZone = thisZone;
        this.playerName = playerName;
        this.totalRemove = false;
    }

    public void initTask(int TaskID) {
        this.blockList = new ArrayList<StructureBlock>();
        this.blockList = Core.getInstance().getAreaManager().getChangedBlocks(this.thisZone);
        this.TaskID = TaskID;
        Core.getInstance().getAreaManager().blockArea(thisZone);
    }

    @Override
    public void run() {
        if (TaskID == -9999)
            return;

        StructureBlock thisBlock = null;
        for (int i = 0; i < Settings.getMaxBlocksReplaceAtOnce(); i++) {
            try {
                thisBlock = blockList.get(counter);
                world.getBlockAt(baseX + thisBlock.getX(), thisBlock.getY(), baseZ + thisBlock.getZ()).setType(Material.AIR);
                counter++;
                if (counter >= blockList.size()) {
                    // DELETE SIGN
                    int x = thisZone.getX() * Settings.getAreaSizeX() - (thisZone.getZ() % 2 != 0 ? (Settings.getAreaSizeX() >> 1) : 0) + (Settings.getAreaSizeX() >> 1);
                    int z = thisZone.getZ() * Settings.getAreaSizeZ() + 12;
                    if (this.totalRemove) {
                        world.getBlockAt(x, Settings.getMinimumBuildY() - 1, z).setType(Material.AIR);
                    }

                    // CANCEL TASK & UNBLOCK AREA
                    Bukkit.getScheduler().cancelTask(this.TaskID);
                    if (this.totalRemove) {
                        Core.getInstance().getAreaManager().removePlayerArea(thisZone);
                        Core.getInstance().getAreaManager().unblockArea(this.thisZone);
                    } else {
                        // REBUILD STEVE
                        Core.getInstance().getStructureManager().getStructure(EnumStructures.ZONE_STEVE).createStructure(thisZone.getX(), thisZone.getZ(), Core.getInstance().getExtendThread());
                    }
                    // PRINT INFO
                    Player player = Bukkit.getPlayer(this.playerName);
                    if (player != null) {
                        TextUtils.sendSuccess(player, "Area [ " + this.thisZone.getX() + " / " + this.thisZone.getZ() + " ] deleted successfully!");
                    }

                    break;
                }
            } catch (Exception e) {
                // DELETE SIGN
                int x = thisZone.getX() * Settings.getAreaSizeX() - (thisZone.getZ() % 2 != 0 ? (Settings.getAreaSizeX() >> 1) : 0) + (Settings.getAreaSizeX() >> 1);
                int z = thisZone.getZ() * Settings.getAreaSizeZ() + 12;
                if (this.totalRemove) {
                    world.getBlockAt(x, Settings.getMinimumBuildY() - 1, z).setType(Material.AIR);
                }
                // CANCEL TASK & UNBLOCK AREA
                Bukkit.getScheduler().cancelTask(this.TaskID);
                if (this.totalRemove) {
                    Core.getInstance().getAreaManager().removePlayerArea(thisZone);
                    Core.getInstance().getAreaManager().unblockArea(this.thisZone);
                } else {
                    // REBUILD STEVE
                    Core.getInstance().getStructureManager().getStructure(EnumStructures.ZONE_STEVE).createStructure(thisZone.getX(), thisZone.getZ(), Core.getInstance().getExtendThread());
                }
                // PRINT INFO
                Player player = Bukkit.getPlayer(this.playerName);
                if (player != null && this.totalRemove) {
                    TextUtils.sendSuccess(player, "Area [ " + this.thisZone.getX() + " / " + this.thisZone.getZ() + " ] deleted successfully!");
                }

                break;
            }
        }

        this.percent = (int) ((float) ((float) counter / (float) blockList.size()) * 100f);
        if (this.percent >= this.oldPercent + 10 && this.percent < 100) {
            // PRINT INFO
            Player player = Bukkit.getPlayer(this.playerName);
            if (player != null) {
                TextUtils.sendInfo(player, "Status: " + this.getPercentString(percent));
            }
            this.oldPercent = this.percent;
        }
    }

    private String getPercentString(int percent) {
        String text = ChatColor.GOLD + "";
        for (int i = 0; i < percent; i += 10) {
            text += "|";
        }
        text += ChatColor.DARK_GRAY;
        for (int i = percent; i <= 100; i += 10) {
            text += "|";
        }
        return text;
    }
}
