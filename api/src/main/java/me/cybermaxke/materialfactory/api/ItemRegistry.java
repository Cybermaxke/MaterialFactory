package me.cybermaxke.materialfactory.api;

import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Optional;

public interface ItemRegistry {

    RegisteredItemType register(Plugin plugin, String identifier, ItemType itemType);

    Optional<RegisteredItemType> getRegistration(String fullIdentifier);

    Optional<RegisteredItemType> getRegistration(Plugin plugin, String identifier);

    Collection<RegisteredItemType> getRegistrations(Plugin plugin);

    Optional<ItemType> get(Plugin plugin, String identifier);

    default String getFullIdentifier(Plugin plugin, String identifier) {
        return plugin.getName().toLowerCase() + ':' + identifier.toLowerCase();
    }
}
