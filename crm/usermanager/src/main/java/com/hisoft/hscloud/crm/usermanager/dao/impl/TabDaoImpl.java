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

import com.hisoft.hscloud.crm.usermanager.dao.TabDao;

@Repository
public class TabDaoImpl extends HibernateDao implements TabDao {
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findBySQL(String sql,Map<String, ?> map) {

      SQLQuery query = getSession().createSQLQuery(sql);
      query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
      query.addScalar("id",Hibernate.LONG);
      Set<String> keys = map.keySet();
      for (String key : keys) {
    	  if(map.get(key) instanceof Collection){
    		  query.setParameterList(key, (Collection)map.get(key));
    	  }else{
    		  query.setParameter(key, map.get(key));
    	  }
	  }
      return query.list();
		
	}

}
