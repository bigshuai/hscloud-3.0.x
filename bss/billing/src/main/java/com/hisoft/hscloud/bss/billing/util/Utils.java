package com.hisoft.hscloud.bss.billing.util;

import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.map.ObjectMapper;

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
}
