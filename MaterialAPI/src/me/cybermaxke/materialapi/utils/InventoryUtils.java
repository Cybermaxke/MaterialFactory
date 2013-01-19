package me.cybermaxke.materialapi.utils;

import me.cybermaxke.materialapi.inventory.CustomItemStack;

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
}