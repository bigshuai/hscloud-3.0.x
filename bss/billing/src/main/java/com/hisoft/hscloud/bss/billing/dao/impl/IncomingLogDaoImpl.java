package com.hisoft.hscloud.bss.billing.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.bss.billing.dao.IncomingLogDao;
import com.hisoft.hscloud.bss.billing.entity.IncomingLog;
import com.hisoft.hscloud.common.util.HsCloudException;

@Repository
public class IncomingLogDaoImpl extends HibernateDao<IncomingLog, Long>
		implements IncomingLogDao {
	private Logger logger = Logger.getLogger(IncomingLogDaoImpl.class);

	@Override
	public void saveIncomingLog(IncomingLog incomingLog)
			throws HsCloudException {
		try {
			super.save(incomingLog);
		} catch (Exception e) {
			throw new HsCloudException("",logger,e);
		}
	}

}
