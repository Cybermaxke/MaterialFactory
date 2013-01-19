package me.cybermaxke.materialapi.material;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.cybermaxke.materialapi.enchantment.EnchantmentInstance;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public class CustomMaterial {
	private List<EnchantmentInstance> enchantments = new ArrayList<EnchantmentInstance>();
	private List<String> lore = new ArrayList<String>();
	
	private String name = null;
	private String id = null;
	private String skullOwner = null;
	
	private Color color = null;
	
	private int minecraftId = 0;
	private int customId = 1000;
	private int damage = -1;
	private byte data = -1;

	public CustomMaterial(String id, int minecraftId, byte data) {
		this.id = id.toLowerCase();
		this.minecraftId = minecraftId;
		this.data = data;
		this.customId = MaterialData.addMaterialData(this);
		MaterialData.addMaterial(this);
	}
	
	public CustomMaterial(String id, int minecraftId) {
		this(id, minecraftId, (byte) -1);
	}
	
	public CustomMaterial(String id, Material material, byte data) {
		this(id, material.getId(), data);
	}
	
	public CustomMaterial(String id, Material material) {
		this(id, material.getId());
	}
	
	public String getName() {
		return this.name;
	}
	
	public CustomMaterial setName(String name) {
		this.name = ChatColor.WHITE + name;
		return this;
	}
	
	public int getMinecraftId() {
		return this.minecraftId;
	}
	
	public CustomMaterial setMinecraftId(int id) {
		this.minecraftId = id;
		return this;
	}
	
	public String getId() {
		return this.id;
	}
	
	public int getCustomId() {
		return this.customId;
	}
	
	public byte getData() {
		return this.data;
	}
	
	public CustomMaterial setData(byte data) {
		this.data = data;
		return this;
	}
	
	public int getDamage() {
		return this.damage;
	}
	
	public CustomMaterial setDamage(int damage) {
		this.damage = damage;
		return this;
	}
	
	public String[] getLore() {
		return this.lore.isEmpty() ? null : this.lore.toArray(new String[] {});
	}
	
	public CustomMaterial setColor(Color color) {
		this.color = color;
		return this;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public CustomMaterial setSkullOwner(String name) {
		this.skullOwner = name;
		return this;
	}
	
	public String getSkullOwner() {
		return this.skullOwner;
	}
	
	public CustomMaterial addEnchantment(Enchantment enchantment, int lvl, boolean visible) {
		this.enchantments.add(new EnchantmentInstance(enchantment, lvl, visible));
		return this;
	}
	
	public CustomMaterial addEnchantment(Enchantment enchantment, int lvl) {
		return this.addEnchantment(enchantment, lvl, true);
	}
	
	public EnchantmentInstance[] getEnchantments() {
		return this.enchantments.isEmpty() ? null : this.enchantments.toArray(new EnchantmentInstance[] {});
	}
	
	public CustomMaterial setLore(String... lore) {
		this.lore = new ArrayList<String>();
		
		if (lore == null) {
			return this;
		}
		
		for (int i = 0; i < lore.length; i++) {
			lore[i] = ChatColor.GRAY + lore[i];
		}
		
		this.lore.addAll(Arrays.asList(lore));
		return this;
	}
	
	public CustomMaterial addLore(String... lore) {
		for (int i = 0; i < lore.length; i++) {
			lore[i] = ChatColor.GRAY + lore[i];
		}
		
		this.lore.addAll(Arrays.asList(lore));
		return this;
	}
	
	public void onHit(Player player, LivingEntity entity) {
		
	}
	
	public void onInteract(Player player, Action action, Block block, BlockFace face) {
		
	}
	
	public void onInteractEntity(Player player, LivingEntity entity) {
		
	}
}