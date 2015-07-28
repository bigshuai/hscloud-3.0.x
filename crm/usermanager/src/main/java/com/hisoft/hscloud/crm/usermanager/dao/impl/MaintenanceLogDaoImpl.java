package com.hisoft.hscloud.crm.usermanager.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.dao.MaintenanceLogDao;
import com.hisoft.hscloud.crm.usermanager.entity.MaintenanceLog;

/**
 * @className: MaintenanceLogDaoImpl
 * @package: com.hisoft.hscloud.crm.usermanager.dao.impl
 * @description: 用于处理维护日志的增删改查
 * @author: liyunhui
 * @createTime: Sep 6, 2013 10:37:55 AM
 */
@Repository
public class MaintenanceLogDaoImpl extends HibernateDao<MaintenanceLog, Long>
		implements MaintenanceLogDao {

	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	public void save(MaintenanceLog ml) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("enter MaintenanceLogDaoImpl save method");
				logger.debug("maintenanceLog:" + ml);
			}
			super.save(ml);
			if (logger.isDebugEnabled()) {
				logger.debug("exit MaintenanceLogDaoImpl save method");
			}
		} catch (Exception e) {
			throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,
					e.getMessage(), logger, e);
		}
	}

}