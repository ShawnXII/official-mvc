package com.official.web.controller;

import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.official.core.exception.LoginSecurityException;

@Component
public class BaseController {
	/**
	 * 用户未登录 返回登录页面
	 * @param runtimeException
	 * @param modelMap
	 * @return
	 */
	@ExceptionHandler(LoginSecurityException.class)
	public String runtimeExceptionHandler(RuntimeException runtimeException,ModelMap modelMap){
		//返回登录
		return "";
	}	
}
