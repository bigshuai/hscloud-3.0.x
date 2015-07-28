package com.hisoft.hscloud.web.action;


import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.constant.ConsumeType;
import com.hisoft.hscloud.bss.billing.constant.PaymentType;
import com.hisoft.hscloud.bss.billing.entity.Account;
import com.hisoft.hscloud.bss.billing.vo.QueryVO;
import com.hisoft.hscloud.bss.billing.vo.TranscationLogVO;
import com.hisoft.hscloud.bss.billing.vo.VMResponsibility;
import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.util.Utils;
import com.hisoft.hscloud.web.facade.Facade;

public class TranscationLogAction extends HSCloudAction{	
	
	
	/** 
	* 注释内容 
	*/
	private static final long serialVersionUID = 3101080193100376483L;

	private	Logger logger = Logger.getLogger(this.getClass());
	
//	private String resourceType="com.hisoft.hscloud.bss.billing.entity.TranscationLog";
	
	private int page;
	
	private int start;
	
	private int limit;
	
	private String sort;
	
	private String query;
	
	private User user;
	
	private Account account;
	
	private String remark;
	
	private Short line;
	
	private Short flow;
	
	private QueryVO queryVO;//查询条件集合
	
	private Page<TranscationLogVO> pageLog = new Page<TranscationLogVO>(Constants.PAGE_NUM);
	
	private Page<VMResponsibility> responsility = new Page<VMResponsibility>(Constants.PAGE_NUM);
	
	
	

	
	@Autowired
	private Facade facade;
	
	
	/**
	 * 查询日志。
	 */
	public void pageLog(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter pageLog method.");			
		}
		try {
			pageLog = new Page<TranscationLogVO>(limit);
			pageLog.setPageNo(page);
			pageLog.setOrder(Page.DESC);
			pageLog.setOrderBy("id");
			Page<TranscationLogVO> log = facade.pagePermissionTrLog(parseSort(), pageLog,query, super.getPrimKeys());
			fillActionResult(log);
		} catch(HsCloudException hce){
			dealThrow(hce,"");
		} catch (Exception e) {
			logger.error("", e);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit pageLog method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 添加消费日志
	 */
	public void addTranLong(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter addTranLong method.");			
		}
		try {
		Admin admin = (Admin)super.getCurrentLoginUser();
		facade.addTransactionLog(admin.getId(),PaymentType.PAYMENT_OFFLINE.getIndex(), user.getId(),account.getId(), account.getBalance(),account.getCoupons(),account.getGiftsBalance(),remark,ConsumeType.CONSUME_BUY.getIndex());
		fillActionResult(true);
		} catch(HsCloudException hce){
			dealThrow(hce,hce.getCode());
		} catch (Exception e) {
			logger.error("", e);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit addTranLong method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 查询消费日志
	 * @return
	 */
	public void findTransactionLog(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findTransactionLog method.");			
		}		
		try{
			Admin admin = (Admin)super.getCurrentLoginUser();
			pageLog = new Page<TranscationLogVO>();
			pageLog.setPageNo(page);
			pageLog.setPageSize(limit);
			pageLog.setOrder(Page.DESC);
			pageLog.setOrderBy("id");
			
			if(queryVO != null){
				if(queryVO.getFuzzy() != null && queryVO.getFuzzy() !=""){
					queryVO.setFuzzy(new String(queryVO.getFuzzy().trim().getBytes("iso8859-1"),"UTF-8"));
				}
			}
			
			pageLog =facade.findTransactionLog(admin,pageLog, queryVO, parseSort(), super.getPrimKeys());			
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.OPTIONS_FAILURE,
					"findTransactionLog异常", logger, ex), Constants.OPTIONS_FAILURE);
		}
		super.fillActionResult(pageLog);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findTransactionLog method.takeTime:" + takeTime + "ms");
		}		
	}
	
	public void findVMResponsibility(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findTransactionLog method.");			
		}		
		try{
			Admin admin = (Admin)super.getCurrentLoginUser();
			responsility = new Page<VMResponsibility>();
			responsility.setPageNo(page);
			responsility.setPageSize(limit);
			responsility.setOrder(Page.DESC);
			responsility.setOrderBy("id");
			
			if(queryVO != null){
				if(queryVO.getFuzzy() != null && queryVO.getFuzzy() !=""){
					queryVO.setFuzzy(new String(queryVO.getFuzzy().trim().getBytes("iso8859-1"),"UTF-8"));
				}
			}
			
			responsility =facade.findVMResponsibility(admin,responsility, queryVO, parseSort(), super.getPrimKeys());
			super.fillActionResult(responsility);
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.OPTIONS_FAILURE,
					"findTransactionLog异常", logger, ex), Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findTransactionLog method.takeTime:" + takeTime + "ms");
		}		
	}
	

	
	private List<Sort> parseSort() throws Exception {
		return Utils.json2Object(sort, new TypeReference<List<Sort>>() {});
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
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

	public QueryVO getQueryVO() {
		return queryVO;
	}

	public void setQueryVO(QueryVO queryVO) {
		this.queryVO = queryVO;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Short getLine() {
		return line;
	}

	public void setLine(Short line) {
		this.line = line;
	}

	public Short getFlow() {
		return flow;
	}

	public void setFlow(Short flow) {
		this.flow = flow;
	}
	
	
	
	

}
