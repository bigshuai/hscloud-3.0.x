package com.hisoft.hscloud.web.dao;

import com.hisoft.hscloud.web.vo.OperationLogVo;

public interface RestOperationLogDao {
	/**
	 * 根据日志Id返回job的结果
	 * @param logId
	 * @return
	 */
  public OperationLogVo getOperationLogById(Long logId,String jobType,String userId);
}
