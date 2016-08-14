package com.official.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import com.google.common.collect.Maps;

public class PropertiesUtil {
	private static Properties prop=null;
	private static InputStream in=null;
	
	static{
		load();
	}
	
	private PropertiesUtil(){
		
	}
	/**
	 * 加载资源
	 */
	private static void load(){
		prop=new Properties();
		in=PropertiesUtil.class.getResourceAsStream("/resources.properties");
		try {
			prop.load(in);
		} catch (IOException e) {
		}
	}
	/**
	 * 清空
	 */
	public static void flush(){
		prop=null;
	}
	/**
	 * 获取全部资源
	 * @return
	 */
	public static Map<String,Object> getResources(){
		if(prop==null){
			load();
		}
		Map<String,Object> map=Maps.newHashMap();
		Set<Entry<Object,Object>> set=prop.entrySet();
		Iterator<Entry<Object,Object>> ite=set.iterator();
		while(ite.hasNext()){
			Entry<Object,Object> entry=ite.next();
			map.put(Commutil.null2String(entry.getKey()),entry.getValue());
		}
		return map;
	}
	
	public static Object get(String key){
		if(prop==null){
			load();
		}
		return prop.get(key);
	}
	
}
