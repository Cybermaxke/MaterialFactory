package me.cybermaxke.materialfactory.v18r3.mixin.bukkit;

import me.cybermaxke.materialfactory.api.ExtendedItemStack;
import me.cybermaxke.materialfactory.api.ItemData;
import me.cybermaxke.materialfactory.api.ItemType;
import me.cybermaxke.materialfactory.api.ItemTypes;
import me.cybermaxke.materialfactory.v18r3.IExtendedItemStack;
import me.cybermaxke.materialfactory.v18r3.IItemData;
import me.cybermaxke.materialfactory.v18r3.SVanillaItemType;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagString;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(ItemStack.class)
public abstract class MixinItemStack implements Cloneable, ConfigurationSerializable, IExtendedItemStack {

    @Shadow private ItemMeta meta;

    @Override
    public ItemType getItemType() {
        ItemStack item = (ItemStack) ((Object) this);
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
    public ItemData getItemMeta() {
        return (ItemData) ((ItemStack) ((Object) this)).getItemMeta();
    }

    @Override
    public ExtendedItemStack copy() {
        return (ExtendedItemStack) ((ItemStack) ((Object) this)).clone();
    }
}
