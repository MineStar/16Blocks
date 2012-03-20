package de.minestar.sixteenblocks.Listener;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

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
        if (!event.getPlayer().isOp() && lastChatList.containsKey(event.getPlayer().getName())) {
            long lastChatEvent = lastChatList.get(event.getPlayer().getName());
            long delta = System.currentTimeMillis() - lastChatEvent;
            System.out.println("delta: " + delta);
            System.out.println("lastChatEvent: " + lastChatEvent);
            if (delta < (Settings.getChatPauseTimeInSeconds() * 1000)) {
                TextUtils.sendError(event.getPlayer(), "You can only chat every " + Settings.getChatPauseTimeInSeconds() + " seconds.");
                event.setCancelled(true);
                return;
            }
        }

        // USED BAD WORD
        if (!event.getPlayer().isOp() && !filter.acceptMessage(event.getMessage())) {
            // troll them by sending the message to them but to no other player
            TextUtils.sendInfo(event.getPlayer(), event.getMessage());
            event.getRecipients().clear();
            event.setCancelled(true);
            return;
        }

        // FORMAT CHAT
        event.setFormat("%2$s");
        if (event.getPlayer().isOp())
            event.setMessage(ChatColor.RED + event.getPlayer().getName() + ChatColor.WHITE + ": " + event.getMessage().replace("$", ""));
        else
            event.setMessage(ChatColor.GREEN + event.getPlayer().getName() + ChatColor.WHITE + ": " + event.getMessage().replace("$", ""));

        lastChatList.put(event.getPlayer().getName(), System.currentTimeMillis());
    }
}
