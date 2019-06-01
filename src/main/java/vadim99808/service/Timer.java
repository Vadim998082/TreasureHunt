package vadim99808.service;

import vadim99808.TreasureHunt;

import java.util.Random;

public class Timer extends Thread{

    private TreasureHunt plugin = TreasureHunt.getInstance();
    private HuntService huntService = TreasureHunt.getInstance().getHuntService();

    public void run(){
        while(true){
            try {
                Thread.sleep(1000 * plugin.getDefaultConfigManager().getSpawnDelay());
                //System.out.println("End of delay");
                if(new Random().nextInt(100) + 1 <= plugin.getDefaultConfigManager().getSpawnChance() && plugin.getTreasureList().size() > 0){
                    //System.out.println("Attempt to spawn!");
                    plugin.getHuntService().startHunt();
                }
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
