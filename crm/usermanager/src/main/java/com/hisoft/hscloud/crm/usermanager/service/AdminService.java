package com.hisoft.hscloud.crm.usermanager.service;

import java.util.List;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.entity.AdminRole;
import com.hisoft.hscloud.crm.usermanager.vo.AdminVO;

public interface AdminService {
	
	
	/**
	 * 管理员用户根据邮箱，密码登陆。
	 * @param email
	 * @param password
	 * @return
	 */
	public Admin loginAdminByEmail(String email,String password);
	
	/**
	 * 通过roleId，分页查询Admin
	 * @param roleId
	 * @return
	 */
	public Page<Admin> pageAdminInRole(Page<Admin> page,long roleId);
	
	/**
	 * 添加管理员，并添加角色。
	 * @param admin
	 * @param roleId
	 */
	public void addAdmin(Admin admin,Long roleId);
	
	/**
	 * <分页查询出所有管理员user> 
	* <功能详细描述> 
	* @param query
	* @param start
	* @param limit
	* @param page
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public Page<AdminVO> getAllAdminUser(List<Sort> sorts,String query,int start,int limit,int page,String type);
	/**
	 * <修改管理员信息> 
	* <功能详细描述> 
	* @param admin 
	* @see [类、类#方法、类#成员]
	 */
	public void modifyAdmin(Admin admin);
	/**
	 * <保存admin 和 role 的关联信息> 
	* <功能详细描述> 
	* @param amdinRole 
	* @see [类、类#方法、类#成员]
	 */
	public void saveAdminRole(AdminRole amdinRole);
	/**
	 * <根据主键Id查询实体Bean:Admin> 
	* <功能详细描述> 
	* @param adminId
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public Admin getAdminById(long adminId);
	/**
	 * <根据主键Id查询实体Bean:AdminRole> 
	* <功能详细描述> 
	* @param adminRoleId
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public AdminRole getAdminRoleById(long adminRoleId);
	/**
	 * <根据管理员邮箱查询管理员实体bean> 
	* <功能详细描述> 
	* @param email
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public Admin getAdminByEmail(String email);
	/**
	 * <根据物理Id删除hc_admin_role表相应的数据> 
	* <功能详细描述> 
	* @param adminRoleId 
	* @see [类、类#方法、类#成员]
	 */
	public void deleteAdminRoleById(Long adminRoleId);
	public void enableAdmin(long adminId);
}
