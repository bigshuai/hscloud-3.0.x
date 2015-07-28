/* 
* 文 件 名:  VpdcRenewalServiceImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-6-6 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.ops.service; 

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.vpdc.ops.dao.VpdcRenewalDao;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcRenewal;

/** 
 * <续订业务数据操作> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-6-6] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Service
public class VpdcRenewalServiceImpl implements VpdcRenewalService {
	@Autowired
	private VpdcRenewalDao vpdcRenewalDao;

	@Override
	@Transactional(readOnly = false)
	public long saveVpdcRenewal(VpdcRenewal vpdcRenewal)
			throws HsCloudException {
		return vpdcRenewalDao.saveVpdcRenewal(vpdcRenewal);
	}
	
	@Transactional(readOnly = false)
	@Override
	public void saveVpdcRenewal(Map<String, Object> condition) {
	    vpdcRenewalDao.saveVpdcRenewal(condition);
	}
	
	@Transactional(readOnly = false)
	@Override
	public void updateVpdcRenewal(Map<String, Object> condition) {
	    vpdcRenewalDao.updateVpdcRenewal(condition);
    }

	@Override
	@Transactional(readOnly = false)
	public boolean deleteVpdcRenewalByReferenceId(long referenceId)
			throws HsCloudException {
		return vpdcRenewalDao.deleteVpdcRenewalByReferenceId(referenceId);
	}

	@Override
	@Transactional(readOnly = true)
	public VpdcRenewal getVpdcRenewalByReferenceId(long referenceId)
			throws HsCloudException {
		return vpdcRenewalDao.getVpdcRenewalByReferenceId(referenceId);
	}

	@Override
	public Page<VpdcRenewal> findVpdcRenewal(Page<VpdcRenewal> page,
			String field, Object fieldValue, String query, long userId)
			throws HsCloudException { 
		return vpdcRenewalDao.findVpdcRenewal(page, field, fieldValue, query, userId);
	}

}
