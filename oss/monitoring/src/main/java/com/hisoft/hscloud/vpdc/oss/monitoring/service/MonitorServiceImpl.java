package com.hisoft.hscloud.vpdc.oss.monitoring.service;

import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openstack.api.compute.WqServerResource;
import org.openstack.model.hscloud.impl.CPUHistory;
import org.openstack.model.hscloud.impl.DISKHistory;
import org.openstack.model.hscloud.impl.HistoryMonitor;
import org.openstack.model.hscloud.impl.HostAcquisition;
import org.openstack.model.hscloud.impl.NETHistory;
import org.openstack.model.hscloud.impl.RAMHistory;
import org.springframework.stereotype.Service;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.OpenstackUtil;
import com.hisoft.hscloud.common.util.RedisUtil;
import com.hisoft.hscloud.vpdc.ops.vo.VmInfoVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.CPULoadAvgBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.CPUMonitorDetailBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.CPUSingleDetailBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.DISKMonitorBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.DISKMonitorDetailBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.DISKSingleDetailBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.HostAcquisitionBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.IOPSMonitorDetailBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.IOPSSingleDetailBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.NETMonitorBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.NETMonitorDetailBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.NETSingleDetailBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.OverallAcquisitionBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.ServerAcquisitionBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.ServerMonitorBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.ZoneAcquisitionBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.CPUHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.CPUMonitorVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.DiskDetailVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.DiskHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.DiskMonitorVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.HostInfoVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.HostOverviewInfoVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.MemoryHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.MemoryMonitorVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.NetHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.NetMonitorVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.NetworkDetailVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.VmInfoMonitorVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.VmOverviewInfoVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.VmRealtimeMonitorVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.WholeOverviewInfoVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.ZoneOverviewInfoVO;

@Service
public class MonitorServiceImpl implements MonitorService {
	private Logger logger = Logger.getLogger(this.getClass());
	
	private OpenstackUtil openstackUtil = new OpenstackUtil();
	private HistoryMonitor historyMonitor = null;	
/*------------2012-09-21添加接口----------------*/
	@Override
	public WholeOverviewInfoVO getWholeOverviewInfo() throws HsCloudException {
		WholeOverviewInfoVO wholeOverviewInfo =new WholeOverviewInfoVO();
		// 获取各个节点的所有资源配备比
		int hostTotal = 0;//节点总数
		int hostActiveNum = 0;//活动的节点数
		int virtualCPUUsed = 0;//虚拟的CPU使用量
		int virtualCPUApply = 0;//虚拟的CPU申请量			
		int virtualMemoryUsed = 0;//虚拟的Memory使用量
		int virtualMemoryApply = 0;//虚拟的Memory申请量			
		int virtualDiskUsed = 0;//虚拟的Disk使用量
		int virtualDiskApply = 0;//虚拟的Disk申请量
		double expandDiskUsed = 0;//扩展盘使用的容量
		double expandDiskTotal = 0;//扩展盘总共的容量
		int virtualCPUTotal = 0;//虚拟的CPU总量
		int virtualMemoryTotal = 0;//虚拟的Memory总量
		int virtualDiskTotal = 0;//虚拟的Disk总量       
    	try{
    		String jsonStr = RedisUtil.getValue("OverallAcquisition");
    		if(StringUtils.isNotBlank(jsonStr)){
    			JSONObject jsonObject = JSONObject.fromObject(jsonStr);	
    	        OverallAcquisitionBean overallAcquisitionBean=(OverallAcquisitionBean) JSONObject.toBean(jsonObject, OverallAcquisitionBean.class);
    	        //节点总数
    	        hostTotal = (overallAcquisitionBean.getHostTotal()!=null?overallAcquisitionBean.getHostTotal():0);
    	        //节点活动数
    	        hostActiveNum = (overallAcquisitionBean.getHostActive()!=null?overallAcquisitionBean.getHostActive():0);
    	        //虚拟的CPU使用量
    	        virtualCPUUsed = (overallAcquisitionBean.getCPUUsed()!=null?overallAcquisitionBean.getCPUUsed():0);
    	        //虚拟的CPU申请量
    	        virtualCPUApply = (overallAcquisitionBean.getCPUApply()!=null?overallAcquisitionBean.getCPUApply():0);
    	        //虚拟的CPU总量
    	        virtualCPUTotal = (overallAcquisitionBean.getCPUTotal()!=null?overallAcquisitionBean.getCPUTotal():0);
    	        //虚拟的Memory使用量
    	        virtualMemoryUsed = (overallAcquisitionBean.getMemoryUsed()!=null?overallAcquisitionBean.getMemoryUsed():0);
    	        //虚拟的Memory申请量
    	        virtualMemoryApply = (overallAcquisitionBean.getMemoryApply()!=null?overallAcquisitionBean.getMemoryApply():0);
    	        //虚拟的Memory总量
    	        virtualMemoryTotal = (overallAcquisitionBean.getMemoryTotal()!=null?overallAcquisitionBean.getMemoryTotal():0);
    	        //虚拟的Disk使用量
    	        virtualDiskUsed = (overallAcquisitionBean.getDiskUsed()!=null?overallAcquisitionBean.getDiskUsed():0);
    	        //虚拟的Disk申请量
    	        virtualDiskApply = (overallAcquisitionBean.getDiskApply()!=null?overallAcquisitionBean.getDiskApply():0);
    	        //虚拟的Disk总量
    	        virtualDiskTotal = (overallAcquisitionBean.getDiskTotal()!=null?overallAcquisitionBean.getDiskTotal():0);
    	        //扩展盘使用的容量
    	        expandDiskUsed = (overallAcquisitionBean.getVgUsed()!=null?overallAcquisitionBean.getVgUsed():0);
    	        //扩展盘总共的容量
    	        expandDiskTotal = (overallAcquisitionBean.getVgTotal()!=null?overallAcquisitionBean.getVgTotal():0);
    		}        		
    	}catch (Exception ex) {
			logger.error("redis OverallAcquisition Exception:",ex);
		}        
		wholeOverviewInfo.setId(1);
		//节点数量信息
		wholeOverviewInfo.setHostTotal(hostTotal);
		wholeOverviewInfo.setHostActiveNum(hostActiveNum);
		//虚拟CPU
		wholeOverviewInfo.setVirtualCPUApply(virtualCPUApply);
		wholeOverviewInfo.setVirtualCPUUsed(virtualCPUUsed);
		wholeOverviewInfo.setVirtualCPUTotal(virtualCPUTotal);
		//虚拟Memory
		wholeOverviewInfo.setVirtualMemoryApply(virtualMemoryApply);
		wholeOverviewInfo.setVirtualMemoryUsed(virtualMemoryUsed);
		wholeOverviewInfo.setVirtualMemoryTotal(virtualMemoryTotal);
		//虚拟Disk
		wholeOverviewInfo.setVirtualDiskApply(virtualDiskApply);
		wholeOverviewInfo.setVirtualDiskUsed(virtualDiskUsed);
		wholeOverviewInfo.setVirtualDiskTotal(virtualDiskTotal);
		//所有节点的扩展盘
		wholeOverviewInfo.setExpandDiskUsed(expandDiskUsed);
		wholeOverviewInfo.setExpandDiskTotal(expandDiskTotal);
		return wholeOverviewInfo;
	}
	
	@Override
	public ZoneOverviewInfoVO getZoneOverviewInfo(String zoneCode) throws HsCloudException {
		ZoneOverviewInfoVO zoneOverviewInfo =new ZoneOverviewInfoVO();
		// 获取各个节点的所有资源配备比
		int hostTotal = 0;//节点总数
		int hostActiveNum = 0;//活动的节点数
		int theoreticalValue = 0;//预计还能创建的虚拟机数
		int virtualCPUUsed = 0;//虚拟的CPU使用量
		int virtualCPUApply = 0;//虚拟的CPU申请量			
		int virtualMemoryUsed = 0;//虚拟的Memory使用量
		int virtualMemoryApply = 0;//虚拟的Memory申请量			
		int virtualDiskUsed = 0;//虚拟的Disk使用量
		int virtualDiskApply = 0;//虚拟的Disk申请量
		double expandDiskUsed = 0;//扩展盘使用的容量
		double expandDiskTotal = 0;//扩展盘总共的容量
		int virtualCPUTotal = 0;//虚拟的CPU总量
		int virtualMemoryTotal = 0;//虚拟的Memory总量
		int virtualDiskTotal = 0;//虚拟的Disk总量
		String theoreticalInfo = "";//理论上还能创建的虚拟机数信息       
    	try{
    		String jsonStr = RedisUtil.getValue("ZoneAcquisition_"+ zoneCode);
    		if(StringUtils.isNotBlank(jsonStr)){
    			JSONObject jsonObject = JSONObject.fromObject(jsonStr);	
    			ZoneAcquisitionBean zoneAcquisitionBean=(ZoneAcquisitionBean) JSONObject.toBean(jsonObject, ZoneAcquisitionBean.class);
    	        //节点总数
    	        hostTotal = (zoneAcquisitionBean.getHostTotal()!=null?zoneAcquisitionBean.getHostTotal():0);
    	        //节点活动数
    	        hostActiveNum = (zoneAcquisitionBean.getHostActive()!=null?zoneAcquisitionBean.getHostActive():0);
    	        //预计还能创建的虚拟机数
    	        theoreticalValue = (zoneAcquisitionBean.getTheoreticalValue()!=null?zoneAcquisitionBean.getTheoreticalValue():0);
    	        //虚拟的CPU使用量
    	        virtualCPUUsed = (zoneAcquisitionBean.getCPUUsed()!=null?zoneAcquisitionBean.getCPUUsed():0);
    	        //虚拟的CPU申请量
    	        virtualCPUApply = (zoneAcquisitionBean.getCPUApply()!=null?zoneAcquisitionBean.getCPUApply():0);
    	        //虚拟的CPU总量
    	        virtualCPUTotal = (zoneAcquisitionBean.getCPUTotal()!=null?zoneAcquisitionBean.getCPUTotal():0);
    	        //虚拟的Memory使用量
    	        virtualMemoryUsed = (zoneAcquisitionBean.getMemoryUsed()!=null?zoneAcquisitionBean.getMemoryUsed():0);
    	        //虚拟的Memory申请量
    	        virtualMemoryApply = (zoneAcquisitionBean.getMemoryApply()!=null?zoneAcquisitionBean.getMemoryApply():0);
    	        //虚拟的Memory总量
    	        virtualMemoryTotal = (zoneAcquisitionBean.getMemoryTotal()!=null?zoneAcquisitionBean.getMemoryTotal():0);
    	        //虚拟的Disk使用量
    	        virtualDiskUsed = (zoneAcquisitionBean.getDiskUsed()!=null?zoneAcquisitionBean.getDiskUsed():0);
    	        //虚拟的Disk申请量
    	        virtualDiskApply = (zoneAcquisitionBean.getDiskApply()!=null?zoneAcquisitionBean.getDiskApply():0);
    	        //虚拟的Disk总量
    	        virtualDiskTotal = (zoneAcquisitionBean.getDiskTotal()!=null?zoneAcquisitionBean.getDiskTotal():0);
    	        //扩展盘使用的容量
    	        expandDiskUsed = (zoneAcquisitionBean.getVgUsed()!=null?zoneAcquisitionBean.getVgUsed():0);
    	        //扩展盘总共的容量
    	        expandDiskTotal = (zoneAcquisitionBean.getVgTotal()!=null?zoneAcquisitionBean.getVgTotal():0);
    	        //理论上还能创建的虚拟机数信息
    	        theoreticalInfo = (zoneAcquisitionBean.getTheoreticalInfo()!=null?zoneAcquisitionBean.getTheoreticalInfo():"");
    		}    		
    	}catch (Exception ex) {
			logger.error("redis getZoneOverviewInfo Exception:",ex);
		}        
        zoneOverviewInfo.setId(1);
		//节点数量信息
        zoneOverviewInfo.setHostTotal(hostTotal);
        zoneOverviewInfo.setHostActiveNum(hostActiveNum);
		//预计还能创建的虚拟机数量
        zoneOverviewInfo.setTheoreticalValue(theoreticalValue);
		//虚拟CPU
        zoneOverviewInfo.setVirtualCPUApply(virtualCPUApply);
        zoneOverviewInfo.setVirtualCPUUsed(virtualCPUUsed);
        zoneOverviewInfo.setVirtualCPUTotal(virtualCPUTotal);
		//虚拟Memory
        zoneOverviewInfo.setVirtualMemoryApply(virtualMemoryApply);
        zoneOverviewInfo.setVirtualMemoryUsed(virtualMemoryUsed);
        zoneOverviewInfo.setVirtualMemoryTotal(virtualMemoryTotal);
		//虚拟Disk
        zoneOverviewInfo.setVirtualDiskApply(virtualDiskApply);
        zoneOverviewInfo.setVirtualDiskUsed(virtualDiskUsed);
        zoneOverviewInfo.setVirtualDiskTotal(virtualDiskTotal);
		//所有节点的扩展盘
        zoneOverviewInfo.setExpandDiskUsed(expandDiskUsed);
        zoneOverviewInfo.setExpandDiskTotal(expandDiskTotal);
        //理论上还能创建的虚拟机数信息
        zoneOverviewInfo.setTheoreticalInfo(theoreticalInfo);
		return zoneOverviewInfo;
	}

	@Override
	public HostOverviewInfoVO getHostOverviewInfo(String hostName)
			throws HsCloudException {
		HostOverviewInfoVO hostOverviewInfo = new HostOverviewInfoVO();
		List<DiskDetailVO> diskDetailList = new ArrayList<DiskDetailVO>();
		List<NetworkDetailVO> networkDetailList = new ArrayList<NetworkDetailVO>();
		DiskDetailVO diskDetailVO = null;
		NetworkDetailVO networkDetailVO = null;
		DISKMonitorBean hostDISKMonitorBean = null;
		NETMonitorBean hostNETMonitorBean = null;
		double cpuUsage =0;
		double memoryUsage =0;
		int virtualCPUUsed =0;
		int virtualCPUApply =0;				
		int virtualMemoryUsed = 0;
		int virtualMemoryApply =0;				
		int virtualDiskUsed = 0;
		int virtualDiskApply =0;		
		if(hostName == null || "".equals(hostName)){
			return hostOverviewInfo;
		}        
    	try{
            HostAcquisitionBean hostAcquisitionBean = this.getHostAcquisitionBean(hostName);
            if(hostAcquisitionBean != null){
            	//虚拟的CPU使用量
                virtualCPUUsed = (hostAcquisitionBean.getCPUUsed()!=null?hostAcquisitionBean.getCPUUsed():0);
                //虚拟的CPU申请量
                virtualCPUApply = (hostAcquisitionBean.getCPUApply()!=null?hostAcquisitionBean.getCPUApply():0);
                //虚拟的Memory使用量
                virtualMemoryUsed = (hostAcquisitionBean.getMemoryUsed()!=null?hostAcquisitionBean.getMemoryUsed():0);
                //虚拟的Memory申请量
                virtualMemoryApply = (hostAcquisitionBean.getMemoryApply()!=null?hostAcquisitionBean.getMemoryApply():0); 
                //虚拟的Disk使用量
                virtualDiskUsed = (hostAcquisitionBean.getDiskUsed()!=null?hostAcquisitionBean.getDiskUsed():0);
                //虚拟的Disk申请量
                virtualDiskApply = (hostAcquisitionBean.getDiskApply()!=null?hostAcquisitionBean.getDiskApply():0);
                //CPU使用率
                cpuUsage = (hostAcquisitionBean.getCPUUsage()!=null?hostAcquisitionBean.getCPUUsage():0); 
                //虚拟内存使用率
                memoryUsage = (hostAcquisitionBean.getMemoryUsage()!=null?hostAcquisitionBean.getMemoryUsage():0); 
               /* expandDiskUsed = Integer.valueOf((String)jsonObject.get("vgUsed"));;//扩展盘使用的容量
                expandDiskTotal = Integer.valueOf((String)jsonObject.get("vgTotal"));;//扩展盘总共的容量*/
            }
    		String jsonDist = RedisUtil.getValue("HostDISKMonitor_" + hostName);
            if(StringUtils.isNotBlank(jsonDist)) {
                JSONArray jsonArray = JSONArray.fromObject(jsonDist);
                JSONObject diskObj = null;
                for(int i = 0; i < jsonArray.size(); i++) {
                    diskObj = (JSONObject)jsonArray.get(i);
                    hostDISKMonitorBean = (DISKMonitorBean) JSONObject.toBean(diskObj, DISKMonitorBean.class);
                    diskDetailVO =new DiskDetailVO();
                    double diskTotal = (hostDISKMonitorBean.getDiskTotal()!=null?hostDISKMonitorBean.getDiskTotal():0);
                    double diskUsage = (hostDISKMonitorBean.getDiskUsage()!=null?hostDISKMonitorBean.getDiskUsage():0);
                    
                    diskDetailVO.setDiskName(hostDISKMonitorBean.getDevice());//"disk"+diskId
                    diskDetailVO.setDiskTotal(diskTotal);
                    diskDetailVO.setDiskUsed(diskTotal*diskUsage/100);                 
                    diskDetailList.add(diskDetailVO);
                }
            }
            String netDist = RedisUtil.getValue("HostNETMonitor_" + hostName);
            if(StringUtils.isNotBlank(netDist)) {
                JSONArray jsonArray = JSONArray.fromObject(netDist);
                JSONObject netObj = null;
                for(int i = 0; i < jsonArray.size(); i++) {
                    netObj = (JSONObject)jsonArray.get(i);
                    hostNETMonitorBean = (NETMonitorBean) JSONObject.toBean(netObj, NETMonitorBean.class);                    
                    if(hostNETMonitorBean.getDevice().contains("eth")){                 
                        networkDetailVO = new NetworkDetailVO();
                        networkDetailVO.setNetName(hostNETMonitorBean.getDevice());
                        networkDetailVO.setNetRx(hostNETMonitorBean.getRxSpeed());
                        networkDetailVO.setNetTx(hostNETMonitorBean.getTxSpeed());                  
                        networkDetailVO.setNetTotal(0);
                        networkDetailList.add(networkDetailVO);
                    }
                }
            }
    	}catch (Exception ex) {
			logger.error("redis HostAcquisition Exception:",ex);
		}        
		hostOverviewInfo.setHostId("1");
		hostOverviewInfo.setHostName(hostName);
		hostOverviewInfo.setIpInner("");
		//------------
		hostOverviewInfo.setCpuUsage(cpuUsage);
		hostOverviewInfo.setMemoryUsage(memoryUsage);
		hostOverviewInfo.setDiskDetail(diskDetailList);
		hostOverviewInfo.setNetworkDetail(networkDetailList);
		//-------------
		//虚拟CPU
		hostOverviewInfo.setVirtualCPUApply(virtualCPUApply);
		hostOverviewInfo.setVirtualCPUUsed(virtualCPUUsed);		
		//虚拟Memory
		hostOverviewInfo.setVirtualMemoryApply(virtualMemoryApply);
		hostOverviewInfo.setVirtualMemoryUsed(virtualMemoryUsed);		
		//虚拟Disk
		hostOverviewInfo.setVirtualDiskApply(virtualDiskApply);
		hostOverviewInfo.setVirtualDiskUsed(virtualDiskUsed);
		return hostOverviewInfo;
	}

	@Override
	public VmOverviewInfoVO getVmOverviewInfo(String vmId)
			throws HsCloudException {
		VmOverviewInfoVO vmOverviewInfo = new VmOverviewInfoVO();
		List<DiskDetailVO> diskDetailList = new ArrayList<DiskDetailVO>();
		List<NetworkDetailVO> networkDetailList = new ArrayList<NetworkDetailVO>();
		DiskDetailVO diskDetailVO =null;
		NetworkDetailVO networkDetailVO = null;
		ServerAcquisitionBean serverAcquisitionBean = null;
		DISKMonitorBean vmDISKMonitorBean = null;
		NETMonitorBean vmNETMonitorBean = null;
		double cpuUsage =0;
		double memoryUsage = 0;
		if(vmId == null || "".equals(vmId)){
			return vmOverviewInfo;
		}        
    	try{
            serverAcquisitionBean = this.getServerAcquisitionBean(vmId);
            if(serverAcquisitionBean != null){
            	// CPU使用率
                cpuUsage = (serverAcquisitionBean.getvCPUUsage()!=null?serverAcquisitionBean.getvCPUUsage():0); 
                //虚拟内存使用大小
                memoryUsage = (serverAcquisitionBean.getvMemoryUsage()!=null?serverAcquisitionBean.getvMemoryUsage():0);
            } 
    		String jsonDist = RedisUtil.getValue("VMDISKMonitor_" + vmId);
            if(StringUtils.isNotBlank(jsonDist)) {
                JSONArray jsonArray = JSONArray.fromObject(jsonDist);
                JSONObject diskObj = null;
                for(int i = 0; i < jsonArray.size(); i++) {
                    diskObj = (JSONObject)jsonArray.get(i);
                    vmDISKMonitorBean = (DISKMonitorBean) JSONObject.toBean(diskObj, DISKMonitorBean.class);
                    double diskTotal = (vmDISKMonitorBean.getDiskTotal()!=null?vmDISKMonitorBean.getDiskTotal():0);
                    double diskUsage = (vmDISKMonitorBean.getDiskUsage()!=null?vmDISKMonitorBean.getDiskUsage():0);
                    String diskName = (vmDISKMonitorBean.getDevice()!=null?vmDISKMonitorBean.getDevice():"");
                    double readSpeed = (vmDISKMonitorBean.getReadSpeed()!=null?vmDISKMonitorBean.getReadSpeed():0);
                    double writeSpeed = (vmDISKMonitorBean.getWriteSpeed()!=null?vmDISKMonitorBean.getWriteSpeed():0);
                    
                    diskDetailVO =new DiskDetailVO();
                    diskDetailVO.setDiskName(diskName);//"disk"+diskId
                    diskDetailVO.setDiskTotal(diskTotal);
                    diskDetailVO.setDiskUsed(diskTotal*diskUsage/100);
                    diskDetailVO.setDiskReadSpeed(readSpeed);
                    diskDetailVO.setDiskWriteSpeed(writeSpeed);
                    diskDetailList.add(diskDetailVO);
                }
            }
            String netDist = RedisUtil.getValue("VMNETMonitor_" + vmId);
            if(StringUtils.isNotBlank(netDist)) {
                JSONArray jsonArray = JSONArray.fromObject(netDist);
                for(int i = 0; i < jsonArray.size(); i++) {
                    JSONObject netObj = (JSONObject)jsonArray.get(i);
                    vmNETMonitorBean = (NETMonitorBean) JSONObject.toBean(netObj, NETMonitorBean.class);
                    String deviceName = (vmNETMonitorBean.getDevice()!=null?vmNETMonitorBean.getDevice():"");                        
                    
                    if(deviceName.contains("eth")){
                    	double rxSpeed = (vmNETMonitorBean.getRxSpeed()!=null?vmNETMonitorBean.getRxSpeed():0);
                        double txSpeed = (vmNETMonitorBean.getTxSpeed()!=null?vmNETMonitorBean.getTxSpeed():0);
                        networkDetailVO = new NetworkDetailVO();
                        networkDetailVO.setNetName(deviceName);
                        networkDetailVO.setNetRx(rxSpeed);
                        networkDetailVO.setNetTx(txSpeed);                  
                        networkDetailVO.setNetTotal(0);
                        networkDetailList.add(networkDetailVO);
                    }
                }
            }
    	}catch (Exception ex) {
			logger.error("redis ServerAcquisition Exception:",ex);
		}        
		vmOverviewInfo.setVmId(vmId);
		vmOverviewInfo.setCpuUsage(cpuUsage);
		vmOverviewInfo.setMemoryUsage(memoryUsage);
		vmOverviewInfo.setDiskDetail(diskDetailList);
		vmOverviewInfo.setNetworkDetail(networkDetailList);
		return vmOverviewInfo;
	}

	@Override
	public Page<HostInfoVO> findHostDetailsByCondition(Page<HostInfoVO> page,
			String field, String fieldValue) throws HsCloudException {
		List<HostInfoVO> hostInfoVOList = page.getResult();
		List<HostInfoVO> hostInfoList = new ArrayList<HostInfoVO>();		
		HostAcquisitionBean hostAcquisitionBean = null;				
		double cpuUsage = 0;
		double memoryUsage = 0; 
		double diskUsage = 0;
		int cpuWorkloadActual = 0;
		int iopsReadActual = 0;
		int iopsWriteActual = 0;
		int netReadActual = 0;
		int netWriteActual = 0;
//		int cpuWorkloadLimit = 0;
//		int iopsReadLimit = 0;		
//		int iopsWriteLimit = 0;		
//		int netReadLimit = 0;		
//		int netWriteLimit = 0;		
		String hostStatus = "false";
		for(HostInfoVO hostInfoVO : hostInfoVOList){
			cpuUsage = 0;
			memoryUsage = 0; 
			diskUsage = 0;
//			cpuWorkloadLimit = 0;
//			iopsReadLimit = 0;
//			iopsWriteLimit = 0;
//			netReadLimit = 0;
//			netWriteLimit = 0;
			cpuWorkloadActual = 0;			
			iopsReadActual = 0;		
			iopsWriteActual = 0;		
			netReadActual = 0;			
			netWriteActual = 0;
			CPUMonitorDetailBean cpuMonitorDetailBean = null;
			IOPSMonitorDetailBean iopsMonitorDetailBean = null;
			NETMonitorDetailBean netMonitorDetailBean = null;
			if(hostInfoVO == null || hostInfoVO.getHostName() == null){
				hostInfoList.add(hostInfoVO);
				continue;
			}
//			NodeIsolationConfig nodeIsolationConfig=hostInfoVO.getNodeIsolationConfig();
//			if(nodeIsolationConfig!= null){
//				cpuWorkloadLimit = nodeIsolationConfig.getCPUWorkload();
//				iopsReadLimit = nodeIsolationConfig.getIOPSRead();
//				iopsWriteLimit = nodeIsolationConfig.getIOPSWrite();
//				netReadLimit = nodeIsolationConfig.getNetworkRead();
//				netWriteLimit = nodeIsolationConfig.getNetworkWrite();
//			}			
			cpuMonitorDetailBean = new CPUMonitorDetailBean();
			iopsMonitorDetailBean = new IOPSMonitorDetailBean();
			netMonitorDetailBean = new NETMonitorDetailBean();
        	try{
                hostAcquisitionBean = this.getHostAcquisitionBean(hostInfoVO.getHostName());
                if(hostAcquisitionBean != null){
                	hostStatus = (hostAcquisitionBean.getHostStatus()!=null?hostAcquisitionBean.getHostStatus():"false");
                    cpuUsage = (hostAcquisitionBean.getCPUUsage()!=null?hostAcquisitionBean.getCPUUsage():0);
                    memoryUsage = (hostAcquisitionBean.getMemoryUsage()!=null?hostAcquisitionBean.getMemoryUsage():0);
                    diskUsage = (hostAcquisitionBean.getDiskUsage()!=null?hostAcquisitionBean.getDiskUsage():0);
                    cpuWorkloadActual = (hostAcquisitionBean.getCpuWorkload()!=null?hostAcquisitionBean.getCpuWorkload():0);
                    iopsReadActual = (hostAcquisitionBean.getIopsRead()!=null?hostAcquisitionBean.getIopsRead():0);
                    iopsWriteActual = (hostAcquisitionBean.getIopsWrite()!=null?hostAcquisitionBean.getIopsWrite():0);
                    netReadActual = (hostAcquisitionBean.getRxSpeed()!=null?hostAcquisitionBean.getRxSpeed():0);
                    netWriteActual = (hostAcquisitionBean.getTxSpeed()!=null?hostAcquisitionBean.getTxSpeed():0);
                } 
//                cpuMonitorDetailBean.setWorkloadLimit(cpuWorkloadLimit);
//                iopsMonitorDetailBean.setReadLimit(iopsReadLimit);
//                iopsMonitorDetailBean.setWriteLimit(iopsWriteLimit);
//                netMonitorDetailBean.setReadLimit(netReadLimit);
//                netMonitorDetailBean.setWriteLimit(netWriteLimit);
                cpuMonitorDetailBean.setWorkloadActual(cpuWorkloadActual);                
                iopsMonitorDetailBean.setReadActual(iopsReadActual);                
                iopsMonitorDetailBean.setWriteActual(iopsWriteActual);                
                netMonitorDetailBean.setReadActual(netReadActual);                
                netMonitorDetailBean.setWriteActual(netWriteActual);                
                this.getCPUMonitorDetailBean(cpuMonitorDetailBean,hostInfoVO.getHostName());
                this.getIOPSMonitorDetailBean(iopsMonitorDetailBean, hostInfoVO.getHostName());
                this.getNETMonitorDetailBean(netMonitorDetailBean, hostInfoVO.getHostName());
			}catch (Exception ex) {
				logger.error("redis HostAcquisition Exception:",ex);
			}            
            hostInfoVO.setCpuMonitorDetailBean(cpuMonitorDetailBean);
            hostInfoVO.setIopsMonitorDetailBean(iopsMonitorDetailBean);
            hostInfoVO.setNetMonitorDetailBean(netMonitorDetailBean);
            hostInfoVO.setHostStatus(hostStatus);//"true"
			hostInfoVO.setCpuUsage(cpuUsage);
			hostInfoVO.setMemoryUsage(memoryUsage);
			hostInfoVO.setDiskUsage(diskUsage);	
			hostInfoList.add(hostInfoVO);
		}
		page.setTotalCount(page.getTotalCount());
		page.setResult(hostInfoList);
		return page;
	}

	@Override
	public Page<VmInfoVO> findVmDetailsByCondition(Page<VmInfoVO> page,
			String hostName, String field, String fieldValue,List<Object> referenceIds)
			throws HsCloudException {
		List<VmInfoVO> vmInfoListVO = page.getResult();
		List<VmInfoVO> vmInfoList = new ArrayList<VmInfoVO>();
		ServerAcquisitionBean serverAcquisitionBean = null;
		double cpuUsage =0;
		double memoryUsage = 0;
		double diskUsage = 0;
		vmInfoList = new ArrayList<VmInfoVO>();
		for(VmInfoVO vmInfoVO:vmInfoListVO){
			cpuUsage =0;
			memoryUsage = 0;
			diskUsage = 0;
			if(vmInfoVO == null || vmInfoVO.getVmId() == null ){
				vmInfoList.add(vmInfoVO);
				continue;
			}	        
        	try{
	            serverAcquisitionBean = this.getServerAcquisitionBean(vmInfoVO.getVmId());
	            if(serverAcquisitionBean != null){
	            	// CPU使用率
		            cpuUsage = (serverAcquisitionBean.getvCPUUsage()!=null?serverAcquisitionBean.getvCPUUsage():0);
		            //虚拟内存使用率
		            memoryUsage = (serverAcquisitionBean.getvMemoryUsage()!=null?serverAcquisitionBean.getvMemoryUsage():0);
		            //虚拟磁盘使用率
		            diskUsage = (serverAcquisitionBean.getvDiskUsage()!=null?serverAcquisitionBean.getvDiskUsage():0);
	            }      		 
        	}catch (Exception ex) {
				logger.error("redis ServerAcquisition Exception:",ex);
			}	        
			vmInfoVO.setCpuUsage(cpuUsage);
			vmInfoVO.setMemoryUsage(memoryUsage);
			vmInfoVO.setDiskUsage(diskUsage);		
			vmInfoList.add(vmInfoVO);
		}		
		page.setResult(vmInfoList);
		return page;
	}	

	@Override
	public List<CPUHistoryVO> getVmCPUHistory(String vmId, long fromTime,
			long toTime) throws HsCloudException {
		List<CPUHistory> list=null;
		CPUHistoryVO cpuHistoryVo =null;
		float cpuRate=0;
		List<CPUHistoryVO> listVO=new ArrayList<CPUHistoryVO>();
		if(vmId == null ||"".equals(vmId)){
			return listVO;
		}
		try{
			WqServerResource wqServerResource = openstackUtil.getCompute().wqservers();
			historyMonitor = new HistoryMonitor();
			historyMonitor.sethN(vmId);
			historyMonitor.setiT("cpu");
			historyMonitor.setsT(String.valueOf(fromTime));
			historyMonitor.seteT(String.valueOf(toTime));
			list = wqServerResource.getCpuHistorys(historyMonitor).getList();
		}catch (Exception ex) {			
			throw new HsCloudException(Constants.MONITOR_REQUEST_EXCEPTION, "getServerHistoryResource Exception:", logger, ex);
		}		
		for(CPUHistory cpuHistory:list){
			cpuHistoryVo =new CPUHistoryVO();
			cpuRate=cpuHistory.getCpuRate();
			//对CPU使用率超过100的进行处理
			if(cpuRate > 100){
				cpuRate = 100;
			}
			cpuHistoryVo.setTimestamp(cpuHistory.getTimestamp()*1000);
			cpuHistoryVo.setCpuRate(cpuRate);
			cpuHistoryVo.setCpuNum(cpuHistory.getCpuNum());
			listVO.add(cpuHistoryVo);
		}
		return listVO;
	}

	@Override
	public List<MemoryHistoryVO> getVmMemoryHistory(String vmId, long fromTime,
			long toTime) throws HsCloudException {
		List<RAMHistory> list=null;
		MemoryHistoryVO memoryHistoryVO =null;
		float ramRate=0;
		List<MemoryHistoryVO> listVO=new ArrayList<MemoryHistoryVO>();
		if(vmId == null ||"".equals(vmId)){
			return listVO;
		}
		try{
			WqServerResource wqServerResource = openstackUtil.getCompute().wqservers();
			historyMonitor = new HistoryMonitor();
			historyMonitor.sethN(vmId);
			historyMonitor.setiT("ram");
			historyMonitor.setsT(String.valueOf(fromTime));
			historyMonitor.seteT(String.valueOf(toTime));
			list = wqServerResource.getRamHistorys(historyMonitor).getList();
		}catch (Exception ex) {			
			throw new HsCloudException(Constants.MONITOR_REQUEST_EXCEPTION, "getServerHistoryResource Exception:", logger, ex);
		}		
		for(RAMHistory ramHistory:list){
			memoryHistoryVO =new MemoryHistoryVO();
			ramRate=ramHistory.getRamRate();
			//对内存使用率超过100的进行处理
			if(ramRate>100){
				ramRate=100;
			}
			memoryHistoryVO.setTimestamp(ramHistory.getTimestamp()*1000);
			memoryHistoryVO.setRamRate(ramRate);
			memoryHistoryVO.setRamTotal(ramHistory.getRamTotal());
			listVO.add(memoryHistoryVO);
		}
		return listVO;
	}

	@Override
	public List<DiskHistoryVO> getVmDiskHistory(String vmId, long fromTime,
			long toTime) throws HsCloudException {
		List<DISKHistory> list=null;
		DiskHistoryVO diskHistoryVO =null;
		List<DiskHistoryVO> listVO=new ArrayList<DiskHistoryVO>();
		if(vmId == null ||"".equals(vmId)){
			return listVO;
		}
		try{
			WqServerResource wqServerResource = openstackUtil.getCompute().wqservers();
			historyMonitor = new HistoryMonitor();
			historyMonitor.sethN(vmId);
			historyMonitor.setiT("disk");
			historyMonitor.setsT(String.valueOf(fromTime));
			historyMonitor.seteT(String.valueOf(toTime));
			list = wqServerResource.getDiskHistorys(historyMonitor).getList();
		}catch (Exception ex) {			
			throw new HsCloudException(Constants.MONITOR_REQUEST_EXCEPTION, "getServerHistoryResource Exception:", logger, ex);
		}
		//统计每一块硬盘的信息
		for(DISKHistory diskHistory:list){
			diskHistoryVO =new DiskHistoryVO();
			diskHistoryVO.setTimestamp(diskHistory.getTimestamp()*1000);
			diskHistoryVO.setReadSpeed(diskHistory.getReadSpeed());
			diskHistoryVO.setWriteSpeed(diskHistory.getWriteSpeed());
			diskHistoryVO.setDiskTotal(diskHistory.getDiskTotal());
			listVO.add(diskHistoryVO);
		}
		return listVO;
	}

	@Override
	public List<NetHistoryVO> getVmNetHistory(String vmId, long fromTime,
			long toTime) throws HsCloudException {
		List<NETHistory> list=null;
		NetHistoryVO netHistoryVO = null;
		List<NetHistoryVO> listVO=new ArrayList<NetHistoryVO>();
		if(vmId == null ||"".equals(vmId)){
			return listVO;
		}
		try{
			WqServerResource wqServerResource = openstackUtil.getCompute().wqservers();
			historyMonitor = new HistoryMonitor();
			historyMonitor.sethN(vmId);
			historyMonitor.setiT("net");
			historyMonitor.setsT(String.valueOf(fromTime));
			historyMonitor.seteT(String.valueOf(toTime));
			list = wqServerResource.getNetHistorys(historyMonitor).getList();
		}catch (Exception ex) {			
			throw new HsCloudException(Constants.MONITOR_REQUEST_EXCEPTION, "getServerHistoryResource Exception:", logger, ex);
		}
		//统计每一个网络的信息
		for(NETHistory netHistory:list){
			netHistoryVO =new NetHistoryVO();
			netHistoryVO.setTimestamp(netHistory.getTimestamp()*1000);
			netHistoryVO.setRxSpeed(netHistory.getRxSpeed());
			netHistoryVO.setTxSpeed(netHistory.getTxSpeed());			
			listVO.add(netHistoryVO);
		}
		return listVO;
	}

	@Override
	public List<CPUHistoryVO> getHostCPUHistory(String hostName, long fromTime,
			long toTime) throws HsCloudException {
		List<CPUHistory> list=null;
		
		CPUHistoryVO cpuHistoryVo = null;
		float cpuRate=0;
		List<CPUHistoryVO> listVO=new ArrayList<CPUHistoryVO>();
		if(hostName == null ||"".equals(hostName)){
			return listVO;
		}
		try{
			WqServerResource wqServerResource = openstackUtil.getCompute().wqservers();
			historyMonitor = new HistoryMonitor();
			historyMonitor.sethN(hostName);
			historyMonitor.setiT("cpu");
			historyMonitor.setsT(String.valueOf(fromTime));
			historyMonitor.seteT(String.valueOf(toTime));
			list = wqServerResource.getCpuHistorys(historyMonitor).getList();
		}catch (Exception ex) {			
			throw new HsCloudException(Constants.MONITOR_REQUEST_EXCEPTION, "getHostHistoryResource Exception:", logger, ex);
		}		
		for(CPUHistory cpuHistory:list){
			cpuHistoryVo =new CPUHistoryVO();
			cpuRate=cpuHistory.getCpuRate();
			//对CPU使用率超过100的进行处理
			if(cpuRate>100){
				cpuRate=100;
			}
			cpuHistoryVo.setTimestamp(cpuHistory.getTimestamp()*1000);
			cpuHistoryVo.setCpuRate(cpuRate);
			cpuHistoryVo.setCpuNum(cpuHistory.getCpuNum());
			listVO.add(cpuHistoryVo);
		}
		return listVO;
	}

	@Override
	public List<MemoryHistoryVO> getHostMemoryHistory(String hostName,
			long fromTime, long toTime) throws HsCloudException {
		List<RAMHistory> list=null;
		MemoryHistoryVO memoryHistoryVO = null;
		float ramRate=0;
		List<MemoryHistoryVO> listVO=new ArrayList<MemoryHistoryVO>();
		if(hostName == null ||"".equals(hostName)){
			return listVO;
		}
		try{
			WqServerResource wqServerResource = openstackUtil.getCompute().wqservers();
			historyMonitor = new HistoryMonitor();
			historyMonitor.sethN(hostName);
			historyMonitor.setiT("ram");
			historyMonitor.setsT(String.valueOf(fromTime));
			historyMonitor.seteT(String.valueOf(toTime));
			list = wqServerResource.getRamHistorys(historyMonitor).getList();
		}catch (Exception ex) {
			throw new HsCloudException(Constants.MONITOR_REQUEST_EXCEPTION, "getHostHistoryResource Exception:", logger, ex);
		}		
		for(RAMHistory ramHistory:list){
			memoryHistoryVO =new MemoryHistoryVO();
			ramRate=ramHistory.getRamRate();
			//对内存使用率超过100的进行处理
			if(ramRate>100){
				ramRate=100;
			}
			memoryHistoryVO.setTimestamp(ramHistory.getTimestamp()*1000);
			memoryHistoryVO.setRamRate(ramRate);
			memoryHistoryVO.setRamTotal(ramHistory.getRamTotal());
			listVO.add(memoryHistoryVO);
		}
		return listVO;
	}

	@Override
	public List<DiskHistoryVO> getHostDiskHistory(String hostName,
			long fromTime, long toTime) throws HsCloudException {
		List<DISKHistory> list=null;
		DiskHistoryVO diskHistoryVO =null;
		List<DiskHistoryVO> listVO=new ArrayList<DiskHistoryVO>();
		if(hostName == null ||"".equals(hostName)){
			return listVO;
		}
		try{
			WqServerResource wqServerResource = openstackUtil.getCompute().wqservers();
			historyMonitor = new HistoryMonitor();
			historyMonitor.sethN(hostName);
			historyMonitor.setiT("disk");
			historyMonitor.setsT(String.valueOf(fromTime));
			historyMonitor.seteT(String.valueOf(toTime));
			list = wqServerResource.getDiskHistorys(historyMonitor).getList();
		}catch (Exception ex) {
			throw new HsCloudException(Constants.MONITOR_REQUEST_EXCEPTION, "getHostHistoryResource Exception:", logger, ex);
		}
		//统计每一块硬盘的信息
		for(DISKHistory diskHistory:list){
			diskHistoryVO =new DiskHistoryVO();
			diskHistoryVO.setTimestamp(diskHistory.getTimestamp()*1000);
			diskHistoryVO.setReadSpeed(diskHistory.getReadSpeed());
			diskHistoryVO.setWriteSpeed(diskHistory.getWriteSpeed());
			diskHistoryVO.setDiskTotal(diskHistory.getDiskTotal());
			listVO.add(diskHistoryVO);
		}
		return listVO;
	}

	@Override
	public List<NetHistoryVO> getHostNetHistory(String hostName, long fromTime,
			long toTime) throws HsCloudException {
		List<NETHistory> list=null;
		NetHistoryVO netHistoryVO =null;
		List<NetHistoryVO> listVO=new ArrayList<NetHistoryVO>();
		if(hostName == null ||"".equals(hostName)){
			return listVO;
		}
		try{
			WqServerResource wqServerResource = openstackUtil.getCompute().wqservers();
			historyMonitor = new HistoryMonitor();
			historyMonitor.sethN(hostName);
			historyMonitor.setiT("net");
			historyMonitor.setsT(String.valueOf(fromTime));
			historyMonitor.seteT(String.valueOf(toTime));
			list = wqServerResource.getNetHistorys(historyMonitor).getList();
		}catch (Exception ex) {
			throw new HsCloudException(Constants.MONITOR_REQUEST_EXCEPTION, "getHostHistoryResource Exception:", logger, ex);
		}
		//统计每一个网络的信息
		for(NETHistory netHistory:list){
			netHistoryVO =new NetHistoryVO();
			netHistoryVO.setTimestamp(netHistory.getTimestamp()*1000);
			netHistoryVO.setRxSpeed(netHistory.getRxSpeed());
			netHistoryVO.setTxSpeed(netHistory.getTxSpeed());			
			listVO.add(netHistoryVO);
		}
		return listVO;
	}

	@Override
	public List<VmRealtimeMonitorVO> getVmRealTimeMonitor(String vmId)
			throws HsCloudException {
		List<VmRealtimeMonitorVO> vmRealtimeMonitorVOList = new ArrayList<VmRealtimeMonitorVO>();
		List<DiskMonitorVO> diskMonitorVOList = new ArrayList<DiskMonitorVO>();
		List<NetMonitorVO> netMonitorVOList = new ArrayList<NetMonitorVO>();
		VmRealtimeMonitorVO vmRealtimeMonitorVO = new VmRealtimeMonitorVO();
		CPUMonitorVO cPUMonitorVO = null;
		MemoryMonitorVO memoryMonitorVO = null;
		DiskMonitorVO diskMonitorVO = null;
		NetMonitorVO netMonitorVO = null;
		ServerMonitorBean serverMonitorBean = null;
		DISKMonitorBean vmDISKMonitorBean = null;
		NETMonitorBean vmNETMonitorBean = null;
		double cpuRate=0;
		double ramRate=0;
		int cpuNum = 0;
		int ramTotal = 0;
		if(vmId == null ||"".equals(vmId)){
			return vmRealtimeMonitorVOList;
		}  
		try{
			String jsonStr = RedisUtil.getValue("ServerMonitor_" + vmId);
	    	if(StringUtils.isNotBlank(jsonStr)){
	    		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
	            serverMonitorBean = (ServerMonitorBean) JSONObject.toBean(jsonObject, ServerMonitorBean.class);
	            cpuRate = serverMonitorBean.getCpuRate();
	            cpuNum = serverMonitorBean.getCpuNum();
	            ramRate = serverMonitorBean.getRamRate();
	            ramTotal = serverMonitorBean.getRamTotal(); 
	    	}                       
	        vmRealtimeMonitorVO = new VmRealtimeMonitorVO();            
	        if(cpuRate>100){
	            cpuRate=100;
	        }
	        cPUMonitorVO =new CPUMonitorVO();
	        cPUMonitorVO.setCpuNum(cpuNum);
	        cPUMonitorVO.setCpuRate((float) cpuRate);
	        vmRealtimeMonitorVO.setcPUMonitorVO(cPUMonitorVO);            
	        if(ramRate>100){
	            ramRate=100;
	        }
	        memoryMonitorVO = new MemoryMonitorVO();
	        memoryMonitorVO.setRamRate((float) ramRate);
	        memoryMonitorVO.setRamTotal(ramTotal);
	        vmRealtimeMonitorVO.setMemoryMonitorVO(memoryMonitorVO);            
	        
	        String jsonDist = RedisUtil.getValue("VMDISKMonitor_" + vmId);
	        if(StringUtils.isNotBlank(jsonDist)) {
	            JSONArray jsonArray = JSONArray.fromObject(jsonDist);
	            JSONObject diskObj = null;
	            for(int i = 0; i < jsonArray.size(); i++) {
	                diskObj = (JSONObject)jsonArray.get(i);
	                vmDISKMonitorBean = (DISKMonitorBean) JSONObject.toBean(diskObj, DISKMonitorBean.class);
	                double diskTotal = vmDISKMonitorBean.getDiskTotal();
	                double diskUsage = vmDISKMonitorBean.getDiskUsage();
	                double readSpeed = vmDISKMonitorBean.getReadSpeed();
	                double writeSpeed = vmDISKMonitorBean.getWriteSpeed();
	                diskMonitorVO = new DiskMonitorVO();
	                diskMonitorVO.setDevice(vmDISKMonitorBean.getDevice());
	                diskMonitorVO.setDiskRate((float) diskUsage);
	                diskMonitorVO.setDiskTotal((float) diskTotal);
	                diskMonitorVO.setReadSpeed((float) readSpeed);
	                diskMonitorVO.setWriteSpeed((float) writeSpeed);
	                diskMonitorVOList.add(diskMonitorVO);
	            }
	        }            
	        String netDist = RedisUtil.getValue("VMNETMonitor_" + vmId);
	        if(StringUtils.isNotBlank(netDist)) {
	            JSONArray jsonArray = JSONArray.fromObject(netDist);
	            JSONObject netObj = null;
	            for(int i = 0; i < jsonArray.size(); i++) {
	                netObj = (JSONObject)jsonArray.get(i);
	                vmNETMonitorBean = (NETMonitorBean) JSONObject.toBean(netObj, NETMonitorBean.class);
	                double rxSpeed = vmNETMonitorBean.getRxSpeed();
	                double txSpeed = vmNETMonitorBean.getTxSpeed();
	                
	                netMonitorVO = new NetMonitorVO();
	                netMonitorVO.setDevice(vmNETMonitorBean.getDevice());
	                netMonitorVO.setIp(vmNETMonitorBean.getIP());
	                netMonitorVO.setRxSpeed((float) rxSpeed);
	                netMonitorVO.setTxSpeed((float) txSpeed);
	                netMonitorVOList.add(netMonitorVO);
	            }
	        } 
		}catch (Exception ex) {
			logger.error("redis ServerAcquisition Exception:",ex);
		}        
        vmRealtimeMonitorVO.setDiskMonitorVOList(diskMonitorVOList);
        vmRealtimeMonitorVO.setNetMonitorVOList(netMonitorVOList);
        vmRealtimeMonitorVOList.add(vmRealtimeMonitorVO);
		return vmRealtimeMonitorVOList;
	}

	@Override
	public List<HostInfoVO> getAllHostUsage(List<HostInfoVO> hostInfoVOList) throws HsCloudException {
		List<HostInfoVO> hostInfoList = new ArrayList<HostInfoVO>();
		HostAcquisitionBean hostAcquisitionBean = null;
		double cpuUsage = 0;
		double memoryUsage = 0; 
		double diskUsage = 0;
		for(HostInfoVO hostInfoVO : hostInfoVOList){
			if(hostInfoVO == null || hostInfoVO.getHostName() == null){
				hostInfoList.add(hostInfoVO);
				continue;
			}
			try{
				hostAcquisitionBean = this.getHostAcquisitionBean(hostInfoVO.getHostName());
                if(hostAcquisitionBean != null){
                    cpuUsage = (hostAcquisitionBean.getCPUUsage()!=null?hostAcquisitionBean.getCPUUsage():0);
                    memoryUsage = (hostAcquisitionBean.getMemoryUsage()!=null?hostAcquisitionBean.getMemoryUsage():0);
                    diskUsage = (hostAcquisitionBean.getDiskUsage()!=null?hostAcquisitionBean.getDiskUsage():0);
                }
			}catch (Exception ex) {
				throw new HsCloudException(Constants.MONITOR_REQUEST_EXCEPTION, "getHostMonitorResource Exception:", logger, ex);
			}			
			hostInfoVO.setCpuUsage(cpuUsage);
			hostInfoVO.setMemoryUsage(memoryUsage);
			hostInfoVO.setDiskUsage(diskUsage);			
			hostInfoList.add(hostInfoVO);
		}
		return hostInfoList;
	}
	@Override
	public HostAcquisitionBean getHostAcquisitionBean(String hostName)
			throws HsCloudException {
		HostAcquisitionBean hostAcquisitionBean = null;
		try{
    		String jsonStr = RedisUtil.getValue("HostAcquisition_" + hostName);
    		if(StringUtils.isNotBlank(jsonStr)){
    			JSONObject jsonObject = JSONObject.fromObject(jsonStr); 
    			hostAcquisitionBean = (HostAcquisitionBean) JSONObject.toBean(jsonObject, HostAcquisitionBean.class);
    		}           
		}catch (Exception ex) {
			logger.error("redis HostAcquisition Exception:",ex);
		} 
		return hostAcquisitionBean;
	}
	
	public ServerAcquisitionBean getServerAcquisitionBean(String vmUUID) throws HsCloudException{
		ServerAcquisitionBean serverAcquisitionBean = null;
		try{
    		String jsonStr = RedisUtil.getValue("ServerAcquisition_" + vmUUID);
    		if(StringUtils.isNotBlank(jsonStr)){
    			JSONObject jsonObject = JSONObject.fromObject(jsonStr);
	            serverAcquisitionBean = (ServerAcquisitionBean) JSONObject.toBean(jsonObject, ServerAcquisitionBean.class);
    		}	        		 
    	}catch (Exception ex) {
			logger.error("redis ServerAcquisition Exception:",ex);
		}
		return serverAcquisitionBean;
	}

	@Override
	public List<HostAcquisition> getHostAcquisition() throws HsCloudException {
		return null;
	}	
	private CPUMonitorDetailBean getCPUMonitorDetailBean(CPUMonitorDetailBean cpuMonitorDetailBean,String keyword) throws HsCloudException{
		List<CPUSingleDetailBean> singleDetailBeanList = null ;
		CPULoadAvgBean loadAvgBean = null;
		CPUSingleDetailBean singleDetailBean = null;		
		String jsonLoadAvg = RedisUtil.getValue("CPULoadAvg_" + keyword);
        if(StringUtils.isNotBlank(jsonLoadAvg)) {
        	JSONObject loadAvgObj = JSONObject.fromObject(jsonLoadAvg);
        	loadAvgBean = (CPULoadAvgBean) JSONObject.toBean(loadAvgObj, CPULoadAvgBean.class);
        	cpuMonitorDetailBean.setLoadAvgBean(loadAvgBean);
        }
        String jsonSingleDetail = RedisUtil.getValue("CPUSingleDetail_" + keyword);
        if(StringUtils.isNotBlank(jsonSingleDetail)) {
            JSONArray jsonArray = JSONArray.fromObject(jsonSingleDetail);
            singleDetailBeanList = new ArrayList<CPUSingleDetailBean>();
            for(int i = 0; i < jsonArray.size(); i++) {
                JSONObject singleDetailObj = (JSONObject)jsonArray.get(i);
                singleDetailBean = (CPUSingleDetailBean) JSONObject.toBean(singleDetailObj, CPUSingleDetailBean.class);
                singleDetailBeanList.add(singleDetailBean);
            }
            cpuMonitorDetailBean.setSingleDetailBeanList(singleDetailBeanList);
        }
		return cpuMonitorDetailBean;
	}
	private void getIOPSMonitorDetailBean(IOPSMonitorDetailBean iopsMonitorDetailBean,String keyword)throws HsCloudException{
		List<IOPSSingleDetailBean> singleDetailBeanList = null ;
		IOPSSingleDetailBean singleDetailBean = null;
		String jsonSingleDetail = RedisUtil.getValue("IOPSSingleDetail_" + keyword);
        if(StringUtils.isNotBlank(jsonSingleDetail)) {
            JSONArray jsonArray = JSONArray.fromObject(jsonSingleDetail);
            singleDetailBeanList = new ArrayList<IOPSSingleDetailBean>();
            for(int i = 0; i < jsonArray.size(); i++) {
                JSONObject singleDetailObj = (JSONObject)jsonArray.get(i);
                singleDetailBean = (IOPSSingleDetailBean) JSONObject.toBean(singleDetailObj, IOPSSingleDetailBean.class);
                singleDetailBeanList.add(singleDetailBean);
            }
            iopsMonitorDetailBean.setSingleDetailBeanList(singleDetailBeanList);
        }
	}
	private void getNETMonitorDetailBean(NETMonitorDetailBean netMonitorDetailBean,String keyword)throws HsCloudException{
		List<NETSingleDetailBean> singleDetailBeanList = null ;
		NETSingleDetailBean singleDetailBean = null;
		String jsonSingleDetail = RedisUtil.getValue("NETSingleDetail_" + keyword);
        if(StringUtils.isNotBlank(jsonSingleDetail)) {
            JSONArray jsonArray = JSONArray.fromObject(jsonSingleDetail);
            singleDetailBeanList = new ArrayList<NETSingleDetailBean>();
            for(int i = 0; i < jsonArray.size(); i++) {
                JSONObject singleDetailObj = (JSONObject)jsonArray.get(i);
                singleDetailBean = (NETSingleDetailBean) JSONObject.toBean(singleDetailObj, NETSingleDetailBean.class);
                singleDetailBeanList.add(singleDetailBean);
            }
            netMonitorDetailBean.setSingleDetailBeanList(singleDetailBeanList);
        }
	}
	private void getDISKMonitorDetailBean(DISKMonitorDetailBean diskMonitorDetailBean,String keyword)throws HsCloudException{
		List<DISKSingleDetailBean> singleDetailBeanList = null ;
		DISKSingleDetailBean singleDetailBean = null;
		String jsonSingleDetail = RedisUtil.getValue("DISKSingleDetail_" + keyword);
        if(StringUtils.isNotBlank(jsonSingleDetail)) {
            JSONArray jsonArray = JSONArray.fromObject(jsonSingleDetail);
            singleDetailBeanList = new ArrayList<DISKSingleDetailBean>();
            for(int i = 0; i < jsonArray.size(); i++) {
                JSONObject singleDetailObj = (JSONObject)jsonArray.get(i);
                singleDetailBean = (DISKSingleDetailBean) JSONObject.toBean(singleDetailObj, DISKSingleDetailBean.class);
                singleDetailBeanList.add(singleDetailBean);
            }
            diskMonitorDetailBean.setSingleDetailBeanList(singleDetailBeanList);
        }
	}

	@Override
	public void fillServerMonitoringInformation(VmInfoMonitorVO vmInfoMonitorVO)
			throws HsCloudException {
		double cpuUsage =0;
		double memoryUsage = 0;
		double diskUsage = 0;
		int cpuWorkloadActual = 0;
		int iopsReadActual = 0;
		int iopsWriteActual = 0;
		int netReadActual = 0;
		int netWriteActual = 0;
		String vmUUID = vmInfoMonitorVO.getVmId();
		ServerAcquisitionBean serverAcquisitionBean = null;
		CPUMonitorDetailBean cpuMonitorDetailBean = vmInfoMonitorVO.getCpuMonitorDetailBean();
		IOPSMonitorDetailBean iopsMonitorDetailBean = vmInfoMonitorVO.getIopsMonitorDetailBean();
		NETMonitorDetailBean netMonitorDetailBean = vmInfoMonitorVO.getNetMonitorDetailBean();
		DISKMonitorDetailBean diskMonitorDetailBean = vmInfoMonitorVO.getDiskMonitorDetailBean();
		if(vmUUID != null && !"".equals(vmUUID)){
			serverAcquisitionBean = this.getServerAcquisitionBean(vmUUID);
//			cpuMonitorDetailBean = new CPUMonitorDetailBean();
//			iopsMonitorDetailBean = new IOPSMonitorDetailBean();
//			netMonitorDetailBean = new NETMonitorDetailBean();
//			diskMonitorDetailBean = new DISKMonitorDetailBean();
			this.getCPUMonitorDetailBean(cpuMonitorDetailBean, vmUUID);
			this.getIOPSMonitorDetailBean(iopsMonitorDetailBean, vmUUID);
			this.getNETMonitorDetailBean(netMonitorDetailBean, vmUUID);
			this.getDISKMonitorDetailBean(diskMonitorDetailBean, vmUUID);
		}
		if(serverAcquisitionBean != null){
        	// CPU使用率
            cpuUsage = (serverAcquisitionBean.getvCPUUsage()!=null?serverAcquisitionBean.getvCPUUsage():0);
            //虚拟内存使用率
            memoryUsage = (serverAcquisitionBean.getvMemoryUsage()!=null?serverAcquisitionBean.getvMemoryUsage():0);
            //虚拟磁盘使用率
            diskUsage = (serverAcquisitionBean.getvDiskUsage()!=null?serverAcquisitionBean.getvDiskUsage():0);
            cpuWorkloadActual = (serverAcquisitionBean.getCpuWorkload()!=null?serverAcquisitionBean.getCpuWorkload():0);
            iopsReadActual = (serverAcquisitionBean.getIopsRead()!=null?serverAcquisitionBean.getIopsRead():0);
            iopsWriteActual = (serverAcquisitionBean.getIopsWrite()!=null?serverAcquisitionBean.getIopsWrite():0);
            netReadActual = (serverAcquisitionBean.getRxSpeed()!=null?serverAcquisitionBean.getRxSpeed():0);
            netWriteActual = (serverAcquisitionBean.getTxSpeed()!=null?serverAcquisitionBean.getTxSpeed():0);
        } 
		cpuMonitorDetailBean.setWorkloadActual(cpuWorkloadActual);                
        iopsMonitorDetailBean.setReadActual(iopsReadActual);                
        iopsMonitorDetailBean.setWriteActual(iopsWriteActual);                
        netMonitorDetailBean.setReadActual(netReadActual);                
        netMonitorDetailBean.setWriteActual(netWriteActual); 
		vmInfoMonitorVO.setCpuUsage(cpuUsage);
    	vmInfoMonitorVO.setMemoryUsage(memoryUsage);
    	vmInfoMonitorVO.setDiskUsage(diskUsage);
		vmInfoMonitorVO.setCpuMonitorDetailBean(cpuMonitorDetailBean);
		vmInfoMonitorVO.setIopsMonitorDetailBean(iopsMonitorDetailBean);
		vmInfoMonitorVO.setNetMonitorDetailBean(netMonitorDetailBean);
		vmInfoMonitorVO.setDiskMonitorDetailBean(diskMonitorDetailBean);		
	}

	@Override
	public void fillHostMonitoringInformation(HostInfoVO hostInfoVO)
			throws HsCloudException {
		double cpuUsage = 0;
		double memoryUsage = 0; 
		double diskUsage = 0;
		int cpuWorkloadActual = 0;			
		int iopsReadActual = 0;		
		int iopsWriteActual = 0;		
		int netReadActual = 0;			
		int netWriteActual = 0;
		String hostStatus = "false";
		HostAcquisitionBean hostAcquisitionBean = null;
		CPUMonitorDetailBean cpuMonitorDetailBean = null;
		IOPSMonitorDetailBean iopsMonitorDetailBean = null;
		NETMonitorDetailBean netMonitorDetailBean = null;
		cpuMonitorDetailBean = new CPUMonitorDetailBean();
		iopsMonitorDetailBean = new IOPSMonitorDetailBean();
		netMonitorDetailBean = new NETMonitorDetailBean();
    	try{
            hostAcquisitionBean = this.getHostAcquisitionBean(hostInfoVO.getHostName());
            if(hostAcquisitionBean != null){
            	hostStatus = (hostAcquisitionBean.getHostStatus()!=null?hostAcquisitionBean.getHostStatus():"false");
                cpuUsage = (hostAcquisitionBean.getCPUUsage()!=null?hostAcquisitionBean.getCPUUsage():0);
                memoryUsage = (hostAcquisitionBean.getMemoryUsage()!=null?hostAcquisitionBean.getMemoryUsage():0);
                diskUsage = (hostAcquisitionBean.getDiskUsage()!=null?hostAcquisitionBean.getDiskUsage():0);
                cpuWorkloadActual = (hostAcquisitionBean.getCpuWorkload()!=null?hostAcquisitionBean.getCpuWorkload():0);
                iopsReadActual = (hostAcquisitionBean.getIopsRead()!=null?hostAcquisitionBean.getIopsRead():0);
                iopsWriteActual = (hostAcquisitionBean.getIopsWrite()!=null?hostAcquisitionBean.getIopsWrite():0);
                netReadActual = (hostAcquisitionBean.getRxSpeed()!=null?hostAcquisitionBean.getRxSpeed():0);
                netWriteActual = (hostAcquisitionBean.getTxSpeed()!=null?hostAcquisitionBean.getTxSpeed():0);
            }
            cpuMonitorDetailBean.setWorkloadActual(cpuWorkloadActual);                
            iopsMonitorDetailBean.setReadActual(iopsReadActual);                
            iopsMonitorDetailBean.setWriteActual(iopsWriteActual);                
            netMonitorDetailBean.setReadActual(netReadActual);                
            netMonitorDetailBean.setWriteActual(netWriteActual);                
            this.getCPUMonitorDetailBean(cpuMonitorDetailBean,hostInfoVO.getHostName());
            this.getIOPSMonitorDetailBean(iopsMonitorDetailBean, hostInfoVO.getHostName());
            this.getNETMonitorDetailBean(netMonitorDetailBean, hostInfoVO.getHostName());
		}catch (Exception ex) {
			logger.error("redis HostAcquisition Exception:",ex);
		}            
        hostInfoVO.setCpuMonitorDetailBean(cpuMonitorDetailBean);
        hostInfoVO.setIopsMonitorDetailBean(iopsMonitorDetailBean);
        hostInfoVO.setNetMonitorDetailBean(netMonitorDetailBean);
        hostInfoVO.setHostStatus(hostStatus);//"true"
		hostInfoVO.setCpuUsage(cpuUsage);
		hostInfoVO.setMemoryUsage(memoryUsage);
		hostInfoVO.setDiskUsage(diskUsage);			
	}
}
