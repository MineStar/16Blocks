package de.minestar.sixteenblocks.Listener;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.Settings;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Manager.ChannelManager;
import de.minestar.sixteenblocks.Threads.AFKThread;
import de.minestar.sixteenblocks.Units.ChatFilter;

public class ChatListener implements Listener {

    private HashMap<String, Long> lastChatList;

    private HashSet<String> mutedPlayers = new HashSet<String>();

    private AFKThread afkThread;
    private ChannelManager channelManager;

    private long chatPause = Settings.getChatPauseTimeInSeconds() * 1000;

    public static boolean radiusOff = false;

    public ChatListener(ChatFilter filter, AFKThread afkThread, ChannelManager channelManager) {
        // this.filter = filter;
        this.lastChatList = new HashMap<String, Long>();
        this.afkThread = afkThread;
        this.channelManager = channelManager;
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
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        afkThread.takeAktion(event.getPlayer());

        // FLOOD-CONTROL
        boolean isSupporter = Core.isSupporter(event.getPlayer());
        if (!isSupporter) {
            if (this.lastChatList.containsKey(event.getPlayer().getName())) {
                long lastChatEvent = this.lastChatList.get(event.getPlayer().getName());
                long delta = System.currentTimeMillis() - lastChatEvent;
                if (delta < chatPause) {
                    TextUtils.sendError(event.getPlayer(), "You can only chat every " + Settings.getChatPauseTimeInSeconds() + " seconds.");
                    event.setCancelled(true);
                    return;
                }
            }
        }

        String message = null;
        if (Core.isVip(event.getPlayer())) {
            message = Settings.getColorVips() + event.getPlayer().getName() + ChatColor.WHITE + ": " + event.getMessage().replace("$", "");
        } else if (isSupporter) {
            message = Settings.getColorSupporter() + event.getPlayer().getName() + ChatColor.WHITE + ": " + event.getMessage().replace("$", "");
        } else {
            message = Settings.getColorNormal() + event.getPlayer().getName() + ChatColor.WHITE + ": " + event.getMessage().replace("$", "");
        }

        lastChatList.put(event.getPlayer().getName(), System.currentTimeMillis());
        event.setCancelled(this.channelManager.handleChat(event.getPlayer(), message));
    }

    public boolean hasWritten(Player player, long time) {
        if (Core.isSupporter(player))
            return true;
        Long lastChatEvent = this.lastChatList.get(player.getName());
        if (lastChatEvent == null)
            return true;
        long delta = time - lastChatEvent;
        return (delta < Settings.getSupportTime());
    }
}
