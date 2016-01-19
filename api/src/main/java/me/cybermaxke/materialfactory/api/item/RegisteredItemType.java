package me.cybermaxke.materialfactory.api.item;

import org.bukkit.plugin.Plugin;

/**
 * Represents a {@link ItemType} that is registered in the registry.
 */
public interface RegisteredItemType {

    /**
     * Gets the {@link Plugin} that registered the {@link ItemType}.
     *
     * @return the plugin
     */
    Plugin getPlugin();

    /**
     * Gets the identifier of the registered {@link ItemType}.
     *
     * @return the identifier
     */
    String getIdentifier();

    /**
     * Gets the registered {@link ItemType}.
     *
     * @return the item type
     */
    ItemType getItemType();

}
