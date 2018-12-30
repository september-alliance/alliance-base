package org.september.smartdao.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 说明：反射工具
 * <p>
 * 修改时间：2014年9月20日
 */
public class ReflectHelper {
    private static final Logger logger = LoggerFactory.getLogger(ReflectHelper.class);

    public static Map<String ,Object> transEmptyString2Null(Object obj) {
        Map<String ,Object> map = new HashMap<>();
        for (Field f : getAllDeclaredFields(obj.getClass())) {
            f.setAccessible(true);
            try {
                Object value = f.get(obj);
                if(value==null){
                	continue;
                }
                if("".equals(value)){
                	map.put(f.getName(), null);
                }else{
                	map.put(f.getName(), value);
                }
            } catch (Exception e) {
                logger.warn("get " + f.getName() + " value of " + obj.getClass().getName() + " failed ");
            }
        }
        return map;
    }

    public static <T> List<T> transformMapToEntity(Class<T> clazz, List<Map<String , Object>> mapResult) {
        List<T> entityResult = new ArrayList<T>();
        for (Map<String , Object> map : mapResult) {
        	entityResult.add(transformMapToEntity(clazz , map));
        }
        return entityResult;
    }

    private static void setProperties(Object dest, Map<String , Object> origin) throws IllegalArgumentException, IllegalAccessException {
        origin = SqlHelper.dbFieldToEntityField(dest.getClass(), origin);
        Field[] fieldList= getAllDeclaredFields(dest.getClass());

        for (Field field : fieldList) {
            String name = field.getName();
            Object value = origin.get(name);
            if (value == null) {
                value = origin.get(name.toUpperCase());
            }
            field.setAccessible(true);
            if (value != null) {
            	//boolean 转 int
            	if(value instanceof Boolean) {
            		if((Boolean)value) {
            			value = 1;
            		}else {
            			value = 0;
            		}
            	}
                if (field.getType().equals(Character.class) || field.getClass().equals(char.class)) {
                    String str = (String) value;
                    if (str.length() > 0) {
                        field.set(dest, str.charAt(0));
                    }
                    /**处理jdk1.8的时间类型 2018-04-13　fan create by */
                } else if (field.getType().equals(Instant.class)) {
                    Timestamp timestamp = (Timestamp) value;
                    field.set(dest, timestamp.toInstant());
                } else if (field.getType().equals(LocalDateTime.class)) {
                    Timestamp timestamp = (Timestamp) value;
                    field.set(dest, timestamp.toLocalDateTime());
                } else if (field.getType().equals(LocalDate.class)) {
                    Date date = (Date) value;
                    field.set(dest, date.toLocalDate());
                } else if (field.getType().equals(LocalTime.class)) {
                    Time time = (Time) value;
                    field.set(dest, time.toLocalTime());
                } else if (field.getType().equals(ZonedDateTime.class)) {
                    Timestamp timestamp = (Timestamp) value;
                    field.set(dest, OffsetDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault()));
                    /**------------------end--------------------*/
                }else if (field.getType().isEnum()) {
                    field.set(dest, field.getType().getEnumConstants()[(int)value]);
                }else {
                    field.set(dest, value);
                }

            }
        }
    }

    public static <T> T transformMapToEntity(Class<T> clazz, Map<String , Object> map) {
    	if(map==null){
    		return null;
    	}
        try {
            T entity = clazz.newInstance();
            setProperties(entity, map);
            return entity;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("try to wrap entity " + clazz.getName() + " failed", e);
        }
    }
    
    public static Field[] getAllDeclaredFields(Class<?> clazz) {
    	List<Field> target = new ArrayList<>();
    	while(clazz!=null && !clazz.equals(Object.class)) {
    		target.addAll(Arrays.asList(clazz.getDeclaredFields()));
    		clazz = clazz.getSuperclass();
    	}
    	return target.toArray(new Field[] {});
    }
    
    public static boolean isTransientField(Field field) {
        return Modifier.isTransient(field.getModifiers());
    }
}
