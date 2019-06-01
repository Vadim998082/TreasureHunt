package vadim99808.executors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vadim99808.TreasureHunt;

public class HuntReloadExecutor implements CommandExecutor {

    private TreasureHunt plugin = TreasureHunt.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            Player player = ((Player) commandSender).getPlayer();
            if(player.hasPermission("th.reload")){
                player.sendMessage(ChatColor.GREEN + "Reloading of configuration file's...");
                plugin.reloadConfigurations();
                player.sendMessage(ChatColor.GREEN + "Reloading of configuration file's complete!");
                return true;
            }else{
                player.sendMessage(ChatColor.RED + "You don't have permission to do that!");
                return false;
            }
        }
        commandSender.sendMessage(ChatColor.GREEN + "Reloading of configuration file's...");
        plugin.reloadConfigurations();
        commandSender.sendMessage(ChatColor.GREEN + "Reloading of configuration file's complete!");
        return true;
    }
}
