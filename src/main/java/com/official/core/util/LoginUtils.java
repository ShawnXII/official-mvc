package com.official.core.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.official.core.entity.LoginUser;
import com.official.foundation.domain.po.user.Account;

/**
 * 用户登录
 * 
 * @author ShawnXII
 * @Version 1.0
 */
public class LoginUtils {

	private static final String LOGIN_KEY = "login_voucher";
	/**
	 * 登录
	 * @param request
	 * @param account
	 */
	public static void login(HttpServletRequest request, Account account) {
		Assert.notNull(account);
		LoginUser lu = new LoginUser(account);
		String json = JSON.toJSONString(lu);
		SessionTool st = SessionTool.getInstance(request);
		st.set(LOGIN_KEY, json);
	}
	/**
	 * 获取登录用户
	 * @param request
	 * @return
	 */
	public static LoginUser getCurrentuser(HttpServletRequest request){
		SessionTool st = SessionTool.getInstance(request);
		String json=Commutil.null2String(st.get(LOGIN_KEY));
		if(StringUtils.isBlank(json)){
			return null;
		}
		LoginUser lu=JSON.parseObject(json, LoginUser.class);
		if(lu.getId()==null){
			return null;
		}
		return lu;
	}
	/**
	 * 退出登录
	 * @param request
	 */
	public static void loginOut(HttpServletRequest request){
		SessionTool st = SessionTool.getInstance(request);
		st.del(LOGIN_KEY);
	}
	
}
