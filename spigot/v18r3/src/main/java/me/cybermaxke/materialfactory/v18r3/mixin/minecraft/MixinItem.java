package me.cybermaxke.materialfactory.v18r3.mixin.minecraft;

import static com.google.common.base.Preconditions.checkNotNull;

import me.cybermaxke.materialfactory.api.inventory.ExtendedItemStack;
import me.cybermaxke.materialfactory.api.item.ItemActionHandler;
import me.cybermaxke.materialfactory.api.item.VanillaItemType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.ItemStack;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Item.class)
public abstract class MixinItem implements VanillaItemType, ItemActionHandler {

    @Shadow(remap = false) public abstract String shadow$getName();
    @Shadow(remap = false) public abstract String e_(ItemStack itemStack);
    @Shadow(remap = false) public abstract boolean a(ItemStack itemStack, EntityHuman entityHuman, EntityLiving entityLiving);

    private Optional<ItemActionHandler> itemActionHandler;

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void onInit(CallbackInfo ci) {
        this.itemActionHandler = Optional.of(this);
    }

    @Override
    public BaseComponent getName() {
        return new TranslatableComponent(this.shadow$getName());
    }

    @Override
    public BaseComponent getNameFor(ExtendedItemStack itemStack) {
        return new TranslatableComponent(this.e_(CraftItemStack.asNMSCopy(checkNotNull((org.bukkit.inventory.ItemStack) itemStack, "itemStack"))));
    }

    @Override
    public Optional<ItemActionHandler> getActionHandler() {
        return this.itemActionHandler;
    }

    @Override
    public boolean onInteractWithEntity(ExtendedItemStack itemStack, Player player, LivingEntity livingEntity) {
        checkNotNull(livingEntity, "livingEntity");
        checkNotNull(itemStack, "itemStack");
        checkNotNull(player, "player");
        return this.a(CraftItemStack.asNMSCopy((org.bukkit.inventory.ItemStack) itemStack), ((CraftPlayer) player).getHandle(),
                ((CraftLivingEntity) livingEntity).getHandle());
    }

    @Override
    public Material toMaterial() {
        return CraftMagicNumbers.getMaterial((Item) ((Object) this));
    }
}
