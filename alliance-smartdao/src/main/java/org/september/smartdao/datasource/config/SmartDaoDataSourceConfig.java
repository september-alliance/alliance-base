package org.september.smartdao.datasource.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.september.core.component.log.LogHelper;
import org.september.smartdao.config.CustomizeDataSourcePropertyProvider;
import org.september.smartdao.datasource.SmartDatasourceHolder;
import org.september.smartdao.datasource.SmartRoutingDataSource;
import org.september.smartdao.util.DataSourceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * 创建数据源的地方，根据配置文件中的配置，读取成DataSourceProperty list。一个DataSourceProperty创建一个数据源，
 * 但多个数据源可以属于同一个DataSourceGroup，通过SmartDatasourceHolder可以切换当前线程中的DataSourceGroup。
 * 而至于最终是使用DataSourceGroup中的哪个DataSource则是由CommonDao和SmartDatasourceHolder决定的。
 * CommonDao确定是走读库还是写库，SmartDatasourceHolder决定使用哪个读库(默认随机选择)
 * @author yexinzhou
 *
 */
@Configuration
@ConfigurationProperties(prefix = "spring.alliance.dao")
public class SmartDaoDataSourceConfig {
	
	private LogHelper logHelper = LogHelper.getLogger(this.getClass());
	
	private List<DataSourceProperty> datasource;
	
	private String dialect;
	
	@Autowired(required=false)
	private CustomizeDataSourcePropertyProvider customizeDataSourcePropertyProvider;

	@Bean(name = "dataSource", initMethod = "init")
	public SmartRoutingDataSource dataSource() {
		SmartRoutingDataSource srds = new SmartRoutingDataSource();
		// targetDataSources里维护的是group打散后的数据源形式，也就是原始意义上的一个数据库代表一个数据源
		Map<Object, Object> dsMap = new HashMap<>();
		srds.setTargetDataSources(dsMap);
		if(dialect==null){
			if(customizeDataSourcePropertyProvider==null) {
				throw new RuntimeException("forgot to config a dialect by spring.alliance.dao.dialect ?");
			}
			dialect = customizeDataSourcePropertyProvider.getDialect();
		}
		srds.setDialect(dialect);
		if(datasource==null || datasource.isEmpty()) {
			datasource = new ArrayList<>();
			if(customizeDataSourcePropertyProvider==null) {
				logHelper.getBuilder().info("forgot to config a datasource?");
			}else {
				datasource.add(customizeDataSourcePropertyProvider.getDataSourceProperty());
			}
		}
		srds.setDataSourcePropertys(datasource);
		//设置多个数据源
		Map<String , DataSourceGroup> dsGroupsMap = new HashMap<>();
		for(DataSourceProperty dsp : datasource) {
			if(StringUtils.isEmpty(dsp.getGroup())) {
				throw new RuntimeException("datasource.group can not be empty");
			}
			if(!dsGroupsMap.containsKey(dsp.getGroup())) {
				DataSourceGroup dsg = new DataSourceGroup();
				dsg.setName(dsp.getGroup());
				dsGroupsMap.put(dsg.getName(), dsg);
			}
			DataSourceGroup group = dsGroupsMap.get(dsp.getGroup());
			DataSource ds = new DataSource();
			
			DataSourceUtil.copyProps(dsp, ds);
			
			if(dsp.getType()!=null) {
				if(dsp.getType().contains("read")) {
					group.getReadDSList().add(ds);
				}
				if(dsp.getType().contains("write")) {
					group.setWriteDS(ds);
				}
			}else {
				throw new RuntimeException("datasource type can not be empty, must be read, write, or read/write.");
			}
		}
		
		for(String key : dsGroupsMap.keySet()) {
			DataSourceGroup group = dsGroupsMap.get(key);
			if(group.getWriteDS()!=null) {
				dsMap.put(group.getName()+"-write", group.getWriteDS());
			}
			for(int i=0;i<group.getReadDSList().size();i++) {
				dsMap.put(group.getName()+"-read-"+i, group.getReadDSList().get(i));
			}
		}
		SmartDatasourceHolder.getInstance().setDataSourceGroupMap(dsGroupsMap);
		if(!dsGroupsMap.isEmpty()) {
			SmartDatasourceHolder.defaultDatasourceGroup=dsGroupsMap.keySet().iterator().next();
		}
		
		SmartDatasourceHolder.srds = srds;
		return srds;
	}
	
	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public void setDatasource(List<DataSourceProperty> datasource) {
		this.datasource = datasource;
	}
}
