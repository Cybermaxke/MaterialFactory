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

public final class TagInteger extends Tag<Integer> {

	public TagInteger(String name, Integer value) {
		super(name, value);
	}

	public TagInteger(Integer value) {
		super("", value);
	}

	@Override
	public String getTagName() {
		return "TAG_Int";
	}

	@Override
	public byte getTypeId() {
		return 3;
	}

	@Override
	public TagInteger clone() {
		return new TagInteger(this.getName(), this.getValue());
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof TagInteger && ((TagInteger) other).getValue() == this.getValue();
	}
}