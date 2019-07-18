package vadim99808.entity;

import io.lumine.xikage.mythicmobs.mobs.MythicMob;

public class MMBossMap {
    private MythicMob mythicMob;
    private int amount;
    private int chance;
    private String name;


    public MythicMob getMythicMob() {
        return mythicMob;
    }

    public void setMythicMob(MythicMob mythicMob) {
        this.mythicMob = mythicMob;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getChance() {
        return chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

}
