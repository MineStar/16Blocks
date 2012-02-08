package de.minestar.sixteenblocks.core;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import de.minestar.sixteenblocks.Listener.BaseListener;
import de.minestar.sixteenblocks.Listener.BlockListener;
import de.minestar.sixteenblocks.Listener.ChatListener;
import de.minestar.sixteenblocks.Listener.MovementListener;
import de.minestar.sixteenblocks.Manager.AreaManager;
import de.minestar.sixteenblocks.Manager.StructureManager;
import de.minestar.sixteenblocks.Manager.WorldManager;

public class Core extends JavaPlugin {
    private static Core instance;
    private Listener baseListener, blockListener, chatListener, movementListener;
    private AreaManager areaManager;
    private WorldManager worldManager;
    private StructureManager structureManager;

    @Override
    public void onDisable() {
        TextUtils.logInfo("Disabled!");
    }

    @Override
    public void onEnable() {
        // INIT INSTANCE
        instance = this;

        // INIT SETTINGS
        Settings.init(this.getDataFolder());

        // SET NAME
        TextUtils.setPluginName(this.getDescription().getName());

        // STARTUP
        this.createManager();
        this.registerListeners();

        // INFO
        TextUtils.logInfo("Enabled!");
    }

    private void createManager() {
        this.areaManager = new AreaManager();
        this.structureManager = new StructureManager(this.areaManager);
        this.worldManager = new WorldManager(this.structureManager);
    }

    private void registerListeners() {
        // CREATE LISTENERS
        this.baseListener = new BaseListener();
        this.blockListener = new BlockListener(this.areaManager, this.structureManager);
        this.chatListener = new ChatListener();
        this.movementListener = new MovementListener(this.worldManager);

        // REGISTER LISTENERS
        Bukkit.getPluginManager().registerEvents(this.baseListener, this);
        Bukkit.getPluginManager().registerEvents(this.blockListener, this);
        Bukkit.getPluginManager().registerEvents(this.chatListener, this);
        Bukkit.getPluginManager().registerEvents(this.movementListener, this);
    }

    public static Core getInstance() {
        return instance;
    }
}
