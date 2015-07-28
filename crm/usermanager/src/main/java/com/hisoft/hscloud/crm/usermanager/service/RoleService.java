package com.hisoft.hscloud.crm.usermanager.service;

import java.util.List;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.crm.usermanager.entity.AdminRole;
import com.hisoft.hscloud.crm.usermanager.entity.Role;
import com.hisoft.hscloud.crm.usermanager.entity.UserRole;

public interface RoleService {
	
	public boolean hasRole(long userId);
	
	/**
	 * 针对前台用户 指企业用户
	 * @param userId
	 * @return
	 */
	public boolean isSpecialUser(long userId);
	
	/**
	 * 针对hu后台用户 指和超级用户具有相同权限的管理员
	 * @param adminId
	 * @return
	 */
	public boolean isSpecialAdmin(long adminId);
	
	/**
	 * 通过roleId删除Role，包括user_role,role_permission,admin_role表的相关记录
	 * @param roleId
	 */
	public void deleteRole(long roleId);
	
	
	/**
	 * 通过roleName分页查询
	 * @param page
	 * @param roleName
	 * @param hql
	 * @return
	 */
	public Page<Role> findPage(Page<Role> page,String roleName);
	
	/**
	 * 通过管理员id查询其 角色（目前一个管理员只有一 个角色）
	 * @param adminId
	 * @return
	 */
	public Role getRole(long adminId);
	
	public Role getRoleById(long roleId);
	
	public void saveRole(Role role);


    /** <一句话功能简述> 
    * <功能详细描述> 
    * @return 
    * @see [类、类#方法、类#成员] 
    */
    public List<Role> getAllRole();
    
    public boolean findRoleName(Role role);

	public List<AdminRole> hasAdminRole(long roleId);
	
	public List<Role> getRoleByPermission(List<Long> ids);
	
	/**
	 * 给前台用户添加企业角色
	 * @param userId
	 * @return
	 */
	public long addEntUserRole(long userId);
}
