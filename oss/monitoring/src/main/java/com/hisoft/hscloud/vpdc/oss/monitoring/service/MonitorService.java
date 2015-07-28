package com.hisoft.hscloud.vpdc.oss.monitoring.service;

import java.util.List;

import org.openstack.model.hscloud.impl.HostAcquisition;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.vpdc.ops.vo.VmInfoVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.HostAcquisitionBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.ServerAcquisitionBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.CPUHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.DiskHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.HostInfoVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.HostOverviewInfoVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.MemoryHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.NetHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.VmInfoMonitorVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.VmOverviewInfoVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.VmRealtimeMonitorVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.WholeOverviewInfoVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.ZoneOverviewInfoVO;
/**
 * 
* <监控接口> 
* <功能详细描述> 
* 
* @author  ljg 
* @version  [版本号, 2012-11-17] 
* @see  [相关类/方法] 
* @since  [产品/模块版本]
 */
public interface MonitorService {
	
	/**
	 * <获取全局概述数据集> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public WholeOverviewInfoVO getWholeOverviewInfo() throws HsCloudException;
	/**
	 * <获取资源域概述数据集> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public ZoneOverviewInfoVO getZoneOverviewInfo(String zoneCode) throws HsCloudException;
	/**
	 * <获取节点概述数据集> 
	* <功能详细描述> 
	* @param hostName
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public HostOverviewInfoVO getHostOverviewInfo(String hostName) throws HsCloudException;
	/**
	 * <获取虚拟机概述数据集> 
	* <功能详细描述> 
	* @param vmId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public VmOverviewInfoVO getVmOverviewInfo(String vmId) throws HsCloudException;
	/**
	 * <获取节点列表数据集> 
	* <功能详细描述> 
	* @param page
	* @param field
	* @param fieldValue
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<HostInfoVO> findHostDetailsByCondition(Page<HostInfoVO> page,String field,
			String fieldValue) throws HsCloudException;
	/**
	 * <获取虚拟机列表数据集> 
	* <功能详细描述> 
	* @param page
	* @param hostName
	* @param field
	* @param fieldValue
	* @param admin
	* @param referenceIds
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<VmInfoVO> findVmDetailsByCondition(Page<VmInfoVO> page,String hostName,String field,
			String fieldValue,List<Object> referenceIds) throws HsCloudException;
	
	/**
	 * <虚拟机CPU历史监控>  
	* <功能详细描述> 
	* @param vmId
	* @param fromTime
	* @param toTime
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<CPUHistoryVO> getVmCPUHistory(String vmId,long fromTime,long toTime) throws HsCloudException;
	/**
	 * <虚拟机Memory历史监控> 
	* <功能详细描述> 
	* @param vmId
	* @param fromTime
	* @param toTime
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<MemoryHistoryVO> getVmMemoryHistory(String vmId,long fromTime,long toTime) throws HsCloudException;
	/**
	 * <虚拟机Disk历史监控> 
	* <功能详细描述> 
	* @param vmId
	* @param fromTime
	* @param toTime
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<DiskHistoryVO> getVmDiskHistory(String vmId,long fromTime,long toTime) throws HsCloudException;
	/**
	 * <虚拟机Net历史监控>  
	* <功能详细描述> 
	* @param vmId
	* @param fromTime
	* @param toTime
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<NetHistoryVO> getVmNetHistory(String vmId,long fromTime,long toTime) throws HsCloudException;
	/**
	 * <节点CPU历史监控> 
	* <功能详细描述> 
	* @param hostName
	* @param fromTime
	* @param toTime
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<CPUHistoryVO> getHostCPUHistory(String hostName,long fromTime,long toTime) throws HsCloudException;
	/**
	 * <节点Memory历史监控> 
	* <功能详细描述> 
	* @param hostName
	* @param fromTime
	* @param toTime
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<MemoryHistoryVO> getHostMemoryHistory(String hostName,long fromTime,long toTime) throws HsCloudException;
	/**
	 * <节点Disk历史监控> 
	* <功能详细描述> 
	* @param hostName
	* @param fromTime
	* @param toTime
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<DiskHistoryVO> getHostDiskHistory(String hostName,long fromTime,long toTime) throws HsCloudException;
	/**
	 * <节点Net历史监控> 
	* <功能详细描述> 
	* @param hostName
	* @param fromTime
	* @param toTime
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<NetHistoryVO> getHostNetHistory(String hostName,long fromTime,long toTime) throws HsCloudException;
	/**
	 * <虚拟机实时监控>
	* <功能详细描述> 
	* @param vmId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<VmRealtimeMonitorVO> getVmRealTimeMonitor(String vmId) throws HsCloudException;
	/**
	 * <获得各节点资源使用率> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<HostInfoVO> getAllHostUsage(List<HostInfoVO> hostInfoVOList) throws HsCloudException;
	/**
	 * <获得所有节点监控信息> 
	* <功能详细描述> 
	* @param hostInfoVOList
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<HostAcquisition> getHostAcquisition() throws HsCloudException;
	/**
	 * <获取某节点监控信息> 
	* <功能详细描述> 
	* @param hostName
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public HostAcquisitionBean getHostAcquisitionBean(String hostName) throws HsCloudException;
	/**
	 * <获取某虚拟机监控信息> 
	* <功能详细描述> 
	* @param vmUUID
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public ServerAcquisitionBean getServerAcquisitionBean(String vmUUID) throws HsCloudException;
	/**
	 * <填充某虚拟机监控信息> 
	* <功能详细描述> 
	* @param vmInfoMonitorVO
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void fillServerMonitoringInformation(VmInfoMonitorVO vmInfoMonitorVO) throws HsCloudException;
	/**
	 * <填充某节点监控信息> 
	* <功能详细描述> 
	* @param hostInfoVO
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void fillHostMonitoringInformation(HostInfoVO hostInfoVO) throws HsCloudException;
}
