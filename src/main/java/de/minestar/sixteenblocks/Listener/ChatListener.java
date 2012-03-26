package de.minestar.sixteenblocks.Listener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
    private HashMap<String, Long> lastChatList;

    private HashSet<String> mutedPlayers = new HashSet<String>();

    public ChatListener(ChatFilter filter) {
        this.filter = filter;
        this.lastChatList = new HashMap<String, Long>();
    }

    public boolean toggleMute(Player player) {
        if (this.isMuted(player)) {
            this.unmutePlayer(player);
        } else {
            this.mutePlayer(player);
        }
        return this.isMuted(player);
    }

    public void mutePlayer(Player player) {
        this.mutedPlayers.add(player.getName());
    }

    public void unmutePlayer(Player player) {
        this.mutedPlayers.remove(player.getName());
    }

    public boolean isMuted(Player player) {
        return this.mutedPlayers.contains(player.getName());
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        // FLOOD-CONTROL
        if (!Core.isSupporter(event.getPlayer())) {
            if (this.lastChatList.containsKey(event.getPlayer().getName())) {
                long lastChatEvent = this.lastChatList.get(event.getPlayer().getName());
                long delta = System.currentTimeMillis() - lastChatEvent;
                if (delta < (Settings.getChatPauseTimeInSeconds() * 1000)) {
                    TextUtils.sendError(event.getPlayer(), "You can only chat every " + Settings.getChatPauseTimeInSeconds() + " seconds.");
                    event.setCancelled(true);
                    return;
                }
            }
        }

        // USED BAD WORD
        String message = " " + event.getMessage().toLowerCase();
        if (!Core.isSupporter(event.getPlayer()) && !filter.acceptMessage(message)) {
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

        // IS PLAYER MUTED
        if (this.isMuted(event.getPlayer())) {
            // troll them by sending the message to them but to no other player
            TextUtils.sendLine(event.getPlayer(), ChatColor.GREEN, event.getPlayer().getName() + ChatColor.WHITE + ": " + event.getMessage());
            event.getRecipients().clear();
            event.setCancelled(true);

            // troll them by sending the message to them but to no other player
            TextUtils.sendLine(event.getPlayer(), ChatColor.RED, "[MUTED] " + ChatColor.GREEN + event.getPlayer().getName() + ChatColor.WHITE + ": " + event.getMessage());
            event.getRecipients().clear();
            event.setCancelled(true);

            return;
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
