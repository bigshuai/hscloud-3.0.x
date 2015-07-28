package com.hisoft.hscloud.crm.usermanager.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.crm.usermanager.dao.MaintenanceLogDao;
import com.hisoft.hscloud.crm.usermanager.entity.MaintenanceLog;
import com.hisoft.hscloud.crm.usermanager.service.MaintenanceLogService;

/**
 * @className: MaintenanceLogServiceImpl
 * @package: com.hisoft.hscloud.crm.usermanager.service.impl
 * @description: TODO
 * @author: liyunhui
 * @createTime: Sep 6, 2013 11:01:01 AM
 */
@Service
public class MaintenanceLogServiceImpl implements MaintenanceLogService {

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private MaintenanceLogDao maintenanceLogDao;

	@Override
	public void saveMaintenanceLog(MaintenanceLog ml) {
		maintenanceLogDao.save(ml);
	}

}