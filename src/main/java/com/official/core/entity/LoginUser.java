package com.official.core.entity;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.official.core.util.Commutil;
import com.official.core.util.PropertiesUtil;
import com.official.foundation.domain.po.user.Account;

/**
 * 登录用户
 * 
 * @author ShawnXII
 * @Version 1.0
 */
public class LoginUser implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4205167688890016005L;
	private static final String[] URL_FORMAT = { "http://", "https://" };
	private String username;

	private Long id;

	private String nickName;

	private String image;

	private String hideName;

	private boolean imageFlag;
	
	public LoginUser() {
		super();
	}

	public LoginUser(Account account) {
		this();
		Assert.notNull(account);
		this.id=account.getId();
		this.username = account.getUsername();
		String nickName = account.getNickname();
		if (StringUtils.isBlank(nickName)) {
			nickName = this.username;
		}
		String image = account.getImage();
		if (StringUtils.isBlank(image)) {
			// 默认图像路径
			image = Commutil.null2String(PropertiesUtil.get("user.image"));
		}
		this.setImageFlag(false);
		if (StringUtils.isBlank(image)) {
			for (String str : URL_FORMAT) {
				if (image.contains(str)) {
					this.setImageFlag(true);
					break;
				}
			}
		}
		this.nickName=nickName;
		this.image=image;
		this.hideName=Commutil.hidename(nickName);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getHideName() {
		return hideName;
	}

	public void setHideName(String hideName) {
		this.hideName = hideName;
	}

	public boolean isImageFlag() {
		return imageFlag;
	}

	public void setImageFlag(boolean imageFlag) {
		this.imageFlag = imageFlag;
	}

	@Override
	public String toString() {
		return "LoginUser [username=" + username + ", id=" + id + ", nickName=" + nickName + ", image=" + image
				+ ", hideName=" + hideName + ", imageFlag=" + imageFlag + "]";
	}

}
