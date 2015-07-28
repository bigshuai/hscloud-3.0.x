package com.hisoft.hscloud.crm.usermanager.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.dao.UserGroupCompanyDao;
import com.hisoft.hscloud.crm.usermanager.entity.UserGroupCompany;

@Repository
public class UserGroupCompanyDaoImp extends HibernateDao<UserGroupCompany, Long>implements UserGroupCompanyDao {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	public List<UserGroupCompany> findBy(String propertyName, Object value){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter UserGroupCompanyDaoImp findBy method.");
				logger.debug("propertyName:"+propertyName);
				logger.debug("value:"+value);
			}
			List<UserGroupCompany> UserGroupCompanys = super.findBy(propertyName, value);
			if(logger.isDebugEnabled()){
				logger.debug("exit UserGroupCompanyDaoImp findBy method");
			}
			return UserGroupCompanys;
			
		} catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}
	
	@Override
	public UserGroupCompany findUniqueBy(String propertyName, Object value){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter UserGroupCompanyDaoImp findUniqueBy method.");
				logger.debug("propertyName:"+propertyName);
				logger.debug("value:"+value);
			}
			UserGroupCompany userGroupCompany = (UserGroupCompany)super.findUniqueBy(propertyName, value);
			if(logger.isDebugEnabled()){
				logger.debug("exit UserGroupCompanyDaoImp findUniqueBy method");
			}
			return userGroupCompany;
			
		} catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}
	
	@Override
	public void save(UserGroupCompany userGroupCompany){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter UserGroupCompanyDaoImp save method.");
				logger.debug("userGroupCompany:"+userGroupCompany);
			}
			super.save(userGroupCompany);
			if(logger.isDebugEnabled()){
				logger.debug("exit UserGroupCompanyDaoImp save method");
			}
			
		} catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}
	
}
