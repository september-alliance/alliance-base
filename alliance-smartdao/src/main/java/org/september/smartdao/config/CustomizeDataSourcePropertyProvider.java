package org.september.smartdao.config;

import org.september.smartdao.datasource.config.DataSourceProperty;

public interface CustomizeDataSourcePropertyProvider {

	String getDialect();
	
	DataSourceProperty getDataSourceProperty();
}
