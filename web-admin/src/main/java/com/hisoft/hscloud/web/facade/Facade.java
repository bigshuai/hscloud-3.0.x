package com.hisoft.hscloud.web.facade;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.entity.Account;
import com.hisoft.hscloud.bss.billing.vo.AccountVO;
import com.hisoft.hscloud.bss.billing.vo.ApplicationTranscationLogVO;
import com.hisoft.hscloud.bss.billing.vo.MonthIncomingVO;
import com.hisoft.hscloud.bss.billing.vo.MonthStatisVO;
import com.hisoft.hscloud.bss.billing.vo.QueryVO;
import com.hisoft.hscloud.bss.billing.vo.ResponsibilityIncoming;
import com.hisoft.hscloud.bss.billing.vo.TranscationLogVO;
import com.hisoft.hscloud.bss.billing.vo.VMResponsibility;
import com.hisoft.hscloud.bss.sla.om.entity.Order;
import com.hisoft.hscloud.bss.sla.om.entity.VmRefundLog;
import com.hisoft.hscloud.bss.sla.om.util.ExcelExport;
import com.hisoft.hscloud.bss.sla.om.vo.InvoiceVO;
import com.hisoft.hscloud.bss.sla.om.vo.OrderItemVMVo;
import com.hisoft.hscloud.bss.sla.om.vo.OrderItemVo;
import com.hisoft.hscloud.bss.sla.om.vo.OrderVo;
import com.hisoft.hscloud.bss.sla.om.vo.VmRefundOrderItemVo;
import com.hisoft.hscloud.bss.sla.sc.entity.Os;
import com.hisoft.hscloud.bss.sla.sc.entity.ServerNode;
import com.hisoft.hscloud.bss.sla.sc.entity.ServerZone;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceCatalog;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceItem;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceType;
import com.hisoft.hscloud.bss.sla.sc.entity.ZoneGroup;
import com.hisoft.hscloud.bss.sla.sc.vo.OsVO;
import com.hisoft.hscloud.bss.sla.sc.vo.SCVo;
import com.hisoft.hscloud.bss.sla.sc.vo.ServiceItemVo;
import com.hisoft.hscloud.bss.sla.sc.vo.ZoneVO;
import com.hisoft.hscloud.common.entity.Excel;
import com.hisoft.hscloud.common.entity.HcEventResource;
import com.hisoft.hscloud.common.entity.HcEventVmOps;
import com.hisoft.hscloud.common.entity.MarketingPromotion;
import com.hisoft.hscloud.common.entity.OperationLog;
import com.hisoft.hscloud.common.entity.Province;
import com.hisoft.hscloud.common.entity.UserBrand;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.common.vo.MarketingPromotionVO;
import com.hisoft.hscloud.common.vo.OperationLogQueryVo;
import com.hisoft.hscloud.common.vo.UserBrandVO;
import com.hisoft.hscloud.common.vo.UserUserBrandVO;
import com.hisoft.hscloud.crm.usermanager.entity.AbstractUser;
import com.hisoft.hscloud.crm.usermanager.entity.Action;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.entity.Country;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.crm.usermanager.entity.Industry;
import com.hisoft.hscloud.crm.usermanager.entity.Region;
import com.hisoft.hscloud.crm.usermanager.entity.ResourceType;
import com.hisoft.hscloud.crm.usermanager.entity.Role;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.vo.AccessAccountVO;
import com.hisoft.hscloud.crm.usermanager.vo.AdminMenuVO;
import com.hisoft.hscloud.crm.usermanager.vo.AdminVO;
import com.hisoft.hscloud.crm.usermanager.vo.DomainVO;
import com.hisoft.hscloud.crm.usermanager.vo.PrivilegeVO;
import com.hisoft.hscloud.crm.usermanager.vo.UserProfileVo;
import com.hisoft.hscloud.crm.usermanager.vo.UserVO;
import com.hisoft.hscloud.message.entity.SMSMessage;
import com.hisoft.hscloud.message.vo.AnnouncementVO;
import com.hisoft.hscloud.payment.alipay.entity.PaymentInterface;
import com.hisoft.hscloud.payment.alipay.vo.PaymentInterfaceVO;
import com.hisoft.hscloud.systemmanagement.vo.BusinessPlatformVO;
import com.hisoft.hscloud.systemmanagement.vo.ControlPanelVO;
import com.hisoft.hscloud.systemmanagement.vo.LogQueryVO;
import com.hisoft.hscloud.systemmanagement.vo.ProcessResourceVO;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.image.vo.FileVO;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.image.vo.ImageVO;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.entity.IPDetail;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.vo.IPDetailInfoVO;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.vo.IPDetailVO;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.vo.IPRangeVO;
import com.hisoft.hscloud.vpdc.ops.json.bean.CreateVmBean;
import com.hisoft.hscloud.vpdc.ops.json.bean.NetWorkBean;
import com.hisoft.hscloud.vpdc.ops.json.bean.ResourceLimit;
import com.hisoft.hscloud.vpdc.ops.vo.FlavorVO;
import com.hisoft.hscloud.vpdc.ops.vo.InstanceDetailVo;
import com.hisoft.hscloud.vpdc.ops.vo.VmInfoVO;
import com.hisoft.hscloud.vpdc.ops.vo.VmSnapShotVO;
import com.hisoft.hscloud.vpdc.ops.vo.VncClientVO;
import com.hisoft.hscloud.vpdc.ops.vo.VolumeVO;
import com.hisoft.hscloud.vpdc.ops.vo.VrouterTemplateVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.CPUHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.DiskHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.HostInfoVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.HostOverviewInfoVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.MemoryHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.NetHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.VmDetailInfoVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.VmInfoMonitorVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.VmOverviewInfoVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.VmRealtimeMonitorVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.WholeOverviewInfoVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.ZoneOverviewInfoVO;
import com.wgdawn.conditions.model.EvaluatePageModel;
import com.wgdawn.persist.model.AppWorkOrderType;

/**
 * 
 * <一句话功能简述> <功能详细描述>
 * 
 * @author houyh
 * @version [1.4, 2012-10-8]
 * @see [相关类/方法]
 * @since [HSCLOUD/1.4]
 */
public interface Facade {
	/**
	 * <获取用户列表> <功能详细描述>
	 * 对 超级管理员 和 与超级管理员有相同权利的管理能看到 所有内容
	 * 其它根据查询条件和域查询
	 * @param sorts
	 * @param page
	 * @param userId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public Page<User> getAllUserByPage(Admin admin,List<Sort> sorts, Page<User> page,
			long userId, String type, String query);

	/**
	 * <获取User国家信息> <功能详细描述>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<Country> loadCountry();

	/**
	 * <获取User行业信息> <功能详细描述>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<Industry> loadIndustry();

	/**
	 * <根据国家ID获取Region信息> <功能详细描述>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<Region> loadRegion(long countryId);
	
	/**
	 * 
	 * @return
	 */
	public List<UserBrand> loadUserBrand();

	/**
	 * <根据userId删除用户> <功能详细描述>
	 * 
	 * @param userId
	 * @see [类、类#方法、类#成员]
	 */
	public void deleteUser(long userId);
	
	public void freezedUser(long userId);

	/**
	 * <重置用户密码> <功能详细描述>
	 * 
	 * @param user
	 * @see [类、类#方法、类#成员]
	 */
	public void resetPasswd(String password, long id);

	/**
	 * <添加用户> <功能详细描述>
	 * 
	 * @param user
	 * @see [类、类#方法、类#成员]
	 */
	public void addUser(User user, String companyName);

	/**
	 * <根据邮箱获取用户信息> <功能详细描述>
	 * 
	 * @param email
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public User getUserByEmail(String email);

	public Admin loginUserByEmail(String email, String password);

	public Role getRoleById(long roleId);

	public String deleteRole(long roleId);

//	public void modifyRole(Role role);

	public Page<Role> getAllRoleByPage(Page<Role> page, String roleName);

	public Page<Admin> pageAdminByRoleId(Page<Admin> page, long roleId);

	

	public Page<AdminVO> getAllAdminUser(List<Sort> sorts,String query, int start, int limit,
			int page,String type);

	/******************************* OPS Module ********************************/	
	/**
	 * <Admin用户创建虚拟机>
	 * 
	 * @param fb
	 * @param cvb
	 * @param uid
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String createVM(CreateVmBean cvb, long uid) throws HsCloudException;

	/**
	 * <启动虚拟机>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean startVmByVmId(String vmId,Object o,String otype) throws HsCloudException;

	/**
	 * <重启虚拟机>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String rebootVmByVmId(String vmId,Object o,String otype) throws HsCloudException;

	/**
	 * <关闭虚拟机>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean closeVmByVmId(String vmId,Object o,String otype) throws HsCloudException;
	
	/**
	 * <系统修复>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean repairOSByVmId(String vmId,Object o,String otype) throws HsCloudException;

	/**
	 * <唤醒虚拟机>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	//public String resumeVmByVmId(String vmId) throws HsCloudException;

	/**
	 * <获取虚拟机远程连接>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String getVNCUrl(String vmId) throws HsCloudException;

	/**
	 * <备份虚拟机>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String backupVmByVmId(String vmId,
			String sn_name, String sn_comments, int sn_type,Object o,String otype) throws HsCloudException;

	/**
	 * <获取所有虚拟机备份历史记录>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<VmSnapShotVO> findAllBackupsByVmId(String vmId) throws HsCloudException;
	/**
	 * <根据虚拟机id和备份的id还原虚拟机>
	 * 
	 * @param vmId
	 * @param backupId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String renewVM(String vmId, String backupId,Object o,String otype) throws HsCloudException;

	/**
	 * <删除虚拟机>
	 * 
	 * @param vmId
	 * @see [类、类#方法、类#成员]
	 */
	public boolean terminateVmByVmId(String vmId,String vmName,String terminateFlag,long adminId) throws HsCloudException;

	/**
	 * <发布虚拟机>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String publishVm(String vmId,long adminId) throws HsCloudException;
	/**
	 * <冻结虚拟机> 
	* <功能详细描述> 
	* @param vmId
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public boolean freezeVM(String vmId,long uid);
	/**
	 * <启用虚拟机> 
	* <功能详细描述> 
	* @param vmId
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public boolean activeVM(String vmId,long uid);
	/**
	 * <修改虚拟机名称>
	 * 
	 * @param vmName
	 * @param vmId
	 * @param uid
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean updateVmName(String vmName, String vmId,long uid) throws HsCloudException;
	/**
	 * <修改虚拟机备注>
	 * 
	 * @param Comments
	 * @param vmId
	 * @param uid
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean updateComments(String Comments,String outComments, String vmId,long uid) throws HsCloudException;

	/**
	 * <查看虚拟机详情>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public InstanceDetailVo findDetailVmById(String vmId) throws HsCloudException;

	/**
	 * <查看试用待审核虚拟机详情>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public InstanceDetailVo getTryVmInfo(long referenceId) throws HsCloudException;

	/**
	 * <自动迁移虚拟机> <功能详细描述>
	 * 
	 * @param uuid
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean autoMigrateVm(String vmId) throws HsCloudException;

	/**
	 * <调整虚拟机flavor> <功能详细描述>
	 * 
	 * @param uuid
	 * @param flavorVO
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean resizeVm(String vmId, FlavorVO flavorVO,String addDisk,AbstractUser a) throws HsCloudException;
	/**
	 * <调整虚拟机确认> 
	* <功能详细描述> 
	* @param vmId
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public boolean confirmResizeVm(String vmId,boolean confirmFlag) throws HsCloudException;
	/**
	 * <迁移虚拟机确认> 
	* <功能详细描述> 
	* @param vmId
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public boolean confirmMigrateVm(String vmId,boolean confirmFlag) throws HsCloudException;
	/**
	 * <更新虚拟机的节点名称> <功能详细描述>
	 * 
	 * @param uuid
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean updateHostNameOfVm(String vmId) throws HsCloudException;
	/**
	 * <根据虚拟机名称查询是否有同名虚拟机> 
	* <功能详细描述> 
	* @param vmName
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public boolean hasSameVmName(String vmName) throws HsCloudException;
	/**
	 * <修改虚拟机绑定的IP> 
	* <功能详细描述> 
	* @param uuid
	* @param oldIP
	* @param newIP
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean updateIPOfVm(String uuid, String oldIP, String newIP,long adminId) throws HsCloudException;
	/**
	 * <获取虚拟机下所有的扩展盘> 
	* <功能详细描述> 
	* @param vmId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<VolumeVO> getAttachVolumesOfVm(String vmId) throws HsCloudException;
	/**
	 * <删除虚拟机的扩展盘> 
	* <功能详细描述> 
	* @param vmId
	* @param volumId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean dettachVolume(String vmId,int volumId,int volumeMode,Admin a)throws HsCloudException;
	/**
	 * <调整虚拟机的所有者> 
	* <功能详细描述> 
	* @param vmOwner
	* @param vmId
	* @param uid
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean updateVmOwmer(long vmOwner, String vmId,long uid)
			throws HsCloudException;
	/**
	 * <添加虚拟机的扩展盘> 
	* <功能详细描述> 
	* @param vmId
	* @param addDisk
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean attachVolume(String vmId,String addDisk,AbstractUser a)throws HsCloudException;
	/**
	 * <手动迁移虚拟机> 
	* <功能详细描述> 
	* @param vmId
	* @param hostName
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean manualMigrateVm(String vmId,String hostName,Admin admin) throws HsCloudException;
	
	/**
	 * <试用VM审核> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public String verifForTryVm(long referenceId,Long adminId,String zoneCode,String remark) throws HsCloudException;
	
	/**
	 * <试用VM延期审核> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public String verifyForDelayTryVm(long referenceId,int delayLong,Long adminId) throws HsCloudException;
	
	/**
	 * <额外延期试用VM> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public String extraDelayTryVm(long referenceId,int delayLong,Long adminId) throws HsCloudException;

	/**
	 * <后台管理员为正式虚拟机延期> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String delayRegularVm(long referenceId,int delayLong,Long adminId) throws HsCloudException;
	
	/**
	 * <额外延期试用VM> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public String cancelApplyTryVm(long referenceId,String comments,Long adminId) throws HsCloudException;
	
	/**
	 * <业务获取虚拟机列表> 
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
	public Page<VmInfoVO> listVMForBusiness(Page<VmInfoVO> page,
			String field, String fieldValue, Admin admin,
			List<Object> referenceIds,String type,String status_buss,String sort) throws HsCloudException;
	/**
	 * <回收站获取虚拟机列表> 
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
	public Page<VmInfoVO> listVMForRecycle(Page<VmInfoVO> page,
			String field, String fieldValue, Admin admin,
			List<Object> referenceIds,String type,String status_buss,String sort) throws HsCloudException;
	/**
	 * <修改虚拟机的扩展盘大小> 
	* <功能详细描述> 
	* @param vmId
	* @param volumId
	* @param volumeSize
	* @param a
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean modifyVolume(String vmId,int volumId,int volumeSize,Admin a)throws HsCloudException;

	/******************************* Monitor Module ********************************/
	/**
	 * <获取全局概述数据集> <功能详细描述>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public WholeOverviewInfoVO getWholeOverviewInfo(List<Object> referenceIds) throws HsCloudException;

	/**
	 * <获取资源域概述数据集> 
	* <功能详细描述> 
	* @param referenceIds
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public ZoneOverviewInfoVO getZoneOverviewInfo(String zoneCode, List<Object> referenceIds) throws HsCloudException;
	/**
	 * <获取节点概述数据集> <功能详细描述>
	 * 
	 * @param hostName
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public HostOverviewInfoVO getHostOverviewInfo(String hostName) throws HsCloudException;

	/**
	 * <获取虚拟机概述数据集> <功能详细描述>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public VmOverviewInfoVO getVmOverviewInfo(String vmId) throws HsCloudException;

	/**
	 * <获取节点列表数据集> <功能详细描述>
	 * 
	 * @param page
	 * @param field
	 * @param fieldValue
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public Page<HostInfoVO> findHostDetailsByCondition(Page<HostInfoVO> page,
			String field, String fieldValue,String zoneCode,List<Object> zoneIds) throws HsCloudException;

	/**
	 * <后台管理员获取虚拟机列表数据集> <功能详细描述>
	 * 
	 * @param page
	 * @param hostName
	 * @param field
	 * @param fieldValue
	 * @param admin
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public Page<VmInfoMonitorVO> findVmDetailsByCondition(Page<VmInfoVO> page,
			String hostName, String field, String fieldValue, Admin admin,
			List<Object> referenceIds,String zoneCode) throws HsCloudException;
	/**
	 * <获取虚拟机关联信息> <功能详细描述>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public VmDetailInfoVO getVmDetailInfo(String vmId) throws HsCloudException;

	/**
	 * <虚拟机CPU历史监控> <功能详细描述>
	 * 
	 * @param vmId
	 * @param fromTime
	 * @param toTime
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<CPUHistoryVO> getVmCPUHistory(String vmId, long fromTime,
			long toTime) throws HsCloudException;

	/**
	 * <虚拟机Memory历史监控> <功能详细描述>
	 * 
	 * @param vmId
	 * @param fromTime
	 * @param toTime
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<MemoryHistoryVO> getVmMemoryHistory(String vmId, long fromTime,
			long toTime) throws HsCloudException;

	/**
	 * <虚拟机Disk历史监控> <功能详细描述>
	 * 
	 * @param vmId
	 * @param fromTime
	 * @param toTime
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<DiskHistoryVO> getVmDiskHistory(String vmId, long fromTime,
			long toTime) throws HsCloudException;

	/**
	 * <虚拟机Net历史监控> <功能详细描述>
	 * 
	 * @param vmId
	 * @param fromTime
	 * @param toTime
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<NetHistoryVO> getVmNetHistory(String vmId, long fromTime,
			long toTime) throws HsCloudException;

	/**
	 * <节点CPU历史监控> <功能详细描述>
	 * 
	 * @param hostName
	 * @param fromTime
	 * @param toTime
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<CPUHistoryVO> getHostCPUHistory(String hostName, long fromTime,
			long toTime) throws HsCloudException;

	/**
	 * <节点Memory历史监控> <功能详细描述>
	 * 
	 * @param hostName
	 * @param fromTime
	 * @param toTime
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<MemoryHistoryVO> getHostMemoryHistory(String hostName,
			long fromTime, long toTime) throws HsCloudException;

	/**
	 * <节点Disk历史监控> <功能详细描述>
	 * 
	 * @param hostName
	 * @param fromTime
	 * @param toTime
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<DiskHistoryVO> getHostDiskHistory(String hostName,
			long fromTime, long toTime) throws HsCloudException;

	/**
	 * <节点Net历史监控> <功能详细描述>
	 * 
	 * @param hostName
	 * @param fromTime
	 * @param toTime
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<NetHistoryVO> getHostNetHistory(String hostName, long fromTime,
			long toTime) throws HsCloudException;

	/**
	 * <虚拟机实时监控> <功能详细描述>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<VmRealtimeMonitorVO> getVmRealTimeMonitor(String vmId) throws HsCloudException;
	/**
	 * <虚拟资源树> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String getNodeAndVmTree(String nodeName,List<Object> referenceIds) throws HsCloudException;
	/**
	 * <获得各节点资源使用率> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public List<HostInfoVO> getAllHostUsage(String zoneCode) throws HsCloudException;

	/**
	 * <获取所有角色信息> <功能详细描述>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<Role> getAllRole();

	/******************************* SC Module ********************************/
	/**
	 * <保存节点> <功能详细描述>
	 * 
	 * @param node
	 * @see [类、类#方法、类#成员]
	 */
	public void save(ServerNode node) throws HsCloudException;

	/**
	 * <查询所有节点> <功能详细描述>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<ServerNode> getAllNodes() throws HsCloudException;

	/**
	 * <根据节点Id查询节点> <功能详细描述>
	 * 
	 * @param nodeId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public ServerNode getNodeById(int nodeId) throws HsCloudException;

	/**
	 * <根据节点名称查询节点> <功能详细描述>
	 * 
	 * @param name
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public ServerNode getNodeByName(String name) throws HsCloudException;

	/**
	 * <删除节点> <功能详细描述>
	 * 
	 * @param nodeId
	 * @see [类、类#方法、类#成员]
	 */
	public void delete(long nodeId) throws HsCloudException;
	/**
	 * <判断是否有重名的节点> 
	* <功能详细描述> 
	* @param serverNode
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean hasSameNodeName(ServerNode serverNode) throws HsCloudException;
	/**
	 * <发现新节点> <功能详细描述>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<ServerNode> getNewNodes() throws HsCloudException;
	/**
	 * <查询节点树> 
	* <功能详细描述> 
	* @param nodeIds
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public String getNodeTree(List<Object> nodeIds,String zoneCode) throws HsCloudException;
	/**
	 * <根据节点Zone查询节点列表> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Map<String,List<ServerNode>> getAllNodesGroupByZone() throws HsCloudException;
	/**
	 * <根据Zone查询节点列表> 
	* <功能详细描述> 
	* @param zoneId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<ServerNode> getAllNodesByServerZone(long zoneId) throws HsCloudException;
	/**
	 * <同步所有节点的资源隔离配置信息> 
	* <功能详细描述> 
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void synchronizationAllNodeIsolation() throws HsCloudException;
	/**
	 * <保存Zone> 
	* <功能详细描述> 
	* @param zone
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public long createServerZone(ServerZone zone) throws HsCloudException;
	/**
	 * <删除Zone> 
	* <功能详细描述> 
	* @param id
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean deleteServerZone(long id) throws HsCloudException;
	/**
	 * <分页查询Zone> 
	* <功能详细描述> 
	* @param page
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<ServerZone> findServerZone(Page<ServerZone> page,String field, String fieldValue) throws HsCloudException;
	/**
	 * <查询所有的Zone> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<ServerZone> getAllZones(List<Object> zoneIds) throws HsCloudException;
	/**
	 * <修改Zone> 
	* <功能详细描述> 
	* @param serverZone
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean updateServerZone(ServerZone serverZone) throws HsCloudException;
	/**
	 * <设置默认Zone> 
	* <功能详细描述> 
	* @param serverZone
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean setDefaultServerZone(ServerZone serverZone) throws HsCloudException;
	/**
	 * <根据ID查询Zone> 
	* <功能详细描述> 
	* @param id
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public ServerZone getServerZoneById(long id) throws HsCloudException;
	/**
	 * <判断是否有重名的Zone> 
	* <功能详细描述> 
	* @param serverZone
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean hasSameZoneName(ServerZone serverZone) throws HsCloudException;
	/**
	 * <判断是否有重复区域编码的Zone> 
	* <功能详细描述> 
	* @param serverZone
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean hasSameZoneCode(ServerZone serverZone) throws HsCloudException;
	/**
	 * <根据区域编码查询Zone> 
	* <功能详细描述> 
	* @param code
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<ServerZone> getServerZonesByCode(String code)  throws HsCloudException;
	/**
	 * <查询资源域树> 
	* <功能详细描述> 
	* @param zoneIds
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public String getZoneTree(List<Object> zoneIds) throws HsCloudException;
	/**
	 * <根据zoneGroupID查询Zone> 
	* <功能详细描述> 
	* @param zoneGroupId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<ServerZone> getAllZonesByGroupId(long zoneGroupId) throws HsCloudException;
	/**
	 * <查询已经关联的资源域> 
	* <功能详细描述> 
	* @param page
	* @param zoneGroupId
	* @param zoneName
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<ZoneVO> getRelatedServerZone(Page<ZoneVO> page,long zoneGroupId, String zoneName) throws HsCloudException;
	/**
	 * <查询未关联的资源域> 
	* <功能详细描述> 
	* @param page
	* @param zoneGroupId
	* @param zoneName
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<ZoneVO> getUnRelatedServerZone(Page<ZoneVO> page,long zoneGroupId, String zoneName) throws HsCloudException;
	/**
	 * <关联资源域与组> 
	* <功能详细描述> 
	* @param scIds
	* @param brandId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean associateZoneAndZoneGroup(Long[] zoneIds, long groupId) throws HsCloudException;
	/**
	 * <解除资源域与组的关联> 
	* <功能详细描述> 
	* @param scIds
	* @param brandId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean disAssociateZoneAndZoneGroup(Long[] zoneIds, long groupId) throws HsCloudException;
	/**
	 * <保存ZoneGroup> 
	* <功能详细描述> 
	* @param zoneGroup
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public long createZoneGroup(ZoneGroup zoneGroup,Long[] domainIds) throws HsCloudException;
	/**
	 * <删除ZoneGroup> 
	* <功能详细描述> 
	* @param id
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean deleteZoneGroup(long id) throws HsCloudException;
	/**
	 * <分页查询ZoneGroup> 
	* <功能详细描述> 
	* @param page
	* @param field
	* @param fieldValue
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<ZoneGroup> findZoneGroup(Page<ZoneGroup> page,String field, String fieldValue) throws HsCloudException;
	/**
	 * <查询所有的ZoneGroup> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<ZoneGroup> getAllZoneGroups() throws HsCloudException;
	/**
	 * <修改ZoneGroup> 
	* <功能详细描述> 
	* @param zoneGroup
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean updateZoneGroup(ZoneGroup zoneGroup,Long[] domainIds) throws HsCloudException;
	/**
	 * <根据ID查询ZoneGroup> 
	* <功能详细描述> 
	* @param id
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public ZoneGroup getZoneGroupById(long id) throws HsCloudException;
	/**
	 * <判断是否有重名的ZoneGroup> 
	* <功能详细描述> 
	* @param zoneGroup
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean hasSameZoneGroupName(ZoneGroup zoneGroup) throws HsCloudException;
	/**
	 * <判断是否有重复区域组编码的ZoneGroup> 
	* <功能详细描述> 
	* @param zoneGroup
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean hasSameZoneGroupCode(ZoneGroup zoneGroup) throws HsCloudException;
	/**
	 * <根据资源域Id查询资源域组Id列表> 
	* <功能详细描述> 
	* @param zoneIds
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<Long> getAllZoneGroupIdsByZoneIds(List<Long> zoneIds) throws HsCloudException;
	/**
	 * <添加管理员> <功能详细描述>
	 * 
	 * @param password
	 * @param telephone
	 * @param name
	 * @param email
	 * @param roleId
	 * @see [类、类#方法、类#成员]
	 */
	public void addAdmin(String password, String telephone, String name,
			String email, Long roleId,Integer adminType);
	/**
	 * <修改管理员> 
	* <功能详细描述> 
	* @param password
	* @param telephone
	* @param name
	* @param email
	* @param roleId 
	* @see [类、类#方法、类#成员]
	 */
	public void modifyAdmin(String telephone,long adminId, Long roleId,Long adminRoleId,Integer adminType);
	/**
	 * <删除admin User 逻辑删除> 
	* <功能详细描述> 
	* @param adminId 
	* @see [类、类#方法、类#成员]
	 */
	public void deleteAdmin(long adminId);
	
	public void freezedAdmin(long adminId);
	/**
	 * <重置管理员密码> 
	* <功能详细描述> 
	* @param adminId
	* @param password 
	* @see [类、类#方法、类#成员]
	 */
	public void resetPasswd(long adminId,String password);
   

  //  public void modifyRole(Role role, String privilegesStr);


    /**
     * 根据roleId获取权限树 <功能详细描述>
     * 
     * @param roleId
     * @return
     * @see [类、类#方法、类#成员]
     */
  //  public List<com.hisoft.hscloud.crm.usermanager.vo.TreeNode> getPermissionTree(long adminId, long roleId);

    public String createRole(Role role, String privilegesStr);

 //   public List<TreeNode> getPermissionTreeByRoleId(long roleId);

    /**
     * <修改用户profile信息> <功能详细描述>
     * 
     * @param userProfile
     * @see [类、类#方法、类#成员]
     */
    public void modifyUserProfile(UserProfileVo userProfileVo);

  //  public List<Resource> getRolePermissionResource(long adminId);

    /**
     * <一句话功能简述> <功能详细描述>
     * 
     * @param roleId
     * @return
     * @see [类、类#方法、类#成员]
     */
  //  public List<Resource> getResourceForRoleId(long roleId);
    public Admin getAdminByEmail(String email);

    /** <一句话功能简述> 
    * <功能详细描述> 
    * @param roleId
    * @return 
    * @see [类、类#方法、类#成员] 
    */
  //  public List<com.hisoft.hscloud.crm.usermanager.vo.TreeNode> getPermissionTree(long roleId);
    
    /**
     * ip列表查询
    * lihonglei
    * @param page
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public Page<IPRangeVO> findIPRanges(Page<IPRangeVO> page,String field,String fieldValue);
    
    /**
     * 添加ip前查询
    *  lihonglei
    * @param startIP
    * @param endIP
     * @param string 
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public List<IPDetail> findIPDetailByIP(long startIP, long endIP, String gateway);
    
    /**
     * ip添加
    *  lihonglei 
    * @param startIP
    * @param endIP
    * @param createUid
    * @param remark
    * @return
    * @throws HsCloudException
    * @throws Exception 
    * @see [类、类#方法、类#成员]
     */
    public String createIP(IPRangeVO ipRangeVO) throws HsCloudException, Exception;
    
    /**
     * 查找符合状态的ip
    * lihonglei
    * @param ipRangeId
    * @param status
    * @return
    * @throws HsCloudException 
    * @see [类、类#方法、类#成员]
     */
    public List<IPDetail> findIPDetailByStatus(long ipRangeId,int status) throws HsCloudException;
    
    /**
     * 删除ip地址
    * lihonglei
    * @param id
    * @return
    * @throws HsCloudException 
    * @see [类、类#方法、类#成员]
     */
    public boolean deleteIP(long id) throws HsCloudException;
    
    /**
     * ip状态修改 
    *  lihonglei
    * @param id
    * @param status
    * @param userId
    * @return
    * @throws HsCloudException 
    * @see [类、类#方法、类#成员]
     */
    public boolean updateIPDetail(long id,int status, long userId,String remark) throws HsCloudException;
    /**
     * 查询所有的空闲IP 
    * lihonglei 
    * @param status
    * @return
    * @throws HsCloudException 
    * @see [类、类#方法、类#成员]
     */
    public List<IPDetailInfoVO> findAvailableIPDetailOfServerZone(ServerZone serverZone) throws HsCloudException;
    
    /**
     * 分页查询属于某一网段的IP 
    * <功能详细描述> 
    * @param page
    * @param ipRangeId
    * @param ip
    * @return
    * @throws HsCloudException 
    * @see [类、类#方法、类#成员]
     */
    public Page<IPDetailVO> findIPDetailsByCondition(Page<IPDetailVO> page,
			long ipRangeId, String field, String fieldValue) throws HsCloudException; 
    /**
     * <判断IP是否已经绑定了Zone> 
    * <功能详细描述> 
    * @param zoneId
    * @return
    * @throws HsCloudException 
    * @see [类、类#方法、类#成员]
     */
    public boolean isServerZoneBindIP(long zoneId) throws HsCloudException;
    
    /**
     * 创建WanNetwork
     * @return
     * @throws HsCloudException
     */
    public boolean createWanNetwork(NetWorkBean netWork,Long adminId) throws HsCloudException;
    /******************************* systemmanagement Module ********************************/
    /**
     * <分页查询所有的进程> 
    * <功能详细描述> 
    * @param page
    * @return
    * @throws HsCloudException 
    * @see [类、类#方法、类#成员]
     */
    public Page<ProcessResourceVO> findAllProcess(Page<ProcessResourceVO> page) throws HsCloudException;
    /**
     * <启动一线程> 
    * <功能详细描述> 
    * @param threadKey
    * @return
    * @throws HsCloudException 
    * @see [类、类#方法、类#成员]
     */
    public boolean startProcess(String threadKey) throws HsCloudException;
    /**
     * <停止一线程> 
    * <功能详细描述> 
    * @param threadKey
    * @return
    * @throws HsCloudException 
    * @see [类、类#方法、类#成员]
     */
    public boolean stopProcess(String threadKey) throws HsCloudException;
    
    /**
     * 显示菜单 
    * 根据父节点查询字节点菜单 
    * @param parentId
    * @param referenceIds
    * @return 
    * @see [类、类#方法、类#成员]
     */
	public List<AdminMenuVO> getAdminMenu(String parentId, List<Object> referenceIds);
	
	/*Servicecatalog begin*/
	/**
	 * <根据套餐Id获取套餐> 
	* <功能详细描述> 
	* @param id
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public ServiceCatalog getServiceCatalogById(int id);
	/**
	 * <根据套餐Id获取套餐中的操作系统> 
	* <功能详细描述> 
	* @param id
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public Os getOsByServicecatalogId(int id);
	/**
	 * <保存套餐信息> 
	* <功能详细描述> 
	* @param sc 
	* @see [类、类#方法、类#成员]
	 */
	public void saveSC(ServiceCatalog sc);
	/**
	 * <根据套餐名称获取套餐> 
	* <功能详细描述> 
	* @param name
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public ServiceCatalog getSCByName(String name);
	/**
	 * <分页获取套餐信息> 
	* <功能详细描述> 
	* @param paging
	* @param sc
	* @param sortList
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public Page<ServiceCatalog> getSCByPage(Page<ServiceCatalog> paging,ServiceCatalog sc,List<Sort> sortList,Long brandId,Long ZoneId,Long domainId);
	/**
	 * <根据Id禁用套餐> 
	* <功能详细描述> 
	* @param id 
	* @see [类、类#方法、类#成员]
	 */
	public void disableSC(int id);
	/**
	 * <根据Id启用套餐> 
	* <功能详细描述> 
	* @param id 
	* @see [类、类#方法、类#成员]
	 */
	public void enableSC(int id);
	/**
	 * <根据Id启用仅试用套餐> 
	 * <功能详细描述> 
	 * @param id 
	 * @see [类、类#方法、类#成员]
	 */
	public void onlyTrySC(int id);
	/**
	 * <根据Id审批套餐> 
	* <功能详细描述> 
	* @param id 
	* @see [类、类#方法、类#成员]
	 */
	public void approveSC(int id);
	/*Servicecatalog end*/
	/**
	 * <根据资源类型获得资源列表> 
	* <功能详细描述> 
	* @param serviceType
	* @param sortList
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public List<ServiceItem> listServiceItem(int serviceType,List<Sort> sortList);
	
	/**
	 * 通过adminId查询角色 
	* <功能详细描述> 
	* @param adminId
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public Role getRoleByAdminId(long adminId);
	/**
	 * <根据权限获取 role> 
	* <功能详细描述> 
	* @param roleIds
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public List<Role> getRoleByPermission(List<Object> roleIds);
	/**
	 * <检查硬盘容量是否大于镜像文件大小> 
	* <功能详细描述> 
	* @param sc
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public boolean checkImageSizeVSDiskCapacity(ServiceCatalog sc)throws HsCloudException;
	/**
	 * <分页获取套餐资源> 
	* <功能详细描述> 
	* @param paging
	* @param serviceType
	* @param sortList
	* @param query
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public Page<ServiceItem> pageServiceItem(Page<ServiceItem> paging,int serviceType,List<Sort> sortList,String query);
	
	/**
	 * <根据类型获取套餐资源> 
	* <功能详细描述> 
	* @param type
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public List<ServiceItem> getServiceItemByType(int type);
	/**
	 * <保存套餐资源> 
	* <功能详细描述> 
	* @param si 
	* @see [类、类#方法、类#成员]
	 */
	public int saveServiceItem(ServiceItem si);
	/**
	 * <修改时使用，用于保留一些属性不被修改> 
	* <功能详细描述> 
	* @param siVo 
	* @see [类、类#方法、类#成员]
	 */
	public void saveServiceItemVo(ServiceItemVo siVo)throws HsCloudException;
	/**
	 * <获取套餐资源类型的对应关系> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public Map<Integer, ServiceType> getServiceType();
	/**
	 * <根据物理Id删除套餐资源> 
	* <功能详细描述> 
	* @param siId 
	* @see [类、类#方法、类#成员]
	 */
	public boolean deleteServiceItem(int siId);
	/**
	 * <检查套餐资源是否重名> 
	* <功能详细描述> 
	* @param serviceType
	* @param name
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public boolean checkServiceItemNameRepeat(int serviceType,String name);
	/**
	 * <启用管理员> 
	* <功能详细描述> 
	* @param adminId 
	* @see [类、类#方法、类#成员]
	 */
	public void enableAdmin(long adminId);
	/**
	 * <启用用户> 
	* <功能详细描述> 
	* @param userId 
	* @see [类、类#方法、类#成员]
	 */
	public void enableUser(long userId);
	/**
	 * <分页获取订单> 
	* <功能详细描述> 
	* @param page
	* @param order
	* @param user
	* @param sorts
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public Page<Order> getOrderByPage(Page<Order> page, Order order,
			List<Sort> sorts,String query,Admin admin,Long domainId);
	/**
	 * <根据订单号获取订单项> 
	* <功能详细描述> 
	* @param id
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public List<OrderItemVo> getAllOrderItemsByOrder(Long id);
	
	/**
	 * <通过查询hc_order表的orderNo获得Order> 
	* <功能详细描述> 
	* @param orderNo
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public Order getOrderByOrderNo(String orderNo);
	
	/**
	 * <根据uuid来获取机器号为uuid的这台虚拟机的退款申请详情> 
	* <功能详细描述> 
	* @param uuid
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public VmRefundLog getVmRefundLogByUuid(String uuid);

	/**
	 * <根据订单号取消订单> 
	* <功能详细描述> 
	* @param orderId 
	* @see [类、类#方法、类#成员]
	 */
	public boolean cancelOrder(Long orderId)throws HsCloudException;
	
	public boolean checkPermission(Long adminId);
	
	/**
	 * 获取ftp路径下文件 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public List<FileVO> getFileList();
	
	/**
	 * 删除ftp上文件 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public boolean deleteFile(String fileName);
	
	/**
	 * 添加image文件 
	* <功能详细描述> 
	* @param image 
	* @see [类、类#方法、类#成员]
	 */
	public void addUploadInfo(ImageVO image, String fileName);
	
	/**
	 * 展示image列表 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public List<ImageVO> showImageList(String query);
	/**
	 * 删除image 
	* <功能详细描述> 
	* @param imageId 
	* @see [类、类#方法、类#成员]
	 */
	public void deleteImage(String imageId);
	/**
	 * 修改image
	 * @param image
	 */
	public void editImage(ImageVO image);

	/**
	 * <延期审核> 
	* <功能详细描述> 
	* @param orderId
	* @param delayDays
	* @param orderStatus
	* @param orderItemId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean delayAudit(long orderId,int delayDays, String orderStatus,Admin admin) throws HsCloudException;

	public Account getAccountByUserId(long id);

	
	/**
	 * 
	 * @param user 操作者
	 * @param accountId
	 * @param depositSource 充值来源
	 * @param balance
	 * @param remark 备注
	 * @param flow   转入/转出
	 */
	public void accountDeposit(long userId,short paymentType,long accountUserId,long accountId,BigDecimal balance,String remark,Short depositSource,Short flow);
	
	public void giftsDeposit(long userId, short paymentType, long accountUserId, long accountId,BigDecimal giftsBalance, String remark,Short depositSource);
	
	/**
	 * 
	 * @param userId
	 * @param depositSource 充值来源
	 * @param paymentType
	 * @param accountUserId
	 * @param accountId
	 * @param coupons
	 * @param remark
	 */
	public void couponsDeposit(long userId,short paymentType,long accountUserId,long accountId,BigDecimal coupons, String remark,Short depositSource);
	
	/**
	 * 
	 * @param user  操作者
	 * @param accountId
	 * @param balance
	 * @param remark 备注
	 * @param flow   转入/转出
	 * @return  
	 */
	public void accountDraw(long userId,short paymentType,long accountUserId, long accountId,BigDecimal balance,String remark,Short flow);
	
	/**
	 * 
	 * @param userId 操作者
	 * @param paymentType
	 * @param accountUserId
	 * @param accountId
	 * @param coupons
	 * @param remark
	 */
	public void couponsDraw(long userId,short paymentType,long accountUserId, long accountId,BigDecimal coupons,String remark);
	public void giftsDraw(long userId, Short paymentType, long accountUserId, long accountId,BigDecimal balance,String remark);

	
	/**
	 * 查询日志
	 * @param sorts 排序
	 * @param page 分页参数
	 * @param query 查询条件
	 * @param primKeys 权限
	 * @return
	 */
	public Page<TranscationLogVO> pagePermissionTrLog(List<Sort> sorts,Page<TranscationLogVO> page,String query,List<Object> primKeys);
	/**
	 * <多条件查询日志> 
	* <功能详细描述> 
	* 超级管理员和具有和超级管理员相同权限的管理员能导出所有信息
	* 其它管理员按查询添加和所属域导出
	* @param page 查询数据集
	* @param queryVO 查询条件集
	* @param sorts 排序参数集
	* @param primKeys 权限集
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public Page<TranscationLogVO> findTransactionLog(Admin admin,Page<TranscationLogVO> page,QueryVO queryVO,List<Sort> sorts,List<Object> primKeys) throws HsCloudException;
	public Page<VMResponsibility> findVMResponsibility(Admin admin,Page<VMResponsibility> page,QueryVO queryVO,List<Sort> sorts,List<Object> primKeys) throws HsCloudException;
	/**
	 * <根据orderId获取orderItem和vm信息> 
	* <功能详细描述> 
	* @param orderId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<OrderItemVMVo> getOrderItemVMByOrderId(Long orderId)throws HsCloudException;
	/**
	 * <虚拟机退款> 
	* <功能详细描述> 
	* @param referenceId
	* @param admin
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean vmRefund(long referenceId,String vmId,Admin admin,String ip,String refundRemark)throws HsCloudException;
	/**
	 * <虚拟机全额退款> 
	* <功能详细描述> 
	* @param referenceId
	* @param admin
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean vmRefundAll(long referenceId,String vmId,Admin admin,String ip,String refundRemark)throws HsCloudException;
	/**
	 * <通过IP查询IP详细信息> 
	* <功能详细描述> 
	* @param ip
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public IPDetail getIPDetailByIP(long ip) throws HsCloudException;
	/**
	 * <一句话功能简述> 
	* <功能详细描述> 
	* @param ig
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public ServiceItem getSIById(int ig)throws HsCloudException;
	/**
	 * <查询所有有效的用户> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<UserVO> getAllAvailableUser(String email) throws HsCloudException;
	
	/**
	 * 查询后台赋权类型
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public Map<String, ResourceType> getResourceTypeMap();
	
	/**
	 * 查询资源类型list 
	* <功能详细描述> 
	* @param status
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public List<ResourceType> getResourceTypeList();
	/**
	 * 查询actionList 
	* <功能详细描述> 
	* @param actionType
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public List<Action> getActionList(String actionType);
	
	/**
	 * 未分配权限查询
	* <功能详细描述> 
	* @param type 
	 * @param pagePrivilege 
	 * @param query 
	 * @return 
	* @see [类、类#方法、类#成员]
	 */
	public List<PrivilegeVO> findUnassignedList(String type, Long roleId, Page<PrivilegeVO> pagePrivilege, String query);
	/**
	 * 已分配权限查询
	* <功能详细描述> 
	* @param type 
	 * @param query 
	 * @return 
	* @see [类、类#方法、类#成员]
	 */
	public List<PrivilegeVO> findAssignedList(String type, Long roleId, Page<PrivilegeVO> pagePrivilege, String query);
	
	/**
	 * 统一定义查询
	* <功能详细描述> 
	* @param type
	* @param id
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public List<PrivilegeVO> findUiformDefList(String type, Long id);
	
	public void approvedUser(long id);
	
	/**
	 * 审批供应商
	 * @param id  用户id
	 * @param status  是否为供应商   true:是     false:否
	 */
	public void supplieApproved(long pid,boolean status);
	
	public void modifyRolePermissiion(String permissionValue,
			String actionValue, String resourceValue, long roleId);

	public String findMenuStore(long id, Map<String, String> map);

	public void addPrivilege(String privilegesStr, Long id, String noCheckStr);
	/**
	 * <套餐克隆> 
	* <功能详细描述> 
	* @param id
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean copySc(int id,String copySCName) throws HsCloudException;

	/**
	 * 导出消费日志
	 * 超级管理员和具有和超级管理员相同权限的管理员能导出所有信息
	 * 其它管理员按查询添加和所属域导出
	 * @param queryVO 查询条件
	 * @param primKeys 权限
	 * @return
	 */
	public Excel  excelExport(Admin admin,QueryVO queryVO,List<Object> primKeys);
	/**
	 * 导出应用列表
	 * @return
	 */
	public Excel  excelExportApplication(Map<String,Object> queryMap);
	/**
	 * 导出应用评价列表
	 * @return
	 */
	public Excel  excelExportAppEvaluate(EvaluatePageModel evaluatePageModel);
	/**
	 * 导出账单日志列表
	 * @return
	 */
	public Excel  excelExportAppBill(Map<String,Object> queryMap);
	/**
	 * 导出云应用订单信息
	 * @return
	 */
	public Excel  excelExportAppOrder(Map<String,Object> queryMap);
	
	public Excel  excelExportApplicationInvoice(Map<String,Object> queryMap);
	
//	public Excel  responsibilityExcelExport(Admin admin,QueryVO queryVO);
	
	public InputStream responsibilityExcelExport(Admin admin,QueryVO queryVO);
	
	public Excel monthStatisExcelExport(Admin admin,QueryVO queryVO);
	
	/**
	 * <添加品牌信息> 
	* <功能详细描述> 
	* @param code
	* @param description
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void addBrand(String name,String desc,Integer rebateRate,Integer giftsDiscountRate,
			boolean approveOrNot)throws HsCloudException; 
	/**
	 * <修改用户品牌数据> 
	* <功能详细描述> 
	* @param brandId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void  modifyBrand(Long brandId,String name,String desc,short status,Integer rebateRate,
			Integer giftsDiscountRate,boolean approveOrNot)throws HsCloudException;
	/**
	 * <分页获取品牌数据> 
	* <功能详细描述> 
	* @param condition
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<UserBrand> getBrandByPage(String condition,Page<UserBrand> paging) 
			throws HsCloudException;
	/**
	 * <获取所有未删除的品牌> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<UserBrand> getAllNormalBrand()throws HsCloudException;
	/**
	 * <根据物理Id禁用用户品牌数据> 
	* <功能详细描述> 
	* @param id
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void deleteBrandById(Long id)throws HsCloudException;
	/**
	 * <校验品牌名称是否重复> 
	* <功能详细描述> 
	* @param name
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean checkBrandNameDup(String name)throws HsCloudException;
	/**
	 * <> 
	* <功能详细描述> 
	* @param id
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void enableBrandById(Long id)throws HsCloudException;
	
	public void addTransactionLog(long consumeId, short paymentType, long accountUserId,long accountId,
		BigDecimal balance, BigDecimal coupons,BigDecimal gifts,String description,short consumeType);
	/**
	 * 发布公告 
	* <功能详细描述> 
	* @param announcementVO 
	 * @param adminId 
	* @see [类、类#方法、类#成员]
	 */
	public void saveAnnouncement(AnnouncementVO announcementVO, long adminId);
	/**
	 * <查询虚拟机对应的退款订单信息> <功能详细描述>
	 * 
	 * @param referenceId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public List<VmRefundOrderItemVo> getVmRefundOrderItemVo(long referenceId)
			throws HsCloudException;
	/**
	 * 查询发票记录
	* <功能详细描述> 
	* @param page
	* @param condition
	* @return 
	* @see [类、类#方法、类#成员]
	 */
    public Page<Map<String, Object>> findInvoiceRecordList(Page<Map<String, Object>> page,
            Map<String, Object> condition, Admin admin);
   /**
    * <获取vm关联的已支付的订单信息> 
   * <功能详细描述> 
   * @param referenceId
   * @return
   * @throws HsCloudException 
   * @see [类、类#方法、类#成员]
    */
   public List<OrderVo> getVmRelatedPaidOrder(long referenceId)throws HsCloudException;
    /**
     * 发票审批未通过 
    * <功能详细描述> 
    * @param id
    * @param innerDescription 
    * @see [类、类#方法、类#成员]
     */
    public void approvalInvoiceFail(long id, String innerDescription);
    
    /**
     * 发票审批通过 
    * <功能详细描述> 
    * @param id
    * @param innerDescription
    * @param invoiceNo
    * @param sendTime 
     * @return 
    * @see [类、类#方法、类#成员]
     */
    public String approvalInvoiceSuccess(long id, String innerDescription,
            String invoiceNo, Date sendTime);
    /**
     * 加载省份列表 
    * <功能详细描述> 
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public List<Province> loadProvince();
    
    /**
     * 公告页查询
    * <功能详细描述> 
    * @param announcementPage
    * @param query
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public Page<Map<String, Object>> findAnnouncementPage(Page<Map<String, Object>> announcementPage, String query);
    
    /**
     * 删除公告 
    * <功能详细描述> 
    * @param id 
    * @see [类、类#方法、类#成员]
     */
    public void delAnnouncement(long announcementId);
    
    
    /**
     * 
     * @param userId
     * @param money
     * @return
     */
    public BigDecimal checkAccountForRefund(long userId, BigDecimal money);
    
    /**
     * <查询分平台页面(用于权限分配)> 
    * <功能详细描述> 
    * @param page
    * @param roleId
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public Page<Map<String, Object>> findDomainList(Page<Map<String, Object>> page,
            long roleId);
    
    /**
     * <查询分平台页面> 
    * <功能详细描述> 
    * @param page
    * @param query
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public Page<Domain> findDomainPage(Page<Domain> page, String query);
    /**
     * <分平台赋权> 
    * <功能详细描述> 
    * @param permissionValue
    * @param resourceValue
    * @param id 
    * @see [类、类#方法、类#成员]
     */
    public void editDomainPermission(String permissionValue, String resourceValue, long id);
    
	 /**
     * <获取所有分平台> 
    * <功能详细描述> 
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public List<Domain> getAllDomain();
	
    /**
	 * <根据查询条件获取所有支付接口信息> 
	* <功能详细描述> 
	* @param query
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<PaymentInterface> getAllPaymentInterfaceByPage(String query,Page<PaymentInterface> paging,Admin admin)throws HsCloudException;
	/**
	 * <修改支付接口信息> 
	* <功能详细描述> 
	* @param config
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void modifyPaymentInterface(PaymentInterfaceVO config)throws HsCloudException;
	/**
	 * <新增支付接口信息> 
	* <功能详细描述> 
	* @param config
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void createPaymentInterface(PaymentInterface config)throws HsCloudException;
	/**
	 * <停用支付接口信息> 
	* <功能详细描述> 
	* @param config
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void disablePaymentInterface(long id)throws HsCloudException;
	/**
	 * <启用支付接口信息> 
	* <功能详细描述> 
	* @param config
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void enablePaymentInterface(long id)throws HsCloudException;
	/**
	 * <根据分平台id查询支付接口用于校验分平台是否重复> 
	* <功能详细描述> 
	* @param domainId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	
	public boolean getPaymentInterfaceByDomain(Domain domain)throws HsCloudException;
	
	/**
	 * <重发激活邮件> 
	* <功能详细描述> 
	* @param userId 
	* @see [类、类#方法、类#成员]
	 */
    public void reActivation(long userId);
    
	/**
     * <编辑分平台> 
    * <功能详细描述> 
    * @param domain
    * @param adminId 
	 * @return 
    * @see [类、类#方法、类#成员]
     */
    public String editDomain(DomainVO domainVO, long adminId) throws HsCloudException;
    
	/**
	 * <修改分平台状态> 
	* <功能详细描述> 
	* @param domainId 
	* @see [类、类#方法、类#成员]
	 */
    public void updateStatusDomain(long domainId, long adminId, String status);
    
    /**
     * 超级管理员 和超级管理有 有相同权限的管理 能查询所有域
     * 其它管理员根据自己所属域查询出相应域
     * @param isSuper
     * @param adminId
     * @return
     */
    public List<Domain> loadDomain(boolean isSuper,long adminId);

    /**
     * 分页查询前台账号（超级管理员和超级管理员同权限的管理员查询所有账户，普通管理员查询管理员域下的所有用户）
     * @param admin 登录管理员
     * @param sorts 排序
     * @param page  分页信息
     * @param type 查询类型
     * @param query 查询条件
     * @return
     */
	public Page<Account> getAccountByPage(Admin admin, List<Sort> sorts,
			Page<Account> page, String type ,String query);
	/**
	 * <重发vm开通邮件> 
	* <功能详细描述> 
	* @param VmName
	* @param ip
	* @param email 
	* @see [类、类#方法、类#成员]
	 */
	public void reSendOpenEmail(String VmName,String ip,String email,String vmuuid)throws HsCloudException;
	  /**
	   * <获取与brand关联的所有套餐> 
	  * <功能详细描述> 
	  * @param brandId
	  * @return
	  * @throws HsCloudException 
	  * @see [类、类#方法、类#成员]
	   */
	  public Page<SCVo> getRelatedSCByBrandId(long brandId,Page<SCVo> paging,String query)throws HsCloudException;
	  /**
	   * <获取与brand没有关联的所有套餐> 
	  * <功能详细描述> 
	  * @param brandId
	  * @return
	  * @throws HsCloudException 
	  * @see [类、类#方法、类#成员]
	   */
	  public Page<SCVo> getUnRelatedScByBrandId(long brandId,Page<SCVo> paging,String query)throws HsCloudException;
	  /**
	   * <关联品牌与套餐> 
	  * <功能详细描述> 
	  * @param scIds
	  * @param brandId
	  * @throws HsCloudException 
	  * @see [类、类#方法、类#成员]
	   */
	  public void brandRelateSCOperation(Integer[] scIds,long brandId)throws HsCloudException;
	  /**
	   * <解除品牌与套餐的关联> 
	  * <功能详细描述> 
	  * @param scIds
	  * @param brandId
	  * @throws HsCloudException 
	  * @see [类、类#方法、类#成员]
	   */
	  public void brandUnRelateSCOperation(Integer[] scIds,long brandId)throws HsCloudException;

	/**
	 * <查询关于资源的操作日志> <功能详细描述>
	 * 
	 * @param param
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public Page<HcEventResource> getResourceLog(Page<HcEventResource> paing,LogQueryVO param)
			throws HsCloudException;
	/**
	 * <发票导出> 
	* <功能详细描述> 
	* @param admin
	* @param queryVO
	* @param primKeys
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public ExcelExport invoiceExcelExport(Map<String, Object> condition, Admin admin);
	
	/**
     * <发票修改> 
    * <功能详细描述> 
    * @param invoiceVO
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public String modifyInvoice(InvoiceVO invoiceVO);
	/**
	 * <查询关于虚拟机操作的日志> 
	* <功能详细描述> 
	* @param param
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<HcEventVmOps> getVmOpsLog(Page<HcEventVmOps> paing,LogQueryVO param)throws HsCloudException;
	
	/**
	 * <重置虚拟机密码> 
	* <功能详细描述> 
	* @param vmId
	* @param password 
	* @see [类、类#方法、类#成员]
	 */
    public void resetSystemPwd(String vmId, String password,Object o,String otype);
    /**
     * <重新发送资源job消息> 
    * <功能详细描述> 
    * @param jobId 
    * @see [类、类#方法、类#成员]
     */
    public void resendResourceJobMessage(Long jobId)throws HsCloudException;
    /**
     * <重新发送ops job消息> 
    * <功能详细描述> 
    * @param jobId 
    * @see [类、类#方法、类#成员]
     */
    public void resendOpsJobMessage(Long jobId)throws HsCloudException;
    /**
     * <重新发送messager消息> 
    * <功能详细描述> 
    * @param jobId 
    * @see [类、类#方法、类#成员]
     */
    public void resendMessagerMessage(Long jobId)throws HsCloudException;
    /**
	 * <分页获取退款日志信息> 
	* <功能详细描述> 
	* @param status
	* @param query
	* @param paging
	* @return 
	* @see [类、类#方法、类#成员]
	 */
    public Page<VmRefundLog> getVmRefundLogByPage(Page<VmRefundLog> paging,String query,short status,Admin admin)throws HsCloudException;
    /**
     * <拒绝退款申请> 
    * <功能详细描述> 
    * @param id 
    * @see [类、类#方法、类#成员]
     */
    public void rejectRefundApply(Long id,String rejectReason,Admin admin);
    /**
	 * <虚拟机退款> 
	* <功能详细描述> 
	* @param referenceId
	* @param admin
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean vmRefundForApply(long referenceId,String vmId,Admin admin,Long refundLogId)throws HsCloudException;
	/**
	 * <虚拟机全额退款> 
	* <功能详细描述> 
	* @param referenceId
	* @param admin
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean vmRefundAllForApply(long referenceId,String vmId,Admin admin,Long refundLogId)throws HsCloudException;
	/**
	 * <查询虚拟机对应的退款订单信息> <功能详细描述>
	 * 
	 * @param referenceId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public List<VmRefundOrderItemVo> getVmRefundOrderItemVoForAplly(long referenceId,Long refundLogId)
			throws HsCloudException;
	
	public Page<MonthIncomingVO> getMonthIncomingMonths(Page<MonthIncomingVO> page,Admin admin,QueryVO queryVO);
	/**
	 * <查询已经关联的品牌> 
	* <功能详细描述> 
	* @param page
	* @param domainId
	* @param domainName
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<UserBrandVO> getRelatedBrand(Page<UserBrandVO> page,long domainId, String domainName) throws HsCloudException;
	/**
	 * <查询未关联的品牌> 
	* <功能详细描述> 
	* @param page
	* @param domainId
	* @param domainName
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<UserBrandVO> getUnRelatedBrand(Page<UserBrandVO> page,long domainId, String domainName) throws HsCloudException;
	/**
	 * <关联分平台与品牌> 
	* <功能详细描述> 
	* @param brandIds
	* @param domainId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean associateBrandAndDomain(Long[] brandIds, long domainId) throws HsCloudException;
	/**
	 * <解除分平台与品牌的关联> 
	* <功能详细描述> 
	* @param brandIds
	* @param domainId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean disAssociateBrandAndDomain(Long[] brandIds, long domainId) throws HsCloudException;
	/**
     * <查询分平台下的品牌> 
    * <功能详细描述> 
    * @param domainId
    * @return
    * @throws HsCloudException 
    * @see [类、类#方法、类#成员]
     */
    public List<UserBrandVO> getRelatedBrandByDomainId(long domainId) throws HsCloudException;

	/**
	 * 分页查询所有业务平台数据
	 * @param paging
	 * @param query
	 * @param admin
	 * @return
	 * @throws HscloudException
	 */
	public Page<BusinessPlatformVO> getBusinessPlatformByPage(
			Page<BusinessPlatformVO> paging, String query, Admin admin)
			throws HsCloudException;
	/**
	 * 分页查询所有控制面板平台数据
	 * @param paging
	 * @param query
	 * @param admin
	 * @return
	 * @throws HsCloudException
	 */
	public Page<ControlPanelVO> getControlPanelVOByPage(
			Page<ControlPanelVO> paging, String query, Admin admin)
			throws HsCloudException;
	/**
	 * <获取业务平台登录掉url> 
	* <功能详细描述> 
	* @param userId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public String getBusinessPlatformLoginUrl(Long userId,Admin admin)throws HsCloudException;
	/**
	 * <获取控制面板登录的url> 
	* <功能详细描述> 
	* @param vmId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public String getControlPanelLoginUrl(String vmId,Admin admin)throws HsCloudException;
	/**
	 * <获取分页显示的市场推广数据> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<MarketingPromotionVO> getMarketingPromotionByPage(String condition,
			Page<MarketingPromotion> paging) throws HsCloudException;
	/**
	 * <启用市场推广> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void enableMarketingPromotionById(Long id) throws HsCloudException;
	/**
	 * <停用市场推广> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void disableMarketingPromotionById(Long id) throws HsCloudException;
	/**
	 * <1.校验推广名称是否重复> <功能详细描述>
	 * <2.校验推广代码是否重复> <功能详细描述>
	 * <3.校验推广地址是否重复> <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	public boolean checkMarketingPromotionParameterDup(String name, String code, 
			String address) throws HsCloudException;
	/**
	 * <添加市场推广> <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	public void addMarketingPromotion(String name, String code, String address,Long domain_id, 
			String brand_code, String description, Admin admin) throws HsCloudException;

	/**
	 * <修改市场推广> <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	public void modifyMarketingPromotion(Long id, String name, String address, Long domain_id, 
			String brand_code, String description, Admin admin) throws HsCloudException;
	/**
     * <查询分平台下的品牌> 
    * <功能详细描述> 
    * @param abbreviation
    * @return
    * @throws HsCloudException 
    * @see [类、类#方法、类#成员]
     */
	public List<UserBrandVO> getRelatedBrand(String abbreviation) throws HsCloudException ;
	/**
	 * <根据用户查询用户及用户品牌信息> 
	* <功能详细描述> 
	* @param userId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public UserUserBrandVO getUserAndBrandByReferenceId(long referenceId)throws HsCloudException;

	/**
	 * <根据referenceId获取vm用户，并根据用户获取用户相关品牌，再跟据品牌获取品牌关联套餐> <功能详细描述>
	 * 
	 * @param referenceId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public List<SCVo> getRelatedScByReferenceId(long referenceId)
			throws HsCloudException;
	/**
	 * <变更虚拟机关联的套餐> 
	* <功能详细描述> 
	* @param referenceId
	* @param scId
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void changeVMSC(long referenceId,int scId)throws HsCloudException;
	/**
	 * <更改VM资源限制>
	 * @author jianwei zhang
	 * @param resourceLimit
	 * @param id
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public void changeVMResourceLimit(ResourceLimit resourceLimit,String id)throws HsCloudException;
	/**
	 * <插入操作日志> 
	* <功能详细描述> 
	* @param admin 后台管理员
	* @param description 操作表述
	* @param actionName 操作中文名 
	* @param operationResult 操作结果
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void insertOperationLog(Admin admin,String description,String actionName,short operationResult)throws HsCloudException;
	
		/**
	 * <查询API用户> 
	* <功能详细描述> 
	* @param accessAccountPage
	* @param query 
	 * @return 
	* @see [类、类#方法、类#成员]
	 */
    public Page<AccessAccountVO> findAccessAccountPage(Page<AccessAccountVO> accessAccountPage,
            String query);
    
    /**
     * <删除API用户> 
    * <功能详细描述> 
    * @param id 
    * @see [类、类#方法、类#成员]
     */
    public void deleteAccessAccount(long id);
    
    /**
     * <创建API用户> 
    * <功能详细描述> 
    * @param accessAccountVO 
    * @see [类、类#方法、类#成员]
     */
    public String saveAccessAccount(AccessAccountVO accessAccountVO);
    /**
     * <通过IP查询IP详细信息> 
	 * <功能详细描述>
     * @param ip
     * @return
     * @throws HsCloudException
     * @see [类、类#方法、类#成员]
     */
    public IPDetail getIPDetailByIP(String ip) throws HsCloudException;
    /**
     * <建立角色与资源域的关系> 
    * <功能详细描述> 
    * @param roleId
    * @param oldZoneIds
    * @param newZoneIds
    * @return
    * @throws HsCloudException 
    * @see [类、类#方法、类#成员]
     */
    public boolean updateZoneOfRolePermission(long roleId,String oldZoneIds,String newZoneIds)throws HsCloudException;
    /**
     * <获取角色下的资源域数据集> 
    * <功能详细描述> 
    * @param page
    * @param roleId
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public Page<Map<String, Object>> findZoneListOfRole(Page<Map<String, Object>> page,long roleId);
    /**
     * <获取管理员下的资源域数据集> 
    * <功能详细描述> 
    * @param adminId
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public List<Object> getZoneIdsByAdminId(long adminId);
	/**
	 * 回收站恢复VM
	 * @param uuid
	 * @param admin
	 * @return
	 * @throws HsCloudException
	 */
	public boolean recycleRestore(String uuid,Admin admin)throws HsCloudException;
	
	/**
	 * 回收站删除VM
	 * @param uuid
	 * @param admin
	 * @return
	 * @throws HsCloudException
	 */
	public boolean recycleTerminate(String uuid,Admin admin)throws HsCloudException;
	/**
	 * 获取vncviewer连接VNC的参数
	 * @param uuid
	 * @return
	 * @throws HsCloudException
	 */
	public VncClientVO getClientVnc(String uuid) throws HsCloudException;
	/**
	 * <系统操作日志查询> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<OperationLog> getOperationByPage(Page<OperationLog> paging,OperationLogQueryVo queryVo)throws HsCloudException;
	/**
	 * <删除虚拟机绑定的IP> 
	* <功能详细描述> 
	* @param uuid
	* @param oldIP
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean deleteIPOfVm(String uuid, String oldIP,long adminId) throws HsCloudException;
	/**
	 * <增加虚拟机的IP> 
	* <功能详细描述> 
	* @param uuid
	* @param newIP
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean addIPOfVm(String uuid, String newIP,long adminId) throws HsCloudException;
	/**
	 * <判断是否是特殊用户> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean isSpecialAdmin(Admin admin) throws HsCloudException;

	/**
	 * 汇总统计
	 * @param pageMonthStatis
	 * @param admin
	 * @param queryVO
	 * @return
	 */
	public Page<MonthStatisVO> getMonthStatis(
			Page<MonthStatisVO> pageMonthStatis, Admin admin, QueryVO queryVO);
	/**
	 * 管理员根据权限获取对应的zone
	 * @param adminId
	 * @return
	 * @throws HsCloudException
	 */
	public List<ZoneVO> getZoneByAdmin(Admin admin)throws HsCloudException;
	
	/**
	 * <vrouter模板分页查询> 
	* <功能详细描述> 
	* @param pageVrouterTemplate
	* @return 
	* @see [类、类#方法、类#成员]
	 */
    public Page<VrouterTemplateVO> pageVrouterTemplateVO(Page<VrouterTemplateVO> pageVrouterTemplate);
    
    /**
     * <删除VrouterTemplate> 
    * <功能详细描述> 
    * @param id 
    * @see [类、类#方法、类#成员]
     */
    public void deleteVrouterTemplate(long id);
    
    /**
     * <编辑vrouter模板> 
    * <功能详细描述> 
    * @param vrouterTemplateVO 
     * @return 
    * @see [类、类#方法、类#成员]
     */
    public String editVrouterTemplate(VrouterTemplateVO vrouterTemplateVO);
    
    /**
     * <查询NetWorkBean分页> 
    * <功能详细描述> 
    * @param pageNetworkBean
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public Page<NetWorkBean> findPageNetwork(Page<NetWorkBean> pageNetworkBean);
    
    /**
     * <删除外网> 
    * <功能详细描述> 
    * @param id
    * @param rangeId
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public boolean deleteNetwork(long id, long rangeId);
    /**
     * <订单详情>
     * @param orderId
     * @param orderType
     * @return
     */
    public List<OrderItemVo> orderDetail(long orderId,short orderType);

    /**
     * 修改账户信息
     * @param accountVO
     */
	public void updateAccountRate(AccountVO accountVO);

	public Page<ResponsibilityIncoming> getMonthIncoming(List<Sort> parseSort,Page<ResponsibilityIncoming> pageResponsibilityIncoming,Admin admin, QueryVO queryVO);
	/**
	 * <禁用节点> 
	* <功能详细描述> 
	* @param id
	* @param userId
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public boolean freezeNode(long id,long userId);
	/**
	 * <启用节点> 
	* <功能详细描述> 
	* @param id
	* @param userId
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public boolean activeNode(long id,long userId);
	/**
	 * <通过套餐code和domain code 获取套餐>
	 * @param scCode
	 * @param domainCode
	 * @return
	 */
	public ServiceCatalog getByCode(String scCode, String domainCode,String brandCode);
	/**
	 * 账户添加返点及礼金折扣率的时候调用
	 * 校验用户品牌和账户有效的折扣率之和不能大于100
	 * @param pointRebate
	 * @param giftRebate
	 * @return
	 */
	public boolean verifyUserBrandDiscountRate(User user,Integer pointRebate,Integer giftRebate);
	/**
	 * 用户添加返点及礼金折扣率的时候调用
	 * 校验账户和用户品牌有效的折扣率之和不能大于100
	 * @param pointRebate
	 * @param giftRebate
	 * @return
	 */
	public boolean verifyUserAccountDiscountRate(User user,Integer pointRebate,Integer giftRebate);


	/**
	 * <通过domain id获取domain code>
	 * @param domainList
	 * @return
	 */
	public List<DomainVO> getDomainCodebyId(List<Domain> domainList);
	/**
	 * <验证套餐编码是否重复>
	 * @param serviceCatalog
	 * @return
	 */
	public boolean hasServiceCatalogCode(ServiceCatalog serviceCatalog) throws HsCloudException;
	/**
	 * <查询客户校验信息> 
	* <功能详细描述> 
	* @param accessId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Object getOuterInfomation(String externalUser,String accessId) throws HsCloudException;
	/**
	 * <同步客户信息> 
	* <将hscloud用户email同步给客户平台> 
	* @param userId
	* @param accessId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public String synchroExternalUser(String externalUser,String localUser,long userId,String accessId) throws HsCloudException;	
	/**
	 * <查询未关联的OS>
	 * @param page
	 * @param zoneGroupId
	 * @param zoneName
	 * @return
	 * @throws HsCloudException
	 */
	public Page<OsVO> getUnRelatedServerOs(Page<OsVO> page,long zoneGroupId, String OsName) throws HsCloudException;
	/**
	 * <查询已关联的OS>
	 * @param page
	 * @param zoneGroupId
	 * @param zoneName
	 * @return
	 * @throws HsCloudException
	 */
	public Page<OsVO> getRelatedServerOs(Page<OsVO> page,long zoneGroupId, String OsName) throws HsCloudException;
	/**
	 * <关联资源域与OS> 
	 * @param osIds
	 * @param groupId
	 * @return
	 * @throws HsCloudException
	 */
	public boolean associateOsAndZoneGroup(int[] osIds, long groupId) throws HsCloudException;
	/**
	 * <解除资源域与OS>
	 * @param osIds
	 * @param groupId
	 * @return
	 * @throws HsCloudException
	 */
	public boolean disAssociateOsAndZoneGroup(int[] osIds, long groupId) throws HsCloudException;
	/**
	 * <解除资源域与OS>
	 * @param osIds
	 * @param groupId
	 * @return
	 * @throws HsCloudException
	 */
	public String synchroExternalUser2(
			long userId) throws HsCloudException;


	public Excel IPMessageExcelExport(Admin admin,QueryVO queryVO);
	
	/**
	 * 保存短信信息并发送
	 */
	public void saveSmsMessage(SMSMessage smsMessage);
	/**
	 * 显示发送信息
	 */
	public Page<SMSMessage> findSmsMessagePage(Page<SMSMessage> page,String query);
	/**
	 * @param id 短信id
	 * @param name 删除用户名
	 */
	public void delSmsMessage(long id,String name);
	 
	/**
	 * 获取工单类型
	 * @return
	 */
	public List<AppWorkOrderType> getWorkOrderTpye();
	/**
	 * 查询账单日志
	 * @param sorts 排序
	 * @param page 分页参数
	 * @param query 查询条件
	 * @return
	 */
	public Page<ApplicationTranscationLogVO> getAppTransactionByPage(List<Sort> sorts,Page<ApplicationTranscationLogVO> page,String query);


}