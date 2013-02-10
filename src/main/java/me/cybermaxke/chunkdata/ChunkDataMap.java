/**
 * 
 * This software is part of the ChunkDataAPI
 * 
 * This api allows plugin developers to store custom data into blocks.
 * 
 * ChunkDataAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * any later version.
 *  
 * ChunkDataAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ChunkDataAPI. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package me.cybermaxke.chunkdata;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class ChunkDataMap {
	private File file;
	private List<ChunkDataBlock> data =  new ArrayList<ChunkDataBlock>();
	private int x, z;
	private String world;
	
	public ChunkDataMap(File file, String world, int x, int z) {
		this.file = file;
		this.world = world;
		this.x = x;
		this.z = z;
	}
	
	public ChunkDataMap(File file, World world, int x, int z) {
		this(file, world.getName(), x, z);
	}
	
	public ChunkDataMap(File file, Chunk chunk) {
		this(file, chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getZ() {
		return this.z;
	}
	
	public World getWorld() {
		return Bukkit.getWorld(this.world);
	}
	
	public Chunk getChunk() {
		return this.getWorld().getChunkAt(this.x, this.z);
	}
	
	public ChunkDataBlock getData(int x, int y, int z) {
		for (ChunkDataBlock d : this.data) {
			if (d.getX() == x && d.getY() == y && d.getZ() == z) {
				return d;
			}
		}
		
		ChunkDataBlock d = new ChunkDataBlock(x, y, z);
		this.data.add(d);
		return d;
	}
	
	public ChunkDataBlock setData(ChunkDataBlock data) {
		ChunkDataBlock old = this.getData(data.getX(), data.getY(), data.getZ());
		this.data.remove(old);
		this.data.add(data);
		return data;
	}
	
	public void save() throws IOException {
		if (this.file.exists()) {
			this.file.delete();
		}
		
		this.file.createNewFile();
		
		YamlConfiguration c = YamlConfiguration.loadConfiguration(this.file);
		c.set("World", this.world);
		c.set("X", this.x);
		c.set("Z", this.z);
		
		ConfigurationSection data = c.createSection("Data");
		for (ChunkDataBlock d : this.data) {
			ConfigurationSection cdata = data.createSection("X" + d.getX() + "Y" + d.getY() + "Z" + d.getZ());		
			
			cdata.set("X", d.getX());
			cdata.set("Y", d.getY());
			cdata.set("Z", d.getZ());
			
			for (String s : d.getKeys()) {
				cdata.set("List." + s, d.getObject(s));
			}
		}
		
		c.save(this.file);
	}
	
	public static ChunkDataMap load(File file) {
		YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
		
		String world = c.getString("World");
		int cx = c.getInt("X");
		int cz = c.getInt("Z");
		
		ChunkDataMap m = new ChunkDataMap(file, world, cx, cz);
		
		ConfigurationSection data = c.getConfigurationSection("Data");
		if (data != null) {
			for (String s : data.getKeys(false)) {
				ConfigurationSection blockData = data.getConfigurationSection(s);
				ConfigurationSection dataValues = blockData.getConfigurationSection("List");
			
				int x = blockData.getInt("X");
				int y = blockData.getInt("Y");
				int z = blockData.getInt("Z");
			
				ChunkDataBlock d = m.getData(x, y, z);
		
				if (dataValues != null) {
					for (String s2 : dataValues.getKeys(false)) {
						d.setObject(s2, dataValues.get(s2));
					}
				}
			}
		}
		return m;
	}
}