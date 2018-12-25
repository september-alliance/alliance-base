package org.september.smartdao.mybatisPlugs;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.MapWrapper;
import org.september.core.util.StringExtUtil;

import java.util.Map;

public class MyMapWrapper extends MapWrapper {

    public MyMapWrapper(MetaObject metaObject, Map<String, Object> map) {
        super(metaObject, map);
    }

    @Override
    public String findProperty(String name, boolean useCamelCaseMapping) {
        if (useCamelCaseMapping
                && ((name.charAt(0) >= 'A' && name.charAt(0) <= 'Z')
                || name.indexOf("_") >= 0)) {
            return StringExtUtil.underlineToCamelhump(name);
        }
        return name;
    }
    
}