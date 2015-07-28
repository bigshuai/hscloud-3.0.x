/**
* @title ServerNodeServiceImpl.java
* @package com.hisoft.hscloud.bss.sla.sc.service.impl
* @description 用一句话描述该文件做什么
* @author jiaquan.hu
* @update 2012-5-8 上午9:34:24
* @version V1.0
*/
package com.hisoft.hscloud.bss.sla.sc.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openstack.api.common.IPMIManager;
import org.openstack.api.compute.HostActionResource;
import org.openstack.api.compute.HostActionsResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.sc.dao.ServerNodeDao;
import com.hisoft.hscloud.bss.sla.sc.entity.IPMIConfig;
import com.hisoft.hscloud.bss.sla.sc.entity.NodeIsolationConfig;
import com.hisoft.hscloud.bss.sla.sc.entity.ServerNode;
import com.hisoft.hscloud.bss.sla.sc.entity.ServerZone;
import com.hisoft.hscloud.bss.sla.sc.jsonBean.HostAcquisitionBean;
import com.hisoft.hscloud.bss.sla.sc.jsonBean.HostIsolationBean;
import com.hisoft.hscloud.bss.sla.sc.service.IServerNodeService;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.OpenstackUtil;
import com.hisoft.hscloud.common.util.RedisUtil;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author jiaquan.hu
 * @update 2012-5-8 上午9:34:24
 */
@Service
public class ServerNodeServiceImpl implements IServerNodeService {
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private ServerNodeDao serverNodeDao;
//	@Autowired
//	private IPMIConfigDao ipmiConfigDao;
	private OpenstackUtil openstackUtil = new OpenstackUtil();
	private Map<String,List<ServerNode>> serverNodeMap =null;
	
	@Override	
	public void save(ServerNode node){
		serverNodeDao.save(node);
		this.setIsolation(node);
	}
	
	@SuppressWarnings("unchecked")
	public List<ServerNode> pageServerNodes(Page<ServerNode> page){
		return (List<ServerNode>) serverNodeDao.findPage(page);
	}
	
	@Override
	public  List<ServerNode> getAllNodes(){
		return serverNodeDao.getAll();
	}	
		
	@Override
	public ServerNode getNodeById(long nodeId) {
		return serverNodeDao.findUniqueBy("id", nodeId);
	}
	
	@Override
	public ServerNode getNodeByName(String name) {
		return serverNodeDao.findUniqueBy("name", name);
	}

	@Override
	@Transactional
	public void delete(long nodeId) {
		serverNodeDao.delete(nodeId);
	}

	@Override
	public Page<ServerNode> findHostsByCondition(Page<ServerNode> page,
			String field, String fieldValue,String zoneCode,List<Object> zoneIds) throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter findHostsByCondition method.");			
		}
		if(fieldValue == null || "".equals(fieldValue)){
			page = serverNodeDao.findServerNode(page,zoneCode,zoneIds);
		}else{
			page = serverNodeDao.queryServerNodeByFuzzy(page, fieldValue,zoneCode,zoneIds);
		}
		if(logger.isDebugEnabled()){
			logger.debug("exit findHostsByCondition method.");
		}
		return page;
		
	}

	@Override
	public List<ServerNode> getNewNodes() throws HsCloudException {	
		if(logger.isDebugEnabled()){
			logger.debug("enter getNewNodes method.");			
		}
		List<ServerNode> nodes=new ArrayList<ServerNode>();
		ServerNode node = null;
		HostAcquisitionBean hostAcquisitionBean = null;
		int nodeId = 0;
		String jsonAcquisitionHostList = RedisUtil.getValue("Acquisition_hosts_list");
        if(StringUtils.isNotBlank(jsonAcquisitionHostList)) {
            JSONArray jsonArray = JSONArray.fromObject(jsonAcquisitionHostList);
            for(int i = 0; i < jsonArray.size(); i++) {
                JSONObject singleDetailObj = (JSONObject)jsonArray.get(i);
                hostAcquisitionBean = (HostAcquisitionBean) JSONObject.toBean(singleDetailObj, HostAcquisitionBean.class);
                if(hostAcquisitionBean.getHostName()==null ||"".equals(hostAcquisitionBean.getHostName())){
    				continue;
    			}
                node = serverNodeDao.findUniqueBy("name", hostAcquisitionBean.getHostName());
                if (node == null) {
    				node = new ServerNode();
    				nodeId += 1;
    				node.setId(nodeId);
    				node.setName(hostAcquisitionBean.getHostName());
    				node.setCpuType(hostAcquisitionBean.getPhysicalCPUFrequency());
    				node.setCpuInfo(String.valueOf(hostAcquisitionBean.getPhysicalCPUCore()));
    				node.setRamInfo(String.valueOf(hostAcquisitionBean.getPhysicalMemoryTotal()));
    				node.setDiskInfo(String.valueOf(hostAcquisitionBean.getPhysicalDiskTotal()));
    				node.setIp(hostAcquisitionBean.getHostIP());
    				node.setCreateDate(new Date());
    				node.setZone(hostAcquisitionBean.getZoneCode());
    				//add zjw time 20131009
    				node.setInnerIP(hostAcquisitionBean.getHostInnerIP());
    				nodes.add(node);
    			}
            }            
        }
//		HostList hostList=openstackUtil.getHscloudResource().getHosts();
//		List<HostBasic> list=hostList.getList();		
//		for(HostBasic host:list){
//			if(host.getHostname()==null ||"".equals(host.getHostname())){
//				continue;
//			}
//			node = serverNodeDao.findUniqueBy("name", host.getHostname());
//			if (node == null) {
//				node = new ServerNode();
//				nodeId += 1;
//				node.setId(nodeId);
//				node.setName(host.getHostname());
//				node.setCpuInfo(String.valueOf(host.getCpuNum()));
//				node.setRamInfo(String.valueOf(host.getRam()));
//				node.setDiskInfo(String.valueOf(host.getDisk()));
//				node.setIp(host.getIp());
//				node.setCreateDate(new Date());
//				node.setZone(host.getAvailabilityZone());
//				nodes.add(node);
//			}
//		}
		if(logger.isDebugEnabled()){
			logger.debug("exit getNewNodes method.");
		}
		return nodes;
	}

	@Override
	public List<ServerNode> getAllNodes(List<Object> nodeIds,String zoneCode) {		
		return serverNodeDao.getAllNodes(nodeIds,zoneCode);
	}

	@Override
	public Map<String, List<ServerNode>> getAllNodesGroupByZone() throws HsCloudException{
		List<ServerNode> nodeList = serverNodeDao.getAll();
		List<ServerNode> list = new ArrayList<ServerNode>();	
		serverNodeMap = new HashMap<String,List<ServerNode>>();		
		for(ServerNode serverNode : nodeList){
			ServerZone serverZone=serverNode.getServerZone();
			if(serverZone==null){
				continue;
			}
			if(serverZone.getName()==null){
				continue;
			}
			if(serverNodeMap.containsKey(serverZone.getName())){
				list.add(serverNode);
				serverNodeMap.put(serverZone.getName(), list);
			}else{
				list = new ArrayList<ServerNode>();
				list.add(serverNode);
				serverNodeMap.put(serverZone.getName(), list);
			}			
		}		
		return serverNodeMap;
	}

	@Override
	public List<ServerNode> getAllNodesByServerZone(long zoneId)
			throws HsCloudException {
		List<ServerNode> nodeList = serverNodeDao.getAllNodesByServerZone(zoneId);
		return nodeList;
	}

	@Override
	public IPMIManager connectIPMIManager(IPMIConfig IPMIConfig)
			throws HsCloudException {
		if(IPMIConfig == null || StringUtils.isEmpty(IPMIConfig.getIp()) || StringUtils.isEmpty(IPMIConfig.getUserName()) || StringUtils.isEmpty(IPMIConfig.getPassword())){
			return null;
		}
		IPMIManager ipmiManager = new IPMIManager(IPMIConfig.getIp(),IPMIConfig.getUserName(),IPMIConfig.getPassword());
		return ipmiManager;
	}

	@Override
	@Transactional
	public boolean updateServerNode(ServerNode serverNode)
			throws HsCloudException {		
		return serverNodeDao.updateServerNode(serverNode);
	}

	@Override
	public List<ServerNode> getAllNodesByServerZone(String zoneCode)
			throws HsCloudException {
		List<ServerNode> nodeList = serverNodeDao.getAllNodesByServerZone(zoneCode);
		return nodeList;
	}

	@Override
	public boolean setIsolation(ServerNode serverNode) throws HsCloudException {
		boolean result = false;
		String hostName = serverNode.getName();
		try{
			NodeIsolationConfig nodeIsolationConfig = serverNode.getNodeIsolationConfig();
			if(nodeIsolationConfig != null){
				HostIsolationBean hostIsolationBean = new HostIsolationBean();
				hostIsolationBean.setHostName(hostName);
				hostIsolationBean.setCpuRatio(serverNode.getCpuRate());
				hostIsolationBean.setMemoryRatio(serverNode.getRamRate());
				hostIsolationBean.setDiskRatio(serverNode.getDiskRate());
				hostIsolationBean.setHostNumber(nodeIsolationConfig.getHostNumber());
				hostIsolationBean.setIopsRead(nodeIsolationConfig.getIOPSRead());
				hostIsolationBean.setIopsWrite(nodeIsolationConfig.getIOPSWrite());
				hostIsolationBean.setCpuWorkload(nodeIsolationConfig.getCPUWorkload());
				hostIsolationBean.setRxSpeed(nodeIsolationConfig.getNetworkRead());
				hostIsolationBean.setTxSpeed(nodeIsolationConfig.getNetworkWrite());
				hostIsolationBean.setLimitStorageLeft(nodeIsolationConfig.getStorageSpace());
				RedisUtil.setValue("HostIsolation_"+hostName, hostIsolationBean.toJsonString());
				result = true;				
			}
		}catch (Exception e) {
			logger.error("setIsolation Exception:", e);
		}				
		return result;
	}

	@Override
	@Transactional
	public void synchronizationAllNodeIsolation() throws HsCloudException {
		List<ServerNode> serverNodeList = this.getAllNodes();
		for(ServerNode serverNode : serverNodeList){
			this.setIsolation(serverNode);
		}		
	}

	@Override
	@Transactional(readOnly=true)
	public boolean hasSameNodeName(ServerNode serverNode)
			throws HsCloudException {
		boolean result = false;
		try{
//			ServerNode node = serverNodeDao.findUniqueBy("nodeAliases", serverNode.getNodeAliases());
			ServerNode node = serverNodeDao.getUniqueServerNode(serverNode.getNodeAliases());
			if(node!=null && node.getNodeAliases()!=null){
				result = true;
			}
			if(serverNode.getId()==node.getId()){
				result = false;
			}
			node = null;
		}catch (Exception e) {
			logger.error("hasSameNodeName Exception:", e);
		}		
		return result;
	}

	@Override
	public List<ServerNode> getAllNodesByServerZone(List<Object> zoneIds,
			String zoneCode) throws HsCloudException {
		return serverNodeDao.getAllNodesByServerZone(zoneIds, zoneCode);
	}

	@Override
	@Transactional
	public boolean enableServerNode(ServerNode serverNode)
			throws HsCloudException {
		boolean resultFlag = false;
		try{
				HostActionsResource har =openstackUtil.getCompute().hostActionsResource();
				HostActionResource ha = har.host(serverNode.getName());//节点名称
				if(serverNode.getIsEnable()==0){				
					ha.enableHostService("compute");//compute目前为固定值
				}else{
					ha.disableHostService("compute");//compute目前为固定值
				}
				resultFlag = serverNodeDao.updateServerNode(serverNode);
		}catch (Exception e) {
			logger.error("enableServerNode Exception:", e);
		}		
		return resultFlag;
	}

}
