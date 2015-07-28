/* 
* 文 件 名:  SiteConfigDao.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  houyh 
* 修改时间:  2012-12-5 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.common.dao; 

import com.hisoft.hscloud.common.entity.SiteConfig;
import com.hisoft.hscloud.common.util.HsCloudException;

/** 
 * <用于hscloud平台参数配置> 
 * <功能详细描述> 
 * 
 * @author  houyh 
 * @version  [版本号, 2012-12-5] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface SiteConfigDao {
	/**
	 * <一句话功能简述> 
	* <功能详细描述> 
	* @param property
	* @param value
	* @return
	* @throws HsCloudException 
	* @see [SiteConfigDao、SiteConfigDao#getSiteConfigByProperty、类#成员]
	 */
	public SiteConfig getSiteConfigByProperty(String property,Object value)throws HsCloudException;
	/**
	 * <一句话功能简述> 
	* <功能详细描述> 
	* @param config
	* @param belongTo
	* @return
	* @throws HsCloudException 
	* @see [SiteConfigDao、SiteConfigDao#getConfigValue、类#成员]
	 */
	public String getConfigValue(String config,String belongTo)throws HsCloudException;
}
