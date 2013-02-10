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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ChunkDataBlock {
	private Map<String, Object> data = new HashMap<String, Object>();
	private int x, y, z;

	public ChunkDataBlock(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getZ() {
		return this.z;
	}
	
	public ChunkDataBlock setObjects(Map<String, Object> objects) {
		for (Entry<String, Object> e : objects.entrySet()) {
			this.data.put(e.getKey(), e.getValue());
		}
		
		return this;
	}
	
	public Map<String, Object> getObjects() {
		return this.data;
	}
	
	public ChunkDataBlock setObject(String key, Object object) {
		this.data.put(key, object);
		return this;
	}
	
	public ChunkDataBlock remove(String key) {
		this.data.remove(key);
		return this;
	}
	
	public String[] getKeys() {
		return this.data.keySet().toArray(new String[] {});
	}
	
	public Object getObject(String key) {
		return this.data.containsKey(key) ? this.data.get(key) : null;
	}
	
	public boolean hasKey(String key) {
		return this.data.containsKey(key);
	}
	
	public <T> T getObject(Class<T> clazz, String key) {
		return this.getObject(key) == null ? null : clazz.cast(this.getObject(key));
	}
	
	public Integer getInt(String key) {
		return this.getObject(Integer.class, key);
	}
	
	public ChunkDataBlock clear() {
		this.data.clear();
		return this;
	}
	
	public ChunkDataBlock clone() {
		return new ChunkDataBlock(this.x, this.y, this.z).setObjects(this.data);
	}
}