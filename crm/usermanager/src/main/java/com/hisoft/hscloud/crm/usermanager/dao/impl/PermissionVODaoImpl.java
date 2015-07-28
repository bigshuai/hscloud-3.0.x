package com.hisoft.hscloud.crm.usermanager.dao.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.dao.PermissionVODao;
import com.hisoft.hscloud.crm.usermanager.vo.PermissionVO;

@Repository
public class PermissionVODaoImpl extends HibernateDao<PermissionVO, Long> implements  PermissionVODao {


	private Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PermissionVO> findBySQL(String sql,Map<String, ?> map) {

	 try{
		 
		  if(logger.isDebugEnabled()){
			  logger.debug("enter PermissionVODaoImpl findBySQL method.");
			  logger.debug("sql:"+sql);
			  logger.debug("map:"+map);
		  }
		  
	      SQLQuery query = getSession().createSQLQuery(sql);
	      query.addScalar("permissionId", Hibernate.LONG).addScalar("resourceId",Hibernate.LONG).addScalar("actionId", Hibernate.LONG).addScalar("primKey", Hibernate.STRING).addScalar("resourceType", Hibernate.STRING);
	      query.setResultTransformer(Transformers.aliasToBean(PermissionVO.class));
	      Set<String> keys = map.keySet();
	      for (String key : keys) {
	    	  if(map.get(key) instanceof Collection){
	    		  query.setParameterList(key, (Collection)map.get(key));
	    	  }else{
	    		  query.setParameter(key, map.get(key));
	    	  }
		  }
	      List<PermissionVO> permissionVOs = query.list();
		  if(logger.isDebugEnabled()){
			 logger.debug("exit PermissionVODaoImpl findBySQL method");
		  }
		  return permissionVOs;
		  
      } catch (Exception e) {
 	      throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
      }
		
	}



}
