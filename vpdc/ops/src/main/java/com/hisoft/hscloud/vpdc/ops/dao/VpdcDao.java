/**
 * @title VpdcDao.java
 * @package com.hisoft.hscloud.vpdc.ops.dao
 * @description 用一句话描述该文件做什么
 * @author hongqin.li
 * @update 2012-3-28 下午7:04:46
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.ops.dao;

import java.util.Date;
import java.util.List;

import org.openstack.model.compute.nova.NovaInstanceThin;
import org.springframework.dao.DataAccessException;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.User;
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
import com.hisoft.hscloud.vpdc.ops.entity.VpdcRouter;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcRouter_OrderItem;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcRouter_Period;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcRouter_Period_Log;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcVrouterTemplate;
import com.hisoft.hscloud.vpdc.ops.json.bean.VmOsBean;
import com.hisoft.hscloud.vpdc.ops.vo.ExpireRemindVO;
import com.hisoft.hscloud.vpdc.ops.vo.InstanceDetail_ServiceCatalogVo;
import com.hisoft.hscloud.vpdc.ops.vo.VmInfoVO;
import com.hisoft.hscloud.vpdc.ops.vo.VpdcInstanceVO;
import com.hisoft.hscloud.vpdc.ops.vo.VpdcReferenceQuotaInfo;
import com.hisoft.hscloud.vpdc.ops.vo.VpdcReferenceVO;

/**
 * 
* <VPDC-Dao> 
* <功能详细描述> 
* 
* @author  dinghb 
* @version  [版本号, 2014-1-27] 
* @see  [相关类/方法] 
* @since  [产品/模块版本]
 */
public interface VpdcDao {

	/**
	 * 保存VPDC
	 * @param vpdc
	 * @throws HsCloudException
	 */
	public void saveVpdc(Vpdc vpdc) throws HsCloudException;
	
	/**
	 * <获取VPDC> 
	* <功能详细描述> 
	* @param id
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Vpdc getVpdc(Long id) throws HsCloudException;
	
	/**
	 * 创建VM
	 * @param vrBean
	 * @param flavorId
	 * @throws HsCloudException
	 */
	public void createVpdcReference(VpdcReference vrBean) throws HsCloudException;

	/**
	 * <更新vpdcReference 实例> 
	* <功能详细描述> 
	* @param vr
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void updateVpdcReference(VpdcReference vr) throws HsCloudException;
	/**
	 * 
	 * @title: updateVpdcInstance
	 * @description 创建或修改vpdcInstance
	 * @param vpdcInstance 设定文件
	 * @return boolean    返回类型
	 * @version 1.4
	 * @author haibin.ding
	 * @throws HsCloudException 
	 * @update 2012-9-17 下午17:55:26
	 */
	public boolean updateVpdcInstance(VpdcInstance vpdcInstance) throws HsCloudException;
	


	/**
	 * <更加ownerId获取所有虚拟机id> 
	* <功能详细描述> 
	* @param ownerId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
 	public List<Object> findVMIdsByOwnerId(long ownerId) throws HsCloudException;
	
 	/**
 	 * <查询操作系统名称> 
 	* <使用表查询而非对象查询> 
 	* @param osId
 	* @return
 	* @throws HsCloudException 
 	* @see [类、类#方法、类#成员]
 	 */
 	public String findOsName(Integer osId) throws HsCloudException;
 	/**
 	 * <根据Zone编码返回名称> 
 	* <功能详细描述> 
 	* @param code
 	* @return
 	* @throws HsCloudException 
 	* @see [类、类#方法、类#成员]
 	 */
 	public String findZoneName(String code) throws HsCloudException;
 	/**
 	 * <查询套餐关联的OS集合> 
 	* <功能详细描述> 
 	* @param scId
 	* @return
 	* @throws HsCloudException 
 	* @see [类、类#方法、类#成员]
 	 */
 	List<VmOsBean> findOsListByReferenceId(Long referenceId) throws HsCloudException;
 	/**
 	 * <查询套餐名称> 
 	* <使用表查询而非对象查询> 
 	* @param osId
 	* @return
 	* @throws HsCloudException 
 	* @see [类、类#方法、类#成员]
 	 */
 	public String findScName(Integer scId) throws HsCloudException;
 	/**
 	 * <查询操作系统其它信息> 
 	* <OS表为手动维护，限OS内字段信息> 
 	* @param field
 	* @param osId
 	* @return
 	* @throws HsCloudException 
 	* @see [类、类#方法、类#成员]
 	 */
 	public String findOsInfo(String field,int osId) throws HsCloudException;
 	/**
 	 * hc_bill_rule表使用表查询而非对象查询
 	 * @title: findRuleInfo
 	 * @param field
 	 * @param id
 	 * @version 1.0
	 * @author haibin.ding
 	 * @throws HsCloudException 
	 * @update 2012-8-22 下午15:10:33
 	 */
 	public String findRuleInfo(String field,int id) throws HsCloudException;
 	/**
 	 * 根据orderItemId集合获取vm当前正在使用的orderitem的套餐信息
 	 * @title: findOrderItemServiceCatalog
 	 * @param orderItemId
 	 * @return
 	 * @throws HsCloudException 
 	 */
 	public InstanceDetail_ServiceCatalogVo findVmServiceCatalog(List<VpdcReference_OrderItem> lvroi) throws HsCloudException;
	/**
	 * <前台无条件只根据权限查询总数> 
	* <功能详细描述> 
	* @param ids
	* @param value
	* @param statusId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public int getVRCountByIds(List<Object> ids,Long statusId) throws HsCloudException;
	/**
	 * <前台无条件只根据权限查询分页> 
	* <功能详细描述> 
	* @param offSet
	* @param length
	* @param ids
	* @param value
	* @param sort
	* @param statusId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<VpdcReference> findVpdcReferencesByIds(int offSet,int length,List<Object> ids,String sort,Long statusId) throws HsCloudException;
	
	/**
	 * <前台根据条件和权限查询总数> 
	* <功能详细描述> 
	* @param ids
	* @param field
	* @param value
	* @param statusId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public int getVRCountByCondition(List<Object> ids,String field,String value,Long statusId) throws HsCloudException;

	/**
	 * <新加的接口：UI3.0   分页获取User用户所有的虚拟机> 
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public int getVRCountByQuery(List<Object> ids,String query,Long statusId,boolean isBuy) throws HsCloudException;
	
	/**
	 * <前台内网安全根据条件和权限查询总数> 
	* <功能详细描述> 
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public int getVRCountByCondition(List<Long> ids,String value,Long statusId) throws HsCloudException;
	
	/**
	 * <用途: 获取当前虚拟机的已经添加外网安全策略的数据
	 * @author liyunhui
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<VmExtranetSecurity> getExtranetSecurityVmInfoByUuid(String uuid)
			throws HsCloudException;
	
	/**
	 * <前台根据条件和权限查询分页> <功能详细描述>
	 * @param offSet
	 * @param length
	 * @param ids
	 * @param field
	 * @param value
	 * @param sort
	 * @param statusId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public List<VpdcReference> findVpdcReferencesByCondition(int offSet,
			int length, String sort, List<Object> ids, String field,
			String value, Long statusId) throws HsCloudException;
	
	/**
	 * <新加的接口：UI3.0   分页获取User用户所有的虚拟机> 
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */	
	public List<VpdcReference> findVpdcReferencesByQuery(int offSet,
			int length, String sort, List<Object> ids, String query, 
			Long statusId,boolean isBuy) throws HsCloudException;	
	
	/**
	 * <前台根据条件和权限查询分页> <功能详细描述>
	 * @author liyunhui
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public List<VpdcReference> findVpdcReferencesByCondition(int offSet,
			int length, String sort, List<Long> ids, String value,
			Long statusId) throws HsCloudException;
	
	/**
	 * <后台查看虚拟机列表总量> 
	* <功能详细描述> 
	* @param nodeName
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public int getVRCount(String nodeName,String zoneCode) throws HsCloudException;
	
	/**
	 * <后台查看虚拟机列表对象> 
	* <功能详细描述> 
	* @param offSet
	* @param length
	* @param nodeName
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	
	public List<VpdcReference> findVpdcReference(int offSet,int length,String nodeName,String zoneCode) throws HsCloudException;
	/**
	 * <后台根据条件查看虚拟机列表总量> 
	* <功能详细描述> 
	* @param nodeName
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	
	public int getVRCount(String nodeName,String field,String value,String zoneCode) throws HsCloudException;
	/**
	 * <后台根据条件查看虚拟机列表对象> 
	* <功能详细描述> 
	* @param offSet
	* @param length
	* @param nodeName
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<VpdcReference> findVpdcReference(int offSet,int length,String nodeName,String field,String value,String zoneCode) throws HsCloudException;	
	/**
	 * <后台业务根据条件查看虚拟机列表总量> 
	* <功能详细描述> 
	* @param ids
	* @param nodeName
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public int getVRCountByIdsAdminBuss(String vmType,String vm_Buss,String field,String value) throws HsCloudException;
	
	/**
	 * <后台业务根据条件查看虚拟机列表对象> 
	* <功能详细描述> 
	* @param offSet
	* @param length
	* @param ids
	* @param nodeName
	* @param sort
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<VpdcReference> findVpdcReferenceByIdsAdminBuss(int offSet,int length,String vmType,String vm_Buss,String field,String value,String sort) throws HsCloudException;
	/**
	 * <后台业务根据referenceId和条件查看虚拟机列表总量> 
	* <功能详细描述> 
	* @param ids
	* @param nodeName
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public int getVRCountByIdsAdminBuss(List<Object> ids,String vmType,String vm_Buss,String field,String value) throws HsCloudException;
	
	/**
	 * <后台业务根据referenceId和条件查看虚拟机列表对象> 
	* <功能详细描述> 
	* @param offSet
	* @param length
	* @param ids
	* @param nodeName
	* @param sort
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<VpdcReference> findVpdcReferenceByIdsAdminBuss(int offSet,int length,List<Object> ids,String vmType,String vm_Buss,String field,String value,String sort) throws HsCloudException;
	
	/**
	 * <根据节点名称获取节点ID> 
	* <功能详细描述> 
	* @param hostName
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public int getHostIdByHostName(String hostName) throws HsCloudException; 
	
	/**
	 * 
	 * @title: updateVmNodeName
	 * @description update the second name of Vm
	 * @param VmName 设定文件
	 * @return void    返回类型
	 * @version 1.0
	 * @author haibin.ding
	 * @throws HsCloudException 
	 * @update 2012-4-5 下午4:59:30
	 */
	public boolean updateVmNodeName(String vmNodeName,String vmId)throws DataAccessException, HsCloudException;

	/**
	 * 
	 * @title: updateVmSnapShotId
	 * @description 存储虚拟机备份
	 * @param snapShot_id
	 * @param vmId
	 * @return
	 * @throws DataAccessException 设定文件
	 * @return boolean    返回类型
	 * @version 1.0
	 * @author hongqin.li
	 * @throws HsCloudException 
	 * @update 2012-6-7 下午5:49:06
	 */
	public boolean createVmSnapShot(VmSnapShot snapShot)throws HsCloudException;
	/**
	 * <创建备份任务(备份会由线程一次去执行)> 
	* <功能详细描述> 
	* @param vsst
	* @return
	* @throws DataAccessException
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean updateVmSnapShotTask(VmSnapShotTask vsst)throws HsCloudException;
	/**
	 * <查找最后一个完成的备份任务(检测openstack是否还在备份中、或已完成，用于判断是否可执行下一个未完成的任务)> 
	* <功能详细描述> 
	* @return
	* @throws DataAccessException
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public VmSnapShotTask getLastDoSnapShotTasks()throws HsCloudException;
	/**
	 * <查找未完成的备份任务(status=0)> 
	* <功能详细描述> 
	* @return
	* @throws DataAccessException
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public VmSnapShotTask getUnSnapShotTask()throws HsCloudException;
	/**
	 * <查找某一虚拟机是否有待备份任务> 
	* <功能详细描述> 
	* @return
	* @throws DataAccessException
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public VmSnapShotTask getUnSnapShotTaskByVmId(String VmId)throws HsCloudException;
	/**
	 * <查找某一虚拟机所有已完成备份任务> 
	* <功能详细描述> 
	* @return
	* @throws DataAccessException
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<VmSnapShotTask> findDoSnapShotTasksByVmId(String VmId)throws HsCloudException;

	/**
	 * <删除与snapshot关联的task> 
	* <功能详细描述> 
	* @param snapShotId
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void deleteSnapShotTasks(String snapShotId) throws HsCloudException;
	
	/**
	 * 
	 * @title: createVmSnapShotPlan
	 * @description 创建虚拟机自动备份时间点
	 * @param vssp
	 * @throws DataAccessException 设定文件
	 * @return boolean    返回类型
	 * @version 1.0
	 * @author haibin.ding
	 * @throws HsCloudException 
	 * @update 2012-9-10 上午10:49:06
	 */
	public boolean createVmSnapShotPlan(VmSnapShotPlan vssp)throws DataAccessException, HsCloudException;
	/**
	 * 
	 * @title: loadVmSnapShotPlanByVmId
	 * @description 获取虚拟机的备份计划
	 * @param vssp
	 * @throws DataAccessException 设定文件
	 * @return VmSnapShotPlan    返回类型
	 * @version 1.0
	 * @author haibin.ding
	 * @throws HsCloudException 
	 * @update 2012-9-10 上午10:49:06
	 */
	public VmSnapShotPlan getVmSnapShotPlanByVmId(String vmId)throws DataAccessException, HsCloudException;
	
	/**
	 * 
	 * @title: findVmSnapShotPlan
	 * @description  查看当前时间是否有需要自动备份的虚拟机
	 * @param vssp
	 * @return
	 * @throws DataAccessException 设定文件
	 * @return List<VmSnapShotPlan>    返回类型
	 * @version 1.0
	 * @author haibin.ding
	 * @throws HsCloudException 
	 * @update 2012-9-10 上午10:49:06
	 */
	public List<VmSnapShotPlan> findVmSnapShotPlan(Date now)throws DataAccessException, HsCloudException;
	
	/**
	 * 
	 * @title: getVmSnapSot
	 * @description 获取虚拟机的所有备份镜像
	 * @param vmId
	 * @return 设定文件
	 * @return VmSnapShot    返回类型
	 * @version 1.0
	 * @author hongqin.li
	 * @throws HsCloudException 
	 * @update 2012-6-7 下午6:16:25
	 */
	public List<VmSnapShot> getVmSnapShot(String vmId) throws HsCloudException;
	
	/**
	 * 
	 * @title: getNewestVmSnapSot
	 * @description 获取虚拟机最新的备份镜像
	 * @param vmId
	 * @return 设定文件
	 * @return VmSnapShot    返回类型
	 * @version 1.0
	 * @author hongqin.li
	 * @throws HsCloudException 
	 * @update 2012-6-7 下午6:21:41
	 */
	public VmSnapShot getNewestVmSnapShot(String vmId) throws HsCloudException;
	/**
	 * <根据snapshot的id获取实体> 
	* <功能详细描述> 
	* @param Id
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public VmSnapShot getVmSnapShotById(Integer Id) throws HsCloudException;
	/**
	 * <根据snapshot的实体> 
	* <功能详细描述> 
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void deleteSnapShot(VmSnapShot vss) throws HsCloudException;
	/**
	  * 
	  * @title: findVpdcReferenceById
	  * @description 根据VpdcReference ID来查询VpdcReference的信息
	  * @return 设定文件
	  * @return VpdcReference    返回类型
	  * @version 1.4
	  * @author haibin.ding
	 * @throws HsCloudException 
	  * @update 2012-9-13 下午18:43:58
	  */
	public VpdcReference findVpdcReferenceById(long id) throws HsCloudException;
	
	 /**
	  * 
	  * @title: findVpdcReferenceByVmId
	  * @description 根据vm的ID来查询VpdcReference的信息
	  * @param vmId
	  * @return 设定文件
	  * @return VpdcReference    返回类型
	  * @version 1.4
	  * @author haibin.ding
	 * @throws HsCloudException 
	  * @update 2012-9-13 下午18:43:58
	  */
	public VpdcReference findVpdcReferenceByVmId(String vmId) throws HsCloudException;
	/**
	 * author: liyunhui
	 * description: 用于解决虚拟机回收站的恢复问题
	 */
	public VpdcInstance findRecycleInstanceByVmId(String vmId) throws HsCloudException;
	/**
	  * @title: findVpdcReferenceByVmName
	  * @description 根据vm的name来查询VpdcReference的信息
	  * @param vmName
	  * @return 设定文件
	  * @return VpdcReference    返回类型
	  * @version 1.4
	  * @author haibin.ding
	 * @throws HsCloudException 
	  * @update 2012-10-29 下午18:43:58
	  */
	public VpdcReference findVpdcReferenceByVmName(String vmName) throws HsCloudException;
	 /**
	  * @title: findVmByVmId
	  * @description 根据vm的ID来查询Vm的信息
	  * @param vmId
	  * @return 设定文件
	  * @return VpdcReference    返回类型
	  * @version 1.4
	  * @author haibin.ding
	 * @throws HsCloudException 
	  * @update 2012-9-13 下午18:43:58
	  */
	public VpdcInstance findVmByVmId(String vmId) throws HsCloudException;
    /**
	 * <删除快照:删除超过30分钟都还没有创建成功的快照> 
	* @param instanceId
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public boolean deleteSnapshot(Long instanceId) throws HsCloudException;	
	/**
	 * 
	 * 此方法描述的是：   保存vpdcReference_orderItem关系
	 * @author: haibin.ding    
	 * @version: 2012-9-19 下午2:54:47
	 * @throws HsCloudException 
	 */
	public boolean saveReferenceOrderItem(VpdcReference_OrderItem vroi) throws HsCloudException;
	/**
	 * 此方法描述的是：  根据referenceId 获取 VpdcReference_OrderItem
	 * @author: haibin.ding    
	 * @version: 2012-11-19 上午11:10:47
	 * @throws HsCloudException 
	 */
	public VpdcReference_OrderItem getOrderItemByReferenceId(long referenceId) throws HsCloudException;
	/**
	 * <  根据referenceId 获取 VpdcReference_OrderItem集合> 
	* <功能详细描述> 
	* @param referenceId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<VpdcReference_OrderItem> getOrderItemsByReferenceId(long referenceId) throws HsCloudException;
	
	/**
	 * 此方法描述的是：  根据orderItemId 获取 VpdcReference_OrderItem
	 * @author: haibin.ding    
	 * @version: 2012-11-19 上午11:10:47
	 * @throws HsCloudException 
	 */
	public VpdcReference_OrderItem getOrderItemByOrderItemId(String orderItemId) throws HsCloudException;
	
	/**
	 * <  保存VpdcRouter_OrderItem关系实体> 
	* <功能详细描述> 
	* @param vroi
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean saveRouterOrderItem(VpdcRouter_OrderItem vroi) throws HsCloudException;
	/**
	 * <根据routerID查询VpdcRouter_OrderItem关系实体> 
	* <功能详细描述> 
	* @param routerId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public VpdcRouter_OrderItem getOrderItemByRouterId(long routerId) throws HsCloudException;
	/**
	 * <根据routerID查询VpdcRouter_OrderItem关系实体集合> 
	* <功能详细描述> 
	* @param referenceId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<VpdcRouter_OrderItem> getOrderItemsByRouterId(long routerId) throws HsCloudException;
	/**
	 * <根据orderItemId 获取 VpdcRouter_OrderItem> 
	* <功能详细描述> 
	* @param orderItemId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public VpdcRouter_OrderItem getRouterOrderItemByOrderItemId(String orderItemId) throws HsCloudException;

	/**
	 * <是否是试用Order> 
	* <功能详细描述> 
	* @param orderItemId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean isTryOrder(String orderItemId) throws HsCloudException;
	
	/**
	 * 此方法描述的是：  根据CompanyId获取  reference
	 * @author: haibin.ding    
	 * @version: 2012-9-24 下午13:56:47
	 * @throws HsCloudException 
	 */
	public List<VpdcReference> findReferenceByCompanyId(long companyId,String value) throws HsCloudException;
	/**
	 * 此方法描述的是：  根据CompanyId获取  reference(分页)
	 * @author: haibin.ding    
	 * @version: 2012-9-24 下午13:56:47
	 * @throws HsCloudException 
	 */
	public List<VpdcReference> findReferenceByCompanyId(int first,int max,long companyId,String value) throws HsCloudException;
	
	/**
	 * 根据id查询vm信息--ljg
	 * @param id
	 * @return
	 * @throws HsCloudException 
	 */
	public VpdcInstance findVmById(long id) throws HsCloudException;
	/**
	 * 根据nodeName查询vm信息--ljg
	 * @param nodeName
	 * @return
	 * @throws HsCloudException 
	 */
	public List<VpdcInstance> getVmIdsByNodeName(String nodeName,List<Object> referenceIds) throws HsCloudException;
	/**
	 * 根据vmName查询vm信息--ljg
	 * @param vmName
	 * @return
	 * @throws HsCloudException 
	 */
	public List<VpdcReference> getVpdcReferencesByVmName(String vmName) throws HsCloudException;
	/**
	 * 根据nodeName、userId、adminId查询vm信息--ljg
	 * @param userId
	 * @param adminId
	 * @param nodeName
	 * @return
	 * @throws HsCloudException 
	 */
	public List<VpdcReference> findVmIds(int userId,int adminId,String nodeName) throws HsCloudException;

	/**
	 * IP置状态
	 * @return
	 * @throws HsCloudException 
	 */
	public void resetIPstatus(int status,Long uid,String Ip) throws HsCloudException;
	
	/**
	 * 根据ip修改ip明细表信息
	 * @return
	 * @throws HsCloudException 
	 */
	public void updateIpDetailByIp(Long objectId,int objectType,Long uid,int hostId,long Ip) throws HsCloudException;
	
	/**
	 * 更加reference id获取VpdcReference
	 * @param ids
	 * @return
	 */
	public List<VpdcReference> findByIds(List<Long> ids);
	/**
	 * 根据instance id 通过detailIP表获取其floating ip
	 * @param vmid
	 * @return
	 */
	public List<String> getFloatingIpsFromDetailIp(long instanceId);
	/**
	 * <获取所有有效的instance> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<VpdcInstance> findAllInstance() throws HsCloudException;
	/**
	 * <根据orderitemId获取VpdcReference> 
	* <功能详细描述> 
	* @param orderItemId
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public VpdcReference getReferenceByOrderItemId(Long orderItemId);
	/**
	 * <根据orderitemIds获取VpdcReferences> 
	* <功能详细描述> 
	* @param orderItemIds
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public List<VpdcReference> findReferencesByOrderItems(List<Long> orderItemIds);
	/**
	 * <更新虚拟机使用周期> 
	* <功能详细描述> 
	* @param referenceId
	* @param start_time
	* @param end_time
	* @see [类、类#方法、类#成员]
	 */
	public void updateReferencePeriod(VpdcReference_Period referencePeriod);
	/**
	 * <获取虚拟机使用周期> 
	* <功能详细描述> 
	* @param referenceId 
	* @see [类、类#方法、类#成员]
	 */
	public VpdcReference_Period getReferencePeriod(long referenceId);
	/**
	 * <新增虚拟机到期日期变更日志> 
	* <功能详细描述> 
	* @param vrpl
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void saveVMPeriodLog(VpdcReference_Period_Log vrpl) throws HsCloudException;
	/**
	 * <更新路由使用周期> 
	* <功能详细描述> 
	* @param routerPeriod 
	* @see [类、类#方法、类#成员]
	 */
	public void updateRouterPeriod(VpdcRouter_Period routerPeriod);
	/**
	 * <获取路由使用周期> 
	* <功能详细描述> 
	* @param referenceId 
	* @see [类、类#方法、类#成员]
	 */
	public VpdcRouter_Period getRouterPeriod(long routerId);
	/**
	 * <新增路由到期日期变更日志> 
	* <功能详细描述> 
	* @param vrpl
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void saveRouterPeriodLog(VpdcRouter_Period_Log vrpl) throws HsCloudException;
	/**
	 * <保存扩展盘信息> 
	* <功能详细描述> 
	* @param vied 
	* @see [类、类#方法、类#成员]
	 */
	public void saveExtDisk(VpdcReference_extdisk vied)throws HsCloudException;
	/**
	 * <删除扩展盘信息> 
	* <功能详细描述> 
	* @param vied 
	* @see [类、类#方法、类#成员]
	 */
	public void deleExtDisk(VpdcReference_extdisk vied)throws HsCloudException;
	/**
	 * <获取扩展盘信息根据vmId,volumeId> 
	* <功能详细描述> 
	* @param vied 
	* @see [类、类#方法、类#成员]
	 */
	public VpdcReference_extdisk getExtDiskByVolumeId(String vmId,int volumeId)throws HsCloudException;
	/**
	 * <获取扩展盘信息根据vmId,volumeName> 
	* <功能详细描述> 
	* @param vied 
	* @see [类、类#方法、类#成员]
	 */
	public VpdcReference_extdisk getExtDiskByVolumeId(String vmId,String name)throws HsCloudException;

	/**
	 * <获取VM的所有扩展盘根据vmId> 
	* <功能详细描述> 
	* @param vied 
	* @see [类、类#方法、类#成员]
	 */
	public List<VpdcReference_extdisk> getExtDisksByVmId(String vmId)throws HsCloudException;

	/**
	 * <获取所有开通业务的虚拟机> 
	* <功能详细描述> 
	* @param referenceIds
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<VpdcReferenceVO> getAllAvailableVM(List<Object> referenceIds,String zoneCode) throws HsCloudException;
	/**
	 * <在权限获取的reference中获取相应状态的ids> 
	* <功能详细描述> 
	* @param ids
	* @param type
	* @param status_buss
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<Object> findReferencesByType(List<Object> ids,String type,String status_buss) throws HsCloudException;
	/**
	 * <通过ipdetail表获取ip所绑定的VpdcInstance的id> 
	* <功能详细描述> 
	* @param vrpl
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Long findObjectIdByIp(String IP) throws HsCloudException;
	/**
	 * <通过instanceId获取IP> 
	* <功能详细描述> 
	* @param instanceId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public String findIPByObjectId(Long instanceId) throws HsCloudException;
	
	/**
	 * <根据orderItem查询VM的id，删除的也算在内> 
	* <功能详细描述> 
	* @param orderItemId
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String getVMIdByOrderItem(String orderItemId);
	/**
	 * 根据nodeName查询vm信息--ljg
	 * @param nodeName
	 * @return
	 * @throws HsCloudException 
	 */
	public List<VpdcReferenceVO> getVpdcReferenceByNodeName(String nodeName,List<Object> referenceIds) throws HsCloudException;
	/**
	 * <根据vmId查询Instance对象--sql语句> 
	* <功能详细描述> 
	* @param vmId
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public NovaInstanceThin getNovaInstance(String vmId);
	/**
	 * <根据fixip查询Instance对象--sql语句> 
	* <功能详细描述> 
	* @param vmId
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public NovaInstanceThin getNovaInstanceByFixIp(String fixip);
	
	/**
	 * <保存flavor> 
	* <功能详细描述> 
	* @param vf 
	* @see [类、类#方法、类#成员]
	 */
	public void saveVpdcFlavor(VpdcFlavor vf);
	/**
	 * <保存flavor> 
	* <功能详细描述> 
	* @param f 
	* @see [类、类#方法、类#成员]
	 */
	public Long sameFlavor(Integer disk,Integer memory,Integer vcpu);
	/**
	 * <获取flavor最大id> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public Long getFlavorMaxId();
	
	/**
	 * <存到期提醒邮件log> 
	* <功能详细描述> 
	* @param veel 
	* @see [类、类#方法、类#成员]
	 */
	public void saveMailLogForExpire(VmExpireEmailLog veel);
	
	/**
	 * <是否在到期指定的提醒周期内发送过提醒邮件> 
	* <功能详细描述> 
	* @param veel 
	* @see [类、类#方法、类#成员]
	 */
	public boolean ifSendMail(long referenceId,int remindCycle,Date expireDate);
	/**
	 * <查询所有符合到期操作的虚拟机> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public List<VpdcInstanceVO> getVpdcInstanceVOForExpireOperate();
	/**
	 * <读取到期操作配置> 
	* <功能详细描述> 
	* @param config
	* @param belongTo
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public String getConfigValue(String config,String belongTo)throws HsCloudException;	
	/**
	 * <查找过期信息> 
	* <功能详细描述> 
	 * @return 
	* @see [类、类#方法、类#成员]
	 */
	public List<ExpireRemindVO> findForExpireRemind();
	/**
	 * <用途: 通过虚拟机的instanceId查找VmIntranetSecurityInstance
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public List<VmIntranetSecurity_Instance> getIntranet_Instance(
			Long instance_id, String group_id) throws HsCloudException;
	/**
	 * <用途: 通过已经添加内网安全的虚拟机的instanceId查找它的VpdcRefrenceId
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public List<Long> getAddIntranetVpdcRefrenceIds(
			List<Long> addIntranetInstanceIds) throws HsCloudException;
	/**
	 * <用途: 通过instanceId查找虚拟机的主机名和内网IP，外网IP
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public List<VpdcInstance> findIntranetVmsByIntranetId(Long intranetSecurityId)
			throws HsCloudException;
	/**
	 * <用途: 通过instanceId数组删除在同一组里面已经添加内网安全策略的虚拟机
	 *       这些虚拟机的instanceId in (传进来的要删除的数组 )
	 * @author liyunhui
	 * @param isDeleteGroupRecord: false 只删除hc_vm_intranetsecurity记录,组的记录保留
	 *                             true 要把hc_vm_intranetsecurity_instance的组的记录也一起清掉
	 * @param groupId: 组在hc_vm_intranetsecurity_instance中的序号
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public void deleteAddedIntranetSecurityVms(List<String> deleted_instanceIds,
			Long groupId, boolean isDeleteGroupRecord) throws HsCloudException;
	/**
	 * <用途: 通过instanceId删除虚拟机的内网安全
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public void deleteVmIntranet(Long instanceId) throws HsCloudException;	
	/**
	 * <用途: 添加内网安全新的组>
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public void saveOrUpdateIntranetSecurityGroup(VmIntranetSecurity vis)
			throws HsCloudException;
	/**
	 * <用途: 通过虚拟机组的组号获得VmIntranetSecurity>
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public VmIntranetSecurity getIntranetSecurityGroup(String groupName)
			throws HsCloudException;
	/**
	 * <用途: 把instanceId集合里面的虚拟机全部添加到groupId这一组>
	 * @param addedVm_list: 需要添加到同一组的虚拟机的instanceId集合
	 * @param groupId: 组在hc_vm_intranetsecurity_instance中的序号
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public void saveOrUpdateIntranetSecurityVms(List<String> addedVm_list,
			Long groupId) throws HsCloudException;
	/**
	 * <用途: 删除uuid这台虚拟机的外网端口>
	 * <适用情形: 1.deleted_port_list为null时,直接删除uuid这台虚拟机的所有的外网端口
	 *          2.deleted_port_list非空时,删除uuid这台虚拟机需要删除的外网端口(不是全部)>
	 * @param deleted_port_list: 用户需要删除的端口集合
	 * @param uuid: 虚拟机的uuid
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public void deleteExtranetSecurity(List<String> deleted_port_list,
			String uuid) throws HsCloudException;
	/**
	 * <用途: 删除instanceId这台虚拟机的外网端口>
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public void deleteVmExtranet(Long instanceId) throws HsCloudException;	
	/**
	 * <用途: 向数据库中批量插入虚拟机的外网安全端口数据>
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public void saveOrUpdateAllExtranetSecurity(List<VmExtranetSecurity> ves_list) 
			throws HsCloudException;
	public Page<VpdcReference> fuzzyFindVmsAdmin(Page<VpdcReference> page,String nodeName,String field, 
			String value,List<Object> ids,String zoneCode,long createrId)throws HsCloudException;
	public VpdcInstance getActiveVmFromVpdcReference(long referenceId)throws HsCloudException;
	public Page<VmInfoVO> findRecyclingVmsAdmin(Page<VmInfoVO> page,String vmType,String vm_Buss,
			String field,String value,List<Object> ids,String sort)throws HsCloudException;

	/**
	 * 清空端口池数据
	 * @param ports
	 * @return
	 */
	public boolean clearVNCPortPool();
	/**
	 * 向端口池插入端口数据
	 * @param ports
	 * @return
	 */
	public boolean saveVNCPortPool(Object[] ports,Long domainId);
	/**
	 * 根据状态值获取端口池数据
	 * @param status 0：空闲；1：占用；null：所有
	 * @return
	 */
	public List<VmVNCPool> loadVNCPortPool(Long domainId);
	/**
	 * 更新端口状态
	 * @param port
	 * @return
	 */
	public boolean updateVNCPort(VmVNCPool port);
	/**
	 * 获取所有分平台id
	 * @return
	 */
	public List<Object> getAvailableDomainIds();
	/**
	 * 根据端口号查询端口实体
	 * @param port
	 * @return
	 */
	public VmVNCPool getPoolByPort(String port,Long domainId);
	/**
	 * <根据referenceId、status查询最新的instance实例> 
	* <功能详细描述> 
	* @param id
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public VpdcInstance getLatestVpdcInstance(long referenceId,int status) throws HsCloudException;
	/**
	 * <后台根据referenceId和条件查看虚拟机列表对象> 
	* <功能详细描述> 
	* @param page
	* @param ids
	* @param nodeName
	* @param field
	* @param value
	* @param zoneCode
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<VpdcReference> findVpdcReferenceByOwnerAdmin(Page<VpdcReference> page,
			String owner,String nodeName,String zoneCode)throws HsCloudException;
	/**
	 * 根据VMId查询虚拟机配置
	 * @param vmId
	 * @return
	 * @throws HsCloudException
	 */
	public VpdcReference getVpdcReferenceByVmId(String vmId) throws HsCloudException;
	
	/**
	 * 存储router配置模板
	 * @param rTemplate
	 * @throws HsCloudException
	 */
	public void saveRouterTemplate(VpdcVrouterTemplate rTemplate) throws HsCloudException;
	
	/**
	 * 根据ID获取router配置模板信息
	 * @param id
	 * @return
	 * @throws HsCloudException
	 */
	public VpdcVrouterTemplate getRouterTemplate(Long id) throws HsCloudException;
	
	/**
	 * 存储network
	 * @param network
	 * @throws HsCloudException
	 */
	public Long saveVpdcNetwork(VpdcNetwork network) throws HsCloudException;
	/**
	 * <根据id获取VpdcNetwork实例> 
	* <功能详细描述> 
	* @param id
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public VpdcNetwork getVpdcNetwork(Long id) throws HsCloudException;
	
	/**
	 * 更新删除network
	 * @param network
	 * @throws HsCloudException
	 */
	public void deleVpdcNetwork(Long id,Long userId) throws HsCloudException;
	
	/**
	 * 存储Lan
	 * @param lan
	 * @throws HsCloudException
	 */
	public Long saveVpdcLan(VpdcLan lan) throws HsCloudException;
	
	/**
	 * 根据机房线路获取有效IP
	 * @param zoneGroupId
	 * @return
	 */
	public String getValidWanNetworkIP(Long zoneGroupId) throws HsCloudException;
	
	/**
	 * 根据IP获取WanNetwork
	 * @param ip
	 * @return
	 * @throws HsCloudException
	 */
	public VpdcNetwork getWanNetwork(String ip) throws HsCloudException;
	
	/**
	 * 存储network和（VM/VRouter）关系
	 * @param networkObject
	 */
	public void saveNetwork_Object(VpdcNetwork_Object networkObject);
	
	/**
	 * <删除关系实例VpdcNetwork_Object> 
	* <功能详细描述> 
	* @param id 
	* @see [类、类#方法、类#成员]
	 */
	public void deleNetwork_Object(VpdcNetwork_Object vno);
	
	/**
	 * <根据对象id查找对象与Network的关联> 
	* <功能详细描述> 
	* @param objectId
	* @param objectType
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public List<VpdcNetwork_Object> findNetwork_Objects(Long objectId,int objectType);
	/**
	 * <根据networkId查找对象与Network的关联> 
	* <功能详细描述> 
	* @param networkId
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public List<VpdcNetwork_Object> findNetwork_Objects(Long networkId);
	
	/**
	 * <获取User用户所有的VPDC的数量> 
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public int getVpdcsCountByUser(int vpdcType, String name, User user) 
			throws HsCloudException;
	
	/**
	 * <分页获取User用户所有的VPDC(包含非路由模式和路由模式)> 
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public List<Vpdc> findVpdcsByUser(int offSet, int length, int vpdcType, 
			String name, User user) throws HsCloudException;
	
	/**
	 * <根据机房线路ID查询机房线路名称> 
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public String findZoneGroupName(Long zoneGroupId) throws HsCloudException;
	
	/**
	 * <获取User用户某一个VPDC下面的所有的云主机> 
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public int getVRCountByQuery(String query, Long statusId, long vpdcId) 
			throws HsCloudException;

	/**
	 * <获取User用户某一个VPDC下面的所有的云主机> 
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public List<VpdcReference> findVpdcReferencesByQuery(int offSet, int length, 
			String sort, String query, Long statusId, long vpdcId) throws HsCloudException;

	/**
	 * 根据VPDC的id查询出VPDC信息> 
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */	
	public Vpdc getVpdcById(long vpdcId) throws HsCloudException;
	
	/**
	 * <删除VPDC> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public void deleteVPDC(Vpdc vpdc) throws HsCloudException;
	/**
	 * <根据id获取Router> 
	* <功能详细描述> 
	* @param id
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public VpdcRouter getRouter(Long id) throws HsCloudException;
	/**
	 * <根据uuid获取Router> 
	* <功能详细描述> 
	* @param uuid
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public VpdcRouter getRouter(String uuid) throws HsCloudException;
	
	/**
	 * <保存VpdcRouter> 
	* <功能详细描述> 
	* @param router
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void saveRouter(VpdcRouter router) throws HsCloudException;
	/**
	 * <根据vpdcId获取Router> 
	* <功能详细描述> 
	* @param uuid
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public VpdcRouter getRouterByVpdcId(long vpdcId) throws HsCloudException;
	/**
	 * <获取vm名称>
	 * @param tableName
	 * @return
	 * @throws HsCloudException
	 */
	public String getVMNameSerialNumber(String tableName) throws HsCloudException;
	/**
	 * <获取domain ID 通过User ID>
	 * @param userId
	 * @return
	 * @throws HsCloudException
	 */
	public String getDomainIdByUserId(Long userId)throws HsCloudException;
	/**
	 * <根据zone code 获取该zone下的IP数量>
	 * @param zoneCode
	 * @return
	 * @throws HsCloudException
	 */
	public int getIPCountByZoneCode(String zoneCode) throws HsCloudException;
	/**
	 * <根据zone code获取该zone下building的vm数量>
	 * @param zoneCode
	 * @return
	 * @throws HsCloudException
	 */
	public int getVmBuilingCountByZoneCode(String zoneCode)throws HsCloudException;
	/**
	 * @param userId 用户id
	 * @return
	 * 获取用户下所有可用云主机
	 */
	public List<VpdcReferenceQuotaInfo> findVpdcReferenceByUserId(long userId);
	/**
	 * @param userId
	 * @return
	 * @throws HsCloudException
	 * 获取当前用户虚拟机总数
	 */
	public int getVmsCountByUser(long userId)throws HsCloudException;
	/**
	 * @param userId
	 * @return
	 * @throws HsCloudException
	 * 获取续费列表中未到期云主机总数
	 */
	public int getVmRenewalCountByUser(long userId)throws HsCloudException;
	
}