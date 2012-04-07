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
import de.minestar.sixteenblocks.Commands.cmdGive;
import de.minestar.sixteenblocks.Commands.cmdHome;
import de.minestar.sixteenblocks.Commands.cmdInfo;
import de.minestar.sixteenblocks.Commands.cmdKick;
import de.minestar.sixteenblocks.Commands.cmdMe;
import de.minestar.sixteenblocks.Commands.cmdMessage;
import de.minestar.sixteenblocks.Commands.cmdMute;
import de.minestar.sixteenblocks.Commands.cmdNumberize;
import de.minestar.sixteenblocks.Commands.cmdRebuild;
import de.minestar.sixteenblocks.Commands.cmdReload;
import de.minestar.sixteenblocks.Commands.cmdReloadFilter;
import de.minestar.sixteenblocks.Commands.cmdReply;
import de.minestar.sixteenblocks.Commands.cmdReset;
import de.minestar.sixteenblocks.Commands.cmdRow;
import de.minestar.sixteenblocks.Commands.cmdSaveArea;
import de.minestar.sixteenblocks.Commands.cmdSay;
import de.minestar.sixteenblocks.Commands.cmdSlots;
import de.minestar.sixteenblocks.Commands.cmdSpawn;
import de.minestar.sixteenblocks.Commands.cmdStartAuto;
import de.minestar.sixteenblocks.Commands.cmdStartHere;
import de.minestar.sixteenblocks.Commands.cmdStop;
import de.minestar.sixteenblocks.Commands.cmdSupporter;
import de.minestar.sixteenblocks.Commands.cmdTP;
import de.minestar.sixteenblocks.Commands.cmdTicket;
import de.minestar.sixteenblocks.Commands.cmdURL;
import de.minestar.sixteenblocks.Commands.cmdUnban;
import de.minestar.sixteenblocks.Commands.cmdVip;
import de.minestar.sixteenblocks.Listener.ActionListener;
import de.minestar.sixteenblocks.Listener.BaseListener;
import de.minestar.sixteenblocks.Listener.ChatListener;
import de.minestar.sixteenblocks.Listener.CommandListener;
import de.minestar.sixteenblocks.Listener.LoginListener;
import de.minestar.sixteenblocks.Listener.MovementListener;
import de.minestar.sixteenblocks.Mail.MailHandler;
import de.minestar.sixteenblocks.Manager.AreaDatabaseManager;
import de.minestar.sixteenblocks.Manager.AreaManager;
import de.minestar.sixteenblocks.Manager.NumberManager;
import de.minestar.sixteenblocks.Manager.StructureManager;
import de.minestar.sixteenblocks.Manager.WorldManager;
import de.minestar.sixteenblocks.Threads.AFKThread;
import de.minestar.sixteenblocks.Threads.BlockThread;
import de.minestar.sixteenblocks.Threads.BroadcastThread;
import de.minestar.sixteenblocks.Threads.DayThread;
import de.minestar.sixteenblocks.Threads.JSONThread;
import de.minestar.sixteenblocks.Units.ChatFilter;

public class Core extends JavaPlugin {
    private static Core instance;

    // LISTENER
    private Listener baseListener, blockListener, movementListener, loginListener, commandListener;
    private ChatListener chatListener;

    // MANAGER
    private AreaDatabaseManager areaDatabaseManager;
    private AreaManager areaManager;
    private WorldManager worldManager;
    private StructureManager structureManager;
    private NumberManager numberManager;
    private MailHandler mHandler;
    private ChatFilter filter;

    // THREADS
    private BlockThread blockThread;
    private AFKThread afkThread;
    private BroadcastThread bThread;

    private Timer timer = new Timer();
    private Timer broadcastTimer = new Timer();

    private CommandList commandList;

    // GLOBAL VARIABLES

    public final static String NAME = "16Blocks";

    public static boolean shutdownServer = false;
    public static boolean isShutDown = false;

    @Override
    public void onDisable() {
        this.areaDatabaseManager.closeConnection();
//        this.ticketDatabaseManager.closeConnection();
//        checkTread.saveCheckTickets();
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
        this.blockThread = new BlockThread(Bukkit.getWorlds().get(0));
        this.blockThread.initTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this.blockThread, 0L, Settings.getTicksBetweenReplace()));

        // AFK THREAD
        this.afkThread = new AFKThread();

        // STARTUP
        this.createManager();

        this.areaManager.updateThread(this.blockThread);

        // INIT AREAMANAGER
        this.areaManager.init();

        // LOAD SUPPORTER (NOT ALL ADMINS ARE OPS)
        loadSupporter();

        this.registerListeners();

        // FINAL INTITIALIZATION
        this.areaManager.checkForZoneExtension();
        createThreads(Bukkit.getScheduler());

        // INIT COMMANDS
        this.initCommands();

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
        // this.ticketDatabaseManager = new TicketDatabaseManager(NAME,
        // getDataFolder());
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
        this.blockListener = new ActionListener(this.areaManager, this.afkThread);
        this.chatListener = new ChatListener(this.filter, this.afkThread);
        this.movementListener = new MovementListener(this.worldManager, this.afkThread);
        this.loginListener = new LoginListener(this.afkThread);
        this.commandListener = new CommandListener();

        // REGISTER LISTENERS
        Bukkit.getPluginManager().registerEvents(this.baseListener, this);
        Bukkit.getPluginManager().registerEvents(this.blockListener, this);
        Bukkit.getPluginManager().registerEvents(this.chatListener, this);
        Bukkit.getPluginManager().registerEvents(this.movementListener, this);
        Bukkit.getPluginManager().registerEvents(this.loginListener, this);
        Bukkit.getPluginManager().registerEvents(this.commandListener, this);
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
                        new cmdRebuild      ("/rebuild",    "",                         "", this.areaManager),    
                        new cmdReset        ("/reset",    "",                           "", this.areaManager),   
                        new cmdNumberize    ("/numberize",    "",                       "", this.numberManager, this.areaManager),              
                        
                        // STOP RELOAD
                        new cmdStop         ("/shutdown",        "",                         ""),                          
                        new cmdReload       ("/rel",        "",                         ""),  
                        
                        // MESSAGE SYSTEM
                        new cmdMessage      ("/m",          "<PlayerName> <Message>",   "", recipients),
                        new cmdMessage      ("/w",          "<PlayerName> <Message>",   "", recipients),
                        new cmdReply        ("/r",          "<Message>",                "", recipients),
                        new cmdMe           ("/me",         "<Message>",                ""),
                        new cmdMute         ("/mute",       "<Player>",                 "", this.chatListener),
                        
                        // PUNISHMENTS
                        new cmdBan          ("/ban",        "<Playername>",             ""),
                        new cmdUnban        ("/unban",      "<Playername>",             ""),
                        new cmdKick         ("/kick",       "<Playername> [Message]",   ""),      
                        new cmdDeleteArea   ("/delete",     "[Playername]",             "", this.areaManager),
                        new cmdSupporter    ("/supporter",  "<Playername>",             ""),  
                        new cmdVip          ("/vip",        "<Playername>",             ""),  
                        
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
                        new cmdAdmin        ("/admins",     "",                         ""),
                        
                        // RELOAD CHATFILTER
                        new cmdReloadFilter ("/filter",     "",                         "", this.filter, bThread),
                        
                        new cmdGive         ("/give",       "<Player> <Item[:SubID]> [Amount]", ""),
                        
                        // URL TO LIVE MAP
                        new cmdURL          ("/livemap",    "[Player]",                 "", this.areaManager)
        );
        /* @formatter:on */
    }

    private void createThreads(BukkitScheduler scheduler) {
        // Keep always day time
        scheduler.scheduleSyncRepeatingTask(this, new DayThread(Bukkit.getWorlds().get(0), Settings.getTime()), 0, 1);

        // Check tickets
        // checkTread = new CheckTicketThread(this.ticketDatabaseManager,
        // getDataFolder());
        // scheduler.scheduleSyncRepeatingTask(this, checkTread, 20L * 60L, 20L
        // * 60L * 10L);

        // Writing JSON with online player
        timer.schedule(new JSONThread(this.areaManager), 1000L * 5L, 1000L * 5L);

        // Broadcasting information to player
        bThread = new BroadcastThread(this.getDataFolder(), this.areaManager);
        broadcastTimer.schedule(bThread, 1000L * 60L, 1000L * 60L * Settings.getJAMES_INTERVAL());

        // AFK Thread
        scheduler.scheduleSyncRepeatingTask(this, this.afkThread, 20L * 10L, 20L * 30L);
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

    // SUPPORT AND VIP HANDELING

    private static Set<String> supporter, vips;

    public void addSupporter(String playerName) {
        Core.supporter.add(playerName.toLowerCase());
    }

    public void removeSupporter(String playerName) {
        Core.supporter.remove(playerName.toLowerCase());
    }

    public boolean toggleSupporter(String playerName) {
        playerName = playerName.toLowerCase();
        this.removeVip(playerName);
        if (!Core.isSupporter(playerName)) {
            this.addSupporter(playerName);
        } else {
            this.removeSupporter(playerName);
        }
        this.saveSupporter();
        return Core.isSupporter(playerName);
    }

    public void addVip(String playerName) {
        Core.vips.add(playerName.toLowerCase());
    }

    public void removeVip(String playerName) {
        Core.vips.remove(playerName.toLowerCase());
    }

    public boolean toggleVip(String playerName) {
        playerName = playerName.toLowerCase();
        this.removeSupporter(playerName);
        if (!Core.isVip(playerName)) {
            this.addVip(playerName);
        } else {
            this.removeVip(playerName);
        }
        this.saveVips();
        return Core.isVip(playerName);
    }

    public static Set<String> getSupporter() {
        return Core.supporter;
    }

    public static Set<String> getVips() {
        return Core.vips;
    }

    private void saveSupporter() {
        this.saveUsers(Core.supporter, "supporter.txt");
        this.saveUsers(Core.vips, "vips.txt");
    }

    private void saveVips() {
        this.saveUsers(Core.supporter, "supporter.txt");
        this.saveUsers(Core.vips, "vips.txt");
    }

    private void saveUsers(Set<String> set, String fileName) {
        File f = new File(getDataFolder(), fileName);
        // IF FILE EXISTS: DELETE IT
        if (f.exists()) {
            f.delete();
        }

        try {
            f.createNewFile();
            BufferedWriter bWriter = new BufferedWriter(new FileWriter(f));
            int count = 0;
            for (String name : set) {
                bWriter.write(name + System.getProperty("line.separator"));
                count++;
            }
            ConsoleUtils.printInfo(NAME, "Saved " + count + " users in '" + fileName + "'!");
            bWriter.flush();
            bWriter.close();
        } catch (Exception e) {
            ConsoleUtils.printException(e, NAME, "Error while saving file '" + fileName + "'!");
        }
    }

    private void loadSupporter() {
        Core.supporter = new HashSet<String>(16);
        Core.vips = new HashSet<String>(16);
        this.loadUsers(Core.supporter, "supporter.txt");
        this.loadUsers(Core.vips, "vips.txt");
    }

    private void loadUsers(Set<String> set, String fileName) {
        File f = new File(getDataFolder(), fileName);
        // CHECK: FILE EXISTS
        if (!f.exists()) {
            ConsoleUtils.printError(NAME, "File '" + fileName + "' not found! No Users were loaded.");
            try {
                f.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            BufferedReader bReader = new BufferedReader(new FileReader(f));
            String line = "";
            while ((line = bReader.readLine()) != null) {
                line = line.trim().replace(" ", "");
                if (!line.isEmpty()) {
                    set.add(line.toLowerCase());
                }
            }
            ConsoleUtils.printInfo(NAME, "Loaded " + set.size() + " users from '" + fileName + "'!");
        } catch (Exception e) {
            ConsoleUtils.printException(e, NAME, "Error while reading file: '" + fileName + "'");
        }
    }

    public static boolean isVip(Player player) {
        return Core.isVip(player.getName());
    }

    public static boolean isVip(String playerName) {
        return vips.contains(playerName.toLowerCase());
    }

    public static boolean isSupporter(Player player) {
        return Core.isSupporter(player.getName());
    }

    public static boolean isSupporter(String playerName) {
        String name = playerName.toLowerCase();
        return Bukkit.getOfflinePlayer(name).isOp() || supporter.contains(name) || Core.isVip(name);
    }

    public static int getAllowedMaxPlayer() {
        return Bukkit.getMaxPlayers() - Settings.getSupporterBuffer();
    }

    /**
     * @return the extendThread
     */
    public BlockThread getExtendThread() {
        return blockThread;
    }
}
