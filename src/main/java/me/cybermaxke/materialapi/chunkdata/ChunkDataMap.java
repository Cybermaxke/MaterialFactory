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
package me.cybermaxke.materialapi.chunkdata;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.cybermaxke.nbtutils.NbtUtils;
import me.cybermaxke.nbtutils.nbt.CompoundMap;
import me.cybermaxke.nbtutils.nbt.CompoundTag;
import me.cybermaxke.nbtutils.nbt.IntTag;
import me.cybermaxke.nbtutils.nbt.StringTag;
import me.cybermaxke.nbtutils.nbt.Tag;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

public class ChunkDataMap extends ChunkData {
	private static final NbtUtils NBT_UTILS = new NbtUtils();

	private final List<ChunkDataBlock> blocks =  new ArrayList<ChunkDataBlock>();
	private final File file;
	private final String world;
	private final int x, z;

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
		for (ChunkDataBlock d : this.blocks) {
			if (d.getX() == x && d.getY() == y && d.getZ() == z) {
				return d;
			}
		}

		ChunkDataBlock d = new ChunkDataBlock(x, y, z);
		this.blocks.add(d);
		return d;
	}

	public ChunkDataBlock setData(ChunkDataBlock data) {
		ChunkDataBlock old = this.getData(data.getX(), data.getY(), data.getZ());
		this.blocks.remove(old);
		this.blocks.add(data);
		return data;
	}

	public void save() throws IOException {
		if (this.file.exists()) {
			this.file.delete();
		}

		CompoundMap map = new CompoundMap();

		map.put(new StringTag("World", this.world));
		map.put(new IntTag("X", this.x));
		map.put(new IntTag("Z", this.z));
		map.put(new CompoundTag("Tag", this.data));

		CompoundMap dataMap = new CompoundMap();
		for (ChunkDataBlock data : this.blocks) {
			CompoundMap blockMap = new CompoundMap();

			blockMap.put(new IntTag("X", data.getX()));
			blockMap.put(new IntTag("Y", data.getY()));
			blockMap.put(new IntTag("Z", data.getZ()));
			blockMap.put(new CompoundTag("Tag", data.data));

			dataMap.put(new CompoundTag("X" + data.getX() + "Y" + data.getY() + "Z" + data.getZ(),
					blockMap));
		}

		map.put(new CompoundTag("Data", dataMap));
		NBT_UTILS.save(this.file, new CompoundTag("", map));
	}

	public static ChunkDataMap load(File file) throws IOException {
		CompoundTag tag = (CompoundTag) NBT_UTILS.load(file);
		CompoundMap map = tag.getValue();

		String world = ((StringTag) map.get("World")).getValue();
		int cx = ((IntTag) map.get("X")).getValue();
		int cz = ((IntTag) map.get("Z")).getValue();

		CompoundTag tag1 = (CompoundTag) map.get("Tag");
		CompoundMap map1 = tag1.getValue();

		ChunkDataMap data = new ChunkDataMap(file, world, cx, cz);
		data.setTag(map1);

		CompoundTag tag2 = (CompoundTag) map.get("Data");
		CompoundMap map2 = tag2.getValue();
		for (Tag<?> tag3 : map2.values()) {
			CompoundTag tag4 = (CompoundTag) tag3;
			CompoundMap map4 = tag4.getValue();

			int x = ((IntTag) map4.get("X")).getValue();
			int y = ((IntTag) map4.get("Y")).getValue();
			int z = ((IntTag) map4.get("Z")).getValue();

			ChunkDataBlock data1 = data.getData(x, y, z);
			data1.setTag(((CompoundTag) map4.get("Tag")).getValue());
		}

		return data;
	}
}