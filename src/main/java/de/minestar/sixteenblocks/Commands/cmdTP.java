package de.minestar.sixteenblocks.Commands;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractExtendedCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;

public class cmdTP extends AbstractExtendedCommand {

    public cmdTP(String syntax, String arguments, String node) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Jump to a specific player";
    }

    @Override
    public void execute(String[] args, Player player) {
        if (args.length == 1) {
            Player target = PlayerUtils.getOnlinePlayer(args[0]);
            if (target == null) {
                TextUtils.sendError(player, "Player '" + args[0] + "' was not found (maybe he is offline?)");
                return;
            } else {
                player.teleport(target.getLocation());
                TextUtils.sendInfo(player, "Teleporting you to '" + target.getName() + "'...");
            }
        } else if (args.length == 3) {
            try {
                Double x = Double.valueOf(args[0]);
                Double y = Double.valueOf(args[1]);
                Double z = Double.valueOf(args[2]);
                player.teleport(new Location(player.getWorld(), x, y, z));
                TextUtils.sendInfo(player, "Teleporting you to " + x + " ,  " + y + " , " + z + "...");
            } catch (NumberFormatException e) {
                TextUtils.sendError(player, "Please use numbers for coordinates!");
            }
        } else
            TextUtils.sendError(player, getHelpMessage());
    }
}
