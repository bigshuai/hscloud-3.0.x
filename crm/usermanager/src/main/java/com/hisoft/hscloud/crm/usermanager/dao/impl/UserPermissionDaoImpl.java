package com.hisoft.hscloud.crm.usermanager.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.crm.usermanager.dao.UserPermissionDao;
import com.hisoft.hscloud.crm.usermanager.entity.UserPermission;

@Repository
public class UserPermissionDaoImpl extends HibernateDao<UserPermission, Long> implements UserPermissionDao {

	@Override
	public void save(UserPermission userPermission){
		super.save(userPermission);
	}
	
	@Override
	public UserPermission findUniqueBy(String propertyName, Object value){
		return (UserPermission)super.findUniqueBy(propertyName, value);
	}
	
	@Override
	public List<UserPermission> findBy(String propertyName, Object value){
		return (List<UserPermission>)super.findBy(propertyName, value);
	}
	
	@Override
	public void delete(String hql, Object... value) {
		
		Query query = (Query)super.createQuery(hql, value);
		query.executeUpdate();
		
		
	}
	
	
}
