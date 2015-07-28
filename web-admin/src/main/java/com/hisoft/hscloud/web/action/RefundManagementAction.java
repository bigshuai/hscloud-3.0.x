package com.hisoft.hscloud.web.action; 

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.om.entity.VmRefundLog;
import com.hisoft.hscloud.bss.sla.om.vo.VmRefundOrderItemVo;
import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.web.facade.Facade;

public class RefundManagementAction extends HSCloudAction {
	private Logger logger = Logger.getLogger(RefundManagementAction.class);
	private Page<VmRefundLog> vmRefundLogPaging = new Page<VmRefundLog>(Constants.PAGE_NUM);
	private int page;
	private int start;
	private int limit;
	private String uuid;
	private String query;
	private Long referenceId;
	private Long vmRefundId;
	private String rejectReason;
	private short status;
	@Autowired
	private Facade facade;
	/**
	 * <分页获取退款日志> 
	* <功能详细描述> 
	* @throws Exception 
	* @see [类、类#方法、类#成员]
	 */
	public void getVmRefundLogByPage() throws Exception {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter vmRefundLogPaging method.");			
		}
		try {
			Admin admin = (Admin)super.getCurrentLoginUser();
			if(admin!=null){
				vmRefundLogPaging = new Page<VmRefundLog>(limit);
				vmRefundLogPaging.setPageNo(page);
				if(StringUtils.isNotBlank(query)){
					query=new String(query.getBytes("iSO8859_1"),"UTF-8");
				}
				vmRefundLogPaging = facade.getVmRefundLogByPage(vmRefundLogPaging, query, status,admin);
				fillActionResult(vmRefundLogPaging);
			}
			
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "getVmRefundLogByPage exception",
					logger, e), Constants.ORDER_PAGING_ERROR, true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit vmRefundLogPaging method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <拒绝退款请求> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void rejectRefundApply(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter rejectRefundApply method.");			
		}
		Admin admin = null;
		try {
			admin= (Admin)super.getCurrentLoginUser();
			if(admin!=null){
				facade.rejectRefundApply(vmRefundId, rejectReason, admin);
				facade.insertOperationLog(admin,"拒绝退款申请","拒绝退款申请",Constants.RESULT_SUCESS);
			}
			
		} catch (Exception e) {
			facade.insertOperationLog(admin,"拒绝退款申请错误："+e.getMessage(),"拒绝退款申请",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException("", "rejectRefundApply exception",
					logger, e), Constants.ORDER_PAGING_ERROR, true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit rejectRefundApply method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * <vm退款> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void vmRefundForApply() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter vmRefundForApply method.");			
		}
		boolean result = false;
		Admin admin =null;
		try {
			admin= (Admin) super.getCurrentLoginUser();
			if (admin != null && (admin.getType()==null||admin.getType()!=2)) {
				result = facade.vmRefundForApply(referenceId, uuid, admin,vmRefundId);
				facade.insertOperationLog(admin,"审核部分退款","审核部分退款",Constants.RESULT_SUCESS);
			}
		} catch (Exception ex) {
			facade.insertOperationLog(admin,"审核部分退款错误："+ex.getMessage(),"审核部分退款",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException("", "vmRefundForApply异常", logger, ex),
					Constants.VM_REFUND_ERROR, true);
		}
		if (result) {
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		} else {
			super.fillActionResult(Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit vmRefundForApply method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * <vm全额退款> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void vmRefundAllForApply() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter vmRefundAllForApply method.");			
		}
		boolean result = false;
		Admin admin = null;
		try {
			admin= (Admin) super.getCurrentLoginUser();
			if (admin != null && (admin.getType()==null||admin.getType()!=2)) {
				result = facade.vmRefundAllForApply(referenceId, uuid, admin,vmRefundId);
				facade.insertOperationLog(admin,"审核全额退款","审核全额退款",Constants.RESULT_SUCESS);
			}
		} catch (Exception ex) {
			facade.insertOperationLog(admin,"审核全额退款错误:"+ex.getMessage(),"审核全额退款",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException("", "vmRefundAllForApply异常", logger, ex),
					Constants.VM_REFUND_ALL_ERROR, true);
		}
		if (result) {
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		} else {
			super.fillActionResult(Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit vmRefundAllForApply method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <获取退款订单信息详情> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void getVMRelatedRefundOrderInfoForApply() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getVMRelatedRefundOrderInfoForApply method.");			
		}
		try {
			List<VmRefundOrderItemVo> result = new ArrayList<VmRefundOrderItemVo>();
			result = facade.getVmRefundOrderItemVoForAplly(referenceId,vmRefundId);
			super.fillActionResult(result);
		} catch (Exception ex) {
			dealThrow(new HsCloudException("", "getVMRelatedRefundOrderInfoForApply异常",
					logger, ex),
					Constants.GET_VM_RELATED_REFUNDABLE_ORDER_ERROR, true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getVMRelatedRefundOrderInfoForApply method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <根据uuid来获取机器号为uuid的这台虚拟机的退款申请详情> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void getVmRefundLogByUuid() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getVmRefundLogByUuid method.");			
		}
		try {
			fillActionResult(facade.getVmRefundLogByUuid(uuid));
		} catch (Exception e) {
			dealThrow(new HsCloudException("",
					"getVmRefundLogByUuid exception", logger, e),
					Constants.ORDERITEM_DETAIL_ERROR, true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getVmRefundLogByUuid method.takeTime:" + takeTime + "ms");
		}
	}	

	public Page<VmRefundLog> getVmRefundLogPaging() {
		return vmRefundLogPaging;
	}

	public void setVmRefundLogPaging(Page<VmRefundLog> vmRefundLogPaging) {
		this.vmRefundLogPaging = vmRefundLogPaging;
	}

	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public Long getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}
	public Long getVmRefundId() {
		return vmRefundId;
	}
	public void setVmRefundId(Long vmRefundId) {
		this.vmRefundId = vmRefundId;
	}
	public short getStatus() {
		return status;
	}
	public void setStatus(short status) {
		this.status = status;
	}
	public String getRejectReason() {
		return rejectReason;
	}
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
	
}
