package vadim99808.dao;

import com.sk89q.worldedit.util.YAMLConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import vadim99808.TreasureHunt;
import vadim99808.entity.UserStatistics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class UserStatisticsDao {

    private File dataFileDirectory;

    public void createOrUpdate(UserStatistics userStatistics){
        File file = new File(dataFileDirectory, userStatistics.getUserId() + ".yml");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                TreasureHunt.getInstance().getLogger().warning("Cannot create file for player " + userStatistics.getUserId() + " !");
            }
        }
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        if(!fileConfiguration.contains("TotalValue")){
            fileConfiguration.set("TotalValue", userStatistics.getTotalValue());
        }else{
            fileConfiguration.set("TotalValue", fileConfiguration.getInt("TotalValue") + userStatistics.getTotalValue());
        }
        if(!fileConfiguration.contains("TotalQuantity")){
            fileConfiguration.set("TotalQuantity", userStatistics.getTotalQuantity());
        }else{
            fileConfiguration.set("TotalQuantity", fileConfiguration.getInt("TotalQuantity") + userStatistics.getTotalQuantity());
        }
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            TreasureHunt.getInstance().getLogger().warning("Error with saving " + file.getName() + " file!");
        }
    }

    public UserStatistics read(String uuidString){
        File[] files = dataFileDirectory.listFiles();
        File neededFile = null;
        for(File file: files){
            if(file.getName().contains(uuidString)){
                neededFile = file;
            }
        }
        if(neededFile == null){
            return null;
        }
        UserStatistics userStatistics = new UserStatistics();
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(neededFile);
        userStatistics.setUserId(UUID.fromString(uuidString));
        userStatistics.setTotalValue(fileConfiguration.getInt("TotalValue"));
        userStatistics.setTotalQuantity(fileConfiguration.getInt("TotalQuantity"));
        return userStatistics;
    }

    public List readAll(){
        File[] files = dataFileDirectory.listFiles();
        List<UserStatistics> userStatisticsList = new ArrayList<>();
        for(File file: files){
            if(file.getName().endsWith(".yml")){
                FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
                int totalValue;
                int totalQuantity;
                if(fileConfiguration.contains("TotalValue")){
                    totalValue = fileConfiguration.getInt("TotalValue");
                }else{
                    continue;
                }
                if(fileConfiguration.contains("TotalQuantity")){
                    totalQuantity = fileConfiguration.getInt("TotalQuantity");
                }else{
                    continue;
                }
                UserStatistics userStatistics = new UserStatistics();
                userStatistics.setTotalQuantity(totalQuantity);
                userStatistics.setTotalValue(totalValue);
                userStatistics.setUserId(UUID.fromString(file.getName().substring(0, file.getName().indexOf("."))));
                userStatisticsList.add(userStatistics);
            }
        }
        return userStatisticsList;
    }

    public void setDataFileDirectory(File dataFileDirectory) {
        this.dataFileDirectory = dataFileDirectory;
    }

//    public void update(UserStatistics userStatistics){
//        Session session = HibernateUtils.getSessionFactory().openSession();
//        Transaction transaction = session.beginTransaction();
//        session.saveOrUpdate(userStatistics);
//        transaction.commit();
//    }

}
