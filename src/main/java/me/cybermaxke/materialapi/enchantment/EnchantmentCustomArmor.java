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
package me.cybermaxke.materialapi.enchantment;

import me.cybermaxke.materialapi.utils.Classes;
import me.cybermaxke.materialapi.utils.ReflectionUtils;

import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public abstract class EnchantmentCustomArmor extends EnchantmentCustom {

	public EnchantmentCustomArmor(int id) {
		super(id);
	}
	
	@Override
	public boolean canEnchantItem(ItemStack itemstack) {
		if (itemstack == null) {
			return false;
		}
		
		Object is = ReflectionUtils.getMethodObject(Classes.CB_CRAFT_ITEMSTACK, "asNMSCopy", new Class[] { ItemStack.class }, null, new Object[] { itemstack });
		Object item = ReflectionUtils.getMethodObject(Classes.NMS_ITEMSTACK, "getItem", is);
		return item.getClass().isAssignableFrom(Classes.NMS_ITEM_ARMOR);
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.ARMOR;
	}
	
	public abstract void onTick(Player player, ItemStack item, int lvl);
	
	public abstract void onDefend(EntityDamageByEntityEvent event, ItemStack item, int lvl);
}