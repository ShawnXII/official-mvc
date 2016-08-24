package com.official.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.official.core.entity.Upload;
import com.official.foundation.facade.system.AccessoryFacadeService;

public class SystemViewController extends BaseController {

	@Autowired
	private AccessoryFacadeService accessoryService;

	// 上传图片接口 可以上传多个
	public void uploadFile(HttpServletRequest request, @RequestParam("files") MultipartFile[] multipartFiles) {
		List<Upload> list=Lists.newArrayList();
		for(MultipartFile multipartFile:multipartFiles){
			try {
				list.add(new Upload(multipartFile,request));
			} catch (IOException e) {
			}
		}
		
		
	}
}
