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

public abstract class Tag<T> {
	private String name;
	private T value;
	
	protected Tag(String name, T value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return this.name;
	}

	public T getValue() {
		return this.value;
	}

	public abstract String getTagName();

	public abstract byte getTypeId();
	
	public abstract Tag<T> clone();
	
	public abstract boolean equals(Object other);
}