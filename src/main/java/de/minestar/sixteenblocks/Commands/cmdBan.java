package de.minestar.sixteenblocks.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.ChatUtils;
import de.minestar.minestarlibrary.utils.PlayerUtils;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;

public class cmdBan extends AbstractCommand {

    public cmdBan(String syntax, String arguments, String node) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Ban a player and delete the area";
    }

    @Override
    public void execute(String[] args, Player player) {

        // CHECK: PLAYER IS OP OR SUPPORTER
        if (!Core.isSupporter(player) || Core.isVip(player)) {
            TextUtils.sendError(player, "You are not allowed to do this!");
            return;
        }

        banPlayer(args, player);
    }

    @Override
    public void execute(String[] args, ConsoleCommandSender console) {

        banPlayer(args, console);
    }

    private void banPlayer(String[] args, CommandSender sender) {
        // GET AREA
        Player target = PlayerUtils.getOnlinePlayer(args[0]);
        if (target == null) {
            String playerName = PlayerUtils.getOfflinePlayerName(args[0]);
            if (playerName == null) {
                ChatUtils.writeInfo(sender, "Player '" + args[0] + "' doesn't exist(was never on the server), but was banned!");
                playerName = args[0];
            } else
                ChatUtils.writeInfo(sender, "Player '" + args[0] + "' is offline!");

            Bukkit.getOfflinePlayer(playerName).setBanned(true);
            ChatUtils.writeSuccess(sender, "Player '" + args[0] + "' banned.");
            return;
        }

        // BAN PLAYER
        target.setBanned(true);
        ChatUtils.writeError(sender, "Player '" + target.getName() + "' banned.");

        // KICK PLAYER
        target.kickPlayer("You were BANNED!");
    }
}
