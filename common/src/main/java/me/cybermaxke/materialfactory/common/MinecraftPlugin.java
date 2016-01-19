package me.cybermaxke.materialfactory.common;

import com.avaje.ebean.EbeanServer;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class MinecraftPlugin implements Plugin {

    public static final MinecraftPlugin INSTANCE = new MinecraftPlugin();

    private final File dataFolder = new File("");
    private final PluginDescriptionFile descriptionFile = new PluginDescriptionFile(
            "Minecraft", Bukkit.getVersion(), "me.cybermaxke.materialfactory.common.MinecraftPlugin");
    private final FileConfiguration pluginConfig = new YamlConfiguration();

    @Override
    public File getDataFolder() {
        return this.dataFolder;
    }

    @Override
    public PluginDescriptionFile getDescription() {
        return this.descriptionFile;
    }

    @Override
    public FileConfiguration getConfig() {
        return this.pluginConfig;
    }

    @Override
    public InputStream getResource(String filename) {
        return MinecraftPlugin.class.getResourceAsStream(filename.startsWith("/") ? filename : "/" + filename);
    }

    @Override
    public void saveConfig() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveDefaultConfig() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveResource(String resourcePath, boolean replace) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reloadConfig() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PluginLoader getPluginLoader() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Server getServer() {
        return Bukkit.getServer();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onEnable() {
    }

    @Override
    public boolean isNaggable() {
        return false;
    }

    @Override
    public void setNaggable(boolean canNag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EbeanServer getDatabase() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Logger getLogger() {
        return Logger.getLogger("Minecraft");
    }

    @Override
    public String getName() {
        return "Minecraft";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
