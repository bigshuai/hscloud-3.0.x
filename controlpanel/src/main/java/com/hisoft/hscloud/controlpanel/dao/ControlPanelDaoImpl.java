/* 
* 文 件 名:  ControlPanelDaoImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  dinghb 
* 修改时间:  2013-1-4 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.controlpanel.dao; 

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.NonUniqueResultException;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.exception.NoUniqueException;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.controlpanel.entity.ControlPanelUser;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  dinghb 
 * @version  [版本号, 2013-1-4] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Repository
public class ControlPanelDaoImpl extends HibernateDao<ControlPanelUser, Long> implements ControlPanelDao {

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private HibernateTemplate hibernateTemplate;
	/** 
	* @param cuser 
	*/
	@Override
	public void saveControlUser(ControlPanelUser cuser) throws HsCloudException{
		hibernateTemplate.saveOrUpdate(cuser);
	}
	
	@Override
	public ControlPanelUser getCPByVmId(String vmId)throws HsCloudException{
		String hql = "from ControlPanelUser cp where cp.status=0 and cp.vmId='"+vmId+"'";
		List<ControlPanelUser> lcp = hibernateTemplate.find(hql);
		if(lcp!=null && lcp.size()>0){
			return lcp.get(0);
		}
		return null;
	}
	
	@Override
	public ControlPanelUser getRecycleCPByVmId(String vmId)throws HsCloudException{
		String hql = "from ControlPanelUser cp where cp.status=1 and cp.vmId='"+vmId+"'";
		List<ControlPanelUser> lcp = hibernateTemplate.find(hql);
		if(lcp!=null && lcp.size()>0){
			return lcp.get(0);
		}
		return null;
	}
	
	@Override
	public ControlPanelUser findUniqueBy(String propertyName,Object value){
		return super.findUniqueBy(propertyName, value);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ControlPanelUser> findBySQL(String sql,Map<String, ?> map) {

		try{
			
			  if(logger.isDebugEnabled()){
				logger.debug("enter ControlPanelDaoImpl findBySQL method.");
				logger.debug("sql:"+sql);
				logger.debug("map:"+map);
			  }
		      SQLQuery query = getSession().createSQLQuery(sql);
		      query.addEntity(ControlPanelUser.class);
		      Set<String> keys = map.keySet();
		      for (String key : keys) {
		    	  if(map.get(key) instanceof Collection){
		    		  query.setParameterList(key, (Collection<?>)map.get(key));
		    	  }else{
		    		  query.setParameter(key, map.get(key));
		    	  }
			  }
		      List<ControlPanelUser> controlPanelUsers =   query.list();
			  if(logger.isDebugEnabled()){
				logger.debug("enter ControlPanelDaoImpl findBySQL method.");
			  }
			  return controlPanelUsers;
			  
		} catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}
	
	@Override
	public ControlPanelUser findUniqueBySQL(String sql,Map<String, ?> map) {

		try{
			
			  if(logger.isDebugEnabled()){
				logger.debug("enter ControlPanelDaoImpl findUniqueBySQL method.");
				logger.debug("sql:"+sql);
				logger.debug("map:"+map);
			  }
		      SQLQuery query = getSession().createSQLQuery(sql);
		      query.addEntity(ControlPanelUser.class);
		      Set<String> keys = map.keySet();
		      for (String key : keys) {
		    	  if(map.get(key) instanceof Collection){
		    		  query.setParameterList(key, (Collection<?>)map.get(key));
		    	  }else{
		    		  query.setParameter(key, map.get(key));
		    	  }
			  }
		      ControlPanelUser controlPanelUser =   (ControlPanelUser)query.uniqueResult();
			  if(logger.isDebugEnabled()){
				  logger.debug("controlPanelUser:"+controlPanelUser);
				logger.debug("enter ControlPanelDaoImpl findUniqueBySQL method.");
			  }
			  return controlPanelUser;
			  
		}catch(NonUniqueResultException nure){
			throw new NoUniqueException();
		} catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}
	
}
