package org.september.smartdao.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加在是实体类的字段上面，表示该字段用作乐观锁，字段类型必须是Integer或者Long
 * @author yexinzhou
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OptimisticLock {
}
