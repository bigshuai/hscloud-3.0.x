/* 
* 文 件 名:  PlatformRelationDaoImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2014-4-16 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.crm.usermanager.dao.impl; 

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.dao.PlatformRelationDao;
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
@Repository
public class PlatformRelationDaoImpl extends HibernateDao<PlatformRelation, Long> implements PlatformRelationDao {

	@Override
	public PlatformRelation getPlatformRelationByLocalUser(String userId)
			throws HsCloudException {		
		return this.findUniqueBy("userId", userId);
	}

	@Override
	public PlatformRelation getPlatformRelationByExternalUser(
			String externalUser) throws HsCloudException {
		return this.findUniqueBy("externalUserId", externalUser);
	}

	@Override
	public String synchroPlatformRelation(PlatformRelation platformRelation)
			throws HsCloudException {
		this.save(platformRelation);
		return Constants.SUCCESS;
	}
	
	@Override
	public String deletePlatformRelation(PlatformRelation platformRelation)
			throws HsCloudException {
		this.delete(platformRelation);
		return Constants.SUCCESS;
	}

}
