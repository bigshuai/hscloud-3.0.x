package com.hisoft.hscloud.bss.billing.service;

import java.util.List;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.vo.IpMessageVO;
import com.hisoft.hscloud.bss.billing.vo.MonthStatisVO;
import com.hisoft.hscloud.bss.billing.vo.QueryVO;

public interface IPMessageService {

	List<IpMessageVO> getIPMessage(QueryVO queryVO);

}
