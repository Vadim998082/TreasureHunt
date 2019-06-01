package vadim99808.service;

import vadim99808.TreasureHunt;
import vadim99808.entity.Hunt;
import vadim99808.storage.Storage;

public class TreasureDestroyerTimer extends Thread{

    private int delay;
    private TreasureHunt plugin = TreasureHunt.getInstance();
    private Hunt hunt;

    public void run(){
        try {
            Thread.sleep(delay * 60000);
        } catch (InterruptedException e) {
            return;
        }
        TreasureDestroyer treasureDestroyer = new TreasureDestroyer();
        treasureDestroyer.setHunt(hunt);
        Storage.getInstance().getTreasureDestroyerTimerList().remove(this);
        treasureDestroyer.runTask(plugin);
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setHunt(Hunt hunt) {
        this.hunt = hunt;
    }
}
