package com.hisoft.hscloud.crm.usermanager.dao.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.dao.UserUserGroupDao;
import com.hisoft.hscloud.crm.usermanager.entity.UserUserGroup;

@Repository
public class UserUserGroupDaoImpl extends HibernateDao<UserUserGroup, Long> implements UserUserGroupDao {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	public List<UserUserGroup> findBy(String propertyName, Object value){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter UserUserGroupDaoImpl findBy method.");
			}
			List<UserUserGroup> userUserGroups = (List<UserUserGroup>)super.findBy(propertyName, value);
			if(logger.isDebugEnabled()){
				logger.debug("userUserGroups:"+userUserGroups);
				logger.debug("exit UserUserGroupDaoImpl findBy method");
			}
			return userUserGroups;
			
		} catch (Exception e) {
			throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
		}
		
	}
	
	@Override
	public void delete(String hql, Map<String,Object> map) {
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter UserUserGroupDaoImpl delete method.");
				logger.debug("hql:"+hql);
				logger.debug("map:"+map);
			}
			Query query = (Query)super.createQuery(hql, map);
		    Set<String> keys = map.keySet();
		    for (String key : keys) {
		    	  if(map.get(key) instanceof Collection){
		    		  query.setParameterList(key, (Collection)map.get(key));
		    	  }else{
		    		  query.setParameter(key, map.get(key));
		    	  }
			}
			query.executeUpdate();
			if(logger.isDebugEnabled()){
				logger.debug("exit UserUserGroupDaoImpl delete method");
			}
			
		} catch (Exception e) {
			throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
		}
		
	}

	

}
