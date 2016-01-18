package me.cybermaxke.materialfactory.api;

import me.cybermaxke.materialfactory.api.data.DataContainer;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Represents a extended version of the {@link ItemMeta} that allows custom
 * data to be applied.
 */
public interface ItemData extends ItemMeta {

    /**
     * Gets the {@link DataContainer} that contains the custom data that
     * is attached to this item data.
     *
     * <p>This container is a live copy of the custom data, this means that
     * all the modifications that are will be applied directly to the item
     * meta.</p>
     *
     * @return the data container
     */
    DataContainer getDataContainer();

    /**
     * Sets the {@link DataContainer} that contains the custom data that
     * is attached to this item data.
     *
     * <p>The container the will be applied will be cloned when the data
     * container is set, any further modifications to the data container
     * will require the data to be applied again.</p>
     *
     * @param dataContainer the data container
     */
    void setDataContainer(DataContainer dataContainer);

    /**
     * Gets whether the unbreakable tag is set to {@code true}.
     *
     * @return is unbreakable tag set to true
     */
    default boolean isUnbreakable() {
        return this.spigot().isUnbreakable();
    }

    /**
     * Sets the state of the unbreakable tag.
     *
     * @param state the state
     */
    default void setUnbreakable(boolean state) {
        this.spigot().setUnbreakable(state);
    }

}
