package vadim99808.executors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import vadim99808.TreasureHunt;

public class HuntTestLocExecutor implements CommandExecutor {

    private TreasureHunt plugin = TreasureHunt.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!commandSender.hasPermission("th.testloc")){
            commandSender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
            return false;
        }
        if(strings.length == 0){
            commandSender.sendMessage(ChatColor.YELLOW + "Please, specify amount of tested treasure's!");
            return false;
        }
        if(strings.length == 1){
            int amount;
            try {
                amount = Integer.parseInt(strings[0]);
            }catch (NumberFormatException e){
                commandSender.sendMessage(ChatColor.YELLOW + "Please, use only numbers!");
                return false;
            }
            commandSender.sendMessage(ChatColor.GREEN + "Test started!");
            plugin.getHuntService().huntTestLocation(amount);
            commandSender.sendMessage(ChatColor.GREEN + "Test finished!");
            return true;
        }
        if(strings.length == 2){
            int amount;
            String treasureName = strings[0];
            try {
                amount = Integer.parseInt(strings[1]);
            }catch (NumberFormatException e){
                commandSender.sendMessage(ChatColor.YELLOW + "Please, use only numbers!");
                return false;
            }
            commandSender.sendMessage(ChatColor.GREEN + "Test started!");
            plugin.getHuntService().huntTestLocation(amount, treasureName);
            commandSender.sendMessage(ChatColor.GREEN + "Test finished!");
            return true;
        }
        return false;
    }
}
