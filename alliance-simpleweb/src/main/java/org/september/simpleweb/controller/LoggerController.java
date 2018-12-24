package org.september.simpleweb.controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.september.simpleweb.auth.PublicMethod;
import org.september.simpleweb.model.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

@RestController
@RequestMapping("/logger")
public class LoggerController {

	protected final static Logger Logger = LoggerFactory.getLogger(LoggerController.class);
	
	@RequestMapping("/loggerList")
	public ModelAndView loggerList(){
		ModelAndView mv = new ModelAndView();
		return mv;
	}
	
	@PublicMethod
	@RequestMapping("/listLoggerData")
	@ResponseBody
	public ResponseVo<List<JSONObject>> listConfigItemData(String filter){
		try {
			StaticLoggerBinder loggerBinder = StaticLoggerBinder.getSingleton();
			Field field = loggerBinder.getClass().getDeclaredField("defaultLoggerContext");
			field.setAccessible(true);
			LoggerContext loggerContext = (LoggerContext) field.get(loggerBinder);
			List<JSONObject> loggerList = new ArrayList<>();
			for(ch.qos.logback.classic.Logger logger : loggerContext.getLoggerList()){
				JSONObject obj = new JSONObject();
				if("ROOT".equalsIgnoreCase(logger.getName())){
					continue;
				}
				if(!StringUtils.isEmpty(filter) && !logger.getName().contains(filter)){
					continue;
				}
				obj.put("name", logger.getName());
				obj.put("level", logger.getEffectiveLevel().levelStr);
				loggerList.add(obj);
			}
			return ResponseVo.<List<JSONObject>> BUILDER().setData(loggerList).setCode(ResponseVo.BUSINESS_CODE.SUCCESS);
		}catch(Exception ex) {
			return ResponseVo.<List<JSONObject>> BUILDER().setDesc(ex.getMessage()).setCode(ResponseVo.BUSINESS_CODE.FAILED);
		}
	}
	
	@RequestMapping("/doUpdateLogger")
	@ResponseBody
	public ResponseVo<String> doUpdateLogger(String name ,String level) throws IOException{
		Logger log = LoggerFactory.getLogger(name);
		if(log instanceof ch.qos.logback.classic.Logger) {
			ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger)log;
			logger.setLevel(Level.toLevel(level));
		}else {
			return ResponseVo.<String> BUILDER().setDesc("you are not using logback").setCode(ResponseVo.BUSINESS_CODE.FAILED);
		}
		return ResponseVo.<String> BUILDER().setData("").setCode(ResponseVo.BUSINESS_CODE.SUCCESS);
	}
}
