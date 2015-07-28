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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.sc.entity.ZoneGroup;
import com.hisoft.hscloud.bss.sla.sc.utils.SCUtil;
import com.hisoft.hscloud.bss.sla.sc.vo.OsVO;
import com.hisoft.hscloud.bss.sla.sc.vo.ZoneVO;
import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.vo.ActionResult;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.web.facade.Facade;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-6-18] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class ZoneGroupAction extends HSCloudAction{

	/** 
	* 注释内容 
	*/
	private static final long serialVersionUID = -6469680273290623839L;
	private Logger logger = Logger.getLogger(this.getClass());	
	@Autowired
	private Facade facade;
	private ZoneGroup zoneGroup;
	private final String resourceType = "com.hisoft.hscloud.bss.sla.sc.entity.ZoneGroup";
	private int page;
    private int limit;
    private String code;//区域编码
    private Page<ZoneGroup> pageZoneGroup = new Page<ZoneGroup>();
    private String type;// 查询类型
	private String query;// 模糊查询条件	
	private long zoneGroupId;//资源域组Id
	private String zoneName;//资源域名称
	private Page<ZoneVO> pageZoneVO = new Page<ZoneVO>();
	private Long[] zoneIds;
	private Long[] domainIds;
	private int[] osIds;
	private Page<OsVO> pageOsVo = new Page<OsVO>();
	public ZoneGroup getZoneGroup() {
		return zoneGroup;
	}
	public void setZoneGroup(ZoneGroup zoneGroup) {
		this.zoneGroup = zoneGroup;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Page<ZoneGroup> getPageZoneGroup() {
		return pageZoneGroup;
	}
	public void setPageZoneGroup(Page<ZoneGroup> pageZoneGroup) {
		this.pageZoneGroup = pageZoneGroup;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getResourceType() {
		return resourceType;
	}
	public long getZoneGroupId() {
		return zoneGroupId;
	}
	public void setZoneGroupId(long zoneGroupId) {
		this.zoneGroupId = zoneGroupId;
	}
	public String getZoneName() {
		return zoneName;
	}
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	public Page<ZoneVO> getPageZoneVO() {
		return pageZoneVO;
	}
	public void setPageZoneVO(Page<ZoneVO> pageZoneVO) {
		this.pageZoneVO = pageZoneVO;
	}
	public Long[] getZoneIds() {
		return zoneIds;
	}
	public void setZoneIds(Long[] zoneIds) {
		this.zoneIds = zoneIds;
	}
	public Long[] getDomainIds() {
		return domainIds;
	}
	public void setDomainIds(Long[] domainIds) {
		this.domainIds = domainIds;
	}
	public int[] getOsIds() {
		return osIds;
	}
	public void setOsIds(int[] osIds) {
		this.osIds = osIds;
	}
	/**
	 * <添加ZoneGroup> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public String createZoneGroup(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter createZoneGroup method.");			
		}
		ActionResult result = getActionResult();
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		try {
			zoneGroup = SCUtil.strutsJson2Object(ZoneGroup.class);
			if(facade.hasSameZoneGroupName(zoneGroup)){
				super.fillActionResult(Constants.SC_ZONEGROUP_NAME_EXISTS);
				return null;
			}
			if(facade.hasSameZoneGroupCode(zoneGroup)){
				super.fillActionResult(Constants.SC_ZONEGROUP_CODE_EXISTS);
				return null;
			}
			zoneGroup.setCreateId(admin.getId());
			zoneGroup.setUpdateId(admin.getId());
			facade.createZoneGroup(zoneGroup,domainIds);
			facade.insertOperationLog(admin,"后台添加机房线路","后台添加机房线路",Constants.RESULT_SUCESS);
		} catch (Exception ex) {
			facade.insertOperationLog(admin,"后台添加机房线路错误:"+ex.getMessage(),"后台添加机房线路",Constants.RESULT_FAILURE);
			result.setResultCode(Constants.OPTIONS_FAILURE);
			dealThrow(new HsCloudException(Constants.SC_SAVE_ZONEGROUP_EXCEPTION,
					"createZoneGroup Exception:", logger, ex), Constants.SC_SAVE_ZONEGROUP_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit createZoneGroup method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <设置ZoneGroup启用、禁用> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public String setZoneGroupEnable(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter setZoneGroupEnable method.");			
		}
		ActionResult result = getActionResult();
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		try {
			ZoneGroup group=facade.getZoneGroupById(zoneGroup.getId());
			group.setUpdateId(admin.getId());
			group.setIsEnable(zoneGroup.getIsEnable());
			facade.updateZoneGroup(group,null);
			facade.insertOperationLog(admin,"后台启用/禁用机房线路","后台启用/禁用机房线路",Constants.RESULT_SUCESS);
		}catch (Exception ex) {	
			facade.insertOperationLog(admin,"后台启用/禁用机房线路错误:"+ex.getMessage(),"后台启用/禁用机房线路",Constants.RESULT_FAILURE);
			result.setSuccess(false);
			result.setResultCode(Constants.OPTIONS_FAILURE);
			dealThrow(new HsCloudException(Constants.SC_SET_ZONEGROUP_STATUS_EXCEPTION,
					"setZoneGroupEnable Exception:", logger, ex), Constants.SC_SET_ZONEGROUP_STATUS_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit setZoneGroupEnable method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <修改ZoneGroup> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public String updateZoneGroup(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter updateZoneGroup method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		boolean hasSameName = facade.hasSameZoneGroupName(zoneGroup);
		ActionResult result = getActionResult();		
		try {
			ZoneGroup group=facade.getZoneGroupById(zoneGroup.getId());
			if(group.getName().equals(zoneGroup.getName())){
				hasSameName = false;
			}
			if(!hasSameName){
				group.setUpdateId(admin.getId());
				group.setName(zoneGroup.getName());
				group.setDescription(zoneGroup.getDescription());
				facade.updateZoneGroup(group,domainIds);
			}else{
				super.fillActionResult(Constants.SC_ZONEGROUP_NAME_EXISTS);
				return null;
			}
			facade.insertOperationLog(admin,"后台修改机房线路","后台修改机房线路",Constants.RESULT_SUCESS);
		}catch (Exception ex) {	
			facade.insertOperationLog(admin,"后台修改机房线路错误:"+ex.getMessage(),"后台修改机房线路",Constants.RESULT_FAILURE);
			result.setSuccess(false);
			result.setResultCode(Constants.OPTIONS_FAILURE);
			dealThrow(new HsCloudException(Constants.SC_UPDATE_ZONEGROUP_EXCEPTION,
					"updateZoneGroup Exception:", logger, ex), Constants.SC_UPDATE_ZONEGROUP_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit updateZoneGroup method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <查询所有ZoneGroup> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public String getAllZoneGroups(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAllZoneGroups method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		List<ZoneGroup> zoneGroupList = null; 
		try{
			zoneGroupList = facade.getAllZoneGroups();
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.SC_ZONEGROUP_LIST_EXCEPTION,
					"getAllZoneGroups Exception:", logger, ex), Constants.SC_ZONEGROUP_LIST_EXCEPTION);
		}
		super.fillActionResult(zoneGroupList);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAllZoneGroups method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <分页查询ZoneGroup> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String findAllZoneGroupByPage() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findAllZoneGroupByPage method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
        int pageNo = page;
        int pageSize = limit;
        try {
        	if(query!=null && !"".equals(query)){
				query = new String(query.getBytes("iso8859_1"), "UTF-8");
			}
            pageZoneGroup.setPageNo(pageNo);
            pageZoneGroup.setPageSize(pageSize);            
            facade.findZoneGroup(pageZoneGroup, type, query);            
            super.fillActionResult(pageZoneGroup);
        } catch (Exception ex) {
            dealThrow(new HsCloudException(Constants.SC_ZONEGROUP_LIST_EXCEPTION,
                    "findAllZoneGroupByPage Exception:", logger, ex), Constants.SC_ZONEGROUP_LIST_EXCEPTION);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findAllZoneGroupByPage method.takeTime:" + takeTime + "ms");
		}
        return null;
    }
	public String findRelatedServerZoneByPage() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findRelatedServerZoneByPage method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
        int pageNo = page;
        int pageSize = limit;
        try {
        	if(query!=null && !"".equals(query)){
				query = new String(query.getBytes("iso8859_1"), "UTF-8");
			}
        	pageZoneVO.setPageNo(pageNo);
        	pageZoneVO.setPageSize(pageSize);            
            facade.getRelatedServerZone(pageZoneVO, zoneGroupId, query);            
            super.fillActionResult(pageZoneVO);
        } catch (Exception ex) {
            dealThrow(new HsCloudException(Constants.SC_ZONEGROUP_LIST_EXCEPTION,
                    "findAllZoneGroupByPage Exception:", logger, ex), Constants.SC_ZONEGROUP_LIST_EXCEPTION);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findAllZoneGroupByPage method.takeTime:" + takeTime + "ms");
		}
        return null;
    }
	public String findUnRelatedServerZoneByPage() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findUnRelatedServerZoneByPage method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
        int pageNo = page;
        int pageSize = limit;
        try {
        	if(query!=null && !"".equals(query)){
				query = new String(query.getBytes("iso8859_1"), "UTF-8");
			}
        	pageZoneVO.setPageNo(pageNo);
        	pageZoneVO.setPageSize(pageSize);            
            facade.getUnRelatedServerZone(pageZoneVO, zoneGroupId, query);            
            super.fillActionResult(pageZoneVO);
        } catch (Exception ex) {
            dealThrow(new HsCloudException(Constants.SC_ZONEGROUP_LIST_EXCEPTION,
                    "findUnRelatedServerZoneByPage Exception:", logger, ex), Constants.SC_ZONEGROUP_LIST_EXCEPTION);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findUnRelatedServerZoneByPage method.takeTime:" + takeTime + "ms");
		}
        return null;
    }
	public String associateZoneAndZoneGroup(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter associateZoneAndZoneGroup method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}		
		ActionResult result = getActionResult();		
		try {
			boolean resultFlag = facade.associateZoneAndZoneGroup(zoneIds, zoneGroupId);
			if(!resultFlag){
				super.fillActionResult(Constants.SC_ZONEGROUP_NAME_EXISTS);
				return null;
			}
			facade.insertOperationLog(admin,"后台关联机房线路","后台关联机房线路",Constants.RESULT_SUCESS);
		}catch (Exception ex) {	
			facade.insertOperationLog(admin,"后台关联机房线路错误:"+ex.getMessage(),"后台关联机房线路",Constants.RESULT_FAILURE);
			result.setSuccess(false);
			result.setResultCode(Constants.OPTIONS_FAILURE);
			dealThrow(new HsCloudException(Constants.SC_UPDATE_ZONEGROUP_EXCEPTION,
					"associateZoneAndZoneGroup Exception:", logger, ex), Constants.SC_UPDATE_ZONEGROUP_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit associateZoneAndZoneGroup method.takeTime:" + takeTime + "ms");
		}
		return null;		
	}
	public String disAssociateZoneAndZoneGroup(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter disAssociateZoneAndZoneGroup method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}		
		ActionResult result = getActionResult();		
		try {
			boolean resultFlag = facade.disAssociateZoneAndZoneGroup(zoneIds, zoneGroupId);
			if(!resultFlag){
				super.fillActionResult(Constants.SC_ZONEGROUP_NAME_EXISTS);
				return null;
			}	
			facade.insertOperationLog(admin,"后台解除关联机房线路","后台解除关联机房线路",Constants.RESULT_SUCESS);
		}catch (Exception ex) {	
			facade.insertOperationLog(admin,"后台解除关联机房线路错误:"+ex.getMessage(),"后台解除关联机房线路",Constants.RESULT_FAILURE);
			result.setSuccess(false);
			result.setResultCode(Constants.OPTIONS_FAILURE);
			dealThrow(new HsCloudException(Constants.SC_UPDATE_ZONEGROUP_EXCEPTION,
					"disAssociateZoneAndZoneGroup Exception:", logger, ex), Constants.SC_UPDATE_ZONEGROUP_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit disAssociateZoneAndZoneGroup method.takeTime:" + takeTime + "ms");
		}
		return null;		
	}
	public String getAllZoneGroupIds(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAllZoneGroupIds method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		List<Long> groupIdList = null; 
		List<Long> ids = new ArrayList<Long>();
		try{
			for(int i = 0; i < zoneIds.length; i++){
				ids.add(zoneIds[i]);
			}
			groupIdList = facade.getAllZoneGroupIdsByZoneIds(ids);
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.SC_ZONEGROUP_LIST_EXCEPTION,
					"getAllZoneGroupIds Exception:", logger, ex), Constants.SC_ZONEGROUP_LIST_EXCEPTION);
		}
		super.fillActionResult(groupIdList);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAllZoneGroupIds method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	public String findUnRelatedServerOsByPage() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findUnRelatedServerOsByPage method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
        int pageNo = page;
        int pageSize = limit;
        try {
        	if(query!=null && !"".equals(query)){
				query = new String(query.getBytes("iso8859_1"), "UTF-8");
			}
        	pageOsVo.setPageNo(pageNo);
        	pageOsVo.setPageSize(pageSize);            
            facade.getUnRelatedServerOs(pageOsVo, zoneGroupId, query);            
            super.fillActionResult(pageOsVo);
        } catch (Exception ex) {
            dealThrow(new HsCloudException(Constants.SC_OS_LIST_EXCEPTION,
                    "findUnRelatedServerOsByPage Exception:", logger, ex), Constants.SC_OS_LIST_EXCEPTION);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findUnRelatedServerOsByPage method.takeTime:" + takeTime + "ms");
		}
        return null;
    }
	
	public String findRelatedServerOsByPage() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findUnRelatedServerOsByPage method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
        int pageNo = page;
        int pageSize = limit;
        try {
        	if(query!=null && !"".equals(query)){
				query = new String(query.getBytes("iso8859_1"), "UTF-8");
			}
        	pageOsVo.setPageNo(pageNo);
        	pageOsVo.setPageSize(pageSize);            
            facade.getRelatedServerOs(pageOsVo, zoneGroupId, query);            
            super.fillActionResult(pageOsVo);
        } catch (Exception ex) {
            dealThrow(new HsCloudException(Constants.SC_OS_LIST_EXCEPTION,
                    "findRelatedServerOsByPage Exception:", logger, ex), Constants.SC_OS_LIST_EXCEPTION);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findRelatedServerOsByPage method.takeTime:" + takeTime + "ms");
		}
        return null;
    }
	public String associateOsAndZoneGroup(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter associateOsAndZoneGroup method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}		
		ActionResult result = getActionResult();		
		try {
			boolean resultFlag = facade.associateOsAndZoneGroup(osIds, zoneGroupId);
			if(!resultFlag){
				super.fillActionResult(Constants.SC_ZONEGROUP_NAME_EXISTS);
				return null;
			}
			facade.insertOperationLog(admin,"OS关联机房线路","OS关联机房线路",Constants.RESULT_SUCESS);
		}catch (Exception ex) {	
			facade.insertOperationLog(admin,"OS关联机房线路错误:"+ex.getMessage(),"OS关联机房线路",Constants.RESULT_FAILURE);
			result.setSuccess(false);
			result.setResultCode(Constants.OPTIONS_FAILURE);
			dealThrow(new HsCloudException(Constants.SC_UPDATE_ZONEGROUP_EXCEPTION,
					"associateOsAndZoneGroup Exception:", logger, ex), Constants.SC_UPDATE_ZONEGROUP_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit associateOsAndZoneGroup method.takeTime:" + takeTime + "ms");
		}
		return null;		
	}
	
	public String disAssociateOsAndZoneGroup(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter associateOsAndZoneGroup method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}		
		ActionResult result = getActionResult();		
		try {
			boolean resultFlag = facade.disAssociateOsAndZoneGroup(osIds, zoneGroupId);
			if(!resultFlag){
				super.fillActionResult(Constants.SC_ZONEGROUP_NAME_EXISTS);
				return null;
			}
			facade.insertOperationLog(admin,"后台OS关联机房线路","后台OS关联机房线路",Constants.RESULT_SUCESS);
		}catch (Exception ex) {	
			facade.insertOperationLog(admin,"后台OS关联机房线路错误:"+ex.getMessage(),"后台OS关联机房线路",Constants.RESULT_FAILURE);
			result.setSuccess(false);
			result.setResultCode(Constants.OPTIONS_FAILURE);
			dealThrow(new HsCloudException(Constants.SC_UPDATE_ZONEGROUP_EXCEPTION,
					"associateOsAndZoneGroup Exception:", logger, ex), Constants.SC_UPDATE_ZONEGROUP_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit associateOsAndZoneGroup method.takeTime:" + takeTime + "ms");
		}
		return null;		
	}
}
