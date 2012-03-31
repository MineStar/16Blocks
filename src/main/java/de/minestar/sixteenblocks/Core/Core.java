package de.minestar.sixteenblocks.Core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import de.minestar.minestarlibrary.commands.CommandList;
import de.minestar.minestarlibrary.utils.ConsoleUtils;
import de.minestar.sixteenblocks.Commands.cmdAdmin;
import de.minestar.sixteenblocks.Commands.cmdBan;
import de.minestar.sixteenblocks.Commands.cmdChatRadius;
import de.minestar.sixteenblocks.Commands.cmdCreateRow;
import de.minestar.sixteenblocks.Commands.cmdDeleteArea;
import de.minestar.sixteenblocks.Commands.cmdHome;
import de.minestar.sixteenblocks.Commands.cmdInfo;
import de.minestar.sixteenblocks.Commands.cmdKick;
import de.minestar.sixteenblocks.Commands.cmdMe;
import de.minestar.sixteenblocks.Commands.cmdMessage;
import de.minestar.sixteenblocks.Commands.cmdMute;
import de.minestar.sixteenblocks.Commands.cmdNumberize;
import de.minestar.sixteenblocks.Commands.cmdRebuild;
import de.minestar.sixteenblocks.Commands.cmdReload;
import de.minestar.sixteenblocks.Commands.cmdReply;
import de.minestar.sixteenblocks.Commands.cmdReset;
import de.minestar.sixteenblocks.Commands.cmdRow;
import de.minestar.sixteenblocks.Commands.cmdSaveArea;
import de.minestar.sixteenblocks.Commands.cmdSay;
import de.minestar.sixteenblocks.Commands.cmdSlots;
import de.minestar.sixteenblocks.Commands.cmdSpawn;
import de.minestar.sixteenblocks.Commands.cmdStartAuto;
import de.minestar.sixteenblocks.Commands.cmdStartHere;
import de.minestar.sixteenblocks.Commands.cmdSupport;
import de.minestar.sixteenblocks.Commands.cmdTP;
import de.minestar.sixteenblocks.Commands.cmdTicket;
import de.minestar.sixteenblocks.Commands.cmdUnban;
import de.minestar.sixteenblocks.Listener.BaseListener;
import de.minestar.sixteenblocks.Listener.BlockListener;
import de.minestar.sixteenblocks.Listener.ChatListener;
import de.minestar.sixteenblocks.Listener.LoginListener;
import de.minestar.sixteenblocks.Listener.MovementListener;
import de.minestar.sixteenblocks.Mail.MailHandler;
import de.minestar.sixteenblocks.Manager.AreaDatabaseManager;
import de.minestar.sixteenblocks.Manager.AreaManager;
import de.minestar.sixteenblocks.Manager.NumberManager;
import de.minestar.sixteenblocks.Manager.StructureManager;
import de.minestar.sixteenblocks.Manager.TicketDatabaseManager;
import de.minestar.sixteenblocks.Manager.WorldManager;
import de.minestar.sixteenblocks.Threads.AFKThread;
import de.minestar.sixteenblocks.Threads.BroadcastThread;
import de.minestar.sixteenblocks.Threads.CheckTicketThread;
import de.minestar.sixteenblocks.Threads.DayThread;
import de.minestar.sixteenblocks.Threads.JSONThread;
import de.minestar.sixteenblocks.Threads.SuperBlockCreationThread;
import de.minestar.sixteenblocks.Units.ChatFilter;

public class Core extends JavaPlugin {
    private static Core instance;

    private Listener baseListener, blockListener, movementListener, loginListener;
    private ChatListener chatListener;

    private AreaDatabaseManager areaDatabaseManager;
    private TicketDatabaseManager ticketDatabaseManager;
    private AreaManager areaManager;
    private WorldManager worldManager;
    private StructureManager structureManager;
    private NumberManager numberManager;
    private MailHandler mHandler;
    private ChatFilter filter;

    private static Set<String> supporter;
    private Set<Player> connectedSupporter = new HashSet<Player>();

    private CheckTicketThread checkTread;
    private SuperBlockCreationThread extendThread;
    private AFKThread afkThread;

    private CommandList commandList;

    private Timer timer = new Timer();
    private Timer broadcastTimer = new Timer();

    public final static String NAME = "16Blocks";

    @Override
    public void onDisable() {
        this.areaDatabaseManager.closeConnection();
        this.ticketDatabaseManager.closeConnection();
        checkTread.saveCheckTickets();
        Settings.saveSettings(this.getDataFolder());
        timer.cancel();
        broadcastTimer.cancel();
        TextUtils.logInfo("Disabled!");
    }

    @Override
    public void onEnable() {
        // INIT INSTANCE
        instance = this;

        // CREATE SKIN-DIR
        new File(this.getDataFolder(), "/skins/").mkdirs();

        // INIT SETTINGS
        Settings.init(this.getDataFolder());

        // SET NAME
        TextUtils.setPluginName("YAM");

        // SUPER EXTENSION-THREAD
        this.extendThread = new SuperBlockCreationThread(Bukkit.getWorlds().get(0));
        this.extendThread.initTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this.extendThread, 0, Settings.getTicksBetweenReplace()));

        // AFK THREAD
        this.afkThread = new AFKThread();

        // STARTUP
        this.createManager();

        // INIT AREAMANAGER
        this.areaManager.init();

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

    /**
     * @return the structureManager
     */
    public StructureManager getStructureManager() {
        return structureManager;
    }

    private void createManager() {
        this.areaDatabaseManager = new AreaDatabaseManager(this.getDescription().getName(), this.getDataFolder());
        this.ticketDatabaseManager = new TicketDatabaseManager(NAME, getDataFolder());
        this.structureManager = new StructureManager();
        this.worldManager = new WorldManager();
        this.areaManager = new AreaManager(this.areaDatabaseManager, this.worldManager, this.structureManager);
        this.mHandler = new MailHandler(getDataFolder());
        this.filter = new ChatFilter(getDataFolder());
        this.numberManager = new NumberManager();
    }

    private void registerListeners() {
        // CREATE LISTENERS
        this.baseListener = new BaseListener();
        this.blockListener = new BlockListener(this.areaManager, this.afkThread);
        this.chatListener = new ChatListener(this.filter, this.afkThread);
        this.movementListener = new MovementListener(this.worldManager, this.afkThread);
        this.loginListener = new LoginListener(this.afkThread, this.connectedSupporter);

        // REGISTER LISTENERS
        Bukkit.getPluginManager().registerEvents(this.baseListener, this);
        Bukkit.getPluginManager().registerEvents(this.blockListener, this);
        Bukkit.getPluginManager().registerEvents(this.chatListener, this);
        Bukkit.getPluginManager().registerEvents(this.movementListener, this);
        Bukkit.getPluginManager().registerEvents(this.loginListener, this);
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
                        new cmdChatRadius   ("/chatradius", "<Number>",                 ""),                        
                        
                        new cmdRow          ("/row",        "<Number>",                 ""),                        
                        new cmdRow          ("/jump",       "<Number>",                 ""),                        
                        new cmdTP           ("/tp",         "<Player>",                 ""),  
                        new cmdReload       ("/reload",     "",                         ""),  
                        new cmdRebuild      ("/rebuild",    "",                         "", this.areaManager),    
                        new cmdReset        ("/reset",    "",                           "", this.areaManager),   
                        new cmdNumberize    ("/numberize",    "",                       "", this.numberManager, this.areaManager),              
                        
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
                        new cmdSupport      ("/support",    "<Playername>",             ""),  
                        
                        // BUG REPORTS
                        new cmdTicket       ("/ticket",     "<Text>",                   "", mHandler),
                        new cmdTicket       ("/bug",        "<Text>",                   "", mHandler),
                        new cmdTicket       ("/report",     "<Text>",                   "", mHandler),
                        
                        // SET SLOTS
                        new cmdSlots        ("/slots",      "<Number>",                 ""),
                        
                        // BROADCASTS
                        new cmdSay          ("/say",        "<Message>",                ""),
                        new cmdSay          ("/cast",       "<Message>",                ""),
                        new cmdSay          ("/broadcast",  "<Message>",                ""),
                        
                        // LOOKING FOR SUPPORTER
                        new cmdAdmin        ("/admins",     "",                         "", this.connectedSupporter)
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
        timer.schedule(new JSONThread(this.areaManager), 1000L * 5L, 1000L * 5L);

        // Broadcasting information to player
        broadcastTimer.schedule(new BroadcastThread(this.getDataFolder(), this.areaManager), (long) (1000 * 60), (long) (1000 * 60 * 5));

        // AFK Thread
        scheduler.scheduleSyncRepeatingTask(this, this.afkThread, 20 * 10, 20 * 30);
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

    public void addSupporter(String playerName) {
        Core.supporter.add(playerName.toLowerCase());
    }

    public void removeSupporter(String playerName) {
        Core.supporter.remove(playerName.toLowerCase());
    }

    public boolean toggleSupporter(String playerName) {
        playerName = playerName.toLowerCase();
        if (!Core.isSupporter(playerName))
            this.addSupporter(playerName);
        else
            this.removeSupporter(playerName);
        this.saveSupporter();
        return Core.isSupporter(playerName);
    }

    private void saveSupporter() {
        File f = new File(getDataFolder(), "supporter.txt");
        if (f.exists()) {
            f.delete();
        }

        try {
            f.createNewFile();
            BufferedWriter bWriter = new BufferedWriter(new FileWriter(f));
            int count = 0;
            for (String name : Core.supporter) {
                bWriter.write(name + System.getProperty("line.separator"));
                count++;
            }
            ConsoleUtils.printInfo(NAME, "Saved " + count + " supporter!");
            bWriter.flush();
            bWriter.close();
        } catch (Exception e) {
            ConsoleUtils.printException(e, NAME, "Can't save support file!");
        }
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
                line = line.trim().replace(" ", "");
                if (!line.isEmpty())
                    this.addSupporter(line);
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

    public static int getAllowedMaxPlayer() {
        return Bukkit.getMaxPlayers() - Settings.getSupporterBuffer();
    }

    /**
     * @return the chatListener
     */
    public ChatListener getChatListener() {
        return chatListener;
    }

    /**
     * @return the extendThread
     */
    public SuperBlockCreationThread getExtendThread() {
        return extendThread;
    }
}
