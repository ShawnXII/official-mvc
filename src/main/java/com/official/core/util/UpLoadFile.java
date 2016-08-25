package com.official.core.util;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.official.core.entity.LoginUser;
import com.official.core.entity.Upload;

public class UpLoadFile {

	private static Logger log = LoggerFactory.getLogger(UpLoadFile.class);
	private static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM");

	public static String uploadFile(List<Upload> list) {
		// 开启线程池 来进行多个图片的上传工作
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
		if (list != null && list.size() > 0) {
			for (final Upload upload : list) {
				fixedThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						insert(upload);
					}
				});
			}
		}
		return null;
	}

	private static void insert(Upload upload) {
		String date = FORMAT.format(new Date());
		String path = "";// 图片上传路径
		String newFileName = upload.getNewFileName();
		if ("system".equals(upload.getType())) {
			path = upload.getAddress() + "/system/" + date;
		} else {
			path = upload.getAddress() + "/" + upload.getUserId() + "/" + date;
		}
		File fileDir = new File(path);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		if(upload.getIsRetain()){
			FileOutputStream out=null;	
			try {
				out = new FileOutputStream(path + "/" + newFileName);
				out.write(upload.getBytes());
				out.flush();
				out.close();
			} catch (IOException e) {

			}finally{
				if(out!=null){
					try {
						out.close();
					} catch (IOException e) {
			
					}
				}
			}
			
		}
		// 切割图片
		boolean cutting = upload.getIsCutting();
		if (cutting) {
			BufferedImage bi = cut(upload);
			try {
				ImageIO.write(bi, upload.getExt(), new File(path + "/" + upload.getId() + "_cut." + upload.getExt()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//是否压缩
		if(upload.getIsCompress()){
			
		}

	}

	public static BufferedImage cut(Upload upload) {
		Integer pointX = upload.getPointX();
		Integer pointY = upload.getPointY();
		InputStream is = upload.getInputStream();
		ImageInputStream iis = null;
		try {
			// 获取图片流
			iis = ImageIO.createImageInputStream(is);
			// 返回包含所有当前已注册 ImageReader 的 Iterator，这些 ImageReader 声称能够解码指定格式。
			// 参数：formatName - 包含非正式格式名称 . (例如 "jpeg" 或 "tiff")等 。
			Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(upload.getExt());
			ImageReader reader = it.next();
			// iis:读取源。true:只向前搜索 .将它标记为 ‘只向前搜索’。
			// 此设置意味着包含在输入源中的图像将只按顺序读取，可能允许 reader
			// 避免缓存包含与以前已经读取的图像关联的数据的那些输入部分。
			reader.setInput(iis, true);
			// 描述如何对流进行解码的类 用于指定如何在输入时从 Java Image I/O
			// 框架的上下文中的流转换一幅图像或一组图像。用于特定图像格式的插件 将从其 ImageReader 实现的
			// getDefaultReadParam 方法中返回 ImageReadParam 的实例。
			ImageReadParam param = reader.getDefaultReadParam();
			int weight = upload.getWeight();
			int height = upload.getHeight();
			int cweight = upload.getCweight();
			int cheight = upload.getCheight();
			if (cweight < 1) {
				cweight = weight - pointX;
			}
			if (cheight < 1) {
				cheight = height - pointY;
			}
			// 图片裁剪区域。Rectangle 指定了坐标空间中的一个区域，通过 Rectangle 对象
			// 的左上顶点的坐标(x，y)、宽度和高度可以定义这个区域。
			Rectangle rect = new Rectangle(pointX, pointY, cweight, cheight);
			// 提供一个 BufferedImage，将其用作解码像素数据的目标。
			param.setSourceRegion(rect);
			// 使用所提供的 ImageReadParam 读取通过索引 imageIndex 指定的对象，并将 它作为一个完整的
			// BufferedImage 返回。
			BufferedImage bi = reader.read(0, param);
			return bi;
		} catch (IOException e) {
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
			if (iis != null) {
				try {
					iis.close();
				} catch (IOException e) {
				}
			}
		}
	}

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

	private static void cut(MultipartFile multipartFile, String path, String fileName, int width, int height) {
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
						fileName = path + fileName + "_" + i + "_" + j + ".jpg";

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
	 * 
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
	 * 
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
