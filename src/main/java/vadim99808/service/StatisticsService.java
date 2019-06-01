package vadim99808.service;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import vadim99808.TreasureHunt;
import vadim99808.entity.Hunt;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StatisticsService {

    private TreasureHunt plugin = TreasureHunt.getInstance();

    public void addToStatistic(Hunt hunt){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd");
        File file = new File(plugin.getStatisticsDirectory(), format.format(date) + ".yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().warning("Problem with creating new statistics file!");
                return;
            }
        }
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        if(fileConfiguration.contains(hunt.getTreasure().getName())){
            int count = fileConfiguration.getInt(hunt.getTreasure().getName());
            count++;
            fileConfiguration.set(hunt.getTreasure().getName(), count);
        }else{
            fileConfiguration.createSection(hunt.getTreasure().getName());
            fileConfiguration.set(hunt.getTreasure().getName(), 1);
        }
        if(hunt.isAlreadyClaimed()){
            if(fileConfiguration.contains("TotalClaimed")){
                int count = fileConfiguration.getInt("TotalClaimed");
                count++;
                fileConfiguration.set("TotalClaimed", count);
            } else{
                fileConfiguration.createSection("TotalClaimed");
                fileConfiguration.set("TotalClaimed", 1);
            }
        }else{
            if(fileConfiguration.contains("TotalNotClaimed")){
                int count = fileConfiguration.getInt("TotalNotClaimed");
                count++;
                fileConfiguration.set("TotalNotClaimed", count);
            } else{
                fileConfiguration.createSection("TotalNotClaimed");
                fileConfiguration.set("TotalNotClaimed", 1);
            }
        }
    }

}
