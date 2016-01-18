package me.cybermaxke.materialfactory.api;

import org.bukkit.Material;

public interface ItemFactory {

    /**
     * Creates a new {@link ExtendedItemStack} for the specified item type.
     * 
     * @param itemType the item type
     * @return the extended item stack
     */
    ExtendedItemStack create(ItemType itemType);

    /**
     * Creates a new {@link ExtendedItemStack} for the specified item type
     * and amount of items in the stack.
     * 
     * @param itemType the item type
     * @param amount the size of the stack
     * @return the extended item stack
     */
    ExtendedItemStack create(ItemType itemType, int amount);

    /**
     * Creates a new {@link ExtendedItemStack} for the specified material.
     * 
     * @param material the material
     * @return the extended item stack
     * @throws IllegalArgumentException if the specified material isn't a valid item
     */
    ExtendedItemStack create(Material material) throws IllegalArgumentException;

    /**
     * Creates a new {@link ExtendedItemStack} for the specified material
     * and amount of items in the stack.
     * 
     * @param material the material
     * @param amount the size of the stack
     * @return the extended item stack
     * @throws IllegalArgumentException if the specified material isn't a valid item
     */
    ExtendedItemStack create(Material material, int amount) throws IllegalArgumentException;
    
}
