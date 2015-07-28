package com.hisoft.hscloud.web.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.vo.PrivilegeVO;
import com.hisoft.hscloud.web.facade.Facade;

public class PermissionAction extends HSCloudAction {

	/**
	 * 注释内容
	 */
	private static final long serialVersionUID = 3856349540251006111L;

	private Logger logger = Logger.getLogger(this.getClass());

	private String resourceType = "com.hisoft.hscloud.vpdc.ops.entity.VpdcReference";
	// 模糊查询条件
	private String query;

	private int page;

	private int start;
	
	private int limit;

	private String sort;

	private String groupId;

	private String actionValue;

	private String permissionValue;

	private String resourceValue;

	@Autowired
	private Facade facade;

	private Page<PrivilegeVO> pagePrivilege = new Page<PrivilegeVO>(
			Constants.PAGE_NUM);

	public String getResourceType() {
		return resourceType;
	}

	public void findUnassignedList() {
		long beginRunTime = 0;
		beginRunTime = System.currentTimeMillis();
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findUnassignedList method.");
		}
		try {
			/*
			 * if (!StringUtils.isEmpty(query)) { // 如果查询框不为空时，进入模糊查询 query =
			 * new String(query.getBytes("iso8859_1"), "UTF-8"); }
			 */

			pagePrivilege.setPageNo(page);
			pagePrivilege.setPageSize(limit);
			List<PrivilegeVO> list = new ArrayList<PrivilegeVO>();
			List<Object> primKeys = getPrimKeys();
			if (primKeys != null && primKeys.size() > 0)
				list = facade.findUnassignedList(resourceType,
						Long.valueOf(groupId), pagePrivilege, query,
						getPrimKeys());
			pagePrivilege.setResult(list);
			fillActionResult(pagePrivilege);
		} catch (Exception e) {
			dealThrow(new HsCloudException(Constants.ROLE_FIND_EXCEPTION,
					logger, e), Constants.ROLE_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findUnassignedList method.takeTime:" + takeTime + "ms");
		}
		long takeTime = System.currentTimeMillis() - beginRunTime;
		System.out.println(takeTime);
	}

	public void findAssignedList() {
		long beginRunTime = 0;
		beginRunTime = System.currentTimeMillis();
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findAssignedList method.");
		}
		try {
			/*
			 * if (!StringUtils.isEmpty(query)) { // 如果查询框不为空时，进入模糊查询 query =
			 * new String(query.getBytes("iso8859_1"), "UTF-8"); }
			 */

			pagePrivilege.setPageNo(page);
			pagePrivilege.setPageSize(limit);
			List<PrivilegeVO> list = new ArrayList<PrivilegeVO>();
			List<Object> primKeys = getPrimKeys();
			if (primKeys != null && primKeys.size() > 0)
				list = facade.findAssignedList(resourceType,
						Long.valueOf(groupId), pagePrivilege, query,
						getPrimKeys());
			pagePrivilege.setResult(list);
			fillActionResult(pagePrivilege);
		} catch (Exception e) {
			dealThrow(new HsCloudException(Constants.ROLE_FIND_EXCEPTION,
					logger, e), Constants.ROLE_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findAssignedList method.takeTime:" + takeTime + "ms");
		}
		long takeTime = System.currentTimeMillis() - beginRunTime;
		System.out.println(takeTime);
	}

	public void findUiformDefList() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findUiformDefList method.");			
		}
		try {
			List<PrivilegeVO> list = facade.findUiformDefList(resourceType,
					Long.valueOf(groupId));

			pagePrivilege.setResult(list);
			pagePrivilege.setTotalCount(1l);
			fillActionResult(pagePrivilege);
		} catch (Exception e) {
			dealThrow(new HsCloudException(Constants.ROLE_FIND_EXCEPTION,
					logger, e), Constants.ROLE_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findUiformDefList method.takeTime:" + takeTime + "ms");
		}
	}

	public void addPermission() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter addPermission method.");			
		}
		User user=(User)super.getCurrentLoginUser();
		String operateObject = "UserGroup[userGroupId:" + groupId + "]";
		try {
			facade.modifyRolePermissiion(permissionValue, actionValue,
					resourceValue, Long.valueOf(groupId));
			facade.insertOperationLog(user,"虚拟机赋权","虚拟机赋权",Constants.RESULT_SUCESS,operateObject);
		} catch (Exception e) {
		    facade.insertOperationLog(user,"虚拟机赋权错误：" + e.getMessage(),"生成未支付订单",Constants.RESULT_FAILURE,operateObject);
		    dealThrow(new HsCloudException(Constants.ROLE_MODIFY_EXCEPTION,
					logger, e), Constants.ROLE_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit addPermission method.takeTime:" + takeTime + "ms");
		}
	}

	public void setResourceType(String resourceType) {
		if (null == resourceType || "".equals(resourceType)) {
			return;
		}
		this.resourceType = resourceType;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
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

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getPermissionValue() {
		return permissionValue;
	}

	public void setPermissionValue(String permissionValue) {
		this.permissionValue = permissionValue;
	}

	public String getResourceValue() {
		return resourceValue;
	}

	public void setResourceValue(String resourceValue) {
		this.resourceValue = resourceValue;
	}

	public Page<PrivilegeVO> getPagePrivilege() {
		return pagePrivilege;
	}

	public void setPagePrivilege(Page<PrivilegeVO> pagePrivilege) {
		this.pagePrivilege = pagePrivilege;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

}
