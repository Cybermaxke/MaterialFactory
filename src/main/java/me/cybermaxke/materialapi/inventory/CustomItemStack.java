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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.cybermaxke.materialapi.enchantment.EnchantmentInstance;
import me.cybermaxke.materialapi.material.CustomMaterial;
import me.cybermaxke.materialapi.material.MaterialData;
import me.cybermaxke.materialapi.utils.Classes;
import me.cybermaxke.materialapi.utils.InventoryUtils;
import me.cybermaxke.materialapi.utils.ReflectionUtils;
import me.cybermaxke.tagutils.Tag;
import me.cybermaxke.tagutils.TagCompound;
import me.cybermaxke.tagutils.TagList;
import me.cybermaxke.tagutils.TagUtils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/**
 * A custom itemstack to manage the custom materials.
 */
public class CustomItemStack {
	public static final String COLOR = "color";

	public static final String SKULL_OWNER = "SkullOwner";

	public static final String DISPLAY = "display";
	public static final String NAME = "Name";
	public static final String LORE = "Lore";

	public static final String ENCHANTMENTS = "ench";
	public static final String ENCHANTMENTS_ID = "id";
	public static final String ENCHANTMENTS_LVL = "lvl";

	public static final String REPAIR = "RepairCost";

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

		TagCompound tag = TagUtils.getTag(itemstack);
		this.setTag(tag);
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

	public boolean addEnchantment(Enchantment enchantment, int lvl) {
		return this.addEnchantment(enchantment, lvl, false);
	}

	public boolean addEnchantment(Enchantment enchantment, int lvl, boolean force) {
		if (!force) {
			if (lvl > enchantment.getMaxLevel() || lvl < enchantment.getStartLevel()) {
				return false;
			}

			if (this.hasEnchantments()) {
				for (Enchantment e : this.getEnchantments().keySet()) {
					if (e.conflictsWith(enchantment)) {
						return false;
					}
				}
			}
		}

		TagCompound tag = this.getTag();
		if (!this.hasEnchantments()) {
			tag.set(ENCHANTMENTS, new TagList());
		}

		TagCompound c = new TagCompound();
		c.setInteger(ENCHANTMENTS_ID, enchantment.getId());
		c.setInteger(ENCHANTMENTS_LVL, lvl);
		
		tag.getList(ENCHANTMENTS).getValue().add(c);
		this.setTag(tag);
		return true;
	}

	public void addEnchantments(Map<Enchantment, Integer> enchantments) {
		this.addEnchantments(enchantments, false);
	}
	
	public void addEnchantments(Map<Enchantment, Integer> enchantments, boolean force) {
		for (Entry<Enchantment, Integer> e : enchantments.entrySet()) {
			this.addEnchantment(e.getKey(), e.getValue(), force);
		}
	}

	public void setEnchantments(Map<Enchantment, Integer> enchantments) {
		this.setEnchantments(enchantments, false);
	}

	public void setEnchantments(Map<Enchantment, Integer> enchantments, boolean force) {
		this.clearEnchantments();
		this.addEnchantments(enchantments, force);
	}

	public void clearEnchantments() {
		TagCompound tag = this.getTag();
		tag.remove(ENCHANTMENTS);
		this.setTag(tag);
	}

	public int getEnchantmentLevel(Enchantment enchantment) {
		return this.hasEnchantments() && this.hasEnchantment(enchantment) ? this.getEnchantments().get(enchantment) : 0;
	}

	public Map<Enchantment, Integer> getEnchantments() {
		Map<Enchantment, Integer> m = new HashMap<Enchantment, Integer>();
		TagCompound tag = this.getTag();

		if (this.hasEnchantments()) {
			return m;
		}

		for (Tag<?> t : tag.getList(ENCHANTMENTS).getValue()) {
			TagCompound c = (TagCompound) t;		
			m.put(Enchantment.getById(c.getInteger(ENCHANTMENTS_ID)), c.getInteger(ENCHANTMENTS_LVL));
		}

		return m;
	}

	public boolean hasEnchantment(Enchantment enchantment) {
		return this.hasEnchantments() && this.getEnchantments().containsKey(enchantment);
	}

	public boolean hasEnchantments() {
		return this.getTag().hasKey(ENCHANTMENTS);
	}

	public boolean removeEnchantment(Enchantment enchantment) {
		boolean b = this.hasEnchantment(enchantment);
		TagCompound tag = this.getTag();
		tag.getList(ENCHANTMENTS).getValue().remove(new TagCompound(enchantment.getName()));
		this.setTag(tag);
		return b;
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

	private TagCompound getDisplay() {
		TagCompound tag = this.getTag();

		if (!tag.hasKey(DISPLAY)) {
			tag.set(DISPLAY, new TagCompound());
		}

		return tag.getCompound(DISPLAY);
	}

	/**
	 * Sets the display name.
	 * @param name
	 */
	public void setName(String name) {
		TagCompound dis = this.getDisplay();
		TagCompound tag = this.getTag();

		dis.setString(NAME, name);

		tag.setCompound(DISPLAY, dis);
		this.setTag(tag);
	}

	/**
	 * Gets the diplay name.
	 * @return name.
	 */
	public String getName() {
		TagCompound dis = this.getDisplay();
		return dis.hasKey(NAME) ? dis.getString(NAME) : null;
	}

	/**
	 * Gets if there is a display name.
	 */
	public boolean hasName() {
		return this.getName() != null;
	}

	/**
	 * Sets the lore with removing the old ones.
	 * @param lore
	 */
	public void setLore(List<String> lore) {
		TagCompound dis = this.getDisplay();
		TagCompound tag = this.getTag();

		if (lore != null) {
			dis.setStringList(DISPLAY, lore);
		} else {
			dis.remove(DISPLAY);
		}

		tag.setCompound(DISPLAY, dis);
		this.setTag(tag);
	}

	/**
	 * Sets the lore with removing the old ones.
	 * @param lore
	 */
	public void setLore(String... lore) {
		this.setLore(lore == null ? null : Arrays.asList(lore));
	}

	/**
	 * Adds a list of lore.
	 * @param lore
	 */
	public void addLore(List<String> lore) {
		List<String> l = this.getLore();

		if (lore != null) {
			l.addAll(lore);
		}

		this.setLore(l);
	}

	/**
	 * Adds a list of lore.
	 * @param lore
	 */
	public void addLore(String... lore) {
		this.addLore(lore == null ? null : Arrays.asList(lore));
	}

	/**
	 * Returns the lore the item contains.
	 * @return lore
	 */
	public List<String> getLore() {
		TagCompound dis = this.getDisplay();
		return dis.hasKey(LORE) ? dis.getStringList(LORE) : new ArrayList<String>();
	}

	/**
	 * Gets if there is lore.
	 */
	public boolean hasLore() {
		return !this.getLore().isEmpty();
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

		TagCompound tag = this.getTag();
		tag.setString(SKULL_OWNER, name);
		this.setTag(tag);
	}

	/**
	 * Returns the owner name of the skull.
	 * @return The name.
	 */
	public String getSkullOwner() {
		TagCompound tag = this.getTag();
		return tag.hasKey(SKULL_OWNER) && !tag.getString(SKULL_OWNER).isEmpty() ? tag.getString(SKULL_OWNER) : null;
	}

	/**
	 * Sets the color of a item if its dyeable.
	 * @param color The bukkit color.
	 */
	public void setBukkitColor(org.bukkit.Color color) {
		TagCompound dis = this.getDisplay();
		TagCompound tag = this.getTag();

		if (color != null) {
			dis.setInteger(COLOR, color.asRGB());
		} else {
			dis.remove(COLOR);
		}

		tag.set(DISPLAY, dis);
		this.setTag(tag);
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
		TagCompound dis = this.getDisplay();
		return dis.hasKey(COLOR) ? org.bukkit.Color.fromRGB(dis.getInteger(COLOR)) : null;
	}

	/**
	 * Returns the color of a item if its dyeable.
	 * @return The color.
	 */
	public Color getColor() {
    	return this.getBukkitColor() == null ? null : new Color(this.getBukkitColor().asRGB());
	}

	/**
	 * Returns a copy of the itemstack.
	 * @return The itemstack.
	 */
	public ItemStack getItem() {
		return this.handle.clone();
	}

	/**
	 * Gets the repair cost.
	 * @return cost
	 */
	public int getRepairCost() {
		return this.hasRepairCost() ? this.getTag().getInteger(REPAIR) : 0;
	}

	/**
	 * Gets if the repair cost exists.
	 * @return exists
	 */
	public boolean hasRepairCost() {
		return this.getTag().hasKey(REPAIR) && this.getTag().getInteger(REPAIR) > 0;
	}

	/**
	 * Sets the repair cost.
	 * @param cost
	 */
	public void setRepairCost(int cost) {
		this.getTag().setInteger(REPAIR, cost);
	}
}