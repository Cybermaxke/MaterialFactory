package me.cybermaxke.materialfactory.v18r3;

import java.io.File;
import java.lang.reflect.Field;

import me.cybermaxke.materialfactory.theplugin.MaterialFactoryPluginInjector;
import net.minecraft.server.v1_8_R3.MinecraftServer;

public final class SMaterialFactory {

    static Field unhandledTagsField;

    public static void onLoadPlugins() {
        final File pluginsFolder = (File) MinecraftServer.getServer().options.valueOf("plugins");
        MaterialFactoryPluginInjector.inject(pluginsFolder);
    }
}
