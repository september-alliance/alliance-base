package org.september.smartdao.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    
    /**
     * 表名
     * @author yexinzhou
     * @date 2017年6月22日 上午10:59:30
     * @return
     */
	String name() default "";
}
