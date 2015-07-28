package com.hisoft.hscloud.crm.usermanager.dao;

import java.util.List;

import com.hisoft.hscloud.crm.usermanager.entity.UserGroupCompany;

public interface UserGroupCompanyDao {
	
	public List<UserGroupCompany> findBy(String propertyName, Object value);
	
	public UserGroupCompany findUniqueBy(String propertyName, Object value);
	
	public void save(UserGroupCompany userGroupCompany);

}
