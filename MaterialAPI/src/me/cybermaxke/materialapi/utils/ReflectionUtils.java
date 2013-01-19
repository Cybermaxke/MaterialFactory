package me.cybermaxke.materialapi.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils {

	public static Object getFieldObject(Class<?> clazz, String field, Object object) {	
		try {
			Field f = clazz.getDeclaredField(field);
			f.setAccessible(true);
			return f.get(object);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			return null;
		}
	}
	
	public static void setFieldObject(Class<?> clazz, String field, Object object, Object newObj) {	
		try {
			Field f = clazz.getDeclaredField(field);
			f.setAccessible(true);
			f.set(object, newObj);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			return;
		}
	}
	
	public static void invokeMethod(Class<?> clazz, String method, Class<?>[] args, Object object, Object[] objects) {
		try {
			Method m = clazz.getDeclaredMethod(method, args);
			m.setAccessible(true);
			m.invoke(object, objects);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {}
	}
	
	public static Object getMethodObject(Class<?> clazz, String method, Class<?>[] args, Object object, Object[] objects) {
		try {
			Method m = clazz.getDeclaredMethod(method, args);
			m.setAccessible(true);
			return m.invoke(object, objects);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return null;
		}
	}
	
	public static Object getMethodObject(Class<?> clazz, String method, Object object) {
		return getMethodObject(clazz, method, new Class[] {}, object, new Object[] {});
	}
	
	public static Object newInstance(Class<?> clazz, Class<?>[] args, Object[] objects) {
		try {
			return clazz.getConstructor(args).newInstance(objects);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			return null;
		}
	}
}