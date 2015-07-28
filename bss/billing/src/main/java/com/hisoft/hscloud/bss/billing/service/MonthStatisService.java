package com.hisoft.hscloud.bss.billing.service;

import java.util.List;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.vo.MonthStatisVO;
import com.hisoft.hscloud.bss.billing.vo.QueryVO;

public interface MonthStatisService {

	Page<MonthStatisVO> getMonthStatisByPage(Page<MonthStatisVO> page, QueryVO queryVO);
	
	List<MonthStatisVO> getMonthStatis(QueryVO queryVO);

}
