/**
 * 
 * This software is part of the MaterialAPI
 * 
 * This api allows plugin developers to create on a easy way custom
 * items with a custom id and recipes depending on them.
 * 
 * MaterialAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * any later version.
 *  
 * MerchantAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MaterialAPI. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
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