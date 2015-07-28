/* 
* 文 件 名:  PlatformRelationServiceImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2014-4-16 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.crm.usermanager.service.impl; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.dao.PlatformRelationDao;
import com.hisoft.hscloud.crm.usermanager.entity.PlatformRelation;
import com.hisoft.hscloud.crm.usermanager.service.PlatformRelationService;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2014-4-16] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Service
public class PlatformRelationServiceImpl implements PlatformRelationService {

	@Autowired
	private PlatformRelationDao platformRelationDao;
	@Override
	@Transactional(readOnly = true)
	public PlatformRelation getPlatformRelationByLocalUser(String userId)
			throws HsCloudException {
		return platformRelationDao.getPlatformRelationByLocalUser(userId);
	}
	@Override
	@Transactional(readOnly = true)
	public PlatformRelation getPlatformRelationByExternalUser(
			String externalUser) throws HsCloudException {
		return platformRelationDao.getPlatformRelationByExternalUser(externalUser);
	}
	@Override
	@Transactional
	public String synchroPlatformRelation(PlatformRelation platformRelation)
			throws HsCloudException {
		return platformRelationDao.synchroPlatformRelation(platformRelation);
	}
	
	@Transactional
	public String deletePlatformRelation(PlatformRelation platformRelation)
			throws HsCloudException {
		return platformRelationDao.deletePlatformRelation(platformRelation);
	}

}
