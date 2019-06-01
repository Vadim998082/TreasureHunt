package vadim99808.executors;

import com.earth2me.essentials.User;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vadim99808.TreasureHunt;
import vadim99808.entity.UserStatistics;
import vadim99808.service.BroadcastService;

import java.util.List;

public class HuntTopExecutor implements CommandExecutor {

    private TreasureHunt plugin = TreasureHunt.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length == 0){
            List<UserStatistics> userStatisticsList = plugin.getUserStatisticsService().getListSortedByQuantity();
            plugin.getBroadcastService().huntTopMessage(plugin.getLocalizationConfigManager().getHuntTop(), userStatisticsList, commandSender);
            return true;
        }else{
            if(strings.length == 1 && strings[0].equals("value")){
                List<UserStatistics> userStatisticsList = plugin.getUserStatisticsService().getListSortedByValue();
                plugin.getBroadcastService().huntTopMessage(plugin.getLocalizationConfigManager().getHuntTop(), userStatisticsList, commandSender);
                return true;
            }
        }
        return false;
    }
}
