package de.minestar.sixteenblocks.core;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import de.minestar.sixteenblocks.Listener.BaseListener;
import de.minestar.sixteenblocks.Listener.BlockListener;
import de.minestar.sixteenblocks.Listener.ChatListener;
import de.minestar.sixteenblocks.Listener.MovementListener;
import de.minestar.sixteenblocks.Manager.AreaManager;
import de.minestar.sixteenblocks.Manager.WorldManager;

public class Core extends JavaPlugin {

    private String pluginName = "16Blocks";
    private Listener baseListener, blockListener, chatListener, movementListener;
    private AreaManager areaManager;
    private WorldManager worldManager;

    @Override
    public void onDisable() {
        Console.logInfo("Disabled!");
    }

    @Override
    public void onEnable() {
        // SET NAME
        Console.setPluginName(this.pluginName);

        // STARTUP
        this.createManager();
        this.registerListeners();

        // INFO
        Console.logInfo("Enabled!");
    }

    private void createManager() {
        this.areaManager = new AreaManager();
        this.worldManager = new WorldManager();
    }

    private void registerListeners() {
        // CREATE LISTENERS
        this.baseListener = new BaseListener();
        this.blockListener = new BlockListener(this.areaManager);
        this.chatListener = new ChatListener();
        this.movementListener = new MovementListener(this.worldManager);

        // REGISTER LISTENERS
        Bukkit.getPluginManager().registerEvents(this.baseListener, this);
        Bukkit.getPluginManager().registerEvents(this.blockListener, this);
        Bukkit.getPluginManager().registerEvents(this.chatListener, this);
        Bukkit.getPluginManager().registerEvents(this.movementListener, this);
    }
}
