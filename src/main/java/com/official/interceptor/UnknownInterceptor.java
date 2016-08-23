package com.official.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.official.core.entity.LoginUser;
import com.official.core.util.LoginUtils;
/**
 * 登陆页面拦截器 已经登陆 无法走登陆流程
 * @author ShawnXII
 * @Version 1.0
 */
public class UnknownInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		LoginUser lu=LoginUtils.getCurrentuser(request);
		if(lu!=null){
			String contextPath=request.getContextPath();
			response.sendRedirect(contextPath+"/admin/index.htm");
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
