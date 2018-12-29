package org.september.smartdao.datasource.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.jdbc.pool.DataSource;

/**
 * 数据源组，维护了一个写库，和多个读库。
 * @author yexinzhou
 *
 */
public class DataSourceGroup {

	private String name;
	
	private DataSource writeDS;
	
	private List<DataSource> readDSList = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DataSource getWriteDS() {
		return writeDS;
	}

	public void setWriteDS(DataSource writeDS) {
		this.writeDS = writeDS;
	}

	public List<DataSource> getReadDSList() {
		return readDSList;
	}

	public void setReadDSList(List<DataSource> readDSList) {
		this.readDSList = readDSList;
	}
}
