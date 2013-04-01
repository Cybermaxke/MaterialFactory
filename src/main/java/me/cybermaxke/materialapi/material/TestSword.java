package me.cybermaxke.materialapi.material;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TestSword extends CustomMaterial {

	public TestSword(String id) {
		super(id, Material.DIAMOND_SWORD);
		this.addEnchantment(Enchantment.KNOCKBACK, 2);
		this.addEnchantment(Enchantment.DAMAGE_ALL, 5);
		this.setName("Master Sword");
	}

	@Override
	public void onHit(EntityDamageByEntityEvent e) {
		((Player) e.getDamager()).sendMessage("Debug");

		if (e.getEntity() instanceof LivingEntity) {
			LivingEntity ent = (LivingEntity) e.getEntity();
			ent.setFireTicks(40);
			ent.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 5));
		}
	}

	@Override
	public void onInteract(PlayerInteractEvent event) {

	}

	@Override
	public void onInteractEntity(PlayerInteractEntityEvent event) {

	}

	@Override
	public void onBlockPlaced(BlockPlaceEvent event) {

	}

	@Override
	public void onBlockBreak(BlockBreakEvent event) {

	}

	@Override
	public void onBlockDamage(BlockDamageEvent event) {

	}

	@Override
	public void onBlockInteract(PlayerInteractEvent event) {

	}

	@Override
	public void onHold(PlayerItemHeldEvent event) {

	}
}