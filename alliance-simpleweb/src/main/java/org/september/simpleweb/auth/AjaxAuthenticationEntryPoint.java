package org.september.simpleweb.auth;

import org.springframework.stereotype.Component;

@Component
public class AjaxAuthenticationEntryPoint
//implements AuthenticationEntryPoint
{

//	@Override
//    public void commence(HttpServletRequest request, HttpServletResponse response,
//            AuthenticationException authException) throws IOException, ServletException {
//		ResponseVo<String> respVo = ResponseVo.<String> BUILDER().setDesc("用户信息超时，请重新登录").setCode(ResponseVo.BUSINESS_CODE.FAILED);
//		request.getSession().setMaxInactiveInterval(Integer.MAX_VALUE);
//		if(request.getRequestedSessionId()!=null && !request.getRequestedSessionId().equals(request.getSession().getId())) {
//			Cookie cookie = new Cookie("JLastSessionId",request.getRequestedSessionId());
//			cookie.setPath("/");
//			cookie.setMaxAge(Integer.MAX_VALUE);
//			response.addCookie(cookie);
//		}
//		//System.out.println("session timeout,old sessionid = "+request.getRequestedSessionId()+",set new cookie. "+response.getHeader("Set-Cookie"));
//		response.setContentType("application/json;charset=UTF-8");
//	    response.getWriter().print(JSON.toJSONString(respVo));
//	    response.flushBuffer();
//    }
}
