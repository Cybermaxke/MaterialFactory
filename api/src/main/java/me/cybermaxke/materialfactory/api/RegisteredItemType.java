package me.cybermaxke.materialfactory.api;

import org.bukkit.plugin.Plugin;

public interface RegisteredItemType {

    Plugin getPlugin();

    String getIdentifier();

    ItemType getItemType();
}
