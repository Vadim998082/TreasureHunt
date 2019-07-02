package vadim99808.entity;

import org.bukkit.entity.Player;

public class PlayerDistance {
    private Player player;
    private int realDistance;
    private int recalcDistance;
    private Hunt hunt;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getRealDistance() {
        return realDistance;
    }

    public void setRealDistance(int realDistance) {
        this.realDistance = realDistance;
    }

    public int getRecalcDistance() {
        return recalcDistance;
    }

    public void setRecalcDistance(int recalcDistance) {
        this.recalcDistance = recalcDistance;
    }

    public Hunt getHunt() {
        return hunt;
    }

    public void setHunt(Hunt hunt) {
        this.hunt = hunt;
    }
}
