package org.september.smartdao.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IntegerDefaultValue {
    /**
     * 	字段的默认值
     * @author yexinzhou
     * @date 2020年5月20日 上午10:58:56
     * @return
     */
	int value();
}
