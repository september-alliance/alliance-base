package org.september.core.util;

import java.lang.reflect.Field;

public class BeanUtil {

	public static void copyProperty(Object src , String propName, Object dest) {
		if(src==null || propName==null || dest==null) {
			return;
		}
		try {
			Field field1 = src.getClass().getDeclaredField(propName);
			if(field1==null) {
				return;
			}
			field1.setAccessible(true);
			Object val = field1.get(src);
			
			Field field2 = dest.getClass().getDeclaredField(propName);
			if(field2==null) {
				return;
			}
			field2.setAccessible(true);
			field2.set(dest, val);
		}catch(Exception ex) {
			System.out.println("copyProperty failed with "+ex.getMessage());
		}
	}
}
