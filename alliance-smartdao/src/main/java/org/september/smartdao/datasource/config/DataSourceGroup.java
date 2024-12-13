package org.september.smartdao.datasource.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.september.smartdao.datasource.MyDataSource;

/**
 * 数据源组，维护了一个写库，和多个读库。
 * @author yexinzhou
 *
 */
public class DataSourceGroup {

	private String name;
	
	private MyDataSource writeDS;
	
	private List<MyDataSource> readDSList = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MyDataSource getWriteDS() {
		return writeDS;
	}

	public void setWriteDS(MyDataSource writeDS) {
		this.writeDS = writeDS;
	}

	public List<MyDataSource> getReadDSList() {
		return readDSList;
	}

	public void setReadDSList(List<MyDataSource> readDSList) {
		this.readDSList = readDSList;
	}
}
