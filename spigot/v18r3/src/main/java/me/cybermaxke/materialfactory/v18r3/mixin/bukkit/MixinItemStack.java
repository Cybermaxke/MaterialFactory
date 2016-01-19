package me.cybermaxke.materialfactory.v18r3.mixin.bukkit;

import static com.google.common.base.Preconditions.checkNotNull;
import static me.cybermaxke.materialfactory.common.ItemFactoryConstants.CUSTOM_ITEM_TYPE;

import me.cybermaxke.materialfactory.api.inventory.ExtendedItemStack;
import me.cybermaxke.materialfactory.api.inventory.ItemData;
import me.cybermaxke.materialfactory.api.item.ItemRegistry;
import me.cybermaxke.materialfactory.api.item.ItemType;
import me.cybermaxke.materialfactory.api.item.ItemTypes;
import me.cybermaxke.materialfactory.api.item.RegisteredItemType;
import me.cybermaxke.materialfactory.api.item.VanillaItemType;
import me.cybermaxke.materialfactory.v18r3.interfaces.IMixinCraftItemStack;
import me.cybermaxke.materialfactory.v18r3.interfaces.IMixinItemMeta;
import me.cybermaxke.materialfactory.v18r3.interfaces.IMixinItemStack;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagString;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ItemStack.class)
public abstract class MixinItemStack implements Cloneable, ConfigurationSerializable, IMixinItemStack {

    @Shadow(remap = false) private int type;
    @Shadow(remap = false) private ItemMeta meta;

    private ItemType itemType;

    @Override
    public ItemType getIItemType() {
        return this.itemType;
    }

    @Override
    public void setIItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    @Override
    public void setItemType(ItemType itemType) {
        this.itemType = checkNotNull(itemType, "itemType");
        String itemId = null;
        final ItemStack item = (ItemStack) ((Object) this);
        if (itemType instanceof VanillaItemType) {
            if (this instanceof IMixinCraftItemStack) {
                ((IMixinCraftItemStack) this).getHandle().setItem((Item) itemType);
            } else {
                this.type = CraftMagicNumbers.getId((Item) itemType);
            }
        } else {
            RegisteredItemType registered = ItemRegistry.get().getRegistration(itemType).orElseThrow(() -> new IllegalArgumentException(
                    "The specified item type isn't registered!"));
            VanillaItemType itemType1 = itemType.getViewedItem().getItemType();
            if (this instanceof IMixinCraftItemStack) {
                ((IMixinCraftItemStack) this).getHandle().setItem((Item) itemType1);
            } else {
                this.type = itemType.getViewedItem().getItemType().toMaterial().getId();
            }
            int dataValue = itemType.getViewedItem().getDataValue();
            if (dataValue != -1) {
                item.setDurability((short) dataValue);
            }
            itemId = registered.getIdentifier();
        }
        if (item instanceof IMixinCraftItemStack) {
            NBTTagCompound tagCompound = ((IMixinCraftItemStack) item).getHandle().getTag();
            if (itemId == null) {
                tagCompound.remove(CUSTOM_ITEM_TYPE);
            } else {
                tagCompound.setString(CUSTOM_ITEM_TYPE, itemId);
            }
        } else if (this.meta != null) {
            Map<String, NBTBase> map = ((IMixinItemMeta) this.meta).getUnhandledTags();
            if (itemId == null) {
                map.remove(CUSTOM_ITEM_TYPE);
            } else {
                map.put(CUSTOM_ITEM_TYPE, new NBTTagString(itemId));
            }
        }
    }

    @Override
    public ItemType getItemType() {
        if (this.itemType != null) {
            return this.itemType;
        }
        final ItemStack item = (ItemStack) ((Object) this);
        String itemId = null;
        if (item instanceof IMixinCraftItemStack) {
            NBTTagCompound tagCompound = ((IMixinCraftItemStack) item).getHandle().getTag();
            if (tagCompound != null && tagCompound.hasKey(CUSTOM_ITEM_TYPE)) {
                itemId = tagCompound.getString(CUSTOM_ITEM_TYPE);
            }
        } else if (this.meta != null) {
            Map<String, NBTBase> map = ((IMixinItemMeta) this.meta).getUnhandledTags();
            if (map.containsKey(CUSTOM_ITEM_TYPE)) {
                itemId = ((NBTTagString) map.get(CUSTOM_ITEM_TYPE)).a_();
            }
        }
        if (itemId != null) {
            this.itemType = ItemRegistry.get().get(itemId).orElse(ItemTypes.UNKNOWN);
        } else {
            this.itemType = (ItemType) CraftMagicNumbers.getItem(this.type);
        }
        return this.itemType;
    }

    @Override
    public ItemData getItemMeta() {
        return (ItemData) ((ItemStack) ((Object) this)).getItemMeta();
    }

    @Override
    public ExtendedItemStack copy() {
        return (ExtendedItemStack) ((ItemStack) ((Object) this)).clone();
    }

    @Inject(method = "setTypeId(I)V", at = @At("RETURN"), remap = false)
    private void onSetTypeId(int typeId, CallbackInfo ci) {
        if (this.meta != null) {
            ((IMixinItemMeta) this.meta).getUnhandledTags().remove(CUSTOM_ITEM_TYPE);
        }
        this.itemType = (ItemType) CraftMagicNumbers.getItem(typeId);
    }

    @Inject(method = "setItemMeta0", at = @At("HEAD"), cancellable = true, remap = false)
    private void onSetItemMeta0(ItemMeta itemMeta, Material material, CallbackInfoReturnable<Boolean> ci) {
        NBTTagString itemId = null;
        if (this.meta != null) {
            Map<String, NBTBase> map = ((IMixinItemMeta) this.meta).getUnhandledTags();
            if (map.containsKey(CUSTOM_ITEM_TYPE)) {
                itemId = (NBTTagString) map.get(CUSTOM_ITEM_TYPE);
            }
        }
        if (itemMeta == null) {
            if (itemId != null) {
                this.meta = Bukkit.getItemFactory().getItemMeta(material);
                ((IMixinItemMeta) this.meta).getUnhandledTags().put(CUSTOM_ITEM_TYPE, itemId);
            } else {
                this.meta = null;
            }
            ci.setReturnValue(true);
            return;
        }
        if (!Bukkit.getItemFactory().isApplicable(itemMeta, material)) {
            ci.setReturnValue(false);
            return;
        }
        this.meta = Bukkit.getItemFactory().asMetaFor(itemMeta, material);
        if (this.meta == itemMeta) {
            this.meta = itemMeta.clone();
        }
        if (itemId != null) {
            ((IMixinItemMeta) this.meta).getUnhandledTags().put(CUSTOM_ITEM_TYPE, itemId);
        } else {
            ((IMixinItemMeta) this.meta).getUnhandledTags().remove(CUSTOM_ITEM_TYPE);
        }
        ci.setReturnValue(true);
    }
}
