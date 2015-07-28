package com.hisoft.hscloud.crm.usermanager.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.dao.CompanyInviteDao;
import com.hisoft.hscloud.crm.usermanager.entity.CompanyInvite;

@Repository
public class CompanyInviteDaoImpl  extends HibernateDao<CompanyInvite, Long>  implements CompanyInviteDao {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	public void save(CompanyInvite companyInvite){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter CompanyInviteDaoImpl save method.");
			}
			super.save(companyInvite);
			if(logger.isDebugEnabled()){
				logger.debug("exit CompanyInviteDaoImpl save method");
			}
			
	    } catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}

	@Override
	public CompanyInvite findUniqueBy(String propertyName, Object value) {
		
		try{
			if(logger.isDebugEnabled()){
				logger.debug("enter CompanyInviteDaoImpl findUniqueBy method.");
				logger.debug("propertyName:"+propertyName);
				logger.debug("value:"+value);
			}
			CompanyInvite companyInvite = (CompanyInvite)super.findUniqueBy(propertyName, value);
			if(logger.isDebugEnabled()){
				logger.debug("exit CompanyInviteDaoImpl findUniqueBy method");
			}
			return companyInvite;
			
	    } catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CompanyInvite> find(String hql, Object... values){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter CompanyInviteDaoImpl findUniqueBy method.");
				logger.debug("hql:"+hql);
				logger.debug("values:"+values);
			}
			List<CompanyInvite> companyInvites = super.find(hql, values);
			
			if(logger.isDebugEnabled()){
				logger.debug("exit CompanyInviteDaoImpl findUniqueBy method");
			}
			return companyInvites;
			
	    } catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public CompanyInvite findUnique(String hql, Object... values){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter CompanyInviteDaoImpl findUnique method.");
				logger.debug("hql:"+hql);
				logger.debug("values:"+values);
			}
			
			CompanyInvite companyInvite =  (CompanyInvite)super.findUnique(hql, values);
			
			if(logger.isDebugEnabled()){
				logger.debug("exit CompanyInviteDaoImpl findUnique method");
			}
			return companyInvite;
			
	    } catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}

	
}
