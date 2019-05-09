package org.september.simpleweb.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.september.simpleweb.model.ResponseVo;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

@Component
public class AjaxAuthenticationEntryPoint implements AuthenticationEntryPoint{

	@Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
		ResponseVo<String> respVo = ResponseVo.<String> BUILDER().setDesc("用户信息超时，请重新登录").setCode(ResponseVo.BUSINESS_CODE.FAILED);
        response.setContentType("application/json;charset=UTF-8");
	    response.getWriter().print(JSON.toJSONString(respVo));
	    response.flushBuffer();
    }
}
