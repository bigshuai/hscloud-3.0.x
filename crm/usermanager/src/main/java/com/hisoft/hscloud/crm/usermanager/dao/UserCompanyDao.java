package com.hisoft.hscloud.crm.usermanager.dao;

import java.util.List;

import com.hisoft.hscloud.crm.usermanager.entity.UserCompany;

public interface UserCompanyDao {
	
	/**
	 * 通过属性 查询唯一值
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public UserCompany findUniqueBy(String propertyName, Object value);
	
	/**
	 * 通过属性 查询List
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public List<UserCompany> findBy(String propertyName, Object value);
	
	public void save(UserCompany userCompany);
	
	public void delete(long id);

	public UserCompany findUnique(String hql, Object... values);
	
	public List<UserCompany> find(String hql, Object...  values);
}
