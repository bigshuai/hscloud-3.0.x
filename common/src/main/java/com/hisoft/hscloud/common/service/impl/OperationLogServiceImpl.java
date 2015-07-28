package com.hisoft.hscloud.common.service.impl; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.dao.OperationLogDao;
import com.hisoft.hscloud.common.entity.OperationLog;
import com.hisoft.hscloud.common.service.OperationLogService;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.vo.OperationLogQueryVo;
@Service
public class OperationLogServiceImpl implements OperationLogService {
	@Autowired
	private OperationLogDao operationLogDao;
	@Override
	public void insertOperationLog(OperationLog log) throws HsCloudException {
		operationLogDao.insertOperationLog(log);
	}
	@Override
	public Page<OperationLog> getOperationByPage(Page<OperationLog> paging,
			OperationLogQueryVo queryVo) throws HsCloudException {
		return operationLogDao.getOperationByPage(paging, queryVo);
	}
	

}
