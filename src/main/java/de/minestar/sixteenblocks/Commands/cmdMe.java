package de.minestar.sixteenblocks.Commands;

import java.io.File;

import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Units.SkinData;

public class cmdMe extends AbstractCommand {

    public cmdMe(String syntax, String arguments, String node) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Ban a player and delete the area";
    }

    @Override
    public void execute(String[] arguments, Player player) {
        // CHECK: PLAYER IS OP OR SUPPORTER

        // TEST: BUILD SKIN
        // SkinData thisSkin = new SkinData(new File(Core.getInstance().getDataFolder(), "GeMoschen.png"));
        // thisSkin.createSkin(player.getLocation());
        // END TEST

        if (!Core.isSupporter(player)) {
            TextUtils.sendError(player, "You are not allowed to do this!");
            return;
        }
    }
}
