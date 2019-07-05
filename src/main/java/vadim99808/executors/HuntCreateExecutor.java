package vadim99808.executors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vadim99808.TreasureHunt;

import java.io.File;
import java.io.IOException;

public class HuntCreateExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.hasPermission("th.huntcreate")){
            File chestsDirectory = TreasureHunt.getInstance().getChestsDirectory();
            int amount = 1;
            if(strings.length != 0){
                try {
                    amount = Integer.parseInt(strings[0]);
                }catch (NumberFormatException e){
                    commandSender.sendMessage(ChatColor.YELLOW + "Please, use only numbers!");
                    return false;
                }
            }
            for(int i = 0; i < amount; i++){
                File file = new File(chestsDirectory, "AutogeneratedEmptyTreasure" + i + ".yml");
                if(!file.exists()){
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        TreasureHunt.getInstance().getLogger().warning("Error with auto generating treasure file!");
                        commandSender.sendMessage(ChatColor.RED + "Error with generating!");
                        return false;
                    }
                }
            }
            commandSender.sendMessage(ChatColor.GREEN + "Successfully created " + amount + " empty chest files!");
            return true;
        }
        commandSender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
        return false;
    }
}
