package com.hisoft.hscloud.bss.billing.dao.impl;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.bss.billing.dao.IncomingReportDao;
import com.hisoft.hscloud.bss.billing.entity.IncomingReport;

@Repository
public class IncomingReportDaoImpl extends HibernateDao<IncomingReport, Long> implements IncomingReportDao {

	public void save(IncomingReport incomingReport){
		super.save(incomingReport);
	}

	@Override
	public IncomingReport findPreMonthIncomingReport(String month,
			Long domainId) {
		return super.findUnique("from IncomingReport ir  where ir.month=? and ir.domainId=?", month,domainId);
	}
	
}
