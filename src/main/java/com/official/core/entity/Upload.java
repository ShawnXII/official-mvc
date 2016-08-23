package com.official.core.entity;

/**
 * 图片上传
 * @author ShawnXII
 * @Version 1.0
 */
public class Upload {
	
	//旧的图片ID 根据状态来决定是否删除旧照片
	private Long oldId;
	
	//图片名称
	private String name;
	
	//是否保留 旧照片
	private Boolean isRetain=true;
	
	//是否切割 默认不切割
	private Boolean isCutting=false;
	
	//是否压缩 默认压缩
	private Boolean isCompress=true;
	
	//是否保存原图
	private Boolean original;
	
	//图片类型 system(系统图片) personal(个人图像)
	private String type;
	
	//用户ID
	private Long userId;
	
	
}
