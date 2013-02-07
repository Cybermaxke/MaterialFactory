/**
 * 
 * This software is part of the MaterialAPI
 * 
 * This api allows plugin developers to create on a easy way custom
 * items with a custom id and recipes depending on them.
 * 
 * MaterialAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * any later version.
 *  
 * MaterialAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MaterialAPI. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package me.cybermaxke.materialapi.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils {

	public static Object getFieldObject(Class<?> clazz, String field, Object object) {	
		try {
			Field f = clazz.getDeclaredField(field);
			f.setAccessible(true);
			return f.get(object);
		} catch (Exception ex) {} 
		
		return null;
	}
	
	public static void setFieldObject(Class<?> clazz, String field, Object object, Object newObj) {	
		try {
			Field f = clazz.getDeclaredField(field);
			f.setAccessible(true);
			f.set(object, newObj);
		} catch (Exception ex) {}
	}
	
	public static void invokeMethod(Class<?> clazz, String method, Class<?>[] args, Object object, Object[] objects) {
		try {
			Method m = clazz.getDeclaredMethod(method, args);
			m.setAccessible(true);
			m.invoke(object, objects);
		} catch (Exception ex) {}
	}
	
	public static <T> Object getMethodObject(Class<? extends T> type, Class<?> clazz, String method, Class<?>[] args, Object object, Object[] objects) {
		Object o = getMethodObject(clazz, method, args, object, objects);
		return o == null ? null : type.cast(o);
	}
	
	public static Object getMethodObject(Class<?> clazz, String method, Class<?>[] args, Object object, Object[] objects) {
		try {
			Method m = clazz.getDeclaredMethod(method, args);
			m.setAccessible(true);
			return m.invoke(object, objects);
		} catch (Exception ex) {}
		
		return null; 
	}
	
	public static Object getMethodObject(Class<?> clazz, String method, Object object) {
		return getMethodObject(clazz, method, new Class[] {}, object, new Object[] {});
	}
	
	public static Object newInstance(Class<?> clazz, Class<?>[] args, Object[] objects) {
		try {
			return clazz.getConstructor(args).newInstance(objects);
		} catch (Exception ex) {} 
		
		return null;
	}
}