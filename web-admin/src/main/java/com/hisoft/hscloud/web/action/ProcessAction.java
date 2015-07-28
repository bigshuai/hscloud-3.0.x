package com.hisoft.hscloud.web.action; 

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.systemmanagement.vo.ProcessResourceVO;
import com.hisoft.hscloud.web.facade.Facade;

public class ProcessAction extends HSCloudAction{

	/** 
	* 注释内容 
	*/
	private static final long serialVersionUID = -3181959789785306683L;
	private Logger logger = Logger.getLogger(this.getClass());	
	@Autowired
	private Facade facade;	
	private String id;
	private String processCode;
	private int page;
	private int limit;
	private Page<ProcessResourceVO> pageProcess = new Page<ProcessResourceVO>();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}	
	public Page<ProcessResourceVO> getPageProcess() {
		return pageProcess;
	}
	public void setPageProcess(Page<ProcessResourceVO> pageProcess) {
		this.pageProcess = pageProcess;
	}
	public String getProcessCode() {
		return processCode;
	}
	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}
	/**
	 * <查询所有的线程记录> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String findAllProcess(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findAllProcess method.");			
		}
		int pageNo=page;
		int pageSize=limit;		
		try{
			pageProcess.setPageNo(pageNo);
			pageProcess.setPageSize(pageSize);
			pageProcess = facade.findAllProcess(pageProcess);
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.PROCESS_LIST_EXCEPTION,
					"findAllProcess Exception:", logger, ex), Constants.OPTIONS_FAILURE);
		}
		super.fillActionResult(pageProcess);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findAllProcess method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <启动线程> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String startProcess(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter startProcess method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		boolean result = false;
		try {
			result = facade.startProcess(processCode);
			facade.insertOperationLog(admin,"后台启动线程","后台启动线程",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"后台启动线程错误:"+e.getMessage(),"后台启动线程",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.PROCESS_START_EXCEPTION, "startProcess Exception:", logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		if (result) {
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit startProcess method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <停止线程> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String stopProcess(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter stopProcess method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		boolean result = false;
		try {
			result = facade.stopProcess(processCode);
			facade.insertOperationLog(admin,"后台停止线程","后台停止线程",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"后台停止线程错误:"+e.getMessage(),"后台停止线程",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.PROCESS_STOP_EXCEPTION, "stopProcess Exception:", logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		if (result) {
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit stopProcess method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
}
