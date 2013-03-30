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

import java.util.Map.Entry;

import me.cybermaxke.materialapi.enchantment.EnchantmentCustom;
import me.cybermaxke.materialapi.inventory.CustomItemStack;
import me.cybermaxke.materialapi.map.RenderMapsTask;
import me.cybermaxke.materialapi.material.MaterialData;
import me.cybermaxke.materialapi.recipe.RecipeData;
import me.cybermaxke.materialapi.utils.InventoryUtils;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class PlayerListener implements Listener {
	private Plugin plugin;
	private boolean firstLogin = false;

	public PlayerListener(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		if (!this.firstLogin) {
			new RenderMapsTask(this.plugin, 13);
			this.firstLogin = true;
		}
	}
	
	@EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
		if (e.isCancelled()) {
			return;
		}
		
		for (int i = 0; i < e.blockList().size(); i++) {
			Block b = e.blockList().get(i);
			
			if (MaterialData.isCustomBlock(b) && MaterialData.getMaterial(b) != null) {
				e.blockList().remove(b);		
				b.setType(Material.AIR);		
				b.getWorld().dropItem(b.getLocation(), new CustomItemStack(MaterialData.getMaterial(b)));
			}
		}
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
			is.getMaterial().onBlockPlaced(e);
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
				if (!p.getGameMode().equals(GameMode.CREATIVE)) {
					b.setType(Material.AIR);
					b.getWorld().dropItemNaturally(b.getLocation(), new CustomItemStack(MaterialData.getMaterial(b)));
					e.setCancelled(true);
				}
				
				MaterialData.getMaterial(b).onBlockBreak(e);
				MaterialData.setCustomBlockId(e.getBlock(), -1);
			}
		}
	}
	
	@EventHandler
	public void onBlockDamage(BlockDamageEvent e) {
		if (e.isCancelled()) {
			return;
		}
		
		Block b = e.getBlock();
		
		if (MaterialData.isCustomBlock(b)) {
			if (MaterialData.getMaterial(b) != null) {
				MaterialData.getMaterial(b).onBlockDamage(e);
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
		
		Block b = e.getClickedBlock();
		CustomItemStack is = new CustomItemStack(i);
			
		if (is.isCustomItem()) {
			is.getMaterial().onInteract(e);
			
			for (Entry<Enchantment, Integer> en : is.getEnchantments().entrySet()) {
				if (en.getKey() instanceof EnchantmentCustom) {
					((EnchantmentCustom) en.getKey()).onInteract(e, is, en.getValue());
				}
			}
		}
		
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (MaterialData.isCustomBlock(b)) {
				if (MaterialData.getMaterial(b) != null) {
					MaterialData.getMaterial(b).onBlockInteract(e);
				}
			}
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
			
			is.getMaterial().onHit(e);
			
			for (Entry<Enchantment, Integer> en : is.getEnchantments().entrySet()) {
				if (en.getKey() instanceof EnchantmentCustom) {
					((EnchantmentCustom) en.getKey()).onHit(e, is, en.getValue());
				}
			}
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
			is.getMaterial().onInteractEntity(e);
			
			for (Entry<Enchantment, Integer> en : is.getEnchantments().entrySet()) {
				if (en.getKey() instanceof EnchantmentCustom) {
					((EnchantmentCustom) en.getKey()).onInteract(e, is, en.getValue());
				}
			}
		}
	}
	
	@EventHandler
	public void onPrepareItemCraft(PrepareItemCraftEvent e) {
		CraftingInventory inv = e.getInventory();
		inv.setResult(RecipeData.getItem(inv));
	}
	
	@EventHandler
	public void onItemHeld(PlayerItemHeldEvent e) {
		if(e.isCancelled()) {
			return;
		}
		
		Player p = e.getPlayer();
		ItemStack i = p.getInventory().getItem(e.getNewSlot());
		
		if (i == null) {
			return;
		}
		
		CustomItemStack is = new CustomItemStack(i);
		
		if(is.isCustomItem()) {
			is.getMaterial().onHold(e);
		}
	}
}