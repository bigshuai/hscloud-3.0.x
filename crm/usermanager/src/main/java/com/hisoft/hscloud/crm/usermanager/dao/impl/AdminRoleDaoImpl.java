package com.hisoft.hscloud.crm.usermanager.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.crm.usermanager.dao.AdminRoleDao;
import com.hisoft.hscloud.crm.usermanager.entity.AdminRole;

@Repository
public class AdminRoleDaoImpl extends HibernateDao<AdminRole, Long> implements AdminRoleDao {

	@Override
	public void delete(String hql, Object... value) {
		
		Query query = (Query)super.createQuery(hql, value);
		query.executeUpdate();
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<AdminRole> find(String hql, Object... value){
		return super.find(hql, value);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public AdminRole findUnique(String hql, Object... values){
		return (AdminRole)super.findUnique(hql, values);
	}

	@Override
	public AdminRole findAdminRoleById(long adminRoleId) {
		return super.get(adminRoleId);
	}
	
	@Override
	public List<AdminRole> findBy(String propertyName, Object value){
		return super.findBy(propertyName, value);
	}
	
	@Override
	public void delete(Long adminRoleId){
		super.delete(adminRoleId);
	}
	

}
