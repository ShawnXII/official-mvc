package com.official.core.entity;

public class Config implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1488189100781207187L;
	// 页面描述
	private String description;
	// 公司名称
	private String corporateName;
	// 作者
	private String author;
	// SEO
	private String keyWords;
	// 图标
	private String favicon;
	// INC
	private String inc;
	// 图片服务器地址
	private String imagePath;
	// 项目地址
	private String contextPath;
	// 静态资源地址
	private String resourcePath;

	private String webLogo;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCorporateName() {
		return corporateName;
	}

	public void setCorporateName(String corporateName) {
		this.corporateName = corporateName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public String getFavicon() {
		return favicon;
	}

	public void setFavicon(String favicon) {
		this.favicon = favicon;
	}



	public String getInc() {
		return inc;
	}

	public void setInc(String inc) {
		this.inc = inc;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public String getWebLogo() {
		return webLogo;
	}

	public void setWebLogo(String webLogo) {
		this.webLogo = webLogo;
	}
}
