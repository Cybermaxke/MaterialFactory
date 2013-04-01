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

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class ChunkDataListener implements Listener {
	private ChunkDataAPI api;

	public ChunkDataListener(ChunkDataAPI api) {
		api.getPlugin().getServer().getPluginManager().registerEvents(this, api.getPlugin());
		this.api = api;
	}

	@EventHandler
	public void onWorldUnload(WorldUnloadEvent e) {
		for (Chunk c : e.getWorld().getLoadedChunks()) {
			try {
				this.api.getData(c).save();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@EventHandler
	public void onWorldLoad(WorldLoadEvent e) {
		this.api.loadData(e.getWorld());
	}

	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent e) {
		try {
			this.api.getData(e.getChunk()).save();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@EventHandler
	public void onPluginDisable(PluginDisableEvent e) {
		if (e.getPlugin().getName().equals(this.api.getPlugin().getName())) {
			for (World w : Bukkit.getServer().getWorlds()) {
				for (Chunk c : w.getLoadedChunks()) {
					try {
						this.api.getData(c).save();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}
}