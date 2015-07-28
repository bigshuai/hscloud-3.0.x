package com.hisoft.hscloud.crm.usermanager.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.dao.UserCompanyDao;
import com.hisoft.hscloud.crm.usermanager.entity.UserCompany;

@Repository
public class UserCompanyDaoImpl extends HibernateDao<UserCompany, Long> implements UserCompanyDao {

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	public UserCompany findUniqueBy(String propertyName, Object value) {
		
		try {	
			
			if(logger.isDebugEnabled()){
				logger.debug("enter UserCompanyDaoImpl findUniqueBy method.");
				logger.debug("propertyName:"+propertyName);
				logger.debug("value:"+value);
			}
			UserCompany userCompanys = super.findUniqueBy(propertyName, value);
			if(logger.isDebugEnabled()){
				logger.debug("exit UserCompanyDaoImpl findUniqueBy method");
			}
			return userCompanys;
		
		} catch (Exception e) {
			throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
		}
		
	}
	
	@Override
	public List<UserCompany> findBy(String propertyName, Object value){
		
		try {	
			
			if(logger.isDebugEnabled()){
				logger.debug("enter UserCompanyDaoImpl findBy method.");
				logger.debug("propertyName:"+propertyName);
				logger.debug("value:"+value);
			}
			List<UserCompany> userCompanys = super.findBy(propertyName, value);
			if(logger.isDebugEnabled()){
				logger.debug("exit UserCompanyDaoImpl findBy method");
			}
			return userCompanys;
			
		} catch (Exception e) {
			throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
		}
			
	}
	
	@Override
	public void save(UserCompany userCompany){
		
		try {
			
			if(logger.isDebugEnabled()){
				logger.debug("enter UserCompanyDaoImpl save method.");
			}
			super.save(userCompany);
			if(logger.isDebugEnabled()){
				logger.debug("exit UserCompanyDaoImpl save method");
			}
			
		} catch (Exception e) {
			throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
		}
		
	}

	@Override
	public void delete(long id) {
		
		try {	
			
			if(logger.isDebugEnabled()){
				logger.debug("enter UserCompanyDaoImpl delete method.");
				logger.debug("id:"+id);
			}
			super.delete(id);
			if(logger.isDebugEnabled()){
				logger.debug("exit UserCompanyDaoImpl delete method");
			}
			
		} catch (Exception e) {
			throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
		}
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public UserCompany findUnique(String hql, Object... values){

		try {
			if(logger.isDebugEnabled()){
				logger.debug("enter UserCompanyDaoImpl save method.");
				logger.debug("hql:"+hql);
				logger.debug("values:"+values);
			}
			UserCompany userCompany =  super.findUnique(hql, values);
			if(logger.isDebugEnabled()){
				logger.debug("exit UserCompanyDaoImpl save method");
			}
			return userCompany;
		} catch (Exception e) {
			throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
		}
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<UserCompany> find(String hql, Object...  values){
		
		try {		
			if(logger.isDebugEnabled()){
				logger.debug("enter UserCompanyDaoImpl save method.");
				logger.debug("hql:"+hql);
				logger.debug("values:"+values);
			}
			List<UserCompany> userCompanys = super.find(hql, values);
			if(logger.isDebugEnabled()){
				logger.debug("exit UserCompanyDaoImpl save method");
			}
			return userCompanys;
		} catch (Exception e) {
			throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
		}
		
	}
	
	
	
	


}
