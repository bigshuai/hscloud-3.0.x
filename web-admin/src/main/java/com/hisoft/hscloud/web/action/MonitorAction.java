package com.hisoft.hscloud.web.action; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;
import org.springside.modules.web.struts2.Struts2Utils;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.vpdc.ops.vo.VmInfoVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.CPUHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.DiskHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.HostInfoVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.HostOverviewInfoVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.MemoryHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.NetHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.VmDetailInfoVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.VmInfoMonitorVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.VmOverviewInfoVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.VmRealtimeMonitorVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.WholeOverviewInfoVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.ZoneOverviewInfoVO;
import com.hisoft.hscloud.web.facade.Facade;

public class MonitorAction extends HSCloudAction{

	/** 
	* 注释内容 
	*/
	private static final long serialVersionUID = 6315395863269186615L;
	private Logger logger = Logger.getLogger(this.getClass());	
	@Autowired
	private Facade facade;	
	private String vmId;
	private String hostName;
	private String nodeAliases;
	private long fromTime;
	private long toTime;
	private String fromDate;
	private String toDate;
	private int page;
	private int limit;
	private String type;// 查询类型
	private String query;// 模糊查询条件		
	private final String resourceType = "com.hisoft.hscloud.vpdc.ops.entity.VpdcReference";
	
	private final String classKey = "oss!findVmDetails";
	
	private Page<HostInfoVO> hostInfoVO = new Page<HostInfoVO>();
	private Page<VmInfoVO> vmInfoVO = new Page<VmInfoVO>();
	private Page<VmInfoMonitorVO> vmInfoMonitorVO = new Page<VmInfoMonitorVO>();
	private String zoneCode;
	public String getVmId() {
		return vmId;
	}

	public void setVmId(String vmId) {
		this.vmId = vmId;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public long getFromTime() {
		return fromTime;
	}

	public void setFromTime(long fromTime) {
		this.fromTime = fromTime;
	}

	public long getToTime() {
		return toTime;
	}

	public void setToTime(long toTime) {
		this.toTime = toTime;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
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
	public String getZoneCode() {
		return zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}

	public String getClassKey() {
		return classKey;
	}
	
	public String getNodeAliases() {
		return nodeAliases;
	}

	public void setNodeAliases(String nodeAliases) {
		this.nodeAliases = nodeAliases;
	}

	/**
	 * <全局概述数据集> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String getWholeOverviewInfo(){	
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getWholeOverviewInfo method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		List<Object> referenceIds = super.getPrimKeys();
		boolean isSpecial = facade.isSpecialAdmin(admin);
		if(!isSpecial){
			referenceIds = facade.getZoneIdsByAdminId(admin.getId());
		}
		Map<String ,Object> map = new HashMap<String, Object>();
		try{
			WholeOverviewInfoVO wholeOverviewInfoVO = facade.getWholeOverviewInfo(referenceIds);
			map.put("totalCount", 1);
			map.put("result", wholeOverviewInfoVO);			
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_WHOLE_OVERVIEW_EXCEPTION,
					"getWholeOverviewInfo Exception:", logger, ex), Constants.MONITOR_WHOLE_OVERVIEW_EXCEPTION);
		}
		super.fillActionResult(map);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getWholeOverviewInfo method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <资源域概述数据集> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String getZoneOverviewInfo(){	
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getZoneOverviewInfo method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		List<Object> referenceIds = super.getPrimKeys();
		boolean isSpecial = facade.isSpecialAdmin(admin);
		if(!isSpecial){
			referenceIds = facade.getZoneIdsByAdminId(admin.getId());
		}
		Map<String ,Object> map = new HashMap<String, Object>();
		try{
			ZoneOverviewInfoVO zoneOverviewInfoVO = facade.getZoneOverviewInfo(zoneCode,referenceIds);
			map.put("totalCount", 1);
			map.put("result", zoneOverviewInfoVO);			
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_ZONE_OVERVIEW_EXCEPTION,
					"getZoneOverviewInfo Exception:", logger, ex), Constants.MONITOR_ZONE_OVERVIEW_EXCEPTION);
		}
		super.fillActionResult(map);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getZoneOverviewInfo method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * <节点概述数据集> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String getHostOverviewInfo(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getHostOverviewInfo method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		Map<String ,Object> map = new HashMap<String, Object>();		
		try{
			HostOverviewInfoVO hostOverviewInfoVO = facade.getHostOverviewInfo(hostName);
			map.put("totalCount", 1);
			map.put("result", hostOverviewInfoVO);			
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_HOST_OVERVIEW_EXCEPTION,
					"getHostOverviewInfo Exception:", logger, ex), Constants.MONITOR_HOST_OVERVIEW_EXCEPTION);
		}
		super.fillActionResult(map);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getHostOverviewInfo method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * <虚拟机概述数据集> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String getVmOverviewInfo(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getVmOverviewInfo method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		Map<String ,Object> map = new HashMap<String, Object>();		
		try{
			VmOverviewInfoVO vmOverviewInfoVO = facade.getVmOverviewInfo(vmId);
			map.put("totalCount", 1);
			map.put("result", vmOverviewInfoVO);			
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_VM_OVERVIEW_EXCEPTION,
					"getVmOverviewInfo Exception:", logger, ex), Constants.MONITOR_VM_OVERVIEW_EXCEPTION);
		}
		super.fillActionResult(map);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getVmOverviewInfo method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * <节点列表数据集> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String findHostDetails(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findHostDetails method.");			
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
		int pageNo=page;
		int pageSize=limit;	
		try{
			if(query!=null && !"".equals(query)){
				query = new String(query.getBytes("iso8859_1"), "UTF-8");
			}
			hostInfoVO.setPageNo(pageNo);
			hostInfoVO.setPageSize(pageSize);
			hostInfoVO = facade.findHostDetailsByCondition(hostInfoVO, type, query,zoneCode,zoneIds);			
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_HOST_DETAILS_EXCEPTION,
					"findHostDetails Exception:", logger, ex), Constants.MONITOR_HOST_DETAILS_EXCEPTION);
		}
		super.fillActionResult(hostInfoVO);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findHostDetails method.takeTime:" + takeTime + "ms");
		}
		return null;		
	}
	
	/**
	 * <虚拟机列表数据集> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String findVmDetails(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findVmDetails method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		int pageNo=page;
		int pageSize=limit;
		List<Object> referenceIds = super.getPrimKeys();
		boolean isSpecial = facade.isSpecialAdmin(admin);
		if(!isSpecial){
			referenceIds = facade.getZoneIdsByAdminId(admin.getId());
		}
		try{
			if(query!=null && !"".equals(query)){
				query = new String(query.getBytes("iso8859_1"), "UTF-8");
			}
			vmInfoVO.setPageNo(pageNo);
			vmInfoVO.setPageSize(pageSize);
			vmInfoMonitorVO = facade.findVmDetailsByCondition(vmInfoVO, hostName, type, query,admin,referenceIds,zoneCode);			
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_VM_DETAILS_EXCEPTION,
					"findVmDetails Exception:", logger, ex), Constants.MONITOR_VM_DETAILS_EXCEPTION);
		}
		super.fillActionResult(vmInfoMonitorVO);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findVmDetails method.takeTime:" + takeTime + "ms");
		}
		return null;		
	}
	
	/**
	 * <虚拟机关联信息> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String getVmDetailInfo(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getVmDetailInfo method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		Map<String ,Object> map = new HashMap<String, Object>();
		try{
			VmDetailInfoVO vmDetailInfoVO = facade.getVmDetailInfo(vmId);
			map.put("totalCount", 1);
			map.put("result", vmDetailInfoVO);			
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_VM_DETAIL_EXCEPTION,
					"getVmDetailInfo Exception:", logger, ex), Constants.MONITOR_VM_DETAIL_EXCEPTION);
		}
		super.fillActionResult(map);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getVmDetailInfo method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * <虚拟机CPU历史监控 >
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String ossVmCPUHistory() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter ossVmCPUHistory method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try{
			List<CPUHistoryVO> list = facade.getVmCPUHistory(vmId, fromTime, toTime);			
			map.put("totalCount", list.size());
			map.put("result", list);			
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_VM_HISTORY_EXCEPTION,
					"ossVmCPUHistory Exception:", logger, ex), Constants.MONITOR_VM_HISTORY_EXCEPTION);
		}
		super.fillActionResult(map);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit ossVmCPUHistory method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * <虚拟机Memory历史监控 >
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String ossVmMemoryHistory() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter ossVmMemoryHistory method.");			
		}	
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<MemoryHistoryVO> list = facade.getVmMemoryHistory(vmId, fromTime, toTime);			
			map.put("totalCount", list.size());
			map.put("result", list);			
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_VM_HISTORY_EXCEPTION,
					"ossVmMemoryHistory Exception:", logger, ex), Constants.MONITOR_VM_HISTORY_EXCEPTION);
		}
		super.fillActionResult(map);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit ossVmMemoryHistory method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * <虚拟机Disk历史监控 >
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String ossVmDiskHistory() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter ossVmDiskHistory method.");			
		}	
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<DiskHistoryVO> list = facade.getVmDiskHistory(vmId, fromTime, toTime);			
			map.put("totalCount", list.size());
			map.put("result", list);			
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_VM_HISTORY_EXCEPTION,
					"ossVmDiskHistory Exception:", logger, ex), Constants.MONITOR_VM_HISTORY_EXCEPTION);
		}
		super.fillActionResult(map);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit ossVmDiskHistory method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * 虚拟机Net历史监控  
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String ossVmNetHistory() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter ossVmNetHistory method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<NetHistoryVO> list = facade.getVmNetHistory(vmId, fromTime, toTime);			
			map.put("totalCount", list.size());
			map.put("result", list);			
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_VM_HISTORY_EXCEPTION,
					"ossVmNetHistory Exception:", logger, ex), Constants.MONITOR_VM_HISTORY_EXCEPTION);
		}
		super.fillActionResult(map);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit ossVmNetHistory method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * <节点CPU历史监控> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String ossHostCPUHistory() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter ossHostCPUHistory method.");			
		}	
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			logger.info("String fromDate:"+fromDate+"String toDate:"+toDate);
			List<CPUHistoryVO> list = facade.getHostCPUHistory(hostName, fromTime, toTime);			
			map.put("totalCount", list.size());
			map.put("result", list);			
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_HOST_HISTORY_EXCEPTION,
					"ossHostCPUHistory Exception:", logger, ex), Constants.MONITOR_HOST_HISTORY_EXCEPTION);
		}
		super.fillActionResult(map);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit ossHostCPUHistory method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * <节点Memory历史监控 >
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String ossHostMemoryHistory() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter ossHostMemoryHistory method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<MemoryHistoryVO> list = facade.getHostMemoryHistory(hostName, fromTime, toTime);			
			map.put("totalCount", list.size());
			map.put("result", list);			
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_HOST_HISTORY_EXCEPTION,
					"ossHostMemoryHistory Exception:", logger, ex), Constants.MONITOR_HOST_HISTORY_EXCEPTION);
		}
		super.fillActionResult(map);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit ossHostMemoryHistory method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <节点Disk历史监控>  
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String ossHostDiskHistory() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter ossHostDiskHistory method.");			
		}	
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<DiskHistoryVO> list = facade.getHostDiskHistory(hostName, fromTime, toTime);			
			map.put("totalCount", list.size());
			map.put("result", list);			
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_HOST_HISTORY_EXCEPTION,
					"ossHostDiskHistory Exception:", logger, ex), Constants.MONITOR_HOST_HISTORY_EXCEPTION);
		}
		super.fillActionResult(map);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit ossHostDiskHistory method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <节点Net历史监控 >
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String ossHostNetHistory() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter ossHostNetHistory method.");			
		}	
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<NetHistoryVO> list = facade.getHostNetHistory(hostName, fromTime, toTime);			
			map.put("totalCount", list.size());
			map.put("result", list);			
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_HOST_HISTORY_EXCEPTION,
					"ossHostNetHistory Exception:", logger, ex), Constants.MONITOR_HOST_HISTORY_EXCEPTION);
		}
		super.fillActionResult(map);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit ossHostNetHistory method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * <虚拟机实时监控> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String ossVmRealTime() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter ossVmRealTime method.");			
		}	
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<VmRealtimeMonitorVO> list = facade.getVmRealTimeMonitor(vmId);			
			map.put("totalCount", list.size());
			map.put("result", list);			
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_VM_REALTIME_EXCEPTION,
					"ossVmRealTimeMonitor Exception:", logger, ex), Constants.MONITOR_VM_REALTIME_EXCEPTION);
		}
		super.fillActionResult(map);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit ossVmRealTime method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <虚拟中心管理树> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String getNodeAndVmTree(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getNodeAndVmTree method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		setActionResult(null);		
		List<Object> referenceIds = super.getPrimKeys();
		try{
			Struts2Utils.renderText(facade.getNodeAndVmTree(hostName,referenceIds));
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_HOST_VM_TREE_EXCEPTION,
					"getNodeAndVmTree Exception:", logger, ex), Constants.MONITOR_HOST_VM_TREE_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getNodeAndVmTree method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <获取节点使用率> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String getAllHostUsage() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAllHostUsage method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();		
		try{
			List<HostInfoVO> list = facade.getAllHostUsage(zoneCode);			
			map.put("totalCount", list.size());
			map.put("result", list);			
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_HOST_USAGE_EXCEPTION,
					"getAllHostUsage Exception:", logger, ex), Constants.MONITOR_HOST_USAGE_EXCEPTION);
		}
		super.fillActionResult(map);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAllHostUsage method.takeTime:" + takeTime + "ms");
		}
		return null;
	}

}
