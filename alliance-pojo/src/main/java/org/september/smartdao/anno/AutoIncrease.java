package org.september.smartdao.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示字段的值由数据库自增生成,暂不支持触发器的方式
 * 需要和@Id注解一起使用才有效
 * @author yexinzhou
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoIncrease {
	boolean dbTrigger() default false;
}
