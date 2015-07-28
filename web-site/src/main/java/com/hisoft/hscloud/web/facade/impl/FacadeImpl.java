package com.hisoft.hscloud.web.facade.impl;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.openstack.model.compute.nova.NovaServerForCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.constant.BillConstant;
import com.hisoft.hscloud.bss.billing.constant.ConsumeType;
import com.hisoft.hscloud.bss.billing.constant.DepositSource;
import com.hisoft.hscloud.bss.billing.constant.FlowsType;
import com.hisoft.hscloud.bss.billing.constant.PaymentType;
import com.hisoft.hscloud.bss.billing.constant.ServiceType;
import com.hisoft.hscloud.bss.billing.constant.TranscationType;
import com.hisoft.hscloud.bss.billing.dao.TranscationLogDao;
import com.hisoft.hscloud.bss.billing.entity.Account;
import com.hisoft.hscloud.bss.billing.entity.IncomingLog;
import com.hisoft.hscloud.bss.billing.entity.PaymentLog;
import com.hisoft.hscloud.bss.billing.entity.TranscationLog;
import com.hisoft.hscloud.bss.billing.service.AccountService;
import com.hisoft.hscloud.bss.billing.service.IncomingLogService;
import com.hisoft.hscloud.bss.billing.service.PaymentLogService;
import com.hisoft.hscloud.bss.billing.service.TranscationLogService;
import com.hisoft.hscloud.bss.billing.vo.AccountVO;
import com.hisoft.hscloud.bss.billing.vo.TranscationLogVO;
import com.hisoft.hscloud.bss.sla.om.entity.InvoiceAccount;
import com.hisoft.hscloud.bss.sla.om.entity.InvoiceRecord;
import com.hisoft.hscloud.bss.sla.om.entity.InvoiceRecordTransaction;
import com.hisoft.hscloud.bss.sla.om.entity.Order;
import com.hisoft.hscloud.bss.sla.om.entity.OrderItem;
import com.hisoft.hscloud.bss.sla.om.entity.OrderProduct;
import com.hisoft.hscloud.bss.sla.om.entity.VmRefundLog;
import com.hisoft.hscloud.bss.sla.om.service.InvoiceAccountService;
import com.hisoft.hscloud.bss.sla.om.service.InvoiceRecordService;
import com.hisoft.hscloud.bss.sla.om.service.InvoiceRecordTransactionService;
import com.hisoft.hscloud.bss.sla.om.service.OrderItemService;
import com.hisoft.hscloud.bss.sla.om.service.OrderService;
import com.hisoft.hscloud.bss.sla.om.service.VmRefundLogService;
import com.hisoft.hscloud.bss.sla.om.util.OrderUtils;
import com.hisoft.hscloud.bss.sla.om.util.SubmitOrderData;
import com.hisoft.hscloud.bss.sla.om.vo.InvoiceVO;
import com.hisoft.hscloud.bss.sla.om.vo.OrderItemVo;
import com.hisoft.hscloud.bss.sla.om.vo.OrderVo;
import com.hisoft.hscloud.bss.sla.om.vo.QueryCondition;
import com.hisoft.hscloud.bss.sla.om.vo.TakeInvoiceType;
import com.hisoft.hscloud.bss.sla.om.vo.VmRefundLogVO;
import com.hisoft.hscloud.bss.sla.sc.entity.Cpu;
import com.hisoft.hscloud.bss.sla.sc.entity.Disk;
import com.hisoft.hscloud.bss.sla.sc.entity.ExtDisk;
import com.hisoft.hscloud.bss.sla.sc.entity.Network;
import com.hisoft.hscloud.bss.sla.sc.entity.Os;
import com.hisoft.hscloud.bss.sla.sc.entity.Ram;
import com.hisoft.hscloud.bss.sla.sc.entity.ScFeeType;
import com.hisoft.hscloud.bss.sla.sc.entity.ScIsolationConfig;
import com.hisoft.hscloud.bss.sla.sc.entity.ServerNode;
import com.hisoft.hscloud.bss.sla.sc.entity.ServerZone;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceCatalog;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceItem;
import com.hisoft.hscloud.bss.sla.sc.entity.Software;
import com.hisoft.hscloud.bss.sla.sc.entity.ZoneGroup;
import com.hisoft.hscloud.bss.sla.sc.service.IServerNodeService;
import com.hisoft.hscloud.bss.sla.sc.service.IServerZoneService;
import com.hisoft.hscloud.bss.sla.sc.service.IServiceCatalogService;
import com.hisoft.hscloud.bss.sla.sc.service.IServiceItemService;
import com.hisoft.hscloud.bss.sla.sc.service.ZoneGroupService;
import com.hisoft.hscloud.bss.sla.sc.utils.CloudCache;
import com.hisoft.hscloud.bss.sla.sc.vo.OAZoneGroupVO;
import com.hisoft.hscloud.bss.sla.sc.vo.ScFeeTypeVo;
import com.hisoft.hscloud.bss.sla.sc.vo.ZoneGroupVO;
import com.hisoft.hscloud.common.entity.DefaultIsolationConfig;
import com.hisoft.hscloud.common.entity.HcEventResource;
import com.hisoft.hscloud.common.entity.HcEventVmOps;
import com.hisoft.hscloud.common.entity.LogOperatorType;
import com.hisoft.hscloud.common.entity.MarketingPromotion;
import com.hisoft.hscloud.common.entity.OperationLog;
import com.hisoft.hscloud.common.entity.Province;
import com.hisoft.hscloud.common.entity.UserBrand;
import com.hisoft.hscloud.common.service.DataService;
import com.hisoft.hscloud.common.service.DefaultIsolationConfigService;
import com.hisoft.hscloud.common.service.MarketingPromotionService;
import com.hisoft.hscloud.common.service.OperationLogService;
import com.hisoft.hscloud.common.service.UserBrandService;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.MD5Util;
import com.hisoft.hscloud.common.util.PasswordUtil;
import com.hisoft.hscloud.common.util.PropertiesUtils;
import com.hisoft.hscloud.common.util.RESTUtil;
import com.hisoft.hscloud.common.util.RabbitMQUtil;
import com.hisoft.hscloud.common.util.SocketUtil;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.common.util.Utils;
import com.hisoft.hscloud.controlpanel.entity.ControlPanelUser;
import com.hisoft.hscloud.controlpanel.service.ControlPanelService;
import com.hisoft.hscloud.crm.usermanager.constant.InviteState;
import com.hisoft.hscloud.crm.usermanager.constant.UserState;
import com.hisoft.hscloud.crm.usermanager.constant.UserType;
import com.hisoft.hscloud.crm.usermanager.entity.AbstractUser;
import com.hisoft.hscloud.crm.usermanager.entity.AccessAccount;
import com.hisoft.hscloud.crm.usermanager.entity.Action;
import com.hisoft.hscloud.crm.usermanager.entity.Company;
import com.hisoft.hscloud.crm.usermanager.entity.CompanyInvite;
import com.hisoft.hscloud.crm.usermanager.entity.Country;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.crm.usermanager.entity.Industry;
import com.hisoft.hscloud.crm.usermanager.entity.MaintenanceLog;
import com.hisoft.hscloud.crm.usermanager.entity.Permission;
import com.hisoft.hscloud.crm.usermanager.entity.PlatformRelation;
import com.hisoft.hscloud.crm.usermanager.entity.Region;
import com.hisoft.hscloud.crm.usermanager.entity.Resource;
import com.hisoft.hscloud.crm.usermanager.entity.ResourceType;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.entity.UserBank;
import com.hisoft.hscloud.crm.usermanager.entity.UserGroup;
import com.hisoft.hscloud.crm.usermanager.entity.UserProfile;
import com.hisoft.hscloud.crm.usermanager.exception.MailAddressException;
import com.hisoft.hscloud.crm.usermanager.service.AccessAccountService;
import com.hisoft.hscloud.crm.usermanager.service.ActionService;
import com.hisoft.hscloud.crm.usermanager.service.CompanyInviteService;
import com.hisoft.hscloud.crm.usermanager.service.CompanyService;
import com.hisoft.hscloud.crm.usermanager.service.DomainService;
import com.hisoft.hscloud.crm.usermanager.service.MaintenanceLogService;
import com.hisoft.hscloud.crm.usermanager.service.OptionService;
import com.hisoft.hscloud.crm.usermanager.service.PermissionService;
import com.hisoft.hscloud.crm.usermanager.service.PlatformRelationService;
import com.hisoft.hscloud.crm.usermanager.service.ResourceService;
import com.hisoft.hscloud.crm.usermanager.service.ResourceTypeService;
import com.hisoft.hscloud.crm.usermanager.service.RoleService;
import com.hisoft.hscloud.crm.usermanager.service.TabResultService;
import com.hisoft.hscloud.crm.usermanager.service.TreeService;
import com.hisoft.hscloud.crm.usermanager.service.UserBankService;
import com.hisoft.hscloud.crm.usermanager.service.UserGroupPermissionService;
import com.hisoft.hscloud.crm.usermanager.service.UserGroupService;
import com.hisoft.hscloud.crm.usermanager.service.UserProfileService;
import com.hisoft.hscloud.crm.usermanager.service.UserService;
import com.hisoft.hscloud.crm.usermanager.service.UserUserGroupService;
import com.hisoft.hscloud.crm.usermanager.util.Constant;
import com.hisoft.hscloud.crm.usermanager.vo.CheckboxVO;
import com.hisoft.hscloud.crm.usermanager.vo.CompanyInviteVO;
import com.hisoft.hscloud.crm.usermanager.vo.Node;
import com.hisoft.hscloud.crm.usermanager.vo.PerGroupVO;
import com.hisoft.hscloud.crm.usermanager.vo.PermissionVO;
import com.hisoft.hscloud.crm.usermanager.vo.PrivilegeVO;
import com.hisoft.hscloud.crm.usermanager.vo.ProfileVo;
import com.hisoft.hscloud.crm.usermanager.vo.TreeQueryVO;
import com.hisoft.hscloud.crm.usermanager.vo.TreeVO;
import com.hisoft.hscloud.crm.usermanager.vo.UserGroupVO;
import com.hisoft.hscloud.crm.usermanager.vo.UserVO;
import com.hisoft.hscloud.crm.usermanager.vo.UserVO1;
import com.hisoft.hscloud.mail.constant.MailTemplateType;
import com.hisoft.hscloud.mail.service.MailService;
import com.hisoft.hscloud.message.entity.Announcement;
import com.hisoft.hscloud.message.entity.Message;
import com.hisoft.hscloud.message.service.AnnouncementService;
import com.hisoft.hscloud.message.service.MessageService;
import com.hisoft.hscloud.payment.alipay.config.AlipayConfig;
import com.hisoft.hscloud.payment.alipay.entity.PaymentInterface;
import com.hisoft.hscloud.payment.alipay.service.AlipayService;
import com.hisoft.hscloud.payment.alipay.util.PaymentVendor;
import com.hisoft.hscloud.payment.alipay.util.TradeStatus;
import com.hisoft.hscloud.payment.alipay.util.TradeType;
import com.hisoft.hscloud.payment.alipay.util.UtilDate;
import com.hisoft.hscloud.systemmanagement.service.HcEventVmOpsService;
import com.hisoft.hscloud.systemmanagement.vo.LogQueryVO;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.service.IPService;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.vo.IPDetailInfoVO;
import com.hisoft.hscloud.vpdc.ops.entity.VmSnapShotPlan;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference_OrderItem;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference_Period;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference_extdisk;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcRenewal;
import com.hisoft.hscloud.vpdc.ops.json.bean.CreateVmBean;
import com.hisoft.hscloud.vpdc.ops.json.bean.CreateVpdcBean;
import com.hisoft.hscloud.vpdc.ops.json.bean.CreateVpdcBean.VlanNetwork;
import com.hisoft.hscloud.vpdc.ops.json.bean.VmExtDiskBean;
import com.hisoft.hscloud.vpdc.ops.service.Operation;
import com.hisoft.hscloud.vpdc.ops.service.OperationImpl;
import com.hisoft.hscloud.vpdc.ops.service.VpdcRenewalService;
import com.hisoft.hscloud.vpdc.ops.service.VpdcVrouterService;
import com.hisoft.hscloud.vpdc.ops.util.VMStatusBussEnum;
import com.hisoft.hscloud.vpdc.ops.util.VMTryStatusBussEnum;
import com.hisoft.hscloud.vpdc.ops.util.VMTypeEnum;
import com.hisoft.hscloud.vpdc.ops.vo.FlavorVO;
import com.hisoft.hscloud.vpdc.ops.vo.ImageVO;
import com.hisoft.hscloud.vpdc.ops.vo.InstanceDetailVo;
import com.hisoft.hscloud.vpdc.ops.vo.InstanceVO;
import com.hisoft.hscloud.vpdc.ops.vo.RenewOrderVO;
import com.hisoft.hscloud.vpdc.ops.vo.SecurityPolicyVO;
import com.hisoft.hscloud.vpdc.ops.vo.UnaddedIntranetInstanceVO;
import com.hisoft.hscloud.vpdc.ops.vo.VmExtranetSecurityVO;
import com.hisoft.hscloud.vpdc.ops.vo.VmIntranetSecurityVO;
import com.hisoft.hscloud.vpdc.ops.vo.VmSnapShotPlanVO;
import com.hisoft.hscloud.vpdc.ops.vo.VmSnapShotVO;
import com.hisoft.hscloud.vpdc.ops.vo.VncClientVO;
import com.hisoft.hscloud.vpdc.ops.vo.VolumeVO;
import com.hisoft.hscloud.vpdc.ops.vo.VpdcRouterVo;
import com.hisoft.hscloud.vpdc.ops.vo.VpdcVo;
import com.hisoft.hscloud.vpdc.ops.vo.VrouterTemplateVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.service.MonitorService;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.CPUHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.DiskHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.MemoryHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.NetHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.VmRealtimeMonitorVO;
import com.hisoft.hscloud.web.facade.Facade;
import com.hisoft.hscloud.web.param.CreateVmForBuyParamObject;

@Service
public class FacadeImpl implements Facade {
	private Logger logger = Logger.getLogger(this.getClass());
	/******************************* OPS Module ********************************/
	@Autowired
	private Operation operation;
	@Autowired
	private UserService userService;
	@Autowired
	private UserGroupService userGroupService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private IPService iPService;
	@Autowired
	private UserUserGroupService userUserGroupService;

	/******************************* Monitor Module ********************************/
	@Autowired
	private MonitorService monitorService;

	/******************************* UserManager Module ********************************/
	@Autowired
	private OptionService optService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private MailService mailService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private TabResultService tabResultService;
	@Autowired
	private UserGroupPermissionService userGroupPermissionService;
	@Autowired
	private IServiceCatalogService serviceCatalogService;
	@Autowired
	private IServerZoneService serverZoneService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private ResourceTypeService resourceTypeService;
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private UserBankService userBankService;
	@Autowired
	private TranscationLogService transcationLogService;
	@Autowired
	private TranscationLogDao transcationLogDao;
	@Autowired
	private IServiceItemService serviceItemService;
	@Autowired
	private AlipayService alipayService;
	@Autowired
	private ActionService actionService;
	@Autowired
	private TreeService treeService;
	@Autowired
	private CompanyInviteService companyInviteService;
	@Autowired
	private ControlPanelService controlPanelService;
	@Autowired
	private UserBrandService userBrandService;
	@Autowired
	private PaymentLogService paymentLogService;
	@Autowired
	private AnnouncementService announcementService;
	@Autowired
	private InvoiceAccountService invoiceAccountService;
	@Autowired
	private InvoiceRecordService invoiceRecordService;
	@Autowired
	private InvoiceRecordTransactionService invoiceRecordTransactionService;
	@Autowired
	private DataService dataService;
	@Autowired
	private DomainService domainService;
	@Autowired
	private VmRefundLogService vmRefundLogService;
	@Autowired
	private VpdcRenewalService vpdcRenewalService;
	@Autowired
	private IncomingLogService incomingLogService;
	@Autowired
	private ZoneGroupService zoneGroupService;
	@Autowired
	private IServerNodeService nodeService;
	@Autowired
	private HcEventVmOpsService vmOpsLogService;
	@Autowired
	private MaintenanceLogService maintenanceLogService;
	@Autowired
	private MarketingPromotionService marketingPromotionService;
	@Autowired
	private OperationLogService logService;
	@Autowired
	private UserProfileService userProfileService;
	@Autowired
	private IServerZoneService zoneService;
	@Autowired
	private DefaultIsolationConfigService defaultIsolationConfigService;
	@Autowired
	private VpdcVrouterService vrouterService;
	@Autowired
	private CloudCache cache;
	@Autowired
	private AccessAccountService accessAccountService;
	@Autowired
	private PlatformRelationService platformRelationService;	
	public static Map<String, ResourceType> resourceTypeMap = new HashMap<String, ResourceType>();
	public static List<ResourceType> resourceTypeList = new ArrayList<ResourceType>();
	private SocketUtil socket = new SocketUtil();

	@Override
	public Page<InstanceVO> getVmsByUser(String page, String limit,
			Page<InstanceVO> pageIns, String queryType,String query,String sort,User user,
			List<Object> referenceIds,Long statusId,String type,String status_buss)
					throws HsCloudException {
		logger.info("OPS-Facade-getVmsByUser start");
		try {
			int pageNo = 1;
			if (StringUtils.isNumeric(page)) {
				pageNo = Integer.parseInt(page);
			}
			int PageSize = 3;
			if (StringUtils.isNumeric(limit)) {
				PageSize = Integer.parseInt(limit);
			}
			pageIns.setPageNo(pageNo);
			pageIns.setPageSize(PageSize);
			if (!StringUtils.isEmpty(query)) {
				// 当有判断条件时
				pageIns = operation.fuzzyFindVmsUser(sort,queryType,query, pageIns, user,referenceIds,statusId,type,status_buss);
			} else {
				// 当无判断条件时
				pageIns = operation.getVmsByUser(sort,user,referenceIds, pageIns,statusId,type,status_buss);
			}
		} catch (HsCloudException e) {
			throw new HsCloudException(Constants.VM_LIST_USER_ERROR, "getVmsByUser异常",
					logger, e);
		}
		return pageIns;
	}

	@Override
	public Page<InstanceVO> getHostsByUser(String page, String limit, Page<InstanceVO> pageIns, 
			String query, String sort, User user, List<Object> referenceIds, Long statusId, 
			String type, String status_buss) throws HsCloudException {
		logger.info("OPS-Facade-getHostsByUser start");
		try {
			int pageNo = 1;
			if (StringUtils.isNumeric(page)) {
				pageNo = Integer.parseInt(page);
			}
			int PageSize = 3;
			if (StringUtils.isNumeric(limit)) {
				PageSize = Integer.parseInt(limit);
			}
			pageIns.setPageNo(pageNo);
			pageIns.setPageSize(PageSize);
			pageIns = operation.getHostsByUser(sort,query, pageIns, user,referenceIds,
					statusId,type,status_buss,true);
		} catch (HsCloudException e) {
			throw new HsCloudException(Constants.VM_LIST_USER_ERROR, "getHostsByUser异常",
					logger, e);
		}
		return pageIns;
	}	

	/**
	 * <用途: 1.默认加载分页获取User用户所有的待添加内网安全策略的云主机
	 *       2.当点击内网安全策略的搜索按钮: 分页获取User用户所有的待添加内网安全策略的云主机> 
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	@Override
	public Page<UnaddedIntranetInstanceVO> getWaitAddIntranetSecurityVms(
			String page, String limit, Page<UnaddedIntranetInstanceVO> pageUnaddedIns, 
			String query, String sort, User user, List<Object> referenceIds, 
			Long statusId, String type, String status_buss, String groupId)
					throws HsCloudException {
		logger.info("OPS-Facade-getWaitAddIntranetSecurityVms start");
		try {
			int pageNo = 1;
			if (StringUtils.isNumeric(page)) {
				pageNo = Integer.parseInt(page);
			}
			int PageSize = 8;
			if (StringUtils.isNumeric(limit)) {
				PageSize = Integer.parseInt(limit);
			}
			pageUnaddedIns.setPageNo(pageNo);
			pageUnaddedIns.setPageSize(PageSize);
			// 当待添加的主机输入框有判断条件时
			pageUnaddedIns = operation.getWaitAddIntranetSecurityVms(sort, query, pageUnaddedIns, 
					user, referenceIds, statusId, type, status_buss, groupId);
		}
		catch (HsCloudException e) {
			throw new HsCloudException(Constants.VM_LIST_USER_ERROR, "getWaitAddIntranetSecurityVms异常", logger, e);
		}
		return pageUnaddedIns;
	}

	/**
	 * <用途: 获取当前用户的当前uuid这台虚拟机的添加了外网安全的协议和端口的详细信息
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	@Override
	public List<VmExtranetSecurityVO> getExtranetSecurityInfo(String uuid)
			throws HsCloudException {
		try {
			logger.info("OPS-Facade-getExtranetSecurityInfo start");
			List<VmExtranetSecurityVO> ves_list = null;
			ves_list = operation.getExtranetSecurityInfo(uuid);
			return ves_list;
		} catch (HsCloudException e) {
			throw new HsCloudException(Constants.VM_LIST_USER_ERROR,
					"getExtranetSecurityInfo异常", logger, e);
		}
	}

	@Override
	public boolean startVmByVmId(String vmId,Object o,String otype) {

		logger.info("OPS-Facade-startVmByVmId start");
		boolean bl = false;
		try {
			bl = operation.openvm(vmId,o,otype);
		} catch (HsCloudException e) {
			throw new HsCloudException(Constants.VM_START_ERROR, "startVmByVmId异常",
					logger, e);
		}
		return bl;
	}

	@Override
	public String rebootVmByVmId(String vmId,Object o,String otype) {
		logger.info("OPS-Facade-rebootVmByVmId start");
		String result;
		try {
			result = operation.rebootVm(vmId,o,otype);
		} catch (HsCloudException e) {
			throw new HsCloudException(Constants.VM_REBOOT_ERROR, "rebootVmByVmId异常",
					logger, e);
		}
		return result;
	}

	@Override
	public boolean closeVmByVmId(String vmId,Object o,String otype) {
		logger.info("OPS-Facade-suspendVmByVmId start");
		boolean bl = false;
		try {
			bl = operation.closeVm(vmId,o,otype);
		} catch (HsCloudException e) {
			throw new HsCloudException(Constants.VM_CLOSE_ERROR, "suspendVmByVmId异常",
					logger, e);
		}
		return bl;
	}

	@Override
	public String getVNCUrl(String vmId) {
		logger.info("OPS-Facade-getVNCUrl start");
		String result;
		try {
			result = operation.getVnc(vmId);
		} catch (HsCloudException e) {
			throw new HsCloudException(Constants.VM_VNC_ERROR, "getVNCUrl异常", logger,
					e);
		}
		return result;
	}

	@Override
	@Transactional
	public VncClientVO getClientVnc(String uuid,User u) throws HsCloudException{
		logger.info("OPS-Facade-getClientVnc start");
		VncClientVO vnc = new VncClientVO();
		try {
			vnc.setProxyIP(socket.getProxyIP());
			//获取业务数据
			InstanceVO instanceVo = operation.getInstance(uuid);
			String UUID = instanceVo.getId();
			String nodeName = instanceVo.getHostName();
			if(StringUtils.isEmpty(nodeName)){
				logger.error("vm's nodename is null");
			}
			ServerNode sn = nodeService.getNodeByName(nodeName);
			String IP = "";
			if(sn!=null){
				IP = sn.getIp();
			}
			//Socket传送业务请求
			JSONObject jsonChild = new JSONObject();
			jsonChild.accumulate("IP", IP);
			jsonChild.accumulate("UUID", UUID);
			JSONObject json = new JSONObject();
			json.accumulate("Node", jsonChild);
			String response = socket.sendRequest(IP,json.toString());
			//String response = json.toString();
			//处理vnc参数
			JSONObject responseJson = JSONObject.fromObject(response);
			Object responsePort = JSONObject.fromObject(responseJson.get("Node")).get("port");
			Integer port = Integer.valueOf(responsePort.toString());
			Integer proxyPort = operation.getAvailablePort(u.getDomain().getId());
			if(proxyPort==null){
				HsCloudException he = new HsCloudException();
				he.setCode(Constants.VM_VNC_CLIENT_PORT);
				//抛无可用的VNC代理端口
				throw new HsCloudException(Constants.VM_VNC_CLIENT_PORT, "getClientVnc异常", logger,
						he);
			}else{
				//要确保服务器安装了socat，才能执行
				String command = "socat TCP4-LISTEN:"+proxyPort+",reuseaddr TCP4:"+IP+":"+port;
				try {
					Runtime.getRuntime().exec(command);
					vnc.setProxyPort(proxyPort);
					operation.useVncPort(proxyPort.toString(), u, uuid);
				} catch (Exception e) {
					throw new HsCloudException(Constants.VM_VNC_ERROR, "getClientVnc异常", logger,e);
				}
			}
		} catch (HsCloudException e) {
			if(e.getClass()==new HsCloudException().getClass()){
				HsCloudException ex = (HsCloudException)e;
				throw new HsCloudException(ex.getCode(), "getClientVnc异常", logger, ex);
			}else{
				throw new HsCloudException(Constants.VM_VNC_ERROR, "getClientVnc异常", logger,e);
			}
		}
		return vnc;
	}

	@Override
	public String backupVmByVmId(String vmId,String sn_name, String sn_comments, int sn_type,Object o,String otype) {
		logger.info("OPS-Facade-backupVmByVmId start");
		String result;
		try {
			result = operation.backupsVm(vmId, sn_name,
					sn_comments, sn_type,o,otype);
		} catch (HsCloudException e) {
			throw new HsCloudException(Constants.VM_BACKUP_ERROR, "backupVmByVmId异常",
					logger, e);
		}
		return result;
	}

	@Override
	public String createBackupVmPlanByVmId(VmSnapShotPlan vssp) {
		logger.info("OPS-Facade-createBackupVmPlanByVmId start");
		String result;
		try {
			result = operation.backupsVmPlan(vssp);
		} catch (HsCloudException e) {
			throw new HsCloudException(Constants.VM_BACKUP_PLAN_ERROR,
					"createBackupVmPlanByVmId异常", logger, e);
		}
		return result;
	}

	@Override
	public String deleteBackupByBackupId(String id,String ssid) {
		logger.info("OPS-Facade-deleteBackupByBackupId start");
		String result;
		try {
			result = operation.deleteSnapShot(id,ssid);
		} catch (HsCloudException e) {
			throw new HsCloudException(Constants.VM_SNAPSHOT_DELETE_ERROR,
					"deleteBackupByBackupId异常", logger, e);
		}
		return result;
	}

	@Override
	public VmSnapShotPlanVO getBackupVmPlanByVmId(String vmId) {
		logger.info("OPS-Facade-getBackupVmPlanByVmId start");
		VmSnapShotPlanVO vsspVO = null;
		try {
			vsspVO = operation.getVmSnapShotPlanByVmId(vmId);
		} catch (HsCloudException e) {
			throw new HsCloudException(Constants.VM_SNAPSHOT_GET_ERROR,
					"getBackupVmPlanByVmId异常", logger, e);
		}
		return vsspVO;
	}

	@Override
	public List<VmSnapShotVO> findAllBackupsByVmId(String vmId) {
		logger.info("OPS-Facade-findAllBackupsByVmId start");
		List<VmSnapShotVO> lvssVO = null;
		try {
			lvssVO = operation.findAllSnapShots(vmId);
		} catch (HsCloudException e) {
			throw new HsCloudException(Constants.VM_SNAPSHOT_LIST_ERROR,
					"findAllBackupsByVmId异常", logger, e);
		}
		return lvssVO;
	}

	@Override
	public String renewVM(String vmId, String backupId,User u,String otype) {
		logger.info("OPS-Facade-renewVM start");
		String result;
		try {
			result = operation.renewVm(vmId, backupId,u,otype);
		} catch (HsCloudException e) {
			throw new HsCloudException(Constants.VM_RENEW_ERROR, "renewVM异常", logger, e);
		}
		return result;
	}

	@Override
	public void terminateVmByVmId(String vmId,String vmName,String terminateFlag,long uid) {
		logger.info("OPS-Facade-terminateVmByVmId start");
		try {
			operation.terminate(vmId,vmName,terminateFlag,uid);
		} catch (HsCloudException e) {
			throw new HsCloudException(Constants.VM_TERMINATE_ERROR, "terminateVmByVmId异常",
					logger, e);
		}
	}

	@Override
	public void terminateVmWhenRefund(String uuid, User user, String comments) {
		logger.info("OPS-Facade-terminateVmWhenRefund start");
		try {
			operation.terminate(uuid, user, comments);
		}
		catch (HsCloudException e) {
			throw new HsCloudException(Constants.VM_TERMINATE_ERROR, "terminateVmWhenRefund异常", logger, e);
		}
	}

	public String getVMOSUser(String osId){
		return operation.getVMOSUser(Integer.valueOf(osId));
	}

	@Transactional(readOnly = false)
	public void resetVMOS(String vmId,String osId,String user,String pwd,User u,String otype){
		logger.info("OPS-Facade-resetVMOS start");
		try {
			operation.resetVMOS(vmId, Integer.valueOf(osId),user,pwd,u,otype);
		} catch (HsCloudException e) {
			throw new HsCloudException(Constants.VM_RESETOS_ERROR, "resetVMOS异常",
					logger, e);
		}
	}

	public boolean hasSameVmName(String vmName){
		try{
			return operation.hasSameVmName(vmName);
		} catch (Exception e) {
			throw new HsCloudException("OPS-Facade016", "hasSameVmName异常", logger, e);
		}
	}
	@Override
	public boolean updateVmName(String vmName, String vmId, long uid) {
		logger.info("OPS-Facade-updateVmName start");
		try {
			return operation.updateVmName(vmName, vmId,uid);
		} catch (HsCloudException e) {
			throw new HsCloudException("OPS-Facade017", "updateVmName异常",
					logger, e);
		}
	}

	@Override
	public InstanceDetailVo findDetailVmById(String vmId) {
		logger.info("OPS-Facade-findDetailVmById start");
		InstanceDetailVo idVO = null;
		try {
			idVO = operation.findVm(vmId);
			if(idVO != null){
				if(idVO.getVmType() ==0 && idVO.getVmBusineStatus()!=null && idVO.getVmBusineStatus()==0){
					List<VmExtDiskBean> lvd = new ArrayList<VmExtDiskBean>();
					List<ExtDisk> led = serviceCatalogService.getExtDiskListByScId(idVO.getScId());
					for(ExtDisk ed:led){
						VmExtDiskBean vedb = new VmExtDiskBean();
						vedb.setEd_name(ed.getName());
						vedb.setEd_capacity(ed.getCapacity());
						lvd.add(vedb);
					}
					idVO.setExtdisks(lvd);
				}
			}
		} catch (HsCloudException e) {
			throw new HsCloudException(Constants.VM_DETAIL_ERROR, "findDetailVmById异常",
					logger, e);
		}
		return idVO;
	}

	@Transactional(readOnly = false)
	public void cancelApplyTryVm(long uid,long referenceId) throws HsCloudException{
		logger.info("OPS-Facade-cancelApplyTryVm start");
		try {
			operation.cancelApplyTryVm(uid, referenceId);
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_CANCEL_ERROR, "cancelApplyTryVm异常",
					logger, e);
		}
	}

	@Transactional(readOnly = false)
	public void applyForDelayTryVm(long uid,long referenceId) throws HsCloudException{
		logger.info("OPS-Facade-cancelApplyTryVm start");
		try {
			operation.applyForDelayTryVm(uid, referenceId);
		} catch (Exception e) {
			throw new HsCloudException(Constants.VM_APPLYDELAY_ERROR, "applyForDelayTryVm异常",
					logger, e);
		}
	}

	@Transactional(readOnly = false)
	public String createVPDC(CreateVpdcBean vpdcBean,User user) throws HsCloudException{
		if(Constants.VPDC_NOROUTE==vpdcBean.getVpdcType()){
			operation.createNoRouterVPDC(vpdcBean, user.getId());
		}
		return null;
	}

	@Transactional(readOnly = false)
	public void updateVPDC(Long vpdcId,String name,List<VlanNetwork> vlans,Long userId) throws HsCloudException{
		operation.updateVPDC(vpdcId, name, vlans, userId);
	}

	/******************************* Monitor Module ********************************/
	@Override
	public List<CPUHistoryVO> getVmCPUHistory(String vmId, long fromTime,
			long toTime) {
		logger.info("Monitor-Facade-getVmCPUHistory start");
		List<CPUHistoryVO> cPUHistoryVOList = null;
		try {
			cPUHistoryVOList = monitorService.getVmCPUHistory(vmId, fromTime,
					toTime);
		} catch (HsCloudException e) {
			throw new HsCloudException("Monitor-Facade07", "getVmCPUHistory异常",
					logger, e);
		}
		return cPUHistoryVOList;
	}

	@Override
	public List<MemoryHistoryVO> getVmMemoryHistory(String vmId, long fromTime,
			long toTime) {
		logger.info("Monitor-Facade-getVmMemoryHistory start");
		List<MemoryHistoryVO> memoryHistoryVOList = null;
		try {
			memoryHistoryVOList = monitorService.getVmMemoryHistory(vmId,
					fromTime, toTime);
		} catch (HsCloudException e) {
			throw new HsCloudException("Monitor-Facade08",
					"getVmMemoryHistory异常", logger, e);
		}
		return memoryHistoryVOList;
	}

	@Override
	public List<DiskHistoryVO> getVmDiskHistory(String vmId, long fromTime,
			long toTime) {
		logger.info("Monitor-Facade-getVmDiskHistory start");
		List<DiskHistoryVO> diskHistoryVOList = null;
		try {
			diskHistoryVOList = monitorService.getVmDiskHistory(vmId, fromTime,
					toTime);
		} catch (HsCloudException e) {
			throw new HsCloudException("Monitor-Facade09",
					"getVmDiskHistory异常", logger, e);
		}
		return diskHistoryVOList;
	}

	@Override
	public List<NetHistoryVO> getVmNetHistory(String vmId, long fromTime,
			long toTime) {
		logger.info("Monitor-Facade-getVmNetHistory start");
		List<NetHistoryVO> netHistoryVOList = null;
		try {
			netHistoryVOList = monitorService.getVmNetHistory(vmId, fromTime,
					toTime);
		} catch (HsCloudException e) {
			throw new HsCloudException("Monitor-Facade09", "getVmNetHistory异常",
					logger, e);
		}
		return netHistoryVOList;
	}

	@Override
	public List<VmRealtimeMonitorVO> getVmRealTimeMonitor(String vmId) {
		logger.info("Monitor-Facade-getVmRealTimeMonitor start");
		List<VmRealtimeMonitorVO> vmRealtimeMonitorVOList = null;
		try {
			vmRealtimeMonitorVOList = monitorService.getVmRealTimeMonitor(vmId);
		} catch (HsCloudException e) {
			throw new HsCloudException("Monitor-Facade09",
					"getVmRealTimeMonitor异常", logger, e);
		}
		return vmRealtimeMonitorVOList;
	}

	/******************************* UserManager Module ********************************/

	@Transactional(readOnly = true)
	public List<Country> loadCountry() {


		if(logger.isDebugEnabled()){
			logger.debug("enter loadCountry method.");
		}

		List<Country> countrys = optService.loadCountry();

		if(logger.isDebugEnabled()){
			logger.debug("country:"+countrys);
			logger.debug("exit loadCountry method.");
		}

		return countrys;

	}

	@Transactional(readOnly = true)
	public List<Industry> loadIndustry() {

		if(logger.isDebugEnabled()){
			logger.debug("enter loadIndustry method.");
		}

		List<Industry> industrys = optService.loadIndustry();
		if(logger.isDebugEnabled()){
			logger.debug("country:"+industrys);
			logger.debug("exit loadIndustry method.");
		}
		return industrys;

	}

	@Override
	public Domain loadDomain(String code) {

		if(logger.isDebugEnabled()){
			logger.debug("enter loadDomain method.");
			logger.debug("code:"+code);
		}
		Domain domain = domainService.getDomainByCode(code);
		if(logger.isDebugEnabled()){
			logger.debug("domain:"+domain);
			logger.debug("exit loadDomain method.");
		}
		return domain;

	}

	/**
	 * 通过分平台id去后台数据库hc_domain里面拿到copyright_cn和copyright_en
	 */
	public Domain getDomainById(long domainId)throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter getDomainById method.");
			logger.debug("domainId:"+domainId);
		}
		Domain domain = domainService.getDomainById(domainId);
		if(logger.isDebugEnabled()){
			logger.debug("exit getDomainById method.");
		}
		return domain;
	}	

	@Transactional
	public void userRegister(User user, String companyName, String groupIds,String externalUser,String localUser,String domainCode) {

		if(logger.isDebugEnabled()){
			logger.debug("enter userRegister method.");
			logger.debug("user:"+user);
			logger.debug("companyName:"+companyName);
			logger.debug("groupIds:"+groupIds);
		}

		try {
			companyName = new String(companyName.getBytes("iso8859_1"),
					"UTF-8");
			if(null != user.getUser_source() && !"".equals(user.getUser_source())){
				MarketingPromotion marketingPromotion = marketingPromotionService.findMarketingPromotionByCode(user.getUser_source());
				if(null == marketingPromotion){
					throw new HsCloudException(Constants.NO_USER_BRAND,"brand is not find.",logger);
				}
				user.setLevel(marketingPromotion.getBrand_code());
			}
			if (UserType.ENTERPRISE_USER.getType().equals(user.getUserType())) {
				userService.createUser(user,UserType.ENTERPRISE_USER.getType());
				accountService.createAccount(user);
				InvoiceAccount invoiceAccount = new InvoiceAccount();
				invoiceAccount.setUserId(user.getId());
				invoiceAccountService.createInvoiceAccount(invoiceAccount);
				// 企业用户注册需创建公司
				Company company = new Company();
				if(null != user.getUserProfile()){
					user.getUserProfile().setCompany(companyName);
				}			
				company.setName(companyName);
				company.setCreateId(user.getId());
				companyService.createCompany(company, user.getId());
				companyService.saveUserCompany(company.getId(), user.getId());
			} else if (UserType.PERSONAL_USER.getType().equals(user.getUserType())) {
				userService.createUser(user,UserType.PERSONAL_USER.getType());
				if(null != user.getUserProfile()){
					user.getUserProfile().setCompany(companyName);
				}
				accountService.createAccount(user);
				InvoiceAccount invoiceAccount = new InvoiceAccount();
				invoiceAccount.setUserId(user.getId());
				invoiceAccountService.createInvoiceAccount(invoiceAccount);
			} 
			//			else if (UserType.SUB_USER.getType().equals(user.getUserType())) {
			//				user.setEnable(UserState.ACTIVE.getIndex());
			//			} else {
			//				String[] gids = groupIds.split(",");
			//				List<Long> ids = new ArrayList<Long>();
			//
			//				userService.createUser(user,UserType.SUB_USER.getType());
			//				accountService.createAccount(user);
			//				Company company = companyService.getCompanyByUserId(user
			//						.getCreateId());
			//				companyService.saveUserCompany(company.getId(), user.getId());
			//				if (StringUtils.isBlank(groupIds)) {
			//
			//					for (String gid : gids) {
			//						ids.add(Long.valueOf(gid));
			//
			//					}
			////				userUserGroupService.saveUserUserGroup(user.getId(), ids);
			//			}
			//
			//		}
			if (UserType.ENTERPRISE_USER.getType().equals(user.getUserType())
					|| UserType.PERSONAL_USER.getType().equals(user.getUserType())) {

				Map<String, String> map = new HashMap<String, String>();
				map.put("username", user.getName());
				map.put("email", user.getEmail());
				map.put("id", String.valueOf(user.getId()));
				String activeKey = MD5Util.getMD5Str(user.getName()
						+ String.valueOf(user.getUpdateDate().getTime()).substring(
								0, 8));
				map.put("activeKey", activeKey);
				//			String body = mailService.getMailTemplate("register", 1, map);

				//lihonglei 20130322 saveEmail
				saveEmail(user, map, MailTemplateType.ACTIVE_USER_TEMPLATE.getType());
			}
				
				PlatformRelation platformRelation =platformRelationService.getPlatformRelationByExternalUser(externalUser);
				PlatformRelation platformRelation2 =platformRelationService.getPlatformRelationByLocalUser(Long.toString(user.getId()));
				if(platformRelation != null && platformRelation2==null){
					platformRelation.setUserId(Long.toString(user.getId()));				
				}else if(platformRelation == null && platformRelation2!=null){
					platformRelation2.setExternalUserId(externalUser);
					platformRelation=platformRelation2;
					
				}else{
					platformRelation = new PlatformRelation();				
					platformRelation.setUserId(Long.toString(user.getId()));
					platformRelation.setExternalUserId(externalUser);
				}					
				 platformRelationService.synchroPlatformRelation(platformRelation);
		} catch (Exception e) {
			throw new HsCloudException("",logger,e);
		}

		if(logger.isDebugEnabled()){
			logger.debug("exit userRegister method.");
		}

	}

	@Override
	@Transactional
	public void updateUser(User user, String companyName, String groupIds) {

		if(logger.isDebugEnabled()){
			logger.debug("enter updateUser method.");
			logger.debug("user:"+user);
			logger.debug("companyName:"+companyName);
			logger.debug("groupIds:"+groupIds);
		}

		if (UserType.ENTERPRISE_USER.getType().equals(user.getUserType())) {

		} else if (UserType.PERSONAL_USER.getType().equals(user.getUserType())) {

		} 
		//		else if (UserType.SUB_USER.getType().equals(user.getUserType())) {
		//
		//			User u = userService.getUser(user.getId());
		//			u.setPassword(user.getPassword());
		//			userService.modifyUser(u);
		//			userUserGroupService.deleteUserUserGroupByUserId(user.getId());
		//			if (StringUtils.isBlank(groupIds)) {
		//
		//			} else {
		//				String[] gids = groupIds.split(",");
		//				List<Long> ids = new ArrayList<Long>();
		//
		//				for (String gid : gids) {
		//					ids.add(Long.valueOf(gid));
		//
		//				}
		//				userUserGroupService.saveUserUserGroup(user.getId(), ids);
		//			}
		//
		//		}

		if(logger.isDebugEnabled()){
			logger.debug("exit updateUser method.");
		}

	}


	@Override
	public void changeGroupForUer(long currentUserId,String userId, String groupIds) {

		if(logger.isDebugEnabled()){
			logger.debug("enter changeGroupForUer method.");
			logger.debug("groupIds:"+groupIds);
			logger.debug("userId:"+userId);
		}

		if (StringUtils.isBlank(groupIds) || StringUtils.isBlank(userId)) {

		} else {
			Company company = companyService.getCompanyByUserId(currentUserId);
			long id = Long.valueOf(userId);
			String[] gids = groupIds.split(",");
			List<Long> ids = new ArrayList<Long>();
			for (String gid : gids) {
				ids.add(Long.valueOf(gid));
			}
			userUserGroupService.saveUserUserGroup(id,company.getId(), ids);
		}

		if(logger.isDebugEnabled()){
			logger.debug("exit changeGroupForUer method.");
		}
	}

	@Transactional
	public void activeUser(User user) {

		if(logger.isDebugEnabled()){
			logger.debug("enter updateUser method.");
			logger.debug("user:"+user);
		}
		//		user.setEnable(UserState.ACTIVE.getIndex());
		//userService.modifyUser(user);

		if(logger.isDebugEnabled()){
			logger.debug("exit updateUser method.");
		}

	}

	@Transactional
	public void resetUserPassword(User user) {

		if(logger.isDebugEnabled()){
			logger.debug("enter resetUserPassword method.");
			logger.debug("user:"+user);
		}
		userService.modifyUser(user);
		if(logger.isDebugEnabled()){
			logger.debug("exit resetUserPassword method.");
		}

	}

	@Transactional
	public User loginUserByEmail(String email, String password) {

		if(logger.isDebugEnabled()){
			logger.debug("enter loginUserByEmail method.");
			logger.debug("email:"+email);
			logger.debug("password:"+password);
		}
		User user = userService.loginUserByEmail(email, password);
		if(logger.isDebugEnabled()){

			logger.debug("user:"+user);
			logger.debug("eixt loginUserByEmail method.");
		}
		return user;

	}

	@SuppressWarnings("unused")
	@Transactional
	public User loginUserByEmail(String email, String password, long domainId) {

		if(logger.isDebugEnabled()){
			logger.debug("enter loginUserByEmail method.");
			logger.debug("email:"+email);
			logger.debug("password:"+password);
		}
		User user = userService.loginUserByEmail(email, password);
		if(user.getDomain().getId()!=domainId){
			Domain d = domainService.getDomainById(domainId);
			String webadd = d.getUrl();
			throw new HsCloudException(Constants.USER_ERR_URL,"login domian error",logger);
		}
		if(logger.isDebugEnabled()){

			logger.debug("user:"+user);
			logger.debug("eixt loginUserByEmail method.");
		}
		return user;

	}

	@Transactional(readOnly = true)
	public User getUserById(long userId) {

		if(logger.isDebugEnabled()){
			logger.debug("enter getUserById method.");
			logger.debug("userId:"+userId);
		}
		User user = userService.getUser(userId);
		if(logger.isDebugEnabled()){

			logger.debug("user:"+user);
			logger.debug("eixt getUserById method.");
		}
		return user;

	}

	@Override
	@Transactional(readOnly = true)
	public UserVO getUserVOById(String userId) throws Exception {


		if(logger.isDebugEnabled()){
			logger.debug("enter getUserVOById method.");
			logger.debug("userId:"+userId);
		}
		if(!StringUtils.isBlank(userId)&&StringUtils.isNumeric(userId)){

		}
		if (StringUtils.isBlank(userId)) {
			throw new Exception();
		} else {
			long id = Long.valueOf(userId);
			UserVO userVO = userService.getUserVO(id);
			if(logger.isDebugEnabled()){
				logger.debug("id:"+id);
				logger.debug("userVO:"+userVO);
				logger.debug("exit getUserVOById method.");
			}
			return userVO;
		}


	}

	@Override
	@Transactional
	public void deleteUserById(String userId) throws Exception {

		if(logger.isDebugEnabled()){
			logger.debug("enter deleteUserById method.");
			logger.debug("userId:"+userId);
		}
		if (!StringUtils.isNumeric(userId)) {
			throw new Exception();
		} else {
			long id = Long.valueOf(userId);
			userService.deleteUser(id);
			if(logger.isDebugEnabled()){
				logger.debug("id:"+id);
				logger.debug("exit deleteUserById method.");
			}
		}

	}

	@Override
	@Transactional
	public void deleteUserGroupById(String groupId) throws Exception {

		if(logger.isDebugEnabled()){
			logger.debug("enter deleteUserById method.");
			logger.debug("groupId:"+groupId);
		}
		if (!StringUtils.isNumeric(groupId)) {
			throw new Exception();
		} else {
			long id = Long.valueOf(groupId);
			userGroupService.deleteUserGroupById(id);
			if(logger.isDebugEnabled()){
				logger.debug("groupId:"+groupId);
				logger.debug("exit deleteUserGroupById method.");
			}
		}


	}

	@Transactional(readOnly = true)
	public User getUserByEmail(String email) {

		if(logger.isDebugEnabled()){
			logger.debug("enter getUserByEmail method.");
			logger.debug("email:"+email);
		}
		User user = userService.getUserByEmail(email);
		if(logger.isDebugEnabled()){
			logger.debug("user:"+user);
			logger.debug("exit getUserByEmail method.");
		}
		return user;

	}

	@Override
	@Transactional
	public void findPassword(User u) throws Exception {

		if(logger.isDebugEnabled()){
			logger.debug("enter findPassword method.");
			logger.debug("user:"+u);
		}

		User user = userService.getUserByEmail(u.getEmail());

		if (null != user) {

			if(!user.getDomain().getCode().equals(u.getDomain().getCode())){
				throw new HsCloudException(BillConstant.ACCOUNT_NOT_FOUND,"user is not freezed or deleted.",logger);
			}

			if(UserState.DELETED.getIndex()==user.getEnable() || UserState.FREEZE.getIndex()==user.getEnable() ){
				throw new HsCloudException(Constants.USER_NO_ACTIVE,"user is not freezed or deleted.",logger);
			}

			String password = PasswordUtil.getRandomNum(Constants.PASSWORD_LEN);
			Map<String, String> map = new HashMap<String, String>();
			map.put("password", password);
			try {
				String enPassword = PasswordUtil.getEncyptedPasswd(password);
				user.setPassword(enPassword);
			} catch (Exception e) {
				throw new HsCloudException(Constants.PASSWORD_ENCRYPTION_FAILED," Encryption failed.",logger);
			}
			//user.setPassword(password);

			userService.modifyUser(user);


			//lihonglei 20130322 saveEmail
			saveEmail(user, map, MailTemplateType.FIND_POSSWORD_TEMPLATE.getType());

			if(logger.isDebugEnabled()){
				logger.debug("password:"+password);
				logger.debug("user:"+user);
				logger.debug("exit findPassword method.");
			}

		} else {
			if(logger.isDebugEnabled()){
				logger.debug("user:"+user);
			}
			throw new MailAddressException();
		}

	}

	//	@Override
	//	@Transactional(readOnly = true)
	//	public Page<UserVO> fuzzySearchPermissionUser(String type, String query,
	//			List<Sort> sorts, Page<User> page, String userType, long userId,List<Object> primKeys) {
	//
	//		Page<UserVO> user = null;
	//		if(logger.isDebugEnabled()){
	//			logger.debug("enter fuzzySearchPermissionUser method.");
	//			logger.debug("type:"+type);
	//			logger.debug("query:"+query);
	//			logger.debug("sorts:"+sorts);
	//			logger.debug("userType:"+userType);
	//			logger.debug("primKeys:"+primKeys);
	//		}
	//		
	//		if ("subUserName".equals(type)) {
	//			user = userService.searchPermissionUserLikeName(query, sorts, page,
	//					userType, userId,primKeys);
	//		} else if ("subUserEmail".equals(type)) {
	//			user = userService.searchPermissionUserLikeEmail(query, sorts,
	//					page, userType, userId,primKeys);
	//		} else {
	//			user = userService.searchPermissionUser(sorts, page, userType,
	//					userId,primKeys);
	//		}
	//		
	//		if(logger.isDebugEnabled()){
	//			logger.debug("user:"+user);
	//			logger.debug("exit fuzzySearchPermissionUser method.");
	//		}
	//		return user;
	//
	//	}

	@Override
	@Transactional(readOnly = true)
	public Page<UserVO> fuzzySearchPermissionUser(String query,
			List<Sort> sorts, Page<User> page, String userType, long userId,
			List<Object> primKeys) {
		if(logger.isDebugEnabled()){
			logger.debug("enter fuzzySearchPermissionUser method.");
			logger.debug("query:"+query);
			logger.debug("sorts:"+sorts);
			logger.debug("userType:"+userType);
			logger.debug("primKeys:"+primKeys);
		}
		try {
			query = new String(query.getBytes("iso8859_1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		Page<UserVO> user = userService.searchPermissionUser(query,sorts, page, userType, userId, primKeys);

		if(logger.isDebugEnabled()){
			logger.debug("user:"+user);
			logger.debug("exit fuzzySearchPermissionUser method.");
		}
		return user;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<UserGroupVO> fuzzySearchPermissionUserGroup(
			String query, List<Sort> sorts, Page<UserGroup> page, long userId,List<Object> primKeys) {

		if(logger.isDebugEnabled()){
			logger.debug("enter fuzzySearchPermissionUserGroup method.");
			logger.debug("query:"+query);
			logger.debug("sorts:"+sorts);
			logger.debug("primKeys:"+primKeys);
		}
		Page<UserGroupVO> userGroup = null;
		try {
			query = null == query ? null :new String(query.getBytes("iso8859_1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//		if ("groupName".equals(type)) {
		//			userGroup = userGroupService.searchPermissionGroupLikeName(query,
		//					sorts, page, userId,primKeys);
		//		} else {
		//			userGroup = userGroupService.searchPermissionGroup(sorts, page,
		//					userId,primKeys);
		//		}

		userGroup = userGroupService.searchPermissionGroup(sorts, query, page, userId, primKeys);
		if(logger.isDebugEnabled()){
			logger.debug("userGroup:"+userGroup);
			logger.debug("exit fuzzySearchPermissionUserGroup method.");
		}
		return userGroup;

	}

	@Override
	@Transactional(readOnly = true)
	public Page<UserVO> userInGroup(Page<User> page, List<Sort> sorts,
			String userType, String groupId, long userId) throws Exception{


		if(logger.isDebugEnabled()){
			logger.debug("enter fuzzySearchPermissionUserGroup method.");
			logger.debug("userType:"+userType);
			logger.debug("groupId:"+groupId);
			logger.debug("sorts:"+sorts);
		}
		if(StringUtils.isNumeric(groupId)){
			long gid = Long.valueOf(groupId);

			Page<UserVO> user = null;
			user = userService.pageUserInGroup(page, sorts, userType, gid, userId);

			if(logger.isDebugEnabled()){

				logger.debug("user:"+user);
				logger.debug("enter fuzzySearchPermissionUserGroup method.");
			}
			return user;
		}else{
			return null;
		}

	}

	@Override
	@Transactional(readOnly = true)
	public List<UserGroupVO> getPermissionUserGroupVO(long userId,List<Object> primKeys) {

		if(logger.isDebugEnabled()){
			logger.debug("enter getPermissionUserGroupVO method.");
			logger.debug("userId:"+userId);
		}
		List<UserGroupVO> userGroupVOs = null;
		if (null==primKeys) {
			Company company = companyService.getCompanyByUserId(userId);
			//同一公司系flag=1的 组
			userGroupVOs = userGroupService.getUserGroupByCompanyId(company.getId());
			if(logger.isDebugEnabled()){
				logger.debug("userGroupVOs:"+userGroupVOs);
				logger.debug("enter getPermissionUserGroupVO method.");

			}
			return userGroupVOs;
		} else {
			List<Long> ids = new ArrayList<Long>();
			for (Object primKey : primKeys) {
				ids.add((Long)primKey);
			}
			userGroupVOs = userGroupService.getPermissionUserGroup(userId,ids);
			if(logger.isDebugEnabled()){
				logger.debug("userGroupVOs:"+userGroupVOs);
				logger.debug("enter getPermissionUserGroupVO method.");

			}
			return userGroupVOs;
		}

	}

	@Override
	@Transactional
	public void modifyUserOnlineStatus(long userId, short onlineStatus) {
		userService.modifyUserOnlineStatus(userId, onlineStatus);
	}

	public long createUserGroup(String groupName,long id){
		UserGroup ug = new UserGroup();
		//		try {
		//			groupName = new String(groupName.getBytes("iso8859_1"),
		//					"UTF-8");
		//		} catch (UnsupportedEncodingException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		ug.setName(groupName);
		long grId = userGroupService.createUserGroup(ug, id);
		return grId;
	}

	//resourceType:primKey:actionId:resourceId:id:permissionId,resourceType:primKey:resourceName:resourceId:id:permissionId
	@Transactional
	public void saveOrUpDatePermission(PerGroupVO gpv, long userId) {


		userGroupPermissionService.deleteUserGroupPermission(gpv.getGroupId());


		List<Long> perIds = new ArrayList<Long>();
		List<TreeVO> tvo = gpv.getP();
		Map<String,Long> rmap = new HashMap<String,Long>();
		Map<String,Long> amap = new HashMap<String,Long>();
		//		for (TreeVO treeVO : tvo) {
		//			if(null!=treeVO.getResourceId()){
		//				rmap.put(treeVO.getResourceType()+"_"+treeVO.getId(), treeVO.getResourceId());
		//			}
		//			if(null!=treeVO.getPermissionId()){
		//				amap.put(treeVO.getActionId()+"_"+treeVO.getResourceId(), treeVO.getPermissionId());
		//			}
		//		}




		//		for(TreeVO r: tvo){
		//			
		//			long resId ;
		//			if(!rmap.containsKey(r.getResourceType()+"_"+r.getId())){
		//				resId = resourceService.addResource(r.getId(), r.getResourceType());
		//				rmap.put(r.getResourceType()+"_"+r.getId(),resId);
		//			}else{
		//				resId = rmap.get(r.getResourceType()+"_"+r.getId());
		//				r.setResourceId(resId);
		//			}
		//			if(null!=r.getPermissionId() && amap.containsKey(r.getActionId()+"_"+r.getResourceId())){
		//				
		//		    	perIds.add(amap.get(r.getActionId()+"_"+r.getResourceId()));
		//		    	
		//			}else{
		//				
		//				long perId = permissionService.addPermission(resId,(long)r.getActionId());
		//				perIds.add(perId);
		//				
		//			}
		//		
		//		}
		//		userGroupPermissionService.saveUserGroupPermission(gpv.getGroupId(), perIds);



		for(TreeVO r: tvo){

			long resId ;
			if(!rmap.containsKey(r.getResourceType()+"_"+r.getId())){
				Resource resource = resourceService.getResource(r.getResourceType(),r.getId());
				if(null == resource){
					resId = resourceService.addResource(r.getId(), r.getResourceType());
				}else{
					resId = resource.getId();
				}

				rmap.put(r.getResourceType()+"_"+r.getId(),resId);
				r.setResourceId(resId);
			}else{
				resId = rmap.get(r.getResourceType()+"_"+r.getId());
				r.setResourceId(resId);
			}
			long perId;
			if(!amap.containsKey(r.getActionId()+"_"+r.getResourceId())){
				Permission permission = permissionService.getPermission(r.getActionId(), r.getResourceId());
				if(null == permission){
					perId = permissionService.addPermission(resId,(long)r.getActionId());
				}else{
					perId = permission.getId();
				}
				amap.put(r.getActionId()+"_"+r.getResourceId(), perId);
				perIds.add(perId);

			}else{
				perIds.add(amap.get(r.getActionId()+"_"+r.getResourceId()));

			}

		}
		userGroupPermissionService.saveUserGroupPermission(gpv.getGroupId(), perIds);

	}

	private Map<String, ResourceType> getResourceTypeMap() {
		if(resourceTypeMap == null || resourceTypeMap.isEmpty()) {
			resourceTypeMap = resourceTypeService.getResourceTypeMap(Constant.STATUS_USER);
		}
		return resourceTypeMap;
	}

	@Override
	@Transactional
	public List<Node> getPermission(long userId, String groupId) {


		Map<String, ResourceType> map = getResourceTypeMap();
		Set<String> rKeys = map.keySet();
		List<TreeVO> treeVOs = new LinkedList<TreeVO>();

		List<Permission> permission = new ArrayList<Permission>();
		if(null != groupId && !"".equals(groupId)){
			permission = permissionService.getPermissionInGroup(Long.valueOf(groupId));
		}

		List<Long> pIds = new ArrayList<Long>();
		for (Permission p : permission) {
			pIds.add(p.getId());
		}

		boolean roleFlag = roleService.hasRole(userId);
		Company company = companyService.getCompanyByUserId(userId);
		for(String rKey:rKeys){
			ResourceType reType = (ResourceType)map.get(rKey);
			List<Long> companyRes = new ArrayList<Long>();
			List<Long> finIds = new ArrayList<Long>();
			List<Long> primKeys = new ArrayList<Long>();
			List<Long> pids = new ArrayList<Long>();
			List<PermissionVO> pvos = new ArrayList<PermissionVO>();
			//			 String [] param = map.get(rKey).split(",");
			Map<String,Object> qMap = new HashMap<String, Object>();
			qMap.put("cid", company.getId());
			//if(null !=param[3] && !"''".equals(param[3])){
			if(null !=reType.getRelationConditon() && !"''".equals(reType.getRelationConditon())){
				//同一公司下的资源
				//List<Map<String,Object>> companyMap = tabResultService.getTabResult(qMap, param[3]);
				List<Map<String,Object>> companyMap = tabResultService.getTabResult(qMap, reType.getRelationConditon());
				for (Map<String, Object> cm : companyMap) {
					companyRes.add((Long)cm.get("id"));
				}
			}
			qMap.clear();
			StringBuffer sb = new StringBuffer();
			sb.append("select id from ");
			//sb.append(param[0]);
			sb.append(reType.getResourceTable());
			if(!companyRes.isEmpty()){
				sb.append(" where id in(:ids)");
				qMap.put("ids", companyRes);
			}
			List<Map<String,Object>> finalResMap = tabResultService.getTabResult(qMap, sb.toString());
			for (Map<String, Object> fm : finalResMap) {
				finIds.add((Long)fm.get("id"));
			}
			finIds.add(0l);
			if(!roleFlag){
				//权限资源
				pvos = permissionService.getUPermissionVO(userId, rKey);
				for(PermissionVO pvo:pvos){
					primKeys.add(Long.valueOf(pvo.getPrimKey()));
					pids.add(pvo.getPermissionId());
				}
				finIds.retainAll(primKeys);
			}
			//treeVOs.addAll(resourceService.getTreeVO(rKey,param[1],param[0], finIds,pids));
			treeVOs.addAll(resourceService.getTreeVO(rKey,reType.getResourceCondition(),reType.getResourceTable(), finIds,pids));
		}


		Map<String,List<TreeVO>> typemap = new HashMap<String, List<TreeVO>>();
		Set<String> reKeys = new HashSet<String>();
		for (TreeVO t : treeVOs) {
			if(pIds.contains(t.getPermissionId())){
				t.setChecked(true);
			}
			if(0l==t.getLevel()){
				reKeys.add(t.getResourceType());
			}
			if(typemap.containsKey(t.getResourceType())){
				typemap.get(t.getResourceType()).add(t);
			}else{
				List<TreeVO> rn = new ArrayList<TreeVO>();
				rn.add(t);
				typemap.put(t.getResourceType(), rn);

			}
		}


		Set<String> set =  typemap.keySet();
		Map<String,Node> l0Map = new HashMap<String,Node>();

		Map<String,Node> rnmap = new HashMap<String,Node>();
		for (String key : set) {
			List<TreeVO> rs = typemap.get(key);
			for (TreeVO t : rs) {

				//					 if(roleFlag || t.isChecked()){
				if(0==t.getLevel()){
					Node rn = new Node();
					if(!l0Map.containsKey(t.getResourceType())){

						rn.setId(t.getId());
						rn.setChecked(t.isChecked());
						rn.setPrimKey(t.getPrimKey());
						rn.setResourceId(t.getResourceId());
						rn.setResourceName(t.getResourceName());
						rn.setResourceType(t.getResourceType());
						com.hisoft.hscloud.crm.usermanager.vo.Action a = new com.hisoft.hscloud.crm.usermanager.vo.Action();
						a.setAcitonName(t.getAcitonName());
						a.setActionId(t.getActionId());
						a.setPermissionId(t.getPermissionId());
						a.setLevel(t.getLevel());
						a.setChecked(null==t.getPermissionId()?false:(pIds.contains(t.getPermissionId())));
						rn.getActions().add(a);
						l0Map.put(t.getResourceType(), rn);
					}else{
						rn = l0Map.get(t.getResourceType());
						com.hisoft.hscloud.crm.usermanager.vo.Action a = new com.hisoft.hscloud.crm.usermanager.vo.Action();
						a.setAcitonName(t.getAcitonName());
						a.setActionId(t.getActionId());
						a.setPermissionId(t.getPermissionId());
						a.setLevel(t.getLevel());
						a.setChecked(null==t.getPermissionId()?false:(pIds.contains(t.getPermissionId())));
						rn.getActions().add(a);
						l0Map.put(t.getResourceType(), rn);
					}
				}else{

					Node rn = new Node();
					if(!rnmap.containsKey(t.getResourceName()+t.getId())){
						rn.setId(t.getId());
						rn.setChecked(t.isChecked());
						rn.setPrimKey(t.getPrimKey());
						rn.setResourceId(t.getResourceId());
						rn.setResourceName(t.getResourceName());
						rn.setResourceType(t.getResourceType());
						com.hisoft.hscloud.crm.usermanager.vo.Action a = new com.hisoft.hscloud.crm.usermanager.vo.Action();
						a.setAcitonName(t.getAcitonName());
						a.setActionId(t.getActionId());
						a.setPermissionId(t.getPermissionId());
						a.setLevel(t.getLevel());
						a.setChecked(null==t.getPermissionId()?false:(pIds.contains(t.getPermissionId())));
						rn.getActions().add(a);
						rnmap.put(t.getResourceName()+t.getId(), rn);
					}else{
						rn = rnmap.get(t.getResourceName()+t.getId());
						com.hisoft.hscloud.crm.usermanager.vo.Action a = new com.hisoft.hscloud.crm.usermanager.vo.Action();
						a.setAcitonName(t.getAcitonName());
						a.setActionId(t.getActionId());
						a.setLevel(t.getLevel());
						a.setPermissionId(t.getPermissionId());
						a.setChecked(null==t.getPermissionId()?false:(pIds.contains(t.getPermissionId())));
						rnmap.get(t.getResourceName()+t.getId()).getActions().add(a);

					}

				}
				//					 }


			}
		}

		Set<String> keys = rnmap.keySet();

		Map<String,Node> finalmap = new HashMap<String,Node>();

		for(String reKey:reKeys){

			Node root = new Node();
			root.setId(l0Map.get(reKey).getId());
			root.setResourceType(l0Map.get(reKey).getResourceType());
			root.setResourceName(((ResourceType)map.get(reKey)).getName());
			root.setResourceId(l0Map.get(reKey).getResourceId());
			root.setChecked(l0Map.get(reKey).isChecked());
			if(l0Map.containsKey(reKey)){
				root.getActions().addAll(l0Map.get(reKey).getActions());
			}
			finalmap.put(reKey, root);

		}

		for (String key : keys) {
			if(finalmap.containsKey(rnmap.get(key).getResourceType())){
				finalmap.get(rnmap.get(key).getResourceType()).getChildren().add(rnmap.get(key));
			}else{

				Node root = new Node();
				root.setId(rnmap.get(key).getId());
				root.setResourceType(rnmap.get(key).getResourceType());
				root.setResourceName(((ResourceType)map.get(rnmap.get(key).getResourceType())).getName());			 
				root.setResourceId(rnmap.get(key).getResourceId());
				root.setChecked(rnmap.get(key).isChecked());
				root.getChildren().add(rnmap.get(key));
				if(l0Map.containsKey(rnmap.get(key).getResourceType())){
					root.getActions().addAll(l0Map.get(rnmap.get(key).getResourceType()).getActions());
					l0Map.remove(rnmap.get(key).getResourceType());
				}
				finalmap.put(rnmap.get(key).getResourceType(), root);
			}

		}

		Set<String> ks = finalmap.keySet();
		List<Node> rns = new LinkedList<Node>();
		for (String key : ks) {
			rns.add(finalmap.get(key));
		}
		return rns;

	}

	@Override
	@Transactional
	public UserGroup getUserGroupById(String groupId) {
		if(StringUtils.isBlank(groupId)){

		}else{

		}

		return userGroupService.getUserGroupById(Long.valueOf(groupId));
	}

	@Override
	@Transactional
	public boolean duplicateUserGroup(long userId, String groupName) {
		Company company = companyService.getCompanyByUserId(userId);
		List<UserGroup> userGroups = userGroupService.duplicateUserGroup(company.getId(),groupName);
		if(userGroups.isEmpty()){
			return false;
		}else{
			return true;
		}

	}
	/** 
	 * @param user 
	 * @throws Exception 
	 */
	@Override
	@Transactional
	public void modifyUser2(User user) {
		User obj=userService.getUser(user.getId());
		//User obj = userService.getUserByEmail(user.getEmail());
		obj.setEmail(user.getEmail());
		obj.setUserType(user.getUserType());
		obj.setPassword(user.getPassword());
		obj.setName(user.getName());
		obj.getUserProfile().setIdCard(user.getUserProfile().getIdCard());
		obj.getUserProfile().setTelephone(user.getUserProfile().getTelephone());
		obj.getUserProfile().setCompany(user.getUserProfile().getCompany());
		obj.getUserProfile().setAddress(user.getUserProfile().getAddress());
		Country country = new Country();
		country.setId(user.getUserProfile().getCountry().getId());
		obj.getUserProfile().setCountry(country);
		Region region = new Region();
		if(user.getUserProfile().getRegion().getId() != 0l) { //Region.Id为0表示地区为空
			region.setId(user.getUserProfile().getRegion().getId());
		} else {
			region = null;
		}
		obj.getUserProfile().setRegion(region);
		Industry industry = new Industry();
		industry.setId(user.getUserProfile().getIndustry().getId());
		obj.getUserProfile().setIndustry(industry);
		try {
			String enPassword = PasswordUtil.getEncyptedPasswd(obj.getPassword());
			obj.setPassword(enPassword);
		} catch (Exception e) {
			throw new HsCloudException(Constants.PASSWORD_ENCRYPTION_FAILED," Encryption failed.",logger);
		}
		//userService.modifyEnterpriseUser(obj);
	}
	
	/** 
	 * @param user 
	 */
	@Override
	@Transactional
	public void modifyUser(User user) {
		User obj = userService.getUserByEmail(user.getEmail());
		obj.getUserProfile().setTelephone(user.getUserProfile().getTelephone());
		obj.getUserProfile().setCompany(user.getUserProfile().getCompany());
		obj.getUserProfile().setAddress(user.getUserProfile().getAddress());
		Country country = new Country();
		country.setId(user.getUserProfile().getCountry().getId());
		obj.getUserProfile().setCountry(country);
		Region region = new Region();
		if(user.getUserProfile().getRegion().getId() != 0l) { //Region.Id为0表示地区为空
			region.setId(user.getUserProfile().getRegion().getId());
		} else {
			region = null;
		}
		obj.getUserProfile().setRegion(region);
		Industry industry = new Industry();
		industry.setId(user.getUserProfile().getIndustry().getId());
		obj.getUserProfile().setIndustry(industry);
		userService.modifyEnterpriseUser(obj);
	}

	@Override
	public List<ServiceCatalog> getAllSC(List<Sort> sorts,String userLevel,Long domainId,Long zoneGroupId) {
		return serviceCatalogService.getAll(sorts,userLevel,domainId,zoneGroupId);
	}
	@Override
	public Page<Order> getOrderByPage(Page<Order> page, Order order,
			User user, List<Sort> sorts,String query) {
		return orderService.pageOrderWebSite(page, order, user, sorts,query);
	}
	@Override
	public List<OrderItemVo> getAllOrderItemsByOrder(Long id) {
		List<OrderItemVo> result=orderService.getAllOrderItemsByOrder(id);
		for(OrderItemVo item:result){
			String orderItemId=String.valueOf(item.getId());
			String machineNum=operation.getVMIdByOrderItem(orderItemId);
			item.setMachineNum(machineNum);
		}
		return result;
	}

	@Override
	public List<OrderItemVo> orderDetail(long orderId,short orderType)
			throws HsCloudException {
		List<OrderItemVo> result=orderService.orderDetail(orderId, orderType);
		for(OrderItemVo item:result){
			String orderItemId=String.valueOf(item.getId());
			String machineNum=operation.getVMIdByOrderItem(orderItemId);
			item.setMachineNum(machineNum);
		}
		return result;
	}

	@Override
	public boolean cancelOrder(Long orderId)throws HsCloudException {
		boolean result=orderService.cancel(orderId);
		return result;
	}

	// 订单支付
	@Override
	@Transactional
	public Map<String,Object> orderPay(Long orderId,User user,String couponAmount,String giftAmount) throws HsCloudException {
		String result = "";
		Map<String,Object> map = new HashMap<String,Object>();
		Map<NovaServerForCreate,HcEventResource> rabbitMQVM = null;
		try {
			Order order = orderService.findOrderById(orderId);
			verfiyOrderCanPayOrNot(user,order);
			Long accountId = orderDiscountOperation(couponAmount, giftAmount,
					order);
			// 判断余额是否大于支付金额
			boolean checkResult = accountService.checkBalance(accountId,
					order.getTotalAmount());
			if (checkResult) {// 如果余额大于支付金额进行订单支付业务操作
				// 账户余额发生变化需要生成站内提醒信息
				String messageContent = sendMegWhenPayAndReturnMegContent(order);
				// 对账户进行扣款操作
				long transactionId=accountService.accountConsume(user.getId(),(short)1,
						PaymentType.PAYMENT_ONLINE.getIndex(),ServiceType.SERVICE_CLOUD_COMPUTER.getIndex(), accountId,
						messageContent, orderId,ConsumeType.CONSUME_BUY.getIndex(),order.getTotalAmount(),order.getTotalPointAmount(),order.getTotalGiftAmount());
				// 对订单及订单项进行支付的业务操作
				order = orderService.pay(order);
				sendEmailWhenOrderPay(order);
				// 创建虚拟机
				String defaultZone = serverZoneService.getDefaultServerZone()==null?"":serverZoneService.getDefaultServerZone().getCode();
				CreateVmForBuyParamObject createVmParams=
						new CreateVmForBuyParamObject(order, defaultZone);
				if(order.getOrderType()==1){
					rabbitMQVM=createVmForSCBuy(createVmParams);
					insertQuanZeDetailInfo(order, accountId, transactionId, createVmParams);
				}else if(order.getOrderType()==2){
					rabbitMQVM=createVmForDemandBuy(createVmParams);
					insertQuanZeDetailInfo(order, accountId, transactionId, createVmParams);
				}else if(order.getOrderType()==3){//3.按需购买创建路由
					rabbitMQVM=creatRouterForDemandBuy(createVmParams);
				}
				result = Constants.ORDER_PAY_SUCCESS;
			} else {
				result = Constants.ORDER_NOT_ENOUGH;
			}
			map.put(result,rabbitMQVM);
		} catch (Exception e) {
			if(e.getClass()==new HsCloudException().getClass()){
				HsCloudException ex = (HsCloudException)e;
				throw new HsCloudException(ex.getCode(), "createVm-Admin异常", logger, e);
			}else{
				throw new HsCloudException("","createVm-Admin异常", logger, e);
			}
		}

		return map;
	}

	/**
	 * @param orderId
	 * @param user
	 * @param order
	 * @param accountId
	 * @param transactionId
	 * @param createVmParams
	 * @throws NumberFormatException
	 * @throws HsCloudException
	 */
	private void insertQuanZeDetailInfo(Order order,
			Long accountId, long transactionId,
			CreateVmForBuyParamObject createVmParams)
					throws NumberFormatException, HsCloudException {
		for(CreateVmBean cvb:createVmParams.createVmBeans){
			VpdcReference_OrderItem vroi = operation.getOrderItemByOrderItemId(cvb.getOrder_item_id());
			String orderItemIdStr=cvb.getOrder_item_id();
			User user=order.getUser();
			long referenceId=vroi.getVpdcRenferenceId();
			String orderNo=order.getOrderNo();
			long orderItemId=Long.parseLong(orderItemIdStr);
			for(OrderItem item:order.getItems()){
				if(item.getId().longValue()==orderItemId){
					IncomingLog incomingLog=OrderUtils.getIncomingLog(item, referenceId, accountId, transactionId, user.getEmail(), order.getId(), orderNo,user.getDomain().getId());
					incomingLogService.saveIncomingLog(incomingLog);
				}
			}
		}
	}

	/**
	 * @param user
	 * @param order
	 * @return
	 * @throws HsCloudException
	 * @throws Exception
	 */
	private Map<NovaServerForCreate,HcEventResource> creatRouterForDemandBuy(CreateVmForBuyParamObject createVmParams) throws HsCloudException, Exception {
		Map<NovaServerForCreate, HcEventResource> result = operation.createRouterVPDC(
				OrderUtils.getCreateVpdcBean(createVmParams.order, vrouterService.getVrouterTemplate().getId()),
				createVmParams.order.getUser());
		return result;
	}

	/**
	 * @param user
	 * @param order
	 * @param defaultZone
	 * @return
	 * @throws Exception
	 * @throws HsCloudException
	 */
	private Map<NovaServerForCreate,HcEventResource> createVmForDemandBuy(CreateVmForBuyParamObject createVmParams) throws Exception,
	HsCloudException {
		DefaultIsolationConfig isolationConfig=defaultIsolationConfigService.getIsolationConfigById(1L);
		createVmParams.createVmBeans=OrderUtils.getCreateVMBeans(createVmParams.order, isolationConfig);
		Long vpdcId = createVmParams.createVmBeans.get(0).getVpdcId();
		String userId=String.valueOf(createVmParams.order.getUser().getId());
		Map<NovaServerForCreate,HcEventResource> result=new HashMap<NovaServerForCreate,HcEventResource>();
		if (vpdcId == null) {//直接通过主机购买的VM
			result= operation.createVm(createVmParams.createVmBeans,userId,createVmParams.defaultZone);
		} else {// 通过vpdc创建的VM
			if (operation.getVpdcById(vpdcId).getVpdcType() == 0) {// 0 非可路由的VPDC下面创建VM
				result = operation.createVmInNoRouterVPDC(createVmParams.createVmBeans,userId,createVmParams.defaultZone);
			} else {// 1 可路由的VPDC下面创建VM
				result  = operation.createVmInRouterVPDC(createVmParams.createVmBeans,userId,createVmParams.defaultZone);
			}
		}
		return result;
	}

	/**
	 * @param user
	 * @param order
	 * @param defaultZone
	 * @return
	 * @throws NumberFormatException
	 * @throws HsCloudException
	 * @throws Exception
	 */
	private Map<NovaServerForCreate,HcEventResource> createVmForSCBuy(CreateVmForBuyParamObject createVmParams)
			throws NumberFormatException, HsCloudException, Exception {
		Map<OrderItem,ScIsolationConfig> dataForCreateVmBean=new HashMap<OrderItem,ScIsolationConfig>();
		for(OrderItem item:createVmParams.order.getItems()){
			String scIdStr=item.getServiceCatalogId();
			ServiceCatalog sc=serviceCatalogService.get(Integer.parseInt(scIdStr));
			dataForCreateVmBean.put(item, sc.getScIsolationConfig());
		}
		createVmParams.createVmBeans=OrderUtils.getCreateVMBeans(dataForCreateVmBean);
		Map<NovaServerForCreate,HcEventResource> result=operation.createVm(createVmParams.createVmBeans,
				String.valueOf(createVmParams.order.getUser().getId()),createVmParams.defaultZone);
		return result;
	}

	/**
	 * @param user
	 * @param order
	 * @throws Exception
	 */
	private void sendEmailWhenOrderPay(Order order) throws Exception {
		Map<String, String> orderMailTemplate = OrderUtils
				.generateMailVariable(order);
		saveEmail(order.getUser(), orderMailTemplate, MailTemplateType.ORDER_PAID_TEMPLATE.getType());
	}

	/**
	 * @param user
	 * @param order
	 * @throws HsCloudException
	 */
	private void verfiyOrderCanPayOrNot(User user, Order order)
			throws HsCloudException {
		if (order.getUser().getId() != user.getId()) {
			throw new HsCloudException(
					"order creater and current login user is not the same user.",
					logger);
		}
		if (order.getStatus().ordinal() != 0) {
			throw new HsCloudException(
					"order state is not upaid when to pay.", logger);
		}
	}

	/**
	 * @param order
	 * @return
	 */
	private String sendMegWhenPayAndReturnMegContent(Order order) {
		Message message = OrderUtils.orderGenMessageForOrderPay(order,(short) 5);
		messageService.saveMessage(message);
		String messageContent=message.getMessage();
		return messageContent;
	}

	/**
	 * @param couponAmount
	 * @param giftAmount
	 * @param order
	 * @param userId
	 * @return
	 */
	private Long orderDiscountOperation(String couponAmount, String giftAmount,
			Order order) {
		Account account = accountService.getAccountByUserId(order.getUser().getId());
		//order.setRebateRate(account.getCouponsRebateRate());
		//order.setGiftsRebateRate(account.getGiftsRebateRate());
		BigDecimal customerPoints=new BigDecimal(couponAmount);
		BigDecimal accountPoints=account.getCoupons();
		if(customerPoints.compareTo(BigDecimal.ZERO)>=0&&accountPoints.compareTo(customerPoints)>=0){
			OrderUtils.afterPoint(order, customerPoints);
		}else{
			OrderUtils.afterPoint(order, accountPoints);
		}
		BigDecimal customerGifts=new BigDecimal(giftAmount);
		BigDecimal accountGifts=account.getGiftsBalance();
		if(customerGifts.compareTo(BigDecimal.ZERO)>=0&&accountGifts.compareTo(customerGifts)>=0){
			OrderUtils.afterGift(order, customerGifts);
		}else{
			OrderUtils.afterGift(order, accountGifts);
		}
		Long accountId = account.getId();
		return accountId;
	}
	@Override
	public ServiceCatalog getSCById(int id) {
		return serviceCatalogService.get(id);
	}

	@Override
	public Long submitOrder(List<OrderItemVo> orderItems, User user) throws HsCloudException {	 
		try {
			return orderService.submitOrder(orderItems, user,getRebate(user),getGiftRebate(user)).getId();
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public OrderItemVo addItem(int scId,int osId,long feeTypeId) throws HsCloudException {
		try {
			ServiceCatalog sc=serviceCatalogService.get(scId);
			if(sc.getStatus()==2 || sc.getStatus()==3){
				throw new Exception("The sc is enable can't generate the order.");
			}
			List<Os> osList=new ArrayList<Os>();
			List<String> addDiskStrList=new ArrayList<String>();
			OrderItemVo item = new OrderItemVo();
			item.setServiceCatalogName(sc.getName());
			item.setServiceCatalogId(String.valueOf(sc.getId()));
			item.setFlavorId(String.valueOf(sc.getFlavorId()));
			item.setServiceDesc(sc.getDescription());
			item.setUsePointOrNot(sc.getUsePointOrNot());
			item.setUseGiftOrNot(sc.getUseGiftOrNot());
			for(ServiceItem si :sc.getItems()){
				switch(si.getServiceType()){
				case 1:{
					Cpu cpu=(Cpu)si;
					item.setCpu(cpu.getName());
					item.setCpuModel(cpu.getModel());
					item.setvCpus(String.valueOf(cpu.getCoreNum()));
					break;
				}
				case 2:{
					Ram ram=(Ram)si;
					item.setMemory(String.valueOf(ram.getSize()));
					item.setMemoryModel(ram.getModel());
					break;
				}
				case 3:{
					Disk disk=(Disk)si;
					item.setDisk(String.valueOf(disk.getCapacity()));
					item.setDiskModel(disk.getModel());
					break;
				}
				case 4:{
					Os os=(Os)si;
					osList.add(os);
					break;
				}
				case 5:{
					Network network=(Network)si;
					item.setNetwork(String.valueOf(network.getBandWidth()));
					item.setNetworkType(network.getType());
					break;
				}
				case 6:{
					Software sf=(Software)si;
					item.setSoftware(sf.getName());
					break;
				}
				case 8:{
					ExtDisk extDisk=(ExtDisk)si;
					addDiskStrList.add(String.valueOf(extDisk.getCapacity()));
					break;
				}
				}
			}

			if(osList.size()>=1){
				StringBuilder osIdsStr = new StringBuilder();
				for(Os os:osList){
					if (osList.lastIndexOf(os) == osList.size() - 1) {
						osIdsStr.append(os.getId());
					} else {
						osIdsStr.append(os.getId()).append(",");
					}
					if(os.getId()==osId){
						item.setOs(os.getName());
						item.setOsId(String.valueOf(osId));
						item.setImageId(os.getImageId());
					}
				}
				item.setOsIds(osIdsStr.toString());
			}

			if(addDiskStrList.size()>0){
				StringBuilder addDiskStrSB = new StringBuilder();
				for(int i=0;i<addDiskStrList.size();i++){
					if(i==addDiskStrList.size()-1){
						addDiskStrSB.append(addDiskStrList.get(i));
					}else{
						addDiskStrSB.append(addDiskStrList.get(i)).append(",");
					}
				}
				item.setAddDisk(addDiskStrSB.toString());
			}

			if (feeTypeId != 0L) {
				ScFeeType feeType = serviceCatalogService
						.getfeeTypeById(feeTypeId,scId);
				if (feeType != null) {
					item.setPrice(feeType.getPrice());
					item.setPriceType(feeType.getPriceType());
					item.setPricePeriodType(feeType.getPricePeriodType());
					item.setPricePeriod(feeType.getPeriod());
				}
			}
			List<ServerZone> zones=sc.getZoneList();
			String firstZoneCode=zones.get(0).getCode();
			StringBuilder zoneListStr=new StringBuilder(firstZoneCode);
			for(int i=1;i<zones.size();i++){
				zoneListStr.append(",").append(zones.get(i).getCode());
			}
			item.setNodeName(zoneListStr.toString());
			return item;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}
	/**
	 * <根据套餐信息生成订单> 
	 * <把套餐中的信息转存或者说是冗余到订单中> 
	 * @param scId
	 * @param osId
	 * @param period
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("unused")
	private OrderItemVo addItemForRest(int scId,int osId,String period){
		try {
			ServiceCatalog sc=serviceCatalogService.get(scId);
			List<Os> osList=new ArrayList<Os>();
			List<String> addDiskStrList=new ArrayList<String>();
			OrderItemVo item = new OrderItemVo();

			item.setServiceCatalogName(sc.getName());
			item.setServiceCatalogId(String.valueOf(sc.getId()));
			item.setFlavorId(String.valueOf(sc.getFlavorId()));
			if(StringUtils.isNotBlank(sc.getNodeName())){
				item.setNodeName(sc.getNodeName());
			}
			item.setServiceDesc(sc.getDescription());
			for(ServiceItem si :sc.getItems()){
				switch(si.getServiceType()){
				//保存cpu信息
				case 1:{
					Cpu cpu=(Cpu)si;
					item.setCpu(cpu.getName());
					item.setCpuModel(cpu.getModel());
					item.setvCpus(String.valueOf(cpu.getCoreNum()));
					break;
				}
				//内存信息
				case 2:{
					Ram ram=(Ram)si;
					item.setMemory(String.valueOf(ram.getSize()));
					item.setMemoryModel(ram.getModel());
					break;
				}
				//系统盘信息
				case 3:{
					Disk disk=(Disk)si;
					item.setDisk(String.valueOf(disk.getCapacity()));
					item.setDiskModel(disk.getModel());
					break;
				}
				//操作系统信息
				case 4:{
					Os os=(Os)si;
					osList.add(os);
					break;
				}
				//带宽信息
				case 5:{
					Network network=(Network)si;
					item.setNetwork(String.valueOf(network.getBandWidth()));
					item.setNetworkType(network.getType());
					break;
				}
				case 6:{
					Software sf=(Software)si;
					item.setSoftware(sf.getName());
					break;
				}
				//扩展盘信息
				case 8:{
					ExtDisk extDisk=(ExtDisk)si;
					addDiskStrList.add(String.valueOf(extDisk.getCapacity()));
					break;
				}
				}
			}
			//套餐可能包括多个操作系统，但是创建虚拟机时是根据客户传递过来的osid来确定默认的os
			if(osList.size()>=1){
				StringBuilder osIdsStr = new StringBuilder();
				for(Os os:osList){
					if (osList.lastIndexOf(os) == osList.size() - 1) {
						osIdsStr.append(os.getId());
					} else {
						osIdsStr.append(os.getId()).append(",");
					}
					if(os.getId()==osId){
						item.setOs(os.getName());
						item.setOsId(String.valueOf(osId));
						item.setImageId(os.getImageId());
					}
				}
				item.setOsIds(osIdsStr.toString());
			}
			//多个扩展盘以形如extdisk，extdisk1，extdisk2的形式存在订单中
			if(addDiskStrList.size()>0){
				StringBuilder addDiskStrSB = new StringBuilder();
				for(int i=0;i<addDiskStrList.size();i++){
					if(i==addDiskStrList.size()-1){
						addDiskStrSB.append(addDiskStrList.get(i));
					}else{
						addDiskStrSB.append(addDiskStrList.get(i)).append(",");
					}
				}
				item.setAddDisk(addDiskStrSB.toString());
			}
			//根据套餐的计费规则填充订单中金额信息 
			for(ScFeeType feeType :sc.getFeeTypes()){
				if(feeType.getPeriod().equals(period)){
					item.setPrice(feeType.getPrice());
					item.setPriceType(feeType.getPriceType());
					item.setPricePeriodType(feeType.getPricePeriodType());
					item.setPricePeriod(period);
				}
			}
			return item;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}
	@Override
	public void queryOrder(QueryCondition condition) {
		orderService.queryOrder(condition);
	}

	@Override
	@Transactional
	public boolean regularApply(long orderId, String orderStatus)
			throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter regularApply method.");			
		}
		boolean result = false;
		result = orderService.appleyToNormal(orderId, orderStatus);	
		if(logger.isDebugEnabled()){
			logger.debug("exit regularApply method.");
		}
		return result;
	}

	@Override
	@Transactional
	public boolean delayApply(long orderId, String orderStatus)
			throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter delayApply method.");			
		}
		boolean result = false;
		result = orderService.applayToDelay(orderId, orderStatus);
		if(logger.isDebugEnabled()){
			logger.debug("exit delayApply method.");
		}
		return result;
	}
	@Override
	@Transactional
	public String renewOrderV2(String uuid, long feeType, User user, String couponAmount,String giftAmount,String planId)
			throws HsCloudException {
		String result = Constants.VM_RENEW_RESULT_FAIL;
		try {
			// 根据虚拟机Id查询orderItemId与referenceId关联实体bean
			VpdcReference vr = operation.getReferenceByVmId(uuid);
			long referenceId=vr.getId();
			int buyType=vr.getBuyType();
			if(buyType==0){//套餐购买续费
				result=renew4SCVM(referenceId,feeType,user,couponAmount,giftAmount,vr);
			}else if(buyType==1){//按需购买续费
				result=renew4NeedVM(vr,uuid,user,feeType,planId);
			}

			return result;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}
	//套餐续费逻辑没有变化还是按照原来的逻辑走
	private String  renew4SCVM(long referenceId, long feeTypeId, User user, String couponAmount,String giftAmount,VpdcReference vr)throws Exception{
		String result="";
		if (VMTypeEnum.REGULAR.getCode() == vr.getVm_type()) {
			// 根据虚拟机Id查询orderItemId与referenceId关联实体bean
			VpdcReference_OrderItem vroi = operation
					.getOrderItemByReferenceId(referenceId);
			// 只有根据vmId查询出相应的reference和orderItem的对应关系，才能进行续费操作，否则续费失败
			if (vroi != null) {
				long oldOrderItemId = Long.valueOf(vroi.getOrder_item_id());
				long userId = user.getId();
				OrderItem orderItem = orderItemService
						.getOrderItemById(oldOrderItemId);
				String scId = orderItem.getServiceCatalogId();
				int scIdIntOld = Integer.parseInt(scId);
				int scIdIntNew= vr.getScId();
				BigDecimal price = null;
				String priceType = null;
				String pricePeriodType = null;
				String period = null;
				if (feeTypeId != 0L) {
					ScFeeType feeType = serviceCatalogService
							.getfeeTypeById(feeTypeId, scIdIntNew);
					if (feeType != null) {
						price = feeType.getPrice();
						priceType = feeType.getPriceType();
						pricePeriodType = feeType.getPricePeriodType();
						period = feeType.getPeriod();
					}
				}

				Account account = accountService.getAccountByUserId(userId);
				Integer rebateRateInt=getRebate(user);
				Integer giftRebateRateInt=getGiftRebate(user);
				BigDecimal rebateRate=new BigDecimal(rebateRateInt);
				BigDecimal giftRebateRate=new BigDecimal(giftRebateRateInt);
				BigDecimal accountPoints=account.getCoupons();
				BigDecimal customerPoints=new BigDecimal(couponAmount);
				BigDecimal actuallyPoints=BigDecimal.ZERO;
				ServiceCatalog sc=serviceCatalogService.get(scIdIntNew);
				BigDecimal actuallyAmont=price;
				if(sc.getUsePointOrNot()){
					if(customerPoints.compareTo(BigDecimal.ZERO)>=0&&accountPoints.compareTo(customerPoints)>=0){
						actuallyPoints=customerPoints;
					}else{
						actuallyPoints=accountPoints;
					}
					actuallyAmont=OrderUtils.getActuallyOrderAmount(price,actuallyPoints,rebateRate);
				}
				BigDecimal customerGifts=new BigDecimal(giftAmount);
				BigDecimal actuallyGifts=BigDecimal.ZERO;
				if(sc.getUseGiftOrNot()){
					if(customerGifts.compareTo(BigDecimal.ZERO)>=0&&account.getGiftsBalance().compareTo(customerGifts)>=0){
						actuallyGifts=customerGifts;
					}else{
						actuallyGifts=account.getGiftsBalance();
					}
					actuallyAmont=OrderUtils.getActuallyOrderAmount(price,actuallyAmont,actuallyGifts,giftRebateRate);
				}
				Long accountId = account.getId();
				boolean checkResult = accountService.checkBalance(
						accountId, actuallyAmont);
				// checkResult为标志，如果余额大于需要支付金额，续费产生的新订单为已支付，否则为未支付
				if (checkResult) {
					String description = Constants.VM_PERIOD_LOG_RENEWCOST;
					// 获取虚拟机失效时间
					VpdcReference_Period vrp =  operation
							.getReferencePeriod(referenceId);
					Date expirationDate = vrp.getEndTime();
					// 对订单及订单项进行支付的业务操作
					OrderItem orderItemNew = orderService.renewOrderV2(
							orderItem,actuallyPoints,actuallyGifts, price, priceType, pricePeriodType,
							period, user, expirationDate,
							rebateRateInt,giftRebateRateInt);
					if(scIdIntNew!=scIdIntOld){
						orderItemNew.setServiceCatalogId(String.valueOf(scIdIntNew));
						orderItemNew.setServiceCatalogName(sc.getName());
						orderItemNew.setServiceDesc(sc.getDescription());
					}
					Order newOrder = orderItemNew.getOrder();
					newOrder=OrderUtils.afterPoint(newOrder,actuallyPoints);
					newOrder=OrderUtils.afterGift(newOrder,actuallyGifts);
					Long newOrderItemId = orderItemNew.getId();
					BigDecimal newAmount=newOrder.getTotalAmount();
					Long orderId = newOrder.getId();
					// 续费需要扣款，需要发送站内提醒
					Message message = OrderUtils.orderGenMessage(vr.getName(),
							newOrder, user.getId(),(short) 2);
					messageService.saveMessage(message);
					// 对账户进行扣款操作
					long transactionId=accountService.accountConsume(userId,(short)1,
							PaymentType.PAYMENT_ONLINE.getIndex(),ServiceType.SERVICE_CLOUD_COMPUTER.getIndex(),
							accountId,message.getMessage(), orderId,
							ConsumeType.CONSUME_RENEW.getIndex(),
							newAmount,newOrder.getTotalPointAmount(),newOrder.getTotalGiftAmount());
					Date expirationDateNew = orderItemNew
							.getExpirationDate();
					Date effectiveDateNew = orderItemNew.getEffectiveDate();
					//新添加虚拟机与订单项的关联关系
					operation.saveReference_OrderItem(referenceId,
							String.valueOf(newOrderItemId));
					// 更新虚拟机的失效时间
					operation.updateReferencePeriod(
							String.valueOf(newOrderItemId), null,
							expirationDateNew);
					//启用已到期VM
					IncomingLog incomingLog=new IncomingLog();
					incomingLog.setAccountId(accountId);
					incomingLog.setAmount(newAmount);
					BigDecimal days=null;
					if(period.equals("12")){
						days=BigDecimal.valueOf(365L);
					}else{
						days=BigDecimal.valueOf(Integer.parseInt(period)*31L);
					}
					incomingLog.setDayPrice(newAmount.divide(days, 2, RoundingMode.DOWN));
					incomingLog.setEffectiveDate(effectiveDateNew);
					incomingLog.setScId(Integer.parseInt(orderItemNew.getServiceCatalogId()));
					incomingLog.setScName(orderItemNew.getServiceCatalogName());
					incomingLog.setExpirationDate(expirationDateNew);
					incomingLog.setEmail(user.getEmail());
					incomingLog.setIncomingType((short)1);
					incomingLog.setObjectId(referenceId);
					incomingLog.setOrderId(orderId);
					incomingLog.setOrderItemId(newOrderItemId);
					incomingLog.setOrderNo(newOrder.getOrderNo());
					incomingLog.setProductType((short)1);
					incomingLog.setTranscationType(TranscationType.TRANSCATION_CONSUME.getIndex());
					incomingLog.setTransactionId(transactionId);
					incomingLog.setDomainId(user.getDomain().getId());
					incomingLogService.saveIncomingLog(incomingLog);
					if(expirationDateNew.after(new Date())){
						operation.activeExpireVM(vr, user.getId());
					}
					operation.saveVmPeriodLog(description, referenceId,
							effectiveDateNew, expirationDateNew);

					result = Constants.VM_RENEW_RESULT_SUCC;
				} else {
					result = Constants.VM_RENEW_RESULT_NOFEE;
				}
			}
		}

		return result;
	}

	// 按需续费逻辑需要先从hc_vpdc_reference拿vm配置数据出来，然后去3p划价来生成订单及扣费。
	private String renew4NeedVM(VpdcReference vr, String uuid, User user,
			long buyPeriod,String planId) throws Exception {
		String result = "";
		// 根据虚拟机Id查询orderItemId与referenceId关联实体bean
		long referenceId = vr.getId();
		long userId = user.getId();

		OrderItem itemNew = generateOrderItemForReference(vr, buyPeriod);
		itemNew.setPlanId(planId);
		ServerZone sz=serverZoneService.getServerZoneByCode(vr.getVmZone());
		String zoneGroupCode=sz.getZoneGroupList().get(0).getCode();
		OrderUtils.getPriceForOrderItemFrom3P(itemNew,user.getDomain().getCode(),user.getLevel(),zoneGroupCode);
		Account account = accountService.getAccountByUserId(userId);
		BigDecimal price = itemNew.getPrice();
		BigDecimal actuallyPoints = BigDecimal.ZERO;
		BigDecimal actuallyAmont = itemNew.getPrice();
		BigDecimal actuallyGifts=BigDecimal.ZERO;
		Long accountId = account.getId();
		boolean checkResult = accountService.checkBalance(accountId,
				actuallyAmont);
		// checkResult为标志，如果余额大于需要支付金额，续费产生的新订单为已支付，否则为未支付
		if (checkResult) {
			String description = Constants.VM_PERIOD_LOG_RENEWCOST;
			// 获取虚拟机失效时间
			VpdcReference_Period vrp = operation
					.getReferencePeriod(referenceId);
			Date expirationDate = vrp.getEndTime();
			// 对订单及订单项进行支付的业务操作
			itemNew.setAmount(actuallyAmont);
			itemNew.setPointAmount(actuallyPoints);
			OrderItem orderItemNew = orderService.renewOrder4Need(uuid,
					itemNew, user, expirationDate,0,0);
			Order newOrder = orderItemNew.getOrder();
			newOrder = OrderUtils.afterPoint(newOrder, actuallyPoints);
			newOrder=OrderUtils.afterGift(newOrder,actuallyGifts);
			Long newOrderItemId = orderItemNew.getId();
			BigDecimal newAmount = newOrder.getTotalAmount();
			Long orderId = newOrder.getId();
			// 续费需要扣款，需要发送站内提醒
			Message message = OrderUtils.orderGenMessage(vr.getName(),
					newOrder, user.getId(), (short) 2);
			messageService.saveMessage(message);
			// 对账户进行扣款操作
			long transactionId = accountService.accountConsume(userId,
					(short) 1, PaymentType.PAYMENT_ONLINE.getIndex(),
					ServiceType.SERVICE_CLOUD_COMPUTER.getIndex(), accountId
					,message.getMessage(),orderId, ConsumeType.CONSUME_RENEW.getIndex()
					,newAmount, newOrder.getTotalPointAmount(),
					newOrder.getTotalGiftAmount());
			Date expirationDateNew = orderItemNew.getExpirationDate();
			Date effectiveDateNew = orderItemNew.getEffectiveDate();
			// 新添加虚拟机与订单项的关联关系
			operation.saveReference_OrderItem(referenceId,
					String.valueOf(newOrderItemId));
			// 更新虚拟机的失效时间
			operation.updateReferencePeriod(String.valueOf(newOrderItemId),
					null, expirationDateNew);
			// 启用已到期VM
			IncomingLog incomingLog = new IncomingLog();
			incomingLog.setAccountId(accountId);
			incomingLog.setAmount(newAmount);
			BigDecimal days = null;
			if (buyPeriod == 12) {
				days = BigDecimal.valueOf(365L);
			} else {
				days = BigDecimal.valueOf(buyPeriod * 31L);
			}
			incomingLog.setDayPrice(newAmount
					.divide(days, 2, RoundingMode.DOWN));
			incomingLog.setEffectiveDate(effectiveDateNew);
			incomingLog.setExpirationDate(expirationDateNew);
			incomingLog.setEmail(user.getEmail());
			incomingLog.setIncomingType((short) 1);
			incomingLog.setObjectId(referenceId);
			incomingLog.setOrderId(orderId);
			incomingLog.setOrderItemId(newOrderItemId);
			incomingLog.setOrderNo(newOrder.getOrderNo());
			incomingLog.setProductType((short) 1);
			incomingLog.setTranscationType(TranscationType.TRANSCATION_CONSUME
					.getIndex());
			incomingLog.setTransactionId(transactionId);
			incomingLog.setDomainId(user.getDomain().getId());
			incomingLogService.saveIncomingLog(incomingLog);
			if (expirationDateNew.after(new Date())) {
				operation.activeExpireVM(vr, user.getId());
			}
			operation.saveVmPeriodLog(description, referenceId,
					effectiveDateNew, expirationDateNew);

			result = Constants.VM_RENEW_RESULT_SUCC;
		} else {
			result = Constants.VM_RENEW_RESULT_NOFEE;
		}
		return result;
	}
	@Override
	@Transactional
	public boolean setCPpwd(String vmId,String pwd,User user)throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter login method.");
			logger.debug("vmId:"+vmId);		
			logger.debug("password:"+pwd);		
		}
		ControlPanelUser controlPanel = controlPanelService.findControlUserByVmID(vmId);
		if(controlPanel!=null){
			controlPanel.setCreate_Id(user.getId());
			controlPanel.setUpdateDate(new Date());
			try {
				controlPanel.setUserPassword(PasswordUtil.getEncyptedPasswd(pwd));
				controlPanelService.saveControlPanelUser(controlPanel);
			} catch (Exception e) {
				throw new HsCloudException(Constants.PASSWORD_ENCRYPTION_FAILED," setCPpwd failed.",logger);
			}
		}else{
			//针对以前创建的VM没有创建控制面板用户，需要新创建
			String ip = operation.getVmIpByVmId(vmId);
			controlPanelService.createControlUser(ip, pwd,user.getId(), vmId);
		}
		return true;
	}

	/** 
	 * @param vmId
	 * @return
	 * @throws HsCloudException 
	 */
	@Override
	public RenewOrderVO getRenewOrderInfo(String vmId,User u) throws HsCloudException {
		logger.info("OPS-Facade-findDetailVmById start");
		RenewOrderVO roVO = null;
		try {
			roVO = operation.getRenewOrder(vmId,u);
		} catch (HsCloudException e) {
			throw new HsCloudException(Constants.VM_DETAIL_ERROR, "findDetailVmById异常",
					logger, e);
		}
		return roVO;
	}

	@Override
	public Account getAccountByUserId(long userId) throws HsCloudException {
		//		Long entUserId=userService.getEntUserId(userId);
		//		return accountService.getAccountByUserId(entUserId);
		Account account= accountService.getAccountByUserId(userId);
	
		return account;
	}
    

	@Override
	public Account getAccountByUserId(User user) {
		Account account= accountService.getAccountByUserId(user.getId());
		 Integer rebate=    getRebate(user);
		 Integer giftRebate=  getGiftRebate(user);
		 if(null!=rebate&&rebate.intValue()!=0){
			 account.setCouponsRebateRate(rebate);
		 }
		 if(null!=giftRebate&&giftRebate.intValue()!=0){
			 account.setGiftsRebateRate(giftRebate);
		 }
		return account;
	}
	

	@Override
	public AccountVO getAccountWithRealRebateByUser(User user)
			throws HsCloudException {
		// TODO Auto-generated method stub
		Account ac=accountService.getAccountByUserId(user.getId());
		AccountVO result=new AccountVO();
		result.setBalance(ac.getBalance());
		result.setCoupons(ac.getCoupons());
		result.setGiftsBalance(ac.getGiftsBalance());
		result.setCouponsRebateRate(getRebate(user));
		result.setGiftsRebateRate(getGiftRebate(user));
		return result;
	}

	//************************lihonglei start**************************************************
	/**
	 * 查询未读消息数
	 * @param userId
	 * @return
	 */
	@Override
	public long findUnreadCount(long userId) {
		return messageService.findUnreadCount(userId);
	}

	/**
	 * 查询未读消息
	 * @param page
	 * @param userId
	 * @param condition
	 * @return
	 */
	@Override
	public Page<Message> findUnreadMessage(Page<Message> page, Long userId, Map<String, Object> condition) {
		return messageService.findUnreadMessage(page, userId, condition);
	}

	/**
	 * 查询已读消息
	 * @param page
	 * @param userId
	 * @param condition
	 * @return
	 */
	@Override
	public Page<Message> findReadedMessage(Page<Message> page, Long userId, Map<String, Object> condition) {
		return messageService.findReadedMessage(page, userId, condition);
	}

	/**
	 * 修改消息状态
	 * @param id
	 */
	@Override
	@Transactional
	public void modifyMessageStatus(Long id) {
		messageService.modifyMessageStatus(id);
	}

	/**
	 * 保存用户银行信息
	 * @param userBank
	 */
	@Override
	@Transactional
	public void saveUserBank(UserBank userBank) {
		userBankService.saveUserBank(userBank);
	}

	@Override
	public Page<TranscationLogVO> pagePermissionTrLog(List<Sort> sort,
			long id, Page<TranscationLogVO> pageLog, String query,
			List<Object> primKeys) {

		if(null != query &&  !"".equals(query)){
			try {
				query = new String(query.getBytes("iso8859_1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
		}

		return transcationLogService.searchPermissionTranLog(sort, id, pageLog,query, primKeys);
	}

	/**
	 * 查询用户银行信息
	 * @param userId
	 * @return
	 */
	@Override
	public UserBank findBankByUserId(Long userId) {
		return userBankService.findBankByUserId(userId);
	}

	/**
	 * 删除消息
	 * @param id
	 */
	@Override
	@Transactional
	public void deleteMessage(Long id) {
		messageService.deleteMessage(id);
	}

	/**
	 * 获取ResourceType列表
	 * @return
	 */
	@Override
	public List<ResourceType> getResourceTypeList() {
		if(resourceTypeList == null || resourceTypeList.isEmpty()) {
			resourceTypeList = resourceTypeService.getResourceTypeList(Constant.STATUS_USER);
		}
		return resourceTypeList;
	}

	/**
	 * 获取action列表
	 * @param actionType
	 * @return
	 */
	@Override
	public List<Action> getActionList(String actionType) {
		return actionService.getAction(actionType, Constant.ACTION_LEVEL, (short)Constant.STATUS_USER);
	}

	/**
	 * 查找未分配列表
	 * @param type
	 * @param roleId
	 * @param pagePrivilege
	 * @param query
	 * @return
	 */
	@Override
	@Transactional(readOnly = false)
	public List<PrivilegeVO> findUnassignedList(String type, Long userId, Page<PrivilegeVO> pagePrivilege, String query, List<Object> primKeys) {
		Map<String, Object> map = getParamMap(type, userId, query, primKeys);
		String conditionTable = "select permission_id from hc_usergroup_permission up where up.usergroup_id = :conditionId";
		map.put("conditionTable", conditionTable);

		List<TreeQueryVO> list = treeService.findUnassignedList(map, pagePrivilege);
		List<Object> ids = new ArrayList<Object>();
		for(TreeQueryVO vo : list) {
			if(vo.getResourceId() == null) {
				Long resourceId = resourceService.addResource(vo.getId(), type);
				vo.setResourceId(resourceId);
			} else {
				ids.add(vo.getResourceId());
			}
		}
		List<CheckboxVO> checkList = new ArrayList<CheckboxVO>();
		if(!ids.isEmpty()) {
			checkList = treeService.findPermissionForUser(ids, userId);
		}
		List<PrivilegeVO> result = new ArrayList<PrivilegeVO>();
		for(TreeQueryVO obj : list) {
			Long resourceId = obj.getResourceId();
			String name = obj.getName();

			filllResult(result, checkList, resourceId, name);
		}

		return result;
	}

	/**
	 * 查找分配列表
	 * @param type
	 * @param roleId
	 * @param pagePrivilege
	 * @param query
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public List<PrivilegeVO> findAssignedList(String type, Long userId, Page<PrivilegeVO> pagePrivilege, String query, List<Object> primKeys) {
		Map<String, Object> map = getParamMap(type, userId, query, primKeys);
		String conditionTable = "select p.resource_id from hc_permission p, hc_usergroup_permission up where p.id = up.permission_id and up.usergroup_id = :conditionId";
		map.put("conditionTable", conditionTable);

		List<Object> list = treeService.findAssignedList(map, pagePrivilege);
		List<Object> ids = new ArrayList<Object>();

		List<PrivilegeVO> result = new ArrayList<PrivilegeVO>();
		if(list != null && !list.isEmpty()) {
			for(Object obj : list) {
				Object[] array = (Object[])obj;
				ids.add(array[0]);
			}
			List<CheckboxVO> checkList = treeService.findPermissionForUser(ids, userId);

			for(Object obj : list) {
				Object[] array = (Object[])obj;
				Long resourceId = ((BigInteger)array[0]).longValue();
				String name = (String)array[1];

				filllResult(result, checkList, resourceId, name);
			}
		}
		return result;
	}

	/**
	 * 返回值封装 
	 * <功能详细描述> 
	 * @param result
	 * @param checkList
	 * @param resourceId
	 * @param name 
	 * @see [类、类#方法、类#成员]
	 */
	private void filllResult(List<PrivilegeVO> result,
			List<CheckboxVO> checkList, Long resourceId, String name) {
		PrivilegeVO  privilegeVO= new PrivilegeVO();
		privilegeVO.setResourceId(resourceId);
		privilegeVO.setName(name);
		for(CheckboxVO checkboxVO : checkList) {
			if(resourceId.equals(checkboxVO.getResourceId() )) {
				privilegeVO.getCheckboxVOList().add(checkboxVO);
			}
		}
		result.add(privilegeVO);
	}

	/**
	 * 查询统一赋权类别
	 * @param type
	 * @param userId
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public List<PrivilegeVO> findUiformDefList(String type, Long userId) {
		String tableAndCondition = " hc_usergroup_permission up on p.id = up.permission_id and up.usergroup_id = :id ";
		String column = "up.permission_id";
		return treeService.findUiformDefList(type, userId, tableAndCondition, column);
	}

	/**
	 * 处理权限查询参数 
	 * <功能详细描述> 
	 * @param type
	 * @param roleId
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	private Map<String, Object> getParamMap(String type, Long roleId, String query, List<Object> primKeys) {
		Map<String, ResourceType> resourceMap = getResourceTypeMap();
		ResourceType resourceType = resourceMap.get(type);
		String table = resourceType.getResourceTable();



		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("type", type);
		conditionMap.put("conditionId", roleId);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("table", table);
		map.put("query", query);
		String resourceCondition = resourceType.getResourceCondition();
		if(primKeys != null && !primKeys.isEmpty()) {
			resourceCondition += " and r.primKey in (:primKeys)";
			conditionMap.put("primKeys", primKeys);
		}
		map.put("resourceCondition", resourceCondition);
		map.put("conditionMap", conditionMap);
		return map;
	}
	/**
	 * 权限变更
	 * @param permissionValue
	 * @param actionValue
	 * @param resourceValue
	 * @param userGroupId
	 */
	@Override
	@Transactional(readOnly = false)
	public void modifyRolePermissiion(String permissionValue, String actionValue, String resourceValue, long userGroupId) {
		userGroupPermissionService.batchDelete(resourceValue, userGroupId);
		String[] permissionArray = permissionValue.split(",");
		List<Long> list = new ArrayList<Long>();
		for(String permissionId : permissionArray) {
			if(StringUtils.isNotBlank(permissionId)) {
				list.add(Long.valueOf(permissionId));
			}
		}
		String[] actionArray = actionValue.split(",");
		for(String actionStr : actionArray) {
			if(StringUtils.isNotBlank(actionStr)) {
				String[] array = actionStr.split("-");
				long resourceId = Long.valueOf(array[0]);
				long actionId = Long.valueOf(array[1]);
				long permissionId = permissionService.addPermission(resourceId, actionId);
				list.add(permissionId);
			}
		}
		userGroupPermissionService.saveUserGroupPermission(userGroupId, list);
	}
	/**
	 * 查找公告信息
	 * @param counter
	 * @param type
	 * @return
	 */
	@Override
	public List<Announcement> findAnnouncementByConditoin(int counter, int type, Long domainId) {
		return announcementService.findAnnouncementByConditoin(counter, type, domainId);
	}
	/**
	 * 发票信息初始化
	 * @param userId
	 * @return
	 */
	@Override
	public Map<String, Object> initInvoice(long userId) {
		InvoiceAccount invoiceAccount = invoiceAccountService.findInvoiceAccount(userId);
		InvoiceVO invoiceVO = new InvoiceVO();
		try {
			BeanUtils.copyProperties(invoiceVO, invoiceAccount);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		List<Province> provinceList = dataService.getProvinceList();
		UserVO userVO = userService.getUserVO(userId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("invoiceVO", invoiceVO);
		result.put("provinceList", provinceList);
		result.put("user", userVO);
		return result;
	}
	/**
	 * 开发票申请
	 * @param userId
	 * @param invoiceVO
	 */
	@Override
	@Transactional(readOnly = false)
	public String applyInvoice(User user, InvoiceVO invoiceVO) {
		InvoiceRecord invoiceRecord = new InvoiceRecord();
		try {
			BeanUtils.copyProperties(invoiceRecord, invoiceVO);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		long userId = user.getId();
		invoiceRecord.setBillingTime(null);
		invoiceRecord.setSendTime(null);
		invoiceRecord.setUserId(userId);

		String result = Constants.INVOICE_EXCEPTION;
		int takeInvoiceType = Integer.valueOf(invoiceVO.getTakeInvoiceType());
		if (takeInvoiceType != TakeInvoiceType.SELF_CREATED.getIndex()) {
			Account account = accountService.getAccountByUserId(userId);

			TakeInvoiceType type = TakeInvoiceType
					.getTakeInvoiceType(takeInvoiceType);
			if (!accountService.checkBalance(account.getId(), type.getPrice())) {
				return Constants.INVOICE_SEND_PRICE_EXCEPTION;
			}
			result = invoiceAccountService.applyInvoice(userId,
					invoiceVO.getInvoiceAmount());
			if(!Constants.SUCCESS.equals(result)) {
				return result;
			}
			long transactionLogId = accountService.accountConsume(userId,(short)1,
					PaymentType.PAYMENT_ONLINE.getIndex(),ServiceType.SERVICE_CLOUD_EXPRESS.getIndex(), account.getId(),
					type.getDescription(), null,(short)1,
					type.getPrice(), new BigDecimal(0), new BigDecimal(0));

			long invoiceRecordId = invoiceRecordService
					.applyInvoiceRecord(invoiceRecord);
			InvoiceRecordTransaction entity = new InvoiceRecordTransaction();
			entity.setInvoiceRecordId(invoiceRecordId);
			entity.setTransactionLogId(transactionLogId);
			invoiceRecordTransactionService.saveEntity(entity);


			Date today = Calendar.getInstance().getTime();
			IncomingLog incomingLog=new IncomingLog();
			incomingLog.setAccountId(account.getId());
			incomingLog.setAmount(type.getPrice());
			incomingLog.setDayPrice(new BigDecimal(0));
			incomingLog.setEffectiveDate(today);
			incomingLog.setEmail(user.getEmail());
			incomingLog.setExpirationDate(today);
			incomingLog.setIncomingType((short)2);
			incomingLog.setDomainId(user.getDomain().getId());
			incomingLog.setProductType((short)2);
			incomingLog.setTransactionId(transactionLogId);
			incomingLog.setTranscationType((short)1);
			incomingLogService.saveIncomingLog(incomingLog);
		} else {
			result = invoiceAccountService.applyInvoice(userId,
					invoiceVO.getInvoiceAmount());
			if(!Constants.SUCCESS.equals(result)) {
				return result;
			}
			invoiceRecordService.applyInvoiceRecord(invoiceRecord);
		}

		return result;
	}
	/**
	 * 查询发票列表
	 * @param invoicePage
	 * @param condition
	 * @return
	 */
	@Override
	public Page<Map<String, Object>> findInvoiceRecordList(
			Page<Map<String, Object>> invoicePage, Map<String, Object> condition) {
		return invoiceRecordService.findInvoiceRecordList(invoicePage, condition);
	}

	//邮件模板设置
	private void saveEmail(User user, Map<String, String> map, String type) {

		Domain domain = domainService.getDomainById(user.getDomain().getId());

		map.put("abbreviation", domain.getAbbreviation());
		map.put("name", domain.getName());
		map.put("telephone", domain.getTelephone());
		map.put("address", domain.getAddress());
		map.put("url", domain.getUrl());
		map.put("bank", domain.getBank());
		map.put("cardNo", domain.getCardNo());
		map.put("serviceHotline", domain.getServiceHotline());
		map.put("webSiteUrl",domain.getPublishingAddress());
		map.put("image", Constants.MAIL_LOGO_SRC+domain.getCode()+"/images/mail_logo_"+domain.getCode()+"_zh_CN.gif");

		map.put("webAddress", domain.getPublishingAddress());

		mailService.saveEmail(user.getEmail(), null, type,
				domain.getId(), map);
	}

	@Override
	public boolean releaseIp(String sIP) {
		return iPService.releaseIp(sIP);
	}

	private Integer getRebate(User user){
		Account account=accountService.getAccountByUserId(user.getId());
		if(account.getCouponsRebateRate()==null||account.getCouponsRebateRate().intValue()==0){
			UserBrand ub=userBrandService.getBrandByCode(user.getLevel());
			if(ub.getRebateRate()!=null&&ub.getRebateRate().intValue()!=0){
				return ub.getRebateRate();
			}else{
				return 0;
			}
		}else{
			return account.getCouponsRebateRate();
		}
	}

	private Integer getGiftRebate(User user){
		Account account=accountService.getAccountByUserId(user.getId());
		if(account.getGiftsRebateRate()==null||account.getGiftsRebateRate().intValue()==0){
			UserBrand ub=userBrandService.getBrandByCode(user.getLevel());
			if(ub.getGiftsDiscountRate()!=null&&ub.getGiftsDiscountRate().intValue()!=0){
				return ub.getGiftsDiscountRate();
			}else{
				return 0;
			}
		}else{
			return account.getGiftsRebateRate();
		}
	}
	//************************lihonglei end**************************************************	
	@SuppressWarnings("unused")
	@Override
	@Transactional
	public String toNormalV2(long feeTypeId, long referenceId,
			User user, String couponAmount,String giftAmount)
					throws HsCloudException {
		if (logger.isDebugEnabled()) {
			logger.debug("enter toNormalV2 method.");
		}
		String result = Constants.VM_RENEW_RESULT_FAIL;
		try {
			VpdcReference vr = operation.getReferenceById(referenceId);
			// 试用状态或已延期状态VM或延期待审核VM 可以【转正】
			if (VMStatusBussEnum.TRY.getCode() == vr.getVm_business_status()
					.intValue()
					|| VMStatusBussEnum.DELAY.getCode() == vr
					.getVm_business_status().intValue() 
					|| VMStatusBussEnum.DELAYWAIT.getCode() == vr
					.getVm_business_status().intValue()) {
				ScFeeType feeType = serviceCatalogService
						.getfeeTypeById(feeTypeId, vr.getScId());
				BigDecimal price = feeType.getPrice();
				// 获取订单关联的用户信息
				Long userId = user.getId();
				// 根据企业用户Id获取其账户信息
				ServiceCatalog sc=serviceCatalogService.get(vr.getScId());
				Account account = accountService.getAccountByUserId(userId);
				Integer rebateRateInt=getRebate(user);
				Integer giftRebateRateInt=getGiftRebate(user);
				BigDecimal rebateRate=new BigDecimal(rebateRateInt);
				BigDecimal giftRebateRate=new BigDecimal(giftRebateRateInt);
				BigDecimal accountPoints=account.getCoupons();
				BigDecimal customerPoints=new BigDecimal(couponAmount);
				BigDecimal actuallyPoints=BigDecimal.ZERO;
				BigDecimal actuallyAmont=price;
				if(sc!=null&&sc.getUsePointOrNot()){
					if(customerPoints.compareTo(BigDecimal.ZERO)>=0&&accountPoints.compareTo(customerPoints)>=0){
						actuallyPoints=customerPoints;
					}else{
						actuallyPoints=accountPoints;
					}
					actuallyAmont=OrderUtils.getActuallyOrderAmount(price,actuallyPoints,rebateRate);
				}
				BigDecimal accountGifts=account.getGiftsBalance();
				BigDecimal customerGifts=new BigDecimal(giftAmount);
				BigDecimal actuallyGifts=BigDecimal.ZERO;
				if(sc.getUseGiftOrNot()){
					if(customerGifts.compareTo(BigDecimal.ZERO)>=0&&accountGifts.compareTo(customerGifts)>=0){
						actuallyGifts=customerGifts;
					}else{
						actuallyGifts=accountGifts;
					}
					actuallyAmont=OrderUtils.getActuallyOrderAmount(price,actuallyAmont,actuallyGifts,giftRebateRate);
				}
				Long accountId = account.getId();
				// 检查账户余额是否大于需要支付的金额
				boolean checkResult = accountService.checkBalance(accountId,
						actuallyAmont);
				if (checkResult) {// 如果余额大于需要支付金额进行正常的转正流程
					String priceType=feeType.getPriceType();
					String pricePeriodType=feeType.getPricePeriodType();
					String period=feeType.getPeriod();
					Order order = vmToNormalGenOrder(price,priceType,pricePeriodType,period,user,
							vr,sc,rebateRateInt,giftRebateRateInt,actuallyPoints,actuallyGifts);
					long orderId = order.getId();
					order=OrderUtils.afterPoint(order, actuallyPoints);
					order=OrderUtils.afterGift(order, actuallyGifts);
					BigDecimal totalAmount=order.getTotalAmount();
					// 对账户进行扣款操作需要生成站内提醒
					Message message = OrderUtils.orderGenMessage(vr.getName(),
							order,user.getId(), (short) 1);
					messageService.saveMessage(message);
					// 从账户内扣钱
					long transactionId=accountService.accountConsume(userId,(short)1,
							PaymentType.PAYMENT_ONLINE.getIndex(),ServiceType.SERVICE_CLOUD_COMPUTER.getIndex(),accountId,
							message.getMessage(),
							orderId,ConsumeType.CONSUME_APPROVED.getIndex(),
							totalAmount,order.getTotalPointAmount(),order.getTotalGiftAmount());
					String description = Constants.VM_PERIOD_LOG_REGULAR;
					// 获取虚拟机试用截止时间
					VpdcReference_Period vrp = operation.getReferencePeriod(referenceId);
					Date VmExpirationDate =vrp.getEndTime();
					OrderItem item = orderService.toNormalV2(order,VmExpirationDate);
					// 更新VM类型及业务状态
					operation.regularTryVm(user.getId(),referenceId);
					Long itemId = item.getId();
					Date expirationDate = item.getExpirationDate();
					Date effectiveDate=item.getEffectiveDate();
					// 更新虚拟机失效时间
					/**
					 * 添加order_item_id和reference_id的关联关系
					 */
					operation.saveReference_OrderItem(vr.getId(),
							String.valueOf(itemId));
					IncomingLog incomingLog=new IncomingLog();
					incomingLog.setAccountId(accountId);
					incomingLog.setAmount(totalAmount);
					incomingLog.setScId(Integer.parseInt(item.getServiceCatalogId()));
					incomingLog.setScName(item.getServiceCatalogName());
					BigDecimal days=null;
					if(period.equals("12")){
						days=BigDecimal.valueOf(365L);
					}else{
						days=BigDecimal.valueOf(Integer.parseInt(period)*31L);
					}
					incomingLog.setDayPrice(totalAmount.divide(days, 2, RoundingMode.DOWN));
					incomingLog.setEffectiveDate(effectiveDate);
					incomingLog.setExpirationDate(expirationDate);
					incomingLog.setEmail(user.getEmail());
					incomingLog.setIncomingType((short)1);
					incomingLog.setObjectId(referenceId);
					incomingLog.setOrderId(orderId);
					incomingLog.setOrderItemId(itemId);
					incomingLog.setOrderNo(order.getOrderNo());
					incomingLog.setDomainId(user.getDomain().getId());
					incomingLog.setProductType((short)1);
					incomingLog.setTranscationType(TranscationType.TRANSCATION_CONSUME.getIndex());
					incomingLog.setTransactionId(transactionId);
					incomingLogService.saveIncomingLog(incomingLog);
					//更新虚拟机的失效时间
					operation.updateReferencePeriod(String.valueOf(itemId),
							null, expirationDate);
					// 如果虚拟机已经到期禁用，则进行激活操作
					operation.activeExpireVM(vr, userId);
					//// 添加虚拟机到期日期变更记录
					operation.saveVmPeriodLog(description,referenceId, effectiveDate,expirationDate);
					result = Constants.VM_RENEW_RESULT_SUCC;
				} else {
					result = Constants.VM_RENEW_RESULT_NOFEE;
				}
			}

		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit toNormalV2 method.");
		}
		return result;
	}
	/**
	 * <根据套餐数据生成订单> 
	 * <功能详细描述> 
	 * @param feeTypeId
	 * @param referecenId
	 * @param user
	 * @param vr
	 * @return
	 * @throws HsCloudException 
	 * @see [类、类#方法、类#成员]
	 */
	private Order vmToNormalGenOrder(BigDecimal price,String priceType,String pricePeriodType,String period,
			User user, VpdcReference vr,ServiceCatalog sc,Integer rebateRate,
			Integer giftRebateRate,BigDecimal actuallyPoints,BigDecimal actuallyGifts) throws HsCloudException {
		try {

			List<OrderItemVo> orderItems = new ArrayList<OrderItemVo>();
			//套餐对应操作系统列表
			List<Os> osList = new ArrayList<Os>();
			//套餐对应扩展盘列表
			List<String> addDiskStrList = new ArrayList<String>();
			OrderItemVo item = new OrderItemVo();
			//订单项中保存套餐名称
			item.setServiceCatalogName(sc.getName());
			//订单项中保存套餐Id
			item.setServiceCatalogId(String.valueOf(sc.getId()));
			//订单项中保存flavorId
			item.setFlavorId(String.valueOf(sc.getFlavorId()));
			//订单项中保存套餐描述
			item.setServiceDesc(sc.getDescription());
			for (ServiceItem si : sc.getItems()) {
				switch (si.getServiceType()) {
				case 1: {
					//订单项中保存cpu信息
					Cpu cpu = (Cpu) si;
					item.setCpu(cpu.getName());//cpu name
					item.setCpuModel(cpu.getModel());//cpu model
					item.setvCpus(String.valueOf(cpu.getCoreNum()));//cpu core number
					break;
				}
				case 2: {
					//订单项中保存内存信息
					Ram ram = (Ram) si;
					item.setMemory(String.valueOf(ram.getSize()));//ram size
					item.setMemoryModel(ram.getModel());//ram model
					break;
				}
				case 3: {
					//订单项中保存硬盘信息
					Disk disk = (Disk) si;
					item.setDisk(String.valueOf(disk.getCapacity()));// disk capacity
					item.setDiskModel(disk.getModel());// disk model
					break;
				}
				case 4: {
					//订单项中保存os信息
					Os os = (Os) si;
					osList.add(os);
					break;
				}
				case 5: {
					//订单项中保存网络信息
					Network network = (Network) si;
					item.setNetwork(String.valueOf(network.getBandWidth()));// network bandwidth
					item.setNetworkType(network.getType());//network type
					break;
				}
				case 6: {
					Software sf = (Software) si;
					item.setSoftware(sf.getName());
					break;
				}
				case 8: {
					//订单项中保存扩展盘信息
					ExtDisk extDisk = (ExtDisk) si;
					addDiskStrList.add(String.valueOf(extDisk.getCapacity()));// extdisk capacity
					break;
				}
				}
			}

			if (osList.size() >= 1) {
				StringBuilder osIdsStr = new StringBuilder();
				for (Os os : osList) {
					if (osList.lastIndexOf(os) == osList.size() - 1) {
						osIdsStr.append(os.getId());
					} else {
						osIdsStr.append(os.getId()).append(",");
					}
					if (os.getId() == vr.getOsId()) {
						item.setOs(os.getName());//保存套餐对应os name
						item.setOsId(String.valueOf(vr.getOsId()));//保存套餐对应os id
						item.setImageId(os.getImageId());//保存套餐对应image
					}
				}
				item.setOsIds(osIdsStr.toString());
			}

			if (addDiskStrList.size() > 0) {
				StringBuilder addDiskStrSB = new StringBuilder();
				for (int i = 0; i < addDiskStrList.size(); i++) {
					if (i == addDiskStrList.size() - 1) {
						addDiskStrSB.append(addDiskStrList.get(i));
					} else {
						addDiskStrSB.append(addDiskStrList.get(i)).append(",");
					}
				}
				item.setAddDisk(addDiskStrSB.toString());
			}
			List<ServerZone> zones=sc.getZoneList();
			String firstZoneCode=zones.get(0).getCode();
			StringBuilder zoneListStr=new StringBuilder(firstZoneCode);
			for(int i=1;i<zones.size();i++){
				zoneListStr.append(",").append(zones.get(i).getCode());
			}
			item.setNodeName(zoneListStr.toString());
			if(actuallyPoints.compareTo(BigDecimal.ZERO)>0&&sc.getUsePointOrNot()){
				item.setUsePointOrNot(true);
			}
			if(actuallyGifts.compareTo(BigDecimal.ZERO)>0&&sc.getUseGiftOrNot()){
				item.setUseGiftOrNot(true);
			}
			//订单项中保存购买的套餐的计费规则
			item.setPrice(price);// 保存套餐价格
			item.setPriceType(priceType);// 价格类型，是周期付费还是一次性付费
			item.setPricePeriodType(pricePeriodType);//
			item.setPricePeriod(period);// 一次性付费的时长，目前按月计算
			orderItems.add(item);
			Order order = orderService.submitOrder(orderItems, user,rebateRate,giftRebateRate);
			return order;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}
	@Override
	public ServiceItem getSIById(int siId) throws HsCloudException {
		return serviceItemService.getSIById(siId);
	}


	@Override
	public boolean existPersonalUser(User user) {

		User u = userService.getPersonalUser(user);
		if(null == u){
			return false;
		}
		return true;

	}

	@Override
	@Transactional
	public String genAlipayPayRequest(Map<String, String> sParaTemp,Long userId,long domainId)
			throws HsCloudException {
		try {
			//获取支付宝支付请求需要的参数
			PaymentInterface config=alipayService.getPaymentInterfaceByDomain(domainId);
			if(config!=null){
				String publishingAddress=config.getDomain().getPublishingAddress();
				sParaTemp.put("service", "create_direct_pay_by_user");
				sParaTemp.put("partner", config.getPartner());
				sParaTemp.put("return_url",publishingAddress+Constants.PAYMENT_RETURN_URL);
				sParaTemp.put("notify_url", publishingAddress+Constants.PAYMENT_NOTIFY_URL);
				sParaTemp.put("seller_email", config.getSellerEmail());
				sParaTemp.put("_input_charset", AlipayConfig.input_charset);
				sParaTemp.put("payment_type",AlipayConfig.payment_type);
				sParaTemp.put("token", AlipayConfig.token);
				sParaTemp.put("subject",AlipayConfig.subject);
				sParaTemp.put("out_trade_no", UtilDate.getOrderNum());
			}else{
				throw new HsCloudException("Can't get paymentInterface by domaindId", logger);
			}
			//根据企业用户Id获取其账户信息
			Account account=accountService.getAccountByUserId(userId);
			Long accountId=account.getId();
			//交易金额
			String tradeAmountStr=sParaTemp.get("total_fee");
			//交易产生的交易流水号
			String serialNumber=sParaTemp.get("out_trade_no");
			//交易详细描述
			String body=sParaTemp.get("body");
			//根据交易参数生成请求字符串
			String requestStr = alipayService.genRequestStrForPay(sParaTemp,config.getKey());
			BigDecimal tradeAmount=new BigDecimal(tradeAmountStr);
			//生成状态为已创建的交易日志
			PaymentLog pl=new PaymentLog();
			//交易时间
			pl.setTradeTime(new Date());
			//账户Id
			pl.setAccountId(accountId);
			//用户Id
			pl.setUserId(userId);
			pl.setDomainId(domainId);
			//交易流水号
			pl.setSerialNumber(serialNumber);
			//交易金额
			pl.setTradeAmount(tradeAmount);
			//交易详细描述
			pl.setDescription(body);
			//交易状态，为已创建
			pl.setTradeStatus(TradeStatus.TRADE_CREATE.getIndex());
			//交易类型，为充值
			pl.setTradeType(TradeType.RECHARGE.getIndex());
			//支付提供商为支付宝
			pl.setVendor(PaymentVendor.ALIPAY.getVendorName());
			paymentLogService.savePaymentLog(pl);
			return requestStr;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	@Transactional
	public boolean verifyAlipayResponse(Map<String, String> params)
			throws HsCloudException {
		try {
			boolean result = false;
			// 获取支付宝返回的交易流水号
			String serialNum = params.get("out_trade_no");
			// 根据交易流水号和交易状态（已创建）获取交易日志信息
			PaymentLog createLog = paymentLogService.searchPaymentLog(
					serialNum, TradeStatus.TRADE_CREATE.getIndex());
			if (createLog != null) {
				Long domainId = createLog.getDomainId();
				PaymentInterface config = alipayService
						.getPaymentInterfaceByDomain(domainId);
				if (config != null) {
					String key = config.getKey();
					String partner = config.getPartner();
					// 获取支付宝同步响应返回的字符串，服务层会对结果进行校验，校验通过才会返回参数，否则返回null
					Map<String, String> resultParams = alipayService
							.verifyPayResponse(params, key, partner);
					// 服务层对返回数据校验通过
					if (resultParams != null && resultParams.size() > 0) {
						// 根据交易流水号和交易状态（已完成）获取交易日志信息
						PaymentLog finishedLog = paymentLogService
								.searchPaymentLog(serialNum,
										TradeStatus.TRADE_FINISHED.getIndex());
						// 如果交易已经创建但是还没有完成则进行完成操作的日志插入
						if (finishedLog == null) {
							String tradeAmountStr = resultParams
									.get("total_fee");
							String serialNumber = resultParams
									.get("out_trade_no");
							String body = resultParams.get("body");
							BigDecimal tradeAmount = new BigDecimal(
									tradeAmountStr);
							PaymentLog pl = new PaymentLog();
							pl.setTradeTime(new Date());
							pl.setAccountId(createLog.getAccountId());
							pl.setUserId(createLog.getUserId());
							pl.setSerialNumber(serialNumber);
							pl.setTradeAmount(tradeAmount);
							pl.setDescription(body);
							pl.setDomainId(domainId);
							pl.setTradeStatus(TradeStatus.TRADE_FINISHED
									.getIndex());
							pl.setTradeType(TradeType.RECHARGE.getIndex());
							pl.setVendor(PaymentVendor.ALIPAY.getVendorName());
							StringBuilder transactionDesc = new StringBuilder();
							transactionDesc.append(body).append(",交易流水号为：")
							.append(serialNumber);
							paymentLogService.savePaymentLog(pl);
							accountService.accountDeposit(
									createLog.getUserId(),(short)1,
									PaymentType.PAYMENT_ONLINE.getIndex(),
									createLog.getAccountId(), tradeAmount,
									transactionDesc.toString(),
									transactionDesc.toString(),
									DepositSource.APLIPAY.getIndex(),
									(short)1
									);
							invoiceAccountService.deposit(
									createLog.getUserId(), tradeAmount);
							result = true;
						} else if (finishedLog != null) {
							// 由于异步响应和同步响应那个更快不确定，不管那个把交易置为完成都返回true
							// 如果交易已经完成则返回true
							if (finishedLog.getTradeStatus() == TradeStatus.TRADE_FINISHED
									.getIndex()) {
								result = true;
							}
						}
					}
				}
			}
			return result;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	@Transactional
	public boolean verifyAlipayAsynResponse(Map<String, String> params)
			throws HsCloudException {
		try {
			boolean result = false;
			// 获取支付宝返回的交易流水号
			String serialNum = params.get("out_trade_no");
			// 根据交易流水号和交易状态（已创建）获取交易日志信息
			PaymentLog createLog = paymentLogService.searchPaymentLog(
					serialNum, TradeStatus.TRADE_CREATE.getIndex());
			if (createLog != null) {
				Long domainId = createLog.getDomainId();
				PaymentInterface config = alipayService
						.getPaymentInterfaceByDomain(domainId);
				if (config != null) {
					String key = config.getKey();
					String partner = config.getPartner();
					// 获取支付宝同步响应返回的字符串，服务层会对结果进行校验，校验通过才会返回参数，否则返回null
					Map<String, String> resultParams = alipayService
							.verifyPayResponse(params, key, partner);
					// 服务层对返回数据校验通过
					if (resultParams != null && resultParams.size() > 0) {
						// 根据交易流水号和交易状态（已完成）获取交易日志信息
						PaymentLog finishedLog = paymentLogService
								.searchPaymentLog(serialNum,
										TradeStatus.TRADE_FINISHED.getIndex());
						// 如果交易已经创建但是还没有完成则进行完成操作的日志插入
						if (createLog != null && finishedLog == null) {

							String tradeAmountStr = resultParams
									.get("total_fee");
							String serialNumber = resultParams
									.get("out_trade_no");
							String body = resultParams.get("body");
							BigDecimal tradeAmount = new BigDecimal(
									tradeAmountStr);
							PaymentLog pl = new PaymentLog();
							pl.setTradeTime(new Date());
							pl.setAccountId(createLog.getAccountId());
							pl.setUserId(createLog.getUserId());
							pl.setSerialNumber(serialNumber);
							pl.setTradeAmount(tradeAmount);
							pl.setDescription(body);
							pl.setDomainId(domainId);
							pl.setTradeStatus(TradeStatus.TRADE_FINISHED
									.getIndex());
							pl.setTradeType(TradeType.RECHARGE.getIndex());
							pl.setVendor(PaymentVendor.ALIPAY.getVendorName());
							// 生成支付日志
							paymentLogService.savePaymentLog(pl);
							Date firstTradeTime=createLog.getTradeTime();
							Date firstTradeTimeAddOneHour=DateUtils.addHours(firstTradeTime, 1);
							Date now=new Date();
							if(now.before(firstTradeTimeAddOneHour)){
								StringBuilder transactionDesc = new StringBuilder();
								transactionDesc.append(body).append(",交易流水号为：")
								.append(serialNumber);
								// 交易成功，更新用户账户余额生成交易日志
								accountService.accountDeposit(
										createLog.getUserId(),(short)1,
										PaymentType.PAYMENT_ONLINE.getIndex(),
										createLog.getAccountId(), tradeAmount,
										transactionDesc.toString(),
										transactionDesc.toString(),
										DepositSource.APLIPAY.getIndex(),(short)1);
								invoiceAccountService.deposit(
										createLog.getUserId(), tradeAmount);
							}
							result = true;
						} else if (createLog != null && finishedLog != null) {
							// 由于异步响应和同步响应那个更快不确定，不管那个把交易置为完成都返回true
							// 如果交易已经完成则返回true
							if (finishedLog.getTradeStatus() == TradeStatus.TRADE_FINISHED
									.getIndex()) {
								result = true;
							}
						}

					}
				}
			}

			return result;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}


	@Override
	@Transactional
	public void companyInvite(long senderId, String email) {

		User user = userService.getUserByEmail(email);
		Company company = companyService.getCompanyByUserId(senderId);
		CompanyInvite companyInvite = new CompanyInvite();
		companyInvite.setCompanyName(company.getName());
		companyInvite.setCreateId(senderId);
		companyInvite.setReceiverId(user.getId());
		companyInvite.setSenderId(senderId);
		companyInvite.setCompanyId(company.getId());
		companyInvite.setStatus(InviteState.INVITE_UNTREATED.getIndex());
		companyInviteService.invite(companyInvite);
		Message message = new Message();
		message.setMessageType(5);
		message.setStatus(com.hisoft.hscloud.message.util.Constant.MESSAGE_STATUS_UNREAD);
		message.setUserId(user.getId());
		message.setMessage(company.getName()+"邀请您加入。");
		messageService.saveMessage(message);

	}

	@Override
	@Transactional
	public void rejectedInvite(String inviteId) {

		CompanyInvite companyInvite = companyInviteService.getInviteById(Long.valueOf(inviteId));
		companyInvite.setStatus(InviteState.INVITE_REJECTED.getIndex());

	}

	@Override
	@Transactional
	public void acceptedInvite(String inviteId) {

		companyInviteService.acceptedInvite(Long.valueOf(inviteId));

	}

	@Override
	@Transactional
	public void entTerminateInvite(long senderId,long receiverId) {

		CompanyInvite companyInvite = companyInviteService.getInvite(senderId,receiverId,InviteState.INVITE_ACCEPTED.getIndex());
		companyInviteService.terminateInvite(companyInvite.getId());

	}

	@Override
	@Transactional
	public void userTerminateInvite(String inviteId) {
		companyInviteService.terminateInvite(Long.valueOf(inviteId));
	}

	@Override
	public List<CompanyInviteVO> getInvite(long userId) {
		List<CompanyInvite> CompanyInvites =companyInviteService.getInvitesByReceiverId(userId);
		List<CompanyInviteVO> CompanyInviteVOs = new ArrayList<CompanyInviteVO>();
		for (CompanyInvite companyInvite : CompanyInvites) {
			CompanyInviteVO companyInviteVO = new CompanyInviteVO();
			companyInviteVO.setId(companyInvite.getId());
			companyInviteVO.setCreateId(companyInvite.getCreateId());
			companyInviteVO.setCreateDate(companyInvite.getCreateDate());
			companyInviteVO.setUpdateId(companyInvite.getUpdateId());
			companyInviteVO.setUpdateDate(companyInvite.getUpdateDate());
			companyInviteVO.setSenderId(companyInvite.getSenderId());
			companyInviteVO.setReceiverId(companyInvite.getReceiverId());
			companyInviteVO.setStatus(companyInvite.getStatus());
			Company company = companyService.getCompany(companyInvite.getCompanyId());
			companyInviteVO.setCompanyName(company.getName());
			CompanyInviteVOs.add(companyInviteVO);
		}
		return CompanyInviteVOs;
	}

	@Override
	public UserBrand getBrandByCode(String code) throws HsCloudException {
		return userBrandService.getBrandByCode(code);
	}

	@Override
	public List<ScFeeTypeVo> getScFeeTypeByScId(int scId) throws HsCloudException {
		return serviceCatalogService.getScFeeTypeByScId(scId);
	}
	@Override
	/**
	 * 按照套餐的配置创建虚拟机
	 * @param scId
	 * @param osId
	 * @param user
	 * @return
	 * @throws HsCloudException
	 */
	public CreateVmBean generateVmBeanBySc(ServiceCatalog sc, int osId,
			User user) throws HsCloudException {
		try {
			CreateVmBean createVmBean = new CreateVmBean();
			List<Os> osList=new ArrayList<Os>();
			//List<Long> osIds = new ArrayList<Long>();
			List<String> addDiskStrList=new ArrayList<String>();
			createVmBean.setFlavorId(String.valueOf(sc.getFlavorId()));
			List<ServerZone> zones=sc.getZoneList();
			String firstZoneCode=zones.get(0).getCode();
			StringBuilder zoneListStr=new StringBuilder(firstZoneCode);
			for(int i=1;i<zones.size();i++){
				zoneListStr.append(",").append(zones.get(i).getCode());
			}
			createVmBean.setVmZone(zoneListStr.toString());
			for(ServiceItem si :sc.getItems()){
				switch(si.getServiceType()){
				case 1:{
					Cpu cpu=(Cpu)si;
					createVmBean.setVcpusType(cpu.getModel());
					createVmBean.setVcpus(String.valueOf(cpu.getCoreNum()));
					break;
				}
				case 2:{
					Ram ram=(Ram)si;
					createVmBean.setRam(String.valueOf(ram.getSize()));
					createVmBean.setRamType(ram.getModel());
					break;
				}
				case 3:{
					Disk disk=(Disk)si;
					createVmBean.setDisk(String.valueOf(disk.getCapacity()));
					createVmBean.setDiskType(disk.getModel());
					break;
				}
				case 4:{
					Os os=(Os)si;
					osList.add(os);
					break;
				}
				case 5:{
					Network network=(Network)si;
					createVmBean.setNetwork(String.valueOf(network.getBandWidth()));
					createVmBean.setNetworkType(network.getType());
					break;
				}
				case 8:{
					ExtDisk extDisk=(ExtDisk)si;
					addDiskStrList.add(String.valueOf(extDisk.getCapacity()));
					break;
				}
				}
			}

			if(osList.size()>=1){
				//StringBuilder osIdsStr = new StringBuilder();
				for(Os os:osList){
					//osIds.add(new Long(os.getId()));
					if(os.getId()==osId){
						createVmBean.setOsId(String.valueOf(osId));
						createVmBean.setImageId(os.getImageId());
					}
				}
				//createVmBean.setOsIds(osIds);
			}

			if(addDiskStrList.size()>0){
				StringBuilder addDiskStrSB = new StringBuilder();
				for(int i=0;i<addDiskStrList.size();i++){
					if(i==addDiskStrList.size()-1){
						addDiskStrSB.append(addDiskStrList.get(i));
					}else{
						addDiskStrSB.append(addDiskStrList.get(i)).append(",");
					}
				}
				createVmBean.setAddDisk(addDiskStrSB.toString());
			}
			createVmBean.setTryLong(sc.getTryDuration());
			createVmBean.setScId(sc.getId());
			createVmBean.setOwner(String.valueOf(user.getId()));
			ScIsolationConfig isolationConfig=sc.getScIsolationConfig();
			if(isolationConfig!=null){
				createVmBean.setCpuLimit(isolationConfig.getCpuLimit());
				createVmBean.setDiskRead(isolationConfig.getDiskRead());
				createVmBean.setDiskWrite(isolationConfig.getDiskWrite());
				createVmBean.setBwtIn(isolationConfig.getBandWidthDown());
				createVmBean.setBwtOut(isolationConfig.getBandWidthUp());
				createVmBean.setTcpConnIn(isolationConfig.getTcpConnectionDown());
				createVmBean.setTcpConnOut(isolationConfig.getTcpConnectionUp());
				createVmBean.setUdpConnIn(isolationConfig.getUdpConnectionDown());
				createVmBean.setUdpConnOut(isolationConfig.getUdpConnectionUp());
				createVmBean.setIpConnIn(isolationConfig.getIpConnectionDown());
				createVmBean.setIpConnOut(isolationConfig.getIpConnectionUp());
			}
			return createVmBean;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public String getVMStateByOrderItemId(long orderItemId) {
		String result ="";
		try {
			// 根据虚拟机Id查询orderItemId与referenceId关联实体bean
			//目前只获取status=0的reference
			VpdcReference vr=operation.getReferenceByOrderItemId(orderItemId);
			if(vr==null){
				result="NotExsit";
			}else{
				if(vr.getVm_type().intValue()==0||vr.getVm_type()==null){
					result="Try";
				}else if(vr.getVm_type().intValue()==1){
					result="Normal";
				}
			}
			return result;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	@Transactional
	public void submitTryVm(int scId, int osId, User user) {
		try {
			ServiceCatalog sc = serviceCatalogService.get(scId);
			if (!sc.getTryOrNo()) {
				throw new HsCloudException("", "", logger);
			}
			CreateVmBean vmBean = generateVmBeanBySc(sc, osId, user);
			String zone = vmBean.getVmZone();
			String firstName= operation.getDomainIdByUserId(user.getId());
			String serialNum= operation.getVMNameSerialNumber(Constants.T_VM_ARRANGING_NAME);
			vmBean.setName(firstName+serialNum);
			vmBean.setBuyType(0);
			/*ServerZone sz = null;
			if(!StringUtils.isEmpty(zone)){
				sz = serverZoneService.getServerZoneByCode(zone);
			}else{
				sz = serverZoneService.getDefaultServerZone();
			}*/
			if (sc.getTryApproveOrNo()) {
				vmBean.setVmBussiness(VMTryStatusBussEnum.TRY.getCode());
				vmBean.setVmType(VMTypeEnum.TRY.getCode());
				List<CreateVmBean> createVmBeans = new ArrayList<CreateVmBean>();
				/*String ip = iPService.assignIPDetail(sz.getId());
				if (StringUtils.isEmpty(ip)) {
					logger.error("NO IP enough!");
					throw new HsCloudException(Constants.VM_IP_NO_ENOUGH,
							"submitTryVm 异常", logger, null);
				}
				vmBean.setFloating_ip(ip);*/
				createVmBeans.add(vmBean);
				Map<NovaServerForCreate,HcEventResource> rabbitMQ = operation.createVm(createVmBeans, String.valueOf(user.getId()),
						zone);
				RabbitMQUtil rmu = new RabbitMQUtil();
				for(NovaServerForCreate nsfc:rabbitMQ.keySet()){
					HcEventResource her = rabbitMQ.get(nsfc);
					rmu.sendMessage(her.getMessage(), nsfc,her,"HcEventResource");
				}
			} else {
				operation.applyForTryVm(vmBean, user.getId(), zone);
			}
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public List<ScFeeTypeVo> getScFeeTypeByReferenceId(long referenceId)
			throws HsCloudException {
		VpdcReference_OrderItem vroi = operation.getOrderItemByReferenceId(referenceId);
		return serviceCatalogService.getScFeeTypeByOrderItemId(Long.valueOf(vroi.getOrder_item_id()));
	}
	@Override
	public List<OrderVo> getVmRelatedPaidOrder(long referenceId)
			throws HsCloudException {
		return orderService.getVmRelatedPaidOrder(referenceId);
	}

	@Override
	public Domain getDomainByDomainId(long domainId) throws HsCloudException {
		return domainService.getDomainById(domainId);
	}

	@Override
	public void resetSystemPwd(String vmId, String password,Object o,String otype) {
		operation.resetSystemPwd(vmId, password,o,otype);
	}

	@Override
	public Page<VpdcRenewal> findVpdcRenewal(Page<VpdcRenewal> page,
			String field, Object fieldValue, String query, long userId)
					throws HsCloudException {
		return vpdcRenewalService.findVpdcRenewal(page, field, fieldValue, query, userId);
	}

	@Override
	@Transactional
	public void submitRefundApply(User user, String uuid,
			String applyReason,Short refundReasonType)
					throws HsCloudException {
		VpdcReference vr=operation.getReferenceByVmId(uuid);
		VpdcReference_Period vrp=operation.getReferencePeriod(vr.getId());
		VmRefundLog vrl=new VmRefundLog();
		vrl.setApplyDate(new Date());
		vrl.setApplyRefundReason(applyReason);
		vrl.setOpenDate(vrp.getStartTime());
		vrl.setExpirationDate(vrp.getEndTime());
		String orderNo=orderService.getVmCurrentOrderNo(uuid);
		if(null==orderNo||"".equals(orderNo)){//获取未生效的订单号
			orderNo=orderService.getVmNoteffectiveOrderNo(uuid);
		}
		vrl.setOrderNo(orderNo);
		vrl.setRefundReasonType(refundReasonType);
		vrl.setOperator(user.getEmail());
		vrl.setOwnerEmail(user.getEmail());
		vrl.setOwnerId(user.getId());
		vrl.setReferenceId(vr.getId());
		vrl.setStatus((short)1);
		vrl.setUuid(uuid);
		vrl.setUpdateDate(new Date());
		vmRefundLogService.saveVmRefundLog(vrl);	
		operation.closeVm(uuid,user,LogOperatorType.PROCESS.getName());
	}

	/**
	 * <用于处理:  申请退款-->取消退款-->再次申请退款时的 再次申请退款操作>
	 */
	@Override
	@Transactional
	public void submitRefundApplyOnceAgain(String uuid, Short refundReasonType,
			String applyReason) throws HsCloudException {
		// 更新时根据uuid去判断是哪条记录,因为uuid字段是unique,需要更新的字段:
		// refundReasonType,applyReason,status更新为1,applyDate和updateDate
		VpdcReference vr=operation.getReferenceByVmId(uuid);
		String referencedStr=String.valueOf(vr.getId())+"@"+uuid;
		vmRefundLogService.updateVmRefundLog(referencedStr,refundReasonType,applyReason);	  
	}
	/**
	 * <根据虚拟机uuid判断云主机是否为申请中> 
	 * <功能详细描述> 
	 * @author liyunhui
	 * @param uuid
	 * @return
	 * @throws HsCloudException 
	 * @see [类、类#方法、类#成员]
	 */
	@Override
	public boolean isVMApplyingRefundByUuid(String uuid) throws HsCloudException {
		// 根据虚拟机uuid判断云主机是否为申请中
	    VpdcReference vr=operation.getReferenceByVmId(uuid);
	    String referenceId=String.valueOf(vr.getId());
		VmRefundLog vrl=vmRefundLogService.isVMApplyingRefundByUuid(referenceId);
		if(vrl==null){
			return false;
		}else{
			return true;
		}		
	}
	/**
	 * <根据虚拟机uuid来判断它的status是否为3.拒绝 4.已取消> 
	 * <功能详细描述> 
	 * @author liyunhui
	 * @param uuid
	 * @return
	 * @throws HsCloudException 
	 * @see [类、类#方法、类#成员]
	 */
	@Override
	public boolean isExistedVMStatusEquals3Or4ByUuid(String uuid) throws HsCloudException {
	    VpdcReference vr=operation.getReferenceByVmId(uuid);
	    String referenceId=String.valueOf(vr.getId());
		VmRefundLog vrl=vmRefundLogService.isExistedVMStatusEquals3Or4ByUuid(referenceId);
		if(vrl==null){
			return false;
		}else{
			return true;
		}		
	}

	@Override
	public boolean isVmDisabledByUUID(String uuid) throws HsCloudException {
		VpdcReference vr = operation.getReferenceByVmId(uuid);
		boolean flag = false;
		// 0:正常；1：手动禁用；2：到期禁用
		if (vr.getIsEnable() != 0) {
			flag = true;
		}
		return flag;
	}
	@Override
	public List<ZoneGroup> getAllZoneGroups() throws HsCloudException {
		return zoneGroupService.getAllZoneGroups();
	}
	/**
	 * <查询关于虚拟机操作的日志> 
	 * <功能详细描述> 
	 * @author liyunhui
	 * @throws HsCloudException 
	 * @see [类、类#方法、类#成员]
	 */
	@Override
	public Page<HcEventVmOps> getVmOpsLog(Page<HcEventVmOps> paing,LogQueryVO param)
			throws HsCloudException {
		return vmOpsLogService.getVmOperationLog(paing,param);
	}

	@Override
	public Page<VmRefundLogVO> getVmRefundLogByPage(Page<VmRefundLogVO> paging,
			String query, Long userId, Short status) throws HsCloudException {
		return vmRefundLogService.vmRefundLogPaging4Website(status, query, paging,userId);
	}

	@Override
	@Transactional
	public void cancelRefundApply(Long vmRefundId) throws Exception {
		VmRefundLog vrl=vmRefundLogService.getVmRefundLogById(vmRefundId); 
		vrl.setStatus((short)4);
	}

	/**
	 * <用途: 获取当前用户的当前instanceId这台虚拟机的同一组下面所有已经添加内网安全的虚拟机
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	@Override
	public List<VmIntranetSecurityVO> findIntranetVmsByUuid(String uuid)
			throws HsCloudException {
		logger.info("OPS-Facade-findIntranetVmsByUuid start");
		List<VmIntranetSecurityVO> vis_list = null;
		try {
			vis_list = operation.findIntranetVmsByUuid(uuid);
		} catch (HsCloudException e) {
			throw new HsCloudException(Constants.VM_LIST_USER_ERROR,
					"findIntranetVmsByUuid异常", logger, e);
		}
		return vis_list;
	}

	/**
	 * <用途: 提交内网安全和外网安全的数据 >
	 * @param SecurityPolicyVO:安全策略封装的VO
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	@Transactional
	@Override
	public boolean submitSecurityPolicy(SecurityPolicyVO spVO) throws HsCloudException {
		logger.info("OPS-Facade-submitSecurityPolicy start");
		try {
			boolean result = false;
			result = operation.submitSecurityPolicy(spVO);
			return result;
		} catch (Exception e) {
			throw new HsCloudException("submitSecurityPolicy异常", logger, e);
		}
	}

	@Override
	public List<ZoneGroupVO> getAllZoneGroupByUser(String brandCode,Long domainId)
			throws HsCloudException {
		return zoneGroupService.getAllZoneGroupByUser(brandCode, domainId);
	}

	@Override
	@Transactional
	public void saveLogByLoginWithoutPass(MaintenanceLog maintenanceLog)
			throws HsCloudException {
		maintenanceLogService.saveMaintenanceLog(maintenanceLog);
	}

	@Override
	@Transactional
	public void insertOperationLog(User user, String description, String actionName, 
			short operationResult, String operateObject) throws HsCloudException {
		if(user!=null){
			OperationLog log=new OperationLog();
			log.setActionName(actionName);
			log.setDescription(description);
			log.setUserName(user.getName());
			log.setDomainName(user.getDomain().getAbbreviation());
			log.setOperationDate(new Date());
			log.setOperationResult(operationResult);
			log.setOperator(user.getEmail());
			log.setOperatorType(LogOperatorType.USER.getIndex());
			log.setOperateObject(operateObject);
			logService.insertOperationLog(log);
		}
	}	

	@Transactional
	public boolean repairOSByVmId(String vmId,Object o,String otype) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter repairOSByVmId method.");			
		}
		boolean bl = false;
		bl = operation.repairOS(vmId,o,otype);
		if(logger.isDebugEnabled()){
			logger.debug("exit repairOSByVmId method.");
		}
		return bl;
	}

	@Override
	public UserVO1 getUserVO(long userId) {
		UserVO1 userVO1 =  userService.findUserVO1(userId);
		ProfileVo profileVo = userProfileService.findProfileVo(userVO1.getProfileId());
		userVO1.setProfileVo(profileVo);
		return userVO1;
	}

	@Override
	@Transactional
	public void modify(User u) {
		User user = userService.getUserByEmail(u.getEmail());
		UserProfile userProfile = user.getUserProfile();
		UserProfile profileVO = u.getUserProfile();
		user.setName(u.getName());
		if (null != profileVO) {
			if (null == userProfile) {
				userProfile = new UserProfile();
			}
			userProfile.setCompany(profileVO.getCompany());
			userProfile.setTelephone(profileVO.getTelephone());
			userProfile.setAddress(profileVO.getAddress());
			userProfile.setIdCard(profileVO.getIdCard());
			userProfile.setCountry(profileVO.getCountry());
			userProfile.setRegion(profileVO.getRegion());
			userProfile.setIndustry(profileVO.getIndustry());
			userProfile.setUpdateDate(new Date());
			userProfile.setUpdateId(u.getId());
			if(null != profileVO.getSex()){
				userProfile.setSex(profileVO.getSex());
			}
		}
		user.setUserProfile(userProfile);

	}

	@Override
	public void modifyAllMessageStatus(Long id) throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter the method modifyAllMessageStatus");
			messageService.modifyAllMessageStatus(id);
		}
	}

	@Override
	public void deleteAllMessage(Long id) throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter the method deleteAllMessage");
			messageService.deleteAllMessage(id);
		}
	}

	@Override
	@Transactional
	public void submitOrder4Need(SubmitOrderData submitData)
			throws HsCloudException {
		try {
			int vmNum = submitData.getVmNum();
			User user = submitData.getUser();
			ZoneGroup zoneGroup = zoneGroupService.getZoneGroupById(submitData.getZoneGroupId());
			OrderItem item = generateOrder4Need(submitData);
			//			OrderUtils.getPriceForOrderItemFrom3P(item,submitData.getUser().getDomain().getCode());
			OrderUtils.getPriceForOrderItemFrom3P(item, submitData.getUser().getDomain().getCode(), user.getLevel(), zoneGroup.getCode());
			List<OrderItem> items = new ArrayList<OrderItem>();
			items.add(item);
			for (int i = 0; i < vmNum - 1; i++) {
				OrderItem itemNew = new OrderItem();
				itemNew.copyItem(item, 2);
				items.add(itemNew);
			}			
			Order order = new Order();
			order.submit(items, user, "Unpaid",getRebate(user),getGiftRebate(user));
			order.setOrderType((short)2);
			orderService.saveOrder(order);
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public void submitRouterOrder(SubmitOrderData submitData) throws HsCloudException {
		try {
			OrderItem item = generateRouterOrder4Need(submitData);
			ZoneGroup zg=zoneGroupService.getZoneGroupById(submitData.getZoneGroupId());
			UserBrand ub=userBrandService.getBrandByCode(submitData.getUser().getLevel());
			OrderUtils.getPriceForOrderItemFrom3P(item,submitData.getUser().getDomain().getCode(),ub.getCode(),zg.getCode());
			List<OrderItem> items = new ArrayList<OrderItem>();
			items.add(item);
			User user = submitData.getUser();
			Order order = new Order();
			order.submit(items, user, "Unpaid",getRebate(user),getGiftRebate(user)
					);
			order.setOrderType((short) 3);//3.路由按需购买
			orderService.saveOrder(order);
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	@Transactional
	public void submitVMOrder(List<SubmitOrderData> vmCart, int vpdcId) throws HsCloudException {
		try {
			List<OrderItem> items = new ArrayList<OrderItem>();
			for (SubmitOrderData submitData : vmCart) {
				OrderItem item = generateOrder4Need(submitData);
				item.setVpdcId((long) vpdcId);
				ZoneGroup zg=zoneGroupService.getZoneGroupById(submitData.getZoneGroupId());
				UserBrand ub=userBrandService.getBrandByCode(submitData.getUser().getLevel());
				OrderUtils.getPriceForOrderItemFrom3P(item,submitData.getUser().getDomain().getCode(),ub.getCode(),zg.getCode());
				items.add(item);
			}
			User user = vmCart.get(0).getUser();
			Order order = new Order();
			order.submit(items, user, "Unpaid",getRebate(user),getGiftRebate(user));
			order.setOrderType((short) 2);
			orderService.saveOrder(order);
		}
		catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	private OrderItem generateOrder4Need(SubmitOrderData submitData)
			throws Exception {
		List<OrderProduct> products = new ArrayList<OrderProduct>();
		FlavorVO flavorVO = new FlavorVO();
		OrderProduct diskProduct=new OrderProduct();
		String imageRef="";
		for (int i = 0; i < submitData.getServiceItemIds().length; i++) {
			ServiceItem si = serviceItemService.getSIById(submitData.getServiceItemIds()[i]);
			int serviceType = si.getServiceType();
			OrderProduct product = new OrderProduct();
			String name = "";
			String spec = "";
			String unit = "";
			String model = "";
			String extColumn="";
			BigDecimal price = BigDecimal.ZERO;
			switch (serviceType) {
			case 1: {
				Cpu cpu = (Cpu) si;
				name = cpu.getName();
				model = cpu.getModel();
				//				price=cpu.getPrice();
				spec = String.valueOf(cpu.getCoreNum());
				flavorVO.setVcpus(cpu.getCoreNum());

				unit = "core";
				break;
			}
			case 2: {
				Ram ram = (Ram) si;
				name = ram.getName();
				//				price=ram.getPrice();
				model = ram.getModel();
				spec = String.valueOf(ram.getSize());
				flavorVO.setRam(ram.getSize());
				unit = "M";
				break;
			}
			//			case 3: {
			//				Disk disk = (Disk) si;
			//				name = disk.getName();
			//				type=3;
			//				price=disk.getPrice();
			//				model = disk.getModel();
			//				spec = String.valueOf(disk.getCapacity());
			//				//addDisk+=(spec+",");
			//				flavorVO.setDisk(disk.getCapacity());
			//				unit = "G";
			//				break;
			//			}
			case 4: {
				Os os = (Os) si;
				name = os.getName();
				model = os.getArch();
				//				price=os.getPrice();
				extColumn=String.valueOf(os.getId());
				spec = os.getImageId();
				List<ImageVO> images = cache.getImages();
				for (ImageVO imageVO : images) {
					if(imageVO.getId().equals(spec)){
						diskProduct.setProductName(imageVO.getName());
						diskProduct.setType(3);
						diskProduct.setModel(imageVO.getMetadata().get("default_size"));
						diskProduct.setSpec(imageVO.getMetadata().get("default_size"));
						flavorVO.setDisk(Integer.parseInt(imageVO.getMetadata().get("default_size")));
					}
				}
				imageRef=spec;
				break;
			}
			case 5: {
				Network network = (Network) si;
				name = network.getName();
				model = network.getType();
				//				price=network.getPrice();
				spec = String.valueOf(network.getBandWidth());
				unit = "M";
				break;
			}
			case 8: {
				ExtDisk extDisk = (ExtDisk) si;
				name = extDisk.getName();
				model = extDisk.getModel();
				//				price=extDisk.getPrice();
				spec = String.valueOf(extDisk.getCapacity());
				//addDisk+=(spec+",");
				unit = "G";
				break;
			}
			}
			product.setModel(model);
			product.setPrice(price);
			product.setExtColumn(extColumn);
			product.setProductName(name);
			product.setSpec(spec);
			product.setType(serviceType);
			product.setUnit(unit);
			products.add(product);
		}
		OrderProduct ip=new OrderProduct();
		ip.setProductName("IP");
		ip.setSpec(String.valueOf(submitData.getIpNum()));
		ip.setType(9);
		products.add(ip);
		products.add(diskProduct);
		OrderItem item=new OrderItem();
		//item.setUsePointOrNot(true);
		item.setPlanId(submitData.getPlanId());
		item.setPricePeriod(String.valueOf(submitData.getBuyPeriod()));
		item.setPricePeriodType("1");
		item.setPriceType("1");
		item.setQuantity(1);
		List<ServerZone> zoneList=serverZoneService.getCustomZonesByGroupId(submitData.getZoneGroupId());
		if(zoneList!=null&&zoneList.size()>0){
			StringBuilder zonesStr=new StringBuilder(zoneList.get(0).getCode());
			for(int i=1;i<zoneList.size();i++){
				zonesStr.append(",").append(zoneList.get(i).getCode());
			}
			item.setNodeName(zonesStr.toString());
		}
		item.setImageId(imageRef);
		String flavorId=operation.createFlavor(flavorVO);
		item.setFlavorId(flavorId);
		item.setOrderProducts(products);
		return item;
	}

	private OrderItem generateRouterOrder4Need(SubmitOrderData submitData) throws Exception {
		// 带宽id通过前台传过来,CPU 内存 操作系统 这些的id通过后台接口查找数值获得id传过去
		int[] serviceItemIds = new int[5];
		serviceItemIds[0] = submitData.getServiceItemIds()[0];//取前台的带宽的id
		VrouterTemplateVO vtVO = vrouterService.getVrouterTemplate();
		serviceItemIds[1] = vtVO.getOsId();//操作系统的id
		ServiceItem cpuItem = serviceItemService.getSIBySize(vtVO.getCpuCore(), "cpu");//CPU的id
		Cpu cpuData = (Cpu) cpuItem;
		serviceItemIds[2] = cpuData.getId();
		ServiceItem memeoryItem = serviceItemService.getSIBySize(vtVO.getMemSize(), "ram");//内存的id
		Ram ramData = (Ram) memeoryItem;
		serviceItemIds[3] = ramData.getId();
		ServiceItem diskItem = serviceItemService.getSIBySize(vtVO.getDiskCapacity(), "disk");//disk的id
		Disk diskData = (Disk) diskItem;
		serviceItemIds[4] = diskData.getId();
		submitData.setServiceItemIds(serviceItemIds);

		List<OrderProduct> products = new ArrayList<OrderProduct>();
		FlavorVO flavorVO = new FlavorVO();
		String imageRef = "";
		for (int i = 0; i < submitData.getServiceItemIds().length; i++) {
			ServiceItem si = serviceItemService.getSIById(submitData.getServiceItemIds()[i]);
			int serviceType = si.getServiceType();
			OrderProduct product = new OrderProduct();
			String name = "";
			String spec = "";
			String unit = "";
			String model = "";
			String extColumn = "";
			int type = 0;
			BigDecimal price = BigDecimal.ZERO;
			switch (serviceType) {
			case 1: {
				Cpu cpu = (Cpu) si;
				name = cpu.getName();
				model = cpu.getModel();
				type = 1;
				//					price = cpu.getPrice();
				spec = String.valueOf(cpu.getCoreNum());
				flavorVO.setVcpus(cpu.getCoreNum());
				unit = "core";
				break;
			}
			case 2: {
				Ram ram = (Ram) si;
				name = ram.getName();
				type = 2;
				//					price = ram.getPrice();
				model = ram.getModel();
				spec = String.valueOf(ram.getSize());
				flavorVO.setRam(ram.getSize());
				unit = "M";
				break;
			}
			case 3: {
				Disk disk = (Disk) si;
				name = disk.getName();
				type = 3;
				//					price = disk.getPrice();
				model = disk.getModel();
				spec = String.valueOf(disk.getCapacity());
				// addDisk+=(spec+",");
				flavorVO.setDisk(disk.getCapacity());
				unit = "G";
				break;
			}
			case 9: {//注意：service_item这张表的serviceType为4代表虚拟机的操作系统,9代表路由的操作系统
				Os os = (Os) si;
				name = os.getName();
				model = os.getArch();
				type = 4;//但是在order_product这张表里面虚拟机的操作系统和路由的操作系统都是type为4
				//					price = os.getPrice();
				extColumn = String.valueOf(os.getId());
				spec = os.getImageId();
				imageRef = spec;
				break;
			}
			case 5: {
				Network network = (Network) si;
				name = network.getName();
				model = network.getType();
				type = 5;
				//					price = network.getPrice();
				spec = String.valueOf(network.getBandWidth());
				unit = "M";
				break;
			}
			case 8: {
				ExtDisk extDisk = (ExtDisk) si;
				name = extDisk.getName();
				model = extDisk.getModel();
				type = 8;
				//					price = extDisk.getPrice();
				spec = String.valueOf(extDisk.getCapacity());
				//addDisk += (spec + ",");
				unit = "G";
				break;
			}
			}
			product.setModel(model);
			product.setPrice(price);
			product.setExtColumn(extColumn);
			product.setProductName(name);
			product.setSpec(spec);
			product.setType(type);
			product.setUnit(unit);
			products.add(product);
		}
		OrderProduct ip = new OrderProduct();
		ip.setSpec("1");// ip
		ip.setType(9);
		products.add(ip);

		String[] vlanParams = submitData.getVlanParams();//按顺序为name,dns1,dns2,vlan的数量
		OrderProduct vlan = new OrderProduct();
		vlan.setExtColumn(vlanParams[1] + "," + vlanParams[2]);//dns1和dns2
		vlan.setSpec(vlanParams[3]);//vlan规模的数量		
		vlan.setType(10);
		products.add(vlan);
		OrderItem item = new OrderItem();
		item.setUsePointOrNot(true);
		item.setPlanId(submitData.getPlanId());
		item.setPricePeriod(String.valueOf(submitData.getBuyPeriod()));
		item.setPricePeriodType("1");
		item.setPriceType("1");
		item.setQuantity(1);
		item.setVpdcName(vlanParams[0]);//传过来的VPDC名称
		item.setExtColumn(String.valueOf(submitData.getZoneGroupId()));//ZoneGroupId
		List<ServerZone> zoneList = serverZoneService.getCustomZonesByGroupId(submitData.getZoneGroupId());
		if (zoneList != null && zoneList.size() > 0) {
			StringBuilder zonesStr = new StringBuilder(zoneList.get(0).getCode());
			for (int i = 1; i < zoneList.size(); i++) {
				zonesStr.append(",").append(zoneList.get(i).getCode());
			}
			item.setNodeName(zonesStr.toString());
		}
		item.setImageId(imageRef);
		String flavorId = String.valueOf(vtVO.getFlavorId());
		item.setFlavorId(flavorId);
		item.setOrderProducts(products);
		return item;
	}

	private OrderItem generateOrderItemForReference(VpdcReference vr,long renewPeriod)throws Exception{
		List<OrderProduct> products = fromReferenceToOrderProduct(vr);
		Set<VpdcReference_extdisk> extDisks=vr.getExtdisks();
		if(extDisks!=null&&extDisks.size()>0){
			for(VpdcReference_extdisk extDisk:extDisks){
				OrderProduct p=new OrderProduct();
				p.setProductName(extDisk.getName());
				p.setSpec(String.valueOf(extDisk.getEd_capacity()));
				p.setUnit("G");
				p.setType(8);
				p.setExtColumn(extDisk.getVolumeId().toString());
				products.add(p);
			}
		}
		OrderProduct ip=new OrderProduct();
		ip.setProductName("IP");
		ip.setSpec(String.valueOf(vr.getIpNum()));
		ip.setType(9);
		products.add(ip);
		
	    Integer osId=vr.getOsId();
		

		OrderItem item=new OrderItem();
		item.setAmount(BigDecimal.ONE);
		item.setPrice(BigDecimal.ONE);
		item.setUsePointOrNot(true);
		item.setPricePeriod(String.valueOf(renewPeriod));
		item.setPricePeriodType("1");
		item.setPriceType("1");
		item.setQuantity(1);
		item.setNodeName(vr.getVmZone());
		item.setImageId(vr.getImageId());
		item.setFlavorId(vr.getFlavorId());
		item.setOrderProducts(products);
		return item;
	}

	private List<OrderProduct> fromReferenceToOrderProduct(VpdcReference vr) {
		List<OrderProduct> products = new ArrayList<OrderProduct>();
		for(int i=1;i<=5;i++){
			OrderProduct product = new OrderProduct();
			String spec = "";
			String unit = "";
			String model = "";
			String extColumn="";
			switch (i) {
			case 1: {
				model = vr.getCpu_type();
				spec = String.valueOf(vr.getCpu_core());
				unit = "core";
				break;
			}
			case 2: {
				model = vr.getMem_type();
				spec = String.valueOf(vr.getMem_size());
				unit = "M";
				break;
			}
			case 3: {
				model =vr.getDisk_type();
				spec = String.valueOf(vr.getDisk_capacity());
				unit = "G";
				break;
			}
			case 4: {
				ServiceItem item=serviceItemService.getSIById(vr.getOsId());
				Os os=(Os)item;
				model = os.getArch();
				extColumn=String.valueOf(vr.getOsId());
				spec = os.getImageId();
				break;
			}
			case 5: {
				model = vr.getNetwork_type();
				spec = String.valueOf(vr.getNetwork_bandwidth());
				unit = "M";
				break;
			}
			}
			product.setModel(model);
			product.setExtColumn(extColumn);
			product.setSpec(spec);
			product.setType(i);
			product.setUnit(unit);
			products.add(product);
		}
		return products;
	}

	@Override
	public List<ServiceItem> listServiceItem(int serviceType,
			List<Sort> sortList) {
		return serviceItemService.listServiceItem(serviceType, sortList);
	}

	@Override
	public List<ServiceItem> listOSItem(int serviceType, List<Sort> sortList, String family) {
		return serviceItemService.listOSItem(serviceType, sortList, family);
	}

	@Override
	public List<ZoneGroupVO> getCustomZoneGroupByUser(String brandCode,
			Long domainId) throws HsCloudException {
		return zoneGroupService.getCustomZoneGroupByUser(brandCode, domainId);
	}

	@Override
	@Transactional
	public void upgradeVM(SubmitOrderData data,String vmId)throws HsCloudException
	{
		try{
			//1，根据原来的vm配置生成orderItem
			VpdcReference vr=operation.getReferenceByVmId(vmId);
			long referenceId=vr.getId();
			OrderItem orderItem=generateOrderItemForReference(vr, 0);
			User user=data.getUser();
			long userId=user.getId();
			int coreDiff=0;//cpu核数差额
			int memDiff=0;//内存差额
			int bandWidthDiff=0;//内存差额
			String extDiskExtend=data.getExtDiskExtend();//扩展盘扩容
			//获取添加的扩展盘的数据
			List<Integer> extDiskAdd=new ArrayList<Integer>();
			String extDiskAddStr="";
			//升级的ServiceItem的id
			int[] siIds=data.getServiceItemIds();
			//Order备注中的升级信息
			StringBuilder regularRemark=new StringBuilder("该订单由云主机[").append(vmId).append("] 升级生成，");
			//升级前的flavor
			FlavorVO flavorVO=new FlavorVO();
			flavorVO.setDisk(vr.getDisk_capacity());
			flavorVO.setVcpus(vr.getCpu_core());
			flavorVO.setRam(vr.getMem_size());
			//flavorVO.setDisk(vr.getDisk_capacity());
			if(siIds!=null&&siIds.length>0){
				for(int id:siIds){
					ServiceItem si=serviceItemService.getSIById(id);
					switch(si.getServiceType()){
					case 1: {//升级项中有CPU
						Cpu cpu = (Cpu) si;
						int newCpuCore=cpu.getCoreNum();
						//获取CPU差额
						coreDiff=newCpuCore-vr.getCpu_core();
						//更新订单CPU中的信息
						OrderProduct p=getOrderProductByType(orderItem,1);
						p.setSpec(String.valueOf(newCpuCore));
						p.setExtColumn("new");
						//新的CPU信息更新到flavor中
						flavorVO.setVcpus(cpu.getCoreNum());
						//更新订单备注中关于CPU升级的信息
						regularRemark.append("CPU升级到").append(newCpuCore).append(p.getUnit()).append("；");
						break;
					}
					case 2: {
						Ram ram = (Ram) si;
						int newMenSize=ram.getSize();
						memDiff=newMenSize-vr.getMem_size();
						OrderProduct p=getOrderProductByType(orderItem,2);
						p.setSpec(String.valueOf(newMenSize));
						p.setExtColumn("new");
						flavorVO.setRam(ram.getSize());
						regularRemark.append("内存升级到").append(newMenSize).append(p.getUnit()).append("；");
						break;
					}
					case 5: {
						Network network = (Network) si;
						int newBandWidthSize=network.getBandWidth();
						bandWidthDiff=newBandWidthSize-vr.getNetwork_bandwidth();
						OrderProduct p=getOrderProductByType(orderItem,5);
						p.setSpec(String.valueOf(newBandWidthSize));
						p.setExtColumn("new");
						flavorVO.setBandwidth(String.valueOf(newBandWidthSize));
						regularRemark.append("带宽升级到").append(newBandWidthSize).append(p.getUnit()).append("；");
						break;
					}
					case 8: {
						ExtDisk extDisk = (ExtDisk) si;
						OrderProduct op=new OrderProduct();
						op.setProductName(extDisk.getName());
						op.setModel(extDisk.getModel());
						op.setType(8);
						//						op.setPrice(extDisk.getPrice());
						op.setSpec(String.valueOf(extDisk.getCapacity()));
						op.setExtColumn("new");
						op.setUnit("G");
						orderItem.getOrderProducts().add(op);
						extDiskAdd.add(extDisk.getCapacity());
						extDiskAddStr+=(extDisk.getCapacity()+",");
						regularRemark.append("添加扩展盘").append(extDisk.getCapacity()).append("G").append("；");
						break;
					}
					}
				}
			}
			int ipNew=data.getIpNum();
			OrderProduct ipProduct=getOrderProductByType(orderItem,9);
			vr.setIpNum(Integer.parseInt(ipProduct.getSpec())+ipNew);
			ipProduct.setSpec(String.valueOf(Integer.parseInt(ipProduct.getSpec())+ipNew));
			ipProduct.setExtColumn("new");
			Map<Integer,Integer> extDiskToExtend=new HashMap<Integer,Integer>();
			if(StringUtils.isNotBlank(extDiskExtend)){
				String[] sigleExtDiskStr=extDiskExtend.split(";");
				Set<VpdcReference_extdisk> orginalExtDisks=vr.getExtdisks();
				for(String disk:sigleExtDiskStr){
					String[] volumnIdAndSize=disk.split("#");
					int volumnId=Integer.parseInt(volumnIdAndSize[0]);
					int serviceItemId=Integer.parseInt(volumnIdAndSize[1]);
					ServiceItem item=serviceItemService.getSIById(serviceItemId);
					ExtDisk extDisk=(ExtDisk)item;
					int extDiskSize=extDisk.getCapacity();
					extDiskToExtend.put(volumnId,extDiskSize);
					for(OrderProduct op:getOrderProductsByType(orderItem, 8)){
						if(op.getExtColumn().equals(String.valueOf(volumnId))){
							op.setSpec(String.valueOf(extDiskSize));
						}
					}
					for(VpdcReference_extdisk originalDisk:orginalExtDisks){
						int localVolumnId=originalDisk.getVolumeId();
						int localSize=originalDisk.getEd_capacity();
						if(volumnId==localVolumnId){
							regularRemark.append("原有扩展盘由").append(localSize).append("扩容到").append(extDiskSize).append("；");
							int extDiskDiffLocal=extDiskSize-localSize;
							extDiskAdd.add(extDiskDiffLocal);
						}
					}
				}
			}
			String restServer=PropertiesUtils.getProperties("common.properties").get("demandBuyPriceRequestUrl");
			StringBuilder requestURL=new StringBuilder(restServer);
			if(coreDiff>0){
				requestURL.append("CPU=").append(coreDiff).append("&");
			}
			if(memDiff>0){
				requestURL.append("memory=").append(memDiff).append("&");
			}
			if(bandWidthDiff>0){
				requestURL.append("bandwidth=").append(bandWidthDiff).append("&");
			}
			if(extDiskAdd.size()>0){
				requestURL.append("extDisk=").append(Utils.joins(extDiskAdd,",")).append("&");
			}
			if(ipNew>0){
				requestURL.append("IP=").append(ipNew).append("&");
			}
			requestURL.append("planId=").append(data.getPlanId()).append("&");
			requestURL.append("payMonth=").append(1).append("&");
			requestURL.append("upgrade=").append(1).append("&");
			requestURL.append("domainCode=").append(data.getUser().getDomain().getCode()).append("&");
			requestURL.append("brandCode=").append(user.getLevel()).append("&");
			ServerZone sz=serverZoneService.getServerZoneByCode(vr.getVmZone());
			String zoneGroupCode=sz.getZoneGroupList().get(0).getCode();
			requestURL.append("zoneGroupCode=").append(zoneGroupCode);
			String priceStr=RESTUtil.load(requestURL.toString());
			if(StringUtils.isBlank(priceStr)||priceStr.indexOf("-")!=-1){
				throw new Exception("submit order for demand get price from 3p exception.");
			}
			//3，根据现在的时间与vm到期时间差计算需要升级多少天
			VpdcReference_Period vrPeriod=operation.getReferencePeriod(referenceId);
			Date effectiveDate=new Date();
			Date expirationDate=vrPeriod.getEndTime();
			long unUsedDays=OrderUtils.getDaysBetweenDateByHour(expirationDate, effectiveDate);
			//4，获取差价
			BigDecimal pricePerDay=new BigDecimal(priceStr);
			BigDecimal diffPrice=pricePerDay.multiply(new BigDecimal(unUsedDays));
			Account account = accountService.getAccountByUserId(userId);
			Long accountId = account.getId();
			boolean checkResult = accountService.checkBalance(
					accountId, diffPrice);
			if(checkResult){
				//5 存储订单数据
				orderItem.setEffectiveDate(effectiveDate);
				orderItem.setExpirationDate(expirationDate);
				orderItem.setPrice(diffPrice);
				orderItem.setAmount(diffPrice);
				orderItem.setPriceType("1");
				orderItem.setPricePeriodType("2");
				orderItem.setPricePeriod(String.valueOf(unUsedDays));
				List<OrderItem> orderItems=new ArrayList<OrderItem>();
				orderItems.add(orderItem);
				Order order = new Order();
				order.setRemark(regularRemark.toString());
				order.setOrderType((short)2);
				order.submit(orderItems,data.getUser(),"Paid",getRebate(user),getGiftRebate(user));
				orderService.saveOrder(order);
				Message message = OrderUtils.orderGenMessage(vr.getName(),
						order, user.getId(),(short)9);
				messageService.saveMessage(message);
				//6，账户扣钱生成交易
				long transactionId=accountService.accountConsume(userId,(short)1,
						PaymentType.PAYMENT_ONLINE.getIndex(),ServiceType.SERVICE_CLOUD_COMPUTER.getIndex(),
						accountId,message.getMessage(),order.getId(),
						ConsumeType.CONSUME_UPGRADE.getIndex(),
						diffPrice,BigDecimal.ZERO,BigDecimal.ZERO);
				IncomingLog incomingLog=new IncomingLog();
				incomingLog.setAccountId(accountId);
				incomingLog.setAmount(diffPrice);
				incomingLog.setDayPrice(diffPrice.divide(new BigDecimal(unUsedDays), 2, RoundingMode.DOWN));
				incomingLog.setEffectiveDate(effectiveDate);
				incomingLog.setExpirationDate(expirationDate);
				incomingLog.setEmail(user.getEmail());
				incomingLog.setIncomingType((short)1);
				incomingLog.setObjectId(referenceId);
				incomingLog.setOrderId(order.getId());
				incomingLog.setOrderItemId(orderItem.getId());
				incomingLog.setOrderNo(order.getOrderNo());
				incomingLog.setProductType((short)1);
				incomingLog.setTranscationType(TranscationType.TRANSCATION_CONSUME.getIndex());
				incomingLog.setTransactionId(transactionId);
				incomingLog.setDomainId(user.getDomain().getId());
				incomingLogService.saveIncomingLog(incomingLog);
				//新添加虚拟机与订单项的关联关系
				operation.saveReference_OrderItem(referenceId,
						String.valueOf(orderItem.getId()));
				//7,调用ops接口进行真正的升级操作
				//升级操作
				String addDisk="";
				if(StringUtils.isNotBlank(extDiskAddStr)){
					addDisk=extDiskAddStr.substring(0,extDiskAddStr.length()-1);
				}
				operation.resizeVm(vmId, flavorVO, addDisk,user);

				if(extDiskToExtend.size()>0){
					for(int volumnId:extDiskToExtend.keySet()){
						int diskSize=extDiskToExtend.get(volumnId);
						operation.modifyExttendDisk(vmId, volumnId, diskSize,user);
					}
				}

				if(ipNew>0){
					for(int i=1;i<=ipNew;i++){
						List<IPDetailInfoVO> availableIps=iPService.findAvailableIPDetailOfServerZone(sz);
						operation.addIPOfVm(vmId, availableIps.get(0).getIp(), user);
					}
				}

			}else{
				throw new HsCloudException(Constants.UPGRADE_VM_BLANCE_NOT_ENOUGH,logger);
			}

			//7，调用ops接口进行实际的升级操作
		}catch(Exception e){
			throw new HsCloudException("",logger,e);
		}
	}

	private OrderProduct getOrderProductByType(OrderItem item,int type)throws Exception{

		for(OrderProduct p:item.getOrderProducts()){
			if(p.getType()==type){
				return p;
			}
		}

		return null;
	}

	private List<OrderProduct> getOrderProductsByType(OrderItem item,int type)throws Exception{
		List<OrderProduct> products=new ArrayList<OrderProduct>();
		for(OrderProduct p:item.getOrderProducts()){
			if(p.getType()==type){
				products.add(p);
			}
		}
		return products;
	}

	@Override
	public void cancelUnpaidOrderByUser(long userId) throws HsCloudException {
		orderService.cancelUnPaidOrderByUser(userId);
	}

	/**
	 * <分页获取User用户所有的VPDC(包含非路由模式和路由模式)> 
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	@Override	
	public Page<VpdcVo> findVpdcsByUser(String page, String limit, Page<VpdcVo> pageVpdc, 
			int vpdcType, String name, User user) throws HsCloudException {
		try {
			int pageNo = 1;
			if (StringUtils.isNumeric(page)) {
				pageNo = Integer.parseInt(page);
			}
			int PageSize = 3;
			if (StringUtils.isNumeric(limit)) {
				PageSize = Integer.parseInt(limit);
			}
			pageVpdc.setPageNo(pageNo);
			pageVpdc.setPageSize(PageSize);
			pageVpdc = operation.findVpdcsByUser(pageVpdc, vpdcType, name, user);
		}
		catch (HsCloudException e) {
			throw new HsCloudException(Constants.VM_LIST_USER_ERROR, "findVpdcsByUser异常", logger, e);
		}
		return pageVpdc;		
	}

	/**
	 * <分页获取User用户某一个VPDC下面的所有的云主机> 
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public Page<InstanceVO> getHostsByVpdcId(String page, String limit, Page<InstanceVO> pageIns, 
			String query, String sort, User user, Long statusId, String type, String status_buss, 
			long vpdcId) throws HsCloudException {
		try {
			int pageNo = 1;
			if (StringUtils.isNumeric(page)) {
				pageNo = Integer.parseInt(page);
			}
			int PageSize = 3;
			if (StringUtils.isNumeric(limit)) {
				PageSize = Integer.parseInt(limit);
			}
			pageIns.setPageNo(pageNo);
			pageIns.setPageSize(PageSize);
			pageIns = operation.getHostsByVpdcId(sort, query, pageIns, user, statusId, 
					type, status_buss, vpdcId);
		}
		catch (HsCloudException e) {
			throw new HsCloudException(Constants.VM_LIST_USER_ERROR, "getHostsByVpdcId异常", logger, e);
		}
		return pageIns;	
	}

	@Override
	public List<VolumeVO> getAttachVolumesOfVm(String vmId)
			throws HsCloudException {
		List<VolumeVO> list = operation.getAllAttachVolumesOfVm(vmId);
		return list;
	}

	@Override
	@Transactional
	public boolean dettachVolume(String vmId, int volumId, int volumeMode,
			User user) throws HsCloudException {
		boolean result = false;
		result = operation.dettachVolume(vmId, volumId,volumeMode,user);
		return result;
	}

	@Override
	@Transactional
	public boolean attachVolume(String vmId, String addDisk, AbstractUser a)
			throws HsCloudException {
		boolean bl = false;
		try {
			String extDiskName = operation.createAndAttachExttendDiskByScsis(addDisk, vmId,a);
			if(!"".equals(extDiskName)){
				bl = true;
			}
		} catch (Exception e) {
			logger.error("attachExttendDisk Exception:"+e);
		}
		return bl;
	}

	@Override
	@Transactional
	public boolean resizeVm(String vmId, FlavorVO flavorVO, String addDisk,
			AbstractUser a) throws HsCloudException {
		boolean result = false;
		result = operation.resizeVm(vmId, flavorVO,addDisk,a);
		//修改带宽
		//		operation.modifyBandwidthOfVm(vmId, flavorVO.getBandwidth());
		return result;
	}

	@Override
	@Transactional
	public boolean modifyVolume(String vmId, int volumId, int volumeSize,
			User user) throws HsCloudException {
		boolean result = false;
		result = operation.modifyExttendDisk(vmId, volumId, volumeSize, user);		
		return result;
	}

	@Override
	public VpdcVo getVpdcById(long vpdcId) throws HsCloudException {
		logger.info("OPS-Facade-getVpdcById start");
		VpdcVo vpdcVo = null;
		try {
			vpdcVo = operation.getVpdcById(vpdcId);
		}
		catch (HsCloudException e) {
			throw new HsCloudException(Constants.VPDC_DETAIL_ERROR, "getVpdcById异常", logger, e);
		}
		return vpdcVo;
	}

	@Override
	@Transactional
	public boolean openRouter(String uuid, Object o, String otype)
			throws HsCloudException {
		return operation.openRouter(uuid, o, otype);
	}

	@Override
	@Transactional
	public boolean closeRouter(String uuid, Object o, String otype)
			throws HsCloudException {
		return operation.closeRouter(uuid, o, otype);
	}

	@Override
	@Transactional
	public String rebootRouter(String uuid, Object o, String otype)
			throws HsCloudException {
		return operation.rebootRouter(uuid, o, otype);
	}

	@Transactional(readOnly = false)
	@Override
	public boolean deleteVPDC(long vpdcId) throws HsCloudException {
		return operation.deleteVPDC(vpdcId);
	}

	@Override
	public VpdcRouterVo getRouterByVpdcId(long vpdcId) throws HsCloudException {
		logger.info("OPS-Facade-getRouterByVpdcId start");
		VpdcRouterVo vrv = null;
		try {
			vrv = operation.getRouterByVpdcId(vpdcId);
		}
		catch (HsCloudException e) {
			throw new HsCloudException(Constants.ROUTER_DETAIL_ERROR, "getRouterByVpdcId异常", logger, e);
		}
		return vrv;
	}

	@Override
	public VrouterTemplateVO getVrouterTemplate() throws HsCloudException {
		logger.info("OPS-Facade-getVrouterTemplate start");
		VrouterTemplateVO vtVO = null;
		try {
			vtVO = vrouterService.getVrouterTemplate();
		}
		catch (HsCloudException e) {
			throw new HsCloudException("", "getVrouterTemplate异常", logger, e);
		}
		return vtVO;
	}

	@Override
	public String getZoneGroupCodeByReferenceId(long referenceId)
			throws HsCloudException {
		try {
			VpdcReference vr=operation.getReferenceById(referenceId);
			String zoneCode=vr.getVmZone();
			ServerZone sz=serverZoneService.getServerZoneByCode(zoneCode);
			ZoneGroup zg=sz.getZoneGroupList().get(0);
			return zg.getCode();
		}
		catch (HsCloudException e) {
			throw new HsCloudException("", "getZoneGroupCodeByReferenceId异常", logger, e);
		}
	}

	@Override
	public Object getOuterInfomation(String userId,String accessId)
			throws HsCloudException {	
		Object result=null;
		StringBuffer requestUrl = new StringBuffer("");		
		try {			
			AccessAccount accessAccount = accessAccountService.getAccessAccount(accessId);
			if(accessAccount != null){
				String key = accessAccount.getAccessKey()+accessId + userId;				
				String encryptedAccessKey = PasswordUtil.getEncyptedPasswd(key);
				//requestAddress为http://yunhosting.com/api/hscloudregapi.asp?act=
				//               http://203.158.18.12/pass/transfer_hscloud.php?method=
				requestUrl.append(accessAccount.getRequestAddress()).append("boss_userinfo");
				requestUrl.append("&accessid=").append(accessId);
				requestUrl.append("&accesskey=").append(encryptedAccessKey);
				requestUrl.append("&userid=").append(userId);				
				result = RESTUtil.getJson(requestUrl.toString());
			}			
		} catch (Exception e) {
			throw new HsCloudException("", "getOuterInfomation异常", logger, e);
		}
		return result;
	}
	@Override
	public Object getUserInfomation(String userId,String accessId)
			throws HsCloudException {	
		Map<String, Object> result=new HashMap<String,Object>();
		StringBuffer requestUrl = new StringBuffer("");
		PlatformRelation platformRelation=platformRelationService.getPlatformRelationByExternalUser(userId);
		if(platformRelation!=null && StringUtils.isNotBlank(platformRelation.getUserId())){
			String localUserid=platformRelation.getUserId();
			if(StringUtils.isNotBlank(localUserid)){
			result.put("user",userService.getUser(Long.parseLong(localUserid)));
			}
		}			
		try {			
			AccessAccount accessAccount = accessAccountService.getAccessAccount(accessId);
			if(accessAccount != null){
				String key = accessAccount.getAccessKey()+accessId + userId;				
				String encryptedAccessKey = PasswordUtil.getEncyptedPasswd(key);
				//requestAddress为http://yunhosting.com/api/hscloudregapi.asp?act=
				//               http://203.158.18.12/pass/transfer_hscloud.php?method=
				requestUrl.append(accessAccount.getRequestAddress()).append("boss_userinfo");
				requestUrl.append("&accessid=").append(accessId);
				requestUrl.append("&accesskey=").append(encryptedAccessKey);
				requestUrl.append("&userid=").append(userId);				
				result.put("outInformation", RESTUtil.getJson(requestUrl.toString()));				
			}			
		} catch (Exception e) {
			throw new HsCloudException("", "getOuterInfomation异常", logger, e);
		}		
		return result;
	}

	@Override
	@Transactional
	public Object transfer(User user, String fee, String feeType,
			String transferMode,String accessId) throws HsCloudException {
		StringBuffer requestUrl = new StringBuffer("");	
		Object result=null;
		long traId=-1;
		String userId = "";//表示从客户平台用户ID
		String requestTransferMode = "2";//1表示从客户平台转出
		if(!"2".equals(transferMode)){
			requestTransferMode = "1";//1表示从客户平台转出,2表示从客户平台转入
		}
		PlatformRelation platformRelation =platformRelationService.getPlatformRelationByLocalUser(String.valueOf(user.getId()));
		if(platformRelation != null){
			userId = platformRelation.getExternalUserId();
			Account account = accountService.getAccountByUserId(user.getId());	
			if(account != null){
				traId = accountService.transfer(account, fee, feeType, transferMode);
			}
		}
		if(traId>=0){
//			if("SDHY".equals(accessId)){
//				try {if("SDHY".equals(accessId) && "2".equals(feeType) ){ //feeType=2是返点，等于1是转账
//					int r=1/0;
//				}}catch (Exception e) {
//					throw new HsCloudException("", "时代宏远未开通返点互转功能", logger, e);
//				}
				try {		
					AccessAccount accessAccount = accessAccountService.getAccessAccount(accessId);
					if(accessAccount != null){
						String key = accessAccount.getAccessKey()+accessId + userId;
						String encryptedAccessKey = PasswordUtil.getEncyptedPasswd(key);					
						//requestAddress为http://yunhosting.com/api/hscloudregapi.asp?act=
						//requestAddress为http://203.158.18.12/pass/transfer_hscloud.php?method= had saved in access_account's request address
						requestUrl.append(accessAccount.getRequestAddress()).append("transfer_accounts");
						requestUrl.append("&accessid=").append(accessId);
						requestUrl.append("&accesskey=").append(encryptedAccessKey);
						requestUrl.append("&userid=").append(userId);
						requestUrl.append("&fee=").append(fee);
						requestUrl.append("&feetype=").append(feeType);
						requestUrl.append("&transfermode=").append(requestTransferMode);
						JSONObject responseJson = RESTUtil.getJsonObject(requestUrl.toString());
						List list=new ArrayList();
						if(responseJson!=null && "true".equals(responseJson.get("success").toString()) && "SDHY".equals(accessId)){
							try {if( "2".equals(feeType) ){ //feeType=2是返点，等于1是转账
								int r=1/0;
							}}catch (Exception e) {
								throw new HsCloudException("", "时代宏远未开通返点互转功能", logger, e);
							}
							JSONObject responseObjectJson = (JSONObject)responseJson.get("resultObject");
							list = transcationLogService.findTransactionLog(traId);
							TranscationLog tl=(TranscationLog)list.get(0);
							if("2".equals(requestTransferMode)){
								tl.setDescription("从老平台转入新平台，交易流水号是"+responseObjectJson.get("billno").toString());						
							}else if("1".equals(requestTransferMode)){
								tl.setDescription("从新平台转入老平台，交易流水号是"+responseObjectJson.get("billno").toString());	
							}else{
								tl.setDescription("此条交易类型编码出错，交易流水号是"+responseObjectJson.get("billno").toString());
							}					
							transcationLogDao.save(tl);
						}else if(responseJson!=null && "true".equals(responseJson.get("success").toString()) && "XR".equals(accessId)){
							JSONObject responseObjectJson = (JSONObject)responseJson.get("resultObject");
							list = transcationLogService.findTransactionLog(traId);
							TranscationLog tl=(TranscationLog)list.get(0);
							if("1".equals(feeType)){
								if("2".equals(requestTransferMode)){
									tl.setDescription("从老平台转入新平台，交易流水号是"+responseObjectJson.get("billno").toString());						
								}else if("1".equals(requestTransferMode)){
									tl.setDescription("从新平台转入老平台，交易流水号是"+responseObjectJson.get("billno").toString());	
								}else{
									tl.setDescription("此条交易类型编码出错，交易流水号是"+responseObjectJson.get("billno").toString());
								}					
								transcationLogDao.save(tl);
							}else if("2".equals(feeType)){
								if("2".equals(requestTransferMode)){
									tl.setDescription("通过返点的方式，从老平台转入新平台，交易流水号是"+responseObjectJson.get("billno").toString());						
								}else if("1".equals(requestTransferMode)){
									tl.setDescription("通过返点的方式，从新平台转入老平台，交易流水号是"+responseObjectJson.get("billno").toString());	
								}else{
									tl.setDescription("此条交易类型编码出错，交易流水号是"+responseObjectJson.get("billno").toString());
								}					
								transcationLogDao.save(tl);
							}
						}
						else{
							result = responseJson;
							throw new HsCloudException("", responseJson.get("reason").toString(), logger, null);
						}
					}			
				} catch (Exception e) {
					throw new HsCloudException("", e.getMessage(), logger, e);
				}
//			 if("XR".equals(accessId)){
//				try {		
//					AccessAccount accessAccount = accessAccountService.getAccessAccount(accessId);
//					if(accessAccount != null){
//						String key = accessAccount.getAccessKey()+accessId + userId;
//						String encryptedAccessKey = PasswordUtil.getEncyptedPasswd(key);					
//						//requestAddress为http://203.158.18.12/pass/transfer_hscloud.php?method= had saved in access_account's request address
//						requestUrl.append(accessAccount.getRequestAddress()).append("transfer_accounts");
//						requestUrl.append("&accessid=").append(accessId);
//						requestUrl.append("&accesskey=").append(encryptedAccessKey);
//						requestUrl.append("&userid=").append(userId);
//						requestUrl.append("&fee=").append(fee);
//						requestUrl.append("&feetype=").append(feeType);
//						requestUrl.append("&transfermode=").append(requestTransferMode);
//						JSONObject responseJson = RESTUtil.getJsonObject(requestUrl.toString());
//						List list=new ArrayList();
//						if(responseJson!=null && "true".equals(responseJson.get("success").toString()) && "XR".equals(accessId)){
//							JSONObject responseObjectJson = (JSONObject)responseJson.get("resultObject");
//							list = transcationLogService.findTransactionLog(traId);
//							TranscationLog tl=(TranscationLog)list.get(0);
//							if("1".equals(feeType)){
//								if("2".equals(requestTransferMode)){
//									tl.setDescription("从老平台转入新平台，交易流水号是"+responseObjectJson.get("billno").toString());						
//								}else if("1".equals(requestTransferMode)){
//									tl.setDescription("从新平台转入老平台，交易流水号是"+responseObjectJson.get("billno").toString());	
//								}else{
//									tl.setDescription("此条交易类型编码出错，交易流水号是"+responseObjectJson.get("billno").toString());
//								}					
//								transcationLogDao.save(tl);
//							}else if("2".equals(feeType)){
//								if("2".equals(requestTransferMode)){
//									tl.setDescription("通过返点的方式，从老平台转入新平台，交易流水号是"+responseObjectJson.get("billno").toString());						
//								}else if("1".equals(requestTransferMode)){
//									tl.setDescription("通过返点的方式，从新平台转入老平台，交易流水号是"+responseObjectJson.get("billno").toString());	
//								}else{
//									tl.setDescription("此条交易类型编码出错，交易流水号是"+responseObjectJson.get("billno").toString());
//								}					
//								transcationLogDao.save(tl);
//							}
//						}else{
//							result = responseJson;
//							throw new HsCloudException("", responseJson.get("reason").toString(), logger, null);
//						}
//					}			
//				} catch (Exception e) {
//					throw new HsCloudException("", e.getMessage(), logger, e);
//				}
//			}
		}
		return result;
	}

	private String synchroPlatformRelation(String userId, User user,String externalUser,String localUser,String domainCode,AccessAccount accessAccount)
			throws HsCloudException {
		PlatformRelation platformRelation =platformRelationService.getPlatformRelationByExternalUser(userId);
		if(platformRelation != null){
			platformRelation.setUserId(userId);
		}else{
			platformRelation = new PlatformRelation();
			platformRelation.setUserId(userId);
			platformRelation.setExternalUserId(userId);
		}
		String result = platformRelationService.synchroPlatformRelation(platformRelation);
		String res = "";
		StringBuffer requestUrl = new StringBuffer("");		
		try {			
				String key = accessAccount.getAccessKey()+domainCode + externalUser;
				String encryptedAccessKey = PasswordUtil.getEncyptedPasswd(key);
				//requestAddress为http://freecloud.yunhosting.com:8090/或http://cloud.xrnet.cn:8090/
				requestUrl.append(accessAccount.getRequestAddress()).append("syncaccount");
				requestUrl.append("&accessid=").append(domainCode);
				requestUrl.append("&accesskey=").append(encryptedAccessKey);
				requestUrl.append("&userid=").append(externalUser);
				requestUrl.append("&email=").append(localUser);
				res = RESTUtil.get(requestUrl.toString());						
		} catch (Exception e) {
			throw new HsCloudException("", "synchroExternalUser异常", logger, e);
		}	
		return result;
	}

	@Override
	public String getExternalUserOfLocal(User localUser)
			throws HsCloudException {
		String userId = null;
		PlatformRelation platformRelation =platformRelationService.getPlatformRelationByLocalUser(String.valueOf(localUser.getId()));
		if(platformRelation != null){
			userId = platformRelation.getExternalUserId();
		}
		return userId;
	}

	@Override
	public List<OAZoneGroupVO> listServiceItemByZoneGroup(int serviceType,
			int zoneGroupId, List<Sort> sortList) {
		return serviceItemService.listServiceItemByZoneGroup(serviceType, zoneGroupId, sortList);
	}

	
}