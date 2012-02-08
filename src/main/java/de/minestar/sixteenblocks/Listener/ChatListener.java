package de.minestar.sixteenblocks.Listener;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import de.minestar.sixteenblocks.core.TextUtils;

public class ChatListener implements Listener {

    private int chatPauseTimeInSeconds = 5;
    private HashMap<String, Long> lastChatList = new HashMap<String, Long>();

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        // FLOOD-CONTROL
        if (!event.getPlayer().isOp() && lastChatList.containsKey(event.getPlayer().getName())) {
            long lastChatEvent = lastChatList.get(event.getPlayer().getName());
            if (lastChatEvent + (this.chatPauseTimeInSeconds * 1000) < System.currentTimeMillis()) {
                TextUtils.sendError(event.getPlayer(), "You can only chat every " + this.chatPauseTimeInSeconds + " seconds.");
                event.setCancelled(true);
            }
        }
        // FORMAT CHAT
        event.setFormat("%2$s");
        if (event.getPlayer().isOp())
            event.setMessage(ChatColor.RED + event.getPlayer().getName() + ChatColor.WHITE + ":" + event.getMessage().replace("$", ""));
        else
            event.setMessage(ChatColor.DARK_GRAY + event.getPlayer().getName() + ChatColor.WHITE + ":" + event.getMessage().replace("$", ""));

        lastChatList.put(event.getPlayer().getName(), System.currentTimeMillis());
    }
}
