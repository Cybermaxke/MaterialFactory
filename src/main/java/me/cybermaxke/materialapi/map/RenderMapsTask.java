package me.cybermaxke.materialapi.map;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class RenderMapsTask extends BukkitRunnable {
	
	public RenderMapsTask(Plugin plugin, int delay) {
		this.runTaskLater(plugin, delay);
	}

	@Override
	public void run() {
		CustomMap[] maps = MapData.getMaps();
		
		if (maps != null) {
			for (CustomMap m : maps) {
				m.renderImage();
			}
		}
	}
}