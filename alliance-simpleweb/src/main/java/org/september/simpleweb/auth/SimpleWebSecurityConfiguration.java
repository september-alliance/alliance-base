package org.september.simpleweb.auth;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.september.simpleweb.model.ResponseVo;
import org.september.simpleweb.utils.SessionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.alibaba.fastjson.JSONObject;

public class SimpleWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private AjaxAuthenticationEntryPoint ajaxAuthenticationEntryPoint;
	
	@Autowired
	WebApplicationContext applicationContext;
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/assets/**");
		web.ignoring().antMatchers("/login");
		// find Mapping with PublicMethod
		RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
		Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
		for (Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {

            RequestMappingInfo requestMappingInfo = entry.getKey();
            HandlerMethod mappingInfoValue = entry.getValue();
            Method method = mappingInfoValue.getMethod();
            PublicMethod publicAnno = method.getDeclaringClass().getAnnotation(PublicMethod.class);
            if (publicAnno == null) {
                publicAnno = method.getAnnotation(PublicMethod.class);
            }

            if (publicAnno != null) {
                String url = requestMappingInfo.getPatternsCondition().getPatterns().iterator().next();
                web.ignoring().antMatchers(url);
            }
        
		}
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin()
				.and().sessionManagement().maximumSessions(3).and().and().logout()
				.logoutUrl("/logout")
				// 定义当需要用户登录时候，转到的登录页面
				.and().formLogin().loginPage("/login").successHandler(new AuthenticationSuccessHandler() {

					@Override
					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) throws IOException, ServletException {
						ResponseVo<String> res = ResponseVo.<String>BUILDER().setData("").setCode(ResponseVo.BUSINESS_CODE.SUCCESS);
						response.setContentType("application/json;charset=utf-8");
						SessionHelper.setSessionUser(authentication.getPrincipal());
						PrintWriter out = response.getWriter();
						out.write(JSONObject.toJSONString(res));
						out.flush();
						out.close();
					}

				}).failureHandler(new AuthenticationFailureHandler() {
					@Override
					public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
							HttpServletResponse httpServletResponse, AuthenticationException e)
							throws IOException, ServletException {
						httpServletResponse.setContentType("application/json;charset=utf-8");
						ResponseVo<String> res = ResponseVo.<String>BUILDER().setDesc(e.getMessage())
								.setCode(ResponseVo.BUSINESS_CODE.FAILED);
						PrintWriter out = httpServletResponse.getWriter();
						out.write(JSONObject.toJSONString(res));
						out.flush();
						out.close();
					}
				}).loginProcessingUrl("/doLogin") // 自定义的登录接口
				.and().logout().logoutUrl("/logout")
				.and().exceptionHandling().defaultAuthenticationEntryPointFor(ajaxAuthenticationEntryPoint, new RequestMatcher() {
					@Override
				    public boolean matches(HttpServletRequest request) {
				        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
				    }
				}).and();
		
		http.csrf().disable();
		// 容许iframe方式打开窗口
		http.headers().frameOptions().disable();
		// 解决中文乱码问题
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		http.addFilterBefore(filter, CsrfFilter.class);
	}
}
