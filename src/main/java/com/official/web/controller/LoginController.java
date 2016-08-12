package com.official.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 登录/注册/找回密码相关页面和接口
 * @author ShawnXII
 * @Version 1.0
 */
@Controller
@RequestMapping("/login")
public class LoginController extends BaseController{
	
	/**
	 * 登录页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/login.htm",method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView toLogin(HttpServletRequest request){
		ModelAndView view=new ModelAndView();
		view.setViewName("/login");
		return view;
	}
	
	@RequestMapping(value = "/toLogin.htm",method = RequestMethod.POST)
	public String login(HttpServletRequest request){
		return "";
	}
	
	
}
