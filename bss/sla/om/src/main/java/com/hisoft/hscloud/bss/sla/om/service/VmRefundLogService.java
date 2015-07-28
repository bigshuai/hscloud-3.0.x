package com.hisoft.hscloud.bss.sla.om.service; 

import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.om.entity.VmRefundLog;
import com.hisoft.hscloud.bss.sla.om.vo.VmRefundLogVO;
import com.hisoft.hscloud.common.util.HsCloudException;

public interface VmRefundLogService {
	/**
	 * <保存退款日志信息> 
	* <功能详细描述> 
	* @param vmRefundLog 
	* @see [类、类#方法、类#成员]
	 */
	public void saveVmRefundLog(VmRefundLog vmRefundLog)throws HsCloudException;
	/**
	 * <更新退款日志信息> 
	* <功能详细描述> 
	* @param uuid 
	* @param refundReasonType 
	* @param applyReason  
	* @see [类、类#方法、类#成员]
	 */
	public void updateVmRefundLog(String uuid, Short refundReasonType,
			String applyReason)throws HsCloudException;
	/**
	 * <根据物理id获取退款日志> 
	* <功能详细描述> 
	* @param id 
	* @see [类、类#方法、类#成员]
	 */
	public VmRefundLog getVmRefundLogById(Long id)throws HsCloudException;
	/**
	 * <根据uuid来获取虚拟机的云主机名和外网IP> <功能详细描述>
	 * @param uuid
	 * @see [类、类#方法、类#成员]
	 */
	public Map<String, String> getVmNameVmOuterIpByUuid(String uuid)
			throws HsCloudException;
	/**
	 * <分页获取日志信息> 
	* <功能详细描述> 
	* @param status
	* @param query
	* @param paging
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public Page<VmRefundLog> vmRefundLogPaging(short status,String query,Page<VmRefundLog> paging,List<String> ownerEmails)throws HsCloudException;
	/**
	 * <根据虚拟机uuid获取退款日志> 
	* <功能详细描述> 
	* @param uuid
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public VmRefundLog getVmRefundLogByUUID(String uuid)throws HsCloudException;
	/**
	 * <根据虚拟机uuid判断云主机是否为申请中> 
	* <功能详细描述> 
	* @author liyunhui
	* @param uuid
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public VmRefundLog isVMApplyingRefundByUuid(String uuid)throws HsCloudException;
	/**
	 * <根据虚拟机uuid来判断它的status是否为3.拒绝 4.已取消> 
	* <功能详细描述> 
	* @author liyunhui
	* @param uuid
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public VmRefundLog isExistedVMStatusEquals3Or4ByUuid(String uuid)throws HsCloudException;
	/**
	 * <分页获取退款日志信息-前台> 
	* <功能详细描述> 
	* @param uuid
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public  Page<VmRefundLogVO> vmRefundLogPaging4Website(Short status,String query,Page<VmRefundLogVO> paging,Long userId)throws HsCloudException;
}
