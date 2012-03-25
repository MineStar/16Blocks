package de.minestar.sixteenblocks.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.Settings;
import de.minestar.sixteenblocks.Core.TextUtils;

public class cmdRow extends AbstractCommand {

    public cmdRow(String syntax, String arguments, String node) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Jump to a specific row";
    }

    @Override
    public void execute(String[] arguments, Player player) {
        try {
            int row = Integer.valueOf(arguments[0]);
            Location teleportTo = new Location(Bukkit.getWorlds().get(0), 0, 50, row * Settings.getAreaSizeZ() + 6);
            player.teleport(teleportTo);
            TextUtils.sendInfo(player, "Teleporting you to row: " + row);
        } catch (Exception e) {
            TextUtils.sendError(player, "Wrong Syntax! Please use /row <Number>");
            TextUtils.sendInfo(player, "Example: /row 5");
        }
    }
}
