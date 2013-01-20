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

import java.awt.image.BufferedImage;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

/**
 * Rendering images on a map and removing the cursors.
 */
public class ImageMapRenderer extends MapRenderer {
	private BufferedImage img;
	
	public ImageMapRenderer(BufferedImage image) {
		this.img = image;
	}

	@Override
	public void render(MapView view, MapCanvas canvas, Player player) {
		if (this.img != null) {
			canvas.drawImage(0, 0, this.img);
			
			/**
			 * Removing the cursors is broken?
			 */
			canvas.setCursors(new MapCursorCollection());
		}
	}
}