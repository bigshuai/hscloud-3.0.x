/**
 * 
 */
package com.hisoft.hscloud.crm.usermanager.dao;


import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.entity.UserBrand;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.vo.UserBrandVO;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.crm.usermanager.vo.DomainVO;

/**
 * 分平台数据接口
 * @author lihonglei
 *
 */
public interface DomainDao {
    /**
     * 编辑分平台
     * @param domain
     * @return
     */
    public long edit(Domain domain);
    
    /**
     * 根据id查询分平台
     * @param domainId
     * @return
     */
    public Domain getDomainById(long domainId);

    /**
     * 查询分平台页
     * @param page
     * @param query 
     * @return
     */
    public Page<Domain> findDomainPage(Page<Domain> page, String query);

    /**
     * 获取所有分平台
     * @return
     */
    public List<Domain> getAllDomain();

    /**
     * 根据code获得分平台,查询不到code对应分平台返回空
     * @param code
     * @return
     */
    public Domain getDomainByCode(String code);

    /**
     * 根据全名查分平台
     * @param name
     * @return
     */
    public List<Domain> findDomainByName(String name);

    /**
     * 查询有效分平台页
     * @param page
     * @param query
     * @return
     */
    public Page<Domain> findValidDomainPage(Page<Domain> page, String query);
    
    
    public List<Domain> findBySQL(String sql,Map<String, ?> map);

    /**
     * 查询分平台是否有已存在的code，名称，简称
     * @param domain
     * @return
     */
    public Domain getDomainByCondition(DomainVO domain);
    /**
     * <查询已经关联的用户品牌> 
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
     * <查询未关联的用户品牌> 
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
     * <查询分平台下的品牌> 
    * <功能详细描述> 
    * @param domainId
    * @return
    * @throws HsCloudException 
    * @see [类、类#方法、类#成员]
     */
    public List<UserBrandVO> getRelatedBrandByDomainId(long domainId) throws HsCloudException;

	public Long getBrandIdByBrandAbbreviation(String abbreviation) throws HsCloudException;

}