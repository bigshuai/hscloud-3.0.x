package com.hisoft.hscloud.crm.usermanager.dao.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.crm.usermanager.dao.LoginLogDao;
import com.hisoft.hscloud.crm.usermanager.entity.LoginLog;

@Repository
public class LoginLogDaoImpl extends HibernateDao<LoginLog, Long> implements LoginLogDao{

	@Override
	public void save(LoginLog loginLog){
		
		super.save(loginLog);
		
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public LoginLog findBySQL(String sql,Map<String, ?> map) {

      SQLQuery query = getSession().createSQLQuery(sql);
      query.addEntity(LoginLog.class);
      Set<String> keys = map.keySet();
      for (String key : keys) {
    	  if(map.get(key) instanceof Collection){
    		  query.setParameterList(key, (Collection)map.get(key));
    	  }else{
    		  query.setParameter(key, map.get(key));
    	  }
	  }
      return (LoginLog)query.uniqueResult();
		
	}


	@Override
	public LoginLog findLastLoginLog(Map<String, ?> map) {
		
		String sql = "select *  from hc_login_log hll where hll.userId=(:id) and hll.user_type = :userType ORDER BY hll.loginDate desc LIMIT 1";
		return this.findBySQL(sql, map);
		
	}
	
}
