package com.hisoft.hscloud.crm.usermanager.dao;

import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.vo.AdminVO;


public interface AdminDao {
	
	/**
	 * 后台用户通过邮箱和密码登陆
	 * @param email
	 * @param password
	 * @return
	 */
	public Admin LoginByEmail(String email, String password);
	
	/**
	 * 通过hql查询
	 * @return
	 */
	public Page<Admin> findPage(Page<Admin> page, String hql,Map<String,?> map);
	
	/**
	 * 添加admin
	 * @param admin
	 */
	public void save(Admin admin);
	
	/**
	 * 通过id查询
	 * @param adminId
	 * @return
	 */
	public Admin get(long adminId);
	
	/**
	 * 删除管理员
	 * @param adminId
	 */
	public void delete(long adminId);
	
	public List<AdminVO> getAllAdminUser(List<Sort> sorts,String query,int start,int limit,int page,String type);
	
	public long getAdminCount(String query,String type);
	
	public Admin getAdminByEmail(String email);

}
