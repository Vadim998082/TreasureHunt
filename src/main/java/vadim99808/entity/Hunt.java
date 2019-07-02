package vadim99808.entity;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import vadim99808.TreasureHunt;
import vadim99808.service.TreasureDestroyer;

import java.util.Map;
import java.util.UUID;


public class Hunt extends Thread{

    private Treasure treasure;
    private ItemStack[] itemStackArray;
    private int value;
    private World world;
    private Block block;
    private int money;
    private int remainingTime;
    private Player claimedPlayer;
    private boolean alreadyClaimed;
    private Map<Player, Integer> closestPlayers;
    private boolean someoneAlreadyClosest;
    private UUID uuid;



    public void run(){
        while(remainingTime > 0){
            try {
                Thread.sleep(1000);
                remainingTime--;
            } catch (InterruptedException e) {
                return;
            }
        }
        if(isInterrupted()){
            return;
        }
        this.setAlreadyClaimed(false);
        TreasureDestroyer treasureDestroyer = new TreasureDestroyer();
        treasureDestroyer.setHunt(this);
        TreasureHunt.getInstance().getBroadcastService().broadcast(TreasureHunt.getInstance().getLocalizationConfigManager().getUnfoundChest(), this, null);
        treasureDestroyer.runTask(TreasureHunt.getInstance());
        TreasureHunt.getInstance().getStatisticsService().addToStatistic(this);
    }

    public Treasure getTreasure() {
        return treasure;
    }

    public void setTreasure(Treasure treasure) {
        this.treasure = treasure;
    }

    public ItemStack[] getItemStackArray() {
        return itemStackArray;
    }

    public void setItemStackArray(ItemStack[] itemStackArray) {
        this.itemStackArray = itemStackArray;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public Player getClaimedPlayer() {
        return claimedPlayer;
    }

    public void setClaimedPlayer(Player claimedPlayer) {
        this.claimedPlayer = claimedPlayer;
    }

    public boolean isAlreadyClaimed() {
        return alreadyClaimed;
    }

    public void setAlreadyClaimed(boolean alreadyClaimed) {
        this.alreadyClaimed = alreadyClaimed;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Map<Player, Integer> getClosestPlayers() {
        return closestPlayers;
    }

    public void setClosestPlayers(Map<Player, Integer> closestPlayers) {
        this.closestPlayers = closestPlayers;
    }

    public boolean isSomeoneAlreadyClosest() {
        return someoneAlreadyClosest;
    }

    public void setSomeoneAlreadyClosest(boolean someoneAlreadyClosest) {
        this.someoneAlreadyClosest = someoneAlreadyClosest;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
