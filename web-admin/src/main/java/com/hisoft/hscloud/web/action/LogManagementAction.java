package com.hisoft.hscloud.web.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.entity.HcEventResource;
import com.hisoft.hscloud.common.entity.HcEventVmOps;
import com.hisoft.hscloud.common.entity.OperationLog;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.vo.OperationLogQueryVo;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.systemmanagement.vo.LogQueryVO;
import com.hisoft.hscloud.web.facade.Facade;

public class LogManagementAction extends HSCloudAction {
	private Logger logger = Logger.getLogger(LogManagementAction.class);
	private int page;

	private int start;

	private int limit;

	private LogQueryVO queryVO=new LogQueryVO();// 查询条件集合
	
	private OperationLogQueryVo olqv=new OperationLogQueryVo();

	private Page<HcEventResource> pageResourceLog = new Page<HcEventResource>(
			Constants.PAGE_NUM);

	private Page<HcEventVmOps> pageVmOpsLog = new Page<HcEventVmOps>(
			Constants.PAGE_NUM);
	private Page<OperationLog> pageOperationLog = new Page<OperationLog>(
			Constants.PAGE_NUM);
	
	private Long jobId;

	@Autowired
	private Facade facade;
    /**
     * <分页获取资源操作日志> 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
	public void pageResourceLog() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter pageResourceLog method.");
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
        List<Object> referenceIds = null;
        boolean isSpecial = facade.isSpecialAdmin(admin);
        if(!isSpecial){
            referenceIds = facade.getZoneIdsByAdminId(admin.getId());
        }
		try {
		    queryVO.setZoneIdArray(referenceIds);
		    pageResourceLog = new Page<HcEventResource>(limit);
			pageResourceLog.setPageNo(page);
			Page<HcEventResource> log = facade.getResourceLog(pageResourceLog, queryVO);
			fillActionResult(log);
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "pageResourceLog 异常", logger, e),"");
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit pageResourceLog method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <分页获取vm操作日志> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void pageVmOpsLog() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter pageVmOpsLog method.");
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		List<Object> referenceIds = null;
		boolean isSpecial = facade.isSpecialAdmin(admin);
		if(!isSpecial){
			referenceIds = facade.getZoneIdsByAdminId(admin.getId());
		}
		try {
			queryVO.setZoneIdArray(referenceIds);
			pageVmOpsLog = new Page<HcEventVmOps>(limit);
			pageVmOpsLog.setPageNo(page);
			Page<HcEventVmOps> log = facade.getVmOpsLog(pageVmOpsLog, queryVO);
			fillActionResult(log);
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "pageVmOpsLog 异常", logger, e),"");
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit pageVmOpsLog method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <分页获取系统操作日志> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void pageOperationLog() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter pageOperationLog method.");
		}
		try {
			pageOperationLog = new Page<OperationLog>(limit);
			pageOperationLog.setPageNo(page);
			Page<OperationLog> log = facade.getOperationByPage(pageOperationLog,olqv);
			fillActionResult(log);
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "pageOperationLog 异常", logger, e),"");
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit pageOperationLog method.takeTime:" + takeTime + "ms");
		}
	}
	
	public void resendResourceJobMessage(){
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter resendResourceJobMessage method.");
		}
		try {
			facade.resendResourceJobMessage(jobId);
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "resendResourceJobMessage异常", logger, e),
					Constants.OPTIONS_FAILURE, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit resendResourceJobMessage method.takeTime:" + takeTime + "ms");
		}
	}
	
	public void resendOpsJobMessage(){
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter resendOpsJobMessage method.");
		}
		try {
			facade.resendOpsJobMessage(jobId);
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "resendOpsJobMessage异常", logger, e),
					Constants.OPTIONS_FAILURE, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit resendOpsJobMessage method.takeTime:" + takeTime + "ms");
		}
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

	public LogQueryVO getQueryVO() {
		return queryVO;
	}

	public void setQueryVO(LogQueryVO queryVO) {
		this.queryVO = queryVO;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public OperationLogQueryVo getOlqv() {
		return olqv;
	}

	public void setOlqv(OperationLogQueryVo olqv) {
		this.olqv = olqv;
	}

}
