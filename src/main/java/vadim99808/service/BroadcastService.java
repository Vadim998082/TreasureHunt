package vadim99808.service;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vadim99808.TreasureHunt;
import vadim99808.entity.Hunt;
import vadim99808.entity.UserStatistics;

import java.util.List;

public class BroadcastService {
    private TreasureHunt plugin = TreasureHunt.getInstance();

    public void broadcast(String message, Hunt hunt, String playerName){
        if(message.contains("<name>")){
            message = message.replace("<name>", hunt.getTreasure().getDisplayName());
        }
        if(message.contains("<value>")){
            message = message.replace("<value>", String.valueOf(hunt.getValue()));
        }
        if(message.contains("<worldname>")){
            message = message.replace("<worldname>", hunt.getWorld().getName());
        }
        if(message.contains("<remainingTime>")){
            message = message.replace("<remainingTime>", String.valueOf(hunt.getTreasure().getLifeTime()));
        }
        if(message.contains("<pname>") && hunt.getClaimedPlayer() != null){
            message = message.replace("<pname>", hunt.getClaimedPlayer().getName());
        }else{
            if(message.contains("<pname>") && playerName != null){
                message = message.replace("<pname>", playerName);
            }
        }
        if(message.contains("<money>")){
            message = message.replace("<money>", String.valueOf(hunt.getMoney()));
        }
        if(message.contains("<location>")){
            message = message.replace("<location>", "X: " + hunt.getBlock().getX() + " Y: " + hunt.getBlock().getY() + " Z: " + hunt.getBlock().getZ());
        }
        for(Player player: plugin.getServer().getOnlinePlayers()){
            if(hunt.getTreasure().getPermission().isPresent() && !player.hasPermission(hunt.getTreasure().getPermission().get())){
                continue;
            }
            player.sendMessage(message.replaceAll("&([0-9a-f])", "§$1"));
        }
    }


    public void minDistance(String message, int distance, int amountOf, Player player){
        if(message.contains("<distance>")){
            message = message.replace("<distance>", String.valueOf(distance));
        }
        if(message.contains("<numhunts>")){
            message = message.replace("<numhunts>", String.valueOf(amountOf));
        }
        player.sendMessage(message.replaceAll("&([0-9a-f])", "§$1"));
    }

    public void privateMessage(String message, Hunt hunt, Player player){
        if(message.contains("<name>")){
            message = message.replace("<name>", hunt.getTreasure().getDisplayName());
        }
        if(message.contains("<value>")){
            message = message.replace("<value>", String.valueOf(hunt.getValue()));
        }
        if(message.contains("<worldname>")){
            message = message.replace("<worldname>", hunt.getWorld().getName());
        }
        if(message.contains("<remainingTime>")){
            message = message.replace("<remainingTime>", String.valueOf(hunt.getTreasure().getLifeTime()));
        }
        if(message.contains("<pname>") && hunt.getClaimedPlayer() != null){
            message = message.replace("<pname>", hunt.getClaimedPlayer().getName());
        }
        if(message.contains("<money>")){
            message = message.replace("<money>", String.valueOf(hunt.getMoney()));
        }
        if(message.contains("<location>")){
            message = message.replace("<location>", "X: " + hunt.getBlock().getX() + " Y: " + hunt.getBlock().getY() + " Z: " + hunt.getBlock().getZ());
        }
        player.sendMessage(message.replaceAll("&([0-9a-f])", "§$1"));
    }

    public void privateMessage(String message, Player player){
        player.sendMessage(message.replaceAll("&([0-9a-f])", "§$1"));
    }

    public void fastCheckMessage(String message, int delay, Player player){
        if(message.contains("<checkdelay>")){
            message = message.replace("<checkdelay>", String.valueOf(delay));
        }
        player.sendMessage(message.replaceAll("&([0-9a-f])", "§$1"));
    }

    public void huntTopMessage(String message, List<UserStatistics> userStatisticsList, CommandSender commandSender){
        int i = 1;
        String copyMessage = new String(message);
        for(UserStatistics userStatistics: userStatisticsList){
            if(message.contains("<pname>")){
                message = message.replace("<pname>", plugin.getServer().getOfflinePlayer(userStatistics.getUserId()).getName());
            }
            if(message.contains("<position>")){
                message = message.replace("<position>", Integer.toString(i));
            }
            if(message.contains("<totalAmount>")){
                message = message.replace("<totalAmount>", Integer.toString(userStatistics.getTotalQuantity()));
            }
            if(message.contains("<totalValue>")){
                message = message.replace("<totalValue>", Integer.toString(userStatistics.getTotalValue()));
            }
            commandSender.sendMessage(message.replaceAll("&([0-9a-f])", "§$1"));
            i++;
            if(i > 10){
                break;
            }
            message = new String(copyMessage);
        }
    }

    public void personalStat(String message, UserStatistics userStatistics){
        Player player = (Player) plugin.getServer().getOfflinePlayer(userStatistics.getUserId());
        if(message.contains("<pname>")){
            message = message.replace("<pname>", plugin.getServer().getOfflinePlayer(userStatistics.getUserId()).getName());
        }
        if(message.contains("<totalAmount>")){
            message = message.replace("<totalAmount>", Integer.toString(userStatistics.getTotalQuantity()));
        }
        if(message.contains("<totalValue>")){
            message = message.replace("<totalValue>", Integer.toString(userStatistics.getTotalValue()));
        }
        player.sendMessage(message.replaceAll("&([0-9a-f])", "§$1"));
    }

    public void dontHaveStat(String message, Player player){
        player.sendMessage(message.replaceAll("&([0-9a-f])", "§$1"));
    }
}
