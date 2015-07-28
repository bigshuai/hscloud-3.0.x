package com.hisoft.hscloud.crm.usermanager.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.crm.usermanager.dao.UserGroupPermissionDao;
import com.hisoft.hscloud.crm.usermanager.entity.UserGroupPermission;

@Repository
public class UserGroupPermissionDaoImpl extends HibernateDao<UserGroupPermission, Long> implements UserGroupPermissionDao {

	@Override
	public void save(UserGroupPermission userGroupPermission){
		super.save(userGroupPermission);
	}
	
	@Override
	public UserGroupPermission findUniqueBy(String propertyName, Object value){
		return (UserGroupPermission)super.findUniqueBy(propertyName, value);
	}
	
	@Override
	public List<UserGroupPermission> findBy(String propertyName, Object value){
		return (List<UserGroupPermission>)super.findBy(propertyName, value);
	}

	@Override
	public void delete(String hql, Object... value) {
		
		Query query = (Query)super.createQuery(hql, value);
		query.executeUpdate();
		
	}
	
	@Override
	public void batchDelete(Map<String, Object> condition) {
		Query query = (Query)super.createQuery("delete UserGroupPermission where userGroupId = :userGroupId and permissionId in " +
				"(select id from Permission where resourceId in (:resource))", condition);
		query.executeUpdate();
	}
}
