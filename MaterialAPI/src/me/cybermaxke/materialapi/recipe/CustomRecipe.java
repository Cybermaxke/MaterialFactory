package me.cybermaxke.materialapi.recipe;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.Recipe;

import me.cybermaxke.materialapi.inventory.CustomItemStack;

public interface CustomRecipe {

	public CustomItemStack getResult();
	
	public CustomItemStack[] getItems();
	
	public Recipe getBukkitRecipe();
	
	public boolean matches(Inventory inventory);
}