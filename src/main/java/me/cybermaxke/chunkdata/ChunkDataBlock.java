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

public class ChunkDataBlock extends ChunkData {
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

	public ChunkDataBlock clone() {
		return (ChunkDataBlock) new ChunkDataBlock(this.x, this.y, this.z).setTag(this.getTag().clone());
	}
}