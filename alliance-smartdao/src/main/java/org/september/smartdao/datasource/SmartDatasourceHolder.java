package org.september.smartdao.datasource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.september.core.exception.BusinessException;
import org.september.smartdao.datasource.config.DataSourceGroup;
import org.september.smartdao.datasource.config.DataSourceProperty;
import org.september.smartdao.util.DataSourceUtil;
import org.springframework.util.StringUtils;

public class SmartDatasourceHolder {

	// 维护了多个数据源的配置信息
	private static Map<String, DataSourceGroup> dsGroupMap = new HashMap<>();

	//通过设置该值来切换数据源，具体通过方法setDataSourceGroup
	private static ThreadLocal<String> currentDataSourceGroup = new ThreadLocal<>();

	// 业务上通过设置该值来选择读库还是写库，具体通过方法switchToWrite，switchToRead
	private static ThreadLocal<Boolean> isReadonly = new ThreadLocal<>();

	private static SmartDatasourceHolder instance = new SmartDatasourceHolder();

	/**
	 * 	锁定数据源后，切换数据源无效，包括读写分离的切换也无效
	 */
	private static ThreadLocal<Boolean> datasourceLock = new ThreadLocal<>();
	
	private static Random rand = new Random();
	
	public static SmartRoutingDataSource srds;
	
	public static String defaultDatasourceGroup = "";

	private SmartDatasourceHolder() {

	}

	public static String getDataSourceKey() {
		String key = currentDataSourceGroup.get();
		if (key == null) {
			key = defaultDatasourceGroup;
		}
		DataSourceGroup group = dsGroupMap.get(key);
		if (isReadonly.get() != null && isReadonly.get() == true) {
			// 读库
			// 随机一个读库
			int num = rand.nextInt(1000);
			return group.getName() + "-read-" + num % group.getReadDSList().size();
		} else {
			// 写库
			return group.getName() + "-write";
		}
	}
	
	public static DataSource getCurrentDataSource() {
		String key = currentDataSourceGroup.get();
		if (key == null) {
			key = defaultDatasourceGroup;
		}
		DataSourceGroup group = dsGroupMap.get(key);
		return group.getWriteDS();
	}

	public static void switchToWrite() {
		if (datasourceLock.get() != null && datasourceLock.get() == true) {
			//log
		}else {
			isReadonly.set(false);
		}
	}

	public static void switchToRead() {
		if (datasourceLock.get() != null && datasourceLock.get() == true) {
			//log
		}else {
			isReadonly.set(true);
		}
	}

	public static void setDataSourceGroup(String group) {
		if (datasourceLock.get() != null && datasourceLock.get() == true) {
			// log
		} else {
			currentDataSourceGroup.set(group);
		}
	}

	/**
	 * 	动态添加一个mysql数据源，本方法只支持一个数据库，没有读写分离的情况。添加的数据库默认为可读可写。
	 * @param groupName 数据源名称
	 * @param jdbcUrl
	 * @param username
	 * @param password
	 */
	public static void addMySQLDataSource(String groupName, String jdbcUrl,String username,String password,String driver) {
		if(StringUtils.isEmpty(groupName)) {
			throw new BusinessException("数据源名不能为空");
		}
		DataSourceGroup group = new DataSourceGroup();
		group.setName(groupName);
		
		DataSourceProperty dsp = new DataSourceProperty();
		dsp.setJdbcUrl(jdbcUrl);
		dsp.setDriverClass(driver);
		dsp.setUsername(username);
		dsp.setPassword(password);
		
		DataSource ds = new DataSource();
		
		DataSourceUtil.copyProps(dsp, ds);
		
		group.setWriteDS(ds);
		group.getReadDSList().add(ds);
		
		dsGroupMap.put(groupName, group);
		srds.addDataSourceGroup(group);
		if("".equals(defaultDatasourceGroup)) {
			defaultDatasourceGroup = groupName;
		}
	}
	
	/**
	 * 	卸载一个数据源，本方法比较危险，一定要确认卸载的数据源名称无误，谨慎操作
	 * @param name
	 */
	public static void removeDataSource(String name) {
		dsGroupMap.remove(name);
	}
	
	public static SmartDatasourceHolder getInstance() {
		return instance;
	}

	public void setDataSourceGroupMap(Map<String, DataSourceGroup> map) {
		dsGroupMap = map;
	}

	public static void lockDataSource() {
		datasourceLock.set(true);
	}

	public static void releaseDataSourceLock() {
		datasourceLock.set(false);
	}
	
	public static Collection<DataSourceGroup> getDataSourceGroups() {
		return dsGroupMap.values();
	}
}
