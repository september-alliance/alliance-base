package org.september.smartdao.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.september.core.component.log.LogHelper;
import org.september.smartdao.anno.AutoIncrease;
import org.september.smartdao.anno.Id;
import org.september.smartdao.anno.IntegerDefaultValue;
import org.september.smartdao.model.QueryPair;

import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;


public class SqlHelper {

    private static final LogHelper logger = LogHelper.getLogger(SqlHelper.class);

    public String getTableName(Class<?> clazz) {
        String tableName = clazz.getSimpleName();
        Table tableAno = clazz.getAnnotation(Table.class);
        if (tableAno != null) {
            tableName = tableAno.name();
        }
        return tableName.toLowerCase();
    }
    public List<QueryPair> getQueryPairs(Object parameterObject) {
    	return getQueryPairs(parameterObject , false);
    }
    public List<QueryPair> getQueryPairs(Object parameterObject,boolean defaultValue) {
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
                }else {
                	if(defaultValue) {
                		//get default value
                    	QueryPair pair = new QueryPair();
                    	pair.setColumnName(getColumnName(fields[i]));
                    	IntegerDefaultValue anno = fields[i].getAnnotation(IntegerDefaultValue.class);
                    	if(anno!=null) {
                    		pair.setColumnValue(anno.value());
                    		result.add(pair);
                    	}
                	}
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        return result;
    }

    public String getColumnName(Field field) {
        Column colAno = field.getAnnotation(Column.class);
        if (colAno == null) {
            return field.getName();
        } else {
            return colAno.name();
        }
    }

    public Map<String,Object> dbFieldToEntityField(Class<?> clazz, Map<String,Object> dbData) throws SecurityException {
        Map<String,Object> result = new HashMap<>();
        for (String key : dbData.keySet()) {
            Field field = getFieldWithAnnotationColumnName(clazz, key.toString());
            if (field == null) {
            	//兼容开启自动转驼峰的情况，把驼峰格式再转会下划线格式。
            	String snakeName = camelToSnake(key.toString());
            	field = getFieldWithAnnotationColumnName(clazz, snakeName);
            	if(field!=null) {
            		result.put(field.getName(), dbData.get(key));
            	}else {
            		logger.getBuilder().error(key.toString()+"与实体类字段不匹配");
            	}
                
            }else{
            	result.put(field.getName(), dbData.get(key));
            }
        }
        return result;
    }

    public Field[] getFieldsWithoutTransient(Class<?> clazz) {
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

    public boolean isAutoInstreaseField(Field field) {
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

    public boolean isIdField(Field field) {
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

    public Field getIdOfEntity(Object obj) {
        return getIdOfClass(obj.getClass());
    }

    public Field getIdOfClass(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            Id idAno = field.getAnnotation(Id.class);
            if (idAno != null) {
                return field;
            }
        }
        if(clazz.getSuperclass()!=Object.class) {
            return getIdOfClass(clazz.getSuperclass());
        }
        throw new RuntimeException("Id not found for " + clazz);
    }

    public String getIdColumnOfEntity(Object obj) {
        return getIdColumnOfClass(obj.getClass());
    }

    public String getIdColumnOfClass(Class<?> clazz) {
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

    protected Field getFieldWithAnnotationColumnName(Class clazz, String dbName) {
        for (Field f : getAllDeclaredFields(clazz)) {
            Column anno = f.getAnnotation(Column.class);
            if (anno == null) {
            	if(f.getName().equals(dbName)) {
            		return f;
            	}
                continue;
            }
            if (dbName.equals(anno.name())) {
                return f;
            }
        }
        return null;
    }
    
    protected List<Field> getAllDeclaredFields(Class clazz){
    	List<Field> result = new ArrayList<>();
    	 while (clazz != null) {
    		 if(clazz.equals(Object.class)) {
    			 break;
    		 }
             Field[] fields = clazz.getDeclaredFields(); // 获取当前类声明的所有字段
             for (Field field : fields) {
            	 result.add(field);
             }
             // 获取当前类的父类
             clazz = clazz.getSuperclass();
         }
    	 return result;
    }
    
    public static String camelToSnake(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        
        // 使用正则表达式进行转换
        return input.replaceAll("([a-z])([A-Z0-9])", "$1_$2").toLowerCase();
    }
    
    public static void main(String[] args) {
    	System.out.println(camelToSnake("maxWeightAxel6"));
    }
}
