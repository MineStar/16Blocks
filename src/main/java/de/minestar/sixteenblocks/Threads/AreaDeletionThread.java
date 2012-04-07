package de.minestar.sixteenblocks.Threads;

import java.util.ArrayList;

import net.minecraft.server.Packet130UpdateSign;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.utils.ConsoleUtils;
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
                        Core.getInstance().getAreaManager().unblockArea(this.thisZone);
                        String skinOwner = Core.getInstance().getAreaManager().getPlayerArea(thisZone).getAreaOwner();
                        if (skinOwner != null)
                            createPlayerSign(skinOwner, thisZone);
                        else
                            ConsoleUtils.printInfo(Core.NAME, "SkinOwner is null!");
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
                    Core.getInstance().getAreaManager().unblockArea(this.thisZone);
                    String skinOwner = Core.getInstance().getAreaManager().getPlayerArea(thisZone).getAreaOwner();
                    if (skinOwner != null)
                        createPlayerSign(skinOwner, thisZone);
                    else
                        ConsoleUtils.printInfo(Core.NAME, "SkinOwner is null!");
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
        StringBuilder sBuilder = new StringBuilder(ChatColor.GOLD.toString());
        for (int i = 0; i < percent; i += 10)
            sBuilder.append('|');

        sBuilder.append(ChatColor.DARK_GRAY.toString());
        for (int i = percent; i <= 100; i += 10)
            sBuilder.append('|');

        return sBuilder.toString();
    }

    private void createPlayerSign(String player, ZoneXZ thisZone) {
        World world = Bukkit.getWorlds().get(0);

        // PLACE SIGN
        int x = thisZone.getX() * Settings.getAreaSizeX() - (thisZone.getZ() % 2 != 0 ? (Settings.getAreaSizeX() >> 1) : 0) + (Settings.getAreaSizeX() >> 1);
        int z = thisZone.getZ() * Settings.getAreaSizeZ() + 12;
        world.getBlockAt(x, Settings.getMinimumBuildY() - 1, z).setTypeIdAndData(Material.WALL_SIGN.getId(), (byte) 0x2, true);

        // UPDATE LINE
        Sign signBlock = (Sign) (world.getBlockAt(x, Settings.getMinimumBuildY() - 1, z).getState());
        String[] playerName = new String[3];

        signBlock.setLine(0, "Built by:");
        playerName[0] = player;
        playerName[1] = "";
        playerName[2] = "";
        if (player.length() > 15) {
            playerName[0] = player.substring(0, 15);
            if (player.length() > 30) {
                playerName[1] = player.substring(15, 30);
                if (player.length() > 45) {
                    playerName[2] = player.substring(30, 45);
                } else {
                    playerName[2] = player.substring(30);
                }
            } else {
                playerName[1] = player.substring(15);
            }
        }

        signBlock.setLine(1, playerName[0]);
        signBlock.setLine(2, playerName[1]);
        signBlock.setLine(3, playerName[2]);
        signBlock.update(true);

//        // SEND UPDATE => NEED HELP OF ORIGINAL MC-SERVERSOFTWARE
//        CraftPlayer cPlayer = (CraftPlayer) player;
//        Packet130UpdateSign signPacket = null;
//        signPacket = new Packet130UpdateSign(signBlock.getX(), signBlock.getY(), signBlock.getZ(), signBlock.getLines());
//        cPlayer.getHandle().netServerHandler.sendPacket(signPacket);
    }
}
