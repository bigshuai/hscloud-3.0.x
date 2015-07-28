package com.hisoft.hscloud.web.facade.impl;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openstack.api.common.IPMIManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.constant.PaymentType;
import com.hisoft.hscloud.bss.billing.constant.TranscationType;
import com.hisoft.hscloud.bss.billing.entity.Account;
import com.hisoft.hscloud.bss.billing.entity.IncomingLog;
import com.hisoft.hscloud.bss.billing.service.AccountService;
import com.hisoft.hscloud.bss.billing.service.ApplicationTranscationLogService;
import com.hisoft.hscloud.bss.billing.service.IPMessageService;
import com.hisoft.hscloud.bss.billing.service.IncomingLogService;
import com.hisoft.hscloud.bss.billing.service.MonthIncomingService;
import com.hisoft.hscloud.bss.billing.service.MonthStatisService;
import com.hisoft.hscloud.bss.billing.service.ReportService;
import com.hisoft.hscloud.bss.billing.service.TranscationLogService;
import com.hisoft.hscloud.bss.billing.vo.AccountVO;
import com.hisoft.hscloud.bss.billing.vo.ApplicationTranscationLogVO;
import com.hisoft.hscloud.bss.billing.vo.CapitalSource;
import com.hisoft.hscloud.bss.billing.vo.IpMessageVO;
import com.hisoft.hscloud.bss.billing.vo.MonthIncomingVO;
import com.hisoft.hscloud.bss.billing.vo.MonthStatisVO;
import com.hisoft.hscloud.bss.billing.vo.OtherResponsibility;
import com.hisoft.hscloud.bss.billing.vo.QueryVO;
import com.hisoft.hscloud.bss.billing.vo.ResponsibilityIncoming;
import com.hisoft.hscloud.bss.billing.vo.Statistics;
import com.hisoft.hscloud.bss.billing.vo.TranscationLogVO;
import com.hisoft.hscloud.bss.billing.vo.VMResponsibility;
import com.hisoft.hscloud.bss.sla.om.entity.InvoiceAccount;
import com.hisoft.hscloud.bss.sla.om.entity.InvoiceRecord;
import com.hisoft.hscloud.bss.sla.om.entity.InvoiceRecordTransaction;
import com.hisoft.hscloud.bss.sla.om.entity.Order;
import com.hisoft.hscloud.bss.sla.om.entity.OrderItem;
import com.hisoft.hscloud.bss.sla.om.entity.RefundResult;
import com.hisoft.hscloud.bss.sla.om.entity.VmRefundLog;
import com.hisoft.hscloud.bss.sla.om.service.InvoiceAccountService;
import com.hisoft.hscloud.bss.sla.om.service.InvoiceRecordService;
import com.hisoft.hscloud.bss.sla.om.service.InvoiceRecordTransactionService;
import com.hisoft.hscloud.bss.sla.om.service.OrderService;
import com.hisoft.hscloud.bss.sla.om.service.VmRefundLogService;
import com.hisoft.hscloud.bss.sla.om.util.ExcelExport;
import com.hisoft.hscloud.bss.sla.om.util.OrderUtils;
import com.hisoft.hscloud.bss.sla.om.vo.InvoiceVO;
import com.hisoft.hscloud.bss.sla.om.vo.OrderItemVMVo;
import com.hisoft.hscloud.bss.sla.om.vo.OrderItemVo;
import com.hisoft.hscloud.bss.sla.om.vo.OrderVo;
import com.hisoft.hscloud.bss.sla.om.vo.TakeInvoiceType;
import com.hisoft.hscloud.bss.sla.om.vo.VmRefundOrderItemVo;
import com.hisoft.hscloud.bss.sla.sc.entity.Disk;
import com.hisoft.hscloud.bss.sla.sc.entity.ExtDisk;
import com.hisoft.hscloud.bss.sla.sc.entity.IPMIConfig;
import com.hisoft.hscloud.bss.sla.sc.entity.NodeIsolationConfig;
import com.hisoft.hscloud.bss.sla.sc.entity.Os;
import com.hisoft.hscloud.bss.sla.sc.entity.ScFeeType;
import com.hisoft.hscloud.bss.sla.sc.entity.ScIsolationConfig;
import com.hisoft.hscloud.bss.sla.sc.entity.ServerNode;
import com.hisoft.hscloud.bss.sla.sc.entity.ServerZone;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceCatalog;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceItem;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceType;
import com.hisoft.hscloud.bss.sla.sc.entity.ZoneGroup;
import com.hisoft.hscloud.bss.sla.sc.service.IIPMIConfigService;
import com.hisoft.hscloud.bss.sla.sc.service.IServerNodeService;
import com.hisoft.hscloud.bss.sla.sc.service.IServerZoneService;
import com.hisoft.hscloud.bss.sla.sc.service.IServiceCatalogService;
import com.hisoft.hscloud.bss.sla.sc.service.IServiceItemService;
import com.hisoft.hscloud.bss.sla.sc.service.ZoneGroupService;
import com.hisoft.hscloud.bss.sla.sc.vo.OsVO;
import com.hisoft.hscloud.bss.sla.sc.vo.SCVo;
import com.hisoft.hscloud.bss.sla.sc.vo.ServiceItemVo;
import com.hisoft.hscloud.bss.sla.sc.vo.ZoneVO;
import com.hisoft.hscloud.common.entity.Excel;
import com.hisoft.hscloud.common.entity.HcEventResource;
import com.hisoft.hscloud.common.entity.HcEventVmOps;
import com.hisoft.hscloud.common.entity.LogOperatorType;
import com.hisoft.hscloud.common.entity.MarketingPromotion;
import com.hisoft.hscloud.common.entity.OperationLog;
import com.hisoft.hscloud.common.entity.Province;
import com.hisoft.hscloud.common.entity.UserBrand;
import com.hisoft.hscloud.common.service.DataService;
import com.hisoft.hscloud.common.service.MarketingPromotionService;
import com.hisoft.hscloud.common.service.OperationLogService;
import com.hisoft.hscloud.common.service.SiteConfigService;
import com.hisoft.hscloud.common.service.UserBrandService;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.ExceptionServiceUtil;
import com.hisoft.hscloud.common.util.HsCloudDateUtil;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.IPConvert;
import com.hisoft.hscloud.common.util.MD5Util;
import com.hisoft.hscloud.common.util.PasswordUtil;
import com.hisoft.hscloud.common.util.RESTUtil;
import com.hisoft.hscloud.common.util.RabbitMQUtil;
import com.hisoft.hscloud.common.util.SecurityHelper;
import com.hisoft.hscloud.common.util.SocketUtil;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.common.util.Zip;
import com.hisoft.hscloud.common.vo.MarketingPromotionVO;
import com.hisoft.hscloud.common.vo.OperationLogQueryVo;
import com.hisoft.hscloud.common.vo.UserBrandVO;
import com.hisoft.hscloud.common.vo.UserUserBrandVO;
import com.hisoft.hscloud.controlpanel.entity.ControlPanelUser;
import com.hisoft.hscloud.controlpanel.service.ControlPanelService;
import com.hisoft.hscloud.crm.usermanager.constant.UserState;
import com.hisoft.hscloud.crm.usermanager.constant.UserType;
import com.hisoft.hscloud.crm.usermanager.entity.AbstractUser;
import com.hisoft.hscloud.crm.usermanager.entity.AccessAccount;
import com.hisoft.hscloud.crm.usermanager.entity.Action;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.entity.AdminRole;
import com.hisoft.hscloud.crm.usermanager.entity.Company;
import com.hisoft.hscloud.crm.usermanager.entity.Country;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.crm.usermanager.entity.Industry;
import com.hisoft.hscloud.crm.usermanager.entity.PlatformRelation;
import com.hisoft.hscloud.crm.usermanager.entity.Region;
import com.hisoft.hscloud.crm.usermanager.entity.ResourceType;
import com.hisoft.hscloud.crm.usermanager.entity.Role;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.entity.UserBank;
import com.hisoft.hscloud.crm.usermanager.entity.UserProfile;
import com.hisoft.hscloud.crm.usermanager.service.AccessAccountService;
import com.hisoft.hscloud.crm.usermanager.service.ActionService;
import com.hisoft.hscloud.crm.usermanager.service.AdminService;
import com.hisoft.hscloud.crm.usermanager.service.CompanyService;
import com.hisoft.hscloud.crm.usermanager.service.DomainService;
import com.hisoft.hscloud.crm.usermanager.service.OptionService;
import com.hisoft.hscloud.crm.usermanager.service.PermissionService;
import com.hisoft.hscloud.crm.usermanager.service.PlatformRelationService;
import com.hisoft.hscloud.crm.usermanager.service.ResourceService;
import com.hisoft.hscloud.crm.usermanager.service.ResourceTypeService;
import com.hisoft.hscloud.crm.usermanager.service.RolePermissionService;
import com.hisoft.hscloud.crm.usermanager.service.RoleService;
import com.hisoft.hscloud.crm.usermanager.service.TreeService;
import com.hisoft.hscloud.crm.usermanager.service.UserBankService;
import com.hisoft.hscloud.crm.usermanager.service.UserService;
import com.hisoft.hscloud.crm.usermanager.util.Constant;
import com.hisoft.hscloud.crm.usermanager.vo.AccessAccountVO;
import com.hisoft.hscloud.crm.usermanager.vo.AdminMenuVO;
import com.hisoft.hscloud.crm.usermanager.vo.AdminVO;
import com.hisoft.hscloud.crm.usermanager.vo.CheckboxVO;
import com.hisoft.hscloud.crm.usermanager.vo.DomainVO;
import com.hisoft.hscloud.crm.usermanager.vo.MenuVO;
import com.hisoft.hscloud.crm.usermanager.vo.PrivilegeVO;
import com.hisoft.hscloud.crm.usermanager.vo.TreeNode;
import com.hisoft.hscloud.crm.usermanager.vo.TreeQueryVO;
import com.hisoft.hscloud.crm.usermanager.vo.UserProfileVo;
import com.hisoft.hscloud.crm.usermanager.vo.UserVO;
import com.hisoft.hscloud.mail.constant.MailTemplateType;
import com.hisoft.hscloud.mail.service.MailService;
import com.hisoft.hscloud.message.entity.Message;
import com.hisoft.hscloud.message.entity.SMSMessage;
import com.hisoft.hscloud.message.service.AnnouncementService;
import com.hisoft.hscloud.message.service.MessageService;
import com.hisoft.hscloud.message.service.SmsMessageService;
import com.hisoft.hscloud.message.vo.AnnouncementVO;
import com.hisoft.hscloud.payment.alipay.entity.PaymentInterface;
import com.hisoft.hscloud.payment.alipay.service.AlipayService;
import com.hisoft.hscloud.payment.alipay.vo.PaymentInterfaceVO;
import com.hisoft.hscloud.systemmanagement.service.BusinessPlatformService;
import com.hisoft.hscloud.systemmanagement.service.ControlPanelMaintenanceService;
import com.hisoft.hscloud.systemmanagement.service.HcEventResourceService;
import com.hisoft.hscloud.systemmanagement.service.HcEventVmOpsService;
import com.hisoft.hscloud.systemmanagement.service.ThreadService;
import com.hisoft.hscloud.systemmanagement.vo.BusinessPlatformVO;
import com.hisoft.hscloud.systemmanagement.vo.ControlPanelVO;
import com.hisoft.hscloud.systemmanagement.vo.LogQueryVO;
import com.hisoft.hscloud.systemmanagement.vo.ProcessResourceVO;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.image.service.ImageService;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.image.vo.FileVO;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.image.vo.ImageVO;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.entity.IPDetail;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.entity.IPRange;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.service.IPService;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.vo.IPDetailInfoVO;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.vo.IPDetailVO;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.vo.IPRangeVO;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.vo.IPStatistics;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcInstance;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference_OrderItem;
import com.hisoft.hscloud.vpdc.ops.json.bean.CreateVmBean;
import com.hisoft.hscloud.vpdc.ops.json.bean.NetWorkBean;
import com.hisoft.hscloud.vpdc.ops.json.bean.ResourceLimit;
import com.hisoft.hscloud.vpdc.ops.json.bean.VmExtDiskBean;
import com.hisoft.hscloud.vpdc.ops.service.Operation;
import com.hisoft.hscloud.vpdc.ops.service.VpdcNetworkService;
import com.hisoft.hscloud.vpdc.ops.service.VpdcVrouterService;
import com.hisoft.hscloud.vpdc.ops.vo.FlavorVO;
import com.hisoft.hscloud.vpdc.ops.vo.InstanceDetailVo;
import com.hisoft.hscloud.vpdc.ops.vo.InstanceVO;
import com.hisoft.hscloud.vpdc.ops.vo.VmInfoVO;
import com.hisoft.hscloud.vpdc.ops.vo.VmSnapShotVO;
import com.hisoft.hscloud.vpdc.ops.vo.VncClientVO;
import com.hisoft.hscloud.vpdc.ops.vo.VolumeVO;
import com.hisoft.hscloud.vpdc.ops.vo.VpdcReferenceVO;
import com.hisoft.hscloud.vpdc.ops.vo.VrouterTemplateVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.CPUMonitorDetailBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.DISKMonitorDetailBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.HostAcquisitionBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.IOPSMonitorDetailBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.NETMonitorDetailBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.service.MonitorService;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.CPUHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.DiskHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.HostInfoVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.HostOverviewInfoVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.HostVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.MemoryHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.NetHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.VmDetailInfoVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.VmInfoMonitorVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.VmOverviewInfoVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.VmRealtimeMonitorVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.WholeOverviewInfoVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.ZoneOverviewInfoVO;
import com.hisoft.hscloud.web.action.ApplicationTranscationLogAction;
import com.hisoft.hscloud.web.facade.Facade;
import com.wgdawn.conditions.model.EvaluatePageModel;
import com.wgdawn.constants.CommonStatusEnum.CommonStatus;
import com.wgdawn.persist.model.AppWorkOrderType;
import com.wgdawn.persist.model.ApplicationInvoiceAccount;
import com.wgdawn.persist.more.model.MoreAppProductReview;
import com.wgdawn.persist.more.model.MoreExportAppProductReview;
import com.wgdawn.persist.more.model.appDetail.MoreAppDetail;
import com.wgdawn.persist.more.model.appDetail.MoreExportAppDetail;
import com.wgdawn.service.IssueApplicationService;
import com.wgdawn.persist.more.model.app.AppBill;
import com.wgdawn.persist.more.model.app.ExportAppBillDetailInfo;
import com.wgdawn.persist.more.model.app.ExportAppOrderDetailInfo;
import com.wgdawn.persist.more.model.center.ApplicationInvoiceRecord;
import com.wgdawn.persist.more.model.center.ApplicationInvoiceRecordVO;
import com.wgdawn.service.AppBillService;
import com.wgdawn.service.AppOrderService;
import com.wgdawn.service.ApplicationInvoiceRecordService;
import com.wgdawn.service.EvaluateService;
import com.wgdawn.service.WorkOrderService;

@Service
public class FacadeImpl implements Facade {
	
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private UserService userService;
	@Autowired
	private OptionService optionService;
	@Autowired
	private Operation operation;
	@Autowired
    private AdminService adminService;
	@Autowired
    private RoleService roleService;
	@Autowired
    private MonitorService monitorService;
	@Autowired
	private ImageService imageService;
	@Autowired
	private IServerNodeService nodeService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private CompanyService companyService;
	@Autowired
    private ResourceService resourceService;
    @Autowired
    private ActionService actionService;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private IPService iPService;
    @Autowired
    private ThreadService threadService;
    @Autowired
    private TreeService treeService;
    @Autowired
	private IServiceCatalogService serviceCatalogService;
    @Autowired
	private IServiceItemService serviceItemService;
    @Autowired
    private OrderService orderService;
    @Autowired
	private ResourceTypeService resourceTypeService;
    @Autowired
    private TranscationLogService transcationLogService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserBankService userBankService;
    @Autowired
    private SiteConfigService siteConfigService;
    @Autowired
	private MailService mailService;
    @Autowired
    private AnnouncementService announcementService;
    @Autowired
    private UserBrandService userBrandService;
    @Autowired
    private InvoiceAccountService invoiceAccountService;
  
    @Autowired
    private ApplicationInvoiceRecordService applicationInvoiceRecordService;
    @Autowired
    private InvoiceRecordService invoiceRecordService;
    @Autowired
    private InvoiceRecordTransactionService invoiceRecordTransactionService;
    @Autowired
    private DataService dataService;
    @Autowired
	private IServerZoneService zoneService;
    @Autowired
	private AlipayService alipayService;
    @Autowired
    private DomainService domainService;
    @Autowired
	private IIPMIConfigService ipmiConfigService;
    @Autowired
	private ControlPanelService controlPanelService;
    @Autowired
    private VmRefundLogService vmRefundLogService;
    @Autowired
    private MonthIncomingService monthIncomingService;
    @Autowired
    private MarketingPromotionService marketingPromotionService;
    @Autowired
    private AccessAccountService accessAccountService;
    @Autowired
    private ApplicationTranscationLogService applicationTranscationLogService;
	@Autowired
	private AppBillService appBillService;
	@Autowired
	private AppOrderService appOrderService;
    private SocketUtil socket = new SocketUtil();   
    
    //抛出异常服务
    private ExceptionServiceUtil eService = new ExceptionServiceUtil(logger);
    @Autowired
    private HcEventResourceService resourceLogService;
    @Autowired
    private HcEventVmOpsService vmOpsLogService;
	@Autowired
	private IncomingLogService incomingLogService;
    @Autowired
	private ZoneGroupService zoneGroupService;
    @Autowired
    private BusinessPlatformService businessPlatformService;
    @Autowired
    private ControlPanelMaintenanceService controlPanelMaintenanceService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private OperationLogService logService;
    @Autowired
    private MonthStatisService monthStatisService;
    @Autowired
    private IPMessageService ipMessageService;
    @Autowired
    private VpdcVrouterService vpdcVrouterService;
    @Autowired
    private VpdcNetworkService vpdcNetworkService;
    @Autowired
	private PlatformRelationService platformRelationService;
    @Autowired
    private SmsMessageService smsMessageService;
    
    @Autowired
    private WorkOrderService workOrderService;
    
    @Autowired
    private IssueApplicationService issueApplicationService;
    
    @Autowired
    private EvaluateService evaluateService;

    public static Map<String, ResourceType> resourceTypeMap = new HashMap<String, ResourceType>();
    public static List<ResourceType> resourceTypeList = new ArrayList<ResourceType>();
    
	@Override
	@Transactional(readOnly=true)
	public Page<User> getAllUserByPage(Admin admin,List<Sort> sorts, Page<User> page,
			long userId,String type,String query) {
		
		Page<User> result=null;
		if(admin.getIsSuper() || roleService.isSpecialAdmin(admin.getId())){
			if(type!=null&&type.equals("userName")){
				result=userService.searchUserLikeName(query, sorts, page, null);
			}else if(type!=null&&type.equals("userEmail")){
				result=userService.searchUserLikeEmail(query, sorts, page, null);
			}else if(type!=null&&type.equals("userDomain")){
				result=userService.searchUserLikeDomain(query, sorts, page, null);
			}else if(type!=null&&type.equals("userState")){
				result=userService.searchUserByState(query, sorts, page, null);
			}else if(type!=null&&type.equals("supplier")){
				result=userService.searchUserBySupplier(query, sorts, page, null);
			}else{
				result=userService.searchUser(sorts, page, null);
			}
		}else{
			List<Domain> domains = domainService.getDomainByAdmin(admin.getId());
			if(domains.isEmpty()){
				return page;
			}
			List<Long> uids = new ArrayList<Long>();
            for (Domain domain : domains) {
            	uids.add(domain.getId());
			}
			if(type!=null&&type.equals("userName")){
				result=userService.searchUserLikeName(query, sorts, page, uids);
			}else if(type!=null&&type.equals("userEmail")){
				result=userService.searchUserLikeEmail(query, sorts, page, uids);
			}else if(type!=null&&type.equals("userDomain")){
				result=userService.searchUserLikeDomain(query, sorts, page, uids);
			}else if(type!=null&&type.equals("userState")){
				result=userService.searchUserByState(query, sorts, page, uids);	
			}else if(type!=null&&type.equals("supplier")){
				result=userService.searchUserBySupplier(query, sorts, page, uids);
			}else{
				result=userService.searchUser(sorts, page, uids);
			}
		}
		List<User> user_list = result.getResult();
		List<User> filtered_user_list = new ArrayList<User>();
		for (User user : user_list) {
			User filtered_user = user;
			filtered_user.setPassword(null);
			filtered_user_list.add(filtered_user);
			if(user.getUserProfile() == null) {
			    UserProfile userProfile = new UserProfile();
			    Country country = new Country();
			    country.setId(1l);
			    userProfile.setCountry(country);
			    Industry industry = new Industry();
			    industry.setId(1l);
			    userProfile.setIndustry(industry);
			    user.setUserProfile(userProfile);
			    
			}
		}
		result.setResult(filtered_user_list);
		return result;
	}
	@Override
	public List<Country> loadCountry() {
		return optionService.loadCountry();
	}
	@Override
	public List<Industry> loadIndustry() {
		return optionService.loadIndustry();
	}
	@Override
	public List<Region> loadRegion(long countryId) {
		
		return optionService.loadRegion(countryId);
	}
	@Override
	@Transactional
	public void deleteUser(long userId) {
		userService.deleteUser(userId);
	}
	
	@Override
	@Transactional
	public void freezedUser(long userId) {
		
		userService.freezedUser(userId);
		
	}
	@Override
	@Transactional
	public void resetPasswd(String password,long id) {
		User user=userService.getUser(id);
		user.setPassword(password);
		userService.resetPassword(user);
	}

	@Override
	@Transactional
	public void addUser(User user,String companyName) {
		if(UserType.ENTERPRISE_USER.getType().equals(user.getUserType())){
			user.setEnable(UserState.APPROVED.getIndex());
			userService.createUser(user,UserType.ENTERPRISE_USER.getType());
			accountService.createAccount(user);
			InvoiceAccount invoiceAccount = new InvoiceAccount();
			invoiceAccount.setUserId(user.getId());
			invoiceAccountService.createInvoiceAccount(invoiceAccount);
			Company company = new Company();
			company.setName(companyName);
			companyService.createCompany(company, user.getId());
			companyService.saveUserCompany(company.getId(), user.getId());
		}else if(UserType.PERSONAL_USER.getType().equals(user.getUserType())){
			user.setEnable(UserState.APPROVED.getIndex());
			userService.createUser(user,UserType.PERSONAL_USER.getType());
			accountService.createAccount(user);
			InvoiceAccount invoiceAccount = new InvoiceAccount();
			invoiceAccount.setUserId(user.getId());
			invoiceAccountService.createInvoiceAccount(invoiceAccount);
		}
		
	}
	@Transactional(readOnly=true)
	public User getUserByEmail(String email){
		return userService.getUserByEmail(email);
	}

	
	@Override
	@Transactional
	public Admin loginUserByEmail(String email, String password) {
		
		return userService.loginAdimnByEmail(email, password);
		
	}
  

	@Override
	@Transactional
	public void modifyUserProfile(UserProfileVo userProfileVo) {
		User user = userService.getUser(userProfileVo.getUserId());
		if(null != userProfileVo.getEmail() && !"".equals(userProfileVo.getName())){
			user.setEmail(userProfileVo.getEmail());
		}
		if(null != userProfileVo.getName() && !"".equals(userProfileVo.getName())){
			user.setName(userProfileVo.getName());
		}
		List<Company> cs = new ArrayList<Company>();
		if((UserType.PERSONAL_USER.getType().equals(user.getUserType()) && UserType.ENTERPRISE_USER.getType().equals(userProfileVo.getUserType())) || userProfileVo.getDomainId()!= user.getDomain().getId()){
			cs = companyService.getJoinedCompanys(userProfileVo.getUserId());
		}
		if(userProfileVo.getDomainId()!=user.getDomain().getId()){
			Domain domain = domainService.getDomainById(userProfileVo.getDomainId());
			user.setDomain(domain);
			if(UserType.ENTERPRISE_USER.getType().equals(user.getUserType())){
				List<Long> uids = companyService.getMembers(user.getId());
				if(!uids.isEmpty()){
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("uids", uids);
					map.put("domainid", domain.getId());
					String sql = "UPDATE hc_user u set u.domain_id=(:domainid) where u.id in(:uids)";
					userService.saveUser(sql, map);
				}
			}
		}
		
		user.setUpdateDate(new Date());
		user.setLevel(userProfileVo.getLevel());
		if(UserType.PERSONAL_USER.getType().equals(user.getUserType()) && UserType.ENTERPRISE_USER.getType().equals(userProfileVo.getUserType())){
			if(cs.size()>0){
				throw new HsCloudException(Constants.ENTUSER_HAS_COMPANY," Must quit company",logger);
			}
			roleService.addEntUserRole(user.getId());
			user.setUserType(userProfileVo.getUserType());
			Company company = new Company();
			company.setName(userProfileVo.getCompany());
			companyService.createCompany(company, user.getId());
			companyService.saveUserCompany(company.getId(), user.getId());
		}
		String companyName = null;
		UserProfile userPro = null;
		if(null != user.getUserProfile()){
		    userPro = user.getUserProfile();
		    companyName = userPro.getCompany();
			userPro.setTelephone(userProfileVo.getTelephone());
			userPro.setCompany(userProfileVo.getCompany());
			userPro.setCountry(userProfileVo.getCountry());
			userPro.setSex(userProfileVo.getSex());
			userPro.setIndustry(userProfileVo.getIndustry());
			userPro.setRegion(userProfileVo.getRegion());
			userPro.setAddress(userProfileVo.getAddress());
			userPro.setIdCard(userProfileVo.getIdCard());
			userPro.setUpdateDate(new Date());
			//企业号和供应商
			userPro.setCompanyNO(userProfileVo.getCompanyNO());
			userPro.setSupplier(userProfileVo.isSupplier());
			//修改人 张建伟 修改时间 20130909
			userPro.setDescription(userProfileVo.getDescription());
		} else {
		    //lihonglei start
		    userPro = new UserProfile();
            userPro.setTelephone(userProfileVo.getTelephone());
            userPro.setCompany(userProfileVo.getCompany());
            userPro.setCountry(userProfileVo.getCountry());
            userPro.setSex(userProfileVo.getSex());
            userPro.setIndustry(userProfileVo.getIndustry());
            userPro.setRegion(userProfileVo.getRegion());
            userPro.setAddress(userProfileVo.getAddress());
            userPro.setIdCard(userProfileVo.getIdCard());
            userPro.setUpdateDate(new Date());
          //企业号和供应商
            userPro.setCompanyNO(userProfileVo.getCompanyNO());
			userPro.setSupplier(userProfileVo.isSupplier());
            userPro.setDescription(userProfileVo.getDescription());
            user.setUserProfile(userPro);
            //lihonglei end
		}
		if(UserType.ENTERPRISE_USER.getType().equals(userProfileVo.getUserType()) && ((null!=userProfileVo.getCompany() && !userProfileVo.getCompany().equals(companyName)))){
			Company company = companyService.getCompanyByUserId(user.getId());
			company.setName(userProfileVo.getCompany());
			company.setUpdateDate(new Date());
			companyService.updateCompany(company);
		}
		//userService.modifyUser(user);
//		UserProfile userProfileOrg=userService.getUserProfileById(userProfileVo.getId());
//		//userProfileOrg.setAddress(userProfileVo.getAddress());
//		userProfileOrg.setTelephone(userProfileVo.getTelephone());
//		userProfileOrg.setCompany(userProfileVo.getCompany());
//		userProfileOrg.setCountry(userProfileVo.getCountry());
//		userProfileOrg.setSex(userProfileVo.getSex());
//		userProfileOrg.setIndustry(userProfileVo.getIndustry());
//		userProfileOrg.setRegion(userProfileVo.getRegion());
//		userProfileOrg.setIdCard(userProfileVo.getIdCard());
//		userService.modifyUserProfile(userProfileOrg);
	}

	@Override
	public Page<AdminVO> getAllAdminUser(List<Sort> sorts,String query,int start,int limit,int page,String type) {
		return adminService.getAllAdminUser(sorts,query,start,limit,page,type);
	}

	/*******************************OPS Module********************************/	
	@Override
	@Transactional
	public String createVM(CreateVmBean cvb, long uid) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter createVM method.");			
		}
		String floatingIP = "";
		String zone=cvb.getVmZone();
		ServerZone sz = null;
		if(StringUtils.isEmpty(zone)){
			sz = zoneService.getDefaultServerZone();
			if(sz!=null && sz.getCode()!=null){
				zone = sz.getCode();
			}
		}else{
			sz = zoneService.getServerZoneByCode(zone);
		}
		//判断是否发布外网IP：1--发布
		if("1".equals(cvb.getIpDeploy())){
			floatingIP = cvb.getFloating_ip();
			//如果要发布外网IP，先看前台是否传过来IP，如果没有，从IP池中取一个
			if(StringUtils.isEmpty(floatingIP)){
				floatingIP = iPService.assignIPDetail(sz.getId());
			}
			//如果IP池中没有IP，则提示错误
			if(floatingIP==null){
				floatingIP = "";
				logger.error("NO IP enough!");
				throw new HsCloudException(Constants.VM_IP_NO_ENOUGH, "createVm——Admin异常", logger, null);
			}
		}		
		cvb.setFloating_ip(floatingIP);
		String vmId = operation.createVm(cvb,uid,zone);
		if(logger.isDebugEnabled()){
			logger.debug("exit createVM method.");
		}
		return vmId;
	}
	@Override
	public boolean startVmByVmId(String vmId,Object o,String otype) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter startVmByVmId method.");			
		}
		boolean bl = false;
		bl = operation.openvm(vmId,o,otype);
		if(logger.isDebugEnabled()){
			logger.debug("exit startVmByVmId method.");
		}
		return bl;
	}
	@Override
	public String rebootVmByVmId(String vmId,Object o,String otype) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter rebootVmByVmId method.");			
		}
		String result = operation.rebootVm(vmId,o,otype);
		if(logger.isDebugEnabled()){
			logger.debug("exit rebootVmByVmId method.");
		}
		return result;
	}
	@Override
	public boolean closeVmByVmId(String vmId,Object o,String otype) throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter closeVmByVmId method.");			
		}
		boolean bl = false;
		bl = operation.closeVm(vmId,o,otype);
		if(logger.isDebugEnabled()){
			logger.debug("exit closeVmByVmId method.");
		}
		return bl;
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
	/*@Override
	public String resumeVmByVmId(String vmId) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter resumeVmByVmId method.");			
		}
		String result = operation.resumeVm(vmId);
		if(logger.isDebugEnabled()){
			logger.debug("exit resumeVmByVmId method.");
		}
		return result;
	}*/
	@Override
	public String getVNCUrl(String vmId) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter getVNCUrl method.");			
		}
		String result = operation.getVnc(vmId);
		if(logger.isDebugEnabled()){
			logger.debug("exit getVNCUrl method.");
		}
		return result;
	}
	@Override
	public String backupVmByVmId(String vmId,
			String sn_name, String sn_comments, int sn_type,Object o,String otype) throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter backupVmByVmId method.");			
		}
		String result = operation.backupsVm(vmId, sn_name, sn_comments,
					sn_type,o,otype);	
		if(logger.isDebugEnabled()){
			logger.debug("exit backupVmByVmId method.");
		}
		return result;
	}
	@Override
	public String renewVM(String vmId, String backupId,Object o,String otype) throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter renewVM method.");			
		}
		String result = operation.renewVm(vmId, backupId,o,otype);	
		if(logger.isDebugEnabled()){
			logger.debug("exit renewVM method.");
		}
		return result;
	}
	@Override
	public List<VmSnapShotVO> findAllBackupsByVmId(String vmId) throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter findAllBackupsByVmId method.");			
		}
		List<VmSnapShotVO> lvssVO = operation.findAllSnapShots(vmId);
		if(logger.isDebugEnabled()){
			logger.debug("exit findAllBackupsByVmId method.");
		}
		return lvssVO;
	}
	@Override	
	public boolean terminateVmByVmId(String vmId,String vmName,String terminateFlag,long adminId) throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter terminateVmByVmId method.");			
		}
		boolean result = false;
		result = operation.terminate(vmId,vmName,terminateFlag,adminId);		
		if(logger.isDebugEnabled()){
			logger.debug("exit terminateVmByVmId method.");
		}
		return result;
	}
	@Override
	@Transactional
	public boolean freezeVM(String vmId,long uid){
		if(logger.isDebugEnabled()){
			logger.debug("enter terminateVmByVmId method.");			
		}
		boolean result = false;
		result = operation.freezeVM(vmId, uid,Constants.VM_ISENABLE_FALSE_ADMIN);
		if(logger.isDebugEnabled()){
			logger.debug("exit terminateVmByVmId method.");
		}
		return result;
	}
	@Override
	@Transactional
	public boolean activeVM(String vmId,long uid){
		if(logger.isDebugEnabled()){
			logger.debug("enter terminateVmByVmId method.");			
		}
		boolean result = false;
		result = operation.activeVM(vmId, uid);	
		if(logger.isDebugEnabled()){
			logger.debug("exit terminateVmByVmId method.");
		}
		return result;
	}
	@Override	
	public boolean updateVmName(String vmName, String vmId,long uid) throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter updateVmName method.");			
		}
		return operation.updateVmName(vmName, vmId,uid);		
	}
	@Override	
	public boolean updateComments(String comments,String outComments, String vmId,long uid) throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter updateComments method.");			
		}
		return operation.updateVmComments(comments,outComments,vmId,uid);		
	}
	@Override
	public InstanceDetailVo findDetailVmById(String vmId) throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter findDetailVmById method.");			
		}
		InstanceDetailVo idVO = operation.findVm(vmId);	
		if(logger.isDebugEnabled()){
			logger.debug("exit findDetailVmById method.");
		}
		return idVO;
	}
	
	@Override
	@Transactional
	public InstanceDetailVo getTryVmInfo(long referenceId) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter findDetailVmById method.");			
		}
		InstanceDetailVo idvo = operation.getTryVmInfo(referenceId);
		if(idvo.getScId()!=null){
			 ServiceCatalog sc = serviceCatalogService.get(idvo.getScId());
			 List<VmExtDiskBean> lvd = new ArrayList<VmExtDiskBean>();
				List<ExtDisk> led = serviceCatalogService.getExtDiskListByScId(idvo.getScId());
				for(ExtDisk ed:led){
					VmExtDiskBean vedb = new VmExtDiskBean();
					vedb.setEd_name(ed.getName());
					vedb.setEd_capacity(ed.getCapacity());
					lvd.add(vedb);
				}
			idvo.setExtdisks(lvd);
			idvo.setScname(sc.getName());
		}
		if(logger.isDebugEnabled()){
			logger.debug("exit findDetailVmById method.");
		}
		return idvo;
	}

	@Override
	@Transactional
	public boolean autoMigrateVm(String vmId) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter autoMigrateVm method.");			
		}
		boolean result = false;
		result = operation.autoMigrateVm(vmId);		
		if(logger.isDebugEnabled()){
			logger.debug("exit autoMigrateVm method.");
		}
		return result;
	}
	@Override
	@Transactional(readOnly = false)
	public boolean resizeVm(String vmId, FlavorVO flavorVO,String addDisk,AbstractUser a) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter resizeVm method.");			
		}
		boolean result = false;
		result = operation.resizeVm(vmId, flavorVO,addDisk,a);
		if(logger.isDebugEnabled()){
			logger.debug("exit resizeVm method.");
		}
		return result;
	}
	@Override
	public boolean confirmResizeVm(String vmId, boolean confirmFlag)
			throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter confirmResizeVm method.");			
		}
		boolean result = false;
		result = operation.confirmResizeVm(vmId,confirmFlag);	
		if(logger.isDebugEnabled()){
			logger.debug("exit confirmResizeVm method.");
		}
		return result;
	}
	@Override
	public boolean confirmMigrateVm(String vmId,boolean confirmFlag) throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter confirmMigrateVm method.");			
		}
		boolean result = false;
		result = operation.confirmMigrateVm(vmId,confirmFlag);	
		if(logger.isDebugEnabled()){
			logger.debug("exit confirmMigrateVm method.");
		}
		return result;
	}
	@Override
	@Transactional
	public boolean updateHostNameOfVm(String vmId) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter updateHostNameOfVm method.");			
		}
		boolean result = false;
		result = operation.updateHostNameOfVm(vmId);
		if(logger.isDebugEnabled()){
			logger.debug("exit updateHostNameOfVm method.");
		}
		return result;
	}
	@Override	
	@Transactional(readOnly = false)
	public String publishVm(String vmName,long adminId) throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter publishVm method.");			
		}
		VpdcReference vr = operation.getReferenceByVmName(vmName);
		String zone = vr.getVmZone();
		if(zone.indexOf('$')>0){
			zone = zone.substring(0, zone.indexOf('$'));
		}
		ServerZone sz = zoneService.getServerZoneByCode(zone);
		String ip = iPService.assignIPDetail(sz.getId());
		if(ip==null){
			logger.error("NO IP enough!");
			throw new HsCloudException(Constants.VM_IP_NO_ENOUGH, "createVm——Admin异常", logger, null);
		}
		vr.setVm_status("BUILDING");
		vr.setVm_task_status("BUILDING");
		String vmid = operation.publishVm(vr,ip,adminId);
		if(logger.isDebugEnabled()){
			logger.debug("exit publishVm method.");
		}
		return vmid;
	}
	@Override
	public boolean hasSameVmName(String vmName)
			throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter hasSameVmName method.");			
		}
		return operation.hasSameVmName(vmName);		
	}
	@Override
	@Transactional
	public boolean updateIPOfVm(String uuid, String oldIP, String newIP,long adminId)
			throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter updateIPOfVm method.");			
		}
		boolean result = false;
//		String[] newIPs=newIP.split(",");
		IPDetail ipDetail = null;
		if(StringUtils.isNotEmpty(oldIP) && StringUtils.isEmpty(newIP)){
			result = operation.updateIPOfVm(uuid, oldIP, null,adminId);
		}
//		for(String ip : newIPs){
		ipDetail=iPService.getIPDetailByIP(newIP);
		if(ipDetail != null){
			result = operation.updateIPOfVm(uuid, oldIP, newIP,adminId);
		}
//		}
		if(logger.isDebugEnabled()){
			logger.debug("exit updateIPOfVm method.");
		}
		return result;
	}
	@Override
	public List<VolumeVO> getAttachVolumesOfVm(String vmId)
			throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter getAttachVolumesOfVm method.");			
		}
		//List<VolumeVO> list = operation.getAttachVolumesOfVm(vmId);
		List<VolumeVO> list = operation.getAllAttachVolumesOfVm(vmId);
		if(logger.isDebugEnabled()){
			logger.debug("exit getAttachVolumesOfVm method.");
		}
		return list;
	}
	@Override	
	@Transactional
	public boolean dettachVolume(String vmId, int volumId,int volumeMode,Admin a)
			throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter dettachVolume method.");			
		} 
		boolean result = false;
		result = operation.dettachVolume(vmId, volumId,volumeMode, a);
		if(logger.isDebugEnabled()){
			logger.debug("exit dettachVolume method.");
		}
		return result;
	}
	@Override
	@Transactional
	public boolean updateVmOwmer(long vmOwner, String vmId, long uid)
			throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter updateVmOwmer method.");			
		}
		return operation.updateVmOwmer(vmOwner,vmId,uid);
	}
	@Override
	@Transactional
	public boolean attachVolume(String vmId, String addDisk,AbstractUser a)
			throws HsCloudException {
		boolean bl = false;
		if(logger.isDebugEnabled()){
			logger.debug("enter attachVolume method.");			
		}
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
	public boolean manualMigrateVm(String vmId,String hostName,Admin admin) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter autoMigrateVm method.");			
		}
		boolean result = false;
		String hostIP = "";
		ServerNode serverNode = nodeService.getNodeByName(hostName);
		if(serverNode != null){
			hostIP = serverNode.getInnerIP();
		}
		result = operation.manualMigrateVm(vmId, hostName,admin,hostIP);		
		if(logger.isDebugEnabled()){
			logger.debug("exit autoMigrateVm method.");
		}
		return result;
	}
	@Transactional
	public String verifForTryVm(long referenceId,Long adminId,String zoneCode,String remark) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("OPS-Facade-verifForTryVm start.");			
		} 
		String vmid = "";
		VpdcReference vr = operation.getReferenceById(referenceId);
		
		if(vr!=null){
			List<ExtDisk> led = serviceCatalogService.getExtDiskListByScId(vr.getScId());
			String extDisks = "";
			if(led!=null){
				for(ExtDisk ed:led){
					extDisks += ed.getCapacity()+",";
				}
			}
			if(StringUtils.isNotBlank(zoneCode)){
				vr.setVmZone(zoneCode);
				vr.setNeedToFindOptimZone(false);
			}
			
			vr.setComments(remark);
			/*String zone = vr.getVmZone();
			if(zone.indexOf('$')>0){
				zone = zone.substring(0, zone.indexOf('$'));
			}
			ServerZone sz = zoneService.getServerZoneByCode(zone);
			String ip = iPService.assignIPDetail(sz.getId());
			if(ip==null){
				logger.error("NO IP enough!");
				throw new HsCloudException(Constants.VM_IP_NO_ENOUGH, "createVm——Admin异常", logger, null);
			}*/
			vmid = operation.verifyTryVm(vr,extDisks, adminId);
		}
		return vmid;
	}
	
	@Transactional
	public String verifyForDelayTryVm(long referenceId,int delayLong,Long adminId) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("OPS-Facade-verifyForDelayTryVm start.");			
		}
		operation.verifyForDelayTryVm(referenceId,delayLong,adminId);
		return null;
	}
	
	@Transactional
	public String extraDelayTryVm(long referenceId,int delayLong,Long adminId) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("OPS-Facade-extraDelayTryVm start.");			
		}
		operation.extraDelayTryVm(referenceId,delayLong, adminId);
		return null;
	}
	
	/**
	 * <后台管理员为正式虚拟机延期> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	@Transactional
	public String delayRegularVm(long referenceId,int delayLong,Long adminId) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("OPS-Facade-delayRegularVm start.");			
		}
		operation.delayRegularVm(referenceId,delayLong,adminId);
		return null;
	}	
	
	@Transactional
	public String cancelApplyTryVm(long referenceId,String comments,Long adminId) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("OPS-Facade-cancelApplyTryVm start.");			
		}
		operation.cancelApplyTryVm(referenceId, comments, adminId);
		return null;
	}
	
	public Page<VmInfoVO> listVMForBusiness(Page<VmInfoVO> page,
			String field, String fieldValue, Admin admin,List<Object> referenceIds,
			String type,String status_buss,String sort) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter listVMForBusiness method.");			
		}
		page = operation.FindVmsAdminBussiness(field, fieldValue,
				page,admin,referenceIds,type,status_buss,sort);
		if(logger.isDebugEnabled()){
			logger.debug("exit listVMForBusiness method.");
		}
		return page;
	}
	
	public Page<VmInfoVO> listVMForRecycle(Page<VmInfoVO> page,
			String field, String fieldValue, Admin admin,List<Object> referenceIds,
			String type,String status_buss,String sort) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter listVMForRecycle method.");			
		}
		page = operation.findVmsAdminRecycle(field, fieldValue,
				page,admin,referenceIds,type,status_buss,sort);
		if(logger.isDebugEnabled()){
			logger.debug("exit listVMForRecycle method.");
		}
		return page;
	}
	
	@Override
	public boolean modifyVolume(String vmId, int volumId, int volumeSize,
			Admin a) throws HsCloudException {
		boolean result = false;
		result = operation.modifyExttendDisk(vmId, volumId, volumeSize, a);		
		return result;
	}
	/*******************************Monitor Module********************************/
	@Override
	public WholeOverviewInfoVO getWholeOverviewInfo(List<Object> zoneIds) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter getWholeOverviewInfo method.");			
		}	
		WholeOverviewInfoVO wholeOverviewInfo = null;
		int ipTotal=0;
		int ipUsed =0;
		int ipFree = 0;
		int vmPaidNum = 0;//正式业务的虚拟机数量
		int vmTotalNum = 0;//业务开通总数
		int vmTrialNum = 0;//试用虚拟机数量
		int vmActiveNum = 0;//活动的虚拟机数量
		int hostTotalNum = 0;//节点总数
		int hostActiveNum = 0;//活动节点数
		int virtualCPUTotal = 0;
		int virtualMemoryTotal = 0;
		int virtualDiskTotal = 0;
		int vmWindowsNum = 0;
		int vmLinuxNum = 0;
		int vmOtherNum = 0;
		String osType = null;
		HostAcquisitionBean hostAcquisitionBean = null;
		HostVO hostVO = null;
		wholeOverviewInfo =monitorService.getWholeOverviewInfo();
		List<HostVO> hostError = wholeOverviewInfo.getHostError();
		//用户统计		
//		CountOfUserVO countOfUserVO=userService.getCountOfUser();
//		if(countOfUserVO != null){
//			wholeOverviewInfo.setEnterpriseUserTotal(countOfUserVO.getCountOfEntUser());
//			wholeOverviewInfo.setCommonUserTotal(countOfUserVO.getCountOfNorUser());
//		}		
		IPStatistics iPStatistics=iPService.getAllIPStatistics();
		if(iPStatistics != null){
			ipFree +=iPStatistics.getFreeIPs();
			ipUsed +=(iPStatistics.getAssignedIPs()+iPStatistics.getAssigningIPs()+iPStatistics.getReleasingIPs()+iPStatistics.getDisabledIPs());//已使用包括（已分配、待释放、待分配、禁用）
			ipTotal +=(iPStatistics.getAssignedIPs()+iPStatistics.getFreeIPs()+iPStatistics.getAssigningIPs()+iPStatistics.getDisabledIPs()+iPStatistics.getReleasingIPs());//取所有状态的
		}
		List<ServerNode> serverNodeList = nodeService.getAllNodesByServerZone(zoneIds, null);
		hostTotalNum = serverNodeList.size();
		if(hostTotalNum > 0){
			for(ServerNode serverNode : serverNodeList){
				ServerZone serverZone = serverNode.getServerZone();
				if(serverZone.getIsEnable()==0){
					hostTotalNum-=1;
					continue;
				}
				int cpuCore = 0;
				int memory = 0;
				int disk = 0;
				hostAcquisitionBean = monitorService.getHostAcquisitionBean(serverNode.getName());
				if(hostAcquisitionBean!=null && "true".equalsIgnoreCase(hostAcquisitionBean.getHostStatus())){
					hostActiveNum +=1;
				}else{
					hostVO = new HostVO();
					hostVO.setHostId(serverNode.getId());
					hostVO.setHostName(serverNode.getName());
					hostVO.setNodeAliases(serverNode.getNodeAliases());
					hostVO.setIpOuter(serverNode.getIp());
					hostVO.setZoneCode(serverZone.getCode());
					hostVO.setZoneName(serverZone.getName());
					hostError.add(hostVO);
				}
				cpuCore = Integer.valueOf(serverNode.getCpuInfo());
				memory = Integer.valueOf(serverNode.getRamInfo());
				disk = Integer.valueOf(serverNode.getDiskInfo());
				virtualCPUTotal += cpuCore*serverNode.getCpuRate();
				virtualMemoryTotal += memory*serverNode.getRamRate();
				virtualDiskTotal += disk*serverNode.getDiskRate();
			}
		}
		//正式虚拟机数量和试用虚拟机数量的统计
		List<VpdcReferenceVO> listVms = operation.getAllAvailableVMs(zoneIds,null);
		vmTotalNum = listVms.size();
		if(vmTotalNum>0){				
			for(VpdcReferenceVO vr:listVms){				
				//用户创建的试用主机
				if(vr.getBusinessType()==0){
					vmTrialNum +=1;
				}
				//用户创建的正式主机
				if(vr.getBusinessType()==1 && Constants.USER.equalsIgnoreCase(vr.getCreaterType())){
					vmPaidNum +=1;
				}
				//管理员创建主机
				if(Constants.ADMIN.equalsIgnoreCase(vr.getCreaterType())){
					vmOtherNum +=1;
				}
				if(vr.getOsType() !=null){
					osType = imageService.getImageType().get(vr.getOsType());
				}				
				if(Constants.WINDOWS.equalsIgnoreCase(osType)){
					vmWindowsNum +=1;
				}
				if(Constants.LINUX.equalsIgnoreCase(osType)){
					vmLinuxNum +=1;
				}
				if(vr.getVmStatus()==null || "".equals(vr.getVmStatus())){
					continue;
				}
				if(vr.getVmStatus().equalsIgnoreCase("ACTIVE")){
					vmActiveNum +=1;
				}
			}				
		}
		//避免监控得到的虚拟机数量与hscloud数据库中的记录不一致，以hscloud数据库为准
		wholeOverviewInfo.setVmTotal(vmTotalNum);
		wholeOverviewInfo.setVmActiveNum(vmActiveNum);
		//IP数量
		wholeOverviewInfo.setIpFree(ipFree);
		wholeOverviewInfo.setIpUsed(ipUsed);
		wholeOverviewInfo.setIpTotal(ipTotal);
		//用户申请虚拟机数量(试用、正式)
		wholeOverviewInfo.setVmPaidNum(vmPaidNum);
		wholeOverviewInfo.setVmTrialNum(vmTrialNum);
		//管理员创建主机
		wholeOverviewInfo.setVmOtherNum(vmOtherNum);
		//windows虚拟机数量和linux虚拟机数量
		wholeOverviewInfo.setVmWindowsNum(vmWindowsNum);
		wholeOverviewInfo.setVmLinuxNum(vmLinuxNum);
		//节点统计
		wholeOverviewInfo.setHostActiveNum(hostActiveNum);
		wholeOverviewInfo.setHostTotal(hostTotalNum);
		//虚拟资源统计
		wholeOverviewInfo.setVirtualCPUTotal(virtualCPUTotal);
		wholeOverviewInfo.setVirtualMemoryTotal(virtualMemoryTotal);
		wholeOverviewInfo.setVirtualDiskTotal(virtualDiskTotal);
		if(logger.isDebugEnabled()){
			logger.debug("exit getWholeOverviewInfo method.");
		}
		return wholeOverviewInfo;
	}
	@Override
	public ZoneOverviewInfoVO getZoneOverviewInfo(String zoneCode, List<Object> zoneIds)
			throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter getWholeOverviewInfo method.");			
		}	
		ZoneOverviewInfoVO zoneOverviewInfo = null;
		int ipTotal = 0;
		int ipUsed = 0;
		int ipFree = 0;
		int vmPaidNum = 0;//正式业务的虚拟机数量
		int vmTotalNum = 0;//业务开通总数
		int vmTrialNum = 0;//试用虚拟机数量
		int vmActiveNum = 0;//活动的虚拟机数量
		int hostTotalNum = 0;//节点总数
		int hostActiveNum = 0;//活动节点数
		int virtualCPUTotal = 0;
		int virtualMemoryTotal = 0;
		int virtualDiskTotal = 0;
		int vmWindowsNum = 0;
		int vmLinuxNum = 0;
		int vmOtherNum = 0;
		String osType = null;
		HostAcquisitionBean hostAcquisitionBean = null;
		zoneOverviewInfo =monitorService.getZoneOverviewInfo(zoneCode);
				
		IPStatistics iPStatistics=iPService.getIPStatisticsByzoneCode(zoneCode);
		if(iPStatistics != null){
			ipFree +=iPStatistics.getFreeIPs();
			ipUsed +=(iPStatistics.getAssignedIPs()+iPStatistics.getAssigningIPs()+iPStatistics.getReleasingIPs()+iPStatistics.getDisabledIPs());//已使用包括（已分配、待释放、待分配、禁用）
			ipTotal +=(iPStatistics.getAssignedIPs()+iPStatistics.getFreeIPs()+iPStatistics.getAssigningIPs()+iPStatistics.getDisabledIPs()+iPStatistics.getReleasingIPs());//取所有状态的
		}
		List<ServerNode> serverNodeList = nodeService.getAllNodesByServerZone(zoneIds, zoneCode);
		hostTotalNum = serverNodeList.size();
		if(hostTotalNum > 0){
			for(ServerNode serverNode : serverNodeList){
				int cpuCore = 0;
				int memory = 0;
				int disk = 0;
				hostAcquisitionBean = monitorService.getHostAcquisitionBean(serverNode.getName());
				if(hostAcquisitionBean!=null && "true".equalsIgnoreCase(hostAcquisitionBean.getHostStatus())){
					hostActiveNum +=1;
				}
				cpuCore = Integer.valueOf(serverNode.getCpuInfo());
				memory = Integer.valueOf(serverNode.getRamInfo());
				disk = Integer.valueOf(serverNode.getDiskInfo());
				virtualCPUTotal += cpuCore*serverNode.getCpuRate();
				virtualMemoryTotal += memory*serverNode.getRamRate();
				virtualDiskTotal += disk*serverNode.getDiskRate();
			}
		}
		//正式虚拟机数量和试用虚拟机数量的统计
		List<VpdcReferenceVO> listVms = operation.getAllAvailableVMs(zoneIds,zoneCode);
		vmTotalNum = listVms.size();
		if(vmTotalNum>0){				
			for(VpdcReferenceVO vr:listVms){				
				//用户创建的试用主机
				if(vr.getBusinessType()==0){
					vmTrialNum +=1;
				}
				//用户创建的正式主机
				if(vr.getBusinessType()==1 && Constants.USER.equalsIgnoreCase(vr.getCreaterType())){
					vmPaidNum +=1;
				}
				//管理员创建主机
				if(Constants.ADMIN.equalsIgnoreCase(vr.getCreaterType())){
					vmOtherNum +=1;
				}
				if(vr.getOsType() !=null){
					osType = imageService.getImageType().get(vr.getOsType());
				}				
				if(Constants.WINDOWS.equalsIgnoreCase(osType)){
					vmWindowsNum +=1;
				}
				if(Constants.LINUX.equalsIgnoreCase(osType)){
					vmLinuxNum +=1;
				}
				if(vr.getVmStatus()==null || "".equals(vr.getVmStatus())){
					continue;
				}
				if(vr.getVmStatus().equalsIgnoreCase("ACTIVE")){
					vmActiveNum +=1;
				}
			}				
		}
		//避免监控得到的虚拟机数量与hscloud数据库中的记录不一致，以hscloud数据库为准
		zoneOverviewInfo.setVmTotal(vmTotalNum);
		zoneOverviewInfo.setVmActiveNum(vmActiveNum);
		//IP数量
		zoneOverviewInfo.setIpFree(ipFree);
		zoneOverviewInfo.setIpUsed(ipUsed);
		zoneOverviewInfo.setIpTotal(ipTotal);
		//用户申请虚拟机数量(试用、正式)
		zoneOverviewInfo.setVmPaidNum(vmPaidNum);
		zoneOverviewInfo.setVmTrialNum(vmTrialNum);
		//管理员创建主机
		zoneOverviewInfo.setVmOtherNum(vmOtherNum);
		//windows虚拟机数量和linux虚拟机数量
		zoneOverviewInfo.setVmWindowsNum(vmWindowsNum);
		zoneOverviewInfo.setVmLinuxNum(vmLinuxNum);
		//节点统计
		zoneOverviewInfo.setHostTotal(hostTotalNum);
		zoneOverviewInfo.setHostActiveNum(hostActiveNum);
		//虚拟资源统计
		zoneOverviewInfo.setVirtualCPUTotal(virtualCPUTotal);
		zoneOverviewInfo.setVirtualMemoryTotal(virtualMemoryTotal);
		zoneOverviewInfo.setVirtualDiskTotal(virtualDiskTotal);
		if(logger.isDebugEnabled()){
			logger.debug("exit getZoneOverviewInfo method.");
		}
		return zoneOverviewInfo;
	}
	@Override
	public HostOverviewInfoVO getHostOverviewInfo(String hostName) throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter getHostOverviewInfo method.");			
		}
		HostOverviewInfoVO hostOverviewInfoVO = null;		
		hostOverviewInfoVO = monitorService.getHostOverviewInfo(hostName);
		int cpuCore = 0;
		int memory = 0;
		int disk = 0;
		int virtualCPUTotal = 0;
		int virtualMemoryTotal = 0;
		int virtualDiskTotal = 0;
		int cpuRate = 0;
		int ramRate = 0;
		int diskRate = 0;
		String cpuType = null; 
		String ipOuter = null;
		long zoneId = 0;
		String zoneName = null;
		String zoneCode = null;
		ServerNode node = nodeService.getNodeByName(hostName);
		if(node != null){
			ServerZone serverZone = node.getServerZone();
			cpuCore = Integer.valueOf(node.getCpuInfo());
			memory = Integer.valueOf(node.getRamInfo());
			disk = Integer.valueOf(node.getDiskInfo());
			virtualCPUTotal = cpuCore*node.getCpuRate();
			virtualMemoryTotal = memory*node.getRamRate();
			virtualDiskTotal = disk*node.getDiskRate();
			cpuType = node.getCpuType();
			ipOuter = node.getIp();
			cpuRate = node.getCpuRate();
			ramRate = node.getRamRate();
			diskRate = node.getDiskRate();
			if(serverZone!=null){
				zoneId = serverZone.getId();
				zoneName = serverZone.getName();
				zoneCode = serverZone.getCode();
			}
		}		
		//左侧物理机信息
		hostOverviewInfoVO.setCpuCore(cpuCore);
		hostOverviewInfoVO.setCpuType(cpuType);
		hostOverviewInfoVO.setMemory(memory);
		hostOverviewInfoVO.setDisk(disk);
		hostOverviewInfoVO.setIpOuter(ipOuter);
		hostOverviewInfoVO.setCpuRate(cpuRate);
		hostOverviewInfoVO.setMemoryRate(ramRate);
		hostOverviewInfoVO.setDiskRate(diskRate);	
		hostOverviewInfoVO.setVirtualCPUTotal(virtualCPUTotal);
		hostOverviewInfoVO.setVirtualMemoryTotal(virtualMemoryTotal);
		hostOverviewInfoVO.setVirtualDiskTotal(virtualDiskTotal);
		hostOverviewInfoVO.setZoneId(zoneId);
		hostOverviewInfoVO.setZoneName(zoneName);
		hostOverviewInfoVO.setZoneCode(zoneCode);
		if(logger.isDebugEnabled()){
			logger.debug("exit getHostOverviewInfo method.");
		}
		return hostOverviewInfoVO;
	}
	@Override
	public VmOverviewInfoVO getVmOverviewInfo(String vmId) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter getVmOverviewInfo method.");			
		}
		VmOverviewInfoVO vmOverviewInfoVO = null;		
		vmOverviewInfoVO = monitorService.getVmOverviewInfo(vmId);
		
		InstanceDetailVo instanceDetailVo = operation.findVm(vmId);
		if(instanceDetailVo != null){
			String[] list=instanceDetailVo.getIp().split(",");
			if(list.length==1){
				vmOverviewInfoVO.setIpInner(list[0]);
			}
			if(list.length>=2){
				vmOverviewInfoVO.setIpInner(list[0]);
				String ipout = ""; 
				for(int i=1;i<list.length;i++){
					ipout += list[i]+",";
				}
				vmOverviewInfoVO.setIpOuter(ipout.substring(0,ipout.length()-1));
			}
			vmOverviewInfoVO.setVmName(instanceDetailVo.getVmName());
			vmOverviewInfoVO.setCpuCore(instanceDetailVo.getCpu());
			vmOverviewInfoVO.setCpuType(instanceDetailVo.getCpuName());
			vmOverviewInfoVO.setMemory(instanceDetailVo.getMemory());
			vmOverviewInfoVO.setDisk(instanceDetailVo.getDisk());
			vmOverviewInfoVO.setImage(instanceDetailVo.getOs());
			vmOverviewInfoVO.setCatalog(instanceDetailVo.getScname());
		}			
		if(logger.isDebugEnabled()){
			logger.debug("exit getVmOverviewInfo method.");
		}
		return vmOverviewInfoVO;
	}
	@Override
	public Page<HostInfoVO> findHostDetailsByCondition(Page<HostInfoVO> page,
			String field, String fieldValue,String zoneCode,List<Object> zoneIds) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter findHostDetailsByCondition method.");			
		}
		int cpuCore = 0;
		double memory = 0;
		double disk = 0;
		int vmActiveNum = 0;
		int vmTotalNum = 0;
		Page<ServerNode> nodePage = new Page<ServerNode>();		
		List<ServerNode> nodeList = null;
		List<HostInfoVO> hostInfoList = new ArrayList<HostInfoVO>();
		HostInfoVO hostInfoVO = null;		
		nodePage.setPageNo(page.getPageNo());
		nodePage.setPageSize(page.getPageSize());
		nodePage=nodeService.findHostsByCondition(nodePage, field, fieldValue,zoneCode,zoneIds);
		nodeList=nodePage.getResult();
		for(ServerNode serverNode : nodeList){
			vmActiveNum = 0;
			vmTotalNum = 0;
			hostInfoVO =new HostInfoVO();
			IPMIManager ipmiManager = null;
			IPMIConfig ipmiConfig = serverNode.getIpmiConfig();
			if(ipmiConfig != null){
				hostInfoVO.setIpmiConfig(ipmiConfig);
				try{
					if(!"".equals(ipmiConfig.getIp()) && !"".equals(ipmiConfig.getUserName()) && !"".equals(ipmiConfig.getPassword())){
						ipmiManager = nodeService.connectIPMIManager(serverNode.getIpmiConfig());
					}
					if(ipmiManager!= null && ipmiManager.getHostImpiInfo()!= null){
						hostInfoVO.setHostIpmiInfo(ipmiManager.getHostImpiInfo());
					}
				}catch (Exception e) {
					logger.error("connectIPMIManager Exception:", e);
				}
			}
			NodeIsolationConfig nodeIsolationConfig = serverNode.getNodeIsolationConfig();
			if(nodeIsolationConfig != null){
				hostInfoVO.setNodeIsolationConfig(nodeIsolationConfig);
			}
			hostInfoVO.setHostId(serverNode.getId());
			hostInfoVO.setHostName(serverNode.getName());
			hostInfoVO.setNodeAliases(serverNode.getNodeAliases());
			hostInfoVO.setIpOuter(serverNode.getIp());
			hostInfoVO.setIpInner(serverNode.getInnerIP());
			hostInfoVO.setCpuType(serverNode.getCpuType());	
			hostInfoVO.setHostZone(serverNode.getZone());
			if(serverNode.getCpuInfo() != null && !"".equals(serverNode.getCpuInfo())){
				cpuCore = Integer.valueOf(serverNode.getCpuInfo());
			}
			if(serverNode.getRamInfo() != null && !"".equals(serverNode.getRamInfo())){
				memory = Double.valueOf(serverNode.getRamInfo());
			}
			if(serverNode.getDiskInfo() != null && !"".equals(serverNode.getDiskInfo())){
				disk = Double.valueOf(serverNode.getDiskInfo());
			}
			hostInfoVO.setCpuCore(cpuCore);
			hostInfoVO.setMemory(memory);
			hostInfoVO.setDisk(disk);
			ServerZone serverZone=serverNode.getServerZone();
			if(serverZone != null){
				hostInfoVO.setZoneId(serverZone.getId());
				hostInfoVO.setZoneName(serverZone.getName());
				hostInfoVO.setZoneCode(serverZone.getCode());
			}			
			hostInfoVO.setCpuRate(serverNode.getCpuRate());
			hostInfoVO.setMemoryRate(serverNode.getRamRate());
			hostInfoVO.setDiskRate(serverNode.getDiskRate());
			hostInfoVO.setNodeAliases(serverNode.getNodeAliases());
			hostInfoVO.setIsEnable(serverNode.getIsEnable());
			List<VpdcReferenceVO> VpdcReferenceVOList = operation.getAllAvailableVMs(serverNode.getName(), null);
			vmTotalNum = VpdcReferenceVOList.size();
			for(VpdcReferenceVO vr : VpdcReferenceVOList){
				if(vr.getVmStatus()==null || "".equals(vr.getVmStatus())){
					continue;
				}
				if(vr.getVmStatus().toUpperCase().equals("ACTIVE")){
					vmActiveNum +=1;
				}
			}
			hostInfoVO.setVmActive(vmActiveNum);
			hostInfoVO.setVmTotal(vmTotalNum);
			monitorService.fillHostMonitoringInformation(hostInfoVO);
			hostInfoList.add(hostInfoVO);
		}
		page.setTotalCount(nodePage.getTotalCount());
		page.setResult(hostInfoList);
//		page = monitorService.findHostDetailsByCondition(page, field, fieldValue);
		if(logger.isDebugEnabled()){
			logger.debug("exit findHostDetailsByCondition method.");
		}
		return page;
	}
	@Override
	public Page<VmInfoMonitorVO> findVmDetailsByCondition(Page<VmInfoVO> page,
			String hostName, String field, String fieldValue, Admin admin,
			List<Object> referenceIds,String zoneCode) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter findVmDetailsByCondition method.");			
		}
		Page<VmInfoMonitorVO> vmInfoMonitorPage = new Page<VmInfoMonitorVO>();
		page = operation.fuzzyFindVmsAdmin(hostName,field, fieldValue,page,admin,referenceIds,"0",zoneCode);
		List<VmInfoVO> vmInfoListVO = page.getResult();
		List<VmInfoMonitorVO> vmInfoList = new ArrayList<VmInfoMonitorVO>();
//		ScIsolationConfig scIsolationConfig = null;
//		int scId = 0;
		ServerNode serverNode = null;
		for(VmInfoVO vmInfoVO:vmInfoListVO){
			VmInfoMonitorVO vmInfoMonitorVO = null;
			CPUMonitorDetailBean cpuMonitorDetailBean = null;
			IOPSMonitorDetailBean iopsMonitorDetailBean = null;
			NETMonitorDetailBean netMonitorDetailBean = null;
			DISKMonitorDetailBean diskMonitorDetailBean = null;
			
			if(vmInfoVO == null){
				continue;
			}
			serverNode = nodeService.getNodeByName(vmInfoVO.getHostName());
			if(serverNode != null){
				vmInfoVO.setHostAliases(serverNode.getNodeAliases());
			}
			cpuMonitorDetailBean = new CPUMonitorDetailBean();
			iopsMonitorDetailBean = new IOPSMonitorDetailBean();
			netMonitorDetailBean = new NETMonitorDetailBean();
			diskMonitorDetailBean = new DISKMonitorDetailBean();
			cpuMonitorDetailBean.setWorkloadLimit(vmInfoVO.getCpuLimit());
			iopsMonitorDetailBean.setReadLimit(vmInfoVO.getDiskRead());
			iopsMonitorDetailBean.setWriteLimit(vmInfoVO.getDiskWrite());
			netMonitorDetailBean.setReadLimit(vmInfoVO.getBandWidthIn());
			netMonitorDetailBean.setWriteLimit(vmInfoVO.getBandWidthOut());			
        	try{        		
        		JSONObject jsonObject = JSONObject.fromObject(vmInfoVO.toJsonString());
        		vmInfoMonitorVO = (VmInfoMonitorVO) JSONObject.toBean(jsonObject, VmInfoMonitorVO.class);
        		vmInfoMonitorVO.setCpuMonitorDetailBean(cpuMonitorDetailBean);
        		vmInfoMonitorVO.setIopsMonitorDetailBean(iopsMonitorDetailBean);
        		vmInfoMonitorVO.setNetMonitorDetailBean(netMonitorDetailBean);
        		vmInfoMonitorVO.setDiskMonitorDetailBean(diskMonitorDetailBean);
	            monitorService.fillServerMonitoringInformation(vmInfoMonitorVO);	             		 
        	}catch (Exception ex) {        		
				logger.error("fillServerMonitoringInformation Exception:",ex);
			}        			
			vmInfoList.add(vmInfoMonitorVO);
		}
		vmInfoMonitorPage.setResult(vmInfoList);
		vmInfoMonitorPage.setPageNo(page.getPageNo());
		vmInfoMonitorPage.setPageSize(page.getPageSize());
		vmInfoMonitorPage.setTotalCount(page.getTotalCount());
//		page = monitorService.findVmDetailsByCondition(page, hostName, field, fieldValue, referenceIds);
		if(logger.isDebugEnabled()){
			logger.debug("exit findVmDetailsByCondition method.");
		}
		return vmInfoMonitorPage;
	}
	@Override
	public VmDetailInfoVO getVmDetailInfo(String vmId) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter getVmDetailInfo method.");			
		}
		VmDetailInfoVO vmDetailInfoVO = new VmDetailInfoVO();
		long catalogRemain=0;	
		long catalogUsed=0;
		InstanceDetailVo instanceDetailVo = operation.findVm(vmId);
		if(instanceDetailVo==null){
			return vmDetailInfoVO;
		}
		if(instanceDetailVo!=null && instanceDetailVo.getSpare()!=null){
			if(instanceDetailVo.getSpare()<=0){
				catalogRemain=0;
			}else{
				catalogRemain =instanceDetailVo.getSpare();
			}	
		}
		catalogUsed=instanceDetailVo.getRunTime()==null?0:instanceDetailVo.getRunTime().getTime();
		if(instanceDetailVo.getIp()!=null){
			String[] list=instanceDetailVo.getIp().split(",");
			if(list.length==1){
				vmDetailInfoVO.setIpInner(list[0]);
			}
			if(list.length>=2){
				vmDetailInfoVO.setIpInner(list[0]);
				String ipout = ""; 
				for(int i=1;i<list.length;i++){
					ipout += list[i]+",";
				}
				vmDetailInfoVO.setIpOuter(ipout.substring(0,ipout.length()-1));
			}
		}
		vmDetailInfoVO.setVmId(instanceDetailVo.getVmId());
		vmDetailInfoVO.setVmName(instanceDetailVo.getVmName());
		vmDetailInfoVO.setCpuCore(instanceDetailVo.getCpu());
		vmDetailInfoVO.setCpuType(instanceDetailVo.getCpuName());
		if(instanceDetailVo.getMemory()!=null){
			vmDetailInfoVO.setMemory(Double.valueOf(instanceDetailVo.getMemory().doubleValue()));
		}
		if(instanceDetailVo.getDisk()!=null){
			vmDetailInfoVO.setDisk(Double.valueOf(instanceDetailVo.getDisk().doubleValue()));			
		}
		vmDetailInfoVO.setZoneName(instanceDetailVo.getZoneName());
		vmDetailInfoVO.setNetwork(instanceDetailVo.getNetwork());
		vmDetailInfoVO.setVmOS(instanceDetailVo.getOs());		
		vmDetailInfoVO.setCatalogName(instanceDetailVo.getScname());
		vmDetailInfoVO.setCatalogDate(instanceDetailVo.getEffectiveDate());
		vmDetailInfoVO.setCatalogRemain(catalogRemain);
		vmDetailInfoVO.setCatalogUsed(catalogUsed);
		vmDetailInfoVO.setOrderNumber(instanceDetailVo.getOrderNo());
		vmDetailInfoVO.setOrderDate(instanceDetailVo.getOrderDate());
		vmDetailInfoVO.setBillingModel(instanceDetailVo.getPricePeriodType());
		vmDetailInfoVO.setPrice(instanceDetailVo.getPrice());
		vmDetailInfoVO.setPriceUnit("");
		if(instanceDetailVo.getVmType() ==0 && instanceDetailVo.getVmBusineStatus()!=null && instanceDetailVo.getVmBusineStatus()==0){
			List<VmExtDiskBean> lvd = new ArrayList<VmExtDiskBean>();
			List<ExtDisk> led = serviceCatalogService.getExtDiskListByScId(instanceDetailVo.getScId());
			for(ExtDisk ed:led){
				VmExtDiskBean vedb = new VmExtDiskBean();
				vedb.setEd_name(ed.getName());
				vedb.setEd_capacity(ed.getCapacity());
				lvd.add(vedb);
			}
			instanceDetailVo.setExtdisks(lvd);
		}
		if(instanceDetailVo.getExtdisks()!=null){
			vmDetailInfoVO.setExtdisks(instanceDetailVo.getExtdisks());
		}		
		if(logger.isDebugEnabled()){
			logger.debug("exit getVmDetailInfo method.");
		}
		return vmDetailInfoVO;
	}
	@Override
	public List<CPUHistoryVO> getVmCPUHistory(String vmId, long fromTime,
			long toTime) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter getVmCPUHistory method.");			
		}
		List<CPUHistoryVO> cPUHistoryVOList = monitorService.getVmCPUHistory(vmId, fromTime, toTime);
		if(logger.isDebugEnabled()){
			logger.debug("exit getVmCPUHistory method.");
		}
		return cPUHistoryVOList;
	}
	@Override
	public List<MemoryHistoryVO> getVmMemoryHistory(String vmId, long fromTime,
			long toTime) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter getVmMemoryHistory method.");			
		}
		List<MemoryHistoryVO> memoryHistoryVOList = monitorService.getVmMemoryHistory(vmId, fromTime, toTime);
		if(logger.isDebugEnabled()){
			logger.debug("exit getVmMemoryHistory method.");
		}
		return memoryHistoryVOList;
	}
	@Override
	public List<DiskHistoryVO> getVmDiskHistory(String vmId, long fromTime,
			long toTime) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter getVmDiskHistory method.");			
		}
		List<DiskHistoryVO> diskHistoryVOList = monitorService.getVmDiskHistory(vmId, fromTime, toTime);
		if(logger.isDebugEnabled()){
			logger.debug("exit getVmDiskHistory method.");
		}
		return diskHistoryVOList;
	}
	@Override
	public List<NetHistoryVO> getVmNetHistory(String vmId, long fromTime,
			long toTime) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter getVmNetHistory method.");			
		}
		List<NetHistoryVO> netHistoryVOList = monitorService.getVmNetHistory(vmId, fromTime, toTime);
		if(logger.isDebugEnabled()){
			logger.debug("exit getVmNetHistory method.");
		}
		return netHistoryVOList;
	}
	@Override
	public List<CPUHistoryVO> getHostCPUHistory(String hostName, long fromTime,
			long toTime) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter getHostCPUHistory method.");			
		}
		List<CPUHistoryVO> cPUHistoryVOList = monitorService.getHostCPUHistory(hostName, fromTime, toTime);
		if(logger.isDebugEnabled()){
			logger.debug("exit getHostCPUHistory method.");
		}
		return cPUHistoryVOList;
	}
	@Override
	public List<MemoryHistoryVO> getHostMemoryHistory(String hostName,
			long fromTime, long toTime) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter getHostMemoryHistory method.");			
		}
		List<MemoryHistoryVO> memoryHistoryVOList = monitorService.getHostMemoryHistory(hostName, fromTime, toTime);
		if(logger.isDebugEnabled()){
			logger.debug("exit getHostMemoryHistory method.");
		}
		return memoryHistoryVOList;
	}
	@Override
	public List<DiskHistoryVO> getHostDiskHistory(String hostName,
			long fromTime, long toTime) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter getHostDiskHistory method.");			
		}
		List<DiskHistoryVO> diskHistoryVOList = monitorService.getHostDiskHistory(hostName, fromTime, toTime);
		if(logger.isDebugEnabled()){
			logger.debug("exit getHostDiskHistory method.");
		}
		return diskHistoryVOList;
	}
	@Override
	public List<NetHistoryVO> getHostNetHistory(String hostName, long fromTime,
			long toTime) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter getHostNetHistory method.");			
		}
		List<NetHistoryVO> netHistoryVOList = monitorService.getHostNetHistory(hostName, fromTime, toTime);
		if(logger.isDebugEnabled()){
			logger.debug("exit getHostNetHistory method.");
		}
		return netHistoryVOList;
	}
	@Override
	public List<VmRealtimeMonitorVO> getVmRealTimeMonitor(String vmId) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter getVmRealTimeMonitor method.");			
		}
		List<VmRealtimeMonitorVO> vmRealtimeMonitorVOList = monitorService.getVmRealTimeMonitor(vmId);
		if(logger.isDebugEnabled()){
			logger.debug("exit getVmRealTimeMonitor method.");
		}
		return vmRealtimeMonitorVOList;
	}
	@Override
	public String getNodeAndVmTree(String nodeName,List<Object> referenceIds) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter getNodeAndVmTree method.");			
		}
		StringBuffer returnStr = new StringBuffer();
			List<VpdcInstance> listVms=operation.getVmIdsByNodeName(nodeName,referenceIds);
			if(listVms.size()>0){				
				for(VpdcInstance vi:listVms){
					returnStr.append("{text:");
					returnStr.append("\"" + vi.getVpdcreference().getName() + "\"");
					returnStr.append(",icon:'images/vmTree.png'");
					returnStr.append(",qtip:");
					returnStr.append("\"" + vi.getVmId() + "\"");
					returnStr.append(",leaf:true},");					
				}				
			}
		if(logger.isDebugEnabled()){
			logger.debug("exit getNodeAndVmTree method.");
		}
		return ("[" + returnStr + "]").replaceAll(",]", "]");
	}
	@Override
	public List<HostInfoVO> getAllHostUsage(String zoneCode) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter getAllHostUsage method.");			
		}
		HostInfoVO hostInfoVO = null;
		List<HostInfoVO> hostInfoList = new ArrayList<HostInfoVO>();
		List<ServerNode> nodeList = nodeService.getAllNodesByServerZone(zoneCode);
		for(ServerNode serverNode : nodeList){
			hostInfoVO =new HostInfoVO();						
			if(serverNode.getCpuInfo() == null || "".equals(serverNode.getCpuInfo())){
				hostInfoVO.setCpuCore(0);
			}else{
				hostInfoVO.setCpuCore(Integer.valueOf(serverNode.getCpuInfo()));
			}
			if(serverNode.getRamInfo() == null || "".equals(serverNode.getRamInfo())){
				hostInfoVO.setMemory(0);
			}else{
				hostInfoVO.setMemory(Double.valueOf(serverNode.getRamInfo()));
			}
			if(serverNode.getDiskInfo() == null || "".equals(serverNode.getDiskInfo())){
				hostInfoVO.setDisk(0);
			}else{
				hostInfoVO.setDisk(Double.valueOf(serverNode.getDiskInfo()));
			}
			hostInfoVO.setHostId(serverNode.getId());
			hostInfoVO.setHostName(serverNode.getName());
			hostInfoVO.setNodeAliases(serverNode.getNodeAliases());
			hostInfoVO.setCpuRate(serverNode.getCpuRate());
			hostInfoVO.setMemoryRate(serverNode.getRamRate());
			hostInfoVO.setDiskRate(serverNode.getDiskRate());
			hostInfoList.add(hostInfoVO);
		}
		List<HostInfoVO> hostInfoVOList = monitorService.getAllHostUsage(hostInfoList);
		if(logger.isDebugEnabled()){
			logger.debug("exit getAllHostUsage method.");
		}
		return hostInfoVOList;
	}
	@Override
	public List<Role> getAllRole() {
		return roleService.getAllRole();
	}

	@Override
	@Transactional
	public void addAdmin(String password,String telephone,String name,String email,Long roleId,Integer adminType) {
		Admin admin=new Admin();
		admin.setName(name);
		admin.setEmail(email);
		admin.setPassword(password);
		admin.setIsSuper(false);
		admin.setEnable((short)3);
		admin.setTelephone(telephone);
		admin.setType(adminType);
		adminService.addAdmin(admin, roleId);
	}

	/*******************************SC Module********************************/
	@Override
	@Transactional
	public void save(ServerNode node) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter savenode method.");			
		}
		boolean setIPMIFlag = false;
		IPMIConfig ipmiConfig=node.getIpmiConfig();
		if(ipmiConfig!=null && ipmiConfig.getIsConsumptionLimit()>0 && ipmiConfig.getPowerConsumption()>0){
			setIPMIFlag = true;
		}
		if(ipmiConfig !=null && ipmiConfig.getId()>0 && setIPMIFlag){
			IPMIConfig ipmiCon =ipmiConfigService.getIPMIConfigById(ipmiConfig.getId());			
			IPMIManager IPMIManager = nodeService.connectIPMIManager(ipmiCon);
			if(setIPMIFlag){
				ipmiCon.setIsConsumptionLimit(ipmiConfig.getIsConsumptionLimit());
				ipmiCon.setPowerConsumption(ipmiConfig.getPowerConsumption());
				node.setIpmiConfig(ipmiCon);
				IPMIManager.enablePowerLimit(ipmiConfig.getPowerConsumption());
			}else{
				IPMIManager.disablePowerLimit();
			}
			node.setIpmiConfig(ipmiCon);
		}
		nodeService.save(node) ;
		if(logger.isDebugEnabled()){
			logger.debug("exit savenode method.");
		}
	}
	@Override
	public List<ServerNode> getAllNodes() throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter getAllNodes method.");			
		}
		String status = null;
		List<ServerNode> nodeList = new ArrayList<ServerNode>();
		List<ServerNode> serverNodeList = nodeService.getAllNodes();
		for(ServerNode node : serverNodeList){
			HostAcquisitionBean hostAcquisitionBean = monitorService.getHostAcquisitionBean(node.getName());
			status = hostAcquisitionBean.getHostStatus();
			if(status!=null && "true".equalsIgnoreCase(status)){
				nodeList.add(node);
			}			
		}
		if(logger.isDebugEnabled()){
			logger.debug("exit getAllNodes method.");
		}
		return nodeList;
	}
	@Override
	public ServerNode getNodeById(int nodeId) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter getNodeById method.");			
		}
		ServerNode serverNode = nodeService.getNodeById(nodeId);
		if(logger.isDebugEnabled()){
			logger.debug("exit getNodeById method.");
		}
		return serverNode;
	}
	@Override
	public ServerNode getNodeByName(String name) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter getNodeByName method.");			
		}
		ServerNode serverNode = nodeService.getNodeByName(name);
		if(logger.isDebugEnabled()){
			logger.debug("exit getNodeByName method.");
		}
		return serverNode;
	}
	@Override
	public void delete(long nodeId) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter deletenode method.");			
		}
		nodeService.delete(nodeId) ;
		if(logger.isDebugEnabled()){
			logger.debug("exit deletenode method.");
		}
	}
	@Override
	public boolean hasSameNodeName(ServerNode serverNode)
			throws HsCloudException {
		return nodeService.hasSameNodeName(serverNode);
	}
	@Override
	public List<ServerNode> getNewNodes() throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter getNewNodes method.");			
		}
		List<ServerNode>  ServerNodeList = nodeService.getNewNodes();
		if(logger.isDebugEnabled()){
			logger.debug("exit getNewNodes method.");
		}
		return ServerNodeList;
	}
	@Override
	public String getNodeTree(List<Object> nodeIds,String zoneCode)
			throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter getNodeTree method.");			
		}
		StringBuffer returnStr = new StringBuffer();		
		List<ServerNode> listNode=nodeService.getAllNodes(nodeIds,zoneCode);
		for(ServerNode sn:listNode){
			returnStr.append("{text:");
			returnStr.append("\"" + sn.getNodeAliases() + "\"");
			returnStr.append(",icon:'images/nodeTree.png'");
			returnStr.append(",expanded:false");
			returnStr.append(",depth:2");
			returnStr.append(",qtip:");
			returnStr.append("\"" + sn.getName() + "\"");
			returnStr.append(",leaf:true},");
		}		
		if(logger.isDebugEnabled()){
			logger.debug("exit getNodeTree method.");
		}
		return ("[" + returnStr + "]").replaceAll(",]", "]");
	}
	@Override
	public Map<String, List<ServerNode>> getAllNodesGroupByZone()
			throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter getAllNodesGroupByZone method.");			
		} 
		return nodeService.getAllNodesGroupByZone();
	}
	@Override
	public List<ServerNode> getAllNodesByServerZone(long zoneId)
			throws HsCloudException {
		List<ServerNode> nodeList = nodeService.getAllNodesByServerZone(zoneId);
		return nodeList;
	}
	@Override
	public void synchronizationAllNodeIsolation() throws HsCloudException {
		nodeService.synchronizationAllNodeIsolation();
		
	}
	@Override
	@Transactional
	public long createServerZone(ServerZone zone) throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter createServerZone method.");			
		} 
		return zoneService.createServerZone(zone);
	}
	@Override
	@Transactional
	public boolean deleteServerZone(long id) throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter deleteServerZone method.");			
		} 
		return zoneService.deleteServerZone(id);
	}
	@Override
	public Page<ServerZone> findServerZone(Page<ServerZone> page,String field, String fieldValue)
			throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter findServerZone method.");			
		}
		return zoneService.findServerZone(page,field,fieldValue);
	}
	@Override
	public List<ServerZone> getAllZones(List<Object> zoneIds) throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter getAllZones method.");			
		}
		return zoneService.getAllZones(zoneIds);
	}
	@Override
	@Transactional
	public boolean updateServerZone(ServerZone serverZone)
			throws HsCloudException {
		rolePermissionService.deleteZoneOfRole(serverZone.getId());
		return zoneService.updateServerZone(serverZone);
	}
	@Override
	@Transactional
	public boolean setDefaultServerZone(ServerZone serverZone)
			throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter setDefaultServerZone method.");			
		}
		return zoneService.setDefaultServerZone(serverZone);
	}
	@Override
	public ServerZone getServerZoneById(long id) throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter getServerZoneById method.");			
		}
		return zoneService.getServerZoneById(id);
	}
	@Override
	public boolean hasSameZoneName(ServerZone serverZone)
			throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter hasSameZoneName method.");			
		} 
		return zoneService.hasSameZoneName(serverZone);
	}
	@Override
	public boolean hasSameZoneCode(ServerZone serverZone)
			throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter hasSameZoneCode method.");			
		} 
		return zoneService.hasSameZoneCode(serverZone);
	}
	@Override
	public List<ServerZone> getServerZonesByCode(String code)
			throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter getServerZonesByCode method.");			
		}
		return zoneService.getServerZonesByCondition("code", code);
	}
	@Override
	public String getZoneTree(List<Object> zoneIds) throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter getZoneTree method.");			
		}
		StringBuffer returnStr = new StringBuffer();		
		List<ServerZone> listZone=zoneService.getAllZones(zoneIds);
		for(ServerZone sz:listZone){
			returnStr.append("{text:");
			returnStr.append("\"" + sz.getName() + "\"");
			returnStr.append(",icon:'images/zoneTree.png'");
			returnStr.append(",expanded:false");
			returnStr.append(",depth:1");
			returnStr.append(",qtip:");
			returnStr.append("\"" + sz.getCode() + "\"");
			returnStr.append(",leaf:false},");
		}		
		if(logger.isDebugEnabled()){
			logger.debug("exit getZoneTree method.");
		}
		return ("[" + returnStr + "]").replaceAll(",]", "]");
	}
	@Override
	public List<ServerZone> getAllZonesByGroupId(long zoneGroupId)
			throws HsCloudException {
		return zoneService.getAllZonesByGroupId(zoneGroupId);
	}
	@Override
	public Page<ZoneVO> getRelatedServerZone(Page<ZoneVO> page,
			long zoneGroupId, String zoneName) throws HsCloudException {
		return zoneService.getRelatedServerZone(page, zoneGroupId, zoneName);
	}
	@Override
	public Page<ZoneVO> getUnRelatedServerZone(Page<ZoneVO> page,
			long zoneGroupId, String zoneName) throws HsCloudException {
		return zoneService.getUnRelatedServerZone(page, zoneGroupId, zoneName);
	}
	@Override
	@Transactional
	public boolean associateZoneAndZoneGroup(Long[] zoneIds, long groupId)
			throws HsCloudException {
		boolean resultFlag = false;
		try {
			if (zoneIds != null && zoneIds.length > 0) {
				ZoneGroup zoneGroup = new ZoneGroup();
				zoneGroup.setId(groupId);
				for (int i = 0; i < zoneIds.length; i++) {
					ServerZone sz = zoneService.getServerZoneById(zoneIds[i]);
					if (sz != null) {
						List<ZoneGroup> groupList = sz.getZoneGroupList();
						groupList.add(zoneGroup);
					}
				}
				resultFlag = true;
			}
		} catch (Exception e) {
			throw new HsCloudException("associateZoneAndZoneGroup Exception",
					logger, e);
		}
		return resultFlag;
	}
	@Override
	@Transactional
	public boolean disAssociateZoneAndZoneGroup(Long[] zoneIds, long groupId)
			throws HsCloudException {
		boolean resultFlag = false;
		try {
			if (zoneIds != null && zoneIds.length > 0) {
				ZoneGroup zoneGroup = zoneGroupService.getZoneGroupById(groupId);
				for (int i = 0; i < zoneIds.length; i++) {
					ServerZone sz = zoneService.getServerZoneById(zoneIds[i]);
					if (sz != null) {
						List<ZoneGroup> groupList = sz.getZoneGroupList();
						groupList.remove(zoneGroup);
					}
				}
				resultFlag = true;
			}
		} catch (Exception e) {
			throw new HsCloudException("disAssociateZoneAndZoneGroup Exception",
					logger, e);
		}
		return resultFlag;
	}
	@Override
	@Transactional
	public long createZoneGroup(ZoneGroup zoneGroup,Long[] domainIds) throws HsCloudException {
		long groupId = zoneGroupService.createZoneGroup(zoneGroup);
		if (domainIds != null && domainIds.length > 0) {
			zoneGroup = zoneGroupService.getZoneGroupById(groupId);
			List<Domain> domainList = new ArrayList<Domain>();
			for (int i = 0; i < domainIds.length; i++) {
				Domain domain = domainService.getDomainById(domainIds[i]);
				domainList.add(domain);
			}
			zoneGroup.setDomainList(domainList);
		}		
		return groupId;
	}
	@Override
	@Transactional
	public boolean deleteZoneGroup(long id) throws HsCloudException {
		return zoneGroupService.deleteZoneGroup(id);
	}
	@Override
	public Page<ZoneGroup> findZoneGroup(Page<ZoneGroup> page, String field,
			String fieldValue) throws HsCloudException {
		return zoneGroupService.findZoneGroup(page, field, fieldValue);
	}
	@Override
	public List<ZoneGroup> getAllZoneGroups() throws HsCloudException {
		return zoneGroupService.getAllZoneGroups();
	}
	@Override
	@Transactional
	public boolean updateZoneGroup(ZoneGroup zoneGroup,Long[] domainIds) throws HsCloudException {
		boolean resultFlag = false;
		resultFlag = zoneGroupService.updateZoneGroup(zoneGroup);
		if (domainIds != null && domainIds.length > 0) {
			zoneGroup = zoneGroupService.getZoneGroupById(zoneGroup.getId());
			List<Domain> domainList = zoneGroup.getDomainList();
			if(domainList != null){
				domainList.clear();
			}
			for (int i = 0; i < domainIds.length; i++) {
				Domain domain = domainService.getDomainById(domainIds[i]);
				domainList.add(domain);
			}
			zoneGroup.setDomainList(domainList);
		}
		return resultFlag;
	}
	@Override
	public ZoneGroup getZoneGroupById(long id) throws HsCloudException {
		return zoneGroupService.getZoneGroupById(id);
	}
	@Override
	public boolean hasSameZoneGroupName(ZoneGroup zoneGroup)
			throws HsCloudException {
		return zoneGroupService.hasSameZoneGroupName(zoneGroup);
	}
	@Override
	public boolean hasSameZoneGroupCode(ZoneGroup zoneGroup)
			throws HsCloudException {
		return zoneGroupService.hasSameZoneGroupCode(zoneGroup);
	}
	@Override
	public List<Long> getAllZoneGroupIdsByZoneIds(List<Long> zoneIds)
			throws HsCloudException { 
		return zoneGroupService.getAllZoneGroupIdsByZoneIds(zoneIds);
	}
	@Override
	@Transactional
	public void modifyAdmin(String telephone,long adminId, Long roleId, Long adminRoleId,Integer adminType) {
		Admin admin=adminService.getAdminById(adminId);
		if(StringUtils.isNotBlank(telephone)){
			admin.setTelephone(telephone);
		}
		if(adminType != null){
			admin.setType(adminType);
		}
		
		admin.setUpdateDate(new Date());
		//adminService.modifyAdmin(admin);
		
		if (adminRoleId != null && roleId != null) {
			AdminRole adminRole = adminService.getAdminRoleById(adminRoleId);
			adminRole.setRoleId(roleId);
			adminService.saveAdminRole(adminRole);
		}else if(adminRoleId!=null&&roleId==null){
			adminService.deleteAdminRoleById(adminRoleId);
		}else if(adminRoleId==null&&roleId!=null){
			AdminRole adminRole = new AdminRole();
			adminRole.setAdminId(admin.getId());
			adminRole.setRoleId(roleId);
			adminService.saveAdminRole(adminRole);
		}
				
	}
	@Override
	@Transactional
	public void deleteAdmin(long adminId) {
		Admin admin=adminService.getAdminById(adminId);
		admin.setUpdateDate(new Date());
		admin.setEnable((short)0);
		adminService.modifyAdmin(admin);
	}
	
	@Override
	@Transactional
	public void freezedAdmin(long adminId) {
		
		Admin admin=adminService.getAdminById(adminId);
		admin.setUpdateDate(new Date());
		admin.setEnable(UserState.FREEZE.getIndex());
		//adminService.modifyAdmin(admin);
		
	}
	@Override
	@Transactional
	public void resetPasswd(long adminId, String password) {
		Admin admin=adminService.getAdminById(adminId);
		if(StringUtils.isNotBlank(password)){
			admin.setPassword(password);
			admin.setUpdateDate(new Date());
		}
		adminService.modifyAdmin(admin);
	}
    
    /** 
    * @param roleId
    * @return 
    */
    @Override
    @Transactional
    public Role getRoleById(long roleId) {
        return roleService.getRoleById(roleId);
    }
   
	@Override
	public Admin getAdminByEmail(String email) {
		return adminService.getAdminByEmail(email);
	}
     
//*******************************lihonglei start********************************************************
	/**删除角色
	 * @param roleId
	 */
	@Override
	@Transactional(readOnly = false)
	public String deleteRole(long roleId) {
		// 企业用户或系统用户无法删除
		if (roleId == Constants.Role_Enterprise_ID
				|| roleId == Constants.Role_SYSTEM_ADMIN_ID) {
			return "false";
		}
		roleService.deleteRole(roleId);
		return "true";
	}

	/**获取角色列表
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<Role> getAllRoleByPage(Page<Role> page, String roleName) {
		return roleService.findPage(page, roleName);
	}

	/**根据角色获取管理员
	 * @param page
	 * @param roleName
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<Admin> pageAdminByRoleId(Page<Admin> page, long roleId) {
		return adminService.pageAdminInRole(page, roleId);
	}
	
    /**
     * @param role
     * @param privilegesStr
     */
    @Override
    @Transactional(readOnly = false)
    public String createRole(Role role, String privilegesStr) {
        boolean flag = roleService.findRoleName(role);
        if (flag == false) {
            return "exist";
        }
        roleService.saveRole(role);
        return "success";
    }

    @Override
    @Transactional(readOnly = true)
    public List<Action> getActionList(String actionType) {
        return actionService.getAction(actionType, Constant.ACTION_LEVEL,
                (short) Constant.STATUS_ADMIN);
    }
       
       
	/**
	 * @param page
	 * @return
	 */
	@Override
	@Transactional
	public Page<IPRangeVO> findIPRanges(Page<IPRangeVO> page,String field,String fieldValue) {
		return iPService.findIPRanges(page,field,fieldValue);
	}

	/**
	 * @param startIP
	 * @param endIP
	 * @return
	 */
	@Override
	@Transactional
	public List<IPDetail> findIPDetailByIP(long startIP, long endIP, String gateway) {
	    List<ServerNode> serverNodeList = nodeService.getAllNodes();
	    for(ServerNode serverNode : serverNodeList) {
	        String ipStr = serverNode.getIp();
	        long ip = IPConvert.getIntegerIP(ipStr);
	        if((startIP <= ip && ip <= endIP) || (gateway.equals(ipStr))) {
	            throw new HsCloudException(Constants.IP_NODE_SAME_EXCEPTION, "ip中包含节点ip", logger);
	        }
	    }
	    return iPService.findIPDetailByIP(startIP, endIP);
	}

	/**
	 * @param startIP
	 * @param endIP
	 * @param createUid
	 * @param remark
	 * @return
	 * @throws HsCloudException
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly = false)
	public String createIP(IPRangeVO ipRangeVO) throws HsCloudException, Exception {
		IPRange ipRange = null;
		if(ipRangeVO!=null){
			ServerZone serverZone = zoneService.getServerZoneById(ipRangeVO.getZoneId());
			ipRange = new IPRange();
			ipRange.setStartIP(ipRangeVO.getStartIP());
			ipRange.setEndIP(ipRangeVO.getEndIP());
			ipRange.setCreateUid(ipRangeVO.getCreateUid());
			ipRange.setGateway(ipRangeVO.getGateWay());
			ipRange.setRemark(ipRangeVO.getRemark());
			if(serverZone!=null){
				ipRange.setServerZone(serverZone);
			}			
		}
		return iPService.createIP(ipRange);
	}

	/**
	 * @param ipRangeId
	 * @param status
	 * @return
	 * @throws HsCloudException
	 */
	@Override
	@Transactional
	public List<IPDetail> findIPDetailByStatus(long ipRangeId, int status)
			throws HsCloudException {
		return iPService.findIPDetailByStatus(ipRangeId, status);
	}

	/**
	 * @param id
	 * @return
	 * @throws HsCloudException
	 */
	@Override
	@Transactional(readOnly = false)
	public boolean deleteIP(long id) throws HsCloudException {
		return iPService.deleteIP(id);
	}

	/**
	 * @param id
	 * @param status
	 * @param userId
	 * @return
	 * @throws HsCloudException
	 */
	@Override
	@Transactional(readOnly = false)
	public boolean updateIPDetail(long id, int status, long userId,String remark)
			throws HsCloudException {
		return iPService.updateIPDetail(id, status, userId,remark);
	}

	/**
	 * 通过状态查ip
	 * @param status
	 * @return
	 * @throws HsCloudException
	 */
	@Override
	public List<IPDetailInfoVO> findAvailableIPDetailOfServerZone(ServerZone serverZone)
			throws HsCloudException {
//		return iPService.findIPDetailByStatus(status);
		return iPService.findAvailableIPDetailOfServerZone(serverZone);
	}
	/**
	 * ip查询
	* @param page
	* @param ipRangeId
	* @param field
	* @param fieldValue
	* @return
	* @throws HsCloudException
	 */
	@Override
	@Transactional
	public Page<IPDetailVO> findIPDetailsByCondition(Page<IPDetailVO> page,
			long ipRangeId, String field, String fieldValue) throws HsCloudException {		
		return iPService.findIPDetailsByCondition(page, ipRangeId, field, fieldValue);
	}
	@Override
	public boolean isServerZoneBindIP(long zoneId) throws HsCloudException {
		boolean result = false;
		List<IPRange> listIP = iPService.getAllIPsByServerZone(zoneId);
		if(listIP!=null && listIP.size()>0){
			result = true;
		}
		return result;
	}
	
	@Override
	@Transactional(readOnly = false)
	public boolean createWanNetwork(NetWorkBean netWork,Long adminId) throws HsCloudException{
		boolean bl = false;
		try {
			//创建WanNetwork下的IP段
			String ipStart = IPConvert.cidrToStringIPs(netWork.getCidr())[0];
			String ipEnd = IPConvert.cidrToStringIPs(netWork.getCidr())[1];
			
			long useIpStart = IPConvert.getIntegerIP(netWork.getUseIPstart());
			long useIpEnd = IPConvert.getIntegerIP(netWork.getUseIPend());
			
			IPRange ipRange  = new IPRange();
			ipRange.setStartIP(IPConvert.getIntegerIP(ipStart));
			ipRange.setEndIP(IPConvert.getIntegerIP(ipEnd));
			ipRange.setCreateUid(adminId);
			ipRange.setGateway(netWork.getGateway());
			ipRange.setType(Constants.IP_RANGE_NETWORK);
			ipRange.setRemark("use for WanNetwork");
			Long ipRangeId = iPService.createWanNetworkIP(ipRange,useIpStart,useIpEnd);
			//创建WanNetwork
			netWork.setIpRangeId(ipRangeId);
			bl = vpdcNetworkService.createWanNetwork(netWork, adminId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new HsCloudException(Constants.VPDC_WANNETWORK_ERROR, "创建WanNetwork失败", logger);
		}
		return bl;
	}
	/**
	 * 获取菜单
	* @param parentId
	* @param referenceIds
	* @return
	 */
	@Override
	@Transactional
	public List<AdminMenuVO> getAdminMenu(String parentId,
			List<Object> referenceIds) {
		List<AdminMenuVO> list = treeService.getMenuBySuper(parentId,
				referenceIds);
		if (Constant.MENU_ROOT.equals(parentId)) {
			return list;
		}
		String ids = "";
		Map<String, AdminMenuVO> toolMap = new HashMap<String, AdminMenuVO>();
		for (AdminMenuVO adminMenuVO : list) {
			ids += adminMenuVO.getId() + ",";
			toolMap.put(adminMenuVO.getId(), adminMenuVO);
		}
		if(StringUtils.isNotBlank(ids)) {
			ids = ids.substring(0, ids.length() - 1);
			AdminMenuVO parent = null;
			List<AdminMenuVO> childList = treeService.getMenuBySuper(ids,
					referenceIds);
			for (AdminMenuVO adminMenuVO : childList) {
				if (toolMap.containsKey(adminMenuVO.getParentId())) {
					parent = toolMap.get(adminMenuVO.getParentId());
					parent.getChildrenList().add(adminMenuVO);
				}
			}
		}
		return list;
	}
	/**
	 * 根据adminId获取角色
	* @param adminId
	* @return
	 */
	@Override
	@Transactional
	public Role getRoleByAdminId(long adminId) {
		return roleService.getRole(adminId);
	}
	/**
	 *  获取ResourceTypeMap
	* @return
	 */
	@Override
	@Transactional
	public Map<String, ResourceType> getResourceTypeMap() {
		if(resourceTypeMap == null || resourceTypeMap.isEmpty()) {
			resourceTypeMap = resourceTypeService.getResourceTypeMap(Constant.STATUS_ADMIN);
		}
		return resourceTypeMap;
	}
	/**
	 * 获取ResourceType列表
	* @return
	 */
	@Override
//	@Transactional
	public List<ResourceType> getResourceTypeList() {
		if(resourceTypeList == null || resourceTypeList.isEmpty()) {
			resourceTypeList = resourceTypeService.getResourceTypeList(Constant.STATUS_ADMIN);
		}
		return resourceTypeList;
	}
	/**
	 * 检查是否有赋值权限
	* @param adminId
	* @return
	 */
	@Override
	@Transactional
	public boolean checkPermission(Long adminId) {
		return resourceService.checkPermission(adminId);
	}
	
	/**
	 * 获取上传文件列表
	* @return
	 */
	@Override
	@Transactional
	public List<FileVO> getFileList() {
		return imageService.getFileList();
	}
	/**
	 * 删除上传文件
	* @param fileName
	* @return
	 */
	@Override
	@Transactional
	public boolean deleteFile(String fileName) {
		return imageService.deleteFile(fileName);
	}
	/**
	 * 上传文件生成image
	* @param image
	* @param fileName
	 */
	@Override
	@Transactional(readOnly = false)
	public void addUploadInfo(ImageVO image, String fileName) {
		imageService.addUploadInfo(image, fileName);
	}
	/**
	 * 展示image列表
	* @param query
	* @return
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ImageVO> showImageList(String query) {
		if(StringUtils.isBlank(query)) {
			return imageService.showImageList();
		}
		return imageService.showImageList(query);
	}
	/**
	 * 删除image
	* @param imageId
	 */
	@Override
	@Transactional
	public void deleteImage(String imageId) {
		imageService.deleteImage(imageId);
	}
	/**
	 * 编辑image
	* @param image
	 */
	@Override
	@Transactional
	public void editImage(ImageVO image) {
		imageService.editImage(image);
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
	public List<PrivilegeVO> findUnassignedList(String type, Long roleId, Page<PrivilegeVO> pagePrivilege, String query) {
		Map<String, Object> map = getParamMap(type, roleId, query);
		String conditionTable = "select permission_id from hc_role_permission rp where rp.role_id = :conditionId";
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
			checkList = treeService.findPermissionByResourceIds(ids, roleId);
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
	public List<PrivilegeVO> findAssignedList(String type, Long roleId, Page<PrivilegeVO> pagePrivilege, String query) {
		Map<String, Object> map = getParamMap(type, roleId, query);
		String conditionTable = "select p.resource_id from hc_permission p, hc_role_permission rp where p.id = rp.permission_id and rp.role_id = :conditionId";
		map.put("conditionTable", conditionTable);
		
		List<Object> list = treeService.findAssignedList(map, pagePrivilege);
		List<Object> ids = new ArrayList<Object>();
		
		List<PrivilegeVO> result = new ArrayList<PrivilegeVO>();
		if(list != null && !list.isEmpty()) {
			for(Object obj : list) {
				Object[] array = (Object[])obj;
				ids.add(array[0]);
			}
			List<CheckboxVO> checkList = treeService.findPermissionByResourceIds(ids, roleId);
		//	List<RolePermission> rolePermissions = rolePermissionService.findRolePermissionByRoleId(roleId);
			
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
	 * 封装返回结果 
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
	 * 处理权限查询参数 
	* <功能详细描述> 
	* @param type
	* @param roleId
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	private Map<String, Object> getParamMap(String type, Long roleId, String query) {
		Map<String, ResourceType> resourceMap = getResourceTypeMap();
		ResourceType resourceType = resourceMap.get(type);
		String table = resourceType.getResourceTable();
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("table", table);
		map.put("query", query);
		map.put("resourceCondition", resourceType.getResourceCondition());
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("type", type);
		conditionMap.put("conditionId", roleId);
		map.put("conditionMap", conditionMap);
		return map;
	}
	/**
	 * 查询统一权限
	* @param type
	* @param roleId
	* @return
	 */
	@Override
	@Transactional(readOnly = true)
	public List<PrivilegeVO> findUiformDefList(String type, Long roleId) {
		String tableAndCondition = " hc_role_permission up on p.id = up.permission_id and up.role_id = :id ";
		String column = "up.permission_id";
		return treeService.findUiformDefList(type, roleId, tableAndCondition, column);
	}
	/**
	 * 权限变更
	* @param permissionValue
	* @param actionValue
	* @param resourceValue
	* @param roleId
	 */
	@Override
	@Transactional(readOnly = false)
	public void modifyRolePermissiion(String permissionValue,
			String actionValue, String resourceValue, long roleId) {
		rolePermissionService.batchDelete(resourceValue, roleId);
		String[] permissionArray = permissionValue.split(",");
		for(String permissionId : permissionArray) {
			if(StringUtils.isNotBlank(permissionId)) {
				rolePermissionService.addRolePermissiion(roleId, Long.valueOf(permissionId));
			}
		}
		String[] actionArray = actionValue.split(",");
		for(String actionStr : actionArray) {
			if(StringUtils.isNotBlank(actionStr)) {
				String[] array = actionStr.split("-");
				long resourceId = Long.valueOf(array[0]);
				long actionId = Long.valueOf(array[1]);
				long permissionId = permissionService.addPermission(resourceId, actionId);
				rolePermissionService.addRolePermissiion(roleId, permissionId);
			}
		}
	}
	
	/**
	 * 组装json数据 
	* <功能详细描述> 
	* @param result
	* @param treeList 
	* @see [类、类#方法、类#成员]
	 */
	private void getJsonStr(StringBuilder result, List<TreeNode> treeList) {
		for(int i = 0; i< treeList.size(); i++) {
			TreeNode treeNode = treeList.get(i);
			result.append("{name:'" + treeNode.getName() + "',param1:'" + treeNode.getText() + "'," );
			if(!treeNode.getChildren().isEmpty()) {
				result.append("iconCls:'task-folder',children:[");
				getJsonStr(result, treeNode.getChildren());
				result.append("]");
			} else {
				result.append("leaf:true,");
				result.append("iconCls:'task'");
			}
			result.append("}");
			if(i != treeList.size() - 1) {
				result.append(",");
			}
			
		}
	}
	
	/**
	 * 查询菜单列表
	* @param roleId
	* @param i18nMap
	* @return
	 */
	@Override
	@Transactional(readOnly = false)
	public String findMenuStore(long roleId, Map<String, String> i18nMap) {
		List<MenuVO> list = treeService.findMenuStore(roleId);
		TreeNode tree = new TreeNode();
		
		TreeNode parent = new TreeNode();
		Map<String, TreeNode> map = new HashMap<String, TreeNode>();
		tree.setId("0");
		map.put(tree.getId(), tree);
		for(MenuVO vo : list) {
			if(vo.getResourceId() == null) {
				Long resourceId = resourceService.addResource(vo.getId(), "com.hisoft.hscloud.crm.usermanager.entity.Menu");
				vo.setResourceId(resourceId);
			}
			
			tree = new TreeNode();
			tree.setId(vo.getId());
			tree.setName(vo.getName());
			if(i18nMap.containsKey(vo.getName())) {
				tree.setName(i18nMap.get(vo.getName()));
	    	}
			
			tree.setResourceId(vo.getResourceId());
			if(vo.getPermissionId() == null || vo.getPermissionId() == 0){
				tree.setText(vo.getResourceId() + "-1,false");
			} else {
				if(StringUtils.isBlank(vo.getChecked())) {
					tree.setText(vo.getPermissionId() + ",false");
				} else {
					tree.setText(vo.getPermissionId() + ",true");
				}
				
			}
			
			map.put(tree.getId(), tree);
			
			String parentId = vo.getParentId();
			if(map.containsKey(parentId)) {
				parent = map.get(parentId);
				parent.getChildren().add(tree);
			}
		}
		StringBuilder result = new StringBuilder("{'text':'.','children': [");
		getJsonStr(result, map.get("0").getChildren());
		
		result.append("]}");
		return result.toString();
	}
	/**
	 * 权限赋值
	* @param privilegesStr
	* @param roleId
	* @param noCheckStr
	 */
	@Override
	@Transactional(readOnly = false)
	public void addPrivilege(String privilegesStr, Long roleId, String noCheckStr) {
	    noCheckStr = noCheckStr + "," + privilegesStr;
		rolePermissionService.deleteRPForMenu(roleId, noCheckStr);
		String[] array = privilegesStr.split(",");
		for(String str : array) {
			if(StringUtils.isNotBlank(str)) {
				if(str.indexOf("-") == -1) {
					rolePermissionService.addRolePermissiion(roleId, Long.valueOf(str));
				} else {
					String[] strArray = str.split("-");
					long permissionId = permissionService.addPermission(Long.valueOf(strArray[0]), Long.valueOf(strArray[1]));
					rolePermissionService.addRolePermissiion(roleId, permissionId);
				}
			}
		}
	}
	
	/**
	 * 发布公告
	* @param announcementVO
	 */
	@Override
	@Transactional(readOnly = false)
    public void saveAnnouncement(AnnouncementVO announcementVO, long adminId) {
	    announcementService.saveAnnouncement(announcementVO, adminId);
    }
	/**
	 * 查询发票列表
	* @param page
	* @param condition
	* @return
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<Map<String, Object>> findInvoiceRecordList(Page<Map<String, Object>> page, Map<String, Object> condition, Admin admin) {
	    if(!(admin.getIsSuper() || roleService.isSpecialAdmin(admin.getId()))){
	        List<Domain> domainList = domainService.getDomainByAdmin(admin.getId());
	        List<Long> domainIdList = new ArrayList<Long>();
	        for(Domain domain : domainList) {
	            domainIdList.add(domain.getId());
	        }
	        condition.put("domainIdList", domainIdList);
	    }
	    return invoiceRecordService.findInvoiceRecordList(page, condition);
	}
	
	/**
	 * 发票审批通过
	* @param id
	* @param innerDescription
	* @param invoiceNo
	* @param sendTime
	 */
	@Override
	@Transactional(readOnly = false)
	public String approvalInvoiceSuccess(long id, String innerDescription, String invoiceNo, Date sendTime) {
	    try{
	        List<InvoiceRecord> list = invoiceRecordService.findInvoiceRecordByInvoiceNo(invoiceNo);
	        if(list != null && !list.isEmpty()) {
	            return Constants.INVOICE_NUMBER_EXIST_EXCEPTION;
	        }
	        InvoiceRecord invoiceRecord = invoiceRecordService.approvalSuccess(id, innerDescription, invoiceNo, sendTime);
	        invoiceAccountService.invoice(invoiceRecord.getUserId(), invoiceRecord.getInvoiceAmount());
	        
	        invoiceMessage(invoiceRecord.getUserId(), Constants.MESSAGE_INVOICE_APPROVE_SUCCESS, invoiceRecord.getInvoiceAmount());
	    } catch(Exception ex) {
	        eService.setMessage("审批通过异常");
	        eService.throwException(Constants.INVOICE_APPROVAL_SUCCESS_EXCEPTION, ex);
	    }
	   
	    return Constants.SUCCESS;
	}
	/**
	 * 发票审批未通过
	* @param id
	* @param innerDescription
	 */
	@Override
	@Transactional(readOnly = false)
	public void approvalInvoiceFail(long id, String innerDescription) {
	    InvoiceRecord invoiceRecord = invoiceRecordService.approvalFail(id, innerDescription);
	    long userId = invoiceRecord.getUserId();
	    
	    int takeInvoiceType = Integer.valueOf(invoiceRecord.getTakeInvoiceType());
        if(takeInvoiceType != TakeInvoiceType.SELF_CREATED.getIndex()) {
            Account account = accountService.getAccountByUserId(userId);
            TakeInvoiceType type = TakeInvoiceType.getTakeInvoiceType(takeInvoiceType);
            long transactionLogId = accountService.accountRefund(userId,(short)0, PaymentType.PAYMENT_ONLINE.getIndex(), com.hisoft.hscloud.bss.billing.constant.ServiceType.SERVICE_CLOUD_EXPRESS.getIndex(),
                    account.getId(), type.getPrice(), new BigDecimal(0),new BigDecimal(0), type.getDescription(), null);
            long invoiceRecordId = invoiceRecord.getId();
            
            InvoiceRecordTransaction entity = new InvoiceRecordTransaction();
            entity.setInvoiceRecordId(invoiceRecordId);
            entity.setTransactionLogId(transactionLogId);
            invoiceRecordTransactionService.saveEntity(entity);
            
            User user = userService.getUser(userId);
            
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
            incomingLog.setTranscationType((short)3);
            incomingLogService.saveIncomingLog(incomingLog);
        }
	    invoiceAccountService.applyInvoiceFail(invoiceRecord.getUserId(), invoiceRecord.getInvoiceAmount());
	    
	    invoiceMessage(userId, Constants.MESSAGE_INVOICE_APPROVE_FAIL, invoiceRecord.getInvoiceAmount());
	}
	
	/**
	 * <发票发布信息> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	private void invoiceMessage(long userId, String content, BigDecimal money) {
	    content = content.replaceAll("\\$\\{money\\}", money.toString());
	    Message message = new Message();
        message.setUserId(userId);
        message.setMessageType(6);
        message.setMessage(content);
        messageService.saveMessage(message);
	}
	
	@Override
	@Transactional(readOnly = false)
    public String modifyInvoice(InvoiceVO invoiceVO) {
        return invoiceRecordService.modifyInvoice(invoiceVO);
    }
	
	/**
	 * 加载省份列表 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Province> loadProvince() {
	    return dataService.getProvinceList();
	}
	
	/**
	 * 公告查询
	* @param announcementPage
	* @param query
	* @return
	 */
    @Override
    @Transactional(readOnly = true)
    public Page<Map<String, Object>> findAnnouncementPage(
            Page<Map<String, Object>> announcementPage, String query) {
        return announcementService.findAnnouncementPage(announcementPage, query);
    }
    
    /**
     * 删除公告
    * @param announcementId
     */
    @Override
    @Transactional(readOnly = false)
    public void delAnnouncement(long announcementId) {
        announcementService.delAnnouncement(announcementId);
    }
    
    /**
     * <查询分平台页面(用于权限分配)> 
    * <功能详细描述> 
    * @param page
    * @param roleId
    * @return 
    * @see [类、类#方法、类#成员]
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Map<String, Object>> findDomainList(Page<Map<String, Object>> page, long roleId) {
        return domainService.findDomainList(page, roleId);
    }
    
    /**
     * 查询分平台页面
    * @param page
    * @param condition
    * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Domain> findDomainPage(Page<Domain> page, String query) {
        return domainService.findDomainPage(page, query);
    }
    
    /**
     * 分平台赋权
    * @param permissionValue
    * @param resourceValue
    * @param id
     */
    @Override
    @Transactional(readOnly = false)
    public void editDomainPermission(String permissionValue,
            String resourceValue, long roleId) {
        domainService.editDomainPermission(permissionValue, resourceValue, roleId);
    }
    
    /**
     * 获取所有分平台
    * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<Domain> getAllDomain() {
        return domainService.getAllDomain();
    }
    
    /**
     * 删除分平台
    * @param domainId
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatusDomain(long domainId, long adminId, String status) {
        domainService.updateStatusDomain(domainId, adminId, status);
    }
    
    /**
     * <编辑分平台> 
    * <功能详细描述> 
    * @param domain
    * @param adminId 
    * @see [类、类#方法、类#成员]
     */
    @Override
    @Transactional(readOnly = false)
    public String editDomain(DomainVO domainVO, long adminId) throws HsCloudException{
        long domainId = domainVO.getId();
        String result = domainService.editDomain(domainVO, adminId);
        if(domainId != domainVO.getId()) {
            mailService.createMailTemplate(domainVO.getId());
            mailService.createMailSender(domainVO.getId());
        }
        return result;
    }
    
    /**
     * 重发激活邮件
    * @param userId
     */
    @Override
    @Transactional
    public void reActivation(long userId)throws HsCloudException {
    	try{
//        User user = new User();
//        user.setId(userId);
//        user.setName("");
//        user = userService.modifyUser(user);
          //User oldUser = userDao.findUniqueBy("id", userId);
    	  User oldUser = userService.getUser(userId);
          oldUser.setUpdateDate(new Date());
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", oldUser.getName());
        map.put("email", oldUser.getEmail());
        map.put("id", String.valueOf(oldUser.getId()));
        String activeKey = MD5Util.getMD5Str(oldUser.getName()
                + String.valueOf(oldUser.getUpdateDate().getTime()).substring(0, 8));
        map.put("activeKey", activeKey);
        saveEmail(oldUser, map, MailTemplateType.ACTIVE_USER_TEMPLATE.getType());
    	}catch(Exception e){
    		throw new HsCloudException("",logger,e);
    	}
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
//        map.put("webSiteUrl",domain.getPublishingAddress());
        map.put("webSiteUrl","http://117.122.193.11/");
//        map.put("image", Constants.MAIL_LOGO_SRC.replaceAll("xxx", Long.toString(domain.getId())));
        map.put("image", "resource/uploads/");
        map.put("webAddress", domain.getPublishingAddress());
        mailService.saveEmail(user.getEmail(), null, type,domain.getId(), map);
    }
    
    @Override
    public void resetSystemPwd(String vmId, String password,Object o,String otype) {
        operation.resetSystemPwd(vmId, password,o,otype);
    }
    
    private String transferStatus(String status) {
        String result = "";
        if("0".equals(status)) {
            result = "申请";
        }
        if("1".equals(status)) {
            result = "已审批";
        }
        if("2".equals(status)) {
            result = "拒绝";
        }
        return result;
    }
    
    private String transferTakeInvoiceType(String transferTakeInvoice) {
        String result = "";
        if("0".equals(transferTakeInvoice)) {
            result = "快递10元";
        }
        if("1".equals(transferTakeInvoice)) {
            result = "EMS20元";
        }
        if("2".equals(transferTakeInvoice)) {
            result = "前台自取(免费)";
        }
        return result;
    }
    
    private String transferCourierDeliveryTime(String transferCourierDelivery) {
        String result = "";
        if("0".equals(transferCourierDelivery)) {
            result = "上班时间";
        }
        if("1".equals(transferCourierDelivery)) {
            result = "双休节假日";
        }
        if("2".equals(transferCourierDelivery)) {
            result = "时间不限";
        }
        return result;
    }
    
    private String transferProvince(String code) {
        List<Province> provinceList = dataService.getProvinceList();
        for(Province province: provinceList) {
            if(province.getProvinceCode().equals(code)) {
                return province.getProvinceName();
            }
        }
        return "";
    }
    
    private String transferInvoiceType(String invoiceType){
    	String result="";
    	if("1".equals(invoiceType) ){
    		result = "增值税普通发票";
    	}else if("2".equals(invoiceType)){
    		result = "增值税专用发票";
    	}else{
    		result="";
    	}
    	return result;
    }
    
    @Override
    @Transactional
    public ExcelExport invoiceExcelExport(Map<String, Object> condition,
            Admin admin) {

        if (!(admin.getIsSuper() || roleService.isSpecialAdmin(admin.getId()))) {
            List<Domain> domainList = domainService.getDomainByAdmin(admin.getId());
            List<Long> domainIdList = new ArrayList<Long>();
            for (Domain domain : domainList) {
                domainIdList.add(domain.getId());
            }
            condition.put("domainIdList", domainIdList);
        }
        List<Map<String, Object>> list = invoiceRecordService.invoiceExcelExport(condition);
        
        
        for(Map<String, Object> map : list) {
            Object id = map.get("id");
            map.put("id", id.toString());
            String status = (String)map.get("status");
            map.put("status", transferStatus(status));
            String takeInvoiceType = (String)map.get("takeInvoiceType");
            map.put("takeInvoiceType", transferTakeInvoiceType(takeInvoiceType));
            String courierDeliveryTime = (String)map.get("courierDeliveryTime");
            map.put("courierDeliveryTime", transferCourierDeliveryTime(courierDeliveryTime));
            String province = (String)map.get("province");
            map.put("province", transferProvince(province));
            String invoiceType = (String)map.get("invoiceType");
            map.put("invoiceType", transferInvoiceType(invoiceType));
        }

        Map<String, List<Map<String, Object>>> datas = new HashMap<String, List<Map<String, Object>>>();
        
        String[] columnStr = {"id", "email", "username", "invoiceNo", "invoiceTitle", "invoiceType","invoiceAmount", "status", "applicationTime", 
                "billingTime", "sendTime", "description", "takeInvoiceType", "courierDeliveryTime",
                "recipient", "recipientCompany", "province", "city", "address", "postcode",
                "mobile", "telephone", "fax", "innerDescription" }; 
        List<String> columnList = new ArrayList<String>();
        columnList = Arrays.asList(columnStr);
        datas.put("发票报表", list);
        InputStream is = FacadeImpl.class.getClassLoader().getResourceAsStream("exceltemplete/invoice.xls");
        return new ExcelExport(is, datas, columnList);

    }
    
    @Override
    public Page<AccessAccountVO> findAccessAccountPage(Page<AccessAccountVO> accessAccountPage,
            String query) {
        return accessAccountService.findAccessAccountPage(accessAccountPage, query);
    }
    
    @Override
    @Transactional
    public void deleteAccessAccount(long id) {
        accessAccountService.deleteAccessAccount(id);
        
    }
    
    @Override
    @Transactional
    public String saveAccessAccount(AccessAccountVO accessAccountVO) {
        AccessAccount accessAccount = accessAccountService.getAccessAccount(accessAccountVO.getAccessId());
        if(accessAccount != null && accessAccount.getId() != accessAccountVO.getId()) {
            return "registered";
        } else if(accessAccount == null) {
            accessAccount = new AccessAccount();
        }
        if(accessAccountVO.getType() == 0) {
            User user = userService.getUserByEmail(accessAccountVO.getAccessId());
            if(user == null) {
                return "userNotExist";
            } else if(user.getEnable() != 3) {
                return "notApprovedStatus";
            } else {
                long userId = user.getId();
                Account account = accountService.getAccountByUserId(userId);
                accessAccountVO.setUserId(Long.toString(userId));
                accessAccountVO.setAccountId(Long.toString(account.getId()));
                accessAccountVO.setEmailAddr(user.getEmail());
            }
        } else if(accessAccountVO.getType() == 1) {
            accessAccountVO.setEmailAddr(accessAccountVO.getAccessId());
        } else {
            throw new RuntimeException();
        }

        BeanUtils.copyProperties(accessAccountVO, accessAccount);
        accessAccountService.saveAccessAccount(accessAccount);
        return Constants.SUCCESS;
    }
    
    @Override
    public Page<VrouterTemplateVO> pageVrouterTemplateVO(Page<VrouterTemplateVO> pageVrouterTemplate) {
        pageVrouterTemplate = vpdcVrouterService.pageVrouterTemplateVO(pageVrouterTemplate);
        return pageVrouterTemplate;
    }
    
    @Override
    @Transactional
    public void deleteVrouterTemplate(long id) {
        vpdcVrouterService.deleteVrouterTemplate(id);
    }
    
    @Override
    @Transactional
    public String editVrouterTemplate(VrouterTemplateVO vrouterTemplateVO) {
        int osId = vrouterTemplateVO.getOsId();
        Os os = serviceItemService.getOs(osId);
        vrouterTemplateVO.setImageId(os.getImageId());
        return vpdcVrouterService.editVrouterTemplate(vrouterTemplateVO);
    }
    
    @Override
    @Transactional
    public Page<NetWorkBean> findPageNetwork(Page<NetWorkBean> pageNetworkBean) {
        pageNetworkBean = vpdcNetworkService.findPageNetwork(pageNetworkBean);
        IPStatistics status = null;
        for(NetWorkBean netWorkBean : pageNetworkBean.getResult()) {
            status = iPService.getIPStatisticsByRangeId(netWorkBean.getIpRangeId());
            netWorkBean.setTotalIPs(status.getFreeIPs() + status.getAssigningIPs()
                    + status.getAssignedIPs() + status.getDisabledIPs()+ status.getReleasingIPs());
            netWorkBean.setUsedIPs(status.getAssigningIPs());
            netWorkBean.setAssignedIPs(status.getAssignedIPs());
            netWorkBean.setFreeIPs(status.getFreeIPs());
        }
        
        return pageNetworkBean;
    }
    
    @Override
    @Transactional
    public boolean deleteNetwork(long id, long rangeId) {
        boolean resultFlg = deleteIP(rangeId);
        if(resultFlg == true) {
            vpdcNetworkService.deleteNetwork(id);
        }
        return resultFlg;
    }
    
  //*******************************lihonglei end********************************************************
            /******************************* systemmanagement Module ********************************/
	@Override
	public Page<ProcessResourceVO> findAllProcess(Page<ProcessResourceVO> page)
			throws HsCloudException {
		return threadService.findAllProcess(page);
	}

	@Override
	public boolean startProcess(String threadKey) throws HsCloudException {
		boolean operateFlag = false;
		threadService.startThread(threadKey);
		operateFlag = true;
		return operateFlag;
	}

	@Override
	public boolean stopProcess(String threadKey) throws HsCloudException {
		boolean operateFlag = false;
		threadService.stopThread(threadKey);
		operateFlag = true;
		return operateFlag;
	}

	@Override
	public ServiceCatalog getServiceCatalogById(int id) {

		return serviceCatalogService.get(id);
	}

	@Override
	public Os getOsByServicecatalogId(int id) {

		return serviceCatalogService.getOs(id);
	}

	@Override
	public void saveSC(ServiceCatalog sc) {
		serviceCatalogService.save(sc);

	}

	@Override
	public ServiceCatalog getSCByName(String name) {
		return serviceCatalogService.getByName(name);
	}

	@Override
	public Page<ServiceCatalog> getSCByPage(Page<ServiceCatalog> paging,
			ServiceCatalog sc, List<Sort> sortList,Long brandId,Long zoneId,Long domainId) {
		return serviceCatalogService.page(paging, sc, sortList,brandId,zoneId,domainId);
	}

	@Override
	public void disableSC(int id) {
		serviceCatalogService.delete(id);
	}

	@Override
	public void enableSC(int id) {
		serviceCatalogService.approve(id);
	}
	
	@Override
	public void onlyTrySC(int id) {
		serviceCatalogService.onlyTrySC(id);
	}

	@Override
	public void approveSC(int id) {
		serviceCatalogService.approve(id);
	}

	@Override
	public List<ServiceItem> listServiceItem(int serviceType,
			List<Sort> sortList) {
		return serviceItemService.listServiceItem(serviceType, sortList);
	}

	@Override
	public List<Role> getRoleByPermission(List<Object> roleIds) {
		if (roleIds == null) {
			return roleService.getAllRole();
		}

		if (roleIds.size() == 0) {
			return new ArrayList<Role>();
		}

		if (roleIds.size() > 0) {
			List<Long> roleIdsTemp = new ArrayList<Long>();
			for (Object o : roleIds) {
				roleIdsTemp.add((Long) o);
			}
			return roleService.getRoleByPermission(roleIdsTemp);
		}
		return null;
	}

	@Override
	public boolean checkImageSizeVSDiskCapacity(ServiceCatalog sc)throws HsCloudException {
		try{
		List<ServiceItem> items = sc.getItems();
		Os os = null;
		Disk disk = null;
		for (ServiceItem serviceItem : items) {
			Map<String, Object> map = new HashMap<String, Object>();
			int type = serviceItem.getServiceType();
			if (type == 3) {
				map.put("id", serviceItem.getId());
				disk = (Disk) serviceItemService.getServiceItemByProperty(3,
						map).get(0);
			} else if (type == 4) {
				map.put("id", serviceItem.getId());
				os = (Os) serviceItemService.getServiceItemByProperty(4, map)
						.get(0);
			} else {
				List<ServiceItem> list = serviceItemService
						.getServiceItemByProperty(4, null);
				for (ServiceItem item : list) {
					Os o = (Os) item;
					if (serviceItem.getId() == o.getId()) {
						os = o;
					}
				}

			}
		}
		if (disk.getCapacity() == 0 || Double.parseDouble(os.getImageSize() )<= disk.getCapacity()) {
			return true;
		} else {
			return false;
		}
		}catch(Exception e){
			throw new HsCloudException("",logger,e);
		}
	}

	@Override
	public Page<ServiceItem> pageServiceItem(Page<ServiceItem> paging,
			int serviceType, List<Sort> sortList, String query) {
		return serviceItemService.pageServiceItemByType(paging, serviceType,
				sortList, query);
	}

	@Override
	public List<ServiceItem> getServiceItemByType(int type) {

		return serviceItemService.getServiceItemByProperty(type, null);
	}

	@Override
	public int saveServiceItem(ServiceItem si) {
		return serviceItemService.save(si);

	}

	@Override
	public Map<Integer, ServiceType> getServiceType() {

		return serviceItemService.getServiceType();
	}

	@Override
	public boolean deleteServiceItem(int siId) {
		if (serviceItemService.isUsed(siId)) {
			return false;
		} else {
			serviceItemService.delete(siId);
			return true;
		}

	}

	@Override
	public boolean checkServiceItemNameRepeat(int serviceType, String name) {

		return serviceItemService.checkServiceItemRepeat(name, serviceType);
	}

	@Override
	public void enableAdmin(long adminId) {
		adminService.enableAdmin(adminId);

	}

	@Override
	public void enableUser(long userId) {
		userService.enableUser(userId);
	}

	@Override
	public Page<Order> getOrderByPage(Page<Order> page, Order order,
			List<Sort> sorts,String query,Admin admin,Long domainId) {
		Page<Order> result=page;
		boolean isSuperAdmin=admin.getIsSuper();
		List<Long> userList=null;
		if(isSuperAdmin||roleService.isSpecialAdmin(admin.getId())){
			isSuperAdmin=true;
			result=orderService.pageOrder(page, order, sorts,query,userList,isSuperAdmin,domainId);
		}else{
			List<Domain> domainList=domainService.getDomainByAdmin(admin.getId());
			if(domainList!=null&&domainList.size()>0){
				List<Long> domainIds=new ArrayList<Long>();
				for(Domain domainLocal:domainList){
					domainIds.add(domainLocal.getId());
				}
				userList=userService.getUserIdsByDomainIds(domainIds);
				if(userList!=null&&userList.size()>0){
					result=orderService.pageOrder(page, order, sorts,query,userList,isSuperAdmin,domainId);
				}
			}
		}
		return result;
	}

	@Override
	public List<OrderItemVo> getAllOrderItemsByOrder(Long id) {
		List<OrderItemVo> result=orderService.getAllOrderItemsByOrder(id);
		for(OrderItemVo item:result){
			String orderItemId=String.valueOf(item.getId());
			String machineNum=operation.getVMIdByOrderItem(orderItemId);
			if(StringUtils.isNotBlank(machineNum)){
				item.setMachineNum(machineNum);
			}
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
	
	/**
	 * <通过查询hc_order表的orderNo获得Order> 
	* <功能详细描述> 
	* @param orderNo
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	@Override
	public Order getOrderByOrderNo(String orderNo) {
		try {
			Order order = orderService.getOrderByOrderNo(orderNo);
			return order;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	/**
	 * <根据uuid来获取机器号为uuid的这台虚拟机的退款申请详情> 
	* <功能详细描述> 
	* @param uuid
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	@Override
	public VmRefundLog getVmRefundLogByUuid(String uuid) {
		try {
			VmRefundLog vrl = vmRefundLogService.getVmRefundLogByUUID(uuid);
			// 向VmRefundLog添加云主机名和外网IP，这2个字段不在hc_vm_refund_log表中
			Map<String, String> map = vmRefundLogService
					.getVmNameVmOuterIpByUuid(uuid);
			vrl.setVmName(map.get("vmName"));
			vrl.setOuterIp(map.get("outerIp"));
			return vrl;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}
	
	@Override
	@Transactional
	public boolean cancelOrder(Long orderId){
		
		try {
			//取消订单需要生成站内提醒信息
			Order order = orderService.findOrderById(orderId);	
			boolean result=orderService.cancel(orderId);
			if(result){
				Message message=OrderUtils.orderGenMessage(order,(short)6);
				messageService.saveMessage(message);
				//绑定邮件模版中需要的参数
				Map<String, String> orderMailTemplate = OrderUtils
						.generateMailVariable(order);
				
				//lihonglei 添加生成邮件
				User user = order.getUser();
                saveEmail(user, orderMailTemplate, MailTemplateType.ORDER_CANCEL_TEMPLATE.getType());

			}
			return result;
		} catch (Exception e) {			
			throw new HsCloudException("", logger, e);
		}		
	}
	
	@Override
	@Transactional
	//延期审核
	public boolean delayAudit(long orderId, int delayDays, String orderStatus,
			Admin admin) throws HsCloudException {
		if (logger.isDebugEnabled()) {
			logger.debug("enter delayAudit method.");
		}
		boolean result = false;
		try {
			// 延期审核调用订单业务逻辑，变更订单项生效时间
			String configDelayDays = siteConfigService.getConfigValue(
					Constants.ALLOW_DELAY_DAYS, "order");
			String configDelayBeginDate = siteConfigService.getConfigValue(
					Constants.DELAY_BEGIN_DATE, "order");
			OrderItem item = orderService.toDelay(orderId, orderStatus,
					delayDays, configDelayDays, configDelayBeginDate);
			Order order = orderService.findOrderById(orderId);
			if (item != null) {
				Long itemId = item.getId();
				// 首先判断虚拟机是否已经删除
				// 根据订单项Id更新订单项关联的虚拟机生效失效时间
				VpdcReference vm = operation.getReferenceByOrderItemId(itemId);
				Date expirationDate = item.getExpirationDate();
				operation.updateReferencePeriod(String.valueOf(itemId), null,
						expirationDate);
				// 如果虚拟机已经到期禁用，则进行激活操作
				operation.activeExpireVM(vm, admin.getId());
				// 取消订单需要生成站内提醒信息
				Message message = OrderUtils.orderGenMessage(order, (short) 8);
				messageService.saveMessage(message);
				// 绑定邮件模版中需要的参数
				Map<String, String> orderMailTemplate = OrderUtils
						.generateMailVariable(order);
				
				//lihonglei 添加生成邮件
				User u = order.getUser();
		        saveEmail(u, orderMailTemplate, MailTemplateType.ORDER_TRYDELAY_TEMPLATE.getType());
				result = true;
			}
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit delayAudit method.");
		}
		return result;
	}
	
	@Override
	@Transactional
	public Account getAccountByUserId(long id) {
		return accountService.getAccountByUserId(id);
	}

	@Override
	@Transactional
	public void accountDeposit(long userId,short paymentType,long accountUserId,long accountId,
			BigDecimal balance,String remark,Short depositSource,Short flow) {
		if(logger.isDebugEnabled()){
			logger.debug("enter accountDeposit method.");
			logger.debug("remark"+remark);
		}
		accountService.accountDeposit(userId,(short)0,paymentType,accountId, balance,"",remark,depositSource,flow);
		invoiceAccountService.deposit(accountUserId, balance.abs());
		Message message = new Message();
		message.setUserId(accountUserId);
		StringBuilder sb = new StringBuilder();
		BigDecimal bg = new BigDecimal(balance.toPlainString()).setScale(2);
		sb.append("账户充值: ").append(bg).append(" 元。");
		message.setMessage(sb.toString());
		messageService.saveMessage(message);
		if(logger.isDebugEnabled()){
			logger.debug("exit accountDeposit method.");
		}
	}
	
	@Override
	public void couponsDeposit(long userId, short paymentType,long accountUserId, long accountId, BigDecimal coupons,String remark,Short depositSource) {
		if(logger.isDebugEnabled()){
			logger.debug("enter couponsDeposit method.");
			logger.debug("remark"+remark);
		}
		accountService.couponsDeposit(userId,(short)0,paymentType,accountId, coupons,"",remark,depositSource);
		Message message = new Message();
		message.setUserId(accountUserId);
		StringBuilder sb = new StringBuilder();
		BigDecimal bg = new BigDecimal(coupons.toPlainString()).setScale(2);
		sb.append("返点充值: ").append(bg).append(" 点。");
		message.setMessage(sb.toString());
		messageService.saveMessage(message);
		if(logger.isDebugEnabled()){
			logger.debug("exit couponsDeposit method.");
		}
	}
	
	@Override
	@Transactional
	public void giftsDeposit(long userId, short paymentType,long accountUserId, long accountId, BigDecimal gifts,String remark, Short depositSource) {
		if(logger.isDebugEnabled()){
			logger.debug("enter giftsDeposit method.");
			logger.debug("remark"+remark);
		}
		accountService.giftsDeposit(userId,(short)0,paymentType,accountId, gifts,"",remark,depositSource);
		Message message = new Message();
		message.setUserId(accountUserId);
		StringBuilder sb = new StringBuilder();
		BigDecimal bg = new BigDecimal(gifts.toPlainString()).setScale(2);
		sb.append("礼金充值: ").append(bg).append(" 元。");
		message.setMessage(sb.toString());
		messageService.saveMessage(message);
		if(logger.isDebugEnabled()){
			logger.debug("exit giftsDeposit method.");
		}
		
	}
	
	@Override
	@Transactional
	public void accountDraw(long userId,short paymentType,long accountUserId,long accountId,BigDecimal balance,String remark,Short flow) {
		if(logger.isDebugEnabled()){
			logger.debug("enter accountDeposit method.");
			logger.debug("remark"+remark);
		}
		UserBank ub = userBankService.findBankByUserId(accountUserId);
		String bankAccount = null;
		if(null != ub){
			 bankAccount = ub.getAccount();
		}
		accountService.accountDraw(userId,(short)0, paymentType, accountId, bankAccount, balance, "", remark,flow);
		invoiceAccountService.refund(accountUserId, balance.abs());
		Message message = new Message();
		message.setUserId(accountUserId);
		StringBuilder sb = new StringBuilder();
		BigDecimal bg = new BigDecimal(balance.toPlainString()).setScale(2);
		sb.append("账户提现: ").append(bg.abs()).append(" 元。");
		message.setMessage(sb.toString());
		messageService.saveMessage(message);
		if(logger.isDebugEnabled()){
			logger.debug("exit accountDeposit method.");
		}
		
	}
	
	@Override
	@Transactional
	public void couponsDraw(long userId, short paymentType, long accountUserId,long accountId, BigDecimal coupons, String remark) {
		if(logger.isDebugEnabled()){
			logger.debug("enter couponsDraw method.");
			logger.debug("remark"+remark);
		}
		accountService.couponsDraw(userId,(short)0, paymentType, accountId,coupons, "", remark);
		Message message = new Message();
		message.setUserId(accountUserId);
		StringBuilder sb = new StringBuilder();
		BigDecimal bg = new BigDecimal(coupons.toPlainString()).setScale(2);
		sb.append("返点提现: ").append(bg.abs()).append(" 点。");
		message.setMessage(sb.toString());
		messageService.saveMessage(message);
		if(logger.isDebugEnabled()){
			logger.debug("exit couponsDraw method.");
		}
	}
	
	@Override
	@Transactional
	public void giftsDraw(long userId, Short paymentType, long accountUserId,
			long accountId, BigDecimal gifts, String remark) {
		if(logger.isDebugEnabled()){
			logger.debug("enter giftsDraw method.");
			logger.debug("remark"+remark);
		}
		accountService.giftsDraw(userId,(short)0, paymentType, accountId,gifts, "", remark);
		Message message = new Message();
		message.setUserId(accountUserId);
		StringBuilder sb = new StringBuilder();
		BigDecimal bg = new BigDecimal(gifts.toPlainString()).setScale(2);
		sb.append("礼金提现: ").append(bg.abs()).append(" 元。");
		message.setMessage(sb.toString());
		messageService.saveMessage(message);
		if(logger.isDebugEnabled()){
			logger.debug("exit giftsDraw method.");
		}
		
	}
	
	@Override
	@Transactional
	public void addTransactionLog(long consumeId, short paymentType, long accountUserId,long accountId,BigDecimal balance,BigDecimal coupons,BigDecimal gifts,String description,short consumeType) {
		if(logger.isDebugEnabled()){
			logger.debug("enter addTransactionLong method.");
			logger.debug("description:"+description);
		}	
		description = "（后台添加消费）："+description;
		long transctionId=accountService.accountConsume(consumeId,(short)0, paymentType, (short)0,accountId, description, null,consumeType, balance,coupons,gifts);
		Message message = new Message();
		message.setUserId(accountUserId);
		StringBuilder sb = new StringBuilder();
		BigDecimal bg = new BigDecimal(balance.toPlainString()).setScale(2);
		sb.append("账户消费：").append(bg.abs()).append(" 元。");
		if(new BigDecimal(0.0).compareTo(coupons.abs())==-1){
			sb.append("返点消费：").append(coupons.setScale(2).abs()).append(" 点。");
		}
		if(new BigDecimal(0.0).compareTo(gifts.abs())==-1){
			sb.append("礼金消费：").append(coupons.setScale(2).abs()).append(" 元。");
		}
		message.setMessage(sb.toString());
		User user=userService.getUser(accountUserId);
		IncomingLog incomingLog=new IncomingLog();
		incomingLog.setAccountId(accountId);
		incomingLog.setAmount(bg.abs());
		incomingLog.setEmail(user.getEmail());
		Date now=new Date();
		incomingLog.setDomainId(user.getDomain().getId());
		incomingLog.setEffectiveDate(now);
		incomingLog.setExpirationDate(now);
		incomingLog.setIncomingType((short)2);
		incomingLog.setProductType((short)3);
		incomingLog.setTransactionId(transctionId);
		incomingLog.setTranscationType(TranscationType.TRANSCATION_CONSUME.getIndex());
		incomingLogService.saveIncomingLog(incomingLog);
		messageService.saveMessage(message);
		if(logger.isDebugEnabled()){
			logger.debug("exit addTransactionLong method.");
		}
	}
	
	@Override
	public List<OrderItemVMVo> getOrderItemVMByOrderId(Long orderId)
			throws HsCloudException {
		return orderService.getOrderItemVMByOrderId(orderId);
	}
	
	@Override
	@Transactional
	public boolean vmRefund(long referenceId,String vmId, Admin admin,String ip,String refundRemark)
			throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter vm refund method.");			
		}
		boolean result = false;
		try {
			//退款的订单业务操作
			VpdcReference vr=operation.getReferenceById(referenceId);
			if(vr.getStatus()!=1){
			RefundResult refundResult=orderService.vmRefund(referenceId,vmId,null);
			User user=refundResult.getUser();
			long userId=user.getId();
			long adminId = admin.getId();
			//根据用户信息获取用户的账户信息
			Account account = accountService.getAccountByUserId(userId);
			long accountId = account.getId();
			//调用订单业务逻辑获取退款金额
			//退款需要生产新的订单，虚拟机需要重新与新的订单的订单项关联
			//获得原orderItemId和新OrderItem的一一对应关系
			Map<Long,OrderItem> remainOrderItem=refundResult.getUnRefundOrderItem();
			List<OrderItem> refundOrderItem=refundResult.getRefundItems();
			Date old = operation.getReferencePeriod(referenceId).getEndTime();
			String description=Constants.VM_PERIOD_LOG_QUIT;
			//添加虚拟机到期日期变更记录
			Date now=new Date();
			operation.updateReferencePeriod(referenceId, null,now);
			operation.saveVmPeriodLog(description,referenceId,old,now);
			//如果原订单包含的订单项没有全部退掉，没有退掉的订单项会重新生成订单，需要更新虚拟机与新订单的关联关系
			if(remainOrderItem!=null&&remainOrderItem.size()>0){
				Set<Long> oldItemIds=remainOrderItem.keySet();
			    for(Long oldItemId:oldItemIds){
			    	String oldOrderItemId=String.valueOf(oldItemId);
			    	OrderItem orderItemNew=remainOrderItem.get(oldItemId);
					String newOrderItemId=String.valueOf(orderItemNew.getId());
					VpdcReference_OrderItem orderItemReference=operation.getOrderItemByOrderItemId(oldOrderItemId);
					long oldReferenceId=orderItemReference.getVpdcRenferenceId();
					operation.saveReference_OrderItem(oldReferenceId,
							String.valueOf(newOrderItemId));
			    }
			}
			//已经使用的订单会生成一个新的订单，订单金额为已经使用的费用，新生成的订单也要跟虚拟机关联。
			if(refundOrderItem!=null&&refundOrderItem.size()>0){
			    for(OrderItem orderItem:refundOrderItem){
					String newOrderItemId=String.valueOf(orderItem.getId());
					operation.saveReference_OrderItem(referenceId,
							String.valueOf(newOrderItemId));
			    }
			}
			//订单操作导致账户变更发送站内信息提醒
			Message message=OrderUtils.orderGenMessageForRefund(Constants.MACHINENUM+vmId+"("+ip+")", refundResult,userId,(short)4);
			messageService.saveMessage(message);
			//账户变更 退款加入账户
			long transactionId=accountService.accountRefund(adminId,(short)0,
					PaymentType.PAYMENT_ONLINE.getIndex(),com.hisoft.hscloud.bss.billing.constant.ServiceType.SERVICE_CLOUD_COMPUTER.getIndex(),
					accountId,refundResult.getRefund(),refundResult.getRefundPoint(),refundResult.getRefundGift(),refundResult.getRemark(),referenceId);
			IncomingLog incomingLog=new IncomingLog();
			incomingLog.setAccountId(accountId);
			incomingLog.setAmount(refundResult.getRefund());
			incomingLog.setOrderId(refundResult.getCurrentOrderId());
			incomingLog.setOrderItemId(refundResult.getCurrentOrderItemId());
			incomingLog.setEffectiveDate(now);
			incomingLog.setExpirationDate(now);
			incomingLog.setEmail(user.getEmail());
			incomingLog.setDomainId(user.getDomain().getId());
			incomingLog.setIncomingType((short)1);
			incomingLog.setObjectId(referenceId);
			incomingLog.setProductType((short)1);
			incomingLog.setTranscationType(TranscationType.TRANSCATION_REFUND.getIndex());
			incomingLog.setTransactionId(transactionId);
			incomingLogService.saveIncomingLog(incomingLog);
			operation.terminate(referenceId, adminId,user,refundRemark);
			result = true;
			}
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
		if(logger.isDebugEnabled()){
			logger.debug("enter vm refund method.");			
		}
		return result;
	}
	
	@Override
	@Transactional
	public boolean vmRefundAll(long referenceId,String vmId, Admin admin,String ip,String refundRemark)
			throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter vm refund all method.");			
		}
		boolean result = false;
		try {
			//退款的订单业务操作
			VpdcReference vr=operation.getReferenceById(referenceId);
			if(vr.getStatus()!=1){
			RefundResult refundResult=orderService.vmRefundAll(referenceId,vmId,null);
			User user=refundResult.getUser();
			long userId=user.getId();
			long adminId = admin.getId();
			//根据用户信息获取用户的账户信息
			Account account = accountService.getAccountByUserId(userId);
			long accountId = account.getId();
			//调用订单业务逻辑获取退款金额
			//退款需要生产新的订单，虚拟机需要重新与新的订单的订单项关联
			//获得原orderItemId和新OrderItem的一一对应关系
			Map<Long,OrderItem> remainOrderItem=refundResult.getUnRefundOrderItem();
			Date old = operation.getReferencePeriod(referenceId).getEndTime();
			String description=Constants.VM_PERIOD_LOG_QUIT;
			//添加虚拟机到期日期变更记录
			Date now=new Date();
			operation.updateReferencePeriod(referenceId, null,now);
			operation.saveVmPeriodLog(description,referenceId,old,now);
			//如果原订单包含的订单项没有全部退掉，没有退掉的订单项会重新生成订单，需要更新虚拟机与新订单的关联关系
			if(remainOrderItem!=null&&remainOrderItem.size()>0){
				Set<Long> oldItemIds=remainOrderItem.keySet();
			    for(Long oldItemId:oldItemIds){
			    	String oldOrderItemId=String.valueOf(oldItemId);
			    	OrderItem orderItemNew=remainOrderItem.get(oldItemId);
					String newOrderItemId=String.valueOf(orderItemNew.getId());
					VpdcReference_OrderItem orderItemReference=operation.getOrderItemByOrderItemId(oldOrderItemId);
					long oldReferenceId=orderItemReference.getVpdcRenferenceId();
					operation.saveReference_OrderItem(oldReferenceId,
							String.valueOf(newOrderItemId));
			    }
			}
			//订单操作导致账户变更发送站内信息提醒
			Message message=OrderUtils.orderGenMessageForRefund(Constants.MACHINENUM+vmId+"("+ip+")", refundResult,userId,(short)3);
			messageService.saveMessage(message);
			//账户变更 退款加入账户
			long transactionId=accountService.accountRefund(adminId,(short)0,
					PaymentType.PAYMENT_ONLINE.getIndex(),
					com.hisoft.hscloud.bss.billing.constant.ServiceType.SERVICE_CLOUD_COMPUTER.getIndex(),
					accountId, refundResult.getRefund(),
					refundResult.getRefundPoint(),refundResult.getRefundGift(),
					refundResult.getRemark(),referenceId);
			IncomingLog incomingLog=new IncomingLog();
			incomingLog.setAccountId(accountId);
			incomingLog.setAmount(refundResult.getRefund());
			incomingLog.setEffectiveDate(now);
			incomingLog.setExpirationDate(now);
			incomingLog.setOrderId(refundResult.getCurrentOrderId());
			incomingLog.setOrderItemId(refundResult.getCurrentOrderItemId());
			incomingLog.setEmail(user.getEmail());
			incomingLog.setIncomingType((short)1);
			incomingLog.setDomainId(user.getDomain().getId());
			incomingLog.setObjectId(referenceId);
			incomingLog.setProductType((short)1);
			incomingLog.setTranscationType(TranscationType.TRANSCATION_REFUND_ALL.getIndex());
			incomingLog.setTransactionId(transactionId);
			incomingLogService.saveIncomingLog(incomingLog);
			operation.terminate(referenceId, adminId,user,refundRemark);
			result = true;
			}
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
		if(logger.isDebugEnabled()){
			logger.debug("enter vm refund all method.");			
		}
		return result;
	}
	
	@Override
	public List<VmRefundOrderItemVo> getVmRefundOrderItemVo(long referenceId)
			throws HsCloudException {
		return orderService.getVmRefundOrderItemVo(referenceId,new Date());
	}
	
	@Override
	public List<VmRefundOrderItemVo> getVmRefundOrderItemVoForAplly(
			long referenceId, Long refundLogId) throws HsCloudException {
		VmRefundLog  vmRefundLog=vmRefundLogService.getVmRefundLogById(refundLogId);
		return orderService.getVmRefundOrderItemVo(referenceId,vmRefundLog.getApplyDate());
	}
	@Override
	public Page<TranscationLogVO> pagePermissionTrLog(List<Sort> sorts,Page<TranscationLogVO> page,String query,
			List<Object> primKeys) {
		if(null != query){
			try {
				 query = new String(query.getBytes("iso8859_1"),"UTF-8");
			  } catch (UnsupportedEncodingException e) {
				 e.printStackTrace();
			  }
		}
		return transcationLogService.searchPermissionTranLog(sorts, page,query, primKeys);
	}
	
	@Override
	public Page<TranscationLogVO> findTransactionLog(Admin admin,
			Page<TranscationLogVO> page, QueryVO queryVO, List<Sort> sorts,
			List<Object> primKeys) throws HsCloudException{
		if(admin.getIsSuper() || roleService.isSpecialAdmin(admin.getId())){
			page = transcationLogService.findTransactionLog(page, queryVO, sorts, primKeys,null);
			return page;
		}else{
			List<Domain> domains = domainService.getDomainByAdmin(admin.getId());
			if(domains.isEmpty()){
				return page;
			}
			List<Long> ids = new ArrayList<Long>();
			for (Domain domain : domains) {
				ids.add(domain.getId());
			}
			page = transcationLogService.findTransactionLog(page, queryVO, sorts, primKeys,ids);
			return page;
		}
		
	}
	
	@Override
	public Page<VMResponsibility> findVMResponsibility(Admin admin,
			Page<VMResponsibility> page, QueryVO queryVO, List<Sort> sorts,
			List<Object> primKeys) throws HsCloudException {
		if(admin.getIsSuper() || roleService.isSpecialAdmin(admin.getId())){
			page = transcationLogService.findVMResponsibility(page, queryVO, sorts, primKeys,null);
			return page;
		}else{
			List<Domain> domains = domainService.getDomainByAdmin(admin.getId());
			if(domains.isEmpty()){
				return page;
			}
			List<Long> ids = new ArrayList<Long>();
			for (Domain domain : domains) {
				ids.add(domain.getId());
			}
			if(!ids.isEmpty()){
				queryVO.setDomainIds(ids);
			}
			page = transcationLogService.findVMResponsibility(page, queryVO, sorts, primKeys,ids);
			return page;
		}
	}
	
	
	@Override
	public IPDetail getIPDetailByIP(long ip) throws HsCloudException {
		IPDetail iPDetail = null;
		if(ip > 0){
			iPDetail = iPService.getIPDetailByIP(ip);
		}
		return iPDetail;
	}
	
	@Override
	public List<UserBrand> loadUserBrand() {
		
		return optionService.loadUserBrand();
		
	}
	
	@Override
	@Transactional
	public void saveServiceItemVo(ServiceItemVo siVo)throws HsCloudException {
		serviceItemService.saveSIVo(siVo);
	}
	
	@Override
	public ServiceItem getSIById(int id)throws HsCloudException {
		return serviceItemService.getSIById(id);
	}
	
	@Override
	public List<UserVO> getAllAvailableUser(String email) throws HsCloudException {		
		return userService.getAllAvailableUser(email);
	}
	
	@Override
	@Transactional
	public void approvedUser(long userId) {
		StringBuffer requestUrl = new StringBuffer("");		
		User u = userService.getUser(userId);
		Domain  domain = u.getDomain();
		String localUser =u.getEmail();
		u.setUpdateDate(new Date());
		u.setEnable(UserState.APPROVED.getIndex());
		Map<String,String> map = new HashMap<String,String>();
		map.put("username", u.getName());
		PlatformRelation platformRelation =platformRelationService.getPlatformRelationByLocalUser(String.valueOf(userId));
		//lihonglei 添加生成邮件
        saveEmail(u, map, MailTemplateType.APPROVED_USER_TEMPLATE.getType());
//        if(domain != null && (domain.getTransferFlag()==1)){
//        	String accessId = domain.getCode();
//        	String externalUser = platformRelation.getExternalUserId();
//        	AccessAccount accessAccount = accessAccountService.getAccessAccount(accessId);
//        	if(accessAccount != null){				
//        		try {
//        			String key = accessAccount.getAccessKey()+accessId + externalUser;
//        			String encryptedAccessKey = PasswordUtil.getEncyptedPasswd(key);
//        			//requestAddress为http://freecloud.yunhosting.com:8090/或http://cloud.xrnet.cn:8090/
//        			requestUrl.append(accessAccount.getRequestAddress()).append("syncaccount");
//        			requestUrl.append("accessid=").append(accessId);
//        			requestUrl.append("&accesskey=").append(encryptedAccessKey);
//        			requestUrl.append("&userid=").append(externalUser);
//        			requestUrl.append("&email=").append(localUser);
//        			RESTUtil.get(requestUrl.toString());
//        		} catch (Exception e) {
//        			throw new HsCloudException("", logger, e);
//        		}
//        	}
//        }		
	}
	
	@Override
	@Transactional
	public void supplieApproved(long id, boolean status) {
		User u = userService.getUser(id);
		UserProfile up = u.getUserProfile();
		if(null != up){
			up.setSupplier(status);
			if(!status){
			  up.setSupplierStatus(2);
			}
			u.setUserProfile(up);
		}
		/**
		 * mail
		 */
		User curUser = u;
		Domain domain = domainService
				.getDomainById(curUser.getDomain().getId());
		Map<String, String> map = new HashMap<String, String>();
		map.put("abbreviation", domain.getAbbreviation());
		map.put("userName", curUser.getName());
		map.put("webSiteUrl", CommonStatus.SERVER_URL);
		map.put("image", CommonStatus.MAIL_LOGO_SRC);
		String keyword = MailTemplateType.APPROVED_USER_TEMPLATE.getType();
		long domainId = domain.getId();
		mailService.saveEmail(curUser.getEmail(), null, keyword, domainId, map);
	}
	
	
	@Override
	@Transactional
	public boolean copySc(int id,String copySCName) throws HsCloudException {
		boolean result = false;
		try {
			ServiceCatalog newSc = new ServiceCatalog();
			ServiceCatalog oldSc = serviceCatalogService.get(id);
			List<ServiceItem> itemList=oldSc.getItems();
		    List<ServiceItem> newItemList=new ArrayList<ServiceItem>();
		    List<UserBrand> newBrandList=new ArrayList<UserBrand>();
		    List<ServerZone> newZoneList=new ArrayList<ServerZone>();
		    List<Domain> newDomainList=new ArrayList<Domain>();
			String[] ignoreProperties={"feeTypes","id","items","scIsolationConfig"};
			BeanUtils.copyProperties(oldSc,newSc,ignoreProperties);
			newSc.setName(copySCName);
			ScIsolationConfig newScIsolationConfig=new ScIsolationConfig();
			ScIsolationConfig oldScIsolationConfig=oldSc.getScIsolationConfig();
			BeanUtils.copyProperties(oldScIsolationConfig, newScIsolationConfig,new String[]{"id"});
			
			List<ScFeeType> feeTypes = oldSc.getFeeTypes();
			List<ScFeeType> newFeeTypes=new ArrayList<ScFeeType>();
			for (ScFeeType feeType : feeTypes) {
				ScFeeType feeTypeTemp=new ScFeeType();
				BeanUtils.copyProperties(feeType, feeTypeTemp,new String[]{"id","sc"});
				newFeeTypes.add(feeTypeTemp);
			}
			for(ServiceItem item:itemList){
				newItemList.add(item);
			}
			for(UserBrand brandTemp:oldSc.getUserBrand()){
				newBrandList.add(brandTemp);
			}
			for(Domain domainTemp:oldSc.getDomainList()){
				newDomainList.add(domainTemp);
			}
			for(ServerZone zoneTemp:oldSc.getZoneList()){
				newZoneList.add(zoneTemp);
			}
			newSc.setScIsolationConfig(newScIsolationConfig);
			newSc.setZoneList(newZoneList);
			newSc.setDomainList(newDomainList);
			newSc.setFeeTypes(newFeeTypes);
			newSc.setItems(newItemList);
			newSc.setUserBrand(newBrandList);
			serviceCatalogService.save(newSc);
			result=true;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
		return result;
	}
	
	@Override
	@Transactional
	public Excel  excelExport(Admin admin,QueryVO queryVO,List<Object> primKeys) {
		 List<TranscationLogVO> list = new ArrayList<TranscationLogVO>();
		 if(admin.getIsSuper() || roleService.isSpecialAdmin(admin.getId())){
			 list = transcationLogService.findTransactionLog(null,queryVO, primKeys);
		 }else{
			 List<Domain> domains = domainService.getDomainByAdmin(admin.getId());
			 if(!domains.isEmpty()){
				 List<Long> ids = new ArrayList<Long>();
				 for (Domain domain : domains) {
					 ids.add(domain.getId());
				}
				 list = transcationLogService.findTransactionLog(ids,queryVO, primKeys); 
			 }
		 }
		 
		 Map<String,List<Object>> datas = new HashMap<String, List<Object>>();
		 List<Object> objects = new ArrayList<Object>();
		 objects.addAll(list);
         datas.put("交易日志", objects);
		 InputStream is = FacadeImpl.class.getClassLoader().getResourceAsStream("exceltemplete/TranscationLog.xls");
         return new Excel(is, datas);
		
	}
	
	
	@Override
	@Transactional
	public Excel  excelExportApplication(Map<String,Object> queryMap) {
		 List<MoreExportAppDetail> list = new ArrayList<MoreExportAppDetail>();
		 list=issueApplicationService.exportAppDetailInfo(queryMap);
		 Map<String,List<Object>> datas = new HashMap<String, List<Object>>();
		 List<Object> objects = new ArrayList<Object>();
		 objects.addAll(list);
         datas.put("应用列表", objects);
		 InputStream is = FacadeImpl.class.getClassLoader().getResourceAsStream("exceltemplete/ApplicationList.xls");
         return new Excel(is, datas);
	}
	/**
	 * 应用评价列表导出
	* @param queryMap
	* @return Excel
	 */
	@Override
	@Transactional
	public Excel  excelExportAppEvaluate(EvaluatePageModel evaluatePageModel){
		 List<MoreAppProductReview> list = new ArrayList<MoreAppProductReview>();
		 List<MoreExportAppProductReview> exlist = new ArrayList<MoreExportAppProductReview>();
		 MoreExportAppProductReview moreExportAppProductReview;
		 list=evaluateService.findAllEvaluateListBySupplierId(evaluatePageModel);
		 for(int i=0;i<list.size();i++){
			 moreExportAppProductReview=new MoreExportAppProductReview();
			 moreExportAppProductReview.setAppName(list.get(i).getAppName());
			 moreExportAppProductReview.setReplyComment(list.get(i).getReplyComment());
			 moreExportAppProductReview.setScore(list.get(i).getScore());
			 moreExportAppProductReview.setComment(list.get(i).getComment());
			 moreExportAppProductReview.setUserName(list.get(i).getUserName());
			 moreExportAppProductReview.setCreateTime(list.get(i).getCreateTimeStr());
			 if(list.get(i).getReplyCount()>0){
				 moreExportAppProductReview.setReplyStatus("已回复");
			 }else{
				 moreExportAppProductReview.setReplyStatus("未回复");
			 }
			 exlist.add(moreExportAppProductReview);
			 
		 }
		 Map<String,List<Object>> datas = new HashMap<String, List<Object>>();
		 List<Object> objects = new ArrayList<Object>();
		 objects.addAll(exlist);
         datas.put("应用评价列表", objects);
		 InputStream is = FacadeImpl.class.getClassLoader().getResourceAsStream("exceltemplete/AppEvaluateList.xls");
        return new Excel(is, datas);
	}
	/**
	 * 应用发票列表导出
	* @param queryMap
	* @return Excel
	 */
	@Override
	@Transactional
	public Excel  excelExportApplicationInvoice(Map<String,Object> queryMap) {
		 List<ApplicationInvoiceRecordVO> list = new ArrayList<ApplicationInvoiceRecordVO>();
		 list=applicationInvoiceRecordService.exportAppInvoiceDetailInfo(queryMap);
		 Map<String,List<Object>> datas = new HashMap<String, List<Object>>();
		 List<Object> objects = new ArrayList<Object>();
		 objects.addAll(list);
         datas.put("应用发票列表", objects);
		 InputStream is = FacadeImpl.class.getClassLoader().getResourceAsStream("exceltemplete/ApplicationInvoiceList.xls");
         return new Excel(is, datas);
	}
	
//	@Override
//	public Excel responsibilityExcelExport(Admin admin,QueryVO queryVO) {
//		
//		
//		 Map<String,List<Object>> datas = new HashMap<String, List<Object>>();
//		 List<Statistics> statistics = new ArrayList<Statistics>();
//		 List<VMResponsibility> vmResp = new ArrayList<VMResponsibility>();
//		 List<TranscationLogVO> trLog = new ArrayList<TranscationLogVO>();
//		 List<OtherResponsibility> otherResp = new ArrayList<OtherResponsibility>();
//		 List<ResponsibilityIncoming> respIncome = new ArrayList<ResponsibilityIncoming>();
//		 List<CapitalSource> capitalSources = new ArrayList<CapitalSource>();
//		 try {
//			 List<Statistics> stas = transcationLogService.findNoReportStatistics(queryVO);
//			 for (Statistics s : stas) {
//					String month ="";
//					try {
//						Calendar c = Calendar.getInstance();
//						c.setTime(new SimpleDateFormat("yyyyMM").parse(s.getMonth()));
//						c.add(Calendar.MONTH, -1);
//						month = new SimpleDateFormat("yyyyMM").format(c.getTime());
//					} catch (ParseException e) {
//						e.printStackTrace();
//					}
//				 IncomingReport ir = transcationLogService.findPreMonthIncomingReport(month, Long.valueOf(queryVO.getDomainId()));
//				 IncomingReport cuir = new IncomingReport();
//				 cuir.setMonth(s.getMonth());
//				 cuir.setDomainId(Long.valueOf(queryVO.getDomainId()));
//				 BigDecimal dBalance = ir.getConsumeBalance().add(s.getDeposit()).subtract(s.getAbsDraw()).subtract(s.getAbsConsume()).add(s.getRefund());
//				 BigDecimal cBalance = ir.getDepositBalance().add(s.getAbsConsume()).subtract(s.getRefund()).subtract(s.getResponsibility());
//				 cuir.setConsumeBalance(cBalance);
//				 cuir.setDepositBalance(dBalance);
//				 transcationLogService.saveIncomingReport(cuir);
//			}
//		 } catch (Exception e) {
//		 }
//
//		 InputStream is = FacadeImpl.class.getClassLoader().getResourceAsStream(queryVO.getExportWay());
//		 if(admin.getIsSuper() || roleService.isSpecialAdmin(admin.getId())){
//			 if(null != is){
//				 return new Excel(is);
//			 }
//	         capitalSources = transcationLogService.findDepositCapitalSource(queryVO);
//	         respIncome =transcationLogService.findResponsibilityIncoming(queryVO);
//	         otherResp = transcationLogService.findOtherResponsibility(queryVO);
//	         trLog = transcationLogService.findTransactionLog(queryVO); 
//	         vmResp = transcationLogService.findVMResponsibility(queryVO);
//	         statistics = transcationLogService.findStatistics(queryVO);
//		 }else{
//			 List<Domain> domains = domainService.getDomainByAdmin(admin.getId());
//			 if(null != domains && !domains.isEmpty()){
//				 boolean flag = false;
//				 for (Domain domain : domains) {
//					 if(queryVO.getDomainId().equals(String.valueOf(domain.getId()))){
//						 flag = true;
//					 }
//				 }
//				 if(flag){
//					 if(null != is){
//						 return new Excel(is);
//					 }
//			         capitalSources = transcationLogService.findDepositCapitalSource(queryVO);
//			         respIncome =transcationLogService.findResponsibilityIncoming(queryVO);
//			         otherResp = transcationLogService.findOtherResponsibility(queryVO);
//			         trLog = transcationLogService.findTransactionLog(queryVO); 
//			         vmResp = transcationLogService.findVMResponsibility(queryVO);
//			         statistics = transcationLogService.findStatistics(queryVO);  
//				 }
//			 }
//			 
//		 }
//         datas.put("云统计表", new ArrayList<Object>(statistics));
//         datas.put("存款分析表", new ArrayList<Object>(capitalSources));
//         datas.put("权责收入", new ArrayList<Object>(respIncome));
//         datas.put("其它权责明细", new ArrayList<Object>(otherResp));
//         datas.put("交易日志", new ArrayList<Object>(trLog));
//         datas.put("云主机权责明细", new ArrayList<Object>(vmResp));
//		 InputStream tis = FacadeImpl.class.getClassLoader().getResourceAsStream("MonthIncoming.xls");
//         return new Excel(tis, datas);
//	}
	
	
	@Override
	public InputStream responsibilityExcelExport(Admin admin,QueryVO queryVO) {
		 Map<String,List<Object>> datas = new HashMap<String, List<Object>>();
		 List<Statistics> statistics = new ArrayList<Statistics>();
		 List<VMResponsibility> vmResp = new ArrayList<VMResponsibility>();
		 List<TranscationLogVO> trLog = new ArrayList<TranscationLogVO>();
		 List<OtherResponsibility> otherResp = new ArrayList<OtherResponsibility>();
		 List<ResponsibilityIncoming> respIncome = new ArrayList<ResponsibilityIncoming>();
		 List<CapitalSource> capitalSources = new ArrayList<CapitalSource>();
		 InputStream is = FacadeImpl.class.getClassLoader().getResourceAsStream(queryVO.getExportWay()+".zip");
		 if(admin.getIsSuper() || roleService.isSpecialAdmin(admin.getId())){
			 if(null != is){
				 return is;
			 }
	         capitalSources = reportService.findCapitalSource(Long.valueOf(queryVO.getDomainId()), queryVO.getMonth());
	         respIncome = reportService.findResponsibilityIncoming(Long.valueOf(queryVO.getDomainId()), queryVO.getMonth());
	         otherResp = reportService.findOtherResponsibility(queryVO);
	         statistics = reportService.findStatistics(Long.valueOf(queryVO.getDomainId()), queryVO.getMonth());
	         trLog = transcationLogService.findTransactionLog(queryVO); 
	         vmResp = transcationLogService.findVMResponsibility(queryVO);
		 }else{
			 List<Domain> domains = domainService.getDomainByAdmin(admin.getId());
			 if(null != domains && !domains.isEmpty()){
				 boolean flag = false;
				 for (Domain domain : domains) {
					 if(queryVO.getDomainId().equals(String.valueOf(domain.getId()))){
						 flag = true;
					 }
				 }
				 if(flag){
					 if(null != is){
						 return is;
					 }
			         capitalSources = reportService.findCapitalSource(Long.valueOf(queryVO.getDomainId()), queryVO.getMonth());
			         respIncome = reportService.findResponsibilityIncoming(Long.valueOf(queryVO.getDomainId()), queryVO.getMonth());
			         otherResp = reportService.findOtherResponsibility(queryVO);
			         statistics = reportService.findStatistics(Long.valueOf(queryVO.getDomainId()), queryVO.getMonth());
			         trLog = transcationLogService.findTransactionLog(queryVO); 
			         vmResp = transcationLogService.findVMResponsibility(queryVO);
				 }
			 }
			 
		 }
         datas.put("云统计表", new ArrayList<Object>(statistics));
         datas.put("存款分析表", new ArrayList<Object>(capitalSources));
         datas.put("权责收入", new ArrayList<Object>(respIncome));
         datas.put("其它权责明细", new ArrayList<Object>(otherResp));
         datas.put("交易日志", new ArrayList<Object>(trLog));
         datas.put("云主机权责明细", new ArrayList<Object>(vmResp));
		 InputStream tis = FacadeImpl.class.getClassLoader().getResourceAsStream("exceltemplete/MonthIncoming.xls");
		 Excel excel = new Excel(tis, datas);
	     excel.outputExcel(getClass().getResource("/").getPath() + queryVO.getExportWay()+".xls");
	     new Zip("responsibility/").doZip(getClass().getResource("/").getPath() + queryVO.getExportWay()+".xls");
		 is = FacadeImpl.class.getClassLoader().getResourceAsStream(queryVO.getExportWay()+".zip");
         return is;
	}
	
	@Override
	public Excel monthStatisExcelExport(Admin admin, QueryVO queryVO) {
		Map<String,List<Object>> datas = new HashMap<String, List<Object>>();
		List<Long> domainIds = new ArrayList<Long>();
		List<Long> l = new ArrayList<Long>();
		List<MonthStatisVO> mss;
		if(null != queryVO && null !=queryVO.getDomainId() && !"".equals(queryVO.getDomainId())){
			l.add(Long.valueOf(queryVO.getDomainId()));
		}
		if(admin.getIsSuper() || roleService.isSpecialAdmin(admin.getId())){
			domainIds.addAll(l);
			queryVO.setDomainIds(domainIds);
			mss = monthStatisService.getMonthStatis(queryVO);
		 }else{
			 List<Domain> domains = domainService.getDomainByAdmin(admin.getId());
             if(null == domains || domains.isEmpty()){
            	 return null;
             }
             if(null != domains && !domains.isEmpty()){
             	for (Domain domain : domains) {
             	   domainIds.add(domain.getId());
				}
             	domainIds.retainAll(l);
             }
             queryVO.setDomainIds(domainIds);
             mss =  monthStatisService.getMonthStatis(queryVO); 
		 }
		InputStream tis = FacadeImpl.class.getClassLoader().getResourceAsStream("exceltemplete/summary.xls");
		System.out.println(FacadeImpl.class.getClassLoader().getResource("exceltemplete/summary.xls"));
        datas.put("统计汇总", new ArrayList<Object>(mss));
        return  new Excel(tis, datas);
			
	}
	
	@Override
	@Transactional
	public Excel IPMessageExcelExport(Admin admin, QueryVO queryVO) {
		Map<String,List<Object>> datas = new HashMap<String, List<Object>>();
		List<Long> domainIds = new ArrayList<Long>();
		List<Long> l = new ArrayList<Long>();
		List<IpMessageVO> mss;
		if(admin.getIsSuper() || roleService.isSpecialAdmin(admin.getId())){
			domainIds.addAll(l);
			//queryVO.setDomainIds(domainIds);
			mss = ipMessageService.getIPMessage(queryVO);
		 }else{
			 List<Domain> domains = domainService.getDomainByAdmin(admin.getId());
             if(null == domains || domains.isEmpty()){
            	 return null;
             }
             if(null != domains && !domains.isEmpty()){
             	for (Domain domain : domains) {
             	   domainIds.add(domain.getId());
				}
             	domainIds.retainAll(l);
             }
//             queryVO.setDomainIds(domainIds);
             mss = ipMessageService.getIPMessage(queryVO); 
		 }
		InputStream tis = FacadeImpl.class.getClassLoader().getResourceAsStream("exceltemplete/IPMessage.xls");
		System.out.println(FacadeImpl.class.getClassLoader().getResource("exceltemplete/IPMessage.xls"));
        datas.put("采集录入表", new ArrayList<Object>(mss));
        
        

//		 InputStream tis = FacadeImpl.class.getClassLoader().getResourceAsStream("exceltemplete/MonthIncoming.xls");
//		 Excel excel = new Excel(tis, datas);
//	     excel.outputExcel(getClass().getResource("/").getPath() + "responsibility/IPMessage"+".xls");
//	     new Zip("responsibility/").doZip(getClass().getResource("/").getPath() + "responsibility/IPMessage"+".xls");
//	     InputStream is  = FacadeImpl.class.getClassLoader().getResourceAsStream("responsibility/IPMessage"+".zip");
//         return is;
        
        
        return  new Excel(tis, datas);
			
	}
	
	@Override
	@Transactional
	public void addBrand(String name, String desc,Integer rebateRate,Integer giftsDiscountRate,
			boolean approveOrNot) throws HsCloudException {
		UserBrand userBrand =new UserBrand();
		userBrand.setName(name);
		userBrand.setDescription(desc);
		userBrand.setRebateRate(rebateRate);
		userBrand.setGiftsDiscountRate(giftsDiscountRate);
		userBrand.setApprovalOrNot(approveOrNot);
		userBrandService.addBrand(userBrand);
		userBrand.setCode("brand"+userBrand.getId());
	}
	
	@Override
	@Transactional
	public void modifyBrand(Long brandId, String name, String desc,short status,Integer rebateRate,
			Integer giftsDiscountRate,boolean approveOrNot) throws HsCloudException {
		UserBrand brand=userBrandService.getBrandById(brandId);
		brand.setName(name);
		brand.setApprovalOrNot(approveOrNot);
		brand.setDescription(desc);
		brand.setRebateRate(rebateRate);
		brand.setGiftsDiscountRate(giftsDiscountRate);
		brand.setStatus(status);
		brand.setUpdateDate(new Date());
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page<UserBrand> getBrandByPage(String condition,
			Page<UserBrand> paging) throws HsCloudException {
		return userBrandService.getBrandByPage(condition, paging);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<UserBrand> getAllNormalBrand() throws HsCloudException {
		return userBrandService.getAllNormalBrand();
	}
	
	@Override
	@Transactional
	public void deleteBrandById(Long id) throws HsCloudException {
		UserBrand brand=userBrandService.getBrandById(id);
		brand.setStatus((short)0);
	}
	
	@Override
	@Transactional(readOnly=true)
	public boolean checkBrandNameDup(String name) throws HsCloudException {
		return userBrandService.checkBrandNameDup(name);
	}
	
	@Override
	@Transactional
	public void enableBrandById(Long id) throws HsCloudException {
		UserBrand brand=userBrandService.getBrandById(id);
		brand.setStatus((short)1);
	}
	
	@Override
	public List<OrderVo> getVmRelatedPaidOrder(long referenceId)
			throws HsCloudException {
		return orderService.getVmRelatedPaidOrder(referenceId);
	}
	
	@Override
	public BigDecimal checkAccountForRefund(long userId, BigDecimal money) {
		
		return invoiceAccountService.checkAccountForRefund(userId, money);
		
	}
	
	@Override
	public Page<PaymentInterface> getAllPaymentInterfaceByPage(String query,Page<PaymentInterface> paging,Admin admin)
			throws HsCloudException {
		if(admin.getIsSuper()||roleService.isSpecialAdmin(admin.getId())){
			return alipayService.getAllPaymentInterfaceByPage(query,paging,null);
		}else{
			List<Domain> domainList=domainService.getDomainByAdmin(admin.getId());
			return alipayService.getAllPaymentInterfaceByPage(query,paging,domainList);
		}
		
	}
	
	@Override
	@Transactional
	public void modifyPaymentInterface(PaymentInterfaceVO config)
			throws HsCloudException {
		PaymentInterface pi=alipayService.getPaymentInterfaceById(config.getId());
		pi.setDomain(config.getDomain());
		pi.setKey(config.getKey());
		pi.setPartner(config.getPartner());
		pi.setSellerEmail(config.getSellerEmail());
		pi.setUpdateDate(new Date());
	}
	
	@Override
	public boolean getPaymentInterfaceByDomain(Domain domain)
			throws HsCloudException {
		boolean result=false;
		PaymentInterface config=alipayService.getPaymentInterfaceByDomain(domain);
		if(config==null){
			result=true;
		}
		return result;
	}
	
	@Override
	@Transactional
	public void createPaymentInterface(PaymentInterface config)
			throws HsCloudException {
//		Map<String, String> properites = PropertiesUtils
//				.getProperties("domain.properties");
		//String domainUrl = properites.get("domainUrl");
		//String requestUrl=properites.get("paymentRequestUrl");
		//config.setNotifyUrl(domainUrl+Constants.PAYMENT_NOTIFY_URL);
		//config.setReturnUrl(domainUrl+Constants.PAYMENT_RETURN_URL);
		//config.setRequestUrl(requestUrl);
		alipayService.saveOrUpdatePaymentInterface(config);
	}
	
	@Override
	@Transactional
	public void disablePaymentInterface(long id) throws HsCloudException {
		PaymentInterface pi=alipayService.getPaymentInterfaceById(id);
		pi.setDataStatus((short)0);
		pi.setUpdateDate(new Date());
	}
	
	@Override
	@Transactional
	public void enablePaymentInterface(long id) throws HsCloudException {
		PaymentInterface pi=alipayService.getPaymentInterfaceById(id);
		pi.setDataStatus((short)1);
		pi.setUpdateDate(new Date());
	}
	
	@Override
	public List<Domain> loadDomain(boolean isSuper,long adminId) {
		
		if(isSuper ||roleService.isSpecialAdmin(adminId)){
			return domainService.getAllDomain();
		}else{
			return domainService.getDomainByAdmin(adminId);
		}
		
	}
	
	@Override
	public Page<Account> getAccountByPage(Admin admin, List<Sort> sorts,
			Page<Account> page, String type, String query) {
		
		Page<Account> result=null;
		if(admin.getIsSuper() || roleService.isSpecialAdmin(admin.getId())){
			result=accountService.searchAccount(type, query,sorts, page);
		}else{
			List<Domain> domains = domainService.getDomainByAdmin(admin.getId());
			if(domains.isEmpty()){
				return page;
			}
			List<Long> dids = new ArrayList<Long>();
            for (Domain domain : domains) {
            	dids.add(domain.getId());
			}
			result=accountService.searchAccount(type, query,sorts, page,dids);
		}
		return result;
	}
	
	@Override
	@Transactional
	public void reSendOpenEmail(String vmName, String ip, String email,String vmuuid)
			throws HsCloudException {
		try {
			VpdcInstance vi = operation.getInstanceByVmName(vmName);
			VpdcReference vr=operation.getReferenceByVmName(vmName);
			
			if(vi!=null){
				User user = userService.getUserByEmail(email);
				String pwd = PasswordUtil.getRandomNum(6);
				//创建控制面板用户
				String pwd2 = PasswordUtil.getEncyptedPasswd(pwd);
				controlPanelService.createControlUser(ip,pwd2,user.getId(), vi.getVmId());
				//发送邮件
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Map<String, String> map = new HashMap<String, String>();
				map.put("userName", user.getName());
				map.put("hostname", vmName);
				map.put("vmuuid", vmuuid);
				map.put("ip", ip);
				map.put("sysuser", vr.getRadom_user());
				map.put("syspwd", vr.getRadom_pwd());
				map.put("website", ip);
				//map.put("login", null);
				map.put("password", pwd);
				map.put("today", dateFormat.format(new Date()));
				
				Os os = (Os)serviceItemService.getSIById(vr.getOsId());
				if(os == null) {
				    map.put("port", "7755(Windows系统)/22(Linux系统)");
				} else {
				    map.put("port", os.getPort());
				}
				saveEmail(user, map, MailTemplateType.OPEN_VM_TEMPLATE.getType());
			}
		} catch (Exception e) {
			throw new HsCloudException("reSendOpenEmail Exception", logger, e);
		}
	}
	
	@Override
	public Page<SCVo> getRelatedSCByBrandId(long brandId,Page<SCVo> paging,String query)
			throws HsCloudException {
		return serviceCatalogService.getRelatedSCByBrandId(brandId,paging,query);
	}
	
	@Override
	public Page<SCVo> getUnRelatedScByBrandId(long brandId,Page<SCVo> paging,String query)
			throws HsCloudException {
		return serviceCatalogService.getUnRelatedScByBrandId(brandId,paging,query);
	}
	
	@Override
	@Transactional
	public void brandRelateSCOperation(Integer[] scIds, long brandId)
			throws HsCloudException {
		try {
			if (scIds != null && scIds.length > 0) {
				UserBrand ub = new UserBrand();
				ub.setId(brandId);
				for (int i = 0; i < scIds.length; i++) {
					ServiceCatalog sc = serviceCatalogService.get(scIds[i]);
					if (sc != null) {
						List<UserBrand> ubs = sc.getUserBrand();
						ubs.add(ub);
					}
				}
			}
		} catch (Exception e) {
			throw new HsCloudException("brandRelateSCOperation Exception",
					logger, e);
		}
		
	}
	
	@Override
	@Transactional
	public void brandUnRelateSCOperation(Integer[] scIds, long brandId)
			throws HsCloudException {
		try {
			if (scIds != null && scIds.length > 0) {
				UserBrand ub = userBrandService.getBrandById(brandId);
				for (int i = 0; i < scIds.length; i++) {
					ServiceCatalog sc = serviceCatalogService.get(scIds[i]);
					if (sc != null) {
						List<UserBrand> ubs = sc.getUserBrand();
						ubs.remove(ub);
					}
				}
			}
		} catch (Exception e) {
			throw new HsCloudException("brandRelateSCOperation Exception",
					logger, e);
		}
	}
	
	@Override
	public Page<HcEventResource> getResourceLog(Page<HcEventResource> paing,LogQueryVO param)
			throws HsCloudException {
		return resourceLogService.getResourceLog(paing,param);
	}
	
	@Override
	public Page<HcEventVmOps> getVmOpsLog(Page<HcEventVmOps> paing,LogQueryVO param)
			throws HsCloudException {
		return vmOpsLogService.getVmOpsLog(paing,param);
	}
	
	@Override
	public void resendResourceJobMessage(Long jobId)throws HsCloudException {
		String resourceJobMessage=resourceLogService.getResourceLogMessageById(jobId);
		if(StringUtils.isNotBlank(resourceJobMessage)){
			RabbitMQUtil rabbitmq=new RabbitMQUtil();
			try {
				rabbitmq.sendMessage(resourceJobMessage);
			} catch (Exception e) {
				throw new HsCloudException("resendResourceJobMessage send message to rabbitmq failure",logger,e);
			}
		}else{
			throw new HsCloudException("resendResourceJobMessage can't get the message to send.",logger);
		}
	}
	
	@Override
	public void resendOpsJobMessage(Long jobId)throws HsCloudException {
		String opsJobMessage=vmOpsLogService.getVMOpsLogMessageById(jobId);
		if(StringUtils.isNotBlank(opsJobMessage)){
			RabbitMQUtil rabbitmq=new RabbitMQUtil();
			try {
				rabbitmq.sendMessage(opsJobMessage);
			} catch (Exception e) {
				throw new HsCloudException("resendOpsJobMessage send message to rabbitmq failure",logger,e);
			}
		}else{
			throw new HsCloudException("resendOpsJobMessage can't get the message to send.",logger);
		}
	}
	
	@Override
	public void resendMessagerMessage(Long jobId)throws HsCloudException {
		
	}
	
	@Override
	public Page<VmRefundLog> getVmRefundLogByPage(Page<VmRefundLog> paging,
			String query, short status,Admin admin) throws HsCloudException {
		boolean isSuperAdmin=admin.getIsSuper();
		List<User> userList=null;
		List<String> ownerEmails=null;
		if(!isSuperAdmin&&!roleService.isSpecialAdmin(admin.getId())){
			List<Domain> domainList=domainService.getDomainByAdmin(admin.getId());
			if(domainList!=null&&domainList.size()>0){
				List<Long> domainIds=new ArrayList<Long>();
				for(Domain domainLocal:domainList){
					domainIds.add(domainLocal.getId());
				}
				userList=userService.getUsersByDomainIds(domainIds);
				if(userList!=null&&userList.size()>0){
					ownerEmails=new ArrayList<String>();
					for(User u:userList){
						ownerEmails.add(u.getEmail());
					}
				}else{
					return paging;
				}
			}
		}
		Page<VmRefundLog> result=vmRefundLogService.vmRefundLogPaging(status, query, paging,ownerEmails);
		List<VmRefundLog> resultList=result.getResult();
		if(resultList!=null&&resultList.size()>0){
			for(VmRefundLog e:resultList){
				Long referenceId=e.getReferenceId();
				VpdcReference vr=operation.getReferenceById(referenceId);
				if(vr!=null){
					e.setOuterIp(vr.getVm_outerIP());
					e.setVmName(vr.getName());
				}
				
			}
		}
		return result;
	}
	
	@Override
	@Transactional
	public void rejectRefundApply(Long id,String rejectReason,Admin admin) {
		VmRefundLog  vmRefundLog=vmRefundLogService.getVmRefundLogById(id);
		vmRefundLog.setOperator(admin.getEmail());
		vmRefundLog.setStatus((short)3);
		vmRefundLog.setUpdateDate(new Date());
		vmRefundLog.setRefundDate(new Date());
		vmRefundLog.setRejectRefundReason(rejectReason);
	}
	
	@Override
	@Transactional
	public boolean vmRefundForApply(long referenceId, String vmId, Admin admin,Long refundLogId) throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter vmRefundForApplymethod.");			
		}
		boolean result = false;
		try {
			//退款的订单业务操作
			VmRefundLog  vmRefundLog=vmRefundLogService.getVmRefundLogById(refundLogId);
			VpdcReference vr=operation.getReferenceById(referenceId);
			if(vmRefundLog.getStatus()==1&&vr.getStatus()!=1){
			RefundResult refundResult=orderService.vmRefund(referenceId,vmId,vmRefundLog.getApplyDate());
			User user=refundResult.getUser();
			long userId=user.getId();
			long adminId = admin.getId();
			//根据用户信息获取用户的账户信息
			Account account = accountService.getAccountByUserId(userId);
			long accountId = account.getId();
			//退款需要生产新的订单，虚拟机需要重新与新的订单的订单项关联
			//获得原orderItemId和新OrderItem的一一对应关系
			Map<Long,OrderItem> remainOrderItem=refundResult.getUnRefundOrderItem();
			List<OrderItem> refundOrderItem=refundResult.getRefundItems();
			Date old = operation.getReferencePeriod(referenceId).getEndTime();
			String description=Constants.VM_PERIOD_LOG_QUIT;
			//添加虚拟机到期日期变更记录
			Date now=new Date();
			operation.updateReferencePeriod(referenceId, null,now);
			operation.saveVmPeriodLog(description,referenceId,old,now);
			//如果原订单包含的订单项没有全部退掉，没有退掉的订单项会重新生成订单，需要更新虚拟机与新订单的关联关系
			if(remainOrderItem!=null&&remainOrderItem.size()>0){
				Set<Long> oldItemIds=remainOrderItem.keySet();
			    for(Long oldItemId:oldItemIds){
			    	String oldOrderItemId=String.valueOf(oldItemId);
			    	OrderItem orderItemNew=remainOrderItem.get(oldItemId);
					String newOrderItemId=String.valueOf(orderItemNew.getId());
					VpdcReference_OrderItem orderItemReference=operation.getOrderItemByOrderItemId(oldOrderItemId);
					long oldReferenceId=orderItemReference.getVpdcRenferenceId();
					operation.saveReference_OrderItem(oldReferenceId,
							String.valueOf(newOrderItemId));
			    }
			}
			if(refundOrderItem!=null&&refundOrderItem.size()>0){
			    for(OrderItem orderItem:refundOrderItem){
					String newOrderItemId=String.valueOf(orderItem.getId());
					operation.saveReference_OrderItem(referenceId,
							String.valueOf(newOrderItemId));
			    }
			}
			
			vmRefundLog.setOperator(admin.getEmail());
			vmRefundLog.setRefundAmount(refundResult.getRefund());
			vmRefundLog.setRefundDate(new Date());
			vmRefundLog.setUpdateDate(new Date());
			vmRefundLog.setRefundType((short)1);
			vmRefundLog.setStatus((short)2);
			vmRefundLog.setRefundPointAmount(refundResult.getRefundPoint());
			vmRefundLog.setRefundGiftAmount(refundResult.getRefundGift());
			//订单操作导致账户变更发送站内信息提醒
			String ip=vr.getVm_outerIP();
			Message message=OrderUtils.orderGenMessageForRefund(Constants.MACHINENUM+vmId+"("+ip+")", refundResult,userId,(short)4);
			messageService.saveMessage(message);
			//账户变更 退款加入账户
			long transactionId=accountService.accountRefund(adminId,(short)0,
					PaymentType.PAYMENT_ONLINE.getIndex(),
					com.hisoft.hscloud.bss.billing.constant.ServiceType.SERVICE_CLOUD_COMPUTER.getIndex(), 
					accountId,refundResult.getRefund(),
					refundResult.getRefundPoint(),refundResult.getRefundGift(),
					refundResult.getRemark(),referenceId);
			IncomingLog incomingLog=new IncomingLog();
			incomingLog.setAccountId(accountId);
			incomingLog.setAmount(refundResult.getRefund());
			incomingLog.setEffectiveDate(now);
			incomingLog.setOrderId(refundResult.getCurrentOrderId());
			incomingLog.setOrderItemId(refundResult.getCurrentOrderItemId());
			incomingLog.setExpirationDate(now);
			incomingLog.setEmail(user.getEmail());
			incomingLog.setIncomingType((short)1);
			incomingLog.setObjectId(referenceId);
			incomingLog.setDomainId(user.getDomain().getId());
			incomingLog.setProductType((short)1);
			incomingLog.setTranscationType(TranscationType.TRANSCATION_REFUND.getIndex());
			incomingLog.setTransactionId(transactionId);
			incomingLogService.saveIncomingLog(incomingLog);
			operation.terminate(referenceId, adminId,user,vmRefundLog.getApplyRefundReason());
			result = true;
			}
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
		if(logger.isDebugEnabled()){
			logger.debug("enter vmRefundForApply method.");			
		}
		return result;
	}
	
	@Override
	@Transactional
	public boolean vmRefundAllForApply(long referenceId, String vmId,Admin admin,Long refundLogId)
			throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter vmRefundAllForApply method.");			
		}
		boolean result = false;
		try {
			VmRefundLog  vmRefundLog=vmRefundLogService.getVmRefundLogById(refundLogId);
			VpdcReference vr=operation.getReferenceById(referenceId);
			//退款的订单业务操作
			if(vmRefundLog.getStatus()==1&&vr.getStatus()!=1){
			RefundResult refundResult=orderService.vmRefundAll(referenceId,vmId,vmRefundLog.getApplyDate());
			User user=refundResult.getUser();
			long userId=user.getId();
			long adminId = admin.getId();
			//根据用户信息获取用户的账户信息
			Account account = accountService.getAccountByUserId(userId);
			long accountId = account.getId();
			//退款需要生产新的订单，虚拟机需要重新与新的订单的订单项关联
			//获得原orderItemId和新OrderItem的一一对应关系
			Map<Long,OrderItem> remainOrderItem=refundResult.getUnRefundOrderItem();
			Date old = operation.getReferencePeriod(referenceId).getEndTime();
			String description=Constants.VM_PERIOD_LOG_QUIT;
			//添加虚拟机到期日期变更记录
			Date now=new Date();
			operation.updateReferencePeriod(referenceId, null,now);
			operation.saveVmPeriodLog(description,referenceId,old,now);
			//如果原订单包含的订单项没有全部退掉，没有退掉的订单项会重新生成订单，需要更新虚拟机与新订单的关联关系
			if(remainOrderItem!=null&&remainOrderItem.size()>0){
				Set<Long> oldItemIds=remainOrderItem.keySet();
			    for(Long oldItemId:oldItemIds){
			    	String oldOrderItemId=String.valueOf(oldItemId);
			    	OrderItem orderItemNew=remainOrderItem.get(oldItemId);
					String newOrderItemId=String.valueOf(orderItemNew.getId());
					VpdcReference_OrderItem orderItemReference=operation.getOrderItemByOrderItemId(oldOrderItemId);
					long oldReferenceId=orderItemReference.getVpdcRenferenceId();
					operation.saveReference_OrderItem(oldReferenceId,
							String.valueOf(newOrderItemId));
			    }
			}
			String orderNo=null;
			//查询全额退款修改后的orderNo
			Long orderId=refundResult.getCurrentOrderId();
			if(null==orderId||"".equals(orderId)){//获取未生效的订单号
				orderNo=orderService.getVmNoteffectiveOrderNo(vmId);
			}else{
				Order order=orderService.getOrderById(orderId);
				
				 orderNo=order.getOrderNo();
			}
			
		
			//订单操作导致账户变更发送站内信息提醒
			vmRefundLog.setOperator(admin.getEmail());
			vmRefundLog.setRefundAmount(refundResult.getRefund());
			vmRefundLog.setRefundDate(new Date());
			vmRefundLog.setUpdateDate(new Date());
			vmRefundLog.setRefundType((short)2);
			vmRefundLog.setStatus((short)2);
			vmRefundLog.setRefundPointAmount(refundResult.getRefundPoint());
			vmRefundLog.setOrderNo(orderNo);
			
			
			
			
			//订单操作导致账户变更发送站内信息提醒
			String ip=vr.getVm_outerIP();
			Message message=OrderUtils.orderGenMessageForRefund(Constants.MACHINENUM+vmId+"("+ip+")", refundResult,userId,(short)3);
			messageService.saveMessage(message);
			//账户变更 退款加入账户
			long transactionId=accountService.accountRefund(adminId,(short)0,
					PaymentType.PAYMENT_ONLINE.getIndex(),
					com.hisoft.hscloud.bss.billing.constant.ServiceType.SERVICE_CLOUD_COMPUTER.getIndex(),
					accountId, refundResult.getRefund(),
					refundResult.getRefundPoint(),refundResult.getRefundGift(),
					refundResult.getRemark(),referenceId);
			IncomingLog incomingLog=new IncomingLog();
			incomingLog.setAccountId(accountId);
			incomingLog.setAmount(refundResult.getRefund());
			incomingLog.setEffectiveDate(now);
			incomingLog.setExpirationDate(now);
			incomingLog.setOrderId(refundResult.getCurrentOrderId());
			incomingLog.setOrderItemId(refundResult.getCurrentOrderItemId());
			incomingLog.setEmail(user.getEmail());
			incomingLog.setIncomingType((short)1);
			incomingLog.setObjectId(referenceId);
			incomingLog.setProductType((short)1);
			incomingLog.setDomainId(user.getDomain().getId());
			incomingLog.setTranscationType(TranscationType.TRANSCATION_REFUND_ALL.getIndex());
			incomingLog.setTransactionId(transactionId);
			incomingLogService.saveIncomingLog(incomingLog);
			operation.terminate(referenceId, adminId,user,vmRefundLog.getApplyRefundReason());
			result = true;
			}
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
		if(logger.isDebugEnabled()){
			logger.debug("enter vmRefundAllForApply method.");			
		}
		return result;
	}
	
	@Override
	public Page<MonthIncomingVO> getMonthIncomingMonths(Page<MonthIncomingVO> page,Admin admin,QueryVO queryVO) {
		
		List<Long> domainIds;
		if(admin.getIsSuper() || roleService.isSpecialAdmin(admin.getId())){
			     return monthIncomingService.getMonthIncomingMonths(page, queryVO);
		 }else{
			 List<Domain> domains = domainService.getDomainByAdmin(admin.getId());
             if(null == domains || domains.isEmpty()){
            	 return page;
             }
             if(null != domains && !domains.isEmpty()){
            	 domainIds = new ArrayList<Long>();
            	 for (Domain domain : domains) {
            		 domainIds.add(domain.getId());
				}
            	queryVO.setDomainIds(domainIds);
             }
			 return monthIncomingService.getMonthIncomingMonths(page, queryVO); 
		 }
	}
	
	@Override
	public Page<UserBrandVO> getRelatedBrand(Page<UserBrandVO> page, long domainId,
			String domainName) throws HsCloudException {
		return domainService.getRelatedBrand(page, domainId, domainName);
	}
	
	@Override
	public Page<UserBrandVO> getUnRelatedBrand(Page<UserBrandVO> page,
			long domainId, String domainName) throws HsCloudException {
		return domainService.getUnRelatedBrand(page, domainId, domainName);
	}
	
	@Override
	@Transactional
	public boolean associateBrandAndDomain(Long[] brandIds, long domainId)
			throws HsCloudException {
		boolean resultFlag = false;
		UserBrand userBrand = null;
		try {
			if (brandIds != null && brandIds.length > 0) {
				Domain domain = domainService.getDomainById(domainId);
				for (int i = 0; i < brandIds.length; i++) {
					userBrand = new UserBrand();
					if (domain != null) {
						userBrand.setId(brandIds[i]);
						List<UserBrand> brandList = domain.getUserBrandList();
						brandList.add(userBrand);
					}
				}
				resultFlag = true;
			}
		} catch (Exception e) {
			throw new HsCloudException("associateBrandAndDomain Exception",
					logger, e);
		}
		return resultFlag;
	}
	
	@Override
	@Transactional
	public boolean disAssociateBrandAndDomain(Long[] brandIds, long domainId)
			throws HsCloudException {
		boolean resultFlag = false;
		try {
			if (brandIds != null && brandIds.length > 0) {
				Domain domain = domainService.getDomainById(domainId);
				for (int i = 0; i < brandIds.length; i++) {
					UserBrand userBrand = userBrandService.getBrandById(brandIds[i]);
					if (domain != null) {
						List<UserBrand> brandList = domain.getUserBrandList();
						brandList.remove(userBrand);
					}
				}
				resultFlag = true;
			}
		} catch (Exception e) {
			throw new HsCloudException("disAssociateBrandAndDomain Exception",
					logger, e);
		}
		return resultFlag;
	}
	
	@Override
	public List<UserBrandVO> getRelatedBrandByDomainId(long domainId)
			throws HsCloudException {
		return domainService.getRelatedBrandByDomainId(domainId);
	}
	
	/**
     * <查询分平台下的品牌> 
    * <功能详细描述> 
    * @param abbreviation
    * @return
    * @throws HsCloudException 
    * @see [类、类#方法、类#成员]
     */
	@Override
	public List<UserBrandVO> getRelatedBrand(String abbreviation) throws HsCloudException {
		return domainService.getRelatedBrand(abbreviation);
	}	

	@Override
	public Page<BusinessPlatformVO> getBusinessPlatformByPage(
			Page<BusinessPlatformVO> paging, String query, Admin admin)
			throws HsCloudException {
		List<Long> domainIds=null;
		if(!admin.getIsSuper()&&!roleService.isSpecialAdmin(admin.getId())){
			List<Domain> domainList=domainService.getDomainByAdmin(admin.getId());
			domainIds=new ArrayList<Long>();
			if(domainList!=null&&domainList.size()>0){
				for(Domain domainLocal:domainList){
					domainIds.add(domainLocal.getId());
				}
			}
		}
		if(domainIds!=null&&domainIds.size()==0){
			return paging;
		}else{
			return businessPlatformService.getAllBusinessPlatformByPage(paging,query,domainIds);
		}
	}
	
	@Override
	public Page<ControlPanelVO> getControlPanelVOByPage(
			Page<ControlPanelVO> paging, String query, Admin admin)
			throws HsCloudException {
		List<Long> domainIds=null;
		if(!admin.getIsSuper()&&!roleService.isSpecialAdmin(admin.getId())){
			List<Domain> domainList=domainService.getDomainByAdmin(admin.getId());
			domainIds=new ArrayList<Long>();
			if(domainList!=null&&domainList.size()>0){
				for(Domain domainLocal:domainList){
					domainIds.add(domainLocal.getId());
				}
			}
		}
		if(domainIds!=null&&domainIds.size()==0){
			return paging;
		}else{
			return controlPanelMaintenanceService.getAllControlPanelByPage(paging, query,domainIds);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public String getBusinessPlatformLoginUrl(Long userId,Admin admin)
			throws HsCloudException {
		String result="";
		try {
			User user=userService.getUser(userId);
			if (user != null) {
				String originalKey = user.getEmail() + user.getPassword();
				String authorKey = PasswordUtil.getEncyptedPasswd(originalKey);
				String dateKey = SecurityHelper.EncryptData(
						HsCloudDateUtil.getNowStr(),
						Constants.DEFAULT_SECURITY_KEY);
				dateKey = URLEncoder.encode(dateKey);
				String domainPublishUrl=user.getDomain().getPublishingAddress();
				String appUrl =domainPublishUrl+ "/user_mgmt/loginByUrl!userLoginByUrl.action?";
				result = appUrl + "authorKey=" + authorKey + "&userId="
						+ userId + "&dateKey=" + dateKey+"&operator="+admin.getEmail();
			}
			return result;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public String getControlPanelLoginUrl(String vmId,Admin admin) throws HsCloudException {
		String result="";
		try {
			ControlPanelUser cpUser = controlPanelService
					.findControlUserByVmID(vmId);
			if (cpUser != null) {
				String originalKey = vmId + cpUser.getUserPassword();
				String authorKey = PasswordUtil.getEncyptedPasswd(originalKey);
				String dateKey = SecurityHelper.EncryptData(
						HsCloudDateUtil.getNowStr(),
						Constants.DEFAULT_SECURITY_KEY);
				dateKey = URLEncoder.encode(dateKey);
				//Map<String, String> properties = PropertiesUtils
				//		.getProperties("mail.properties");
				//String appUrl = properties.get("controlpanelLoginUrl");
				long userId=operation.getReferenceByVmId(vmId).getOwner();
				User user=userService.getUser(userId);
				Domain domain=user.getDomain();
				String appUrl=domain.getPublishing_address_cp()+Constants.CP_LOGIN_ACTION;
				result = appUrl + "authorKey=" + authorKey + "&vmId="
						+ vmId + "&dateKey=" + dateKey+"&operator="+admin.getEmail();
			}
			return result;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}
	
	@Override
	public Page<MarketingPromotionVO> getMarketingPromotionByPage(String condition, 
			Page<MarketingPromotion> paging) throws HsCloudException {
		Page<MarketingPromotion> mp_page = marketingPromotionService.getMarketingPromotionByPage(condition, paging);
		Page<MarketingPromotionVO> mp_VO_page = new Page<MarketingPromotionVO>();
		List<MarketingPromotion> mp_list = mp_page.getResult();
		List<MarketingPromotionVO> mp_VO_list = new ArrayList<MarketingPromotionVO>();
		// 通过domian_id查询出Domain,再把domain_abbreviation添加到Page<MarketingPromotion>中
		for (int i = 0; i < mp_list.size(); i++) {
			MarketingPromotionVO mp_VO = new MarketingPromotionVO(
					mp_list.get(i).getId(), 
					mp_list.get(i).getName(), 
					mp_list.get(i).getCode(), 
					mp_list.get(i).getAddress(), 
					mp_list.get(i).getDomain_id(), 
					mp_list.get(i).getBrand_code(), 
					mp_list.get(i).getStatus(), 
					mp_list.get(i).getDescription(), 
					mp_list.get(i).getCreateDate()
			);
			String abbreviation = domainService.getDomainById(
					mp_list.get(i).getDomain_id()).getAbbreviation();
			mp_VO.setDomain_abbreviation(abbreviation);
			String brand_name = userBrandService.getBrandByCode(
					mp_list.get(i).getBrand_code()).getName();
			mp_VO.setBrand_name(brand_name);
			mp_VO_list.add(mp_VO);
		}
		mp_VO_page.setResult(mp_VO_list);
		mp_VO_page.setTotalCount(mp_page.getTotalCount());
		mp_VO_page.setPageNo(mp_page.getPageNo());
		mp_VO_page.setPageSize(mp_page.getPageSize());
		return mp_VO_page;
	}

	/**
	 * <启用市场推广> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	@Override
	@Transactional
	public void enableMarketingPromotionById(Long id) throws HsCloudException {
		MarketingPromotion mp=marketingPromotionService.getMarketingPromotionById(id);
		mp.setStatus((short)1);
	}
	
	/**
	 * <停用市场推广> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	@Override
	@Transactional
	public void disableMarketingPromotionById(Long id) throws HsCloudException {
		MarketingPromotion mp=marketingPromotionService.getMarketingPromotionById(id);
		mp.setStatus((short)0);
	}
	
	/**
	 * <1.校验推广名称是否重复> <功能详细描述>
	 * <2.校验推广代码是否重复> <功能详细描述>
	 * <3.校验推广地址是否重复> <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	@Override
	@Transactional(readOnly=true)
	public boolean checkMarketingPromotionParameterDup(String name, String code, 
			String address) throws HsCloudException {
		return marketingPromotionService.checkMarketingPromotionParameterDup(name,code,address);
	}
	
	/**
	 * <添加市场推广> <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	@Override
	@Transactional
	public void addMarketingPromotion(String name, String code, String address,Long domain_id, 
			String brand_code, String description, Admin admin) throws HsCloudException {
	    MarketingPromotion mp = new MarketingPromotion();
		mp.setCreateDate(new Date());
		mp.setCreateId(admin.getId());
		mp.setName(name);
		mp.setUpdateDate(new Date());
		mp.setUpdateId(admin.getId());
		mp.setVersion(admin.getVersion());
		mp.setAddress(address);
		mp.setBrand_code(brand_code);
		mp.setCode(code);
		mp.setDescription(description);
		mp.setDomain_id(domain_id);
		mp.setStatus((short) 1);
		marketingPromotionService.addMarketingPromotion(mp);
	}
	
	/**
	 * <修改市场推广> <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	@Override
	@Transactional
	public void modifyMarketingPromotion(Long id, String name, String address, Long domain_id, 
			String brand_code, String description, Admin admin) throws HsCloudException {
		MarketingPromotion mp = marketingPromotionService.getMarketingPromotionById(id);
		mp.setName(name);
		mp.setUpdateDate(new Date());
		mp.setUpdateId(admin.getId());
		mp.setVersion(admin.getVersion());
		mp.setAddress(address);
		mp.setBrand_code(brand_code);
		mp.setDescription(description);
		mp.setDomain_id(domain_id);
	}
	@Override
	public UserUserBrandVO getUserAndBrandByReferenceId(long referenceId)
			throws HsCloudException {
		return userBrandService.getUserAndBrandByReferenceId(referenceId);
	}
	@Override
	public List<SCVo> getRelatedScByReferenceId(long referenceId)
			throws HsCloudException {
		return serviceCatalogService.getRelatedScByReferenceId(referenceId);
	}
	@Override
	@Transactional
	public void changeVMSC(long referenceId, int scId) throws HsCloudException {
		VpdcReference vr=operation.getReferenceById(referenceId);
		vr.setScId(scId);
	}
	/**
	 * @author jianwei zhang
	 * <vm资源控制>
	 */
	@Override
	@Transactional
	public void changeVMResourceLimit(ResourceLimit resourceLimit, String id)
			throws HsCloudException {
		operation.updateResourceLimit(resourceLimit, id);
	}
	@Override
	@Transactional
	public void insertOperationLog(Admin admin, String description,
			String actionName, short operationResult) throws HsCloudException {
		if(admin!=null){
			OperationLog log=new OperationLog();
			log.setActionName(actionName);
			log.setDescription(description);
			log.setOperationDate(new Date());
			log.setUserName(admin.getName());
			log.setOperationResult(operationResult);
			log.setOperator(admin.getEmail());
			log.setOperatorType(LogOperatorType.ADMIN.getIndex());
			logService.insertOperationLog(log);
		}
	}
	@Override
	public boolean updateZoneOfRolePermission(long roleId, String oldZoneIds,
			String newZoneIds) throws HsCloudException {
		return rolePermissionService.updateZoneOfRolePermission(roleId, oldZoneIds, newZoneIds);
	}
	@Override
	public Page<Map<String, Object>> findZoneListOfRole(
			Page<Map<String, Object>> page, long roleId) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		Page<ServerZone> zonePage = new Page<ServerZone>();
		zonePage.setPageNo(page.getPageNo());
		zonePage.setPageSize(page.getPageSize());
		zonePage = zoneService.findServerZone(zonePage,"isEnable",1);
		List<ServerZone> zoneList = zonePage.getResult();
		for(ServerZone serverZone:zoneList){
			map = new HashMap<String, Object>();
			map.put("id", serverZone.getId());
            map.put("name", serverZone.getName());
            map.put("param1", "false");
            if(rolePermissionService.hasZoneOfRolePermission(roleId, serverZone.getId())){
            	map.put("param1", "true");
            }
            result.add(map);
		}
        page.setResult(result);
        page.setTotalCount(zonePage.getTotalCount());
        return page;
	}
	@Override
	public List<Object> getZoneIdsByAdminId(long adminId) {
		return rolePermissionService.getZoneIdsByAdminId(adminId);
	}
	
	@Override
	@Transactional
	public VncClientVO getClientVnc(String uuid) throws HsCloudException{
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
			
			//处理vnc参数
			JSONObject responseJson = JSONObject.fromObject(response);
			Object responsePort = JSONObject.fromObject(responseJson.get("Node")).get("port");
			Integer port = Integer.valueOf(responsePort.toString());
			Integer proxyPort = operation.getAvailablePort(null);
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
					//null作为对管理员的处理方式
					operation.useVncPort(proxyPort.toString(), null, uuid);
				} catch (Exception e) {
					throw new HsCloudException(Constants.VM_VNC_ERROR, "getClientVnc异常", logger,e);
				}
			}
		} catch (HsCloudException e) {
			if(e.getClass()==new HsCloudException().getClass()){
				HsCloudException ex = (HsCloudException)e;
				throw new HsCloudException(ex.getCode(), "getClientVnc异常", logger, ex);
			}else{
				throw new HsCloudException(Constants.VM_VNC_ERROR, "getClientVnc异常", logger,
						e);
			}
		}
		return vnc;
	}	
	
	@Override
	@Transactional
	public boolean recycleRestore(String uuid, Admin admin) throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter restoreVM method.");			
		}
		boolean bl = false;
		bl = operation.recycleRestore(uuid,admin);
		if(logger.isDebugEnabled()){
			logger.debug("exit restoreVM method.");
		}
		return bl;
	}

	@Override
	@Transactional
	public boolean recycleTerminate(String uuid, Admin admin)
			throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter forceTerminate method.");			
		}
		boolean bl = false;
		bl = operation.recycleTerminate(uuid,admin);
		if(logger.isDebugEnabled()){
			logger.debug("exit forceTerminate method.");
		}
		return bl;
	}
	@Override
	public Page<OperationLog> getOperationByPage(Page<OperationLog> paging,
			OperationLogQueryVo queryVo) throws HsCloudException {
		return logService.getOperationByPage(paging, queryVo);
	}
	@Override
	public IPDetail getIPDetailByIP(String ip) throws HsCloudException {
		IPDetail iPDetail = null;
		if (ip != null ){
			iPDetail=iPService.getIPDetailByIP(ip);
		}
		return iPDetail;
	}
	@Override
	@Transactional
	public boolean deleteIPOfVm(String uuid, String oldIP, long adminId)
			throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter deleteIPOfVm method.");			
		}
		boolean result = false;
		if(StringUtils.isNotEmpty(oldIP)){
			result = operation.deleteIPOfVm(uuid, oldIP, adminId);
		}
		if(logger.isDebugEnabled()){
			logger.debug("exit deleteIPOfVm method.");
		}
		return result;
	}
	@Override
	@Transactional
	public boolean addIPOfVm(String uuid, String newIP, long adminId)
			throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter addIPOfVm method");
		}
		boolean result = false;
		if(StringUtils.isNotEmpty(newIP)){
			Admin admin=adminService.getAdminById(adminId);
			result = operation.addIPOfVm(uuid, newIP, admin);
		}
		if(logger.isDebugEnabled()){
			logger.debug("exit addIPOfVm method.");
		}
		return result;
	}
	@Override
	public boolean isSpecialAdmin(Admin admin) throws HsCloudException {
		boolean specialFlag = false;
		if(admin.getIsSuper()){
			specialFlag = true;
		}
		if(roleService.isSpecialAdmin(admin.getId())){
			specialFlag = true;
		}
		return specialFlag;
	}
	
	@Override
	@Transactional
	public Page<MonthStatisVO> getMonthStatis(
			Page<MonthStatisVO> page, Admin admin, QueryVO queryVO) {
		
		List<Long> domainIds = new ArrayList<Long>();
		List<Long> l = new ArrayList<Long>();
		if(null != queryVO && null !=queryVO.getDomainId() && !"".equals(queryVO.getDomainId())){
			l.add(Long.valueOf(queryVO.getDomainId()));
		}
		if(admin.getIsSuper() || roleService.isSpecialAdmin(admin.getId())){
			domainIds.addAll(l);
			queryVO.setDomainIds(domainIds);
			return monthStatisService.getMonthStatisByPage(page, queryVO);
		 }else{
			 List<Domain> domains = domainService.getDomainByAdmin(admin.getId());
             if(null == domains || domains.isEmpty()){
            	 return page;
             }
             if(null != domains && !domains.isEmpty()){
             	for (Domain domain : domains) {
             	   domainIds.add(domain.getId());
				}
             	if(!l.isEmpty()){
                 	domainIds.retainAll(l);
             	}
             }
             queryVO.setDomainIds(domainIds);
			 return monthStatisService.getMonthStatisByPage(page, queryVO); 
		 }
	}
	@Override
	public List<ZoneVO> getZoneByAdmin(Admin admin)
			throws HsCloudException {
		boolean needFilter=true;
		boolean isSuperAdmin=admin.getIsSuper();
		if(isSuperAdmin||roleService.isSpecialAdmin(admin.getId())){
			needFilter=false;
		}
		return zoneService.getZoneByAdmin(admin.getId(), needFilter);
	}
	@Override
	public void updateAccountRate(AccountVO accountVO) {
		accountService.updateAccountRate(accountVO);
		
	}
	@Override
	public Page<ResponsibilityIncoming> getMonthIncoming(List<Sort> sort,Page<ResponsibilityIncoming> page,Admin admin,QueryVO queryVO) {
		List<Long> domainIds;
		if(admin.getIsSuper() || roleService.isSpecialAdmin(admin.getId())){
			     //return monthIncomingService.getMonthIncomingMonths(page, queryVO);
			return reportService.findResponsibilityByPage(sort,page, queryVO);
		 }else{
			 List<Domain> domains = domainService.getDomainByAdmin(admin.getId());
             if(null == domains || domains.isEmpty()){
            	 return page;
             }
             if(null != domains && !domains.isEmpty()){
            	 domainIds = new ArrayList<Long>();
            	 for (Domain domain : domains) {
            		 domainIds.add(domain.getId());
				}
            	queryVO.setDomainIds(domainIds);
             }
			 return reportService.findResponsibilityByPage(sort,page, queryVO);
		 }
		
	}
	@Override
	public boolean freezeNode(long id, long userId) {
		boolean resultFlag = false;
		ServerNode serverNode = nodeService.getNodeById(id);
		if(serverNode != null){
			serverNode.setIsEnable(1);
			serverNode.setUpdateDate(new Date());
		}
		resultFlag = nodeService.enableServerNode(serverNode);
		return resultFlag;
	}
	
	@Override
	public boolean activeNode(long id, long userId) {
		boolean resultFlag = false;
		ServerNode serverNode = nodeService.getNodeById(id);
		if(serverNode != null){
			serverNode.setIsEnable(0);
			serverNode.setUpdateDate(new Date());
		}
		resultFlag = nodeService.enableServerNode(serverNode);
		return resultFlag;
	}
	@Override
	public ServiceCatalog getByCode(String scCode, String domainCode, String brandCode) {
		
		return serviceCatalogService.getByCode(scCode, domainCode,brandCode);
	}
	@Override
	public List<DomainVO> getDomainCodebyId(List<Domain> domainList) {
		
		return serviceCatalogService.getDomainCodebyId(domainList);
	}
	@Override
	public boolean verifyUserBrandDiscountRate(User user,Integer pointRebate,
			Integer giftRebate) {
		UserBrand ub=userBrandService.getBrandByCode(user.getLevel());
		boolean result=checkSumOfPointRebateGiftRebate(pointRebate,giftRebate,
				ub.getRebateRate(),ub.getGiftsDiscountRate());
		
		return result;
	}
	@Override
	public boolean verifyUserAccountDiscountRate(User user,Integer pointRebate,
			Integer giftRebate) {
		Account acc=accountService.getAccountByUserId(user.getId());
		boolean result=checkSumOfPointRebateGiftRebate(pointRebate,giftRebate,
				acc.getCouponsRebateRate(),acc.getGiftsRebateRate());
		return result;
	}
	
	private boolean checkSumOfPointRebateGiftRebate(Integer currentPointRebate,
			Integer currentGiftRebate,
			Integer needToComparePointRebate,Integer needToCompareGiftRebate){
		if(currentPointRebate>0&&currentGiftRebate>0){
			if(currentPointRebate+currentGiftRebate>100){
				return false;
			}
		}
		
		if(currentPointRebate<=0&&currentGiftRebate>0){
			if(needToComparePointRebate!=null){
				if((currentGiftRebate+needToComparePointRebate)>100){
					return false;
				}
			}
		}
		if(currentGiftRebate<=0&&currentPointRebate>0){
			if(needToCompareGiftRebate!=null){
				if((currentPointRebate+needToCompareGiftRebate)>100){
					return false;
				}
			}
		}
		return true;
	}
	@Override
	public boolean hasServiceCatalogCode(ServiceCatalog serviceCatalog) {
		
		return serviceCatalogService.hasServiceCatalogCode(serviceCatalog);
	}
	@Override
	public Object getOuterInfomation(String externalUser, String accessId)
			throws HsCloudException {
		Object result=null;
		StringBuffer requestUrl = new StringBuffer("");		
		try {			
			AccessAccount accessAccount = accessAccountService.getAccessAccount(accessId);
			if(accessAccount != null){
				String key = accessAccount.getAccessKey()+accessId + externalUser;
				String encryptedAccessKey = PasswordUtil.getEncyptedPasswd(key);			
				//requestAddress为http://yunhosting.com/api/hscloudregapi.asp?act=
				//               http://203.158.18.12/pass/transfer_hscloud.php?method=
				requestUrl.append(accessAccount.getRequestAddress()).append("boss_userinfo");
				requestUrl.append("&accessid=").append(accessId);
				requestUrl.append("&accesskey=").append(encryptedAccessKey);
				requestUrl.append("&userid=").append(externalUser);				
				result = RESTUtil.getJson(requestUrl.toString());
			}			
		} catch (Exception e) {
			throw new HsCloudException("", "getOuterInfomation异常", logger, e);
		}
		return result;
	}
	@Override
	public String synchroExternalUser(String externalUser,String localUser,long userId,String accessId)
			throws HsCloudException {
		String result = "";
		StringBuffer requestUrl = new StringBuffer("");		
		try {	
			PlatformRelation platformRelation =platformRelationService.getPlatformRelationByExternalUser(externalUser);
			PlatformRelation platformRelation2 =platformRelationService.getPlatformRelationByLocalUser(Long.toString(userId));
			
			if(platformRelation != null && platformRelation2==null){
				platformRelation.setUserId(Long.toString(userId));				
			}else if(platformRelation == null && platformRelation2!=null){
				platformRelation2.setExternalUserId(externalUser);
				platformRelation=platformRelation2;
				
			}else if(platformRelation != null && platformRelation2!=null){
				String flag=platformRelationService.deletePlatformRelation(platformRelation2);
				if("success"==flag){
					platformRelation.setUserId(Long.toString(userId));					
					}else{
						throw new HsCloudException("", "建立关联关系异常", logger, null); 
					}				
				
			}else{
				platformRelation = new PlatformRelation();				
				platformRelation.setUserId(Long.toString(userId));
				platformRelation.setExternalUserId(externalUser);
			}		
			result = platformRelationService.synchroPlatformRelation(platformRelation);			
			AccessAccount accessAccount = accessAccountService.getAccessAccount(accessId);
			if(accessAccount != null){
				String key = accessAccount.getAccessKey()+accessId + externalUser;
				String encryptedAccessKey = PasswordUtil.getEncyptedPasswd(key);
				//requestAddress为http://freecloud.yunhosting.com:8090/或http://cloud.xrnet.cn:8090/
				requestUrl.append(accessAccount.getRequestAddress()).append("syncaccount");
				requestUrl.append("&accessid=").append(accessId);
				requestUrl.append("&accesskey=").append(encryptedAccessKey);
				requestUrl.append("&userid=").append(externalUser);
				requestUrl.append("&email=").append(localUser);
				result = RESTUtil.get(requestUrl.toString());
			}			
		} catch (Exception e) {
			throw new HsCloudException("", "synchroExternalUser异常", logger, e);
		}
		return result;
	}
	@Override
	@Transactional
	public String synchroExternalUser2(long userId)
			throws HsCloudException {
		String result = "";
		StringBuffer requestUrl = new StringBuffer("");		
		try {			
			PlatformRelation platformRelation =platformRelationService.getPlatformRelationByLocalUser(Long.toString(userId));
			if(platformRelation != null){
				String externalUser=platformRelation.getExternalUserId();
				User user=userService.getUser(userId);
				String accessId=user.getDomain().getCode();
				String email=user.getEmail();
				if(externalUser!=null && user!=null && accessId!=null && email!=null){
					AccessAccount accessAccount = accessAccountService.getAccessAccount(accessId);
					if(accessAccount != null){
						String key = accessAccount.getAccessKey()+accessId + externalUser;
						String encryptedAccessKey = PasswordUtil.getEncyptedPasswd(key);
						//requestAddress为http://freecloud.yunhosting.com:8090/或http://cloud.xrnet.cn:8090/
						requestUrl.append(accessAccount.getRequestAddress()).append("syncaccount");
						requestUrl.append("&accessid=").append(accessId);
						requestUrl.append("&accesskey=").append(encryptedAccessKey);
						requestUrl.append("&userid=").append(externalUser);
						requestUrl.append("&email=").append(email);
						result = RESTUtil.get(requestUrl.toString());
				}
			}									
			}			
		} catch (Exception e) {
			throw new HsCloudException("", "synchroExternalUser异常", logger, e);
		}
		return result;
	}
	@Override
	public Page<OsVO> getUnRelatedServerOs(Page<OsVO> page, long zoneGroupId,
			String OsName) throws HsCloudException {
		
		return zoneService.getUnRelatedServerOs(page, zoneGroupId, OsName);
	}
	@Override
	public Page<OsVO> getRelatedServerOs(Page<OsVO> page, long zoneGroupId,
			String OsName) throws HsCloudException {
		return zoneService.getRelatedServerOs(page, zoneGroupId, OsName);
	}
	@Override
	@Transactional
	public boolean associateOsAndZoneGroup(int[] osIds, long groupId)
			throws HsCloudException {
		boolean resultFlag = false;
		try {
			if (osIds != null && osIds.length > 0) {
				ZoneGroup zoneGroup = zoneGroupService.getZoneGroupById(groupId);
				zoneGroup.setId(groupId);
				for (int i = 0; i < osIds.length; i++) {
					Os os = serviceItemService.getOs(osIds[i]);
					if (os != null) {
						List<ZoneGroup> groupList = os.getZoneGroupList();
						groupList.add(zoneGroup);
					}
				}
				resultFlag = true;
			}
		} catch (Exception e) {
			throw new HsCloudException("associateOsAndZoneGroup Exception",
					logger, e);
		}
		return resultFlag;
	}
	@Override
	@Transactional
	public boolean disAssociateOsAndZoneGroup(int[] osIds, long groupId)
			throws HsCloudException {
		boolean resultFlag = false;
		try {
			if (osIds != null && osIds.length > 0) {
				ZoneGroup zoneGroup=zoneGroupService.getZoneGroupById(groupId);
				for (int i = 0; i < osIds.length; i++) {
					Os os = serviceItemService.getOs(osIds[i]);
					if (os != null) {
						List<ZoneGroup> groupList = os.getZoneGroupList();
						groupList.remove(zoneGroup);
					}
				}
				resultFlag = true;
			}
		} catch (Exception e) {
			throw new HsCloudException("associateOsAndZoneGroup Exception",
					logger, e);
		}
		return resultFlag;
		
	}
	@Override
	@Transactional
	public void saveSmsMessage(SMSMessage smsMessage) {
		List<String> mobileList=userService.getAllUserMobile();
		smsMessageService.saveSmsMessage(smsMessage,mobileList);
	}
	@Override
	public Page<SMSMessage> findSmsMessagePage(Page<SMSMessage> page,String query) {
		return smsMessageService.findSmsMessagePage(page,query);
	}
	@Override
	@Transactional
	public void delSmsMessage(long id,String name) {
		SMSMessage smsMessage=smsMessageService.findSmsMessageById(id);
		smsMessage.setStatus(1);
		smsMessage.setUpdateDate(new Date());
		smsMessage.setDeleter(name);
		smsMessageService.delSmsMessage(smsMessage);
	}
	
	@Override
	public List<AppWorkOrderType> getWorkOrderTpye(){
		return workOrderService.getTypes();
	}
	@Override
	public Page<ApplicationTranscationLogVO> getAppTransactionByPage(
			List<Sort> sorts, Page<ApplicationTranscationLogVO> page,
			String query) {
		return applicationTranscationLogService.getAppTransactionByPage(sorts,page,query);
	}
	@Override
	public Excel excelExportAppBill(Map<String, Object> param) {
		List<ExportAppBillDetailInfo> lst=appBillService.exportAppBillDetailInfo(param); 			 
		Map<String,List<Object>> datas = new HashMap<String, List<Object>>();
		List<Object> objects = new ArrayList<Object>();
		objects.addAll(lst);
        datas.put("账单日志", objects);
		InputStream is = FacadeImpl.class.getClassLoader().getResourceAsStream("exceltemplete/AppBillTranscationLog.xls");
		return new Excel(is, datas);
	}
	@Override
	public Excel excelExportAppOrder(Map<String, Object> param) {
		List<ExportAppOrderDetailInfo> lst=appOrderService.exportAppOrderDetailInfo(param); 			 
		Map<String,List<Object>> datas = new HashMap<String, List<Object>>();
		List<Object> objects = new ArrayList<Object>();
		objects.addAll(lst);
		datas.put("订单信息", objects);
		InputStream is = FacadeImpl.class.getClassLoader().getResourceAsStream("exceltemplete/AppOrderDetail.xls");
		return new Excel(is, datas);
	}

}