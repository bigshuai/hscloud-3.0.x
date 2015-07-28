package com.hisoft.hscloud.systemmanagement.dao; 

import java.util.List;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.entity.HcEventResource;
import com.hisoft.hscloud.common.entity.HcEventVmOps;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.systemmanagement.vo.LogQueryVO;

public interface HcEventVmOpsDao {
	/**
	 * <查询关于虚拟机操作的日志> 
	* <功能详细描述> 
	* @param param
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<HcEventVmOps> getVmOpsLog(Page<HcEventVmOps> paing,LogQueryVO param)throws HsCloudException;
	/**
	 * <根据物理id获取实体bean> 
	* <功能详细描述> 
	* @param id
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public HcEventVmOps getVMOpsLogById(Long id)throws HsCloudException;
	/**
	 * <查询关于虚拟机操作的日志> 
	* <功能详细描述> 
	* @param param
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<HcEventVmOps> getVmOperationLog(Page<HcEventVmOps> paing,
			LogQueryVO param)throws HsCloudException;
	public HcEventVmOps getVMOperationLogById(Long opsId)throws HsCloudException;
}
