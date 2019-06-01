package vadim99808.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import vadim99808.TreasureHunt;

import java.io.File;
import java.io.IOException;

public class DefaultConfigManager {

    private TreasureHunt plugin = TreasureHunt.getInstance();
    private int spawnDelay;
    private int spawnChance;
    private int lifeTime;
    private int variety;
    private int OOLimit;
    private int minPlayers;
    private String huntToolString;
    private int maxRadius;
    private int minRadius;
    private int maxHeight;
    private int minHeight;
    private int maxLight;
    private int minLight;
    private int destroyDelay;
    private int checkDelay;
    private int closestAfter;

    public DefaultConfigManager(){
        spawnDelay = 60;
        spawnChance = 1;
        lifeTime = 10;
        variety = 5;
        OOLimit = 2;
        minPlayers = 1;
        huntToolString = "ROTTEN_FLESH";
        maxRadius = 3000;
        minRadius = 200;
        maxHeight = 100;
        minHeight = 20;
        maxLight = 15;
        minLight = 0;
        destroyDelay = 2;
        checkDelay = 5;
        closestAfter = 200;
    }

    public boolean isConfigExists(){
        File file = new File(plugin.getDataFolder(), "config.yml");
        return file.exists();
    }

    public File initializeDefaultConfig(){
        File file = new File(plugin.getDataFolder(), "config.yml");
        try {
            file.createNewFile();
        } catch (IOException e) {
            plugin.getLogger().warning("Error with creating of default config!");
        }
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection spawnOptions = fileConfiguration.createSection("Spawn");
        spawnOptions.set("Delay", spawnDelay);
        spawnOptions.set("Chance", spawnChance);
        ConfigurationSection defaultTreasureOptions = fileConfiguration.createSection("DefaultTreasureOptions");
        defaultTreasureOptions.set("LifeTime", lifeTime);
        defaultTreasureOptions.set("Variety", variety);
        defaultTreasureOptions.set("OOLimit", OOLimit);
        defaultTreasureOptions.set("MinPlayers", minPlayers);
        defaultTreasureOptions.set("HuntTool", huntToolString);
        defaultTreasureOptions.set("MaxRadius", maxRadius);
        defaultTreasureOptions.set("MinRadius", minRadius);
        defaultTreasureOptions.set("MaxHeight", maxHeight);
        defaultTreasureOptions.set("MinHeight", minHeight);
        defaultTreasureOptions.set("MaxLight", maxLight);
        defaultTreasureOptions.set("MinLight", minLight);
        ConfigurationSection destroyOptions = fileConfiguration.createSection("Destroy");
        destroyOptions.set("DestroyDelay", destroyDelay);
        ConfigurationSection searchOptions = fileConfiguration.createSection("Search");
        searchOptions.set("CheckDelay", checkDelay);
        searchOptions.set("ClosestAfter", closestAfter);
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Error with saving default config!");
        }
        return file;
    }

    public File loadDefaultConfig(){
        checkConfiguration();
        File file = new File(plugin.getDataFolder(), "config.yml");
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        spawnChance = fileConfiguration.getInt("Spawn.Chance");
        spawnDelay = fileConfiguration.getInt("Spawn.Delay");
        ConfigurationSection defaultTreasure = fileConfiguration.getConfigurationSection("DefaultTreasureOptions");
        lifeTime = defaultTreasure.getInt("LifeTime");
        variety = defaultTreasure.getInt("Variety");
        OOLimit = defaultTreasure.getInt("OOLimit");
        minPlayers = defaultTreasure.getInt("MinPlayers");
        huntToolString = defaultTreasure.getString("HuntTool");
        maxRadius = defaultTreasure.getInt("MaxRadius");
        minRadius = defaultTreasure.getInt("MinRadius");
        maxHeight = defaultTreasure.getInt("MaxHeight");
        minHeight = defaultTreasure.getInt("MinHeight");
        maxLight = defaultTreasure.getInt("MaxLight");
        minLight = defaultTreasure.getInt("MinLight");
        checkDelay = fileConfiguration.getInt("Search.CheckDelay");
        closestAfter = fileConfiguration.getInt("Search.ClosestAfter");
        destroyDelay = fileConfiguration.getInt("Destroy.DestroyDelay");
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Error with saving default config!");
        }
        return file;
    }

    private void checkConfiguration(){
        File file = new File(plugin.getDataFolder(), "config.yml");
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        if(!fileConfiguration.contains("Spawn.Chance")){
            fileConfiguration.set("Spawn.Chance", spawnChance);
        }
        if(!fileConfiguration.contains("Spawn.Delay")){
            fileConfiguration.set("Spawn.Delay", spawnDelay);
        }
        if(!fileConfiguration.contains("DefaultTreasureOptions.LifeTime")){
            fileConfiguration.set("DefaultTreasureOptions.LifeTime", lifeTime);
        }
        if(!fileConfiguration.contains("DefaultTreasureOptions.Variety")){
            fileConfiguration.set("DefaultTreasureOptions.Variety", variety);
        }
        if(!fileConfiguration.contains("DefaultTreasureOptions.OOLimit")){
            fileConfiguration.set("DefaultTreasureOptions.OOLimit", OOLimit);
        }
        if(!fileConfiguration.contains("DefaultTreasureOptions.MinPlayers")){
            fileConfiguration.set("DefaultTreasureOptions.MinPlayers", minPlayers);
        }
        if(!fileConfiguration.contains("DefaultTreasureOptions.HuntTool")){
            fileConfiguration.set("DefaultTreasureOptions.HuntTool", huntToolString);
        }
        if(!fileConfiguration.contains("DefaultTreasureOptions.MaxRadius")){
            fileConfiguration.set("DefaultTreasureOptions.MaxRadius", maxRadius);
        }
        if(!fileConfiguration.contains("DefaultTreasureOptions.MinRadius")){
            fileConfiguration.set("DefaultTreasureOptions.MinRadius", minRadius);
        }
        if(!fileConfiguration.contains("DefaultTreasureOptions.MaxHeight")){
            fileConfiguration.set("DefaultTreasureOptions.MaxHeight", maxHeight);
        }
        if(!fileConfiguration.contains("DefaultTreasureOptions.MinHeight")){
            fileConfiguration.set("DefaultTreasureOptions.MinHeight", minHeight);
        }
        if(!fileConfiguration.contains("DefaultTreasureOptions.MaxLight")){
            fileConfiguration.set("DefaultTreasureOptions.MaxLight", maxLight);
        }
        if(!fileConfiguration.contains("DefaultTreasureOptions.MinLight")){
            fileConfiguration.set("DefaultTreasureOptions.MinLight", minLight);
        }
        if(!fileConfiguration.contains("Destroy.DestroyDelay")){
            fileConfiguration.set("Destroy.DestroyDelay", destroyDelay);
        }
        if(!fileConfiguration.contains("Search.CheckDelay")){
            fileConfiguration.set("Search.CheckDelay", checkDelay);
        }
        if(!fileConfiguration.contains("Search.ClosestAfter")){
            fileConfiguration.set("Search.ClosestAfter", closestAfter);
        }
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Error with saving default config!");
        }
    }

    public int getSpawnDelay() {
        return spawnDelay;
    }

    public int getSpawnChance() {
        return spawnChance;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public int getVariety() {
        return variety;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public String getHuntToolString() {
        return huntToolString;
    }

    public int getMaxRadius() {
        return maxRadius;
    }

    public int getMinRadius() {
        return minRadius;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public int getMaxLight(){return maxLight;}

    public int getMinLight(){return minLight;}

    public int getOOLimit(){return OOLimit;}

    public int getDestroyDelay() {
        return destroyDelay;
    }


    public int getCheckDelay(){
        return checkDelay;
    }

    public int getClosestAfter() {
        return closestAfter;
    }

}
