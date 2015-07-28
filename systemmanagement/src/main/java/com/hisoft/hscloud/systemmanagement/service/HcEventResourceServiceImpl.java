package com.hisoft.hscloud.systemmanagement.service; 

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.entity.HcEventResource;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.systemmanagement.dao.HcEventResourceDao;
import com.hisoft.hscloud.systemmanagement.vo.LogQueryVO;
@Service
public class HcEventResourceServiceImpl implements HcEventResourceService {
	@Autowired
	private HcEventResourceDao resourceLogDao;
	@Override
	public Page<HcEventResource> getResourceLog(Page<HcEventResource> paing,LogQueryVO param)
			throws HsCloudException {
		return resourceLogDao.getResourceLog(paing,param);
	}
	@Override
	public String getResourceLogMessageById(Long id) throws HsCloudException {
		String result=null;
		HcEventResource resourceLog=resourceLogDao.getResourceLogById(id);
		if(resourceLog!=null){
			result=resourceLog.getMessage();
		}
		return result;
	}
	

}
