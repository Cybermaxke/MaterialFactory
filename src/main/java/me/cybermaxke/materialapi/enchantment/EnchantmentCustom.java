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

import java.lang.reflect.Field;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class EnchantmentCustom extends Enchantment {
	public static final EnchantmentCustom DATA_ID = new EnchantmentCustomData(100);

	@SuppressWarnings("unchecked")
	public EnchantmentCustom(int id) {
		super(id);

		if (id >= 256) {
			throw new IllegalArgumentException("A enchantment id has to be lower then 256!");
		}

		try {
			Field byIdField = Enchantment.class.getDeclaredField("byId");
			Field byNameField = Enchantment.class.getDeclaredField("byName");
					
			byIdField.setAccessible(true);
			byNameField.setAccessible(true);

			Map<Integer, Enchantment> byId = (Map<Integer, Enchantment>) byIdField.get(null);
			Map<String, Enchantment> byName = (Map<String, Enchantment>) byNameField.get(null);
					
			if (byId.containsKey(id)) {
				byId.remove(id);
			}
					
			if (byName.containsKey(this.getName())) {
				byName.remove(this.getName());
			}

			Field f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null, Boolean.valueOf(true));
		      	
			Enchantment.registerEnchantment(this);
			Enchantment.stopAcceptingRegistrations();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public abstract boolean canEnchantItem(ItemStack itemstack);

	public abstract boolean conflictsWith(Enchantment enchantment);

	public abstract EnchantmentTarget getItemTarget();

	public abstract int getMaxLevel();
	
	public abstract int getStartLevel();
	
	public abstract int getWeight();
	
	public abstract String getEnchantmentName();
	
	public abstract void onHit(EntityDamageByEntityEvent event, ItemStack item, int lvl);
	
	public abstract void onInteract(PlayerInteractEvent event, ItemStack item, int lvl);
	
	public abstract void onInteract(PlayerInteractEntityEvent event, ItemStack item, int lvl);

	@Override
	public String getName() {
		return "MAPIEnchantment" + this.getId();
	}
}
