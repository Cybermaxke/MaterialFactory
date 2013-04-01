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

import me.cybermaxke.tagutils.Tag;
import me.cybermaxke.tagutils.TagCompound;
import me.cybermaxke.tagutils.TagUtils;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

public class ChunkDataMap extends ChunkData {
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

		TagCompound tag = new TagCompound();
		tag.setString("World", this.world);
		tag.setInteger("X", this.x);
		tag.setInteger("Z", this.z);
		tag.setCompound("Tag", this.getTag());

		TagCompound data = new TagCompound();
		for (ChunkDataBlock d : this.data) {
			TagCompound t = new TagCompound();
			t.setInteger("X", d.getX());
			t.setInteger("Y", d.getY());
			t.setInteger("Z", d.getZ());
			t.setCompound("Tag", d.getTag());
			data.setCompound("X" + d.getX() + "Y" + d.getY() + "Z" + d.getZ(), t);
		}

		tag.setCompound("Data", data);
		TagUtils.save(this.file, tag);
	}

	public static ChunkDataMap load(File file) {
		TagCompound tag = (TagCompound) TagUtils.load(file);

		String world = tag.getString("World");
		int cx = tag.getInteger("X");
		int cz = tag.getInteger("Z");
		TagCompound t = tag.getCompound("Tag");

		ChunkDataMap m = new ChunkDataMap(file, world, cx, cz);
		m.setTag(t);

		TagCompound data = tag.getCompound("Data");
		for (Tag<?> v : data.getValue().values()) {
			TagCompound t2 = (TagCompound) v;

			int x = t2.getInteger("X");
			int y = t2.getInteger("Y");
			int z = t2.getInteger("Z");

			ChunkDataBlock d = m.getData(x, y, z);
			d.setTag(t2.getCompound("Tag"));
		}

		return m;
	}
}