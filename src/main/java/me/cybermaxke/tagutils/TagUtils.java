/**
 * 
 * This software is part of the TagUtils
 * 
 * TagUtils is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * any later version.
 *  
 * TagUtils is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TagUtils. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package me.cybermaxke.tagutils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.server.v1_5_R3.*;

import org.bukkit.craftbukkit.v1_5_R3.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TagUtils {

	@SuppressWarnings("unchecked")
	private static Tag<?> createTag(NBTBase tag) {
		byte d = tag.getTypeId();
		
		switch (d) {
			case 0:
				return new TagEnd();
			case 1:
				return new TagByte(tag.getName(), ((NBTTagByte) tag).data);
			case 2:
				return new TagShort(tag.getName(), ((NBTTagShort) tag).data);
			case 3:
				return new TagInteger(tag.getName(), ((NBTTagInt) tag).data);
			case 4:
				return new TagLong(tag.getName(), ((NBTTagLong) tag).data);
			case 5:
				return new TagFloat(tag.getName(), ((NBTTagFloat) tag).data);
			case 6:
				return new TagDouble(tag.getName(), ((NBTTagDouble) tag).data);
			case 7:
				return new TagByteArray(tag.getName(), ((NBTTagByteArray) tag).data);
			case 8:
				return new TagString(tag.getName(), ((NBTTagString) tag).data);
			case 9:
				List<Tag<?>> tags = new ArrayList<Tag<?>>();

				try {
					Field f = NBTTagList.class.getDeclaredField("list");
					f.setAccessible(true);
					List<NBTBase> list = (List<NBTBase>) f.get(tag);
					for (NBTBase b : list) {
						tags.add(createTag(b));
					}
				} catch (Exception e1) {}

				return new TagList(tag.getName(), tags);
			case 10:
				TagCompound c = new TagCompound(tag.getName());

				try {
					Field f2 = NBTTagCompound.class.getDeclaredField("map");
					f2.setAccessible(true);
					Map<String, NBTBase> map = (Map<String, NBTBase>) f2.get(tag);

					for (Entry<String, NBTBase> en : map.entrySet()) {
						c.set(en.getKey(), createTag(en.getValue()));
					}
				} catch (Exception e) {}

				return c;
			case 11:
				return new TagIntegerArray(((NBTTagIntArray) tag).data);
			default:
				return null;
		}
	}

	private static NBTBase createTag(Tag<?> tag) {
		byte d = tag.getTypeId();

		switch (d) {
			case 0:
				return new NBTTagEnd();
			case 1:
				return new NBTTagByte(tag.getName(), (Byte) tag.getValue());
			case 2:
				return new NBTTagShort(tag.getName(), (Short) tag.getValue());
			case 3:
				return new NBTTagInt(tag.getName(), (Integer) tag.getValue());
			case 4:
				return new NBTTagLong(tag.getName(), (Long) tag.getValue());
			case 5:
				return new NBTTagFloat(tag.getName(), (Float) tag.getValue());
			case 6:
				return new NBTTagDouble(tag.getName(), (Double) tag.getValue());
			case 7:
				return new NBTTagByteArray(tag.getName(), (byte[]) tag.getValue());
			case 8:
				return new NBTTagString(tag.getName(), (String) tag.getValue());
			case 9:
				NBTTagList l = new NBTTagList(tag.getName());
				for (Tag<?> t : ((TagList) tag).getValue()) {
					l.add(createTag((Tag<?>) t));
				}
				return l;
			case 10:
				NBTTagCompound c = new NBTTagCompound(tag.getName());			
				for (Entry<String, Tag<?>> en : ((TagCompound) tag).entrySet()) {
					c.set(en.getKey(), createTag((Tag<?>) en.getValue()));
				}
				return c;
			case 11:
				return new NBTTagIntArray(tag.getName(), (int[]) tag.getValue());
			default:
				return null;
		}
	}

	public static boolean save(File target, Tag<?> tag) {
		if (target.isDirectory()) {
			return false;
		}

		if (!target.exists()) {
			try {
				target.createNewFile();
			} catch (Exception e) {
				return false;
			}
		}

		try {
			FileOutputStream fos = new FileOutputStream(target);
			DataOutputStream dos = new DataOutputStream(fos);
			NBTBase.a(createTag(tag), dos);
			dos.close();
			fos.close();
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public static Tag<?> load(File target) {
		if (!target.exists() || target.isDirectory()) {
			return null;
		}

		try {		
			FileInputStream fis = new FileInputStream(target);
			DataInputStream dis = new DataInputStream(fis);
			NBTBase t = NBTBase.b(dis);
			dis.close();
			fis.close();
			return createTag(t);
		} catch (Exception e) {}

		return null;
	}

	public static <T extends Tag<?>> T load(Class<T> clazz, File target) {
		return clazz.cast(load(target));
	}

	public static TagCompound getItemStackAsTag(ItemStack itemstack) {
		net.minecraft.server.v1_5_R3.ItemStack is = CraftItemStack.asNMSCopy(itemstack);
		NBTTagCompound c = is.save(new NBTTagCompound());
		return (TagCompound) createTag(c);
	}

	public static ItemStack getItemStackFromTag(TagCompound tag) {
		NBTTagCompound c = (NBTTagCompound) createTag(tag);
		return CraftItemStack.asCraftMirror(net.minecraft.server.v1_5_R3.ItemStack.createStack(c));
	}

	public static TagCompound getInventoryAsTag(Inventory inventory) {
		TagCompound t = new TagCompound();
		for (int i = 0; i < inventory.getSize(); i++) {
			ItemStack is = inventory.getItem(i);
			if (is != null) {
				t.setCompound("Slot" + i, getItemStackAsTag(is));
			}
		}
		return t;
	}

	public static Inventory getInventoryFromTag(Inventory inventory, TagCompound tag) {
		for (int i = 0; i < inventory.getSize(); i++) {
			if (tag.hasKey("Slot" + i)) {
				inventory.setItem(i, getItemStackFromTag(tag.getCompound("Slot" + i)));
			}
		}
		return inventory;
	}

	public static TagCompound getTag(ItemStack itemstack) {
		net.minecraft.server.v1_5_R3.ItemStack is = CraftItemStack.asNMSCopy(itemstack);
		if (is == null) {
			return null;
		}
		NBTTagCompound t = is.tag;
		return (TagCompound) (t == null ? null : createTag(t));
	}

	public static ItemStack setTag(ItemStack itemstack, TagCompound tag) {
		net.minecraft.server.v1_5_R3.ItemStack is = CraftItemStack.asNMSCopy(itemstack);
		if (is == null) {
			return itemstack;
		}
		is.tag = (NBTTagCompound) (tag == null ? null : createTag(tag));
		return CraftItemStack.asCraftMirror(is);
	}

	public static void saveInventory(Inventory inventory, File target) {
		save(target, getInventoryAsTag(inventory));
	}

	public static Inventory loadInventory(Inventory inventory, File target) {
		return getInventoryFromTag(inventory, load(TagCompound.class, target));
	}
}