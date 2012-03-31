package de.minestar.sixteenblocks.Commands;

import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.Settings;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Manager.AreaManager;
import de.minestar.sixteenblocks.Manager.NumberManager;

public class cmdNumberize extends AbstractCommand {

    private NumberManager numberManager;
    private AreaManager areaManager;

    public cmdNumberize(String syntax, String arguments, String node, NumberManager numberManager, AreaManager areaManager) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Numberize the rows";
        this.numberManager = numberManager;
        this.areaManager = areaManager;
    }

    @Override
    public void execute(String[] arguments, Player player) {
        // CHECK: PLAYER IS OP OR SUPPORTER
        if (!Core.isSupporter(player)) {
            TextUtils.sendError(player, "You are not allowed to do this!");
            return;
        }

        for (int i = 1; i <= this.areaManager.getLastRow(); i = i + 2) {
            this.numberManager.print((-Settings.getSkinsRight() - 1) * Settings.getAreaSizeX() + 16, i * Settings.getAreaSizeZ() + 11, i + 1);
        }
    }
}
