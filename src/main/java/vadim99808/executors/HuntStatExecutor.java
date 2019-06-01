package vadim99808.executors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vadim99808.TreasureHunt;
import vadim99808.entity.UserStatistics;

public class HuntStatExecutor implements CommandExecutor {

    private TreasureHunt plugin = TreasureHunt.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            UserStatistics userStatistics = plugin.getUserStatisticsDao().read(((Player) commandSender).getPlayer().getUniqueId().toString());
            if(userStatistics == null){
                plugin.getBroadcastService().dontHaveStat(plugin.getLocalizationConfigManager().getDontHaveStat(), (Player) commandSender);
                return true;
            }else{
                plugin.getBroadcastService().personalStat(plugin.getLocalizationConfigManager().getPersonalStat(), userStatistics);
                return true;
            }
        }else{
            commandSender.sendMessage(ChatColor.RED + "This command only for players!");
            return true;
        }
    }
}
