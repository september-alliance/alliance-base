package org.september.core.util;

import java.lang.management.ManagementFactory;
import java.util.List;

public class JVMUtil {

    /**
     * 判断系统是否以debug模式运行
     * @author yexinzhou
     * @date 2017年6月22日 上午10:26:00
     * @return
     */
	public static boolean isDebug(){
		List<String> args = ManagementFactory.getRuntimeMXBean().getInputArguments();
		boolean isDebug = false;
		for (String arg : args) {
		  if (arg.startsWith("-Xrunjdwp") || arg.startsWith("-agentlib:jdwp")) {
		    isDebug = true;
		    break;
		  }
		}
		return isDebug;
	}
}
