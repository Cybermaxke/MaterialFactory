package me.cybermaxke.materialfactory.common;

import org.bukkit.plugin.Plugin;

import me.cybermaxke.materialfactory.api.ItemType;
import me.cybermaxke.materialfactory.api.RegisteredItemType;

public class SimpleRegisteredItemType implements RegisteredItemType {

    private final Plugin plugin;
    private final String identifier;
    private final ItemType itemType;
	
	public SimpleRegisteredItemType(Plugin plugin, String identifier, ItemType itemType) {
        this.identifier = identifier;
        this.itemType = itemType;
		this.plugin = plugin;
	}

    @Override
    public Plugin getPlugin() {
		return this.plugin;
	}

    @Override
    public String getIdentifier() {
		return this.identifier;
	}

    @Override
    public ItemType getItemType() {
		return this.itemType;
	}

}
