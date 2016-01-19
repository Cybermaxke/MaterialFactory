package me.cybermaxke.materialfactory.api.item;

import org.bukkit.Material;

/**
 * Represents a vanilla item type, or at least not provided
 * by this plugin. 
 */
public interface VanillaItemType extends ItemType {

    /**
     * Gets the {@link Material} that represents this item type.
     *
     * @return the material
     */
    Material toMaterial();

}
