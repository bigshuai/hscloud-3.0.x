package com.hisoft.hscloud.common.dao; 

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.entity.OperationLog;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.vo.OperationLogQueryVo;

public interface OperationLogDao {
	/**
	 * <插入操作日志> 
	* <功能详细描述> 
	* @param log
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void insertOperationLog(OperationLog log)throws HsCloudException;
	/**
	 * <系统操作日志查询> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<OperationLog> getOperationByPage(Page<OperationLog> paging,OperationLogQueryVo queryVo)throws HsCloudException;
	
}
