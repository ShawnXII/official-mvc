package com.official.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.official.core.entity.LoginUser;
import com.official.core.util.LoginUtils;
/**
 * 登录拦截器 
 * @author ShawnXII
 * @Version 1.0
 */
public class SecurityInterceptor implements HandlerInterceptor{
	/*
	 * 拦截器
	 * (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		LoginUser lu=LoginUtils.getCurrentuser(request);
		if(lu==null){
			String contextPath=request.getContextPath();
			response.sendRedirect(contextPath+"/login/login.htm");
			return false;
		}
		return true;
	}
	/**
	 * 
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if(modelAndView!=null){
			LoginUser lu=LoginUtils.getCurrentuser(request);
			modelAndView.addObject("loginUser", lu);
		}
		
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	
	}

}
