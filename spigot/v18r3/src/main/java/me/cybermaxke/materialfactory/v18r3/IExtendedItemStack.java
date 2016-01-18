package me.cybermaxke.materialfactory.v18r3;

import java.util.Map;

import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagString;

import org.bukkit.inventory.ItemStack;

import me.cybermaxke.materialfactory.api.ExtendedItemStack;
import me.cybermaxke.materialfactory.api.ItemData;
import me.cybermaxke.materialfactory.api.ItemType;
import me.cybermaxke.materialfactory.api.ItemTypes;

public interface IExtendedItemStack extends ExtendedItemStack {

    String customItemType = "cymb_itype";

    @Override
    default ItemType getItemType() {
        ItemStack item = (ItemStack) this;
        ItemType type = null;
        if (item.hasItemMeta()) {
            IItemData itemData = (IItemData) item.getItemMeta();
            Map<String, NBTBase> map = itemData.getUnhandledTags();
            if (map.containsKey(customItemType)) {
                String itemId = ((NBTTagString) map.get(customItemType)).a_();
                // TODO: Lookup the item type
            }
        }
        if (type == null) {
            type = SVanillaItemType.getByMaterial(item.getType());
            if (type == null) {
                type = ItemTypes.UNKNOWN;
            }
        }
        return type;
    }

    @Override
    default ItemData getItemMeta() {
        return (ItemData) ((ItemStack) this).getItemMeta();
    }

    @Override
    default ExtendedItemStack copy() {
        return (ExtendedItemStack) ((ItemStack) this).clone();
    }

}
