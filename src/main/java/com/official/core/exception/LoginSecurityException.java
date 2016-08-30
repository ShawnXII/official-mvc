package com.official.core.exception;
/**
 * 用户登录错误
 * @author ShawnXII
 * @Version 1.0
 */
public class LoginSecurityException extends SecurityException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7624947974452419032L;
	//未登录 :404(默认) 405:退出登录  500:登录异常
	private Integer code;
	
	public LoginSecurityException(){
		super();
		this.code=404;
	}
	
	public LoginSecurityException(String msg){
		super(msg);
	}
	
	public LoginSecurityException(Integer code){
		super();
		this.code=code;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
	
	
}
