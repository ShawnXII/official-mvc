package com.official.web.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;
import com.official.core.util.Commutil;
import com.official.core.util.PropertiesUtil;

/**
 * 公共的ajax接口
 * 
 * @author ShawnXII
 * @Version 1.0
 */
@Controller
@RequestMapping("/common")
public class CommonController extends BaseController {

	/**
	 * 图片上传
	 * 
	 * @param request
	 * @param multipartFile
	 * @return
	 */
	@RequestMapping(value = "/uploadFile.htm", method = RequestMethod.POST)
	public Map<String, Object> uploadFile(HttpServletRequest request,
			@RequestParam(value = "avatar_file", required = false) MultipartFile multipartFile) {
		String avatar_data=request.getParameter("avatar_data");
		String avatar_src=request.getParameter("avatar_src");
		 Map<String, Object> map=Maps.newHashMap();
		 map.put("flag",false);
		if (multipartFile != null && !multipartFile.isEmpty()) {
			// 文件名称
			String fileName = multipartFile.getOriginalFilename();
			// 获取图片的扩展名
			String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
			Long id = Commutil.randomId();
			// 新的图片名称 随机生成的ID+扩展名
			String newFileName = String.valueOf(id) + "." + extensionName;
			if(saveFile(multipartFile,newFileName,map)){
				map.put("flag",true);
			}
		}
		return map;
	}

	private boolean saveFile(MultipartFile multipartFile, String newFileName,Map<String, Object> map) {
		// 读取文件服务器地址 本地D盘
		String path = Commutil.null2String(PropertiesUtil.get("image.path"));
		File fileDir = new File(path);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		FileOutputStream out=null;
		try {
			 out = new FileOutputStream(path + "\\" + newFileName);
			 out.write(multipartFile.getBytes());
			 out.flush();
			 return true;
		} catch (FileNotFoundException e) {
			map.put("errorMsg", e.getMessage());
		} catch (IOException e) {
			map.put("errorMsg", e.getMessage());
		}finally{
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return false;
	}
}
