package de.minestar.sixteenblocks.Commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Manager.AreaDatabaseManager;

public class cmdImport extends AbstractCommand {

    private AreaDatabaseManager dbManager;

    public cmdImport(String syntax, String arguments, String node, AreaDatabaseManager dbManager) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Import the database";
        this.dbManager = dbManager;
    }

    @Override
    public void execute(String[] arguments, Player player) {
        if (!player.isOp()) {
            TextUtils.sendMessage(player, ChatColor.RED, "You are not allowed to do this!");
            return;
        }

        TextUtils.sendMessage(player, ChatColor.YELLOW, "Importing database...");
        try {
            this.dbManager.importData();
            TextUtils.sendMessage(player, ChatColor.GREEN, "DONE!");
        } catch (Exception e) {
            TextUtils.sendMessage(player, ChatColor.RED, "Error importing data!");
        }
    }
}
