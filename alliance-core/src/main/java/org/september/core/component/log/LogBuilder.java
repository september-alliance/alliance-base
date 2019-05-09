package org.september.core.component.log;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.helpers.MessageFormatter;

import com.alibaba.fastjson.JSON;

public final class LogBuilder {

	private Random random  = new Random();
	
	Map<String, String> attrs = new HashMap<String, String>();

	private static final String Msg_Body = "__body";
	
	private Logger logger;

	public LogBuilder(Logger logger) {
		this.logger = logger;
	}

	protected String toHeaderString() {
		return JSON.toJSONString(attrs);
	}

	public LogBuilder tag(String key, String value) {
		attrs.put(key, value);
		return this;
	}

	public void alarm(String msg) {
	    this.tag("alarm", "true");
	    this.tag(Msg_Body, msg);
		logger.warn(toHeaderString());
	}
	
	public void info(String msg) {
		this.tag(Msg_Body, msg);
        logger.info(toHeaderString());
    }

	public void info(String msg, Object... paramArrayOfObject) {
		this.tag(Msg_Body, MessageFormatter.arrayFormat(msg, paramArrayOfObject).getMessage());
		logger.info(toHeaderString());
	}
	
	/**
	 * 按概率打印日志，适用于一些debug信息
	 * @param percent 0到1直接的小数.概率从1到100%。精确率0.1%，小于这个概率视为0
	 * @param msg
	 * @param paramArrayOfObject
	 */
	public void info(float percent,String msg, Object... paramArrayOfObject) {
		if(percent<0) {
			// never log
			return;
		}
		if(percent>=1) {
			info(msg , paramArrayOfObject);
			return;
		}
		int num = random.nextInt(1000);
		if(num>percent*1000) {
			return;
		}
		this.tag("__roll", String.valueOf(num));
		this.tag(Msg_Body, MessageFormatter.arrayFormat(msg, paramArrayOfObject).getMessage());
		logger.info(toHeaderString());
	}

	public void info(String msg, Throwable ex) {
		this.tag(Msg_Body, msg);
		logger.info(toHeaderString(), ex);
	}

	public void debug(String msg) {
		this.tag(Msg_Body, msg);
		logger.debug(toHeaderString());
	}

	public void debug(String msg, Object... paramArrayOfObject) {
		this.tag(Msg_Body, MessageFormatter.arrayFormat(msg, paramArrayOfObject).getMessage());
		logger.debug(toHeaderString());
	}

	public void debug(String msg, Throwable ex) {
		this.tag(Msg_Body, msg);
		logger.debug(toHeaderString(), ex);
	}
	
	public void warn(String msg) {
		this.tag(Msg_Body, msg);
		logger.warn(toHeaderString());
	}

	public void warn(String msg, Object... paramArrayOfObject) {
		this.tag(Msg_Body, MessageFormatter.arrayFormat(msg, paramArrayOfObject).getMessage());
		logger.warn(toHeaderString());
	}

	public void warn(String msg, Throwable ex) {
		this.tag(Msg_Body, msg);
		logger.warn(toHeaderString(), ex);
	}
	
	public void warn(String msg,Throwable ex, Object... paramArrayOfObject) {
		this.tag(Msg_Body, MessageFormatter.arrayFormat(msg, paramArrayOfObject).getMessage());
		logger.warn(toHeaderString() , ex);
	}

	public void error(String msg) {
		this.tag(Msg_Body, msg);
		logger.error(toHeaderString());
	}

	public void error(String msg, Object... paramArrayOfObject) {
		this.tag(Msg_Body, MessageFormatter.arrayFormat(msg, paramArrayOfObject).getMessage());
		logger.error(toHeaderString());
	}

	public void error(String msg, Throwable ex) {
		this.tag(Msg_Body, msg);
		logger.error(toHeaderString(), ex);
	}

	public void error(String msg, Throwable ex , Object... paramArrayOfObject) {
		this.tag(Msg_Body, MessageFormatter.arrayFormat(msg, paramArrayOfObject).getMessage());
		logger.error(toHeaderString() , ex);
	}
}
