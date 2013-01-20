/**
 * 
 * This software is part of the MaterialAPI
 * 
 * This api allows plugin developers to create on a easy way custom
 * items with a custom id and recipes depending on them.
 * 
 * MaterialAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * any later version.
 *  
 * MaterialAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MaterialAPI. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package me.cybermaxke.materialapi;

import me.cybermaxke.materialapi.inventory.CustomItemStack;
import me.cybermaxke.materialapi.material.MaterialData;
import me.cybermaxke.materialapi.recipe.RecipeData;
import me.cybermaxke.materialapi.utils.InventoryUtils;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
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
	public void onBlockPlace(BlockPlaceEvent e) {
		if (e.isCancelled()) {
			return;
		}
		
		Block b = e.getBlockPlaced();
		ItemStack i = e.getItemInHand();		
		if (i == null) {
			return;
		}
		
		CustomItemStack is = new CustomItemStack(i);		
		if (is.isCustomItem()) {
			if (!is.getMaterial().canPlace(b.getLocation())) {
				e.setCancelled(true);
				return;
			}
			
			MaterialData.setCustomBlockId(b, is.getMaterial().getCustomId());
			is.getMaterial().onBlockPlaced(e.getPlayer(), b);
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (e.isCancelled()) {
			return;
		}
		
		Player p = e.getPlayer();
		Block b = e.getBlock();
		
		if (MaterialData.isCustomBlock(b)) {
			if (MaterialData.getMaterial(b) != null) {
				MaterialData.getMaterial(b).onBlockBreak(p, b);
				
				b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getTypeId());
				b.setType(Material.AIR);
				b.getWorld().dropItemNaturally(b.getLocation(), new CustomItemStack(MaterialData.getMaterial(b)));
				e.setCancelled(true);
			}
		}
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