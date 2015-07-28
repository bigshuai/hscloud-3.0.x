package com.hisoft.hscloud.bss.billing.dao;

import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.entity.TranscationLog;
import com.hisoft.hscloud.bss.billing.vo.CapitalSource;
import com.hisoft.hscloud.bss.billing.vo.OtherResponsibility;
import com.hisoft.hscloud.bss.billing.vo.ResponsibilityIncoming;
import com.hisoft.hscloud.bss.billing.vo.Statistics;
import com.hisoft.hscloud.bss.billing.vo.TranscationLogVO;
import com.hisoft.hscloud.bss.billing.vo.VMResponsibility;
import com.hisoft.hscloud.common.util.Sort;

public interface TranscationLogDao {
	
	public void save(TranscationLog transcationLog);
	
	public List<TranscationLogVO> findBySQL(String sql,Map<String, ?> map);
	
	//public TranscationLogVO findById(Long id);
	
	public Long findCountBySQL(String sql, Map<String, ?> map);
	
	public Page<TranscationLogVO> findPageBySQL(List<Sort> sorts,Page<TranscationLogVO> page,String sql,Map<String,Object> map);
	
	public List<CapitalSource> findCapitalSource(String sql);
	
	public List<ResponsibilityIncoming> findResponsibilityIncoming(String sql);
	
	public List<OtherResponsibility> findOtherResponsibility(String sql);

	public List<VMResponsibility> findVMResponsibility(String sql);
	
	public List<Statistics> findstatistics(String sql);
	
	public List<Statistics> findNoReportStatistics(String sql);
	
	public Page<VMResponsibility> findVMResponsibilityPageBySQL(List<Sort> sorts,Page<VMResponsibility> page,String sql,Map<String,Object> map);
	
	public List<VMResponsibility> findResponsibilityBySQL(String sql,Map<String, ?> map);

	public List<TranscationLog> findByHQL(String hql, Map<String, ?> map);

}
