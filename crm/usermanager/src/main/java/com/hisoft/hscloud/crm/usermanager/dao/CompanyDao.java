package com.hisoft.hscloud.crm.usermanager.dao;

import java.util.List;
import java.util.Map;

import com.hisoft.hscloud.crm.usermanager.entity.Company;

public interface CompanyDao {
	
	public Company findUniqueBy(String propertyName, Object value);
	
	public void save(Company company);
	
	public List<Company> findCompanys(String hql, Map<String,?> map);

}
