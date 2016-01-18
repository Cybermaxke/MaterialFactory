package me.cybermaxke.materialfactory.theplugin;

import java.util.logging.Level;

import me.cybermaxke.materialfactory.api.ExtendedItemStack;
import me.cybermaxke.materialfactory.api.ItemData;
import me.cybermaxke.materialfactory.api.data.DataQuery;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class will be cloned to a plugin jar at
 * runtime. Don't move or rename this.
 */
public final class MaterialFactoryPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
    	// Testing whether the class are injected correctly
        ExtendedItemStack itemStack = (ExtendedItemStack) new ItemStack(Material.APPLE);
        ItemData itemData = itemStack.getItemMeta();
        itemData.getDataContainer().set(DataQuery.of("test"), "Hehe :p");
        itemStack.setItemMeta(itemData);
        itemStack.setAmount(10);

        this.getLogger().log(Level.INFO, "Successfully loaded!");
    }

}
