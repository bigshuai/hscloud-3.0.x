/* 
* 文 件 名:  ZoneAction.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-5-8 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.action; 

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;
import org.springside.modules.web.struts2.Struts2Utils;

import com.hisoft.hscloud.bss.sla.sc.entity.ServerZone;
import com.hisoft.hscloud.bss.sla.sc.utils.SCUtil;
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
 * @version  [版本号, 2013-5-8] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class ZoneAction extends HSCloudAction{

	/** 
	* 注释内容 
	*/
	private static final long serialVersionUID = 1813506435313011023L;

	private Logger logger = Logger.getLogger(this.getClass());	
	@Autowired
	private Facade facade;
	private ServerZone serverZone;
	private final String resourceType = "com.hisoft.hscloud.bss.sla.sc.entity.ServerZone";
	private int page;
    private int limit;
    private String code;//区域编码
    private Page<ServerZone> pageZone = new Page<ServerZone>();
    private String type;// 查询类型
	private String query;// 模糊查询条件	
	private long zoneGroupId;//资源域组Id
	public ServerZone getServerZone() {
		return serverZone;
	}
	public void setServerZone(ServerZone serverZone) {
		this.serverZone = serverZone;
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
	public Page<ServerZone> getPageZone() {
		return pageZone;
	}
	public void setPageZone(Page<ServerZone> pageZone) {
		this.pageZone = pageZone;
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
	/**
	 * <添加Zone> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public String createZone(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter createZone method.");			
		}
		ActionResult result = getActionResult();
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		try {
			serverZone = SCUtil.strutsJson2Object(ServerZone.class);
			if(facade.hasSameZoneName(serverZone)){
				super.fillActionResult(Constants.SC_ZONE_NAME_EXISTS);
				return null;
			}
			if(facade.hasSameZoneCode(serverZone)){
				super.fillActionResult(Constants.SC_ZONE_CODE_EXISTS);
				return null;
			}
			serverZone.setCreateId(admin.getId());
			serverZone.setUpdateId(admin.getId());
			facade.createServerZone(serverZone);
			facade.insertOperationLog(admin,"后台添加资源域","后台添加资源域",Constants.RESULT_SUCESS);
		} catch (Exception ex) {
			facade.insertOperationLog(admin,"后台添加资源域错误:"+ex.getMessage(),"后台添加资源域",Constants.RESULT_FAILURE);
			result.setResultCode(Constants.OPTIONS_FAILURE);
			dealThrow(new HsCloudException(Constants.SC_SAVE_ZONE_EXCEPTION,
					"createZone Exception:", logger, ex), Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit createZone method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <设置Zone启用、禁用> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public String setZoneEnable(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter setZoneEnable method.");			
		}
		ActionResult result = getActionResult();
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		try {
			ServerZone zone=facade.getServerZoneById(serverZone.getId());
			zone.setUpdateId(admin.getId());
			zone.setIsEnable(serverZone.getIsEnable());
			facade.updateServerZone(zone);
			facade.insertOperationLog(admin,"后台启用/禁用资源域","后台启用/禁用资源域",Constants.RESULT_SUCESS);
		}catch (Exception ex) {	
			facade.insertOperationLog(admin,"后台启用/禁用资源域错误:"+ex.getMessage(),"后台启用/禁用资源域",Constants.RESULT_FAILURE);
			result.setSuccess(false);
			result.setResultCode(Constants.OPTIONS_FAILURE);
			dealThrow(new HsCloudException(Constants.SC_SET_ZONE_STATUS_EXCEPTION,
					"setZoneEnable Exception:", logger, ex), Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit setZoneEnable method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <修改Zone> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public String updateZone(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter updateZone method.");			
		}
		boolean hasSameName = facade.hasSameZoneName(serverZone);
		ActionResult result = getActionResult();
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		try {
			ServerZone zone=facade.getServerZoneById(serverZone.getId());
			if(zone.getName().equals(serverZone.getName())){
				hasSameName = false;
			}
			if(!hasSameName){
				zone.setUpdateId(admin.getId());
				zone.setName(serverZone.getName());
				zone.setCode(serverZone.getCode());
				zone.setIsDefault(serverZone.getIsDefault());
				zone.setIsCustom(serverZone.getIsCustom());
				zone.setDescription(serverZone.getDescription());
				facade.updateServerZone(zone);
			}else{
				super.fillActionResult(Constants.SC_ZONE_NAME_EXISTS);
				return null;
			}
			facade.insertOperationLog(admin,"后台修改资源域","后台修改资源域",Constants.RESULT_SUCESS);
		}catch (Exception ex) {	
			facade.insertOperationLog(admin,"后台修改资源域错误:"+ex.getMessage(),"后台修改资源域",Constants.RESULT_FAILURE);
			result.setSuccess(false);
			result.setResultCode(Constants.OPTIONS_FAILURE);
			dealThrow(new HsCloudException(Constants.SC_UPDATE_ZONE_EXCEPTION,
					"updateZone Exception:", logger, ex), Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit updateZone method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <设置主区域> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public String setDefaultServerZone(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter setDefaultServerZone method.");			
		}
		ActionResult result = getActionResult();
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		try {
			serverZone.setUpdateId(admin.getId());
			facade.setDefaultServerZone(serverZone);
			facade.insertOperationLog(admin,"后台设置主区域","后台设置主区域",Constants.RESULT_SUCESS);
		}catch (Exception ex) {	
			facade.insertOperationLog(admin,"后台设置主区域错误:"+ex.getMessage(),"后台设置主区域",Constants.RESULT_FAILURE);
			result.setSuccess(false);
			result.setResultCode(Constants.OPTIONS_FAILURE);
			dealThrow(new HsCloudException(Constants.SC_UPDATE_ZONE_EXCEPTION,
					"updateZone Exception:", logger, ex), Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit setDefaultServerZone method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <查询所有Zone> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public String getAllZones(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAllZones method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		List<Object> zoneIds = super.getPrimKeys();
		boolean isSpecial = facade.isSpecialAdmin(admin);
		if(!isSpecial){
			zoneIds = facade.getZoneIdsByAdminId(admin.getId());
		}
		List<ServerZone> serverZoneList = null; 
		try{
			serverZoneList = facade.getAllZones(zoneIds);
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.SC_ZONE_LIST_EXCEPTION,
					"getAllZones Exception:", logger, ex), Constants.OPTIONS_FAILURE);
		}
		super.fillActionResult(serverZoneList);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAllZones method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <分页查询Zone> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String findAllZoneByPage() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findAllZoneByPage method.");			
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
            pageZone.setPageNo(pageNo);
            pageZone.setPageSize(pageSize);            
            facade.findServerZone(pageZone, type, query);            
            super.fillActionResult(pageZone);
        } catch (Exception ex) {
            dealThrow(new HsCloudException(Constants.SC_ZONE_LIST_EXCEPTION,
                    "findAllZoneByPage Exception:", logger, ex), Constants.OPTIONS_FAILURE);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findAllZoneByPage method.takeTime:" + takeTime + "ms");
		}
        return null;
    }
	public String getServerZonesByCode(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getServerZonesByCode method.");			
		}	
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		List<ServerZone> serverZoneList = null; 
		try{
			serverZoneList = facade.getServerZonesByCode(code);
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.SC_ZONE_LIST_EXCEPTION,
					"getServerZonesByCode Exception:", logger, ex), Constants.OPTIONS_FAILURE);
		}
		super.fillActionResult(serverZoneList);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getServerZonesByCode method.takeTime:" + takeTime + "ms");
		}
		return null;		
	}
	/**
	 * <查询资源域树> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String getZoneTree(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getZoneTree method.");			
		}	
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		setActionResult(null);
		List<Object> zoneIds = super.getPrimKeys();
		boolean isSpecial = facade.isSpecialAdmin(admin);
		if(!isSpecial){
			zoneIds = facade.getZoneIdsByAdminId(admin.getId());
		}		
		try{
			Struts2Utils.renderText(facade.getZoneTree(zoneIds));
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_HOST_VM_TREE_EXCEPTION,
					"getZoneTree Exception:", logger, ex), Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getZoneTree method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	public String getServerZonesByGroupId(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getServerZonesByGroupId method.");			
		}	
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		List<ServerZone> serverZoneList = null; 
		try{
			serverZoneList = facade.getAllZonesByGroupId(zoneGroupId);
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.SC_ZONE_LIST_EXCEPTION,
					"getServerZonesByGroupId Exception:", logger, ex), Constants.OPTIONS_FAILURE);
		}
		super.fillActionResult(serverZoneList);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getServerZonesByGroupId method.takeTime:" + takeTime + "ms");
		}
		return null;		
	}
	
	public void getZoneByAdmin(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getZoneByAdmin method.");			
		}	
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
		}
		List<ZoneVO> serverZoneList = null; 
		try{
			serverZoneList = facade.getZoneByAdmin(admin);
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.SC_ZONE_LIST_EXCEPTION,
					"getZoneByAdmin Exception:", logger, ex), Constants.OPTIONS_FAILURE);
		}
		super.fillActionResult(serverZoneList);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getZoneByAdmin method.takeTime:" + takeTime + "ms");
		}
	}
}
