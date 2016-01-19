package me.cybermaxke.materialfactory.api.item;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Optional;

public interface ItemRegistry {

    /**
     * The instance of the item registry.
     */
    ItemRegistry instance = null;

    /**
     * Gets the instance of the {@link ItemRegistry}.
     *
     * @return the item registry
     */
    static ItemRegistry get() {
        return instance;
    }

    /**
     * Registers a new {@link ItemType} for the specified {@link Plugin} and identifier.
     *
     * @param plugin the plugin
     * @param identifier the identifier
     * @param itemType the identifier
     * @return the registered item type entry
     */
    RegisteredItemType register(Plugin plugin, String identifier, ItemType itemType);

    Optional<RegisteredItemType> getRegistration(String fullIdentifier);

    Optional<RegisteredItemType> getRegistration(Plugin plugin, String identifier);

    Optional<RegisteredItemType> getRegistration(ItemType itemType);

    /**
     * Gets all the registrations for the specified plugin.
     *
     * @param plugin the plugin
     * @return the registered item types
     */
    Collection<RegisteredItemType> getRegistrations(Plugin plugin);

    Optional<ItemType> get(Plugin plugin, String identifier);

    Optional<ItemType> get(String fullIdentifier);

    Optional<VanillaItemType> get(Material material);

    default String getFullIdentifier(Plugin plugin, String identifier) {
        return plugin.getName().toLowerCase() + ':' + identifier.toLowerCase();
    }
}
