package de.minestar.sixteenblocks.Listener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.Settings;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Units.ChatFilter;

public class ChatListener implements Listener {

    private ChatFilter filter;

    public ChatListener(ChatFilter filter) {
        this.filter = filter;
    }

    private HashMap<String, Long> lastChatList = new HashMap<String, Long>();

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        // FLOOD-CONTROL
        if (!Core.isSupporter(event.getPlayer())) {

            long lastChatEvent = lastChatList.get(event.getPlayer().getName());
            long delta = System.currentTimeMillis() - lastChatEvent;
            if (delta < (Settings.getChatPauseTimeInSeconds() * 1000)) {
                TextUtils.sendError(event.getPlayer(), "You can only chat every " + Settings.getChatPauseTimeInSeconds() + " seconds.");
                event.setCancelled(true);
                return;
            }
        }

        // USED BAD WORD
        if (!Core.isSupporter(event.getPlayer()) && !filter.acceptMessage(event.getMessage().toLowerCase())) {
            // troll them by sending the message to them but to no other player
            TextUtils.sendLine(event.getPlayer(), ChatColor.GREEN, event.getPlayer().getName() + ChatColor.WHITE + ": " + event.getMessage());
            event.getRecipients().clear();
            event.setCancelled(true);

            // WARN ADMINS
            Set<OfflinePlayer> opList = Bukkit.getOperators();
            for (OfflinePlayer offPlayer : opList) {
                if (offPlayer.isOnline()) {
                    TextUtils.sendLine((Player) offPlayer, ChatColor.RED, "CAUTION! User '" + event.getPlayer().getName() + "' is trolling around. Message: " + ChatColor.GRAY + event.getMessage());
                }
            }

            return;
        }

        // CHAT-RADIUS
        if (Settings.getChatRadius() > 0 && !Core.isSupporter(event.getPlayer())) {
            Location chatLocation = event.getPlayer().getLocation();
            Iterator<Player> iterator = event.getRecipients().iterator();
            Player thisPlayer;
            while (iterator.hasNext()) {
                thisPlayer = iterator.next();
                if (!Core.isSupporter(thisPlayer) && thisPlayer.getLocation().distance(chatLocation) > Settings.getChatRadius()) {
                    iterator.remove();
                }
            }
        }

        // FORMAT CHAT
        event.setFormat("%2$s");
        if (Core.isSupporter(event.getPlayer()))
            event.setMessage(ChatColor.RED + event.getPlayer().getName() + ChatColor.WHITE + ": " + event.getMessage().replace("$", ""));
        else
            event.setMessage(ChatColor.GREEN + event.getPlayer().getName() + ChatColor.WHITE + ": " + event.getMessage().replace("$", ""));

        lastChatList.put(event.getPlayer().getName(), System.currentTimeMillis());
    }
}
