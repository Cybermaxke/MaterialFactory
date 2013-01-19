package me.cybermaxke.materialapi;

import me.cybermaxke.materialapi.inventory.CustomItemStack;
import me.cybermaxke.materialapi.recipe.RecipeData;
import me.cybermaxke.materialapi.utils.InventoryUtils;

import org.bukkit.block.Furnace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class PlayerListener implements Listener {

	public PlayerListener(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onFurnaceSmelt(FurnaceSmeltEvent e) {
		FurnaceInventory inv = ((Furnace) e.getBlock().getState()).getInventory();
		CustomItemStack r = RecipeData.getFurnaceItem(new CustomItemStack(e.getSource()));
		CustomItemStack i = inv.getResult() == null ? null : new CustomItemStack(inv.getResult());
		
		if (i != null && !InventoryUtils.doItemsMatch(r, i)) {
			e.setCancelled(true);
			return;
		}
		
		e.setResult(r);
	}
	
	@EventHandler
	public void onFurnaceBurn(FurnaceBurnEvent e) {
		FurnaceInventory inv = ((Furnace) e.getBlock().getState()).getInventory();
		CustomItemStack r = inv.getSmelting() == null ? null : RecipeData.getFurnaceItem(new CustomItemStack(inv.getSmelting()));
		CustomItemStack i = inv.getResult() == null ? null : new CustomItemStack(inv.getResult());
		CustomItemStack f = e.getFuel() == null ? null : new CustomItemStack(e.getFuel());
		
		if (f == null || f.isCustomItem()) {
			e.setCancelled(true);
			return;
		}
		
		if (i != null && !InventoryUtils.doItemsMatch(r, i)) {
			e.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		ItemStack i = p.getItemInHand();
		
		if (i == null) {
			return;
		}
		
		CustomItemStack is = new CustomItemStack(i);
			
		if (is.isCustomItem()) {
			is.getMaterial().onInteract(p, e.getAction(), e.getClickedBlock(), e.getBlockFace());
		}
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if (e.isCancelled()) {
			return;
		}
		
		if (!(e.getDamager() instanceof Player)) {
			return;
		}
		
		if (!(e.getEntity() instanceof LivingEntity)) {
			return;
		}
		
		Player p = (Player) e.getDamager();
		ItemStack i = p.getItemInHand();
		
		if (i == null) {
			return;
		}
		
		CustomItemStack is = new CustomItemStack(i);
		
		if (is.isCustomItem()) {
			if (is.getMaterial().getDamage() != -1) {
				e.setDamage(is.getMaterial().getDamage());
			}
			
			is.getMaterial().onHit(p, (LivingEntity) e.getEntity());
		}
	}
	
	@EventHandler
	public void onInteractEntity(PlayerInteractEntityEvent e) {
		if (!(e.getRightClicked() instanceof LivingEntity)) {
			return;
		}
		
		Player p = e.getPlayer();
		ItemStack i = p.getItemInHand();
		
		if (i == null) {
			return;
		}
		
		CustomItemStack is = new CustomItemStack(i);
		
		if (is.isCustomItem()) {
			is.getMaterial().onInteractEntity(p, (LivingEntity) e.getRightClicked());
		}
	}
	
	@EventHandler
	public void onPrepareItemCraft(PrepareItemCraftEvent e) {
		CraftingInventory inv = e.getInventory();
		inv.setResult(RecipeData.getItem(inv));
	}
}