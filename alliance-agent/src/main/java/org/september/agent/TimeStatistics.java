package org.september.agent;

import java.util.logging.Logger;



public class TimeStatistics {
	public static ThreadLocal<Long> t = new ThreadLocal<>();

	private static final Logger log = LoggerUtil.getLogger(TimeStatistics.class);
	
    public static void start() {
        t.set(System.currentTimeMillis());
    }
    public static void end() {
        long time = System.currentTimeMillis() - t.get();
        if(time>=10) {
        	StackTraceElement trace = Thread.currentThread().getStackTrace()[2];
        	log.info(Thread.currentThread().getName()+","+trace + " spend: " + time+" ms");
        }
    }
}
