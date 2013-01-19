package me.cybermaxke.materialapi.material;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class MaterialData {
	private static File dataFolder;
	private static File dataFile;
	
	public static String DATA_PREFIX = "MMCustomID: ";
	
	private static int data = 0;
	private static Map<Integer, CustomMaterial> byCustomId = new HashMap<Integer, CustomMaterial>();
	private static Map<String, CustomMaterial> byId = new HashMap<String, CustomMaterial>();
	private static Map<String, Integer> matData = new HashMap<String, Integer>();
	private static Map<Integer, String> matDataById = new HashMap<Integer, String>();

	public MaterialData(Plugin plugin) {
		dataFolder = plugin.getDataFolder();
		dataFile = new File(dataFolder + File.separator + "MaterialData.yml");
		load();
	}
	
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
	
	public static CustomMaterial getMaterialByCustomId(int id) {
		return !byCustomId.containsKey(id) ? null : byCustomId.get(id);
	}
	
	public static CustomMaterial getMaterialByCustomId(String id) {
		return !byId.containsKey(id.toLowerCase()) ? null : byId.get(id.toLowerCase());
	}
	
	public static int getNextId() {
		while (matDataById.containsKey(data)) {
			data++;
		}
		
		return data;
	}
	
	public static int addMaterialData(CustomMaterial material) {
		if (matData.containsKey(material.getId())) {
			return matData.get(material.getId());
		}
		
		int id = getNextId();
		
		matData.put(material.getId(), id);
		matDataById.put(id, material.getId());
		return id;
	}
	
	public static CustomMaterial addMaterial(CustomMaterial material) {
		byId.put(material.getId(), material);
		byCustomId.put(material.getCustomId(), material);
		return material;
	}
	
	public static boolean isCustomItem(ItemStack itemstack) {
		return getCustomId(itemstack) != -1;
	}
	
	public static int getCustomId(ItemStack itemstack) {
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
		
		return -1;
	}
}