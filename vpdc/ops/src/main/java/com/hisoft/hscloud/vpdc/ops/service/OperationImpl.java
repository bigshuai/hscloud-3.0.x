package com.hisoft.hscloud.vpdc.ops.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openstack.api.compute.ServerResource;
import org.openstack.api.compute.VpdcNetworkResource;
import org.openstack.model.compute.Flavor;
import org.openstack.model.compute.Image;
import org.openstack.model.compute.Server;
import org.openstack.model.compute.Volume;
import org.openstack.model.compute.nova.NovaFlavorForCreate;
import org.openstack.model.compute.nova.NovaServerForCreate;
import org.openstack.model.compute.nova.NovaServerForCreate.NetworkLan;
import org.openstack.model.compute.nova.server.actions.Console;
import org.openstack.model.compute.nova.server.actions.SecurityIngressRules;
import org.openstack.model.compute.nova.server.actions.SecurityRules;
import org.openstack.model.compute.nova.server.actions.SetSecurityBandwidthRules;
import org.openstack.model.compute.nova.server.actions.SetSecurityBandwidthRules.BandwidthRule;
import org.openstack.model.compute.nova.server.actions.SetSecurityConnlimitRules;
import org.openstack.model.compute.nova.server.actions.SetSecurityConnlimitRules.ConnlimitRule;
import org.openstack.model.compute.nova.volume.NovaVolumeForCreate;
import org.openstack.model.hscloud.impl.Network;
import org.openstack.model.hscloud.impl.SecurityLan;
import org.openstack.model.hscloud.impl.VifAdd;
import org.openstack.model.hscloud.impl.VifRemove;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.dao.AccountDao;
import com.hisoft.hscloud.bss.billing.entity.Account;
import com.hisoft.hscloud.common.entity.HcEventResource;
import com.hisoft.hscloud.common.entity.HcEventVmOps;
import com.hisoft.hscloud.common.entity.LogBizType;
import com.hisoft.hscloud.common.entity.LogOPSType;
import com.hisoft.hscloud.common.entity.LogOperatorType;
import com.hisoft.hscloud.common.entity.LogResourceOperationType;
import com.hisoft.hscloud.common.entity.LogResourceType;
import com.hisoft.hscloud.common.service.SiteConfigService;
import com.hisoft.hscloud.common.util.CharUtil;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.IPConvert;
import com.hisoft.hscloud.common.util.OpenstackUtil;
import com.hisoft.hscloud.common.util.PasswordUtil;
import com.hisoft.hscloud.common.util.PropertiesUtils;
import com.hisoft.hscloud.common.util.RabbitMQUtil;
import com.hisoft.hscloud.common.util.RedisUtil;
import com.hisoft.hscloud.common.util.SocketUtil;
import com.hisoft.hscloud.controlpanel.dao.ControlPanelDao;
import com.hisoft.hscloud.controlpanel.entity.ControlPanelUser;
import com.hisoft.hscloud.controlpanel.service.ControlPanelService;
import com.hisoft.hscloud.crm.usermanager.constant.ResourceType;
import com.hisoft.hscloud.crm.usermanager.dao.AdminDao;
import com.hisoft.hscloud.crm.usermanager.entity.AbstractUser;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.entity.Company;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.crm.usermanager.entity.Resource;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.service.CompanyService;
import com.hisoft.hscloud.crm.usermanager.service.DomainService;
import com.hisoft.hscloud.crm.usermanager.service.PermissionService;
import com.hisoft.hscloud.crm.usermanager.service.ResourceService;
import com.hisoft.hscloud.crm.usermanager.service.RoleService;
import com.hisoft.hscloud.crm.usermanager.service.UserService;
import com.hisoft.hscloud.mail.constant.MailTemplateType;
import com.hisoft.hscloud.mail.service.MailService;
import com.hisoft.hscloud.message.entity.Message;
import com.hisoft.hscloud.message.service.MessageService;
import com.hisoft.hscloud.vpdc.ops.dao.VpdcDao;
import com.hisoft.hscloud.vpdc.ops.entity.VmExpireEmailLog;
import com.hisoft.hscloud.vpdc.ops.entity.VmExtranetSecurity;
import com.hisoft.hscloud.vpdc.ops.entity.VmIntranetSecurity;
import com.hisoft.hscloud.vpdc.ops.entity.VmIntranetSecurity_Instance;
import com.hisoft.hscloud.vpdc.ops.entity.VmSnapShot;
import com.hisoft.hscloud.vpdc.ops.entity.VmSnapShotPlan;
import com.hisoft.hscloud.vpdc.ops.entity.VmSnapShotTask;
import com.hisoft.hscloud.vpdc.ops.entity.VmVNCPool;
import com.hisoft.hscloud.vpdc.ops.entity.Vpdc;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcFlavor;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcInstance;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcLan;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcNetwork;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcNetwork_Object;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference_OrderItem;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference_Period;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference_Period_Log;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference_extdisk;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcRenewal;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcRouter;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcRouter_OrderItem;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcRouter_Period;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcRouter_Period_Log;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcVrouterTemplate;
import com.hisoft.hscloud.vpdc.ops.json.bean.CreateVmBean;
import com.hisoft.hscloud.vpdc.ops.json.bean.CreateVpdcBean;
import com.hisoft.hscloud.vpdc.ops.json.bean.CreateVpdcBean.VlanNetwork;
import com.hisoft.hscloud.vpdc.ops.json.bean.ResourceLimit;
import com.hisoft.hscloud.vpdc.ops.json.bean.VmExtDiskBean;
import com.hisoft.hscloud.vpdc.ops.json.bean.VmOsBean;
import com.hisoft.hscloud.vpdc.ops.util.VMStatusBussEnum;
import com.hisoft.hscloud.vpdc.ops.util.VMTypeEnum;
import com.hisoft.hscloud.vpdc.ops.vo.ExpireRemindVO;
import com.hisoft.hscloud.vpdc.ops.vo.FlavorVO;
import com.hisoft.hscloud.vpdc.ops.vo.ImageVO;
import com.hisoft.hscloud.vpdc.ops.vo.InstanceDetailVo;
import com.hisoft.hscloud.vpdc.ops.vo.InstanceDetail_ServiceCatalogVo;
import com.hisoft.hscloud.vpdc.ops.vo.InstanceVO;
import com.hisoft.hscloud.vpdc.ops.vo.RenewOrderVO;
import com.hisoft.hscloud.vpdc.ops.vo.SecurityPolicyVO;
import com.hisoft.hscloud.vpdc.ops.vo.UnaddedIntranetInstanceVO;
import com.hisoft.hscloud.vpdc.ops.vo.VmExtranetSecurityVO;
import com.hisoft.hscloud.vpdc.ops.vo.VmInfoVO;
import com.hisoft.hscloud.vpdc.ops.vo.VmIntranetSecurityVO;
import com.hisoft.hscloud.vpdc.ops.vo.VmSnapShotPlanVO;
import com.hisoft.hscloud.vpdc.ops.vo.VmSnapShotVO;
import com.hisoft.hscloud.vpdc.ops.vo.VolumeVO;
import com.hisoft.hscloud.vpdc.ops.vo.VpdcInstanceVO;
import com.hisoft.hscloud.vpdc.ops.vo.VpdcReferenceQuotaInfo;
import com.hisoft.hscloud.vpdc.ops.vo.VpdcReferenceVO;
import com.hisoft.hscloud.vpdc.ops.vo.VpdcRouterVo;
import com.hisoft.hscloud.vpdc.ops.vo.VpdcVo;

@Service
public class OperationImpl implements Operation {

	private Logger log = Logger.getLogger(this.getClass());
	private OpenstackUtil openstackUtil = new OpenstackUtil();
	private RabbitMQUtil rabbitMqUtil = new RabbitMQUtil();
	private PropertiesUtils propertiesUtils = new PropertiesUtils();
	@Autowired
	private VpdcDao vpdcDao;
	@Autowired
	private AccountDao accountDao;
	@Autowired
	private MessageService messageService;
	@Autowired
	private UserService userService;
	@Autowired
	private AdminDao adminDao;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private MailService mailService;
	@Autowired
	private SiteConfigService siteConfigService;
	@Autowired
	private ControlPanelService controlPanelService;
	@Autowired
	private ControlPanelDao controlPanelDao;
	@Autowired
	private DomainService domainService;
	@Autowired
	private VpdcRenewalService vpdcRenewalService;
	@Autowired
	private RoleService roleService;
	private final String resourceType = "com.hisoft.hscloud.vpdc.ops.entity.VpdcReference";

	/*
	 * private boolean bl = false; private boolean waitForState(final String
	 * state,final String vmId) { try { final ScheduledThreadPoolExecutor timer
	 * = new ScheduledThreadPoolExecutor( 1); timer.scheduleAtFixedRate(new
	 * Runnable() {
	 * 
	 * @Override public void run() { Server s_ =
	 * openstackUtil.getCompute().servers().server(vmId).get(); if
	 * (state.equalsIgnoreCase(s_.getStatus()) ||
	 * "ERROR".equalsIgnoreCase(s_.getStatus())) { bl = true; timer.shutdown();
	 * } else { System.out.print("."); } } }, 10, 2, TimeUnit.SECONDS);
	 * timer.awaitTermination(3600, TimeUnit.SECONDS); timer.shutdown(); } catch
	 * (Exception e) { } return bl; }
	 */

	private boolean volumeStateBl = false;

	private boolean waitForVolumeState(final String state,
			final Integer volumeId) {
		try {
			final ScheduledThreadPoolExecutor timer = new ScheduledThreadPoolExecutor(
					1);
			timer.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					Volume v2 = openstackUtil.getCompute().volumes()
							.volume(volumeId).get();
					if (state.equalsIgnoreCase(v2.getStatus())) {
						volumeStateBl = true;
						timer.shutdown();
					} else {
						System.out.print(".");
					}
				}
			}, 10, 2, TimeUnit.SECONDS);
			timer.awaitTermination(300, TimeUnit.SECONDS);
			timer.shutdown();
		} catch (Exception e) {

		}
		return volumeStateBl;
	}

	@Transactional(readOnly = false)
	public boolean openvm(String uuid, Object o, String otype) {
		try {
			VpdcReference vr = vpdcDao.findVpdcReferenceByVmId(uuid);
			if (Constants.VM_ISENABLE_TURE != vr.getIsEnable()) {
				throw new HsCloudException(Constants.VM_FREEZE_NODO,
						"openvm Exception", log, null);
			}
			vr.setVm_task_status("RESUMING");
			vpdcDao.updateVpdcReference(vr);
			HcEventVmOps hevo = loadHcOps(o, otype);
			hevo.setObj_name(vr.getName());
			hevo.setOps(LogOPSType.START.getIndex());
			hevo.setReference_id(vr.getId());
			hevo.setUuid(uuid);
			Map<String, String> vmMap = new HashMap<String, String>();
			vmMap.put("uuid", uuid);
			rabbitMqUtil.sendMessage(Constants.JOBSERVER_METHOD_VMOpen, vmMap,
					hevo, "HcEventVmOps");

			/*
			 * ServerResource serverResource =
			 * openstackUtil.getCompute().servers().server(uuid);
			 * serverResource.action().post(new ResumeAction(), String.class);
			 */
			// waitForState("ACTIVE");
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_START_ERROR,
					"openvm Exception", log, e);
		}
		return true;
	}

	private HcEventVmOps loadHcOps(Object o, String otype) {
		HcEventVmOps hevo = new HcEventVmOps();
		hevo.setEvent_time(new Date());
		if (LogOperatorType.USER.getName().equals(otype)) {
			User u = (User) o;
			hevo.setOperator(u.getEmail());
			hevo.setOperator_id(u.getId());
			hevo.setOperator_type(LogOperatorType.USER.getIndex());
		} else if (LogOperatorType.ADMIN.getName().equals(otype)) {
			Admin a = (Admin) o;
			hevo.setOperator(a.getEmail());
			hevo.setOperator_id(a.getId());
			hevo.setOperator_type(LogOperatorType.ADMIN.getIndex());
		} else if (LogOperatorType.CP.getName().equals(otype)) {
			ControlPanelUser c = (ControlPanelUser) o;
			hevo.setOperator(c.getVmId());
			hevo.setOperator_id(c.getId());
			hevo.setOperator_type(LogOperatorType.CP.getIndex());
		} else if (LogOperatorType.PROCESS.getName().equals(otype)) {
			hevo.setOperator_type(LogOperatorType.PROCESS.getIndex());
			if (o != null) {
				hevo.setRemark("云主机退款申请");
			}
		} else {
			hevo.setOperator_type(LogOperatorType.UNKNOWN.getIndex());
		}
		return hevo;
	}

	@Transactional(readOnly = false)
	public boolean closeVm(String uuid, Object o, String otype)
			throws HsCloudException {
		log.info("the uuid is :" + uuid);
		try {
			VpdcReference vr = vpdcDao.findVpdcReferenceByVmId(uuid);
			if (vr == null) {
				return false;
			}
			// Server s =
			// openstackUtil.getCompute().servers().server(uuid).get();
			if (!"ACTIVE".equalsIgnoreCase(vr.getVm_status())
					|| !StringUtils.isEmpty(vr.getVm_task_status())) {
				log.debug("Close VM failed cause of the status is't ACTIVE");
				return false;
			}
			vr.setVm_task_status("SUSPENDING");
			vr.setProcessState(null);
			vpdcDao.updateVpdcReference(vr);
			HcEventVmOps hevo = this.loadHcOps(o, otype);
			hevo.setObj_name(vr.getName());
			hevo.setReference_id(vr.getId());
			hevo.setUuid(uuid);
			hevo.setOps(LogOPSType.SHUTDOWN.getIndex());
			Map<String, String> vmMap = new HashMap<String, String>();
			vmMap.put("uuid", uuid);
			rabbitMqUtil.sendMessage(Constants.JOBSERVER_METHOD_VMClose, vmMap,
					hevo, "HcEventVmOps");
			/*
			 * ServerResource serverResource =
			 * openstackUtil.getCompute().servers().server(uuid);
			 * serverResource.action().post(new SuspendAction(), String.class);
			 */
			// waitForState("SUSPENDED");
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_CLOSE_ERROR,
					"closeVm Exception", log, e);
		}
		return true;
	}

	@Transactional(readOnly = false)
	public String rebootVm(String uuid, Object o, String otype)
			throws HsCloudException {
		String result = null;
		try {
			VpdcReference vr = vpdcDao.findVpdcReferenceByVmId(uuid);
			if (Constants.VM_ISENABLE_TURE != vr.getIsEnable()) {
				throw new HsCloudException(Constants.VM_FREEZE_NODO,
						"openvm Exception", log, null);
			}
			/*
			 * ServerResource serverResource =
			 * openstackUtil.getCompute().servers().server(uuid);
			 * //serverResource.reboot("SOFT"); serverResource.reboot("HARD");
			 */
			vr.setVm_task_status("REBOOTING_HARD");
			vpdcDao.updateVpdcReference(vr);
			HcEventVmOps hevo = this.loadHcOps(o, otype);
			hevo.setObj_name(vr.getName());
			hevo.setReference_id(vr.getId());
			hevo.setUuid(uuid);
			hevo.setOps(LogOPSType.REBOOT.getIndex());
			Map<String, String> vmMap = new HashMap<String, String>();
			vmMap.put("uuid", uuid);
			rabbitMqUtil.sendMessage(Constants.JOBSERVER_METHOD_VMReboot,
					vmMap, hevo, "HcEventVmOps");
			// waitForState("ACTIVE");
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_REBOOT_ERROR,
					"rebootVm Exception", log, e);
		}
		return result;
	}

	@Transactional(readOnly = false)
	public String backupsVm(String uuid, String sn_name, String comments,
			int sn_type, Object o, String otype) throws HsCloudException {
		String result = null;
		try {
			VpdcInstance instance = vpdcDao.findVmByVmId(uuid);
			if (instance == null) {
				return null;
			}
			VpdcReference vr = instance.getVpdcreference();
			if (Constants.VM_ISENABLE_TURE != vr.getIsEnable()) {
				throw new HsCloudException(Constants.VM_FREEZE_NODO,
						"openvm异常", log, null);
			}
			List<VolumeVO> volumes = getAttachVolumesOfVm(uuid);
			if (volumes != null && volumes.size() > 0) {
				log.error("VM has volume of iscsis!");
				throw new HsCloudException(Constants.VM_BACKUP_ERROR,
						"backupsVm异常", log, null);
			}
			// 如果VM不是ACTIVE状态则禁止备份操作
			if (!"ACTIVE".equalsIgnoreCase(vr.getVm_status())
					|| !StringUtils.isEmpty(vr.getVm_task_status())) {
				log.debug("backup VM failed cause of the status is't ACTIVE");
				return result;
			}
			Date d = new Date();
			vr.setVm_task_status(Constants.VM_STATUS_SNAPSHOT);
			vr.setVm_status(Constants.VM_STATUS_SNAPSHOT);
			vr.setUpdateDate(d);
			vpdcDao.updateVpdcReference(vr);
			VmSnapShot snapShot = new VmSnapShot();
			snapShot.setCreateTime(d);
			snapShot.setSnapShot_name(sn_name);
			snapShot.setSnapShot_comments(comments);
			snapShot.setSnapShot_type(sn_type);
			snapShot.setInstance(instance);
			/*
			 * snapShot.setStatus(1); ServerResource resource =
			 * openstackUtil.getCompute().servers().server(uuid); SnapshotBase
			 * sb = resource.createSnapshot(backupName_stack);
			 * snapShot.setSnapShot_id
			 * (sb==null?"No SnapshotBase return!":sb.getName());
			 */
			vpdcDao.createVmSnapShot(snapShot);
			HcEventVmOps hevo = this.loadHcOps(o, otype);
			hevo.setObj_name(vr.getName());
			hevo.setRemark(comments);
			hevo.setReference_id(vr.getId());
			hevo.setUuid(uuid);
			hevo.setOps(LogOPSType.BACKUP.getIndex());
			Map<String, String> backupMap = new HashMap<String, String>();
			backupMap.put("uuid", uuid);
			backupMap.put("backupID", String.valueOf(snapShot.getId()));
			rabbitMqUtil.sendMessage(Constants.JOBSERVER_METHOD_VMBackup,
					backupMap, hevo, "HcEventVmOps");
			result = sn_name;
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_BACKUP_ERROR,
					"backupsVm异常", log, e);
		}
		return result;
	}

	/**
	 * <终止快照:实质就是删除超过30分钟都还没有创建成功的快照>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean stopSnapshot(String vmId) throws HsCloudException {
		try {
			boolean result = false;
			VpdcInstance vi = vpdcDao.findVmByVmId(vmId);
			if (vi != null) {
				result = vpdcDao.deleteSnapshot(vi.getId());
			}
			return result;
		} catch (HsCloudException e) {
			throw new HsCloudException("", "stopSnapshot异常", log, e);
		}
	}

	@Transactional(readOnly = false)
	public String backupsVmPlan(VmSnapShotPlan vssp) throws HsCloudException {
		String result = null;
		try {
			VpdcReference vr = vpdcDao.findVpdcReferenceByVmId(vssp.getVmId());
			if (Constants.VM_ISENABLE_TURE != vr.getIsEnable()) {
				throw new HsCloudException(Constants.VM_FREEZE_NODO,
						"openvm异常", log, null);
			}
			vpdcDao.createVmSnapShotPlan(vssp);
		} catch (HsCloudException e) {
			throw new HsCloudException(Constants.VM_BACKUP_PLAN_ERROR,
					"backupsVmPlan异常", log, e);
		}
		return result;
	}

	@Transactional(readOnly = false)
	public VmSnapShotPlanVO getVmSnapShotPlanByVmId(String vmId)
			throws HsCloudException {
		VmSnapShotPlan vssp = vpdcDao.getVmSnapShotPlanByVmId(vmId);
		VmSnapShotPlanVO vsspVO = null;
		if (vssp != null) {
			vsspVO = new VmSnapShotPlanVO();
			BeanUtils.copyProperties(vssp, vsspVO);
		}
		return vsspVO;
	}

	@Transactional(readOnly = false)
	public String renewVm(String uuid, String ssid, Object o, String otype) {
		String result = null;
		try {
			VpdcReference vr = vpdcDao.findVpdcReferenceByVmId(uuid);
			if (Constants.VM_ISENABLE_TURE != vr.getIsEnable()) {
				throw new HsCloudException(Constants.VM_FREEZE_NODO,
						"openvm异常", log, null);
			}
			// ServerResource sr =
			// openstackUtil.getCompute().servers().server(uuid);
			if (!"ACTIVE".equalsIgnoreCase(vr.getVm_status())) {
				log.debug("Renew VM failed cause of the status is't ACTIVE");
				return result;
			}
			if (ssid != null && !"".equals(ssid)) {
				VmSnapShot vss = vpdcDao.getVmSnapShotById(Integer
						.valueOf(ssid));
				String opstk_vssName = vss.getSnapShot_id();
				if (opstk_vssName != null && !"".equals(opstk_vssName)) {
					// sr.recoverSnapshot(opstk_vssName);
					HcEventVmOps hevo = this.loadHcOps(o, otype);
					hevo.setObj_name(vr.getName());
					hevo.setReference_id(vr.getId());
					hevo.setUuid(uuid);
					hevo.setOps(LogOPSType.RESTORE.getIndex());
					Map<String, String> renewVmMap = new HashMap<String, String>();
					renewVmMap.put("uuid", uuid);
					renewVmMap.put("snapshotName", opstk_vssName);
					rabbitMqUtil.sendMessage(
							Constants.JOBSERVER_METHOD_VMReduce, renewVmMap,
							hevo, "HcEventVmOps");

				}
			}
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_RENEW_ERROR, "renewVm异常",
					log, e);
		}
		return result;
	}

	@Transactional(readOnly = false)
	public List<VmSnapShotVO> findAllSnapShots(String uuid)
			throws HsCloudException {
		List<VmSnapShotVO> vmVoList = new ArrayList<VmSnapShotVO>();
		try {
			List<VmSnapShot> vmList = vpdcDao.getVmSnapShot(uuid);
			VmSnapShotVO vssvo = null;
			for (VmSnapShot vss : vmList) {
				if (!StringUtils.isEmpty(vss.getSnapShot_id())
						&& !vss.getSnapShot_id().contains("No")) {
					vssvo = new VmSnapShotVO();
					vssvo.setId(vss.getId());
					vssvo.setCreateTime(vss.getCreateTime());
					vssvo.setSnapShot_name(vss.getSnapShot_name());
					vssvo.setSnapShot_comments(vss.getSnapShot_comments());
					vssvo.setSnapShot_id(vss.getSnapShot_id());
					vssvo.setSnapShot_type(vss.getSnapShot_type());
					vmVoList.add(vssvo);
				}
			}
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_SNAPSHOT_LIST_ERROR,
					"findAllSnapShots异常", log, e);
		}
		return vmVoList;
	}

	@Transactional(readOnly = false)
	public String deleteSnapShot(String VmId, String id)
			throws HsCloudException {
		String result = null;
		try {
			VmSnapShot vss = vpdcDao.getVmSnapShotById(Integer.valueOf(id));
			// delete OpenStack
			String opstk_vssName = vss.getSnapShot_id();
			if (opstk_vssName != null && !"".equals(opstk_vssName)) {
				openstackUtil.getCompute().servers().server(VmId)
						.deleteSnapshot(opstk_vssName);
			}
			// delete DB
			vpdcDao.deleteSnapShot(vss);// 删除备份记录
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_SNAPSHOT_DELETE_ERROR,
					"deleteSnapShot异常", log, e);
		}
		return result;
	}

	public String deleteSnapShot(String VmId) throws HsCloudException {
		List<VmSnapShot> vmList = vpdcDao.getVmSnapShot(VmId);
		for (VmSnapShot vss : vmList) {
			// delete OpenStack
			String opstk_vssName = vss.getSnapShot_id();
			if (opstk_vssName != null && !"".equals(opstk_vssName)) {
				openstackUtil.getCompute().servers().server(VmId)
						.deleteSnapshot(opstk_vssName);
			}
			// delete DB
			vpdcDao.deleteSnapShot(vss);
		}
		return null;
	}

	public String getVMOSUser(int osId) {
		String imageId = vpdcDao.findOsInfo("image_id", osId);
		List<Image> images = openstackUtil.getCompute().images().get()
				.getList();
		String radom_user = "administrator";
		for (Image im : images) {
			if (imageId.equals(im.getId())) {
				Map<String, String> m = im.getMetadata();
				radom_user = m.get("username");
				if (StringUtils.isEmpty(radom_user)) {
					if ("Linux".equals(m.get("os_type"))) {
						radom_user = "root";
					}
				}
				break;
			}
		}
		return radom_user;
	}

	public boolean resetVMOS(String vmId, int osId, String user, String pwd,
			Object o, String otype) {
		boolean bl = false;
		try {
			String imageId = vpdcDao.findOsInfo("image_id", osId);
			VpdcReference vr = vpdcDao.findVpdcReferenceByVmId(vmId);
			if (vr == null) {
				return bl;
			}
			if (Constants.VM_ISENABLE_TURE == vr.getIsEnable()) {
				// openstack reset OS
				// ServerResource serverResource =
				// openstackUtil.getCompute().servers().server(vmId);
				if ("ACTIVE".equalsIgnoreCase(vr.getVm_status())) {
					HcEventVmOps hevo = this.loadHcOps(o, otype);
					if (hevo == null) {
						vr.setUpdateId(0l);
					} else {
						vr.setUpdateId(hevo.getOperator_id());
					}
					vpdcDao.updateVpdcReference(vr);
					hevo.setReference_id(vr.getId());
					hevo.setUuid(vmId);
					hevo.setObj_name(vr.getName());
					hevo.setOps(LogOPSType.REBUILD.getIndex());
					Map<String, String> imageMap = new HashMap<String, String>();
					imageMap.put("uuid", vmId);
					imageMap.put("imageId", imageId);
					imageMap.put("osId", String.valueOf(osId));
					imageMap.put("use_pwd", user + "/" + pwd);
					rabbitMqUtil.sendMessage(
							Constants.JOBSERVER_METHOD_VMRebuild, imageMap,
							hevo, "HcEventVmOps");
					bl = true;
				}
				// 删除DB和openstack的snapshot
				this.deleteSnapShot(vmId);
			} else {
				throw new HsCloudException(Constants.VM_FREEZE_NODO,
						"openvm异常", log, null);
			}
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_RESETOS_ERROR,
					"resetVMOS异常", log, e);
		}
		return bl;
	}

	@Transactional(readOnly = false)
	public boolean terminate(String uuid, String vmName, String terminateFlag,
			long adminId) throws HsCloudException {
		boolean result = false;
		long referenceId = 0;
		List<String> floatingIps = null;
		final String terminateFlag_ = terminateFlag;
		Date d = new Date();
		try {
			Admin a = adminDao.get(adminId);
			if (!StringUtils.isEmpty(uuid)) {
				VpdcInstance instance = vpdcDao.findVmByVmId(uuid);
				// 释放IP
				floatingIps = vpdcDao.getFloatingIpsFromDetailIp(instance
						.getId());
				if (floatingIps != null) {
					for (String ip : floatingIps) {
						vpdcDao.resetIPstatus(4, adminId, ip);
					}
				}
				// 删除snapshot
				List<VmSnapShot> vssList = vpdcDao.getVmSnapShot(uuid);
				if (vssList != null) {
					for (VmSnapShot vss : vssList) {
						vpdcDao.deleteSnapShot(vss);
					}
				}
				// 删除VM
				instance.setStatus(1);
				instance.setUpdateDate(d);
				instance.setUpdateId(adminId);
				vpdcDao.updateVpdcInstance(instance);
				// 同步删除内、外网安全记录
				vpdcDao.deleteVmExtranet(instance.getId());
				vpdcDao.deleteVmIntranet(instance.getId());
				// 同步删除控制面板信息
				controlPanelService.deleCPByVmId(uuid);
				VpdcReference vr = vpdcDao.findVpdcReferenceByVmId(instance
						.getVmId());
				vr.setVm_status("");
				vr.setVm_task_status("");
				if ("1".equals(terminateFlag_)) {// terminateFlag为1时删除配置
					vr.setStatus(1);
					vr.setUpdateDate(d);
					vr.setUpdateId(adminId);
					vpdcDao.updateVpdcReference(vr);
				}
				if (!uuid.contains("temp")) {// 如果uuid不是临时id则发消息删除VM实例
					// 调用RabbitMQ发送消息
					HcEventResource her = new HcEventResource();
					her.setEvent_time(new Date());
					her.setType(LogResourceOperationType.DELETE.getIndex());// 删除
					her.setRes_type(LogResourceType.VM.getIndex());// 操作VM
					if (LogOperatorType.UNKNOWN.getIndex() == adminId) {
						her.setBiz_type(LogBizType.EXPIRE_DELETE.getIndex());// 到期删除
						her.setOperator_type(LogOperatorType.PROCESS.getIndex());
					} else {
						her.setBiz_type(LogBizType.ADMIN_DELETE.getIndex());// 管理员删除
						her.setOperator(a.getEmail());
						her.setOperator_id(a.getId());
						her.setOperator_type(LogOperatorType.ADMIN.getIndex());
					}
					her.setObj_name(vr.getName());
					her.setObj_id(vr.getId());
					Map<String, String> vmMap = new HashMap<String, String>();
					vmMap.put("uuid", instance.getVmId());
					if (adminId == 0) {// 到期系统删除
						rabbitMqUtil.sendMessage(
								Constants.JOBSERVER_METHOD_DeVMExpire, vmMap,
								her, "HcEventResource");
					} else {// 管理员删除
						rabbitMqUtil.sendMessage(
								Constants.JOBSERVER_METHOD_DeVMAdmin, vmMap,
								her, "HcEventResource");
					}
				}
			} else if (!StringUtils.isEmpty(vmName)) {
				// 当uuid为空vmName不为空时，说明instance已配删除，此次操作需要删除reference。
				log.info("############instance already deleted then only delete reference...");
				VpdcReference vr = vpdcDao.findVpdcReferenceByVmName(vmName);
				vr.setUpdateDate(new Date());
				vr.setUpdateId(adminId);
				vr.setStatus(1);
				referenceId = vr.getId();
				vpdcDao.updateVpdcReference(vr);
				VpdcInstance instance = this.getActiveVmFromVpdcReference(vr);
				if (instance != null) {
					instance.setStatus(1);
					instance.setUpdateDate(d);
					instance.setUpdateId(adminId);
					vpdcDao.updateVpdcInstance(instance);
					// 同步删除内、外网安全记录
					vpdcDao.deleteVmExtranet(instance.getId());
					vpdcDao.deleteVmIntranet(instance.getId());
				}
			}
			result = true;
			vpdcRenewalService.deleteVpdcRenewalByReferenceId(referenceId);
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_TERMINATE_ERROR,
					"terminate Exception", log, e);
		}
		return result;
	}

	@Transactional(readOnly = false)
	public boolean terminate(long referenceId, long adminId, User u,
			String comments) {
		boolean result = false;
		String name = null;
		Date d = new Date();
		try {
			Admin a = adminDao.get(adminId);
			VpdcReference vr = vpdcDao.findVpdcReferenceById(referenceId);
			name = vr.getName();
			// 云主机已被删除则直接返回true
			if (0 == vr.getStatus()) {
				VpdcInstance instance = this.getActiveVmFromVpdcReference(vr);
				// 释放IP
				List<String> floatingIps = vpdcDao
						.getFloatingIpsFromDetailIp(instance.getId());
				if (floatingIps != null) {
					for (String ip : floatingIps) {
						vpdcDao.resetIPstatus(4, adminId, ip);
					}
				}
				// 删除snapshot
				List<VmSnapShot> vssList = vpdcDao.getVmSnapShot(instance
						.getVmId());
				if (vssList != null) {
					for (VmSnapShot vss : vssList) {
						vpdcDao.deleteSnapShot(vss);
					}
				}
				// 删除Volume
				/*
				 * List<VpdcReference_extdisk> lvre =
				 * vpdcDao.getExtDisksByVmId(instance.getVmId());
				 * if(lvre!=null){ for(VpdcReference_extdisk vre : lvre){
				 * if(vre.getVolumeId()!=null){ VpdcReference_extdisk vied =
				 * vpdcDao
				 * .getExtDiskByVolumeId(instance.getVmId(),vre.getVolumeId());
				 * if(vied!=null){ vpdcDao.deleExtDisk(vied); } } } }
				 */
				// 删除VM
				instance.setStatus(1);
				instance.setUpdateDate(d);
				instance.setUpdateId(adminId);
				vpdcDao.updateVpdcInstance(instance);
				// 同步删除控制面板信息
				controlPanelService.deleCPByVmId(instance.getVmId());
				String ip = vr.getVm_outerIP();
				if (StringUtils.isEmpty(ip)) {
					ip = vpdcDao.findIPByObjectId(instance.getId());
				}
				vr.setStatus(1);
				vr.setUpdateDate(d);
				vr.setComments(comments);
				vr.setUpdateId(adminId);
				vr.setVm_status("");
				vr.setVm_task_status("");
				vpdcDao.updateVpdcReference(vr);
				// 发消息（云主机到期删除）
				sendMsgForTrialExpire(u, "您购买的云主机【" + name + "】退款已删除！");
				// 调用RabbitMQ发送消息
				HcEventResource her = new HcEventResource();
				her.setEvent_time(new Date());
				her.setType(LogResourceOperationType.DELETE.getIndex());// 删除
				her.setRes_type(LogResourceType.VM.getIndex());// 操作VM
				her.setBiz_type(LogBizType.REFUND_DELETE.getIndex());// 退款删除
				her.setObj_name(instance.getVpdcreference().getName());
				her.setObj_id(instance.getVpdcreference().getId());
				her.setOperator(a.getEmail());
				her.setOperator_id(a.getId());
				her.setOperator_type(LogOperatorType.ADMIN.getIndex());
				Map<String, String> vmMap = new HashMap<String, String>();
				vmMap.put("uuid", instance.getVmId());
				vmMap.put("floatingIp", ip);
				rabbitMqUtil.sendMessage(Constants.JOBSERVER_METHOD_DeVMReduce,
						vmMap, her, "HcEventResource");
				result = true;
			} else {
				result = true;
			}
			vpdcRenewalService.deleteVpdcRenewalByReferenceId(referenceId);
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_TERMINATE_ERROR,
					"terminate异常", log, e);
		}
		return result;
	}

	@Transactional(readOnly = false)
	public boolean terminate(String uuid, User user, String comments)
			throws HsCloudException {
		boolean result = false;
		String name = null;
		Date d = new Date();
		try {
			VpdcInstance vi = vpdcDao.findVmByVmId(uuid);
			VpdcReference vr = vi.getVpdcreference();
			name = vr.getName();
			// 云主机已被删除则直接返回true
			if (0 == vr.getStatus()) {
				VpdcInstance instance = this.getActiveVmFromVpdcReference(vr);
				// 释放IP
				List<String> floatingIps = vpdcDao
						.getFloatingIpsFromDetailIp(instance.getId());
				if (floatingIps != null) {
					for (String ip : floatingIps) {
						vpdcDao.resetIPstatus(4, user.getId(), ip);
					}
				}
				// 删除snapshot
				List<VmSnapShot> vssList = vpdcDao.getVmSnapShot(instance
						.getVmId());
				if (vssList != null) {
					for (VmSnapShot vss : vssList) {
						vpdcDao.deleteSnapShot(vss);
					}
				}
				// 删除VM
				instance.setStatus(1);
				instance.setUpdateDate(d);
				instance.setUpdateId(user.getId());
				vpdcDao.updateVpdcInstance(instance);
				// 同步删除控制面板信息
				controlPanelService.deleCPByVmId(instance.getVmId());
				String ip = vr.getVm_outerIP();
				if (StringUtils.isEmpty(ip)) {
					ip = vpdcDao.findIPByObjectId(instance.getId());
				}
				vr.setStatus(1);
				vr.setUpdateDate(d);
				vr.setComments(comments);
				vr.setUpdateId(user.getId());
				vr.setVm_status("");
				vr.setVm_task_status("");
				vpdcDao.updateVpdcReference(vr);
				// 发消息（云主机到期删除）
				sendMsgForTrialExpire(user, "您购买的云主机【" + name + "】退款已删除！");
				// 调用RabbitMQ发送消息
				HcEventResource her = new HcEventResource();
				her.setEvent_time(new Date());
				her.setType(LogResourceOperationType.DELETE.getIndex());// 删除
				her.setRes_type(LogResourceType.VM.getIndex());// 操作VM
				her.setBiz_type(LogBizType.REFUND_DELETE.getIndex());// 退款删除
				her.setObj_name(instance.getVpdcreference().getName());
				her.setObj_id(instance.getVpdcreference().getId());
				her.setOperator(user.getEmail());
				her.setOperator_id(user.getId());
				her.setOperator_type(LogOperatorType.ADMIN.getIndex());
				Map<String, String> vmMap = new HashMap<String, String>();
				vmMap.put("uuid", instance.getVmId());
				vmMap.put("floatingIp", ip);
				rabbitMqUtil.sendMessage(Constants.JOBSERVER_METHOD_DeVMReduce,
						vmMap, her, "HcEventResource");
				result = true;
			} else {
				result = true;
			}
			vpdcRenewalService.deleteVpdcRenewalByReferenceId(vr.getId());
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_TERMINATE_ERROR,
					"terminate异常", log, e);
		}
		return result;
	}

	public Page<InstanceVO> getVmsByUser(String sort, User user,
			List<Object> referenceIds, Page<InstanceVO> page, Long statusId,
			String type, String status_buss) throws HsCloudException {
		int offSet, length;
		if (page.getPageNo() == 1) {
			offSet = 0;
			length = page.getPageSize();
		} else {
			offSet = (page.getPageNo() - 1) * page.getPageSize();
			length = page.getPageSize();
		}
		// 添加分页处理
		int recordSize = 0;
		List<VpdcReference> vpdcRefernces = new ArrayList<VpdcReference>();
		List<InstanceVO> instanceVOs = new ArrayList<InstanceVO>();
		// 通过权限给予的VM
		List<Object> ids = new ArrayList<Object>();
		if (referenceIds != null) {
			for (Object id : referenceIds) {
				ids.add(id);
			}
		} else {// 当权限返回NULL时，则根据当前身份ID查询出自己或所属企业用户下的VM
			ids = vpdcDao.findVMIdsByOwnerId(statusId);
		}
		// 根据云主机的类型过滤ids(type为null:云主机中心；type有值[0:试用；1正式]业务中心)
		ids = vpdcDao.findReferencesByType(ids, type, status_buss);
		recordSize = vpdcDao.getVRCountByIds(ids, statusId);
		vpdcRefernces = vpdcDao.findVpdcReferencesByIds(offSet, length, ids,
				sort, statusId);
		// 将查询出来的VM封装成VO
		this.loadUserReferences(vpdcRefernces, instanceVOs);
		page.setResult(instanceVOs);
		page.setTotalCount(recordSize);
		return page;
	}

	public Page<InstanceVO> fuzzyFindVmsUser(String sort, String queryType,
			String value, Page<InstanceVO> page, User user,
			List<Object> referenceIds, Long statusId, String type,
			String status_buss) throws HsCloudException {
		int offSet, length;
		if (page.getPageNo() == 1) {
			offSet = 0;
			length = page.getPageSize();
		} else {
			offSet = (page.getPageNo() - 1) * page.getPageSize();
			length = page.getPageSize();
		}
		// 分页处理
		int recordSize = 0;
		List<VpdcReference> references = new ArrayList<VpdcReference>();
		List<InstanceVO> instanceVOs = new ArrayList<InstanceVO>();
		// 权限获取VM
		List<Object> ids = new ArrayList<Object>();
		if (referenceIds != null) {
			for (Object id : referenceIds) {
				ids.add(id);
			}
		} else {// 当权限返回NULL时，则根据当前身份ID查询出自己或所属企业用户下的VMIds
			ids = vpdcDao.findVMIdsByOwnerId(statusId);
		}
		// 根据云主机的类型过滤ids(type为null:云主机中心；type有值[0:试用；1正式]业务中心)
		ids = vpdcDao.findReferencesByType(ids, type, status_buss);
		String condition = "name";
		if ("ipOuter".equals(queryType)) {
			condition = "vm_outerIP";
		} else if ("ipInner".equals(queryType)) {
			condition = "vm_innerIP";
		}
		recordSize = vpdcDao.getVRCountByCondition(ids, condition, value,
				statusId);
		references = vpdcDao.findVpdcReferencesByCondition(offSet, length,
				sort, ids, condition, value, statusId);

		// 将查询出来的VM封装成VO
		loadUserReferences(references, instanceVOs);
		page.setResult(instanceVOs);
		page.setTotalCount(recordSize);
		return page;
	}

	/**
	 * <新加的接口：UI3.0 分页获取User用户所有的虚拟机>
	 * 
	 * @param boolean isBuy 如果true，则是用户购买的虚机，如是false，则是供应商看见用户买的虚机列表
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public Page<InstanceVO> getHostsByUser(String sort, String query,
			Page<InstanceVO> page, User user, List<Object> referenceIds,
			Long statusId, String type, String status_buss, boolean isBuy)
			throws HsCloudException {
		int offSet, length;
		if (page.getPageNo() == 1) {
			offSet = 0;
			length = page.getPageSize();
		} else {
			offSet = (page.getPageNo() - 1) * page.getPageSize();
			length = page.getPageSize();
		}
		// 分页处理
		int recordSize = 0;
		List<VpdcReference> references = new ArrayList<VpdcReference>();
		List<InstanceVO> instanceVOs = new ArrayList<InstanceVO>();
		// 权限获取VM
		List<Object> ids = new ArrayList<Object>();
		if (referenceIds != null) {
			for (Object id : referenceIds) {
				ids.add(id);
			}
		} else {// 当权限返回NULL时，则根据当前身份ID查询出自己或所属企业用户下的VMIds
			ids = vpdcDao.findVMIdsByOwnerId(statusId);
		}
		// 根据云主机的类型过滤ids(type为null:云主机中心；type有值[0:试用；1正式]业务中心)
		ids = vpdcDao.findReferencesByType(ids, type, status_buss);
		recordSize = vpdcDao.getVRCountByQuery(ids, query, statusId, isBuy);
		references = vpdcDao.findVpdcReferencesByQuery(offSet, length, sort,
				ids, query, statusId, isBuy);

		// 将查询出来的VM封装成VO
		loadUserReferences(references, instanceVOs);
		page.setResult(instanceVOs);
		page.setTotalCount(recordSize);
		return page;
	}

	/**
	 * <封装用户的VM信息为VO> <功能详细描述>
	 * 
	 * @param references
	 * @param instanceVOs
	 * @see [类、类#方法、类#成员]
	 */
	private void loadUserReferences(List<VpdcReference> references,
			List<InstanceVO> instanceVOs) {
		if (references.isEmpty()) {
			if (log.isDebugEnabled()) {
				log.debug("vpdcRefernces is null");
			}
			return;
		}
		VpdcInstance vi = null;
		String osName = null;
		List<VmOsBean> osList = null;
		String ips = null;
		for (VpdcReference vr : references) {
			vi = getActiveVmFromVpdcReference(vr);
			InstanceVO instanceVO = new InstanceVO();
			// 设置CPU 内存 硬盘
			instanceVO.setCpu_core(vr.getCpu_core());
			instanceVO.setMemory_size(vr.getMem_size());
			if (vr.getDisk_capacity() != null) {
				instanceVO.setDisk_capacity(vr.getDisk_capacity());
			}

			if (vi != null) {
				instanceVO.setInstanceId(vi.getId());
				ips = (StringUtils.isEmpty(vr.getVm_innerIP()) ? "" : vr
						.getVm_innerIP())
						+ (StringUtils.isEmpty(vr.getVm_outerIP()) ? "" : ","
								+ vr.getVm_outerIP());
				instanceVO.setIp(ips);
			}
			instanceVO.setStatus(vr.getVm_status() == null ? null : vr
					.getVm_status().toUpperCase());
			instanceVO
					.setTask(StringUtils.isEmpty(vr.getVm_task_status()) ? null
							: vr.getVm_task_status().toUpperCase());
			instanceVO.setComments(vr.getComments());
			instanceVO.setReferenceId(vr.getId());
			instanceVO.setApplyTime(vr.getCreateDate());
			instanceVO.setScId(vr.getScId());
			instanceVO.setName(vr.getName());
			instanceVO.setSysUser(vr.getRadom_user());
			instanceVO.setZone(vr.getVmZone());
			instanceVO.setVMtype(getVMTypeName(vr.getVm_type()));
			instanceVO.setStatus_buss(getVMStatusBussName(vr
					.getVm_business_status()));
			instanceVO.setCreateType(vr.getCreaterType());
			instanceVO.setIsEnable(vr.getIsEnable());
			instanceVO.setBuyType(vr.getBuyType());
			instanceVO.setOsId(vr.getOsId());
			if (vr.getOsId() != null) {
				osName = vpdcDao.findOsName(vr.getOsId());
				instanceVO.setOsName(osName);
			}
			osList = vpdcDao.findOsListByReferenceId(vr.getId());
			instanceVO.setOsList(osList);
			if (vi == null) {
				instanceVO.setStatus(Constants.VM_NOINSTANCE);
				instanceVOs.add(instanceVO);
				continue;
			}
			instanceVO.setId(vi.getVmId());
			try {
				Date now = new Date();
				VpdcReference_Period vrp = vpdcDao.getReferencePeriod(vr
						.getId());
				instanceVO.setCreateTime(vrp.getStartTime());
				instanceVO.setExpireTime(vrp.getEndTime());
				if (vrp.getEndTime() != null
						&& vrp.getEndTime().before(new Date())) {
					if (instanceVO.getStatus_buss().equals(
							VMStatusBussEnum.TRY.getName())
							|| instanceVO.getStatus_buss().equals(
									VMStatusBussEnum.DELAY.getName())) {
						instanceVO.setStatus_buss(VMStatusBussEnum.EXPIRE
								.getName() + "_" + instanceVO.getStatus_buss());
					} else if (instanceVO.getStatus_buss() == null
							|| instanceVO.getStatus_buss().equals(
									VMStatusBussEnum.REGULAR.getName())) {
						instanceVO.setStatus_buss(VMStatusBussEnum.EXPIRE
								.getName());
					}
				}
				if (vrp != null) {
					Date startTime = vrp.getStartTime();
					// 如果开始时间为空，则已使用时长为null
					if (startTime != null) {
						long between = (now.getTime() - startTime.getTime()) / 1000;// 除以1000是为了转换成秒
						Date runTime = new Date(between);
						instanceVO.setRunTime(runTime);
					}
					// 如果终止时间不为空
					if (vrp.getEndTime() != null) {
						long titleTime = 0;
						Long spare = 0L;
						if (startTime != null) {// 在开始时间不为空下正常计时
							titleTime = (vrp.getEndTime().getTime() - startTime
									.getTime()) / 1000;
							spare = titleTime
									- instanceVO.getRunTime().getTime();
							if (spare <= 0) {
								instanceVO.setRunTime(new Date(titleTime));
							}
						} else {// 在开始时间为空下则为终止时间减去当前时间
							spare = (vrp.getEndTime().getTime() - new Date()
									.getTime()) / 1000;
						}
						instanceVO.setSpare(spare);
					}
				}
				instanceVOs.add(instanceVO);
			} catch (Exception e) {
				log.error(e);
				instanceVOs.add(instanceVO);
				continue;
			}
		}
	}

	/**
	 * <用途: 1.默认加载分页获取User用户所有的待添加内网安全策略的云主机 2.当点击内网安全策略的搜索按钮:
	 * 分页获取User用户所有的待添加内网安全策略的云主机>
	 * 
	 * @author liyunhui
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@Override
	public Page<UnaddedIntranetInstanceVO> getWaitAddIntranetSecurityVms(
			String sort, String value,
			Page<UnaddedIntranetInstanceVO> pageUnaddedIns, User user,
			List<Object> referenceIds, Long statusId, String type,
			String status_buss, String groupId) throws HsCloudException {
		int offSet, length;
		if (pageUnaddedIns.getPageNo() == 1) {
			offSet = 0;
			length = pageUnaddedIns.getPageSize();
		} else {
			offSet = (pageUnaddedIns.getPageNo() - 1)
					* pageUnaddedIns.getPageSize();
			length = pageUnaddedIns.getPageSize();
		}
		// 分页处理
		int recordSize = 0;
		List<VpdcReference> references = new ArrayList<VpdcReference>();
		List<UnaddedIntranetInstanceVO> unaddedInsVOs = new ArrayList<UnaddedIntranetInstanceVO>();
		// 权限获取VM
		List<Object> vpdcRefrenceIds = new ArrayList<Object>();
		if (referenceIds != null) {
			for (Object id : referenceIds) {
				vpdcRefrenceIds.add(id);
			}
		} else {// 当权限返回NULL时，则根据当前身份ID查询出自己或所属企业用户下的VMIds
			vpdcRefrenceIds = vpdcDao.findVMIdsByOwnerId(statusId);
		}
		// 根据云主机的类型过滤ids(type为null:云主机中心；type有值[0:试用；1正式]业务中心)
		vpdcRefrenceIds = vpdcDao.findReferencesByType(vpdcRefrenceIds, type,
				status_buss);
		List<VmIntranetSecurity_Instance> visi_list = vpdcDao
				.getIntranet_Instance(null, groupId);
		List<Long> filteredVpdcRefrenceIds = new ArrayList<Long>();
		// 如果没有需要过滤的云主机,直接查询所有的数据
		if (visi_list.size() == 0) {
			// filteredVpdcRefrenceIds = vpdcRefrenceIds;
			for (Object id : vpdcRefrenceIds) {
				filteredVpdcRefrenceIds.add((Long) id);
			}
		} else {
			List<Long> addIntranetInstanceIds = new ArrayList<Long>();
			for (int i = 0; i < visi_list.size(); i++) {
				addIntranetInstanceIds.add(visi_list.get(i).getInstance_id());
			}
			List<Long> addIntranetVpdcRefrenceIds = vpdcDao
					.getAddIntranetVpdcRefrenceIds(addIntranetInstanceIds);
			// 去掉被添加到其他组的云主机
			for (int i = 0; i < vpdcRefrenceIds.size(); i++) {
				Long vpdcRefrenceId = ((Long) vpdcRefrenceIds.get(i))
						.longValue();
				for (int j = 0; j < addIntranetVpdcRefrenceIds.size(); j++) {
					if (vpdcRefrenceId == addIntranetVpdcRefrenceIds.get(j)
							.longValue()) {
						vpdcRefrenceIds.set(i, null);
					}
				}
			}
			for (int i = 0; i < vpdcRefrenceIds.size(); i++) {
				if (vpdcRefrenceIds.get(i) != null) {
					filteredVpdcRefrenceIds.add(Long.valueOf(vpdcRefrenceIds
							.get(i).toString()));
				}
			}
		}
		recordSize = vpdcDao.getVRCountByCondition(filteredVpdcRefrenceIds,
				value, statusId);
		references = vpdcDao.findVpdcReferencesByCondition(offSet, length,
				sort, filteredVpdcRefrenceIds, value, statusId);
		// 将查询出来的VM封装成VO
		loadWaitAddIntranetSecurityVms(references, unaddedInsVOs);
		pageUnaddedIns.setResult(unaddedInsVOs);
		pageUnaddedIns.setTotalCount(recordSize);
		return pageUnaddedIns;
	}

	/**
	 * <用途: 获取当前用户的当前uuid这台云主机的添加了外网安全的协议和端口的详细信息
	 * 
	 * @author liyunhui
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@Override
	public List<VmExtranetSecurityVO> getExtranetSecurityInfo(String uuid)
			throws HsCloudException {
		try {
			List<VmExtranetSecurity> ves_list = vpdcDao
					.getExtranetSecurityVmInfoByUuid(uuid);
			// 没有查询到一条数据
			if (ves_list == null || ves_list.size() <= 0) {
				return null;
			}
			// 把VmExtranetSecurity转换为VmExtranetSecurityVO
			List<VmExtranetSecurityVO> vesVO_list = new ArrayList<VmExtranetSecurityVO>();
			for (VmExtranetSecurity ves : ves_list) {
				VmExtranetSecurityVO vesVO = new VmExtranetSecurityVO();
				vesVO.setUuid(ves.getUuid());
				vesVO.setPort_from(ves.getPort_from());
				vesVO.setPort_to(ves.getPort_to());
				vesVO.setProtocal(ves.getProtocal());
				vesVO_list.add(vesVO);
			}
			return vesVO_list;
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001",
					"getExtranetSecurityInfo异常", log, e);
		}
	}

	/**
	 * <封装用户的待添加的云主机VM信息为VO> <功能详细描述>
	 * 
	 * @param references
	 * @param instanceVOs
	 * @see [类、类#方法、类#成员]
	 */
	private void loadWaitAddIntranetSecurityVms(List<VpdcReference> references,
			List<UnaddedIntranetInstanceVO> unaddedInsVOs) {
		if (references.isEmpty()) {
			if (log.isDebugEnabled()) {
				log.debug("vpdcRefernces is null");
			}
			return;
		}
		try {
			VpdcInstance vi = null;
			String ips = null;
			for (VpdcReference vr : references) {
				UnaddedIntranetInstanceVO unaddedInsVO = new UnaddedIntranetInstanceVO();
				vi = getActiveVmFromVpdcReference(vr);
				unaddedInsVO.setReferenceId(vr.getId());
				unaddedInsVO.setName(vr.getName());
				ips = (StringUtils.isEmpty(vr.getVm_innerIP()) ? "" : vr
						.getVm_innerIP())
						+ (StringUtils.isEmpty(vr.getVm_outerIP()) ? "" : ","
								+ vr.getVm_outerIP());
				unaddedInsVO.setIp(ips);
				unaddedInsVO.setIsEnable(vr.getIsEnable());
				unaddedInsVO.setStatus(vr.getVm_status() == null ? null : vr
						.getVm_status().toUpperCase());
				// 如果云主机没有实例
				if (vi == null) {
					unaddedInsVO.setInstanceId(null);
					unaddedInsVO.setId(null);
					unaddedInsVO.setStatus(Constants.VM_NOINSTANCE);
					unaddedInsVOs.add(unaddedInsVO);
					continue;
				}
				unaddedInsVO.setInstanceId(vi.getId());
				unaddedInsVO.setId(vi.getVmId());
				unaddedInsVOs.add(unaddedInsVO);
			}
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001",
					"loadWaitAddIntranetSecurityVms异常", log, e);
		}
	}

	public Page<VmInfoVO> fuzzyFindVmsAdmin(String nodeName, String field,
			String value, Page<VmInfoVO> page, Admin admin,
			List<Object> referenceIds, String sort, String zoneCode)
			throws HsCloudException {
		String condition = "name";
		;
		Page<VpdcReference> referencePage = new Page<VpdcReference>();
		referencePage.setPageNo(page.getPageNo());
		referencePage.setPageSize(page.getPageSize());
		int offSet, length;
		if (page.getPageNo() == 1) {
			offSet = 0;
			length = page.getPageSize();
		} else {
			offSet = (page.getPageNo() - 1) * page.getPageSize();
			length = page.getPageSize();
		}

		List<VmInfoVO> instanceAdminVos = new ArrayList<VmInfoVO>();
		List<VpdcReference> referenceList = null;
		List<Object> ids = null;
		int referenceSize = 0;
		if (admin.getIsSuper() || roleService.isSpecialAdmin(admin.getId())
				|| referenceIds == null) {// 当admin为超级管理员
			if (field == null || value == null || "".equals(value)) {// 当没有条件时
				referenceSize = vpdcDao.getVRCount(nodeName, zoneCode);
				referenceList = vpdcDao.findVpdcReference(offSet, length,
						nodeName, zoneCode);
			} else {// 当有条件时
				if ("vmOwner".equals(field)) {// 条件为用户时
					// 显示所有resources 的prikey
					// ids =
					// resourceService.getOwnerResourcePrimKey(ResourceType.VM.getEntityName(),
					// value);
					referencePage = vpdcDao.findVpdcReferenceByOwnerAdmin(
							referencePage, value, nodeName, zoneCode);
					referenceSize = (int) referencePage.getTotalCount();
					referenceList = referencePage.getResult();
				} else {// 条件为其它
					if ("ipOuter".equals(field)) {// 条件为外网IP
						condition = "vm_outerIP";
					} else if ("ipInner".equals(field)) {// 条件为内网IP
						condition = "vm_innerIP";
					} else if ("remark".equals(field)) {// 条件为备注
						condition = "comments";
					} else if ("outComments".equals(field)) {// 条件为备注
						condition = "outComments";
					}
					referenceSize = vpdcDao.getVRCount(nodeName, condition,
							value, zoneCode);
					referenceList = vpdcDao.findVpdcReference(offSet, length,
							nodeName, condition, value, zoneCode);
				}
			}
			page.setTotalCount(referenceSize);
		} else {// 当admin为普通管理员
			if ("vmOwner".equals(field)) {// 条件为用户时
				condition = "vmOwner";
			}
			ids = new ArrayList<Object>();
			for (Object id : referenceIds) {
				ids.add(id);
			}
			if ("ipOuter".equals(field)) {
				condition = "vm_outerIP";
			}
			if ("ipInner".equals(field)) {
				condition = "vm_innerIP";
			}
			if ("remark".equals(field)) {
				condition = "comments";
			}
			if ("outComments".equals(field)) {
				condition = "outComments";
			}
			referencePage = vpdcDao.fuzzyFindVmsAdmin(referencePage, nodeName,
					condition, value, ids, zoneCode, admin.getId());
			page.setTotalCount(referencePage.getTotalCount());
			referenceList = referencePage.getResult();
		}
		loadAdminReferences(instanceAdminVos, referenceList);
		page.setResult(instanceAdminVos);
		return page;
	}

	/**
	 * <封装管理员的VM信息为VO> <功能详细描述>
	 * 
	 * @param instanceAdminVos
	 * @param referencesPage
	 * @see [类、类#方法、类#成员]
	 */
	private void loadAdminReferences(List<VmInfoVO> instanceAdminVos,
			List<VpdcReference> referencesPage) {
		VpdcInstance vpdcinstance = null;
		User u = null;
		for (VpdcReference vpdcReference : referencesPage) {
			VmInfoVO instanceAdminVO = new VmInfoVO();
			instanceAdminVO.setReferenceId(vpdcReference.getId());
			instanceAdminVO.setVmName(vpdcReference.getName());
			instanceAdminVO.setScId(vpdcReference.getScId());
			instanceAdminVO.setSysUser(vpdcReference.getRadom_user());
			instanceAdminVO.setApplyTime(vpdcReference.getCreateDate());
			instanceAdminVO.setIsEnable(vpdcReference.getIsEnable());
			// 增加 张建伟 增加日期 20131011
			instanceAdminVO.setBandWidthIn(vpdcReference.getBwtIn());
			instanceAdminVO.setBandWidthOut(vpdcReference.getBwtOut());
			instanceAdminVO.setIpConnectionIn(vpdcReference.getIpConnIn());
			instanceAdminVO.setIpConnectionOut(vpdcReference.getIpConnOut());
			instanceAdminVO.setTcpConnectionIn(vpdcReference.getTcpConnIn());
			instanceAdminVO.setTcpConnectionOut(vpdcReference.getTcpConnOut());
			instanceAdminVO.setUdpConnectionIn(vpdcReference.getUdpConnIn());
			instanceAdminVO.setUdpConnectionOut(vpdcReference.getUdpConnOut());
			instanceAdminVO.setCpuLimit(vpdcReference.getCpuLimit());
			instanceAdminVO.setDiskRead(vpdcReference.getDiskRead());
			instanceAdminVO.setDiskWrite(vpdcReference.getDiskWrite());
			if (vpdcReference.getComments() != null) {
				instanceAdminVO.setRemark(vpdcReference.getComments());// 获取备注
			}
			if (vpdcReference.getOutComments() != null) {
				instanceAdminVO.setOutComments(vpdcReference.getOutComments());// 获取备注
			}
			instanceAdminVO.setVmStatus(vpdcReference.getVm_status());
			instanceAdminVO.setProcessState(vpdcReference.getProcessState());
			if (vpdcReference.getOwner() != null) {
				u = userService.getUser(vpdcReference.getOwner());
				if (u != null) {
					instanceAdminVO.setUserName(u.getEmail());
				}
			}
			instanceAdminVO.setVmStatus_buss(getVMStatusBussName(vpdcReference
					.getVm_business_status()));
			if (vpdcReference.getDisk_capacity() != null
					&& !"null".equals(vpdcReference.getDisk_capacity())) {
				instanceAdminVO.setDisk(vpdcReference.getDisk_capacity());
			}
			if (vpdcReference.getCpu_core() != null
					&& !"null".equals(vpdcReference.getCpu_core())) {
				instanceAdminVO.setCpuCore(vpdcReference.getCpu_core());
			}
			if (vpdcReference.getMem_size() != null
					&& !"null".equals(vpdcReference.getMem_size())) {
				instanceAdminVO.setMemory(vpdcReference.getMem_size());
			}
			String osName = vpdcDao.findOsName(vpdcReference.getOsId());
			instanceAdminVO.setVmOS(osName);
			instanceAdminVO.setZoneCode(vpdcReference.getVmZone());
			vpdcinstance = vpdcDao.getActiveVmFromVpdcReference(vpdcReference
					.getId());
			if (vpdcinstance == null) {
				instanceAdminVO.setVmStatus(Constants.VM_NOINSTANCE);
				addInstanceVoToList(instanceAdminVos, instanceAdminVO);
				continue;
			} else {
				instanceAdminVO
						.setIpOuter((StringUtils.isEmpty(vpdcReference
								.getVm_outerIP()) ? "" : vpdcReference
								.getVm_outerIP()));
				instanceAdminVO
						.setIpInner((StringUtils.isEmpty(vpdcReference
								.getVm_innerIP()) ? "" : vpdcReference
								.getVm_innerIP()));
				instanceAdminVO
						.setHostName((vpdcinstance.getNodeName() == null ? ""
								: vpdcinstance.getNodeName()));
			}
			instanceAdminVO.setVmId(vpdcinstance.getVmId());
			VpdcReference_Period vrp = vpdcDao.getReferencePeriod(vpdcReference
					.getId());
			if (vrp != null) {
				instanceAdminVO.setCreateTime(vrp.getStartTime());
				instanceAdminVO.setExpireTime(vrp.getEndTime());
				if (vrp.getEndTime() != null
						&& vrp.getEndTime().before(new Date())) {
					if (instanceAdminVO.getVmStatus_buss().equals(
							VMStatusBussEnum.TRY.getName())
							|| instanceAdminVO.getVmStatus_buss().equals(
									VMStatusBussEnum.DELAY.getName())) {
						instanceAdminVO
								.setVmStatus_buss(VMStatusBussEnum.EXPIRE
										.getName()
										+ "_"
										+ instanceAdminVO.getVmStatus_buss());
					} else if (instanceAdminVO.getVmStatus_buss() == null
							|| instanceAdminVO.getVmStatus_buss().equals(
									VMStatusBussEnum.REGULAR.getName())) {
						instanceAdminVO
								.setVmStatus_buss(VMStatusBussEnum.EXPIRE
										.getName());
					}
				}
			}
			addInstanceVoToList(instanceAdminVos, instanceAdminVO);
		}
	}

	public Page<VmInfoVO> FindVmsAdminBussiness(String field, String value,
			Page<VmInfoVO> page, Admin admin, List<Object> referenceIds,
			String type, String status_buss, String sort)
			throws HsCloudException {
		int offSet, length;
		if (page.getPageNo() == 1) {
			offSet = 0;
			length = page.getPageSize();
		} else {
			offSet = (page.getPageNo() - 1) * page.getPageSize();
			length = page.getPageSize();
		}
		List<VmInfoVO> instanceAdminVos = new ArrayList<VmInfoVO>();
		List<VpdcReference> referencesPage = null;
		List<Object> ids = null;
		int referenceSize = 0;
		String condition = "name";
		if ("ipOuter".equals(field)) {// 条件为外网IP
			condition = "vm_outerIP";
		} else if ("ipInner".equals(field)) {// 条件为内网IP
			condition = "vm_innerIP";
		} else if ("vmId".equals(field)) {
			condition = "vmId";
		}
		if (admin.getIsSuper() || roleService.isSpecialAdmin(admin.getId())) {// 当admin为超级管理员
			referenceSize = vpdcDao.getVRCountByIdsAdminBuss(type, status_buss,
					condition, value);
			referencesPage = vpdcDao.findVpdcReferenceByIdsAdminBuss(offSet,
					length, type, status_buss, condition, value, sort);
		} else {// 当admin为普通管理员
			ids = new ArrayList<Object>();
			if (referenceIds == null) {
				List<Domain> domainList = domainService.getDomainByAdmin(admin
						.getId());
				if (domainList != null && domainList.size() > 0) {
					List<Long> domainIds = new ArrayList<Long>();
					for (Domain domainLocal : domainList) {
						domainIds.add(domainLocal.getId());
					}
					List<Long> userList = userService
							.getUserIdsByDomainIds(domainIds);
					ids = resourceService.getVMResourceKeyList(resourceType,
							userList);
				}
			}
			// 根据云主机的类型状态过滤ids
			try {
				ids = vpdcDao.findReferencesByType(ids, type, status_buss);
				referenceSize = vpdcDao.getVRCountByIdsAdminBuss(ids, type,
						status_buss, condition, value);
				referencesPage = vpdcDao.findVpdcReferenceByIdsAdminBuss(
						offSet, length, ids, type, status_buss, condition,
						value, sort);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		page.setTotalCount(referenceSize);
		loadAdminReferences(instanceAdminVos, referencesPage);
		page.setResult(instanceAdminVos);
		return page;
	}

	public Page<VmInfoVO> findVmsAdminRecycle(String field, String value,
			Page<VmInfoVO> page, Admin admin, List<Object> referenceIds,
			String type, String status_buss, String sort)
			throws HsCloudException {
		// Page<VpdcReference> referencePage = new Page<VpdcReference>();
		// referencePage.setPageNo(page.getPageNo());
		// referencePage.setPageSize(page.getPageSize());
		// List<VmInfoVO> instanceAdminVos = new ArrayList<VmInfoVO>();
		// List<VpdcReference> referenceList = null;
		List<Object> ids = null;
		String condition = "name";
		if ("ipOuter".equals(field)) {// 条件为外网IP
			condition = "vm_outerIP";
		} else if ("ipInner".equals(field)) {// 条件为内网IP
			condition = "vm_innerIP";
		} else if ("vmId".equals(field)) {
			condition = "vmId";
		}
		if (admin.getIsSuper() || roleService.isSpecialAdmin(admin.getId())) {// 当admin为超级管理员
			ids = null;
		} else {// 当admin为普通管理员
			ids = new ArrayList<Object>();
			if (referenceIds == null) {
				List<Domain> domainList = domainService.getDomainByAdmin(admin
						.getId());
				if (domainList != null && domainList.size() > 0) {
					List<Long> domainIds = new ArrayList<Long>();
					for (Domain domainLocal : domainList) {
						domainIds.add(domainLocal.getId());
					}
					List<Long> userList = userService
							.getUserIdsByDomainIds(domainIds);
					ids = resourceService.getVMResourceKeyList(resourceType,
							userList);
				}
				ids = vpdcDao.findReferencesByType(ids, type, status_buss);
			} else {
				ids = referenceIds;
			}
		}
		page = vpdcDao.findRecyclingVmsAdmin(page, type, status_buss,
				condition, value, ids, sort);
		this.fillDataOfVmInfoVO(page);
		// page.setTotalCount(referencePage.getTotalCount());
		// referenceList = referencePage.getResult();
		// loadAdminReferences(instanceAdminVos, referenceList);
		// page.setResult(instanceAdminVos);
		return page;
	}

	public Page<ImageVO> fuzzyFindImages(String name, String value,
			Page<ImageVO> page) {
		List<Image> imageList = openstackUtil.getCompute().images().get()
				.getList();
		List<ImageVO> imageVOList = new ArrayList<ImageVO>();
		for (Image image : imageList) {
			ImageVO imageVO = new ImageVO();
			BeanUtils.copyProperties(image, imageVO);
			imageVOList.add(imageVO);
		}
		page.setResult(imageVOList);
		return page;
	}

	public String applyForTryVm(CreateVmBean createVmBean, long userId,
			String defaultZone) throws HsCloudException {
		if (createVmBean == null) {
			return null;
		}
		createVmBean.setName(UUID.randomUUID().toString());
		if (StringUtils.isEmpty(createVmBean.getVmZone())) {
			createVmBean.setVmZone(defaultZone);
		}

		String heardDomainIdDate = vpdcDao.getDomainIdByUserId(userId);
		String serialNum = vpdcDao
				.getVMNameSerialNumber(Constants.T_VM_ARRANGING_NAME);
		createVmBean.setName(heardDomainIdDate + serialNum);

		// 装载试用申请云主机的vpdcReference
		VpdcReference vrBean = createVmReferenceBeanForTry(createVmBean, userId);
		// create VpdcReference、VpdcInstance(可用的status为0)
		vpdcDao.createVpdcReference(vrBean);
		VpdcReference vr = vpdcDao.findVpdcReferenceByVmName(createVmBean
				.getName());
		// resource添加记录信息
		permissionService.savePermission(ResourceType.VM.getEntityName(),
				Long.valueOf(createVmBean.getOwner()),
				String.valueOf(vr.getId()));
		// 初始化云主机使用周期记录（无起止日期，待审核后更新）
		updateReferencePeriod(vr.getId(), null, null);
		return vr.getName();
	}

	public String createVm(CreateVmBean createVmBean, long adminId,
			String defaultZone) throws HsCloudException {
		if (createVmBean == null) {
			return null;
		}
		if (StringUtils.isEmpty(createVmBean.getVmZone())) {
			createVmBean.setVmZone(defaultZone);
		}
		Admin admin = adminDao.get(adminId);
		String diskStr = "";
		String temp_vmId = "temp_" + UUID.randomUUID().toString();
		if (StringUtils.isEmpty(createVmBean.getName())) {
			createVmBean.setName(UUID.randomUUID().toString());
		}
		try {
			// create flavor
			FlavorVO flavorVO = new FlavorVO();
			flavorVO.setVcpus(StringUtils.isEmpty(createVmBean.getVcpus()) ? 0
					: Integer.valueOf(createVmBean.getVcpus()));
			flavorVO.setRam(StringUtils.isEmpty(createVmBean.getRam()) ? 0
					: Integer.valueOf(createVmBean.getRam()));
			flavorVO.setDisk(StringUtils.isEmpty(createVmBean.getDisk()) ? 0
					: Integer.valueOf(createVmBean.getDisk()));

			String flavorId = createFlavor(flavorVO);
			if (!StringUtils.isEmpty(flavorId)) {
				createVmBean.setFlavorId(flavorId);
			}
			// 装载VpdcReference
			VpdcReference vrBean = createVmReferenceBean(createVmBean, adminId,
					temp_vmId, Constants.ADMIN);

			String optinalZone = createVmBean.getVmZone();
			if (!StringUtils.isEmpty(createVmBean.getVmNode())) {
				optinalZone += "$" + createVmBean.getVmNode();
			}

			// optinalZone =
			// this.getOptimalZoneOfZones(createVmBean.getVmZone());

			if (!StringUtils.isEmpty(optinalZone)) {
				vrBean.setVmZone(optinalZone);
			} else {
				throw new HsCloudException(Constants.SYSTEM_BUSY_NOW,
						"the system is busy, try it again later!", log, null);
			}

			// create VpdcReference、VpdcInstance(可用的status为0)
			vpdcDao.createVpdcReference(vrBean);
			VpdcInstance inst = vpdcDao.findVmByVmId(temp_vmId);
			VpdcReference vr = inst.getVpdcreference();
			// 添加扩展盘关系
			String[] exttendDisks = createVmBean.getAddDisk() == null ? null
					: createVmBean.getAddDisk().split(",");
			SimpleDateFormat extDiskFormat = new SimpleDateFormat(
					"yyyyMMddHHmmss");
			if (exttendDisks != null && exttendDisks.length > 0) {
				String eDiskName = "";
				for (int j = 0; j < exttendDisks.length; j++) {
					if (!"".equals(exttendDisks[j].toString())) {
						eDiskName = extDiskFormat.format(new Date());
						VpdcReference_extdisk vied = new VpdcReference_extdisk();
						vied.setName(eDiskName + j);
						vied.setVmId(temp_vmId);
						vied.setEd_capacity(Integer.parseInt(exttendDisks[j]));
						vied.setEd_reference(vr);
						Date d = new Date();
						vied.setCreateDate(d);
						vied.setUpdateDate(d);
						vpdcDao.saveExtDisk(vied);
						diskStr += (vied.getName() + ":"
								+ vied.getEd_capacity() + ";");
					}
				}
			}
			// 添加IP关联
			if (!StringUtils.isEmpty(createVmBean.getFloating_ip())) {
				vpdcDao.updateIpDetailByIp(inst.getId(), 0, admin.getId(), 0,
						IPConvert.getIntegerIP(createVmBean.getFloating_ip()));
			}
			// 更新云主机使用周期(后台admin创建VM只有起始时间)
			updateReferencePeriod(vr.getId(), new Date(), null);
			// resource添加记录信息
			permissionService.savePermission(ResourceType.VM.getEntityName(),
					Long.valueOf(createVmBean.getOwner()),
					String.valueOf(vr.getId()));
			// 封装创建VM实体bean
			NovaServerForCreate serverForCreate = new NovaServerForCreate();
			serverForCreate.setJobExt(temp_vmId);
			serverForCreate.setName(createVmBean.getName());
			// 如果传给openstack的VM的name有中文，则赋予referenceID-instanceID作为其name（openstack不识别中文）
			if (CharUtil.isChineseByREG(createVmBean.getName())) {
				serverForCreate.setName(vr.getId() + "-" + inst.getId());
			}
			serverForCreate.setFlavorRef(createVmBean.getFlavorId());
			serverForCreate.setImageRef(createVmBean.getImageId());
			if (!StringUtils.isEmpty(vr.getVmZone())) {
				serverForCreate.setZone(vr.getVmZone());
			}
			serverForCreate.setAdminPassword(vr.getRadom_user() + "/"
					+ vr.getRadom_pwd());
			serverForCreate.setReferenceId(String.valueOf(vr.getId()));
			serverForCreate.setObjInstanceId(String.valueOf(inst.getId()));
			serverForCreate.setFloatingIp(createVmBean.getFloating_ip());
			if (diskStr.endsWith(";")) {
				diskStr = diskStr.substring(0, diskStr.length() - 1);
			}
			serverForCreate.setDisksize(diskStr);
			// 资源隔离设置
			SecurityRules sr = new SecurityRules();
			SecurityRules.BandwidthRule br = new SecurityRules.BandwidthRule();
			br.setBwtIn(createVmBean.getBwtIn());
			br.setBwtOut(createVmBean.getBwtOut());
			SecurityRules.ConnlimitRule cr = new SecurityRules.ConnlimitRule();
			cr.setIpIn(createVmBean.getIpConnIn());
			cr.setIpOut(createVmBean.getIpConnOut());
			cr.setTcpIn(createVmBean.getTcpConnIn());
			cr.setTcpOut(createVmBean.getTcpConnOut());
			cr.setUdpIn(createVmBean.getUdpConnIn());
			cr.setUdpOut(createVmBean.getUdpConnOut());
			sr.setBandwidthRule(br);
			sr.setConnlimitRule(cr);
			serverForCreate.setSecurityRules(sr);
			// 调用RebbitMQ
			HcEventResource her = new HcEventResource();
			her.setEvent_time(new Date());
			her.setType(LogResourceOperationType.ADD.getIndex());// 新增
			her.setRes_type(LogResourceType.VM.getIndex());// 操作VM
			her.setBiz_type(LogBizType.ADMIN_CREATE.getIndex());// 管理员创建
			her.setObj_name(vr.getName());
			her.setObj_id(vr.getId());
			her.setObj_instance_id(inst.getId());// 传instanceID以确定更新消息对应唯一instance
			her.setOperator(admin.getEmail());
			her.setOperator_id(admin.getId());
			her.setOperator_type(LogOperatorType.ADMIN.getIndex());// 后台操作
			rabbitMqUtil.sendMessage(Constants.JOBSERVER_METHOD_CrVMAdmin,
					serverForCreate, her, "HcEventResource");
		} catch (Exception e) {
			if (e.getClass() == new HsCloudException().getClass()) {
				HsCloudException ex = (HsCloudException) e;
				throw new HsCloudException(ex.getCode(), "createVm-Admin异常",
						log, e);
			} else {
				throw new HsCloudException(Constants.VM_CREATE_ADMIN_ERROR,
						"createVm-Admin异常", log, e);
			}
		}
		return temp_vmId;
	}

	public Map<NovaServerForCreate, HcEventResource> appCreateVm(
			List<CreateVmBean> createVmBeans, int userId, int supplierId)
			throws HsCloudException {
		if (createVmBeans == null) {
			return null;
		}
		String[] vmIds = new String[createVmBeans.size()];
		int i = 0;
		String rabbitMQMethod = null;
		Map<NovaServerForCreate, HcEventResource> rabbitMQVM = new HashMap<NovaServerForCreate, HcEventResource>();
		try {
			User user = userService.getUser((long) supplierId);
			String diskStr = "";
			String temp_vmId = "";
			String optinalZone = createVmBeans.get(0).getVmZone();
			String[] zones = optinalZone.split(",");

			// 根据zone code获取空闲IP数是否符合 getIPCountByZoneCode
			// 根据可用节点数*3(倍数，配置文件设置)的值 - 根据zone code查询创建中的vm >= 要创建的VM数
			// 根据前两个条件过滤出的zone，选择cpu loadaverage最低的进行创建(如果没有提示：系统忙，稍后再试)
			// 1.获取sc中的zoneList 2.遍历每个zone，选出最优zone 3. 将最优zone放置到creatvmbean中
			String zone = this.getOptimalZoneOfZones(zones, vmIds.length);
			if (StringUtils.isNotEmpty(zone)) {
				for (CreateVmBean createVmBean : createVmBeans) {
					if (!StringUtils.isEmpty(diskStr)) {
						diskStr = "";
					}
					createVmBean.setName(UUID.randomUUID().toString());
					temp_vmId = "temp_" + UUID.randomUUID().toString();
					vmIds[i] = temp_vmId;
					i++;
					// 装载VpdcReference
					VpdcReference vrBean = AppCreateVmReferenceBean(
							createVmBean, (long) userId, (long) supplierId,
							temp_vmId, Constants.USER);
					// 设置最优zone
					vrBean.setVmZone(zone);
					// create VpdcReference、VpdcInstance(可用的status为0)
					vpdcDao.createVpdcReference(vrBean);
					VpdcInstance inst = vpdcDao.findVmByVmId(temp_vmId);
					VpdcReference vr = inst.getVpdcreference();
					HcEventResource her = new HcEventResource();
					her.setEvent_time(new Date());
					her.setType(LogResourceOperationType.ADD.getIndex());// 新增
					if (VMTypeEnum.REGULAR.getCode() == vr.getVm_type()) {
						her.setBiz_type(LogBizType.ORDER_CREATE.getIndex());// 订单创建
						rabbitMQMethod = Constants.JOBSERVER_METHOD_CrVMBuy;
						// 更新云主机使用周期
						updateReferencePeriod(vr.getId(),
								createVmBean.getStart_time(),
								createVmBean.getEnd_time());
						this.saveVmPeriodLog(Constants.VM_PERIOD_LOG_BUY,
								vr.getId(), createVmBean.getEnd_time(),
								createVmBean.getEnd_time());
					}
					// resource添加记录信息
					permissionService.savePermission(
							ResourceType.VM.getEntityName(), supplierId,
							String.valueOf(vr.getId()));

					// 封装创建VM实体bean
					NovaServerForCreate serverForCreate = new NovaServerForCreate();
					serverForCreate.setJobExt(temp_vmId);
					serverForCreate.setName(createVmBean.getName());
					serverForCreate.setFlavorRef(createVmBean.getFlavorId());
					serverForCreate.setImageRef(createVmBean.getImageId());
					if (!StringUtils.isEmpty(vr.getVmZone())) {
						serverForCreate.setZone(vr.getVmZone());
					}
					serverForCreate.setAdminPassword(vr.getRadom_user() + "/"
							+ vr.getRadom_pwd());
					serverForCreate.setReferenceId(String.valueOf(vr.getId()));
					serverForCreate.setObjInstanceId(String.valueOf(inst
							.getId()));
					serverForCreate
							.setFloatingIp(createVmBean.getFloating_ip());
					serverForCreate.setDisksize(diskStr);
					// 资源隔离设置
					SecurityRules sr = new SecurityRules();
					SecurityRules.BandwidthRule br = new SecurityRules.BandwidthRule();
					br.setBwtIn(createVmBean.getBwtIn());
					br.setBwtOut(createVmBean.getBwtOut());
					SecurityRules.ConnlimitRule cr = new SecurityRules.ConnlimitRule();
					cr.setIpIn(createVmBean.getIpConnIn());
					cr.setIpOut(createVmBean.getIpConnOut());
					cr.setTcpIn(createVmBean.getTcpConnIn());
					cr.setTcpOut(createVmBean.getTcpConnOut());
					cr.setUdpIn(createVmBean.getUdpConnIn());
					cr.setUdpOut(createVmBean.getUdpConnOut());
					sr.setBandwidthRule(br);
					sr.setConnlimitRule(cr);
					serverForCreate.setSecurityRules(sr);
					// 调用RebbitMQ
					her.setRes_type(LogResourceType.VM.getIndex());// 操作VM
					her.setNeedIP((short) 0);// 需要RabbitMQ获取IP
					her.setObj_id(vr.getId());
					her.setObj_name(vr.getName());
					her.setObj_instance_id(inst.getId());// 传instanceID以确定更新消息对应唯一instance
					her.setOperator(user.getEmail());
					her.setOwner_email(user.getEmail());
					her.setDomain_id(user.getDomain().getId());
					her.setOperator_id(user.getId());
					her.setOperator_type(LogOperatorType.USER.getIndex());
					// 借用message属性存储方法，发消息时再将该属性值置空。
					her.setMessage(rabbitMQMethod);
					rabbitMQVM.put(serverForCreate, her);
				}
			} else {
				throw new HsCloudException(Constants.SYSTEM_BUSY_NOW,
						"createVm——getzone异常", log);
			}

		} catch (Exception e) {
			if (e.getClass() == new HsCloudException().getClass()) {
				HsCloudException ex = (HsCloudException) e;
				throw new HsCloudException(ex.getCode(), "createVm-User异常",
						log, e);
			} else {
				throw new HsCloudException(Constants.VM_CREATE_USER_ERROR,
						"createVm——User异常", log, e);
			}
		}
		return rabbitMQVM;
	}

	// 云应用创建虚拟机
	public Map<NovaServerForCreate, HcEventResource> createVm(
			List<CreateVmBean> createVmBeans, String userId, String defaultZone)
			throws HsCloudException {
		if (createVmBeans == null) {
			return null;
		}
		String[] vmIds = new String[createVmBeans.size()];
		int i = 0;
		String rabbitMQMethod = null;
		Map<NovaServerForCreate, HcEventResource> rabbitMQVM = new HashMap<NovaServerForCreate, HcEventResource>();
		try {
			User user = userService.getUser(Long.parseLong(userId));
			String diskStr = "";
			VpdcReference_OrderItem vroi = null;
			String temp_vmId = "";
			String optinalZone = createVmBeans.get(0).getVmZone();
			String[] zones = optinalZone.split(",");

			// 根据zone code获取空闲IP数是否符合 getIPCountByZoneCode
			// 根据可用节点数*3(倍数，配置文件设置)的值 - 根据zone code查询创建中的vm >= 要创建的VM数
			// 根据前两个条件过滤出的zone，选择cpu loadaverage最低的进行创建(如果没有提示：系统忙，稍后再试)
			// 1.获取sc中的zoneList 2.遍历每个zone，选出最优zone 3. 将最优zone放置到creatvmbean中
			String zone = this.getOptimalZoneOfZones(zones, vmIds.length);
			// zoneIps = this.getIPCountByzone(zones);
			// String jsonStr = RedisUtil.getValue("ZoneAcquisition_"+
			// optinalZone);
			// String cpuWorkload = RedisUtil.getValue("	 "+ optinalZone);
			if (StringUtils.isNotEmpty(zone)) {
				for (CreateVmBean createVmBean : createVmBeans) {
					if (!StringUtils.isEmpty(diskStr)) {
						diskStr = "";
					}
					createVmBean.setName(UUID.randomUUID().toString());
					if (StringUtils.isEmpty(createVmBean.getVmZone())) {
						createVmBean.setVmZone(defaultZone);
					}
					// createVmBean.getVmZone();
					temp_vmId = "temp_" + UUID.randomUUID().toString();
					vmIds[i] = temp_vmId;
					i++;
					// 装载VpdcReference
					VpdcReference vrBean = createVmReferenceBean(createVmBean,
							Long.parseLong(userId), temp_vmId, Constants.USER);
					// 设置最优zone
					vrBean.setVmZone(zone);
					// create VpdcReference、VpdcInstance(可用的status为0)
					vpdcDao.createVpdcReference(vrBean);
					VpdcInstance inst = vpdcDao.findVmByVmId(temp_vmId);
					VpdcReference vr = inst.getVpdcreference();
					// 添加扩展盘关系
					String[] exttendDisks = createVmBean.getAddDisk() == null ? null
							: createVmBean.getAddDisk().split(",");
					SimpleDateFormat extDiskFormat = new SimpleDateFormat(
							"yyyyMMddHHmmss");
					if (exttendDisks != null && exttendDisks.length > 0) {
						String eDiskName = "";
						for (int j = 0; j < exttendDisks.length; j++) {
							if (!"".equals(exttendDisks[j].toString())) {
								eDiskName = extDiskFormat.format(new Date());
								VpdcReference_extdisk vied = new VpdcReference_extdisk();
								vied.setName(eDiskName + j);
								vied.setVmId(temp_vmId);
								vied.setInitVmId(temp_vmId);
								vied.setEd_capacity(Integer
										.parseInt(exttendDisks[j]));
								vied.setEd_reference(vr);
								Date d = new Date();
								vied.setCreateDate(d);
								vied.setUpdateDate(d);
								vpdcDao.saveExtDisk(vied);
								diskStr += (vied.getName() + ":"
										+ vied.getEd_capacity() + ";");
							}
						}
					}
					HcEventResource her = new HcEventResource();
					her.setEvent_time(new Date());
					her.setType(LogResourceOperationType.ADD.getIndex());// 新增
					if (VMTypeEnum.TRY.getCode() == vr.getVm_type()) {
						her.setBiz_type(LogBizType.TRIAL_CREATE.getIndex());// 试用创建
						rabbitMQMethod = Constants.JOBSERVER_METHOD_CrVMTry;
						Date d = new Date();
						long times = d.getTime() + createVmBean.getTryLong()
								* 24 * 3600 * 1000L;
						updateReferencePeriod(vr.getId(), d, new Date(times));
						this.saveVmPeriodLog(
								Constants.VM_PERIOD_LOG_TRYNOVERIFY,
								vr.getId(), new Date(times), new Date(times));
					} else if (VMTypeEnum.REGULAR.getCode() == vr.getVm_type()) {
						her.setBiz_type(LogBizType.ORDER_CREATE.getIndex());// 订单创建
						rabbitMQMethod = Constants.JOBSERVER_METHOD_CrVMBuy;
						// add VpdcReference和OrderItem关系
						vroi = new VpdcReference_OrderItem();
						vroi.setVpdcRenferenceId(vr.getId());
						vroi.setOrder_item_id(createVmBean.getOrder_item_id());
						vpdcDao.saveReferenceOrderItem(vroi);
						// 更新云主机使用周期
						updateReferencePeriod(vr.getId(),
								createVmBean.getStart_time(),
								createVmBean.getEnd_time());
						this.saveVmPeriodLog(Constants.VM_PERIOD_LOG_BUY,
								vr.getId(), createVmBean.getEnd_time(),
								createVmBean.getEnd_time());
					}
					// resource添加记录信息
					permissionService.savePermission(
							ResourceType.VM.getEntityName(),
							Long.valueOf(createVmBean.getOwner()),
							String.valueOf(vr.getId()));

					// 封装创建VM实体bean
					NovaServerForCreate serverForCreate = new NovaServerForCreate();
					serverForCreate.setJobExt(temp_vmId);
					serverForCreate.setName(createVmBean.getName());
					serverForCreate.setFlavorRef(createVmBean.getFlavorId());
					serverForCreate.setImageRef(createVmBean.getImageId());
					if (!StringUtils.isEmpty(vr.getVmZone())) {
						serverForCreate.setZone(vr.getVmZone());
					}
					serverForCreate.setAdminPassword(vr.getRadom_user() + "/"
							+ vr.getRadom_pwd());
					serverForCreate.setReferenceId(String.valueOf(vr.getId()));
					serverForCreate.setObjInstanceId(String.valueOf(inst
							.getId()));
					serverForCreate
							.setFloatingIp(createVmBean.getFloating_ip());
					if (diskStr.endsWith(";")) {
						diskStr = diskStr.substring(0, diskStr.length() - 1);
					}
					serverForCreate.setDisksize(diskStr);
					// 资源隔离设置
					SecurityRules sr = new SecurityRules();
					SecurityRules.BandwidthRule br = new SecurityRules.BandwidthRule();
					br.setBwtIn(createVmBean.getBwtIn());
					br.setBwtOut(createVmBean.getBwtOut());
					SecurityRules.ConnlimitRule cr = new SecurityRules.ConnlimitRule();
					cr.setIpIn(createVmBean.getIpConnIn());
					cr.setIpOut(createVmBean.getIpConnOut());
					cr.setTcpIn(createVmBean.getTcpConnIn());
					cr.setTcpOut(createVmBean.getTcpConnOut());
					cr.setUdpIn(createVmBean.getUdpConnIn());
					cr.setUdpOut(createVmBean.getUdpConnOut());
					sr.setBandwidthRule(br);
					sr.setConnlimitRule(cr);
					serverForCreate.setSecurityRules(sr);
					// 调用RebbitMQ
					her.setRes_type(LogResourceType.VM.getIndex());// 操作VM
					her.setNeedIP((short) 0);// 需要RabbitMQ获取IP
					her.setObj_id(vr.getId());
					her.setObj_name(vr.getName());
					her.setObj_instance_id(inst.getId());// 传instanceID以确定更新消息对应唯一instance
					her.setOperator(user.getEmail());
					her.setOwner_email(user.getEmail());
					her.setDomain_id(user.getDomain().getId());
					her.setOperator_id(user.getId());
					her.setOperator_type(LogOperatorType.USER.getIndex());
					// 借用message属性存储方法，发消息时再将该属性值置空。
					her.setMessage(rabbitMQMethod);
					rabbitMQVM.put(serverForCreate, her);
				}
			} else {
				throw new HsCloudException(Constants.SYSTEM_BUSY_NOW,
						"createVm——getzone异常", log);
			}

		} catch (Exception e) {
			if (e.getClass() == new HsCloudException().getClass()) {
				HsCloudException ex = (HsCloudException) e;
				throw new HsCloudException(ex.getCode(), "createVm-User异常",
						log, e);
			} else {
				throw new HsCloudException(Constants.VM_CREATE_USER_ERROR,
						"createVm——User异常", log, e);
			}
		}
		return rabbitMQVM;
	}

	@Override
	public Map<NovaServerForCreate, HcEventResource> createVmByAPI(
			List<CreateVmBean> createVmBeans, String userId,
			String defaultZone, String id, String accessId)
			throws HsCloudException {
		if (createVmBeans == null) {
			return null;
		}
		String[] vmIds = new String[createVmBeans.size()];
		int i = 0;
		String rabbitMQMethod = null;
		Map<NovaServerForCreate, HcEventResource> rabbitMQVM = new HashMap<NovaServerForCreate, HcEventResource>();
		try {
			User user = userService.getUser(Long.parseLong(userId));
			String diskStr = "";
			VpdcReference_OrderItem vroi = null;
			String temp_vmId = "";

			for (CreateVmBean createVmBean : createVmBeans) {
				if (!StringUtils.isEmpty(diskStr)) {
					diskStr = "";
				}
				createVmBean.setName(UUID.randomUUID().toString());
				if (StringUtils.isEmpty(createVmBean.getVmZone())) {
					createVmBean.setVmZone(defaultZone);
				}
				temp_vmId = "temp_" + UUID.randomUUID().toString();
				vmIds[i] = temp_vmId;
				i++;
				// 装载VpdcReference
				VpdcReference vrBean = createVmReferenceBean(createVmBean,
						Long.parseLong(userId), temp_vmId, Constants.USER);
				// create VpdcReference、VpdcInstance(可用的status为0)
				vpdcDao.createVpdcReference(vrBean);

				VpdcInstance inst = vpdcDao.findVmByVmId(temp_vmId);
				VpdcReference vr = inst.getVpdcreference();
				// 添加扩展盘关系
				String[] exttendDisks = createVmBean.getAddDisk() == null ? null
						: createVmBean.getAddDisk().split(",");
				SimpleDateFormat extDiskFormat = new SimpleDateFormat(
						"yyyyMMddHHmmss");
				if (exttendDisks != null && exttendDisks.length > 0) {
					String eDiskName = "";
					for (int j = 0; j < exttendDisks.length; j++) {
						if (!"".equals(exttendDisks[j].toString())) {
							eDiskName = extDiskFormat.format(new Date());
							VpdcReference_extdisk vied = new VpdcReference_extdisk();
							vied.setName(eDiskName + j);
							vied.setVmId(temp_vmId);
							vied.setInitVmId(temp_vmId);
							vied.setEd_capacity(Integer
									.parseInt(exttendDisks[j]));
							vied.setEd_reference(vr);
							Date d = new Date();
							vied.setCreateDate(d);
							vied.setUpdateDate(d);
							vpdcDao.saveExtDisk(vied);
							diskStr += (vied.getName() + ":"
									+ vied.getEd_capacity() + ";");
						}
					}
				}
				HcEventResource her = new HcEventResource();
				her.setEvent_time(new Date());
				her.setType(LogResourceOperationType.ADD.getIndex());// 新增
				if (VMTypeEnum.TRY.getCode() == vr.getVm_type()) {
					her.setBiz_type(LogBizType.TRIAL_CREATE.getIndex());// 试用创建
					rabbitMQMethod = Constants.JOBSERVER_METHOD_CrVMTry;
					Date d = new Date();
					long times = d.getTime() + createVmBean.getTryLong() * 24
							* 3600 * 1000L;
					updateReferencePeriod(vr.getId(), d, new Date(times));
					this.saveVmPeriodLog(Constants.VM_PERIOD_LOG_TRYNOVERIFY,
							vr.getId(), new Date(times), new Date(times));
				} else if (VMTypeEnum.REGULAR.getCode() == vr.getVm_type()) {
					her.setBiz_type(LogBizType.ORDER_CREATE.getIndex());// 订单创建
					rabbitMQMethod = Constants.JOBSERVER_METHOD_CrVMBuy;
					// add VpdcReference和OrderItem关系
					vroi = new VpdcReference_OrderItem();
					vroi.setVpdcRenferenceId(vr.getId());
					vroi.setOrder_item_id(createVmBean.getOrder_item_id());
					vpdcDao.saveReferenceOrderItem(vroi);
					// 更新云主机使用周期
					updateReferencePeriod(vr.getId(),
							createVmBean.getStart_time(),
							createVmBean.getEnd_time());
					this.saveVmPeriodLog(Constants.VM_PERIOD_LOG_BUY,
							vr.getId(), createVmBean.getEnd_time(),
							createVmBean.getEnd_time());
				}
				// resource添加记录信息
				permissionService.savePermission(
						ResourceType.VM.getEntityName(),
						Long.valueOf(createVmBean.getOwner()),
						String.valueOf(vr.getId()));
				// 封装创建VM实体bean
				NovaServerForCreate serverForCreate = new NovaServerForCreate();
				serverForCreate.setJobExt(temp_vmId);
				serverForCreate.setName(createVmBean.getName());
				serverForCreate.setFlavorRef(createVmBean.getFlavorId());
				serverForCreate.setImageRef(createVmBean.getImageId());
				if (!StringUtils.isEmpty(vr.getVmZone())) {
					serverForCreate.setZone(vr.getVmZone());
				}
				serverForCreate.setAdminPassword(vr.getRadom_user() + "/"
						+ vr.getRadom_pwd());
				serverForCreate.setReferenceId(String.valueOf(vr.getId()));
				serverForCreate.setObjInstanceId(String.valueOf(inst.getId()));
				serverForCreate.setFloatingIp(createVmBean.getFloating_ip());
				if (diskStr.endsWith(";")) {
					diskStr = diskStr.substring(0, diskStr.length() - 1);
				}
				serverForCreate.setDisksize(diskStr);
				// 资源隔离设置
				SecurityRules sr = new SecurityRules();
				SecurityRules.BandwidthRule br = new SecurityRules.BandwidthRule();
				br.setBwtIn(createVmBean.getBwtIn());
				br.setBwtOut(createVmBean.getBwtOut());
				SecurityRules.ConnlimitRule cr = new SecurityRules.ConnlimitRule();
				cr.setIpIn(createVmBean.getIpConnIn());
				cr.setIpOut(createVmBean.getIpConnOut());
				cr.setTcpIn(createVmBean.getTcpConnIn());
				cr.setTcpOut(createVmBean.getTcpConnOut());
				cr.setUdpIn(createVmBean.getUdpConnIn());
				cr.setUdpOut(createVmBean.getUdpConnOut());
				sr.setBandwidthRule(br);
				sr.setConnlimitRule(cr);
				serverForCreate.setSecurityRules(sr);
				// 调用RebbitMQ
				her.setRes_type(LogResourceType.VM.getIndex());// 操作VM
				her.setNeedIP((short) 0);// 需要RabbitMQ获取IP
				her.setObj_id(vr.getId());
				her.setObj_name(vr.getName());
				her.setObj_instance_id(inst.getId());
				her.setOperator(accessId);
				her.setOwner_email(user.getEmail());
				her.setDomain_id(user.getDomain().getId());
				her.setOperator_id(Long.valueOf(id));
				her.setOperator_type(LogOperatorType.API.getIndex());
				// 借用message属性存储方法，发消息时再将该属性值置空。
				her.setMessage(rabbitMQMethod);
				rabbitMQVM.put(serverForCreate, her);
			}
		} catch (Exception e) {
			if (e.getClass() == new HsCloudException().getClass()) {
				HsCloudException ex = (HsCloudException) e;
				throw new HsCloudException(ex.getCode(), "createVm-API异常", log,
						e);
			} else {
				throw new HsCloudException(Constants.VM_CREATE_USER_ERROR,
						"createVm-API异常", log, e);
			}
		}
		return rabbitMQVM;
	}

	public Map<NovaServerForCreate, HcEventResource> createVmInNoRouterVPDC(
			List<CreateVmBean> createVmBeans, String userId, String defaultZone)
			throws HsCloudException {
		if (createVmBeans == null) {
			return null;
		}
		String[] vmIds = new String[createVmBeans.size()];
		int i = 0;
		String rabbitMQMethod = null;
		Map<NovaServerForCreate, HcEventResource> rabbitMQVM = new HashMap<NovaServerForCreate, HcEventResource>();
		try {
			User user = userService.getUser(Long.parseLong(userId));
			String diskStr = "";
			VpdcReference_OrderItem vroi = null;
			String temp_vmId = "";

			for (CreateVmBean createVmBean : createVmBeans) {
				if (!StringUtils.isEmpty(diskStr)) {
					diskStr = "";
				}
				createVmBean.setName(UUID.randomUUID().toString());
				if (StringUtils.isEmpty(createVmBean.getVmZone())) {
					createVmBean.setVmZone(defaultZone);
				}
				temp_vmId = "temp_" + UUID.randomUUID().toString();
				vmIds[i] = temp_vmId;
				i++;
				// 装载VpdcReference
				VpdcReference vrBean = createVmReferenceBean(createVmBean,
						Long.parseLong(userId), temp_vmId, Constants.USER);
				// create VpdcReference、VpdcInstance(可用的status为0)
				vpdcDao.createVpdcReference(vrBean);
				VpdcInstance inst = vpdcDao.findVmByVmId(temp_vmId);
				VpdcReference vr = inst.getVpdcreference();
				// 添加扩展盘关系
				String[] exttendDisks = createVmBean.getAddDisk() == null ? null
						: createVmBean.getAddDisk().split(",");
				SimpleDateFormat extDiskFormat = new SimpleDateFormat(
						"yyyyMMddHHmmss");
				if (exttendDisks != null && exttendDisks.length > 0) {
					String eDiskName = "";
					for (int j = 0; j < exttendDisks.length; j++) {
						if (!"".equals(exttendDisks[j].toString())) {
							eDiskName = extDiskFormat.format(new Date());
							VpdcReference_extdisk vied = new VpdcReference_extdisk();
							vied.setName(eDiskName + j);
							vied.setVmId(temp_vmId);
							vied.setInitVmId(temp_vmId);
							vied.setEd_capacity(Integer
									.parseInt(exttendDisks[j]));
							vied.setEd_reference(vr);
							Date d = new Date();
							vied.setCreateDate(d);
							vied.setUpdateDate(d);
							vpdcDao.saveExtDisk(vied);
							diskStr += (vied.getName() + ":"
									+ vied.getEd_capacity() + ";");
						}
					}
				}
				HcEventResource her = new HcEventResource();
				her.setEvent_time(new Date());
				her.setType(LogResourceOperationType.ADD.getIndex());// 新增
				her.setBiz_type(LogBizType.ORDER_CREATE.getIndex());// 订单创建
				rabbitMQMethod = Constants.JOBSERVER_METHOD_CrVMNoRouterVPDC;
				// add VpdcReference和OrderItem关系
				vroi = new VpdcReference_OrderItem();
				vroi.setVpdcRenferenceId(vr.getId());
				vroi.setOrder_item_id(createVmBean.getOrder_item_id());
				vpdcDao.saveReferenceOrderItem(vroi);
				// 更新云主机使用周期
				updateReferencePeriod(vr.getId(), createVmBean.getStart_time(),
						createVmBean.getEnd_time());
				this.saveVmPeriodLog(Constants.VM_PERIOD_LOG_BUY, vr.getId(),
						createVmBean.getEnd_time(), createVmBean.getEnd_time());
				// resource添加记录信息
				permissionService.savePermission(
						ResourceType.VM.getEntityName(),
						Long.valueOf(createVmBean.getOwner()),
						String.valueOf(vr.getId()));
				// 封装创建VM实体bean
				NovaServerForCreate serverForCreate = new NovaServerForCreate();
				serverForCreate.setJobExt(temp_vmId);
				serverForCreate.setName(createVmBean.getName());
				serverForCreate.setFlavorRef(createVmBean.getFlavorId());
				serverForCreate.setImageRef(createVmBean.getImageId());
				if (!StringUtils.isEmpty(vr.getVmZone())) {
					serverForCreate.setZone(vr.getVmZone());
				}
				serverForCreate.setAdminPassword(vr.getRadom_user() + "/"
						+ vr.getRadom_pwd());
				serverForCreate.setReferenceId(String.valueOf(vr.getId()));
				serverForCreate.setObjInstanceId(String.valueOf(inst.getId()));
				serverForCreate.setFloatingIp(createVmBean.getFloating_ip());
				if (diskStr.endsWith(";")) {
					diskStr = diskStr.substring(0, diskStr.length() - 1);
				}
				serverForCreate.setDisksize(diskStr);
				// 资源隔离设置
				SecurityRules sr = new SecurityRules();
				SecurityRules.BandwidthRule br = new SecurityRules.BandwidthRule();
				br.setBwtIn(createVmBean.getBwtIn());
				br.setBwtOut(createVmBean.getBwtOut());
				SecurityRules.ConnlimitRule cr = new SecurityRules.ConnlimitRule();
				cr.setIpIn(createVmBean.getIpConnIn());
				cr.setIpOut(createVmBean.getIpConnOut());
				cr.setTcpIn(createVmBean.getTcpConnIn());
				cr.setTcpOut(createVmBean.getTcpConnOut());
				cr.setUdpIn(createVmBean.getUdpConnIn());
				cr.setUdpOut(createVmBean.getUdpConnOut());
				sr.setBandwidthRule(br);
				sr.setConnlimitRule(cr);
				serverForCreate.setSecurityRules(sr);

				// VM绑定private LanNetwork
				List<NetworkLan> networks = new ArrayList<NetworkLan>();
				NetworkLan nl = new NetworkLan();
				nl.setNetworkId("default");// 注意：此参数固定只能传default值
				Vpdc vpdc = vpdcDao.getVpdcById(createVmBean.getVpdcId());
				nl.setLanId(vpdc.getLans().iterator().next().getLanId());
				networks.add(nl);
				serverForCreate.setNetworks(networks);

				// 调用RebbitMQ
				her.setRes_type(LogResourceType.VM.getIndex());// 操作VM
				her.setNeedIP((short) 0);// 需要RabbitMQ获取IP
				her.setObj_id(vr.getId());
				her.setObj_name(vr.getName());
				her.setObj_instance_id(inst.getId());
				her.setOperator(user.getEmail());
				her.setOwner_email(user.getEmail());
				her.setDomain_id(user.getDomain().getId());
				her.setOperator_id(user.getId());
				her.setOperator_type(LogOperatorType.USER.getIndex());
				// 借用message属性存储方法，发消息时再将该属性值置空。
				her.setMessage(rabbitMQMethod);
				rabbitMQVM.put(serverForCreate, her);
			}
		} catch (Exception e) {
			if (e.getClass() == new HsCloudException().getClass()) {
				HsCloudException ex = (HsCloudException) e;
				throw new HsCloudException(ex.getCode(),
						"createVmInNoRouterVPDC异常", log, e);
			} else {
				throw new HsCloudException(Constants.VM_CREATE_USER_ERROR,
						"createVmInNoRouterVPDC异常", log, e);
			}
		}
		return rabbitMQVM;
	}

	public Map<NovaServerForCreate, HcEventResource> createVmInRouterVPDC(
			List<CreateVmBean> createVmBeans, String userId, String defaultZone)
			throws HsCloudException {
		if (createVmBeans == null) {
			return null;
		}
		String[] vmIds = new String[createVmBeans.size()];
		int i = 0;
		String rabbitMQMethod = null;
		Map<NovaServerForCreate, HcEventResource> rabbitMQVM = new HashMap<NovaServerForCreate, HcEventResource>();
		try {
			User user = userService.getUser(Long.parseLong(userId));
			String diskStr = "";
			VpdcReference_OrderItem vroi = null;
			String temp_vmId = "";

			for (CreateVmBean createVmBean : createVmBeans) {
				if (!StringUtils.isEmpty(diskStr)) {
					diskStr = "";
				}
				createVmBean.setName(UUID.randomUUID().toString());
				if (StringUtils.isEmpty(createVmBean.getVmZone())) {
					createVmBean.setVmZone(defaultZone);
				}
				temp_vmId = "temp_" + UUID.randomUUID().toString();
				vmIds[i] = temp_vmId;
				i++;
				// 装载VpdcReference
				VpdcReference vrBean = createVmReferenceBean(createVmBean,
						Long.parseLong(userId), temp_vmId, Constants.USER);
				// create VpdcReference、VpdcInstance(可用的status为0)
				vpdcDao.createVpdcReference(vrBean);
				VpdcInstance inst = vpdcDao.findVmByVmId(temp_vmId);
				VpdcReference vr = inst.getVpdcreference();
				// 添加扩展盘关系
				String[] exttendDisks = createVmBean.getAddDisk() == null ? null
						: createVmBean.getAddDisk().split(",");
				SimpleDateFormat extDiskFormat = new SimpleDateFormat(
						"yyyyMMddHHmmss");
				if (exttendDisks != null && exttendDisks.length > 0) {
					String eDiskName = "";
					for (int j = 0; j < exttendDisks.length; j++) {
						if (!"".equals(exttendDisks[j].toString())) {
							eDiskName = extDiskFormat.format(new Date());
							VpdcReference_extdisk vied = new VpdcReference_extdisk();
							vied.setName(eDiskName + j);
							vied.setVmId(temp_vmId);
							vied.setInitVmId(temp_vmId);
							vied.setEd_capacity(Integer
									.parseInt(exttendDisks[j]));
							vied.setEd_reference(vr);
							Date d = new Date();
							vied.setCreateDate(d);
							vied.setUpdateDate(d);
							vpdcDao.saveExtDisk(vied);
							diskStr += (vied.getName() + ":"
									+ vied.getEd_capacity() + ";");
						}
					}
				}
				HcEventResource her = new HcEventResource();
				her.setEvent_time(new Date());
				her.setType(LogResourceOperationType.ADD.getIndex());// 新增
				her.setBiz_type(LogBizType.ORDER_CREATE.getIndex());// 订单创建
				rabbitMQMethod = Constants.JOBSERVER_METHOD_CrVMRouterVPDC;
				// add VpdcReference和OrderItem关系
				vroi = new VpdcReference_OrderItem();
				vroi.setVpdcRenferenceId(vr.getId());
				vroi.setOrder_item_id(createVmBean.getOrder_item_id());
				vpdcDao.saveReferenceOrderItem(vroi);
				// 更新云主机使用周期
				updateReferencePeriod(vr.getId(), createVmBean.getStart_time(),
						createVmBean.getEnd_time());
				this.saveVmPeriodLog(Constants.VM_PERIOD_LOG_BUY, vr.getId(),
						createVmBean.getEnd_time(), createVmBean.getEnd_time());
				// resource添加记录信息
				permissionService.savePermission(
						ResourceType.VM.getEntityName(),
						Long.valueOf(createVmBean.getOwner()),
						String.valueOf(vr.getId()));
				// 封装创建VM实体bean
				NovaServerForCreate serverForCreate = new NovaServerForCreate();
				serverForCreate.setJobExt(temp_vmId);
				serverForCreate.setName(createVmBean.getName());
				serverForCreate.setFlavorRef(createVmBean.getFlavorId());
				serverForCreate.setImageRef(createVmBean.getImageId());
				if (!StringUtils.isEmpty(vr.getVmZone())) {
					serverForCreate.setZone(vr.getVmZone());
				}
				serverForCreate.setAdminPassword(vr.getRadom_user() + "/"
						+ vr.getRadom_pwd());
				serverForCreate.setReferenceId(String.valueOf(vr.getId()));
				serverForCreate.setObjInstanceId(String.valueOf(inst.getId()));
				serverForCreate.setFloatingIp(createVmBean.getFloating_ip());
				if (diskStr.endsWith(";")) {
					diskStr = diskStr.substring(0, diskStr.length() - 1);
				}
				serverForCreate.setDisksize(diskStr);
				// 资源隔离设置
				SecurityRules sr = new SecurityRules();
				SecurityRules.BandwidthRule br = new SecurityRules.BandwidthRule();
				br.setBwtIn(createVmBean.getBwtIn());
				br.setBwtOut(createVmBean.getBwtOut());
				SecurityRules.ConnlimitRule cr = new SecurityRules.ConnlimitRule();
				cr.setIpIn(createVmBean.getIpConnIn());
				cr.setIpOut(createVmBean.getIpConnOut());
				cr.setTcpIn(createVmBean.getTcpConnIn());
				cr.setTcpOut(createVmBean.getTcpConnOut());
				cr.setUdpIn(createVmBean.getUdpConnIn());
				cr.setUdpOut(createVmBean.getUdpConnOut());
				sr.setBandwidthRule(br);
				sr.setConnlimitRule(cr);
				serverForCreate.setSecurityRules(sr);

				// VM绑定LanNetwork
				List<NetworkLan> networks = new ArrayList<NetworkLan>();
				NetworkLan nl = new NetworkLan();
				VpdcNetwork lanNetwork = getValidNetworkUUID(createVmBean
						.getVpdcId());
				if (lanNetwork == null) {
					log.error("VLan内网网段已占满，不能再添加VM");
					throw new HsCloudException(Constants.VLAN_CAPACITY_FULL,
							"createVmInRouterVPDC异常", log, null);
				}
				nl.setNetworkId(lanNetwork.getNetworkId());
				nl.setLanId(lanNetwork.getSecurytyVlan().getLanId());
				networks.add(nl);
				serverForCreate.setNetworks(networks);

				// 调用RebbitMQ
				her.setRes_type(LogResourceType.VM.getIndex());// 操作VM
				her.setObj_id(vr.getId());
				her.setObj_name(vr.getName());
				her.setObj_instance_id(inst.getId());
				her.setOperator(user.getEmail());
				her.setOwner_email(user.getEmail());
				her.setDomain_id(user.getDomain().getId());
				her.setOperator_id(user.getId());
				her.setOperator_type(LogOperatorType.USER.getIndex());
				// 借用message属性存储方法，发消息时再将该属性值置空。
				her.setMessage(rabbitMQMethod);
				rabbitMQVM.put(serverForCreate, her);
			}
		} catch (Exception e) {
			if (e.getClass() == new HsCloudException().getClass()) {
				HsCloudException ex = (HsCloudException) e;
				throw new HsCloudException(ex.getCode(),
						"createVmInRouterVPDC异常", log, e);
			} else {
				throw new HsCloudException(Constants.VM_CREATE_USER_ERROR,
						"createVmInRouterVPDC异常", log, e);
			}
		}
		return rabbitMQVM;
	}

	/**
	 * <在VPDC中查找可用的Vlan类型的NetworkUUID> <功能详细描述>
	 * 
	 * @param vpdcId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	private VpdcNetwork getValidNetworkUUID(Long vpdcId) {
		Vpdc vpdc = vpdcDao.getVpdcById(vpdcId);
		for (VpdcLan vl : vpdc.getLans()) {
			VpdcNetwork vnw = vl.getLanNetwork().iterator().next();
			int size = vnw.getNetworkSize();
			List<VpdcNetwork_Object> lvo = vpdcDao.findNetwork_Objects(vnw
					.getId());
			if (lvo.size() < size) {
				return vnw;
			}
		}
		return null;
	}

	public String publishVm(VpdcReference vr, String ip, Long adminId)
			throws HsCloudException {
		VpdcInstance instance = null;
		String extDisk = "";
		String diskStr = "";
		Date d = new Date();
		String ownerEmail = "";
		Long domainId = null;
		String newvmid = "temp_" + UUID.randomUUID().toString();
		try {
			Admin admin = adminDao.get(adminId);
			if (vr == null) {
				if (log.isDebugEnabled()) {
					log.debug("not fount the configration of the uuid ");
				}
				return null;
			}
			// 云主机在有实例情况下不能再次发布
			if (this.getActiveVmFromVpdcReference(vr) != null) {
				log.error("VM can't be published because of it has an instance!");
				throw new HsCloudException(Constants.VM_CREATE_ADMIN_ERROR,
						"publishVm异常", log, null);
			}
			// 添加扩展盘关系
			Set<VpdcReference_extdisk> svred = vr.getExtdisks();
			if (svred != null && svred.size() > 0) {
				String vmid = svred.iterator().next().getVmId();
				for (VpdcReference_extdisk vred : svred) {
					if (vmid.equals(vred.getVmId())) {
						extDisk += vred.getEd_capacity() + ",";
					}
				}
			}
			String[] exttendDisks = extDisk == null ? null : extDisk.split(",");
			SimpleDateFormat extDiskFormat = new SimpleDateFormat(
					"yyyyMMddHHmmss");
			if (exttendDisks != null && exttendDisks.length > 0) {
				String eDiskName = "";
				for (int j = 0; j < exttendDisks.length; j++) {
					if (!"".equals(exttendDisks[j].toString())) {
						eDiskName = extDiskFormat.format(new Date());
						VpdcReference_extdisk vied = new VpdcReference_extdisk();
						vied.setName(eDiskName + j);
						vied.setVmId(newvmid);
						vied.setEd_capacity(Integer.parseInt(exttendDisks[j]));
						vied.setEd_reference(vr);
						vied.setCreateDate(d);
						vied.setUpdateDate(d);
						vpdcDao.saveExtDisk(vied);
						diskStr += (vied.getName() + ":"
								+ vied.getEd_capacity() + ";");
					}
				}
			}
			// 添加新instance
			String imageId = vr.getImageId();
			String flavorId = vr.getFlavorId();
			instance = new VpdcInstance();
			instance.setVpdcreference(vr);
			instance.setCreateId(adminId);
			instance.setCreateDate(d);
			instance.setUpdateId(adminId);
			instance.setUpdateDate(d);
			Set<VpdcInstance> instances = new HashSet<VpdcInstance>();
			instance.setVmId(newvmid);
			instance.setInitVmId(newvmid);
			instance.setStatus(0);
			instances.add(instance);
			vr.setInstances(instances);
			vr.setVm_innerIP("");
			vr.setVm_outerIP("");
			vpdcDao.updateVpdcReference(vr);
			// 添加IP关联
			VpdcInstance inst = this.getActiveVmFromVpdcReference(vr);
			vpdcDao.updateIpDetailByIp(inst.getId(), 0, admin.getId(), 0,
					IPConvert.getIntegerIP(ip));

			// 封装创建VM实体bean
			NovaServerForCreate serverForCreate = new NovaServerForCreate();
			serverForCreate.setJobExt(newvmid);
			serverForCreate.setName(vr.getName());
			// 如果传给openstack的VM的name有中文，则赋予referenceID-instanceID作为其name（openstack不识别中文）
			if (CharUtil.isChineseByREG(vr.getName())) {
				serverForCreate.setName(vr.getId() + "-" + inst.getId());
			}
			serverForCreate.setFlavorRef(flavorId);
			serverForCreate.setImageRef(imageId);
			if (vr.getVmZone() != null) {
				serverForCreate.setZone(vr.getVmZone());
			}
			serverForCreate.setAdminPassword(vr.getRadom_user() + "/"
					+ vr.getRadom_pwd());
			serverForCreate.setReferenceId(String.valueOf(vr.getId()));
			serverForCreate.setObjInstanceId(String.valueOf(inst.getId()));
			serverForCreate.setFloatingIp(ip);
			if (diskStr.endsWith(";")) {
				diskStr = diskStr.substring(0, diskStr.length() - 1);
			}
			serverForCreate.setDisksize(diskStr);
			// 判断是否发邮件
			if (Constants.USER.equalsIgnoreCase(vr.getCreaterType())) {
				User user = userService.getUser(vr.getOwner());
				ownerEmail = user.getEmail();
				domainId = user.getDomain().getId();
			}
			// 资源隔离设置
			SecurityRules sr = new SecurityRules();
			SecurityRules.BandwidthRule br = new SecurityRules.BandwidthRule();
			if (vr.getBwtIn() != null) {
				br.setBwtIn(vr.getBwtIn());
			}
			if (vr.getBwtOut() != null) {
				br.setBwtOut(vr.getBwtOut());
			}
			SecurityRules.ConnlimitRule cr = new SecurityRules.ConnlimitRule();
			if (vr.getIpConnIn() != null) {
				cr.setIpIn(vr.getIpConnIn());
			}
			if (vr.getIpConnOut() != null) {
				cr.setIpOut(vr.getIpConnOut());
			}
			if (vr.getTcpConnIn() != null) {
				cr.setTcpIn(vr.getTcpConnIn());
			}
			if (vr.getTcpConnOut() != null) {
				cr.setTcpOut(vr.getTcpConnOut());
			}
			if (vr.getUdpConnIn() != null) {
				cr.setUdpIn(vr.getUdpConnIn());
			}
			if (vr.getUdpConnOut() != null) {
				cr.setUdpOut(vr.getUdpConnOut());
			}
			sr.setBandwidthRule(br);
			sr.setConnlimitRule(cr);
			serverForCreate.setSecurityRules(sr);
			// 调用RebbitMQ
			HcEventResource her = new HcEventResource();
			her.setEvent_time(new Date());
			her.setType(LogResourceOperationType.ADD.getIndex());// 新增
			her.setRes_type(LogResourceType.VM.getIndex());// 操作VM
			her.setBiz_type(LogBizType.PUBLISH_CREATE.getIndex());// 发布创建
			her.setObj_id(vr.getId());
			her.setObj_name(vr.getName());
			her.setObj_instance_id(inst.getId());// 传instanceID以确定更新消息对应唯一instance
			her.setDomain_id(domainId);
			her.setOperator(admin.getEmail());
			her.setOperator_id(admin.getId());
			her.setOperator_type(LogOperatorType.ADMIN.getIndex());
			her.setOwner_email(ownerEmail);
			rabbitMqUtil.sendMessage(Constants.JOBSERVER_METHOD_CrVMPublish,
					serverForCreate, her, "HcEventResource");
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_CREATE_ADMIN_ERROR,
					"publishVm异常", log, e);
		}
		return newvmid;
	}

	// 审核通过创建云主机
	public String verifyTryVm(VpdcReference reference, String extDisk,
			long adminId) throws HsCloudException {
		String newvmid = null;
		String diskStr = "";
		try {
			Admin admin = adminDao.get(adminId);
			User user = userService.getUser(reference.getOwner());
			if (VMStatusBussEnum.TRYWAIT.getCode() == reference
					.getVm_business_status().intValue()) {
				VpdcInstance instance = null;
				Date d = new Date();
				String imageId = reference.getImageId();
				String flavorId = reference.getFlavorId();
				newvmid = "temp_" + UUID.randomUUID().toString();
				instance = new VpdcInstance();
				instance.setVpdcreference(reference);
				instance.setCreateId(adminId);
				instance.setCreateDate(d);
				instance.setUpdateId(adminId);
				instance.setUpdateDate(d);
				Set<VpdcInstance> instances = new HashSet<VpdcInstance>();
				instance.setVmId(newvmid);
				instance.setInitVmId(newvmid);
				instance.setStatus(0);
				instances.add(instance);
				reference.setInstances(instances);
				reference.setVm_business_status(VMStatusBussEnum.TRY.getCode());
				reference.setUpdateDate(d);
				// reference.setCreateDate(d);
				// 审核创建VM先判定资源最优的zone
				if (reference.isNeedToFindOptimZone()) {
					String optinalZone = this.getOptimalZoneOfZones(reference
							.getVmZone());
					if (!StringUtils.isEmpty(optinalZone)) {
						reference.setVmZone(optinalZone);
					} else {
						throw new HsCloudException(Constants.VM_ZONE_NO_ENOUGH,
								"the resource of zone is not enough!", log,
								null);
					}
				}

				vpdcDao.updateVpdcReference(reference);
				// 添加扩展盘关系
				String[] exttendDisks = extDisk == null ? null : extDisk
						.split(",");
				SimpleDateFormat extDiskFormat = new SimpleDateFormat(
						"yyyyMMddHHmmss");
				if (extDisk != null && exttendDisks.length > 0) {
					String eDiskName = "";
					for (int j = 0; j < exttendDisks.length; j++) {
						if (!"".equals(exttendDisks[j].toString())) {
							eDiskName = extDiskFormat.format(new Date());
							VpdcReference_extdisk vied = new VpdcReference_extdisk();
							vied.setName(eDiskName + j);
							vied.setVmId(newvmid);
							vied.setEd_capacity(Integer
									.parseInt(exttendDisks[j]));
							vied.setEd_reference(reference);
							vied.setCreateDate(d);
							vied.setUpdateDate(d);
							vpdcDao.saveExtDisk(vied);
							diskStr += (vied.getName() + ":"
									+ vied.getEd_capacity() + ";");
						}
					}
				}
				// 更新云主机使用周期
				long times = d.getTime() + reference.getTryLong() * 24 * 3600
						* 1000L;
				updateReferencePeriod(reference.getId(), d, new Date(times));
				// 添加IP关联
				instance = vpdcDao.findVmByVmId(newvmid);
				// vpdcDao.updateIpDetailByIp(instance.getId(), 0, user.getId(),
				// 0, IPConvert.getIntegerIP(ip));
				// 记录VM到期日期变更日志
				this.saveVmPeriodLog(Constants.VM_PERIOD_LOG_VERIFYTRY,
						reference.getId(), new Date(times), new Date(times));
				// 封装创建VM实体bean
				NovaServerForCreate serverForCreate = new NovaServerForCreate();
				serverForCreate.setJobExt(newvmid);
				serverForCreate.setName(reference.getName());
				serverForCreate.setFlavorRef(flavorId);
				serverForCreate.setImageRef(imageId);
				if (reference.getVmZone() != null) {
					serverForCreate.setZone(reference.getVmZone());
				}
				serverForCreate.setAdminPassword(reference.getRadom_user()
						+ "/" + reference.getRadom_pwd());
				serverForCreate
						.setReferenceId(String.valueOf(reference.getId()));
				serverForCreate.setObjInstanceId(String.valueOf(instance
						.getId()));
				// serverForCreate.setFloatingIp(ip);
				if (diskStr.endsWith(";")) {
					diskStr = diskStr.substring(0, diskStr.length() - 1);
				}
				serverForCreate.setDisksize(diskStr);
				// 资源隔离设置
				SecurityRules sr = new SecurityRules();
				SecurityRules.BandwidthRule br = new SecurityRules.BandwidthRule();
				if (reference.getBwtIn() != null) {
					br.setBwtIn(reference.getBwtIn());
				}
				if (reference.getBwtOut() != null) {
					br.setBwtOut(reference.getBwtOut());
				}
				SecurityRules.ConnlimitRule cr = new SecurityRules.ConnlimitRule();
				if (reference.getIpConnIn() != null) {
					cr.setIpIn(reference.getIpConnIn());
				}
				if (reference.getIpConnOut() != null) {
					cr.setIpOut(reference.getIpConnOut());
				}
				if (reference.getTcpConnIn() != null) {
					cr.setTcpIn(reference.getTcpConnIn());
				}
				if (reference.getTcpConnOut() != null) {
					cr.setTcpOut(reference.getTcpConnOut());
				}
				if (reference.getUdpConnIn() != null) {
					cr.setUdpIn(reference.getUdpConnIn());
				}
				if (reference.getUdpConnOut() != null) {
					cr.setUdpOut(reference.getUdpConnOut());
				}
				sr.setBandwidthRule(br);
				sr.setConnlimitRule(cr);
				serverForCreate.setSecurityRules(sr);
				// 调用RebbitMQ
				HcEventResource her = new HcEventResource();
				her.setEvent_time(new Date());
				her.setNeedIP((short) 0);// 需要RabbitMQ获取IP
				her.setType(LogResourceOperationType.ADD.getIndex());// 新增
				her.setRes_type(LogResourceType.VM.getIndex());// 操作VM
				her.setBiz_type(LogBizType.TRIAL_CREATE.getIndex());// 试用创建
				her.setObj_name(reference.getName());
				her.setObj_id(reference.getId());
				her.setObj_instance_id(instance.getId());// 传instanceID以确定更新消息对应唯一instance
				her.setDomain_id(user.getDomain().getId());
				her.setOperator(admin.getEmail());
				her.setOwner_email(user.getEmail());
				her.setExt_column_str(String.valueOf(user.getId()));
				her.setOperator_id(admin.getId());
				her.setOperator_type(LogOperatorType.ADMIN.getIndex());// 管理员操作
				rabbitMqUtil.sendMessage(Constants.JOBSERVER_METHOD_CrVMTry,
						serverForCreate, her, "HcEventResource");
			} else {
				throw new HsCloudException(Constants.VM_NOT_STATUS,
						"verifyTryVm拒绝", log, null);
			}
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_CREATE_ADMIN_ERROR,
					"verifyTryVm异常", log, e);
		}
		return newvmid;
	}

	@Transactional(readOnly = false)
	public String getVnc(String uuid) throws HsCloudException {
		String url = null;
		try {
			VpdcReference vr = vpdcDao.findVpdcReferenceByVmId(uuid);
			if (Constants.VM_ISENABLE_TURE != vr.getIsEnable()) {
				throw new HsCloudException(Constants.VM_FREEZE_NODO,
						"openvm Exception", log, null);
			}
			String zone = vr.getVmZone();
			if (zone.contains("$")) {
				zone = zone.substring(0, zone.indexOf("$"));
			}
			ServerResource serverResource = openstackUtil.getClient()
					.getComputeEndpoint(zone).servers().server(uuid);
			Console console = serverResource.getVncConsole("novnc");
			url = console.getUrl();
			// waitForState("ACTIVE");
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_VNC_ERROR,
					"getVnc Exception", log, e);
		}
		return url;
	}

	@Transactional(readOnly = false)
	public boolean hasSameVmName(String vmName) throws HsCloudException {
		log.info("OPS-Service-hasSameVmName start");
		VpdcReference reference = vpdcDao.findVpdcReferenceByVmName(vmName);
		if (reference != null) {
			return true;
		}
		return false;
	}

	@Transactional(readOnly = false)
	public boolean updateVmName(String vmName, String vmId, long uid)
			throws HsCloudException {
		log.info("OPS-Service-updateVmName start");
		VpdcReference vr = vpdcDao.findVpdcReferenceByVmId(vmId);
		vr.setName(vmName);
		vr.setUpdateDate(new Date());
		vr.setUpdateId(uid);
		vpdcDao.updateVpdcReference(vr);
		return true;
	}

	@Transactional(readOnly = false)
	public boolean updateVmComments(String comments, String outComments,
			String vmId, long uid) throws HsCloudException {
		log.info("OPS-Service-updateVmComments start");
		VpdcReference vr = vpdcDao.findVpdcReferenceByVmId(vmId);
		vr.setComments(comments);
		vr.setOutComments(outComments);
		vr.setUpdateDate(new Date());
		vr.setUpdateId(uid);
		vpdcDao.updateVpdcReference(vr);
		return true;
	}

	@Transactional(readOnly = false)
	public InstanceDetailVo findVm(String vmId) throws HsCloudException {
		log.info("service 层中接受到vmId是：" + vmId);
		// ServerResource serverResource =
		// openstackUtil.getCompute().servers().server(vmId);
		/*
		 * HsInfoResource hsResource = openstackUtil.getCompute().hsInfo(); if
		 * (vmId==null || hsResource == null) { return null; }
		 */
		InstanceDetailVo detailVo = new InstanceDetailVo();
		try {
			VpdcReference reference = vpdcDao.getVpdcReferenceByVmId(vmId);
			if (null == reference) {
				return null;
			} else {
				detailVo.setVmId(vmId);
				detailVo.setReferenceId(reference.getId());
				detailVo.setVmIsEnable(reference.getIsEnable());
				detailVo.setVmName(reference.getName());
				// ////////////////////////////////////////////////
				detailVo.setOsLoginUser(reference.getRadom_user());
				detailVo.setOsLoginPwd(reference.getRadom_pwd());
				// ////////////////////////////////////////////////
				String zone = reference.getVmZone();
				if (zone != null && zone.contains("$")) {
					zone = zone.split("\\$")[0];
				}
				detailVo.setZoneName(vpdcDao.findZoneName(zone));
				detailVo.setScId(reference.getScId());
				detailVo.setVmType(reference.getVm_type());
				detailVo.setBuyType(reference.getBuyType());
				detailVo.setRemark(reference.getOutComments());
				detailVo.setVmBusineStatus(reference.getVm_business_status());
				String scname = vpdcDao.findScName(detailVo.getScId());
				detailVo.setScname(scname);
				detailVo.setOsId(reference.getOsId());
				String os = vpdcDao.findOsName(reference.getOsId());
				// String osSize = vpdcDao.getOsSizeByOsId(reference.getOsId());
				detailVo.setOs(os == null ? "" : os);
				List<VmOsBean> osList = null;
				// osList = findOsList(osList, reference);
				osList = vpdcDao.findOsListByReferenceId(reference.getId());
				detailVo.setOsList(osList);
				List<VpdcReference_OrderItem> lvroi = vpdcDao
						.getOrderItemsByReferenceId(reference.getId());
				if (lvroi != null && lvroi.size() > 0) {
					InstanceDetail_ServiceCatalogVo idscv = vpdcDao
							.findVmServiceCatalog(lvroi);
					if (idscv != null) {
						// detailVo.setScname(idscv.getServiceCatalogName());
						detailVo.setOrderDate(idscv.getOrderDate());
						detailVo.setOrderNo(idscv.getOrderNo());
						detailVo.setPrice(idscv.getPrice());
						detailVo.setPricePeriodType(idscv.getPriceTypeName());
					}

				}
				if (reference.getCpu_core() != null) {
					detailVo.setCpu(reference.getCpu_core());
				}
				detailVo.setCpuName(reference.getCpu_type());
				if (reference.getDisk_capacity() != null) {
					detailVo.setDisk(reference.getDisk_capacity());
				}
				detailVo.setIpNum(reference.getIpNum());
				// 加载云主机下的所有扩展盘,只有当VM创建出来之后，才可使用业务数据。否则【申请】状态的VM是没有扩展盘的业务数据的，查询就会看不到扩展盘信息
				if (reference.getVm_type() != 0
						|| (reference.getVm_type() == 0
								&& reference.getVm_business_status() != null && reference
								.getVm_business_status() != 0)) {
					List<VolumeVO> volumeVOList = this
							.getAllAttachVolumesOfVm(vmId);
					if (volumeVOList != null) {
						List<VmExtDiskBean> extdisks = new ArrayList<VmExtDiskBean>();
						for (VolumeVO vo : volumeVOList) {
							VmExtDiskBean vedb = new VmExtDiskBean();
							vedb.setEd_name(vo.getName());
							vedb.setVolumnId(vo.getId());
							vedb.setEd_capacity(vo.getSize());
							extdisks.add(vedb);
						}
						detailVo.setExtdisks(extdisks);
					}
				}
				detailVo.setDiskName(reference.getDisk_type());
				if (reference.getMem_size() != null) {
					detailVo.setMemory(reference.getMem_size());
				}
				detailVo.setMemName(reference.getMem_type());
				if (reference.getNetwork_bandwidth() != null) {
					detailVo.setNetwork(String.valueOf(reference
							.getNetwork_bandwidth()));
				}
				detailVo.setNetworkName(reference.getNetwork_type() == null ? ""
						: reference.getNetwork_type());
				String vmStatus = reference.getVm_status() == null ? null
						: reference.getVm_status().toUpperCase();
				detailVo.setVmStatus(vmStatus);
				String taskStatus = reference.getVm_task_status() == null ? null
						: reference.getVm_task_status().toUpperCase();
				detailVo.setVmTask(taskStatus);
				String ip = (StringUtils.isEmpty(reference.getVm_innerIP()) ? ""
						: reference.getVm_innerIP())
						+ (StringUtils.isEmpty(reference.getVm_outerIP()) ? ""
								: "," + reference.getVm_outerIP());
				detailVo.setIp(ip);
				Date now = new Date();
				VpdcReference_Period vrp = vpdcDao.getReferencePeriod(reference
						.getId());
				if (vrp != null) {
					Date startTime = vrp.getStartTime();
					detailVo.setCreateTime(startTime);
					detailVo.setEffectiveDate(startTime);
					// 如果开始时间为空，则已使用时长为null
					if (startTime != null) {
						long between = (now.getTime() - startTime.getTime()) / 1000;// 除以1000是为了转换成秒
						Date runTime = new Date(between);
						detailVo.setRunTime(runTime);
					}
					// 如果终止时间不为空
					if (vrp.getEndTime() != null) {
						detailVo.setExpireDate(vrp.getEndTime());
						long titleTime = 0;
						long spare = 0;
						if (startTime != null) {// 在开始时间不为空下正常计时
							titleTime = (vrp.getEndTime().getTime() - startTime
									.getTime()) / 1000;
							spare = titleTime - detailVo.getRunTime().getTime();
							if (spare <= 0) {
								detailVo.setRunTime(new Date(titleTime));
							}
						} else {// 在开始时间为空下则为终止时间减去当前时间
							spare = (vrp.getEndTime().getTime() - new Date()
									.getTime()) / 1000;
						}
						detailVo.setSpare(spare);
					}
				}
			}
		} catch (Exception e) {
			log.error(
					"Sorry,openstack get the VM failed,please check the vm is normal Exception",
					e);
			detailVo.setVmStatus(Constants.VM_NOINSTANCE);
		}
		if (log.isDebugEnabled()) {
			log.debug("the deatil info of vm is ：" + detailVo.toString());
		}
		return detailVo;
	}

	@Transactional(readOnly = false)
	public InstanceDetailVo getTryVmInfo(long referenceId)
			throws HsCloudException {
		log.info("service 层中接受到referenceId是：" + referenceId);
		InstanceDetailVo detailVo = new InstanceDetailVo();
		VpdcReference reference = vpdcDao.findVpdcReferenceById(referenceId);
		VpdcInstance vi = this.getActiveVmFromVpdcReference(reference);
		if (reference != null) {
			detailVo.setVmIsEnable(reference.getIsEnable());
			detailVo.setVmName(reference.getName());
			String zone = reference.getVmZone();
			if (zone != null && zone.contains("$")) {
				zone = zone.split("\\$")[0];
			}
			detailVo.setZoneName(vpdcDao.findZoneName(zone));
			detailVo.setScId(reference.getScId());
			String scname = vpdcDao.findScName(detailVo.getScId());
			detailVo.setScname(scname);
			detailVo.setOsId(reference.getOsId());
			String os = vpdcDao.findOsName(reference.getOsId());
			detailVo.setOs(os == null ? "" : os);
			List<VmOsBean> osList = null;
			// osList = findOsList(osList, reference);
			osList = vpdcDao.findOsListByReferenceId(reference.getId());
			detailVo.setOsList(osList);
			if (reference.getCpu_core() != null) {
				detailVo.setCpu(reference.getCpu_core());
			}
			detailVo.setCpuName(reference.getCpu_type());
			if (reference.getDisk_capacity() != null) {
				detailVo.setDisk(reference.getDisk_capacity());
			}
			detailVo.setDiskName(reference.getDisk_type());
			if (reference.getMem_size() != null) {
				detailVo.setMemory(reference.getMem_size());
			}
			detailVo.setMemName(reference.getMem_type());
			if (reference.getNetwork_bandwidth() != null) {
				detailVo.setNetwork(String.valueOf(reference
						.getNetwork_bandwidth()));
			}
			detailVo.setNetworkName(reference.getNetwork_type() == null ? ""
					: reference.getNetwork_type());
		}
		if (vi != null) {
			detailVo.setVmId(vi.getVmId());
			try {
				// ServersResource serversResource =
				// openstackUtil.getCompute().servers();
				// Server server = serversResource.server(vi.getVmId()).get();
				detailVo.setCreateTime(reference.getCreateDate());
				detailVo.setVmStatus(reference.getVm_status());
				// 任务状态分为两方面，1:看server的真是任务状态，2：看是否有等待的备份任务
				String taskStatus = reference.getVm_task_status();
				if (taskStatus == null) {
					VmSnapShotTask vsst = vpdcDao.getUnSnapShotTaskByVmId(vi
							.getVmId());
					if (vsst != null) {
						taskStatus = Constants.VM_STATUS_SNAPSHOT;
					}
				}
				detailVo.setVmTask(taskStatus);
				detailVo.setIp(this.getIP(reference));
				Date now = new Date();
				VpdcReference_Period vrp = vpdcDao.getReferencePeriod(reference
						.getId());
				if (vrp != null) {
					Date startTime = vrp.getStartTime();
					// 如果开始时间为空，则已使用时长为null
					if (startTime != null) {
						long between = (now.getTime() - startTime.getTime()) / 1000;// 除以1000是为了转换成秒
						Date runTime = new Date(between);
						detailVo.setRunTime(runTime);
					}
					// 如果终止时间不为空
					if (vrp.getEndTime() != null) {
						detailVo.setExpireDate(vrp.getEndTime());
						long titleTime = 0;
						long spare = 0;
						if (startTime != null) {// 在开始时间不为空下正常计时
							titleTime = (vrp.getEndTime().getTime() - startTime
									.getTime()) / 1000;
							spare = titleTime - detailVo.getRunTime().getTime();
							if (spare <= 0) {
								detailVo.setRunTime(new Date(titleTime));
							}
						} else {// 在开始时间为空下则为终止时间减去当前时间
							spare = (vrp.getEndTime().getTime() - new Date()
									.getTime()) / 1000;
						}
						detailVo.setSpare(spare);
					}
				}
			} catch (Exception e) {
				log.error(
						"Sorry,openstack get the VM failed,please check the vm is normal Exception",
						e);
				detailVo.setVmStatus(Constants.VM_NOINSTANCE);
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("the deatil info of vm is ：" + detailVo.toString());
		}
		return detailVo;
	}

	public InstanceDetailVo getVmOsListByVmId(String vmId)
			throws HsCloudException {
		InstanceDetailVo detailVo = new InstanceDetailVo();
		VpdcReference vr = vpdcDao.findVpdcReferenceByVmId(vmId);
		detailVo.setOsId(vr.getOsId());
		detailVo.setOs(vpdcDao.findOsName(vr.getOsId()));
		detailVo.setOsLoginUser(vr.getRadom_user());
		List<VmOsBean> lvob = vpdcDao.findOsListByReferenceId(vr.getId());
		detailVo.setOsList(lvob);
		return detailVo;
	}

	public RenewOrderVO getRenewOrder(String vmId, User u)
			throws HsCloudException {
		VpdcReference reference = vpdcDao.findVpdcReferenceByVmId(vmId);
		RenewOrderVO roVo = new RenewOrderVO();
		roVo.setVmId(vmId);
		roVo.setIp((StringUtils.isEmpty(reference.getVm_innerIP()) ? ""
				: reference.getVm_innerIP())
				+ (StringUtils.isEmpty(reference.getVm_outerIP()) ? "" : ","
						+ reference.getVm_outerIP()));
		if (reference != null) {
			roVo.setVmName(reference.getName());
			String os = vpdcDao.findOsName(reference.getOsId());
			roVo.setOs(os == null ? "" : os);
			List<VpdcReference_OrderItem> lvroi = vpdcDao
					.getOrderItemsByReferenceId(reference.getId());
			if (lvroi != null && lvroi.size() > 0) {
				InstanceDetail_ServiceCatalogVo idscv = vpdcDao
						.findVmServiceCatalog(lvroi);
				if (idscv != null) {
					roVo.setScname(idscv.getServiceCatalogName());
					roVo.setRenewPeriodStart(idscv.getExpirationDate());
					long period = idscv.getExpirationDate().getTime()
							- idscv.getEffectiveDate().getTime();
					roVo.setRenewPeriodEnd(new Date(idscv.getExpirationDate()
							.getTime() + period + 24 * 3600 * 1000L));
					roVo.setPricePeriodType(idscv.getPriceTypeName());
					roVo.setTotle(idscv.getPrice());
				}
			}
			roVo.setCpu(reference.getCpu_core());
			roVo.setCpuName(reference.getCpu_type());
			roVo.setDisk(reference.getDisk_capacity());
			roVo.setDiskName(reference.getDisk_type());
			roVo.setMemory(reference.getMem_size());
			roVo.setMemName(reference.getMem_type());
			roVo.setNetwork(String.valueOf(reference.getNetwork_bandwidth()));
			roVo.setNetworkName(reference.getNetwork_type());
			Account acc = accountDao.getAccountByUserId(u.getId());
			if (acc != null) {
				roVo.setBalance(acc.getBalance());
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("the deatil info of vm is ：" + roVo.toString());
		}
		return roVo;
	}

	public String createFlavor(FlavorVO flavorVO) throws HsCloudException {
		int vcpus = flavorVO.getVcpus();
		int ram = flavorVO.getRam();
		int disk = flavorVO.getDisk();
		// 判断是否已有匹配的flavor
		Long flavorId = vpdcDao.sameFlavor(disk, ram, vcpus);
		if (flavorId > 0) {
			return String.valueOf(flavorId);
		}
		// 没有相同配置flavor则新建flavor
		int id = vpdcDao.getFlavorMaxId().intValue() + 1;
		String name = "New Flavor " + id;
		int ephemeral = 0;
		Map<String, String> extensions = flavorVO.getExtensions();
		if (extensions != null) {
			String ephemeralStr = extensions.get("ephemeral");
			if (StringUtils.isNumeric(ephemeralStr))
				ephemeral = Integer.parseInt(ephemeralStr);
		}
		NovaFlavorForCreate flavor = new NovaFlavorForCreate();
		flavor.setId(id);
		flavor.setName(name);
		flavor.setVcpus(vcpus);
		flavor.setRam(ram);
		flavor.setDisk(disk);
		flavor.setEphemeral(ephemeral);
		Flavor newFlavor = openstackUtil.getCompute().flavors().post(flavor);
		VpdcFlavor vf = new VpdcFlavor();
		Date d = new Date();
		vf.setId(Long.valueOf(id));
		vf.setCreateDate(d);
		vf.setUpdateDate(d);
		vf.setName(name);
		vf.setVcpus(vcpus);
		vf.setMemory_mb(ram);
		vf.setRoot_gb(disk);
		vf.setEphemeral_gb(ephemeral);
		vpdcDao.saveVpdcFlavor(vf);
		return newFlavor.getId();
	}

	public NovaVolumeForCreate createVolume(int diskSize) {
		NovaVolumeForCreate volume = new NovaVolumeForCreate();
		volume.setSizeInGB(diskSize);
		return volume;
	}

	public InstanceVO getInstance(String vmId) {
		try {
			VpdcInstance instance = vpdcDao.findVmByVmId(vmId);
			if (instance == null) {
				return null;
			}
			InstanceVO instanceVO = new InstanceVO();
			instanceVO.setName(instance.getVpdcreference().getName());
			// ServersResource serversResource =
			// openstackUtil.getCompute().servers();
			// Server server = serversResource.server(vmId).get();

			instanceVO.setId(instance.getVmId());
			instanceVO.setStatus(instance.getVpdcreference().getVm_status());
			instanceVO.setCreateTime(instance.getVpdcreference()
					.getCreateDate());
			// instanceVO.setFlavorId(server.getFlavor().getId());
			String taskStatus = instance.getVpdcreference().getVm_task_status();
			instanceVO.setTask(taskStatus);
			String ips = (StringUtils.isEmpty(instance.getVpdcreference()
					.getVm_innerIP()) ? "" : instance.getVpdcreference()
					.getVm_innerIP())
					+ (StringUtils.isEmpty(instance.getVpdcreference()
							.getVm_outerIP()) ? "" : ","
							+ instance.getVpdcreference().getVm_outerIP());
			instanceVO.setIp(ips);
			instanceVO.setHostName(instance.getNodeName());
			return instanceVO;

		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}

	public void createNoRouterVPDC(CreateVpdcBean vpdcBean, Long userId)
			throws HsCloudException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");// yyyy-MM-dd
																				// hh:mm:ss
		Date d = new Date();
		// 接口创建不可路由Lan
		SecurityLan sl = null;
		try {
			sl = openstackUtil.getCompute().vpdcLans().createNoRoutedLan();
		} catch (Exception e) {
			e.printStackTrace();
			throw new HsCloudException(Constants.VM_OPENSTACK_API_ERROR,
					"createNoRouterVPDC异常", log, e);
		}
		// 创建不可路由Lan
		VpdcLan vl = new VpdcLan();
		vl.setName(dateFormat.format(d));
		vl.setType(Constants.LAN_NOROUTED);
		vl.setLanId(sl.getId());// 如改消息模式，该字段需由消息更新
		vl.setCreateDate(d);
		vl.setCreateId(userId);
		vl.setUpdateDate(d);
		vl.setUpdateId(userId);
		Set<VpdcLan> lans = new HashSet<VpdcLan>();
		lans.add(vl);
		// 创建VPDC业务数据
		Vpdc vpdc = new Vpdc();
		vpdc.setCreateDate(d);
		vpdc.setCreateId(userId);
		vpdc.setUpdateDate(d);
		vpdc.setUpdateId(userId);
		vpdc.setOwner(userId);
		vpdc.setName(vpdcBean.getName());
		vpdc.setType(Constants.VPDC_NOROUTE);
		vpdc.setZoneGroupId(vpdcBean.getZoneGroup());
		vpdc.setLans(lans);
		vl.setVpdc(vpdc);
		vpdcDao.saveVpdc(vpdc);
	}

	public Map<NovaServerForCreate, HcEventResource> createRouterVPDC(
			CreateVpdcBean vpdcBean, User user) throws HsCloudException {
		Map<NovaServerForCreate, HcEventResource> rabbitMQRouter = new HashMap<NovaServerForCreate, HcEventResource>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");// yyyy-MM-dd
																				// hh:mm:ss
		Date d = new Date();
		FlavorVO flavorVO = new FlavorVO();
		flavorVO.setVcpus(vpdcBean.getCpuCore());
		flavorVO.setRam(vpdcBean.getMemSize());
		flavorVO.setDisk(vpdcBean.getDiskCapacity());
		// 创建VRouter
		NovaServerForCreate serverForCreate = new NovaServerForCreate();
		String routerName = UUID.randomUUID().toString();
		serverForCreate.setName(routerName);
		String flavorId = createFlavor(flavorVO);
		serverForCreate.setFlavorRef(flavorId);
		serverForCreate.setImageRef(vpdcBean.getRouterImage());
		// ————获取资源充沛的zone
		String optinalZone = this.getOptimalZoneOfZones(vpdcBean.getZones());
		// String optinalZone = "beijing";//test
		if (!StringUtils.isEmpty(optinalZone)) {
			serverForCreate.setZone(optinalZone);
		} else {
			throw new HsCloudException(Constants.VM_ZONE_NO_ENOUGH,
					"the resource of zone is not enough!", log, null);
		}
		// 绑定network
		// ————Wan类型的由Jobserver调用路由可用IP接口赋参数，业务由Message更新到Router表中
		VpdcRouter router = getVRouter(vpdcBean, user.getId(), d, routerName,
				optinalZone);
		router.setFlavorId(Integer.valueOf(flavorId));
		// 创建VPDC业务数据
		Vpdc vpdc = getVpdc(vpdcBean, user.getId(), d, router);
		router.setVpdc(vpdc);
		try {
			vpdcDao.saveVpdc(vpdc);
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new HsCloudException(Constants.VPDC_CREATE_ERROR,
					"insert vpdc failed!", log, null);
		}
		serverForCreate.setReferenceId(String.valueOf(router.getId()));
		// 资源隔离设置
		SecurityRules sr = new SecurityRules();
		SecurityRules.BandwidthRule br = new SecurityRules.BandwidthRule();
		br.setBwtIn(vpdcBean.getBandWidth());
		br.setBwtOut(vpdcBean.getBandWidth());
		sr.setBandwidthRule(br);
		serverForCreate.setSecurityRules(sr);
		// 调用RebbitMQ
		HcEventResource her = new HcEventResource();
		her.setEvent_time(new Date());
		her.setType(LogResourceOperationType.ADD.getIndex());// 新增
		her.setNeedIP((short) 0);// Jobserver需要调用获取路由可用IP接口
		her.setRes_type(LogResourceType.ROUTER.getIndex());// 操作Router
		her.setBiz_type(LogBizType.ORDER_CREATE.getIndex());// 订单创建
		her.setObj_name(router.getName());
		her.setObj_id(router.getId());
		her.setOperator(user.getEmail());
		her.setOperator_id(user.getId());
		her.setOperator_type(LogOperatorType.USER.getIndex());// 前台用户操作
		// 借用message属性存储方法，发消息时再将该属性值置空。
		her.setMessage(Constants.JOBSERVER_METHOD_CrRouterBuy);
		rabbitMQRouter.put(serverForCreate, her);

		// WanNetwork和Object的关联关系（需由Message更新其它关联字段）
		saveNetworkObject(null, router.getId(), null, Constants.OBJECT_ROUTER);
		// add VpdcRouter和OrderItem关系
		VpdcRouter_OrderItem vroi = new VpdcRouter_OrderItem();
		vroi.setVpdcRouterId(router.getId());
		vroi.setOrder_item_id(vpdcBean.getOrder_item_id());
		vpdcDao.saveRouterOrderItem(vroi);
		// 更新router有效周期
		if (vpdcBean.getStart_time() != null && vpdcBean.getEnd_time() != null) {
			VpdcRouter_Period vrp = new VpdcRouter_Period();
			vrp.setVpdcrouterId(router.getId());
			vrp.setStartTime(vpdcBean.getStart_time());
			vrp.setEndTime(vpdcBean.getEnd_time());
			vpdcDao.updateRouterPeriod(vrp);
			VpdcRouter_Period_Log vrpl = new VpdcRouter_Period_Log();
			vrpl.setRouterId(router.getId());
			vrpl.setCreateDate(d);
			vrpl.setEndTimeLast(vpdcBean.getEnd_time());
			vrpl.setEndTimeNow(vpdcBean.getEnd_time());
			vrpl.setDescription("create vpdc");
			vpdcDao.saveRouterPeriodLog(vrpl);
		}
		// ————创建Lan、LanNetwork并绑定LanNetwork
		// ————————LanNetwork
		Long lanNetWorkId = 0l;
		VpdcNetworkResource vnr = openstackUtil.getCompute().vpdcNetworks();
		Network nw = null;
		for (VlanNetwork vn : vpdcBean.getVlans()) {
			// ————————Routed lan
			SecurityLan sl = null;
			try {
				sl = openstackUtil.getCompute().vpdcLans().createRoutedLan();
			} catch (Exception e) {
				e.printStackTrace();
				throw new HsCloudException(Constants.VM_OPENSTACK_API_ERROR,
						"createNoRouterVPDC异常", log, e);
			}
			VpdcLan vl = new VpdcLan();
			vl.setName(dateFormat.format(d));
			vl.setType(Constants.LAN_ROUTED);
			vl.setLanId(sl.getId());
			vl.setCreateDate(d);
			vl.setCreateId(user.getId());
			vl.setUpdateDate(d);
			vl.setUpdateId(user.getId());
			vl.setVpdc(vpdc);
			vpdcDao.saveVpdcLan(vl);
			log.info("RoutedLan created success");
			nw = vnr.createLanNetwork(Constants.NETWORK_LAN, vn.getDns1(),
					vn.getDns2(), vn.getSize());
			VpdcNetwork lanNetwork = getNetWork(vpdcBean.getZoneGroup(), vl,
					vn, nw);
			lanNetWorkId = vpdcDao.saveVpdcNetwork(lanNetwork);
			log.info("LanNetwork created success");
			// 添加lanNetwork
			VifAdd va = new VifAdd();
			List<VifAdd.RequestedNetwork> lvarn = new ArrayList<VifAdd.RequestedNetwork>();
			VifAdd.RequestedNetwork varn = new VifAdd.RequestedNetwork();
			varn.setNetworkId(nw.getId());
			varn.setLanId(sl.getId());
			lvarn.add(varn);
			va.setRequestedNetworks(lvarn);
			// LanNetwork和Object的关联关系
			saveNetworkObject(lanNetWorkId, router.getId(), null,
					Constants.OBJECT_ROUTER);
			// saveNetworkObject(lanNetWorkId,router.getId(),routerUUID,Constants.OBJECT_ROUTER);

		}
		return rabbitMQRouter;
	}

	/**
	 * <保存Network和object关联关系> <功能详细描述>
	 * 
	 * @param networkId
	 * @param objectId
	 * @param objectUUID
	 * @param objectType
	 * @see [类、类#方法、类#成员]
	 */
	private void saveNetworkObject(Long networkId, Long objectId,
			String objectUUID, int objectType) {
		VpdcNetwork_Object no = new VpdcNetwork_Object();
		if (networkId != null) {
			no.setNetworkId(networkId);
		}
		no.setObjectId(objectId);
		if (!StringUtils.isEmpty(objectUUID)) {
			no.setObjectUUID(objectUUID);
		}
		no.setObjectType(objectType);
		vpdcDao.saveNetwork_Object(no);
	}

	private Vpdc getVpdc(CreateVpdcBean vpdcBean, Long userId, Date d,
			VpdcRouter router) {
		Vpdc vpdc = new Vpdc();
		vpdc.setCreateDate(d);
		vpdc.setCreateId(userId);
		vpdc.setUpdateDate(d);
		vpdc.setUpdateId(userId);
		vpdc.setOwner(userId);
		vpdc.setName(vpdcBean.getName());
		vpdc.setType(Constants.VPDC_ROUTED);
		vpdc.setZoneGroupId(vpdcBean.getZoneGroup());
		Set<VpdcRouter> routers = new HashSet<VpdcRouter>();
		routers.add(router);
		vpdc.setRouters(routers);
		return vpdc;
	}

	private VpdcNetwork getNetWork(Long zoneGroup, VpdcLan vl, VlanNetwork vn,
			Network nw) {
		VpdcNetwork vnet = new VpdcNetwork();
		vnet.setLabel(Constants.NETWORK_LAN);
		vnet.setDns1(vn.getDns1());
		vnet.setDns2(vn.getDns2());
		vnet.setNetworkSize(vn.getSize());
		vnet.setBridge(nw.getBridge());
		// vnet.setBridge("br101");
		vnet.setBridgeInterface(nw.getBridgeInterface());
		// vnet.setBridgeInterface("eth1");
		vnet.setBroadcast(nw.getBroadcast());
		// vnet.setBroadcast("172.12.0.191");
		vnet.setCidr(nw.getCidr());
		// vnet.setCidr("172.12.0.128/26");
		vnet.setDhcptart(nw.getDhcpStart() == null ? "" : nw.getDhcpStart());
		vnet.setGateway(nw.getGateway());
		// vnet.setGateway("172.12.0.129");
		vnet.setNetworkId(nw.getId());
		// vnet.setNetworkId("bd24f800-9450");
		vnet.setInjected(nw.getInjected());
		// vnet.setInjected(true);
		vnet.setNetmask(nw.getNetmask());
		// vnet.setNetmask("255.255.255.192");
		vnet.setProjectId(nw.getProjectId() == null ? "" : nw.getProjectId());
		vnet.setVlanId(nw.getVlan());
		// vnet.setVlanId(101);
		vnet.setZoneGroup(zoneGroup);
		vnet.setSecurytyVlan(vl);
		return vnet;
	}

	private VpdcRouter getVRouter(CreateVpdcBean vpdcBean, Long userId, Date d,
			String routerName, String optinalZone) {
		VpdcRouter router = new VpdcRouter();
		router.setName(routerName);
		router.setCpuCore(vpdcBean.getCpuCore());
		router.setMemSize(vpdcBean.getMemSize());
		router.setDiskCapacity(vpdcBean.getDiskCapacity());
		router.setBandWidth(vpdcBean.getBandWidth());
		router.setBuyLong(vpdcBean.getUseLong());
		router.setOsId(vpdcBean.getOsId());
		router.setImageId(vpdcBean.getRouterImage());
		router.setZone(optinalZone);
		router.setRouterOwner(userId);
		router.setRouterStatus(Constants.VM_STATUS_BUILDING);
		router.setRouterTaskStatus(Constants.VM_STATUS_BUILDING);
		VpdcVrouterTemplate vvt = new VpdcVrouterTemplate();
		vvt.setId(vpdcBean.getRouterTemplateId());
		router.setVrouterTemplate(vvt);
		router.setCreateDate(d);
		router.setCreateId(userId);
		router.setUpdateDate(d);
		router.setUpdateId(userId);
		return router;
	}

	public void updateVPDC(Long vpdcId, String name, List<VlanNetwork> vlans,
			Long userId) throws HsCloudException {
		Vpdc vpdc = vpdcDao.getVpdc(vpdcId);
		vpdc.setName(name);
		if (Constants.VPDC_ROUTED == vpdc.getType() && vlans != null
				&& vlans.size() > 0) {
			Set<VpdcRouter> svr = vpdc.getRouters();
			VpdcRouter router = svr.iterator().next();
			// 清除原有Vlan
			List<VpdcLan> securityLans = new ArrayList<VpdcLan>();
			List<VpdcNetwork_Object> lvno = vpdcDao.findNetwork_Objects(
					router.getId(), Constants.OBJECT_ROUTER);
			for (VpdcNetwork_Object vno : lvno) {
				VpdcNetwork vn = vpdcDao.getVpdcNetwork(vno.getNetworkId());
				if (Constants.NETWORK_LAN.equals(vn.getLabel())) {
					securityLans.add(vn.getSecurytyVlan());
					// openstack 删除原网卡
					VifRemove vr = new VifRemove();
					List<VifRemove.RequestedNetwork> lvrrn = new ArrayList<VifRemove.RequestedNetwork>();
					VifRemove.RequestedNetwork vrrn = new VifRemove.RequestedNetwork();
					vrrn.setNetworkId(vn.getNetworkId());
					vrrn.setFixedIp(vn.getGateway());
					lvrrn.add(vrrn);
					vr.setRequestedNetworks(lvrrn);
					try {
						openstackUtil.getCompute().servers()
								.server(router.getRouterUUID()).removeVif(vr);
					} catch (Exception e) {
						e.printStackTrace();
						throw new HsCloudException(
								Constants.VM_OPENSTACK_API_ERROR,
								"removeVif异常", log, e);
					}
					// 业务删除原网卡
					vpdcDao.deleNetwork_Object(vno);
					vpdcDao.deleVpdcNetwork(vn.getId(), userId);
				}
			}
			// 增加修改后的Vlan
			VpdcNetworkResource vnr = openstackUtil.getCompute().vpdcNetworks();
			Network nw = null;
			for (int i = 0; i < vlans.size(); i++) {
				VlanNetwork vn = vlans.get(i);
				try {
					nw = vnr.createLanNetwork(Constants.NETWORK_LAN,
							vn.getDns1(), vn.getDns2(), vn.getSize());
				} catch (Exception e) {
					e.printStackTrace();
					throw new HsCloudException(
							Constants.VM_OPENSTACK_API_ERROR,
							"createLanNetwork异常", log, e);
				}
				VpdcLan vl = securityLans.get(i);
				VpdcNetwork lanNetwork = getNetWork(vpdc.getZoneGroupId(), vl,
						vn, nw);
				Long lanNetWorkId = vpdcDao.saveVpdcNetwork(lanNetwork);
				log.info("LanNetwork created success");

				VifAdd va = new VifAdd();
				List<VifAdd.RequestedNetwork> lvarn = new ArrayList<VifAdd.RequestedNetwork>();
				VifAdd.RequestedNetwork varn = new VifAdd.RequestedNetwork();
				varn.setNetworkId(nw.getId());
				varn.setLanId(vl.getLanId());
				lvarn.add(varn);
				va.setRequestedNetworks(lvarn);
				try {
					openstackUtil.getCompute().servers()
							.server(router.getRouterUUID()).addVif(va);
				} catch (Exception e) {
					e.printStackTrace();
					throw new HsCloudException(
							Constants.VM_OPENSTACK_API_ERROR, "addVif异常", log,
							e);
				}
				// WanNetwork和Object的关联关系
				saveNetworkObject(lanNetWorkId, router.getId(),
						router.getRouterUUID(), 1);
			}
		}
	}

	public VpdcInstance getActiveVmFromVpdcReference(VpdcReference reference) {
		VpdcInstance instance = null;
		if (reference != null) {
			Set<VpdcInstance> svi = reference.getInstance();
			if (svi != null && svi.size() > 0) {
				for (VpdcInstance vi : svi) {
					if (0 == vi.getStatus()) {
						instance = vi;
						break;
					}
				}
			}
		}
		return instance;
	}

	private String getIP(VpdcReference vr) {
		String ips = "";
		if (vr != null) {
			String fixip = vr.getVm_innerIP();
			String floatingip = vr.getVm_outerIP();
			if (!StringUtils.isEmpty(fixip)) {
				ips = fixip;
				if (!StringUtils.isEmpty(floatingip)) {
					ips += "," + floatingip;
				}
			} else {
				if (!StringUtils.isEmpty(floatingip)) {
					ips = floatingip;
				}
			}
		}
		return ips;
	}

	/**
	 * <不重复向instanceAdminVos中添加instanceAdminVO> <功能详细描述>
	 * 
	 * @param instanceAdminVos
	 * @param instanceAdminVO
	 * @see [类、类#方法、类#成员]
	 */
	private void addInstanceVoToList(List<VmInfoVO> instanceAdminVos,
			VmInfoVO instanceAdminVO) {
		// instanceAdminVos.add(instanceAdminVO);
		boolean bl = false;
		for (VmInfoVO iav : instanceAdminVos) {
			if (instanceAdminVO.getVmId() != null
					&& instanceAdminVO.getVmId().equals(iav.getVmId())) {
				bl = true;
				break;
			}
		}
		if (!bl) {
			instanceAdminVos.add(instanceAdminVO);
		}
	}

	private VpdcReference createVmReferenceBeanForTry(
			CreateVmBean createVmBean, long userId) {
		Date d = new Date();
		VpdcReference reference = new VpdcReference();
		reference.setCreateId(userId);
		reference.setCreaterType(Constants.USER);
		reference.setVm_type(VMTypeEnum.TRY.getCode());
		reference.setBuyType(createVmBean.getBuyType());
		reference.setVm_business_status(VMStatusBussEnum.TRYWAIT.getCode());
		reference.setTryLong(createVmBean.getTryLong());
		reference.setScId(createVmBean.getScId());
		if (!StringUtils.isEmpty(createVmBean.getOwner())) {
			reference.setOwner(Long.valueOf(createVmBean.getOwner()));
		}
		reference.setCreateDate(d);
		reference.setUpdateId(userId);
		reference.setUpdateDate(d);
		reference.setName(createVmBean.getName());
		reference.setVmZone(createVmBean.getVmZone());
		reference.setCpu_core(Integer.valueOf(createVmBean.getVcpus()));
		if (createVmBean.getVcpusType() != null) {
			reference.setCpu_type(createVmBean.getVcpusType());
		}
		reference.setDisk_capacity(Integer.valueOf(createVmBean.getDisk()));
		if (createVmBean.getDiskType() != null) {
			reference.setDisk_type(createVmBean.getDiskType());
		}
		reference.setMem_size(Integer.valueOf(createVmBean.getRam()));
		if (createVmBean.getVcpusType() != null) {
			reference.setMem_type(createVmBean.getRamType());
		}
		reference.setNetwork_bandwidth(createVmBean.getNetwork() == null ? 0
				: Integer.valueOf(createVmBean.getNetwork()));
		if (createVmBean.getNetworkType() != null) {
			reference.setNetwork_type(createVmBean.getNetworkType());
		}
		reference.setImageId(createVmBean.getImageId());
		if (!StringUtils.isEmpty(createVmBean.getOsId())) {
			reference.setOsId(Integer.valueOf(createVmBean.getOsId()));
		}
		String radom_user = "administrator";
		reference.setFlavorId(createVmBean.getFlavorId());
		if (null != createVmBean.getCpuLimit()) {
			reference.setCpuLimit(createVmBean.getCpuLimit());
		}
		if (null != createVmBean.getDiskRead()) {
			reference.setDiskRead(createVmBean.getDiskRead());
		}
		if (null != createVmBean.getDiskWrite()) {
			reference.setDiskWrite(createVmBean.getDiskWrite());
		}
		reference.setBwtIn(createVmBean.getBwtIn());
		reference.setBwtOut(createVmBean.getBwtOut());
		reference.setIpConnIn(createVmBean.getIpConnIn());
		reference.setIpConnOut(createVmBean.getIpConnOut());
		reference.setTcpConnIn(createVmBean.getTcpConnIn());
		reference.setTcpConnOut(createVmBean.getTcpConnOut());
		reference.setUdpConnIn(createVmBean.getUdpConnIn());
		reference.setUdpConnOut(createVmBean.getUdpConnOut());
		reference.setTryTime(0);
		List<Image> images = openstackUtil.getCompute().images().get()
				.getList();
		for (Image im : images) {
			if (reference.getImageId().equals(im.getId())) {
				Map<String, String> m = im.getMetadata();
				radom_user = m.get("username");
				if (StringUtils.isEmpty(radom_user)) {
					if ("Linux".equals(m.get("os_type"))) {
						radom_user = "root";
					}
				}
				// radom_pwd = m.get("passwd");
				break;
			}
		}
		reference.setRadom_user(radom_user);
		reference.setRadom_pwd(PasswordUtil.getRandomNum(12));

		return reference;
	}

	private VpdcReference createVmReferenceBean(CreateVmBean createVmBean,
			Long uid, String vmId, String createrType) throws HsCloudException {
		Date d = new Date();
		VpdcReference reference = new VpdcReference();
		reference.setCreateId(0L);
		reference.setCreaterType(createrType);
		reference.setCreateDate(d);
		reference.setUpdateId(uid);
		reference.setUpdateDate(d);
		reference.setBuyType(createVmBean.getBuyType());
		if (createVmBean.getVpdcId() != null) {
			Vpdc v = new Vpdc();
			v.setId(createVmBean.getVpdcId());
			reference.setVpdc(v);
		}
		if (Constants.USER.contains(createrType)) {
			String heardDomainIdDate = vpdcDao.getDomainIdByUserId(uid);
			String serialNum = vpdcDao
					.getVMNameSerialNumber(Constants.T_VM_ARRANGING_NAME);
			createVmBean.setName(heardDomainIdDate + serialNum);
		}

		// 获取资源充沛的zone
		// String optinalZone = createVmBean.getVmZone();
		// if(!StringUtils.isEmpty(createVmBean.getVmNode())){
		// optinalZone+="$"+createVmBean.getVmNode();
		// }
		// if(Constants.USER.equals(createrType)){
		// optinalZone = this.getOptimalZoneOfZones(createVmBean.getVmZone());
		// }
		// if(!StringUtils.isEmpty(optinalZone)){
		// reference.setVmZone(optinalZone);
		// }else{
		// throw new HsCloudException(Constants.VM_ZONE_NO_ENOUGH,
		// "the resource of zone is not enough!", log, null);
		// }
		if (createVmBean.getVmType() != null) {
			reference.setVm_type(createVmBean.getVmType());
		} else {
			reference.setVm_type(VMTypeEnum.REGULAR.getCode());
		}
		if (createVmBean.getVmBussiness() != null) {
			reference.setVm_business_status(createVmBean.getVmBussiness());
		}
		reference.setTryLong(createVmBean.getTryLong());
		reference.setScId(createVmBean.getScId());
		if (!StringUtils.isEmpty(createVmBean.getOwner())) {
			reference.setOwner(Long.valueOf(createVmBean.getOwner()));
		}
		reference.setName(createVmBean.getName());
		reference.setCpu_core(Integer.valueOf(createVmBean.getVcpus()));
		if (createVmBean.getVcpusType() != null) {
			reference.setCpu_type(createVmBean.getVcpusType());
		}

		if (StringUtils.isNotBlank(createVmBean.getDisk())) {
			reference.setDisk_capacity(Integer.valueOf(createVmBean.getDisk()));
		}
		if (createVmBean.getDiskType() != null) {
			reference.setDisk_type(createVmBean.getDiskType());
		}
		reference.setMem_size(Integer.valueOf(createVmBean.getRam()));
		if (createVmBean.getVcpusType() != null) {
			reference.setMem_type(createVmBean.getRamType());
		}
		reference.setNetwork_bandwidth(createVmBean.getNetwork() == null ? 0
				: Integer.valueOf(createVmBean.getNetwork()));
		if (createVmBean.getNetworkType() != null) {
			reference.setNetwork_type(createVmBean.getNetworkType());
		}
		reference.setImageId(createVmBean.getImageId());
		if (!StringUtils.isEmpty(createVmBean.getOsId())) {
			reference.setOsId(Integer.valueOf(createVmBean.getOsId()));
		}
		reference.setIpNum(createVmBean.getIpNum() == null ? 0 : Integer
				.valueOf(createVmBean.getIpNum()));
		reference.setFlavorId(createVmBean.getFlavorId());
		reference.setVm_status(Constants.VM_STATUS_BUILDING);
		reference.setVm_task_status(Constants.VM_STATUS_BUILDING);
		if (null != createVmBean.getCpuLimit()) {
			reference.setCpuLimit(createVmBean.getCpuLimit());
		}
		if (null != createVmBean.getDiskRead()) {
			reference.setDiskRead(createVmBean.getDiskRead());
		}
		if (null != createVmBean.getDiskWrite()) {
			reference.setDiskWrite(createVmBean.getDiskWrite());
		}
		reference.setBwtIn(createVmBean.getBwtIn());
		reference.setBwtOut(createVmBean.getBwtOut());
		reference.setIpConnIn(createVmBean.getIpConnIn());
		reference.setIpConnOut(createVmBean.getIpConnOut());
		reference.setTcpConnIn(createVmBean.getTcpConnIn());
		reference.setTcpConnOut(createVmBean.getTcpConnOut());
		reference.setUdpConnIn(createVmBean.getUdpConnIn());
		reference.setUdpConnOut(createVmBean.getUdpConnOut());
		reference.setTryTime(0);
		// reference.setVm_outerIP(createVmBean.getFloating_ip());

		String radom_user = "administrator";
		List<Image> images = openstackUtil.getCompute().images().get()
				.getList();
		for (Image im : images) {
			if (reference.getImageId().equals(im.getId())) {
				Map<String, String> m = im.getMetadata();
				radom_user = m.get("username");
				if (StringUtils.isEmpty(radom_user)) {
					if ("Linux".equals(m.get("os_type"))) {
						radom_user = "root";
					}
				}
				// radom_pwd = m.get("passwd");
				break;
			}
		}
		reference.setRadom_user(radom_user);
		reference.setRadom_pwd(PasswordUtil.getRandomNum(12));

		VpdcInstance instance = new VpdcInstance();
		instance.setVpdcreference(reference);
		instance.setCreateId(uid);
		instance.setCreateDate(d);
		instance.setUpdateId(uid);
		instance.setUpdateDate(d);
		if (!StringUtils.isEmpty(createVmBean.getVmNode())) {
			instance.setNodeName(createVmBean.getVmNode());
		}
		Set<VpdcInstance> instances = new HashSet<VpdcInstance>();
		instance.setVmId(vmId);
		instance.setInitVmId(vmId);
		instances.add(instance);
		reference.setInstances(instances);
		return reference;
	}

	private VpdcReference AppCreateVmReferenceBean(CreateVmBean createVmBean,
			Long userId, Long supplierId, String vmId, String createrType)
			throws HsCloudException {
		Date d = new Date();
		VpdcReference reference = new VpdcReference();
		reference.setCreateId(userId);
		reference.setCreaterType(createrType);
		reference.setCreateDate(d);
		reference.setUpdateId(supplierId);
		reference.setUpdateDate(d);
		reference.setBuyType(createVmBean.getBuyType());
		if (createVmBean.getVpdcId() != null) {
			Vpdc v = new Vpdc();
			v.setId(createVmBean.getVpdcId());
			reference.setVpdc(v);
		}
		if (Constants.USER.contains(createrType)) {
			String heardDomainIdDate = vpdcDao.getDomainIdByUserId(userId);
			String serialNum = vpdcDao
					.getVMNameSerialNumber(Constants.T_VM_ARRANGING_NAME);
			createVmBean.setName(heardDomainIdDate + serialNum);
		}
		if (createVmBean.getVmBussiness() != null) {
			reference.setVm_business_status(createVmBean.getVmBussiness());
		}
		reference.setVm_type(VMTypeEnum.REGULAR.getCode());
		reference.setTryLong(createVmBean.getTryLong());
		reference.setScId(createVmBean.getScId());
		reference.setOwner(supplierId);
		reference.setName(createVmBean.getName());
		reference.setCpu_core(Integer.valueOf(createVmBean.getVcpus()));
		if (createVmBean.getVcpusType() != null) {
			reference.setCpu_type(createVmBean.getVcpusType());
		}

		if (StringUtils.isNotBlank(createVmBean.getDisk())) {
			reference.setDisk_capacity(Integer.valueOf(createVmBean.getDisk()));
		}
		if (createVmBean.getDiskType() != null) {
			reference.setDisk_type(createVmBean.getDiskType());
		}
		reference.setMem_size(Integer.valueOf(createVmBean.getRam()));
		if (createVmBean.getVcpusType() != null) {
			reference.setMem_type(createVmBean.getRamType());
		}
		reference.setNetwork_bandwidth(createVmBean.getNetwork() == null ? 0
				: Integer.valueOf(createVmBean.getNetwork()));
		if (createVmBean.getNetworkType() != null) {
			reference.setNetwork_type(createVmBean.getNetworkType());
		}
		reference.setImageId(createVmBean.getImageId());
		if (!StringUtils.isEmpty(createVmBean.getOsId())) {
			reference.setOsId(Integer.valueOf(createVmBean.getOsId()));
		}
		reference.setIpNum(createVmBean.getIpNum() == null ? 0 : Integer
				.valueOf(createVmBean.getIpNum()));
		reference.setFlavorId(createVmBean.getFlavorId());
		reference.setVm_status(Constants.VM_STATUS_BUILDING);
		reference.setVm_task_status(Constants.VM_STATUS_BUILDING);
		if (null != createVmBean.getCpuLimit()) {
			reference.setCpuLimit(createVmBean.getCpuLimit());
		}
		if (null != createVmBean.getDiskRead()) {
			reference.setDiskRead(createVmBean.getDiskRead());
		}
		if (null != createVmBean.getDiskWrite()) {
			reference.setDiskWrite(createVmBean.getDiskWrite());
		}
		reference.setBwtIn(createVmBean.getBwtIn());
		reference.setBwtOut(createVmBean.getBwtOut());
		reference.setIpConnIn(createVmBean.getIpConnIn());
		reference.setIpConnOut(createVmBean.getIpConnOut());
		reference.setTcpConnIn(createVmBean.getTcpConnIn());
		reference.setTcpConnOut(createVmBean.getTcpConnOut());
		reference.setUdpConnIn(createVmBean.getUdpConnIn());
		reference.setUdpConnOut(createVmBean.getUdpConnOut());
		reference.setTryTime(0);
		// reference.setVm_outerIP(createVmBean.getFloating_ip());

		String radom_user = "administrator";
		List<Image> images = openstackUtil.getCompute().images().get()
				.getList();
		for (Image im : images) {
			if (reference.getImageId().equals(im.getId())) {
				Map<String, String> m = im.getMetadata();
				radom_user = m.get("username");
				if (StringUtils.isEmpty(radom_user)) {
					if ("Linux".equals(m.get("os_type"))) {
						radom_user = "root";
					}
				}
				// radom_pwd = m.get("passwd");
				break;
			}
		}
		reference.setRadom_user(radom_user);
		reference.setRadom_pwd(PasswordUtil.getRandomNum(12));

		VpdcInstance instance = new VpdcInstance();
		instance.setVpdcreference(reference);
		instance.setCreateId(supplierId);
		instance.setCreateDate(d);
		instance.setUpdateId(supplierId);
		instance.setUpdateDate(d);
		if (!StringUtils.isEmpty(createVmBean.getVmNode())) {
			instance.setNodeName(createVmBean.getVmNode());
		}
		Set<VpdcInstance> instances = new HashSet<VpdcInstance>();
		instance.setVmId(vmId);
		instance.setInitVmId(vmId);
		instances.add(instance);
		reference.setInstances(instances);
		return reference;
	}

	@Override
	@Transactional(readOnly = false)
	public boolean autoMigrateVm(String uuid) throws HsCloudException {
		boolean result = false;
		boolean updateFlag = false;
		try {
			updateFlag = vpdcDao.updateVmNodeName("", uuid);
			ServerResource serverResource = openstackUtil.getCompute()
					.servers().server(uuid);
			serverResource.migrate();
			result = updateFlag;
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_AUTO_MIGRATE_ERROR,
					"autoMigrateVm Exception", log, e);
		}
		return result;
	}

	@Override
	public boolean resizeVm(String uuid, FlavorVO flavorVO, String addDisk,
			AbstractUser a) throws HsCloudException {
		boolean result = false;
		String ipNum = "0";
		try {
			// Admin a = adminDao.get(adminid);
			if (!"".equals(addDisk)) {
				this.createAndAttachExttendDiskByScsis(addDisk, uuid, a);
			}
			// 修改带宽
			if (!"".equals(flavorVO.getBandwidth())) {
				this.modifyBandwidthOfVm(uuid, flavorVO.getBandwidth());
			}
			// 增加IP
			if (!"".equals(flavorVO.getIp())) {
				ipNum = flavorVO.getIp();
			}
			String flavorId = createFlavor(flavorVO);
			// ServerResource serverResource =
			// openstackUtil.getCompute().servers().server(uuid);
			// 旧的调整接口
			// serverResource.resize(flavorId);
			// 新的调整接口
			// serverResource.hsResize(Integer.valueOf(flavorId));

			VpdcReference vpdcReference = vpdcDao.findVpdcReferenceByVmId(uuid);
			if (vpdcReference != null
					&& (!vpdcReference.getFlavorId().equals(flavorId))) {
				vpdcReference.setFlavorId(flavorId);
				vpdcReference.setCpu_core(flavorVO.getVcpus());
				vpdcReference.setMem_size(flavorVO.getRam());
				vpdcReference.setDisk_capacity(flavorVO.getDisk());
				vpdcReference.setCpu_type(flavorVO.getVcpusType());
				vpdcDao.updateVpdcReference(vpdcReference);
				// 调用RebbitMQ
				HcEventResource her = new HcEventResource();
				her.setEvent_time(new Date());
				her.setType(LogResourceOperationType.RESET.getIndex());// 调整
				her.setRes_type(LogResourceType.FLAVOR.getIndex());// 操作VM
				her.setBiz_type(LogBizType.FLAVOR_ADJUST.getIndex());// 管理员创建
				her.setObj_name(vpdcReference.getName());
				her.setObj_id(vpdcReference.getId());
				her.setOperator(a.getEmail());
				her.setOperator_id(a.getId());
				her.setOperator_type(LogOperatorType.ADMIN.getIndex());// 后台操作
				Map<String, String> flavorMap = new HashMap<String, String>();
				flavorMap.put("uuid", uuid);
				flavorMap.put("flavorId", flavorId);
				flavorMap.put("ipNum", ipNum);
				rabbitMqUtil.sendMessage(Constants.JOBSERVER_METHOD_VMReset,
						flavorMap, her, "HcEventResource");
				result = true;
			}

		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_RESIZE_ERROR,
					"resizeVm Exception", log, e);
		}
		return result;
	}

	@Override
	public List<VpdcInstance> getVmIdsByNodeName(String nodeName,
			List<Object> referenceIds) throws HsCloudException {
		List<VpdcInstance> vpdcInstance = null;
		try {
			vpdcInstance = vpdcDao.getVmIdsByNodeName(nodeName, referenceIds);
		} catch (Exception e) {
			throw new HsCloudException(Constants.MONITOR_VM_DETAILS_EXCEPTION,
					"getVmIdsByNodeName异常", log, e);
		}
		return vpdcInstance;
	}

	@Override
	public List<VpdcReference> getCompanyRefefenceByUserId(long uid)
			throws HsCloudException {
		List<VpdcReference> references = null;
		try {
			Company c = companyService.getCompanyByUserId(uid);
			references = vpdcDao.findReferenceByCompanyId(c.getId(), null);
		} catch (Exception e) {
			throw new HsCloudException("OPS-Service001",
					"getCompanyRefefenceByUserId异常", log, e);
		}

		return references;
	}

	@Override
	@Transactional(readOnly = false)
	public boolean confirmMigrateVm(String uuid, boolean confirmFlag)
			throws HsCloudException {
		boolean result = false;
		try {
			// server = openstackUtil.getCompute().servers().server(uuid).get();
			ServerResource serverResource = openstackUtil.getCompute()
					.servers().server(uuid);
			if (confirmFlag) {
				serverResource.confirmResize();
				// serverResource.hsResizeConfirm();
			} else {
				serverResource.revertResize();
			}
			if (serverResource.get() != null) {
				String hostName = serverResource.get().getHostName();
				log.info("new vm hostName:" + hostName);
				boolean updateFlag = vpdcDao.updateVmNodeName(hostName, uuid);
				result = updateFlag;
			}
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_CONFIRM_MIGRATE_ERROR,
					"confirmMigrateVm Exception", log, e);
		}
		return result;
	}

	@Override
	@Transactional(readOnly = false)
	public boolean updateHostNameOfVm(String uuid) throws HsCloudException {
		boolean result = false;
		try {
			// server = openstackUtil.getCompute().servers().server(uuid).get();
			ServerResource serverResource = openstackUtil.getCompute()
					.servers().server(uuid);
			if (serverResource.get() != null) {
				String hostName = serverResource.get().getHostName();
				if (log.isDebugEnabled()) {
					log.debug("new vm hostName:" + hostName);
				}
				boolean updateFlag = vpdcDao.updateVmNodeName(hostName, uuid);
				result = updateFlag;
			}
		} catch (Exception e) {
			throw new HsCloudException(Constants.SC_UPDATE_NODENAME_ERROR,
					"updateHostNameOfVm Exception", log, e);
		}
		return result;
	}

	public static int index = 0;

	@Override
	public void appExpireRemind() throws HsCloudException {

	}

	@Override
	public void expireRemind() {
		List<ExpireRemindVO> list = vpdcDao.findForExpireRemind();

		String remindForYear = siteConfigService.getConfigValue(
				Constants.CONFIG_VM_beforeDayRemindForY, "ops");
		String remindForMonth = siteConfigService.getConfigValue(
				Constants.CONFIG_VM_beforeDayRemindForM, "ops");

		for (ExpireRemindVO expireRemindVO : list) {
			expireRemind(expireRemindVO, remindForYear, remindForMonth);
		}
	}

	@Transactional(readOnly = false)
	public void expireRemind(ExpireRemindVO expireRemindVO,
			String remindForYear, String remindForMonth) {
		String orderNo = "";// 订单编号
		int orderPeriod = 1;// 订单周期
		boolean recordAddFlag = false;
		try {
			long referenceId = expireRemindVO.getReferenceId();
			Date startTime = expireRemindVO.getStartTime();
			Date endTime = expireRemindVO.getEndTime();
			Date now = new Date();
			// 使用时长
			long useLong = (now.getTime() - startTime.getTime())
					/ (1000 * 3600 * 24);
			log.debug("useLong" + useLong);
			// 剩余时长
			long spareExpireL = endTime.getTime() - now.getTime();// 计算毫秒
			long spareExpire = spareExpireL / (1000 * 3600 * 24);// 计算整天数
			// log.info("spareExpire"+spareExpire);
			long spareRemainder = spareExpireL % (1000 * 3600 * 24);// 计算不足1天数值
			if (spareRemainder > 0) {// 大于0有不足1天，需按1天算加1；等于0表示没有不需加1
				spareExpire = spareExpire + 1;
			}
			log.debug("##############spareExpire:" + spareExpire);
			String msgInfo = "您使用的云主机【" + expireRemindVO.getName() + "】还差"
					+ (spareExpire > 0 ? spareExpire : 0) + "天到期";
			// 监听云主机到期后的提醒方案:按月[7,1];按季、半年、年[30,15,3,1]

			// 正式购买
			if ("1".equals(expireRemindVO.getVmType())) {
				InstanceDetail_ServiceCatalogVo idscv = null;
				List<VpdcReference_OrderItem> lvroi = vpdcDao
						.getOrderItemsByReferenceId(referenceId);
				if (lvroi != null) {
					idscv = vpdcDao.findVmServiceCatalog(lvroi);
					;
				}
				if (idscv != null) {
					orderNo = idscv.getOrderNo();
					if (Constants.scPriceType_MONTH.equals(idscv
							.getPriceTypeName())) {
						orderPeriod = idscv.getPeriod();
					}
				}
			}

			String remindCycle = "";
			String[] dayArr = null;
			// 按季、半年、年
			if (orderPeriod >= 3) {
				remindCycle = remindForYear;
			} else {// 按月、试用云主机按月获取提醒周期
				remindCycle = remindForMonth;
			}

			log.debug("##############remindCycle" + remindCycle);
			if (!StringUtils.isEmpty(remindCycle)) {
				dayArr = remindCycle.split(",");
				if (dayArr != null) {
					// 循环查找当前日期符合哪个提醒周期
					for (int i = dayArr.length - 1; i >= 0; i--) {
						if (Long.valueOf(dayArr[i]) >= spareExpire) {// 查找到后，不再进行其它的周期范围对比，效率较高
							recordAddFlag = true;
							// 判断是否在该提醒周期中发过邮件
							boolean bl = vpdcDao.ifSendMail(referenceId,
									Integer.valueOf(dayArr[i]), endTime);
							if (!bl) {
								// 发消息
								sendMsgForTrialExpire(
										expireRemindVO.getUserId(), msgInfo);
								// 发邮件
								sendEmailForVmTrialExpire(
										expireRemindVO,
										String.valueOf(useLong),
										String.valueOf((spareExpire > 0 ? spareExpire
												: 0)));
							}
							break;
						}
					}
				}
			}
			// 将做到期提醒的数据添加到续订业务表中
			if (recordAddFlag) {
				VpdcRenewal vpdcRenewal = null;
				vpdcRenewal = vpdcRenewalService
						.getVpdcRenewalByReferenceId(referenceId);
				Map<String, Object> condition = new HashMap<String, Object>();
				condition.put("name", expireRemindVO.getName());
				condition.put("referenceId", referenceId);
				condition.put("orderNo", orderNo);
				condition.put("vmUuid", expireRemindVO.getVmId());
				condition.put("businessType", expireRemindVO.getVmType());
				condition.put("businessStatus",
						expireRemindVO.getVmBusinessStatus());
				condition.put("startTime", startTime);
				condition.put("endTime", endTime);
				condition.put("updateDate", new Date());
				condition.put("userId", expireRemindVO.getUserId());
				condition.put("serviceCatalogId", expireRemindVO.getScId());
				condition.put("isEnable", expireRemindVO.getIsEnable());
				condition.put("buyType", 0);
				if (expireRemindVO.getBuyType() != null) {
					condition.put("buyType", expireRemindVO.getBuyType());
				}

				if (vpdcRenewal == null) {
					condition.put("createDate", new Date());
					vpdcRenewalService.saveVpdcRenewal(condition);
				} else {
					condition.put("id", vpdcRenewal.getId());
					vpdcRenewalService.updateVpdcRenewal(condition);
				}
			}
		} catch (Exception e) {
			log.error("expireRemind exception", e);
		}
	}

	/**
	 * @author lihonglei <云主机使用到期消息提醒> <功能详细描述>
	 * @param u
	 * @param msgInfo
	 * @see [类、类#方法、类#成员]
	 */
	private void sendMsgForTrialExpire(long userId, String msgInfo) {
		Message msg = new Message();
		msg.setMessage(msgInfo);
		msg.setMessageType(3);
		msg.setUserId(userId);
		messageService.saveMessage(msg);
	}

	/**
	 * <云主机使用到期消息提醒> <功能详细描述>
	 * 
	 * @param u
	 * @param msgInfo
	 * @see [类、类#方法、类#成员]
	 */
	private void sendMsgForTrialExpire(User u, String msgInfo) {
		Message msg = new Message();
		msg.setMessage(msgInfo);
		msg.setMessageType(3);
		msg.setUserId(u.getId());
		messageService.saveMessage(msg);
	}

	/**
	 * <VM试用到期提醒发邮件给用户> <功能详细描述>
	 * 
	 * @param user
	 * @param vmName
	 * @param elapsed
	 * @param left
	 * @see [类、类#方法、类#成员]
	 */
	@Transactional(readOnly = true)
	private void sendEmailForVmTrialExpire(ExpireRemindVO expireRemindVO,
			String elapsed, String left) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// yyyy-MM-dd
																			// hh:mm:ss
		Date d = new Date();
		Map<String, String> map = new HashMap<String, String>();
		map.put("greeting_name", expireRemindVO.getUserName());

		map.put("vmName", expireRemindVO.getName());
		map.put("day_elapsed", elapsed);
		map.put("day_left", left);
		map.put("today", dateFormat.format(d));

		Domain domain = domainService.getDomainById(expireRemindVO
				.getDomainId());
		map.put("abbreviation", domain.getAbbreviation());
		map.put("name", domain.getName());
		map.put("telephone", domain.getTelephone());
		map.put("address", domain.getAddress());
		map.put("url", domain.getUrl());
		map.put("bank", domain.getBank());
		map.put("cardNo", domain.getCardNo());
		map.put("serviceHotline", domain.getServiceHotline());
		// map.put("webSiteUrl",domain.getPublishingAddress());
		map.put("webSiteUrl", "http://117.122.193.11/");
		map.put("image", "resource/uploads/");
		// map.put("image", Constants.MAIL_LOGO_SRC.replaceAll("xxx",
		// Long.toString(domain.getId())));
		long mailId = mailService.saveEmail(expireRemindVO.getEmail(), null,
				MailTemplateType.TRIALVM_EXPIRE_TEMPLATE.getType(),
				domain.getId(), map);
		VmExpireEmailLog veel = new VmExpireEmailLog();
		veel.setCreateTime(d);
		veel.setReferenceId(expireRemindVO.getReferenceId());
		veel.setMailId(mailId);
		vpdcDao.saveMailLogForExpire(veel);

		/*
		 * MailTemplate template =
		 * mailService.getMailTemplate(MailTemplateType.TRIALVM_EXPIRE_TEMPLATE
		 * .getType()); mailService.saveEmail(user.getEmail(), null,
		 * template.getTitle(), template.getReplacedTemplate(map));
		 */
	}

	@Override
	public boolean updateIPOfVm(String uuid, String oldIP, String newIP,
			Long adminId) throws HsCloudException {
		boolean result = false;
		try {
			Admin a = adminDao.get(adminId);
			// ServerResource serverResource =
			// openstackUtil.getCompute().servers().server(uuid);
			ControlPanelUser cpu = controlPanelDao.getCPByVmId(uuid);
			String cpu_ip = "";
			String vmName = "";
			// 如果增加了新的IP，则绑定新的IP，同时同步CP用户IP
			if (!StringUtils.isEmpty(newIP)) {
				VpdcInstance vpdcInstance = vpdcDao.findVmByVmId(uuid);
				vmName = vpdcInstance.getVpdcreference().getName();
				// IP状态置为【待分配】
				vpdcDao.resetIPstatus(1, adminId, newIP);
				// 更新IP其它信息
				vpdcDao.updateIpDetailByIp(vpdcInstance.getId(), 0, adminId, 0,
						IPConvert.getIntegerIP(newIP));
				if (cpu != null) {
					cpu_ip = cpu.getVmIP();
					String newIP_ = "";
					if (!StringUtils.isEmpty(cpu_ip) && !cpu_ip.contains(",")) {
						newIP_ = "," + newIP;
					}
					cpu_ip = cpu_ip + newIP_;
					cpu.setVmIP(cpu_ip);
					cpu.setUpdateDate(new Date());
					controlPanelDao.saveControlUser(cpu);
				}
			} else if (!"".equals(oldIP)) {
				// 如果存在旧的IP，现将其删除，同时同步CP用户IP
				// 释放ip为【待释放】
				vpdcDao.resetIPstatus(4, adminId, oldIP);
				if (cpu != null) {
					cpu_ip = cpu.getVmIP();
					cpu_ip = cpu_ip.replace(oldIP, "");
					cpu.setVmIP(cpu_ip);
					cpu.setUpdateDate(new Date());
					controlPanelDao.saveControlUser(cpu);
				}
			}
			// 调用RebbitMQ
			HcEventResource her = new HcEventResource();
			her.setEvent_time(new Date());
			her.setType(LogResourceOperationType.RESET.getIndex());// 重置
			her.setRes_type(LogResourceType.IP.getIndex());// 操作VM
			her.setBiz_type(LogBizType.IP_ADD.getIndex());// 外网IP调整
			// her.setObj_name("oldIP:"+oldIP+";newIP:"+newIP);
			her.setObj_name(vmName);
			her.setOperator(a.getEmail());
			her.setOperator_id(a.getId());
			her.setOperator_type(LogOperatorType.ADMIN.getIndex());
			Map<String, String> ipMap = new HashMap<String, String>();
			ipMap.put("uuid", uuid);
			ipMap.put("oldIP", oldIP);
			ipMap.put("newIP", newIP);
			rabbitMqUtil.sendMessage(Constants.JOBSERVER_METHOD_IPreset, ipMap,
					her, "HcEventResource");
			result = true;
		} catch (Exception e) {
			throw new HsCloudException(Constants.OPTIONS_FAILURE,
					"updateIPOfVm Exception", log, e);
		}
		return result;
	}

	@Override
	public List<VolumeVO> getAttachVolumesOfVm(String vmId)
			throws HsCloudException {
		VolumeVO volumeVO = null;
		List<VolumeVO> VolumeVOList = new ArrayList<VolumeVO>();
		List<Volume> volumes = openstackUtil.getCompute().volumes().get()
				.getList();
		String serverId = "";
		for (Volume volume : volumes) {
			serverId = volume.getAttachments().get(0).getServerId();
			if (StringUtils.isEmpty(serverId)) {
				continue;
			}
			if (vmId.equals(serverId)) {
				volumeVO = new VolumeVO();
				volumeVO.setId(volume.getId());
				volumeVO.setName(volume.getName());
				volumeVO.setSize(volume.getSizeInGB());
				volumeVO.setStatus(volume.getStatus());
				volumeVO.setVmId(vmId);
				volumeVO.setDevice(volume.getAttachments().get(0).getDevice());
				volumeVO.setMode(1);
				VolumeVOList.add(volumeVO);
			}
		}
		return VolumeVOList;
	}

	@Override
	public boolean dettachVolume(String vmId, int volumId, int volumeMode,
			AbstractUser a) {
		boolean result = false;
		try {
			// 删除isics方式扩展盘
			if (volumeMode == 1) {
				ServerResource serverResource = openstackUtil.getCompute()
						.servers().server(vmId);
				serverResource.attachments().attachment(volumId).delete();
				final int volumeId = volumId;
				new Thread(new Runnable() {
					@Override
					public void run() {
						if (waitForVolumeState("available", volumeId)) {
							openstackUtil.getCompute().volumes()
									.volume(volumeId).delete();
						}
					}
				}).start();
			}
			// 删除SCIS方式扩展盘
			if (volumeMode == 2) {
				this.deleteAndDetachExttendDiskByScsis(vmId, volumId, a);
			}
			result = true;
		} catch (Exception e) {
			throw new HsCloudException(Constants.OPTIONS_FAILURE,
					"dettachVolume", log, e);
		}
		return result;
	}

	@Override
	public VpdcReference_OrderItem getOrderItemByReferenceId(long referenceId) {
		VpdcReference_OrderItem vroi = null;
		VpdcReference vr = vpdcDao.findVpdcReferenceById(referenceId);
		if (vr != null) {
			vroi = vpdcDao.getOrderItemByReferenceId(vr.getId());
		}
		return vroi;
	}

	@Override
	public VpdcReference getReferenceByOrderItemId(Long orderItemId)
			throws HsCloudException {
		return vpdcDao.getReferenceByOrderItemId(orderItemId);
	}

	@Override
	public VpdcReference getReferenceByVmId(String vmId) {
		return vpdcDao.findVpdcReferenceByVmId(vmId);
	}

	@Override
	public VpdcReference getReferenceByVmName(String vmName) {
		return vpdcDao.findVpdcReferenceByVmName(vmName);
	}

	@Override
	public VpdcReference getReferenceById(long referenceId) {
		return vpdcDao.findVpdcReferenceById(referenceId);
	}

	@Override
	public VpdcInstance getInstanceByVmId(String vmId) throws HsCloudException {
		return vpdcDao.findVmByVmId(vmId);
	}

	@Override
	public VpdcInstance getInstanceByVmName(String vmName)
			throws HsCloudException {
		VpdcReference vr = vpdcDao.findVpdcReferenceByVmName(vmName);
		if (vr != null) {
			return this.getActiveVmFromVpdcReference(vr);
		}
		return null;
	}

	@Override
	public List<VpdcReference> findReferencesByOrderItems(
			List<Long> orderItemIds) throws HsCloudException {
		return vpdcDao.findReferencesByOrderItems(orderItemIds);
	}

	@Override
	public VpdcReference_OrderItem getOrderItemByOrderItemId(String orderItemId) {
		return vpdcDao.getOrderItemByOrderItemId(orderItemId);
	}

	@Override
	public boolean updateReference_OrderItem(long referenceId,
			String orderItemId) throws HsCloudException {
		VpdcReference_OrderItem vroi = null;
		vroi = vpdcDao.getOrderItemByReferenceId(referenceId);
		if (vroi == null) {
			vroi = new VpdcReference_OrderItem();
			vroi.setVpdcRenferenceId(referenceId);
		}
		vroi.setOrder_item_id(orderItemId);
		return vpdcDao.saveReferenceOrderItem(vroi);
	}

	@Override
	public boolean saveReference_OrderItem(long referenceId, String orderItemId) {
		VpdcReference_OrderItem vroi = new VpdcReference_OrderItem();
		vroi.setVpdcRenferenceId(referenceId);
		vroi.setOrder_item_id(orderItemId);
		return vpdcDao.saveReferenceOrderItem(vroi);
	}

	@Override
	public boolean updateReference_OrderItem(String oldOrderItemId,
			String newOrderItemId) {
		VpdcReference_OrderItem vroi = vpdcDao
				.getOrderItemByOrderItemId(oldOrderItemId);
		vroi.setOrder_item_id(newOrderItemId);
		return vpdcDao.saveReferenceOrderItem(vroi);
	}

	@Override
	public boolean updateReferencePeriod(long referenceId, Date start_time,
			Date end_time) {
		boolean bl = false;
		VpdcReference_Period vrp = null;
		try {
			vrp = vpdcDao.getReferencePeriod(referenceId);
			if (vrp == null) {
				vrp = new VpdcReference_Period();
			}
			vrp.setVpdcreferenceId(referenceId);
			if (start_time != null) {
				vrp.setStartTime(start_time);
			}
			if (end_time != null) {
				vrp.setEndTime(end_time);
			}
			vpdcDao.updateReferencePeriod(vrp);
			bl = true;
		} catch (Exception e) {
			log.error("OPS-ServiceImpl-updateReferencePeriod Exception", e);
		}
		return bl;
	}

	@Override
	public boolean updateReferencePeriod(String orderItemId, Date start_time,
			Date end_time) {
		boolean bl = false;
		try {
			VpdcReference vr = vpdcDao.getReferenceByOrderItemId(Long
					.parseLong(orderItemId));
			if (vr != null && vr.getStatus() == 0) {
				bl = updateReferencePeriod(vr.getId(), start_time, end_time);
			} else {
				throw new HsCloudException(Constants.VM_TERMINATE_ALREADY,
						"updateReferencePeriod", log, new Exception());
			}
		} catch (NumberFormatException e) {
			log.error("OPS-ServiceImpl-updateReferencePeriod Exception", e);
		}
		return bl;
	}

	/**
	 * @param vmId
	 * @return
	 */
	@Override
	public boolean freezeVM(String vmId, long uid, int freezeType) {
		boolean bl = false;
		// 关闭VM
		this.closeVm(vmId, null, null);
		// 禁用VM
		try {
			VpdcReference vr = vpdcDao.findVpdcReferenceByVmId(vmId);
			if (vr != null) {
				vr.setIsEnable(freezeType);
				vr.setUpdateId(uid);
				vr.setUpdateDate(new Date());
				vpdcDao.updateVpdcReference(vr);
			}
			bl = true;
		} catch (HsCloudException e) {
			log.error("OPS-ServiceImpl-freezeVM Exception", e);
		}
		return bl;
	}

	/**
	 * @param vmId
	 * @return
	 */
	@Override
	public boolean activeVM(String vmId, long uid) {
		boolean bl = false;
		try {
			VpdcReference vr = vpdcDao.findVpdcReferenceByVmId(vmId);
			vr.setIsEnable(Constants.VM_ISENABLE_TURE);
			vr.setUpdateId(uid);
			vr.setUpdateDate(new Date());
			vpdcDao.updateVpdcReference(vr);
			bl = true;
		} catch (HsCloudException e) {
			log.error("OPS-ServiceImpl-activeVM Exception", e);
		}
		return bl;
	}

	public boolean activeExpireVM(VpdcReference vr, long uid) {
		boolean bl = false;
		try {
			if (vr.getStatus() == 0) {
				if (Constants.VM_ISENABLE_FALSE_EXPIRE == vr.getIsEnable()) {
					vr.setIsEnable(Constants.VM_ISENABLE_TURE);
					vr.setUpdateId(uid);
					vr.setUpdateDate(new Date());
					vpdcDao.updateVpdcReference(vr);
				}
				bl = true;
			} else {
				throw new HsCloudException(Constants.VM_TERMINATE_ALREADY,
						"activeExpireVM", log, new Exception());
			}
		} catch (HsCloudException e) {
			log.error("OPS-ServiceImpl-activeVM Exception", e);
		}
		return bl;
	}

	@Override
	public boolean updateVmOwmer(long vmOwner, String vmId, long uid)
			throws HsCloudException {
		log.info("OPS-Service-updateVmOwmer start");
		VpdcReference vr = vpdcDao.findVpdcReferenceByVmId(vmId);
		vr.setOwner(vmOwner);
		vr.setUpdateDate(new Date());
		vr.setUpdateId(uid);
		vpdcDao.updateVpdcReference(vr);
		Resource re = resourceService.getResource(
				ResourceType.VM.getEntityName(), String.valueOf(vr.getId()));
		re.setUpdateId(uid);
		re.setUpdateDate(new Date());
		re.setOwnerId(vmOwner);
		resourceService.saveResource(re);
		return true;
	}

	@Override
	public boolean manualMigrateVm(String uuid, String hostName, Admin admin,
			String hostIP) throws HsCloudException {
		boolean result = false;
		boolean updateFlag = false;
		try {
			VpdcReference vr = vpdcDao.findVpdcReferenceByVmId(uuid);
			// 调用RebbitMQ
			HcEventResource her = new HcEventResource();
			her.setEvent_time(new Date());
			her.setType(LogResourceOperationType.MIGRATE.getIndex());// 迁移
			her.setRes_type(LogResourceType.VM.getIndex());// 操作VM
			her.setBiz_type(LogBizType.MANUAL_MIGRATE.getIndex());// 手动迁移
			// her.setObj_name("oldIP:"+oldIP+";newIP:"+newIP);
			her.setObj_name(vr.getName());
			her.setOperator(admin.getEmail());
			her.setOperator_id(admin.getId());
			her.setOperator_type(LogOperatorType.ADMIN.getIndex());
			Map<String, String> ipMap = new HashMap<String, String>();
			ipMap.put("uuid", uuid);
			ipMap.put("hostName", hostName);
			ipMap.put("hostIP", hostIP);
			rabbitMqUtil.sendMessage(Constants.JOBSERVER_METHOD_VMMigrate,
					ipMap, her, "HcEventResource");
			// updateFlag = vpdcDao.updateVmNodeName(hostName, uuid);
			// ServerResource serverResource =
			// openstackUtil.getCompute().servers().server(uuid);
			// serverResource.migrate2(hostName);
			result = updateFlag;
		} catch (Exception e) {
			throw new HsCloudException(Constants.OPTIONS_FAILURE,
					"manualMigrateVm Exception", log, e);
		}
		return result;
	}

	@Override
	public String getVmIpByVmId(String vmId) {
		String ip = "";
		try {
			// Server ipServer =
			// openstackUtil.getCompute().servers().server(vmId).get();
			VpdcReference vr = vpdcDao.findVpdcReferenceByVmId(vmId);
			ip = this.getIP(vr);
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_TERMINATE_ALREADY,
					"getVmIpByVmId异常", log, e);
		}
		return ip;
	}

	@Override
	public VpdcReference_Period getReferencePeriod(long referenceId) {
		return vpdcDao.getReferencePeriod(referenceId);
	}

	// @Override
	// @Transactional(readOnly = false)
	public void operateVmForExpired() {
		log.info("service-operateVmForExpired start");
		String afterDayForTryDisable = vpdcDao.getConfigValue(
				Constants.CONFIG_TRY_VM_DISABLE_DAY, "ops");
		String afterDayForTryTerminate = vpdcDao.getConfigValue(
				Constants.CONFIG_TRY_VM_TERMINATE_DAY, "ops");
		String afterDayForDisable = vpdcDao.getConfigValue(
				Constants.CONFIG_VM_DISABLE_DAY, "ops");
		String afterDayForTerminate = vpdcDao.getConfigValue(
				Constants.CONFIG_VM_TERMINATE_DAY, "ops");
		long referenceId = 0L;
		long userId = 0L;
		List<VpdcInstanceVO> vivList = vpdcDao
				.getVpdcInstanceVOForExpireOperate();
		for (VpdcInstanceVO viv : vivList) {
			try {
				referenceId = viv.getReferenceId();
				userId = viv.getUserId();
				// 剩余时长
				long spareExpireL = viv.getSpareTime();// 计算秒
				long spareExpire = spareExpireL / (3600 * 24);// 计算整天数
				log.debug("spareExpire" + spareExpire);
				long spareRemainder = spareExpireL % (3600 * 24);// 计算不足1天数值
				if (spareRemainder < 0) {// 小于0有不足1天，需按1天算减1；等于0表示没有不需减1
					spareExpire = spareExpire - 1;
				}
				log.debug("##############spareExpire" + spareExpire);
				// 监听云主机到期后的操作
				long afterDayOfDisable = 0L;
				long afterDayOfTerminate = 0L;
				// //试用云主机
				if (viv.getVmType() == 0) {
					afterDayOfDisable = Long
							.valueOf(afterDayForTryDisable == null ? "0"
									: afterDayForTryDisable);
					afterDayOfTerminate = Long
							.valueOf(afterDayForTryTerminate == null ? "0"
									: afterDayForTryTerminate);
				}
				// //正式云主机
				if (viv.getVmType() == 1) {
					afterDayOfDisable = Long
							.valueOf(afterDayForDisable == null ? "0"
									: afterDayForDisable);
					afterDayOfTerminate = Long
							.valueOf(afterDayForTerminate == null ? "0"
									: afterDayForTerminate);
				}
				operateVmForExpired(viv, spareExpire, afterDayOfDisable,
						afterDayOfTerminate, userId, referenceId);
			} catch (Exception e) {
				log.error("operateVmForExpired exception", e);
			}
		}
	}

	/**
	 * <云主机到期处理操作> <功能详细描述>
	 * 
	 * @param vi
	 * @param spareExpire
	 * @param afterDayOfDisable
	 * @param afterDayOfTerminate
	 * @param userId
	 * @param referenceId
	 * @see [类、类#方法、类#成员]
	 */
	@Transactional(readOnly = false)
	private void operateVmForExpired(VpdcInstanceVO viv, long spareExpire,
			long afterDayOfDisable, long afterDayOfTerminate, long userId,
			long referenceId) {

		// 到期后关闭云主机
		this.closeVm(viv.getVmId(), null, LogOperatorType.PROCESS.getName());
		// 到期后禁用处理
		this.disableVmForExpired(spareExpire, afterDayOfDisable, viv, userId);
		// 到期后删除处理
		log.debug("############spareExpire:" + spareExpire);
		log.debug("############afterDay for terminate:" + afterDayOfTerminate);
		this.terminateForExpired(spareExpire, afterDayOfTerminate, viv, "1");
	}

	// 到期后禁用处理
	private boolean disableVmForExpired(long spareExpire, long afterDay,
			VpdcInstanceVO viv, long uid) throws HsCloudException {
		boolean resultFlag = false;
		if (spareExpire <= -afterDay) {
			if (Constants.VM_ISENABLE_TURE == viv.getIsEnable()) {
				log.debug("service-disableVmForExpired start");
				freezeVM(viv.getVmId(), uid, Constants.VM_ISENABLE_FALSE_EXPIRE);
				resultFlag = true;
			}
		}
		return resultFlag;
	}

	// 到期后删除处理
	private boolean terminateForExpired(long spareExpire, long afterDay,
			VpdcInstanceVO viv, String terminateFlag) throws HsCloudException {
		boolean resultFlag = false;
		if (spareExpire <= -afterDay) {
			// 删除VM(instance)
			log.debug("###########begin delete vm...");
			this.terminate(viv.getVmId(), null, "1", 0);// 最后一个参数为0即：操作者是系统
			log.debug("############fininshed delete vm");
			resultFlag = true;
		}
		return resultFlag;
	}

	@Override
	public List<VpdcReferenceVO> getAllAvailableVM(List<Object> referenceIds,
			String zoneCode) throws HsCloudException {
		List<VpdcReferenceVO> vpdcReferenceVO = null;
		try {
			vpdcReferenceVO = vpdcDao.getAllAvailableVM(referenceIds, zoneCode);
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_LIST_ADMIN_ERROR,
					"getAllAvailableVM异常", log, e);
		}
		return vpdcReferenceVO;
	}

	// 根据云主机类型编码获取VM的类型名称
	private String getVMTypeName(Integer code) {
		if (code == null) {
			code = 0;
		}
		for (int i = 0; i < VMTypeEnum.values().length; i++) {
			if (code == VMTypeEnum.values()[i].getCode()) {
				return VMTypeEnum.values()[i].getName();
			}
		}
		return "";
	}

	// 根据云主机业务状态编码获取VM的业务状态名称
	private String getVMStatusBussName(Integer code) {
		if (code == null) {
			return "NULL";
		}
		for (int i = 0; i < VMStatusBussEnum.values().length; i++) {
			if (code == VMStatusBussEnum.values()[i].getCode()) {
				return VMStatusBussEnum.values()[i].getName();
			}
		}
		return "";
	}

	@Override
	public VmSnapShot getNewestVmSnapShot(String vmId) throws HsCloudException {
		VmSnapShot vmSnapShot = null;
		try {
			vmSnapShot = vpdcDao.getNewestVmSnapShot(vmId);
		} catch (Exception e) {
			throw new HsCloudException(Constants.OPTIONS_FAILURE,
					"getNewestVmSnapShot异常", log, e);
		}
		return vmSnapShot;
	}

	@Override
	public void cancelApplyTryVm(long uid, long referenceId)
			throws HsCloudException {
		VpdcReference vr = vpdcDao.findVpdcReferenceById(referenceId);
		// 试用待审核状态可以被【取消】
		if (VMStatusBussEnum.TRYWAIT.getCode() == vr.getVm_business_status()) {
			vr.setVm_business_status(VMStatusBussEnum.CANCEL.getCode());
			vr.setUpdateDate(new Date());
			vr.setUpdateId(uid);
			vpdcDao.updateVpdcReference(vr);
		} else {
			throw new HsCloudException(Constants.VM_NOT_STATUS,
					"cancelApplyTryVm拒绝", log, null);
		}
	}

	@Override
	public void applyForDelayTryVm(long uid, long referenceId)
			throws HsCloudException {
		VpdcReference vr = vpdcDao.findVpdcReferenceById(referenceId);
		// 试用状态可以【申请延期】
		if (0 == vr.getIsEnable()
				&& VMStatusBussEnum.TRY.getCode() == vr.getVm_business_status()
						.intValue()) {
			vr.setVm_business_status(VMStatusBussEnum.DELAYWAIT.getCode());
			vr.setUpdateDate(new Date());
			vr.setUpdateId(uid);
			vpdcDao.updateVpdcReference(vr);
			// 修改续订业务表中到期日期发生变更的记录
			VpdcRenewal vpdcRenewal = vpdcRenewalService
					.getVpdcRenewalByReferenceId(referenceId);
			if (vpdcRenewal != null) {
				vpdcRenewal.setBusinessStatus(VMStatusBussEnum.DELAYWAIT
						.getCode());// 修改为延期待审核状态
				vpdcRenewalService.saveVpdcRenewal(vpdcRenewal);
			}
		} else {
			throw new HsCloudException(Constants.VM_NOT_STATUS,
					"applyForDelayTryVm拒绝", log, null);
		}
	}

	@Override
	public void regularTryVm(long uid, Long referenceId)
			throws HsCloudException {
		try {
			VpdcReference vr = vpdcDao.findVpdcReferenceById(referenceId);
			vr.setVm_type(VMTypeEnum.REGULAR.getCode());
			vr.setVm_business_status(VMStatusBussEnum.REGULAR.getCode());
			vr.setUpdateDate(new Date());
			vr.setUpdateId(uid);
			vpdcDao.updateVpdcReference(vr);
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_REGULAR_ERROR,
					"regularTryVm异常", log, e);
		}
	}

	@Override
	public void verifyForDelayTryVm(long referenceId, int delayLong,
			long adminId) throws HsCloudException {
		VpdcReference vr = vpdcDao.findVpdcReferenceById(referenceId);
		// 试用延期待审核状态可以【延期审核】
		if (VMStatusBussEnum.DELAYWAIT.getCode() == vr.getVm_business_status()
				.intValue()) {
			try {
				VpdcReference_Period vrp = vpdcDao.getReferencePeriod(vr
						.getId());
				vr.setVm_business_status(VMStatusBussEnum.DELAY.getCode());
				vr.setUpdateDate(new Date());
				vr.setUpdateId(adminId);
				// 重新设置到期日期
				Date oldExpire = vrp.getEndTime();
				if (vrp.getEndTime().getTime() - vrp.getStartTime().getTime()
						+ delayLong * 24 * 3600 * 1000L <= (30 * 24 * 3600 * 1000L)) {
					Date newExpire = new Date(vrp.getEndTime().getTime()
							+ delayLong * 24 * 3600 * 1000L);
					vrp.setEndTime(newExpire);
					if (newExpire.after(new Date())) {
						if (vr.getIsEnable() != Constants.VM_ISENABLE_TURE) {
							vr.setIsEnable(Constants.VM_ISENABLE_TURE);
						}
					}
					vpdcDao.updateVpdcReference(vr);
					vpdcDao.updateReferencePeriod(vrp);
					// 记录到期日期变更日志
					this.saveVmPeriodLog(Constants.VM_PERIOD_LOG_VERIFYDELAY,
							vr.getId(), oldExpire, newExpire);
				} else {
					throw new HsCloudException(Constants.VM_TryEXTRA_LONG,
							"verifyForDelayTryVm异常", log);
				}
			} catch (Exception e) {
				throw new HsCloudException(Constants.VM_VERIFYDELAY_ERROR,
						"verifyForDelayTryVm异常", log, e);
			}
		} else {
			throw new HsCloudException(Constants.VM_NOT_STATUS,
					"verifyForDelayTryVm拒绝", log, null);
		}
	}

	@Override
	public void extraDelayTryVm(long referenceId, int delayLong, long adminId)
			throws HsCloudException {
		VpdcReference vr = vpdcDao.findVpdcReferenceById(referenceId);
		// 试用延期待审核状态可以【延期审核】
		if (VMStatusBussEnum.DELAY.getCode() == vr.getVm_business_status()
				.intValue()) {
			try {
				VpdcReference_Period vrp = vpdcDao.getReferencePeriod(vr
						.getId());
				vr.setVm_business_status(VMStatusBussEnum.DELAY.getCode());
				vr.setUpdateDate(new Date());
				vr.setUpdateId(adminId);
				// 重新设置到期日期
				Date oldExpire = vrp.getEndTime();
				Date newExpire = new Date(oldExpire.getTime() + delayLong * 24
						* 3600 * 1000L);
				// 额外延期总天数不能超过30天
				if ((newExpire.getTime() - vrp.getStartTime().getTime()) <= (30 * 24 * 3600 * 1000L)) {
					vrp.setEndTime(newExpire);
					if (newExpire.after(new Date())) {
						if (vr.getIsEnable() != Constants.VM_ISENABLE_TURE) {
							vr.setIsEnable(Constants.VM_ISENABLE_TURE);
						}
					}
					vpdcDao.updateVpdcReference(vr);
					vpdcDao.updateReferencePeriod(vrp);
					// 记录到期日期变更日志
					this.saveVmPeriodLog(Constants.VM_PERIOD_LOG_EXTRADELAY,
							vr.getId(), oldExpire, newExpire);
				} else {
					throw new HsCloudException(Constants.VM_EXTRA_LONG,
							"extraDelayTryVm超长", log, null);
				}
			} catch (Exception e) {
				throw new HsCloudException(Constants.VM_EXTRADELAY_ERROR,
						"extraDelayTryVm异常", log, e);
			}
		} else {
			throw new HsCloudException(Constants.VM_NOT_STATUS,
					"extraDelayTryVm拒绝", log, null);
		}
	}

	/**
	 * <后台管理员为正式云主机延期> <功能详细描述>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@Override
	public void delayRegularVm(long referenceId, int delayLong, long adminId)
			throws HsCloudException {
		VpdcReference vr = vpdcDao.findVpdcReferenceById(referenceId);
		try {
			VpdcReference_Period vrp = vpdcDao.getReferencePeriod(vr.getId());
			vr.setVm_business_status(VMStatusBussEnum.DELAY.getCode());
			vr.setUpdateDate(new Date());
			vr.setUpdateId(adminId);
			// 重新设置到期日期
			Date oldExpire = vrp.getEndTime();
			Date newExpire = new Date(vrp.getEndTime().getTime() + delayLong
					* 24 * 3600 * 1000L);
			vrp.setEndTime(newExpire);
			if (newExpire.after(new Date())) {
				if (vr.getIsEnable() != Constants.VM_ISENABLE_TURE) {
					vr.setIsEnable(Constants.VM_ISENABLE_TURE);
				}
			}
			vpdcDao.updateVpdcReference(vr);
			vpdcDao.updateReferencePeriod(vrp);
			// 记录到期日期变更日志
			this.saveVmPeriodLog(Constants.VM_PERIOD_LOG_VERIFYDELAY,
					vr.getId(), oldExpire, newExpire);
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_VERIFYDELAY_ERROR,
					"verifyForDelayTryVm异常", log, e);
		}
	}

	public void cancelApplyTryVm(long referenceId, String comments, long adminId)
			throws HsCloudException {
		VpdcReference vr = vpdcDao.findVpdcReferenceById(referenceId);
		// 试用状态可以【申请延期】
		if (VMStatusBussEnum.TRYWAIT.getCode() == vr.getVm_business_status()) {
			vr.setVm_business_status(VMStatusBussEnum.CANCEL.getCode());
			vr.setUpdateDate(new Date());
			vr.setUpdateId(adminId);
			vr.setComments(comments);
			vpdcDao.updateVpdcReference(vr);
		} else {
			throw new HsCloudException(Constants.VM_NOT_STATUS,
					"cancelApplyTryVm拒绝", log, null);
		}
	}

	public void saveVmPeriodLog(String description, Long referenceId,
			Date oldExpire, Date newExpire) {
		VpdcReference_Period_Log vrpl = new VpdcReference_Period_Log();
		vrpl.setVpdcreferenceId(referenceId);
		vrpl.setDescription(description);
		if (oldExpire != null) {
			vrpl.setEndTimeLast(oldExpire);
		} else {
			vrpl.setEndTimeLast(vpdcDao.getReferencePeriod(referenceId)
					.getEndTime());
		}
		vrpl.setEndTimeNow(newExpire);
		vrpl.setCreateDate(new Date());
		vpdcDao.saveVMPeriodLog(vrpl);
		// 将到期日期发生变更的记录从续订业务表中删除
		vpdcRenewalService.deleteVpdcRenewalByReferenceId(referenceId);
	}

	public boolean hasSnapshotTask(String vmId) {
		boolean bl = false;
		VmSnapShotTask vsst = vpdcDao.getUnSnapShotTaskByVmId(vmId);
		if (vsst != null) {
			bl = true;
		}
		try {
			ServerResource serverResource = openstackUtil.getCompute()
					.servers().server(vmId);
			Server s = serverResource.get();
			if (!"ACTIVE".equals(s.getStatus()) || s.getTaskStatus() != null) {
				bl = true;
			}
		} catch (HsCloudException e) {
			log.error(
					"expire-Process openstack getServer by vmId Exception(method->hasSnapshotTask):",
					e);
		}
		return bl;
	}

	@Override
	@Transactional(readOnly = false)
	public String createAndAttachExttendDiskByScsis(String addDisk,
			String vmId, AbstractUser a) throws HsCloudException {
		String eDiskName = "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");// yyyy-MM-dd
																				// hh:mm:ss
		String[] exttendDisks = addDisk.split(",");
		VpdcReference vr = vpdcDao.findVpdcReferenceByVmId(vmId);
		try {
			// ServerResource serverResource =
			// openstackUtil.getCompute().servers().server(vmId);
			if (exttendDisks.length > 0) {
				String diskStr = "";
				for (int i = 0; i < exttendDisks.length; i++) {
					if (!"".equals(exttendDisks[i].toString())) {
						eDiskName = dateFormat.format(new Date());
						// NovaScsiBase nsb =
						// serverResource.createAndAttachDisk(eDiskName,
						// Integer.parseInt(exttendDisks[i]));
						VpdcReference_extdisk vied = new VpdcReference_extdisk();
						/*
						 * if(nsb!=null){ eDiskId = nsb.getId();
						 * vied.setVolumeId(eDiskId); }
						 */
						vied.setName(eDiskName + i);
						vied.setVmId(vmId);
						vied.setEd_capacity(Integer.parseInt(exttendDisks[i]));
						vied.setEd_reference(vr);
						// 保存扩展盘数据库信息
						Date d = new Date();
						vied.setCreateDate(d);
						vied.setUpdateDate(d);
						vpdcDao.saveExtDisk(vied);
						diskStr += (vied.getName() + ":"
								+ vied.getEd_capacity() + ";");
					}
				}
				// 调用RebbitMQ
				HcEventResource her = new HcEventResource();
				her.setEvent_time(new Date());
				her.setType(LogResourceOperationType.ADD.getIndex());// 新增
				her.setRes_type(LogResourceType.EXTEND_DISK.getIndex());// 操作VM
				her.setBiz_type(LogBizType.ADD_EXTDISK.getIndex());// 新增扩展盘
				her.setObj_name(vr.getName());
				her.setObj_id(vr.getId());
				her.setOperator(a.getEmail());
				her.setOperator_id(a.getId());
				her.setOperator_type(LogOperatorType.ADMIN.getIndex());// 后台操作
				Map<String, String> extDisksMap = new HashMap<String, String>();
				extDisksMap.put("uuid", vmId);
				extDisksMap.put("extDisks", diskStr);
				rabbitMqUtil.sendMessage(Constants.JOBSERVER_METHOD_extDiskAdd,
						extDisksMap, her, "HcEventResource");
			}
		} catch (Exception e) {
			log.error(
					"expire-Process openstack getServer by vmId Exception(method->createAndAttachExttendDiskByScsis):",
					e);
		}
		return eDiskName;
	}

	@Override
	public List<VpdcReference_extdisk> getAttachExttendDisksOfVmByScsis(
			String vmId) throws HsCloudException {
		// NovaScsiList novaScsiList=null;
		List<VpdcReference_extdisk> lvred = null;
		try {
			/*
			 * ServerResource serverResource =
			 * openstackUtil.getCompute().servers().server(vmId); novaScsiList =
			 * serverResource.getScsis();
			 */
			lvred = vpdcDao.getExtDisksByVmId(vmId);
		} catch (HsCloudException e) {
			log.error(
					"expire-Process openstack getServer by vmId Exception(method->getAttachExttendDisksOfVmByScsis):",
					e);
		}
		return lvred;
	}

	@Override
	@Transactional(readOnly = false)
	public boolean deleteAndDetachExttendDiskByScsis(String vmId, int diskId,
			AbstractUser a) throws HsCloudException {
		boolean resultFlag = false;
		try {
			// ServerResource serverResource =
			// openstackUtil.getCompute().servers().server(vmId);
			// serverResource.deleteAndDetachDisk(diskId);
			// 删除volume的DB信息
			VpdcReference_extdisk vied = vpdcDao.getExtDiskByVolumeId(vmId,
					diskId);
			if (vied != null) {
				vpdcDao.deleExtDisk(vied);
			}
			resultFlag = true;
			// 调用RebbitMQ
			HcEventResource her = new HcEventResource();
			her.setEvent_time(new Date());
			her.setType(LogResourceOperationType.DELETE.getIndex());// 新增
			her.setRes_type(LogResourceType.EXTEND_DISK.getIndex());// 操作VM
			her.setBiz_type(LogBizType.DELETE_EXTDISK.getIndex());// 删除扩展盘
			her.setObj_name(vied.getName());
			her.setObj_id(vied.getId());
			her.setOperator(a.getEmail());
			her.setOperator_id(a.getId());
			her.setOperator_type(LogOperatorType.ADMIN.getIndex());// 后台操作
			Map<String, String> extDisksMap = new HashMap<String, String>();
			extDisksMap.put("uuid", vmId);
			extDisksMap.put("extDiskId", String.valueOf(vied.getVolumeId()));
			rabbitMqUtil.sendMessage(Constants.JOBSERVER_METHOD_extDiskDel,
					extDisksMap, her, "HcEventResource");
		} catch (Exception e) {
			log.error(
					"expire-Process openstack getServer by vmId Exception(method->deleteAndDetachExttendDiskByScsis):",
					e);
		}
		return resultFlag;
	}

	@Override
	@Transactional(readOnly = false)
	public boolean confirmResizeVm(String uuid, boolean confirmFlag)
			throws HsCloudException {
		boolean result = false;
		try {
			ServerResource serverResource = openstackUtil.getCompute()
					.servers().server(uuid);
			if (confirmFlag) {
				serverResource.hsResizeConfirm();
			} else {
				serverResource.hsResizeRevert();
			}
			if (serverResource.get() != null) {
				String hostName = serverResource.get().getHostName();
				log.info("new vm hostName:" + hostName);
				boolean updateFlag = vpdcDao.updateVmNodeName(hostName, uuid);
				result = updateFlag;
			}
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_CONFIRM_RESIZE_ERROR,
					"confirmResizeVm Exception", log, e);
		}
		return result;
	}

	@Override
	public List<VolumeVO> getAllAttachVolumesOfVm(String vmId)
			throws HsCloudException {
		// List<VolumeVO> VolumeVOList = getAttachVolumesOfVm(vmId);
		List<VolumeVO> VolumeVOList = new ArrayList<VolumeVO>();
		VolumeVO volumeVO = null;
		List<VpdcReference_extdisk> extDiskList = vpdcDao
				.getExtDisksByVmId(vmId);
		for (VpdcReference_extdisk extdisk : extDiskList) {
			volumeVO = new VolumeVO();
			if (extdisk.getVolumeId() != null) {
				volumeVO.setId(extdisk.getVolumeId());
				volumeVO.setName(extdisk.getName());
				volumeVO.setSize(extdisk.getEd_capacity());
				volumeVO.setVmId(vmId);
				volumeVO.setMode(2);
				VolumeVOList.add(volumeVO);
			}
		}
		return VolumeVOList;
	}

	@Override
	public String getVMIdByOrderItem(String orderItemId)
			throws HsCloudException {
		String vmId = vpdcDao.getVMIdByOrderItem(orderItemId);
		return vmId;
	}

	@Override
	public List<VpdcReferenceVO> getAllAvailableVMs(List<Object> referenceIds,
			String zoneCode) throws HsCloudException {
		List<VpdcReferenceVO> listVms = this.getAllAvailableVM(referenceIds,
				zoneCode);
		return listVms;
	}

	@Override
	public List<VpdcReferenceVO> getAllAvailableVMs(String nodeName,
			List<Object> referenceIds) throws HsCloudException {
		List<VpdcReferenceVO> listVms = vpdcDao.getVpdcReferenceByNodeName(
				nodeName, referenceIds);
		return listVms;
	}

	/**
	 * @throws HsCloudException
	 */
	@Override
	public void resetSystemPwd(String vmId, String password, Object o,
			String otype) throws HsCloudException {
		try {
			VpdcReference vr = vpdcDao.findVpdcReferenceByVmId(vmId);
			if (Constants.VM_ISENABLE_TURE != vr.getIsEnable()) {
				throw new HsCloudException(Constants.VM_FREEZE_NODO,
						"openvm Exception", log, null);
			}
			Map<String, String> syspwdMap = new HashMap<String, String>();
			HcEventVmOps hevo = prepareResetSystemPwd(vmId, password, o, otype,
					syspwdMap);
			rabbitMqUtil.sendMessage(Constants.JOBSERVER_METHOD_VMSYSPWD,
					syspwdMap, hevo, "HcEventVmOps");
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_RESETOS_ERROR,
					"resetSystemPwd异常", log, e);
		}
	}

	/**
	 * <修改云主机系统密码公共代码提炼> <功能详细描述>
	 * 
	 * @param vmId
	 * @param password
	 * @param o
	 * @param otype
	 * @param syspwdMap
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	private HcEventVmOps prepareResetSystemPwd(String vmId, String password,
			Object o, String otype, Map<String, String> syspwdMap) {
		String[] userpwd = password.split("/");
		VpdcReference vr = vpdcDao.findVpdcReferenceByVmId(vmId);
		String user = "";
		String pwd = "";
		if (userpwd.length > 1) {
			user = userpwd[0];
			pwd = userpwd[1];
			if (!user.equals(vr.getRadom_user())) {
				pwd = password;
			}
		} else {
			pwd = userpwd[0];
		}
		HcEventVmOps hevo = this.loadHcOps(o, otype);
		hevo.setObj_name(vr.getName());
		hevo.setReference_id(vr.getId());
		hevo.setUuid(vmId);
		hevo.setOps(LogOPSType.RESETPASSWD.getIndex());

		syspwdMap.put("uuid", vmId);
		syspwdMap.put("password", pwd);
		return hevo;
	}

	/**
	 * <修改云主机系统密码> <功能详细描述>
	 * 
	 * @param vmId
	 * @param password
	 * @param o
	 * @param otype
	 * @param taskId
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	@Override
	public void resetSystemPwd(String vmId, String password, Object o,
			String otype, long taskId) throws HsCloudException {
		try {
			VpdcReference vr = vpdcDao.findVpdcReferenceByVmId(vmId);
			if (Constants.VM_ISENABLE_TURE != vr.getIsEnable()) {
				throw new HsCloudException(Constants.VM_FREEZE_NODO,
						"openvm Exception", log, null);
			}
			Map<String, String> syspwdMap = new HashMap<String, String>();
			HcEventVmOps hevo = prepareResetSystemPwd(vmId, password, o, otype,
					syspwdMap);
			hevo.setTask_id(taskId);
			rabbitMqUtil.sendMessage(Constants.JOBSERVER_METHOD_VMSYSPWD,
					syspwdMap, hevo, "HcEventVmOps");
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_RESETOS_ERROR,
					"resetSystemPwd异常", log, e);
		}
	}

	@Override
	public String getOptimalZoneOfZones(String zoneCodes)
			throws HsCloudException {
		String serverZone = null;
		int theoreticalValue = 0;// 预计还能创建的云主机数
		int theoreticalMaxValue = 0;// 预计还能创建的最多云主机数
		try {
			String[] zones = zoneCodes.split(",");
			if (zones.length > 0) {
				for (int i = 0; i < zones.length; i++) {
					theoreticalValue = 0;
					if (!StringUtils.isEmpty(zones[i])) {
						String jsonStr = RedisUtil.getValue("ZoneAcquisition_"
								+ zones[i]);
						if (StringUtils.isNotBlank(jsonStr)) {
							JSONObject jsonObject = JSONObject
									.fromObject(jsonStr);
							Object value = jsonObject.get("theoreticalValue");
							// 预计还能创建的云主机数
							theoreticalValue = value == null ? 0 : Double
									.valueOf(
											Math.floor(Double.valueOf(
													value.toString())
													.doubleValue())).intValue();
							if (theoreticalValue >= theoreticalMaxValue) {
								theoreticalMaxValue = theoreticalValue;
								if (theoreticalValue > 0) {
									serverZone = zones[i];
								}
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			log.error("redis getZoneOverviewInfo Exception", ex);
		}
		return serverZone;
	}

	@Override
	@Transactional(readOnly = false)
	public boolean modifyExttendDisk(String vmId, int diskId, int diskSize,
			AbstractUser a) throws HsCloudException {
		boolean resultFlag = false;
		try {
			// 修改volume的DB信息
			VpdcReference_extdisk vied = vpdcDao.getExtDiskByVolumeId(vmId,
					diskId);
			if (vied != null) {
				vied.setEd_capacity(diskSize);
				vpdcDao.saveExtDisk(vied);
			}
			resultFlag = true;
			// 调用RebbitMQ
			HcEventResource her = new HcEventResource();
			her.setEvent_time(new Date());
			her.setType(LogResourceOperationType.UPDATE.getIndex());// 修改
			her.setRes_type(LogResourceType.EXTEND_DISK.getIndex());// 操作VM
			her.setBiz_type(LogBizType.ADJUST_EXTDISK.getIndex());// 删除扩展盘
			her.setObj_name(vied.getName());
			her.setObj_id(vied.getId());
			her.setOperator(a.getEmail());
			her.setOperator_id(a.getId());
			her.setOperator_type(LogOperatorType.ADMIN.getIndex());// 后台操作
			Map<String, String> extDisksMap = new HashMap<String, String>();
			extDisksMap.put("uuid", vmId);
			extDisksMap.put("extDiskInfo", String.valueOf(vied.getVolumeId())
					+ ":" + String.valueOf(vied.getEd_capacity()));
			// extDisksMap.put("extDiskId", String.valueOf(vied.getVolumeId()));
			// extDisksMap.put("extDiskSize",
			// String.valueOf(vied.getEd_capacity()));
			rabbitMqUtil.sendMessage(Constants.JOBSERVER_METHOD_extDiskModify,
					extDisksMap, her, "HcEventResource");
		} catch (Exception e) {
			log.error(
					"expire-Process openstack getServer by vmId Exception(method->modifyExttendDisk):",
					e);
		}
		return resultFlag;
	}

	/**
	 * <用途: 获取当前用户的当前instanceId这台云主机的同一组下面所有已经添加内网安全的云主机
	 * 
	 * @author liyunhui
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@Override
	public List<VmIntranetSecurityVO> findIntranetVmsByUuid(String uuid)
			throws HsCloudException {
		try {
			// 查找当前这台云主机的instance_id
			Long instance_id = vpdcDao.findVmByVmId(uuid).getId();
			// 如果没取到正常值
			if (instance_id == null || instance_id <= 0) {
				return null;
			}
			// 查找当前这台云主机的intranetSecurityId
			List<VmIntranetSecurity_Instance> visi_list = vpdcDao
					.getIntranet_Instance(instance_id, null);
			// 如果没取到正常值
			if (visi_list.size() == 0 || visi_list.size() >= 2) {
				return null;
			}
			Long intranetSecurityId = visi_list.get(0).getIntranetsecurity_id();
			List<VpdcInstance> instances = vpdcDao
					.findIntranetVmsByIntranetId(intranetSecurityId);
			List<VmIntranetSecurityVO> lvis = new ArrayList<VmIntranetSecurityVO>();
			VmIntranetSecurityVO vis = null;
			for (VpdcInstance vi : instances) {
				vis = new VmIntranetSecurityVO();
				vis.setInstanceId(vi.getId());
				vis.setVmName(vi.getVpdcreference().getName());
				vis.setInnerIp(vi.getVpdcreference().getVm_innerIP());
				vis.setOuterIp(vi.getVpdcreference().getVm_outerIP());
				// 为所有同一组的云主机添加组号groupId
				vis.setGroupId(intranetSecurityId);
				vis.setUuid(vi.getVmId());
				lvis.add(vis);
			}
			return lvis;
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "findIntranetVmsByUuid异常",
					log, e);
		}
	}

	/**
	 * <用途: 提交内网安全和外网安全的数据 >
	 * 
	 * @param SecurityPolicyVO
	 *            :安全策略封装的VO
	 * @author liyunhui
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@Override
	public boolean submitSecurityPolicy(SecurityPolicyVO spVO)
			throws HsCloudException {
		try {
			boolean result = false;
			// 内网安全提交的所有的云主机的instanceId
			String submit_instanceIds = spVO.getSubmit_instanceIds();
			// 内网安全提交的所有的云主机的uuid
			String submit_uuids = spVO.getSubmit_uuids();
			// 原来的数据库中加载的内网安全的所有的云主机的instanceId
			String old_instanceIds = spVO.getOld_instanceIds();
			// 原来的数据库中加载的内网安全的所有的云主机的uuid
			String old_uuids = spVO.getOld_uuids();
			// 提交的 protocal@portFrom@portTo,protocal@portFrom@portTo 的String数据
			String extranet_submit_data = spVO.getExtranet_submit_data();
			// 原来的 protocal@portFrom@portTo,protocal@portFrom@portTo 的String数据
			String extranet_old_data = spVO.getExtranet_old_data();
			String uuid = spVO.getUuid();// 选中的这台云主机的uuid
			String instanceId = spVO.getInstanceId();// 选中的这台云主机的instanceId
			String groupId = spVO.getGroupId();// 选中的这台云主机所属内网安全组的groupId
			User user = spVO.getUser();// 当前用户
			// 提交内网安全 提交外网安全
			submitIntranetSecurity(submit_instanceIds, submit_uuids,
					old_instanceIds, old_uuids, instanceId, groupId, user);
			submitExtranetSecurity(extranet_submit_data, extranet_old_data,
					uuid, user);
			result = true;
			return result;
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "submitSecurityPolicy异常",
					log, e);
		}
	}

	// 提交内网安全
	private void submitIntranetSecurity(String submit_instanceIds,
			String submit_uuids, String old_instanceIds, String old_uuids,
			String instanceId, String groupId, User user) {
		// 只有当用户提交的内网安全数据不为默认的选中的云主机instanceId,才说明用户做了操作
		if (!instanceId.equals(submit_instanceIds)) {
			// 内网提交的instanceId集合 和 uuid集合 ,内网原来的instanceId集合 和 uuid集合
			List<String> submit_instanceId_list = Arrays
					.asList(submit_instanceIds.split(","));
			submit_instanceId_list = new ArrayList<String>(
					submit_instanceId_list);
			List<String> submit_uuid_list = Arrays.asList(submit_uuids
					.split(","));
			submit_uuid_list = new ArrayList<String>(submit_uuid_list);
			List<String> old_instanceId_list = Arrays.asList(old_instanceIds
					.split(","));
			old_instanceId_list = new ArrayList<String>(old_instanceId_list);
			List<String> old_uuid_list = Arrays.asList(old_uuids.split(","));
			old_uuid_list = new ArrayList<String>(old_uuid_list);
			// 1.如果原来没数据,提交的时候也没有提交数据,什么都不做,不用考虑
			// 2.如果原来没数据,提交的时候提交了2条或者以上的数据,直接往数据库里面插,还要创建新的组
			if ("".equals(old_instanceIds) && !"".equals(submit_instanceIds)
					&& submit_instanceId_list.size() >= 2) {
				// 先创建组,向hc_vm_intranetsecurity中插入新数据,使用UUID作为组名
				Date date = new Date();
				UUID groupName = UUID.randomUUID();
				VmIntranetSecurity vis = new VmIntranetSecurity();
				vis.setCreateDate(date);
				vis.setCreateId(user.getId());
				vis.setName(groupName.toString());
				vis.setUpdateDate(date);
				vis.setUpdateId(user.getId());
				vis.setVersion(user.getVersion());
				vpdcDao.saveOrUpdateIntranetSecurityGroup(vis);
				// 通过组名groupName获得云主机组的组号
				Long group_id = vpdcDao.getIntranetSecurityGroup(
						groupName.toString()).getId();
				addIntranetSecurityVms(submit_uuid_list, group_id);// openstack添加新的云主机
				// 向数据库中插入添加了内网安全的云主机集合
				vpdcDao.saveOrUpdateIntranetSecurityVms(submit_instanceId_list,
						group_id);
			} else if (!"".equals(old_instanceIds)
					&& "".equals(submit_instanceIds)) {
				// 3.如果原来有数据,提交的时候提交 空,直接往数据库里面删除老数据和组号的那条记录
				// deleteIntranetSecurity(groupId, old_uuid_list);//
				// openstack删除原来的云主机
				vpdcDao.deleteAddedIntranetSecurityVms(old_instanceId_list,
						Long.valueOf(groupId), true);
			} else if (!"".equals(old_instanceIds)
					&& !"".equals(submit_instanceIds)
					&& submit_instanceId_list.size() >= 2) {
				// 4.如果原来有数据,提交的时候时候也有数据 分析找出哪一条是新增的,哪一条是要删除的
				List<String> deleted_instanceIds = old_instanceId_list;
				deleted_instanceIds.removeAll(submit_instanceId_list);
				List<String> deleted_uuids = old_uuid_list;
				deleted_uuids.removeAll(submit_uuid_list);
				List<String> added_instanceIds = submit_instanceId_list;
				added_instanceIds.removeAll(new ArrayList<String>(Arrays
						.asList(old_instanceIds.split(","))));
				List<String> added_uuids = submit_uuid_list;
				added_uuids.removeAll(new ArrayList<String>(Arrays
						.asList(old_uuids.split(","))));
				// 把用户新添加的内网安全的云主机插入到数据库,不要创建新的组
				if (added_instanceIds.size() > 0) {
					addIntranetSecurityVms(added_uuids, Long.valueOf(groupId));// openstack添加新的云主机
					vpdcDao.saveOrUpdateIntranetSecurityVms(added_instanceIds,
							Long.valueOf(groupId));
				}
				// 只删除hc_vm_intranetsecurity记录,组的记录保留
				if (deleted_instanceIds.size() > 0) {
					deleteIntranetSecurity(groupId, deleted_uuids);// openstack删除原来的云主机
					vpdcDao.deleteAddedIntranetSecurityVms(deleted_instanceIds,
							null, false);
				}
			}
		}
	}

	// 提交外网安全
	private void submitExtranetSecurity(String extranet_submit_data,
			String extranet_old_data, String uuid, User user) {
		// 1.如果原来没数据,提交的时候也没有提交数据,不用考虑
		// 2.如果原来有数据,提交的时候提交 空,直接通过instanceId删除所有的老数据
		if (!"".equals(extranet_old_data) && "".equals(extranet_submit_data)) {
			deleteOpenstackExtranet(uuid);// Openstack删除外网安全
			vpdcDao.deleteExtranetSecurity(null, uuid);
		} else if (!"".equals(extranet_submit_data)
				&& "".equals(extranet_old_data)) {
			// 3.如果原来没数据,提交的时候提交了1条或者以上的数据,直接往数据库里面插
			if (verifyExtranetSecurity(extranet_submit_data)) {
				List<String> added_port_list = Arrays
						.asList(extranet_submit_data.split(","));
				added_port_list = new ArrayList<String>(added_port_list);
				List<VmExtranetSecurity> ves_lsit = vmExtranetSecurity(uuid,
						user, added_port_list);
				addOpenstackExtranet(uuid, added_port_list);// Openstack添加外网安全
				vpdcDao.saveOrUpdateAllExtranetSecurity(ves_lsit);
			}
		} else if (!"".equals(extranet_submit_data)
				&& !"".equals(extranet_old_data)) {
			// 4.如果原来有数据,提交的时候时候也有数据 分析找出哪一条是新增的,哪一条是要删除的
			if (verifyExtranetSecurity(extranet_submit_data)) {
				List<String> submit_port_list = Arrays
						.asList(extranet_submit_data.split(","));
				submit_port_list = new ArrayList<String>(submit_port_list);
				/* 因python开发组提供的接口改变,现在思路改变为: openstack依旧使用添加接口即可，会自动覆盖原端口策略 */
				// deleteOpenstackExtranet(uuid);// Openstack删除云主机的外网安全
				addOpenstackExtranet(uuid, submit_port_list);// Openstack添加云主机的外网安全
				/* 数据库操作还是找出需要删除的外网安全数据和需要添加的外网安全数据 */
				List<String> added_port_list = Arrays
						.asList(extranet_submit_data.split(","));
				added_port_list = new ArrayList<String>(added_port_list);
				List<String> deleted_port_list = Arrays
						.asList(extranet_old_data.split(","));
				deleted_port_list = new ArrayList<String>(deleted_port_list);
				// 找出需要删除的外网安全数据和需要添加的外网安全数据
				added_port_list.removeAll(deleted_port_list);
				deleted_port_list.removeAll(new ArrayList<String>(Arrays
						.asList(extranet_submit_data.split(","))));
				if (added_port_list.size() > 0) {
					List<VmExtranetSecurity> ves_lsit = vmExtranetSecurity(
							uuid, user, added_port_list);
					vpdcDao.saveOrUpdateAllExtranetSecurity(ves_lsit);// 数据库添加外网端口
				}
				if (deleted_port_list.size() > 0) {
					vpdcDao.deleteExtranetSecurity(deleted_port_list, uuid);// 数据库删除外网端口
				}
			}
		}
	}

	// 后台再次校验外网安全提交的端口
	private boolean verifyExtranetSecurity(String extranet_submit_data) {
		boolean result = false;
		// 提交的时候提交了1条或者以上的数据,对提交的数据再次过滤
		List<List<Integer[]>> extranet_list = new ArrayList<List<Integer[]>>();
		for (int i = 0; i < 3; i++) {
			List<Integer[]> protocal_list = new ArrayList<Integer[]>();
			extranet_list.add(protocal_list);
		}
		// 判断端口是否占用 ,端口的值是否正常1-65535
		boolean isPortOverlaped = false, isPortRegular = true;
		// extranet_array为2维数组[[1@100@1111],[2@200@2222]]
		String[] extranet_array = extranet_submit_data.split(",");
		for (int i = 0; i < extranet_array.length; i++) {
			// element为2维数组中的一维数组["0","100","1111"]
			String[] element = extranet_array[i].split("@");
			if (element[0].equals("0")) {
				// ["0","100","1111"] --> [1,100,1111]和[2,100,1111]
				Integer[] ele1 = { 1, Integer.valueOf(element[1]),
						Integer.valueOf(element[2]) };
				Integer[] ele2 = { 2, Integer.valueOf(element[1]),
						Integer.valueOf(element[2]) };
				extranet_list.get(0).add(ele1);
				extranet_list.get(1).add(ele2);
			} else {
				for (int j = 1; j <= 3; j++) {
					if (element[0].equals(String.valueOf(j))) {
						Integer[] ele = { j, Integer.valueOf(element[1]),
								Integer.valueOf(element[2]) };
						extranet_list.get(j - 1).add(ele);
					}
				}
			}
		}
		// protocal_list: 数组的list集合[[1,100,1111],[1,2222,3000]]
		for (int i = 0; i < extranet_list.size() - 1; i++) {
			// 如果protocal_list有2个元素才会存在端口被占用
			if (extranet_list.get(i).size() >= 2) {
				Integer port_from = extranet_list.get(i).get(0)[1];
				Integer port_to = extranet_list.get(i).get(0)[2];
				for (int j = 0; j < extranet_list.get(i).size(); j++) {
					// 从第2个元素开始和第一个作为标准的元素作比较
					if (j > 0) {
						// 假设现在有150-230 101-200
						// 400-600这么三个TCP端口,如果第一个元素的150或者230
						// 在数轴上位于其他的端口101-200或者400-600之间,那么说明重叠
						if (port_from >= extranet_list.get(i).get(j)[1]
								&& port_from <= extranet_list.get(i).get(j)[2])
							isPortOverlaped = true;// 判断端口是否重叠
						if (port_to >= extranet_list.get(i).get(j)[1]
								&& port_to <= extranet_list.get(i).get(j)[2])
							isPortOverlaped = true;// 判断端口是否重叠
						if (port_from < 1 || port_from > 65535 || port_to < 1
								|| port_to > 65535 || port_from > port_to)
							isPortRegular = false;
					}
				}
			}
		}
		// 如果protocal_list有2个元素ICMP协议必定会存在端口重叠
		if (extranet_list.get(2).size() > 1)
			isPortOverlaped = true;// 判断端口是否重叠
		if (!isPortOverlaped && isPortRegular)
			result = true;
		return result;
	}

	// Openstack操作 删除原来云主机的所有外网安全数据
	private void deleteOpenstackExtranet(String uuid) {
		VpdcReference vr = vpdcDao.findVpdcReferenceByVmId(uuid);
		String zone = vr.getVmZone().split("\\$")[0];
		try {
			openstackUtil.getCompute(zone).servers().server(uuid)
					.clearSecurityIngressRules();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// openstack操作:提取出来的向云主机内网安全组中添加新的云主机的方法
	private void addIntranetSecurityVms(List<String> addedVm_uuid_list,
			Long group_id) {
		String zone = "";
		for (int i = 0; i < addedVm_uuid_list.size(); i++) {
			VpdcReference vr = vpdcDao
					.findVpdcReferenceByVmId(addedVm_uuid_list.get(i));
			zone = vr.getVmZone().split("\\$")[0];
			try {
				openstackUtil.getCompute(zone).servers()
						.server(addedVm_uuid_list.get(i))
						.setSecurityLan(new Long(group_id).intValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// openstack操作:提取出来的向云主机内网安全组中删除需要删除的原来的云主机的方法
	private void deleteIntranetSecurity(String groupId,
			List<String> deletedVm_uuid_list) {
		String zone = "";
		for (int i = 0; i < deletedVm_uuid_list.size(); i++) {
			VpdcReference vr = vpdcDao
					.findVpdcReferenceByVmId(deletedVm_uuid_list.get(i));
			zone = vr.getVmZone().split("\\$")[0];
			try {
				openstackUtil.getCompute(zone).servers()
						.server(deletedVm_uuid_list.get(i))
						.removeSecurityLan(new Long(groupId).intValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// openstack操作: 提取出来的添加外网安全的方法
	private void addOpenstackExtranet(String uuid, List<String> added_port_list) {
		List<Integer[]> addedPort_list = new ArrayList<Integer[]>();
		for (int i = 0; i < added_port_list.size(); i++) {
			String[] ele1 = added_port_list.get(i).split("@");
			Integer[] ele2 = { Integer.valueOf(ele1[0]),
					Integer.valueOf(ele1[1]), Integer.valueOf(ele1[2]) };
			addedPort_list.add(ele2);
		}
		List<SecurityIngressRules.IngressRule> ir_list = new ArrayList<SecurityIngressRules.IngressRule>();
		for (int i = 0; i < addedPort_list.size(); i++) {
			SecurityIngressRules.IngressRule ir = new SecurityIngressRules.IngressRule();
			if (addedPort_list.get(i)[0] == 0)// all(tcp&udp);tcp;udp;icmp;
				ir.setIpProtocol("all");
			else if (addedPort_list.get(i)[0] == 1)
				ir.setIpProtocol("tcp");
			else if (addedPort_list.get(i)[0] == 2)
				ir.setIpProtocol("udp");
			else
				ir.setIpProtocol("icmp");
			ir.setFromPort(addedPort_list.get(i)[1]);
			ir.setToPort(addedPort_list.get(i)[2]);
			ir.getIpRange().setCidr("0.0.0.0/0");
			ir_list.add(ir);
		}
		SecurityIngressRules sir = new SecurityIngressRules();
		sir.setIngressRules(ir_list);
		try {
			VpdcReference vr = vpdcDao.findVpdcReferenceByVmId(uuid);
			String zone = vr.getVmZone().split("\\$")[0];
			openstackUtil.getCompute(zone).servers().server(uuid)
					.setSecurityIngressRules(sir);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 提取出来的添加外网的端口的集合的方法
	private List<VmExtranetSecurity> vmExtranetSecurity(String uuid, User user,
			List<String> added_port_list) {
		List<VmExtranetSecurity> ves_lsit = new ArrayList<VmExtranetSecurity>();
		List<Integer[]> addedPort_list = new ArrayList<Integer[]>();
		for (int i = 0; i < added_port_list.size(); i++) {
			String[] ele1 = added_port_list.get(i).split("@");
			Integer[] ele2 = { Integer.valueOf(ele1[0]),
					Integer.valueOf(ele1[1]), Integer.valueOf(ele1[2]) };
			addedPort_list.add(ele2);
		}
		for (int i = 0; i < addedPort_list.size(); i++) {
			VmExtranetSecurity ves = new VmExtranetSecurity();
			Date date = new Date();
			ves.setCreateDate(date);
			ves.setCreateId(user.getId());
			// 外网安全name直接放云主机的uuid,这个字段其实没意义
			ves.setName(uuid);
			ves.setUpdateDate(date);
			ves.setUpdateId(user.getId());
			ves.setVersion(user.getVersion());
			ves.setPort_from(addedPort_list.get(i)[1]);
			ves.setPort_to(addedPort_list.get(i)[2]);
			ves.setProtocal(addedPort_list.get(i)[0]);
			ves.setUuid(uuid);
			VpdcInstance instance = vpdcDao.findVmByVmId(uuid);
			ves.setInstance(instance);
			ves_lsit.add(ves);
		}
		return ves_lsit;
	}

	/**
	 * 根据vmId修改资源限制
	 */
	@Override
	@Transactional
	public void updateResourceLimit(ResourceLimit resourceLimit, String id)
			throws HsCloudException {
		Integer bwtIn = 0;
		Integer bwtOut = 0;
		Integer ipConnIn = 0;
		Integer ipConnOut = 0;
		Integer tcpConnIn = 0;
		Integer tcpConnOut = 0;
		Integer udpConnIn = 0;
		Integer udpConnOut = 0;
		if (null != resourceLimit) {
			bwtIn = resourceLimit.getBwtIn();
			bwtOut = resourceLimit.getBwtOut();
			ipConnIn = resourceLimit.getIpConnIn();
			ipConnOut = resourceLimit.getIpConnOut();
			tcpConnIn = resourceLimit.getTcpConnIn();
			tcpConnOut = resourceLimit.getTcpConnOut();
			udpConnIn = resourceLimit.getUdpConnIn();
			udpConnOut = resourceLimit.getUdpConnOut();
		}
		// update to datebase
		VpdcReference vpdcReference = vpdcDao.findVpdcReferenceByVmId(id);
		vpdcReference.setNetwork_bandwidth(bwtIn);
		vpdcReference.setBwtIn(bwtIn);
		vpdcReference.setBwtOut(bwtOut);
		vpdcReference.setIpConnIn(ipConnIn);
		vpdcReference.setIpConnOut(ipConnOut);
		vpdcReference.setTcpConnIn(tcpConnIn);
		vpdcReference.setTcpConnOut(tcpConnOut);
		vpdcReference.setUdpConnIn(udpConnIn);
		vpdcReference.setUdpConnOut(udpConnOut);
		vpdcDao.updateVpdcReference(vpdcReference);
		// set for openstack
		SetSecurityConnlimitRules sscr = new SetSecurityConnlimitRules();
		SetSecurityBandwidthRules ssbr = new SetSecurityBandwidthRules();
		ConnlimitRule cr = new ConnlimitRule();
		BandwidthRule br = new BandwidthRule();
		cr.setIpIn(ipConnIn);
		cr.setIpOut(ipConnOut);
		cr.setTcpIn(tcpConnIn);
		cr.setTcpOut(tcpConnOut);
		cr.setUdpIn(udpConnIn);
		cr.setUdpOut(udpConnOut);
		br.setBwtIn(bwtIn);
		br.setBwtOut(bwtOut);
		sscr.setConnlimitRule(cr);
		ssbr.setBandwidthRule(br);
		try {
			String zoneCode = vpdcReference.getVmZone();
			if (zoneCode.indexOf("$") != -1) {
				zoneCode = zoneCode.split("\\$")[0];
			}
			openstackUtil.getCompute(zoneCode).servers().server(id)
					.setSecurityConnlimitRules(sscr);
			openstackUtil.getCompute(zoneCode).servers().server(id)
					.setSecurityBandwidthRules(ssbr);
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_OPENSTACK_API_ERROR,
					"updateResourceLimit异常", log, e);
		}

	}

	@Override
	public boolean repairOS(String uuid, Object o, String otype)
			throws HsCloudException {
		boolean bl = false;
		try {
			VpdcReference vr = vpdcDao.findVpdcReferenceByVmId(uuid);
			if (Constants.VM_ISENABLE_TURE != vr.getIsEnable()) {
				throw new HsCloudException(Constants.VM_FREEZE_NODO,
						"openvm Exception", log, null);
			}
			vr.setVm_task_status(Constants.VM_STATUS_REPAIR);
			vpdcDao.updateVpdcReference(vr);
			HcEventVmOps hevo = loadHcOps(o, otype);
			hevo.setObj_name(vr.getName());
			hevo.setOps(LogOPSType.REPAIR.getIndex());
			hevo.setReference_id(vr.getId());
			hevo.setUuid(uuid);
			Map<String, String> vmMap = new HashMap<String, String>();
			vmMap.put("uuid", uuid);
			rabbitMqUtil.sendMessage(Constants.JOBSERVER_METHOD_VMOSRepair,
					vmMap, hevo, "HcEventVmOps");
			bl = true;
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_REPAIR_ERROR,
					"repairOS Exception", log, e);
		}
		return bl;
	}

	@Override
	public boolean recycleRestore(String uuid, Admin admin)
			throws HsCloudException {
		boolean bl = false;
		try {
			VpdcInstance vi = vpdcDao.findRecycleInstanceByVmId(uuid);
			vi.setStatus(0);
			vi.setUpdateDate(new Date());
			vi.setUpdateId(admin.getId());
			vpdcDao.updateVpdcInstance(vi);
			VpdcReference vr = vi.getVpdcreference();
			vr.setVm_status(Constants.VM_STATUS_ACTIVE);
			vr.setStatus(0);
			vr.setUpdateDate(new Date());
			vr.setUpdateId(admin.getId());
			vpdcDao.updateVpdcReference(vr);
			// IP重新置为已使用状态
			List<String> floatingIps = vpdcDao.getFloatingIpsFromDetailIp(vi
					.getId());
			if (floatingIps != null) {
				for (String ip : floatingIps) {
					vpdcDao.resetIPstatus(2, admin.getId(), ip);
				}
			}
			ControlPanelUser cp = controlPanelDao.getRecycleCPByVmId(uuid);
			if (cp != null) {
				cp.setStatus(0);
				controlPanelDao.saveControlUser(cp);
			}
			HcEventResource her = new HcEventResource();
			her.setEvent_time(new Date());
			her.setType(LogResourceOperationType.ADD.getIndex());// 新增
			her.setRes_type(LogResourceType.VM.getIndex());// 操作VM
			her.setBiz_type(LogBizType.RECYCLE_RESTORE.getIndex());// 回收站恢复
			her.setObj_name(vr.getName());
			her.setObj_id(vr.getId());
			her.setOperator(admin.getEmail());
			her.setOperator_id(admin.getId());
			her.setOperator_type(LogOperatorType.ADMIN.getIndex());// 后台操作
			Map<String, String> vmMap = new HashMap<String, String>();
			vmMap.put("uuid", uuid);
			rabbitMqUtil.sendMessage(
					Constants.JOBSERVER_METHOD_VMRecycleRestore, vmMap, her,
					"HcEventResource");
			bl = true;
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_RECYCLE_RESTORE_ERROR,
					"recycleRestore Exception", log, e);
		}
		return bl;
	}

	@Override
	public boolean recycleTerminate(String uuid, Admin admin) {
		boolean bl = false;
		try {
			VpdcInstance vi = vpdcDao.findRecycleInstanceByVmId(uuid);
			VpdcReference vr = vi.getVpdcreference();
			vr.setVm_status(Constants.VM_STATUS_DELETED);
			vpdcDao.updateVpdcReference(vr);
			HcEventResource her = new HcEventResource();
			her.setEvent_time(new Date());
			her.setType(LogResourceOperationType.DELETE.getIndex());// 新增
			her.setRes_type(LogResourceType.VM.getIndex());// 操作VM
			her.setBiz_type(LogBizType.RECYCLE_DELETE.getIndex());// 回收站彻底删除
			her.setObj_name(vr.getName());
			her.setObj_id(vr.getId());
			her.setOperator(admin.getEmail());
			her.setOperator_id(admin.getId());
			her.setOperator_type(LogOperatorType.ADMIN.getIndex());// 后台操作
			Map<String, String> vmMap = new HashMap<String, String>();
			vmMap.put("uuid", uuid);
			rabbitMqUtil.sendMessage(
					Constants.JOBSERVER_METHOD_VMRecycleDelete, vmMap, her,
					"HcEventResource");
			bl = true;
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_RECYCLE_DELETE_ERROR,
					"recycleTerminate Exception", log, e);
		}
		return bl;
	}

	@Override
	public void initVNCPortPool() {
		// 清空端口池
		vpdcDao.clearVNCPortPool();
		// 初始化所有分平台的端口数据
		SocketUtil su = new SocketUtil();
		List<Object> domains = vpdcDao.getAvailableDomainIds();
		Object[] ports = su.getRandomPorts();
		for (int i = 0; i < domains.size(); i++) {
			// 系统获取端口数据
			// 初始化端口池数据
			vpdcDao.saveVNCPortPool(ports,
					Long.valueOf(domains.get(i).toString()));
		}
	}

	@Override
	public Integer getAvailablePort(Long domainId) {
		Integer port = null;
		List<VmVNCPool> lvnc = vpdcDao.loadVNCPortPool(domainId);
		if (lvnc != null && lvnc.size() > 0) {
			Random r = new Random();
			SocketUtil su = new SocketUtil();
			int count = lvnc.size();
			for (int i = 0; i < count; i++) {
				VmVNCPool portE = lvnc.get(r.nextInt(count));
				if (su.isPortAvailable(Integer.valueOf(portE.getName()))) {
					port = Integer.valueOf(portE.getName());
					break;
				}
			}
		}
		return port;
	}

	@Override
	public void useVncPort(String port, User u, String vmid) {
		VmVNCPool pool = vpdcDao.getPoolByPort(port, u == null ? null : u
				.getDomain().getId());
		if (pool != null) {
			pool.setVmid(vmid);
			pool.setStatus(1);
			pool.setUpdateDate(new Date());
			pool.setUpdateId(u == null ? 0 : u.getId());
			vpdcDao.updateVNCPort(pool);
		}
	}

	private void fillDataOfVmInfoVO(Page<VmInfoVO> page) {
		List<VmInfoVO> vmInfoList = page.getResult();
		VpdcInstance vpdcInstance = null;
		for (VmInfoVO vmInfoVO : vmInfoList) {
			vpdcInstance = vpdcDao.getLatestVpdcInstance(
					vmInfoVO.getReferenceId(), 1);
			if (vpdcInstance != null) {
				vmInfoVO.setHostName(vpdcInstance.getNodeName());
				vmInfoVO.setVmId(vpdcInstance.getVmId());
			}
		}
	}

	@Override
	public boolean deleteIPOfVm(String uuid, String oldIP, Long adminId)
			throws HsCloudException {
		boolean result = false;
		try {
			Admin a = adminDao.get(adminId);
			ControlPanelUser cpu = controlPanelDao.getCPByVmId(uuid);
			String cpu_ip = "";
			String vmName = "";
			//
			if (!StringUtils.isEmpty(oldIP)) {
				vpdcDao.resetIPstatus(4, adminId, oldIP);
				if (cpu != null) {
					cpu_ip = cpu.getVmIP();
					cpu_ip = cpu_ip.replace("," + oldIP, "");
					cpu.setVmIP(cpu_ip);
					cpu.setUpdateDate(new Date());
					controlPanelDao.saveControlUser(cpu);
				}
			}
			// 调用RebbitMQ
			HcEventResource her = new HcEventResource();
			her.setEvent_time(new Date());
			her.setType(LogResourceOperationType.DELETE.getIndex());// 删除
			her.setRes_type(LogResourceType.IP.getIndex());// 操作VM
			her.setBiz_type(LogBizType.IP_DELETE.getIndex());// 外网IP删除
			her.setObj_name(vmName);
			her.setOperator(a.getEmail());
			her.setOperator_id(a.getId());
			her.setOperator_type(LogOperatorType.ADMIN.getIndex());
			Map<String, String> ipMap = new HashMap<String, String>();
			ipMap.put("uuid", uuid);
			ipMap.put("ip", oldIP);
			rabbitMqUtil.sendMessage(Constants.JOBSERVER_METHOD_IPdelete,
					ipMap, her, "HcEventResource");
			result = true;
		} catch (Exception e) {
			throw new HsCloudException(Constants.OPTIONS_FAILURE,
					"deleteIPOfVm Exception", log, e);
		}
		return result;
	}

	@Override
	public boolean addIPOfVm(String uuid, String newIP, AbstractUser a)
			throws HsCloudException {
		boolean result = false;
		try {
			ControlPanelUser cpu = controlPanelDao.getCPByVmId(uuid);
			String cpu_ip = "";
			String vmName = "";
			if (!StringUtils.isEmpty(newIP)) {
				VpdcInstance vpdcInstance = vpdcDao.findVmByVmId(uuid);
				vmName = vpdcInstance.getVpdcreference().getName();
				// IP状态置为【待分配】
				vpdcDao.resetIPstatus(1, a.getId(), newIP);
				// 更新IP其它信息
				vpdcDao.updateIpDetailByIp(vpdcInstance.getId(), 0, a.getId(),
						0, IPConvert.getIntegerIP(newIP));
				if (cpu != null) {
					cpu_ip = cpu.getVmIP();
					if (!StringUtils.isEmpty(cpu_ip)) {
						cpu_ip = cpu_ip + "," + newIP;
					} else {
						cpu_ip = newIP;
					}
					cpu.setVmIP(cpu_ip);
					cpu.setUpdateDate(new Date());
					controlPanelDao.saveControlUser(cpu);
				}
			}
			// 调用RebbitMQ
			HcEventResource her = new HcEventResource();
			her.setEvent_time(new Date());
			her.setType(LogResourceOperationType.ADD.getIndex());// 增加
			her.setRes_type(LogResourceType.IP.getIndex());// 操作VM
			her.setBiz_type(LogBizType.IP_ADD.getIndex());// 外网IP添加
			her.setObj_name(vmName);
			her.setOperator(a.getEmail());
			her.setOperator_id(a.getId());
			her.setOperator_type(LogOperatorType.ADMIN.getIndex());
			Map<String, String> ipMap = new HashMap<String, String>();
			ipMap.put("uuid", uuid);
			ipMap.put("ip", newIP);
			rabbitMqUtil.sendMessage(Constants.JOBSERVER_METHOD_IPbind, ipMap,
					her, "HcEventResource");
			result = true;
		} catch (Exception e) {
			throw new HsCloudException(Constants.OPTIONS_FAILURE,
					"addIPOfVm Exception", log, e);
		}
		return result;
	}

	/**
	 * <分页获取User用户所有的VPDC(包含非路由模式和路由模式)>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@Override
	public Page<VpdcVo> findVpdcsByUser(Page<VpdcVo> pageVpdc, int vpdcType,
			String name, User user) throws HsCloudException {
		int offSet, length;
		if (pageVpdc.getPageNo() == 1) {
			offSet = 0;
			length = pageVpdc.getPageSize();
		} else {
			offSet = (pageVpdc.getPageNo() - 1) * pageVpdc.getPageSize();
			length = pageVpdc.getPageSize();
		}
		int recordSize = 0;
		recordSize = vpdcDao.getVpdcsCountByUser(vpdcType, name, user);
		List<VpdcVo> vpdcVo_list = new ArrayList<VpdcVo>();
		List<Vpdc> vpdc_list = vpdcDao.findVpdcsByUser(offSet, length,
				vpdcType, name, user);
		for (Vpdc vpdc : vpdc_list) {
			VpdcVo vpdcVo = new VpdcVo();
			vpdcVo.setId(vpdc.getId());
			vpdcVo.setName(vpdc.getName());
			vpdcVo.setVpdcType(vpdc.getType());
			vpdcVo.setZoneGroup(vpdc.getZoneGroupId());
			String zoneGroupName = vpdcDao.findZoneGroupName(vpdc
					.getZoneGroupId());
			// 根据ZoneGroup获得ZoneGroupName
			vpdcVo.setZoneGroupName(zoneGroupName);
			Set<VpdcRouter> vr_set = vpdc.getRouters();
			Iterator<VpdcRouter> vr_it = vr_set.iterator();
			if (vr_it.hasNext()) {
				VpdcRouter vpdcRouter = (VpdcRouter) vr_it.next();
				vpdcVo.setUseLong(vpdcRouter.getBuyLong());
			} else {
				vpdcVo.setUseLong(null);// null表示时长为不限
			}
			vpdcVo_list.add(vpdcVo);
		}
		pageVpdc.setResult(vpdcVo_list);
		pageVpdc.setTotalCount(recordSize);
		return pageVpdc;
	}

	/**
	 * <分页获取User用户某一个VPDC下面的所有的云主机 当有判断条件时>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public Page<InstanceVO> getHostsByVpdcId(String sort, String query,
			Page<InstanceVO> page, User user, Long statusId, String type,
			String status_buss, long vpdcId) throws HsCloudException {
		int offSet, length;
		if (page.getPageNo() == 1) {
			offSet = 0;
			length = page.getPageSize();
		} else {
			offSet = (page.getPageNo() - 1) * page.getPageSize();
			length = page.getPageSize();
		}
		// 分页处理
		int recordSize = 0;
		List<VpdcReference> references = new ArrayList<VpdcReference>();
		List<InstanceVO> instanceVOs = new ArrayList<InstanceVO>();
		recordSize = vpdcDao.getVRCountByQuery(query, statusId, vpdcId);
		references = vpdcDao.findVpdcReferencesByQuery(offSet, length, sort,
				query, statusId, vpdcId);
		// 将查询出来的VM封装成VO
		loadUserReferences(references, instanceVOs);
		page.setResult(instanceVOs);
		page.setTotalCount(recordSize);
		return page;
	}

	@Override
	public void modifyBandwidthOfVm(String vmId, String bandwidth)
			throws HsCloudException {
		Integer bwt = 0;

		if (StringUtils.isNotBlank(bandwidth)) {
			bwt = Integer.valueOf(bandwidth);
			// update to datebase
			VpdcReference vpdcReference = vpdcDao.findVpdcReferenceByVmId(vmId);
			vpdcReference.setBwtIn(bwt);
			vpdcReference.setBwtOut(bwt);
			vpdcReference.setNetwork_bandwidth(bwt);
			vpdcDao.updateVpdcReference(vpdcReference);
			// set for openstack
			SetSecurityBandwidthRules ssbr = new SetSecurityBandwidthRules();
			BandwidthRule br = new BandwidthRule();
			br.setBwtIn(bwt);
			br.setBwtOut(bwt);
			ssbr.setBandwidthRule(br);
			try {
				openstackUtil.getCompute().servers().server(vmId)
						.setSecurityBandwidthRules(ssbr);
			} catch (Exception e) {
				throw new HsCloudException(Constants.VM_OPENSTACK_API_ERROR,
						"updateResourceLimit异常", log, e);
			}
		}

	}

	@Override
	public VpdcVo getVpdcById(long vpdcId) throws HsCloudException {
		Vpdc vpdc = vpdcDao.getVpdcById(vpdcId);
		VpdcVo vpdcVo = new VpdcVo();
		vpdcVo.setId(vpdc.getId());// VPDC的id
		vpdcVo.setName(vpdc.getName());// VPDC名称
		vpdcVo.setVpdcType(vpdc.getType());// VPDC类型（0：非路由；1：路由）
		vpdcVo.setZoneGroup(vpdc.getZoneGroupId());// 机房线路id
		String zoneGroupName = vpdcDao.findZoneGroupName(vpdc.getZoneGroupId());
		vpdcVo.setZoneGroupName(zoneGroupName);// 机房线路名称
		if (vpdc.getRouters() != null && vpdc.getRouters().size() > 0) {
			vpdcVo.setUseLong(vpdc.getRouters().iterator().next().getBuyLong());// 购买时长
			vpdcVo.setBandWidth(vpdc.getRouters().iterator().next()
					.getBandWidth());// 选择带宽
		}
		if (vpdc.getLans() != null && vpdc.getLans().size() > 0) {// VLAN设置
			Set<VpdcNetwork> vn_set = vpdc.getLans().iterator().next()
					.getLanNetwork();
			List<VpdcVo.VlanNetwork> vvvn_list = new ArrayList<VpdcVo.VlanNetwork>();
			for (VpdcNetwork vpdcNetwork : vn_set) {
				VpdcVo.VlanNetwork inner_VlanNetwork = new VpdcVo().new VlanNetwork();
				inner_VlanNetwork.setDns1(vpdcNetwork.getDns1());
				inner_VlanNetwork.setDns2(vpdcNetwork.getDns2());
				inner_VlanNetwork.setSize(vpdcNetwork.getNetworkSize());
				vvvn_list.add(inner_VlanNetwork);
			}
			vpdcVo.setVlans(vvvn_list);
		}
		return vpdcVo;
	}

	@Override
	public boolean deleteVPDC(long vpdcId) throws HsCloudException {
		boolean bl = true;
		Vpdc vpdc = vpdcDao.getVpdcById(vpdcId);
		// 首先验证是否有VM
		for (VpdcReference vr : vpdc.getReferences()) {
			if (vr.getStatus() == 0) {
				bl = false;
				break;
			}
		}
		// 然后验证是否有Router
		if (bl) {
			for (VpdcRouter vr : vpdc.getRouters()) {
				if (vr.getDeleted() == 0) {
					bl = false;
					break;
				}
			}
		}
		// 确认两子集均为空，则可删除VPDC
		if (bl) {
			vpdcDao.deleteVPDC(vpdc);
		}
		return bl;
	}

	@Override
	public boolean openRouter(String uuid, Object o, String otype)
			throws HsCloudException {
		try {
			VpdcRouter router = vpdcDao.getRouter(uuid);
			if ("ACTIVE".equalsIgnoreCase(router.getRouterStatus())
					|| !StringUtils.isEmpty(router.getRouterTaskStatus())) {
				log.debug("Open Router failed cause of the status is ACTIVE");
				return false;
			}
			router.setRouterTaskStatus("RESUMING");
			vpdcDao.saveRouter(router);
			HcEventVmOps hevo = loadHcOps(o, otype);
			hevo.setObj_type((short) 1);
			hevo.setObj_name(router.getName());
			hevo.setOps(LogOPSType.START.getIndex());
			hevo.setReference_id(router.getId());
			hevo.setUuid(uuid);
			Map<String, String> vmMap = new HashMap<String, String>();
			vmMap.put("uuid", uuid);
			rabbitMqUtil.sendMessage(Constants.JOBSERVER_METHOD_RouterOpen,
					vmMap, hevo, "HcEventVmOps");
		} catch (Exception e) {
			throw new HsCloudException(Constants.ROUTER_START_ERROR,
					"openRouter Exception", log, e);
		}
		return true;
	}

	@Override
	public boolean closeRouter(String uuid, Object o, String otype)
			throws HsCloudException {
		try {
			VpdcRouter router = vpdcDao.getRouter(uuid);
			if (!"ACTIVE".equalsIgnoreCase(router.getRouterStatus())
					|| !StringUtils.isEmpty(router.getRouterTaskStatus())) {
				log.debug("Close Router failed cause of the status is't ACTIVE");
				return false;
			}
			router.setRouterTaskStatus("SUSPENDING");
			vpdcDao.saveRouter(router);
			HcEventVmOps hevo = this.loadHcOps(o, otype);
			hevo.setObj_type((short) 1);
			hevo.setObj_name(router.getName());
			hevo.setReference_id(router.getId());
			hevo.setOps(LogOPSType.SHUTDOWN.getIndex());
			hevo.setUuid(uuid);
			Map<String, String> vmMap = new HashMap<String, String>();
			vmMap.put("uuid", uuid);
			rabbitMqUtil.sendMessage(Constants.JOBSERVER_METHOD_RouterClose,
					vmMap, hevo, "HcEventVmOps");
		} catch (Exception e) {
			throw new HsCloudException(Constants.ROUTER_CLOSE_ERROR,
					"closeRouter Exception", log, e);
		}
		return true;
	}

	@Override
	public String rebootRouter(String uuid, Object o, String otype)
			throws HsCloudException {
		String result = null;
		try {
			VpdcRouter router = vpdcDao.getRouter(uuid);
			router.setRouterTaskStatus("REBOOTING_HARD");
			vpdcDao.saveRouter(router);
			HcEventVmOps hevo = this.loadHcOps(o, otype);
			hevo.setObj_type((short) 1);
			hevo.setObj_name(router.getName());
			hevo.setReference_id(router.getId());
			hevo.setUuid(uuid);
			hevo.setOps(LogOPSType.REBOOT.getIndex());
			Map<String, String> vmMap = new HashMap<String, String>();
			vmMap.put("uuid", uuid);
			rabbitMqUtil.sendMessage(Constants.JOBSERVER_METHOD_RouterReboot,
					vmMap, hevo, "HcEventVmOps");
		} catch (Exception e) {
			throw new HsCloudException(Constants.ROUTER_REBOOT_ERROR,
					"rebootRouter Exception", log, e);
		}
		return result;
	}

	@Override
	public VpdcRouterVo getRouterByVpdcId(long vpdcId) throws HsCloudException {
		VpdcRouter vr = vpdcDao.getRouterByVpdcId(vpdcId);
		if (vr == null) {
			return null;
		}
		VpdcRouterVo vrVO = new VpdcRouterVo();
		vrVO.setId(vr.getId());
		vrVO.setName(vr.getName());
		vrVO.setFixIP(vr.getFixIP());// 内网IP
		vrVO.setFloatingIP(vr.getFloatingIP());// 外网IP
		vrVO.setOsId(vr.getOsId());
		vrVO.setRouterUUID(vr.getRouterUUID());
		String osName = vpdcDao.findOsName(vr.getOsId());// osName 操作系统名称
		vrVO.setOsName(osName);
		vrVO.setCpuCore(vr.getCpuCore());
		vrVO.setDiskCapacity(vr.getDiskCapacity());
		vrVO.setMemSize(vr.getMemSize());
		vrVO.setBuyLong(vr.getBuyLong());// 购买时长（单位：月）
		VpdcRouter_Period vrp = vpdcDao.getRouterPeriod(vr.getId());
		if (vrp != null) {
			Date now = new Date();
			Date startTime = vrp.getStartTime();
			// 如果开始时间为空，则已使用时长为null
			if (startTime != null) {
				long between = (now.getTime() - startTime.getTime()) / 1000;// 除以1000是为了转换成秒
				Date usedTime = new Date(between);
				vrVO.setUsedTime(usedTime);// 使用时间或叫运行时间
			}
			// 如果终止时间不为空
			if (vrp.getEndTime() != null) {
				long titleTime = 0;
				Long remainingTime = 0L;
				if (startTime != null) {// 在开始时间不为空下正常计时
					titleTime = (vrp.getEndTime().getTime() - startTime
							.getTime()) / 1000;
					remainingTime = titleTime - vrVO.getUsedTime().getTime();
					if (remainingTime <= 0) {
						vrVO.setUsedTime(new Date(titleTime));
					}
				} else {// 在开始时间为空下则为终止时间减去当前时间
					remainingTime = (vrp.getEndTime().getTime() - new Date()
							.getTime()) / 1000;
				}
				vrVO.setRemainingTime(remainingTime);// remainingTime
														// 剩余时间（单位：毫秒）
			}
		}
		vrVO.setRouterStatus(vr.getRouterStatus());
		vrVO.setRouterTaskStatus(vr.getRouterTaskStatus());
		return vrVO;
	}

	@Override
	public int getVmBuilingCountByZoneCode(String zoneCode)
			throws HsCloudException {
		return vpdcDao.getVmBuilingCountByZoneCode(zoneCode);
	}

	@Override
	public String getVMNameSerialNumber(String tableName)
			throws HsCloudException {

		return vpdcDao.getVMNameSerialNumber(tableName);
	}

	@Override
	public String getDomainIdByUserId(Long userId) throws HsCloudException {
		return vpdcDao.getDomainIdByUserId(userId);
	}

	@SuppressWarnings("static-access")
	@Override
	public String getOptimalZoneOfZones(String[] zones, int vmNum)
			throws HsCloudException {
		Map<Double, String> zoneMap = new HashMap<Double, String>();
		String result = "";
		try {
			int valueCount = 0;
			Double theoreticalValue = 0.0;
			// int multi =
			// Integer.valueOf(propertiesUtils.getProperties("common.properties").get("theoreticalCountBase"));
			int multi = 0;
			String theoreticalCountBase = propertiesUtils.getProperties(
					"common.properties").get("theoreticalCountBase");
			multi = theoreticalCountBase == null ? 0 : Double.valueOf(
					Math.floor(Double.valueOf(theoreticalCountBase.toString())
							.doubleValue())).intValue();
			// Integer multi =
			// propertiesUtils.getProperties("common.properties").get("theoreticalCountBase");
			for (String zone : zones) {
				String nodeActive = RedisUtil.getValue("ZoneAcquisition_"
						+ zone);
				if (StringUtils.isNotEmpty(nodeActive)) {
					JSONObject jsObject = JSONObject.fromObject(nodeActive);
					// theoreticalCount可用节点数
					Object theoreticalCount = jsObject.get("theoreticalCount");// theoreticalCount
					valueCount = theoreticalCount == null ? 0 : Double
							.valueOf(
									Math.floor(Double.valueOf(
											theoreticalCount.toString())
											.doubleValue())).intValue();
					// valueCount=500;
					if (valueCount > 0) {
						int ipNum = this.getIPCountByZoneCode(zone);
						int vmBuiling = this.getVmBuilingCountByZoneCode(zone);
						int predictIPNum = Integer.valueOf(ipNum) - vmNum;
						int predictCreateVMNum = Integer.valueOf(valueCount)
								* multi - vmBuiling - vmNum;
						if (predictIPNum >= 0 && predictCreateVMNum >= 0) {
							// theoreticalValue cpu负载
							Object nodeLoadAverage = jsObject
									.get("theoreticalValue");
							theoreticalValue = nodeLoadAverage == null ? 0
									: Double.valueOf(Math.floor(Double.valueOf(
											nodeLoadAverage.toString())
											.doubleValue()));
							zoneMap.put(theoreticalValue, zone);
						}
					}

				}
			}
			if (!zoneMap.isEmpty()) {
				List<Map.Entry<Double, String>> value = new ArrayList<Map.Entry<Double, String>>(
						zoneMap.entrySet());
				Collections.sort(value,
						new Comparator<Map.Entry<Double, String>>() {
							@Override
							public int compare(Entry<Double, String> o1,
									Entry<Double, String> o2) {

								return (int) (o2.getKey() - o1.getKey());
							}

						});
				result = value.get(0).getValue();
			}

		} catch (Exception e) {
			throw new HsCloudException(Constants.SYSTEM_BUSY_NOW,
					"getOptimalZoneOfZones Exception", log, e);
		}
		return result;

	}

	@Override
	public int getIPCountByZoneCode(String zoneCode) throws HsCloudException {

		return vpdcDao.getIPCountByZoneCode(zoneCode);
	}

	@Override
	public List<VpdcReferenceQuotaInfo> getVmsByUser(long userId)
			throws HsCloudException {
		List<VpdcReferenceQuotaInfo> vpdcRefernces = new ArrayList<VpdcReferenceQuotaInfo>();
		vpdcRefernces = vpdcDao.findVpdcReferenceByUserId(userId);
		return vpdcRefernces;
	}

	@Override
	public int getVmsCountByUser(long userId) throws HsCloudException {
		return vpdcDao.getVmsCountByUser(userId);
	}

	@Override
	public int getVmRenewalCountByUser(long userId) throws HsCloudException {
		return vpdcDao.getVmRenewalCountByUser(userId);
	}

}