package de.minestar.sixteenblocks.Core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import de.minestar.minestarlibrary.commands.CommandList;
import de.minestar.minestarlibrary.utils.ConsoleUtils;
import de.minestar.sixteenblocks.Commands.cmdBan;
import de.minestar.sixteenblocks.Commands.cmdCreateRow;
import de.minestar.sixteenblocks.Commands.cmdDeleteArea;
import de.minestar.sixteenblocks.Commands.cmdHome;
import de.minestar.sixteenblocks.Commands.cmdInfo;
import de.minestar.sixteenblocks.Commands.cmdKick;
import de.minestar.sixteenblocks.Commands.cmdMe;
import de.minestar.sixteenblocks.Commands.cmdMessage;
import de.minestar.sixteenblocks.Commands.cmdMute;
import de.minestar.sixteenblocks.Commands.cmdReload;
import de.minestar.sixteenblocks.Commands.cmdReply;
import de.minestar.sixteenblocks.Commands.cmdRow;
import de.minestar.sixteenblocks.Commands.cmdSaveArea;
import de.minestar.sixteenblocks.Commands.cmdSpawn;
import de.minestar.sixteenblocks.Commands.cmdStartAuto;
import de.minestar.sixteenblocks.Commands.cmdStartHere;
import de.minestar.sixteenblocks.Commands.cmdTP;
import de.minestar.sixteenblocks.Commands.cmdTicket;
import de.minestar.sixteenblocks.Commands.cmdUnban;
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
import de.minestar.sixteenblocks.Threads.JSONThread;
import de.minestar.sixteenblocks.Units.ChatFilter;

public class Core extends JavaPlugin {
    private static Core instance;

    private Listener baseListener, blockListener, movementListener;
    private ChatListener chatListener;

    private AreaDatabaseManager areaDatabaseManager;
    private TicketDatabaseManager ticketDatabaseManager;
    private AreaManager areaManager;
    private WorldManager worldManager;
    private StructureManager structureManager;
    private MailHandler mHandler;
    private ChatFilter filter;

    private static Set<String> supporter;

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
        TextUtils.setPluginName("YAM");

        // STARTUP
        this.createManager();

        // LOAD SUPPORTER (NOT ALL ADMINS ARE OPS)
        loadSupporter();

        // INIT COMMANDS
        this.initCommands();

        this.registerListeners();

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

    private void registerListeners() {
        // CREATE LISTENERS
        this.baseListener = new BaseListener();
        this.blockListener = new BlockListener(this.areaManager);
        this.chatListener = new ChatListener(this.filter);
        this.movementListener = new MovementListener(this.worldManager);

        // REGISTER LISTENERS
        Bukkit.getPluginManager().registerEvents(this.baseListener, this);
        Bukkit.getPluginManager().registerEvents(this.blockListener, this);
        Bukkit.getPluginManager().registerEvents(this.chatListener, this);
        Bukkit.getPluginManager().registerEvents(this.movementListener, this);
    }

    private void initCommands() {
        /* @formatter:off */
        // Empty permission because permissions are handeld in the commands
        Map<Player, Player> recipients = new HashMap<Player, Player>(256);
        commandList = new CommandList(Core.NAME, 
                        new cmdSpawn        ("/spawn",      "",                         ""),
                        new cmdInfo         ("/info",       "",                         ""),
                        new cmdStartAuto    ("/start",      "",                         "", this.areaManager),
                        new cmdStartAuto    ("/startauto",  "",                         "", this.areaManager),
                        new cmdStartHere    ("/starthere",  "",                         "", this.areaManager),
                        new cmdHome         ("/home",       "[Playername]",             "", this.areaManager),
                        new cmdSaveArea     ("/save",       "<StructureName>",          "", this.areaManager, this.structureManager),
                        
                        // TELEPORT
                        new cmdCreateRow    ("/createrow",  "<Number>",                 ""),                        
                        
                        new cmdRow          ("/row",        "<Number>",                 ""),                        
                        new cmdRow          ("/jump",       "<Number>",                 ""),                        
                        new cmdTP           ("/tp",         "<Player>",                 ""),  
                        new cmdReload       ("/reload",     "",                         ""),  
                        
                        // MESSAGE SYSTEM
                        new cmdMessage      ("/m",          "<PlayerName> <Message>",   "", recipients),
                        new cmdMessage      ("/w",          "<PlayerName> <Message>",   "", recipients),
                        new cmdReply        ("/r",          "<Message>",                "", recipients),
                        new cmdMe           ("/me",         "<Message>",                ""),
                        new cmdMute         ("/mute",       "<Player>",                 ""),
                        
                        // PUNISHMENTS
                        new cmdBan          ("/ban",        "<Playername>",             ""),
                        new cmdUnban        ("/unban",      "<Playername>",             ""),
                        new cmdKick         ("/kick",       "<Playername> [Message]",   ""),      
                        new cmdDeleteArea   ("/delete",     "[Playername]",             "", this.areaManager),
                        
                        // BUG REPORTS
                        new cmdTicket       ("/ticket",     "<Text>",                   "", mHandler),
                        new cmdTicket       ("/bug",        "<Text>",                   "", mHandler),
                        new cmdTicket       ("/report",     "<Text>",                   "", mHandler)
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
        scheduler.scheduleAsyncRepeatingTask(this, new JSONThread(this.areaManager), 20 * 10, 20 * 10);
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

    private void loadSupporter() {
        File f = new File(getDataFolder(), "supporter.txt");
        supporter = new HashSet<String>(16);
        if (!f.exists()) {
            ConsoleUtils.printError(NAME, "Can't find supporter file! Only ops can execute support commands!");
            return;
        }

        try {
            BufferedReader bReader = new BufferedReader(new FileReader(f));
            String line = "";
            while ((line = bReader.readLine()) != null) {
                if (!line.isEmpty())
                    supporter.add(line.toLowerCase());
            }
            ConsoleUtils.printInfo(NAME, "Loaded " + supporter.size() + " supporter!");
        } catch (Exception e) {
            ConsoleUtils.printException(e, NAME, "Can't load support file!");
        }
    }

    public static boolean isSupporter(Player player) {
        return player.isOp() || supporter.contains(player.getName().toLowerCase());
    }

    public static boolean isSupporter(String playerName) {
        return Bukkit.getOfflinePlayer(playerName).isOp() || supporter.contains(playerName.toLowerCase());
    }

    /**
     * @return the chatListener
     */
    public ChatListener getChatListener() {
        return chatListener;
    }
}
