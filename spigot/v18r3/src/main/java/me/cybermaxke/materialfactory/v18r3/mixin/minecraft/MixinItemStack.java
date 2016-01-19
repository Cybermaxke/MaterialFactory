package me.cybermaxke.materialfactory.v18r3.mixin.minecraft;

import static me.cybermaxke.materialfactory.common.ItemFactoryConstants.CUSTOM_ITEM_TYPE;

import me.cybermaxke.materialfactory.api.item.ItemRegistry;
import me.cybermaxke.materialfactory.api.item.ItemType;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ItemStack.class)
public class MixinItemStack {

    @Shadow(remap = false) private NBTTagCompound tag;

    @Inject(method = "getMaxStackSize", at = @At("HEAD"), cancellable = true, remap = false)
    private void onGetMaxStackSize(CallbackInfoReturnable<Integer> ci) {
        if (this.tag != null && this.tag.hasKey(CUSTOM_ITEM_TYPE)) {
            Optional<ItemType> optItemType = ItemRegistry.get().get(this.tag.getString(CUSTOM_ITEM_TYPE));
            if (optItemType.isPresent()) {
                ci.setReturnValue(optItemType.get().getMaxStackSize());
            }
        }
    }
}
