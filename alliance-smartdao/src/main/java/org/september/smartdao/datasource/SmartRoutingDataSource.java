package org.september.smartdao.datasource;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.september.core.exception.BusinessException;
import org.september.smartdao.datasource.config.DataSourceGroup;
import org.september.smartdao.datasource.config.DataSourceProperty;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class SmartRoutingDataSource extends AbstractRoutingDataSource {

	/**
	 * 	没什么用，后面要删掉
	 */
	@Deprecated
	private List<DataSourceProperty> dataSourcePropertys;

	private String dialect;
	
	@Override
	protected Object determineCurrentLookupKey() {
		return SmartDatasourceHolder.getDataSourceKey();
	}

	public void init() throws SQLException {
	}

	@Deprecated
	public List<DataSourceProperty> getDataSourcePropertys() {
		return dataSourcePropertys;
	}

	@Deprecated
	public void setDataSourcePropertys(List<DataSourceProperty> dataSourcePropertys) {
		this.dataSourcePropertys = dataSourcePropertys;
	}

	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}
	
	public void addDataSourceGroup(DataSourceGroup group) {
		try {
			Field field = AbstractRoutingDataSource.class.getDeclaredField("targetDataSources");
			field.setAccessible(true);
			Map<Object, Object> map = (Map<Object, Object>) field.get(this);
			
			if(group.getWriteDS()!=null) {
				map.put(group.getName()+"-write", group.getWriteDS());
			}
			for(int i=0;i<group.getReadDSList().size();i++) {
				map.put(group.getName()+"-read-"+i, group.getReadDSList().get(i));
			}
			
		} catch (Exception e) {
			throw new BusinessException("添加数据源失败",e);
		}
		
		super.afterPropertiesSet();
	}
	
	public void removeDataSourceGroup(DataSourceGroup group) {
		try {
			Field field = AbstractRoutingDataSource.class.getDeclaredField("targetDataSources");
			field.setAccessible(true);
			Map<Object, Object> map = (Map<Object, Object>) field.get(this);
			
			if(group.getWriteDS()!=null) {
				map.remove(group.getName()+"-write");
			}
			for(int i=0;i<group.getReadDSList().size();i++) {
				map.remove(group.getName()+"-read-"+i);
			}
		} catch (Exception e) {
			throw new BusinessException("卸载数据源失败",e);
		}
		super.afterPropertiesSet();
	}
	
}
