package com.hisoft.hscloud.crm.usermanager.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.dao.ResourceDao;
import com.hisoft.hscloud.crm.usermanager.entity.Resource;

@Repository
public class ResourceDaoImpl extends HibernateDao<Resource, Long> implements
        ResourceDao {

	private Logger logger = Logger.getLogger(this.getClass());
	
    @Override
    public void save(Resource resource) {
    	
    	try{
    		
			if(logger.isDebugEnabled()){
				logger.debug("enter ResourceDaoImpl save method.");
			}
	        super.save(resource);
			if(logger.isDebugEnabled()){
				logger.debug("exit ResourceDaoImpl save method");
			}
			
		} catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
    	
    }

    @Override
    public List<Resource> findBy(String propertyName, Object value) {

    	try{
    		
			if(logger.isDebugEnabled()){
				logger.debug("enter ResourceDaoImpl findBy method.");
				logger.debug("propertyName:"+propertyName);
				logger.debug("value:"+value);
			}
			List<Resource> resources = super.findBy(propertyName, value);
			if(logger.isDebugEnabled()){
				logger.debug("resources:"+resources);
				logger.debug("exit ResourceDaoImpl findBy method");
			}
			return resources;
			
		} catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }

    }

    @Override
    @SuppressWarnings("unchecked")@Transactional
    public Resource findUnique(String hql, Object... values) {
    	
    	try{
    		
			if(logger.isDebugEnabled()){
				logger.debug("enter ResourceDaoImpl findUnique method.");
				logger.debug("hql:"+hql);
				logger.debug("values:"+values);
			}
			Resource resource = super.findUnique(hql, values);
			if(logger.isDebugEnabled()){
				logger.debug("resource:"+resource);
				logger.debug("exit ResourceDaoImpl findUnique method");
			}
			return resource;
			
		} catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
    }

    @Override
    public Resource findUniqueBy(String propertyName, Object value) {
    	
    	try{
    		
			if(logger.isDebugEnabled()){
				logger.debug("enter ResourceDaoImpl findUniqueBy method.");
				logger.debug("propertyName:"+propertyName);
				logger.debug("value:"+value);
			}
			Resource resource =  super.findUniqueBy(propertyName, value);
			if(logger.isDebugEnabled()){
				logger.debug("resource:"+resource);
				logger.debug("enter ResourceDaoImpl findUniqueBy method.");
			}
			return resource;
			
		} catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Resource> find(String hql, Object... values) {
    	
    	try{
    		
			if(logger.isDebugEnabled()){
				logger.debug("enter ResourceDaoImpl find method.");
				logger.debug("hql:"+hql);
				logger.debug("values:"+values);
			}
			List<Resource> resources =  super.find(hql, values);
			if(logger.isDebugEnabled()){
				logger.debug("resource:"+resources);
				logger.debug("enter ResourceDaoImpl find method.");
			}
			return resources;
			
		} catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
    }

    @Override
    public List<Resource> findByIds(List<Long> ids) {
    	
    	try{
    		
			if(logger.isDebugEnabled()){
				logger.debug("enter ResourceDaoImpl findByIds method.");
				logger.debug("ids:"+ids);
			}
			List<Resource> resources =  super.findByIds(ids);
			if(logger.isDebugEnabled()){
				logger.debug("resource:"+resources);
				logger.debug("enter ResourceDaoImpl findByIds method.");
			}
			return resources;
			
		} catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
    }
    
	@Override
	@SuppressWarnings("unchecked")
	public List<Resource> findBySQL(String sql,Map<String, ?> map) {

		try{
			
			  if(logger.isDebugEnabled()){
				logger.debug("enter ResourceDaoImpl findBySQL method.");
				logger.debug("sql:"+sql);
				logger.debug("map:"+map);
			  }
		      SQLQuery query = getSession().createSQLQuery(sql);
		      query.addEntity(Resource.class);
		      Set<String> keys = map.keySet();
		      for (String key : keys) {
		    	  if(map.get(key) instanceof Collection){
		    		  query.setParameterList(key, (Collection<?>)map.get(key));
		    	  }else{
		    		  query.setParameter(key, map.get(key));
		    	  }
			  }
		      List<Resource> resources =   query.list();
			  if(logger.isDebugEnabled()){
				logger.debug("enter ResourceDaoImpl findBySQL method.");
			  }
			  return resources;
			  
		} catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}

	@Override
	public List<Resource> findByHQL(String hql, Map<String, ?> map) {
		
		try{
			
			  if(logger.isDebugEnabled()){
					logger.debug("enter ResourceDaoImpl findByHQL method.");
					logger.debug("hql:"+hql);
					logger.debug("map:"+map);
			  }
		      Query query = getSession().createQuery(hql);
		      Set<String> keys = map.keySet();
		      for (String key : keys) {
		    	  if(map.get(key) instanceof Collection){
		    		  query.setParameterList(key, (Collection<?>)map.get(key));
		    	  }else{
		    		  query.setParameter(key, map.get(key));
		    	  }
			  }
		      List<Resource> resources = query.list();
			  if(logger.isDebugEnabled()){
				  logger.debug("enter ResourceDaoImpl findByHQL method.");
		      }
			  return resources;
		  
		 } catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	     }
		  
	      
	}
	
	public List<Object> findPrimKeyByHQL(String hql,Map<String,?> map){
		try{
			  if(logger.isDebugEnabled()){
					logger.debug("enter ResourceDaoImpl findByHQL method.");
					logger.debug("hql:"+hql);
					logger.debug("map:"+map);
			  }
		      Query query = getSession().createQuery(hql);
		      Set<String> keys = map.keySet();
		      for (String key : keys) {
		    	  if(map.get(key) instanceof Collection){
		    		  query.setParameterList(key, (Collection<?>)map.get(key));
		    	  }else{
		    		  query.setParameter(key, map.get(key));
		    	  }
			  }
		      List<String> primkeys = query.list();
		      List<Object> primkeys_ = new ArrayList<Object>();
		      for(String key : primkeys){
		    	  primkeys_.add(Long.valueOf(key));
		      }
			  if(logger.isDebugEnabled()){
				  logger.debug("enter ResourceDaoImpl findByHQL method.");
		      }
			  return primkeys_;
		  
		 } catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	     }
	}

    /**
     * @param roleId
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Resource> getResourceForRoleId(long roleId) {
        String sql = "select a.* from hc_resource a, hc_permission b, hc_role_permission c where a.id = b.resource_id and b.id = c.permission_id and c.role_id = ? ";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter(0, roleId);
        query.addEntity(Resource.class);
        return query.list();
    }



}
