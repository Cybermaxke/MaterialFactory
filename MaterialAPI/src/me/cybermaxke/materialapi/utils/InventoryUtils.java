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
package me.cybermaxke.materialapi.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.cybermaxke.materialapi.inventory.CustomItemStack;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class InventoryUtils {

	public static CustomItemStack getStackInRowAndColumn(Inventory inventory, int i, int j) {
		Object ci = ReflectionUtils.getFieldObject(Classes.CB_CRAFT_INVENTORY, "inventory", inventory);
		Object is = ReflectionUtils.getMethodObject(Classes.NMS_INVENTORY_CRAFTING, "b", new Class[] { int.class, int.class }, ci, new Object[] { i, j });
		
		return is == null ? null : new CustomItemStack(is);
	}
	
	public static boolean doItemsMatch(CustomItemStack is, CustomItemStack is2) {
		if ((is == null && is2 != null) || (is != null && is2 == null)) {
			return false;
		}
		
		if (is == null && is2 == null) {
			return true;
		}
			
		if (!is.getType().equals(is2.getType())) {
			return false;
		}
		
		if (is.getDurability() != -1 && is.getDurability() != is2.getDurability()) {
			return false;
		}
		
		if ((is.isCustomItem() && !is2.isCustomItem()) || (!is.isCustomItem() && is2.isCustomItem())) {
			return false;
		}
		
		if (is.isCustomItem() && is.getMaterial().getCustomId() != is2.getMaterial().getCustomId()) {
			return false;
		}
		
		return true;
	}
	
	public static boolean isLeather(Material material) {
		List<Material> m = new ArrayList<Material>();
		m.addAll(Arrays.asList(new Material[] { Material.LEATHER_BOOTS, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_HELMET }));
		return m.contains(material);
	}
}