package me.cybermaxke.materialfactory.api;

import net.md_5.bungee.api.chat.BaseComponent;

/**
 * Represents a item type, this can be either a vanilla type
 * or a custom one.
 */
public interface ItemType {

    /**
     * Gets the name of the item type.
     * 
     * @return the name
     */
    BaseComponent getName();
}
