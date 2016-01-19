package me.cybermaxke.materialfactory.v18r3.mixin.bukkit;

import static me.cybermaxke.materialfactory.common.ItemFactoryConstants.CUSTOM_ITEM_TYPE;

import me.cybermaxke.materialfactory.api.item.ItemType;
import me.cybermaxke.materialfactory.v18r3.interfaces.IMixinCraftItemStack;
import me.cybermaxke.materialfactory.v18r3.interfaces.IMixinItemMeta;
import me.cybermaxke.materialfactory.v18r3.interfaces.IMixinItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemFactory;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.inventory.meta.ItemMeta;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CraftItemStack.class)
public abstract class MixinCraftItemStack extends MixinItemStack implements IMixinCraftItemStack {

    @Shadow(remap = false) net.minecraft.server.v1_8_R3.ItemStack handle;

    @Override
    public net.minecraft.server.v1_8_R3.ItemStack getHandle() {
        return this.handle;
    }

    @Inject(method = "setTypeId(I)V", at = @At("RETURN"), remap = false)
    private void onSetTypeId(int typeId, CallbackInfo ci) {
        ((IMixinItemStack) this).setIItemType((ItemType) CraftMagicNumbers.getItem(typeId));
        if (this.handle != null && this.handle.hasTag()) {
            this.handle.getTag().remove(CUSTOM_ITEM_TYPE);
        }
    }

    private static Material getType(net.minecraft.server.v1_8_R3.ItemStack item) {
        final Material material = Material.getMaterial(item == null ? 0 : CraftMagicNumbers.getId(item.getItem()));
        return material == null ? Material.AIR : material;
    }

    @Inject(method = "setItemMeta(Lnet/minecraft/server/v1_8_R3/ItemStack;Lorg/bukkit/inventory/meta/ItemMeta;)Z",
            at = @At("HEAD"), cancellable = true, remap = false)
    private static void onSetItemMeta(net.minecraft.server.v1_8_R3.ItemStack item, ItemMeta itemMeta, CallbackInfoReturnable<Boolean> ci) {
        if (item == null) {
            ci.setReturnValue(false);
            return;
        }
        NBTTagCompound tag = item.getTag();
        if (CraftItemFactory.instance().equals(itemMeta, null)) {
            if (tag.hasKey(CUSTOM_ITEM_TYPE)) {
                NBTTagCompound tag1 = new NBTTagCompound();
                tag1.setString(CUSTOM_ITEM_TYPE, tag.getString(CUSTOM_ITEM_TYPE));
                item.setTag(tag1);
            } else {
                item.setTag(null);
            }
            ci.setReturnValue(true);
            return;
        }
        if (!CraftItemFactory.instance().isApplicable(itemMeta, getType(item))) {
            ci.setReturnValue(false);
            return;
        }
        itemMeta = CraftItemFactory.instance().asMetaFor(itemMeta, getType(item));
        if (itemMeta == null) {
            ci.setReturnValue(true);
            return;
        }

        NBTTagCompound tag1 = new NBTTagCompound();
        ((IMixinItemMeta) itemMeta).applyDataToItem(tag);

        if (tag.hasKey(CUSTOM_ITEM_TYPE)) {
            tag1.setString(CUSTOM_ITEM_TYPE, tag.getString(CUSTOM_ITEM_TYPE));
        } else {
            tag1.remove(CUSTOM_ITEM_TYPE);
        }

        item.setTag(tag1);
        ci.setReturnValue(true);
    }
}
