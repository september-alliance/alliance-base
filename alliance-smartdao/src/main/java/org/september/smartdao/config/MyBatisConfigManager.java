package org.september.smartdao.config;

import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.SqlSessionFactoryBean;

/**
 * mybatis配置入口，业务系统需实现该接口,并注入到spring context中
 * @author yexinzhou
 * @version 1.0.0
 * @date 2018/11/7 9:32
 * @see
 */
public interface MyBatisConfigManager {
     
     default String getMapperLocation(){
    	 return "classpath:/mybatis/mapper/**/*.xml";
     }
     
     default void addMapperResources(org.springframework.core.io.Resource[] resList) {
     }
     public void config(Configuration myBatisCfg);
}
