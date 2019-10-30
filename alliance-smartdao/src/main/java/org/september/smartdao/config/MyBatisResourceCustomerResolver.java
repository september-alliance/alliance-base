package org.september.smartdao.config;

import java.util.List;

import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * @author yexinzhou
 * @version 1.0.0
 * @date 2019/10/11 9:32
 * @see
 */
public interface MyBatisResourceCustomerResolver {
     
	List<org.springframework.core.io.Resource> getResourcesList(ResourcePatternResolver resolver);
}
