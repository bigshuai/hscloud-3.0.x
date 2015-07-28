package com.hisoft.hscloud.crm.usermanager.dao.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.dao.UserGroupDao;
import com.hisoft.hscloud.crm.usermanager.entity.UserGroup;

@Repository
public class UserGroupDaoImpl extends HibernateDao<UserGroup,Long> implements UserGroupDao {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	public void save(UserGroup userGroup){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter UserGroupDaoImpl save method.");
			}
			super.save(userGroup);
			if(logger.isDebugEnabled()){
				logger.debug("exit UserGroupDaoImpl findBy method");
			}
			
		} catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}
	
	@Override
	public List<UserGroup> findByIds(List<Long> ids){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter UserGroupDaoImpl findByIds method.");
				logger.debug("ids:"+ids);
			}
			List<UserGroup> userGroups = (List<UserGroup>)super.findByIds(ids);
			if(logger.isDebugEnabled()){
				logger.debug("exit UserGroupDaoImpl findByIds method");
			}
			return userGroups;
			
		} catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}
	
	@Override
	public Page<UserGroup> findPage(Page<UserGroup> page, String hql, Object... object){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter UserGroupDaoImpl findPage method.");
				logger.debug("page:"+page);
				logger.debug("hql:"+hql);
				logger.debug("object:"+object);
			}
			Page<UserGroup> userGroups = (Page<UserGroup>)super.findPage(page, hql, object);
			if(logger.isDebugEnabled()){
				logger.debug("exit UserGroupDaoImpl findPage method");
			}
			return userGroups;
			
	    } catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}

	@Override
	public Page<UserGroup> findPage(Page<UserGroup> page, String hql,Map<String, ?> values) {
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter UserGroupDaoImpl findPage method.");
				logger.debug("page:"+page);
				logger.debug("values:"+values);
				logger.debug("hql:"+hql);
			}
			Page<UserGroup> userGroups =  (Page<UserGroup>)super.findPage(page, hql, values);
			if(logger.isDebugEnabled()){
				logger.debug("exit UserGroupDaoImpl findPage method");
			}
			return userGroups;
			
	    } catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
	}
	
	public UserGroup findUniqueBy(String propertyName, Object value){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter UserGroupDaoImpl findUniqueBy method.");
				logger.debug("propertyName:"+propertyName);
				logger.debug("value:"+value);
			}
			UserGroup userGroup =  super.findUniqueBy(propertyName, value);
			if(logger.isDebugEnabled()){
				logger.debug("exit UserGroupDaoImpl findUniqueBy method");
			}
			return userGroup;
		
	   } catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	   }
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<UserGroup> findByHQL(String hql, Map<String, ?> map) {
	
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter UserGroupDaoImpl findUniqueBy method.");
				logger.debug("hql:"+hql);
				logger.debug("map:"+map);
			}
			Query query  = getSession().createQuery(hql);
		      Set<String> keys = map.keySet();
		      for (String key : keys) {
		    	  if(map.get(key) instanceof Collection){
		    		  query.setParameterList(key, (Collection)map.get(key));
		    	  }else{
		    		  query.setParameter(key, map.get(key));
		    	  }
			  }
		    List<UserGroup> userGroups =  query.list();
			if(logger.isDebugEnabled()){
					logger.debug("exit UserGroupDaoImpl findUniqueBy method");
			}
			return userGroups;
		
	   } catch (Exception e) {
	 	throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	   }
			
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<UserGroup> findBySQL(String sql,Map<String, ?> map) {

		try{
			
		  if(logger.isDebugEnabled()){
			logger.debug("enter UserGroupDaoImpl findBySQL method.");
			logger.debug("sql:"+sql);
			logger.debug("map:"+map);
		  }
			
	      SQLQuery query = getSession().createSQLQuery(sql);
	      query.addEntity(UserGroup.class);
	      Set<String> keys = map.keySet();
	      for (String key : keys) {
	    	  if(map.get(key) instanceof Collection){
	    		  query.setParameterList(key, (Collection)map.get(key));
	    	  }else{
	    		  query.setParameter(key, map.get(key));
	    	  }
		  }
	      List<UserGroup> userGroups =   query.list();
		  if(logger.isDebugEnabled()){
				logger.debug("exit UserGroupDaoImpl findUniqueBy method");
		  }
		  return userGroups;
		  
	   } catch (Exception e) {
	 	throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	   }
	  
	}



}
