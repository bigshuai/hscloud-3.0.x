package com.hisoft.hscloud.bss.billing.service.impl; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.bss.billing.dao.IncomingLogDao;
import com.hisoft.hscloud.bss.billing.entity.IncomingLog;
import com.hisoft.hscloud.bss.billing.service.IncomingLogService;
import com.hisoft.hscloud.common.util.HsCloudException;
@Service
public class IncomingLogServiceImpl implements IncomingLogService {
	@Autowired
	private IncomingLogDao incomingLogDao;
	@Override
	public void saveIncomingLog(IncomingLog incomingLog)
			throws HsCloudException {
		incomingLogDao.saveIncomingLog(incomingLog);
	}

}
