package de.minestar.sixteenblocks.Core;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.commands.CommandList;
import de.minestar.sixteenblocks.Commands.cmdHome;
import de.minestar.sixteenblocks.Commands.cmdInfo;
import de.minestar.sixteenblocks.Commands.cmdSaveArea;
import de.minestar.sixteenblocks.Commands.cmdSpawn;
import de.minestar.sixteenblocks.Commands.cmdStartAuto;
import de.minestar.sixteenblocks.Commands.cmdStartHere;
import de.minestar.sixteenblocks.Listener.BaseListener;
import de.minestar.sixteenblocks.Listener.BlockListener;
import de.minestar.sixteenblocks.Listener.ChatListener;
import de.minestar.sixteenblocks.Listener.MovementListener;
import de.minestar.sixteenblocks.Manager.AreaManager;
import de.minestar.sixteenblocks.Manager.DatabaseManager;
import de.minestar.sixteenblocks.Manager.StructureManager;
import de.minestar.sixteenblocks.Manager.WorldManager;
import de.minestar.sixteenblocks.Threads.DayThread;

public class Core extends JavaPlugin {
    private static Core instance;
    private Listener baseListener, blockListener, chatListener, movementListener;
    private DatabaseManager databaseManager;
    private AreaManager areaManager;
    private WorldManager worldManager;
    private StructureManager structureManager;
    private CommandList commandList;

    public final static String NAME = "16Blocks";

    @Override
    public void onDisable() {
        this.databaseManager.closeConnection();
        Settings.saveSettings(this.getDataFolder());
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

        // INIT COMMANDS
        this.initCommands();

        // FINAL INTITIALIZATION
        this.areaManager.checkForZoneExtesion();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new DayThread(Bukkit.getWorlds().get(0), Settings.getTime()), 0, 1);

        // INFO
        TextUtils.logInfo("Enabled!");
    }

    private void createManager() {
        this.databaseManager = new DatabaseManager(this.getDescription().getName(), this.getDataFolder());
        this.structureManager = new StructureManager();
        this.worldManager = new WorldManager();
        this.areaManager = new AreaManager(this.databaseManager, this.worldManager, this.structureManager);
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

    private void initCommands() {
        /* @formatter:off */
        // Add an command to this list to register it in the plugin       
        AbstractCommand[] commands = new AbstractCommand[] {
                        new cmdSpawn("[16Blocks]", "/spawn", "", ""),
                        new cmdInfo("[16Blocks]", "/info", "", ""),
                        new cmdStartAuto("[16Blocks]", "/start", "", "",  this.areaManager),
                        new cmdStartAuto("[16Blocks]", "/startauto", "", "",  this.areaManager),
                        new cmdStartHere("[16Blocks]", "/starthere", "", "",  this.areaManager),
                        new cmdHome("[16Blocks]", "/home", "[Playername]", "",  this.areaManager),
                        new cmdSaveArea("[16Blocks]", "/save", "<StructureName>", "", this.areaManager, this.structureManager)
        };
        /* @formatter:on */
        // store the commands in the hash map
        commandList = new CommandList("[16Blocks]", commands);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        commandList.handleCommand(sender, label, args);
        return true;
    }

    public static Core getInstance() {
        return instance;
    }
}
