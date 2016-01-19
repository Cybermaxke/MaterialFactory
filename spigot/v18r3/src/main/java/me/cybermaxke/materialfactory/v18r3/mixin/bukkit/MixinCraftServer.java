package me.cybermaxke.materialfactory.v18r3.mixin.bukkit;

import me.cybermaxke.materialfactory.api.item.ItemTypes;
import me.cybermaxke.materialfactory.api.item.VanillaItemType;
import me.cybermaxke.materialfactory.common.MinecraftPlugin;
import me.cybermaxke.materialfactory.common.SimpleItemRegistry;
import me.cybermaxke.materialfactory.common.inventory.SimpleItemFactory;
import me.cybermaxke.materialfactory.v18r3.SItemRegistry;
import net.minecraft.server.v1_8_R3.Item;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftServer.class)
public abstract class MixinCraftServer implements Server {

    @Inject(method = "loadPlugins()V", at = @At("HEAD"), remap = false)
    public void onLoadPlugins(CallbackInfo ci) {
        final SItemRegistry registry = new SItemRegistry();
        SimpleItemRegistry.setInstance(registry);
        SimpleItemFactory.setInstance(new SimpleItemFactory());
        // Register all the vanilla item types
        for (Item item : Item.REGISTRY) {
            registry.register(MinecraftPlugin.INSTANCE, Item.REGISTRY.c(item).a(), (VanillaItemType) item);
        }
        registry.register(MinecraftPlugin.INSTANCE, "unknown", ItemTypes.UNKNOWN);
    }
}
