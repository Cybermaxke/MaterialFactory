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
package me.cybermaxke.materialapi.inventory;

import java.awt.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.cybermaxke.materialapi.enchantment.EnchantmentInstance;
import me.cybermaxke.materialapi.enchantment.EnchantmentCustom;
import me.cybermaxke.materialapi.material.CustomMaterial;
import me.cybermaxke.materialapi.material.MaterialData;
import me.cybermaxke.materialapi.utils.Classes;
import me.cybermaxke.materialapi.utils.InventoryUtils;
import me.cybermaxke.materialapi.utils.ReflectionUtils;
import me.cybermaxke.tagutils.TagCompound;
import me.cybermaxke.tagutils.TagUtils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * A custom itemstack to manage the custom materials.
 */
public class CustomItemStack {
	private CustomMaterial material = null;
	private ItemStack handle;

	public CustomItemStack(Material mat, int amount, short damage) {
		this.handle = new ItemStack(mat, amount, damage);
	}

	public CustomItemStack(int id, int amount, short damage) {
		this.handle = new ItemStack(id, amount, damage);
	}

	public CustomItemStack(int id, int amount) {
		this.handle = new ItemStack(id, amount);
	}

	public CustomItemStack(Material mat, int amount) {
		this.handle = new ItemStack(mat, amount);
	}

	public CustomItemStack(Material mat) {
		this.handle = new ItemStack(mat);
	}

	/**
	 * This loads a nms itemstack.
	 * @param itemstack The itemstack.
	 */
	public CustomItemStack(Object itemstack) {
		this((ItemStack) (ReflectionUtils.getMethodObject(Classes.CB_CRAFT_ITEMSTACK, "asCraftMirror", new Class[] { Classes.NMS_ITEMSTACK }, null, new Object[] { itemstack })));
	}

	/**
	 * Creating a custom itemstack from a regular one.
	 * @param itemstack The itemstack.
	 */
	public CustomItemStack(ItemStack itemstack) {
		this(itemstack.getType(), itemstack.getAmount(), (short) itemstack.getDurability());
		this.setItemMeta(itemstack.getItemMeta());

		this.setTag(TagUtils.getTag(itemstack));
		TagCompound tag = this.getTag();
		if (tag != null && tag.hasKey(MaterialData.DATA_PATH)) {
			this.material = MaterialData.getMaterialByCustomId(tag.getInteger(MaterialData.DATA_PATH));
		}
	}

	/**
	 * Creating a custom itemstack from a custom material.
	 * @param material The material.
	 * @param amount The amount.
	 */
	public CustomItemStack(CustomMaterial material, int amount) {
		this(material);
		this.setAmount(amount);
	}

	/**
	 * Creating a custom itemstack from a custom material.
	 * @param material The material.
	 */
	public CustomItemStack(CustomMaterial material) {
		this(material.getMinecraftId(), 1);
		this.material = material;

		if (material.getData() != -1) {
			this.handle.setDurability(Short.valueOf(material.getData() + ""));
		}

		this.setName(material.getName());
		this.setLore(material.getLore());
		this.setColor(material.getColor());
		this.setSkullOwner(material.getSkullOwner());

		if (material.getEnchantments() != null) {
			for (EnchantmentInstance e : material.getEnchantments()) {
				this.addEnchantment(e.getEnchantment(), e.getLvl());
			}
		}

		if (material.getColor() != null) {
			this.setColor(material.getColor());
		}

		if (material.getMap() != null) {
			material.getMap().apply(this.handle);
		}

		TagCompound tag = this.getTag() == null ? new TagCompound() : this.getTag();
		tag.setInteger(MaterialData.DATA_PATH, material.getCustomId());
		this.setTag(tag);
	}

	public void setAmount(int amount) {
		this.handle.setAmount(amount);
	}

	public void addUnsafeEnchantment(Enchantment enchantment, int lvl) {
		this.handle.addUnsafeEnchantment(enchantment, lvl);

		if (enchantment instanceof EnchantmentCustom) {
			EnchantmentCustom e = (EnchantmentCustom) enchantment;

			if (e.getEnchantmentName() != null) {
				this.addLore(e.getEnchantmentName());
			}
		}
	}

	public void addUnsafeEnchantments(Map<Enchantment, Integer> enchantments) {
		for (Entry<Enchantment, Integer> e : enchantments.entrySet()) {
			this.addUnsafeEnchantment(e.getKey(), e.getValue());
		}
	}

	public void addEnchantment(Enchantment enchantment, int lvl) {
		this.addUnsafeEnchantment(enchantment, lvl);
	}

	public void addEnchantments(Map<Enchantment, Integer> enchantments) {
		this.addUnsafeEnchantments(enchantments);
	}

	public Map<Enchantment, Integer> getEnchantments() {
		return this.handle.getEnchantments();
	}

	/**
	 * Returns the custom material the item is holding.
	 * @return The material.
	 */
	public CustomMaterial getMaterial() {
		return this.material;
	}

	/**
	 * Returns if the item is a custom item.
	 * @return If its a custom item.
	 */
	public boolean isCustomItem() {
		return this.material != null;
	}

	public Material getType() {
		return this.handle.getType();
	}

	public short getDurability() {
		return this.handle.getDurability();
	}

	public void setDurability(short durability) {
		this.handle.setDurability(durability);
	}

	public int getTypeId() {
		return this.handle.getTypeId();
	}

	public TagCompound getTag() {
		return TagUtils.getTag(this.handle);
	}

	public void setTag(TagCompound tag) {
		this.handle = TagUtils.setTag(this.handle, tag);
	}

	/**
	 * Sets the display name.
	 * @param name The name.
	 */
	public void setName(String name) {
		ItemMeta m = this.getItemMeta();
		m.setDisplayName(name != null ? name : this.getName());
		this.setItemMeta(m);
	}

	/**
	 * Returns the diplay name.
	 * @return The name.
	 */
	public String getName() {
		return this.getItemMeta().getDisplayName();
	}

	/**
	 * Sets the lore with removing the old ones.
	 * @param lore The lore.
	 */
	public void setLore(List<String> lore) {
		ItemMeta m = this.getItemMeta();
		List<String> l = m.hasLore() ? m.getLore() : new ArrayList<String>();

		if (lore != null) {
			l.addAll(lore);
		}

		m.setLore(l);
		this.setItemMeta(m);
	}

	public void setLore(String... lore) {
		this.setLore(lore == null ? null : Arrays.asList(lore));
	}

	/**
	 * Adds a list of lore.
	 * @param lore The lore.
	 */
	public void addLore(List<String> lore) {
		ItemMeta m = this.getItemMeta();
		List<String> l = m.hasLore() ? m.getLore() : new ArrayList<String>();

		if (lore != null) {
			l.addAll(lore);
		}

		m.setLore(l);
		this.setItemMeta(m);
	}

	public void addLore(String... lore) {
		this.addLore(lore == null ? null : Arrays.asList(lore));
	}

	/**
	 * Returns the lore the item contains.
	 * @return The lore.
	 */
	public List<String> getLore() {
		ItemMeta m = this.getItemMeta();
		return m == null ? null : m.getLore();
	}

	public boolean hasLore() {
		ItemMeta m = this.getItemMeta();
		return m == null ? false : m.hasLore();
	}

	public boolean isSimilar(ItemStack itemstack) {
		return this.handle.isSimilar(itemstack) && InventoryUtils.doItemsMatch(this, itemstack == null ? null : new CustomItemStack(itemstack));
	}

	/**
	 * Sets the owner name of a skull.
	 * @param name The name.
	 */
	public void setSkullOwner(String name) {
		if (name == null) {
			name = "";
		}

		ItemMeta m = this.getItemMeta();
		if (m instanceof SkullMeta) {
			((SkullMeta) m).setOwner(name);
			this.setItemMeta(m);
		}
	}

	/**
	 * Returns the owner name of the skull.
	 * @return The name.
	 */
	public String getSkullOwner() {
		ItemMeta m = this.getItemMeta();

		if (m instanceof SkullMeta) {
			return ((SkullMeta) m).getOwner().length() > 0 ? ((SkullMeta) m).getOwner() : null;
		}

		return null;
	}
	
	/**
	 * Sets the color of a item if its dyeable.
	 * @param color The bukkit color.
	 */
	public void setBukkitColor(org.bukkit.Color color) {
		ItemMeta m = this.getItemMeta();

		if (m instanceof LeatherArmorMeta) {
			((LeatherArmorMeta) m).setColor(color);
			this.setItemMeta(m);
		}
	}

	/**
	 * Sets the color of a item if its dyeable.
	 * @param color The color.
	 */
	public void setColor(Color color) {
		org.bukkit.Color c = color == null ? null : org.bukkit.Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue());
		this.setBukkitColor(c);
	}

	/**
	 * Returns the bukkit color of a item if its dyeable.
	 * @return The bukkit color.
	 */
	public org.bukkit.Color getBukkitColor() {
		ItemMeta m = this.getItemMeta();

		if (m instanceof LeatherArmorMeta) {		
			return ((LeatherArmorMeta) m).getColor();
		}

		return null;
	}

	/**
	 * Returns the color of a item if its dyeable.
	 * @return The color.
	 */
	public Color getColor() {
    	return this.getBukkitColor() == null ? null : new Color(this.getBukkitColor().asRGB());
	}

	/**
	 * Sets the itemmeta.
	 * @param The itemmeta.
	 */
	public void setItemMeta(ItemMeta meta) {
		this.handle.setItemMeta(meta);
	}

	/**
	 * Returns the itemmeta.
	 * @return The itemmeta.
	 */
	public ItemMeta getItemMeta() {
		return this.handle.getItemMeta();
	}

	/**
	 * Returns a copy of the itemstack.
	 * @return The itemstack.
	 */
	public ItemStack getItem() {
		return this.handle.clone();
	}
}