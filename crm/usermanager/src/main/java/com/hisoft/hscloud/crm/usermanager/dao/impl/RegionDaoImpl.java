package com.hisoft.hscloud.crm.usermanager.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.dao.RegionDao;
import com.hisoft.hscloud.crm.usermanager.entity.Region;

@Repository
public class RegionDaoImpl  extends HibernateDao<Region, Long>  implements RegionDao {

	private Logger logger = Logger.getLogger(this.getClass());
	
	public List<Region> findBy(String propertyName, Object value){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter RegionDaoImpl findBy method.");
				logger.debug("propertyName:"+propertyName);
				logger.debug("value:"+value);
			}
			List<Region> regions = super.findBy(propertyName, value);
			if(logger.isDebugEnabled()){
				logger.debug("exit RegionDaoImpl findBy method");
			}
			return regions;
		
	    } catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Region> find(String hql, Object... values){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter RegionDaoImpl findBy method.");
				logger.debug("hql:"+hql);
				logger.debug("values:"+values);
			}
			List<Region> regions = super.find(hql, values);
			if(logger.isDebugEnabled()){
				logger.debug("exit RegionDaoImpl findBy method");
			}
			return regions;
		
	    } catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}
	
}
