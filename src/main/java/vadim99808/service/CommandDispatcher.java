package vadim99808.service;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import vadim99808.TreasureHunt;
import vadim99808.entity.Hunt;

public class CommandDispatcher {

    private TreasureHunt plugin = TreasureHunt.getInstance();

    public boolean dispatchCommand(Player player, Hunt hunt){
        if(hunt.getTreasure().getCommand().isPresent()){
            if(hunt.getTreasure().getCommandExecutor().isPresent()){
                if(hunt.getTreasure().getCommandExecutor().get().equals("player")){
                    plugin.getServer().dispatchCommand(player, hunt.getTreasure().getCommand().get());
                    return true;
                }
                if(hunt.getTreasure().getCommandExecutor().get().equals("console")){
                    player.getServer().dispatchCommand(Bukkit.getConsoleSender(), hunt.getTreasure().getCommand().get());
                    return true;
                }
                plugin.getLogger().warning("Unknown command executor: " + hunt.getTreasure().getCommandExecutor().get());
                return false;
            }else{
                plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), hunt.getTreasure().getCommand().get());
                return true;
            }
        }else{
            return false;
        }
    }
}
