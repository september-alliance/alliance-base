package org.september.smartdao.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.september.core.component.log.LogHelper;
import org.september.smartdao.anno.AutoIncrease;
import org.september.smartdao.anno.Column;
import org.september.smartdao.anno.Id;
import org.september.smartdao.anno.Table;
import org.september.smartdao.model.QueryPair;


public class SqlHelper {

    private static final LogHelper Logger = LogHelper.getLogger(SqlHelper.class);

    public static String getTableName(Class<?> clazz) {
        String tableName = clazz.getSimpleName();
        Table tableAno = clazz.getAnnotation(Table.class);
        if (tableAno != null) {
            tableName = tableAno.name();
        }
        return tableName.toLowerCase();
    }

    public static List<QueryPair> getQueryPairs(Object parameterObject) {
    	List<QueryPair> result = new ArrayList<QueryPair>();
    	if(parameterObject==null){
    		return result;
    	}
    	Field[] fields = getFieldsWithoutTransient(parameterObject.getClass());
        for (int i = 0; i < fields.length; i++) {
            Id idAno = fields[i].getAnnotation(Id.class);
            if (idAno != null) {
                continue;
            }
            fields[i].setAccessible(true);
            try {
                Object val = fields[i].get(parameterObject);

                if (val != null) {
                	QueryPair pair = new QueryPair();
//                	if (!"".equals(val)) {
//                        pair.setColumnName(getColumnName(fields[i]));
//                        pair.setColumnValue(val);
//                        result.add(pair);
//                    }
                    if (val instanceof Enum) {
                        pair.setColumnName(getColumnName(fields[i]));
                        pair.setColumnValue(((Enum<?>)val).ordinal());
                        result.add(pair);
                    } else {
                        pair.setColumnName(getColumnName(fields[i]));
                        pair.setColumnValue(val);
                        result.add(pair);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        return result;
    }

    public static String getColumnName(Field field) {
        Column colAno = field.getAnnotation(Column.class);
        if (colAno == null) {
            return field.getName();
        } else {
            return colAno.name();
        }
    }

    public static Map<String,Object> dbFieldToEntityField(Class<?> clazz, Map<String,Object> dbData) throws SecurityException {
        Map<String,Object> result = new HashMap<>();
        for (String key : dbData.keySet()) {
            Field field = getFieldWithAnnotationColumnName(clazz, key.toString());
            if (field == null) {
                result.put(key, dbData.get(key));
            }else{
            	result.put(field.getName(), dbData.get(key));
            }
        }
        return result;
    }

    public static Field[] getFieldsWithoutTransient(Class<?> clazz) {
        Field[] fields = ReflectHelper.getAllDeclaredFields(clazz);
        List<Field> result = new ArrayList<Field>();
        for (int i = 0; i < fields.length; i++) {
            if (ReflectHelper.isTransientField(fields[i])) {
                continue;
            }
            result.add(fields[i]);
        }
        return result.toArray(new Field[] {});
    }

    public static boolean isAutoInstreaseField(Field field) {
        if (field == null) {
            return false;
        }
        AutoIncrease ano = field.getAnnotation(AutoIncrease.class);
        if (ano == null) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isIdField(Field field) {
        if (field == null) {
            return false;
        }
        Id ano = field.getAnnotation(Id.class);
        if (ano == null) {
            return false;
        } else {
            return true;
        }
    }

    public static Field getIdOfEntity(Object obj) {
        return getIdOfClass(obj.getClass());
    }

    public static Field getIdOfClass(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            Id idAno = field.getAnnotation(Id.class);
            if (idAno != null) {
                return field;
            }
        }
        throw new RuntimeException("Id not found for " + clazz);
    }

    public static String getIdColumnOfEntity(Object obj) {
        return getIdColumnOfClass(obj.getClass());
    }

    public static String getIdColumnOfClass(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            Id idAno = field.getAnnotation(Id.class);
            if (idAno != null) {
                Column colAno = field.getAnnotation(Column.class);
                if (colAno == null) {
                    return field.getName();
                } else {
                    return colAno.name();
                }
            }
        }
        throw new RuntimeException("Id not found for " + clazz);
    }

    private static Field getFieldWithAnnotationColumnName(Class clazz, String annoName) {
        for (Field f : clazz.getDeclaredFields()) {
            Column anno = f.getAnnotation(Column.class);
            if (anno == null) {
                continue;
            }
            if (annoName.equals(anno.name())) {
                return f;
            }
        }
        return null;
    }
}
