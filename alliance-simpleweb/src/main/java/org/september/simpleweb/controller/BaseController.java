package org.september.simpleweb.controller;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.september.core.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class BaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}
	
	@InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		if(binder.getTarget()==null) {
			return;
		}
    	for(Field f : binder.getTarget().getClass().getDeclaredFields()) {
    		DateTimeFormat anno = f.getAnnotation(DateTimeFormat.class);
    		if(anno!=null) {
    			if(anno.pattern()==null) {
    				throw new BusinessException(binder.getTarget().getClass().getName()+"."+f.getName()+"没有设置时间格式");
    			}
    			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(anno.pattern());
    	        simpleDateFormat.setLenient(false);
    	        CustomDateEditor dateEditor = new CustomDateEditor(simpleDateFormat, true);
    	        binder.registerCustomEditor(Date.class, f.getName(), dateEditor);
    		}else {
    			if(f.getType()==Date.class) {
    				if(request.getParameter(f.getName())!=null) {
    					throw new BusinessException(binder.getTarget().getClass().getName()+"."+f.getName()+"没有设置时间格式");
    				}
    			}
    		}
    	}
    }
}
