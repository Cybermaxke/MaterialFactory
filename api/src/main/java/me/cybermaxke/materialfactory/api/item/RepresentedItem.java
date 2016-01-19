package me.cybermaxke.materialfactory.api.item;

import static com.google.common.base.Preconditions.checkNotNull;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public final class RepresentedItem {

    private final VanillaItemType itemType;
    private final int dataValue;

    /**
     * Creates a new {@link RepresentedItem} for the specified {@link MaterialData}.
     *
     * @param materialData the material data
     * @throws IllegalArgumentException if the material isn't a valid item type
     */
    public RepresentedItem(MaterialData materialData) throws IllegalArgumentException {
        this(checkNotNull(materialData, "materialData").getItemType(), materialData.getData());
    }

    /**
     * Creates a new {@link RepresentedItem} for the specified {@link Material}.
     *
     * @param material the material
     * @throws IllegalArgumentException if the material isn't a valid item type
     */
    public RepresentedItem(Material material) throws IllegalArgumentException {
        this(material, -1);
    }

    /**
     * Creates a new {@link RepresentedItem} for the specified {@link Material} and data value.
     *
     * @param material the material
     * @param dataValue the data value
     * @throws IllegalArgumentException if the material isn't a valid item type
     */
    public RepresentedItem(Material material, int dataValue) throws IllegalArgumentException {
        this.itemType = ItemRegistry.get().get(checkNotNull(material, "material"))
                .orElseThrow(() -> new IllegalArgumentException("Material must be a valid item type!"));
        this.dataValue = dataValue;
    }

    /**
     * Creates a new {@link RepresentedItem} for the specified {@link VanillaItemType}.
     *
     * @param itemType the item type
     */
    public RepresentedItem(VanillaItemType itemType) {
        this(itemType, -1);
    }

    /**
     * Creates a new {@link RepresentedItem} for the specified {@link VanillaItemType} and data value.
     *
     * @param itemType the item type
     * @param dataValue the data value
     */
    public RepresentedItem(VanillaItemType itemType, int dataValue) {
        this.itemType = checkNotNull(itemType, "itemType");
        this.dataValue = dataValue;
    }

    /**
     * Gets the data value, where -1 means that the data value won't affect the item.
     *
     * @return the data value
     */
    public int getDataValue() {
        return this.dataValue;
    }

    /**
     * Gets the {@link VanillaItemType}.
     *
     * @return the item type
     */
    public VanillaItemType getItemType() {
        return this.itemType;
    }
}
