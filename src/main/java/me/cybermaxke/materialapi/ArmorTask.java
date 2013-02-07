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
package me.cybermaxke.materialapi;

import java.util.Map.Entry;

import me.cybermaxke.materialapi.enchantment.EnchantmentCustomArmor;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ArmorTask extends BukkitRunnable {

	public ArmorTask(Plugin plugin) {
		this.runTaskTimer(plugin, 0, 1);
	}

	@Override
	public void run() {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			for (ItemStack is : p.getInventory().getArmorContents()) {
				if (is != null) {
					for (Entry<Enchantment, Integer> e : is.getEnchantments().entrySet()) {
						if (e.getKey() instanceof EnchantmentCustomArmor) {
							((EnchantmentCustomArmor) e.getKey()).onTick(p, is, e.getValue());
						}
					}
				}
			}
		}
	}
}