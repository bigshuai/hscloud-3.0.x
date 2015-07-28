package com.hisoft.hscloud.crm.usermanager.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.crm.usermanager.dao.UserRoleDao;
import com.hisoft.hscloud.crm.usermanager.entity.UserRole;

@Repository
public class UserRoleDaoImpl extends HibernateDao<UserRole, Long> implements UserRoleDao {

	@Override
	public void save(UserRole userRole){
		super.save(userRole);
	}
	
	@Override
	public List<UserRole> findBy(String propertyName, Object value){
		return (List<UserRole>)super.findBy(propertyName, value);
	}
	
	@Override
	public void delete(String hql, Object... value) {
		
		Query query = (Query)super.createQuery(hql, value);
		query.executeUpdate();
		
		
	}
	
	
}
