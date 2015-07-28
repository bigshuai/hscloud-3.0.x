/* 
 * 文 件 名:  ZoneGroupAction.java 
 * 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
 * 描    述:  <描述> 
 * 修 改 人:  ljg 
 * 修改时间:  2013-6-18 
 * 跟踪单号:  <跟踪单号> 
 * 修改单号:  <修改单号> 
 * 修改内容:  <修改内容> 
 */
package com.hisoft.hscloud.web.action;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hisoft.hscloud.bss.sla.sc.entity.ZoneGroup;
import com.hisoft.hscloud.bss.sla.sc.vo.ZoneGroupVO;
import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.web.facade.Facade;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author houyh
 * @version [版本号, 2013-6-24]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ZoneGroupAction extends HSCloudAction {

	/**
	 * 注释内容
	 */
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private Facade facade;
	private final String resourceType = "com.hisoft.hscloud.bss.sla.sc.entity.ZoneGroup";
	private long zoneGroupId;// 资源域组Id
	private long referenceId;

	/**
	 * <查询所有ZoneGroup> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public String getAllZoneGroups() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAllZoneGroups method.");
		}
		User user = (User) super.getCurrentLoginUser();
		if (user == null) {
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		List<ZoneGroup> zoneGroupList = null;
		try {
			zoneGroupList = facade.getAllZoneGroups();
		} catch (Exception ex) {
			dealThrow(new HsCloudException(
					Constants.SC_ZONEGROUP_LIST_EXCEPTION,
					"getAllZoneGroups Exception:", logger, ex),
					Constants.SC_ZONEGROUP_LIST_EXCEPTION);
		}
		super.fillActionResult(zoneGroupList);
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAllZoneGroups method.takeTime:" + takeTime
					+ "ms");
		}
		return null;
	}
	
	/**
	 * <查询所有ZoneGroup> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public String getAllZoneGroupsByUser() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAllZoneGroups method.");
		}
		User user = (User) super.getCurrentLoginUser();
		if (user == null) {
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		List<ZoneGroupVO> zoneGroupList = null;
		try {
			Domain domain=user.getDomain();
			if(domain!=null){
				zoneGroupList = facade.getAllZoneGroupByUser(user.getLevel(),domain.getId());
			}
		} catch (Exception ex) {
			dealThrow(new HsCloudException(
					Constants.SC_ZONEGROUP_LIST_EXCEPTION,
					"getAllZoneGroups Exception:", logger, ex),
					Constants.SC_ZONEGROUP_LIST_EXCEPTION);
		}
		super.fillActionResult(zoneGroupList);
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAllZoneGroups method.takeTime:" + takeTime
					+ "ms");
		}
		return null;
	}
	/**
	 * <查询所有的可按需购买的机房线路> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String getCustomZoneGroupsByUser() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAllZoneGroups method.");
		}
		User user = (User) super.getCurrentLoginUser();
		if (user == null) {
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		List<ZoneGroupVO> zoneGroupList = null;
		try {
			Domain domain=user.getDomain();
			if(domain!=null){
				zoneGroupList = facade.getCustomZoneGroupByUser(user.getLevel(),domain.getId());
			}
		} catch (Exception ex) {
			dealThrow(new HsCloudException(
					Constants.SC_ZONEGROUP_LIST_EXCEPTION,
					"getAllZoneGroups Exception:", logger, ex),
					Constants.SC_ZONEGROUP_LIST_EXCEPTION);
		}
		super.fillActionResult(zoneGroupList);
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAllZoneGroups method.takeTime:" + takeTime
					+ "ms");
		}
		return null;
	}
	/**
	 * 根据referenceId 获取zonecode 再根据zonecode获取zoneGroupCode
	 */
	public void getZoneGroupCodeByReferenceId(){
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getZoneGroupCodeByReferenceId method.");
		}
		String zoneGroupCode="";
		try {
			zoneGroupCode=facade.getZoneGroupCodeByReferenceId(referenceId);
		} catch (Exception ex) {
			dealThrow(new HsCloudException(
					Constants.SC_ZONEGROUP_LIST_EXCEPTION,
					"getZoneGroupCodeByReferenceId Exception:", logger, ex),
					Constants.SC_ZONEGROUP_LIST_EXCEPTION);
		}
		super.getActionResult().setResultObject(zoneGroupCode);
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getZoneGroupCodeByReferenceId method.takeTime:" + takeTime
					+ "ms");
		}
	}


	public long getZoneGroupId() {
		return zoneGroupId;
	}

	public void setZoneGroupId(long zoneGroupId) {
		this.zoneGroupId = zoneGroupId;
	}

	public long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(long referenceId) {
		this.referenceId = referenceId;
	}
	
	

}
