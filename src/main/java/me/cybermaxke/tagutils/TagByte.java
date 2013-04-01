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

public final class TagByte extends Tag<Byte> {

	public TagByte(String name, Byte value) {
		super(name, value);
	}

	public TagByte(Byte value) {
		super("", value);
	}

	@Override
	public String getTagName() {
		return "TAG_Byte";
	}

	@Override
	public byte getTypeId() {
		return 1;
	}

	@Override
	public TagByte clone() {
		return new TagByte(this.getName(), this.getValue());
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof TagByte && ((TagByte) other).getValue() == this.getValue();
	}
}