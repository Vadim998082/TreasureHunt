package vadim99808.entity;

import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

public class Treasure {
    private String displayName;
    private int OOLimit;
    private String name;
    private int value;
    private int maxValue;
    private int minValue;
    private Optional<String> permission;
    private Optional<Integer> chance;
    private Optional<String> appearMessage;
    private Optional<Integer> exactDistanceAfter;
    private Optional<Integer> augmentDistance;
    private int minMoney;
    private int maxMoney;
    private int lifeTime;
    private int variety;
    private int minPlayers;
    private int minLight;
    private int maxLight;
    private ItemStack huntTool;
    private List<WorldMap> worldList;
    private List<ItemMap> itemStackList;
    private Optional<String> command;
    private Optional<String> commandExecutor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getMinMoney() {
        return minMoney;
    }

    public void setMinMoney(int minMoney) {
        this.minMoney = minMoney;
    }

    public int getMaxMoney() {
        return maxMoney;
    }

    public void setMaxMoney(int maxMoney) {
        this.maxMoney = maxMoney;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }

    public int getVariety() {
        return variety;
    }

    public void setVariety(int variety) {
        this.variety = variety;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public ItemStack getHuntTool() {
        return huntTool;
    }

    public void setHuntTool(ItemStack huntTool) {
        this.huntTool = huntTool;
    }

    public List<WorldMap> getWorldList() {
        return worldList;
    }

    public void setWorldList(List<WorldMap> worldList) {
        this.worldList = worldList;
    }

    public List<ItemMap> getItemStackList() {
        return itemStackList;
    }

    public void setItemStackList(List<ItemMap> itemStackList) {
        this.itemStackList = itemStackList;
    }

    public int getOOLimit() {
        return OOLimit;
    }

    public void setOOLimit(int OOLimit) {
        this.OOLimit = OOLimit;
    }

    public int getMinLight() {
        return minLight;
    }

    public void setMinLight(int minLight) {
        this.minLight = minLight;
    }

    public int getMaxLight() {
        return maxLight;
    }

    public void setMaxLight(int maxLight) {
        this.maxLight = maxLight;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public Optional<String> getPermission() {
        return permission;
    }

    public void setPermission(Optional<String> permission) {
        this.permission = permission;
    }

    public Optional<Integer> getChance() {
        return chance;
    }

    public void setChance(Optional<Integer> chance) {
        this.chance = chance;
    }

    public Optional<String> getAppearMessage() {
        return appearMessage;
    }

    public void setAppearMessage(Optional<String> appearMessage) {
        this.appearMessage = appearMessage;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Optional<Integer> getExactDistanceAfter() {
        return exactDistanceAfter;
    }

    public void setExactDistanceAfter(Optional<Integer> exactDistanceAfter) {
        this.exactDistanceAfter = exactDistanceAfter;
    }

    public Optional<Integer> getAugmentDistance() {
        return augmentDistance;
    }

    public void setAugmentDistance(Optional<Integer> augmentDistance) {
        this.augmentDistance = augmentDistance;
    }

    public Optional<String> getCommand() {
        return command;
    }

    public void setCommand(Optional<String> command) {
        this.command = command;
    }

    public Optional<String> getCommandExecutor() {
        return commandExecutor;
    }

    public void setCommandExecutor(Optional<String> commandExecutor) {
        this.commandExecutor = commandExecutor;
    }
}
