package com.hisoft.hscloud.bss.billing.service;

import java.util.List;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.vo.CapitalSource;
import com.hisoft.hscloud.bss.billing.vo.OtherResponsibility;
import com.hisoft.hscloud.bss.billing.vo.QueryVO;
import com.hisoft.hscloud.bss.billing.vo.ResponsibilityIncoming;
import com.hisoft.hscloud.bss.billing.vo.Statistics;
import com.hisoft.hscloud.common.util.Sort;

public interface ReportService {
	
	public  List<CapitalSource> findCapitalSource(Long domainId,String yearMonth);
	public List<ResponsibilityIncoming> findResponsibilityIncoming(Long domainId,String yearMonth);
	public List<Statistics> findStatistics(Long domainId, String yearMonth);
	public List<OtherResponsibility> findOtherResponsibility(QueryVO queryVO);
	public Page<ResponsibilityIncoming> findResponsibilityByPage(List<Sort> sort, Page<ResponsibilityIncoming> page, QueryVO queryVO);
	

}
