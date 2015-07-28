package com.hisoft.hscloud.crm.usermanager.service;

import java.util.List;

import com.hisoft.hscloud.crm.usermanager.entity.Company;

public interface CompanyService {
	
	public Company getCompanyByUserId(long userId);
	
	public Company getCompany(long companyId);
	
	public void createCompany(Company company);
	
	public void createCompany(Company company,long adminId);
	
	public void saveUserCompany(long companyId,long userId);
	
	public void updateCompany(Company company);
	
	public List<Company> getJoinedCompanys(long userId);

	/**
	 *查询企业成员
	 * @param id
	 */
	public List<Long> getMembers(long id);
	

}
