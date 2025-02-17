package org.september.smartdao.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.september.smartdao.anno.Id;
import org.september.smartdao.datasource.SmartDatasourceHolder;
import org.september.smartdao.model.QueryPair;
import org.springframework.util.StringUtils;

import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;



public class DMSqlHelper extends SqlHelper{

    public String getTableName(Class<?> clazz) {
    	String schema = SmartDatasourceHolder.getCurrentDataSource().getShcema();
        String tableName = clazz.getSimpleName();
        Table tableAno = clazz.getAnnotation(Table.class);
        if (tableAno != null) {
            tableName = tableAno.name();
        }
        if(StringUtils.hasText(schema)) {
        	return wrapWithQuotation(schema)+"."+wrapWithQuotation(tableName.toLowerCase());
        }else {
        	return wrapWithQuotation(tableName.toLowerCase());
        }
    }

    public List<QueryPair> getQueryPairs(Object parameterObject,boolean defaultValue) {
    	List<QueryPair> result = super.getQueryPairs(parameterObject, defaultValue);
    	for(QueryPair pair : result) {
    		pair.setColumnName(wrapWithQuotation(pair.getColumnName()));
    	}
        return result;
    }

    private String wrapWithQuotation(String str) {
    	if(str.startsWith("\"") && str.endsWith("\"")) {
    		return str;
    	}
    	return "\""+str+"\"";
	}

	public String getColumnName(Field field) {
        Column colAno = field.getAnnotation(Column.class);
//        if (colAno == null) {
//            return field.getName();
//        } else {
//            return colAno.name();
//        }
        if (colAno == null) {
            return wrapWithQuotation(field.getName());
        } else {
            return wrapWithQuotation(colAno.name());
        }
    }

    public Map<String,Object> dbFieldToEntityField(Class<?> clazz, Map<String,Object> dbData) throws SecurityException {
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

    public String getIdColumnOfEntity(Object obj) {
        return getIdColumnOfClass(obj.getClass());
    }

    public String getIdColumnOfClass(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            Id idAno = field.getAnnotation(Id.class);
            if (idAno != null) {
                Column colAno = field.getAnnotation(Column.class);
                if (colAno == null) {
                    return wrapWithQuotation(field.getName());
                } else {
                    return wrapWithQuotation(colAno.name());
                }
            }
        }
        throw new RuntimeException("Id not found for " + clazz);
    }
}
