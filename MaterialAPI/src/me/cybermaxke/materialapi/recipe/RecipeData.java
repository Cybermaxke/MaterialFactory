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
 * MaterialAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MaterialAPI. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package me.cybermaxke.materialapi.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import me.cybermaxke.materialapi.inventory.CustomItemStack;
import me.cybermaxke.materialapi.utils.Classes;
import me.cybermaxke.materialapi.utils.InventoryUtils;
import me.cybermaxke.materialapi.utils.ReflectionUtils;

public class RecipeData {
	private static List<CustomRecipe> recipes = new ArrayList<CustomRecipe>();
	private static List<CustomRecipeFurnace> furnaceRecipes = new ArrayList<CustomRecipeFurnace>();
	
	public static void registerRecipe(CustomRecipe recipe) {
		if (recipe instanceof CustomRecipeFurnace) {
			furnaceRecipes.add((CustomRecipeFurnace) recipe);
		} else {
			recipes.add(recipe);
		}
		
		Bukkit.getServer().addRecipe(recipe.getBukkitRecipe());
	}
	
	public static CustomItemStack getFurnaceItem(CustomItemStack input) {
		if (furnaceRecipes.isEmpty()) {
			return null;
		}
		
		for (int i = 0; i < furnaceRecipes.size(); i++) {
			CustomRecipeFurnace r = furnaceRecipes.get(i);
			CustomItemStack in = r.getInput();
			
			if (InventoryUtils.doItemsMatch(input, in)) {
				return r.getResult();
			}
		}
		
		if (input.isCustomItem()) {
			return null;
		}
		
		Object furnacerecipes = ReflectionUtils.getFieldObject(Classes.NMS_FURNACE_RECIPES, "a", null);
		Map<?,?> map = (Map<?,?>) ReflectionUtils.getFieldObject(Classes.NMS_FURNACE_RECIPES, "recipes", furnacerecipes);
		Object result = !map.containsKey(input.getTypeId()) ? null : map.get(input.getTypeId());
		return result == null ? null : new CustomItemStack(result);
	}
	
	public static CustomRecipe[] getRecipes() {
		return recipes.toArray(new CustomRecipe[] {});
	}
	
	public static void clearRecipes() {
		recipes.clear();
	}
	
	public static CustomItemStack getItem(CraftingInventory inv) {
		for (CustomRecipe rs : recipes) {
			if (rs.matches(inv)) {
				return rs.getResult();
			}
		}
		
		for (ItemStack is : inv.getContents()) {
			if (is != null && new CustomItemStack(is).isCustomItem()) {
				return null;
			}
		}
		
		Object result = null;
		Object craftmanager = ReflectionUtils.getFieldObject(Classes.NMS_CRAFTING_MANAGER, "a", null);
		Object inc = ReflectionUtils.getFieldObject(Classes.CB_CRAFT_INVENTORY, "inventory", inv);
		List<?> recipes = (List<?>) ReflectionUtils.getFieldObject(Classes.NMS_CRAFTING_MANAGER, "recipes", craftmanager);
		
		for (Object obj : recipes) {			
			if (!(boolean) ReflectionUtils.getMethodObject(Classes.NMS_IRECIPE, "a", new Class[] { Classes.NMS_INVENTORY_CRAFTING, Classes.NMS_WORLD }, obj, new Object[] { inc, null })) {
				continue;
			}
			
			if (obj.getClass().isAssignableFrom(Classes.NMS_SHAPED_RECIPES)) {
				result = ReflectionUtils.getFieldObject(Classes.NMS_SHAPED_RECIPES, "result", obj);
			} else {
				result = ReflectionUtils.getFieldObject(Classes.NMS_SHAPELESS_RECIPES, "result", obj);
			}
		}
		
		return result == null ? null : new CustomItemStack(result);
	}
}