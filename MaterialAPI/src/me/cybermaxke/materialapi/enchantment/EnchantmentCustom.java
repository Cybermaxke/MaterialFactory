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

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public abstract class EnchantmentCustom extends Enchantment {
	public static final EnchantmentCustom DATA_ID = new EnchantmentCustomData(100);

	public EnchantmentCustom(int id) {
		super(id);
		
		if (id >= 256) {
			throw new IllegalArgumentException("A enchantment id has to be lower then 256!");
		}
		
		try {
	      	Field f = Enchantment.class.getDeclaredField("acceptingNew");
	      	f.setAccessible(true);
	      	f.set(null, Boolean.valueOf(true));
	      	
	      	Enchantment.registerEnchantment(this);
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

	@Override
	public String getName() {
		return "MAPIEnchantment" + this.getId();
	}
}