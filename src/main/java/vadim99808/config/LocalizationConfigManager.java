package vadim99808.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import vadim99808.TreasureHunt;

import java.io.File;
import java.io.IOException;

public class LocalizationConfigManager {

    private TreasureHunt plugin = TreasureHunt.getInstance();

    private String spawnedChest;
    private String playerCloseToChest;
    private String youAreClosest;
    private String noLongerClosest;
    private String playerFoundChest;
    private String moneyFound;
    private String unfoundChest;
    private String alreadyClaimed;
    private String closestChest;
    private String noChests;
    private String fastCheckLocation;
    private String huntTop;
    private String personalStat;
    private String dontHaveStat;

    public void loadConfiguration(){
        File file = new File(plugin.getDataFolder(), "localization.yml");
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        spawnedChest = fileConfiguration.getString("SpawnedChest");
        playerCloseToChest = fileConfiguration.getString("PlayerCloseToChest");
        youAreClosest = fileConfiguration.getString("YouAreClosest");
        noLongerClosest = fileConfiguration.getString("NoLongerClosest");
        playerFoundChest = fileConfiguration.getString("PlayerFoundChest");
        moneyFound = fileConfiguration.getString("MoneyFound");
        unfoundChest = fileConfiguration.getString("UnfoundChest");
        alreadyClaimed = fileConfiguration.getString("AlreadyClaimed");
        closestChest = fileConfiguration.getString("ClosestChest");
        noChests = fileConfiguration.getString("NoChests");
        fastCheckLocation = fileConfiguration.getString("FastCheckLocation");
        huntTop = fileConfiguration.getString("HuntTop");
        personalStat = fileConfiguration.getString("PersonalStat");
        dontHaveStat = fileConfiguration.getString("DontHaveStat");
    }

    public void initializeLocalization(){
        File file = new File(plugin.getDataFolder(), "localization.yml");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().warning("Error with creating default localization file!");
            }
        }
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        if(!fileConfiguration.contains("SpawnedChest")) {
            fileConfiguration.set("SpawnedChest", "&fA <name> &ftreasure chest (Value:<value>) appeared in &9<worldname>&f!");
        }
        if(!fileConfiguration.contains("PlayerCloseToChest")) {
            fileConfiguration.set("PlayerCloseToChest", "&fA player is very close to the <name> value <value> &fchest!");
        }
        if(!fileConfiguration.contains("YouAreClosest")) {
            fileConfiguration.set("YouAreClosest", "&aYou are now the closest player to the <name> value <value> &achest!");
        }
        if(!fileConfiguration.contains("NoLongerClosest")) {
            fileConfiguration.set("NoLongerClosest", "&cYou are no longer the closest player to the <name> value <value> &cchest!");
        }
        if(!fileConfiguration.contains("PlayerFoundChest")) {
            fileConfiguration.set("PlayerFoundChest", "&fThe chest of value &a<value> &fhas been claimed by &2<pname> &fat &a<location>&f!");
        }
        if(!fileConfiguration.contains("MoneyFound")) {
            fileConfiguration.set("MoneyFound", "&aYou found <money> in the chest!");
        }
        if(!fileConfiguration.contains("UnfoundChest")) {
            fileConfiguration.set("UnfoundChest", " &fThe chest of value &a<value> &fhas &cfaded &fwithout being found!");
        }
        if(!fileConfiguration.contains("AlreadyClaimed")) {
            fileConfiguration.set("AlreadyClaimed", "&7This <name> &7chest has already been claimed by &a<pname>&7!");
        }
        if(!fileConfiguration.contains("ClosestChest")) {
            fileConfiguration.set("ClosestChest", "&7The closest chest (of <numhunts>) is currently &9<distance> &7blocks away.");
        }
        if(!fileConfiguration.contains("NoChests")) {
            fileConfiguration.set("NoChests", "&7No hunts are currently active in this world!");
        }
        if(!fileConfiguration.contains("FastCheckLocation")){
            fileConfiguration.set("FastCheckLocation", "&4You can check distance every <checkdelay> seconds!");
        }
        if(!fileConfiguration.contains("HuntTop")){
            fileConfiguration.set("HuntTop", "&d<position>. <pname>... Total amount: <totalAmount>... Total value: <totalValue>...");
        }
        if(!fileConfiguration.contains("PersonalStat")){
            fileConfiguration.set("PersonalStat", "&dTotal amount: <totalAmount>... Total value: <totalValue>...");
        }
        if(!fileConfiguration.contains("DontHaveStat")){
            fileConfiguration.set("DontHaveStat", "&dYou have not found the chests!");
        }
        if(!fileConfiguration.contains("TooFarFromAllChests")){
            fileConfiguration.set("TooFarFromAllChests", "&dYou are to far from all chests! (<numhunts> are active)");
        }
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Error with saving default localization file!");
        }
    }

    public String getSpawnedChest() {
        return spawnedChest;
    }


    public String getPlayerCloseToChest() {
        return playerCloseToChest;
    }

    public String getYouAreClosest() {
        return youAreClosest;
    }

    public String getNoLongerClosest() {
        return noLongerClosest;
    }

    public String getPlayerFoundChest() {
        return playerFoundChest;
    }

    public String getMoneyFound() {
        return moneyFound;
    }

    public String getUnfoundChest() {
        return unfoundChest;
    }

    public String getAlreadyClaimed() {
        return alreadyClaimed;
    }

    public String getClosestChest() {
        return closestChest;
    }

    public String getNoChests() {
        return noChests;
    }

    public String getFastCheckLocation() {
        return fastCheckLocation;
    }

    public String getHuntTop() {
        return huntTop;
    }

    public String getPersonalStat() {
        return personalStat;
    }

    public String getDontHaveStat() {
        return dontHaveStat;
    }

}
