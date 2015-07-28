package com.hisoft.hscloud.crm.usermanager.dao.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.dao.PermissionDao;
import com.hisoft.hscloud.crm.usermanager.entity.Permission;

@Repository
public class PermissionDaoImp extends HibernateDao<Permission, Long> implements PermissionDao {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	public void save(Permission permission){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter PermissionDaoImp save method.");
				logger.debug("permission:"+permission);
			}
			super.save(permission);
			if(logger.isDebugEnabled()){
				logger.debug("exit PermissionDaoImp save method");
			}
			
	    } catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Permission> find(String hql, Object... values){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter PermissionDaoImp find method.");
				logger.debug("hql:"+hql);
				logger.debug("values:"+values);
			}
			List<Permission>  permissions = super.find(hql, values);
			if(logger.isDebugEnabled()){
				logger.debug("exit PermissionDaoImp find method");
			}
			return permissions;
			
	    } catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Permission findUnique(String hql, Object... values){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter PermissionDaoImp find method.");
				logger.debug("hql:"+hql);
				logger.debug("values:"+values);
			}
			Permission permission = super.findUnique(hql, values);
			if(logger.isDebugEnabled()){
				logger.debug("exit PermissionDaoImp find method");
			}
			return permission;
			
	    } catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Permission> findBySQL(String sql, Map<String, ?> map) {

		try{
			
		  if(logger.isDebugEnabled()){
			 logger.debug("enter PermissionDaoImp findBySQL method.");
			 logger.debug("sql:"+sql);
			 logger.debug("map:"+map);
		  }
	      SQLQuery query = getSession().createSQLQuery(sql);
	      query.addEntity(Permission.class);
	      Set<String> keys = map.keySet();
	      for (String key : keys) {
	    	  if(map.get(key) instanceof Collection){
	    		  query.setParameterList(key, (Collection)map.get(key));
	    	  }else{
	    		  query.setParameter(key, map.get(key));
	    	  }
		  }
	      List<Permission> permissions = query.list();
		  if(logger.isDebugEnabled()){
			 logger.debug("exit PermissionDaoImp findBySQL method");
		  }
		  return permissions;
		  
	    } catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
	      
	}

	@Override
	public void deleteByIds(List<Long> ids) {
		
		try{
			
		    if(logger.isDebugEnabled()){
				logger.debug("enter PermissionDaoImp deleteByIds method.");
			    logger.debug("ids:"+ids);
			}
			for (Long id : ids) {
				super.delete(id);
			}
			if(logger.isDebugEnabled()){
				logger.debug("exit PermissionDaoImp deleteByIds method");
			}
			
        } catch (Exception e) {
 	        throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
        }

	}
	

}
