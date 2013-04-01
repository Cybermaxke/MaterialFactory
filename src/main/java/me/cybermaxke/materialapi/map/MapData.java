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
package me.cybermaxke.materialapi.map;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Managing all the custom maps created.
 */
public class MapData {
	private static Plugin plugin;

	private static File dataFolder;
	private static File dataFile;

	private static Map<String, CustomMap> byId = new HashMap<String, CustomMap>();
	private static Map<Short, CustomMap> byMapId = new HashMap<Short, CustomMap>();
	private static Map<String, Short> mapDataById = new HashMap<String, Short>();
	private static Map<Short, String> mapData = new HashMap<Short, String>();

	public MapData(JavaPlugin plugin) {
		MapData.plugin = plugin;
		dataFolder = plugin.getDataFolder();
		dataFile = new File(dataFolder + File.separator + "MapData.yml");
		load();
	}

	/**
	 * Returns all the maps added to the data.
	 * @return The maps.
	 */
	public static CustomMap[] getMaps() {
		return byId.isEmpty() ? null : byId.values().toArray(new CustomMap[] {});
	}

	/**
	 * Creating or getting the bukkit map.
	 * @param id The id of your custom map.
	 * @return The map.
	 */
	public static MapView addMapData(String id) {
		if (mapDataById.containsKey(id.toLowerCase())) {
			short i = mapDataById.get(id.toLowerCase());

			if (plugin.getServer().getMap(i) != null) {
				return plugin.getServer().getMap(i);
			}
		}

		MapView m = plugin.getServer().createMap(plugin.getServer().getWorlds().get(0));
		mapDataById.put(id.toLowerCase(), m.getId());
		mapData.put(m.getId(), id.toLowerCase());
		return m;
	}

	/**
	 * Adding a custom map to the data to be able to use it.
	 * @param map The map.
	 */
	public static CustomMap addMap(CustomMap map) {
		byId.put(map.getId(), map);
		byMapId.put(map.getMapId(), map);
		return map;
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

		for (Entry<String, Short> d : mapDataById.entrySet()) {
			c.set(d.getKey(), d.getValue() + "");
		}

		try {
			c.save(dataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loading all the data from a config file.
	 */
	public static void load() {
		if (!dataFolder.exists() || !dataFile.exists()) {
			return;
		}

		YamlConfiguration c = YamlConfiguration.loadConfiguration(dataFile);
		for (Entry<String, Object> d : c.getValues(false).entrySet()) {
			mapDataById.put(d.getKey(), Short.valueOf((String) d.getValue()));
			mapData.put(Short.valueOf((String) d.getValue()), d.getKey());
		}
	}
}