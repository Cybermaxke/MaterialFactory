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

import me.cybermaxke.nbtutils.nbt.CompoundMap;

public class ChunkData {
	protected CompoundMap data = new CompoundMap();

	public void setTag(CompoundMap data) {
		this.data = data;
	}

	public CompoundMap getDataMap() {
		return this.data;
	}

	public void clear() {
		this.data.clear();
	}

	@Override
	public ChunkData clone() {
		ChunkData data = new ChunkData();
		data.data = this.data;

		return data;
	}
}