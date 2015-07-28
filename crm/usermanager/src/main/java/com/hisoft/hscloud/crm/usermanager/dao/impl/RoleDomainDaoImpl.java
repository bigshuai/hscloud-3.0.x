/**
 * 
 */
package com.hisoft.hscloud.crm.usermanager.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.crm.usermanager.dao.RoleDomainDao;
import com.hisoft.hscloud.crm.usermanager.entity.RoleDomain;


/**
 * @author lihonglei
 *
 */
@Repository
public class RoleDomainDaoImpl extends HibernateDao<RoleDomain, Long> implements RoleDomainDao {

    /**
     * 查询RoleDomain列表
     * @see com.hisoft.hscloud.crm.usermanager.dao.RoleDomainDao#findRoleDomainList(long)
     */
    @Override
    public List<RoleDomain> findRoleDomainList(long roleId) {
        return this.findBy("roleId", roleId);
    }

    /**
     * 批量删除角色分平台权限
     * @see com.hisoft.hscloud.crm.usermanager.dao.RoleDomainDao#batchDeleteRoleDomain(java.lang.String[], long)
     */
    @Override
    public void batchDeleteRoleDomain(Long[] domainLongArray, long roleId) {
        String hql = "delete RoleDomain where roleId = :roleId and domainId in (:domainLongArray)";
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("roleId", roleId);
        condition.put("domainLongArray", domainLongArray);
        this.batchExecute(hql, condition);
    }

    /**
     * 批量添加
     * @see com.hisoft.hscloud.crm.usermanager.dao.RoleDomainDao#batchAdd(java.lang.Long[], long)
     */
    @Override
    public void batchAdd(Long[] permissionLongArray, long roleId) {
        RoleDomain roleDomain;
        for(Long domainId : permissionLongArray) {
            roleDomain = new RoleDomain();
            roleDomain.setRoleId(roleId);
            roleDomain.setDomainId(domainId);
            this.save(roleDomain);
        }
    }

}
