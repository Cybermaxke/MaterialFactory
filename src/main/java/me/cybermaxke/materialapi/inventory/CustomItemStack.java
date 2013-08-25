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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import me.cybermaxke.materialapi.enchantment.EnchantmentInstance;
import me.cybermaxke.materialapi.material.CustomMaterial;
import me.cybermaxke.materialapi.material.MaterialData;
import me.cybermaxke.materialapi.utils.Classes;
import me.cybermaxke.materialapi.utils.InventoryUtils;
import me.cybermaxke.materialapi.utils.ReflectionUtils;
import me.cybermaxke.nbtutils.NbtUnwrapUtils;
import me.cybermaxke.nbtutils.nbt.CompoundMap;
import me.cybermaxke.nbtutils.nbt.CompoundTag;
import me.cybermaxke.nbtutils.nbt.IntTag;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * A custom itemstack to manage the custom materials.
 */
public class CustomItemStack {
	private static final String DATA_TAG = "CustomNbtData";
	private static final NbtUnwrapUtils UNWRAP_UTILS = new NbtUnwrapUtils();

	private CustomMaterial material = null;
	private ItemStack handle;

	public CustomItemStack(Material mat, int amount, short damage) {
		this(new ItemStack(mat, amount, damage));
	}

	public CustomItemStack(int id, int amount, short damage) {
		this(new ItemStack(id, amount, damage));
	}

	public CustomItemStack(int id, int amount) {
		this(new ItemStack(id, amount));
	}

	public CustomItemStack(Material mat, int amount) {
		this(new ItemStack(mat, amount));
	}

	public CustomItemStack(Material mat) {
		this(new ItemStack(mat));
	}

	/**
	 * This loads a nms itemstack.
	 * @param itemstack The itemstack.
	 */
	public CustomItemStack(Object itemstack) {
		this((ItemStack) (ReflectionUtils.getMethodObject(Classes.CB_CRAFT_ITEMSTACK,
				"asCraftMirror", new Class[] { Classes.NMS_ITEMSTACK }, null,
				new Object[] { itemstack })));
	}

	/**
	 * Creating a custom itemstack from a regular one.
	 * @param itemstack The itemstack.
	 */
	public CustomItemStack(ItemStack itemstack) {
		this.handle = UNWRAP_UTILS.getCraftItem(itemstack);
		//this(itemstack.getType(), itemstack.getAmount(), (short) itemstack.getDurability());

		CompoundMap map = this.getTag().getValue();
		if (map.containsKey(MaterialData.DATA_PATH)) {
			this.material = MaterialData
					.getMaterialByCustomId(((IntTag) map.get(MaterialData.DATA_PATH)).getValue());
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

		ItemMeta meta = this.getMeta();
		if (meta != null) {
			meta.setDisplayName(material.getName());
			meta.setLore(material.getLore() == null ? new ArrayList<String>() :
				Arrays.asList(material.getLore()));

			if (material.getEnchantments() != null) {
				for (EnchantmentInstance e : material.getEnchantments()) {
					this.addEnchantment(e.getEnchantment(), e.getLvl());
				}
			}

			if (meta instanceof SkullMeta) {
				String owner = material.getSkullOwner();
				((SkullMeta) meta).setOwner(owner == null ? "" : owner);
			} else if (meta instanceof LeatherArmorMeta) {
				Color color = material.getColor();
				if (color != null) {
					((LeatherArmorMeta) meta).setColor(org.bukkit.Color.fromRGB(
							material.getColor().getRGB()));
				}
			}
		}

		if (material.getMap() != null) {
			material.getMap().apply(this.handle);
		}

		CompoundMap map = this.getDataMap();
		map.put(new IntTag(MaterialData.DATA_PATH, material.getCustomId()));
		this.setDataMap(map);
	}

	public void setAmount(int amount) {
		this.handle.setAmount(amount);
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

	public CompoundTag getTag() {
		CompoundTag tag = UNWRAP_UTILS.getTag(this.handle);
		return tag == null ? new CompoundTag("", new CompoundMap()) : tag;
	}

	public void setTag(CompoundTag tag) {
		UNWRAP_UTILS.setTag(this.handle, tag);
	}

	public CompoundMap getDataMap() {
		CompoundMap map = this.getTag().getValue();
		return map.containsKey(DATA_TAG) ? ((CompoundTag) map.get(DATA_TAG)).getValue() :
			new CompoundMap();
	}

	public void setDataMap(CompoundMap map) {
		CompoundTag tag = this.getTag();
		CompoundMap map1 = tag.getValue();
		map1.put(new CompoundTag(DATA_TAG, map));

		this.setTag(tag);
	}

	public ItemMeta getMeta() {
		return this.handle.getItemMeta();
	}

	public boolean hasMeta() {
		return this.getMeta() != null;
	}

	public void setMeta(ItemMeta meta) {
		CompoundMap map = this.getDataMap();
		this.handle.setItemMeta(meta);
		this.setDataMap(map);
	}

	public boolean isSimilar(ItemStack itemstack) {
		return this.handle.isSimilar(itemstack) && InventoryUtils.doItemsMatch(this, itemstack == null ? null
				: new CustomItemStack(itemstack));
	}

	/**
	 * Returns the itemstack.
	 * @return The itemstack.
	 */
	public ItemStack getItem() {
		return this.handle;
	}

	/**
	 * Sets the display name.
	 * @param name
	 */
	@Deprecated
	public void setName(String name) {
		ItemMeta meta = this.getMeta();

		if (meta != null) {
			meta.setDisplayName(name);
			this.setMeta(meta);
		}
	}

	/**
	 * Gets the diplay name.
	 * @return name.
	 */
	@Deprecated
	public String getName() {
		return this.hasMeta() ? this.getMeta().getDisplayName() : null;
	}

	/**
	 * Gets if there is a display name.
	 */
	@Deprecated
	public boolean hasName() {
		return this.getName() != null;
	}

	/**
	 * Sets the lore with removing the old ones.
	 * @param lore
	 */
	@Deprecated
	public void setLore(List<String> lore) {
		ItemMeta meta = this.getMeta();

		if (meta != null) {
			meta.setLore(lore);
			this.setMeta(meta);
		}
	}

	/**
	 * Sets the lore with removing the old ones.
	 * @param lore
	 */
	@Deprecated
	public void setLore(String... lore) {
		this.setLore(lore == null ? null : Arrays.asList(lore));
	}

	/**
	 * Adds a list of lore.
	 * @param lore
	 */
	@Deprecated
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
	@Deprecated
	public void addLore(String... lore) {
		this.addLore(lore == null ? null : Arrays.asList(lore));
	}

	/**
	 * Returns the lore the item contains.
	 * @return lore
	 */
	@Deprecated
	public List<String> getLore() {
		return this.hasMeta() ? this.getMeta().getLore() : new ArrayList<String>();
	}

	/**
	 * Gets if there is lore.
	 */
	@Deprecated
	public boolean hasLore() {
		return !this.getLore().isEmpty();
	}

	/**
	 * Sets the owner name of a skull.
	 * @param name The name.
	 */
	@Deprecated
	public void setSkullOwner(String name) {
		ItemMeta meta = this.getMeta();

		if (meta != null && meta instanceof SkullMeta) {
			((SkullMeta) meta).setOwner(name == null ? "" : name);
			this.setMeta(meta);
		}
	}

	/**
	 * Returns the owner name of the skull.
	 * @return The name.
	 */
	@Deprecated
	public String getSkullOwner() {
		ItemMeta meta = this.getMeta();
		return meta != null && meta instanceof SkullMeta ? ((SkullMeta) meta).getOwner() : null;
	}

	/**
	 * Sets the color of a item if its dyeable.
	 * @param color The bukkit color.
	 */
	@Deprecated
	public void setBukkitColor(org.bukkit.Color color) {
		ItemMeta meta = this.getMeta();

		if (meta != null && meta instanceof LeatherArmorMeta) {
			((LeatherArmorMeta) meta).setColor(color);
			this.setMeta(meta);
		}
	}

	/**
	 * Sets the color of a item if its dyeable.
	 * @param color The color.
	 */
	@Deprecated
	public void setColor(Color color) {
		org.bukkit.Color c = color == null ? null :
			org.bukkit.Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue());
		this.setBukkitColor(c);
	}

	/**
	 * Returns the bukkit color of a item if its dyeable.
	 * @return The bukkit color.
	 */
	@Deprecated
	public org.bukkit.Color getBukkitColor() {
		ItemMeta meta = this.getMeta();
		return meta != null && meta instanceof LeatherArmorMeta ?
				((LeatherArmorMeta) meta).getColor() : null;
	}

	/**
	 * Returns the color of a item if its dyeable.
	 * @return The color.
	 */
	@Deprecated
	public Color getColor() {
    	return this.getBukkitColor() == null ? null : new Color(this.getBukkitColor().asRGB());
	}

	/**
	 * Gets the repair cost.
	 * @return cost
	 */
	@Deprecated
	public int getRepairCost() {
		ItemMeta meta = this.getMeta();
		return meta != null && meta instanceof Repairable ?
				((Repairable) meta).getRepairCost() : 0;
	}

	/**
	 * Gets if the repair cost exists.
	 * @return exists
	 */
	@Deprecated
	public boolean hasRepairCost() {
		return this.getRepairCost() > 0;
	}

	/**
	 * Sets the repair cost.
	 * @param cost
	 */
	@Deprecated
	public void setRepairCost(int cost) {
		ItemMeta meta = this.getMeta();

		if (meta != null && meta instanceof Repairable) {
			((Repairable) meta).setRepairCost(cost);
			this.setMeta(meta);
		}
	}

	public boolean addEnchantment(Enchantment enchantment, int lvl) {
		return this.addEnchantment(enchantment, lvl, false);
	}

	public boolean addEnchantment(Enchantment enchantment, int lvl, boolean force) {
		ItemMeta meta = this.getMeta();

		boolean succes = false;
		if (meta != null) {
			succes = meta.addEnchant(enchantment, lvl, force);
			this.setMeta(meta);
		}

		return succes;
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
		ItemMeta meta = this.getMeta();

		if (meta != null) {
			Set<Enchantment> enchants = meta.getEnchants().keySet();
			Iterator<Enchantment> it = enchants.iterator();

			while (it.hasNext()) {
				Enchantment enchant = it.next();
				it.remove();
				meta.removeEnchant(enchant);
			}

			this.setMeta(meta);
		}
	}

	public int getEnchantmentLevel(Enchantment enchantment) {
		return this.hasEnchantments() && this.hasEnchantment(enchantment) ?
				this.getEnchantments().get(enchantment) : 0;
	}

	public Map<Enchantment, Integer> getEnchantments() {
		return this.hasMeta() ? this.getMeta().getEnchants() : new HashMap<Enchantment, Integer>();
	}

	public boolean hasEnchantment(Enchantment enchantment) {
		return this.hasEnchantments() && this.getEnchantments().containsKey(enchantment);
	}

	public boolean hasEnchantments() {
		return this.getEnchantments() != null && !this.getEnchantments().isEmpty();
	}

	public boolean removeEnchantment(Enchantment enchantment) {
		ItemMeta meta = this.getMeta();

		if (meta != null) {
			boolean succes = meta.removeEnchant(enchantment);
			this.setMeta(meta);
			return succes;
		}

		return false;
	}
}