package com.official.core.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.persistence.jpa.jpql.Assert.AssertException;

import com.google.common.collect.Maps;
import com.official.core.base.entity.BaseEntity;
import com.official.core.base.entity.Entity;

/**
 * 绑定工具类
 * 主要用于绑定request的参数工具类
 * @author ShawnXII
 * @Version 1.0
 */
public class Binding {
	// 搜索列
	private static final String SEARCH_REG = "^search_[A-Za-z0-9]{1,}$";

	private static final String SORT_REG = "^sort_[A-Za-z0-9]{1,}$";

	/**
	 * 查询方法 定义一个通用规则 所有的搜索列用[name=search_xxx] xxx 就是实体类里面的列 排序列[name=sort_xxx]
	 * xxx 就是实体类里面的列 默认是搜索列 如果没有前缀 则默认是搜索列
	 * 
	 * @param type
	 * @param request
	 * @return
	 */
	public <M extends Entity<Long>> Map<String, Object> search(Class<M> clazz, HttpServletRequest request) {
		// 公共的属性(pageIndex pageSize)
		Enumeration<String> enu = request.getParameterNames();
		Integer pageIndex = 0, pageSize = 20;
		Pattern searchP = Pattern.compile(SEARCH_REG);
		Pattern sortP = Pattern.compile(SORT_REG);
		while (enu.hasMoreElements()) {
			String paraName = enu.nextElement();
			if (paraName.equals("pageIndex")) {
				pageIndex = Commutil.null2Int(request.getParameter(paraName), 0);
			} else if (paraName.equals("pageSize")) {
				pageSize = Commutil.null2Int(request.getParameter(paraName), 20);
			} else if (sortP.matcher(paraName).matches()) {
				// 排序的列
			} else if (searchP.matcher(paraName).matches()) {
				// 查找的列
			}
		}
		M entity = null;
		//
		return null;
	}

	/**
	 * 该分页样式都是基于
	 * <p>
	 * DT_bootstrap.css
	 * </p>
	 * 
	 * @return
	 */
	public String getPage() {
		return "";
	}
	
	
	public static <M extends BaseEntity<Long>> M analysis(HttpServletRequest request, Class<M> clazz,final GainCallback<M> gain){	
		M entity=null;
		//当前时间
		Date currentDate=new Date();
		//当前登录对象
		if(gain!=null){
			Long id=Commutil.null2Long(request.getParameter("id"), -1);
			if(id>0){
				entity=gain.findOne(id);
			}
		}
		if(entity!=null&&entity.getId()!=null){
			entity.setUpdateTime(currentDate);
			
		}else{
			try {
				entity=clazz.newInstance();
				entity.setCreateTime(currentDate);
				entity.setDeleteStatus(false);				
			} catch (InstantiationException | IllegalAccessException e) {
				throw new AssertException(e.getMessage());
			}
		}
		Enumeration<String> enu = request.getParameterNames();
		Map<String, String> map = Maps.newHashMap();
		while (enu.hasMoreElements()) {
			String paraName = enu.nextElement();
			map.put(paraName, request.getParameter(paraName));
		}
		try {
			BeanUtils.populate(entity, map);
		} catch (InvocationTargetException | IllegalAccessException e) {
			throw new AssertException(e.getMessage());
		}
		return entity;
	}
	
	/*public <M extends BaseEntity<java.io.Serializable>> M analysis(HttpServletRequest request, Class<M> clazz)
			throws InstantiationException, IllegalAccessException {
		// 当前登录帐号 当前修改/创建时间 都可以在这里面维护
		Long id = Commutil.null2Long(request.getParameter("id"), -1);
		Commodity entity = null;
		// 存在ID 从数据库根据ID查询实体类
		if (id != null && id > 0) {
			//entity = this.commodityService.findOne(id);
		}
		
		 * if (entity != null && Commutil.null2Long(entity.getId()) > 0) {
		 * entity.setUpdateTime(new Date()); entity.setUpdateBy(""); } else {
		 * entity = clazz.newInstance(); entity.setCreateTime(new Date());
		 * entity.setCreateBy(""); }
		 
		Enumeration<String> enu = request.getParameterNames();
		Map<String, String> map = Maps.newHashMap();
		while (enu.hasMoreElements()) {
			String paraName = enu.nextElement();
			map.put(paraName, request.getParameter(paraName));
		}
		try {
			BeanUtils.populate(entity, map);
		} catch (InvocationTargetException e) {
		}
		return null;
	}*/
}
