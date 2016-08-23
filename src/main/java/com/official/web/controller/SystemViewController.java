package com.official.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.official.foundation.facade.system.AccessoryFacadeService;

public class SystemViewController extends BaseController {

	@Autowired
	private AccessoryFacadeService accessoryService;

	// 上传图片接口 可以上传多个
	public void uploadFile(HttpServletRequest request, @RequestParam("files") MultipartFile[] multipartFile) {
		Long lodId = null;
		// 上传图片(线程处理,考虑到可以上传多张)
		// 判断是否已经登录 没有登录 则不能上传图片
		// 是否保留旧的照片(默认保留) isRetain 
		// 是否需要切割图片 如果需要切割图片 切割图片的参数是写死的  isCutting
		// 是否压缩图片
		// 是否保存原图
		// 图片类型(系统,个人) 个人图片需要保存到个人相册 系统图片则统一保存到系统相册里面
		// 分析图片hashCode值(原图) 并保存
		// 图片命名规则 切割后的图片以 path_宽_高.后缀 命名
		
	}
}
