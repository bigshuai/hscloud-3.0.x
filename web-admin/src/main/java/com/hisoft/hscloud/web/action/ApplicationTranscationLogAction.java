package com.hisoft.hscloud.web.action;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.entity.Account;
import com.hisoft.hscloud.bss.billing.vo.QueryVO;
import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.entity.Excel;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.util.Utils;
import com.hisoft.hscloud.web.facade.Facade;
import com.wgdawn.persist.more.model.app.AppBill;
import com.wgdawn.service.AppBillService;

public class ApplicationTranscationLogAction extends HSCloudAction{	
	private static final long serialVersionUID = 3101080193100376483L;
	private	Logger logger = Logger.getLogger(this.getClass());
	private int page;
	private int limit;
	private String sort;
	private String query;
	private QueryVO queryVO;//查询条件集合
	private AppBill appBill;
	private Page<AppBill> pageLog = new Page<AppBill>(Constants.PAGE_NUM);
	@Autowired
	private AppBillService appBillService;
	@Autowired
	private Facade facade;
	
	/**
	 * 查询日志。
	 */
	public void getAppTransactionByPage(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter pageLog method.");
		}
		try {
			Map<String,Object> queryMap = new HashMap<String,Object>();
			pageLog = new Page<AppBill>(limit);
			pageLog.setPageNo(page);
			pageLog.setOrder(Page.DESC);
			pageLog.setOrderBy("id");
			if(queryVO != null){
				if(queryVO.getFuzzy() != null && queryVO.getFuzzy() !=""){
					queryVO.setFuzzy(new String(queryVO.getFuzzy().trim().getBytes("iso8859-1"),"UTF-8"));
					queryMap.put("fuzzy", queryVO.getFuzzy());
				}
				if(queryVO.getEmail()!=null && !"".equals(queryVO.getEmail())){
					queryMap.put("useremail", queryVO.getEmail());
				}
				if(queryVO.getStartTime()!=null){
					queryMap.put("startTime", queryVO.getStartTime());
				}
				if(queryVO.getEndTime()!=null){
					queryMap.put("endTime", queryVO.getEndTime());
				}
				queryMap.put("transcationType", queryVO.getTransactionType());
			}
			queryMap.put("start",(page - 1) * limit);
			queryMap.put("size", limit);
			List<AppBill> lst=appBillService.selectBillDetailInfo(queryMap);
			int count=appBillService.selectBillDetailCount(queryMap);
			pageLog.setResult(lst);
			pageLog.setTotalCount(count);
			fillActionResult(pageLog);
		} catch(HsCloudException hce){
			dealThrow(hce,hce.getCode());
		} catch (Exception e) {
			dealThrow(new HsCloudException("",
					"分页获取账单信息异常", logger, e),Constants.APPBILL_PAGE_EXCEPTION,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAppTransactionByPage method.takeTime:" + takeTime + "ms");
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
		Admin admin=(Admin)super.getCurrentLoginUser();
		try {
			if(appBill!=null){
				User user=facade.getUserByEmail(appBill.getUserEmail());
				appBill.setCreateDate(new Date());
				appBill.setDealDate(new Date());
				appBill.setUserName(user.getName());
				appBill.setUserId((int)user.getId());
			}
		appBillService.insertAppBillByAdmin(appBill);
		facade.insertOperationLog(admin, "后台手动添加账单日志", "后台手动添加账单日志", Constants.RESULT_SUCESS);
		fillActionResult(true);
		} catch(HsCloudException hce){
			dealThrow(hce,hce.getCode());
			facade.insertOperationLog(admin, "后台手动添加账单日志", "后台手动添加账单日志", Constants.RESULT_FAILURE);
		} catch (Exception e) {
			dealThrow(new HsCloudException("",
					"orderDetail exception", logger, e),Constants.APPBILL_SAVE_EXCEPTION,true);
			facade.insertOperationLog(admin, "后台手动添加账单日志", "后台手动添加账单日志", Constants.RESULT_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit addTranLong method.takeTime:" + takeTime + "ms");
		}
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

	public AppBill getAppBill() {
		return appBill;
	}

	public void setAppBill(AppBill appBill) {
		this.appBill = appBill;
	}

}
