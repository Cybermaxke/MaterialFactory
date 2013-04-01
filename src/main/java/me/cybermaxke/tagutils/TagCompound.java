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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

public final class TagCompound extends Tag<TagMap> {
	
	public TagCompound(String name, TagMap value) {
		super(name, value);
	}
	
	public TagCompound(String name) {
		super(name, new TagMap());
	}
	
	public TagCompound() {
		this("");
	}

	public void set(String key, Tag<?> tag) {
		this.getValue().put(key, tag);
	}

	public void setString(String key, String value) {
		this.set(key, new TagString(key, value));
	}

	public void setInteger(String key, Integer value) {
		this.set(key, new TagInteger(key, value));
	}

	public void setByte(String key, Byte value) {
		this.set(key, new TagByte(key, value));
	}

	public void setLong(String key, Long value) {
		this.set(key, new TagLong(key, value));
	}

	public void setFloat(String key, Float value) {
		this.set(key, new TagFloat(key, value));
	}

	public void setBoolean(String key, Boolean value) {
		this.set(key, new TagByte(key, (byte) (value ? 1 : 0)));
	}

	public void setList(String key, TagList list) {
		this.set(key, list);
	}

	public void setDouble(String key, Double value) {
		this.set(key, new TagDouble(key, value));
	}

	public void setCompound(String key, TagCompound tag) {
		this.getValue().put(key, tag);
	}

	public void setStringList(String key, List<String> list) {
		TagList l = new TagList();
		for (String s : list) {
			l.getValue().add(new TagString(s));
		}
		this.set(key, l);
	}

	public boolean hasKey(String key) {
		return this.getValue().containsKey(key);
	}

	public Tag<?> get(String key) {
		return (Tag<?>) this.getValue().get(key);
	}

	public <T> T get(Class<T> clazz, String key) {
		return this.hasKey(key) ? clazz.cast(this.get(key)) : null;
	}

	public <T> T getValue(Class<T> clazz, String key) {
		return this.hasKey(key) ? clazz.cast(this.get(key).getValue()) : null;
	}

	public String getString(String key) {
		return this.getValue(String.class, key);
	}

	public Integer getInteger(String key) {
		return this.getValue(Integer.class, key);
	}

	public Byte getByte(String key) {
		return this.getValue(Byte.class, key);
	}

	public Long getLong(String key) {
		return this.getValue(Long.class, key);
	}

	public Float getFloat(String key) {
		return this.getValue(Float.class, key);
	}

	public Boolean getBoolean(String key) {
		return this.hasKey(key) && this.getByte(key) == 1 ? true : false;
	}

	public Double getDouble(String key) {
		return this.getValue(Double.class, key);
	}

	public TagList getList(String key) {
		return this.get(TagList.class, key);
	}

	public Set<Entry<String, Tag<?>>> entrySet() {
		return this.getValue().entrySet();
	}

	public Set<String> keySet() {
		return this.getValue().keySet();
	}

	public Collection<Tag<?>> values() {
		return this.getValue().values();
	}

	public int size() {
		return this.getValue().size();
	}

	public List<String> getStringList(String key) {
		TagList t = this.getList(key);
		if (t == null) {
			return null;
		}
		
		List<String> l = new ArrayList<String>();
		for (Tag<?> tag : t.getValue()) {
			if (tag instanceof TagString) {
				l.add(((TagString) tag).getValue());
			}
		}
		return null;
	}

	public TagCompound getCompound(String key) {
		return this.get(TagCompound.class, key);
	}

	public void remove(String key) {
		if (this.hasKey(key)) {
			this.getValue().remove(key);
		}
	}

	@Override
	public String getTagName() {
		return "TAG_Compound";
	}

	@Override
	public byte getTypeId() {
		return 10;
	}

	@Override
	public TagCompound clone() {
		return new TagCompound(this.getName(), this.getValue());
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof TagCompound)) {
			return false;
		}
		
		TagCompound t = (TagCompound) other;
		for (Entry<String, Tag<?>> s : this.entrySet()) {
			if (!t.hasKey(s.getKey()) || !t.get(s.getKey()).equals(s.getValue())) {
				return false;
			}
		}
		
		return true;
	}
}