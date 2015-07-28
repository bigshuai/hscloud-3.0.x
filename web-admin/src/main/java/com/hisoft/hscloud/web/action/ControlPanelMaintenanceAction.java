package com.hisoft.hscloud.web.action;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.systemmanagement.vo.ControlPanelVO;
import com.hisoft.hscloud.web.facade.Facade;

public class ControlPanelMaintenanceAction extends HSCloudAction {
	private Logger logger = Logger
			.getLogger(ControlPanelMaintenanceAction.class);
	private int limit;
	private int page;
	private String query;
	@Autowired
	private Facade facade;
	/**
	 * 
	 * @throws Exception
	 */
	public void getControlPanelByPage() throws Exception {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getControlPanelByPage method.");
		}
		try {
			Admin admin = (Admin) super.getCurrentLoginUser();
			if (admin != null) {
				Page<ControlPanelVO> paging = new Page<ControlPanelVO>(
						limit);
				paging.setPageNo(page);
				if (StringUtils.isNotBlank(query)) {
					query = new String(query.getBytes("iSO8859_1"), "UTF-8");
				}
				paging = facade.getControlPanelVOByPage(paging, query, admin);
				fillActionResult(paging);
			}

		} catch (Exception e) {
			dealThrow(new HsCloudException("", "getControlPanelByPage exception",
					logger, e), Constants.GET_CONTROL_PANEL_BY_PAGE_ERROR, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getControlPanelByPage method.takeTime:" + takeTime + "ms");
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

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
}
