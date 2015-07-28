package com.hisoft.hscloud.crm.usermanager.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hisoft.hscloud.crm.usermanager.dao.CompanyDao;
import com.hisoft.hscloud.crm.usermanager.dao.UserCompanyDao;
import com.hisoft.hscloud.crm.usermanager.entity.Company;
import com.hisoft.hscloud.crm.usermanager.entity.UserCompany;
import com.hisoft.hscloud.crm.usermanager.service.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	private UserCompanyDao userCompanyDao;
	
	@Autowired
	private CompanyDao companyDao;
	
	@Override
	@Transactional
	public Company getCompany(long companyId) {
		Company company = companyDao.findUniqueBy("id",companyId);
		return company;
	}

	@Override
	@Transactional
	public Company getCompanyByUserId(long userId) {
		
		UserCompany userCompany = userCompanyDao.findUniqueBy("userId", userId);
		Company company = companyDao.findUniqueBy("id", userCompany.getCompanyId());
		return company;
		
	}

	@Override
	public void createCompany(Company company,long adminId) {
		companyDao.save(company);
//		UserCompany userCompany = new UserCompany();
//		userCompany.setCompanyId(company.getId());
//		userCompany.setUserId(adminId);
		
	}

	@Override
	public void createCompany(Company company) {
		companyDao.save(company);
	}

	@Override
	public void saveUserCompany(long companyId, long userId) {

		UserCompany userCompany = new UserCompany();
		userCompany.setCompanyId(companyId);
		userCompany.setUserId(userId);
		userCompanyDao.save(userCompany);
	}

	@Override
	public void updateCompany(Company company) {
		companyDao.save(company);
		
	}

	@Override
	public List<Company> getJoinedCompanys(long userId) {
		String hql1 = "from UserCompany uc where uc.userId=?";
		List<Long> cIds = new ArrayList<Long>();
		List<UserCompany> ucs =  userCompanyDao.find(hql1, userId);
		if(ucs.isEmpty()){
		  return new ArrayList<Company>();
		}
		for (UserCompany userCompany : ucs) {
			cIds.add(userCompany.getCompanyId());
		}
		Map<String,List<Long>> map = new HashMap<String, List<Long>>();
		map.put("ids", cIds);
		String hql2 = "from Company c where c.id in(:ids)";
		List<Company> cs = companyDao.findCompanys(hql2, map);
		return cs;
	}

	@Override
	public List<Long> getMembers(long id) {
		String hql1 = "from UserCompany uc where uc.userId=?";
		UserCompany uc = userCompanyDao.findUnique(hql1, id);
		String hql2 = "from UserCompany uc where uc.companyId=? and uc.userId !=?";
		List<UserCompany> ucs = userCompanyDao.find(hql2, uc.getCompanyId(),id);
		List<Long> uids = new ArrayList<Long>();
		for (UserCompany userCompany : ucs) {
			uids.add(userCompany.getUserId());
		}
		return uids;
	}

}
