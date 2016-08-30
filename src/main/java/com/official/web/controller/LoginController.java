package com.official.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
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
	 * 用户名或者密码错误
	 */
	private static final String ERROR_USER_PWD = "ERROR_USER_PWD";
	/**
	 * 手机号码未通过验证
	 */
	private static final String ERROR_MOBILE_STATE = "ERROR_MOBILE_STATE";
	/**
	 * 邮箱未验证
	 */
	private static final String ERROR_EMAIL_STATE = "ERROR_EMAIL_STATE";
	/**
	 * 帐号被禁用
	 */
	private static final String ERROR_USER_FORBIDDEN = "ERROR_USER_FORBIDDEN";
	/**
	 * 帐号被锁定
	 */
	private static final String ERROR_USER_LOCKING = "ERROR_USER_LOCKING";

	/**
	 * 登录页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/login.htm", method = RequestMethod.GET)
	public String loginView(Model model) {
		model.addAttribute("title", "登录");
		return "/login";
	}

	/**
	 * 登录动作
	 * 
	 * @param attr
	 * @return
	 */
	@RequestMapping(value = "/toLogin.htm", method = RequestMethod.POST)
	public String loginAction(HttpServletRequest request, HttpServletResponse response, RedirectAttributes attr,
			String username, String password) {
		try {
			// 验证帐号是否正常
			Account account = accountService.getAccountByUsername(username);
			// 错误码 ERROR_USER_PWD:用户名或者密码错误 ERROR_MOBILE_STATE:手机未通过验证
			// ERROR_EMAIL_STATE:邮箱未通过验证
			Assert.notNull(account, ERROR_USER_PWD);
			// 判断帐号是 手机号/邮箱/帐号 登陆
			String mobile = account.getMobile();
			String email = account.getEmail();
			if (username.equals(mobile)) {
				// 手机号吗登陆
				Assert.isTrue(account.getMobileState() == 1, ERROR_MOBILE_STATE);
			}
			if (username.equals(email)) {
				Assert.isTrue(account.getEmailState() == 1, ERROR_EMAIL_STATE);
			}
			String salt = account.getSalt();
			String findPassword = Commutil.encrypt(password, salt);
			String pwd = account.getPassword();
			Assert.isTrue(pwd.equals(findPassword), ERROR_USER_PWD);
			// 判断帐号状态
			Integer state = account.getState();
			Assert.isTrue(state == 1, ERROR_USER_FORBIDDEN);
			Assert.isTrue(state == 2, ERROR_USER_LOCKING);
			LoginUtils.login(request, response, account);
			return "redirect:/admin/index.htm";
		} catch (Exception e) {
			
		}
		return "";
	}

	/**
	 * 登陆
	 * 
	 * @param request
	 * @param attr
	 * @return
	 */
	@RequestMapping(value = "/toLogin.htm", method = RequestMethod.POST)
	public String login(HttpServletRequest request, HttpServletResponse response, RedirectAttributes attr) {
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
				LoginUtils.login(request, response, account);
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
	 * 
	 * @param request
	 * @param username
	 * @param password
	 */
	@RequestMapping(value = "/doRegister.htm", method = { RequestMethod.POST })
	public String doRegister(HttpServletResponse response, HttpServletRequest request,
			@RequestParam(required = true) String username, @RequestParam(required = true) String password,
			RedirectAttributes attr) {
		Account account = new Account();
		String salt = Commutil.getSalt();
		account.setSalt(salt);
		String finepassword = Commutil.encrypt(password, salt);
		account.setPassword(finepassword);
		account.setUsername(username);
		this.accountService.register(account);
		// 登录
		return this.login(request, response, attr);
	}

}
