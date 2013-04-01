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

public final class TagLong extends Tag<Long> {

	public TagLong(String name, Long value) {
		super(name, value);
	}

	public TagLong(Long value) {
		super("", value);
	}

	@Override
	public String getTagName() {
		return "TAG_Long";
	}

	@Override
	public byte getTypeId() {
		return 4;
	}

	@Override
	public TagLong clone() {
		return new TagLong(this.getName(), this.getValue());
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof TagLong && ((TagLong) other).getValue() == this.getValue();
	}
}