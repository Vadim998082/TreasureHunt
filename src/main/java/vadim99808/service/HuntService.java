package vadim99808.service;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import vadim99808.TreasureHunt;
import vadim99808.entity.Hunt;
import vadim99808.entity.ItemMap;
import vadim99808.entity.Treasure;
import vadim99808.entity.WorldMap;
import vadim99808.storage.Storage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


public class HuntService{

    private TreasureHunt plugin = TreasureHunt.getInstance();
    private BroadcastService broadcastService = new BroadcastService();

    private ItemStack[] populateItems(Treasure treasure, Hunt hunt){
        Random random = new Random();
        List<ItemStack> itemStackList = new ArrayList<>();
        Map<ItemMap, Integer> rawItems = new HashMap<>();
        int randomValue;
        if(treasure.getMaxValue() - treasure.getMinValue() == 0){
            randomValue = treasure.getMaxValue();
        }else{
            randomValue = random.nextInt(treasure.getMaxValue() - treasure.getMinValue()) + treasure.getMinValue();
        }
        int counterVariety = 0;
        int counterValue = 0;
        int counterOoLimit = 0;
        while(counterVariety < treasure.getVariety() && counterValue < randomValue){
            ItemMap itemMap = treasure.getItemStackList().get(random.nextInt(treasure.getItemStackList().size() - 1));
            if(!itemMap.isOnlyOnce()) {
                if (rawItems.containsKey(itemMap)) {
                    rawItems.replace(itemMap, rawItems.get(itemMap) + 1);
                    counterValue += itemMap.getValue();
                } else {
                    rawItems.put(itemMap, 1);
                    counterVariety++;
                    counterValue += itemMap.getValue();
                }
            }else{
                if(!rawItems.containsKey(itemMap) && counterOoLimit < treasure.getOOLimit()) {
                    rawItems.put(itemMap, 1);
                    counterVariety++;
                    counterOoLimit++;
                    counterValue += itemMap.getValue();
                }
            }
        }
        Set<Map.Entry<ItemMap, Integer>> entrySet =  rawItems.entrySet();
        List<Map.Entry<ItemMap, Integer>> entryList = new ArrayList<>(entrySet);
        int k = entryList.size();
        int onlyOnceCounter = 0;
        for(Map.Entry<ItemMap, Integer> entry: entryList){
            if(entry.getKey().isOnlyOnce()){
                onlyOnceCounter++;
            }
        }
        if(k != onlyOnceCounter){
            while(counterValue < randomValue){
                Map.Entry<ItemMap, Integer> entry;
                if(entryList.size() == 1){
                    entry = entryList.get(0);
                }else{
                    entry = entryList.get(random.nextInt(entryList.size() - 1));
                }
                if(!entry.getKey().isOnlyOnce()){
                    if(entry.getKey().getValue() > randomValue - counterValue){
                        break;
                    }
                    entry.setValue(entry.getValue() + 1);
                    counterValue += entry.getKey().getValue();
                }
            }
        }
        entrySet = new HashSet<>(entryList);
        //System.out.println("Value: " + counterValue + " Variety: " + counterVariety + " OOCounter: " + counterOoLimit);
        for(Map.Entry<ItemMap, Integer> entry : entrySet){
            ItemStack itemStack = entry.getKey().getItem();
            int amount = entry.getValue();
            while(amount >= itemStack.getMaxStackSize()){
                ItemStack itemStackToAdd = itemStack.clone();
                itemStackToAdd.setAmount(itemStack.getMaxStackSize());
                amount -= itemStack.getMaxStackSize();
                itemStackList.add(itemStackToAdd);
            }
            if(amount > 0){
                ItemStack itemStackToAdd = itemStack.clone();
                itemStackToAdd.setAmount(amount);
                itemStackList.add(itemStackToAdd);
            }
        }
        hunt.setValue(counterValue);
        if(itemStackList.size() > 27){
            plugin.getLogger().warning("Too many items try to store in a chest! Please, check the configuration of " + treasure.getName() + " to avoid that problem!");
        }
        while(itemStackList.size() > 27){
            itemStackList.remove(0);
        }
        return itemStackList.toArray(new ItemStack[itemStackList.size()]);
    }

    private Treasure chooseRandomTreasure(){
        Random random = new Random();
        int counter = 0;
        Treasure treasure = null;
        while(counter < 10000){
            treasure = plugin.getTreasureList().get(random.nextInt(plugin.getTreasureList().size()));
            if(!treasure.getChance().isPresent()){
                return treasure;
            }
            if(treasure.getChance().get() == 0){
                continue;
            }
            if(random.nextInt(101) <= treasure.getChance().get()){
                return treasure;
            }
            counter++;
            //System.out.println(counter);
        }
        if(counter >= 10000){
            return null;
        }
        return treasure;
    }

    private int chooseRandomMoney(Treasure treasure){
        Random random = new Random();
        int money;
        if(treasure.getMinMoney() == 0 && treasure.getMaxMoney() == 0){
            money = 0;
        }else{
            if(treasure.getMaxMoney() - treasure.getMinMoney() == 0){
                money = treasure.getMaxMoney();
            }else{
                money = treasure.getMinMoney() + random.nextInt(treasure.getMaxMoney() - treasure.getMinMoney());
            }
        }
        return money;
    }

    private Block chooseRandomLocation(Treasure treasure){
        Random random = new Random();
        List<WorldMap> worldMapList = treasure.getWorldList();
        WorldMap worldMap = treasure.getWorldList().get(random.nextInt(worldMapList.size()));
        Location location = new Location(worldMap.getWorld(), randomXZ(worldMap, random), randomY(worldMap, random), randomXZ(worldMap, random));
        int chunkCounter = 0;
        int iterationCounter = 0;
        while(!checkLocation(location, treasure) && chunkCounter <= 10){
            iterationCounter++;
            if(iterationCounter > 10000){
                location = new Location(worldMap.getWorld(), randomXZ(worldMap, random), randomY(worldMap, random), randomXZ(worldMap, random));
                chunkCounter++;
                iterationCounter = 0;
            }
            location = randomXZChunk(location.getChunk(), random, worldMap);
        }
        if(chunkCounter > 10){
            plugin.getLogger().warning("Treasure " + treasure.getName() + " cannot be spawned due to hard spawn conditions in world " + worldMap.getWorld().getName() + "! Check spawn conditions of it!");
            return null;
        }
        return location.getBlock();
    }

    private Treasure findTreasure(String treasureName){
        List<Treasure> treasureList = plugin.getTreasureList();
        for(Treasure treasure: treasureList){
            if(treasure.getName().equals(treasureName)){
                return treasure;
            }
        }
        return null;
    }

    public boolean startHunt(String treasureName, Location location){
        Treasure treasureToSpawn = findTreasure(treasureName);
        if(treasureToSpawn == null){
            plugin.getLogger().warning("There are no treasure with name " + treasureName + "!");
            return false;
        }
        Hunt hunt = new Hunt();
        ItemStack[] itemStackArray = populateItems(treasureToSpawn, hunt);
        int money = chooseRandomMoney(treasureToSpawn);
        Block block = location.getBlock();
        hunt.setBlock(block);
        hunt.setItemStackArray(itemStackArray);
        hunt.setMoney(money);
        hunt.setTreasure(treasureToSpawn);
        hunt.setWorld(location.getWorld());
        hunt.setRemainingTime(treasureToSpawn.getLifeTime() * 60);
        hunt.setClosestPlayers(new HashMap<>());
        hunt.setSomeoneAlreadyClosest(false);
        hunt.setUuid(UUID.randomUUID());
        TreasurePlacer treasurePlacer = new TreasurePlacer();
        treasurePlacer.setHunt(hunt);
        treasurePlacer.runTask(plugin);
        Storage.getInstance().getHuntList().add(hunt);
        if(treasureToSpawn.getAppearMessage().isPresent()){
            broadcastService.broadcast(treasureToSpawn.getAppearMessage().get(), hunt, null);
        }else{
            broadcastService.broadcast(plugin.getLocalizationConfigManager().getSpawnedChest(), hunt, null);
        }
        plugin.getLogger().info("New hunt started at x: " + hunt.getBlock().getX() + " y: " + hunt.getBlock().getY() + " z: " + hunt.getBlock().getZ() + ". Treasure: " + hunt.getTreasure().getName() + ", value: " + hunt.getValue() + ", world: " + hunt.getWorld().getName() + ".");
        hunt.start();
        return true;
    }

    public boolean startHunt(String treasureName){
        Treasure treasureToSpawn = findTreasure(treasureName);
        if(treasureToSpawn == null){
            plugin.getLogger().warning("There are no treasure with name " + treasureName + "!");
            return false;
        }
        Random random = new Random();
        Hunt hunt = new Hunt();
        ItemStack[] itemStackArray = populateItems(treasureToSpawn, hunt);
        int money = chooseRandomMoney(treasureToSpawn);
        Block block = chooseRandomLocation(treasureToSpawn);
        if(block == null){
            return false;
        }
        hunt.setBlock(block);
        hunt.setItemStackArray(itemStackArray);
        hunt.setMoney(money);
        hunt.setTreasure(treasureToSpawn);
        hunt.setWorld(block.getWorld());
        hunt.setRemainingTime(treasureToSpawn.getLifeTime() * 60);
        hunt.setClosestPlayers(new HashMap<>());
        hunt.setSomeoneAlreadyClosest(false);
        hunt.setUuid(UUID.randomUUID());
        TreasurePlacer treasurePlacer = new TreasurePlacer();
        treasurePlacer.setHunt(hunt);
        treasurePlacer.runTask(plugin);
        Storage.getInstance().getHuntList().add(hunt);
        if(treasureToSpawn.getAppearMessage().isPresent()){
            broadcastService.broadcast(treasureToSpawn.getAppearMessage().get(), hunt, null);
        }else{
            broadcastService.broadcast(plugin.getLocalizationConfigManager().getSpawnedChest(), hunt, null);
        }
        plugin.getLogger().info("New hunt started at x: " + hunt.getBlock().getX() + " y: " + hunt.getBlock().getY() + " z: " + hunt.getBlock().getZ() + ". Treasure: " + hunt.getTreasure().getName() + ", value: " + hunt.getValue() + ", world: " + hunt.getWorld().getName() + ".");
        hunt.start();
        return true;
    }

    public void startHunt(Location location){
        Hunt hunt = new Hunt();
        Random random = new Random();
        Treasure treasure = chooseRandomTreasure();
        if(treasure == null){
            plugin.getLogger().warning("No treasure cannot be spawned due to little chance's. Check chance's of every treasure!");
            return;
        }
        ItemStack[] itemStackArray = populateItems(treasure, hunt);
        List<WorldMap> worldMapList = treasure.getWorldList();
        int money = chooseRandomMoney(treasure);
        Block block = location.getBlock();
        hunt.setBlock(block);
        hunt.setItemStackArray(itemStackArray);
        hunt.setMoney(money);
        hunt.setTreasure(treasure);
        hunt.setWorld(location.getWorld());
        hunt.setRemainingTime(treasure.getLifeTime() * 60);
        hunt.setClosestPlayers(new HashMap<>());
        hunt.setSomeoneAlreadyClosest(false);
        hunt.setUuid(UUID.randomUUID());
        TreasurePlacer treasurePlacer = new TreasurePlacer();
        treasurePlacer.setHunt(hunt);
        treasurePlacer.runTask(plugin);
        Storage.getInstance().getHuntList().add(hunt);
        if(treasure.getAppearMessage().isPresent()){
            broadcastService.broadcast(treasure.getAppearMessage().get(), hunt, null);
        }else{
            broadcastService.broadcast(plugin.getLocalizationConfigManager().getSpawnedChest(), hunt, null);
        }
        plugin.getLogger().info("New hunt started at x: " + hunt.getBlock().getX() + " y: " + hunt.getBlock().getY() + " z: " + hunt.getBlock().getZ() + ". Treasure: " + hunt.getTreasure().getName() + ", value: " + hunt.getValue() + ", world: " + hunt.getWorld().getName() + ".");
        hunt.start();
    }

    public void startHunt(){ //TODO chunk counter
        Hunt hunt = new Hunt();
        Random random = new Random();
        Treasure treasure = chooseRandomTreasure();
        if(treasure == null){
            plugin.getLogger().warning("No treasure cannot be spawned due to little chance's. Check chance's of every treasure!");
            return;
        }
        if(treasure.getMinPlayers() > plugin.getServer().getOnlinePlayers().size()){
            plugin.getLogger().info("Spawning of treasure " + treasure.getName() + " aborted because amount of online players less than MinPlayers.");
            return;
        }
        ItemStack[] itemStackArray = populateItems(treasure, hunt);
        Block block = chooseRandomLocation(treasure);
        if(block == null){
            return;
        }
        int money = chooseRandomMoney(treasure);
        hunt.setBlock(block);
        hunt.setItemStackArray(itemStackArray);
        hunt.setMoney(money);
        hunt.setTreasure(treasure);
        hunt.setWorld(block.getWorld());
        hunt.setRemainingTime(treasure.getLifeTime() * 60);
        hunt.setClosestPlayers(new HashMap<>());
        hunt.setSomeoneAlreadyClosest(false);
        hunt.setUuid(UUID.randomUUID());
        TreasurePlacer treasurePlacer = new TreasurePlacer();
        treasurePlacer.setHunt(hunt);
        treasurePlacer.runTask(plugin);
        Storage.getInstance().getHuntList().add(hunt);
        if(treasure.getAppearMessage().isPresent()){
            broadcastService.broadcast(treasure.getAppearMessage().get(), hunt, null);
        }else{
            broadcastService.broadcast(plugin.getLocalizationConfigManager().getSpawnedChest(), hunt, null);
        }
        plugin.getLogger().info("New hunt started at x: " + hunt.getBlock().getX() + " y: " + hunt.getBlock().getY() + " z: " + hunt.getBlock().getZ() + ". Treasure: " + hunt.getTreasure().getName() + ", value: " + hunt.getValue() + ", world: " + hunt.getWorld().getName() + ".");
        hunt.start();
    }

    private int randomXZ(WorldMap worldMap, Random random){
        int x = random.nextInt(worldMap.getMaxRadius() - worldMap.getMinRadius() + 1);
        x += worldMap.getMinRadius();
        if (random.nextInt(2) == 0) {
            x = 0 - x;
        }
        return x;
    }

    private int randomY(WorldMap worldMap, Random random){
        int y = random.nextInt(worldMap.getMaxHeight() - worldMap.getMinHeight() + 1);
        y += worldMap.getMinHeight();
        return y;
    }

    private Location randomXZChunk(Chunk chunk, Random random, WorldMap worldMap){
        int x = random.nextInt(16);
        x += chunk.getX() * 16;
        int z = random.nextInt(16);
        z += chunk.getZ() * 16;
        int y = random.nextInt(worldMap.getMaxHeight() - worldMap.getMinHeight());
        y += worldMap.getMinHeight();
        return new Location(worldMap.getWorld(), x, y, z);
    }

    private boolean checkLocation(Location location, Treasure treasure){
        if(plugin.getWorldGuardPlugin() != null && plugin.getWorldEditPlugin() != null) {
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionManager regionManager = container.get(BukkitAdapter.adapt(location.getWorld()));
            ApplicableRegionSet regionSet = regionManager.getApplicableRegions(BlockVector3.at(location.getX(), location.getY(), location.getZ()));
            if (regionSet.size() != 0) {
                return false;
            }
        }
        if(location.getBlock().getType() != Material.AIR && location.getBlock().getType() != Material.CAVE_AIR){
            return false;
        }
        if(location.getBlock().getLightLevel() < treasure.getMinLight() || location.getBlock().getLightLevel() > treasure.getMaxLight()){
            return false;
        }
        location = new Location(location.getWorld(), location.getX(), location.getY() - 1, location.getZ());
        if(!location.getBlock().getType().isSolid()){
            return false;
        }
        return true;
    }

    public int amountOfSameHunts(Hunt hunt, World world){
        int amount = 0;
        for(Hunt huntToCompare: Storage.getInstance().getHuntList()){
            if(hunt.getTreasure().getHuntTool().getType().equals(huntToCompare.getTreasure().getHuntTool().getType()) && hunt.getWorld().equals(huntToCompare.getWorld())){
                amount++;
            }
        }
        return amount;
    }

    public int findHunts(World world, Material material, Player player){
        int amount = 0;
        for(Hunt hunt: Storage.getInstance().getHuntList()){
            if(hunt.getTreasure().getPermission().isPresent() && !player.hasPermission(hunt.getTreasure().getPermission().get())){
                continue;
            }
            if(hunt.getWorld().equals(world) && hunt.getTreasure().getHuntTool().getType().equals(material) && !hunt.isAlreadyClaimed()){
                amount++;
            }
        }
        return amount;
    }

    public Optional<Integer> findDistanceToClosestTreasure(World world, Player player, Material huntTool){
        int minDistance = 200000;
        Hunt hunt = null;
        for(Hunt closestHunt: Storage.getInstance().getHuntList()){
            if(closestHunt.getTreasure().getPermission().isPresent() && !player.hasPermission(closestHunt.getTreasure().getPermission().get())){
                continue;
            }
            if(closestHunt.getWorld().equals(world)){
                if(closestHunt.getTreasure().getHuntTool().getType().equals(huntTool) && !closestHunt.isAlreadyClaimed()) {
                    if (Math.sqrt(Math.pow(player.getLocation().getBlockX() - closestHunt.getBlock().getX(), 2) + Math.pow(player.getLocation().getBlockZ() - closestHunt.getBlock().getZ(), 2)) < minDistance) {
                        minDistance = (int) Math.sqrt(Math.pow(player.getLocation().getBlockX() - closestHunt.getBlock().getX(), 2) + Math.pow(player.getLocation().getBlockZ() - closestHunt.getBlock().getZ(), 2));
                        hunt = closestHunt;
                    }
                }
            }
        }
        if(hunt.getTreasure().getAugmentDistance().isPresent() && hunt.getTreasure().getExactDistanceAfter().isPresent() && !plugin.getDefaultConfigManager().isImprovedDistanceCalc()){
            if(minDistance < hunt.getTreasure().getExactDistanceAfter().get()){
                return Optional.of(minDistance);
            }else{
                int divisionResult = minDistance / hunt.getTreasure().getAugmentDistance().get();
                return Optional.of(divisionResult * hunt.getTreasure().getAugmentDistance().get());
            }
        }
        return Optional.of(minDistance);
    }

    public int transformDistance(int distance){
        float coeff = (float) ((distance ^ (1/8))/5 - 0.15);
        coeff = 1 + coeff;
        Random random = new Random();
        float randomCoeff = random.nextInt(11)/100;
        if(random.nextInt(1) == 0) {
            coeff = coeff * randomCoeff + coeff;
        }else{
            coeff = coeff - coeff * randomCoeff;
        }
        return (int) (distance * coeff);
    }

    public Hunt findClosestTreasure(World world, Player player, Material huntTool){
        Hunt closestHunt = null;
        int minDistance = 200000;
        for(Hunt hunt: Storage.getInstance().getHuntList()){
            if(hunt.getTreasure().getPermission().isPresent() && !player.hasPermission(hunt.getTreasure().getPermission().get())){
                continue;
            }
            if(hunt.getWorld().equals(world) && hunt.getTreasure().getHuntTool().getType().equals(huntTool) && !hunt.isAlreadyClaimed()){
                if(Math.sqrt(Math.pow(player.getLocation().getBlockX() - hunt.getBlock().getX(), 2) + Math.pow(player.getLocation().getBlockZ() - hunt.getBlock().getZ(), 2)) < minDistance){
                    minDistance = (int) Math.sqrt(Math.pow(player.getLocation().getBlockX() - hunt.getBlock().getX(), 2) + Math.pow(player.getLocation().getBlockZ() - hunt.getBlock().getZ(), 2));
                    closestHunt = hunt;
                }
            }
        }
        return closestHunt;
    }

    public List<Material> loadHuntToolList(List<Treasure> treasureList){
        List<Material> huntToolList = new ArrayList<>();
        for(Treasure treasure: treasureList){
            if(!huntToolList.contains(treasure.getHuntTool().getType())){
                huntToolList.add(treasure.getHuntTool().getType());
            }
        }
        return huntToolList;
    }

    public void deleteAllHunts(){
        for(TreasureDestroyerTimer treasureDestroyerTimer: Storage.getInstance().getTreasureDestroyerTimerList()){
            treasureDestroyerTimer.interrupt();
        }
        for(Hunt hunt: Storage.getInstance().getHuntList()){
            if(!hunt.isInterrupted()) {
                hunt.interrupt();
            }
//            TreasureDestroyer treasureDestroyer = new TreasureDestroyer();
//            treasureDestroyer.setHunt(hunt);
//            treasureDestroyer.runTask(TreasureHunt.getInstance());
        }
    }

    public void huntTest(int amount){
        plugin.getLogger().info("[Test]Test started! Amount of tested treasure's: " + amount);
        Treasure treasure;
        Random random = new Random();
        Map<String, Integer> treasureMap = new HashMap<>();
        for(Treasure treasureCount: plugin.getTreasureList()){
            treasureMap.put(treasureCount.getName(), 0);
        }
        int spawnedAmount = 0;
        int abortedAmount = 0;
        for(int i = 0; i < amount; i++) {
            int counter = 0;
            while (counter < 10000) {
                treasure = plugin.getTreasureList().get(random.nextInt(plugin.getTreasureList().size()));
                if (!treasure.getChance().isPresent()) {
                    spawnedAmount++;
                    treasureMap.replace(treasure.getName(), treasureMap.get(treasure.getName()) + 1);
                    break;
                }
                if (treasure.getChance().get() == 0) {
                    continue;
                }
                if (random.nextInt(101) <= treasure.getChance().get()) {
                    treasureMap.replace(treasure.getName(), treasureMap.get(treasure.getName()) + 1);
                    spawnedAmount++;
                    break;
                }
                counter++;
                //System.out.println(counter);
            }
            if (counter >= 10000) {
                plugin.getLogger().warning("[Test]No treasure cannot be spawned due to little chance's. Check chance's of every treasure!");
                abortedAmount++;
            }
        }
    	Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        File file = new File(plugin.getTestDirectory(), "[SimpleTest]" + format.format(date) + ".yml");
        try {
            file.createNewFile();
        } catch (IOException e) {
            plugin.getLogger().warning("[Test]Can't create log of the test!");
        }
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        fileConfiguration.set("Amount", amount);
        fileConfiguration.set("Amount.SpawnedAmount", spawnedAmount);
        fileConfiguration.set("Amount.AbortedAmount", abortedAmount);
        for(Treasure treasureCount: plugin.getTreasureList()){
            fileConfiguration.set(treasureCount.getName(), treasureMap.get(treasureCount.getName()));
        }
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("[Test]Error with saving of test!");
        }
        plugin.getLogger().info("[Test]Test successfully finished!");
    }

    public void huntTestLocation(int amount){
        plugin.getLogger().info("[Test]Test started! Amount of tested treasure's: " + amount);
        Treasure treasure = null;
        Random random = new Random();
        Map<String, Integer> treasureMap = new HashMap<>();
        for(Treasure treasureCount: plugin.getTreasureList()){
            treasureMap.put(treasureCount.getName(), 0);
        }
        int spawnedAmount = 0;
        int abortedAmount = 0;
        for(int i = 0; i < amount; i++) {
            int counter = 0;
            while (counter < 10000) {
                treasure = plugin.getTreasureList().get(random.nextInt(plugin.getTreasureList().size()));
                if (!treasure.getChance().isPresent()) {
                    treasureMap.replace(treasure.getName(), treasureMap.get(treasure.getName()) + 1);
                    break;
                }
                if (treasure.getChance().get() == 0) {
                    continue;
                }
                if (random.nextInt(101) <= treasure.getChance().get()) {
                    treasureMap.replace(treasure.getName(), treasureMap.get(treasure.getName()) + 1);
                    break;
                }
                counter++;
                //System.out.println(counter);
            }
            if (counter >= 10000) {
                plugin.getLogger().warning("[Test]No treasure cannot be spawned due to little chance's. Check chance's of every treasure!");
                treasure = null;
            }
            if(treasure != null) {
                if(chooseRandomLocation(treasure) == null){
                    abortedAmount++;
                }else{
                    spawnedAmount++;
                }
            }
        }
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        File file = new File(plugin.getTestDirectory(), "[LocationTest]" + format.format(date) + ".yml");
        try {
            file.createNewFile();
        } catch (IOException e) {
            plugin.getLogger().warning("[Test]Can't create log of the test!");
        }
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        fileConfiguration.set("Amount", amount);
        fileConfiguration.set("Amount.SpawnedAmount", spawnedAmount);
        fileConfiguration.set("Amount.AbortedAmount", abortedAmount);
        for(Treasure treasureCount: plugin.getTreasureList()){
            fileConfiguration.set(treasureCount.getName(), treasureMap.get(treasureCount.getName()));
        }
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("[Test]Error with saving of test!");
        }
        plugin.getLogger().info("[Test]Test successfully finished!");
    }

    public void huntTestLocation(int amount, String treasureName){
        List<Treasure> treasureList = plugin.getTreasureList();
        Treasure treasureToSpawn = null;
        Random random = new Random();
        int abortedAmount = 0;
        int spawnedAmount = 0;
        for(Treasure treasure: treasureList){
            if(treasure.getName().equals(treasureName)){
                treasureToSpawn = treasure;
                break;
            }
        }
        if(treasureToSpawn == null){
            plugin.getLogger().warning("[Test]There are no treasure with name " + treasureName + "! Test aborted!");
            return;
        }
        for(int i = 0; i < amount; i++){
            if(chooseRandomLocation(treasureToSpawn) == null){
                abortedAmount++;
            }else{
                spawnedAmount++;
            }
        }
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        File file = new File(plugin.getTestDirectory(), "[LocationTest]" + format.format(date) + ".yml");
        try {
            file.createNewFile();
        } catch (IOException e) {
            plugin.getLogger().warning("[Test]Can't create log of the test!");
        }
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        fileConfiguration.set("Treasure", treasureName);
        fileConfiguration.set("Amount", amount);
        fileConfiguration.set("Amount.SpawnedAmount", spawnedAmount);
        fileConfiguration.set("Amount.AbortedAmount", abortedAmount);
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("[Test]Error with saving of test!");
        }
        plugin.getLogger().info("[Test]Test successfully finished!");
    }
}
