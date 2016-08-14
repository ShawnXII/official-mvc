package com.official.core.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.official.core.entity.LoginUser;

public class UpLoadFile {

	private static Logger log = LoggerFactory.getLogger(UpLoadFile.class);
	private static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM");

	public static String upload(MultipartFile multipartFile, HttpServletRequest request) {
		if (multipartFile != null && !multipartFile.isEmpty()) {
			// 文件名称
			String fileName = multipartFile.getOriginalFilename();
			// 获取图片的扩展名
			String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
			Long id = Commutil.randomId();
			// 新的图片名称 随机生成的ID+扩展名
			LoginUser lu = LoginUtils.getCurrentuser(request);
			String newFileName = String.valueOf(id) + "." + extensionName;
			String date = FORMAT.format(new Date());
			String sub = lu.getUsername() + "/" + date;
			return saveFile(multipartFile, newFileName, sub);
		}
		return "";
	}

	private static String saveFile(MultipartFile multipartFile, String newFileName, String sub) {
		// 读取文件服务器地址
		String path = Commutil.null2String(PropertiesUtil.get("image.uploadPath"));
		if (StringUtils.isNotBlank(sub)) {
			path = path + "/" + sub;
		}
		File fileDir = new File(path);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		FileOutputStream out = null;
		try {
			path = path + "/" + newFileName;
			out = new FileOutputStream(path);
			out.write(multipartFile.getBytes());
			out.flush();
			if (StringUtils.isNotBlank(sub)) {
				// 切割图片 把图片放入到Accessory中
				
				return "/" + sub + "/" + newFileName;
			}
			return "/" + newFileName;
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return "";
	}

	private static void cut(MultipartFile multipartFile,String path,String fileName,int width, int height) {
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(multipartFile.getInputStream());
			int sWidth = bi.getWidth();
			int sHeight = bi.getHeight();
			if (sWidth > width && sHeight > height) {
				int cols = 0; // 横向切片总数
				int rows = 0; // 纵向切片总数
				int eWidth = 0; // 末端切片宽度
				int eHeight = 0; // 末端切片高度
				if (sWidth % width == 0) {
					cols = sWidth / width;
				} else {
					eWidth = sWidth % width;
					cols = sWidth / width + 1;
				}
				if (sHeight % height == 0) {
					rows = sHeight / height;
				} else {
					eHeight = sHeight % height;
					rows = sHeight / height + 1;
				}
				BufferedImage image = null;
				int cWidth = 0; // 当前切片宽度
				int cHeight = 0; // 当前切片高度
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < cols; j++) {
						cWidth = getWidth(j, cols, eWidth, width);
						cHeight = getHeight(i, rows, eHeight, height);
						// x坐标,y坐标,宽度,高度
						image = bi.getSubimage(j * width, i * height, cWidth, cHeight);
						fileName = path+fileName + "_" + i + "_" + j + ".jpg";
						
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取当前切片宽度
	 * @param index
	 * @param cols
	 * @param endWidth
	 * @param width
	 * @return
	 */
	private static int getWidth(int index, int cols, int endWidth, int width) {
		if (index == cols - 1) {
			if (endWidth != 0) {
				return endWidth;
			}
		}
		return width;
	}
	/**
	 * 获取当前切片高度
	 * @param index
	 * @param rows
	 * @param endHeight
	 * @param height
	 * @return
	 */
	private static int getHeight(int index, int rows, int endHeight, int height) {  
        if (index == rows - 1) {  
            if (endHeight != 0) {  
                return endHeight;  
            }  
        }  
        return height;  
    }  

}
