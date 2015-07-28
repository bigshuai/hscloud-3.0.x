package com.hisoft.hscloud.bss.sla.om.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.bss.sla.om.dao.RefundVmDao;
import com.hisoft.hscloud.bss.sla.om.entity.RefundVm;
import com.hisoft.hscloud.bss.sla.om.service.RefundVmService;
import com.hisoft.hscloud.common.util.HsCloudException;
@Service
public class RefundVmServiceImpl implements RefundVmService {
	@Autowired
	private RefundVmDao refundVmDao;
	@Override
	public void saveRefundVm(RefundVm rv) throws HsCloudException {
		refundVmDao.saveRefundVm(rv);
	}

}
