package vadim99808.service;

import org.bukkit.entity.Player;
import vadim99808.TreasureHunt;
import vadim99808.entity.Hunt;
import vadim99808.entity.PlayerDistance;
import vadim99808.storage.Storage;

public class UserDistanceService {
    private TreasureHunt plugin = TreasureHunt.getInstance();
    private HuntService huntService = TreasureHunt.getInstance().getHuntService();

    public int checkPlayerDistance(Player player, Hunt hunt, int realDistance, int recalcDistance){
        if(Storage.getInstance().getPlayerDistanceMap().containsKey(player)){
            if(Storage.getInstance().getPlayerDistanceMap().get(player).getHunt().getUuid().equals(hunt.getUuid())){
                if(Storage.getInstance().getPlayerDistanceMap().get(player).getRealDistance() > realDistance && Storage.getInstance().getPlayerDistanceMap().get(player).getRecalcDistance() < recalcDistance){
                    int newDistance;
                    int i = 0;
                    do{
                        newDistance = huntService.transformDistance(realDistance);
                        i++;
                    }while (Storage.getInstance().getPlayerDistanceMap().get(player).getRecalcDistance() < newDistance && i < 40);
                    System.out.println("iter: " + i);
                    savePlayerDistance(player,hunt, realDistance, newDistance);
                    return newDistance;
                }else{
                    if(Storage.getInstance().getPlayerDistanceMap().get(player).getRealDistance() < realDistance && Storage.getInstance().getPlayerDistanceMap().get(player).getRecalcDistance() > recalcDistance){
                        int newDistance;
                        int i = 0;
                        do{
                            newDistance = huntService.transformDistance(realDistance);
                            i++;
                        }while (Storage.getInstance().getPlayerDistanceMap().get(player).getRecalcDistance() > newDistance && i < 40);
                        System.out.println("iter: " + i);
                        savePlayerDistance(player,hunt, realDistance, newDistance);
                        return newDistance;
                    }
                    if(Storage.getInstance().getPlayerDistanceMap().get(player).getRealDistance() == realDistance){
                        return Storage.getInstance().getPlayerDistanceMap().get(player).getRecalcDistance();
                    }
                    savePlayerDistance(player,hunt, realDistance, recalcDistance);
                    return recalcDistance;
                }
            }else{
                savePlayerDistance(player, hunt, realDistance, recalcDistance);
                return recalcDistance;
            }
        }else{
            savePlayerDistance(player, hunt, realDistance, recalcDistance);
            return recalcDistance;
        }
    }

    private void savePlayerDistance(Player player, Hunt hunt, int realDistance, int recalcDistance) {
        PlayerDistance playerDistance = new PlayerDistance();
        playerDistance.setHunt(hunt);
        playerDistance.setPlayer(player);
        playerDistance.setRealDistance(realDistance);
        playerDistance.setRecalcDistance(recalcDistance);
        Storage.getInstance().getPlayerDistanceMap().put(player, playerDistance);
    }
}
