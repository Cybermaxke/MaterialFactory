/*
 * Copyright (c) Cybermaxke - All Rights Reserved
 */
package me.cybermaxke.materialfactory.api.recipe;

import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

/**
 * This is a interface devs can implement to create custom recipes.
 */
public interface Recipe {

    /**
     * Tries to get the result for the crafting inventory.
     *
     * @param player the player who is crafting
     * @param inventory the inventory
     * @return the result if present, otherwise {@link Optional#empty()}
     */
    Optional<ItemStack> tryToMatch(Player player, CraftingInventory inventory);

    /**
     * Gets the default result of the recipe.
     *
     * @return the default result
     */
    ItemStack getDefaultResult();

    /**
     * Gets the default ingredients of the recipe.
     *
     * @return the default ingredients
     */
    List<ItemStack> getDefaultIngredients();
}
