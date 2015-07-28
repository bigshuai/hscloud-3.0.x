package com.hisoft.hscloud.crm.usermanager.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.dao.CompanyDao;
import com.hisoft.hscloud.crm.usermanager.entity.Company;

@Repository
public class CompanyDaoImpl extends HibernateDao<Company, Long> implements CompanyDao {

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	public Company findUniqueBy(String propertyName, Object value) {
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter CompanyDaoImpl findUniqueBy method.");
				logger.debug("propertyName:"+propertyName);
				logger.debug("value:"+value);
			}
			
			Company company = super.findUniqueBy(propertyName, value);
			
			if(logger.isDebugEnabled()){
				logger.debug("exit CompanyDaoImpl findUniqueBy method");
			}
			return company;
			
	    } catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}

	
	@Override
	public void save(Company company){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter CompanyDaoImpl save method.");
				logger.debug("company:"+company);
			}
			
			super.save(company);
			
			if(logger.isDebugEnabled()){
				logger.debug("enter CompanyDaoImpl save method.");
			}
			
	    } catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}


	@Override
	public List<Company> findCompanys(String hql, Map<String, ?> map) {
		return super.find(hql, map);
	}
	


}
