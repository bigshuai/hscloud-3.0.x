package com.hisoft.hscloud.crm.usermanager.dao.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.crm.usermanager.dao.UserVO1Dao;
import com.hisoft.hscloud.crm.usermanager.vo.UserVO1;

@Repository
public class UserVO1DaoImpl extends HibernateDao<UserVO1, Long> implements UserVO1Dao {

	@Override
	@SuppressWarnings("unchecked")
	public List<UserVO1> findUserVO1(String sql, Map<String, ?> params) {
		    SQLQuery query = getSession().createSQLQuery(sql);
		    query.addScalar("id", Hibernate.LONG).addScalar("createDate",Hibernate.TIMESTAMP)
		    .addScalar("email", Hibernate.STRING).addScalar("name", Hibernate.STRING)
		    .addScalar("userType", Hibernate.STRING)
		    .addScalar("domainId", Hibernate.LONG).addScalar("enable", Hibernate.SHORT)
		    .addScalar("lastLoginDate", Hibernate.TIMESTAMP).addScalar("level", Hibernate.STRING)
		    .addScalar("profileId", Hibernate.LONG);
		    query.setResultTransformer(Transformers.aliasToBean(UserVO1.class));
			Set<String> keys = params.keySet();
		    for (String key : keys) {
		    	 Object o = params.get(key);
		    	 if( o instanceof Collection){
		    		 query.setParameterList(key, (Collection)params.get(key));
		    	 }else{
		    		 query.setParameter(key, params.get(key));
		    	 }
			 }
			return query.list();
	}



}
