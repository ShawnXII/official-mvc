package com.official.interceptor;

import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
/**
 * 资源拦截器
 * @author ShawnXII
 * @Version 1.0
 */
public class ResourceInterceptor implements WebRequestInterceptor {

	@Override
	public void preHandle(WebRequest request) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(WebRequest request, ModelMap model) throws Exception {
		if(model!=null){
			model.containsKey("");
			System.out.println("123");
		}
	}

	@Override
	public void afterCompletion(WebRequest request, Exception ex) throws Exception {
		// TODO Auto-generated method stub
		
	}


}
