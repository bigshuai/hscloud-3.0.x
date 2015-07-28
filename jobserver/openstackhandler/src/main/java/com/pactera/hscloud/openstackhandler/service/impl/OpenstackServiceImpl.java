package com.pactera.hscloud.openstackhandler.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hisoft.hscloud.common.entity.LogBizType;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.IPConvert;
import com.hisoft.hscloud.common.util.MD5Util;
import com.hisoft.hscloud.common.util.PasswordUtil;
import com.hisoft.hscloud.common.util.RabbitMQUtil;
import com.hisoft.hscloud.mail.constant.MailTemplateType;
import com.pactera.hscloud.common4j.util.Common4jDateFormat;
import com.pactera.hscloud.common4j.util.CommonThreadLocal;
import com.pactera.hscloud.openstackhandler.bo.OPSResult;
import com.pactera.hscloud.openstackhandler.bo.O_Domain;
import com.pactera.hscloud.openstackhandler.bo.O_EmailQueue;
import com.pactera.hscloud.openstackhandler.bo.O_IP;
import com.pactera.hscloud.openstackhandler.bo.O_MailTemplate;
import com.pactera.hscloud.openstackhandler.bo.O_Message;
import com.pactera.hscloud.openstackhandler.bo.O_Network;
import com.pactera.hscloud.openstackhandler.bo.O_NodeName;
import com.pactera.hscloud.openstackhandler.bo.O_OS;
import com.pactera.hscloud.openstackhandler.bo.O_PanelUser;
import com.pactera.hscloud.openstackhandler.bo.O_Router;
import com.pactera.hscloud.openstackhandler.bo.O_Snapshot;
import com.pactera.hscloud.openstackhandler.bo.O_User;
import com.pactera.hscloud.openstackhandler.bo.O_VPDCInstance;
import com.pactera.hscloud.openstackhandler.bo.O_VPDCReference;
import com.pactera.hscloud.openstackhandler.bo.O_VPDC_Extdisk;
import com.pactera.hscloud.openstackhandler.bo.ResourceResult;
import com.pactera.hscloud.openstackhandler.dao.OpenstackDao;
import com.pactera.hscloud.openstackhandler.dao.impl.OpenstackDaoImpl;
import com.pactera.hscloud.openstackhandler.exception.OpenstackHandlerException;
import com.pactera.hscloud.openstackhandler.service.OpenstackService;
import com.pactera.hscloud.openstackhandler.vo.IPVO;
import com.pactera.hscloud.openstackhandler.vo.MessageVO;
import com.pactera.hscloud.openstackhandler.vo.NetWorkVO;
import com.pactera.hscloud.openstackhandler.vo.SnapshotVO;
import com.pactera.hscloud.openstackhandler.vo.VMOpsVO;
import com.pactera.hscloud.openstackhandler.vo.VMVO;
import com.pactera.hscloud.openstackhandler.vo.VPDCReferenceVO;

public class OpenstackServiceImpl implements OpenstackService {

	private static Log logger = LogFactory.getLog(OpenstackService.class);
	private OpenstackDao openstackDao = new OpenstackDaoImpl();

	/**
	 * 创建虚拟机
	 */
	@Override
	public void createVM(VPDCReferenceVO vpdcrvo, O_VPDCInstance vpdci) {

		try {
			this.updateVPDCRNoState(vpdcrvo);
			this.updateDiskVmId(vpdci.getVpdcRefrenceId(), vpdci.getVm_id(),vpdcrvo.getInitVmId());
			StringBuffer sb = new StringBuffer();
			if(null == vpdcrvo.getInitVmId() || "".equals(vpdcrvo.getInitVmId())){
				if(null == vpdci.getId()){
					throw new OpenstackHandlerException(" initVmId and instance id are null");
				}
				sb.append(" where id=").append(vpdci.getId());
			}else{
				sb.append(" where initVmId='").append(vpdcrvo.getInitVmId()).append("'");
			}
			
//			O_VPDCInstance i = (O_VPDCInstance) openstackDao.getUnique(
//					"hc_vpdc_instance"," where initVmId='"+vpdcrvo.getInitVmId()+"'");
			O_VPDCInstance i = (O_VPDCInstance) openstackDao.getUnique("hc_vpdc_instance",sb.toString());
			i.copy(vpdci);
			openstackDao.save(i, "sync_hc_vpdc_instance.update");
		} catch (Exception e) {
			logger.error("" + e.getMessage(), e);
			throw new OpenstackHandlerException(e.getMessage());
		}

	}
	
	/**
	 * 创建路由
	 */
	@Override
	public void createRouter(O_Router router) {
		try {
			this.updateNetWorkUUID(router);
			this.updateRouterNoState(router);
		} catch (Exception e) {
			logger.error("" + e.getMessage(), e);
			throw new OpenstackHandlerException(e.getMessage());
		}
		
	}

	/**
	 * 绑定磁盘
	 */
	@Override
	public void bindingDisk(List<O_VPDC_Extdisk> disks) {

		if(null == disks || disks.isEmpty()){
			return ;
		}
		try {
            for (O_VPDC_Extdisk disk : disks) {
            	if (null == disk.getReferenceId()
    					|| "".equals(disk.getReferenceId())) {
    				if (null == disk.getVmId() && "".equals(disk.getVmId())) {
    					throw new OpenstackHandlerException(
    							"uuid and referenceid are null");
    				}
    				O_VPDCInstance vpdcivo = (O_VPDCInstance) openstackDao
    						.getUnique("hc_vpdc_instance", " where vm_id='"
    								+ disk.getVmId() + "'");
    				disk.setReferenceId(vpdcivo.getVpdcRefrenceId());
    			}
    			if ((null == disk.getName() || "".equals(disk.getName()))
    					|| null == disk.getReferenceId()
    					|| "".equals(disk.getReferenceId())) {
    				throw new OpenstackHandlerException(
    						"disk name or referenceid is null");
    			}
    			O_VPDC_Extdisk vd = (O_VPDC_Extdisk) openstackDao.getUnique(
    					"hc_vpdcreference_extdisk",
    					" where name='" + disk.getName() + "' and referenceId="
    							+ disk.getReferenceId());
    			vd.copy(disk);
    			openstackDao.save(vd, "sync_hc_vpdcreference_extdisk.update");
			}

		} catch (Exception e) {
			logger.error("" + e.getMessage(), e);
			throw new OpenstackHandlerException(e.getMessage());
		}

	}

	/**
	 * 更新快照
	 */
	@Override
	public void updateSnapshot(O_Snapshot ss) {

		try {

			if (null == ss.getId() || "".equals(ss.getId())) {
				throw new OpenstackHandlerException("snampshot id is null");
			}
			O_Snapshot s = (O_Snapshot) openstackDao.getUnique(
					"hc_vm_snapshot", " where id=" + ss.getId());
			s.copy(ss);
			openstackDao.save(s, "sync_hc_vm_snapshot.update");

		} catch (Exception e) {
			logger.error("" + e.getMessage(), e);
			throw new OpenstackHandlerException(e.getMessage());
		}

	}

	/**
	 * 备份
	 */
	@Override
	public void backupVM(SnapshotVO snapshotVO) {

		try {
			
			if (null == snapshotVO.getId()
					|| "".equals(snapshotVO.getId())) {
				throw new OpenstackHandlerException(" snapshot id is null.");
			}
			O_Snapshot ss = (O_Snapshot)snapshotVO;
			this.updateSnapshot(ss);

		} catch (Exception e) {
			logger.error("" + e.getMessage(), e);
			throw new OpenstackHandlerException(e.getMessage());
		}
	}

	/**
	 * 绑定ip(虚拟机)
	 */
	@Override
	public void bindingIP(VPDCReferenceVO vpdcrvo, IPVO ipvo) {

		try {
			logger.info("enter bindingIP !");
			O_IP ip = (O_IP) ipvo;
			StringBuffer sb = new StringBuffer();
			if (null != vpdcrvo.getInstanceId() && !"".equals(vpdcrvo.getInstanceId())){
				sb.append("  where id=").append(vpdcrvo.getInstanceId());
			}else if(null != vpdcrvo.getUuid()
					&& !"".equals(vpdcrvo.getUuid())) {
				sb.append("  where vm_id='").append(vpdcrvo.getUuid()).append("'");
			}else if (null != vpdcrvo.getId()) {
				sb.append("  where status=0 and VpdcRefrenceId=").append(vpdcrvo.getId());
			}else{
				throw new OpenstackHandlerException("instanceId  vm_id or refrenceId is null");
			}
			O_VPDCInstance vpdci = (O_VPDCInstance) openstackDao
					.getUnique("hc_vpdc_instance", sb.toString());
			if(1 != vpdci.getStatus()){
				this.updateVPDCRNoState(vpdcrvo);
			}
			if (null != ipvo.getIp() && !"".equals(ipvo.getIp())) {
				ip.setObject_id(vpdci.getId());
				O_NodeName node = this.getNode(ipvo.getNodeName());
				if (null != node) {
					ip.setHost_id(node.getId());
				}
				this.updateIP(ip);
				if(null == vpdcrvo.getUuid()
						|| "".equals(vpdcrvo.getUuid())){
					throw new OpenstackHandlerException("vm uuid is null");
				}
				O_PanelUser user = this.getPanelUser(vpdcrvo.getUuid());
				if(null != user){
					String ipString=IPConvert.getStringIP(ipvo.getIp());
					user.setVmIP((null == user.getVmIP() || "".equals(user.getVmIP()))?ipString:(user.getVmIP()+","+ipString));
					openstackDao.save(user, "hc_controlpanel_user.update");
				}
				logger.info("exit bindingIP !");
			}

		} catch (Exception e) {
			logger.error("" + e.getMessage(), e);
			throw new OpenstackHandlerException(e.getMessage());
		}

	}
	
	/**
	 * 释放ip(虚拟机)
	 */
	@Override
	public void unbindingIP(IPVO ipvo) {
		logger.info("enter unbindingIP:");
		O_IP ip = (O_IP) openstackDao.getUnique("hc_ip_detail", " where ip='"
				+ ipvo.getIp() + "'");
		this.updateIP(ipvo);
		O_VPDCInstance vpdci = (O_VPDCInstance) openstackDao
				.getUnique("hc_vpdc_instance", " where id="+ ip.getObject_id());
		O_VPDCReference vpdcr = (O_VPDCReference) openstackDao.get("hc_vpdc_reference"," where status=0 and id="+ vpdci.getVpdcRefrenceId());
		if(null != vpdcr){
			String ipString=IPConvert.getStringIP(ipvo.getIp());
			String ipHscloud=vpdcr.getVm_outerIP();
			logger.info("ipHscloud:"+ipHscloud);
			StringBuffer sb = new StringBuffer();
			String ips = this.deRepeatRemove(ipHscloud, ipString);
			vpdcr.setVm_outerIP(ips);
			logger.info("ipHscloud---:"+vpdcr.getVm_outerIP());
			openstackDao.save(vpdcr, "sync_hc_vpdc_reference.update");
			if(null == ipvo.getUuid()
					|| "".equals(ipvo.getUuid())){
				throw new OpenstackHandlerException("vm uuid is null");
			}
			O_PanelUser user = this.getPanelUser(ipvo.getUuid());
			if(null != user && sb.length()>0){
				user.setVmIP(sb.toString());
				openstackDao.save(user, "hc_controlpanel_user.update");
			}
			logger.info("exit unbindingIP:");
		}
	}
	
	/**
	 * 绑定ip(路由)
	 */
	@Override
	public void bindingRouterIP(O_Network network,IPVO ipvo,O_Router r) {
		try {
			logger.info("enter bindingIP !");
			O_IP ip = (O_IP) ipvo;
			this.updateRouter(r);
			if (null != ipvo.getIp() && !"".equals(ipvo.getIp())) {
				if(null != network.getObjectId() && !"".equals(network.getObjectId())){
					ip.setObject_id(network.getObjectId());
				}else if(null != network.getObjectUUID() && !"".equals(network.getObjectUUID())){
					StringBuffer sb = new StringBuffer();
					sb.append("  where router_uuid='").append(network.getObjectUUID()).append("'");
					O_Router router = (O_Router) openstackDao
							.getUnique("hc_vpdc_vrouter", sb.toString());
					ip.setObject_id(router.getId());
				}else{
					throw new OpenstackHandlerException("router uuid is null.");
				}
				if(null != ipvo.getNodeName() && !"".equals( ipvo.getNodeName())){
					O_NodeName node = this.getNode(ipvo.getNodeName());
					if (null != node) {
						ip.setHost_id(node.getId());
					}
				}
				this.updateIP(ip);
			}
			this.updateNetWork(network);
			logger.info("exit bindingRouterIP !");
		} catch (Exception e) {
			logger.error("" + e.getMessage(), e);
			throw new OpenstackHandlerException(e.getMessage());
		}
	}
	
	/**
	 * 更新  hc_vpdcnetwork_object 表
	 * @param network
	 */
	private void updateNetWork(O_Network network){
		try {
			StringBuffer sb = new StringBuffer();
			if(null == network.getObjectId() || "".equals(network.getObjectId())){
				if(null == network.getObjectUUID() || "".equals(network.getObjectId())){
					throw new OpenstackHandlerException(" network objectId or objectUUID is null");
				}
				sb.append(" where objectUUID='").append(network.getObjectUUID()).append("'");
			}else{
				sb.append(" where objectId=").append(network.getObjectId());
			}
			if(null == network.getObjectType() || "".equals(network.getObjectType())){
				throw new OpenstackHandlerException(" network objectType is null");
			}else{
				sb.append(" and ").append(" objectType=").append(network.getObjectType());
			}
			sb.append(" and networkId is null");
			O_Network n = (O_Network)openstackDao.getUnique("hc_vpdcnetwork_object", sb.toString());
			n.setNetworkId((null == network.getNetworkId() || "".equals(network.getNetworkId()))?n.getNetworkId():network.getNetworkId());
			n.setObjectUUID((null == network.getObjectUUID() || "".equals(network.getObjectUUID()))?n.getObjectUUID():network.getObjectUUID());
			openstackDao.save(n, "hc_vpdcnetwork_object.update");
		} catch (Exception e) {
			logger.error("" + e.getMessage(), e);
			throw new OpenstackHandlerException(e.getMessage());
		}
	}
	
	private void updateNetWorkUUID(O_Router r){
		StringBuffer ewsb = new StringBuffer();
		if(null == r.getId() && "".equals(r.getId())){
			throw new OpenstackHandlerException(" router objectId is null");
		}else{
			ewsb.append(" where objectId=").append(r.getId());
		}
		ewsb.append(" and ").append(" objectType=1");
		List<Object> nws = (List<Object>) openstackDao.gets("hc_vpdcnetwork_object", ewsb.toString());
		for (Object object : nws) {
			O_Network nw = (O_Network)object;
			nw.setObjectUUID((null == r.getRouter_uuid() || "".equals(r.getRouter_uuid()))?nw.getObjectUUID():r.getRouter_uuid());
			//nw.setObjectUUID(r.getRouter_uuid());
			openstackDao.save(nw, "hc_vpdcnetwork_object.uuid.update");
		}
	}

	/**
	 * 同步虚拟机状态
	 */
	@Override
	public void syncVMState(VPDCReferenceVO vpdcrvo, O_VPDCInstance vpdci) {

		try {
			StringBuffer sb = new StringBuffer();
			if(null == vpdci.getId() || "".equals(vpdci.getId())){
				if(null == vpdci.getVm_id() || "".equals(vpdci.getVm_id())){
				   throw new OpenstackHandlerException(" vm_id and instanceId are null");
				}
				sb.append(" where vm_id='").append(vpdci.getVm_id()).append("'");
			}else{
				sb.append(" where id=").append(vpdci.getId());
			}
			O_VPDCInstance i = (O_VPDCInstance)openstackDao.getUnique("hc_vpdc_instance", sb.toString());
			i.copy(vpdci);
		    openstackDao.save(i, "sync_hc_vpdc_instance.update");
		    if(1!=i.getStatus()){//在同步被删除的instance信息时，不需同步refrence表
				if(!"DELETED".equals(vpdcrvo.getVm_status()) && !"DELETING".equals(vpdcrvo.getVm_task_status())){
					i.copy(vpdci);
					openstackDao.save(i, "sync_hc_vpdc_instance.update");
				}
				if(null == i.getVpdcRefrenceId() || "".equals(i.getVpdcRefrenceId())){
					throw new OpenstackHandlerException(" reference id is null");
				}
				vpdcrvo.setId(i.getVpdcRefrenceId());
				this.updateVPDCRState(vpdcrvo);
		    }

		} catch (Exception e) {
			logger.error("" + e.getMessage(), e);
			throw new OpenstackHandlerException(e.getMessage());
		}

	}
	
	/**
	 * 同步路由器状态
	 * @param r
	 */
	@Override
	public void syncRouterState(O_Router r) {
		try {
			this.updateRouter(r);
		} catch (Exception e) {
			logger.error("" + e.getMessage(), e);
			throw new OpenstackHandlerException(e.getMessage());
		}
	}


	/**
	 * 重置操作系统
	 */
	@Override
	public void resetVmOs(VMVO vmVO) {
		StringBuffer sb = new StringBuffer();
		if(null == vmVO.getInstanceId() || "".equals(vmVO.getInstanceId())){
			if(null == vmVO.getUuid() || "".equals(vmVO.getUuid())){
				throw new OpenstackHandlerException(" instanceId or UUID is null .");
			}
			sb.append(" where vm_id='").append(vmVO.getUuid()).append("'");
		}else{
			sb.append(" where id=").append(vmVO.getInstanceId());
		}
		O_VPDCInstance vpdci = (O_VPDCInstance) openstackDao.getUnique("hc_vpdc_instance", sb.toString());
		if(null == vpdci){
			throw new OpenstackHandlerException("vm is not find!");
		}
		O_VPDCReference vpdcr = (O_VPDCReference) openstackDao.getUnique("hc_vpdc_reference"," where status=0 and id="+ vpdci.getVpdcRefrenceId());
		if(null == vpdcr){
			throw new OpenstackHandlerException("vm reference is not find or be deleted!");
		}
		vpdcr.setImageId(null != vmVO && null != vmVO.getImageId() && !"".equals(vmVO.getImageId())?vmVO.getImageId():vpdcr.getImageId());
		vpdcr.setOsId(null != vmVO && null != vmVO.getOsId() && !"".equals(vmVO.getOsId())?vmVO.getOsId():vpdcr.getOsId());
		vpdcr.setRadom_pwd(vmVO.getPassword());
		vpdcr.setRadom_user(vmVO.getUser());
		vpdcr.setUpdate_date(new Date());
		openstackDao.save(vpdcr, "sync_hc_vpdc_reference.update");
		O_User u = (O_User) openstackDao.getUnique("hc_user"," where id="+ vpdcr.getCreate_id());
		if(null == u){
			throw new OpenstackHandlerException("user is not find!");
		}
		O_Domain domain = this.getDomain(u.getDomain_id());
		O_MailTemplate template = this.getMailTemplate("VM_REBUILDOS_TEMPLATE",u.getDomain_id());
		Map<String, String> map = new HashMap<String, String>();
		map.put("hostname", vpdcr.getName());
		map.put("vmuuid", vmVO.getUuid());
		String ip =  (null != vpdcr.getVm_innerIP() && !"".equals(vpdcr.getVm_innerIP()) && null != vpdcr.getVm_outerIP() && !"".equals(vpdcr.getVm_outerIP()))?"("+vpdcr.getVm_innerIP()+","+vpdcr.getVm_outerIP()+")":((!"".equals(vpdcr.getVm_innerIP()) && null != vpdcr.getVm_innerIP()) || (!"".equals(vpdcr.getVm_outerIP()) && null != vpdcr.getVm_outerIP()))?"("+vpdcr.getVm_innerIP()+vpdcr.getVm_outerIP()+")":"";
		map.put("ip", ip);
		map.put("sysuser",vmVO.getUser());
		map.put("syspwd", vmVO.getPassword());
		map.put("name", domain.getName());
		map.put("today", Common4jDateFormat.getTimestamp(new Date(), "yyyy-MM-dd"));
		map.put("serviceHotline", domain.getService_hotline());
		map.put("bank", domain.getBank());
		map.put("cardNo", domain.getCard_no());
		map.put("address", domain.getAddress());
		map.put("telephone", domain.getTelephone());
		map.put("url", domain.getUrl());
		map.put("image",Constants.MAIL_LOGO_SRC.replaceAll("xxx",Long.toString(domain.getId())));
		map.put("webSiteUrl", domain.getPublishing_address());
		
		String body = template.getReplacedTemplate(map);
		logger.info("body:"+body);
		O_EmailQueue email = new O_EmailQueue();
		email.setBody(body);
		email.setSubject(template.getTitle()+ip);
		email.setReceive_users(u.getEmail());
		email.setCreate_time(new Date());
		email.setStatus(0);
		openstackDao.save(email, "hc_email_queue.new");
		O_Message me = new O_Message();
		me.setMessage("您的云主机（"+vpdcr.getName()+"）已经重置操作系统成功!");
		logger.info("message:"+me.getMessage());
		me.setCreate_time(new Date());
		me.setMessge_type(3);
		me.setStatus(1);
		me.setUser_id(vpdcr.getCreate_id());
		openstackDao.save(me, "hc_message.new");
	}

	
	@Override
	@SuppressWarnings({ "static-access" })
	public void saveResult(Object rv) {
		CommonThreadLocal tl = new CommonThreadLocal();
		MessageVO m = (MessageVO)tl.getMessage();
		this.updateHSEvent(rv);	
		try {
			if(null == m.getObj_type() || "" == m.getObj_type() || "0".equals(m.getObj_type())){
				if (rv instanceof ResourceResult) {
					O_VPDCReference vr = new O_VPDCReference();
					ResourceResult rr = (ResourceResult) rv;
					vr.setCreateflag(rr.getResult());
					ResourceResult r = this.getEventResource(rr.getId());
					if(1== r.getType() && 3 == r.getRes_type()){
						/**
						 * 1:添加资源
						 * 3:删除资源
						 */
						O_VPDCReference vpdcr = (O_VPDCReference) openstackDao.getUnique("hc_vpdc_reference"," where status=0 and id="+ r.getObj_id());
						vpdcr.setCreateflag(rr.getResult());
						openstackDao.save(vpdcr, "sync_hc_vpdc_reference.update");
					}
				}
			}

		 } catch (Exception e) {
			logger.error("" + e.getMessage(), e);
			throw new OpenstackHandlerException(e.getMessage());
		 }finally{
			if(null != m.getObj_type() && "" != m.getObj_type() && "1".equals(m.getObj_type())){
				this.checkRouter();
			}else{
				this.check();
			}
		 }
	}
	
	/**
	 * 当为创建成功时，对前面的状态做check操作。
	 */
	@SuppressWarnings({ "static-access" })
	private void check(){
		
		CommonThreadLocal tl = new CommonThreadLocal();
		MessageVO m = (MessageVO)tl.getMessage();
		VPDCReferenceVO rv = m.getReference();
		O_VPDCInstance vi = m.getInstance();
		IPVO ipv = m.getIP();
		List<O_VPDC_Extdisk> disks = m.getDisks();
		Object rrv = m.getResults();
		ResourceResult r = null;
		ResourceResult rr = null;
		String method = null;
		
		try {
			
			if(rrv instanceof ResourceResult){
					rr = (ResourceResult) rrv;
					r = this.getEventResource(rr.getId());
					if(null == r){
						logger.error("job is not find!");
						throw new OpenstackHandlerException(" Resource is not find. ");
					}
					method = this.pattern("JobMethod", r.getMessage());
					if(method.startsWith("createVM_")){
						O_VPDCReference vpdcr = (O_VPDCReference) openstackDao.getUnique("hc_vpdc_reference"," where status=0 and id="+ r.getObj_id());
						if(null == vpdcr){
							logger.error("job is not find!");
							throw new OpenstackHandlerException(" O_VPDCReference is not find. ");
						}
						if(1 == rr.getResult()){
							try {
								if(null != r && 1== r.getType() && 3 == r.getRes_type()){
									vpdcr.setCreateflag(m.getResult());
									openstackDao.save(vpdcr, "sync_hc_vpdc_reference.update");
								}
								StringBuffer sbi = new StringBuffer();
								if(null == rv.getInitVmId() || "".equals(rv.getInitVmId())){
									if(null == vi.getId()){
										throw new OpenstackHandlerException(" initVmId and instance id are null");
									}
									sbi.append(" where id=").append(vi.getId());
								}else{
									sbi.append(" where initVmId='").append(rv.getInitVmId()).append("'");
								}
								O_VPDCInstance i = (O_VPDCInstance) openstackDao.getUnique(
												"hc_vpdc_instance",sbi.toString());
								rv.setProcess_state("100");//rr.getResult()为成功,将进度设置为100;
								this.updateVPDCR(rv);
								i.copy(vi);
								openstackDao.save(i, "sync_hc_vpdc_instance.update");
								this.updateDiskVmId(rv.getId(), vi.getVm_id(),rv.getInitVmId());
								if (null != ipv.getIp() && !"".equals(ipv.getIp())) {
								   ipv.setObject_id(i.getId());
								   O_NodeName node = this.getNode(ipv.getNodeName());
								   if (null != node) {
									  ipv.setHost_id(node.getId());
								   }
								   this.updateIP(ipv);
								}
								this.bindingDisk(disks);
							} catch (Exception e) {
								logger.error("" + e.getMessage(), e);
								throw e;
							} finally{
								if ((LogBizType.ORDER_CREATE.getIndex() == r.getBiz_type()
										|| LogBizType.TRIAL_CREATE.getIndex() == r.getBiz_type() 
										|| LogBizType.PUBLISH_CREATE.getIndex()==r.getBiz_type())
										&& null != r && !"".equals(r.getOwner_email()) 
										&& null != r.getOwner_email()) {
									this.createVMCallBack(r);
								}
							}
						}else if((null == vpdcr.getTry_time() || vpdcr.getTry_time()<3 ) && !method.startsWith("createVM_Publish") && null != r.getMessage() && !"".equals(r.getMessage())){
								this.postMessage(r.getMessage());
								vpdcr.setTry_time((null==vpdcr.getTry_time()?0:vpdcr.getTry_time())+1);
								openstackDao.save(vpdcr, "sync_hc_vpdc_reference.update");
						}
				  }
			}else{
				if(null != m.getJob_ext() && "VM_OSRepair".equals(m.getJob_ext().getMethod())){
					rv.setVm_task_status("");
					this.updateVPDCR(rv);
				}
			}
			
		} catch (Exception e) {
			logger.error("" + e.getMessage(), e);
			throw new OpenstackHandlerException(e.getMessage());
		} 
		
	}

	/**
	 * 当为露由创建成功时，对前面的状态做check操作。
	 */
	@SuppressWarnings({ "static-access" })
	private void checkRouter(){
		
		CommonThreadLocal tl = new CommonThreadLocal();
		MessageVO m = (MessageVO)tl.getMessage();
		Object rrv = m.getResults();
		O_Router router = m.getRouter();
		ResourceResult r = null;
		ResourceResult rr = null;
		String method = null;
		O_Router uRouter = null;
		try {
			if(rrv instanceof ResourceResult){
				rr = (ResourceResult) rrv;
			    r = this.getEventResource(rr.getId());
				if(null == r){
					logger.error("job is not find!");
					throw new OpenstackHandlerException(" Resource is not find. ");
				}
				method = this.pattern("JobMethod", r.getMessage());
				if(method.startsWith("createRouter_")){
					if(1 == rr.getResult()){//rr.getResult()为成功,将进度设置为100;
					   router.setProcess_state("100");
					   this.updateNetWorkUUID(m.getRouter());
					   this.bindingRouterIP(m.getNetwork(),m.getIP(),router);
					}
				}
			}
		} catch (Exception e) {
			logger.error("" + e.getMessage(), e);
			throw new OpenstackHandlerException(e.getMessage());
		} finally{//驱动更新network
			if(1 == rr.getResult()){
				StringBuffer sb = new StringBuffer();
				if(null != router.getRouter_uuid() && !"".equals(router.getRouter_uuid())){
					sb.append(" where router_uuid='").append(router.getRouter_uuid()).append("'");
				}else{
					throw new OpenstackHandlerException(" router id or uuid is null");
				}
				uRouter = (O_Router) openstackDao.getUnique("hc_vpdc_vrouter", sb.toString());
				VMOpsVO vmOps = new VMOpsVO();
				vmOps.setUuid(router.getRouter_uuid());
				vmOps.setObj_name(uRouter.getName());
				List<Object> objects = (List<Object>) openstackDao.gets("networkvo", " where vn.label ='lan' and vo.objectId ="+router.getId());
				for (Object object : objects) {
					NetWorkVO network = (NetWorkVO)object;
					Map<String,String> lanMap = new HashMap<String, String>();
					lanMap.put("vm_id", String.valueOf(router.getId()));
					lanMap.put("routerUUID", router.router_uuid);
					lanMap.put("securytyVlanId", network.getSecurytyVlanId());
					lanMap.put("networkId", network.getNetworkId());
					this.postMessage(Constants.JOBSERVER_METHOD_RouterAddVif, lanMap, vmOps, "HcEventVmOps");
				}
			}
		}
	}
	
	
	/**
	 * 做开通业务逻辑
	 * @param resourceResult
	 */
	@SuppressWarnings({ "static-access" })
	private void createVMCallBack(ResourceResult resourceResult) {

		CommonThreadLocal tl = new CommonThreadLocal();
		MessageVO m = (MessageVO)tl.getMessage();
		StringBuffer sb = new StringBuffer();
		if(null != m.getInitVmId() && !"".equals(m.getInitVmId())){
			sb.append(" where initVmId='").append(m.getInitVmId()).append("'");
		}else if(null != m.getUuid() && !"".equals(m.getUuid())){
			sb.append(" where vm_id='").append(m.getUuid()).append("'");
		}else{
			throw new OpenstackHandlerException("vm_id or initVmId is null .");
		}
		O_VPDCInstance vpdci = (O_VPDCInstance) openstackDao
				.get("hc_vpdc_instance",sb.toString());
		if(null != vpdci && 0==vpdci.getStatus()){
			O_VPDCReference vpdcr = (O_VPDCReference) openstackDao.getUnique("hc_vpdc_reference"," where id="+ vpdci.getVpdcRefrenceId());
			O_PanelUser user = new O_PanelUser();
			O_IP oip = new O_IP();
			StringBuffer sbi = new StringBuffer();
			user.setVersion(0);
			String floatingIp = this.pattern("floatingIp",
					resourceResult.getMessage());
			if (!"".equals(floatingIp)) {
				user.setVmIP(floatingIp);
				sbi.append(";IP: ");
				oip.setIp(IPConvert.getIntegerIP(floatingIp));
				oip.setStatus(2);
			}
			StringBuffer sbv = new StringBuffer();
			user.setVmId(vpdci.getVm_id());
			sbv.append("您的云主机已开通！主机信息：[机器号:");
			sbv.append(vpdci.getVm_id());
			String	syspwd = vpdcr.getRadom_pwd();
			String	sysuser = vpdcr.getRadom_user();
				user.setVmIP((null == user.getVmIP() || "".equals(user.getVmIP()) ? vpdcr
						.getVm_innerIP() : vpdcr.getVm_innerIP() + ","
						+ user.getVmIP()));
			this.updateIP(oip);
			user.setCreate_date(new Date());
			user.setUpdate_date(new Date());
			user.setStatus(0);
			user.setCreate_Id(resourceResult.getOperator_id());
			String panelpw = PasswordUtil.getRandomNum(6);
			try {
				String enpw = MD5Util.getMD5Str(MD5Util.getMD5Str(panelpw));
				user.setUserPassword(enpw);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("" + e.getMessage(), e);
				throw new OpenstackHandlerException(" password generating failure。");
			}
			O_PanelUser  u = this.getPanelUser(user.getVmId());
	        if(null == u){
	        	openstackDao.save(user, "hc_controlpanel_user.new");
	        }else{
	        	user.setUserPassword(user.getUserPassword());
	        	user.setVmId(user.getVmId());
	        	user.setUpdate_date(new Date());
	        	user.setVersion(u.getVersion()+1);
	        	openstackDao.save(user, "hc_controlpanel_user.update");
	        }
			sbi.append(user.getVmIP());
			sbi.append(" ]");
			
			O_Domain domain = this.getDomain(resourceResult.getDomain_id());
			O_MailTemplate template = this.getMailTemplate(MailTemplateType.OPEN_VM_TEMPLATE.getType(),
					resourceResult.getDomain_id());
			O_EmailQueue email = new O_EmailQueue();
			Map<String, String> map = new HashMap<String, String>();
			map.put("today",
					Common4jDateFormat.getTimestamp(new Date(), "yyyy-MM-dd"));
			map.put("abbreviation", domain.getAbbreviation());
			map.put("name", domain.getName());
			map.put("telephone", domain.getTelephone());
			map.put("address", domain.getAddress());
			map.put("url", domain.getUrl());
			map.put("bank", domain.getBank());
			map.put("cardNo", domain.getCard_no());
			map.put("serviceHotline", domain.getService_hotline());
			map.put("webSiteUrl", "http://117.122.193.11/");
			map.put("cpUrl", domain.getPublishing_address_cp());
			map.put("image","resource/uploads/");
			if (!"".equals(user.getVmIP())) {
				map.put("ip", user.getVmIP());
			}
			map.put("hostname", resourceResult.getObj_name());
			map.put("sysuser", sysuser);
			map.put("syspwd", syspwd);
			map.put("password", panelpw);
			map.put("vmuuid", user.getVmId());
			
			O_OS os = (O_OS)openstackDao.getUnique("hc_os"," where os_id="+ vpdcr.getOsId());
			map.put("port", os.getPort());
			String body = template.getReplacedTemplate(map);
			logger.info("body:"+body);
			email.setBody(body);
			email.setSubject(template.getTitle()+"("+user.getVmIP()+")");
			email.setReceive_users(resourceResult.getOwner_email());
			email.setCreate_time(new Date());
			email.setStatus(0);
			email.setDomain_id(domain.getId());
			openstackDao.save(email, "hc_email_queue.new");

			O_Message me = new O_Message();

			me.setMessage(sbv.toString() + sbi.toString());
			logger.info("message:"+me.getMessage());
			me.setCreate_time(new Date());
			me.setMessge_type(3);
			me.setStatus(1);
			me.setUser_id(null == resourceResult.getExt_column_str()?resourceResult.getOperator_id():resourceResult.getExt_column_str());
			openstackDao.save(me, "hc_message.new");
		}
		
	}

	/**
	 * 更新全部（vm_status,vm_task_status,vm_innerIP,vm_outerIP,radom_user,radom_pwd,createflag,imageId,osId,id）
	 * @param vpdcrvo
	 */
	private void updateVPDCR(VPDCReferenceVO vpdcrvo) {
		if("DELETED".equals(vpdcrvo.getVm_status())){
			return ;
		}
		if (null == vpdcrvo.getId() || "".equals(vpdcrvo.getId())) {
			if (null == vpdcrvo.getUuid() || "".equals(vpdcrvo.getUuid())) {
				throw new OpenstackHandlerException(" reference id is null");
			}
			O_VPDCInstance vpdci = (O_VPDCInstance) openstackDao.getUnique(
					"hc_vpdc_instance", " where vm_id='" + vpdcrvo.getUuid()
							+ "'");
			vpdcrvo.setId(vpdci.getVpdcRefrenceId());
		}
		O_VPDCReference vpdcr = (O_VPDCReference) vpdcrvo;
		O_VPDCReference vpdcReference = (O_VPDCReference) openstackDao
				.getUnique("hc_vpdc_reference", " where id=" + vpdcr.getId());
		vpdcReference.copy(vpdcr);
		openstackDao.save(vpdcReference, "sync_hc_vpdc_reference.update");
	}

	/**
	 * 只更新虚拟机状态 （vm_status，vm_task_status）；
	 * @param vpdcrvo
	 */
	private void updateVPDCRState(VPDCReferenceVO vpdcrvo){
		if("DELETED".equals(vpdcrvo.getVm_status())){
			return ;
		}
		if (null == vpdcrvo.getId() || "".equals(vpdcrvo.getId())) {
			if (null == vpdcrvo.getUuid() || "".equals(vpdcrvo.getUuid())) {
				throw new OpenstackHandlerException(" reference id is null");
			}
			O_VPDCInstance vpdci = (O_VPDCInstance) openstackDao.getUnique(
					"hc_vpdc_instance", " where vm_id='" + vpdcrvo.getUuid()
							+ "'");
			vpdcrvo.setId(vpdci.getVpdcRefrenceId());
		}
		O_VPDCReference vpdcr = (O_VPDCReference) vpdcrvo;
		O_VPDCReference vpdcReference = (O_VPDCReference) openstackDao
				.getUnique("hc_vpdc_reference", " where id=" + vpdcr.getId());
		vpdcReference.setVm_status((null == vpdcrvo.getVm_status() || ""
				.equals(vpdcrvo.getVm_status())) ? vpdcReference.getVm_status()
				: vpdcrvo.getVm_status());
		vpdcReference.setProcess_state(null == vpdcrvo.getProcess_state()?vpdcReference.getProcess_state():vpdcrvo.getProcess_state());
		vpdcReference.setVm_task_status(null == vpdcrvo.getVm_task_status()?vpdcReference.getVm_task_status():vpdcrvo.getVm_task_status());
		vpdcReference.setEvent_time(vpdcr.getEvent_time());
		openstackDao.save(vpdcReference, "sync_hc_vpdc_reference.state.update");
		
	}
	
	/**
	 * 更新除虚拟机状态 （vm_status，vm_task_status）；
	 * @param vpdcrvo
	 */
	private void updateVPDCRNoState(VPDCReferenceVO vpdcrvo){
		if("DELETED".equals(vpdcrvo.getVm_status())){
			return ;
		}
		if (null == vpdcrvo.getId() || "".equals(vpdcrvo.getId())) {
			if (null == vpdcrvo.getUuid() || "".equals(vpdcrvo.getUuid())) {
				throw new OpenstackHandlerException(" vmid and reference id is null");
			}
			O_VPDCInstance vpdci = (O_VPDCInstance) openstackDao.getUnique(
					"hc_vpdc_instance", " where vm_id='" + vpdcrvo.getUuid()
							+ "'");
			vpdcrvo.setId(vpdci.getVpdcRefrenceId());
		}
		O_VPDCReference vpdcr = (O_VPDCReference) vpdcrvo;
		O_VPDCReference vpdcReference = (O_VPDCReference) openstackDao
				.getUnique("hc_vpdc_reference", " where id=" + vpdcr.getId());
		vpdcReference.setVm_innerIP((null == vpdcr.getVm_innerIP() || ""
				.equals(vpdcr.getVm_innerIP())) ? vpdcReference.getVm_innerIP()
				: vpdcr.getVm_innerIP());
		logger.info("-------"+vpdcReference.getVm_outerIP());
		if("".equals(vpdcReference.getVm_outerIP()) || null == vpdcReference.getVm_outerIP()){
			vpdcReference.setVm_outerIP(null==vpdcr.getVm_outerIP()?"":vpdcr.getVm_outerIP());
		}else if(vpdcr.getVm_outerIP()!=null && !"".equals(vpdcr.getVm_outerIP())){
			String ips = this.deRepeatAppend(vpdcReference.getVm_outerIP(), vpdcr.getVm_outerIP());
			//可以添加多个ip(保留原ip,增加一个ip，用,分隔)
			//vpdcReference.setVm_outerIP((null==vpdcr.getVm_outerIP() || "".equals(vpdcr.getVm_outerIP()))?(vpdcReference.getVm_outerIP()):(vpdcReference.getVm_outerIP()+","+vpdcr.getVm_outerIP()));
			vpdcReference.setVm_outerIP(ips);
		}else{
			vpdcReference.setVm_outerIP(vpdcReference.getVm_outerIP());
		}
		logger.info("+++++++out ip"+vpdcReference.getVm_outerIP());
		vpdcReference.setRadom_user((null == vpdcr.getRadom_user() || ""
				.equals(vpdcr.getRadom_user())) ? vpdcReference.getRadom_user()
				: vpdcr.getRadom_user());
		vpdcReference.setRadom_pwd((null == vpdcr.getRadom_pwd() || ""
				.equals(vpdcr.getRadom_pwd())) ? vpdcReference.getRadom_pwd()
				: vpdcr.getRadom_pwd());
		vpdcReference.setCreateflag((null == vpdcr.getCreateflag() || ""
				.equals(vpdcr.getCreateflag())) ? vpdcReference.getCreateflag()
				: vpdcr.getCreateflag());
		vpdcReference.setImageId((null == vpdcr.getImageId() || ""
				.equals(vpdcr.getImageId())) ? vpdcReference.getImageId()
				: vpdcr.getImageId());
		vpdcReference.setOsId((null == vpdcr.getOsId() || ""
				.equals(vpdcr.getOsId())) ? vpdcReference.getOsId()
				: vpdcr.getOsId());
		openstackDao.save(vpdcReference, "sync_hc_vpdc_reference.nostate.update");
		
	}
	
	/**
	 * 更新路由状态（router_status，router_task_status,nodeName）字段
	 * @param r
	 */
	@SuppressWarnings("unused")
	private void updateRouterState(O_Router r){
		StringBuffer sb = new StringBuffer();
		if(null != r.getId() && !"".equals(r.getId())){
			sb.append(" where id=").append(r.getId());
		}else if(null != r.getRouter_uuid() && !"".equals(r.getRouter_uuid())){
			sb.append(" where router_uuid='").append(r.getRouter_uuid()).append("'");
		}else{
			throw new OpenstackHandlerException(" router id or uuid is null");
		}
		O_Router router = (O_Router) openstackDao
				.getUnique("hc_vpdc_vrouter", sb.toString());
		router.setRouter_status((null == r.getRouter_status() || ""
				.equals(r.getRouter_status())) ? router.getRouter_status()
				: r.getRouter_status());
		router.setProcess_state(null == r.getProcess_state()?router.getProcess_state():r.getProcess_state());
		router.setRouter_task_status(null == r.getRouter_task_status()?router.getRouter_task_status():r.getRouter_task_status());
		router.setNodeName((null == r.getNodeName() || "".equals(r.getNodeName()))?router.getNodeName():r.getNodeName());
		router.setEvent_time(r.getEvent_time());
		openstackDao.save(router, "sync_hc_vpdc_vrouter.state.update");
	}

	/**
	 * 更新非路由状态
	 * @param r
	 */
	private void updateRouterNoState(O_Router r){
		StringBuffer sb = new StringBuffer();
		if(null != r.getId() && !"".equals(r.getId())){
			sb.append(" where id=").append(r.getId());
		}else if(null != r.getRouter_uuid() && !"".equals(r.getRouter_uuid())){
			sb.append(" where router_uuid='").append(r.getRouter_uuid()).append("'");
		}else{
			throw new OpenstackHandlerException(" router id or uuid is null");
		}
		O_Router router = (O_Router) openstackDao
				.getUnique("hc_vpdc_vrouter", sb.toString());
		router.setFixIP((null == r.getFixIP() || "".equals(r.getFixIP()))?router.getFixIP():r.getFixIP());
		if("".equals(router.getFloatingIP()) || null == r.getFloatingIP()){
			router.setFloatingIP(null==r.getFloatingIP()?"":r.getFloatingIP());
		}else if(router.getFloatingIP()!=null && !"".equals(r.getFloatingIP())){
			String ips = this.deRepeatAppend(router.getFloatingIP(), r.getFloatingIP());
			router.setFloatingIP(ips);
		}else{
			router.setFloatingIP(router.getFloatingIP());
		}  
		router.setEvent_time(r.getEvent_time());
		router.setRouter_uuid((null == r.getRouter_uuid() || "".equals(r.getRouter_uuid()))?router.getRouter_uuid():r.getRouter_uuid());
		router.setNodeName((null == r.getNodeName() || "".equals(r.getNodeName()))?router.getNodeName():r.getNodeName());
		openstackDao.save(router, "sync_hc_vpdc_vrouter.nostate.update");
		
		
	}
	
	/**
	 * 更新路由信息
	 * @param r
	 */
	private O_Router updateRouter(O_Router r){
		StringBuffer sb = new StringBuffer();
		if(null != r.getId() && !"".equals(r.getId())){
			sb.append(" where id=").append(r.getId());
		}else if(null != r.getRouter_uuid() && !"".equals(r.getRouter_uuid())){
			sb.append(" where router_uuid='").append(r.getRouter_uuid()).append("'");
		}else{
			throw new OpenstackHandlerException(" router id or uuid is null");
		}
		O_Router router = (O_Router) openstackDao
				.getUnique("hc_vpdc_vrouter", sb.toString());
		router.copy(r);
		openstackDao.save(router, "sync_hc_vpdc_vrouter.update");
		return router;
		
	}

	/**
	 * 更新磁盘信息
	 * @param referenceId   hc_vpdc_reference 中id 
	 * @param tempId  
	 * @param uuid  虚拟机id
	 */
	private void updateDiskVmId(Long referenceId,String uuid,String initVmId) {
		if(null == initVmId || "".equals(initVmId)){
			throw new OpenstackHandlerException(" initVmId is null");
		}
		List<Object> ds = (List<Object>) openstackDao.gets(
				"hc_vpdcreference_extdisk", " where initVmId='"+initVmId+"'");
		for (Object object : ds) {
			O_VPDC_Extdisk d = (O_VPDC_Extdisk) object;
			d.setVmId(uuid);
			openstackDao.save(d, "sync_hc_vpdcreference_extdisk.update");
		}

	}

	/**
	 * 通过节点名获得节点信息
	 * @param nodeName
	 * @return
	 */
	private O_NodeName getNode(String nodeName) {
		O_NodeName node = (O_NodeName) openstackDao.getUnique("hc_node",
				" where name='" + nodeName + "'");
		return node;
	}

	private void updateIP(O_IP ip) {
		O_IP i = (O_IP) openstackDao.getUnique("hc_ip_detail", " where ip='"
				+ ip.getIp() + "'");
		i.copy(ip);
		i.setModify_time(new Date());
		openstackDao.save(i, "sync_hc_ip_detail.update");
	}

	/**
	 * 根据 object 类型 判断更新  hc_event_resource，hc_event_vmops 表。
	 * @param rv
	 */
	private void updateHSEvent(Object rv) {
		if (rv instanceof OPSResult) {
			OPSResult opsv = (OPSResult) rv;
			OPSResult opsr = (OPSResult) openstackDao.getUnique(
					"hc_event_vmops", " where id=" + opsv.getId());
			opsr.setUpdate_time(new Date());
			opsr.setResult(opsv.getResult());
			opsr.setError_info(opsv.getError_info());
			openstackDao.save(opsr, "sync_hc_event_vmops.update");
		} else {
			ResourceResult rev = (ResourceResult) rv;
			ResourceResult resourceResult = (ResourceResult) openstackDao
					.getUnique("hc_event_resource", " where id=" + rev.getId());
			resourceResult.setUpdate_time(new Date());
			if(4 != resourceResult.getResult()){
				resourceResult.setResult(rev.getResult());
				resourceResult.setError_info(rev.getError_info());
			}
			openstackDao.save(resourceResult, "sync_hc_event_resource.update");
		}

	}

	private ResourceResult getEventResource(Long id) {
		ResourceResult result = (ResourceResult) openstackDao.getUnique(
				"hc_event_resource", " where id=" + id);
		return result;
	}

	private O_Domain getDomain(Long id) {
		O_Domain domain = (O_Domain) openstackDao.getUnique("hc_domain",
				" where id=" + id);
		return domain;
	}

	/**
	 * 获得模板
	 * @param key       模板健值
	 * @param domainId  域名
	 * @return
	 */
	private O_MailTemplate getMailTemplate(String key, Long domainId) {
		O_MailTemplate template = (O_MailTemplate) openstackDao.getUnique(
				"hc_mail_template", " where keyword='" + key
						+ "' and domain_id=" + domainId);
		return template;
	}

	/**
	 * 获得key对应的值
	 * 
	 * @param key
	 *            搜索健值
	 * @param message
	 *            被搜索json串
	 * @return
	 */
	private String pattern(String key, String message) {

		if (null == message || "".equals(message)) {
			return "";
		} else {
			String reg = "\"" + key + "\":\"[^\\[\\],{}\\\\]+\"";
			Pattern pattern = Pattern.compile(reg);
			Matcher matcher = pattern.matcher(message);
			if (matcher.find()) {
				return matcher.group(0).split(":")[1].replaceAll("\"", "");
			}
			return "";
		}

	}
	
	/**
	 * 重发消息
	 * @param message
	 */
	private void postMessage(String message){
		
		try {
			logger.info("post message:  "+message);
			new RabbitMQUtil().sendMessage(message);
			logger.info(" exit postMessage method.");
		} catch (Exception e) {
			logger.error("message send error:"+e.getMessage());
			throw new OpenstackHandlerException(" message send error!");
		};
		
	}
	
	/**
	 * 重发消息
	 * @param message
	 */
	private void postMessage(String methodName,Object param,Object log,String logkey){
		try {
			logger.info("post message:  ");
			new RabbitMQUtil().sendMessage(methodName, param, log, logkey);
			logger.info(" exit postMessage method.");
		} catch (Exception e) {
			logger.error("message send error:"+e.getMessage());
			e.printStackTrace();
			throw new OpenstackHandlerException(" message send error!");
		}
	}

	/**
	 * 通过ip搜索控制面板用户
	 * @param vmId
	 * @return
	 */
	private O_PanelUser getPanelUser(String vmId){
		return (O_PanelUser)openstackDao.get("hc_controlpanel_user", " where status=0 and vmId='"+vmId+"'");
	}
	
	/**
	 *去掉ip,并去重复
	 * @param oldIPs  老ip
	 * @param appendIP 新ip
	 * @return
	 */
	private String deRepeatRemove(String oldIPs,String appendIP){
		StringBuffer sb = new StringBuffer();
		String[] ips=oldIPs.split(",");
		Set<String> s = new LinkedHashSet<String>();
		for (int i = 0; i < ips.length; i++) {
			if(null == ips[i] || "".equals(ips[i]))
				continue;
			s.add(ips[i]);
		}
		s.remove(appendIP);
		for (String  ip: s) {
			if(0 == sb.length()){
				sb.append(ip);
			}else{
				sb.append(",").append(ip);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 添加ip,并去重复
	 * @param oldIPs
	 * @param appendIP
	 * @return
	 */
	private String deRepeatAppend(String oldIPs,String appendIP){
		StringBuffer sb = new StringBuffer();
		String[] ips=oldIPs.split(",");
		Set<String> s = new LinkedHashSet<String>();
		for (int i = 0; i < ips.length; i++) {
			if(null == ips[i] || "".equals(ips[i]))
				continue;
			s.add(ips[i]);
		}
		s.add(appendIP);
		for (String  ip: s) {
			if(0 == sb.length()){
				sb.append(ip);
			}else{
				sb.append(",").append(ip);
			}
		}
		return sb.toString();
	}
	
	@Override
	public void doNothing() {

	}

	@Override
	public void error() {

	}

}
