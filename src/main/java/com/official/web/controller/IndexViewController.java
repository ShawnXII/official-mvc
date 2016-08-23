package com.official.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.official.core.entity.LoginUser;
import com.official.core.util.Commutil;
import com.official.core.util.LoginUtils;
import com.official.core.util.UpLoadFile;
import com.official.foundation.domain.po.user.Account;
import com.official.foundation.facade.user.AccountFacadeService;

@Controller
@RequestMapping("/admin")
public class IndexViewController extends BaseController {
	@Autowired
	private AccountFacadeService accountService;
	
	@RequestMapping(value = { "/demo.htm" }, method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView Demo(HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		view.setViewName("/image/demo");
		view.addObject("title", "测试图片");		
		return view;
	}
	/**
	 * 个人信息主页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "/index.htm", "/" }, method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView toIndex(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.setViewName("/persional/persional");
		view.addObject("title", "我的资料");
		LoginUser lu = LoginUtils.getCurrentuser(request);
		Account account = accountService.findOne(lu.getId());
		view.addObject("account", account);
		return view;
	}

	/**
	 * 修改账号
	 * 
	 * @param request
	 * @param account
	 * @return
	 */
	@RequestMapping(value = "/updateAccount.htm", method = RequestMethod.POST)
	public String updateAccount(HttpServletRequest request, RedirectAttributes attr) {
		LoginUser lu = LoginUtils.getCurrentuser(request);
		Account account = accountService.findOne(lu.getId());
		String nickname = Commutil.null2String(request.getParameter("nickname"));
		account.setNickname(nickname);
		String remarks = Commutil.null2String(request.getParameter("remarks"));
		account.setRemarks(remarks);
		String mobile1 = request.getParameter("mobile");
		if (Commutil.isMobile(mobile1)) {
			String mobile = account.getMobile();
			boolean flag=false;
			if (StringUtils.isNotBlank(mobile)) {
				if (!mobile.equals(mobile1)) {
					// 修改了手机号
					flag=true;
				}
			}
			account.setMobile(mobile1);
			if(flag){
				account.setMobileState(1);
			}
		}
		String email1=request.getParameter("email");
		if(Commutil.isEmail(email1)){
			String email = account.getEmail();
			boolean flag=false;
			if (StringUtils.isNotBlank(email)) {
				if (!email.equals(email1)) {
					// 修改了邮箱
					flag=true;
				}
			}
			account.setEmail(email1);
			if(flag){
				account.setEmailState(1);
			}
		}
		attr.addFlashAttribute("msg", "修改成功");
		LoginUtils.login(request, this.accountService.updateAccount(account));
		return "redirect:/admin/index.htm";
	}
	/**
	 * 修改个人照
	 * @return
	 */
	@RequestMapping(value = "/updateAccountImage.htm", method = RequestMethod.POST)
	public String updateImage(HttpServletRequest request,@RequestParam("file") MultipartFile multipartFile,RedirectAttributes attr){
		LoginUser lu = LoginUtils.getCurrentuser(request);
		String path=UpLoadFile.upload(multipartFile,request);
		if(StringUtils.isNotBlank(path)){
			Account account = accountService.findOne(lu.getId());
			account.setImage(path);
			attr.addFlashAttribute("msg", "修改成功");
			LoginUtils.login(request, this.accountService.updateAccount(account));
			return "redirect:/admin/index.htm";
		}	
		attr.addFlashAttribute("msg", "修改图片失败");
		return "redirect:/admin/index.htm";
	}
	
}
