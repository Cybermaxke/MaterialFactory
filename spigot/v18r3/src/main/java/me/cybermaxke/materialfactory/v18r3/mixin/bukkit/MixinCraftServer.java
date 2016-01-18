package me.cybermaxke.materialfactory.v18r3.mixin.bukkit;

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
        System.out.println("DEBUG!");
    }
}
