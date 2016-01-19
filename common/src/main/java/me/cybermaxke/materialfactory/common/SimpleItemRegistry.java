package me.cybermaxke.materialfactory.common;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableList;
import me.cybermaxke.materialfactory.api.item.ItemRegistry;
import me.cybermaxke.materialfactory.api.item.ItemType;
import me.cybermaxke.materialfactory.api.item.RegisteredItemType;
import me.cybermaxke.materialfactory.common.util.ReflectionHelper;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class SimpleItemRegistry implements ItemRegistry {

    public static void setInstance(ItemRegistry itemRegistry) {
        try {
            ReflectionHelper.setField(ItemRegistry.class.getDeclaredField("instance"), null, itemRegistry);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final Map<ItemType, RegisteredItemType> byItemType = new HashMap<>();
    private final Map<String, RegisteredItemType> byFullIdentifier = new HashMap<>();

    @Override
    public RegisteredItemType register(Plugin plugin, String identifier, ItemType itemType) {
        checkNotNull(plugin, "plugin");
        checkNotNull(identifier, "identifier");
        checkNotNull(itemType, "itemType");
        checkArgument(!identifier.isEmpty(), "identifier may not be empty");
        checkArgument(!this.byItemType.containsKey(itemType), "ItemType is already registered");
        final String fullIdentifier = this.getFullIdentifier(plugin, identifier);
        checkArgument(!this.byFullIdentifier.containsKey(fullIdentifier), "Identifier is already in use (" + identifier + ")");
        final RegisteredItemType registeredItemType = new SimpleRegisteredItemType(plugin, identifier, itemType);
        this.byItemType.put(itemType, registeredItemType);
        this.byFullIdentifier.put(fullIdentifier, registeredItemType);
        return registeredItemType;
    }

    @Override
    public Optional<RegisteredItemType> getRegistration(String fullIdentifier) {
        return Optional.ofNullable(this.byFullIdentifier.get(checkNotNull(fullIdentifier, "fullIdentifier")));
    }

    @Override
    public Optional<RegisteredItemType> getRegistration(Plugin plugin, String identifier) {
        checkNotNull(plugin, "plugin");
        checkNotNull(identifier, "identifier");
        return this.byFullIdentifier.values().stream().filter(type -> type.getPlugin().equals(plugin) && type.getIdentifier()
                .equalsIgnoreCase(identifier)).findAny();
    }

    @Override
    public Optional<RegisteredItemType> getRegistration(ItemType itemType) {
        return Optional.ofNullable(this.byItemType.get(checkNotNull(itemType, "itemType")));
    }

    @Override
    public Collection<RegisteredItemType> getRegistrations(Plugin plugin) {
        checkNotNull(plugin, "plugin");
        return ImmutableList.copyOf(this.byFullIdentifier.values().stream().filter(type -> type.getPlugin().equals(plugin))
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<ItemType> get(Plugin plugin, String identifier) {
        final Optional<RegisteredItemType> optItemType = this.getRegistration(plugin, identifier);
        return optItemType.isPresent() ? Optional.of(optItemType.get().getItemType()) : Optional.empty();
    }

    @Override
    public Optional<ItemType> get(String fullIdentifier) {
        final Optional<RegisteredItemType> optItemType = this.getRegistration(fullIdentifier);
        return optItemType.isPresent() ? Optional.of(optItemType.get().getItemType()) : Optional.empty();
    }
}
