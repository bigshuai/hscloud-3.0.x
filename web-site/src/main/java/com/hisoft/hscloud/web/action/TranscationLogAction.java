package com.hisoft.hscloud.web.action;

import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.vo.TranscationLogVO;
import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.util.Utils;
import com.hisoft.hscloud.web.facade.Facade;

public class TranscationLogAction extends HSCloudAction{

	private static final long serialVersionUID = 1L;
	
	
	private	Logger logger = Logger.getLogger(this.getClass());
	
	private final String resourceType="com.hisoft.hscloud.bss.billing.entity.TranscationLog";
	
	private int page;
	
	private int start;
	
	private int limit;
	
	private String sort;
	
	private String query;
	
	private Page<TranscationLogVO> pageLog = new Page<TranscationLogVO>(Constants.PAGE_NUM);
	

	
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
			User user = (User)super.getCurrentLoginUser();
			Page<TranscationLogVO> log = facade.pagePermissionTrLog(parseSort(),user.getId(),pageLog,query, super.getPrimKeys());
//			log.setPageSize(Constants.PAGE_NUM);
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
	
	public String getResourceType() {
		return resourceType;
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
	
	
	

}
