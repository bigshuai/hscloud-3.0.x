/**
 * 
 */
package com.hisoft.hscloud.crm.usermanager.service;

import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.entity.UserBrand;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.vo.UserBrandVO;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.crm.usermanager.vo.DomainVO;

/**
 * 分平台服务
 * @author lihonglei
 *
 */
public interface DomainService {

    /**
     * 查询分平台页面(用于权限分配)
     * @param page
     * @param roleId
     * @return
     */
    public Page<Map<String, Object>> findDomainList(Page<Map<String, Object>> page,
            long roleId);

    /**
     * 查询分平台页面
     * @param page
     * @return
     */
    public Page<Domain> findDomainPage(Page<Domain> page, String query);

    /**
     * 分平台赋权
     * @param permissionValue
     * @param resourceValue
     * @param roleId
     */
    public void editDomainPermission(String permissionValue,
            String resourceValue, long roleId);

    /**
     * 获取所有分平台
     * @return
     */
    public List<Domain> getAllDomain();

    /**
     * 编辑分平台
     * @param domainVO
     * @return
     */
    public String editDomain(DomainVO domainVO, long adminId);

    /**
     * 根据code获得分平台,查询不到code对应分平台返回空
     * @param code
     * @return
     */
    public Domain getDomainByCode(String code);

    /**
     * 根据id查询分平台
     * @param domainId
     * @return
     */
    public Domain getDomainById(long domainId);

    /**
     * 修改分平台状态
     * @param domainId
     * @param adminId
     * @param status
     */
    public void updateStatusDomain(long domainId, long adminId, String status);
    
    /**
     * 查询该管理员所属域
     * @param adminId
     * @return
     */
    public List<Domain> getDomainByAdmin(long adminId);
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
	/**
     * <查询分平台下的品牌> 
    * <功能详细描述> 
    * @param abbreviation
    * @return
    * @throws HsCloudException 
    * @see [类、类#方法、类#成员]
     */
	public List<UserBrandVO> getRelatedBrand(String abbreviation) throws HsCloudException;

}
