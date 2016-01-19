/*
 * Copyright (c) Cybermaxke - All Rights Reserved
 */
package me.cybermaxke.materialfactory.api.recipe;

import org.bukkit.Bukkit;

/**
 * This recipe wraps around a {@link Recipe} and allows this recipe to be
 * registered through the {@link Bukkit#addRecipe(org.bukkit.inventory.Recipe)} method.
 */
public interface RecipeWrapper extends org.bukkit.inventory.Recipe {

    /**
     * Gets the recipe base of the custom recipe.
     *
     * @return the recipe base
     */
    Recipe getBase();
}
