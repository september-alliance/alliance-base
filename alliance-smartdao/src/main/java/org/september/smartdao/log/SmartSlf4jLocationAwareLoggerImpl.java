package org.september.smartdao.log;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.slf4j.spi.LocationAwareLogger;

public class SmartSlf4jLocationAwareLoggerImpl implements Log{

	  
	  private static final Marker MARKER = MarkerFactory.getMarker(LogFactory.MARKER);

	  private static final String FQCN = Slf4jImpl.class.getName();

	  private final LocationAwareLogger logger;
	  
	  public static ThreadLocal<Boolean> enableDebugger = new ThreadLocal<Boolean>();

	  SmartSlf4jLocationAwareLoggerImpl(LocationAwareLogger logger) {
	    this.logger = logger;
	  }

	  @Override
	  public boolean isDebugEnabled() {
		  Boolean flag = enableDebugger.get();
		  if(flag!=null && flag==false) {
			  return false;
		  }
		  return logger.isDebugEnabled();
	  }

	  @Override
	  public boolean isTraceEnabled() {
	    return logger.isTraceEnabled();
	  }

	  @Override
	  public void error(String s, Throwable e) {
	    logger.log(MARKER, FQCN, LocationAwareLogger.ERROR_INT, s, null, e);
	  }

	  @Override
	  public void error(String s) {
	    logger.log(MARKER, FQCN, LocationAwareLogger.ERROR_INT, s, null, null);
	  }

	  @Override
	  public void debug(String s) {
	    logger.log(MARKER, FQCN, LocationAwareLogger.DEBUG_INT, s, null, null);
	  }

	  @Override
	  public void trace(String s) {
	    logger.log(MARKER, FQCN, LocationAwareLogger.TRACE_INT, s, null, null);
	  }

	  @Override
	  public void warn(String s) {
	    logger.log(MARKER, FQCN, LocationAwareLogger.WARN_INT, s, null, null);
	  }


}
