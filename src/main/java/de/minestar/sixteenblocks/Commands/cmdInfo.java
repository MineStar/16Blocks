package de.minestar.sixteenblocks.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.Settings;
import de.minestar.sixteenblocks.Core.TextUtils;

public class cmdInfo extends AbstractCommand {

    public cmdInfo(String syntax, String arguments, String node) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Teleport to infowall";
    }

    @Override
    public void execute(String[] arguments, Player player) {
        TextUtils.sendMessage(player, ChatColor.YELLOW, "Teleporting you to infowall...");
        player.teleport(new Location(player.getWorld(), Settings.getInfoWallVector().getX(), Settings.getInfoWallVector().getY(), Settings.getInfoWallVector().getZ()));
    }
}
