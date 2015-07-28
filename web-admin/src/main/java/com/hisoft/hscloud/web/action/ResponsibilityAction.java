package com.hisoft.hscloud.web.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.vo.MonthIncomingVO;
import com.hisoft.hscloud.bss.billing.vo.MonthStatisVO;
import com.hisoft.hscloud.bss.billing.vo.QueryVO;
import com.hisoft.hscloud.bss.billing.vo.ResponsibilityIncoming;
import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.util.Utils;
import com.hisoft.hscloud.web.facade.Facade;

public class ResponsibilityAction extends HSCloudAction{
	
    private int limit;
    
    private int page;
    
    private String sort;
    
    private Page<MonthIncomingVO> pageMonthIncoming = new Page<MonthIncomingVO>(Constants.PAGE_NUM);
    
    private Page<ResponsibilityIncoming> pageResponsibilityIncoming = new Page<ResponsibilityIncoming>(Constants.PAGE_NUM);
    
    private Page<MonthStatisVO> pageMonthStatis = new Page<MonthStatisVO>(Constants.PAGE_NUM);
	
	private Map<String,Object> map = new HashMap<String, Object>();
	
	private QueryVO queryVO = new QueryVO();
	
	@Autowired
	private Facade facade;
	
	public void responsibilityMonth(){
//		pageMonthIncoming = new Page<MonthIncomingVO>(limit);
//		pageMonthIncoming.setPageNo(page);
//		pageMonthIncoming.setOrder(Page.DESC);
//		pageMonthIncoming.setOrderBy("id");
		
		pageResponsibilityIncoming = new Page<ResponsibilityIncoming>(limit);
		pageResponsibilityIncoming.setPageNo(page);
		pageResponsibilityIncoming.setOrder(Page.DESC);
		pageResponsibilityIncoming.setOrderBy("id");
		
       try {
    	   Admin admin=(Admin)super.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
    	   //Page<MonthIncomingVO> monthIncoming  =  facade.getMonthIncomingMonths(pageMonthIncoming,admin,queryVO);
    	   Page<ResponsibilityIncoming> responsibilityIncoming  =  facade.getMonthIncoming(parseSort(),pageResponsibilityIncoming,admin,queryVO);
    	   fillActionResult((responsibilityIncoming));
    	   
		} catch (HsCloudException hce){
			dealThrow(hce, "");
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		 
	}
	
	public void monthStatis(){
		
		pageMonthStatis = new Page<MonthStatisVO>(limit);
		pageMonthStatis.setPageNo(page);
		pageMonthStatis.setOrder(Page.DESC);
		pageMonthStatis.setOrderBy("id");
		
       try {
    	   Admin admin=(Admin)super.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
    	   Page<MonthStatisVO> monthStatis  =  facade.getMonthStatis(pageMonthStatis,admin,queryVO);
    	   fillActionResult(monthStatis);
    	   
		} catch (HsCloudException hce){
			dealThrow(hce, "");
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
       
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public Page<MonthIncomingVO> getPageMonthIncoming() {
		return pageMonthIncoming;
	}

	public void setPageMonthIncoming(Page<MonthIncomingVO> pageMonthIncoming) {
		this.pageMonthIncoming = pageMonthIncoming;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public Facade getFacade() {
		return facade;
	}

	public void setFacade(Facade facade) {
		this.facade = facade;
	}

	public QueryVO getQueryVO() {
		return queryVO;
	}

	public void setQueryVO(QueryVO queryVO) {
		this.queryVO = queryVO;
	}
	
	
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	private List<Sort> parseSort() throws Exception {
		return Utils.json2Object(sort, new TypeReference<List<Sort>>() {});
	}

}
