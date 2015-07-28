package com.hisoft.hscloud.bss.sla.om.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.bss.sla.om.dao.UserRefundDao;
import com.hisoft.hscloud.bss.sla.om.entity.UserRefund;
import com.hisoft.hscloud.bss.sla.om.service.UserRefundService;
import com.hisoft.hscloud.common.util.HsCloudException;
@Service
public class UserRefundServiceImpl implements UserRefundService {
    @Autowired
    private UserRefundDao userRefundDao;
	@Override
	public Long saveUserRefund(UserRefund ur) throws HsCloudException {
		return userRefundDao.saveUserRefund(ur);
	}

}
