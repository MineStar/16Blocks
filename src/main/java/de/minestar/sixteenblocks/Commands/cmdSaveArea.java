package de.minestar.sixteenblocks.Commands;

import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Enums.EnumStructures;
import de.minestar.sixteenblocks.Manager.AreaManager;
import de.minestar.sixteenblocks.Manager.StructureManager;
import de.minestar.sixteenblocks.Units.ZoneXZ;

public class cmdSaveArea extends AbstractCommand {

    private StructureManager structureManager;

    public cmdSaveArea(String syntax, String arguments, String node, AreaManager areaManager, StructureManager structureManager) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Saves the current area as a structure";
        this.structureManager = structureManager;
    }

    @Override
    public void execute(String[] args, Player player) {

        // CHECK: PLAYER IS OP OR SUPPORTER
        if (!Core.isSupporter(player)) {
            TextUtils.sendError(player, "You are not allowed to do this!");
            return;
        }

        ZoneXZ thisZone = ZoneXZ.fromPoint(player.getLocation().getBlockX(), player.getLocation().getBlockZ());
        EnumStructures thisStructure = EnumStructures.fromName(args[0]);
        if (thisStructure == null) {
            TextUtils.sendError(player, "Structure '" + args[0] + "' not found!");
            return;
        }

        if (structureManager.exportStructure(player.getWorld(), thisStructure.getName(), thisZone)) {
            TextUtils.sendSuccess(player, "Saved area as '" + thisStructure.getName() + "'.");
            this.structureManager.addStructure(thisStructure);
        } else {
            TextUtils.sendError(player, "Could not save area!");
        }
    }

}
