package com.hisoft.hscloud.bss.sla.om.service.impl; 

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.om.dao.VmRefundLogDao;
import com.hisoft.hscloud.bss.sla.om.entity.VmRefundLog;
import com.hisoft.hscloud.bss.sla.om.service.VmRefundLogService;
import com.hisoft.hscloud.bss.sla.om.vo.VmRefundLogVO;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.User;
@Service
public class VmRefundLogServiceImpl implements VmRefundLogService {
	@Autowired
    private VmRefundLogDao vmRefundLogDao;
	@Override
	public void saveVmRefundLog(VmRefundLog vmRefundLog)
			throws HsCloudException {
		vmRefundLogDao.saveVmRefundLog(vmRefundLog);
	}
    /**
     * <用于处理:  申请退款-->取消退款-->再次申请退款时的 再次申请退款操作>
     */
	@Override
	public void updateVmRefundLog(String uuid, Short refundReasonType,
			String applyReason)throws HsCloudException {
		vmRefundLogDao.updateVmRefundLog(uuid,refundReasonType,applyReason);
	}

	@Override
	public VmRefundLog getVmRefundLogById(Long id) throws HsCloudException {
		return vmRefundLogDao.getVmRefundLogById(id);
	}
	/**
	 * <根据uuid来获取虚拟机的云主机名和外网IP> <功能详细描述>
	 * @param uuid
	 * @see [类、类#方法、类#成员]
	 */
	public java.util.Map<String, String> getVmNameVmOuterIpByUuid(String uuid)
			throws HsCloudException {
		return vmRefundLogDao.getVmNameVmOuterIpByUuid(uuid);
	}
	
	@Override
	public Page<VmRefundLog> vmRefundLogPaging(short status, String query,
			Page<VmRefundLog> paging,List<String> ownerEmails) throws HsCloudException {
		return vmRefundLogDao.vmRefundLogPaging(status, query, paging,ownerEmails);
	}

	@Override
	public VmRefundLog getVmRefundLogByUUID(String uuid)
			throws HsCloudException {
		return vmRefundLogDao.getVmRefundLogByUUID(uuid);
	}
	
	/**
	 * <根据虚拟机uuid判断云主机是否为申请中> 
	* <功能详细描述> 
	* @author liyunhui
	* @param uuid
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	@Override
	public VmRefundLog isVMApplyingRefundByUuid(String uuid)
			throws HsCloudException {
		return vmRefundLogDao.isVMApplyingRefundByUuid(uuid);
	}
	/**
	 * <根据虚拟机uuid来判断它的status是否为3.拒绝 4.已取消> 
	* <功能详细描述> 
	* @author liyunhui
	* @param uuid
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	@Override
	public VmRefundLog isExistedVMStatusEquals3Or4ByUuid(String uuid)
			throws HsCloudException {
		return vmRefundLogDao.isExistedVMStatusEquals3Or4ByUuid(uuid);
	}

	@Override
	public  Page<VmRefundLogVO> vmRefundLogPaging4Website(Short status,
			String query, Page<VmRefundLogVO> paging,Long userId)
			throws HsCloudException {
		List<VmRefundLogVO> result=vmRefundLogDao.vmRefundLogPaging4Website(status, query, paging, userId);
		Long totalCount=vmRefundLogDao.getVmRefundLogCount(status, query, userId);
		paging.setResult(result);
		paging.setTotalCount(totalCount);
		return paging;
	}
	
}
