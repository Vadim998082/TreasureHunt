package vadim99808.storage;

import vadim99808.entity.Hunt;
import vadim99808.service.TreasureDestroyerTimer;

import java.util.ArrayList;
import java.util.List;

public class Storage {

    private static Storage instance;
    private List<TreasureDestroyerTimer> treasureDestroyerTimerList;
    private List<Hunt> huntList;

    private Storage(){
        treasureDestroyerTimerList = new ArrayList<>();
        huntList = new ArrayList<>();
    }

    public static Storage getInstance() {
        if(instance == null){
            instance = new Storage();
        }
        return instance;
    }

    public List<TreasureDestroyerTimer> getTreasureDestroyerTimerList() {
        return treasureDestroyerTimerList;
    }

    public List<Hunt> getHuntList() {
        return huntList;
    }
}
