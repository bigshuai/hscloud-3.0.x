package com.hisoft.hscloud.bss.billing.dao;

import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.vo.MonthStatisVO;

public interface MonthStatisVODao {

	public List<MonthStatisVO> findBySQL(String sql, Map<String, ?> map);
	
	public Page<MonthStatisVO> findPageBySQL(Page<MonthStatisVO> page,
			String sql, Map<String, Object> map);
	
}
