package com.hisoft.hscloud.bss.billing.dao;

import com.hisoft.hscloud.bss.billing.entity.IncomingLog;
import com.hisoft.hscloud.common.util.HsCloudException;

public interface IncomingLogDao {
	/**
	 * <保存数据到hc_incoming_log> 
	* <功能详细描述> 
	* @param incomingLog
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void saveIncomingLog(IncomingLog incomingLog)throws HsCloudException;
}
