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

public final class TagFloat extends Tag<Float> {

	public TagFloat(String name, Float value) {
		super(name, value);
	}

	public TagFloat(Float value) {
		super("", value);
	}

	@Override
	public String getTagName() {
		return "TAG_Float";
	}

	@Override
	public byte getTypeId() {
		return 5;
	}

	@Override
	public TagFloat clone() {
		return new TagFloat(this.getName(), this.getValue());
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof TagFloat && ((TagFloat) other).getValue() == this.getValue();
	}
}