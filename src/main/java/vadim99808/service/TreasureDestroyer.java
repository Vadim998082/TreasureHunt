package vadim99808.service;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import vadim99808.TreasureHunt;
import vadim99808.entity.Hunt;
import vadim99808.storage.Storage;

public class TreasureDestroyer extends BukkitRunnable {

    private Hunt hunt;
    private TreasureHunt plugin = TreasureHunt.getInstance();

    @Override
    public void run() {
        InventoryHolder inventoryHolder = (InventoryHolder)hunt.getBlock().getState();
        Inventory inventory = inventoryHolder.getInventory();
        inventory.setContents(new ItemStack[1]);
        hunt.getBlock().setType(Material.AIR);
        Storage.getInstance().getHuntList().remove(hunt);
    }

    public void setHunt(Hunt hunt) {
        this.hunt = hunt;
    }
}
