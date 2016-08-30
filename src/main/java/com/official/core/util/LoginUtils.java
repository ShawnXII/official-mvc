package com.official.core.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSON;
import com.official.core.entity.LoginUser;
import com.official.core.exception.LoginSecurityException;
import com.official.foundation.domain.po.user.Account;

/**
 * 用户登录 设计流程: 用户登录信息存放在redis里面,主要存放 用户信息,用户权限
 * 
 * @author ShawnXII
 * @Version 1.0
 */
public class LoginUtils {

	private static final String LOGIN_KEY = "tooken_voucher";

	private static final String REDIS_LOGIN_KEY = "login_tooken_";
	/**
	 * 用户信息表
	 */
	private static final String REDIS_LOGIN_DATA = "login_data";
	/**
	 * 临时redis表
	 */
	private static final String REDIS_LOGIN_DATA_TEMPORARY = "login_data_temporary";

	private static StringRedisTemplate redisTemplate;

	public static StringRedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

	public static void setRedisTemplate(StringRedisTemplate arguments) {
		LoginUtils.redisTemplate = arguments;
	}

	/**
	 * 生成tooken
	 * 
	 * @param account
	 * @return
	 */
	private static String createTooken(Account account, Long currenTime) {
		Long id = account.getId();
		return Sha256Encrypt.encrypt(currenTime + "" + id);
	}

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		ip = ip.contains(",") ? ip.split(",")[0] : ip;
		return ip;
	}
	/**
	 * 修改帐号
	 * @param account
	 */
	public static void update(Account account){
		BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(REDIS_LOGIN_DATA);
		account.setState(100);
		LoginUser lu=new LoginUser(account);
		String luJson = JSON.toJSONString(lu);
		ops.put(Commutil.null2String(account.getId()), luJson);
	}
	/**
	 * 用户登录
	 * 
	 * @param request
	 * @param response
	 * @param account
	 */
	public static void login(HttpServletRequest request, HttpServletResponse response, Account account) {
		// cookie是否被禁用 false cookie被禁用 navigator.cookieEnabled
		String remember = request.getParameter("remember");
		Long currenTime = System.currentTimeMillis();
		Boolean cookieEnabled = Commutil.null2Boolean(request.getParameter("cookieEnabled"), false);
		String tooken = createTooken(account, currenTime);
		/*
		 * Map<String, Object> map = Maps.newHashMap(); map.put("tooken",
		 * tooken); map.put("createTime", currenTime); String json =
		 * JSON.toJSONString(map);
		 */
		String encoder = tooken;
		try {
			encoder = URLEncoder.encode(tooken, "utf-8");
		} catch (UnsupportedEncodingException e) {
		}
		// 判断是否登录 如果cookie被禁用 则获取session里面 的tooken
		if (cookieEnabled) {
			Cookie[] cookies = request.getCookies();
			boolean flag = false;
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					String name = cookie.getName();
					// cookie 是否过期在
					if (name.equals(LOGIN_KEY)) {
						flag = true;
						cookie.setValue(encoder);
						break;
					}
				}
			}
			if (!flag) {
				// 如果有记住我 则保存
				Cookie cookie = new Cookie(LOGIN_KEY, encoder);
				cookie.setPath("/");
				cookie.setMaxAge(60 * 60 * 24);
				if ("1".equals(remember)) {
					cookie.setMaxAge(30 * 24 * 60 * 60);
				}
				response.addCookie(cookie);
			}
		} else {
			// 存储在session里面 无法用记住我
			HttpSession session = request.getSession(false);
			session.setAttribute(LOGIN_KEY, encoder);
			session.setMaxInactiveInterval(2 * 60 * 60);
		}
		// 把这个唯一key值和帐号ID存入缓存中
		BoundHashOperations<String, String, String> tempOps = redisTemplate.boundHashOps(REDIS_LOGIN_DATA_TEMPORARY);
		// 判断该用户是否已经登录过 如果已经登录 则退出登录 进行当前登录进程
		Map<String, String> map = tempOps.entries();
		Set<Entry<String, String>> set = map.entrySet();
		Iterator<Entry<String, String>> ite = set.iterator();
		boolean flag = false;
		while (ite.hasNext()) {
			Entry<String, String> entry = ite.next();
			String aid = entry.getValue();
			if (aid.equals(Commutil.null2String(account.getId()))) {
				// 退出登陆
				tempOps.delete(entry.getKey());
				flag = true;
				break;
			}
		}
		// 24小时过期
		tempOps.expire(24, TimeUnit.HOURS);
		tempOps.put(REDIS_LOGIN_KEY + tooken, Commutil.null2String(account.getId()));
		// 用户信息,该记录会被持久化

		// IP 临时存放
		account.setRemarks(getIpAddr(request));
		LoginUser lu = new LoginUser(account);
		String luJson = JSON.toJSONString(lu);
		BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(REDIS_LOGIN_DATA);
		ops.put(Commutil.null2String(account.getId()), luJson);
	}

	/**
	 * 获取登录用户
	 * @param request
	 * @return
	 */
	public static LoginUser getCurrentuser(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		boolean flag = false;
		String value = "";
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				String name = cookie.getName();
				if (name.equals(LOGIN_KEY)) {
					flag = true;
					value = cookie.getValue();
					break;
				}
			}
		}
		if (!flag || StringUtils.isBlank(value)) {
			HttpSession session = request.getSession();
			value = Commutil.null2String(session.getAttribute(LOGIN_KEY));
		}
		String tooken = value;
		try {
			tooken = java.net.URLDecoder.decode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new LoginSecurityException(500);
		}
		if (StringUtils.isBlank(tooken)) {
			throw new LoginSecurityException(404);
		}
		BoundHashOperations<String, String, String> tempOps = redisTemplate.boundHashOps(REDIS_LOGIN_DATA_TEMPORARY);
		Long sid = Commutil.null2Long(tempOps.get(REDIS_LOGIN_KEY + tooken));
		if (sid == null || sid < 1) {
			// 清除cookie 和 session
			String str = "";
			for (Cookie cookie : cookies) {
				String name = cookie.getName();
				if (name.equals(LOGIN_KEY)) {
					cookie.setMaxAge(0);
					str = cookie.getValue();
					break;
				}
			}
			// 删除session
			if (StringUtils.isBlank(str)) {
				HttpSession session = request.getSession();
				str = Commutil.null2String(session.getAttribute(LOGIN_KEY));
				session.removeAttribute(LOGIN_KEY);
			}
			throw new LoginSecurityException(405);
		}
		BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(REDIS_LOGIN_DATA);
		String jsonLu = ops.get("" + sid); 
		if (StringUtils.isBlank(jsonLu)) {
			throw new LoginSecurityException(405);
		}
		LoginUser lu = JSON.parseObject(jsonLu, LoginUser.class);
		if (lu == null) {
			throw new LoginSecurityException(500);
		}
		return lu;
	}

	/**
	 * 退出登录
	 * @param request
	 */
	public static void loginOut(HttpServletRequest request) {
		String str = "";
		// 删除cookie
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			String name = cookie.getName();
			if (name.equals(LOGIN_KEY)) {
				str = cookie.getValue();
				cookie.setMaxAge(0);
				break;
			}
		}
		// 删除session
		if (StringUtils.isBlank(str)) {
			HttpSession session = request.getSession();
			str = Commutil.null2String(session.getAttribute(LOGIN_KEY));
			session.removeAttribute(LOGIN_KEY);
		}
		String json = str;
		try {
			json = java.net.URLDecoder.decode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
		}
		if (StringUtils.isBlank(json)) {
			return;
		}
		Map<?, ?> map = JSON.parseObject(json, Map.class);
		if (map != null) {
			String tooken = Commutil.null2String(map.get("tooken"));
			// 删除redis
			BoundHashOperations<String, String, String> tempOps = redisTemplate
					.boundHashOps(REDIS_LOGIN_DATA_TEMPORARY);
			tempOps.delete(REDIS_LOGIN_KEY + tooken);
		}
	}

}
