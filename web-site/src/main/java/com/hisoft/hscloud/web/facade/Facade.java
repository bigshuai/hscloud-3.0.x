package com.hisoft.hscloud.web.facade;

import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.entity.Account;
import com.hisoft.hscloud.bss.billing.vo.AccountVO;
import com.hisoft.hscloud.bss.billing.vo.TranscationLogVO;
import com.hisoft.hscloud.bss.sla.om.entity.Order;
import com.hisoft.hscloud.bss.sla.om.util.SubmitOrderData;
import com.hisoft.hscloud.bss.sla.om.vo.InvoiceVO;
import com.hisoft.hscloud.bss.sla.om.vo.OrderItemVo;
import com.hisoft.hscloud.bss.sla.om.vo.OrderVo;
import com.hisoft.hscloud.bss.sla.om.vo.QueryCondition;
import com.hisoft.hscloud.bss.sla.om.vo.VmRefundLogVO;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceCatalog;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceItem;
import com.hisoft.hscloud.bss.sla.sc.entity.ZoneGroup;
import com.hisoft.hscloud.bss.sla.sc.vo.OAZoneGroupVO;
import com.hisoft.hscloud.bss.sla.sc.vo.ScFeeTypeVo;
import com.hisoft.hscloud.bss.sla.sc.vo.ZoneGroupVO;
import com.hisoft.hscloud.common.entity.HcEventVmOps;
import com.hisoft.hscloud.common.entity.UserBrand;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.entity.AbstractUser;
import com.hisoft.hscloud.crm.usermanager.entity.AccessAccount;
import com.hisoft.hscloud.crm.usermanager.entity.Action;
import com.hisoft.hscloud.crm.usermanager.entity.Country;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.crm.usermanager.entity.Industry;
import com.hisoft.hscloud.crm.usermanager.entity.MaintenanceLog;
import com.hisoft.hscloud.crm.usermanager.entity.ResourceType;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.entity.UserBank;
import com.hisoft.hscloud.crm.usermanager.entity.UserGroup;
import com.hisoft.hscloud.crm.usermanager.vo.CompanyInviteVO;
import com.hisoft.hscloud.crm.usermanager.vo.Node;
import com.hisoft.hscloud.crm.usermanager.vo.PerGroupVO;
import com.hisoft.hscloud.crm.usermanager.vo.PrivilegeVO;
import com.hisoft.hscloud.crm.usermanager.vo.UserGroupVO;
import com.hisoft.hscloud.crm.usermanager.vo.UserVO;
import com.hisoft.hscloud.crm.usermanager.vo.UserVO1;
import com.hisoft.hscloud.message.entity.Announcement;
import com.hisoft.hscloud.message.entity.Message;
import com.hisoft.hscloud.systemmanagement.vo.LogQueryVO;
import com.hisoft.hscloud.vpdc.ops.entity.VmSnapShotPlan;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcRenewal;
import com.hisoft.hscloud.vpdc.ops.json.bean.CreateVmBean;
import com.hisoft.hscloud.vpdc.ops.json.bean.CreateVpdcBean;
import com.hisoft.hscloud.vpdc.ops.json.bean.CreateVpdcBean.VlanNetwork;
import com.hisoft.hscloud.vpdc.ops.vo.FlavorVO;
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
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.CPUHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.DiskHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.MemoryHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.NetHistoryVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.vo.VmRealtimeMonitorVO;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author dinghb
 * @version [1.4, 2012-9-28]
 * @see [相关类/方法]
 * @since [HSCLOUD/1.4]
 */
public interface Facade {
	/**
	 * <User用户查询虚拟机（初始化获取、条件查询）>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public Page<InstanceVO> getVmsByUser(String page, String limit,
			Page<InstanceVO> pageIns, String queryType,String query, String sort, User user,
			List<Object> referenceIds, Long statusId,String type,String status_buss);

	/**
	 * <新加的接口：UI3.0   分页获取User用户所有的虚拟机> 
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public Page<InstanceVO> getHostsByUser(String page, String limit,
			Page<InstanceVO> pageIns, String query, String sort, User user,
			List<Object> referenceIds, Long statusId,String type,String status_buss);
	
	/**
	 * <用途: 1.默认加载分页获取User用户所有的待添加内网安全策略的云主机
	 *       2.当点击内网安全策略的搜索按钮: 分页获取User用户所有的待添加内网安全策略的云主机> 
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public Page<UnaddedIntranetInstanceVO> getWaitAddIntranetSecurityVms(
			String page, String limit, Page<UnaddedIntranetInstanceVO> pageUnaddedIns, 
			String query, String sort, User user, List<Object> referenceIds, 
			Long statusId, String type, String status_buss, String groupId)
			throws HsCloudException;
	
	/**
	 * <用途: 获取当前用户的当前uuid这台虚拟机的添加了外网安全的协议和端口的详细信息
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public List<VmExtranetSecurityVO> getExtranetSecurityInfo(String uuid)
			throws HsCloudException;

	/**
	 * <启动虚拟机>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean startVmByVmId(String vmId,Object o,String otype);

	/**
	 * <重启虚拟机>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String rebootVmByVmId(String vmId,Object o,String otype);

	/**
	 * <关闭虚拟机>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean closeVmByVmId(String vmId,Object o,String otype);

	/**
	 * <获取虚拟机远程连接>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String getVNCUrl(String vmId);
	
	/**
	 * 获取vncviewer连接VNC的参数
	 * @param uuid
	 * @return
	 * @throws HsCloudException
	 */
	public VncClientVO getClientVnc(String uuid,User u) throws HsCloudException;

	/**
	 * <备份虚拟机> 
	* <功能详细描述> 
	* @param vmId
	* @param sn_name_opsk
	* @param sn_name
	* @param sn_comments
	* @param sn_type
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String backupVmByVmId(String vmId, String sn_name, String sn_comments, int sn_type,Object o,String otype);

	/**
	 * <创建虚拟机备份计划>
	 * 
	 * @param vssp
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String createBackupVmPlanByVmId(VmSnapShotPlan vssp);

	/**
	 * <根据id删除某一备份>
	 * 
	 * @param id
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String deleteBackupByBackupId(String id, String ssid);

	/**
	 * <根据虚拟机id获取虚拟机备份计划>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public VmSnapShotPlanVO getBackupVmPlanByVmId(String vmId);

	/**
	 * <获取所有虚拟机备份历史记录>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<VmSnapShotVO> findAllBackupsByVmId(String vmId);

	/**
	 * <根据虚拟机id和备份的id还原虚拟机>
	 * 
	 * @param vmId
	 * @param backupId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String renewVM(String vmId, String backupId,User u,String otype);

	/**
	 * <删除虚拟机>
	 * 
	 * @param vmId
	 * @see [类、类#方法、类#成员]
	 */
	public void terminateVmByVmId(String vmId, String vmName,
			String terminateFlag, long uid);
	
	/**
	 * <正式云主机退款删除VM接口> <功能详细描述>
	 * 
	 * @param uuid
	 * @see [类、类#方法、类#成员]
	 */
	public void terminateVmWhenRefund(String uuid, User user, String comments);	

	/**
	 * <获取虚拟机操作系统用户名> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public String getVMOSUser(String osId);
	
	/**
	 * <重置虚拟机操作系统> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void resetVMOS(String vmId, String osId,String user,String pwd, User u,String otype);

	/**
	 * <根据虚拟机名称查询是否有同名虚拟机> <功能详细描述>
	 * 
	 * @param vmName
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean hasSameVmName(String vmName);

	/**
	 * <修改虚拟机名称>
	 * 
	 * @param vmName
	 * @param vmId
	 * @param uid
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean updateVmName(String vmName, String vmId, long uid);

	/**
	 * <查看虚拟机详情>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public InstanceDetailVo findDetailVmById(String vmId);
	
	/**
	 * <取消试用待审核VM>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public void cancelApplyTryVm(long uid,long referenceId) throws HsCloudException;

	/**
	 * <试用VM申请延期> <功能详细描述>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public void applyForDelayTryVm(long uid,long referenceId) throws HsCloudException;
	
	/**
	 * 创建VPDC
	 * @param vpdcBean
	 * @return
	 */
	public String createVPDC(CreateVpdcBean vpdcBean,User user) throws HsCloudException;
	
	/**
	 * <修改VPDC> 
	* <功能详细描述> 
	* @param vpdcId
	* @param name
	* @param vlans
	* @param userId
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void updateVPDC(Long vpdcId,String name,List<VlanNetwork> vlans,Long userId) throws HsCloudException;

	/******************************* Monitor Module ********************************/

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
			long toTime);

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
			long toTime);

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
			long toTime);

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
			long toTime);

	/**
	 * <虚拟机实时监控> <功能详细描述>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<VmRealtimeMonitorVO> getVmRealTimeMonitor(String vmId);

	/**
	 * usermanage 加载所以国家
	 */
	public List<Country> loadCountry();

	/**
	 * 加载所以行业
	 * 
	 * @return
	 */
	public List<Industry> loadIndustry();
	
	/**
	 * 通过Domain code 查询
	 * @param code
	 * @return Domain
	 */
	public Domain loadDomain(String code);

	public void userRegister(User user, String companyName, String groupIds,String externalUser,String localUser,String domainCode);

	/**
	 * 
	 * @param user
	 *            修改信息
	 * @param companyName
	 *            公司名称
	 * @param groupIds
	 *            组id
	 */
	public void updateUser(User user, String companyName, String groupIds);

	/**
	 * 
	 * @param email
	 *            登录邮箱名
	 * @param password
	 *            秘密
	 * @return User
	 */
	public User loginUserByEmail(String email, String password);
	
	/**
	 * 
	 * @param email  登录邮箱名
	 * @param password 密码
	 * @param domainId 域ID
	 * @return
	 */
	public User loginUserByEmail(String email, String password,long domainId);

	/**
	 * 激活用户
	 * 
	 * @param user
	 */
	public void activeUser(User user);

	public User getUserById(long userId);

	public UserVO getUserVOById(String userId) throws Exception;

	public void deleteUserById(String userId) throws Exception;

	public User getUserByEmail(String email);

	/**
	 * 修改密码
	 * 
	 * @param user
	 */
	public void resetUserPassword(User user);

	public void findPassword(User user) throws Exception;

	public List<UserGroupVO> getPermissionUserGroupVO(long userId,
			List<Object> primKeys);

	/**
	 * 
	 * @param query
	 *            查询条件
	 * @param sorts
	 *            排序条件
	 * @param page
	 *            分页信息
	 * @param userType
	 *            用户类型
	 * @param userId
	 *            企业用户id
	 * @param primKeys
	 *            权限
	 * @return
	 */
	public Page<UserVO> fuzzySearchPermissionUser(String query,
			List<Sort> sorts, Page<User> page, String userType, long userId,
			List<Object> primKeys);

	/**
	 * 
	 * @param page
	 *            分页信息
	 * @param sorts
	 *            排序条件
	 * @param userType
	 *            用户类型
	 * @param groupId
	 *            组id
	 * @param userId
	 *            用户id
	 * @return
	 * @throws Exception
	 */
	public Page<UserVO> userInGroup(Page<User> page, List<Sort> sorts,
			String userType, String groupId, long userId) throws Exception;

	/**
	 * 分页模糊查询组
	 * 
	 * @param query
	 * @param sorts
	 * @param page
	 * @param userId
	 * @param primKeys
	 * @return
	 */
	public Page<UserGroupVO> fuzzySearchPermissionUserGroup(String query,
			List<Sort> sorts, Page<UserGroup> page, long userId,
			List<Object> primKeys);

	public boolean duplicateUserGroup(long userId, String groupName);

	public void modifyUserOnlineStatus(long userId, short onlineStatus);

	public void deleteUserGroupById(String groupId) throws Exception;

	public void saveOrUpDatePermission(PerGroupVO gpv, long userId);

	public List<Node> getPermission(long userId, String groupId);

	public UserGroup getUserGroupById(String groupId);

	/**
	 * 修改用户信息
	 * 
	 * @param user
	 */
	public void modifyUser(User user);

	public long createUserGroup(String groupName, long id);

	public void changeGroupForUer(long currentUserId, String enUserId,
			String groupIds);

	public List<ServiceCatalog> getAllSC(List<Sort> sortList, String userLevel,Long domainId,
			Long zoneGroupId);

	public Page<Order> getOrderByPage(Page<Order> page, Order order, User user,
			List<Sort> sorts, String query);

	/**
	 * <根据订单号获取订单项> <功能详细描述>
	 * 
	 * @param id
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<OrderItemVo> getAllOrderItemsByOrder(Long id);

	/**
	 * <根据订单号取消订单> <功能详细描述>
	 * 
	 * @param orderId
	 * @see [类、类#方法、类#成员]
	 */
	public boolean cancelOrder(Long orderId) throws HsCloudException;

	/**
	 * <订单支付> <功能详细描述>
	 * 
	 * @param id
	 * @param userId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public Map<String,Object> orderPay(Long orderId, User user, String couponAmount,String giftAmount) throws HsCloudException;

	/**
	 * <根据套餐Id获取套餐对象> <功能详细描述>
	 * 
	 * @param id
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public ServiceCatalog getSCById(int id);

	/**
	 * <把购物车中的订单数据插入库中> <功能详细描述>
	 * 
	 * @param orderItems
	 * @param user
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public Long submitOrder(List<OrderItemVo> orderItems, User user)
			throws Exception;

	/**
	 * <根据条件分页查找订单信息> <功能详细描述>
	 * 
	 * @param condition
	 * @see [类、类#方法、类#成员]
	 */
	public void queryOrder(QueryCondition condition);
	/**
	 * <转正申请> <功能详细描述>
	 * 
	 * @param orderId
	 * @param orderStatus
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public boolean regularApply(long orderId, String orderStatus)
			throws HsCloudException;

	/**
	 * <延期申请> <功能详细描述>
	 * 
	 * @param orderId
	 * @param orderStatus
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public boolean delayApply(long orderId, String orderStatus)
			throws HsCloudException;

	/**
	 * <获取续费虚拟机订单信息> <功能详细描述>
	 * 
	 * @param vmId
	 * @param user
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public RenewOrderVO getRenewOrderInfo(String vmId, User u)
			throws HsCloudException;
	/**
	 * <设置控制面板密码> <功能详细描述>
	 * 
	 * @param vmId
	 * @param user
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public boolean setCPpwd(String vmId, String pwd, User user)
			throws HsCloudException;

	/**
	 * 通过用户id,获得账户信息
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public Account getAccountByUserId(long userId) throws HsCloudException;
	/**
	 * 通过用户获取账户信息及最终的折扣率信息
	 * @param user
	 * @return
	 * @throws HsCloudException
	 */
	public AccountVO getAccountWithRealRebateByUser(User user) throws HsCloudException;
	
	public long findUnreadCount(long userId);

	/**
	 * 查询未读信息
	 * @param page
	 * @param userId
	 * @return
	 */
	public Page<Message> findUnreadMessage(Page<Message> page, Long userId,
			Map<String, Object> condition);

	/**
	 * 查询已读信息
	 * @param page
	 * @param userId
	 * @return
	 */
	public Page<Message> findReadedMessage(Page<Message> page, Long userId,
			Map<String, Object> condition);

	/**
	 * 修改消息状态
	 * @param id
	 */
	public void modifyMessageStatus(Long id);

	/**
	 * 删除消息 <功能详细描述>
	 * @param id
	 * @see [类、类#方法、类#成员]
	 */
	public void deleteMessage(Long id);

	/**
	 * 保存用户银行账号
	 * @param userBank
	 */
	public void saveUserBank(UserBank userBank);

	/**
	 * @return Page<TranscationLogVO>
	 */
	public Page<TranscationLogVO> pagePermissionTrLog(List<Sort> sort, long id,
			Page<TranscationLogVO> pageLog, String query, List<Object> primKeys);

	/**
	 * 通过用户id查询用户银行账号
	 * @param userId
	 * @return
	 */
	public UserBank findBankByUserId(Long userId);
	/**
	 * <转正审核> <功能详细描述>
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public String toNormalV2(long feeTypeId,long referecenId, User user, String couponAmount,String giftAmount) throws HsCloudException;
	/**
	 * <根据资源Id获取资源> <功能详细描述>
	 * @param siId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public ServiceItem getSIById(int siId) throws HsCloudException;
	/**
	 * 通过邮件查询个人用户
	 * @param user
	 */
	public boolean existPersonalUser(User user);
	/**
	 * <根据前台传递参数生成支付宝请求需要的字符串> <功能详细描述>
	 * @param params
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public String genAlipayPayRequest(Map<String, String> params, Long userId,long domainId)
			throws HsCloudException;
	/**
	 * <校验支付宝同步响应返回结果> <功能详细描述>
	 * @param params
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public boolean verifyAlipayResponse(Map<String, String> params)
			throws HsCloudException;
	/**
	 * <校验支付宝异步响应返回结果> <功能详细描述>
	 * @param params
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public boolean verifyAlipayAsynResponse(Map<String, String> params)
			throws HsCloudException;
	/**
	 * 公司邀请
	 * @param senderId 邀请人id
	 * @param userId 被邀请人id
	 */
	public void companyInvite(long senderId, String email);

	/**
	 * 拒绝邀请
	 * @param inviteId 拒绝邀请id
	 */
	public void rejectedInvite(String inviteId);

	/**
	 * 接受邀请
	 * @param inviteId 接受邀请id
	 */
	public void acceptedInvite(String inviteId);

	/**
	 * 个人用户解除关系
	 * @param inviteId 解除邀请id
	 */
	public void userTerminateInvite(String inviteId);

	/**
	 * 企业用户解除关系
	 * @param senderId 邀请人id
	 * @param receiverId 接受人id
	 */
	public void entTerminateInvite(long senderId, long receiverId);

	public List<CompanyInviteVO> getInvite(long userId);

	/**
	 * 查询资源类型list <功能详细描述>
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<ResourceType> getResourceTypeList();

	/**
	 * 查询actionList <功能详细描述>
	 * @param actionType
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<Action> getActionList(String actionType);

	/**
	 * 未分配权限查询 <功能详细描述>
	 * @param type
	 * @param pagePrivilege
	 * @param query
	 * @param primKeys
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<PrivilegeVO> findUnassignedList(String type, Long id,
			Page<PrivilegeVO> pagePrivilege, String query, List<Object> primKeys);

	/**
	 * 已分配权限查询 <功能详细描述>
	 * @param type
	 * @param userId
	 * @param pagePrivilege
	 * @param query
	 * @param primKeys
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<PrivilegeVO> findAssignedList(String type, Long userId,
			Page<PrivilegeVO> pagePrivilege, String query, List<Object> primKeys);

	/**
	 * 统一定义查询 <功能详细描述>
	 * @param type
	 * @param userId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<PrivilegeVO> findUiformDefList(String type, Long userId);

	/**
	 * 权限赋权修改 <功能详细描述>
	 * @param permissionValue
	 * @param actionValue
	 * @param resourceValue
	 * @param userId
	 * @see [类、类#方法、类#成员]
	 */
	public void modifyRolePermissiion(String permissionValue,
			String actionValue, String resourceValue, long userId);

	/**
	 * <> <功能详细描述>
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public OrderItemVo addItem(int scId, int osId, long feeTypeId)
			throws HsCloudException;

	/**
	 * <根据品牌code获取品牌数据> <功能详细描述>
	 * @param code
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public UserBrand getBrandByCode(String code) throws HsCloudException;

	/**
	 * 读取公告信息 <功能详细描述>
	 * @param counter
	 * @param type
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<Announcement> findAnnouncementByConditoin(int counter, int type, Long domainId);

	/**
	 * <根据套餐id获取套餐对应的计费规则> <功能详细描述>
	 * @param scId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public List<ScFeeTypeVo> getScFeeTypeByScId(int scId) throws HsCloudException;

	/**
	 * <虚拟机续费> <功能详细描述>
	 * @param vmId
	 * @param feeTypeId
	 * @param user
	 * @param couponAmount 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String renewOrderV2(String uuid, long feeTypeId, User user, String couponAmount,String giftAmount,String renewOrderV2)
			throws HsCloudException;

	/**
	 * <根据套餐生成试用虚拟机> <功能详细描述>
	 * @param scId
	 * @param feeTypeId
	 * @param osId
	 * @param user
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public CreateVmBean generateVmBeanBySc(ServiceCatalog sc, int osId, User user)
			throws HsCloudException;

	/**
	 * <根据orderitem id 去查找对应的vm会出现三种情况> 1 NotExist 未创建 2 Try 试用 3 Normal 正常使用
	 * <功能详细描述>
	 * @param orderItemId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String getVMStateByOrderItemId(long orderItemId);

	/**
	 * 发票信息初始化 <功能详细描述>
	 * @param userId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public Map<String, Object> initInvoice(long userId);

	/**
	 * 发票申请 <功能详细描述>
	 * @param userId
	 * @param invoiceVO
	 * @see [类、类#方法、类#成员]
	 */
	public String applyInvoice(User user, InvoiceVO invoiceVO);
	/**
	 * <提交试用云主机> 
	* <功能详细描述> 
	* @param scId
	* @param osId
	* @param user 
	* @see [类、类#方法、类#成员]
	 */
	public void submitTryVm(int scId, int osId,User user);
	/**
	 * <根据ReferenceId获取套餐的计费规则信息> 
	* <功能详细描述> 
	* @param orderItemId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<ScFeeTypeVo> getScFeeTypeByReferenceId(long referenceId)throws HsCloudException;

	/**
	 * <获取vm关联的已支付的订单信息> <功能详细描述>
	 * @param referenceId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public List<OrderVo> getVmRelatedPaidOrder(long referenceId)
			throws HsCloudException;
	
	/**
	 * <发票列表查询> 
	* <功能详细描述> 
	* @param invoicePage
	* @param condition
	* @return 
	* @see [类、类#方法、类#成员]
	 */
    public Page<Map<String, Object>> findInvoiceRecordList(
            Page<Map<String, Object>> invoicePage, Map<String, Object> condition);
    /**
     * <根据物理主键获取实体bean> 
    * <功能详细描述> 
    * @param domainId
    * @return
    * @throws HsCloudException 
    * @see [类、类#方法、类#成员]
     */
    public Domain getDomainByDomainId(long domainId)throws HsCloudException;
    /**
	 * <重置虚拟机密码> 
	* <功能详细描述> 
	* @param vmId
	* @param password 
	* @see [类、类#方法、类#成员]
	 */
    public void resetSystemPwd(String vmId, String password,Object o,String otype);
    /**
     * <释放ip为空闲状态> 
    * <功能详细描述> 
    * @param sIP
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public boolean releaseIp(String sIP);
    /**
     * <查询续订业务数据列表> 
    * <功能详细描述> 
    * @param page
    * @param field
    * @param fieldValue
    * @param query
    * @param userId
    * @return
    * @throws HsCloudException 
    * @see [类、类#方法、类#成员]
     */
    public Page<VpdcRenewal> findVpdcRenewal(Page<VpdcRenewal> page,
			String field, Object fieldValue, String query, long userId)
			throws HsCloudException;
    /**
     * <提交退款申请> 
    * <功能详细描述> 
    * @param user
    * @param uuid
    * @param vmName
    * @param orderNo
    * @param ipVal
    * @param applyReason
    * @throws HsCloudException 
    * @see [类、类#方法、类#成员]
     */
	public void submitRefundApply(User user, String uuid,String applyReason,Short refundReasonType)
			throws HsCloudException;
    /**
     * <用于处理:  申请退款-->取消退款-->再次申请退款时的 再次申请退款操作>
    * <功能详细描述> 
    * @param uuid
    * @param refundReasonType
    * @param applyReason
    * @throws HsCloudException 
    * @see [类、类#方法、类#成员]
     */
	public void submitRefundApplyOnceAgain(String uuid, Short refundReasonType,
			String applyReason) throws HsCloudException;
	/**
	 * <根据虚拟机uuid判断云主机是否为申请中> 
	* <功能详细描述> 
	* @author liyunhui
	* @param uuid
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean isVMApplyingRefundByUuid(String uuid)throws HsCloudException;
	/**
	 * <根据虚拟机uuid来判断它的status是否为3.拒绝 4.已取消> 
	* <功能详细描述> 
	* @author liyunhui
	* @param uuid
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean isExistedVMStatusEquals3Or4ByUuid(String uuid)throws HsCloudException;
	/**
	 * <根据虚拟机uuid判断虚拟机是否被禁用> 
	* <功能详细描述:根据VpdcReference的属性来判断
	* private int isEnable = 0;//0:正常；1：手动禁用；2：到期禁用> 
	* @param uuid
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean isVmDisabledByUUID(String uuid)throws HsCloudException;
	/**
	 * <查询所有的ZoneGroup> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<ZoneGroup> getAllZoneGroups() throws HsCloudException;
	
	/**
	 * <查询关于虚拟机操作的日志> 
	* <功能详细描述> 
	* @author liyunhui
	* @param param
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<HcEventVmOps> getVmOpsLog(Page<HcEventVmOps> paing,LogQueryVO param)throws HsCloudException;
	/**
	 * <分页获取退款信息> 
	* <功能详细描述> 
	* @param paging
	* @param query
	* @param email
	* @param status
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<VmRefundLogVO> getVmRefundLogByPage(Page<VmRefundLogVO> paging,String query,Long userId,Short status)throws HsCloudException;
	/**
	 * <取消退款申请> 
	* <功能详细描述> 
	* @param uuid
	* @throws Exception 
	* @see [类、类#方法、类#成员]
	 */
	public void cancelRefundApply(Long vmRefundId) throws Exception;
	/**
	 * <用途: 获取当前用户的当前instanceId这台虚拟机的同一组下面所有已经添加内网安全的虚拟机
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public List<VmIntranetSecurityVO> findIntranetVmsByUuid(String uuid)
			throws HsCloudException;
	/**
	 * <用途: 提交内网安全和外网安全的数据 >
	 * @param SecurityPolicyVO:安全策略封装的VO
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public boolean submitSecurityPolicy(SecurityPolicyVO spVO) throws HsCloudException;
	/**
	 * 
	 * @param brandCode
	 * @return
	 * @throws HsCloudException
	 */
	public List<ZoneGroupVO> getAllZoneGroupByUser(String brandCode,Long domainId) throws HsCloudException;
	/**
	 * <记录没有密码登录的登录日志> 
	* <功能详细描述> 
	* @param maintenanceLog
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void saveLogByLoginWithoutPass(MaintenanceLog maintenanceLog)throws HsCloudException;
	/**
	 * 通过分平台id去后台数据库hc_domain里面拿到copyright_cn和copyright_en
	 */
	public Domain getDomainById(long domainId)throws HsCloudException;
	/**
	 * <插入操作日志> 
	* <功能详细描述> 
	* @param user 前台用户
	* @param description 操作描述
	* @param actionName 操作中文名称
	* @param operationResult 操作结果
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void insertOperationLog(User user, String description, String actionName, 
			short operationResult, String operateObject) throws HsCloudException;
	/**
	 * <系统修复>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean repairOSByVmId(String vmId,Object o,String otype) throws HsCloudException;
	
	public UserVO1 getUserVO(long userId);

	public void modify(User u);
	
	/**
	 * 修改全部消息状态
	 * @param id
	 */
	public void modifyAllMessageStatus(Long id) throws HsCloudException;
	
	/**
	 * 删除全部消息
	 */
	
	public void deleteAllMessage(Long id) throws HsCloudException;
	/**
	 * <按需购买提交订单>
	 * @param siIds
	 * @param vmNum
	 * @param ipNums
	 * @throws HsCloudException
	 */
	public void submitOrder4Need(SubmitOrderData submitData)throws HsCloudException;
	/**
	 * <路由按需购买提交订单>
	 * @throws HsCloudException
	 */
	public void submitRouterOrder(SubmitOrderData submitData)throws HsCloudException;
	/**
	 * <提交通过VPDC添加云主机的订单>
	 * @throws HsCloudException
	 */
	public void submitVMOrder(List<SubmitOrderData> vmCart, int vpdcId) throws HsCloudException;
	/**
	 * <3.0订单详情接口>
	 * @param orderId
	 * @param orderType
	 * @throws HsCloudException
	 */
	public List<OrderItemVo> orderDetail(long orderId,short orderType)throws HsCloudException;
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
	 * <获取操作系统资源列表> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public List<ServiceItem> listOSItem(int serviceType, List<Sort> sortList, String family);
	/**
	 * <查询所有的可按需购买的机房线路> 
	* <功能详细描述> 
	* @param brandCode
	* @param domainId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<ZoneGroupVO> getCustomZoneGroupByUser(String brandCode,Long domainId) throws HsCloudException;
	/**
	 * <正式虚拟机升级> 
	 * @param data
	 * @param referenceId
	 * @throws HsCloudException
	 */
	public void upgradeVM(SubmitOrderData data,String vmId)throws HsCloudException;
	/**
	 * <取消用户所有未支付订单>
	 * @param userId
	 * @throws HsCloudException
	 */
	public void cancelUnpaidOrderByUser(long userId)throws HsCloudException;
	/**
	 * <分页获取User用户所有的VPDC(包含非路由模式和路由模式)> 
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public Page<VpdcVo> findVpdcsByUser(String page, String limit, Page<VpdcVo> pageVpdc, 
			int vpdcType, String name, User user) throws HsCloudException;
	/**
	 * <分页获取User用户某一个VPDC下面的所有的云主机> 
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public Page<InstanceVO> getHostsByVpdcId(String page, String limit, Page<InstanceVO> pageIns, 
			String query, String sort, User user, Long statusId, String type, String status_buss, 
			long vpdcId) throws HsCloudException;
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
	public boolean dettachVolume(String vmId,int volumId,int volumeMode,User user)throws HsCloudException;
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
	 * <调整虚拟机flavor> <功能详细描述>
	 * 
	 * @param uuid
	 * @param flavorVO
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean resizeVm(String vmId, FlavorVO flavorVO,String addDisk,AbstractUser a) throws HsCloudException;
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
	public boolean modifyVolume(String vmId,int volumId,int volumeSize,User user)throws HsCloudException;
	/**
	 * 根据VPDC的id查询出VPDC信息> 
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public VpdcVo getVpdcById(long vpdcId) throws HsCloudException;

	/**
	 * <删除VPDC> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public boolean deleteVPDC(long vpdcId) throws HsCloudException;
	/**
	 * 根据VPDC的id查询出VpdcRouter信息> 
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public VpdcRouterVo getRouterByVpdcId(long vpdcId) throws HsCloudException;
	/**
	 * <查询出VrouterTemplate的第一条信息> 
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public VrouterTemplateVO getVrouterTemplate() throws HsCloudException;
	/**
	 * <启动路由器> 
	* <功能详细描述> 
	* @param uuid
	* @param o
	* @param otype
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean openRouter(String uuid,Object o,String otype) throws HsCloudException;
	
	/**
	 * <关闭路由器> 
	* <功能详细描述> 
	* @param uuid
	* @param o
	* @param otype
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean closeRouter(String uuid,Object o,String otype) throws HsCloudException;
	
	/**
	 * <重启路由器> 
	* <功能详细描述> 
	* @param uuid
	* @param o
	* @param otype
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public String rebootRouter(String uuid,Object o,String otype) throws HsCloudException;
	/**
	 * 根据referenceId 获取zonecode 再根据zonecode获取zoneGroupCode
	 * @param referenceId
	 * @return
	 * @throws HsCloudException
	 */
	public String getZoneGroupCodeByReferenceId(long referenceId) throws HsCloudException;
	/**
	 * <查询客户校验信息> 
	* <功能详细描述> 
	* @param accessId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Object getOuterInfomation(String userId,String accessId) throws HsCloudException;
	/**
	 * <前台转账> 
	* <前台转账操作并向客户平台发起转账请求> 
	* @param user
	* @param fee
	* @param feeType
	* @param transferMode
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Object transfer(User user,String fee,String feeType,String transferMode,String accessId) throws HsCloudException;
//	/**
//	 * <同步客户平台用户信息> 
//	* <在hscloud数据库中建立信息> 
//	* @param userId
//	* @param user
//	* @return
//	* @throws HsCloudException 
//	* @see [类、类#方法、类#成员]
//	 */
//	String synchroPlatformRelation(String userId,User user) throws HsCloudException;
	/**
	 * <查询本地数据库中对应的客户信息> 
	* <功能详细描述> 
	* @param localUser
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public String getExternalUserOfLocal(User localUser) throws HsCloudException;

	/**
	 * <更具zoneGroup ID获取相应的资源>
	 * @param serviceType
	 * @param sortList
	 * @return
	 */
	public List<OAZoneGroupVO> listServiceItemByZoneGroup(int serviceType,int zoneGroupId,List<Sort> sortList);

	public Object getUserInfomation(String userId, String accessId)
			throws HsCloudException;

	public void modifyUser2(User user);

	public Account getAccountByUserId(User user);
}