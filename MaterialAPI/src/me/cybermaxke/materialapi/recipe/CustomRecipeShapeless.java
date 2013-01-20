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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;

import me.cybermaxke.materialapi.inventory.CustomItemStack;
import me.cybermaxke.materialapi.utils.InventoryUtils;

public class CustomRecipeShapeless implements CustomRecipe {
	private CustomItemStack result;
	private List<CustomItemStack> items = new ArrayList<CustomItemStack>();

	public CustomRecipeShapeless(CustomItemStack result) {
		this.result = result;
	}
	
	public void addIngedients(CustomItemStack... ingredients) {
		this.items.addAll(Arrays.asList(ingredients));
	}
	
	@Override
	public CustomItemStack getResult() {
		return new CustomItemStack(this.result.clone());
	}

	@Override
	public CustomItemStack[] getItems() {
		return this.items.toArray(new CustomItemStack[] {});
	}

	@Override
	public Recipe getBukkitRecipe() {
		ShapelessRecipe recipe = new ShapelessRecipe(this.result);
		    
		for (CustomItemStack stack : this.items) {
			if (stack != null) {
				recipe.addIngredient(stack.getType(), stack.getDurability());
			}
		}
		
		return recipe;
	}

	@Override
	public boolean matches(Inventory inventory) {
		List<CustomItemStack> l = new ArrayList<CustomItemStack>(this.items);

        for (int i = 0; i < 3; i++) {
        	for (int j = 0; j < 3; j++) {
        		CustomItemStack is = InventoryUtils.getStackInRowAndColumn(inventory, i, j);

        		if (is != null) {
        			boolean f = false;
        			Iterator<CustomItemStack> iter = l.iterator();

        			while (iter.hasNext()) {
        				CustomItemStack is2 = (CustomItemStack) iter.next();
        				
        				if (InventoryUtils.doItemsMatch(is, is2)) {
        					f = true;
        					l.remove(is2);
        					break;
        				}
        			}

        			if (!f) {
        				return false;
        			}
        		}
        	}
        }

        return l.isEmpty();
	}
}