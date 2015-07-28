package com.hisoft.hscloud.crm.usermanager.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.crm.usermanager.dao.AdminRoleDao;
import com.hisoft.hscloud.crm.usermanager.dao.RoleDao;
import com.hisoft.hscloud.crm.usermanager.dao.RolePermissionDao;
import com.hisoft.hscloud.crm.usermanager.dao.UserRoleDao;

import com.hisoft.hscloud.crm.usermanager.entity.AdminRole;
import com.hisoft.hscloud.crm.usermanager.entity.Role;
import com.hisoft.hscloud.crm.usermanager.entity.UserRole;
import com.hisoft.hscloud.crm.usermanager.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private AdminRoleDao adminRoleDao;

    @Autowired
    private RolePermissionDao rolePermissionDao;
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasRole(long userId) {

        List<UserRole> userRoles = userRoleDao.findBy("userId", userId);
        return userRoles.isEmpty() ? false : true;

    }
    
    
	@Override
	@Transactional(readOnly = true)
	public boolean isSpecialUser(long userId) {
		
		String sql = "select  hr.* from hc_role hr left join hc_user_role hur on hr.id=hur.roleId where hr.`code`='Role_Enterprise'  and hur.userId=(:userId)";
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		return roleDao.findBySQL(sql, map).isEmpty()?false:true;
		
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isSpecialAdmin(long adminId) {
		
		String sql ="select hr.* from hc_role hr left join hc_admin_role har on hr.id=har.roleId where hr.`code`='Role_SystemAdmin' and har.adminId=(:adminId)";
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("adminId", adminId);
		List<Role> roles = roleDao.findBySQL(sql, map);
		if(!roles.isEmpty()){
			return true;
		}else{
			return false;
		}
		
	}

    @Override
    @Transactional
    public void deleteRole(long roleId) {

        roleDao.delete(roleId);
        
        String rolePermissionHQL = "delete RolePermission rp where rp.roleId=?";
        rolePermissionDao.delete(rolePermissionHQL, roleId);
        /*String userRoleHQL = "delete UserRole ur where ur.roleId=?";
        userRoleDao.delete(userRoleHQL, roleId);
        String adminRoleHQL = "delete AdminRole ar where ar.roleId=?";
        adminRoleDao.delete(adminRoleHQL, roleId);
        */

    }

    @Override
    @Transactional(readOnly = true)
    public Page<Role> findPage(Page<Role> page, String roleName) {

        StringBuffer sb = new StringBuffer();
        sb.append("from Role r where r.status = 0 ");
        if(!StringUtils.isBlank(roleName)) {
            sb.append("and r.name like '%");
            sb.append(roleName);
            sb.append("%'");
        }
        sb.append(" order by id desc");
        String hql = sb.toString();
        Page<Role> roles = roleDao.findPage(page, hql);
        return roles;

    }

    @Override
    @Transactional(readOnly = true)
    public Role getRole(long adminId) {

        String adminRoleHQL = "from AdminRole ar where ar.adminId=?";
        AdminRole adminRole = adminRoleDao.findUnique(adminRoleHQL, adminId);
        if(adminRole == null) {
        	return null;
        }
        Role role = roleDao.get(adminRole.getRoleId());
        return role;

    }

    /**
     * @param roleid
     * @return
     */
    @Override
    public Role getRoleById(long roleId) {
        return roleDao.get(roleId);
    }
    
    @Override
    public List<AdminRole> hasAdminRole(long roleId) {
    	return adminRoleDao.findBy("roleId", roleId);
    }

    /**
     * @param role
     * @param privilegesStr
     */
    @Override
    public void saveRole(Role role) {
        roleDao.save(role);
    }

    
    
    @Override
    public List<Role> getAllRole() {
        return roleDao.findRole(" from Role r where r.status!=1 ",new HashMap<String, Object>());
    }

    /** 
    * @param role 
    */
    @Override
    public boolean findRoleName(Role role) {
        StringBuilder hql = new StringBuilder("from Role where name = :name ");
        if(role.getId()>0) {
            hql.append(" and id != :roleId");
        }
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("name", role.getName());
        values.put("roleId", role.getId());
        List<Role> roleList = roleDao.findRole(hql.toString(), values);
        if(roleList != null && roleList.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

	@Override
	public List<Role> getRoleByPermission(List<Long> ids) {
		return roleDao.findByIds(ids);
	}


	@Override
	public long addEntUserRole(long userId) {
		Role role = roleDao.findUniqueBy("code", "Role_Enterprise");
		UserRole userRole = new UserRole();
		userRole.setRoleId(role.getId());
		userRole.setUserId(userId);
		userRoleDao.save(userRole);
		return userRole.getId();
	}
    
}
