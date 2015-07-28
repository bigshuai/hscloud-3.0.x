package com.hisoft.hscloud.web.action; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.CPUHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.DiskHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.MemoryHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.NetHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.VmRealtimeMonitorVO;
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
	private String fromTime;
	private String toTime;
	private int page;
	private int limit;
	private String type;// 查询类型
	private String query;// 模糊查询条件	
	
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


	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
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
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<CPUHistoryVO> list = facade.getVmCPUHistory(vmId, Long.valueOf(fromTime), Long.valueOf(toTime));			
			map.put("totalCount", list.size());
			map.put("result", list);			
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_VM_HISTORY_EXCEPTION,
					"ossVmCPUHistory Exception:", logger, ex), Constants.OPTIONS_FAILURE);
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
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<MemoryHistoryVO> list = facade.getVmMemoryHistory(vmId, Long.valueOf(fromTime), Long.valueOf(toTime));			
			map.put("totalCount", list.size());
			map.put("result", list);			
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_VM_HISTORY_EXCEPTION,
					"ossVmMemoryHistory Exception:", logger, ex), Constants.OPTIONS_FAILURE);
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
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<DiskHistoryVO> list = facade.getVmDiskHistory(vmId, Long.valueOf(fromTime), Long.valueOf(toTime));			
			map.put("totalCount", list.size());
			map.put("result", list);			
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_VM_HISTORY_EXCEPTION,
					"ossVmDiskHistory Exception:", logger, ex), Constants.OPTIONS_FAILURE);
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
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<NetHistoryVO> list = facade.getVmNetHistory(vmId, Long.valueOf(fromTime), Long.valueOf(toTime));			
			map.put("totalCount", list.size());
			map.put("result", list);			
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_VM_HISTORY_EXCEPTION,
					"ossVmNetHistory Exception:", logger, ex), Constants.OPTIONS_FAILURE);
		}
		super.fillActionResult(map);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit ossVmNetHistory method.takeTime:" + takeTime + "ms");
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
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<VmRealtimeMonitorVO> list = facade.getVmRealTimeMonitor(vmId);			
			map.put("totalCount", list.size());
			map.put("result", list);			
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_VM_REALTIME_EXCEPTION,
					Constants.MONITOR_VM_REALTIME_EXCEPTION, logger, ex), "000");
		}
		super.fillActionResult(map);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit ossVmRealTime method.takeTime:" + takeTime + "ms");
		}
		return null;
	}

}
