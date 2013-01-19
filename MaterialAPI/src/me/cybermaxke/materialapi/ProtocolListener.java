package me.cybermaxke.materialapi;

import java.util.List;
import java.util.logging.Level;

import me.cybermaxke.materialapi.enchantment.EnchantmentInstance;
import me.cybermaxke.materialapi.inventory.CustomItemStack;
import me.cybermaxke.materialapi.material.MaterialData;
import me.cybermaxke.materialapi.utils.Classes;
import me.cybermaxke.materialapi.utils.ReflectionUtils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;

public class ProtocolListener extends PacketAdapter {

	public ProtocolListener(Plugin plugin) {
		super(plugin, ConnectionSide.SERVER_SIDE, 0x67, 0x68);
		ProtocolLibrary.getProtocolManager().addPacketListener(this);
	}

	@Override
	public void onPacketSending(PacketEvent e) {
		PacketContainer packet = e.getPacket();

		try {
			switch (e.getPacketID()) {
				case 0x67: {
					this.removeHiddenEnchantments(packet.getItemModifier().read(0));
					this.removeCustomId(packet.getItemModifier().read(0));
					break;
				}

				case 0x68: {
					ItemStack[] elements = packet.getItemArrayModifier().read(0);

					for (int i = 0; i < elements.length; i++) {
						if (elements[i] != null) {
							this.removeHiddenEnchantments(elements[i]);
							this.removeCustomId(elements[i]);
						}
					}
					
					break;
				}
			}
		} catch (FieldAccessException ex) {
			this.getPlugin().getLogger().log(Level.SEVERE, "Couldn't access field.", ex);
		}
	}
	
	private void removeCustomId(ItemStack item) {
		if (item == null) {
			return;
		}

		ItemMeta m = item.getItemMeta();
		List<String> lore = m.getLore();
		
		if (m.hasLore()) {
			for (int i = 0; i < lore.size(); i++) {
				String t = lore.get(i);
				
				if (t.contains(MaterialData.DATA_PREFIX)) {
					lore.remove(t);
				}
			}
		}
		
		if (item.getEnchantments().isEmpty()) {
			ReflectionUtils.setFieldObject(Classes.CB_CRAFT_METAITEM, "enchantments", m, null);
		}
		
		m.setLore(lore);
		item.setItemMeta(m);
	}
	
	private void removeHiddenEnchantments(ItemStack item) {
		if (item == null) {
			return;
		}
		
		CustomItemStack ci = new CustomItemStack(item);
		
		if (!ci.isCustomItem() || item.getEnchantments().isEmpty()) {
			return;
		}
		
		for (EnchantmentInstance e : ci.getMaterial().getEnchantments()) {
			if (item.getEnchantments().containsKey(e.getEnchantment()) && !e.isVisible()) {
				item.removeEnchantment(e.getEnchantment());
			}
		}
	}
}