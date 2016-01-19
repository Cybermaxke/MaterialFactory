package me.cybermaxke.materialfactory.api.item;

import me.cybermaxke.materialfactory.api.inventory.ExtendedItemStack;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.Optional;

/**
 * Represents a item type, this can be either a vanilla type
 * or a custom one.
 */
public interface ItemType {

    /**
     * Gets the default name of the item type.
     *
     * @return the name
     */
    BaseComponent getName();

    /**
     * Gets the name of the item type for the specified {@link ExtendedItemStack}.
     *
     * @param itemStack the item stack
     * @return the name
     */
    BaseComponent getNameFor(ExtendedItemStack itemStack);

    /**
     * Gets the maximum stack size of this item type.
     *
     * @return the max stack size
     */
    int getMaxStackSize();

    /**
     * Gets the {@link ItemActionHandler} of this item type.
     *
     * @return  the action handler
     */
    Optional<ItemActionHandler> getActionHandler();

    /**
     * Gets the {@link RepresentedItem} that will be displayed on the client.
     *
     * @return the viewed item
     */
    RepresentedItem getViewedItem();

}
