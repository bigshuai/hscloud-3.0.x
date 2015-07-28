/**
 * 
 */
package com.hisoft.hscloud.crm.usermanager.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 角色分平台关联表
 * @author lihonglei
 *
 */
@Entity
@Table(name = "hc_role_domain")
public class RoleDomain {
    // 主健
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    //角色id
    @Column(name = "role_id", nullable = false)
    private long roleId;
    
    //分平台id
    @Column(name = "domain_id", nullable = false)
    private long domainId;
    
    //备注
    @Column(name = "remark", nullable = true)
    private String remark;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public long getDomainId() {
        return domainId;
    }

    public void setDomainId(long domainId) {
        this.domainId = domainId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
