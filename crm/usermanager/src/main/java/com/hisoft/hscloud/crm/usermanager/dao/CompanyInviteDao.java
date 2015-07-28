package com.hisoft.hscloud.crm.usermanager.dao;

import java.util.List;

import com.hisoft.hscloud.crm.usermanager.entity.CompanyInvite;

public interface CompanyInviteDao {
	
	public void save(CompanyInvite companyInvite);
	
	public CompanyInvite findUniqueBy(String propertyName,Object value);
	
	public CompanyInvite findUnique(String hql, Object... values);
	
	public List<CompanyInvite> find(String hql, Object... values);

}
