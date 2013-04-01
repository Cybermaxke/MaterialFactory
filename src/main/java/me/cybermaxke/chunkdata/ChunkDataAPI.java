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
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ChunkDataAPI {
	private File f;
	private Plugin plugin;
	private List<ChunkDataMap> data = new ArrayList<ChunkDataMap>();

	public ChunkDataAPI(Plugin plugin) {
		this.plugin = plugin;
		this.f = new File(this.plugin.getDataFolder() + File.separator + "ChunkData");
		new ChunkDataListener(this);
		new UpdateWorlds(this);
	}

	public Plugin getPlugin() {
		return this.plugin;
	}

	public void loadData(World world) {
		if (!this.f.exists()) {
			this.f.mkdirs();
		}

		File f = new File(this.f + File.separator + world.getName());

		if (!f.exists()) {
			return;
		}

		for (File d : f.listFiles()) {
			if (d.getName().endsWith(".cdata")) {
				this.data.add(ChunkDataMap.load(d));
			}
		}
	}
	
	public ChunkDataMap getData(Chunk chunk) {
		for (ChunkDataMap d : this.data) {
			if (chunk.getWorld().getName().equals(d.getWorld().getName()) && d.getX() == chunk.getX() && d.getZ() == chunk.getZ()) {
				return d;
			}
		}

		File f1 = new File(this.f + File.separator + chunk.getWorld().getName());

		if (!f1.exists()) {
			f1.mkdirs();
		}

		File f2 = new File(f1, "W" + chunk.getWorld().getName() + "X" + chunk.getX() + "Z" + chunk.getZ() + ".cdata");
		ChunkDataMap d = new ChunkDataMap(f2, chunk);
		this.data.add(d);
		return d;
	}

	public ChunkDataBlock getBlockData(Block block) {
		return this.getData(block.getChunk()).getData(block.getX(), block.getY(), block.getZ());
	}

	public void setBlockData(Block block, ChunkDataBlock data) {
		this.getData(block.getChunk()).setData(data);
	}

	public class UpdateWorlds extends BukkitRunnable {
		private ChunkDataAPI api;

		public UpdateWorlds(ChunkDataAPI api) {
			this.api = api;
			this.runTaskLater(api.getPlugin(), 60L);
		}

		@Override
		public void run() {
			for (World w : Bukkit.getWorlds()) {
				this.api.loadData(w);
			}
		}
	}
}