package me.cybermaxke.materialfactory.api;

import org.bukkit.inventory.meta.ItemMeta;

/**
 * Represents a extended {@link ItemStack}, every item stack can
 * be safely casted to this interface.
 */
public interface ExtendedItemStack {

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
}
