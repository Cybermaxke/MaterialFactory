package me.cybermaxke.materialfactory.api;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public final class ItemTypes {

    public static final ItemType UNKNOWN = new ItemType() {
        @Override
        public BaseComponent getName() {
            return new TextComponent("unknown");
        }
    };

    private ItemTypes() {
    }
}
