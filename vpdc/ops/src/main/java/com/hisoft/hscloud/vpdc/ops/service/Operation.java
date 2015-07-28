package com.hisoft.hscloud.vpdc.ops.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openstack.model.compute.nova.NovaServerForCreate;
import org.springframework.dao.DataAccessException;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.entity.HcEventResource;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.AbstractUser;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.vpdc.ops.entity.VmSnapShot;
import com.hisoft.hscloud.vpdc.ops.entity.VmSnapShotPlan;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcInstance;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference_OrderItem;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference_Period;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference_extdisk;
import com.hisoft.hscloud.vpdc.ops.json.bean.CreateVmBean;
import com.hisoft.hscloud.vpdc.ops.json.bean.CreateVpdcBean;
import com.hisoft.hscloud.vpdc.ops.json.bean.ResourceLimit;
import com.hisoft.hscloud.vpdc.ops.json.bean.CreateVpdcBean.VlanNetwork;
import com.hisoft.hscloud.vpdc.ops.vo.FlavorVO;
import com.hisoft.hscloud.vpdc.ops.vo.ImageVO;
import com.hisoft.hscloud.vpdc.ops.vo.InstanceDetailVo;
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
import com.hisoft.hscloud.vpdc.ops.vo.VpdcReferenceQuotaInfo;
import com.hisoft.hscloud.vpdc.ops.vo.VpdcReferenceVO;
import com.hisoft.hscloud.vpdc.ops.vo.VpdcRouterVo;
import com.hisoft.hscloud.vpdc.ops.vo.VpdcVo;

public interface Operation {

	/**
	 * 
	 * @title: getInstance
	 * @description 根据虚拟机的ID 来获取虚拟机实例
	 * @param vmId
	 * @return 设定文件
	 * @return InstanceVO 返回类型
	 * @throws
	 * @version 1.0
	 * @author hongqin.li
	 * @update 2012-6-8 下午1:54:19
	 */
	public InstanceVO getInstance(String vmId);

	/**
	 * 创建非路由VPDC
	 * 
	 * @param vpdcBean
	 * @return
	 * @throws HsCloudException
	 */
	public void createNoRouterVPDC(CreateVpdcBean vpdcBean, Long userId)
			throws HsCloudException;

	/**
	 * 创建路由VPDC
	 * 
	 * @param vpdcBean
	 * @return
	 * @throws HsCloudException
	 */
	public Map<NovaServerForCreate, HcEventResource> createRouterVPDC(
			CreateVpdcBean vpdcBean, User user) throws HsCloudException;

	/**
	 * <修改VPDC> <功能详细描述>
	 * 
	 * @param vpdcId
	 * @param name
	 * @param vlans
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public void updateVPDC(Long vpdcId, String name, List<VlanNetwork> vlans,
			Long userId) throws HsCloudException;

	/**
	 * <申请使用VM> <功能详细描述>
	 * 
	 * @param createVmBean
	 * @param uid
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public String applyForTryVm(CreateVmBean createVmBean, long userId,
			String defaultZone) throws HsCloudException;

	/**
	 * <admin用户手工创建虚拟机>
	 * 
	 * @param fb
	 * @param createVmBean
	 * @param uid
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public String createVm(CreateVmBean createVmBean, long adminId,
			String defaultZone) throws HsCloudException;

	/**
	 * <user用户购买创建虚拟机>
	 * 
	 * @param createVmBeans
	 * @param userId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public Map<NovaServerForCreate, HcEventResource> createVm(
			List<CreateVmBean> createVmBeans, String userId, String defaultZone)
			throws HsCloudException;

	/**
	 * <云应用user用户购买创建虚拟机>
	 * 
	 * @param createVmBeans
	 * @param userId
	 * @param supplierId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public Map<NovaServerForCreate, HcEventResource> appCreateVm(
			List<CreateVmBean> createVmBeans, int userId, int supplier)
			throws HsCloudException;

	/**
	 * <user用户通过API购买创建虚拟机>
	 * 
	 * @param createVmBeans
	 * @param userId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public Map<NovaServerForCreate, HcEventResource> createVmByAPI(
			List<CreateVmBean> createVmBeans, String userId,
			String defaultZone, String id, String accessId)
			throws HsCloudException;

	/**
	 * <user用户在非路由VPDC下购买创建虚拟机>
	 * 
	 * @param createVmBeans
	 * @param userId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public Map<NovaServerForCreate, HcEventResource> createVmInNoRouterVPDC(
			List<CreateVmBean> createVmBeans, String userId, String defaultZone)
			throws HsCloudException;

	/**
	 * <user用户在路由VPDC下购买创建虚拟机>
	 * 
	 * @param createVmBeans
	 * @param userId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public Map<NovaServerForCreate, HcEventResource> createVmInRouterVPDC(
			List<CreateVmBean> createVmBeans, String userId, String defaultZone)
			throws HsCloudException;

	/**
	 * <发布虚拟机> <功能详细描述>
	 * 
	 * @param vmName
	 * @param adminId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public String publishVm(VpdcReference vr, String ip, Long adminId)
			throws HsCloudException;

	/**
	 * <审核试用VM> <功能详细描述>
	 * 
	 * @param referenceId
	 * @param adminId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public String verifyTryVm(VpdcReference vr, String extDisks, long adminId)
			throws HsCloudException;

	/**
	 * 启动vm
	 * 
	 * @throws RESTRequestException
	 * @throws HsCloudException
	 * @throws HsCloudException
	 */
	public boolean openvm(String uuid, Object o, String otype)
			throws HsCloudException, HsCloudException;

	/**
	 * 关闭VM
	 * 
	 * @throws RESTRequestException
	 * @throws HsCloudException
	 */
	public boolean closeVm(String uuid, Object o, String otype)
			throws HsCloudException;

	/**
	 * 重启Vm
	 * 
	 * @throws RESTRequestException
	 * 
	 */
	public String rebootVm(String uuid, Object o, String otype)
			throws HsCloudException;

	/**
	 * 根据不同的image进行还原
	 * 
	 * @param uuid
	 * @param ssid
	 * @return
	 * @throws HsCloudException
	 * @throws HsCloudException
	 */
	public String renewVm(String uuid, String ssid, Object o, String otype);

	/**
	 * <后台管理员删除VM> <功能详细描述>
	 * 
	 * @param uuid
	 * @param vmName
	 * @param terminateFlag
	 * @param adminId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public boolean terminate(String uuid, String vmName, String terminateFlag,
			long adminId) throws HsCloudException;

	/**
	 * <获取虚拟机操作系统用户名> <功能详细描述>
	 * 
	 * @param osId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String getVMOSUser(int osId);

	/**
	 * <重置虚拟机操作系统> <功能详细描述>
	 * 
	 * @param vmId
	 * @param osId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean resetVMOS(String vmId, int osId, String user, String pwd,
			Object o, String otype);

	/**
	 * <退单删除VM接口> <功能详细描述>
	 * 
	 * @param referenceId
	 * @param adminId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public boolean terminate(long referenceId, long adminId, User u,
			String comments) throws HsCloudException;

	/**
	 * <正式云主机退款删除VM接口> <功能详细描述>
	 * 
	 * @param uuid
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public boolean terminate(String uuid, User user, String comments)
			throws HsCloudException;

	/**
	 * <备份Vm 完全快照等于备份 VM id=uuid, 备份实体=snapShot,备份=comments> <功能详细描述>
	 * 
	 * @param uuid
	 * @param backupName_stack
	 * @param backupName_db
	 * @param comments
	 * @param sn_type
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public String backupsVm(String uuid, String sn_name, String comments,
			int sn_type, Object o, String otype) throws HsCloudException;

	/**
	 * <终止快照:实质就是删除超过30分钟都还没有创建成功的快照>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean stopSnapshot(String vmId) throws HsCloudException;

	/**
	 * <设置自动备份Vm时间点> <功能详细描述>
	 * 
	 * @param vssp
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public String backupsVmPlan(VmSnapShotPlan vssp) throws HsCloudException;

	/**
	 * 获取虚拟机的备份计划根据虚拟机ID
	 * 
	 * @param vmId
	 * @return
	 * @throws DataAccessException
	 * @throws HsCloudException
	 */
	public VmSnapShotPlanVO getVmSnapShotPlanByVmId(String vmId)
			throws HsCloudException;

	/**
	 * 查询所有的备份--数据库层面
	 * 
	 * @throws HsCloudException
	 */
	public List<VmSnapShotVO> findAllSnapShots(String uuid)
			throws HsCloudException;

	/**
	 * <删除VM的指定snapshot> <功能详细描述>
	 * 
	 * @param VmId
	 * @param id
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public String deleteSnapShot(String VmId, String id)
			throws HsCloudException;

	/**
	 * <删除VM的所有snapshot> <功能详细描述>
	 * 
	 * @param VmId
	 * @param id
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public String deleteSnapShot(String VmId) throws HsCloudException;

	/**
	 * <根据reference获取其有效的instance> <功能详细描述>
	 * 
	 * @param reference
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public VpdcInstance getActiveVmFromVpdcReference(VpdcReference reference);

	/**
	 * 
	 * @title: releaseClient
	 * @description 获取VNC的路径
	 * @return void 返回类型
	 * @throws
	 * @version 1.0
	 * @author hongqin.li
	 * @throws RESTRequestException
	 * @update 2012-4-1 下午4:47:23
	 */
	public String getVnc(String uuid) throws HsCloudException;

	/**
	 * <查看新Vm name是否被使用> <功能详细描述>
	 * 
	 * @param vmName
	 * @param vmId
	 * @param userId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public boolean hasSameVmName(String vmName) throws HsCloudException;

	/**
	 * <更新VM name> <功能详细描述>
	 * 
	 * @param vmName
	 * @param vmId
	 * @param userId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public boolean updateVmName(String vmName, String vmId, long uid)
			throws HsCloudException;

	/**
	 * 
	 * @title: findVm
	 * @description 查找Vm 的详细信息
	 * @param vmId
	 * @return 设定文件
	 * @return InstanceDetailVo 返回类型
	 * @throws
	 * @version 1.0
	 * @author hongqin.li
	 * @throws RESTRequestException
	 * @update 2012-4-5 下午3:02:31
	 */
	public InstanceDetailVo findVm(String vmId) throws HsCloudException;

	/**
	 * <根据referenceId获取试用待审核VM详细信息> <功能详细描述>
	 * 
	 * @param referenceId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public InstanceDetailVo getTryVmInfo(long referenceId)
			throws HsCloudException;

	/**
	 * <根据虚拟机id只获取OS集合信息> <功能详细描述>
	 * 
	 * @param vmId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public InstanceDetailVo getVmOsListByVmId(String vmId)
			throws HsCloudException;

	/**
	 * <获取续费订单信息> <功能详细描述>
	 * 
	 * @param vmId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public RenewOrderVO getRenewOrder(String vmId, User u)
			throws HsCloudException;

	/**
	 * <获取某一个用户的VM(分页)> <功能详细描述>
	 * 
	 * @param sort
	 * @param user
	 * @param referenceIds
	 * @param pages
	 * @param statusId
	 * @param type
	 * @param status_buss
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public Page<InstanceVO> getVmsByUser(String sort, User user,
			List<Object> referenceIds, Page<InstanceVO> pages, Long statusId,
			String type, String status_buss) throws HsCloudException;

	/**
	 * 
	 * @title: fuzzyFindVmsNoType
	 * @description 按照IP或主机别名等进行模糊查询，不单独区分条件
	 * @param value
	 * @return 设定文件
	 * @return Page<InstanceVO> 返回类型
	 * @throws
	 * @version 1.0
	 * @author haibin.ding
	 * @throws RESTRequestException
	 * @update 2012-8-30 上午11:00:27
	 */
	public Page<InstanceVO> fuzzyFindVmsUser(String sort, String queryType,
			String value, Page<InstanceVO> page, User user,
			List<Object> referenceIds, Long statusId, String type,
			String status_buss) throws HsCloudException;

	/**
	 * <新加的接口：UI3.0 分页获取User用户所有的虚拟机>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public Page<InstanceVO> getHostsByUser(String sort, String query,
			Page<InstanceVO> page, User user, List<Object> referenceIds,
			Long statusId, String type, String status_buss, boolean isBuy)
			throws HsCloudException;

	/**
	 * <用途: 1.默认加载分页获取User用户所有的待添加内网安全策略的云主机 2.当点击内网安全策略的搜索按钮:
	 * 分页获取User用户所有的待添加内网安全策略的云主机>
	 * 
	 * @author liyunhui
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public Page<UnaddedIntranetInstanceVO> getWaitAddIntranetSecurityVms(
			String sort, String value,
			Page<UnaddedIntranetInstanceVO> pageUnaddedIns, User user,
			List<Object> referenceIds, Long statusId, String type,
			String status_buss, String groupId) throws HsCloudException;

	/**
	 * <用途: 获取当前用户的当前uuid这台虚拟机的添加了外网安全的协议和端口的详细信息
	 * 
	 * @author liyunhui
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<VmExtranetSecurityVO> getExtranetSecurityInfo(String uuid)
			throws HsCloudException;

	/**
	 * <后台管理员查询虚拟机列表> <功能详细描述>
	 * 
	 * @param nodeName
	 * @param field
	 * @param value
	 * @param page
	 * @param admin
	 * @param referenceIds
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public Page<VmInfoVO> fuzzyFindVmsAdmin(String nodeName, String field,
			String value, Page<VmInfoVO> page, Admin admin,
			List<Object> referenceIds, String sort, String zoneCode)
			throws HsCloudException;

	/**
	 * <后台管理员查询虚拟机业务列表> <功能详细描述>
	 * 
	 * @param nodeName
	 * @param field
	 * @param value
	 * @param page
	 * @param admin
	 * @param referenceIds
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public Page<VmInfoVO> FindVmsAdminBussiness(String field, String value,
			Page<VmInfoVO> page, Admin admin, List<Object> referenceIds,
			String type, String status_buss, String sort)
			throws HsCloudException;

	/**
	 * <后台管理员查询虚拟机回收站列表> <功能详细描述>
	 * 
	 * @param nodeName
	 * @param field
	 * @param value
	 * @param page
	 * @param admin
	 * @param referenceIds
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public Page<VmInfoVO> findVmsAdminRecycle(String field, String value,
			Page<VmInfoVO> page, Admin admin, List<Object> referenceIds,
			String type, String status_buss, String sort)
			throws HsCloudException;

	/**
	 * 
	 * @title: fuzzyFindImages
	 * @description 查询所有的image
	 * @param name
	 *            查询条件名称
	 * @param value
	 *            查询条件值
	 * @param page
	 * @return 设定文件
	 * @return Page<ImageVO> 返回类型
	 * @throws
	 * @version 1.0
	 * @author hongqin.li
	 * @update 2012-5-24 上午11:18:44
	 */
	public Page<ImageVO> fuzzyFindImages(String name, String value,
			Page<ImageVO> page);

	/**
	 * 
	 * @title: createFlavor
	 * @description 创建新的Flavor
	 * @param flavorVO
	 * @return
	 * @throws HsCloudException
	 *             设定文件
	 * @return String 返回类型
	 * @throws
	 * @version 1.0
	 * @author hongqin.li
	 * @update 2012-5-24 上午11:19:51
	 */
	public String createFlavor(FlavorVO flavorVO) throws HsCloudException;

	/**
	 * 
	 * @title: autoMigrateVm
	 * @description 自动迁移虚拟机
	 * @return
	 * @throws HsCloudException
	 *             设定文件
	 * @return String 返回类型
	 * @throws
	 * @version 1.3
	 * @author ljg
	 * @update 2012-9-24 下午2:48:08
	 */
	public boolean autoMigrateVm(String uuid) throws HsCloudException;

	/**
	 * 
	 * @title: resizeVm
	 * @description 调整虚拟机flavor
	 * @param uuid
	 * @param flavorVO
	 * @return
	 * @throws HsCloudException
	 *             设定文件
	 * @return boolean 返回类型
	 * @throws
	 * @version 1.3
	 * @author ljg
	 * @update 2012-9-25 下午12:37:26
	 */
	public boolean resizeVm(String uuid, FlavorVO flavorVO, String addDisk,
			AbstractUser a) throws HsCloudException;

	/**
	 * <根据nodeName查询vm信息> <功能详细描述>
	 * 
	 * @param nodeName
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public List<VpdcInstance> getVmIdsByNodeName(String nodeName,
			List<Object> referenceIds) throws HsCloudException;

	/**
	 * <根据userid查询该用户所在公司下所有vm信息> <功能详细描述>
	 * 
	 * @param uid
	 * @return
	 * @throws HsCloudException
	 */
	public List<VpdcReference> getCompanyRefefenceByUserId(long uid)
			throws HsCloudException;

	/**
	 * <迁移虚拟机确认> <功能详细描述>
	 * 
	 * @param uuid
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public boolean confirmMigrateVm(String uuid, boolean confirmFlag)
			throws HsCloudException;

	/**
	 * <更新虚拟机节点名称> <功能详细描述>
	 * 
	 * @param uuid
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public boolean updateHostNameOfVm(String uuid) throws HsCloudException;

	/**
	 * <虚拟机到期提醒线程> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void expireRemind() throws HsCloudException;

	/**
	 * <云应用到期提醒线程> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void appExpireRemind() throws HsCloudException;

	/**
	 * <修改虚拟机绑定的IP> <如果只传回oldIP，则只做解除绑定IP，否则再绑定上新的IP>
	 * 
	 * @param uuid
	 * @param oldIP
	 * @param newIP
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public boolean updateIPOfVm(String uuid, String oldIP, String newIP,
			Long adminId) throws HsCloudException;

	/**
	 * <获取虚拟机下所有的isics方式扩展盘> <功能详细描述>
	 * 
	 * @param vmId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public List<VolumeVO> getAttachVolumesOfVm(String vmId)
			throws HsCloudException;

	/**
	 * <删除虚拟机的某一指定扩展盘> <功能详细描述>
	 * 
	 * @param vmId
	 * @param volumId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public boolean dettachVolume(String vmId, int volumId, int volumeMode,
			AbstractUser a) throws HsCloudException;

	/**
	 * <根据vmId获取orderItemId> <功能详细描述>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public VpdcReference_OrderItem getOrderItemByReferenceId(long referenceId);

	/**
	 * <根据orderItemId获取reference> <功能详细描述>
	 * 
	 * @param orderId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public VpdcReference getReferenceByOrderItemId(Long orderItemId);

	/**
	 * <根据Id获取reference> <功能详细描述>
	 * 
	 * @param VmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public VpdcReference getReferenceById(long referenceId);

	/**
	 * <根据vmId获取reference> <功能详细描述>
	 * 
	 * @param VmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public VpdcReference getReferenceByVmId(String vmId);

	/**
	 * <根据vmName获取reference> <功能详细描述>
	 * 
	 * @param referenceId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public VpdcReference getReferenceByVmName(String vmName);

	/**
	 * <根据vmid的名称获取其instance> <功能详细描述>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public VpdcInstance getInstanceByVmId(String vmId) throws HsCloudException;

	/**
	 * <根据VM的名称获取其instance> <功能详细描述>
	 * 
	 * @param vmName
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public VpdcInstance getInstanceByVmName(String vmName)
			throws HsCloudException;

	/**
	 * <查看订单下所有虚拟机> <功能详细描述>
	 * 
	 * @param orderId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<VpdcReference> findReferencesByOrderItems(
			List<Long> orderItemIds);

	/**
	 * <更加orderItemId获取VpdcReference_OrderItem实体> <功能详细描述>
	 * 
	 * @param orderItemId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public VpdcReference_OrderItem getOrderItemByOrderItemId(String orderItemId);

	/**
	 * <更新虚拟机与订单的关联> <功能详细描述>
	 * 
	 * @param referenceId
	 * @param orderItemId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean updateReference_OrderItem(long referenceId,
			String orderItemId);

	/**
	 * <新增虚拟机与订单的关联> <功能详细描述>
	 * 
	 * @param referenceId
	 * @param orderItemId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean saveReference_OrderItem(long referenceId, String orderItemId);

	/**
	 * <更新虚拟机与订单的关联> <功能详细描述>
	 * 
	 * @param oldOrderItemId
	 * @param newOrderItemId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean updateReference_OrderItem(String oldOrderItemId,
			String newOrderItemId);

	/**
	 * <根据referenceId更新VM的使用周期> <功能详细描述>
	 * 
	 * @param referenceId
	 * @param start_time
	 * @param end_time
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean updateReferencePeriod(long referenceId, Date start_time,
			Date end_time);

	/**
	 * <根据orderItemId更新VM的使用周期> <功能详细描述>
	 * 
	 * @param orderItemId
	 * @param start_time
	 * @param end_time
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean updateReferencePeriod(String orderItemId, Date start_time,
			Date end_time);

	/**
	 * <冻结虚拟机> <功能详细描述>
	 * 
	 * @param referenceId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean freezeVM(String vmId, long uid, int freezeType);

	/**
	 * <启用所有虚拟机> <功能详细描述>
	 * 
	 * @param referenceId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean activeVM(String vmId, long uid);

	/**
	 * <启用到期虚拟机> <功能详细描述>
	 * 
	 * @param referenceId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean activeExpireVM(VpdcReference vr, long uid);

	/**
	 * <调整虚拟机的所有者> <功能详细描述>
	 * 
	 * @param vmOwner
	 * @param vmId
	 * @param uid
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public boolean updateVmOwmer(long vmOwner, String vmId, long uid)
			throws HsCloudException;

	/**
	 * <添加虚拟机的扩展盘> <功能详细描述>
	 * 
	 * @param addDisk
	 * @param vmId
	 * @see [类、类#方法、类#成员]
	 */
	/*
	 * public boolean attachExttendDisk(final String addDisk, final String vmId,
	 * long uid) throws Exception;
	 */

	/**
	 * <手动迁移虚拟机> <功能详细描述>
	 * 
	 * @param uuid
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public boolean manualMigrateVm(String uuid, String hostName, Admin admin,
			String hostIP) throws HsCloudException;

	/**
	 * <根据VM的id获取该VM的IP> <功能详细描述>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String getVmIpByVmId(String vmId);

	/**
	 * <获取虚拟机使用周期> <功能详细描述>
	 * 
	 * @param referenceId
	 * @see [类、类#方法、类#成员]
	 */
	public VpdcReference_Period getReferencePeriod(long referenceId);

	/**
	 * <虚拟机到期处理> <包括到期的挂起、关闭、删除>
	 * 
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public void operateVmForExpired() throws HsCloudException;

	/**
	 * <获取所有开通业务的虚拟机> <功能详细描述>
	 * 
	 * @param referenceIds
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public List<VpdcReferenceVO> getAllAvailableVM(List<Object> referenceIds,
			String zoneCode) throws HsCloudException;

	/**
	 * <获取最后一次的备份信息> <功能详细描述>
	 * 
	 * @param vmId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public VmSnapShot getNewestVmSnapShot(String vmId) throws HsCloudException;

	/**
	 * <取消试用待审核VM> <功能详细描述>
	 * 
	 * @param uid
	 * @param referenceId
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public void cancelApplyTryVm(long uid, long referenceId)
			throws HsCloudException;

	/**
	 * <试用VM延期申请> <功能详细描述>
	 * 
	 * @param uid
	 * @param referenceId
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public void applyForDelayTryVm(long uid, long referenceId)
			throws HsCloudException;

	/**
	 * <试用VM转正> <功能详细描述>
	 * 
	 * @param uid
	 * @param referenceId
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public void regularTryVm(long uid, Long referenceId)
			throws HsCloudException;

	/**
	 * <审核延期VM> <功能详细描述>
	 * 
	 * @param vmId
	 * @param adminId
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public void verifyForDelayTryVm(long referenceId, int delayLong,
			long adminId) throws HsCloudException;

	/**
	 * <额外延期> <功能详细描述>
	 * 
	 * @param referenceId
	 * @param adminId
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public void extraDelayTryVm(long referenceId, int delayLong, long adminId)
			throws HsCloudException;

	/**
	 * <后台管理员为正式虚拟机延期> <功能详细描述>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public void delayRegularVm(long referenceId, int delayLong, long adminId)
			throws HsCloudException;

	/**
	 * <拒绝通过试用待审核VM> <功能详细描述>
	 * 
	 * @param referenceId
	 * @param comments
	 * @param adminId
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public void cancelApplyTryVm(long referenceId, String comments, long adminId)
			throws HsCloudException;

	/**
	 * <记录虚拟机到期日期变更日志> <功能详细描述>
	 * 
	 * @param description
	 * @param referenceId
	 * @param oldExpire
	 * @param newExpire
	 * @see [类、类#方法、类#成员]
	 */
	public void saveVmPeriodLog(String description, Long referenceId,
			Date oldExpire, Date newExpire);

	/**
	 * <查看当前VM是否有备份任务-DB&openstack> <功能详细描述>
	 * 
	 * @param description
	 * @param referenceId
	 * @param oldExpire
	 * @param newExpire
	 * @see [类、类#方法、类#成员]
	 */
	public boolean hasSnapshotTask(String vmId);

	/**
	 * <通过Scsis方式创建并挂载扩展盘> <功能详细描述>
	 * 
	 * @param vmId
	 * @param diskName
	 * @param diskSize
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public String createAndAttachExttendDiskByScsis(String addDisk,
			String vmId, AbstractUser a) throws HsCloudException;

	/**
	 * <获取某一虚拟机下所有通过Scsis方式挂载的扩展盘> <功能详细描述>
	 * 
	 * @param vmId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public List<VpdcReference_extdisk> getAttachExttendDisksOfVmByScsis(
			String vmId) throws HsCloudException;

	/**
	 * <删除某一虚拟机下所有通过Scsis方式挂载的扩展盘> <功能详细描述>
	 * 
	 * @param vmId
	 * @param diskId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public boolean deleteAndDetachExttendDiskByScsis(String vmId, int diskId,
			AbstractUser a) throws HsCloudException;

	/**
	 * <调整虚拟机确认> <功能详细描述>
	 * 
	 * @param uuid
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public boolean confirmResizeVm(String uuid, boolean confirmFlag)
			throws HsCloudException;

	/**
	 * <获取虚拟机下所有的扩展盘> <功能详细描述>
	 * 
	 * @param vmId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public List<VolumeVO> getAllAttachVolumesOfVm(String vmId)
			throws HsCloudException;

	/**
	 * <根据orderitem获取VM的id> <功能详细描述>
	 * 
	 * @param orderItemId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public String getVMIdByOrderItem(String orderItemId)
			throws HsCloudException;

	/**
	 * <获取所有的虚拟机数据集以及其状态> <功能详细描述>
	 * 
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public List<VpdcReferenceVO> getAllAvailableVMs(List<Object> referenceIds,
			String zoneCode) throws HsCloudException;

	/**
	 * <获取某一节点下的虚拟机数据集以及其状态> <功能详细描述>
	 * 
	 * @param nodeName
	 * @param referenceIds
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public List<VpdcReferenceVO> getAllAvailableVMs(String nodeName,
			List<Object> referenceIds) throws HsCloudException;

	/**
	 * <重置VM系统密码> <功能详细描述>
	 * 
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public void resetSystemPwd(String vmId, String password, Object o,
			String otype) throws HsCloudException;

	/**
	 * <重置VM系统密码> <功能详细描述>
	 * 
	 * @param vmId
	 * @param password
	 * @param o
	 * @param otype
	 * @param taskId
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public void resetSystemPwd(String vmId, String password, Object o,
			String otype, long taskId) throws HsCloudException;

	/**
	 * <获取资源最充沛的zone code>
	 * 
	 * @param zoneIds
	 * @return
	 * @throws HsCloudException
	 */
	public String getOptimalZoneOfZones(String zoneCodes)
			throws HsCloudException;

	/**
	 * <修改某一虚拟机下所有通过Scsis方式挂载的扩展盘大小> <功能详细描述>
	 * 
	 * @param vmId
	 * @param diskId
	 * @param diskSize
	 * @param a
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public boolean modifyExttendDisk(String vmId, int diskId, int diskSize,
			AbstractUser a) throws HsCloudException;

	/**
	 * <用途: 获取当前用户的当前instanceId这台虚拟机的同一组下面所有已经添加内网安全的虚拟机
	 * 
	 * @author liyunhui
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<VmIntranetSecurityVO> findIntranetVmsByUuid(String uuid)
			throws HsCloudException;

	/**
	 * <用途: 提交内网安全和外网安全的数据 >
	 * 
	 * @param SecurityPolicyVO
	 *            :安全策略封装的VO
	 * @author liyunhui
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean submitSecurityPolicy(SecurityPolicyVO spVO)
			throws HsCloudException;

	/**
	 * <用途：更新Reference资源限制>
	 * 
	 * @author zhangjianwei
	 * @param resourceLimit
	 * @param id
	 * @throws HsCloudException
	 */
	public void updateResourceLimit(ResourceLimit resourceLimit, String id)
			throws HsCloudException;

	/**
	 * <用途：系统修复功能>
	 * 
	 * @param uuid
	 * @param o
	 * @param otype
	 * @return
	 * @throws HsCloudException
	 */
	public boolean repairOS(String uuid, Object o, String otype)
			throws HsCloudException;

	/**
	 * 恢复回收站中的虚拟机
	 * 
	 * @param uuid
	 * @param o
	 * @param otype
	 * @return
	 * @throws HsCloudException
	 */
	public boolean recycleRestore(String uuid, Admin admin)
			throws HsCloudException;

	/**
	 * 回收站中彻底删除VM
	 * 
	 * @param uuid
	 * @param o
	 * @param otype
	 * @return
	 */
	public boolean recycleTerminate(String uuid, Admin admin);

	/**
	 * 初始化VNC的端口池
	 */
	public void initVNCPortPool();

	/**
	 * 获取指定分平台的1个可用的端口
	 * 
	 * @return
	 */
	public Integer getAvailablePort(Long domainId);

	/**
	 * VNC获取端口并占用端口
	 * 
	 * @param port
	 * @param vmid
	 */
	public void useVncPort(String port, User u, String vmid);

	/**
	 * <删除虚拟机绑定的IP> <传回oldIP，只做解除绑定IP>
	 * 
	 * @param uuid
	 * @param oldIP
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public boolean deleteIPOfVm(String uuid, String oldIP, Long adminId)
			throws HsCloudException;

	/**
	 * <虚拟机绑定的IP> <传回newIP，只做绑定IP>
	 * 
	 * @param uuid
	 * @param newIP
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public boolean addIPOfVm(String uuid, String newIP, AbstractUser a)
			throws HsCloudException;

	/**
	 * <分页获取User用户所有的VPDC(包含非路由模式和路由模式)>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public Page<VpdcVo> findVpdcsByUser(Page<VpdcVo> pageVpdc, int vpdcType,
			String name, User user) throws HsCloudException;

	/**
	 * <分页获取User用户某一个VPDC下面的所有的云主机>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public Page<InstanceVO> getHostsByVpdcId(String sort, String query,
			Page<InstanceVO> page, User user, Long statusId, String type,
			String status_buss, long vpdcId) throws HsCloudException;

	/**
	 * <修改虚拟机配置的带宽信息> <功能详细描述>
	 * 
	 * @param id
	 * @param bandwidth
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public void modifyBandwidthOfVm(String vmId, String bandwidth)
			throws HsCloudException;

	/**
	 * 根据VPDC的id查询出VPDC信息>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public VpdcVo getVpdcById(long vpdcId) throws HsCloudException;

	/**
	 * <删除VPDC>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean deleteVPDC(long vpdcId) throws HsCloudException;

	/**
	 * <启动路由器> <功能详细描述>
	 * 
	 * @param uuid
	 * @param o
	 * @param otype
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public boolean openRouter(String uuid, Object o, String otype)
			throws HsCloudException;

	/**
	 * <关闭路由器> <功能详细描述>
	 * 
	 * @param uuid
	 * @param o
	 * @param otype
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public boolean closeRouter(String uuid, Object o, String otype)
			throws HsCloudException;

	/**
	 * <重启路由器> <功能详细描述>
	 * 
	 * @param uuid
	 * @param o
	 * @param otype
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public String rebootRouter(String uuid, Object o, String otype)
			throws HsCloudException;

	/**
	 * 根据VPDC的id查询出VpdcRouter信息>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public VpdcRouterVo getRouterByVpdcId(long vpdcId) throws HsCloudException;

	/**
	 * <获取vm名称>
	 * 
	 * @param tableName
	 * @return
	 * @throws HsCloudException
	 */
	public String getVMNameSerialNumber(String tableName)
			throws HsCloudException;

	/**
	 * <根据user id 获取domain id>
	 * 
	 * @param userId
	 * @return
	 * @throws HsCloudException
	 */
	public String getDomainIdByUserId(Long userId) throws HsCloudException;

	/**
	 * 
	 * @param comments
	 * @param vmId
	 * @param uid
	 * @return
	 * @throws HsCloudException
	 */
	public boolean updateVmComments(String comments, String outComments,
			String vmId, long uid) throws HsCloudException;

	/**
	 * <根据zone code 获取该zone下可以ip数量>
	 * 
	 * @param zoneCode
	 * @param VMNum
	 * @return
	 */
	public int getIPCountByZoneCode(String zoneCode) throws HsCloudException;

	/**
	 * <根据zone code获取该区域下building的vm数量>
	 * 
	 * @param zoneCode
	 * @return
	 * @throws HsCloudException
	 */
	public int getVmBuilingCountByZoneCode(String zoneCode)
			throws HsCloudException;

	/**
	 * <根据zone code获取该zone的最优节点数>
	 * 
	 * @param zones
	 * @return
	 * @throws HsCloudException
	 */
	public String getOptimalZoneOfZones(String[] zones, int vmNum)
			throws HsCloudException;

	/**
	 * @param userId
	 * @return
	 * @throws HsCloudException
	 *             获取当前用户可用虚拟机指标信息
	 */
	public List<VpdcReferenceQuotaInfo> getVmsByUser(long userId)
			throws HsCloudException;

	/**
	 * @param userId
	 * @return
	 * @throws HsCloudException
	 *             获取当前用户虚拟机总数
	 */
	public int getVmsCountByUser(long userId) throws HsCloudException;

	/**
	 * @param userId
	 * @return
	 * @throws HsCloudException
	 *             获取续费列表中未到期云主机总数
	 */
	public int getVmRenewalCountByUser(long userId) throws HsCloudException;

}