package com.hisoft.hscloud.bss.billing.dao;

import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.entity.Report;
import com.hisoft.hscloud.bss.billing.vo.OtherResponsibility;
import com.hisoft.hscloud.bss.billing.vo.ResponsibilityIncoming;
import com.hisoft.hscloud.bss.billing.vo.Statistics;
import com.hisoft.hscloud.common.util.Sort;

public interface ReportDao {

	public void save(List<Report> reports);
	
	public List<Report> report(String yearMonth);
	
	public List<ResponsibilityIncoming> findResponsibilityIncoming(String sql);
	
	public List<Statistics>  findStatistics(String sql);

	public List<OtherResponsibility> findOtherResponsibility(String sql);

	public Page<ResponsibilityIncoming> findPageBySQL(List<Sort> sort,Page<ResponsibilityIncoming> page, String string,Map<String, Object> map);
	
	public List<ResponsibilityIncoming> findBySQL(String sql, Map<String, ?> map);
	
	public Long findCountBySQL(String sql, Map<String, ?> map);
	
}
