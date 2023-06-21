package org.september.smartdao.datasource.config;

import org.apache.tomcat.jdbc.pool.DataSourceFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceProperty {
	
	private String group;
	private String jdbcUrl;
	
	/**
	 * read
	 * write
	 * read/write
	 */
	private String type;
	
	private boolean enable = true;
	
	private int weight;
	
	// 一下为tomcat 连接池配置
	private String driverClass;
	private String username;
	private String password;
	private int maxActive = 20;
	private int initialSize = 10;
	private Boolean defaultAutoCommit = null;
    private Boolean defaultReadOnly = null;
    private int defaultTransactionIsolation = DataSourceFactory.UNKNOWN_TRANSACTIONISOLATION;
    private String defaultCatalog = null;
    private int maxIdle = maxActive;
    private int minIdle = initialSize;
    private int maxWait = 30000;
    private String validationQuery = "SELECT 1";
    private int validationQueryTimeout = -1;
    private boolean testOnBorrow = true;
    private boolean testOnReturn = false;
    private boolean testWhileIdle = false;
    private int timeBetweenEvictionRunsMillis = 5000;
    private int numTestsPerEvictionRun;
    private int minEvictableIdleTimeMillis = 60000;
    private boolean accessToUnderlyingConnectionAllowed = true;
    private boolean removeAbandoned = false;
    private int removeAbandonedTimeout = 60;
    private boolean logAbandoned = false;
    private long validationInterval = 3000;
    private boolean jmxEnabled = true;
    private String initSQL;
    private boolean testOnConnect =false;
    private String jdbcInterceptors=null;
    private boolean fairQueue = true;
    private boolean useEquals = true;
    private  int abandonWhenPercentageFull = 0;
    private long maxAge = 0;
    private boolean useLock = false;
    private int suspectTimeout = 0;
    private String dataSourceJNDI = null;
    private  boolean alternateUsernameAllowed = false;
    private boolean commitOnReturn = false;
    private boolean rollbackOnReturn = false;
    private boolean useDisposableConnectionFacade = true;
    private boolean logValidationErrors = false;
    private boolean propagateInterruptState = false;
    private boolean ignoreExceptionOnPreLoad = false;
    private boolean useStatementFacade = true;
	
	public String getDriverClass() {
		return driverClass;
	}
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getJdbcUrl() {
		return jdbcUrl;
	}
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getMaxActive() {
		return maxActive;
	}
	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}
	public int getInitialSize() {
		return initialSize;
	}
	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Boolean getDefaultAutoCommit() {
		return defaultAutoCommit;
	}
	public void setDefaultAutoCommit(Boolean defaultAutoCommit) {
		this.defaultAutoCommit = defaultAutoCommit;
	}
	public Boolean getDefaultReadOnly() {
		return defaultReadOnly;
	}
	public void setDefaultReadOnly(Boolean defaultReadOnly) {
		this.defaultReadOnly = defaultReadOnly;
	}
	public int getDefaultTransactionIsolation() {
		return defaultTransactionIsolation;
	}
	public void setDefaultTransactionIsolation(int defaultTransactionIsolation) {
		this.defaultTransactionIsolation = defaultTransactionIsolation;
	}
	public String getDefaultCatalog() {
		return defaultCatalog;
	}
	public void setDefaultCatalog(String defaultCatalog) {
		this.defaultCatalog = defaultCatalog;
	}
	public int getMaxIdle() {
		return maxIdle;
	}
	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}
	public int getMinIdle() {
		return minIdle;
	}
	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}
	public int getMaxWait() {
		return maxWait;
	}
	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}
	public String getValidationQuery() {
		return validationQuery;
	}
	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}
	public int getValidationQueryTimeout() {
		return validationQueryTimeout;
	}
	public void setValidationQueryTimeout(int validationQueryTimeout) {
		this.validationQueryTimeout = validationQueryTimeout;
	}
	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}
	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}
	public boolean isTestOnReturn() {
		return testOnReturn;
	}
	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}
	public boolean isTestWhileIdle() {
		return testWhileIdle;
	}
	public void setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}
	public int getTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}
	public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}
	public int getNumTestsPerEvictionRun() {
		return numTestsPerEvictionRun;
	}
	public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
		this.numTestsPerEvictionRun = numTestsPerEvictionRun;
	}
	public int getMinEvictableIdleTimeMillis() {
		return minEvictableIdleTimeMillis;
	}
	public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}
	public boolean isAccessToUnderlyingConnectionAllowed() {
		return accessToUnderlyingConnectionAllowed;
	}
	public void setAccessToUnderlyingConnectionAllowed(boolean accessToUnderlyingConnectionAllowed) {
		this.accessToUnderlyingConnectionAllowed = accessToUnderlyingConnectionAllowed;
	}
	public boolean isRemoveAbandoned() {
		return removeAbandoned;
	}
	public void setRemoveAbandoned(boolean removeAbandoned) {
		this.removeAbandoned = removeAbandoned;
	}
	public int getRemoveAbandonedTimeout() {
		return removeAbandonedTimeout;
	}
	public void setRemoveAbandonedTimeout(int removeAbandonedTimeout) {
		this.removeAbandonedTimeout = removeAbandonedTimeout;
	}
	public boolean isLogAbandoned() {
		return logAbandoned;
	}
	public void setLogAbandoned(boolean logAbandoned) {
		this.logAbandoned = logAbandoned;
	}
	public long getValidationInterval() {
		return validationInterval;
	}
	public void setValidationInterval(long validationInterval) {
		this.validationInterval = validationInterval;
	}
	public boolean isJmxEnabled() {
		return jmxEnabled;
	}
	public void setJmxEnabled(boolean jmxEnabled) {
		this.jmxEnabled = jmxEnabled;
	}
	public String getInitSQL() {
		return initSQL;
	}
	public void setInitSQL(String initSQL) {
		this.initSQL = initSQL;
	}
	public boolean isTestOnConnect() {
		return testOnConnect;
	}
	public void setTestOnConnect(boolean testOnConnect) {
		this.testOnConnect = testOnConnect;
	}
	public String getJdbcInterceptors() {
		return jdbcInterceptors;
	}
	public void setJdbcInterceptors(String jdbcInterceptors) {
		this.jdbcInterceptors = jdbcInterceptors;
	}
	public boolean isFairQueue() {
		return fairQueue;
	}
	public void setFairQueue(boolean fairQueue) {
		this.fairQueue = fairQueue;
	}
	public boolean isUseEquals() {
		return useEquals;
	}
	public void setUseEquals(boolean useEquals) {
		this.useEquals = useEquals;
	}
	public int getAbandonWhenPercentageFull() {
		return abandonWhenPercentageFull;
	}
	public void setAbandonWhenPercentageFull(int abandonWhenPercentageFull) {
		this.abandonWhenPercentageFull = abandonWhenPercentageFull;
	}
	public long getMaxAge() {
		return maxAge;
	}
	public void setMaxAge(long maxAge) {
		this.maxAge = maxAge;
	}
	public boolean isUseLock() {
		return useLock;
	}
	public void setUseLock(boolean useLock) {
		this.useLock = useLock;
	}
	public int getSuspectTimeout() {
		return suspectTimeout;
	}
	public void setSuspectTimeout(int suspectTimeout) {
		this.suspectTimeout = suspectTimeout;
	}
	public String getDataSourceJNDI() {
		return dataSourceJNDI;
	}
	public void setDataSourceJNDI(String dataSourceJNDI) {
		this.dataSourceJNDI = dataSourceJNDI;
	}
	public boolean isAlternateUsernameAllowed() {
		return alternateUsernameAllowed;
	}
	public void setAlternateUsernameAllowed(boolean alternateUsernameAllowed) {
		this.alternateUsernameAllowed = alternateUsernameAllowed;
	}
	public boolean isCommitOnReturn() {
		return commitOnReturn;
	}
	public void setCommitOnReturn(boolean commitOnReturn) {
		this.commitOnReturn = commitOnReturn;
	}
	public boolean isRollbackOnReturn() {
		return rollbackOnReturn;
	}
	public void setRollbackOnReturn(boolean rollbackOnReturn) {
		this.rollbackOnReturn = rollbackOnReturn;
	}
	public boolean isUseDisposableConnectionFacade() {
		return useDisposableConnectionFacade;
	}
	public void setUseDisposableConnectionFacade(boolean useDisposableConnectionFacade) {
		this.useDisposableConnectionFacade = useDisposableConnectionFacade;
	}
	public boolean isLogValidationErrors() {
		return logValidationErrors;
	}
	public void setLogValidationErrors(boolean logValidationErrors) {
		this.logValidationErrors = logValidationErrors;
	}
	public boolean isPropagateInterruptState() {
		return propagateInterruptState;
	}
	public void setPropagateInterruptState(boolean propagateInterruptState) {
		this.propagateInterruptState = propagateInterruptState;
	}
	public boolean isIgnoreExceptionOnPreLoad() {
		return ignoreExceptionOnPreLoad;
	}
	public void setIgnoreExceptionOnPreLoad(boolean ignoreExceptionOnPreLoad) {
		this.ignoreExceptionOnPreLoad = ignoreExceptionOnPreLoad;
	}
	public boolean isUseStatementFacade() {
		return useStatementFacade;
	}
	public void setUseStatementFacade(boolean useStatementFacade) {
		this.useStatementFacade = useStatementFacade;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
}
