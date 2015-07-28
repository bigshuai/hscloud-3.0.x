package com.hisoft.hscloud.vpdc.oss.monitoring.dao;

import java.util.Date;
import java.util.List;

import com.hisoft.hscloud.vpdc.oss.monitoring.entity.HostMonitorHistory;
import com.hisoft.hscloud.vpdc.oss.monitoring.entity.VmMonitorHistory;

public interface MonitorDao {
	/**
	 * 
	        * 此方法描述的是： 存储虚拟机的历史监控记录   
	        * @author: hongqin.li    
	        * @version: 2012-5-18 下午7:33:16
	 */
	public boolean saveVmMonitorHistory(List<VmMonitorHistory> vmMonitorHistories);

	/**
	 * 
	        * 此方法描述的是：    根据虚拟机ID 获取改虚拟机所有的历史监控记录
	        * @author: hongqin.li    
	        * @version: 2012-5-18 下午7:35:24
	 */
	public List<VmMonitorHistory> getVmMonitorHistoryByVmId(String vmId,Date fromTime,Date toDate);
	
	/**
	 * 
	        * 此方法描述的是：    根据订单项ID获取所有的历史监控记录
	        * 意义不太
	        * @author: hongqin.li    
	        * @version: 2012-5-18 下午7:36:51
	 */
	public List<VmMonitorHistory> getVmMonitorHistoryByOrderItemId(Long order_item_id);

	/**
	 * 
	        * 此方法描述的是：   存储主机的历史监控记录 
	        * @author: hongqin.li    
	        * @version: 2012-5-18 下午7:42:28
	 */
	public boolean saveHostMonitorHistory(List<HostMonitorHistory> hostMonitorHistories);

	/**
	 * 
	        * 此方法描述的是：  根据节点的Name 来获取节点的历史监控信息
	        * 节点的ID在重启的时候有可能会发生变化，name 被当做是节点的主键来使用  
	        * @author: hongqin.li    
	        * @version: 2012-5-18 下午7:44:48
	 */
	public List<HostMonitorHistory> getHostMonitorHistoryByHostName(String name,Date fromTime,Date toTime);
 
}
