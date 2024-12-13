package org.september.smartdao.datasource;

import org.apache.tomcat.jdbc.pool.DataSource;

public class MyDataSource extends DataSource{

	private String shcema;

	public String getShcema() {
		return shcema;
	}

	public void setShcema(String shcema) {
		this.shcema = shcema;
	}
	
}
