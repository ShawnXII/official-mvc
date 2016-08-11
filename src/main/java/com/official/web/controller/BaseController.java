package com.official.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Component
public class BaseController {
	/**
	 * 错误拦截
	 * @param request
	 * @param response
	 * @param ex
	 */
	@ExceptionHandler
	public void exp(HttpServletRequest request,HttpServletResponse response, Exception ex){
		
	}
}
