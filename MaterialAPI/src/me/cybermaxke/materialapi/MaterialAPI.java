package me.cybermaxke.materialapi;

import java.util.logging.Level;

import me.cybermaxke.materialapi.material.MaterialData;
import me.cybermaxke.materialapi.recipe.RecipeData;
import me.cybermaxke.materialapi.utils.Classes;

import org.bukkit.plugin.java.JavaPlugin;

public class MaterialAPI extends JavaPlugin {
	private String version = "v1.4.6";

	@Override
	public void onEnable() {
		String pack = this.getServer().getClass().getPackage().getName();
   		this.version = pack.substring(pack.lastIndexOf('.') + 1).replace("_", ".");
   		
		new PlayerListener(this);
		new Classes(this);
		new MaterialData(this);
		new RecipeData();

		if (this.getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
			new ProtocolListener(this);
			this.getLogger().log(Level.INFO, "Hooked into ProtocolLib!");
		}
	}
	
	@Override
	public void onDisable() {
		MaterialData.save();
	}
	
	public String getCraftbukkitPackage() {
		return "org.bukkit.craftbukkit." + (this.version.replace(".", "_"));
	}
	
	public String getNMSPackage() {
		return "net.minecraft.server." + (this.version.replace(".", "_"));
	}
}