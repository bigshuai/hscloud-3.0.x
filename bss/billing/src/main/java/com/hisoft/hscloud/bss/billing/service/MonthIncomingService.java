package com.hisoft.hscloud.bss.billing.service;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.vo.MonthIncomingVO;
import com.hisoft.hscloud.bss.billing.vo.QueryVO;

public interface MonthIncomingService {
	
	public Page<MonthIncomingVO>  getMonthIncomingMonths(Page<MonthIncomingVO> page,QueryVO queryVO);

}
