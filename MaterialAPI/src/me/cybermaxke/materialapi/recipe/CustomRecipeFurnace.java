package me.cybermaxke.materialapi.recipe;

import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.Recipe;

import me.cybermaxke.materialapi.inventory.CustomItemStack;
import me.cybermaxke.materialapi.utils.InventoryUtils;

public class CustomRecipeFurnace implements CustomRecipe {
	private CustomItemStack result;
	private CustomItemStack ingredient;
	private int amount = 1;
	
	public CustomRecipeFurnace(CustomItemStack input, CustomItemStack result) {
		this.ingredient = input;
		this.result = result;
		this.amount = 1;
	}
	
	public CustomItemStack getInput() {
		return this.ingredient;
	}
	
	public CustomItemStack getResult() {
		return new CustomItemStack(this.result.clone());
	}
	
	public int getAmount() {
		return this.amount;
	}

	@Override
	public CustomItemStack[] getItems() {
		return new CustomItemStack[] { this.ingredient };
	}

	@Override
	public Recipe getBukkitRecipe() {
		return new FurnaceRecipe(this.result, this.ingredient.getType(), this.ingredient.getDurability());
	}

	@Override
	public boolean matches(Inventory inventory) {
		return InventoryUtils.doItemsMatch(this.ingredient, inventory.getItem(0) == null ? null : new CustomItemStack(inventory.getItem(0)));
	}
}