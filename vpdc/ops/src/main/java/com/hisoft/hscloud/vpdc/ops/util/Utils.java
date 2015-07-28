package com.hisoft.hscloud.vpdc.ops.util;

import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.map.ObjectMapper;

public class Utils {
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
}
