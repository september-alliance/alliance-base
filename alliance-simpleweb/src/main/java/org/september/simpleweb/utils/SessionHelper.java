package org.september.simpleweb.utils;

import java.util.Map;


import org.september.simpleweb.model.WebConst;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionHelper {

	@SuppressWarnings("unchecked")
	public static <T> T getSessionUser(Class<T> clazz) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		Object user = request.getSession().getAttribute(WebConst.Session.Session_User_Key);
		return (T) user;
	}
	
	public static void setSessionUser(Object user) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		request.getSession().setAttribute(WebConst.Session.Session_User_Key, user);
	}
	
	public static HttpSession getSession() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return request.getSession();
	}
	
	public static void setLoginReturnData(Map data) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		request.getSession().setAttribute(WebConst.Session.Login_Return_Data_Key, data);
	}
	
	public static Map getLoginReturnData() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return (Map)request.getSession().getAttribute(WebConst.Session.Login_Return_Data_Key);
	}
	
}
