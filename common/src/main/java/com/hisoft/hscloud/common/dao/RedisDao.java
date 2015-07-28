/* 
* 文 件 名:  RedisDao.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  dinghb 
* 修改时间:  2013-4-19 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.common.dao; 

import java.util.Map;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  dinghb 
 * @version  [版本号, 2013-4-19] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface RedisDao {
	/**
	 * <保存redis信息> 
	* <功能详细描述> 
	* @param vms 
	* @see [类、类#方法、类#成员]
	 */
	public void setValue(String key,String value);
	/**
	 * <获取字符串信息> 
	* <功能详细描述> 
	* @param key
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String getValue(String key);
	/**
	 * <保存redis信息> 
	* <功能详细描述> 
	* @param vms 
	* @see [类、类#方法、类#成员]
	 */
	public void setMap(String key,Map<String,String> map);
	/**
	 * <获取redis信息> 
	* <功能详细描述> 
	* @param key
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public Map<String, String> getMap(String key);
}
