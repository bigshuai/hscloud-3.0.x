/* 
* 文 件 名:  PlatformRelationDao.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2014-4-16 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.crm.usermanager.dao; 

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.PlatformRelation;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2014-4-16] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface PlatformRelationDao {

	/**
	 * <根据本地用户email查询关联信息> 
	* <功能详细描述> 
	* @param userId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public PlatformRelation getPlatformRelationByLocalUser(String userId) throws HsCloudException;
	/**
	 * <根据客户用户userId查询关联信息> 
	* <功能详细描述> 
	* @param externalUser
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public PlatformRelation getPlatformRelationByExternalUser(String externalUser) throws HsCloudException;
	/**
	 * <同步本地与客户信息> 
	* <功能详细描述> 
	* @param platformRelation
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public String synchroPlatformRelation(PlatformRelation platformRelation) throws HsCloudException;
	public String deletePlatformRelation(PlatformRelation platformRelation)
			throws HsCloudException;
}
