package vadim99808.service;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import vadim99808.TreasureHunt;
import vadim99808.config.DefaultConfigManager;
import vadim99808.entity.Hunt;
import vadim99808.entity.UserStatistics;
import vadim99808.storage.Storage;


public class TreasureListener implements Listener {

    private TreasureHunt plugin = TreasureHunt.getInstance();
    private BroadcastService broadcastService = plugin.getBroadcastService();
    private HuntService huntService = plugin.getHuntService();
    private UserDistanceService userDistanceService = plugin.getUserDistanceService();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.CHEST){
            for(Hunt hunt: Storage.getInstance().getHuntList()){
                if(hunt.getBlock().getLocation().equals(event.getClickedBlock().getLocation())){
                    if(!hunt.isAlreadyClaimed()){
                        hunt.interrupt();
                        hunt.setAlreadyClaimed(true);
                        hunt.setClaimedPlayer(player);
                        broadcastService.broadcast(plugin.getLocalizationConfigManager().getPlayerFoundChest(), hunt, null);
                        if(plugin.getEconomy() != null) {
                            plugin.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), hunt.getMoney());
                            broadcastService.privateMessage(plugin.getLocalizationConfigManager().getMoneyFound(), hunt, player);
                        }
                        plugin.getLogger().info("In world " + hunt.getWorld().getName() + "chest found by " + player.getName() + "!" + "Name: " + hunt.getTreasure().getName() + ", Value: " + hunt.getValue() + ", Coordinates: " + hunt.getBlock().getX() + " " + hunt.getBlock().getY() + " " + hunt.getBlock().getZ());
                        UserStatistics userStatistics = new UserStatistics();
                        userStatistics.setUserId(player.getUniqueId());
                        userStatistics.setTotalQuantity(1);
                        userStatistics.setTotalValue(hunt.getValue());
                        plugin.getUserStatisticsDao().createOrUpdate(userStatistics);
                        TreasureDestroyerTimer treasureDestroyerTimer = new TreasureDestroyerTimer();
                        treasureDestroyerTimer.setDelay(plugin.getDefaultConfigManager().getDestroyDelay());
                        treasureDestroyerTimer.setHunt(hunt);
                        Storage.getInstance().getTreasureDestroyerTimerList().add(treasureDestroyerTimer);
                        treasureDestroyerTimer.start();
                        plugin.getStatisticsService().addToStatistic(hunt);
                        return;
                    }else{
                        if(player.equals(hunt.getClaimedPlayer())){
                            broadcastService.privateMessage(plugin.getLocalizationConfigManager().getAlreadyClaimed(), hunt, player);
                            return;
                        }else{
                            broadcastService.privateMessage(plugin.getLocalizationConfigManager().getAlreadyClaimed(), hunt, player);
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }

        if((event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) && event.hasItem()){
            if(plugin.getHuntToolList().contains(event.getItem().getType())){
                World world = player.getWorld();
                if(plugin.getLastChecks().containsKey(player) && plugin.getLastChecks().get(player) >= System.currentTimeMillis() - plugin.getDefaultConfigManager().getCheckDelay() * 1000){
                    broadcastService.fastCheckMessage(plugin.getLocalizationConfigManager().getFastCheckLocation(), plugin.getDefaultConfigManager().getCheckDelay(), player);
                    return;
                }
                if(huntService.findHunts(world, event.getItem().getType(), player) > 0){
                    if(huntService.findDistanceToClosestTreasure(player.getWorld(), player, event.getItem().getType()).isPresent()){
                        int minDistance = huntService.findDistanceToClosestTreasure(player.getWorld(), player, event.getItem().getType()).get();
                        if(plugin.getDefaultConfigManager().isImprovedDistanceCalc()){
                            int recalcDistance = huntService.transformDistance(minDistance);
                            recalcDistance = userDistanceService.checkPlayerDistance(player, huntService.findClosestTreasure(world, player, event.getItem().getType()), minDistance, recalcDistance);
                            minDistance = recalcDistance;
                        }
                        broadcastService.minDistance(plugin.getLocalizationConfigManager().getClosestChest(), minDistance, huntService.findHunts(world, event.getItem().getType(), player), player);
                        if(minDistance < plugin.getDefaultConfigManager().getClosestAfter()){
                            Hunt hunt = huntService.findClosestTreasure(world, player, event.getItem().getType());
                            if(hunt != null){
                                if(!hunt.getClosestPlayers().containsKey(player)){
                                    hunt.getClosestPlayers().put(player, minDistance);
                                }else{
                                    hunt.getClosestPlayers().replace(player, minDistance);
                                }
                            }
                            if(hunt.getClosestPlayers().size() == 1 && !hunt.isSomeoneAlreadyClosest()){
                                broadcastService.broadcast(plugin.getLocalizationConfigManager().getPlayerCloseToChest(), hunt, player.getName());
                                hunt.setSomeoneAlreadyClosest(true);
                            }
                        }
                        event.getItem().setAmount(event.getItem().getAmount() - 1);
                        if(!plugin.getLastChecks().containsKey(player)) {
                            plugin.getLastChecks().put(player, System.currentTimeMillis());
                        }else{
                            plugin.getLastChecks().replace(player, System.currentTimeMillis());
                        }
                        return;
                    }
                }else{
                    broadcastService.privateMessage(plugin.getLocalizationConfigManager().getNoChests(), player);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent blockBreakEvent){
        Block block = blockBreakEvent.getBlock();
        if(!block.getType().equals(Material.CHEST)){
            return;
        }
        for(Hunt hunt: Storage.getInstance().getHuntList()){
            if(hunt.getBlock().equals(block)){
                Player player = blockBreakEvent.getPlayer();
                player.sendMessage(ChatColor.RED + "You cannot break treasure!");
                blockBreakEvent.setCancelled(true);
            }
        }
    }
}
