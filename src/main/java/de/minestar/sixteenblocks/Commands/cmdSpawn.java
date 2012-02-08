package de.minestar.sixteenblocks.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.Command;
import de.minestar.sixteenblocks.Core.Settings;
import de.minestar.sixteenblocks.Core.TextUtils;

public class cmdSpawn extends Command {

    public cmdSpawn(String pluginName, String syntax, String arguments, String node) {
        super(pluginName, syntax, arguments, node);
        this.description = "Teleport to spawn";
    }

    @Override
    public void execute(String[] arguments, Player player) {
        TextUtils.sendMessage(player, ChatColor.YELLOW, "Teleporting you to spawn...");
        player.teleport(new Location(player.getWorld(), Settings.getSpawnVector().getX(), Settings.getSpawnVector().getY(), Settings.getSpawnVector().getZ()));
    }
}
