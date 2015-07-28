package com.hisoft.hscloud.bss.billing.dao;

import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.vo.IpMessageVO;
import com.hisoft.hscloud.bss.billing.vo.MonthStatisVO;

public interface IPMessageVODao {

	public List<IpMessageVO> findBySQL(String sql, Map<String, ?> map);	
	
}
