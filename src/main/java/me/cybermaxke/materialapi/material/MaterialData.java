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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.cybermaxke.chunkdata.ChunkDataAPI;

import me.cybermaxke.materialapi.MaterialAPI;
import me.cybermaxke.materialapi.enchantment.EnchantmentCustom;

import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

/**
 * Managing all the custom materials and ids.
 */
public class MaterialData {
	//private static Plugin plugin;
	
	private static File dataFolder;
	private static File dataFile;
	
	public static String DATA_PREFIX = "MMCustomID: ";
	public static String DATA_PATH = "MMCustomID";
	
	private static int data = 0;
	private static Map<Integer, CustomMaterial> byCustomId = new HashMap<Integer, CustomMaterial>();
	private static Map<String, CustomMaterial> byId = new HashMap<String, CustomMaterial>();
	private static Map<String, Integer> matData = new HashMap<String, Integer>();
	private static Map<Integer, String> matDataById = new HashMap<Integer, String>();

	public MaterialData(Plugin plugin) {
		//MaterialData.plugin = plugin;
		dataFolder = plugin.getDataFolder();
		dataFile = new File(dataFolder + File.separator + "MaterialData.yml");
		load();
	}
	
	/**
	 * Saving all the data to the config file.
	 */
	public static void save() {
		YamlConfiguration c = new YamlConfiguration();
		
		if (!dataFolder.exists()) {
			dataFolder.mkdirs();
		}
		
		if (!dataFile.exists()) {
			try {
				dataFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			c = YamlConfiguration.loadConfiguration(dataFile);
		}
		
		for (Entry<String, Integer> d : matData.entrySet()) {
			c.set(d.getKey(), d.getValue());
		}
		
		try {
			c.save(dataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loading all the data from the config file.
	 */
	public static void load() {
		if (!dataFolder.exists() || !dataFile.exists()) {
			return;
		}
		
		YamlConfiguration c = YamlConfiguration.loadConfiguration(dataFile);

		for (Entry<String, Object> d : c.getValues(false).entrySet()) {
			matData.put(d.getKey(), (Integer) d.getValue());
			matDataById.put((Integer) d.getValue(), d.getKey());
		}
	}
	
	/**
	 * Returns the custom material from the given custom id.
	 * @param id The id.
	 * @return The material.
	 */
	public static CustomMaterial getMaterialByCustomId(int id) {
		return !byCustomId.containsKey(id) ? null : byCustomId.get(id);
	}
	
	/**
	 * Returns the custom material from the given id.
	 * @param id The id.
	 * @return The material.
	 */
	public static CustomMaterial getMaterialById(String id) {
		return !byId.containsKey(id.toLowerCase()) ? null : byId.get(id.toLowerCase());
	}
	
	/**
	 * Returns the next available id.
	 * @return
	 */
	public static int getNextId() {
		while (matDataById.containsKey(data)) {
			data++;
		}
		
		return data;
	}
	
	/**
	 * Registering the custom ids for material.
	 * @param material The material.
	 * @return The custom id.
	 */
	public static int addMaterialData(CustomMaterial material) {
		if (matData.containsKey(material.getId())) {
			return matData.get(material.getId());
		}
		
		int id = getNextId();
		
		matData.put(material.getId(), id);
		matDataById.put(id, material.getId());
		return id;
	}
	
	/**
	 * Registering the material to be able to use0
	 * @param material The material.
	 * @return The material.
	 */
	public static CustomMaterial addMaterial(CustomMaterial material) {
		byId.put(material.getId(), material);
		byCustomId.put(material.getCustomId(), material);
		return material;
	}
	
	/**
	 * Returns if the given itemstack is a custom item.
	 * @param itemstack The itemstack.
	 * @return If the itemstack is a custom item.
	 */
	public static boolean isCustomItem(ItemStack itemstack) {
		return getCustomId(itemstack) != -1;
	}
	
	/**
	 * Returns the custom id of the itemstack, '-1' if it's not a custom item.
	 * @param itemstack The itemstack.
	 * @return The custom id.
	 */
	public static int getCustomId(ItemStack itemstack) {
		if (MaterialAPI.ENCHANTMENT_DATA) {
			if (!itemstack.containsEnchantment(EnchantmentCustom.DATA_ID)) {
				return -1;
			}
			
			return itemstack.getEnchantmentLevel(EnchantmentCustom.DATA_ID);
		} else {
			ItemMeta m = itemstack.getItemMeta();
		
			if (m == null || !m.hasLore()) {
				return -1;
			}
		
			List<String> l = m.getLore();
			for (int i = 0; i < l.size(); i++) {
				String s = l.get(i);
			
				if (s.contains(DATA_PREFIX)) {
					return Integer.valueOf(s.replace(DATA_PREFIX, ""));
				}
			}
		}
		
		return -1;
	}
	
	/**
	 * Returns the custom material of the block, 'null' if it's not a custom one.
	 * @param block The block.
	 * @return The material.
	 */
	public static CustomMaterial getMaterial(Block block) {
		return !isCustomBlock(block) ? null : getMaterialByCustomId(getCustomId(block));
	}
	
	/**
	 * Returns if the block is a custom one.
	 * @param block The block.
	 */
	public static boolean isCustomBlock(Block block) {
		return getCustomId(block) != -1;
	}
	
	/**
	 * Sets the custom id of a block.
	 * @param block The block.
	 * @param id The custom id.
	 * @return 
	 * @return The block.
	 */
	public static Block setCustomBlockId(Block block, int id) {
		ChunkDataAPI c = MaterialAPI.getChunkData();
		
		if (id == -1) {
			c.getBlockData(block).remove(DATA_PATH);
		} else {
			c.getBlockData(block).setObject(DATA_PATH, id);
		}
		
		return block;
	}
	
	/**
	 * Returns the custom id of a block, '-1' if its not a custom block.
	 * @param block The block.
	 * @return The custom id.
	 */
	public static int getCustomId(Block block) {
		ChunkDataAPI c = MaterialAPI.getChunkData();
		return c.getBlockData(block).hasKey(DATA_PATH) ? c.getBlockData(block).getObject(Integer.class, DATA_PATH) : -1;
	}
}