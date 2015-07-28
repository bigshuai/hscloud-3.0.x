package com.hisoft.hscloud.bss.billing.dao;

import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.vo.MonthIncomingVO;

public interface MonthIncomingVODao {
	
	public Page<MonthIncomingVO> findPageBySQL(Page<MonthIncomingVO> page,String sql,Map<String,Object> map);

	public List<MonthIncomingVO> findBySQL(String sql, Map<String, ?> map);
}
