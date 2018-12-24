package org.september.smartdao.datasource;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.september.smartdao.datasource.config.DataSourceGroup;

public class SmartDatasourceHolder {

	private static Map<String, DataSourceGroup> dsGroupMap = new HashMap<>();

	private static ThreadLocal<String> currentDataSourceGroup = new ThreadLocal<>();

	private static ThreadLocal<Boolean> isReadonly = new ThreadLocal<>();

	private static SmartDatasourceHolder instance = new SmartDatasourceHolder();

	private static ThreadLocal<Boolean> datasourceLock = new ThreadLocal<>();
	
	private static Random rand = new Random();

	private SmartDatasourceHolder() {

	}

	public static String getDataSourceKey() {
		String key = currentDataSourceGroup.get();
		if (key == null) {
			DataSourceGroup group = dsGroupMap.values().iterator().next();
			setDataSourceGroup(group.getName());
		}
		key = currentDataSourceGroup.get();
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
}
