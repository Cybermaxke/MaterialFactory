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
package me.cybermaxke.materialapi.material;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.cybermaxke.materialapi.enchantment.EnchantmentInstance;
import me.cybermaxke.materialapi.map.CustomMap;
import me.cybermaxke.materialapi.utils.InventoryUtils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public class CustomMaterial {
	private List<EnchantmentInstance> enchantments = new ArrayList<EnchantmentInstance>();
	private List<String> lore = new ArrayList<String>();
	
	private String name = null;
	private String id = null;
	private String skullOwner = null;
	
	private Color color = null;
	private CustomMap map = null;
	
	private int minecraftId = 0;
	private int customId = 1000;
	private int damage = -1;
	private byte data = -1;
	
	private boolean canPlace = true;

	public CustomMaterial(String id, int minecraftId, byte data) {
		this.id = id.toLowerCase();
		this.minecraftId = minecraftId;
		this.data = data;
		this.customId = MaterialData.addMaterialData(this);
		MaterialData.addMaterial(this);
	}
	
	public CustomMaterial(String id, int minecraftId) {
		this(id, minecraftId, (byte) -1);
	}
	
	public CustomMaterial(String id, Material material, byte data) {
		this(id, material.getId(), data);
	}
	
	public CustomMaterial(String id, Material material) {
		this(id, material.getId());
	}
	
	/**
	 * Returns the display name of the material, 'null' if it was never changed.
	 * @return The name.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Sets the display name of the material.
	 * @param name The name.
	 */
	public CustomMaterial setName(String name) {
		this.name = ChatColor.WHITE + name;
		return this;
	}
	
	/**
	 * Returns the minecraft block/item id.
	 * @return
	 */
	public int getMinecraftId() {
		return this.minecraftId;
	}
	
	/**
	 * Sets the minecraft block/item id.
	 * @param id The id.
	 */
	public CustomMaterial setMinecraftId(int id) {
		this.minecraftId = id;
		return this;
	}
	
	/**
	 * Returns the bukkit material.
	 * @return The material.
	 */
	public Material getType() {
		return Material.getMaterial(this.minecraftId);
	}
	
	/**
	 * Sets the bukkit material.
	 * @param material The material.
	 */
	public CustomMaterial setType(Material material) {
		return this.setMinecraftId(material.getId());
	}
	
	/**
	 * Returns the id.
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * Returns the custom id.
	 * @return
	 */
	public int getCustomId() {
		return this.customId;
	}
	
	/**
	 * Returns the data value of the material.
	 * @return The data value.
	 */
	public byte getData() {
		return this.data;
	}
	
	/**
	 * Sets the data value of the material.
	 * @param data The data value.
	 */
	public CustomMaterial setData(byte data) {
		this.data = data;
		return this;
	}
	
	/**
	 * Returns the custom map the material is holding, 
	 * 'null' if it's not a map or if it doesn't exist.
	 * @return The map.
	 */
	public CustomMap getMap() {
		return this.getType().equals(Material.MAP) ? this.map : null;
	}
	
	/**
	 * Sets the custom map of the material.
	 * @param map The map.
	 */
	public CustomMaterial setMap(CustomMap map) {
		this.map = map;
		return this;
	}
	
	/**
	 * Returns the custom damage, '-1' if it was never changed.
	 * @return The damage.
	 */
	public int getDamage() {
		return this.damage;
	}
	
	/**
	 * Sets the default damage to a custom value.
	 * @param damage The damage.
	 */
	public CustomMaterial setDamage(int damage) {
		this.damage = damage;
		return this;
	}
	
	/**
	 * Sets the color of the material.
	 * @param color The color.
	 */
	public CustomMaterial setColor(Color color) {
		this.color = color;
		return this;
	}
	
	/**
	 * Returns the color of the material, 'null' if its not dyeable.
	 * @return The color.
	 */
	public Color getColor() {
		return InventoryUtils.isLeather(this.getType()) ? this.color : null;
	}
	
	/**
	 * Sets the name of the skull owner.
	 * @param name The name.
	 */
	public CustomMaterial setSkullOwner(String name) {
		this.skullOwner = name;
		return this;
	}
	
	/**
	 * Returns the name of the skull owner of the material, 'null' if it's not a skull.
	 * @return The name.
	 */
	public String getSkullOwner() {
		return this.minecraftId != Material.SKULL_ITEM.getId() ? null : this.skullOwner;
	}
	
	/**
	 * Sets if the block can be placed by default.
	 * @param can Can be placed.
	 */
	public CustomMaterial setCanPlace(boolean can) {
		this.canPlace = can;
		return this;
	}
	
	/**
	 * Returns if the custom block can be placed on the given location.
	 * @param location The location.
	 */
	public boolean canPlace(Location location) {
		return this.canPlace;
	}
	
	/**
	 * Adds a enchantment to the material, it can be hidden.
	 * @param enchantment The enchantment.
	 * @param lvl The lvl.
	 * @param visible If its visible on the item.
	 */
	public CustomMaterial addEnchantment(Enchantment enchantment, int lvl, boolean visible) {
		this.addEnchantment(new EnchantmentInstance(enchantment, lvl, visible));
		return this;
	}
	
	/**
	 * Adds a enchantment to the material.
	 * @param enchantment The enchantment.
	 * @param lvl The lvl.
	 */
	public CustomMaterial addEnchantment(Enchantment enchantment, int lvl) {
		return this.addEnchantment(enchantment, lvl, true);
	}
	
	/**
	 * Adds a enchantment instance to the material.
	 * @param enchantment
	 */
	public CustomMaterial addEnchantment(EnchantmentInstance enchantment) {
		this.enchantments.add(enchantment);
		return this;
	}
	
	/**
	 * Adds multiple enchantment instances at once.
	 * @param enchantments The enchantments.
	 */
	public CustomMaterial addEnchantments(EnchantmentInstance... enchantments) {
		this.enchantments.addAll(Arrays.asList(enchantments));
		return this;
	}
	
	/**
	 * Returns all the enchantment instances, 'null' if there don't exist any.
	 * @return The enchantment instances.
	 */
	public EnchantmentInstance[] getEnchantments() {
		return this.enchantments.isEmpty() ? null : this.enchantments.toArray(new EnchantmentInstance[] {});
	}
	
	/**
	 * Returns all the lore added to the material.
	 * @return The lore.
	 */
	public String[] getLore() {
		return this.lore.isEmpty() ? null : this.lore.toArray(new String[] {});
	}
	
	/**
	 * Sets all the lore, with clearing the old.
	 * @param lore The lore.
	 */
	public CustomMaterial setLore(String... lore) {
		this.lore = new ArrayList<String>();
		
		if (lore == null) {
			return this;
		}
		
		for (int i = 0; i < lore.length; i++) {
			lore[i] = ChatColor.GRAY + lore[i];
		}
		
		this.lore.addAll(Arrays.asList(lore));
		return this;
	}
	
	/**
	 * Adds lore to the material.
	 * @param lore The lore.
	 */
	public CustomMaterial addLore(String... lore) {
		for (int i = 0; i < lore.length; i++) {
			lore[i] = ChatColor.GRAY + lore[i];
		}
		
		this.lore.addAll(Arrays.asList(lore));
		return this;
	}
	
	public void onHit(Player player, LivingEntity entity) {
		
	}
	
	public void onInteract(Player player, Action action, Block block, BlockFace face) {
		
	}
	
	public void onInteractEntity(Player player, LivingEntity entity) {
		
	}
	
	public void onBlockPlaced(Player player, Block block) {
		
	}
	
	public void onBlockBreak(Player player, Block block) {
		
	}
	
	public void onBlockDamage(Player player, Block block) {
		
	}
	
	public void onBlockInteract(Player player, Block block) {
		
	}
}