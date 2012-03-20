package de.minestar.sixteenblocks.Commands;

import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Manager.AreaManager;
import de.minestar.sixteenblocks.Manager.SkinArea;

public class cmdDeleteArea extends AbstractCommand {

    private AreaManager areaManager;

    public cmdDeleteArea(String syntax, String arguments, String node, AreaManager areaManager) {
        super(Core.NAME, syntax, arguments, node);
        this.areaManager = areaManager;
        this.description = "Make the current zone to your zone";
    }

    @Override
    public void execute(String[] arguments, Player player) {

        // CHECK: PLAYER IS OP
        if (!player.isOp()) {
            TextUtils.sendError(player, "You are not allowed to do this!");
            return;
        }

        // GET AREA
        SkinArea thisArea = this.areaManager.getExactPlayerArea(arguments[0]);

        // CHECK: NO AREA FOUND
        if (thisArea == null) {
            TextUtils.sendError(player, "No area for player '" + arguments[0] + "' found!");
            return;
        }

        // TAKE THIS AREA
        this.areaManager.deletePlayerArea(thisArea, player);
        TextUtils.sendSuccess(player, "Starting deletion of '" + thisArea.getAreaOwner() + "'.");
    }
}
