package com.hisoft.hscloud.crm.usermanager.dao.impl;


import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.dao.CountryDao;
import com.hisoft.hscloud.crm.usermanager.entity.Country;

@Repository
public class CountryDaoImpl extends HibernateDao<Country, Long> implements CountryDao{
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public List<Country> getAll(String orderBy, boolean isAsc){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter CountryDaoImpl getAll method.");
				logger.debug("orderBy:"+orderBy);
				logger.debug("isAsc:"+isAsc);
			}
			List<Country> Countrys = super.getAll(orderBy,isAsc);
			if(logger.isDebugEnabled()){
				logger.debug("exit CountryDaoImpl getAll method");
			}
			return Countrys;
			
	    } catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}

}
