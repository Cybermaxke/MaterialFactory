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
import java.io.File;

import me.cybermaxke.materialapi.utils.ImageUtils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;

public class CustomMap {
	private String id;
	private MapView map;
	private BufferedImage image;

	public CustomMap(String id) {
		this.id = id;
		this.map = MapData.addMapData(id);
		MapData.addMap(this);
	}
	
	public CustomMap(String id, File file) {
		this(id, ImageUtils.getResizedImage(file));
	}
	
	public CustomMap(String id, BufferedImage image) {
		this(id);
		this.image = image;
	}
	
	/**
	 * Returns the id of the map.
	 * @return The id.
	 */
	public String getId() {
		return this.id.toLowerCase();
	}
	
	/**
	 * Returns the bukkit map view.
	 * @return The map view.
	 */
	public MapView getMapView() {
		return this.map;
	}
	
	/**
	 * Returns the bukkit map id.
	 * @return The id.
	 */
	public short getMapId() {
		return this.map.getId();
	}
	
	/**
	 * Rendering the map with a image.
	 * @param image The image.
	 */
	public CustomMap renderImage(BufferedImage image) {
		this.map.getRenderers().clear();
		this.map.addRenderer(new ImageMapRenderer(image));
		return this;
	}
	
	/**
	 * Rendering the map with a image from a file.
	 * @param file The file.
	 */
	public CustomMap renderImage() {
		return this.image != null ? this.renderImage(this.image) : this;
	}
	
	/**
	 * Rendering the map with the current image.
	 * @param file The file.
	 */
	public CustomMap renderImage(File file) {
		return this.renderImage(ImageUtils.getResizedImage(file));
	}
	
	
	/**
	 * Applying the map to a itemstack.
	 * @param itemstack The itemstack.
	 */
	public CustomMap apply(ItemStack itemstack) {
		itemstack.setDurability(this.getMapId());
		//this.renderImage();
		return this;
	}
}