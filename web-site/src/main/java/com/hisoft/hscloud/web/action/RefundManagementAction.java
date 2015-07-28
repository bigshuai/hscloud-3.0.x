package com.hisoft.hscloud.web.action; 

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.om.entity.VmRefundLog;
import com.hisoft.hscloud.bss.sla.om.vo.VmRefundLogVO;
import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.web.facade.Facade;

public class RefundManagementAction extends HSCloudAction {
	private Logger logger = Logger.getLogger(RefundManagementAction.class);
	private Page<VmRefundLogVO> vmRefundLogPaging = new Page<VmRefundLogVO>(Constants.PAGE_NUM);
	private String uuid;
	private Short status;
	private int page;
	private int start;
	private int limit;
	private String query;
	private Short refundReasonType;
	private Long vmRefundId;
	private String applyReason;
	@Autowired
	private Facade facade;
	/**
	 * <一句话功能简述> 
	* <功能详细描述> 
	* @throws Exception 
	* @see [类、类#方法、类#成员]
	 */
	public void submitRefundApply() throws Exception {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter submitRefundApply method.");			
		}
		User user =null;
		String operateObject = "VM[vmId:" + uuid + "]";
		try {
			user=(User)super.getCurrentLoginUser();
			if(user!=null){
//				if(StringUtils.isNotBlank(applyReason)){
//					applyReason = new String(applyReason.getBytes("iso-8859-1"),"UTF-8");
//				}
				facade.submitRefundApply(user,uuid,applyReason,refundReasonType);
				facade.insertOperationLog(user, "提交退款申请","提交退款申请",Constants.RESULT_SUCESS,operateObject);
			}
		} catch (Exception e) {
			facade.insertOperationLog(user, "提交退款申请错误","提交退款申请",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException("", "submitRefundApply exception",
					logger, e), Constants.APPLY_REFUND_ERROR, true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit submitRefundApply method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <用于处理:  申请退款-->取消退款-->再次申请退款时的 再次申请退款操作> 
	* <功能详细描述> 
	* @throws Exception 
	* @see [类、类#方法、类#成员]
	 */
	public void submitRefundApplyOnceAgain() throws Exception {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter submitRefundApplyOnceAgain method.");			
		}
		User user=null;
		String operateObject = "VM[vmId:" + uuid + "]";
		try {
			user =(User)super.getCurrentLoginUser();
			if(user!=null){
//				if(StringUtils.isNotBlank(applyReason)){
//					applyReason = new String(applyReason.getBytes("iso8859-1"),"UTF-8");
//				}
				facade.submitRefundApplyOnceAgain(uuid,refundReasonType,applyReason);
				facade.insertOperationLog(user, "申请退款-->取消退款-->再次申请退款","再次提交退款申请",Constants.RESULT_SUCESS,operateObject);
			}
		} catch (Exception e) {
			facade.insertOperationLog(user, "申请退款-->取消退款-->再次申请退款出现错误","再次提交退款申请",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException("", "submitRefundApplyOnceAgain exception",
					logger, e), Constants.APPLY_REFUND_ERROR, true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit submitRefundApplyOnceAgain method.takeTime:" + takeTime + "ms");
		}
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
	public void isVMApplyingRefundByUuid(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter isVMApplyingRefundByUuid method.");			
		}
		try {
			super.getActionResult().setResultObject(facade.isVMApplyingRefundByUuid(uuid));
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "isVMApplyingRefundByUuid exception",
					logger, e), Constants.APPLY_REFUND_ERROR, true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit isVMApplyingRefundByUuid method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <根据虚拟机uuid判断虚拟机是否被禁用> 
	* <功能详细描述:根据VpdcReference的属性来判断>
	* <private int isEnable = 0; 0:正常；1：手动禁用；2：到期禁用> 
	* @param uuid
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void judgeWhetherVmIsDisabledByUUID(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter judgeWhetherVmIsDisabledByUUID method.");			
		}
		try {
			super.getActionResult().setResultObject(facade.isVmDisabledByUUID(uuid));
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "judgeWhetherVmIsDisabledByUUID exception",
					logger, e), Constants.APPLY_REFUND_ERROR, true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit judgeWhetherVmIsDisabledByUUID method.takeTime:" + takeTime + "ms");
		}
	}
	
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
			logger.debug("enter getVmRefundLogByPage method.");			
		}
		try {
			User user = (User)super.getCurrentLoginUser();
			if(user!=null){
				vmRefundLogPaging = new Page<VmRefundLogVO>(limit);
				vmRefundLogPaging.setPageNo(page);
				vmRefundLogPaging = facade.getVmRefundLogByPage(vmRefundLogPaging, query, user.getId(), status);
				fillActionResult(vmRefundLogPaging);
			}
			
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "getVmRefundLogByPage exception",
					logger, e), Constants.ORDER_PAGING_ERROR, true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getVmRefundLogByPage method.takeTime:" + takeTime + "ms");
		}
	}
	
	public void cancelRefundApply()throws Exception{
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter cancelRefundApply method.");			
		}
		User user =null;
		String operateObject = "VmRefund[vmRefundId:" + vmRefundId + "]";
		try {
			user = (User)super.getCurrentLoginUser();
			if(user!=null){
				facade.cancelRefundApply(vmRefundId);
				facade.insertOperationLog(user, "取消退款申请","取消退款申请",Constants.RESULT_SUCESS,operateObject);
			}
			
		} catch (Exception e) {
			facade.insertOperationLog(user, "取消退款申请出现错误","取消退款申请",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException("", "cancelRefundApply exception",
					logger, e),Constants.OPTIONS_FAILURE, true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit cancelRefundApply method.takeTime:" + takeTime + "ms");
		}
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
	public void isExistedVMStatusEquals3Or4ByUuid(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter isExistedVMStatusEquals3Or4ByUuid method.");			
		}
		try {
			super.getActionResult().setResultObject(facade.isExistedVMStatusEquals3Or4ByUuid(uuid));
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "isExistedVMStatusEquals3Or4ByUuid exception",
					logger, e), Constants.APPLY_REFUND_ERROR, true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit isExistedVMStatusEquals3Or4ByUuid method.takeTime:" + takeTime + "ms");
		}
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getApplyReason() {
		return applyReason;
	}
	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
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
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public Long getVmRefundId() {
		return vmRefundId;
	}
	public void setVmRefundId(Long vmRefundId) {
		this.vmRefundId = vmRefundId;
	}
	public Short getRefundReasonType() {
		return refundReasonType;
	}
	public void setRefundReasonType(Short refundReasonType) {
		this.refundReasonType = refundReasonType;
	}
	
}