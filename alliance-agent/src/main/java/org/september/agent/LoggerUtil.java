package org.september.agent;

import java.util.logging.Logger;

public class LoggerUtil {
	
	public static Logger getLogger(Class<?> clazz) {
		Logger log =  Logger.getLogger(clazz.getName());
		return log;
	}
}
