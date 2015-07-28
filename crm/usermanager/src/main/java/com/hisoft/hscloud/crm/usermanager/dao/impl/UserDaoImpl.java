package com.hisoft.hscloud.crm.usermanager.dao.impl;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.constant.UserState;
import com.hisoft.hscloud.crm.usermanager.constant.UserType;
import com.hisoft.hscloud.crm.usermanager.dao.UserDao;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.util.Constant;
import com.hisoft.hscloud.crm.usermanager.vo.CountOfUserVO;
import com.hisoft.hscloud.crm.usermanager.vo.UserVO;

@Repository
public class UserDaoImpl extends HibernateDao<User, Long> implements UserDao {

	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	@Override
	public User get(long id) {
		
		//return super.get(id);
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter UserDaoImpl get method.");
				logger.debug("id:"+id);
			}
			User user =  super.findUniqueBy("id", id);
			if(logger.isDebugEnabled()){
				logger.debug("exit UserDaoImpl get method");
			}
			return user;
			
		} catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}

	@Override
	public User load(long id) {
		return null;
	}

	@Override
	public void save(User user) {
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter UserDaoImpl save method.");
				logger.debug("user:"+user);
			}
			super.save(user);
			if(logger.isDebugEnabled()){
				logger.debug("exit UserDaoImpl save method");
			}
			
		} catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}

	@Override
	public void delete(long id) {
		
		if(logger.isDebugEnabled()){
			logger.debug("enter UserDaoImpl delete method.");
			logger.debug("id:"+id);
		}
		super.delete(id);
		if(logger.isDebugEnabled()){
			logger.debug("exit UserDaoImpl delete method");
		}
		
	}
	
	@Override
	public void delete(User user){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter UserDaoImpl delete method.");
				logger.debug("user:"+user);
			}
			super.delete(user);
			if(logger.isDebugEnabled()){
				logger.debug("exit UserDaoImpl delete method");
			}
			
		} catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public User findUnique(String hql, Object... values){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter UserDaoImpl findUnique method.");
				logger.debug("hql:"+hql);
				logger.debug("values:"+values);
			}
			User user =  (User)super.findUnique(hql, values);
			if(logger.isDebugEnabled()){
				logger.debug("exit UserDaoImpl findUnique method");
			}
			return user;
			
		} catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<User> find(String hql, Object... values){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter UserDaoImpl find method.");
				logger.debug("hql:"+hql);
				logger.debug("values:"+values);
			}
			List<User>  users = super.find(hql, values);
			if(logger.isDebugEnabled()){
				logger.debug("exit UserDaoImpl find method");
			}
			return users;
			
		} catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}
	
	@Override
	public List<User> findUser(String hql, Map<String,?> map){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter UserDaoImpl findUser method.");
				logger.debug("hql:"+hql);
				logger.debug("map:"+map);
			}
			List<User>  users = super.find(hql, map);
			if(logger.isDebugEnabled()){
				logger.debug("exit UserDaoImpl findUser method");
			}
			return users;
			
		} catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
		
	}
	
	@Override
	public List<User> findBy(String propertyName, Object value){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter UserDaoImpl findBy method.");
				logger.debug("propertyName:"+propertyName);
				logger.debug("value:"+value);
			}
			List<User> users = super.findBy(propertyName, value);
			if(logger.isDebugEnabled()){
				logger.debug("exit UserDaoImpl findBy method");
			}
			return users;
			
		} catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}
	
	@Override
	public User findUniqueBy(String propertyName,Object value){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter UserDaoImpl findUniqueBy method.");
				logger.debug("propertyName:"+propertyName);
				logger.debug("value:"+value);
			}
			User user =  (User)super.findUniqueBy(propertyName, value);
			if(logger.isDebugEnabled()){
				logger.debug("exit UserDaoImpl findUniqueBy method");
			}
			return user;
			
		} catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public User LoginByEmail(String email, String password){
		Session session = null;
		User user = null;
		try{
			if(logger.isDebugEnabled()){
				logger.debug("enter UserDaoImpl LoginByEmail method.");
				logger.debug("email:"+email);
				logger.debug("password:"+password);
			}
			String sql = "select * from hc_user where binary email='" + email
					+ "' and password='" + password + "'";
			session = hibernateTemplate.getSessionFactory().openSession();
			SQLQuery sqlQuery = session.createSQLQuery(sql).addEntity(User.class);
			List<User> user_list = sqlQuery.list();
			if(user_list.size()>0){
				user = user_list.get(0);
			}		
//			String hql = "from User where email=? and password=?";
//			User user =  findUnique(hql, email,password);
			if(logger.isDebugEnabled()){
				logger.debug("exit UserDaoImpl LoginByEmail method");
			}
			return user;
			
		} catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}
	
	@Override
	public List<User> getAll(){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter UserDaoImpl getAll method.");
			}
			List<User> users =  (List<User>)super.getAll();
			if(logger.isDebugEnabled()){
				logger.debug("exit UserDaoImpl getAll method");
			}
			return users;
			
		} catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}

	@Override
	public Page<User> findPage(Page<User> page,String hql,Object... values) {
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter UserDaoImpl findPage method.");
				logger.debug("hql:"+hql);
				logger.debug("page:"+page);
				logger.debug("values:"+values);
			}
			Page<User> users =super.findPage(page, hql, values);
			if(logger.isDebugEnabled()){
				logger.debug("exit UserDaoImpl findPage method");
			}
			return users;
			
		} catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}
	
	@Override
	public Page<User> findPage(Page<User> page, final String hql, final Map<String, ?> values){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter UserDaoImpl findPage method.");
				logger.debug("hql:"+hql);
				logger.debug("page:"+page);
				logger.debug("values:"+values);
			}
			Page<User> users = super.findPage(page, hql, values);
			if(logger.isDebugEnabled()){
				logger.debug("exit UserDaoImpl findPage method");
			}
			return users;
			
		} catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}
	
	@Override
	public List<User> findByIds(List<Long> ids){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter UserDaoImpl findByIds method.");
				logger.debug("ids:"+ids);
			}
			List<User> users = super.findByIds(ids);
			if(logger.isDebugEnabled()){
				logger.debug("exit UserDaoImpl findByIds method");
			}
			return users;
			
		} catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> findBySQL(String sql, Map<String, ?> map) {
		try{
			
			  if(logger.isDebugEnabled()){
				logger.debug("enter UserDaoImpl findBySQL method.");
				logger.debug("sql:"+sql);
				logger.debug("map:"+map);
			  }
		      SQLQuery query = getSession().createSQLQuery(sql);
		      query.addEntity(User.class);
		      Set<String> keys = map.keySet();
		      for (String key : keys) {
		    	  if(map.get(key) instanceof Collection){
		    		  query.setParameterList(key, (Collection<?>)map.get(key));
		    	  }else{
		    		  query.setParameter(key, map.get(key));
		    	  }
			  }
		      List<User> users =   query.list();
			  if(logger.isDebugEnabled()){
				logger.debug("enter UserDaoImpl findBySQL method.");
			  }
			  return users;
			  
		} catch (Exception e) {
			throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
	}

	@SuppressWarnings("unchecked")
	@Override
	public CountOfUserVO getCountOfUser() {
		CountOfUserVO countOfUserVO = new CountOfUserVO(0,0,0,0);
		long entUser = 0;
		long norUser = 0;
		try {	
			Session session = getSession();
			Query query;
			StringBuffer hql= new StringBuffer("SELECT count(u.id),u.userType,u.onlineStatus FROM User as u WHERE 2>1");
			hql.append(" AND u.enable = ").append(UserState.APPROVED.getIndex());
			hql.append(" group by u.userType,u.onlineStatus");
			//String hql = " AND u.enable ="+UserState.APPROVED.getIndex()+" group by u.userType,u.onlineStatus";
			query=session.createQuery(hql.toString());
			List<Object[]> list = query.list();
			Iterator<Object[]> it = list.iterator();
			while (it.hasNext()) {
				Object[] key = it.next();
				if (key[1].equals(UserType.ENTERPRISE_USER.getType())) {
					entUser += Long.valueOf(key[0].toString());
				}
				if (key[1].equals(UserType.PERSONAL_USER.getType())) {
					norUser += Long.valueOf(key[0].toString());
				}
				if (key[1].equals(UserType.ENTERPRISE_USER.getType()) && key[2].equals(1)) {
					countOfUserVO.setCountOfOnlineEntUser(Long.valueOf(key[0].toString()));
				}
				if (key[1].equals(UserType.PERSONAL_USER.getType()) && key[2].equals(1)) {
					countOfUserVO.setCountOfOnlineNorUser(Long.valueOf(key[0].toString()));
				}
			}
			countOfUserVO.setCountOfEntUser(entUser);
			countOfUserVO.setCountOfNorUser(norUser);
		} catch (Exception e) {
			logger.error("getCountOfUser Exception:", e);
		} 
		return countOfUserVO;
	}

	public void saves(String sql,Map<String,Object> map){
		 SQLQuery query = getSession().createSQLQuery(sql);
	      Set<String> keys = map.keySet();
	      for (String key : keys) {
	    	  if(map.get(key) instanceof Collection){
	    		  query.setParameterList(key, (Collection<?>)map.get(key));
	    	  }else{
	    		  query.setParameter(key, map.get(key));
	    	  }
		  }
		 query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getUserIdsByDomainIds(List<Long> domainIds) {
		if(domainIds.isEmpty()){
			return new ArrayList<Long>();
		}
		StringBuffer sql=new StringBuffer("SELECT u.id AS Id FROM  hc_user AS u WHERE u.domain_id in (:domainIds)");
		SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
		sqlQuery.addScalar("Id",Hibernate.LONG);
		sqlQuery.setParameterList("domainIds", domainIds);
		List<Long> userIds = sqlQuery.list();
		return userIds;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserVO> getAllAvailableUser(String email) {		
		StringBuffer sql = new StringBuffer();
		sql.append("select u.id as id,u.name as name,u.email as email,u.create_date as createDate,u.is_enable as enable from hc_user u where u.is_enable=:enable");
		if(email != null){
			sql.append(" and u.email =:email");
		}
		SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
		sqlQuery.setParameter("enable", Constant.USER_STATE_APPROVED);
		if(email != null){
			sqlQuery.setParameter("email",email);
		}
		sqlQuery.addScalar("id",Hibernate.LONG);
		sqlQuery.addScalar("name",Hibernate.STRING);
		sqlQuery.addScalar("email",Hibernate.STRING);
		sqlQuery.addScalar("createDate",Hibernate.DATE);
		sqlQuery.addScalar("enable",Hibernate.SHORT);
		sqlQuery.setResultTransformer(Transformers.aliasToBean(UserVO.class));
		return sqlQuery.list();
	}

	@Override
	public List<String> getAllUserMobile(String sql) {
		List<String> userMobile=new ArrayList<String>();
		try {
			SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
			userMobile = sqlQuery.list();
		} catch (Exception e) {
			throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
		}
		return userMobile;
	}


}
