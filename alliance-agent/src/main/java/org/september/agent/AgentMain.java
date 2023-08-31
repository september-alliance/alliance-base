package org.september.agent;

import java.io.File;
import java.io.FileInputStream;
import java.lang.instrument.Instrumentation;
import java.util.Properties;

public class AgentMain {

	public static Properties prop  = new Properties();
	// premain()函数中注册MyClassFileTransformer转换器
    public static void premain (String agentArgs, Instrumentation instrumentation) throws Exception {
        System.out.println("premain方法");
        prop.load(new FileInputStream(new File("./agent.prop")));
        SimpleClassFileTransformer transformer = new SimpleClassFileTransformer();
        instrumentation.addTransformer(transformer, true);
    }
}
