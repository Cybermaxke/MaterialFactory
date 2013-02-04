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

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class EnchantmentCustomData extends EnchantmentCustom {

	public EnchantmentCustomData(int id) {
		super(id);
	}

	@Override
	public boolean canEnchantItem(ItemStack itemstack) {
		return false;
	}

	@Override
	public boolean conflictsWith(Enchantment enchantment) {
		return false;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.ALL;
	}

	@Override
	public int getMaxLevel() {
		return 99999;
	}

	@Override
	public int getStartLevel() {
		return 0;
	}

	@Override
	public int getWeight() {
		return 1000;
	}

	@Override
	public String getEnchantmentName() {
		return null;
	}
}