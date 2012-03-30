package de.minestar.sixteenblocks.Commands;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.Settings;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Manager.AreaManager;
import de.minestar.sixteenblocks.Manager.SkinArea;
import de.minestar.sixteenblocks.Units.SkinData;
import de.minestar.sixteenblocks.Units.ZoneXZ;

public class cmdRebuild extends AbstractCommand {

    private AreaManager areaManager;

    public cmdRebuild(String syntax, String arguments, String node, AreaManager areaManager) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Rebuild a skin on the base of the skinfile";
        this.areaManager = areaManager;
    }

    @Override
    public void execute(String[] arguments, Player player) {
        // CHECK: PLAYER IS OP OR SUPPORTER
        if (!Core.isSupporter(player)) {
            TextUtils.sendError(player, "You are not allowed to do this!");
            return;
        }

        // CHECK : AREA IS USED
        ZoneXZ thisZone = ZoneXZ.fromPoint(player.getLocation().getBlockX(), player.getLocation().getBlockZ());
        if (!this.areaManager.containsPlayerArea(thisZone)) {
            TextUtils.sendError(player, "This area is not taken by anyone.");
            return;
        }

        SkinArea thisArea = this.areaManager.getPlayerArea(thisZone);
        TextUtils.sendInfo(player, "Downloading skin of '" + thisArea.getAreaOwner() + "'...");
        if (!this.downloadSkin(thisArea.getAreaOwner())) {
            TextUtils.sendError(player, "Downloading failed!");
            return;
        }

        // BUILD THE SKIN
        File skinFolder = new File(Core.getInstance().getDataFolder() + "/skins/");
        SkinData skinData = new SkinData(new File(skinFolder, thisArea.getAreaOwner() + ".png"));
        skinData.createSkin(Bukkit.getWorlds().get(0), thisZone.getBaseX() + 12, Settings.getMinimumBuildY(), thisZone.getBaseZ() + 14);
        TextUtils.sendSuccess(player, "Skin completed!");
    }

    private boolean downloadSkin(String playerName) {
        int len1 = 0;
        InputStream is = null;
        OutputStream os = null;
        try {
            URL url = new URL("http://s3.amazonaws.com/MinecraftSkins/" + playerName + ".png");
            is = url.openStream();
            os = new FileOutputStream(new File(Core.getInstance().getDataFolder(), "/skins/" + playerName + ".png"));
            byte[] buff = new byte[4096];
            while (-1 != (len1 = is.read(buff))) {
                os.write(buff, 0, len1);
            }
            os.flush();
        } catch (Exception ex) {
            return false;
        } finally {
            if (os != null)
                try {
                    os.close();
                } catch (IOException ex) {/* ok */
                }
            if (is != null)
                try {
                    is.close();
                } catch (IOException ex) {/* ok */
                }
        }
        return true;
    }
}
