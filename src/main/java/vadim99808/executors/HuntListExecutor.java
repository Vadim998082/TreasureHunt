package vadim99808.executors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vadim99808.TreasureHunt;
import vadim99808.entity.Hunt;
import vadim99808.storage.Storage;

public class HuntListExecutor implements CommandExecutor {

    private TreasureHunt plugin = TreasureHunt.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            Player player = ((Player) commandSender).getPlayer();
            if(!player.hasPermission("th.huntlist")){
                player.sendMessage(ChatColor.RED + "You don't have permission to do that!");
                return false;
            }
        }
        if(Storage.getInstance().getHuntList().size() == 0){
            commandSender.sendMessage(ChatColor.GRAY + "There are no available hunts!");
            return true;
        }
        int i = 1;
        for(Hunt hunt: Storage.getInstance().getHuntList()){
            if(!hunt.isAlreadyClaimed()){
                commandSender.sendMessage(i + ". Name: " + ChatColor.GREEN + hunt.getTreasure().getName() + ChatColor.WHITE + " permission: " + ChatColor.RED + hunt.getTreasure().getPermission().orElse("everybody") + ChatColor.WHITE + ", value: " + ChatColor.GOLD + hunt.getValue() + ChatColor.WHITE + ", remaining time: " + ChatColor.RED + hunt.getRemainingTime() + ChatColor.WHITE + " seconds, X: " + ChatColor.LIGHT_PURPLE + hunt.getBlock().getX() + ChatColor.WHITE +  ", Y: " + ChatColor.LIGHT_PURPLE + hunt.getBlock().getY() + ChatColor.WHITE + ", Z: " + ChatColor.LIGHT_PURPLE + hunt.getBlock().getZ() + ChatColor.WHITE + ", world: " + ChatColor.DARK_BLUE + hunt.getWorld().getName());
                i++;
            }
        }
        return true;
    }
}
