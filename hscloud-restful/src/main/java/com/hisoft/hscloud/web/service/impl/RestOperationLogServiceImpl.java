package com.hisoft.hscloud.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.web.dao.RestOperationLogDao;
import com.hisoft.hscloud.web.service.RestOperationLogService;
import com.hisoft.hscloud.web.vo.OperationLogVo;
@Service
public class RestOperationLogServiceImpl implements RestOperationLogService {
	@Autowired
	private RestOperationLogDao restOperationLogDao;
	@Override
	public OperationLogVo getOperationLogById(Long logId, String jobType,String userId) {
		return restOperationLogDao.getOperationLogById(logId, jobType,userId);
	}

}
