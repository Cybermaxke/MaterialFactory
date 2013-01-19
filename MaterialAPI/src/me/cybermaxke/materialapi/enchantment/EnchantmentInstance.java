package me.cybermaxke.materialapi.enchantment;

import org.bukkit.enchantments.Enchantment;

public class EnchantmentInstance {
	private Enchantment enchantment;
	private boolean visible;
	private int lvl;

	public EnchantmentInstance(Enchantment enchantment, int lvl, boolean visible) {
		this.enchantment = enchantment;
		this.visible = visible;
		this.lvl = lvl;
	}
	
	public Enchantment getEnchantment() {
		return this.enchantment;
	}
	
	public boolean isVisible() {
		return this.visible;
	}
	
	public int getLvl() {
		return this.lvl;
	}
}