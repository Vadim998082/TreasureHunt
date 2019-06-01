package vadim99808.service;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitRunnable;
import vadim99808.entity.Hunt;

public class TreasurePlacer extends BukkitRunnable {

    private Hunt hunt;

    @Override
    public void run() {
        Block block = hunt.getBlock();
        block.setType(Material.CHEST);
        InventoryHolder inventoryHolder = (InventoryHolder) block.getState();
        Inventory inventory = inventoryHolder.getInventory();
        inventory.setContents(hunt.getItemStackArray());
        //System.out.println("Chest " + hunt.getTreasure().getName() + " spawned! " + block.getX() + " " + block.getY() + " " + block.getZ());
    }

    public void setHunt(Hunt hunt) {
        this.hunt = hunt;
    }

    public Hunt getHunt() {
        return hunt;
    }
}
