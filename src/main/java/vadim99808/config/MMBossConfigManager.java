package vadim99808.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import vadim99808.TreasureHunt;
import vadim99808.entity.MMBossCondition;
import vadim99808.entity.MMBossMap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MMBossConfigManager {

    private static TreasureHunt plugin = TreasureHunt.getInstance();

//    public List<MMBossCondition> loadAllBossConditions(){
//        File[] files = plugin.getMmBossConditionsDirectory().listFiles();
//        List<MMBossCondition> mmBossMapList = new ArrayList<>();
//        for(File file: files){
//            mmBossMapList.add(loadMmBossMapFromFile(file));
//        }
//        return mmBossMapList;
//    }

//    private MMBossCondition loadMmBossMapFromFile(File file){
//        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
//        MMBossCondition mmBossCondition = new MMBossCondition();
//        String name;
//        List<String> stringBosses;
//        if(fileConfiguration.contains("Name")){
//            name = fileConfiguration.getString("Name");
//        }else{
//            printWarning("Name", file);
//            return null;
//        }
//        if(fileConfiguration.contains("Bosses")){
//            stringBosses = fileConfiguration.getStringList("Bosses");
//            if(stringBosses.isEmpty()){
//                printWarning("Bosses", file);
//                return null;
//            }
//        }else{
//            printWarning("Bosses", file);
//            return null;
//        }
//        if()
//    }

    private void printWarning(String absentParam, File file){
        plugin.getLogger().warning("Cannot load " + file.getName() + " because parameter " + absentParam + " is absent!");
    }

}
