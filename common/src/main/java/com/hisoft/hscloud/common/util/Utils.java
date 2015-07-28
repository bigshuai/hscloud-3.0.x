/**
 * @title Utils.java
 * @package com.hisoft.hscloud.bss.sla.om.util
 * @description 工具类
 * @author MaDai
 * @update 2012-3-28 下午5:34:57
 * @version V1.0
 */
package com.hisoft.hscloud.common.util;

import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 * @description 工具类
 * @version 1.0
 * @author MaDai
 * @update 2012-3-28 下午5:34:57
 */
public final class Utils {

	/**
	 * 
	 * @title: strutsJson2Object
	 * @description Struts JSON --> Object
	 * @param c
	 * @return
	 * @throws Exception
	 *             设定文件
	 * @return T 返回类型
	 * @throws
	 * @version 1.0
	 * @author MaDai
	 * @update 2012-3-28 下午5:58:24
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T strutsJson2Object(Class c) throws Exception {
		return (T) new ObjectMapper().readValue(ServletActionContext
				.getRequest().getReader(), c);
	}

	/**
	 * 
	 * @title: strutsJson2GenericObject
	 * @description Struts JSON --> Generic Object
	 * @param typeReference
	 * @return
	 * @throws Exception
	 *             设定文件
	 * @return T 返回类型
	 * @throws
	 * @version 1.0
	 * @author MaDai
	 * @update 2012-3-28 下午5:58:33
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T strutsJson2GenericObject(TypeReference typeReference)
			throws Exception {
		return (T) new ObjectMapper().readValue(ServletActionContext
				.getRequest().getReader(), typeReference);
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
	
	public static <T> String joins(List<T> list,String joinStr){
		StringBuilder result=new StringBuilder("");
		if(list!=null&&list.size()>0){
			Object o=list.get(0);
			result.append(o.toString());
			for(int i=1;i<list.size();i++){
				result.append(joinStr).append(list.get(i).toString());
			}
		}
		return result.toString();
	}
	
	private Utils() {

	}
}
