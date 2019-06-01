package vadim99808;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import io.lumine.xikage.mythicmobs.MythicMobs;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import vadim99808.config.ChestConfigManager;
import vadim99808.config.DefaultConfigManager;
import vadim99808.config.EnchItemsConfigManager;
import vadim99808.config.LocalizationConfigManager;
import vadim99808.dao.UserStatisticsDao;
import vadim99808.entity.Treasure;
import vadim99808.executors.*;
import vadim99808.service.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class TreasureHunt extends JavaPlugin {

    private File chestsDirectory;
    private File enchantedItemsDirectory;
    private File dataDirectory;
    private File testDirectory;
    private File mmBossConditionsDirectory;
    private File statisticsDirectory;
    private Timer timer;
    private MythicMobs mythicMobs;
    private Economy economy;
    private WorldGuardPlugin worldGuardPlugin;
    private WorldEditPlugin worldEditPlugin;
    private ChestConfigManager chestConfigManager;
    private EnchItemsConfigManager enchItemsConfigManager;
    private DefaultConfigManager defaultConfigManager;
    private LocalizationConfigManager localizationConfigManager;
    private HuntService huntService;
    private BroadcastService broadcastService;
    private StatisticsService statisticsService;
    private Map<String, ItemStack> enchantedItems;
    private List<Treasure> treasureList;
    private List<Material> huntToolList;
    private Map<Player, Long> lastChecks;
    private static TreasureHunt instance;
    private UserStatisticsDao userStatisticsDao;
    private UserStatisticsService userStatisticsService;

    public static TreasureHunt getInstance() {
        return instance;
    }

    public void onEnable(){
        getLogger().info("Start enabling!");
        try {
            mythicMobs = MythicMobs.inst();
        }catch (NoClassDefFoundError noClassDefFoundError){ }
        if(mythicMobs != null){
            getLogger().info("MythicMobs found!");
        }else{
            getLogger().info("MythicMobs not found!");
        }
        RegisteredServiceProvider<Economy> rsp = null;
        try {
            rsp = getServer().getServicesManager().getRegistration(Economy.class);
        }catch (NoClassDefFoundError noClassDefFoundError){ }
        if (rsp != null) {
            economy = rsp.getProvider();
        }
        if(economy != null){
            getLogger().info("Vault found!");
        }else{
            getLogger().info("Vault not found!");
        }
        worldGuardPlugin = (WorldGuardPlugin)getServer().getPluginManager().getPlugin("WorldGuard");
        if(worldGuardPlugin != null){
            getLogger().info("WorldGuard found!");
        }else{
            getLogger().info("WorldGuard not found!");
        }
        worldEditPlugin = (WorldEditPlugin)getServer().getPluginManager().getPlugin("WorldEdit");
        if(worldEditPlugin != null){
            getLogger().info("WorldEdit found!");
        }else{
            getLogger().info("WorldEdit not found!");
        }
        lastChecks = new HashMap<>();
        instance = this;
        install();
        defaultConfigManager = new DefaultConfigManager();
        if(!defaultConfigManager.isConfigExists()){
            defaultConfigManager.initializeDefaultConfig();
        }else{
            defaultConfigManager.loadDefaultConfig();
        }
        localizationConfigManager = new LocalizationConfigManager();
        localizationConfigManager.initializeLocalization();
        localizationConfigManager.loadConfiguration();
        chestConfigManager = new ChestConfigManager();
        enchItemsConfigManager = new EnchItemsConfigManager();
        enchantedItems = enchItemsConfigManager.loadAllEnchantedItems();
        treasureList = chestConfigManager.loadAllTreasures();
        huntService = new HuntService();
        broadcastService = new BroadcastService();
        userStatisticsDao = new UserStatisticsDao();
        userStatisticsDao.setDataFileDirectory(dataDirectory);
        userStatisticsService = new UserStatisticsService();
        statisticsService = new StatisticsService();
        huntToolList = huntService.loadHuntToolList(treasureList);
        getServer().getPluginManager().registerEvents(new TreasureListener(), this);
        getCommand("huntstart").setExecutor(new StartHuntExecutor());
        getCommand("huntlist").setExecutor(new HuntListExecutor());
        getCommand("huntreload").setExecutor(new HuntReloadExecutor());
        getCommand("hunttop").setExecutor(new HuntTopExecutor());
        getCommand("hunttest").setExecutor(new HuntTestExecutor());
        getCommand("hunttestloc").setExecutor(new HuntTestLocExecutor());
        getCommand("huntstat").setExecutor(new HuntStatExecutor());
        getLogger().info("Successfully loaded " + enchantedItems.size() + " enchanted item's.");
        getLogger().info("Successfully loaded " + treasureList.size() + " treasure's.");
        timer = new Timer();
        timer.start();
        getLogger().info("Plugin fully loaded!");
    }

    public void onDisable(){
        getLogger().info("Start disabling");
        timer.interrupt();
        huntService.deleteAllHunts();
    }

    private void install(){
        if(!getDataFolder().exists()){
            getDataFolder().mkdir();
        }
        chestsDirectory = new File(getDataFolder(), "Chests");
        if(!chestsDirectory.exists()){
            chestsDirectory.mkdir();
        }
        enchantedItemsDirectory = new File(getDataFolder(), "EnchantedItems");
        if(!enchantedItemsDirectory.exists()){
            enchantedItemsDirectory.mkdir();
        }
        dataDirectory = new File(getDataFolder(), "Data");
        if(!dataDirectory.exists()){
            dataDirectory.mkdir();
        }
        testDirectory = new File(getDataFolder(), "Tests");
        if(!testDirectory.exists()){
            testDirectory.mkdir();
        }
        mmBossConditionsDirectory = new File(getDataFolder(), "MMBossConditions");
        if(!mmBossConditionsDirectory.exists()){
            mmBossConditionsDirectory.mkdir();
        }
        statisticsDirectory = new File(getDataFolder(), "Statistics");
        if(!statisticsDirectory.exists()){
            statisticsDirectory.mkdir();
        }
    }

    public void reloadConfigurations(){
        getLogger().info("Reloading of configuration file's...");
        install();
        if(!defaultConfigManager.isConfigExists()){
            defaultConfigManager.initializeDefaultConfig();
        }else{
            defaultConfigManager.loadDefaultConfig();
        }
        localizationConfigManager.initializeLocalization();
        localizationConfigManager.loadConfiguration();
        enchantedItems = enchItemsConfigManager.loadAllEnchantedItems();
        treasureList = chestConfigManager.loadAllTreasures();
        huntToolList = huntService.loadHuntToolList(treasureList);
        getLogger().info("Reloading of configuration file's complete!");
    }

    public File getChestsDirectory(){
        return chestsDirectory;
    }

    public Map<String, ItemStack> getEnchantedItems(){return enchantedItems;}

    public File getEnchantedItemsDirectory(){return enchantedItemsDirectory;}

    public List<Treasure> getTreasureList() {
        return treasureList;
    }

    public DefaultConfigManager getDefaultConfigManager(){return defaultConfigManager;}

    public HuntService getHuntService() {
        return huntService;
    }

    public LocalizationConfigManager getLocalizationConfigManager(){return localizationConfigManager;}

    public Map<Player, Long> getLastChecks() {
        return lastChecks;
    }

    public void setLastChecks(Map<Player, Long> lastChecks) {
        this.lastChecks = lastChecks;
    }

    public List<Material> getHuntToolList() {
        return huntToolList;
    }

    public void setHuntToolList(List<Material> huntToolList) {
        this.huntToolList = huntToolList;
    }

    public MythicMobs getMythicMobs() {
        return mythicMobs;
    }

    public Economy getEconomy() {
        return economy;
    }

    public WorldGuardPlugin getWorldGuardPlugin() {
        return worldGuardPlugin;
    }

    public WorldEditPlugin getWorldEditPlugin() {
        return worldEditPlugin;
    }

    public BroadcastService getBroadcastService() {
        return broadcastService;
    }

    public UserStatisticsDao getUserStatisticsDao() {
        return userStatisticsDao;
    }

    public UserStatisticsService getUserStatisticsService() {
        return userStatisticsService;
    }

    public File getTestDirectory() {
        return testDirectory;
    }

    public File getMmBossConditionsDirectory() {
        return mmBossConditionsDirectory;
    }

    public File getStatisticsDirectory() {
        return statisticsDirectory;
    }

    public StatisticsService getStatisticsService() {
        return statisticsService;
    }
}
