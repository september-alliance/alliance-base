package org.september.smartdao.config;

import org.springframework.stereotype.Component;

/**
 * TODO使用map读取mybatis的配置项
 * mybatis 配置应接口配置
 * @author yexinzhou
 * @version 1.0.0
 * @date 2018/11/7 9:32
 * @see
 */
@Component
public interface MyBatisConfigManager {
     String getTypeAliasesPackage();
     
     default String getMapperLocation(){
    	 return "classpath:/mybatis/mapper/**/*.xml";
     }
}
