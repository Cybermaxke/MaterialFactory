package me.cybermaxke.materialfactory.api;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Represents a extended {@link ItemStack}, every item stack can
 * be safely casted to this interface.
 */
public interface ExtendedItemStack {
    
    static final ItemFactory factory = null;

    /**
     * Creates a new {@link ExtendedItemStack} for the specified item type.
     * 
     * @param itemType the item type
     * @return the extended item stack
     */
    public static ExtendedItemStack create(ItemType itemType) {
        return factory.create(itemType);
    }

    /**
     * Creates a new {@link ExtendedItemStack} for the specified item type
     * and amount of items in the stack.
     * 
     * @param itemType the item type
     * @param amount the size of the stack
     * @return the extended item stack
     */
    public static ExtendedItemStack create(ItemType itemType, int amount) {
        return factory.create(itemType, amount);
    }

    /**
     * Creates a new {@link ExtendedItemStack} for the specified material.
     * 
     * @param material the material
     * @return the extended item stack
     * @throws IllegalArgumentException if the specified material isn't a valid item
     */
    public static ExtendedItemStack create(Material material) throws IllegalArgumentException {
        return factory.create(material);
    }

    /**
     * Creates a new {@link ExtendedItemStack} for the specified material
     * and amount of items in the stack.
     * 
     * @param material the material
     * @param amount the size of the stack
     * @return the extended item stack
     * @throws IllegalArgumentException if the specified material isn't a valid item
     */
    public static ExtendedItemStack create(Material material, int amount) throws IllegalArgumentException {
        return factory.create(material, amount);
    }
    
    /**
     * Casts this item stack back to {@link ItemStack}, this method
     * is just added for cleaner operations.
     * 
     * @return this item stack
     */
    default ItemStack toBukkit() {
        return (ItemStack) this;
    }

    /**
     * Gets the {@link ItemType} of this item stack.
     * 
     * @return the item type
     */
    ItemType getItemType();

    /**
     * Gets the amount of items in this item stack.
     * 
     * @return the amount
     */
    int getAmount();

    /**
     * Sets the amount of items in this item stack.
     * 
     * @param amount the amount
     */
    void setAmount(int amount);

    /**
     * Gets a copy of the {@link ItemData} (or {@link ItemMeta}) of this
     * item stack.
     * 
     * @return the item data
     */
    ItemData getItemMeta();

    /**
     * Tries to apply the specified {@link ItemMeta} to this item stack and
     * gets whether it was successful.
     * 
     * @param itemMeta the item meta
     * @return is successful
     */
    boolean setItemMeta(ItemMeta itemMeta);

    /**
     * Creates a copy of this item stack.
     *
     * @return the copy
     */
    ExtendedItemStack copy();

}
