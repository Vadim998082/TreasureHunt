package vadim99808.config;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import javafx.beans.property.ObjectProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import vadim99808.TreasureHunt;
import vadim99808.entity.ItemMap;
import vadim99808.entity.Treasure;
import vadim99808.entity.WorldMap;

import java.io.File;
import java.util.*;

public class ChestConfigManager {

    private static TreasureHunt plugin = TreasureHunt.getInstance();
    private static DefaultConfigManager defaultConfigManager = TreasureHunt.getInstance().getDefaultConfigManager();

    public List<Treasure> loadAllTreasures(){
        List<Treasure> treasureList = new ArrayList<>();
        File chestsDirectory = plugin.getChestsDirectory();
        File[] files = chestsDirectory.listFiles();
        if(files.length == 0){
            plugin.getLogger().info("There are no treasure's to load!");
            return treasureList;
        }
        for (File file : files) {
            Treasure treasure = loadTreasureFromFile(file);
            if (treasure != null) {
                treasureList.add(treasure);
            } else {
                plugin.getLogger().warning("Treasure with filename " + file.getName() + " not loaded!");
            }
        }
        return treasureList;
    }

    private Treasure loadTreasureFromFile(File file){
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        Treasure treasure = new Treasure();
        String name;
        String displayName;
        Optional<String> permission;
        Optional<Integer> exactDistanceAfter;
        Optional<Integer> augmentDistance;
//        int value;
        int maxValue;
        int minValue;
        int minMoney = 0;
        int maxMoney = 0;
        int lifeTime;
        Optional<Integer> chance;
        Optional<String> appearMessage;
        int variety;
        int OOLimit;
        int minPlayers;
        int mimLight;
        int maxLight;
        List<String> worldListString;
        Set<String> itemSetString;
        Set<String> onlyOnceSetString;
        String huntToolString;
        Optional<String> command;
        Optional<String> commandExecutor;
        String biomeString;
        Optional<Biome> biome;
        if(configuration.contains("Name")){
            name = configuration.getString("Name");
        }else{
            plugin.getLogger().warning("Treasure with filename '" + file.getName() + "' does not have NAME! Loading of it aborted!");
            return null;
        }
        if(configuration.contains("DisplayName")){
            displayName = configuration.getString("DisplayName");
        }else{
            plugin.getLogger().warning("Treasure with filename '" + file.getName() + "' does not have DISPLAYNAME! Loading of it aborted!");
            return null;
        }
//        if(configuration.contains("Value")){
//            value = configuration.getInt("Value");
//        }else{
//            plugin.getLogger().warning("Treasure with filename '" + file.getName() + "' does not have VALUE! Loading of it aborted!");
//            return null;
//        }
        if(configuration.contains("MaxValue")){
            maxValue = configuration.getInt("MaxValue");
        }else{
            plugin.getLogger().warning("Treasure with filename '" + file.getName() + "' does not have MaxValue! Loading of it aborted!");
            return null;
        }
        if(configuration.contains("MinValue")){
            minValue = configuration.getInt("MinValue");
        }else{
            plugin.getLogger().warning("Treasure with filename '" + file.getName() + "' does not have MinValue! Loading of it aborted!");
            return null;
        }
        if(configuration.contains("Chance")){
            chance = Optional.of(configuration.getInt("Chance"));
        }else{
            chance = Optional.empty();
        }
        if(maxValue < minValue){
            plugin.getLogger().warning("MaxValue must be bigger than MinValue! Loading of treasure with filename " + file.getName() + " aborted!");
            return null;
        }
        if(maxValue == 0 || minValue == 0){
            plugin.getLogger().warning("MaxValue and MinValue should be not 0! Loading of treasure with filename " + file.getName() + " aborted!");
            return null;
        }
        if(configuration.contains("MinMoney")){
            minMoney = configuration.getInt("MinMoney");
        }
        if(configuration.contains("MaxMoney")){
            maxMoney = configuration.getInt("MaxMoney");
        }
        if(maxMoney < minMoney){
            plugin.getLogger().warning("MaxMoney should be bigger or the same of MinMoney! Loading of treasure with filename " + file.getName() + " aborted!");
            return null;
        }
        if(configuration.contains("LifeTime")){
            lifeTime = configuration.getInt("LifeTime");
        }else{
            plugin.getLogger().info("Treasure with filename '" + file.getName() + "' does not have own LIFETIME. Lifetime will be used from default configuration.");
            lifeTime = defaultConfigManager.getLifeTime();
        }
        if(configuration.contains("Variety")){
            variety = configuration.getInt("Variety");
        }else{
            plugin.getLogger().info("Treasure with filename '" + file.getName() + "' does not have own VARIETY. Variety will be used from default configuration.");
            variety = defaultConfigManager.getVariety();
        }
        if(configuration.contains("OOLimit")){
            OOLimit = configuration.getInt("OOLimit");
        }else{
            plugin.getLogger().info("Treasure with filename '" + file.getName() + "' does not have own OOLimit. OOLimit will be used from default configuration.");
            OOLimit = defaultConfigManager.getOOLimit();
        }
        if(configuration.contains("MinPlayers")){
            minPlayers = configuration.getInt("MinPlayers");
        }else{
            plugin.getLogger().info("Treasure with filename '" + file.getName() + "' does not have own MINPLAYERS. MinPlayers will be used from default configuration.");
            minPlayers = defaultConfigManager.getMinPlayers();
        }
        if(configuration.contains("MaxLight")){
            maxLight = configuration.getInt("MaxLight");
        }else{
            plugin.getLogger().info("Treasure with filename '" + file.getName() + "' does not have own MAXLIGHT. MaxLight will be used from default configuration.");
            maxLight = defaultConfigManager.getMaxLight();
        }
        if(configuration.contains("MinLight")){
            mimLight = configuration.getInt("MinLight");
        }else{
            plugin.getLogger().info("Treasure with filename '" + file.getName() + "' does not have own MINLIGHT. MinLight will be used from default configuration.");
            mimLight = defaultConfigManager.getMinLight();
        }
        if(configuration.contains("Worlds")){
            worldListString = configuration.getStringList("Worlds");
            if(worldListString.isEmpty()){
                plugin.getLogger().warning("There are no worlds defined in treasure '" + file.getName() +"'. Loading of it aborted!");
                return null;
            }
        }else{
            plugin.getLogger().warning("Treasure with filename '" + file.getName() + "' does not have WORLDS! Loading of it aborted!");
            return null;
        }
        if(configuration.contains("HuntTool")){
            huntToolString = configuration.getString("HuntTool");
        }else{
            plugin.getLogger().info("Treasure with filename '" + file.getName() + "' does not have own HUNTTOOL. HuntTool will be used from default configuration.");
            huntToolString = defaultConfigManager.getHuntToolString();
        }
        if(configuration.contains("Items")){
            itemSetString = configuration.getConfigurationSection("Items").getKeys(false);
        }else{
            plugin.getLogger().info("Treasure with filename '" + file.getName() + "' does not have ITEMS!");
            itemSetString = new HashSet<String>();
        }
        if(configuration.contains("OnlyOnce")){
            onlyOnceSetString = configuration.getConfigurationSection("OnlyOnce").getKeys(false);
        }else{
            plugin.getLogger().info("Treasure with filename '" + file.getName() + "' does not have ONLYONCE ITEMS!");
            onlyOnceSetString = new HashSet<String>();
        }
        if(configuration.contains("Permission")){
            permission = Optional.ofNullable(configuration.getString("Permission"));
        }else{
            permission = Optional.empty();
        }
        if(configuration.contains("AppearMessage")){
            appearMessage = Optional.ofNullable(configuration.getString("AppearMessage"));
        }else{
            appearMessage = Optional.empty();
        }
        if(configuration.contains("ExactDistanceAfter")){
            exactDistanceAfter = Optional.ofNullable(configuration.getInt("ExactDistanceAfter"));
        }else{
            exactDistanceAfter = Optional.empty();
        }
        if(configuration.contains("AugmentDistance")){
            augmentDistance = Optional.ofNullable(configuration.getInt("AugmentDistance"));
        }else{
            augmentDistance = Optional.empty();
        }
        if(configuration.contains("Command")){
            command = Optional.ofNullable(configuration.getString("Command"));
        }else{
            command = Optional.empty();
        }
        if(configuration.contains("CommandExecutor")){
            commandExecutor = Optional.ofNullable(configuration.getString("CommandExecutor"));
            if(!commandExecutor.get().equals("player") && !commandExecutor.get().equals("console")){
                plugin.getLogger().warning("Unknown command executor! Check " + file.getName() + " file!");
            }
        }else{
            commandExecutor = Optional.empty();
        }
        if(configuration.contains("Biome")){
            biomeString = configuration.getString("Biome");
            try {
                biome = Optional.of(Biome.valueOf(biomeString.toUpperCase()));
            }catch (IllegalArgumentException exception){
                biome = Optional.empty();
                plugin.getLogger().warning("Unknown biome " + biomeString + " in " + file.getName() + " file! Biome setted to null!");
            }
        }else{
            biome = Optional.empty();
        }

        List<ItemMap> itemStackList = new ArrayList<>();
        for(String stringItem : itemSetString){
            if(stringItem.startsWith("MM_") && plugin.getMythicMobs() != null){
                Optional<MythicItem> maybeItem = MythicMobs.inst().getItemManager().getItem(stringItem.substring(3));
                if(maybeItem.isPresent()){
                    MythicItem mythicItem = maybeItem.get();
                    ItemMap itemMap = new ItemMap();
                    itemMap.setItem(BukkitAdapter.adapt(mythicItem.generateItemStack(1)));
                    itemMap.setValue(configuration.getInt("Items." + stringItem));
                    itemMap.setOnlyOnce(false);
                    itemStackList.add(itemMap);
                    continue;
                }else{
                    plugin.getLogger().warning("Unknown MythicItem with name " + stringItem.substring(3));
                }
            }
            if(Material.getMaterial(stringItem) != null) {
                ItemMap itemMap = new ItemMap();
                itemMap.setItem(new ItemStack(Material.getMaterial(stringItem)));
                itemMap.setValue(configuration.getInt("Items." + stringItem));
                itemMap.setOnlyOnce(false);
                itemStackList.add(itemMap);
                continue;
            }
            if(plugin.getEnchantedItems().containsKey(stringItem)){
                ItemMap itemMap = new ItemMap();
                itemMap.setItem(plugin.getEnchantedItems().get(stringItem));
                itemMap.setValue(configuration.getInt("Items." + stringItem));
                itemMap.setOnlyOnce(false);
                itemStackList.add(itemMap);
            }
        }
        for(String stringItem : onlyOnceSetString){
            if(stringItem.startsWith("MM_") && plugin.getMythicMobs() != null){
                Optional<MythicItem> maybeItem = MythicMobs.inst().getItemManager().getItem(stringItem.substring(3));
                if(maybeItem.isPresent()){
                    MythicItem mythicItem = maybeItem.get();
                    ItemMap itemMap = new ItemMap();
                    itemMap.setItem(BukkitAdapter.adapt(mythicItem.generateItemStack(1)));
                    itemMap.setValue(configuration.getInt("OnlyOnce." + stringItem));
                    itemMap.setOnlyOnce(true);
                    itemStackList.add(itemMap);
                    continue;
                }else{
                    plugin.getLogger().warning("Unknown MythicItem with name " + stringItem.substring(3));
                }
            }
            if(Material.getMaterial(stringItem) != null){
                ItemMap itemMap = new ItemMap();
                itemMap.setItem(new ItemStack(Material.getMaterial(stringItem)));
                itemMap.setValue(configuration.getInt("OnlyOnce." + stringItem));
                itemMap.setOnlyOnce(true);
                itemStackList.add(itemMap);
                continue;
            }
            if(plugin.getEnchantedItems().containsKey(stringItem)){
                ItemMap itemMap = new ItemMap();
                itemMap.setItem(plugin.getEnchantedItems().get(stringItem));
                itemMap.setValue(configuration.getInt("OnlyOnce." + stringItem));
                itemMap.setOnlyOnce(true);
                itemStackList.add(itemMap);
            }else{
                plugin.getLogger().warning("Unknown item in file " + file.getName() + " with name " + stringItem + ".");
            }
        }
        ItemStack huntTool;
        if(Material.getMaterial(huntToolString) != null){
            huntTool = new ItemStack(Material.getMaterial(huntToolString));
        }else{
            plugin.getLogger().warning("Unknown hunttool " + huntToolString + ". Loading of treasure " + name + " aborted!");
            return null;
        }
        List<WorldMap> worldMapList = new ArrayList<>();
        for(String worldString : worldListString){
            List<World> worldList = Bukkit.getWorlds();
            for(World world : worldList){
                if(world.getName().equals(worldString)){
                    WorldMap worldMap = new WorldMap();
                    int maxRadius;
                    int minRadius;
                    int maxHeight;
                    int minHeight;
                    if(configuration.contains(worldString + ".MaxRadius")){
                        maxRadius = configuration.getInt(worldString + ".MaxRadius");
                    }else{
                        plugin.getLogger().info("Missing MaxRadius for treasure " + name + ". Value will be set by default config.");
                        maxRadius = defaultConfigManager.getMaxRadius();
                    }
                    if(configuration.contains(worldString + ".MinRadius")){
                        minRadius = configuration.getInt(worldString + ".MinRadius");
                    }else{
                        plugin.getLogger().info("Missing MinRadius for treasure " + name + ". Value will be set by default config.");
                        minRadius = defaultConfigManager.getMinRadius();
                    }
                    if(configuration.contains(worldString + ".MaxHeight")){
                        maxHeight = configuration.getInt(worldString + ".MaxHeight");
                    }else{
                        plugin.getLogger().info("Missing MaxHeight for treasure " + name + ". Value will be set by default config.");
                        maxHeight = defaultConfigManager.getMaxHeight();
                    }
                    if(configuration.contains(worldString + ".MinHeight")){
                        minHeight = configuration.getInt(worldString + ".MinHeight");
                    }else{
                        plugin.getLogger().info("Missing MinHeight for treasure " + name + " of world " + world.getName() + ". Value will be set by default config.");
                        minHeight = defaultConfigManager.getMinHeight();
                    }
                    if(maxHeight < minHeight){
                        plugin.getLogger().warning("MinHeight bigger than MaxHeight in treasure with filename " + file.getName() + " in world " + world.getName() + "! Loading of it aborted!");
                        return null;
                    }
                    if(maxRadius < minRadius){
                        plugin.getLogger().warning("MinRadius bigger than MaxRadius in treasure with filename " + file.getName() + " in world " + world.getName() + "! Loading of it aborted!");
                        return null;
                    }
                    worldMap.setMaxHeight(maxHeight);
                    worldMap.setMinHeight(minHeight);
                    worldMap.setMaxRadius(maxRadius);
                    worldMap.setMinRadius(minRadius);
                    worldMap.setWorld(world);
                    worldMapList.add(worldMap);
                }
            }
        }
        treasure.setHuntTool(huntTool);
        treasure.setItemStackList(itemStackList);
        treasure.setLifeTime(lifeTime);
        treasure.setMaxMoney(maxMoney);
        treasure.setMinMoney(minMoney);
        treasure.setMinPlayers(minPlayers);
        treasure.setName(name);
        treasure.setDisplayName(displayName);
//        treasure.setValue(value);
        treasure.setChance(chance);
        treasure.setPermission(permission);
        treasure.setMaxValue(maxValue);
        treasure.setMinValue(minValue);
        treasure.setVariety(variety);
        treasure.setOOLimit(OOLimit);
        treasure.setWorldList(worldMapList);
        treasure.setMaxLight(maxLight);
        treasure.setMinLight(mimLight);
        treasure.setAppearMessage(appearMessage);
        treasure.setExactDistanceAfter(exactDistanceAfter);
        treasure.setAugmentDistance(augmentDistance);
        treasure.setCommand(command);
        treasure.setCommandExecutor(commandExecutor);
        treasure.setBiome(biome);
        return treasure;
    }

}
