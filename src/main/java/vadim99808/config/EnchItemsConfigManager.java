package vadim99808.config;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import vadim99808.TreasureHunt;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EnchItemsConfigManager {

    private TreasureHunt plugin = TreasureHunt.getInstance();

    public Map<String, ItemStack> loadAllEnchantedItems(){
        Map<String, ItemStack> enchantedItems = new HashMap<>();
        File directory = plugin.getEnchantedItemsDirectory();
        File[] files = directory.listFiles();
        if(files.length == 0){
            plugin.getLogger().info("There are no enchanted items to load!");
        }
        for(File file : files){
            enchantedItems.putAll(loadEnchantedItemsFromFile(file));
        }
        return enchantedItems;
    }

    private Map<String, ItemStack> loadEnchantedItemsFromFile(File file){
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        Map<String, ItemStack> itemStackMap = new HashMap<>();
        for(String internalName : fileConfiguration.getConfigurationSection("").getKeys(false)){
            String material;
            Set<String> enchants;
            if(fileConfiguration.contains(internalName + ".Type")){
                material = fileConfiguration.getString(internalName + ".Type");
            }else{
                plugin.getLogger().warning("Missing type for " + internalName + " enchanted item! Loading of it aborted!");
                continue;
            }
            if(fileConfiguration.contains(internalName + ".Enchantments")){
                enchants = fileConfiguration.getConfigurationSection(internalName + ".Enchantments").getKeys(false);
            }else{
                plugin.getLogger().warning("Missing ENCHANTS for " + internalName + " enchanted item! Loading of it aborted!");
                continue;
            }
            if(Material.getMaterial(material) == null){
                plugin.getLogger().warning("Unknown material name " + material + " in item " + internalName + ". Loading of item aborted!");
                continue;
            }
            ItemStack itemStack = new ItemStack(Material.getMaterial(material));
            ItemMeta itemMeta = itemStack.getItemMeta();
            for(String enchantment : enchants){
                if(Enchantment.getByName(enchantment) == null){
                    plugin.getLogger().warning("Unknown enchantment with name " + enchantment + "!");
                    continue;
                }
                itemMeta.addEnchant(Enchantment.getByName(enchantment), fileConfiguration.getInt(internalName + ".Enchantments." + enchantment), true);
                itemStack.setItemMeta(itemMeta);
            }
            itemStackMap.put(internalName, itemStack);
        }
        return itemStackMap;
    }
}
