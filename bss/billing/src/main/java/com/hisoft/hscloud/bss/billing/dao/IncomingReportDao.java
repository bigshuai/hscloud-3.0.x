package com.hisoft.hscloud.bss.billing.dao;

import com.hisoft.hscloud.bss.billing.entity.IncomingReport;

public interface IncomingReportDao {
	
	public void save(IncomingReport incomingReport);
	
	public IncomingReport findPreMonthIncomingReport(String month,Long domainId);

}
