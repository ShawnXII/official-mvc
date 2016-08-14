package com.official.interceptor;

import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import com.official.core.entity.Config;
import com.official.core.util.Commutil;
import com.official.foundation.facade.system.SystemConfigFacadeService;
/**
 * 资源拦截器
 * @author ShawnXII
 * @Version 1.0
 */
public class ResourceInterceptor implements WebRequestInterceptor {
	
	private SystemConfigFacadeService systemConfigService;
	
	@Override
	public void preHandle(WebRequest request) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(WebRequest request, ModelMap model) throws Exception {
		if(model!=null&&!Commutil.null2Boolean(model.get("resourcesFlag"))){
			//加载系统资源 
			Config config=new Config();
			config.setAuthor("ShawnXII");
			config.setKeyWords("");
			config.setCorporateName("欧沃泉");
			config.setImagePath("http://127.0.0.1/resources");
			config.setResourcePath("/resources");
			config.setContextPath("/resources");
			model.addAttribute("config", config);
		}
	}

	@Override
	public void afterCompletion(WebRequest request, Exception ex) throws Exception {
		// TODO Auto-generated method stub
		
	}


}
