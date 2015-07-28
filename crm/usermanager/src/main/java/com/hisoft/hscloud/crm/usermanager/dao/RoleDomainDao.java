/**
 * 
 */
package com.hisoft.hscloud.crm.usermanager.dao;

import java.util.List;

import com.hisoft.hscloud.crm.usermanager.entity.RoleDomain;

/**
 * 角色分平台接口
 * @author lihonglei
 *
 */
public interface RoleDomainDao {
    /**
     * 查询RoleDomain列表
     * @param roleId
     * @return
     */
    public List<RoleDomain> findRoleDomainList(long roleId);

    /**
     * 批量删除角色分平台权限
     * @param domainArray
     * @param roleId
     */
    public void batchDeleteRoleDomain(Long[] domainLongArray, long roleId);

    /**
     * 批量添加
     * @param permissionLongArray
     * @param roleId
     */
    public void batchAdd(Long[] permissionLongArray, long roleId);
}
