package me.cybermaxke.materialapi.inventory;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.cybermaxke.materialapi.enchantment.EnchantmentInstance;
import me.cybermaxke.materialapi.material.CustomMaterial;
import me.cybermaxke.materialapi.material.MaterialData;
import me.cybermaxke.materialapi.utils.Classes;
import me.cybermaxke.materialapi.utils.InventoryUtils;
import me.cybermaxke.materialapi.utils.ReflectionUtils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class CustomItemStack extends ItemStack {
	private CustomMaterial material = null;
	
	public CustomItemStack(Material mat, int amount, short damage) {
		super(mat, amount, damage);
	}
	
	public CustomItemStack(Material mat, int amount) {
		super(mat, amount);
	}
	
	public CustomItemStack(Material mat) {
		super(mat);
	}
	
	public CustomItemStack(Object itemstack) {
		this((ItemStack) (ReflectionUtils.getMethodObject(Classes.CB_CRAFT_ITEMSTACK, "asBukkitCopy", new Class[] { Classes.NMS_ITEMSTACK }, null, new Object[] { itemstack })));
	}

	public CustomItemStack(ItemStack itemstack) {
		super(itemstack.getTypeId(), itemstack.getAmount(), itemstack.getDurability());
		this.setItemMeta(itemstack.getItemMeta());

		if (MaterialData.isCustomItem(itemstack)) {
			this.material = MaterialData.getMaterialByCustomId(MaterialData.getCustomId(itemstack));
		}
	}

	public CustomItemStack(CustomMaterial material) {
		super(material.getMinecraftId(), 1);
		this.material = material;
				
		if (material.getData() != -1) {
			this.setDurability(Short.valueOf(material.getData() + ""));
		}
		
		this.setName(material.getName());
		this.setLore(material.getLore());
		
		if (material.getEnchantments() != null) {
			for (EnchantmentInstance e : material.getEnchantments()) {
				this.addUnsafeEnchantment(e.getEnchantment(), e.getLvl());
			}
		}
		
		if (material.getColor() != null) {
			this.setColor(material.getColor());
		}
	}

	public CustomMaterial getMaterial() {
		return this.material;
	}

	public boolean isCustomItem() {
		return this.material != null;
	}
	
	public void setName(String name) {
		ItemMeta m = this.getItemMeta();
		m.setDisplayName(name != null ? name : this.getName());
		this.setItemMeta(m);
	}

	public String getName() {
		return this.getItemMeta().getDisplayName();
	}

	public void setLore(String... lore) {
		ItemMeta m = this.getItemMeta();
		List<String> l = m.getLore();
		
		if (this.isCustomItem()) {
			if (l == null) {
				l = new ArrayList<String>();
			}
			
			l.add(MaterialData.DATA_PREFIX + this.material.getCustomId());
		}
		
		if (lore != null) {
			l.addAll(Arrays.asList(lore));
		}
		
		m.setLore(l);
		this.setItemMeta(m);
	}

	public void addLore(String... lore) {
		ItemMeta m = this.getItemMeta();
		List<String> l = m.hasLore() ? m.getLore() : new ArrayList<String>();
		l.addAll(Arrays.asList(lore));
		m.setLore(l);
		this.setItemMeta(m);
	}

	public String[] getLore() {
		ItemMeta m = this.getItemMeta();
		return !m.hasLore() ? null : m.getLore().toArray(new String[] {});
	}
	
	@Override
	public boolean isSimilar(ItemStack itemstack) {
		return super.isSimilar(itemstack) && InventoryUtils.doItemsMatch(this, new CustomItemStack(itemstack));
	}
	
	public void setSkullOwner(String name) {
		if (name == null) {
			name = "";
		}
		
		ItemMeta m = this.getItemMeta();
		
		if (m instanceof SkullMeta) {
			((SkullMeta) m).setOwner(name);
			this.setItemMeta(m);
		}
	}
	
	public String getSkullOwner() {
		ItemMeta m = this.getItemMeta();
		
		if (m instanceof SkullMeta) {
			return ((SkullMeta) m).getOwner().length() > 0 ? ((SkullMeta) m).getOwner() : null;
		}
		
		return null;
	}
	
	public void setBukkitColor(org.bukkit.Color color) {
		ItemMeta m = this.getItemMeta();
		
		if (m instanceof LeatherArmorMeta) {
			((LeatherArmorMeta) m).setColor(color);
			this.setItemMeta(m);
		}
	}
	
	public void setColor(Color color) {
		org.bukkit.Color c = org.bukkit.Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue());
		this.setBukkitColor(c);
	}
	
	public org.bukkit.Color getBukkitColor() {
		ItemMeta m = this.getItemMeta();
		
		if (m instanceof LeatherArmorMeta) {		
			return ((LeatherArmorMeta) m).getColor();
		}
    	
    	return null;
	}
	
	public Color getColor() {	
    	return this.getBukkitColor() == null ? null : new Color(this.getBukkitColor().asRGB());
	}
}