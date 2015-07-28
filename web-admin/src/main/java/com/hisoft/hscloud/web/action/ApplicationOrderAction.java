package com.hisoft.hscloud.web.action;


import java.util.HashMap;
import java.util.List;
import java.util.Map;





import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.vo.QueryVO;
import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.web.facade.Facade;
import com.wgdawn.persist.model.AppOrder;
import com.wgdawn.persist.more.model.app.MoreAppOrderDetailInfo;
import com.wgdawn.service.AppOrderService;

public class ApplicationOrderAction extends HSCloudAction{	
	private static final long serialVersionUID = 3101080193100376483L;
	private	Logger logger = Logger.getLogger(this.getClass());
	private int page;
	private int limit;
	private String query;
	private int appOrderId;
	private QueryVO queryVO;//查询条件集合
	@Autowired
	private AppOrderService appOrderService;
	@Autowired
	private Facade facade;
	
	/**
	 * 查询订单详情
	 */
	public void getAppOrderByPage(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAppOrderByPage method.");
		}
		try {
			Map<String,Object> queryMap = new HashMap<String,Object>();
			Page<MoreAppOrderDetailInfo> pageLog = new Page<MoreAppOrderDetailInfo>(limit);
			pageLog.setPageNo(page);
			if(StringUtils.isNotBlank(query)){
				query=new String(query.getBytes("iSO8859_1"),"UTF-8");
				queryMap.put("fuzzy", query);
			}
			if(queryVO != null){
				queryMap.put("orderStatus", queryVO.getTransactionType());
			}
			queryMap.put("start",(page - 1) * limit);
			queryMap.put("size", limit);
			List<MoreAppOrderDetailInfo> lst=appOrderService.selectAppOrderDetailInfo(queryMap);
			int count=appOrderService.selectAppOrderDetailCount(queryMap);
			pageLog.setResult(lst);
			pageLog.setTotalCount(count);
			fillActionResult(pageLog);
		} catch(HsCloudException hce){
			dealThrow(hce,hce.getCode());
		} catch (Exception e) {
			dealThrow(new HsCloudException("",
					"分页获取订单信息异常", logger, e),Constants.APPORDER_PAGE_EXCEPTION,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAppOrderByPage method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 通过id获取指定订单详情
	 */
	public void getAppOrderById(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAppOrderById method.");
		}
		try {
			List<MoreAppOrderDetailInfo> lst=appOrderService.selectAppOrderDetailById(appOrderId);
		    fillActionResult(lst);
		} catch(HsCloudException hce){
			dealThrow(hce,hce.getCode());
		} catch (Exception e) {
			dealThrow(new HsCloudException("",
					"通过id获取指定订单详情", logger, e),Constants.APPORDER_GET_EXCEPTION,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAppOrderById method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * 取消订单
	 */
	public void cancelAppOrder(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter cancelAppOrder method.");
		}
		try {
			AppOrder appOrder = new AppOrder();
			appOrder.setId(appOrderId);
			appOrder.setStatus(2);
			appOrderService.updateAppOrderStatusById(appOrder);
		} catch(HsCloudException hce){
			dealThrow(hce,hce.getCode());
		} catch (Exception e) {
			dealThrow(new HsCloudException("",
					"取消订单异常", logger, e),Constants.APPORDER_CANCEL_EXCEPTION,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit cancelAppOrder method.takeTime:" + takeTime + "ms");
		}
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

	public QueryVO getQueryVO() {
		return queryVO;
	}

	public void setQueryVO(QueryVO queryVO) {
		this.queryVO = queryVO;
	}

	public int getAppOrderId() {
		return appOrderId;
	}

	public void setAppOrderId(int appOrderId) {
		this.appOrderId = appOrderId;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

}
