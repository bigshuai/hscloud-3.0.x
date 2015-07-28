package com.pactera.hscloud.hscloudhandler.adapter;

import java.util.ArrayList;
import java.util.List;

import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openstack.client.JobHeader;
import org.openstack.client.OpenStackClient;
import org.openstack.model.compute.nova.NovaServerForCreate;
import org.openstack.model.compute.nova.NovaServerForCreate.NetworkLan;
import org.openstack.model.compute.nova.server.actions.RebuildAction;
import org.openstack.model.hscloud.impl.VifAdd;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.IPConvert;
import com.pactera.hscloud.common4j.util.DBUtil;
import com.pactera.hscloud.hscloudhandler.bo.HcVpdcReference;
import com.pactera.hscloud.hscloudhandler.bo.O_IP_NETWORK;
import com.pactera.hscloud.hscloudhandler.util.OpenstackUtil;

public class HsCloudAdapter {
	private static Logger logger = Logger.getLogger(HsCloudAdapter.class);
	private OpenstackUtil openstackUtil = new OpenstackUtil();
	private OpenStackClient client =openstackUtil.initClient();

	// 购买创建
	public void createVM_Buy(JSONObject param, Long jobId, String jobType)
			throws Exception {
		NovaServerForCreate serverForCreate = (NovaServerForCreate) JSONObject
				.toBean(param, NovaServerForCreate.class);
		logger.info("createVMBean:" + serverForCreate.toString());
		String zoneCode=serverForCreate.getZone();
		client.getComputeEndpoint(zoneCode)
				.servers(
						getJobHeader(jobId, jobType,
								genMethodJson("createVM_Buy")))
				.post(serverForCreate);
	}

	// 试用创建
	public void createVM_Try(JSONObject param, Long jobId, String jobType)
			throws Exception {
		NovaServerForCreate serverForCreate = (NovaServerForCreate) JSONObject
				.toBean(param, NovaServerForCreate.class);
		logger.info("createVMBean:" + serverForCreate.toString());
		String zoneCode=serverForCreate.getZone();
		client.getComputeEndpoint(zoneCode)
				.servers(
						getJobHeader(jobId, jobType,
								genMethodJson("createVM_Try")))
				.post(serverForCreate);
	}

	// 后台管理员创建
	public void createVM_Admin(JSONObject param, Long jobId, String jobType)
			throws Exception {
		NovaServerForCreate serverForCreate = (NovaServerForCreate) JSONObject
				.toBean(param, NovaServerForCreate.class);
		logger.info("createVMBean:" + serverForCreate.toString());
		String zoneCode=serverForCreate.getZone();
		if(zoneCode.indexOf("$")!=-1){
			zoneCode=zoneCode.split("\\$")[0];
		}
		client.getComputeEndpoint(zoneCode)
				.servers(
						getJobHeader(jobId, jobType,
								genMethodJson("createVM_Admin")))
				.post(serverForCreate);
	}

	// 发布创建
	public void createVM_Publish(JSONObject param, Long jobId, String jobType)
			throws Exception {
		NovaServerForCreate serverForCreate = (NovaServerForCreate) JSONObject
				.toBean(param, NovaServerForCreate.class);
		logger.info("createVMBean:" + serverForCreate.toString());
		String zoneCode=serverForCreate.getZone();
		if(zoneCode.indexOf("$")!=-1){
			zoneCode=zoneCode.split("\\$")[0];
		}
		client.getComputeEndpoint(zoneCode)
				.servers(
						getJobHeader(jobId, jobType,
								genMethodJson("createVM_Publish")))
				.post(serverForCreate);
	}

	// 退款删除
	public void deleteVM_Refund(JSONObject param, Long jobId, String jobType)
			throws Exception {
		String uuid = (String) param.get("uuid");
		logger.info("VM uuid:" + uuid);
		String zoneCode=getZonebyUUID(uuid);
		client.getComputeEndpoint(zoneCode)
				.servers(
						getJobHeader(jobId, jobType,
								genMethodJson("deleteVM_Refund"))).server(uuid)
				.delete();
	}

	// 到期删除
	public void deleteVM_Expire(JSONObject param, Long jobId, String jobType)
			throws Exception {
		String uuid = (String) param.get("uuid");
		logger.info("VM uuid:" + uuid);
		String zoneCode=getZonebyUUID(uuid);
		client.getComputeEndpoint(zoneCode)
				.servers(
						getJobHeader(jobId, jobType,
								genMethodJson("deleteVM_Expire"))).server(uuid)
				.delete();
	}

	// 后台管理员删除
	public void deleteVM_Admin(JSONObject param, Long jobId, String jobType)
			throws Exception {
		String uuid = (String) param.get("uuid");
		logger.info("VM uuid:" + uuid);
		String zoneCode=getZonebyUUID(uuid);
		client.getComputeEndpoint(zoneCode)
				.servers(
						getJobHeader(jobId, jobType,
								genMethodJson("deleteVM_Admin"))).server(uuid)
				.delete();
	}

	// 实例删除
	public void deleteVM_Instance_Admin(JSONObject param, Long jobId,
			String jobType) throws Exception {
		String uuid = (String) param.get("uuid");
		logger.info("VM uuid:" + uuid);
		String zoneCode=getZonebyUUID(uuid);
		client.getComputeEndpoint(zoneCode)
				.servers(
						getJobHeader(jobId, jobType,
								genMethodJson("deleteVM_Instance_Admin")))
				.server(uuid).delete();
	}

	// 绑定ip
	public void IP_Bind(JSONObject param, Long jobId, String jobType)
			throws Exception {
		String uuid = (String) param.get("uuid");
		String ip = (String) param.get("ip");
		String zoneCode=getZonebyUUID(uuid);
		logger.info("IP_Bind======" + "uuid:" + uuid + "======" + "ip:" + ip);
		client.getComputeEndpoint(zoneCode)
				.servers(getJobHeader(jobId, jobType, genMethodJson("IP_Bind")))
				.server(uuid).addFloatingIp(ip);
	}

	// 删除ip
	public void IP_Delete(JSONObject param, Long jobId, String jobType)
			throws Exception {
		String uuid = (String) param.get("uuid");
		String ip = (String) param.get("ip");
		String zoneCode=getZonebyUUID(uuid);
		logger.info("IP_Delete======" + "uuid:" + uuid + "======" + "ip:" + ip);
		client.getComputeEndpoint(zoneCode)
				.servers(
						getJobHeader(jobId, jobType, genMethodJson("IP_Delete")))
				.server(uuid).removeFloatingIp(ip);
	}

	// 绑定扩展盘
	public void extendedDisk_Add(JSONObject param, Long jobId, String jobType)
			throws Exception {
		String uuid = (String) param.get("uuid");
		String extDisks = (String) param.get("extDisks");
		String zoneCode=getZonebyUUID(uuid);
		if (StringUtils.isNotBlank(extDisks) && StringUtils.isNotBlank(uuid)) {
			String[] disks = extDisks.split(";");
			for (String disk : disks) {
				String[] diskInfo = disk.split(":");
				if (diskInfo.length == 2) {
					String diskName = diskInfo[0];
					String diskSize = diskInfo[1];
					logger.info("extendDisk_Add:" + "=====" + "diskName:"
							+ diskName + "======" + "diskSize:" + diskSize);
					client.getComputeEndpoint(zoneCode)
							.servers(
									getJobHeader(jobId, jobType,
											genMethodJson("extendedDisk_Add")))
							.server(uuid)
							.createAndAttachDisk(diskName,
									Integer.parseInt(diskSize));
				}
			}
		}
	}

	// 删除扩展盘
	public void extendedDisk_Delete(JSONObject param, Long jobId, String jobType)
			throws Exception {
		String uuid = (String) param.get("uuid");
		String extDiskId = (String) param.get("extDiskId");
		String zoneCode=getZonebyUUID(uuid);
		logger.info("extendedDisk_Delete:" + "=====" + "uuid:" + uuid
				+ "======" + "diskId:" + extDiskId);
		client.getComputeEndpoint(zoneCode)
				.servers(
						getJobHeader(jobId, jobType,
								genMethodJson("extendedDisk_Delete")))
				.server(uuid).deleteAndDetachDisk(Integer.parseInt(extDiskId));
	}

	// 重置vm
	public void VM_Reset(JSONObject param, Long jobId, String jobType) {
		String uuid = (String) param.get("uuid");
		String flavorStr=(String)param.get("flavorId");
		String zoneCode=getZonebyUUID(uuid);
		Integer flavorId = Integer.parseInt(flavorStr) ;
		logger.info("VM uuid:" + uuid + ",flavorId:" + flavorId);
		client.getComputeEndpoint(zoneCode)
				.servers(
						getJobHeader(jobId, jobType, genMethodJson("VM_Reset")))
				.server(uuid).hsResize(flavorId);
	}

	// 重置系统
	public void VM_Rebuild(JSONObject param, Long jobId, String jobType) {
		String uuid = (String) param.get("uuid");
		String imageId = (String) param.get("imageId");
		String user_pwd = (String) param.get("use_pwd");
		String osId=(String)param.get("osId");
		String zoneCode=getZonebyUUID(uuid);
		logger.info("VM uuid:" + uuid + ",imageId:" + imageId+"zoneCode=========="+zoneCode);
		RebuildAction action = new RebuildAction();
		action.setImageRef(imageId);
		action.setAdminPass(user_pwd);
		action.setOsId(Integer.parseInt(osId));
		
		client.getComputeEndpoint(zoneCode)
				.servers(
						getJobHeader(jobId, jobType,
								genMethodJson("VM_Rebuild"))).server(uuid)
				.rebuild(action);
	}

	// 开启虚拟机
	public void VM_Open(JSONObject param, Long jobId, String jobType) {
		String uuid = (String) param.get("uuid");
		String zoneCode=getZonebyUUID(uuid);
		logger.info("VM uuid:" + uuid);
		client.getComputeEndpoint(zoneCode)
				.servers(getJobHeader(jobId, jobType, genMethodJson("VM_Open")))
				.server(uuid).resume();
	}

	// 关闭虚拟机
	public void VM_Close(JSONObject param, Long jobId, String jobType) {
		String uuid = (String) param.get("uuid");
		String zoneCode=getZonebyUUID(uuid);
		logger.info("VM uuid:" + uuid+"ZoneCode==========="+zoneCode);
		client.getComputeEndpoint(zoneCode)
				.servers(
						getJobHeader(jobId, jobType, genMethodJson("VM_Close")))
				.server(uuid).suspend();
	}

	// 重启虚拟机
	public void VM_Reboot(JSONObject param, Long jobId, String jobType) {
		String uuid = (String) param.get("uuid");
		String zoneCode=getZonebyUUID(uuid);
		logger.info("VM uuid:" + uuid);
		client.getComputeEndpoint(zoneCode)
				.servers(
						getJobHeader(jobId, jobType, genMethodJson("VM_Reboot")))
				.server(uuid).reboot("HARD");
	}

	// 备份虚拟机
	public void VM_Backup(JSONObject param, Long jobId, String jobType) {
		String uuid=(String)param.get("uuid");
        String backupID=(String)param.get("backupID");
        String zoneCode=getZonebyUUID(uuid);
        logger.info("VM uuid:" + uuid+";backupID:"+backupID);
        client.getComputeEndpoint(zoneCode)
		.servers(
				getJobHeader(jobId, jobType, genMethodJson("VM_Backup")))
		.server(uuid).createSnapshot(backupID);
	}

	// 还原虚拟机
	public void VM_Reduce(JSONObject param, Long jobId, String jobType) {
		String uuid=(String)param.get("uuid");
		String snapshotName=(String)param.get("snapshotName");
		String zoneCode=getZonebyUUID(uuid);
		logger.info("VM uuid:" + uuid+";snapshotName:"+snapshotName);
		 client.getComputeEndpoint(zoneCode)
			.servers(
					getJobHeader(jobId, jobType, genMethodJson("VM_Reduce")))
			.server(uuid).recoverSnapshot(snapshotName);
	}
	
	//重置密码
	public void VM_ResetSysPwd(JSONObject param, Long jobId, String jobType){
		String uuid=(String)param.get("uuid");
		String passwd=(String)param.get("password");
		String zoneCode=getZonebyUUID(uuid);
		logger.info("VM uuid:" + uuid+";passwd:"+passwd);
		 client.getComputeEndpoint(zoneCode)
			.servers(
					getJobHeader(jobId, jobType, genMethodJson("ResetSysPwd")))
			.server(uuid).injectAdminPassword(passwd);
	}
	//调整原有扩展盘
	public void extendedDisk_Modify(JSONObject param, Long jobId, String jobType) {
		String uuid = (String) param.get("uuid");
		String extDiskInfo = (String) param.get("extDiskInfo");
		String zoneCode=getZonebyUUID(uuid);
		logger.info("VM uuid:" + uuid + ";extDiskInfo:" + extDiskInfo);
		if (StringUtils.isNotBlank(extDiskInfo)) {
			String[] extDiskInfoArrayLocal = extDiskInfo.split(",");
			if (extDiskInfoArrayLocal != null
					&& extDiskInfoArrayLocal.length > 0) {
				client.getComputeEndpoint(zoneCode)
						.servers(
								getJobHeader(jobId, jobType,
										genMethodJson("extendedDisk_Modify")))
						.server(uuid).expandDiskSize(extDiskInfoArrayLocal);
			}
		}
	}
	//重置操作系统失败后修复操作
	public void VM_OSRepair(JSONObject param, Long jobId, String jobType){
		String uuid = (String) param.get("uuid");
		String zoneCode=getZonebyUUID(uuid);
		logger.info("VM uuid:" + uuid);
		client.getComputeEndpoint(zoneCode)
				.servers(
						getJobHeader(jobId, jobType,genMethodJson(Constants.JOBSERVER_METHOD_VMOSRepair)))
				.server(uuid).osRepair();
	}
	
	//对软删除状态的VM进行恢复
	public void VM_RecycleRestore(JSONObject param, Long jobId, String jobType){
		String uuid = (String) param.get("uuid");
		String zoneCode=getZonebyUUID(uuid);
		logger.info("VM uuid:" + uuid);
		client.getComputeEndpoint(zoneCode)
				.servers(
						getJobHeader(jobId, jobType,genMethodJson(Constants.JOBSERVER_METHOD_VMRecycleRestore)))
				.server(uuid).restore();
	}

	//对软删除状态的VM进行彻底删除
	public void VM_RecycleDelete(JSONObject param, Long jobId, String jobType){
		String uuid = (String) param.get("uuid");
		String zoneCode=getZonebyUUID(uuid);
		logger.info("VM uuid:" + uuid);
		client.getComputeEndpoint(zoneCode)
				.servers(
						getJobHeader(jobId, jobType,genMethodJson(Constants.JOBSERVER_METHOD_VMRecycleDelete)))
				.server(uuid).forceDelete();
	}
	
	//迁移虚拟机
	public void VM_Migrate(JSONObject param,Long jobId,String jobType){
		String uuid=(String)param.get("uuid");
		String hostName=(String)param.get("hostName");
		String hostIP=(String)param.get("hostIP");
		String zoneCode=getZonebyUUID(uuid);
		logger.info("VM uuid:" + uuid+",hostName:"+hostName+",hostIP:"+hostIP);
		client.getComputeEndpoint(zoneCode)
				.servers(
						getJobHeader(jobId, jobType,genMethodJson(Constants.JOBSERVER_METHOD_VMMigrate )))
				.server(uuid).osColdMigrate(hostName,hostIP);
	}
	//启动路由
	public void Router_Open(JSONObject param,Long jobId,String jobType){
		String uuid = (String) param.get("uuid");
		String zoneCode=getZonebyUUID(uuid);
		logger.info("ROUTER uuid:" + uuid);
		client.getComputeEndpoint(zoneCode)
				.servers(getJobHeader(jobId, jobType, 
						genMethodJson(Constants.JOBSERVER_METHOD_RouterOpen
								,Constants.OBJECT_ROUTER)))
				.server(uuid).resume();
	}
	//关闭路由
	public void Router_Close(JSONObject param,Long jobId,String jobType){
		String uuid = (String) param.get("uuid");
		String zoneCode=getZonebyUUID(uuid);
		logger.info("ROUTER uuid:" + uuid);
		client.getComputeEndpoint(zoneCode)
				.servers(getJobHeader(jobId, jobType, 
						genMethodJson(Constants.JOBSERVER_METHOD_RouterClose
								,Constants.OBJECT_ROUTER)))
				.server(uuid).suspend();
	}
	//重启路由
	public void Router_Reboot(JSONObject param,Long jobId,String jobType){
			String uuid = (String) param.get("uuid");
			String zoneCode=getZonebyUUID(uuid);
			logger.info("ROUTER uuid:" + uuid);
			client.getComputeEndpoint(zoneCode)
					.servers(getJobHeader(jobId, jobType, 
							genMethodJson(Constants.JOBSERVER_METHOD_RouterReboot
									,Constants.OBJECT_ROUTER)))
					.server(uuid).reboot("HARD");
	}
	//非路由模式VPDC下创建VM
	public void createVM_NoRouterVPDC(JSONObject param,Long jobId,String jobType){
		NovaServerForCreate serverForCreate = (NovaServerForCreate) JSONObject
				.toBean(param, NovaServerForCreate.class);
		logger.info("createVMBean:" + serverForCreate.toString());
		Object networksObj=serverForCreate.getNetworks().get(0);
		MorphDynaBean networksJsonObj=(MorphDynaBean)networksObj;
		int lanId=(Integer)networksJsonObj.get("lanId");
		List<NetworkLan> networks=new ArrayList<NetworkLan>();
		NetworkLan network=new NetworkLan();
		network.setLanId(lanId);
		network.setNetworkId("default");
		networks.add(network);
		serverForCreate.setNetworks(networks);
		String zoneCode=serverForCreate.getZone();
		client.getComputeEndpoint(zoneCode)
				.servers(
						getJobHeader(jobId, jobType,
								genMethodJson(Constants.JOBSERVER_METHOD_CrVMNoRouterVPDC,
										Constants.OBJECT_VM)))
				.post(serverForCreate);
	}
	//路由模式VPDC下创建VM
	public void createVM_RouterVPDC(JSONObject param,Long jobId,String jobType)throws Exception{
		NovaServerForCreate serverForCreate = (NovaServerForCreate) JSONObject
				.toBean(param, NovaServerForCreate.class);
		Object networksObj=serverForCreate.getNetworks().get(0);
		MorphDynaBean networksJsonObj=(MorphDynaBean)networksObj;
		int lanId=(Integer)networksJsonObj.get("lanId");
		String networkId=(String)networksJsonObj.get("networkId");
		List<NetworkLan> networks=new ArrayList<NetworkLan>();
		NetworkLan network=new NetworkLan();
		network.setLanId(lanId);
		network.setNetworkId(networkId);
		networks.add(network);
		serverForCreate.setNetworks(networks);
		logger.info("createVMBean:" + serverForCreate.toString());
		String zoneCode=serverForCreate.getZone();
		client.getComputeEndpoint(zoneCode)
				.servers(
						getJobHeader(jobId, jobType,
								genMethodJson(Constants.JOBSERVER_METHOD_CrVMRouterVPDC,
										Constants.OBJECT_VM)))
				.post(serverForCreate);
	}
	
	//创建路由模式VPDC
	public void createRouter_Buy(JSONObject param,Long jobId,String jobType)throws Exception{
		NovaServerForCreate serverForCreate = (NovaServerForCreate) JSONObject
				.toBean(param, NovaServerForCreate.class);
        List<NetworkLan> networks = new ArrayList<NetworkLan>();
		NetworkLan nl = new NetworkLan();
		logger.info("createRouter_Buy:当前IP"+serverForCreate.getFloatingIp());
		O_IP_NETWORK IPObject=getIpForNetwork(serverForCreate.getFloatingIp());
		nl.setNetworkId(IPObject.getNetwork_id());
		nl.setFixedIp(serverForCreate.getFloatingIp());
		networks.add(nl);
		serverForCreate.setNetworks(networks);
		logger.info("createRouterBean:" + serverForCreate.toString());
		String zoneCode=serverForCreate.getZone();
		StringBuilder ext = new StringBuilder("{\"method\":\"");
		ext.append(Constants.JOBSERVER_METHOD_CrRouterBuy)
		.append("\"").append(",\"obj_type\":")
		.append(Constants.OBJECT_ROUTER).append(",\"networkId\":").
		append(IPObject.getId()).append("}");
		serverForCreate.setFloatingIp(null);
		client.getComputeEndpoint(zoneCode)
				.servers(
						getJobHeader(jobId, jobType,
								ext.toString()))
				.post(serverForCreate);
	}
	
	// 路由绑定LanNetwork
	public void routerAddVif(JSONObject param, Long jobId, String jobType) {
		String routerUUID=(String)param.get("routerUUID");
		String securytyVlanId=(String)param.get("securytyVlanId");
		String networkId=(String)param.get("networkId");
		String vm_id=(String)param.get("vm_id");
		String zoneCode=getZonebyUUID(routerUUID);
		logger.info("Router uuid:" + routerUUID);
		VifAdd va = new VifAdd();
		List<VifAdd.RequestedNetwork> lvarn = new ArrayList<VifAdd.RequestedNetwork>();
		VifAdd.RequestedNetwork varn = new VifAdd.RequestedNetwork();
		varn.setNetworkId(networkId);
		varn.setLanId(Integer.valueOf(securytyVlanId));
		lvarn.add(varn);
		va.setRequestedNetworks(lvarn);
		client.getComputeEndpoint(zoneCode)
		.servers(
				getJobHeader(jobId, jobType,
				genMethodJosn(Constants.JOBSERVER_METHOD_RouterAddVif,
		        		Constants.OBJECT_ROUTER,vm_id)))
				.server(routerUUID).addVif(va);
	}
	
	private JobHeader getJobHeader(Long jobId, String jobType, String jobExt) {
		JobHeader jobHeader = new JobHeader();
		jobHeader.setJobId(String.valueOf(jobId));
		jobHeader.setJobType(jobType);
		jobHeader.setJobExt(jobExt);
		return jobHeader;
	}

	private String genMethodJson(String value) {
		StringBuilder ext = new StringBuilder("{\"method\":\"");
		ext.append(value).append("\"}");
		return ext.toString();
	}
	
	private String genMethodJosn(String method,int objectType,String vm_id){
		StringBuilder ext = new StringBuilder("{\"method\":\"");
		ext.append(method).append("\"").append(",\"obj_type\":")
		.append(objectType).append(",\"vm_id\":").append(vm_id).append("}");
		return ext.toString();
	}
	
	private String genMethodJson(String method,int objectType){
		StringBuilder ext = new StringBuilder("{\"method\":\"");
		ext.append(method).append("\"").append(",\"obj_type\":")
		.append(objectType).append("}");
		return ext.toString();
	}
	
	private String getZonebyUUID(String uuid){
		String result=null;
		StringBuilder sql = new StringBuilder();
		sql.append(
				" where t1.id=t2.VpdcRefrenceId and t2.vm_id = '")
				.append(uuid).append("'");
		try{
			List<HcVpdcReference> zoneList = DBUtil.getResult("hc_vpdc_reference",sql.toString());
			if(zoneList!=null&&zoneList.size()==1){
				HcVpdcReference vr=zoneList.get(0);
				if(vr!=null){
					String vm_zone=vr.getVm_zone();
					result=vm_zone.split("\\$")[0];
				}
			}
		}catch(Exception e){
			logger.debug(e.getMessage(),e);
			throw new RuntimeException(e);
			
		}
		return result;
	}
	
	/**
	 *  通过zoneGroup_id获取ip并更新hc_ip_detail 表中ip的object_id,status,modify_uid字段。 
	* <功能详细描述> 
	* @param message
	* @return
	* @throws Exception 
	* @see [类、类#方法、类#成员]
	 */
	private O_IP_NETWORK getIpForNetwork(String ipStr) throws Exception {
		try {
			// 获取zone信息，因为要根据zone去查询对应的ip
			O_IP_NETWORK result =null;
			StringBuilder sql = new StringBuilder();
			sql.append(" where t.ip_range_id=t1.id and t1.id=t2.ipRange_id ")
					.append(" and t.ip=").append(IPConvert.getIntegerIP(ipStr));
			// 一次性取出10个ip
			List<Object> ipDetailList = DBUtil.getResult("hc_vpdc_network",
					sql.toString());
			if (null != ipDetailList && ipDetailList.size() >= 1) {
				O_IP_NETWORK ip = (O_IP_NETWORK) ipDetailList.get(0);
				result = ip;
			} else {
				throw new Exception("根据ip获取networkId异常，异常IP："+ipStr);
			}
			return result;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
} 
