package com.hisoft.hscloud.bss.sla.sc.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.sc.entity.ScIsolationConfig;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceCatalog;
import com.hisoft.hscloud.bss.sla.sc.vo.SCVo;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.crm.usermanager.vo.DomainVO;

/**
* @description service catalog的数据库处理层
* @version 1.0
* @author jiaquan.hu
* @update 2012-3-31 上午9:57:48
*/
public interface ServiceCatalogDao{
  /**
   * <根据物理主键获取bean> 
  * <功能详细描述> 
  * @param propertyName
  * @param id
  * @return 
  * @see [类、类#方法、类#成员]
   */
  public ServiceCatalog findScById(int id)throws HsCloudException;
  /**
   * <获取套餐创建日期> 
  * <功能详细描述> 
  * @param propertyName
  * @param idh
  * @return 
  * @see [类、类#方法、类#成员]
   */
  public Date getCreateTime(int id)throws HsCloudException;//findUni
  /**
   * <保存套餐> 
  * <功能详细描述> 
  * @param sc 
  * @see [类、类#方法、类#成员]
   */
  public void save(ServiceCatalog sc)throws HsCloudException;
  /**
   * <根据查询获取套餐列表信息> 
  * <功能详细描述> 
  * @param hql
  * @param params
  * @return 
  * @see [类、类#方法、类#成员]
   */
  public List<ServiceCatalog> getServiceCatalogList(String hql,Map<String,Object> params)throws HsCloudException;
  /**
   * <分页获取套餐信息> 
  * <功能详细描述> 
  * @param page
  * @param hql
  * @param params
  * @return 
  * @see [类、类#方法、类#成员]
   */
  public Page<ServiceCatalog> findByPage(Page<ServiceCatalog> page,String hql,Map<String,Object> params)throws HsCloudException;
  /**
   * <获取与brand关联的所有套餐> 
  * <功能详细描述> 
  * @param brandId
  * @return
  * @throws HsCloudException 
  * @see [类、类#方法、类#成员]
   */
  public List<SCVo> getRelatedSCByBrandId(long brandId,int limit,int pageNo,String query)throws HsCloudException;
  /**
   * <获取与brand没有关联的所有套餐> 
  * <功能详细描述> 
  * @param brandId
  * @return
  * @throws HsCloudException 
  * @see [类、类#方法、类#成员]
   */
  public List<SCVo> getUnRelatedScByBrandId(long brandId,int limit,int pageNo,String query)throws HsCloudException;
  /**
   * <获取与brand关联的所有套餐> 
  * <功能详细描述> 
  * @param brandId
  * @return
  * @throws HsCloudException 
  * @see [类、类#方法、类#成员]
   */
  public int  getRelatedSCcountByBrandId(long brandId,int limit,int pageNo,String query)throws HsCloudException;
  /**
   * <获取与brand没有关联的所有套餐> 
  * <功能详细描述> 
  * @param brandId
  * @return
  * @throws HsCloudException 
  * @see [类、类#方法、类#成员]
   */
  public int getUnRelatedScCountByBrandId(long brandId,int limit,int pageNo,String query)throws HsCloudException;
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
   * <根据套餐code和domain code查询套餐>
   * @param scCode
   * @param domainCode
   * @return
   * @throws HsCloudException
   */
  
  public ServiceCatalog getByCode (String scCode,String domainCode,String brandCode) throws HsCloudException;
  /**
   * <根据domain id 查询domain code>
   * @param domainList
   * @return
   */
  public List<DomainVO> getDomainCodebyId(List<Domain> domainList)throws HsCloudException;
  /**
   * <通过name获取套餐code>
   * @param name
   * @param serviceCatalog
   * @return
   * @throws HsCloudException
   */
  public List<ServiceCatalog> getServiceCatalogCodeByCondtion(ServiceCatalog serviceCatalog) throws HsCloudException;
  
}
