package de.minestar.sixteenblocks.Commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Manager.AreaManager;
import de.minestar.sixteenblocks.Manager.SkinArea;

public class cmdStartAuto extends AbstractCommand {

    private AreaManager areaManager;

    public cmdStartAuto(String syntax, String arguments, String node, AreaManager areaManager) {
        super(Core.NAME, syntax, arguments, node);
        this.areaManager = areaManager;
        this.description = "Make a random zone to your zone";
    }

    @Override
    public void execute(String[] arguments, Player player) {
        // CHECK : SHUTDOWN / RELOAD
        if (Core.shutdownServer) {
            TextUtils.sendError(player, "/start is not possible at this moment. Server will reload or restart in a few moments.");
            TextUtils.sendLine(player, ChatColor.GRAY, "Please try again in a few moments.");
            return;
        }

        // CHECK : PLAYER HAS NO AREA
        if (this.areaManager.hasPlayerArea(player)) {
            TextUtils.sendError(player, "You already own an area.");
            TextUtils.sendLine(player, ChatColor.GRAY, "Go there with '/home'");
            return;
        }

        TextUtils.sendError(player, "The event is nearly over and you are not able to start a new skin. Sorry for that :/");

//        // TAKE THIS AREA
        SkinArea randomArea = this.areaManager.getRandomUnusedArea();
        this.areaManager.createPlayerArea(new SkinArea(randomArea.getZoneXZ().getX(), randomArea.getZoneXZ().getZ(), player.getName()), true, player);
        TextUtils.sendSuccess(player, "You are now owner of this area: [ " + randomArea.getZoneXZ().getX() + " / " + randomArea.getZoneXZ().getZ() + " ]");

        // TELEPORT TO HOME-POINT
        player.teleport(randomArea.getZoneXZ().getSpawnLocation());
    }
}
