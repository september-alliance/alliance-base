package org.september.smartdao.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.september.core.component.ApplicationContextHolder;
import org.september.smartdao.datasource.SmartRoutingDataSource;
import org.september.smartdao.model.ParamMap;
import org.september.smartdao.mybatisPlugs.MapWrapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import com.github.pagehelper.PageInterceptor;

@Configuration
public class MyBatisConfig implements TransactionManagementConfigurer {

    @Autowired(required = false)
    private MyBatisConfigManager myBatisConfigManager;
    @Resource
    SmartRoutingDataSource dataSource;
    
    @Autowired
    private ApplicationContextHolder applicationContextHolder;

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean() {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setCacheEnabled(false);
        configuration.setUseGeneratedKeys(true);
        configuration.setCallSettersOnNulls(true);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setLogPrefix("dao.");
        configuration.setDefaultExecutorType(ExecutorType.REUSE);
        configuration.getTypeAliasRegistry().registerAlias("ParamMap", ParamMap.class);
        
        if (myBatisConfigManager != null) {
        	myBatisConfigManager.config(configuration);
        }
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties prop = new Properties();
        prop.put("helperDialect", dataSource.getDialect());
        pageInterceptor.setProperties(prop);
        configuration.addInterceptor(pageInterceptor);
        bean.setConfiguration(configuration);
        
        bean.setObjectWrapperFactory(new MapWrapperFactory());

        // 添加XML目录
        try {
        	org.springframework.core.io.Resource[] resources1 = new org.springframework.core.io.Resource[]{};
        	if(myBatisConfigManager!=null) {
        		resources1 = resolver.getResources(myBatisConfigManager.getMapperLocation());
        	}else {
        		resources1 = resolver.getResources("classpath:/mybatis/mapper/**/*.xml");
        	}
        	List<org.springframework.core.io.Resource> list = new ArrayList<org.springframework.core.io.Resource>();
            for (org.springframework.core.io.Resource res : resources1) {
                if (!res.getFilename().contains("CommonEntityMapper.xml")) {
                    list.add(res);
                }
            }
            resources1 = list.toArray(new org.springframework.core.io.Resource[]{});
            List<org.springframework.core.io.Resource> resourcesList = new ArrayList<>();
            resourcesList.add(resolver.getResource("classpath:/mybatis/mapper/CommonEntityMapper.xml"));
            
            if(applicationContextHolder.getContext().getBeansOfType(MyBatisResourceCustomerResolver.class)!=null) {
            	for(MyBatisResourceCustomerResolver resolverx : applicationContextHolder.getContext().getBeansOfType(MyBatisResourceCustomerResolver.class).values()) {
            		resourcesList.addAll(resolverx.getResourcesList(resolver));
            	}
            }
            
            org.springframework.core.io.Resource[] resources2 = resourcesList.toArray(new org.springframework.core.io.Resource[] {});
            int length = resources1.length + resourcesList.size();
            org.springframework.core.io.Resource[] resources = Arrays.copyOf(resources1, length);
            System.arraycopy(resources2, 0, resources, resources1.length, resources2.length);
            bean.setMapperLocations(resources);

            return bean.getObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

}
