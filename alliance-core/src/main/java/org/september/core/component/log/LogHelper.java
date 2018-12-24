package org.september.core.component.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogHelper {

	private Logger logger;
	
	public LogBuilder getBuilder(){
		return new LogBuilder(logger);
	}
	
	/**
	 * 获取class的Logger
	 * @author yexinzhou
	 * @date 2017年6月22日 上午11:13:54
	 * @param clazz
	 * @return
	 */
	public static LogHelper getLogger(Class<?> clazz){
		LogHelper logHelper = new LogHelper();
		logHelper.logger =LoggerFactory.getLogger(clazz);
		return logHelper;
	}
	
	public static void main(String[] args){
		LogHelper log = getLogger(LogHelper.class);
		log.getBuilder().info("this {} is a format {} log test {}",11 , 22);
	}
}
