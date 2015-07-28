package com.hisoft.hscloud.crm.usermanager.util;

import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public final class Utils {
	/**
	 * Struts JSON --> Object
	 * 
	 * @param <T>
	 * @param c
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T strutsJson2Object(Class c) throws Exception {
		return (T) new ObjectMapper().readValue(ServletActionContext.getRequest().getReader(), c);
	}

	private Utils() {

	}
	
	/**
	 * 
	 * @title: json2Object
	 * @description JSON --> Object
	 * @param json
	 * @param typeReference
	 * @return
	 * @throws Exception 设定文件
	 * @return T    返回类型
	 * @throws
	 * @version 1.0
	 * @author MaDai
	 * @update 2012-4-16 下午1:27:22
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T json2Object(String json, TypeReference typeReference) throws Exception {
		return (T) new ObjectMapper().readValue(json, typeReference);
	}
}
