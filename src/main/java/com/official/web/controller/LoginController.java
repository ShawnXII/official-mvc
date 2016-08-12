package com.official.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;

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
	
	@RequestMapping(value = "/toLogin.htm",method = {RequestMethod.GET,RequestMethod.POST})
	public String login(HttpServletRequest request, RedirectAttributes attr){
		attr.addAttribute("test", "aaa");
		return "redirect:/login/login.htm";
	}
	
	@RequestMapping(value = "/ajax.htm",method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Map<String,Object> ajax(HttpServletRequest request){
		Map<String,Object> map=Maps.newHashMap();
		map.put("test", "测试ajax");		
		return map;
	}
}
