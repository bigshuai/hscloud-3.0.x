package com.hisoft.hscloud.bss.billing.service;

import java.util.List;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.vo.ApplicationTranscationLogVO;
import com.hisoft.hscloud.common.util.Sort;

public interface ApplicationTranscationLogService {
	/**
	 * 查询账单日志
	 * @param sorts 排序
	 * @param page 分页参数
	 * @param query 查询条件
	 * @return
	 */
	public Page<ApplicationTranscationLogVO> getAppTransactionByPage(List<Sort> sorts,Page<ApplicationTranscationLogVO> page,String query);
}
