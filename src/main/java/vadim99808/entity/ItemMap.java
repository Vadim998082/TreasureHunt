package vadim99808.entity;

import org.bukkit.inventory.ItemStack;

public class ItemMap
{
    private ItemStack item;
    private int value;
    private boolean onlyOnce;

    public ItemStack getItem()
    {
        return this.item;
    }

    public void setItem(ItemStack item)
    {
        this.item = item;
    }

    public int getValue()
    {
        return this.value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public boolean isOnlyOnce()
    {
        return this.onlyOnce;
    }

    public void setOnlyOnce(boolean onlyOnce)
    {
        this.onlyOnce = onlyOnce;
    }
}
