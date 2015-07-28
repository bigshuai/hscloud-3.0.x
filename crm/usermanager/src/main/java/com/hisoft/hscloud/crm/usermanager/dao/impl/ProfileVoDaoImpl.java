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

import com.hisoft.hscloud.crm.usermanager.dao.ProfileVoDao;
import com.hisoft.hscloud.crm.usermanager.vo.ProfileVo;

@Repository
public class ProfileVoDaoImpl extends HibernateDao<ProfileVo, Long> implements ProfileVoDao{

	@Override
	public List<ProfileVo> findProfileVo(String sql, Map<String, ?> params) {
		SQLQuery query = getSession().createSQLQuery(sql);
	    query.addScalar("id", Hibernate.LONG).addScalar("idCard", Hibernate.STRING)
	    .addScalar("telephone", Hibernate.STRING).addScalar("company", Hibernate.STRING)
	    .addScalar("address", Hibernate.STRING).addScalar("industryId", Hibernate.LONG)
	    .addScalar("countryId",  Hibernate.LONG).addScalar("regionId", Hibernate.LONG)
	    .addScalar("countryCode", Hibernate.STRING).addScalar("regionCode", Hibernate.STRING)
	    .addScalar("industryCode", Hibernate.STRING);
	    query.setResultTransformer(Transformers.aliasToBean(ProfileVo.class));
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
