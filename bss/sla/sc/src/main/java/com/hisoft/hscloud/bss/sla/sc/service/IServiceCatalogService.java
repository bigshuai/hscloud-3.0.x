package com.hisoft.hscloud.bss.sla.sc.service;

import java.util.List;

import org.hibernate.HibernateException;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.sc.entity.ExtDisk;
import com.hisoft.hscloud.bss.sla.sc.entity.Os;
import com.hisoft.hscloud.bss.sla.sc.entity.ScFeeType;
import com.hisoft.hscloud.bss.sla.sc.entity.ScIsolationConfig;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceCatalog;
import com.hisoft.hscloud.bss.sla.sc.vo.SCVo;
import com.hisoft.hscloud.bss.sla.sc.vo.ScFeeTypeVo;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.crm.usermanager.vo.DomainVO;

/**
* @description service catalog服务层的接口类
* @version 1.0
* @author jiaquan.hu
* @update 2012-3-31 上午10:00:14
*/
public interface IServiceCatalogService {

	/**
	* @title: get
	* @description 根据id获取service catalog信息
	* @param id
	* @return ServiceCatalog 返回类型
	* @throws
	* @version 1.0
	* @author jiaquan.hu
	* @update 2012-3-31 上午10:06:24
	*/
	public ServiceCatalog get(int id);

	/**
	* @title: save
	* @description 更新 或者存储service catalog
	* @param serviceCatalog 设定文件
	* @return void 返回类型
	* @throws HibernateException
	* @version 1.0
	* @author jiaquan.hu
	 * @throws HsCloudException 
	* @update 2012-3-31 上午10:07:26
	*/
	public void save(ServiceCatalog serviceCatalog) throws HibernateException, HsCloudException;

	/**
	* @title: delete
	* @description 根据id删除service catalog
	* @param id 设定文件
	* @return void 返回类型
	* @throws
	* @version 1.0
	* @author jiaquan.hu
	* @update 2012-3-31 上午10:07:53
	*/
	public void delete(int id);

	/**
	* @title: getAll
	* @description 获取所有service catalog 信息
	* @return 设定文件
	* @return List<ServiceCatalog> 返回类型
	* @throws
	* @version 1.0
	* @author jiaquan.hu
	* @update 2012-3-31 上午10:08:07
	*/
	public List<ServiceCatalog> getAll(List<Sort> sorts,String userLevel,Long domainId,Long zoneGroupId);



	/**
	* @title: page
	* @description 获取service catalog列表（分页）
	* @param page
	* @return 设定文件
	* @return Page<ServiceCatalog> 返回类型
	* @throws
	* @version 1.0
	* @author jiaquan.hu
	* @update 2012-3-31 上午10:08:58
	*/
	public Page<ServiceCatalog> page(Page<ServiceCatalog> page,ServiceCatalog serviceCatalog,List<Sort> sortList,Long brandId,Long zoneId,Long domainId);
	/**
	 * @param page
	 * @param sortList  排序方式
	 * @param domainId  分平台
	 * @param userLevel 用户品牌code
	 * @return
	 * 分页获取套餐详情
	 */
	public Page<ServiceCatalog> getScByPage(Page<ServiceCatalog> page,List<Sort> sortList,Long domainId,String  userLevel);

	/**
	* @title: getByName
	* @description 用一句话说明这个方法做什么
	* @param name
	* @return 设定文件
	* @return ServiceCatalog 返回类型
	* @throws
	* @version 1.0
	* @author jiaquan.hu
	* @update 2012-4-13 上午10:51:19
	*/
	public ServiceCatalog getByName(String name);
	
	/**
	* @title: getOs
	* @description 用一句话说明这个方法做什么
	* @param serviceCatalogId
	* @return 设定文件
	* @return Os 返回类型
	* @throws
	* @version 1.0
	* @author jiaquan.hu
	* @update 2012-5-15 下午2:02:22
	*/
	public Os getOs(int serviceCatalogId);
	/**
	 * <审批套餐> 
	* <功能详细描述> 
	* @param id 
	* @see [类、类#方法、类#成员]
	 */
	public void approve(int id);
	/**
	 * <仅试用套餐> 
	 * <功能详细描述> 
	 * @param id 
	 * @see [类、类#方法、类#成员]
	 */
	public void onlyTrySC(int id);
	/**
	 * <根据套餐计费类型Id获取实体Bean> 
	* <功能详细描述> 
	* @param id
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public ScFeeType getfeeTypeById(Long id,int scId)throws HsCloudException;
	/**
	 * <根据套餐计费时长获取实体Bean> 
	* <功能详细描述> 
	* @param id
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public ScFeeType getfeeTypeByPeriod(String period,int scId)throws HsCloudException;
	
	/**
	 * <根据套餐Id获取绑定在其上的操作系统> 
	* <功能详细描述> 
	* @param scId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<Os> getOsListByScId(int scId)throws HsCloudException;
	/**
	 * <根据套餐Id获取绑定在其上的操作系统> 
	* <功能详细描述> 
	* @param scId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<ExtDisk> getExtDiskListByScId(int scId)throws HsCloudException;
	/**
	 * <根据套餐id获取套餐对应的计费规则> 
	* <功能详细描述> 
	* @param scId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<ScFeeTypeVo> getScFeeTypeByScId(int scId)throws HsCloudException;
	/**
	 * <根据orderItemId获取套餐的计费规则信息> 
	* <功能详细描述> 
	* @param orderItemId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<ScFeeTypeVo> getScFeeTypeByOrderItemId(long orderItemId)throws HsCloudException;
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
	   * <根据referenceId获取vm用户，并根据用户获取用户相关品牌，再跟据品牌获取品牌关联套餐> 
	  * <功能详细描述> 
	  * @param referenceId
	  * @return
	  * @throws HsCloudException 
	  * @see [类、类#方法、类#成员]
	   */
	  public List<SCVo> getRelatedScByReferenceId(long referenceId)throws HsCloudException;
	  /**
	   * <根据套餐ID查询对应的资源隔离限制参数> 
	  * <功能详细描述> 
	  * @param scId
	  * @return
	  * @throws HsCloudException 
	  * @see [类、类#方法、类#成员]
	   */
	  public ScIsolationConfig getScIsolationConfigByScId(int scId) throws HsCloudException;
	  /**
	   * <根据code查询套餐> 
	  * <功能详细描述> 
	  * @param code
	  * @return 
	  * @see [类、类#方法、类#成员]
	   */
	  public ServiceCatalog getByCode(String scCode,String domainCode,String brandCode) throws HsCloudException;
	  /**
	   * <根据domain id 查询domain code>
	   * @param domainList
	   * @return
	   */
	  public List<DomainVO> getDomainCodebyId(List<Domain> domainList) throws HsCloudException;
	  /**
	   * <根据套餐查询套餐code>
	   * @param serviceCatalog
	   * @return
	   * @throws HsCloudException
	   */
	  
	  public boolean hasServiceCatalogCode(ServiceCatalog serviceCatalog) throws HsCloudException;

}
