package me.cybermaxke.materialfactory.common.inventory;

import static com.google.common.base.Preconditions.checkNotNull;

import me.cybermaxke.materialfactory.api.inventory.ExtendedItemStack;
import me.cybermaxke.materialfactory.api.inventory.ItemFactory;
import me.cybermaxke.materialfactory.api.item.ItemType;
import me.cybermaxke.materialfactory.api.item.RepresentedItem;
import me.cybermaxke.materialfactory.api.item.VanillaItemType;
import me.cybermaxke.materialfactory.common.util.ReflectionHelper;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SimpleItemFactory implements ItemFactory {

    public static void setInstance(ItemFactory itemFactory) {
        try {
            ReflectionHelper.setField(ExtendedItemStack.class.getDeclaredField("factory"), null, itemFactory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ExtendedItemStack create(ItemType itemType, int amount) {
        checkNotNull(itemType, "itemType");
        if (itemType instanceof VanillaItemType) {
            return (ExtendedItemStack) new ItemStack(((VanillaItemType) itemType).toMaterial(), amount);
        } else {
            final RepresentedItem representedItem = itemType.getViewedItem();
            ExtendedItemStack itemStack = (ExtendedItemStack) new ItemStack(representedItem.getItemType().toMaterial(), amount,
                    (short) representedItem.getDataValue());
            itemStack.setItemType(itemType);
            return itemStack;
        }
    }

    @Override
    public ExtendedItemStack create(ItemType itemType) {
        return this.create(itemType, 1);
    }

    @Override
    public ExtendedItemStack create(Material material) throws IllegalArgumentException {
        return this.create(material, 1);
    }

    @Override
    public ExtendedItemStack create(Material material, int amount) throws IllegalArgumentException {
        return (ExtendedItemStack) new ItemStack(material, amount);
    }
}
