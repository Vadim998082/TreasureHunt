package vadim99808.storage;

import org.bukkit.entity.Player;
import vadim99808.entity.Hunt;
import vadim99808.entity.PlayerDistance;
import vadim99808.service.TreasureDestroyerTimer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Storage {

    private static Storage instance;
    private List<TreasureDestroyerTimer> treasureDestroyerTimerList;
    private List<Hunt> huntList;
    private Map<Player, PlayerDistance> playerDistanceMap;

    private Storage(){
        treasureDestroyerTimerList = new ArrayList<>();
        huntList = new ArrayList<>();
        playerDistanceMap = new HashMap<>();
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

    public Map<Player, PlayerDistance> getPlayerDistanceMap() {
        return playerDistanceMap;
    }
}
