package org.september.smartdao.log;

import org.apache.ibatis.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.spi.LocationAwareLogger;

public class SmartSlf4jImpl implements Log{


	  private Log log;

	  public SmartSlf4jImpl(String clazz) {
	    Logger logger = LoggerFactory.getLogger(clazz);

	    if (logger instanceof LocationAwareLogger) {
	      try {
	        // check for slf4j >= 1.6 method signature
	        logger.getClass().getMethod("log", Marker.class, String.class, int.class, String.class, Object[].class, Throwable.class);
	        log = new SmartSlf4jLocationAwareLoggerImpl((LocationAwareLogger) logger);
	        return;
	      } catch (SecurityException e) {
	        // fail-back to Slf4jLoggerImpl
	      } catch (NoSuchMethodException e) {
	        // fail-back to Slf4jLoggerImpl
	      }
	    }

	  }

	  @Override
	  public boolean isDebugEnabled() {
	    return log.isDebugEnabled();
	  }

	  @Override
	  public boolean isTraceEnabled() {
	    return log.isTraceEnabled();
	  }

	  @Override
	  public void error(String s, Throwable e) {
	    log.error(s, e);
	  }

	  @Override
	  public void error(String s) {
	    log.error(s);
	  }

	  @Override
	  public void debug(String s) {
	    log.debug(s);
	  }

	  @Override
	  public void trace(String s) {
	    log.trace(s);
	  }

	  @Override
	  public void warn(String s) {
	    log.warn(s);
	  }


}
