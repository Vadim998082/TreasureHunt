package vadim99808.executors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vadim99808.TreasureHunt;

public class StartHuntExecutor implements CommandExecutor {

    private TreasureHunt plugin = TreasureHunt.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            Player player = ((Player) commandSender).getPlayer();
            if(!player.hasPermission("th.spawn")){
                player.sendMessage(ChatColor.RED + "You don't have permission to do that!");
                return false;
            }
        }else{
            plugin.getLogger().info("This command only for players!");
            return false;
        }
        if(plugin.getTreasureList().size() == 0){
            commandSender.sendMessage(ChatColor.RED + "There are no chests to spawn! Look into log.");
            return false;
        }
        if(strings.length != 0){
            if(strings[0].equals("here")){
                if(strings.length == 2){
                    if(!plugin.getHuntService().startHunt(strings[1], ((Player) commandSender).getLocation())){
                        commandSender.sendMessage(ChatColor.RED + "There are some problems with spawning of that treasure, check the name of treasure!");
                        return false;
                    }else{
                        return true;
                    }
                }else{
                    plugin.getHuntService().startHunt(((Player) commandSender).getLocation());
                    return true;
                }
            }else{
                if(!plugin.getHuntService().startHunt(strings[0])){
                    commandSender.sendMessage(ChatColor.RED + "There are some problems with spawning of that treasure, check the name of treasure!");
                    return false;
                }else{
                    return true;
                }
            }
        }
        plugin.getHuntService().startHunt();
        return true;
    }
}
