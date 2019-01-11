package org.september.smartdao.datasource;

import java.sql.SQLException;
import java.util.List;

import org.september.smartdao.datasource.config.DataSourceProperty;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class SmartRoutingDataSource extends AbstractRoutingDataSource {

	private List<DataSourceProperty> dataSourcePropertys;

	private String dialect;
	
	@Override
	protected Object determineCurrentLookupKey() {
		return SmartDatasourceHolder.getDataSourceKey();
	}

	public void init() throws SQLException {
	}

	public List<DataSourceProperty> getDataSourcePropertys() {
		return dataSourcePropertys;
	}

	public void setDataSourcePropertys(List<DataSourceProperty> dataSourcePropertys) {
		this.dataSourcePropertys = dataSourcePropertys;
	}

	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}
}
