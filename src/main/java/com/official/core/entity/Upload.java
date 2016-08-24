package com.official.core.entity;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.official.core.util.Commutil;
import com.official.core.util.PropertiesUtil;

/**
 * 图片上传
 * 
 * @author ShawnXII
 * @Version 1.0
 */
public class Upload implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4272411546003193742L;
	
	private Long id;
	// 旧的图片ID 根据状态来决定是否删除旧照片
	private Long oldId;

	// 图片名称 原图名称
	private String name;

	private String originalFilename;

	// 是否保留 旧照片
	private Boolean isRetain = true;

	// 是否切割 默认不切割
	private Boolean isCutting = false;

	// 是否压缩 默认压缩
	private Boolean isCompress = true;

	// 是否保存原图
	private Boolean original = true;
	
	//是否设置水印
	private Boolean watermark=false;

	// 图片类型 system(系统图片) personal(个人图像)
	private String type;

	// 用户ID
	private Long userId;
	//切点X坐标
	private Integer pointX;
	//切点Y坐标
	private Integer pointY;
	//剪切点宽度
	private Integer cweight;
	//剪切点高度
	private Integer cheight;
	// 图片宽度(原图)
	private Integer weight;

	// 图片高度(原图)
	private Integer height;

	// 图片后缀
	private String ext;

	private InputStream inputStream;

	private Long size;

	private byte[] bytes;

	private BufferedImage bi;

	// 标准
	private String standard;
	
	private String address;
	
	private String newFileName;
	
	public Upload(){	
		super();
		this.id=Commutil.generateId();
		this.address= Commutil.null2String(PropertiesUtil.get("image.uploadPath"));
	}
	/**
	 * 
	 * @param multipartFile
	 * @throws IOException
	 */
	public Upload(MultipartFile multipartFile) throws IOException {
		this();
		if (multipartFile == null) {
			throw new IOException("multipartFile is null");
		}
		this.inputStream = multipartFile.getInputStream();
		this.bytes = multipartFile.getBytes();
		this.bi = ImageIO.read(this.inputStream);
		this.size = multipartFile.getSize();
		this.height = this.bi.getHeight();
		this.weight = this.bi.getWidth();
		this.name = multipartFile.getName();
		this.originalFilename = multipartFile.getOriginalFilename();
		// 获取后缀
		this.ext = this.originalFilename.substring(this.originalFilename.lastIndexOf(".") + 1);
		this.newFileName=this.id+"."+ext;
		//获取登录用户 
	}

	/**
	 * 参数:<br>
	 * id:旧照片ID<br>
	 * retain:是否保留旧照片(只有存在id时该字段才会有效) 默认true <br>
	 * standard:标准 可以 宽_高 的格式上传,系统会自动判定这种参数 默认default <br>
	 * pointX:图片切点X坐标  <br>
	 * pointY:图片切点Y坐标  <br>
	 * weight:图片切点宽度  <br>
	 * height:图片切点高度   <br>
	 * compress:是否压缩图片 默认true<br>
	 * original:是否保留原图 默认保留<br>
	 * watermark:是否设置水印<br>
	 * @param multipartFile
	 * @param request
	 * @throws IOException
	 */
	public Upload(MultipartFile multipartFile, HttpServletRequest request) throws IOException {
		this(multipartFile);
		// 标准:图片切割压缩大小标准(配置文件维护这个标准) 标准有 用户
		// user,展位xxx_xxx(后面代表展位需要的大小)stand100x100,logoxxx_xxx,品牌,商品(该标准是切割成多个图片)goods,分类(压缩图片)goodsClass,菜单
		// menu,default(默认,不做处理)
		String standard = Commutil.null2String(request.getParameter("standard"), "default");
		// logo 分类 菜单 都是属于系统图片(如果确认不了 则评定图片为个人图片)
		this.standard = standard;
		Long oid = Commutil.null2Long(request.getParameter("id"), -1);
		if (oid > 0) {
			this.oldId = oid;
			Boolean isRetain = Commutil.null2Boolean(request.getParameter("retain"), true);
			this.isRetain = isRetain;
		}
		this.isCutting =false;
		Integer pointX=Commutil.null2Int(request.getParameter("pointX"), 0);		
		Integer pointY=Commutil.null2Int(request.getParameter("pointY"), 0);
		Integer cweight=Commutil.null2Int(request.getParameter("weight"), 0);
		Integer cheight=Commutil.null2Int(request.getParameter("height"), 0);
		this.cweight=cweight;
		this.cheight=cheight;
		if(pointX>0&&pointY>0&&pointY<=this.height&&pointX<=this.weight){
			this.pointX=pointX;
			this.pointY=pointY;
			this.isCutting=true;
		}
		// 是否压缩
		this.isCompress = Commutil.null2Boolean(request.getParameter("compress"), true);
		// 是否保留原图
		this.original = Commutil.null2Boolean(request.getParameter("original"), true);
		//是否设置水印
		this.setWatermark(Commutil.null2Boolean(request.getParameter("watermark"), false));
	}

	public BufferedImage getBi() {
		return bi;
	}

	public void setBi(BufferedImage bi) {
		this.bi = bi;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public Long getOldId() {
		return oldId;
	}

	public void setOldId(Long oldId) {
		this.oldId = oldId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsRetain() {
		return isRetain;
	}

	public void setIsRetain(Boolean isRetain) {
		this.isRetain = isRetain;
	}

	public Boolean getIsCutting() {
		return isCutting;
	}

	public void setIsCutting(Boolean isCutting) {
		this.isCutting = isCutting;
	}

	public Boolean getIsCompress() {
		return isCompress;
	}

	public void setIsCompress(Boolean isCompress) {
		this.isCompress = isCompress;
	}

	public Boolean getOriginal() {
		return original;
	}

	public void setOriginal(Boolean original) {
		this.original = original;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getOriginalFilename() {
		return originalFilename;
	}

	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getNewFileName() {
		return newFileName;
	}
	public void setNewFileName(String newFileName) {
		this.newFileName = newFileName;
	}
	public Boolean getWatermark() {
		return watermark;
	}
	public void setWatermark(Boolean watermark) {
		this.watermark = watermark;
	}
	public Integer getPointX() {
		return pointX;
	}
	public void setPointX(Integer pointX) {
		this.pointX = pointX;
	}
	public Integer getPointY() {
		return pointY;
	}
	public void setPointY(Integer pointY) {
		this.pointY = pointY;
	}
	public Integer getCweight() {
		return cweight;
	}
	public void setCweight(Integer cweight) {
		this.cweight = cweight;
	}
	public Integer getCheight() {
		return cheight;
	}
	public void setCheight(Integer cheight) {
		this.cheight = cheight;
	}

}
