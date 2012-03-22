package de.minestar.sixteenblocks.Core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import de.minestar.minestarlibrary.commands.CommandList;
import de.minestar.minestarlibrary.utils.ConsoleUtils;
import de.minestar.sixteenblocks.Commands.cmdBan;
import de.minestar.sixteenblocks.Commands.cmdDeleteArea;
import de.minestar.sixteenblocks.Commands.cmdHome;
import de.minestar.sixteenblocks.Commands.cmdInfo;
import de.minestar.sixteenblocks.Commands.cmdKick;
import de.minestar.sixteenblocks.Commands.cmdSaveArea;
import de.minestar.sixteenblocks.Commands.cmdSpawn;
import de.minestar.sixteenblocks.Commands.cmdStartAuto;
import de.minestar.sixteenblocks.Commands.cmdStartHere;
import de.minestar.sixteenblocks.Commands.cmdTicket;
import de.minestar.sixteenblocks.Listener.BaseListener;
import de.minestar.sixteenblocks.Listener.BlockListener;
import de.minestar.sixteenblocks.Listener.ChatListener;
import de.minestar.sixteenblocks.Listener.MovementListener;
import de.minestar.sixteenblocks.Mail.MailHandler;
import de.minestar.sixteenblocks.Manager.AreaDatabaseManager;
import de.minestar.sixteenblocks.Manager.AreaManager;
import de.minestar.sixteenblocks.Manager.StructureManager;
import de.minestar.sixteenblocks.Manager.TicketDatabaseManager;
import de.minestar.sixteenblocks.Manager.WorldManager;
import de.minestar.sixteenblocks.Threads.CheckTicketThread;
import de.minestar.sixteenblocks.Threads.DayThread;
import de.minestar.sixteenblocks.Threads.PlayerCountThread;
import de.minestar.sixteenblocks.Units.ChatFilter;

public class Core extends JavaPlugin {
    private static Core instance;

    private Listener baseListener, blockListener, chatListener, movementListener;

    private AreaDatabaseManager areaDatabaseManager;
    private TicketDatabaseManager ticketDatabaseManager;
    private AreaManager areaManager;
    private WorldManager worldManager;
    private StructureManager structureManager;
    private MailHandler mHandler;
    private ChatFilter filter;

    private CheckTicketThread checkTread;

    private CommandList commandList;

    public final static String NAME = "16Blocks";

    @Override
    public void onDisable() {
        this.areaDatabaseManager.closeConnection();
        this.ticketDatabaseManager.closeConnection();
        checkTread.saveCheckTickets();
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

        Set<String> supporter = loadSupporter();

        // INIT COMMANDS
        this.initCommands(supporter);

        this.registerListeners(supporter);

        // FINAL INTITIALIZATION
        this.areaManager.checkForZoneExtension();
        createThreads(Bukkit.getScheduler());

        // UPDATE SPAWN
        Bukkit.getWorlds().get(0).setSpawnLocation(Settings.getSpawnVector().getBlockX(), Settings.getSpawnVector().getBlockY(), Settings.getSpawnVector().getBlockZ());

        // INFO
        TextUtils.logInfo("Enabled!");
    }

    private void createManager() {
        this.areaDatabaseManager = new AreaDatabaseManager(this.getDescription().getName(), this.getDataFolder());
        this.ticketDatabaseManager = new TicketDatabaseManager(NAME, getDataFolder());
        this.structureManager = new StructureManager();
        this.worldManager = new WorldManager();
        this.areaManager = new AreaManager(this.areaDatabaseManager, this.worldManager, this.structureManager);
        this.mHandler = new MailHandler(getDataFolder());
        this.filter = new ChatFilter(getDataFolder());
    }

    private void registerListeners(Set<String> supporter) {
        // CREATE LISTENERS
        this.baseListener = new BaseListener();
        this.blockListener = new BlockListener(this.areaManager);
        this.chatListener = new ChatListener(this.filter, supporter);
        this.movementListener = new MovementListener(this.worldManager);

        // REGISTER LISTENERS
        Bukkit.getPluginManager().registerEvents(this.baseListener, this);
        Bukkit.getPluginManager().registerEvents(this.blockListener, this);
        Bukkit.getPluginManager().registerEvents(this.chatListener, this);
        Bukkit.getPluginManager().registerEvents(this.movementListener, this);
    }

    private void initCommands(Set<String> supporter) {
        /* @formatter:off */
        // Empty permission because permissions are handeld in the commands
        commandList = new CommandList(Core.NAME, 
                        new cmdSpawn        ("/spawn",      "",                         ""),
                        new cmdInfo         ("/info",       "",                         ""),
                        new cmdStartAuto    ("/start",      "",                         "", this.areaManager),
                        new cmdStartAuto    ("/startauto",  "",                         "", this.areaManager),
                        new cmdStartHere    ("/starthere",  "",                         "", this.areaManager),
                        new cmdHome         ("/home",       "[Playername]",             "", this.areaManager),
                        new cmdSaveArea     ("/save",       "<StructureName>",          "", this.areaManager, this.structureManager, supporter),

                        new cmdBan          ("/ban",        "<Playername>",             "", this.areaManager, supporter),
                        new cmdKick         ("/kick",       "<Playername> [Message]",   "", supporter),      
                        new cmdDeleteArea   ("/delete",     "[Playername]",             "", this.areaManager, supporter),
                        
                        new cmdTicket       ("/ticket",     "<Text>",                   "", mHandler, supporter),
                        new cmdTicket       ("/bug",        "<Text>",                   "", mHandler, supporter),
                        new cmdTicket       ("/report",     "<Text>",                   "", mHandler, supporter)
        );
        /* @formatter:on */
    }

    private void createThreads(BukkitScheduler scheduler) {
        // Keep always day time
        scheduler.scheduleSyncRepeatingTask(this, new DayThread(Bukkit.getWorlds().get(0), Settings.getTime()), 0, 1);
        // Check tickets
        checkTread = new CheckTicketThread(this.ticketDatabaseManager, getDataFolder());
        scheduler.scheduleSyncRepeatingTask(this, checkTread, 20 * 60, 20 * 60 * 10);
        // Writing JSON with online player
        scheduler.scheduleAsyncRepeatingTask(this, new PlayerCountThread(), 20 * 10, 20 * 10);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        commandList.handleCommand(sender, label, args);
        return true;
    }

    public static Core getInstance() {
        return instance;
    }

    /**
     * @return the areaManager
     */
    public AreaManager getAreaManager() {
        return areaManager;
    }

    private Set<String> loadSupporter() {
        File f = new File(getDataFolder(), "supporter.txt");
        Set<String> supporter = new HashSet<String>(16);
        if (!f.exists()) {
            ConsoleUtils.printError(NAME, "Can't find supporter file! Only ops can execute support commands!");
            return supporter;
        }

        try {
            BufferedReader bReader = new BufferedReader(new FileReader(f));
            String line = "";
            while ((line = bReader.readLine()) != null) {
                if (!line.isEmpty())
                    supporter.add(line);
            }
            ConsoleUtils.printInfo(NAME, "Loaded " + supporter.size() + " supporter!");
        } catch (Exception e) {
            ConsoleUtils.printException(e, NAME, "Can't load support file!");
        }
        return supporter;
    }
}
