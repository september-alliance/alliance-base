package org.september.smartdao.util;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.september.smartdao.datasource.config.DataSourceProperty;

public class DataSourceUtil {

	public static void copyProps(DataSourceProperty from , DataSource to) {
		to.setUrl(from.getJdbcUrl());
		to.setDriverClassName(from.getDriverClass());
		to.setUsername(from.getUsername());
		to.setPassword(from.getPassword());
		
		to.setAbandonWhenPercentageFull(from.getAbandonWhenPercentageFull());
		to.setAccessToUnderlyingConnectionAllowed(from.isAccessToUnderlyingConnectionAllowed());
		to.setAlternateUsernameAllowed(from.isAlternateUsernameAllowed());
		to.setCommitOnReturn(from.isCommitOnReturn());
		to.setDataSourceJNDI(from.getDataSourceJNDI());
		to.setDefaultAutoCommit(from.getDefaultAutoCommit());
		to.setDefaultReadOnly(from.getDefaultReadOnly());
		to.setDefaultTransactionIsolation(from.getDefaultTransactionIsolation());
		to.setFairQueue(from.isFairQueue());
		to.setIgnoreExceptionOnPreLoad(from.isIgnoreExceptionOnPreLoad());
		to.setInitialSize(from.getInitialSize());
		to.setInitSQL(from.getInitSQL());
		to.setJdbcInterceptors(from.getJdbcInterceptors());
		to.setJmxEnabled(from.isJmxEnabled());
		to.setLogAbandoned(true);
		to.setLogValidationErrors(from.isLogValidationErrors());
		to.setMaxActive(from.getMaxActive());
		to.setMaxAge(from.getMaxAge());
		to.setMaxIdle(from.getMaxIdle());
		to.setMaxWait(from.getMaxWait());
		to.setMinIdle(from.getMinIdle());
		to.setMinEvictableIdleTimeMillis(from.getMinEvictableIdleTimeMillis());
		to.setPropagateInterruptState(from.isPropagateInterruptState());
		to.setRemoveAbandoned(from.isRemoveAbandoned());
		to.setRemoveAbandonedTimeout(from.getRemoveAbandonedTimeout());
		to.setRollbackOnReturn(from.isRollbackOnReturn());
		to.setSuspectTimeout(from.getSuspectTimeout());
		to.setTestOnBorrow(from.isTestOnBorrow());
		to.setTestOnConnect(from.isTestOnConnect());
		to.setTestOnReturn(from.isTestOnReturn());
		to.setTestWhileIdle(true);
		to.setTimeBetweenEvictionRunsMillis(from.getTimeBetweenEvictionRunsMillis());
		to.setUseDisposableConnectionFacade(from.isUseDisposableConnectionFacade());
		to.setUseEquals(from.isUseEquals());
		to.setUseLock(from.isUseLock());
		to.setUseStatementFacade(from.isUseStatementFacade());
		to.setValidationQuery(from.getValidationQuery());
		to.setValidationInterval(from.getValidationInterval());
		to.setValidationQueryTimeout(from.getValidationQueryTimeout());
	}
}
