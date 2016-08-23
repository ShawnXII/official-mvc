package com.official.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.official.core.util.Commutil;
import com.official.core.util.LoginUtils;
import com.official.foundation.domain.po.user.Account;
import com.official.foundation.facade.user.AccountFacadeService;

/**
 * 登录/注册/找回密码相关页面和接口
 * 
 * @author ShawnXII
 * @Version 1.0
 */
@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {

	@Autowired
	private AccountFacadeService accountService;

	/**
	 * 登录页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "/login.htm", "/" }, method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView toLogin(HttpServletRequest request, @ModelAttribute("errorMsg") String errorMsg) {
		ModelAndView view = new ModelAndView();
		view.setViewName("/login");
		view.addObject("title", "登录");
		view.addObject("errorMsg", errorMsg);
		return view;
	}
	
	/**
	 * 登陆
	 * 
	 * @param request
	 * @param attr
	 * @return
	 */
	@RequestMapping(value = "/toLogin.htm", method = RequestMethod.POST)
	public String login(HttpServletRequest request, RedirectAttributes attr) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		Account account = accountService.getAccountByUsername(username);
		if (account != null) {
			String salt = account.getSalt();
			String findPassword = Commutil.encrypt(password, salt);
			String password1 = account.getPassword();
			if (findPassword.equals(password1)) {
				String redirectUrl = "redirect:/admin/index.htm";
				// 把账号密码存入session中
				LoginUtils.login(request, account);
				return redirectUrl;
			}
		}
		attr.addFlashAttribute("errorMsg", "账号不存在或者密码错误!");
		return "redirect:/login/login.htm";
	}

	/**
	 * 退出登陆
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/loginOut.htm", method = { RequestMethod.POST })
	public String loginOut(HttpServletRequest request) {
		LoginUtils.loginOut(request);
		return "redirect:/login/login.htm";
	}

	/**
	 * 找回密码页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/forgotPassword.htm")
	public ModelAndView forgotPassword(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		return view;
	}

	/**
	 * 注册页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/register.htm")
	public ModelAndView register(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.setViewName("/register");
		view.addObject("title", "用户注册");
		return view;
	}

	/**
	 * 用户注册
	 * @param request
	 * @param username
	 * @param password
	 */
	@RequestMapping(value = "/doRegister.htm", method = { RequestMethod.POST })
	public String doRegister(HttpServletRequest request, @RequestParam(required = true) String username,
			@RequestParam(required = true) String password,RedirectAttributes attr) {
		Account account=new Account();
		String salt=Commutil.getSalt();
		account.setSalt(salt);
		String finepassword=Commutil.encrypt(password, salt);
		account.setPassword(finepassword);
		account.setUsername(username);
		this.accountService.register(account);
		//登录
		return this.login(request, attr);
	}
	
	
}
