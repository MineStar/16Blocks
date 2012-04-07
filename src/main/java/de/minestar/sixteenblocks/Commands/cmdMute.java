package de.minestar.sixteenblocks.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.ChatUtils;
import de.minestar.minestarlibrary.utils.PlayerUtils;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Listener.ChatListener;

public class cmdMute extends AbstractCommand {

    private ChatListener chatListener;

    public cmdMute(String syntax, String arguments, String node, ChatListener chatListener) {
        super(Core.NAME, syntax, arguments, node);
        this.chatListener = chatListener;

        this.description = "Mute a player";
    }

    @Override
    public void execute(String[] args, Player player) {

        // CHECK: PLAYER IS OP OR SUPPORTER
        if (!Core.isSupporter(player) || Core.isVip(player)) {
            TextUtils.sendError(player, "You are not allowed to do this!");
            return;
        }

        mutePlayer(args, player);
    }
    @Override
    public void execute(String[] args, ConsoleCommandSender console) {

        mutePlayer(args, console);
    }

    private void mutePlayer(String[] args, CommandSender sender) {

        Player target = PlayerUtils.getOnlinePlayer(args[0]);
        if (target == null)
            ChatUtils.writeError(sender, "Player '" + args[0] + "' was not found (maybe he is offline?)");

        else {
            boolean muted = chatListener.toggleMute(target);
            if (muted)
                ChatUtils.writeInfo(sender, "Player '" + target.getName() + "' is now muted...");
            else
                ChatUtils.writeInfo(sender, "Player '" + target.getName() + "' is no longer muted...");
        }
    }
}
